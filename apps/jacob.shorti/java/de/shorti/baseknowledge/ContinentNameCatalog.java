package de.shorti.baseknowledge;

/**
 * short-i Class generated by automatic ClassGenerator
 * Date: Mon May 21 18:30:16 GMT+02:00 2001
 */
import de.shorti.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;
import de.shorti.util.basic.*;
import de.shorti.util.search.fuzzy.*;
import de.shorti.baseknowledge.objects.Continent;



public class ContinentNameCatalog
{
	static final int MIN_HIT_PERCENTAGE = 30;
	static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();
	static FuzzySearch search = new FuzzySearch();


	/**
	* returns ArrayList<Keyword>
	*/
	static public ArrayList find(String s)
	{
		ArrayList tmp    = search.find(s,MIN_HIT_PERCENTAGE);
		ArrayList result = new ArrayList();
		// the first Item is the best hit....move only the 'top ten'
		// to the result set
		//
		int lastHitPercentage = 0;
		Iterator iter = tmp.iterator();
		while(iter.hasNext())
		{
			Keyword k = (Keyword)iter.next();
			if(k.m_percentage >= lastHitPercentage )
			{
				result.add(k);
				lastHitPercentage = k.m_percentage;
			}
			else
			{
				return result;
			}
		}
		return result;
	}


	/**
	* load the keywords in the FuzzySearch engine
	*/
	static
	{
		trace.debug2("loading ContinentNameCatalog from database...");
		ArrayList   objects    = Continent.getAll();
		Iterator iter       = objects.iterator();
		HashSet  newCatalog = new HashSet();
		Continent object = null;

		while(iter.hasNext())
		{
			object = (Continent)iter.next();
			newCatalog.add(object.getName());
		}
		search.setCatalog(newCatalog);
		trace.debug2("...done");

	}

}
