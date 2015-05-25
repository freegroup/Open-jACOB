package {package};

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jacob.common.AppLogger;
import jacob.config.Config;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;
import de.tif.jacob.util.file.Directory;

/**
 * Example scheduled job for scanning a directory and import a file into 
 * the database (create a call).
 * 
 * @author {author}
 *
 */
public class {class} extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: FileScanner.java,v 1.1 2007/05/18 16:13:49 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
	// use this to log relvant information....not the System.out.println(...)    ;-)
	static protected final transient Log logger = AppLogger.getLogger();

	// Start the task every 1 minutes
	// for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
	final ScheduleIterator iterator = new MinutesIterator(10);

	/**
	 * Returns the Iterator which defines the run interval of this job.<br>
	 */
	public ScheduleIterator iterator()
	{
		return iterator;
	}

	/**
	 * MAIN METHOD OF THE JOB.
	 * 
	 * Example code snippet which scans a well define directory and printout the
	 * content of all text documents.
	 * 
	 * Use this template to parse the files via a StringTokenizer/Castor/XMLBean....
	 * and generate your required jobs or DB entries.
	 * 
	 */
	public void run(TaskContextSystem context) throws Exception
	{
		// retrieve the directory for the file scan from the application
		// configuration file
		//
		Config conf = new Config();
		String path = conf.getProperty("filesystem.path");
		
		// do nothing if you have not configured proper
		//
		if(path==null)
		{
			logger.error("Required property 'filesystem.path' not found in 'config.properties'");
			return;
		}
		
		// Collect all files in the defined directory. (hands over 'true' as additional parameter
		// if you want collect the files in all subdirectories)
		//
		ArrayList files = Directory.getAll(new File(path),false);
		Iterator iter = files.iterator();
		while (iter.hasNext()) 
		{
			File element = (File) iter.next();
			try 
			{
				String filename = element.getName();
				String content  = FileUtils.readFileToString(element,"ISO-8859-1"); // possible change them to UTF-8
				
				// DO SOMETHING WITH THE FILE CONTENT!!!!!
				// (e.g. create a db entry .....)
				createCall(context,content);
				
				// DELETE THE PROCEED FILE
				FileUtils.forceDelete(element);
			} 
			catch (Exception e) 
			{
				// handsover the exception the jACOB system for reporting
				ExceptionHandler.handle(e);
				// delete or move the corrupt file(?)
				// FileUtils.forceDelete(element);
			}
		}
	}
	
	/**
	 * Create a call with the hands over data.
	 * 
	 * ========================================================================
	 * ========================================================================
	 * Be in Mind:
	 *   This is only a example code. Fit the table names and attributes to your
	 *   data model an business logic!!! ...or delete this code ;-)
	 * 
	 * ========================================================================
	 * ========================================================================
	 * 
	 * @param context The current working context of the scheduled job
	 * @param content the data from the read file.
	 * @throws Exception
	 */
  public void createCall(TaskContextSystem context, String content) throws Exception
  {
  		// parse the content and extract a customer and some call data.
  		String customerId = content.substring(0,10);
  		String callData   = content.substring(10);
  		
      IDataTable customer = context.getDataTable("customer");
      IDataTable call     = context.getDataTable("call");
      
      // search the related customer
      //
      customer.clear();
      customer.qbeSetKeyValue("pkey", customerId);
      customer.search();
      
      // hip - we found the customer
      //
      if (customer.recordCount()==1)
      {
      	// create a call for the customer
	      IDataTransaction trans = call.startNewTransaction();
	      try
	      {
	        IDataTableRecord callrec = call.newRecord(trans);
          callrec.setValue(trans,"customercall",customerId); // set the foreing relation to the customer
	        callrec.setValue(trans,"problem",callData);        // set the other call data values....
	        trans.commit();                                    // commit the transaction
	      }
	      finally
				{
	        trans.close();                                     // and free all resources from the transaction
	      }
      }
      else
      {
      	// do your error handling here
      }
  }	
}
