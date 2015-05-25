package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.model.Document;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.SecondsIterator;

/**
 * @author andherz
 *
 */
public class RecreateThumbnailCache extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: RecreateThumbnailCache.java,v 1.2 2010/02/08 16:23:38 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";
	
	// Start the task every 1 minutes
	// for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
	//
	final ScheduleIterator iterator = new SecondsIterator(60);


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
    IApplicationDefinition app = context.getApplicationDefinition();
	  IDataTable documentTable = context.getDataTable(Document.NAME);
	  documentTable.qbeSetValue(Document.thumbnail, "!null");
	  documentTable.search();
	  for(int i=0;i<documentTable.recordCount();i++)
	  {
	    IDataTableRecord tableRecord = documentTable.getRecord(i);
	    String pkey = tableRecord.getSaveStringValue(Document.pkey);
	    String normalizedPath = Bootstrap.getApplicationRootPath()+"application/"+app.getName()+"/"+app.getVersion().toShortString()+"/cache/"+pkey+"_normalized.png";
      String optimalPath    = Bootstrap.getApplicationRootPath()+"application/"+app.getName()+"/"+app.getVersion().toShortString()+"/cache/"+pkey+"_optimal.png";
	    if(!new File(normalizedPath).exists())
	    {
	      DataDocumentValue thumbnail = tableRecord.getDocumentValue(Document.normalized_thumbnail);
        FileUtils.writeByteArrayToFile(new File(normalizedPath), thumbnail.getContent());

        thumbnail = tableRecord.getDocumentValue(Document.thumbnail);
        FileUtils.writeByteArrayToFile(new File(optimalPath), thumbnail.getContent());
	    }
	  }
	}
}
