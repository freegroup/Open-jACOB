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
import de.shorti.baseknowledge.objects.*;
import de.shorti.message.Message;
import de.shorti.util.basic.*;
import java.util.*;

public class Location  extends GenericContext
{
    public class ResponseItem
    {
        public String continent;
        public String country;
        public String city;
        public String distance;
    }

    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public Location()
    {
        super("./context/Location.xml");
    }

	public int onStart2Location(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        if(m.getHasPosition())
            return findCities(userDataMap, matchedGroups,m);
        m.setNeedPosition();
        return REPLY;
	}

    private int findCities(HashMap userDataMap, String[] matchedGroups, Message m)
    {
        String oldCityName="";
        ArrayList result = ZipCode.findZipCodesInRange(m.getLatitude(), m.getLongitude(),5000);
        Iterator  iter = result.iterator();
        while(iter.hasNext())
        {
            ZipCode zip     = (ZipCode)iter.next();
            City    city    = zip.getCity();
            Country country = city.getState().getCountry();

            if(!city.getName().equals(oldCityName))
            {
                oldCityName = city.getName();
                ResponseItem item = new ResponseItem();
                try
                {
                    item.continent = country.getContinent().getName();
                    item.country   = country.getName();
                    item.city      = city.getName();
                    item.distance  = ""+fitKM(zip.getDistance(m.getLatitude(),m.getLongitude()));
                    addResponseItem(m, item);
                }
                catch (Exception exc)
                {
                    trace.error(m,exc);
                }
            }
        }

        if(result.size()>0)
            return RENDER_OUTPUT;

        m.setResponse("Konnte leider keine Stadt in ihrer Nähe finden");
        return REPLY;
    }


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