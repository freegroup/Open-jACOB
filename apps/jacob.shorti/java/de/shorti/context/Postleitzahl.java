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
import de.shorti.util.search.fuzzy.*;

public class Postleitzahl  extends GenericContext
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public Postleitzahl()
    {
        super("./context/Postleitzahl.xml");
    }

    /**
     *
     */
	public int onStart2City(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        ArrayList result = CityNameCatalog.find(matchedGroups[0]);
        if(result.size()==0)
        {
            m.setResponse("Kenne leider die Stadt ["+matchedGroups[0]+"] nicht.");
            m.setStatusWarning();
        }
        else
        {
            m.setResponse("Funktion leider noch nicht implementiert.");
            m.setStatusError();
//            Iterator iter = result.iterator();
//            while(iter.hasNext())
//            {
//                Keyword kw = (Keyword)iter.next();
//                Iterator cityIter = City.findByName(kw.m_keyword).iterator();
//                while(cityIter.hasNext())
//                {
//                    City city = (City)cityIter.next();
//                    _PostleitzahlResponseItem item = createResponseItem(m);
//                    item.city = city.getName();
//                    item.country = "unset";
//                    item.plz = city.get
//                }
//            }
        }
        return REPLY;
   }

}