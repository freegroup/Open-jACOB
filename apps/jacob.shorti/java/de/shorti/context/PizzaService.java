package de.shorti.context;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */

import de.shorti.context.basis.*;
import de.shorti.baseknowledge.*;
import de.shorti.util.search.fuzzy.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.util.basic.*;
import de.shorti.message.Message;
import de.shorti.util.basic.*;
import org.apache.oro.text.regex.*;
import java.util.*;
import java.util.Locale;
import java.util.Comparator;
import de.shorti.context.basis.*;

public class PizzaService  extends GenericContext
{
    public class ResponseItem
    {
        public String distance;
        public String city;
        public String dial;
        public String name;
        public String plz;
        public String street;
    }

    private static final ArrayList ALL_ZIPCODES = ZipCode.getAll(); // static data wich never changes during runtime

    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public PizzaService()
    {
        super("./context/PizzaService.xml");
    }

    /**
     * The user hands over a city name (e.g. Mannheim) and we must find
     * a corresponding Pizza-Service
     *
     */
	public int onStart2City(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        Country country = null;
        String cityName = null;

        country = m.getShortiUser().getHomeCountry();
        if(country != null)
        {
            ArrayList result = CityNameCatalog.find(matchedGroups[0]);
            Iterator iter = result.iterator();
            if(iter.hasNext())
            {
                Keyword cityKey = (Keyword)iter.next();
                cityName = cityKey.m_keyword;
            }

            if(cityName != null)
            {
                ArrayList requestCities = City.findByNameAndCountry(cityName, country);
                if(requestCities.size() > 0)
                {
                    City requestCity = (City) requestCities.get(0);
                    ArrayList requestZipCodes = ZipCode.findByCity(requestCity);
                    if(requestZipCodes.size() > 0)
                    {
                        trace.debug1(m, "City for the request found ["+requestCity.getName()+"]");
                        responseByZipCode((ZipCode)requestZipCodes.get(0),userDataMap, matchedGroups, m);
                    }
                    else
                    {
                        m.setResponse("Ich kenne die Stadt ´"+cityName+"´ leider nicht.");
                        trace.fatal(m, "city not in the database found but CityNameCatalog.find(...) returnd them as possible entry");
                    }
                }
            }
        }
		return RENDER_OUTPUT;
	}

    /**
     * The user hands over a zip code and we must find a corresponding
     * Pizza-Service
     *
     */
	public int onStart2Plz(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        Country country = m.getShortiUser().getHomeCountry();;

        if(country != null)
        {
            trace.debug2(m,"calling 'ZipCode.findByCodeAndCountry(matchedGroups[0], country)'");
            ZipCode zipCode = ZipCode.findByCodeAndCountry(matchedGroups[0], country);
            if(zipCode!=null)
            {
                responseByZipCode(zipCode,userDataMap, matchedGroups, m);
                return RENDER_OUTPUT;
            }
            else
            {
                m.setResponse("Ich kenne die Postleitzahl ´"+matchedGroups[0]+"´ leider nicht.");
                return REPLY;
            }
        }
		return REPLY;
	}

    /**
     * The user hands over NO Location.....try to find in his history
     * a PizzaRequest with a location definition....use them
     * or report an error/warning
     *
     */
	public int onStart2NoLocation(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        if(m.getHasPosition())
        {
            // try to find the zipcode for the message location
            //
            ArrayList result = ZipCode.findZipCodesInRange(m.getLatitude() ,m.getLongitude() ,5000);
            if(result.size()>0)
            {
                responseByZipCode((ZipCode)result.get(0),userDataMap,matchedGroups, m);
                return RENDER_OUTPUT;
            }

            m.setResponse("Konnte leider keine Stadt in Ihrer Nähe bestimmen.");
            return REPLY;
        }
        m.setNeedPosition();
		return REPLY;
	}


    /**
     * detect the locale information for the hands over question and
     * try to find a matching question-pattern. If one pattern matches,
     * try to find an answer for the question and return a result string
     * in the required answerType (e.g. TEXT_PLAIN/ TEXT_HTML/ ..... )
     *
     * If the question doesn't concern the context return null
     */
    private void responseByZipCode(ZipCode zipCode,HashMap userDataMap, String[] matchedGroups, Message m)
    {
        trace.debug1(m, "try to find pizzaservice for the zipcode ["+zipCode.getCode()+"]");

        Service pizzaService = Service.findByName("PizzaService");
        if(pizzaService == null)
        {
            trace.debug1(m,"can't find Service 'PizzaService' in database");
            m.setResponse("Konnte leider keinen Pizzadienst finden :-(");
            m.setStatusWarning();
            return;
        }

//        trace.debug3(m,"calling ZipCode.getAll()");
//        ArrayList allZipCodes  = ZipCode.getAll();
        trace.debug3(m,"calling ZipCode.findZipCodesInRange(....,5000.0)");
        ArrayList nearZipCodes = zipCode.findZipCodesInRange(ALL_ZIPCODES, 5000.0);

        trace.debug3(m,"calling Geschaeftsstelle.findByServiceAndZipCodes(....) with "+nearZipCodes.size()+" zip codes");
        ArrayList result = Geschaeftsstelle.findByServiceAndZipCodes(pizzaService, nearZipCodes);
        trace.debug2(m,"findByServiceAndZipCodes(....) returns "+result.size()+" possible entries in the database");
        for(int i= 0;i<result.size();i++)
        {
            Geschaeftsstelle gs = (Geschaeftsstelle)result.get(i);
            ResponseItem item = new ResponseItem();
            Company company = gs.getCompany();
            Telekom telekom = gs.getTelekom();
            Address address = gs.getAddress();
            ZipCode zip     = address.getZipCode();
            City    city    = zip.getCity();

            item.distance = ""+fitKM(zip.getDistance(zipCode));
            item.city     = city.getName();
            item.dial     = telekom.getDialPrefix()+ telekom.getNumber();
            item.name     = company.getName();
			item.plz      = zip.getCode();
            item.street = address.getStreet()+" "+address.getNumber();

            addResponseItem(m,item);
        }
        // sort the result ArrayList by the distance
        // ...nearest pizzaservie at first
        //
        sortResultArrayList(m, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                if((o1 instanceof ResponseItem) && (o2 instanceof ResponseItem))
                {
                    return ((ResponseItem)o1).distance.compareTo(((ResponseItem)o2).distance);
                }
                return 0;
            }
            public boolean equals(Object obj)
            {
                return false;
            }
        });
    }



    /**
     * only to fit a number from '123123.2324324'  to '123123.2'
     * ...userful for some outputs.
     * and values less than 1000. are 1000
     */
    private static double fitKM(double value)
    {
        if(value < 1)
            value = 1000.0;
        int distInt = (int) (value / 100.0);
        value = (double) distInt / 10.0;

        if((((int)(value * 10.0)) % 10) == 0)
        {
            return (double)(int)value;
        }

        return value;
    }
}
