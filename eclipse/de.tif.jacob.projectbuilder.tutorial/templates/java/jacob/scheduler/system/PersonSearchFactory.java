package jacob.scheduler.system;

import jacob.common.AppLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;
import de.tif.jacob.util.search.ISearch;
import de.tif.jacob.util.search.Result;
import de.tif.jacob.util.search.fuzzy.FuzzySearch;
import de.tif.jacob.util.search.levenshtein.LevenshteinSearch;
import de.tif.jacob.util.search.metaphone.DoubleMetaphoneSearch;

/**
 * @author andherz
 *
 */
public class PersonSearchFactory extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: PersonSearchFactory.java,v 1.1 2007/11/25 22:19:42 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
	// use this to log relvant information....not the System.out.println(...)    ;-)
	//
	static protected final transient Log logger = AppLogger.getLogger();

	public final static ISearch LEVENSHTEIN = new LevenshteinSearch();
	public final static ISearch FUZZY       = new FuzzySearch();
	public final static ISearch METAPHONE   = new DoubleMetaphoneSearch();

	private static Set catalog = new HashSet();
	
	// Start the task every 1 minutes
	// for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
	//
	final ScheduleIterator iterator = new MinutesIterator(1);


	/**
	 * 
	 * @param lastname
	 * @return List[String]
	 */
	public static List search(ISearch search, String lastname, int factor)
	{
		List result = new ArrayList();
		Iterator iter=search.find(catalog, lastname,factor).iterator();
		while (iter.hasNext()) 
		{
			Result element = (Result) iter.next();
			result.add(element.keyword);
		}
		return result;
	}
	
	/**
	 * Returns the Iterator which defines the run interval of this job.<br>
	 * 
	 */
	public ScheduleIterator iterator()
	{
		return iterator;
	}

	/**
	 * The run method of the job.<br>
	 * The object <code>context>/code> defines your current context in the jACOB application
	 * server.<br>
	 * You can use it to access the database or other relevatn application data.<br>
	 */
	public void run(TaskContextSystem context) throws Exception
	{
		HashSet newCatalog = new HashSet();
		IDataTable table = context.getDataTable("person");
		table.setMaxRecords(IDataTable.UNLIMITED_RECORDS);
		table.search();
		for(int i=0 ;i<table.recordCount(); i++)
		{
			IDataTableRecord record = table.getRecord(i);
			newCatalog.add(record.getSaveStringValue("lastname"));
		}
		catalog = newCatalog;
	}

	/**
	 * If you return false the job is disabled per default. You can enable the job in the jACOB
	 * administrator console. <br>
	 * <b>Note:</b> The job doesn't start after a restart of the application server (or tomcat,...)
	 * if you return <code>false</code>.<br>
	 * You must enable the job after each restart manualy!<br>
	 *  
	 * @return false if you want to disable the job per default. Return true in the other case.
	 */
	public boolean hibernatedOnSchedule()
	{
		return false;
	}
}
