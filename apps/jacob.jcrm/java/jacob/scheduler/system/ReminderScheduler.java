package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.scheduler.system.reminder.IScheduleReminder;
import jacob.scheduler.system.reminder.ScheduleReminderFactory;

import org.apache.commons.logging.Log;

import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * @author andreas
 *
 */
public class ReminderScheduler extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: ReminderScheduler.java,v 1.1 2005/10/12 15:29:36 sonntag Exp $";
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
	 * You can use it to access the database or other relevant application data.<br>
	 */
	public void run(TaskContextSystem context) throws Exception
	{
	  IScheduleReminder reminderScheduler = ScheduleReminderFactory.getScheduler();
      reminderScheduler.run(context);
	}

	/**
	 * If you return false the job is disabled per default. You can enable the job in the jACOB
	 * administrator console. <br>
	 * <b>Note:</b> The job doesn't start after a restart of the application server (or tomcat,...)
	 * if you return <code>false</code>.<br>
	 * You must enable the job after each restart manually!<br>
	 *  
	 * @return false if you want to disable the job per default. Return true in the other case.
	 */
	public boolean hibernatedOnSchedule()
	{
		return false;
	}
}
