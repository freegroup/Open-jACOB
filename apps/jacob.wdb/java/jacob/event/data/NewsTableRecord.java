/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 05 15:31:38 CEST 2010
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.model.News;
import jacob.scheduler.system.NewsActivator;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.util.DatetimeUtil;

/**
 *
 * @author andherz
 */
public class NewsTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: NewsTableRecord.java,v 1.1 2010-08-06 16:00:27 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		// Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
		//
		if(tableRecord.isDeleted())
			return;
		
    boolean renew = tableRecord.getSaveStringValue(News.renew).equals(News.renew_ENUM._year);
    Date start = tableRecord.getDateValue(News.intervall_start);
    Date end = tableRecord.getDateValue(News.intervall_end);
    Date tmp=null;
    if(start!=null && end!=null && start.after(end))
    {
      tmp = start;
      start = end;
      end = tmp;
   }
   // Falls die news Jedes Jahr erneuert werden soll, dann muss ein Start und EndeDatum gesetzt sein.
   // Falls dies nicht der Fall ist, dann machen wir dies jetzt einfach
   //
   if(renew)
   {
     if(start==null && end!=null)
     {
       start = new Date(end.getTime());
       start.setMonth(0);
       start.setDate(1);
     }
     if(end==null && start!=null)
     {
       end = new Date(start.getTime());
       end.setMonth(11);
       end.setDate(31);
     }
   }

   tableRecord.setDateValue(transaction, News.intervall_start, start);
   tableRecord.setDateValue(transaction, News.intervall_end, end);
    
   NewsActivator.activate(transaction, tableRecord);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}
