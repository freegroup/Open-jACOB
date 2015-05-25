package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.common.data.DataUtils;
import jacob.exception.BusinessException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * @author mike
 * 
 */
public class CustomerCallCreator extends SchedulerTaskSystem
{

  static public final transient String RCS_ID = "$Id: CustomerCallCreator.java,v 1.4 2006/05/15 13:50:56 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  // Konstanten für Feldnamen
  public static final String WORKGROUP = "workgroupcall";
  public static final String AGENT = "agentcall";
  public static final String CUSTOMER = "employeecall";
  public static final String PROCESS = "process_key";
  public static final String CATEGORY = "categorycall";
  public static final String AFFECTEDPERSON = "affectedperson_key";

  static protected final transient Log logger = AppLogger.getLogger();

  // Start the task every 1 minutes
  //
  final ScheduleIterator iterator = new MinutesIterator(1);

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.scheduler.SchedulerTaskSystem#iterator()
   */
  public ScheduleIterator iterator()
  {
    return iterator;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.scheduler.SchedulerTaskSystem#run(de.tif.jacob.scheduler.TaskContextSystem)
   */
  public void run(TaskContextSystem context) throws Exception
  {
    boolean hasError = false;
    Exception globalExeption = null;
    IDataTable custcall = context.getDataTable("custcall");
    try
    {
      List myJobs = getJobs(context);
      Iterator it = myJobs.iterator();
      while (it.hasNext())
      {
        try
        {
          IDataTableRecord task = (IDataTableRecord) it.next();
          if (logger.isDebugEnabled())
            logger.debug("Bearbeite: " + task.getValue("pkey"));
          if (processJob(context, task) == true)
          {
            if (logger.isDebugEnabled())
              logger.debug("Bearbeitung erfolgreich von " + task.getValue("pkey"));
            IDataTransaction trans = custcall.startNewTransaction();
            try
            {
              task.delete(trans);
              trans.commit();
            }
            finally
            {
              trans.close();
            }
          }
          else
          {
            hasError = true;
            if (logger.isDebugEnabled())
              logger.debug("CustInfo " + task.getSaveStringValue("pkey") + " ist nicht erfolgreich bearbeitet worden.");
          }
        }
        catch (Exception e)
        {
          globalExeption = e;
        }
      }
      IDataTransaction trans = custcall.startNewTransaction();
      try
      {
        DataUtils.getAppprofileRecord(context).setValue(trans, "last_custcall", "now");
        trans.commit();
      }
      catch (RecordLockedException e)
      {
        // aus irgendeinen Grund kann es passieren dass der Record gelockt ist (
        // nach dem runterfahren?)
        IDataTable qwLocks = context.getDataTable("qw_locks");
        qwLocks.clear();
        qwLocks.qbeSetKeyValue("tablename", "appprofile");
        qwLocks.qbeSetKeyValue("username", "systemuser");
        qwLocks.search();
        if (qwLocks.recordCount() > 0)
        {
          IDataTransaction lockTransaction = qwLocks.startNewTransaction();
          try
          {
            qwLocks.getRecord(0).delete(lockTransaction);
            lockTransaction.commit();
          }
          finally
          {
            lockTransaction.close();
          }
        }
      }
      finally
      {
        trans.close();
      }
    }

    catch (Exception e)
    {
      globalExeption = e;
    }

    finally
    {
      // Admin benachrichtigen, dass ein Fehler aufgetreten ist
      if (hasError)
        DataUtils.notifyAdministrator(context, "Fehler in CustomerCallCreator. Bitte in Administration/ YAN/Kundenmeldungen und catalina.out schauen");

      if (globalExeption != null)
        throw globalExeption;
    }
  }

  /**
   * schließt eine eventuell vorhandene Transaktion auf call oder task und
   * protokoliert den Fehler
   * 
   * @param context
   *          TaskContextSystem
   * @param e
   *          Exception
   */
  public void setError(IDataAccessor accessor, IDataTableRecord custcall, Exception e) throws Exception
  {

    IDataTransaction trans = custcall.getAccessor().getTable("custcall").startNewTransaction();
    try
    {
      custcall.setValue(trans, "error", "Ja");

      String message = e.getMessage();
      if (message == null)
      {
        message = ExceptionUtils.getStackTrace(e);
      }
      custcall.appendLongTextValue(trans, "errortext", message);
      trans.commit();
    }
    finally
    {
      trans.close();
    }

  }

  /**
   * fügt neuen Meldungsdatensatz ein Wichtig! Objekt Tätigkeit und AK sind
   * schon in der QBE!
   * 
   * @param context
   * @param parameter
   */
  public void createCall(IDataAccessor accessor, IDataTableRecord custcall, IDataTransaction trans) throws Exception
  {

    // neue Call anlegen
    IDataTableRecord callRecord = accessor.getTable("call").newRecord(trans);

    DataUtils.linkTable(accessor, trans, callRecord, CUSTOMER, "customerint", "pkey", custcall.getSaveStringValue("employee_key"));
    DataUtils.linkTable(accessor, trans, callRecord, AFFECTEDPERSON, "affectedperson", "pkey", custcall.getSaveStringValue("employee_key"));
    DataUtils.linkTable(accessor, trans, callRecord, AGENT, "agent", "pkey", DataUtils.getAppprofileValue(accessor, "agent_key"));
    DataUtils.linkTable(accessor, trans, callRecord, CATEGORY, "category", "pkey", DataUtils.getAppprofileValue(accessor, "category_key"));
    DataUtils.linkTable(accessor, trans, callRecord, PROCESS, "process", "pkey", DataUtils.getAppprofileValue(accessor, "process_key"));
    DataUtils.linkTable(accessor, trans, callRecord, WORKGROUP, "callworkgroup", "pkey", DataUtils.getAppprofileValue(accessor, "callworkgroup_key"));

    callRecord.setDateValue(trans, "datereported", custcall.getDateValue("datecreated"));
    callRecord.setDateValue(trans, "datecallconnected", custcall.getDateValue("datecreated"));
    callRecord.setValue(trans, "problemtext", custcall.getValue("description"));
    callRecord.setValue(trans, "callbackmethod", "Keine");
    callRecord.setValue(trans, "dateassigned", "now");
    callRecord.setValue(trans, "callstatus", "AK zugewiesen");
    callRecord.setValue(trans, "custtext", "Der Auftrag wurde zur Bearbeitung weitergeleitet");
    String problem = "";
    if (accessor.getTable("customerint").recordCount() != 1)
    {
      DataUtils.linkTable(accessor, trans, callRecord, CUSTOMER, "customerint", "pkey", DataUtils.getAppprofileValue(accessor, "agent_key"));
      problem = "Meldung von einem unbekannten Melder";

    }
    else
    {
      problem = "Meldung von " + accessor.getTable("customerint").getRecord(0).getStringValue("fullname");
    }
    callRecord.setStringValueWithTruncation(trans, "problem", problem);

  }

  /**
   * @param context
   * @param task
   * @return
   * @throws Exception
   */
  public boolean processJob(TaskContextSystem context, IDataTableRecord custcall) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    IDataTransaction trans = accessor.newTransaction();
    try
    {
      createCall(accessor, custcall, trans);
      trans.commit();
    }
    catch (BusinessException e)
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("Verstoß gegen Businessregel:" + SystemUtils.LINE_SEPARATOR + e.toString());
      }
      setError(accessor, custcall, e);
    }
    catch (Exception e)
    {
      setError(accessor, custcall, e);
      throw e;
    }
    finally
    {
      trans.close();
    }
    return true;
  }

  /**
   * @return ArrayList of taskrecords
   * @throws Exception
   */
  public List getJobs(TaskContextSystem context) throws Exception
  {
    // Aufträge suchen
    IDataTable custcall = context.getDataTable("custcall");
    custcall.qbeClear();
    custcall.qbeSetValue("error", "Nein");
    custcall.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
    custcall.search();
    List jobs = new ArrayList();
    for (int i = 0; i < custcall.recordCount(); i++)
    {
      jobs.add(custcall.getRecord(i));

    }

    return jobs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.scheduler.SchedulerTask#hibernatedOnSchedule()
   */
  public boolean hibernatedOnSchedule()
  {
    return true;
  }
}
