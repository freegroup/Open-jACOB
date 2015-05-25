package de.shorti.message.filter;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author Andreas Herz
 * @version 1.0
 */
import java.util.*;
import de.shorti.util.basic.*;
import org.apache.oro.text.regex.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.baseknowledge.*;
import de.shorti.util.search.fuzzy.Keyword;
import de.shorti.message.*;

public class LocationFilter implements IMessageFilter
{
    static TraceDispatcher  trace      = TraceFactory.getTraceDispatcher();

    static HashMap messageChache       = new HashMap();
    static Perl5Compiler compiler      = null;
    static Perl5Matcher  matcher       = null;
    static ArrayList     mptpPatterns  = null;
    static ArrayList     zipPatterns   = null;
    static ArrayList     cityPatterns  = null;


    /**
     *
     */
    public boolean onBeginProcess(Message m)
    {
        String connectionString = m.getChannel().getConnectionString();
        // check if we have requested the position before
        //
        if(messageChache.containsKey(connectionString))
        {
            trace.debug1(m,"response from location request found. Try to detect location information from message.");
            String oldQuestion = (String)messageChache.get(connectionString);

            // remove the message from the cache
            //
            messageChache.remove(connectionString);

            // try to find the location
            //
            if(m.getHasPosition()==false)
            {
                if (checkMPTP(m) || checkZipCode(m) || checkCity(m))
                {
                    trace.debug1(m,"location found...replace question with original question");
                    m.setQuestion(oldQuestion);
                }
                else
                {
                    trace.warning(m,"unable to extract location from response");
                }
            }
        }
        return true;
    }


    public boolean onEndProcess(Message m)
    {
        // no position request neccessary
        //
        if(!m.getNeedPosition())
            return true;

        // prepare the message for the position request
        //
        if(m.getChannel().getCanMPTP())
        {
            trace.debug1(m,"prepare message for location request via MPTP.");
            // the channel understands the "Mobile Position Transport Protocol"
            m.setResponse("?LOC"); // request the location from the device
            messageChache.put(m.getChannel().getConnectionString(),m.getQuestion());
        }
        else
        {
            trace.debug1(m,"prepare message for location request.");
            m.setResponse("Benötige Stadt oder Postleitzahl, damit Ich Ihnen weiterhelfen kann.");
            messageChache.put(m.getChannel().getConnectionString(),m.getQuestion());
        }

        return true;
    }


    /**
     * try to retrieve the MPTP location from the message
     * and update the message with the location information
     */
    private static boolean checkMPTP(Message m)
    {
        trace.debug1(m,"try to find MPTP location information in message");
        Iterator iter =mptpPatterns.iterator();
        while(iter.hasNext())
        {
            Pattern pattern = (Pattern)iter.next();
            if(matcher.matches(m.getQuestion(),pattern))
            {
                trace.debug1(m,"pattern ["+pattern.getPattern()+"] matched. Extract longitude and latitude infromation from message.");
                MatchResult match = matcher.getMatch();
                double bGrad     = Double.parseDouble(match.group(1));
                double bMinute   = Double.parseDouble( match.group(2));
                double bSekunde  = Double.parseDouble( match.group(3)+"."+match.group(4));
                double lGrad     = Double.parseDouble( match.group(5));
                double lMinute   = Double.parseDouble( match.group(6));
                double lSekunde  = Double.parseDouble( match.group(7)+"."+ match.group(8));
                m.setLatitude(bGrad+(bMinute/60)+(bSekunde/3600));
                m.setLongitude(lGrad+(lMinute/60)+(lSekunde/3600));
                m.setHasPosition(true);
                return true;
            }
        }
        return false;
    }


    /**
     * try to retrieve the ZipCode location from the message
     * and update the message with the location information
     */
    private static boolean checkZipCode(Message m)
    {
        trace.debug1(m,"try to find ZipCode location information in message");
        Iterator iter =zipPatterns.iterator();
        while(iter.hasNext())
        {
            Pattern pattern = (Pattern)iter.next();
            if(matcher.matches(m.getQuestion(),pattern))
            {
                trace.debug1(m,"pattern ["+pattern.getPattern()+"] matched. Extract ZipCode information from message.");
                MatchResult match = matcher.getMatch();
                ZipCode zip = ZipCode.findByCodeAndCountry(match.group(1),Country.findByShortName("DE"));
                if(zip!=null)
                {
                    m.setLatitude(zip.getLatitude());
                    m.setLongitude(zip.getLongitude());
                    m.setHasPosition(true);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * try to retrieve the City location from the message
     * and update the message with the location information
     */
    private static boolean checkCity(Message m)
    {
        trace.info(m,"try to find City information in message");
        Iterator iter =cityPatterns.iterator();
        while(iter.hasNext())
        {
            Pattern pattern = (Pattern)iter.next();
            if(matcher.matches(m.getQuestion(),pattern))
            {
                trace.info(m,"pattern ["+pattern.getPattern()+"] matched. Extract City from message.");
                MatchResult match = matcher.getMatch();
                ArrayList result= CityNameCatalog.find(match.group(1));
                if(result.size()>0)
                {
                    Keyword cityKey = (Keyword)result.get(0);
                    Country country= m.getShortiUser().getHomeCountry();
                    ArrayList cities = City.findByNameAndCountry(cityKey.m_keyword ,country);
                    if(cities.size()>0)
                    {
                        ZipCode zip = ((City)(cities.get(0))).getBestCenterZipCode();
                        m.setLatitude(zip.getLatitude());
                        m.setLongitude(zip.getLongitude());
                        m.setHasPosition(true);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static
    {
        // create the patterns for the message scanner
        //
        try
        {
            mptpPatterns = new ArrayList();
            zipPatterns  = new ArrayList();
            cityPatterns = new ArrayList();

            compiler = new Perl5Compiler();
            matcher  = new Perl5Matcher();
            mptpPatterns.add(compiler.compile("!loc.{23,23}n(\\d+).(\\d+).(\\d+),(\\d+)_e(\\d+).(\\d+).(\\d+),(\\d+)([^\\z]*)"));

            zipPatterns.add(compiler.compile("([0-9]+)"));

            cityPatterns.add(compiler.compile("bin\\s+in\\s+([-a-zäöüß.\\s]+)"));
            cityPatterns.add(compiler.compile("([-a-zäöüß.\\s]+)"));
        }
        catch(Exception exc)
        {
            trace.fatal(exc);
        }
    }
}