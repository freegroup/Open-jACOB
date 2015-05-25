package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.common.sap.CImportObjects;
import jacob.common.sap.CSAPHelperClass;
import jacob.common.sap.CWriteSAPExchange;
import jacob.common.sap.ConnManager;
import jacob.common.sap.DateFormater;
import jacob.model.Sapadmin;
import jacob.model.Task;

import java.util.Date;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.DailyIterator;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * @author achim
 *
 */
public class SAPObjectImport extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: SAPObjectImport.java,v 1.5 2008/06/27 15:00:18 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.5 $";
	
	// use this to log relvant information....not the System.out.println(...)    ;-)
	//
	static protected final transient Log logger = AppLogger.getLogger();


	// Start the task every 1 minutes
	// for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
	//
	final ScheduleIterator iterator = new DailyIterator(2,0,0);

  //final ScheduleIterator iterator = new MinutesIterator(5);
	/**
	 * Returns the Iterator which defines the run interval of this job.<br>
	 * 
	 */
	public ScheduleIterator iterator()
	{
		return iterator;
	}

  public String getSchedulerName()
  {
    // run all sap tasks in an own thread
    return "sap";
  }

  /**
	 * The run method of the job.<br>
	 * The object <code>context>/code> defines your current context in the jACOB application
	 * server.<br>
	 * You can use it to access the database or other relevatn application data.<br>
	 */
	public void run(TaskContextSystem context) throws Exception
	{
    // Debug Modus abfragen und ggf. setzen
    if (CSAPHelperClass.checkDebug()) //
    {
      context.setPropertyForSession("SAPdebug", "1");
    }
    else
    {
      context.setPropertyForSession("SAPdebug", "0");
    }
    String sFromTime="";
    //Startzeit 
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable sapadmin = acc.getTable(Sapadmin.NAME);
    sapadmin.qbeClear();
    sapadmin.qbeSetValue(Sapadmin.active, "1");
    sapadmin.search();
    if (sapadmin.recordCount() != 1)
    {
      throw new Exception("Aktiver Verbindungsdatensatz zu SAP konnte nicht ermittelt werden!");
    }
    sFromTime = DateFormater.GetStringTime(sapadmin.getRecord(0).getDateValue(Sapadmin.lastobj_job_exe));
    String sFromDate="";
    sFromDate = DateFormater.GetStringDate(sapadmin.getRecord(0).getDateValue(Sapadmin.lastobj_job_exe));
    

    // Job starten
    CSAPHelperClass.printDebug("*** Job SAPObjectimport started at: " + (new Date()) + " ***");
    ConnManager oConMan = new ConnManager();


    CImportObjects.getSAPObjects(oConMan,sFromDate, sFromTime,"");
    //CImportObjects.getSAPObjStatus(oConMan, sFromDate, sFromTime);
    
    IDataTransaction transaction = acc.newTransaction();
    try 
    {
      sapadmin.getRecord(0).setValue(transaction, Sapadmin.lastobj_job_exe, "now");
      transaction.commit();
    } 
    finally 
    {
      transaction.close();
      // TODO: handle exception
    }
    CImportObjects.writeSAPObjects();
    CImportObjects.cleanSAPObjects();


    CSAPHelperClass.printDebug("*** Job SAPObjectimport ended at: " + (new Date()) + " ***");
    }
}
