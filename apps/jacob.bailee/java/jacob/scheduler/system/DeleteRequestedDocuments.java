package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.model.Document;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * @author andherz
 *
 */
public class DeleteRequestedDocuments extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: DeleteRequestedDocuments.java,v 1.1 2010/01/11 08:50:43 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	// Start the task every 1 minutes
	// for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
	//
	final ScheduleIterator iterator = new MinutesIterator(10);


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
		IDataTransaction trans = context.getDataAccessor().newTransaction();
		try
		{
			IDataTable documentTable = context.getDataTable(Document.NAME);
			documentTable.qbeSetValue(Document.request_for_delete_date,"<now-14d");
			documentTable.searchAndDelete(trans);
			trans.commit();
		}
		finally
		{
			trans.close();
		}
	}
}
