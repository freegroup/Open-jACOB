package jacob.scheduler.system;

import java.util.Date;

import jacob.common.AppLogger;
import jacob.common.sap.CSAPHelperClass;
import jacob.model.Appprofile;
import jacob.model.Call;
import jacob.model.Sapcallexchange;
import jacob.model.Task;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
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
public class CloseCallTask extends SchedulerTaskSystem
{
  static public final transient String RCS_ID = "$Id: CloseCallTask.java,v 1.1 2008/04/29 07:52:11 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this to log relvant information....not the System.out.println(...) ;-)
  //
  static protected final transient Log logger = AppLogger.getLogger();

  // Start the task every 1 minutes
  // for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
  //
  final ScheduleIterator iterator = new DailyIterator(23, 0, 0);

  // final ScheduleIterator iterator = new MinutesIterator(3);

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
    return "SapCloseCalls";
  }

  public static String clearDocumentedTask(Context context, IDataTableRecord task) throws Exception
  {
    IDataTransaction transaction = context.getDataAccessor().newTransaction();
    try
    {
      task.setValue(transaction, Task.accountdate, "now");
      // taskrec.setValue(transaction, Task.dateclosed, "now");
      task.setValue(transaction, Task.taskstatus, Task.taskstatus_ENUM._Abgerechnet);

      transaction.commit();

    }
    catch (Exception e)
    {
      CSAPHelperClass.printDebug(e.getMessage());
      return "0";
    }
    finally
    {
      transaction.close();
    }
    return "1";
  }

  public static void closeDocumentedCall(Context context, IDataTableRecord call) throws Exception
  {
    IDataTransaction transaction = context.getDataAccessor().newTransaction();
    try
    {
      call.setValue(transaction, Call.callstatus, Call.callstatus_ENUM._Geschlossen);
      call.setValue(transaction, Call.dateclosed, "now");
      call.setValue(transaction, Call.modifiedby, "CloseCallJob");
      transaction.commit();
    }
    catch (Exception e)
    {
      CSAPHelperClass.printDebug(e.getMessage());
    }
    finally
    {
      transaction.close();
    }
  }

  private static void writeCloseExchangeCall(IDataTableRecord call) throws Exception
  {
    IDataAccessor acc = call.getAccessor().newAccessor();
    IDataTransaction transaction = acc.newTransaction();
    IDataTable sapexchange = acc.getTable("sapcallexchange");
    IDataTableRecord exchangerec = sapexchange.newRecord(transaction);
    exchangerec.setValue(transaction, "action", "close");
    exchangerec.setValue(transaction, "type", "export");
    exchangerec.setValue(transaction, "exc_table", "call");
    exchangerec.setValue(transaction, "ttscallid", call.getSaveStringValue("pkey"));
    exchangerec.setValue(transaction, "sapssleid", call.getSaveStringValue("sap_ssle_nr"));
    try
    {
      transaction.commit();
    }
    finally
    {
      transaction.close();
    }
  }

  private static void writeCloseExchangeTaskCT(IDataTableRecord task) throws Exception
  {
    IDataAccessor acc = task.getAccessor().newAccessor();
    IDataTransaction transaction = acc.newTransaction();
    IDataTable sapexchange = acc.getTable("sapcallexchange");
    IDataTableRecord exchangerec = sapexchange.newRecord(transaction);
    exchangerec.setValue(transaction, "action", "close");
    exchangerec.setValue(transaction, "type", "export");
    exchangerec.setValue(transaction, "exc_table", "task");
    exchangerec.setValue(transaction, "ttstaskno", task.getSaveStringValue("taskno"));
    exchangerec.setValue(transaction, "ttstaskpkey", task.getSaveStringValue("pkey"));
    exchangerec.setValue(transaction, "ttscallid", task.getSaveStringValue("calltask"));
    exchangerec.setValue(transaction, Sapcallexchange.saptaskid, task.getSaveStringValue(Task.sap_auftrag));
    try
    {
      transaction.commit();
    }
    finally
    {
      transaction.close();
    }
  }

  public static void repairCallsBeforeClose(Context context) throws Exception
  {

    if (CSAPHelperClass.checkDebug()) //
    {
      context.setPropertyForSession("SAPdebug", "1");
    }
    else
    {
      context.setPropertyForSession("SAPdebug", "0");
    }

    String closeDelay = "30d";
    String dokDelay = "35d";
    // appprofile.callclosedelay
    IDataTable profile = context.getDataTable(Appprofile.NAME);
    profile.search();
    if (profile.recordCount() == 1)
    {
      IDataTableRecord profilerec = profile.getRecord(0);
      closeDelay = profilerec.getSaveStringValue(Appprofile.callclosedelay);
      dokDelay = profilerec.getSaveStringValue(Appprofile.calldokdelay);
    }

    IDataTable call = context.getDataTable(Call.NAME);
    call.setMaxRecords(IDataTable.UNLIMITED_RECORDS);
    // Wir suchen die zu reparierenden Meldungen/Aufträge für Schließen

    call.qbeClear();
    call.qbeSetValue(Call.callstatus, Call.callstatus_ENUM._Dokumentiert);
    call.qbeSetValue(Call.datedocumented, "<today-" + closeDelay);
    call.qbeSetValue(Call.opentaskcount, ">0");
    call.search();
    CSAPHelperClass.printDebug("Closecalls: dokumentierte Calls mit offenen Aufträgen: " + call.recordCount());
    for (int i = 0; i < call.recordCount(); i++)
    {
      IDataTableRecord callrec = call.getRecord(i);
      IDataTable task = context.getDataTable(Task.NAME);
      task.qbeClear();
      task.qbeSetKeyValue(Task.calltask, callrec.getValue(Call.pkey));
      task.qbeSetValue(Task.taskstatus, Task.taskstatus_ENUM._In_Arbeit);
      task.search();
      CSAPHelperClass.printDebug("Closecalls: Offenen Aufträge zu call: " + callrec.getValue(Call.pkey) + ": " + task.recordCount());
      for (int j = 0; j < task.recordCount(); j++)
      {
        IDataTableRecord taskrec = task.getRecord(j);

        try
        {
          jacob.common.Task.setStatus(null, context, taskrec, "Fertig gemeldet");
          jacob.common.Task.setDocumented(null, taskrec);
        }
        catch (Exception e)
        {

          CSAPHelperClass.printDebug("Closecalls: Reparieren " + e.getMessage());
        }
      }
    }

  }

  public static void dokResolvedCalls(Context context) throws Exception
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

    String closeDelay = "30d";
    String dokDelay = "35d";
    // appprofile.callclosedelay
    IDataTable profile = context.getDataTable(Appprofile.NAME);
    profile.search();
    if (profile.recordCount() == 1)
    {
      IDataTableRecord profilerec = profile.getRecord(0);
      closeDelay = profilerec.getSaveStringValue(Appprofile.callclosedelay);
      dokDelay = profilerec.getSaveStringValue(Appprofile.calldokdelay);
    }

    IDataTable call = context.getDataTable(Call.NAME);

    call.setMaxRecords(IDataTable.UNLIMITED_RECORDS);
    call.qbeClear();
    call.qbeSetValue(Call.callstatus, Call.callstatus_ENUM._Fertig_gemeldet);
    call.qbeSetValue(Call.dateresolved, "<today-" + dokDelay);
    call.qbeSetValue(Call.opentaskcount, "0");
    call.search();
    CSAPHelperClass.printDebug("Closecalls: fertiggemeldete Calls zu dokumentieren: " + call.recordCount());
    for (int l = 0; l < call.recordCount(); l++)
    {
      try
      {
        jacob.common.Call.setStatus(context, call.getRecord(l), "Dokumentiert");
      }
      catch (Exception e)
      {
        CSAPHelperClass.printDebug("Closecalls: Dokumentieren " + e.getMessage());
      }
    }

  }

  public static void dokTasksForCallsResolved(Context context) throws Exception
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

    String closeDelay = "30d";
    String dokDelay = "35d";
    // appprofile.callclosedelay
    IDataTable profile = context.getDataTable(Appprofile.NAME);
    profile.search();
    if (profile.recordCount() == 1)
    {
      IDataTableRecord profilerec = profile.getRecord(0);
      closeDelay = profilerec.getSaveStringValue(Appprofile.callclosedelay);
      dokDelay = profilerec.getSaveStringValue(Appprofile.calldokdelay);
    }

    IDataTable call = context.getDataTable(Call.NAME);
    call.setMaxRecords(IDataTable.UNLIMITED_RECORDS);
    // Wir suchen Dokumentieren die Auftrage der hier gefunden Meldungen

    call.qbeClear();
    call.qbeSetValue(Call.callstatus, Call.callstatus_ENUM._Fertig_gemeldet);
    call.qbeSetValue(Call.dateresolved, "<today-" + dokDelay);
    call.qbeSetValue(Call.opentaskcount, "0");
    call.search();
    CSAPHelperClass.printDebug("Closecalls: Fertiggemeldete Calls,  mit fertiggemeldeten Aufträgen, die dokumentiert werden: " + call.recordCount());
    for (int i = 0; i < call.recordCount(); i++)
    {
      IDataTableRecord callrec = call.getRecord(i);
      IDataTable task = context.getDataTable(Task.NAME);
      task.qbeClear();
      task.qbeSetKeyValue(Task.calltask, callrec.getValue(Call.pkey));
      task.qbeSetValue(Task.taskstatus, Task.taskstatus_ENUM._Fertig_gemeldet);
      task.search();
      CSAPHelperClass.printDebug("Closecalls: fertiggemeldete Aufträge zu call: " + callrec.getValue(Call.pkey) + ": " + task.recordCount());
      for (int j = 0; j < task.recordCount(); j++)
      {
        IDataTableRecord taskrec = task.getRecord(j);

        try
        {
          jacob.common.Task.setDocumented(null, taskrec);
        }
        catch (Exception e)
        {

          CSAPHelperClass.printDebug("Closecalls: DokTask Reparieren " + e.getMessage());
        }
      }
    }

  }

  public static void closeCalls(Context context) throws Exception
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

    String closeDelay = "30d";
    String dokDelay = "35d";
    // appprofile.callclosedelay
    IDataTable profile = context.getDataTable(Appprofile.NAME);
    profile.search();
    if (profile.recordCount() == 1)
    {
      IDataTableRecord profilerec = profile.getRecord(0);
      closeDelay = profilerec.getSaveStringValue(Appprofile.callclosedelay);
      dokDelay = profilerec.getSaveStringValue(Appprofile.calldokdelay);
    }

    IDataTable fixcall = context.getDataTable("fixcalls");
    fixcall.setMaxRecords(IDataTable.UNLIMITED_RECORDS);
    // Nach erfolgter Reparatur schließen wir die Dokumentierten
    // Meldungen/Aufträge
    fixcall.qbeClear();
    fixcall.qbeSetValue(Call.callstatus, Call.callstatus_ENUM._Dokumentiert);
    fixcall.qbeSetValue(Call.datedocumented, "<today-" + closeDelay);
    fixcall.qbeSetValue(Call.opentaskcount, "0");
    fixcall.search();
    CSAPHelperClass.printDebug("Closecalls: dokumentierte Calls zu schließen: " + fixcall.recordCount());
    for (int k = 0; k < fixcall.recordCount(); k++)
    // for (int k = 0; k < call.recordCount(); k++)
    {
      String taskcommitReached = "1";

      IDataTableRecord callrec = fixcall.getRecord(k);
      IDataTable fixtask = context.getDataTable("fixtask");
      fixtask.qbeClear();
      fixtask.qbeSetKeyValue(Task.calltask, callrec.getValue(Call.pkey));
      fixtask.search();
      CSAPHelperClass.printDebug("Closecalls:  Aufträge zu call schließen : " + callrec.getValue(Call.pkey) + ": " + fixtask.recordCount());

      for (int m = 0; m < fixtask.recordCount(); m++)

      {
        IDataTableRecord taskrec = fixtask.getRecord(m);
        if (taskrec.getValue(Task.taskstatus).equals(Task.taskstatus_ENUM._Dokumentiert))
        {
          CSAPHelperClass.printDebug("Closecalls:  Auftrag gefunden ");
          taskcommitReached = CloseCallTask.clearDocumentedTask(context, taskrec);
          if (taskcommitReached.equals("1"))
          {
            writeCloseExchangeTaskCT(taskrec);
          }
          else
          {
            break;
          }
        }

        else
        {
          taskcommitReached = "1";
          CSAPHelperClass.printDebug("Closecalls: Kein Auftrag gefunden ");
        }

      }

      if (taskcommitReached.equals("1"))
      {
        CSAPHelperClass.printDebug("Closecalls: Meldung Schließen ");
        CloseCallTask.closeDocumentedCall(context, callrec);
        writeCloseExchangeCall(callrec);
      }
      else
      {
        CSAPHelperClass.printDebug("Closecalls:  Schließen nicht möglich, Fehler bei Auftrag. MeldungNr.: " + callrec.getValue(Call.pkey));
      }

    }
  }

  /**
   * The run method of the job.<br>
   * The object
   * <code>context>/code> defines your current context in the jACOB application
   * server.<br>
   * You can use it to access the database or other relevatn application data.<br>
   */
  public void run(TaskContextSystem context) throws Exception
  {
    CSAPHelperClass.printDebug("Closecalls:  Job Started at " + (new Date()));

    dokTasksForCallsResolved(context);
    dokResolvedCalls(context);
    repairCallsBeforeClose(context);
    closeCalls(context);
    CSAPHelperClass.printDebug("Closecalls:  Job Ended at " + (new Date()));
  }
}
