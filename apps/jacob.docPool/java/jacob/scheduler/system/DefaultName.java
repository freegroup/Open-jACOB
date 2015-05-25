package jacob.scheduler.system;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * @author andherz
 *
 */
public class DefaultName extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: Empty.java,v 1.1 2007/05/18 16:13:49 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
	// use this to log relvant information....not the System.out.println(...)    ;-)
	//
	static protected final transient Log logger = AppLogger.getLogger();


	// Start the task every 1 minutes
	// for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
	//
	final ScheduleIterator iterator = new MinutesIterator(1);


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
	  // insert your code here!!!
	}
}
