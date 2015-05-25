package de.shorti.context;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.shorti.baseknowledge.CityNameCatalog;
import de.shorti.baseknowledge.objects.City;
import de.shorti.context.basis.GenericContext;
import de.shorti.message.Message;
import de.shorti.util.basic.TraceDispatcher;
import de.shorti.util.basic.TraceFactory;
import de.shorti.util.search.fuzzy.Keyword;


public class DialPrefix extends GenericContext
{
   static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

   public class ResponseItem
   {
        public String cityName;
        public String dialprefix;
   }


    public DialPrefix()
    {
        super("./context/DialPrefix.xml");
    }
   /**
    * Try to find the dialprefix for the required city
    *
    */
	public int onStart2City(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        // try to find a valid city for the retrieved matched
        //
        ArrayList result = CityNameCatalog.find(matchedGroups[0]);
        Iterator iter = result.iterator();
        if(iter.hasNext())
        {
            m.log(result.size()+" possible entries in <i>city</i> found");
            while(iter.hasNext())
            {
                Keyword cityName = (Keyword)iter.next();
                Iterator ci     = City.findByName(cityName.m_keyword).iterator();
                while(ci.hasNext() )
                {
                    City city = (City)ci.next();
                    ResponseItem item = new ResponseItem();
                    item.cityName   = city.getName();
                    item.dialprefix = city.getDialPrefix();
                    addResponseItem(m,item);
                }
            }
            return RENDER_OUTPUT;
        }

        m.setResponse("Konnte die Vorwahl leider nicht bestimmen");
        m.setStatusWarning();
        return REPLY;
   }

   /**
    * send the last message to the user
    *
    */
	public int onCity2Repeat(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        m.setResponse("Ich wiederhole:"+ getLastValidResponse(userDataMap));
		return REPLY;
	}

    /**
     *
     */
	public int onRepeat2Repeat(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        m.setResponse(getLastValidResponse(userDataMap));
		return REPLY;
	}
}