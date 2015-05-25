package de.shorti.context;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.baseknowledge.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.message.Message;
import de.shorti.util.basic.*;
import de.shorti.context.basis.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import de.shorti.util.search.fuzzy.*;

public class CountryLanguage extends GenericContext
{
    public class ResponseItem
    {
        public String country;
        public String language;
    }

    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public CountryLanguage()
    {
        super("./context/CountryLanguage.xml");
    }

    /**
     *
     */
	public int onStart2Country(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        Country country = Country.findByName(matchedGroups[0]);

        // try to find a 'similar' country name from the fuzzy search
        // and search again in the country table
        //
        if(country==null)
        {
            Iterator iter = CountryNameCatalog.find(matchedGroups[0]).iterator();
            if(iter.hasNext())
                country = Country.findByName(((Keyword)iter.next()).m_keyword);
        }

        if(country!=null)
        {
            Language language = country.getLanguage();
            if(language!=null)
            {
                ResponseItem item = new ResponseItem();
                item.country = country.getName();
                item.language= language.getName();
                addResponseItem(m,item);
                return RENDER_OUTPUT;
            }
            else
            {
                trace.warning(m,"country ["+country.getName()+"] has no language attribute in the database");
                return REPLY;
            }
        }
        else
        {
            trace.debug1(m,"unable to find a country with the name ["+matchedGroups[0]+"]");
        }
        return REPLY;
    }
}