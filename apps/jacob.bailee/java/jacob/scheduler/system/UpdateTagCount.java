package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.model.Document;
import jacob.model.Tagging;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * @author andherz
 *
 */
public class UpdateTagCount extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: UpdateTagCount.java,v 1.3 2010/02/08 16:23:38 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

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
	  IDataTable taggingTable = context.getDataAccessor().getTable(Tagging.NAME);
    IDataTable docTable = context.getDataAccessor().getTable(Document.NAME);
    
	  taggingTable.search();
	  for(int i=0; i<taggingTable.recordCount();i++)
	  {
	    IDataTableRecord tagRecord= taggingTable.getRecord(i);
	    String tag = tagRecord.getSaveStringValue(Tagging.tag);
	    
	    IDataTransaction transaction = context.getDataAccessor().newTransaction();
	    
	    // Alle Dokumente finden die zu dem Tag gehören
	    //
	    docTable.qbeClear();
	    docTable.qbeSetValue(Document.tag, tag);
	    long count = docTable.count();
	    try
	    {
	      tagRecord.setValue(transaction, Tagging.document_count, count);
	      transaction.commit();
	    }
	    catch(Exception exc)
	    {
	      // ignore
	    }
	    finally
	    {
	      transaction.close(); 
	    }
	  }
	  
    IDataTransaction transaction = context.getDataAccessor().newTransaction();
    taggingTable.qbeClear();
    taggingTable.clear();
    taggingTable.qbeSetValue(Tagging.document_count, "0");
    try
    {
      taggingTable.fastDelete(transaction);
      transaction.commit();
    }
    catch(Exception exc)
    {
      // ignore
    }
    finally
    {
      transaction.close(); 
    }
	  
	}
}
