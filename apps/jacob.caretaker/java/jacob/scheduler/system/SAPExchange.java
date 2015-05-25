package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.common.sap.CSAPHelperClass;
import jacob.common.sap.CSAPJobFunctions;
import jacob.model.Sapcallexchange;
import jacob.model.Saperror;

import java.util.Date;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * @author E050_FWT-ANT_o_test
 * 
 */
public class SAPExchange extends SchedulerTaskSystem
{
  static public final transient String RCS_ID = "$Id: SAPExchange.java,v 1.11 2008/07/24 13:17:39 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.11 $";

  // use this to log relvant information....not the System.out.println(...) ;-)
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

  public String getSchedulerName()
  {
    // run all sap tasks in an own thread
    return "sap";
  }

  /**
   * Methode zur Löschen der erledigten Aufgabensätze in der Tabelle
   * Sapcallexchange swie zum Aufräumen und reparieren etc.
   * 
   * @param Context,
   *          aktueller Context
   * @exception Exception
   */
  // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
  // SAP - Aufräumen der Tabelle Sapcallexchange
  // --------------------------------------------------------------------------------
  private void sapCleanUp(TaskContextSystem context) throws Exception
  {
    // Versuchen Fehler aufgrund gesperrter Datensätze zu finden
    // und diese wier auf "Erneut verarbeiten setzen
    // TODO Achim/Andreas überlegen, ob das gut ist oder etwas aufwendiger
    // gestaltet werden sollte

    // Verarbeitete Sätze Löschen
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable callex = acc.getTable(Sapcallexchange.NAME);
    callex.qbeClear();
    // Nur die fertigen
    callex.qbeSetValue(Sapcallexchange.status, Sapcallexchange.status_ENUM._Done);
    callex.qbeSetValue(Sapcallexchange.errorstatus, "0");
    //Änderung: Wir behalten erst einmal die Import datensätze zur besseren Fehlerrecherche.
    //Jetzt können wir  sofort feststellen, welche Daten aus SAP gekommen sind, und ob wir in SAP oder bei uns nach
    //etwaigen Fehlern suchen müssem
    //TODO überlegen, wie wir die Tabelle bereinigen, da sie sonst zu groß wird und die Verarbeitung verlangsamt
    callex.qbeSetValue(Sapcallexchange.type, Sapcallexchange.type_ENUM._export);

    callex.search();

    IDataTransaction transaction2 = acc.newTransaction();
    try
    {
      callex.fastDelete(transaction2);
      transaction2.commit();
    }
    finally
    {
      transaction2.close();
    }

    // Sollte nicht mehr nötig sein, da wir direkt auf diee Tabelle schreiben
    /*
     * // / SM Nr setzten, wo wir wissen wie
     * 
     * callex.qbeClear(); // Nur die fertigen
     * callex.qbeSetValue(Sapcallexchange.status,
     * Sapcallexchange.status_ENUM._ToDo);
     * callex.qbeSetValue(Sapcallexchange.errorstatus, "1");
     * callex.qbeSetValue(Sapcallexchange.message, "Störmeldung*bitte über die
     * SAP-Meldung*abschließen*");
     * 
     * callex.search(); CSAPHelperClass.printDebug(callex.recordCount() + "
     * Repair Sätze gefunden"); for (int i = 0; i < callex.recordCount(); i++) {
     * IDataTransaction transaction3 = acc.newTransaction(); try { StringBuffer
     * buf= new StringBuffer(); IDataTableRecord callexrec =
     * callex.getRecord(i);
     * buf.append(callexrec.getSaveStringValue(Sapcallexchange.message)); String
     * smnr = "0000" + buf.substring(46, 54); String callpkey =
     * callexrec.getSaveStringValue(Sapcallexchange.ttscallid); IDataTable call =
     * acc.getTable(Call.NAME); call.qbeClear(); call.qbeSetKeyValue(Call.pkey,
     * callpkey); call.search(); if (call.recordCount()==1) { IDataTableRecord
     * callrec = call.getRecord(0); callrec.setValue(transaction3,
     * Call.sap_sm_nr, smnr); callexrec.setIntValue(transaction3,
     * Sapcallexchange.errorstatus, 0); transaction3.commit(); } else {
     * CSAPHelperClass.printDebug(" *** SapCallRepair: Call nicht gefunden");
     * transaction3.close(); } } catch (Exception e) { // wir ignorieren die
     * exception CSAPHelperClass.printDebug(e.getLocalizedMessage()); } finally {
     * transaction3.close(); }
     *  }
     */

    // Paramerierte Methode zum Löschen oder Zurücksetzen von Callexchange
    // Datensätzen
    if (CSAPHelperClass.checkDebug()) //
    {
      context.setPropertyForSession("SAPdebug", "1");
    }
    else
    {
      context.setPropertyForSession("SAPdebug", "0");
    }

    IDataAccessor accCU = context.getDataAccessor().newAccessor();
    IDataTable saperror = accCU.getTable(Saperror.NAME);
    // zuerst mal alles löschen
    saperror.qbeClear();
    saperror.qbeSetValue(Saperror.actiontype, Saperror.actiontype_ENUM._loeschen);
    saperror.qbeSetValue(Saperror.aktiv, "1");
    for (int i = 0; i < saperror.search(); i++)
    {
      IDataTableRecord errorRec = saperror.getRecord(i);
      IDataTable callexCU = accCU.getTable(Sapcallexchange.NAME);
      callexCU.qbeClear();
      // Nur die gesperrten auf Retry setzten

      callexCU.qbeSetValue(Sapcallexchange.errorstatus, "1");
      callexCU.qbeSetValue(Sapcallexchange.message, errorRec.getSaveStringValue(Saperror.errortext));
      callexCU.qbeSetValue(Sapcallexchange.type, errorRec.getSaveStringValue(Saperror.type));
      callexCU.qbeSetValue(Sapcallexchange.action, errorRec.getSaveStringValue(Saperror.action));
      callexCU.qbeSetValue(Sapcallexchange.exc_table, errorRec.getSaveStringValue(Saperror.exc_table));
      callexCU.search();
      CSAPHelperClass.printDebug("  ***" + errorRec.getSaveStringValue(Saperror.outputmessage) + ": " + callexCU.recordCount());

      IDataTransaction transaction = accCU.newTransaction();
      try
      {
        callexCU.fastDelete(transaction);
        transaction.commit();
      }
      finally
      {
        transaction.close();
      }

    }

    // Error zurücksetzen
    saperror.qbeClear();
    saperror.qbeSetValue(Saperror.actiontype, Saperror.actiontype_ENUM._Fehler_zuruecksetzen);
    saperror.qbeSetValue(Saperror.aktiv, "1");
    for (int i = 0; i < saperror.search(); i++)
    {
      IDataTableRecord errorRec = saperror.getRecord(i);
      IDataTable callexCU1 = accCU.getTable(Sapcallexchange.NAME);
      callexCU1.qbeClear();
      // Nur die gesperrten auf Retry setzten

      callexCU1.qbeSetValue(Sapcallexchange.errorstatus, "1");
      callexCU1.qbeSetValue(Sapcallexchange.message, errorRec.getSaveStringValue(Saperror.errortext));
      callexCU1.qbeSetValue(Sapcallexchange.type, errorRec.getSaveStringValue(Saperror.type));
      callexCU1.qbeSetValue(Sapcallexchange.action, errorRec.getSaveStringValue(Saperror.action));
      callexCU1.qbeSetValue(Sapcallexchange.exc_table, errorRec.getSaveStringValue(Saperror.exc_table));
      callexCU1.search();
      CSAPHelperClass.printDebug("  ***" + errorRec.getSaveStringValue(Saperror.outputmessage) + ": " + callexCU1.recordCount());

      IDataTransaction transaction = accCU.newTransaction();
      try
      {
        for (int j = 0; j < callexCU1.recordCount(); j++)
        {
          callexCU1.getRecord(j).setValue(transaction, Sapcallexchange.errorstatus, "0");
          callexCU1.getRecord(j).setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
        }

        transaction.commit();
      }
      finally
      {
        transaction.close();
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

    // Um beim Import der Datensätze die Rücksynchronisation (Endlosschleife) zu
    // vermeiden
    // Property setzten,welches beim Call Tablerecord abgefragt wird
    context.setPropertyForSession("SAPimport", "1");
    // Debug Modus abfragen und ggf. setzen
    if (CSAPHelperClass.checkDebug()) //
    {
      context.setPropertyForSession("SAPdebug", "1");
    }
    else
    {
      context.setPropertyForSession("SAPdebug", "0");
    }

    // Job starten
    CSAPHelperClass.printDebug("*** Job SAPExchange started at: " + (new Date()) + " ***");
    // CSAPJobFunctions.sapCallWriteBack(context);
    CSAPJobFunctions.sapSSLECreate(context);
    CSAPJobFunctions.sapSSLE_SMUpdate(context);
    CSAPJobFunctions.sapSSLE_SM_Import(context);
    CSAPJobFunctions.sapTaskCreate(context);
    CSAPJobFunctions.sapTaskUpdate(context);// Includiert Task Close durch
    // setzten des Status -- Seit 17.7.
    // lt Mohamned nicht mehr s.
    // sapTaskClose
    CSAPJobFunctions.sapTaskImport(context);
    CSAPJobFunctions.sapTaskClose(context);
    CSAPJobFunctions.sapCallClose(context);
    // Nach Migration löschen
    CSAPJobFunctions.sapCallDelete(context);
    // CSAPJobFunctions.sapCallWriteBack(context);
    //
    sapCleanUp(context);

    CSAPHelperClass.printDebug("*** Job SAPExchange ended at: " + (new Date()) + " ***");
    // Ende des Jobs
  }

  /**
   * If you return false the job is disabled per default. You can enable the job
   * in the jACOB administrator console. <br>
   * <b>Note:</b> The job doesn't start after a restart of the application
   * server (or tomcat,...) if you return <code>false</code>.<br>
   * You must enable the job after each restart manualy!<br>
   * 
   * @return false if you want to disable the job per default. Return true in
   *         the other case.
   */
  public boolean hibernatedOnSchedule()
  {
    return true;
  }
}
