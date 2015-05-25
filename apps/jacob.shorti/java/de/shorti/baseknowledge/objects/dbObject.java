package de.shorti.baseknowledge.objects;

import java.util.*;
import java.util.WeakHashMap;

public class dbObject
{
    protected String id;
    private static HashMap cache = new HashMap();

	protected static String toSQL(String input)
	{
        if(input==null)
            return "";

		StringBuffer erg = new StringBuffer(input);
		int pos = 0;
		while(pos < erg.length())
		{
			if(erg.charAt(pos) == '\'')
			{
				if(((pos+1)==erg.length()) || erg.charAt(pos+1) != '\'')
				{
					erg.insert(pos+1, '\'');
				}
				pos++;
			}
			pos++;
		}
		return erg.toString();
	}

	protected static String toSQL(java.sql.Date input)
	{
        if(input==null)
        {
            return "";
        }

        return input.toString();
	}

	protected static String toSQL(boolean input)
	{
        if(input)
            return "1";
        return "0";
	}

    protected static dbObject getFromCache(String id)
    {
        return (dbObject)cache.get(id);
    }

    protected static void putToCache(dbObject obj)
    {
       cache.put(obj.id, obj);
    }

    protected static void removeFromCache(dbObject obj)
    {
       cache.remove(obj.id);
    }
}
