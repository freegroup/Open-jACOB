package de.shorti.context;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import java.util.HashMap;

import de.shorti.baseknowledge.objects.Country;
import de.shorti.baseknowledge.objects.ZipCode;
import de.shorti.context.basis.GenericContext;
import de.shorti.message.Message;
import de.shorti.util.basic.TraceDispatcher;
import de.shorti.util.basic.TraceFactory;

public class CityDistance extends GenericContext
{
    public class ResponseItem
    {
        public String fromCountry;
        public String fromName;
        public String fromPlz;
        public String toCountry;
        public String toName;
        public String toPlz;
        public String distanz;
    }

	static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public CityDistance()
    {
        super("./context/CityDistance.xml");
    }

    /**
     *
     */
	public int onStart2ZipZip(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        Country country = m.getShortiUser().getHomeCountry();

        ZipCode zip1 = ZipCode.findByCodeAndCountry(matchedGroups[0],country);
        ZipCode zip2 = ZipCode.findByCodeAndCountry(matchedGroups[1],country);

        if((zip1!=null) && (zip2!=null))
        {
            double dist = fitKM(zip1.getDistance(zip2));
            ResponseItem item = new ResponseItem();
            item.fromCountry = zip1.getCity().getState().getName();
            item.fromName    = zip1.getCity().getName();
            item.fromPlz     = zip1.getCode();
            item.toCountry   = zip2.getCity().getState().getName();
            item.toName      = zip2.getCity().getName();
            item.toPlz       = zip2.getCode();
            item.distanz     = ""+dist;
            addResponseItem(m,item);
            return RENDER_OUTPUT;
        }
        else
        {
            if(zip1== null && zip2!=null)
                m.setResponse("Kenne leider die Postleitzahl "+matchedGroups[0]+" nicht");
            else if(zip2== null && zip1!=null)
                m.setResponse("Kenne leider die Postleitzahl "+matchedGroups[1]+" nicht");
            else
                m.setResponse("Kenne leider die Postleitzahlen "+matchedGroups[1]+" und "+matchedGroups[0]+" nicht");
            trace.error(m, "one or more zipcode are unknown");
        }
		return REPLY;
	}

    /**
     * only to fit a number from '123123.2324324'  to '123123.2'
     * ...userful for some outputs.
     *
     */
    private static double fitKM(double value)
    {
        int distInt = (int) (value / 100.0);
        value = (double) distInt / 10.0;

        if((((int)(value * 10.0)) % 10) == 0)
        {
            return (double)(int)value;
        }

        return value;
    }
}