package de.shorti.baseknowledge.objects;

/**
 * short-i Class generated by automatic ClassGenerator
 * Date: Wed Mar 14 14:59:41 GMT+01:00 2001
 */
import de.shorti.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;
import de.shorti.util.basic.*;

public class Country extends _dbCountry
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();
    static HashMap dialprefix2Country =null;

    /**
     *
     */
    public static Country getHomeCountry(String dialNumber)
    {
        // TODO
        // The country table are not filled with the dialPrefixes!!!!!
        return Country.findByShortName("DE");
    }


    /**
     *
     */
    static
    {
        dialprefix2Country = new HashMap();

        ArrayList countries = Country.getAll();
        Iterator iter = countries.iterator();
        while(iter.hasNext())
        {
            Country country = (Country)iter.next();
            String dialPrefix = country.getDialPrefix();
            if((dialPrefix != null) && (!dialPrefix.equals("")))
            {
                dialprefix2Country.put(dialPrefix,country);
            }
            else
            {
                trace.warning("the country table are not propper filled with the dialPrefixes");
            }
        }
    }
}
