/**
 * 
 */
package jacob.common.sap;

import jacob.common.AppLogger;
import jacob.exception.SapProcessingException;
import jacob.model.Accountingcode;
import jacob.model.Call;
import jacob.model.Calls;
import jacob.model.Callworkgroup;
import jacob.model.Ext_system;
import jacob.model.Location;
import jacob.model.Object;
import jacob.model.Sapadmin;
import jacob.model.Sapcallexchange;
import jacob.model.Saplog;
import jacob.model.Task;
import jacob.model.Taskobject;
import jacob.model.Taskworkgroup;
import jacob.model.Workgroup;
import jacob.model.Workgrouphwg;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.scheduler.TaskContextSystem;

/**
 * @author achim
 * 
 */
public class CSAPJobFunctions
{
  // use this logger to write messages and NOT the System.println(..) ;-)
  private static final transient Log logger = AppLogger.getLogger();

  public CSAPJobFunctions()
  {
    // no constructor
  }

  /**
   * Hilfsmethode Prüft ob es sich um einen Nach SAP migrierten AK handelt
   * Tabelle Workgroup.sap_ak (Ist nur während der Migrationsphase nötig)
   */
  private static boolean isSapAk(IDataTableRecord call) throws Exception
  {
    IDataTableRecord ak = call.getLinkedRecord("callworkgroup");
    if (ak.getintValue("sap_ak") == 1)
    {
      // Es ist ein SAP AK
      CSAPHelperClass.printDebug("  * IsSAP AK ");
      return true;
    }
    return false;
  }

  /**
   * Hilfsmethode Prüft ob es sich um ein SAP relevantes Objekt handelt
   */
  private static boolean isSapObject(IDataTableRecord call) throws Exception
  {
    CSAPHelperClass.printDebug(" *** IsSapObject: Start");
    if (call.hasLinkedRecord(Object.NAME))
    {

      if (call.getLinkedRecord(Object.NAME).hasLinkedRecord("objectext_system"))
      {
        String sObjsystem = call.getLinkedRecord(Object.NAME).getLinkedRecord("objectext_system").getSaveStringValue(Ext_system.pkey);
        IDataTable sapadmin = call.getAccessor().newAccessor().getTable(Sapadmin.NAME);
        sapadmin.qbeClear();
        sapadmin.qbeSetValue(Sapadmin.active, "1");
        sapadmin.search();
        if (sapadmin.recordCount() != 1)
        {
          throw new Exception("Aktiver Verbindungsdatensatz zu SAP konnte nicht ermittelt werden!");
        }
        String sSAPsystem = sapadmin.getRecord(0).getSaveStringValue(Sapadmin.ext_system_key);
        if (sSAPsystem.equals(sObjsystem))
        {
          CSAPHelperClass.printDebug(" *** IsSapObject: Object Ipro Objekt --> true");
          return true;
        }
        else
        {
          CSAPHelperClass.printDebug(" *** IsSapObject: Object ist kein Ipro Objekt --> false");
          return false;
        }
      }
      else
      {
        CSAPHelperClass.printDebug(" *** IsSapObject: Object hat kein EXT Sytem --> false");
        return false;
      }

    }
    else
    {
      CSAPHelperClass.printDebug(" *** IsSapObject: Call hat kein Object --> true");
      return true;
    }

  }

  /**
   * Methode zur Erzeugung von Störstellenlisteneintägen in SAP Diese Methode
   * arbeitet die entsprechenden Eintrage
   * ("action","create"),("errorstatus","0"),("exc_table","call") in der Tabelle
   * Sapcallexchange ab, und erzeugt daraus die Störstellenlisteneintäge
   * 
   * @param Context,
   *          aktueller Context
   * @exception Exception
   */
  // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
  // SAP - Erzeugung von Störstellenlisteneintägen
  // --------------------------------------------------------------------------------
  public static void sapSSLECreate(TaskContextSystem context) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable callex = acc.getTable(Sapcallexchange.NAME);
    IDataTable call = acc.getTable(Call.NAME);
    IDataTable log = acc.getTable(Saplog.NAME);
    callex.clear();
    // Nur die neuen Records, die nicht fehlerhaft sind
    callex.qbeSetValue(Sapcallexchange.action, "create");
    callex.qbeSetValue(Sapcallexchange.errorstatus, "0");
    callex.qbeSetValue(Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
    callex.qbeSetValue(Sapcallexchange.exc_table, "call");
    callex.search();
    ConnManager oConman = new ConnManager();
    CSAPHelperClass.printDebug(" **SAPExchange:" + callex.recordCount() + " SSLE-Records to create **");

    for (int i = 0; i < callex.recordCount(); i++)
    {
      CSAPHelperClass.printDebug("  *Creating SSLE-Record  " + (i + 1) + "*");
      IDataTransaction transaction = acc.newTransaction();
      try
      {
        IDataTableRecord callexrec = callex.getRecord(i);
        call.qbeClear();
        call.qbeSetKeyValue(Call.pkey, callexrec.getSaveStringValue(Sapcallexchange.ttscallid));
        call.search();
        if (call.recordCount() == 1)
        {
          IDataTableRecord callrec = call.getRecord(0);
          if (callrec.hasNullValue(Call.sap_ssle_nr))
          {

            // TODO Nach migration Objektfrage löschen
            // Wir machen das ganze nur, wenn es ein SAP AK ist (und vorerst
            // wenn
            // es ein SAP Objekt ist
            if (isSapAk(callrec) && isSapObject(callrec))
            {
              boolean commitReached = false;
              try
              {
                // //#/#/ Hier findet die tatsächliche Aktion statt
                CExportSSLERFC.createSSLE(oConman, callrec, callexrec, transaction);
                // //#/#///////////////////////////////////////////
              }
              // catch (RecordLockedException ex)
              // {
              // // callexrec.setValue(transaction, Sapcallexchange.action,
              // Sapcallexchange.action_ENUM._writeback);
              // // callexrec.setStringValueWithTruncation(transaction,
              // Sapcallexchange.message,
              // // "SSLE Datensatz wurde erzeugt, SSLE-Nr ist noch zu schreiben
              // (Writeback)");
              // IDataTableRecord logrec = log.newRecord(transaction);
              // logrec.setStringValueWithTruncation(transaction,
              // Saplog.message,
              // "SSLE Datensatz wurde erzeugt, SSLE-Nr ist noch zu schreiben");
              // logrec.setValue(transaction, Saplog.modus, "Create");
              // logrec.setValue(transaction, Saplog.ttscallid,
              // callrec.getSaveStringValue(Call.pkey));
              // logrec.setValue(transaction, Saplog.sapssleid,
              // callrec.getSaveStringValue(Call.sap_ssle_nr));
              // CSAPHelperClass.printDebug(" *SSLE-Record Created " + (i + 1) +
              // "*");
              //
              // transaction.commit();
              // commitReached = true;
              // // return;
              // }

              catch (SapProcessingException e)
              {
                // Log-Meldung mit Stacktrace ins catalina.out schreiben
                logger.error("sapSSLECreate() failed", e);
                // Wenn eine Exception auftrat, dann Datensatz auf Error stellen
                // und Log schreiben

                callexrec.setIntValue(transaction, Sapcallexchange.errorstatus, 1);
                callexrec.setStringValueWithTruncation(transaction, Sapcallexchange.message, e.getMessage());
                IDataTableRecord logrec = log.newRecord(transaction);
                logrec.setStringValueWithTruncation(transaction, Saplog.message, e.getMessage());
                logrec.setValue(transaction, Saplog.modus, "Create");
                logrec.setValue(transaction, Saplog.ttscallid, callrec.getSaveStringValue(Call.pkey));
                logrec.setIntValue(transaction, Saplog.errorstatus, 1);
                transaction.commit();
                commitReached = true;
                // return;
              }

              // Wenn keine Exception auftrat, dann Datensatz auf Done stellen
              // und Log schreiben
              if (!commitReached)
              {
                callexrec.setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._Done);
                IDataTableRecord logrec = log.newRecord(transaction);
                logrec.setValue(transaction, Saplog.message, "SSLE Datensatz wurde erzeugt");
                logrec.setValue(transaction, Saplog.modus, "Create");
                logrec.setValue(transaction, Saplog.ttscallid, callrec.getSaveStringValue(Call.pkey));
                logrec.setValue(transaction, Saplog.sapssleid, callrec.getSaveStringValue(Call.sap_ssle_nr));
                CSAPHelperClass.printDebug("  *SSLE-Record Created " + (i + 1) + "*");
                transaction.commit();

              }
            }
            else
            {
              // Wenn es kein SAP AK war, dann Datensatz auf Done stellen und
              // Log schreiben
              callexrec.setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._Done);
              IDataTableRecord logrec = log.newRecord(transaction);
              logrec.setValue(transaction, Saplog.message, "Datensatz wurde NICHT erzeugt, AK ode Objekt ist nicht SAP relevant");
              logrec.setValue(transaction, Saplog.modus, "Create");
              logrec.setValue(transaction, Saplog.ttscallid, callrec.getSaveStringValue(Call.pkey));
              logrec.setValue(transaction, Saplog.sapssleid, callrec.getSaveStringValue(Call.sap_ssle_nr));
              CSAPHelperClass.printDebug("  *SSLE-Record No Creation needed " + (i + 1) + "*");
              transaction.commit();
            }
          }
          else
          {
            callexrec.setValue(transaction, Sapcallexchange.action, Sapcallexchange.action_ENUM._update);
            transaction.commit();
          }
        }
        else
        {
          // Log-Meldung mit Stacktrace ins catalina.out schreiben
          logger.error("sapSSLECreate() failed: Call konnte nicht angelegt werden! Ursprungsdatensatz wurde wahrscheinlich gelöscht");

          callexrec.setIntValue(transaction, Sapcallexchange.errorstatus, 1);
          callexrec.setValue(transaction, Sapcallexchange.message, "Call konnte nicht angelegt werden! Ursprungsdatensatz wurde wahrscheinlich gelöscht");
          IDataTableRecord logrec = log.newRecord(transaction);
          logrec.setValue(transaction, Saplog.message, "Call konnte nicht angelegt werden! Ursprungsdatensatz wurde wahrscheinlich gelöscht");
          logrec.setValue(transaction, Saplog.modus, "Create");
          logrec.setValue(transaction, Saplog.ttscallid, callexrec.getSaveStringValue(Sapcallexchange.ttscallid));
          logrec.setIntValue(transaction, Saplog.errorstatus, 1);
          transaction.commit();
        }
      }
      finally
      {
        transaction.close();
      }
    }
  }

  /**
   * Methode zur Änderung von Störstellenlisteneintägen oder Störmeldungen in
   * SAP. Diese Methode arbeitet die entsprechenden Eintrage
   * ("action","update"),("errorstatus","0"),("exc_table","call") in der Tabelle
   * Sapcallexchange ab, und ändert die entsprechenden Einträge in SAP
   * 
   * @param Context,
   *          aktueller Context
   * @exception Exception
   */
  // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
  // SAP - Änderung von Störstellenlisteneintägen
  // --------------------------------------------------------------------------------
  public static void sapSSLE_SMUpdate(TaskContextSystem context) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable callex = acc.getTable(Sapcallexchange.NAME);
    IDataTable call = acc.getTable(Call.NAME);
    IDataTable log = acc.getTable(Saplog.NAME);
    callex.clear();
    // Nur die "update" Records vom Typ Call , die nicht fehlerhaft sind
    callex.qbeSetValue("action", "update");
    callex.qbeSetValue("errorstatus", "0");
    callex.qbeSetValue(Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
    callex.qbeSetValue("exc_table", "call");
    callex.search();
    ConnManager oConman = new ConnManager();
    CSAPHelperClass.printDebug(" **SAPExchange:" + callex.recordCount() + " SSLE/SM-Records to update **");
    for (int i = 0; i < callex.recordCount(); i++)
    {
      IDataTransaction transaction = acc.newTransaction();
      try
      {
        IDataTableRecord callexrec = callex.getRecord(i);
        call.qbeClear();
        call.qbeSetKeyValue(Call.pkey, callexrec.getSaveStringValue(Sapcallexchange.ttscallid));
        call.search();
        if (call.recordCount() == 1)
        {
          IDataTableRecord callrec = call.getRecord(0);
          try
          {
            String sModus;
            String sMess;
            CSAPHelperClass.printDebug("  *Updating SSLE/SM-Record  " + (i + 1) + "*");
            // Feststellen ob SSLE ODER SM
            // Wenn SSLE
            if ("".equals(callrec.getSaveStringValue("sap_sm_nr")))
            {
              if (isSapAk(callrec) && isSapObject(callrec))
              {
                // Wenn keine SSLE, dann neue anlegen
                if ("".equals(callrec.getSaveStringValue(Call.sap_ssle_nr)))
                {
                  sModus = "Create";
                  sMess = "SSLE Datensatz wurde erzeugt";

                  // //#/#/ Hier findet die tatsächliche Aktion statt
                  CExportSSLERFC.createSSLE(oConman, callrec, callexrec, transaction);
                  // //#/#///////////////////////////////////////////

                  CSAPHelperClass.printDebug("  - SSLE Datensatz war nicht vorhanden und wurde daher erzeugt");
                }
                else
                {
                  // SonstStörstellenlisteneintrag ändern
                  sModus = "Update";
                  sMess = "SSLE Datensatz wurde geändert";
                  // //#/#/ Hier findet die tatsächliche Aktion statt
                  CExportSSLERFC.updateSSLE(oConman, callrec);
                  // //#/#///////////////////////////////////////////
                  CSAPHelperClass.printDebug("  - SSLE Nr: " + callrec.getSaveStringValue(Call.sap_ssle_nr));
                }
              }
              // Kein SAP AK; Also prüfen ob schon SSLE Exisitiert, wenn ja,
              // muss sie gelöscht werden
              else if (!"".equals(callrec.getSaveStringValue(Call.sap_ssle_nr)))
              {
                // SSLE Gefunden, also löschen
                sModus = "Delete";
                sMess = "SSLE Datensatz wurde in SAP storniert";
                CExportSSLERFC.deleteSSLE(oConman, callrec);
                CSAPHelperClass.printDebug("  - SSLE Nr: " + callrec.getSaveStringValue(Call.sap_ssle_nr) + " wurde in SAP storniert");
              }
              else
              {
                sModus = "Irrelevant";
                sMess = "  - SSLE Datensatz ist für SAP nicht (mehr) relevant";
                CSAPHelperClass.printDebug(sMess);
              }
            }
            else
            {
              // Dann Störmeldung ändern
              sModus = "Update";
              sMess = "SM Datensatz wurde geändert";

              // //#/#/ Hier findet die tatsächliche Aktion statt
              CExportSSLERFC.updateSM(oConman, callrec);
              // //#/#///////////////////////////////////////////
              CSAPHelperClass.printDebug("  -   SM Nr: " + callrec.getSaveStringValue(Call.sap_ssle_nr));
            }

            // Wenn keine Exception auftrat, dann Datensatz auf Done stellen
            // und Log schreiben
            callexrec.setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._Done);
            IDataTableRecord logrec = log.newRecord(transaction);
            logrec.setValue(transaction, Saplog.message, sMess);
            logrec.setValue(transaction, Saplog.modus, sModus);
            logrec.setValue(transaction, Saplog.ttscallid, callrec.getSaveStringValue(Call.pkey));
            logrec.setValue(transaction, Saplog.sapssleid, callrec.getSaveStringValue(Call.sap_ssle_nr));
            logrec.setValue(transaction, Saplog.sapsmid, callrec.getSaveStringValue(Call.sap_sm_nr));
            CSAPHelperClass.printDebug("  *SSLE-Record Updated " + (i + 1) + "*");
            transaction.commit();
          }
          catch (SapProcessingException e)
          {
            // Log-Meldung mit Stacktrace ins catalina.out schreiben
            logger.error("sapSSLE_SMUpdate() failed", e);

            // Wenn eine Exception auftrat, dann Datensatz auf Error stellen
            // und Log schreiben
            callexrec.setIntValue(transaction, Sapcallexchange.errorstatus, 1);
            callexrec.setStringValueWithTruncation(transaction, Sapcallexchange.message, e.getMessage());
            IDataTableRecord logrec = log.newRecord(transaction);
            logrec.setStringValueWithTruncation(transaction, Saplog.message, e.getMessage());
            logrec.setValue(transaction, Saplog.modus, "Update");
            logrec.setValue(transaction, Saplog.ttscallid, callrec.getSaveStringValue(Call.pkey));
            logrec.setValue(transaction, Saplog.sapssleid, callrec.getSaveStringValue(Call.sap_ssle_nr));
            logrec.setValue(transaction, Saplog.sapsmid, callrec.getSaveStringValue(Call.sap_sm_nr));
            logrec.setIntValue(transaction, Saplog.errorstatus, 1);
            transaction.commit();
          }
        }
        else
        {
          // Log-Meldung mit Stacktrace ins catalina.out schreiben
          logger.error("sapSSLE_SMUpdate() failed: Call konnte nicht geändert werden! Ursprungsdatensatz wurde wahrscheinlich gelöscht");

          callexrec.setIntValue(transaction, Sapcallexchange.errorstatus, 1);
          callexrec.setValue(transaction, Sapcallexchange.message, "Call konnte nicht geändert werden! Ursprungsdatensatz wurde wahrscheinlich gelöscht");
          IDataTableRecord logrec = log.newRecord(transaction);
          logrec.setValue(transaction, Saplog.message, "Call konnte nicht geändert werden! Ursprungsdatensatz wurde wahrscheinlich gelöscht");
          logrec.setValue(transaction, Saplog.modus, "Create");
          logrec.setValue(transaction, Saplog.ttscallid, callexrec.getSaveStringValue(Sapcallexchange.ttscallid));
          logrec.setIntValue(transaction, Saplog.errorstatus, 1);
          transaction.commit();
        }
      }
      finally
      {
        transaction.close();
      }
    }// End for
  }

  /**
   * Methode zum Import von Störstellenlisteneintägen oder Störmeldungen aus
   * SAP. Diese Methode liest die Austauschtabelle in SAP (RFC Tabelle) und
   * schreibt die Informationen in die TTS Austauschtabelle /sapcallexchange)
   * und versucht anschließend die Änderungen in die Tabelle Calls
   * zurückzuschreiben. Der Weg über die Autauschtabelle wurde gewählt um bei
   * einer Recordlock-Exception einfach den Import zu wiederholen.
   * 
   * @param Context,
   *          aktueller Context
   * @exception Exception
   */
  // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
  // SAP - Import von Störstellenlisteneintägen und Störmeldugen
  // --------------------------------------------------------------------------------
  public static void sapSSLE_SM_Import(TaskContextSystem context) throws Exception
  {
    ConnManager oConman = new ConnManager();
    // Hier holen wir die die Änderungen der Störstellenlisten
    CImportCalls_Tasks.getSAPSSLEUpdates(oConman);
    // Hier holen wir die die Änderungen der Störmeldungen
    CImportCalls_Tasks.getSAPSMUpdates(oConman);
    // Verarbeitung
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable callex = acc.getTable(Sapcallexchange.NAME);
    IDataTable call = acc.getTable(Call.NAME);
    IDataTable log = acc.getTable(Saplog.NAME);
    callex.qbeClear();
    // Nur die zu importierenden Records, die nicht fehlerhaft sind
    callex.qbeSetValue(Sapcallexchange.action, "import");
    callex.qbeSetValue(Sapcallexchange.errorstatus, "0");
    callex.qbeSetValue(Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
    callex.qbeSetValue(Sapcallexchange.exc_table, Sapcallexchange.exc_table_ENUM._call);
    callex.search();
    // Es gibt Importdatensaätze
    CSAPHelperClass.printDebug(" **SAPExchange:" + callex.recordCount() + " SSLE/SM-Records to import **");
    if (callex.recordCount() > 0)
    {
      for (int i = 0; i < callex.recordCount(); i++)
      {
        IDataTableRecord callexrec = callex.getRecord(i);
        IDataTransaction transaction = acc.newTransaction();
        IDataTransaction transaction2 = acc.newTransaction();
        CSAPHelperClass.printDebug("  *Importing SSLE/SM-Record  " + (i + 1) + "*");
        boolean commitReached = false;
        try
        {
          call.qbeClear();
          call.qbeSetKeyValue(Call.pkey, callexrec.getValue(Sapcallexchange.ttscallid));
          call.search();
          if (call.recordCount() == 1)
          {
            IDataTableRecord callrec = call.getRecord(0);
            // Prüfen of Störmeldung oder SSLE
            boolean bSM = false;
            if (!"".equals(callexrec.getSaveStringValue(Sapcallexchange.sapsmid)))
            {
              bSM = true;

              // Hier sollten wir sicherstellen, dass die SM-ID immer gesetzt
              // wird
              if (callrec.hasNullValue(Call.sap_sm_nr))
              {
                SQLDataSource caretaker = (SQLDataSource) DataSource.get("caretakerDataSource");
                Connection con = caretaker.getConnection();
                try
                {
                  Statement statemet = con.createStatement();
                  String sSql = "";
                  try
                  {
                    // Wenn Call noch nicht angenommen dann Status setzten
                    List status = Arrays.asList(new String[]
                      { "Verworfen", "AK zugewiesen", "Fehlgeroutet", "Rückruf", "Durchgestellt" });

                    if (status.contains(callrec.getSaveStringValue(Calls.callstatus)))
                    {

                      SimpleDateFormat ORA = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                      sSql = "UPDATE caretaker.calls SET sap_sm_nr='" + callexrec.getValue(Sapcallexchange.sapsmid) + "', callstatus=5, "
                          + "dateowned=to_date('" + ORA.format(new Date()) + "', 'yyyy/mm/dd hh24:mi:ss') WHERE pkey="
                          + callexrec.getValue(Sapcallexchange.ttscallid);
                    }
                    // Sonst nur Nr zurückschreiben
                    else
                    {
                      sSql = "UPDATE caretaker.calls SET sap_sm_nr='" + callexrec.getValue(Sapcallexchange.sapsmid) + "' WHERE pkey="
                          + callexrec.getValue(Sapcallexchange.ttscallid);
                    }
                    statemet.executeUpdate(sSql);

                    con.commit();
                  }
                  finally
                  {
                    statemet.close();

                  }
                }

                finally
                {
                  con.close();
                }
              }

              // Wird eine SM erzeugt,so mußder Status auf angenommen gesetzt
              // werden
              if (callrec.getSaveStringValue(Call.callstatus).equals(Call.callstatus_ENUM._AK_zugewiesen))
              {
                callrec.setValue(transaction, Call.callstatus, Call.callstatus_ENUM._Angenommen);
              }
            }
            if (callrec.hasNullValue(Call.sap_sm_nr))
            {
              callrec.setValue(transaction, Call.sap_sm_nr, callexrec.getValue(Sapcallexchange.sapsmid));
            }
            if (!"".equals(callexrec.getSaveStringValue(Sapcallexchange.imp_description)))
            {
              callrec.setValue(transaction, Call.problem, callexrec.getValue(Sapcallexchange.imp_description));
            }
            if (!"".equals(callexrec.getSaveStringValue(Sapcallexchange.imp_longtext)))
            {
              callrec.setValue(transaction, Call.problemtext, callexrec.getValue(Sapcallexchange.imp_longtext));
            }

            // Prüfen ob es Linked Records gibt
            // Object
            // Prüfen ob Techn.Platz gefüllt, dann den nehmen, sonst Equipment
            // nehmen

            IDataTable object = acc.getTable(Object.NAME);
            String sObject;
            if (!"".equals(callexrec.getSaveStringValue(Sapcallexchange.imp_techplatz)))
            {
              sObject = callexrec.getSaveStringValue(Sapcallexchange.imp_techplatz);
            }
            else
            {
              sObject = callexrec.getSaveStringValue(Sapcallexchange.imp_equipment);
            }
            // Wenn kein Objekt vorhanden
            if ("".equals(sObject))
            {
              callrec.setValue(transaction, Call.object_key, null);
            }
            // Sonst: Object suchen und verlinken
            else
            {
              object.qbeClear();
              object.qbeSetKeyValue(Object.external_id, sObject);
              object.search();
              if (object.recordCount() == 1)
              {
                IDataTableRecord objrec = object.getRecord(0);
                callrec.setLinkedRecord(transaction, objrec);
              }
              else
              {
                throw new Exception("Objekt '" + sObject + "' konnte nicht eindeutig zugewiesen werden");
              }
            }

            // AK,
            // If Schleife wird nur bei SSLE synchronisiert da nach Status
            // angenomen
            // AK Nicht mehr geändert wird
            if (!bSM)
            {
              CSAPHelperClass.printDebug(" **SAP Update der SSLE");
              IDataTable ak = acc.getTable(Callworkgroup.NAME);
              ak.qbeClear();
              ak.qbeSetKeyValue(Callworkgroup.hwg_name, callexrec.getValue(Sapcallexchange.imp_ak));
              ak.search();
              if (ak.recordCount() == 1)
              {
                IDataTableRecord akrec = ak.getRecord(0);
                callrec.setLinkedRecord(transaction, akrec);
              }
              else
              {
                throw new Exception("AK '" + callexrec.getValue(Sapcallexchange.imp_ak) + "' konnte nicht eindeutig zugewiesen werden");
              }
              // Location
              IDataTableRecord locationrec = callrec.getLinkedRecord(Location.NAME);
              locationrec.setValue(transaction, Location.description, callexrec.getSaveStringValue(Sapcallexchange.imp_location));

              // Prio setzten
              // Zu importierender Satz hat keine Prio gesetzt.
              // Regel: Keine SAP Prio --> 1-Normal in TTS
              if ("".equals(callexrec.getSaveStringValue(Sapcallexchange.imp_priority)))
              {
                // 1-Normal
                callrec.setValue(transaction, Call.priority, Call.priority_ENUM._1_Normal);
              }
              // Zu importierender Satz hat Prio gesetzt.
              // Regel: Wenn Prio höher als 1-Normal: dann nicht ändern, sonst
              // 2-Kritisch setzten
              else if (Call.priority_ENUM._1_Normal.equals(callrec.getValue(Call.priority)))
              {
                callrec.setValue(transaction, Call.priority, Call.priority_ENUM._2_Kritisch);
              }
              // Status setzten
              // TODO Statushandling überprüfen
              // Erst mal nur geschlossen und fehlgeroutet behandeln

              // /TODO erst mal prüfen ob das Ding nicht schon zu ist.
              // Zeitstempel/SapReopen
              if ("IZMAB".equals(callexrec.getSaveStringValue(Sapcallexchange.imp_callstatus)))
              {
                callrec.setValue(transaction, Call.callstatus, Call.callstatus_ENUM._Dokumentiert);
                // Da z.zT das Datumnicht von SAPübergeben wird,stzten wir datum
                // fertiggemeldet
                // auf "now"
                // Zeit Ändern!!!!!!

                // Geändert: Da In Sap Reopen Call geht
                if (!callrec.hasNullValue(Call.dateresolved))
                {
                  callrec.setValue(transaction, Call.dateresolved, "now");
                }
                if (!callrec.hasNullValue(Call.datedocumented))
                {
                  callrec.setValue(transaction, "datedocumented", "now");
                }
                // Ende Geändert: Da In Sap Reopen Call geht

                callrec.setValue(transaction, Call.sapimport, "1");
              }

              else if ("PM".equals(callexrec.getSaveStringValue(Sapcallexchange.imp_ak)))
              {
                callrec.setValue(transaction, Call.callstatus, Call.callstatus_ENUM._Fehlgeroutet);
              }
            }
            // Wenn Störmeldung
            else
            {
              CSAPHelperClass.printDebug(" **SAP Update der SM");
              // Kostenstelle
              // gibt es SAP Kostenstelle?
              if (!"".equals(callexrec.getSaveStringValue(Sapcallexchange.imp_costcenter)))
              {
                // Ja!, Kostenstelle im TTS suchen
                IDataTable costcenter = acc.getTable(Accountingcode.NAME);
                costcenter.qbeClear();
                costcenter.qbeSetKeyValue(Accountingcode.code, callexrec.getSaveStringValue(Sapcallexchange.imp_costcenter));
                costcenter.search();

                // gibt es SAP Kostenstelle im TTS?
                if (costcenter.recordCount() == 1)
                {
                  // Ja!, Kostenstelle setzen
                  callrec.setValue(transaction, Call.accountingcode_key, callexrec.getSaveStringValue(Sapcallexchange.imp_costcenter));
                }
              }
              // Status setzten
              // TODO Statushandling überprüfen
              // Erst mal nur geschlossen behandeln

              // Wird eine SSLE zu einer SM ,so mußder Status auf angenommen
              // gesetzt
              // werden
              if (callrec.getSaveStringValue(Call.callstatus).equals(Call.callstatus_ENUM._AK_zugewiesen))
              {
                callrec.setValue(transaction, Call.callstatus, Call.callstatus_ENUM._Angenommen);
              }
              // /TODO erst mal prüfen ob das Ding nicht schon zu ist.
              // Zeitstempel/SapReopen
              if ("MMAB".equals(callexrec.getSaveStringValue(Sapcallexchange.imp_callstatus)))
              {
                callrec.setValue(transaction, Call.callstatus, Call.callstatus_ENUM._Dokumentiert);
                // TODO Achim Hack: um Statusübergang zu überwinden
                // firstlevelclose setzen,Muß!!! geändert werden
                callrec.setValue(transaction, Call.firstlevelclosed, "1");
              }
            }

            transaction.commit();
            commitReached = true;

            // Wenn keine Exception auftrat, dann Datensatz auf Done stellen und
            // Log schreiben
            callexrec.setValue(transaction2, Sapcallexchange.status, Sapcallexchange.status_ENUM._Done);
            IDataTableRecord logrec = log.newRecord(transaction2);
            logrec.setValue(transaction2, Saplog.message, "SSLE/SM Datensatz wurde importiert");
            logrec.setValue(transaction2, Saplog.modus, "Import");
            logrec.setValue(transaction2, Saplog.ttscallid, callrec.getSaveStringValue(Call.pkey));
            logrec.setValue(transaction2, Saplog.sapssleid, callrec.getSaveStringValue(Call.sap_ssle_nr));
            logrec.setValue(transaction2, Saplog.sapsmid, callrec.getSaveStringValue(Call.sap_sm_nr));
            CSAPHelperClass.printDebug("  *SSLE/SM-Record updated " + (i + 1) + "*");
            transaction2.commit();
          }
          else
          {
            throw new Exception("Meldung konnte nicht eindeutig zugewiesen werden");
          }
        }
        catch (Exception e)
        {
          if (commitReached)
          {
            // hier ist was beim Schreiben des Logrecords schief gegangen ->
            // also nicht noch mal probieren
            throw e;
          }

          // ein Fehler in der Abarbeitung und nicht beim Schreiben des
          // Log-Records

          // Log-Meldung mit Stacktrace ins catalina.out schreiben
          logger.error("sapSSLE_SM_Import() failed", e);

          callexrec.setValue(transaction2, Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
          callexrec.setIntValue(transaction2, Sapcallexchange.errorstatus, 1);
          callexrec.setStringValueWithTruncation(transaction2, Sapcallexchange.message, e.getMessage());
          IDataTableRecord logrec = log.newRecord(transaction2);
          logrec.setStringValueWithTruncation(transaction2, Saplog.message, e.getMessage());
          // geändert wg. doppeltes Create: in Log erscheint Create
          // logrec.setValue(transaction2, Saplog.modus, "Create");
          logrec.setValue(transaction2, Saplog.modus, "Import");
          logrec.setValue(transaction2, Saplog.ttscallid, callexrec.getSaveStringValue(Sapcallexchange.ttscallid));
          logrec.setIntValue(transaction2, Saplog.errorstatus, 1);
          transaction2.commit();
        }
        finally
        {
          transaction.close();
          transaction2.close();
        }
        CSAPHelperClass.printDebug("  *SSLE-Record Imported " + (i + 1) + "*");
      }
    }
  }

  /**
   * Methode zur Erzeugung von Aufträgen (Hauptaufträge oder Unteraufträge) in
   * SAP Diese Methode arbeitet die entsprechenden Eintrage
   * ("action","create"),("errorstatus","0"),("exc_table","task"),
   * (type,"export") in der Tabelle Sapcallexchange ab, und erzeugt daraus die
   * Aufträge
   * 
   * @param Context,
   *          aktueller Context
   * @exception Exception
   */
  // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
  // SAP - Erzeugung von Aufträgen
  // --------------------------------------------------------------------------------
  public static void sapTaskCreate(TaskContextSystem context) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataBrowser taskbrowser = acc.getBrowser("sapTaskSortBrowser");
    IDataTable callex = acc.getTable(Sapcallexchange.NAME);
    IDataTable task = acc.getTable(Task.NAME);
    IDataTable log = acc.getTable(Saplog.NAME);

    // Nur die neuen Records, die nicht fehlerhaft sind
    callex.qbeSetValue(Sapcallexchange.action, "create");
    callex.qbeSetValue(Sapcallexchange.errorstatus, "0");
    callex.qbeSetValue(Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
    callex.qbeSetValue(Sapcallexchange.exc_table, "task");
    callex.qbeSetValue(Sapcallexchange.type, "export");
    // callex.search();
    taskbrowser.search(IRelationSet.LOCAL_NAME);
    ConnManager oConman = new ConnManager();
    CSAPHelperClass.printDebug(" **SAPExchange:" + callex.recordCount() + " Task-Records to create **");
    // for (int i = 0; i < callex.recordCount(); i++)
    for (int i = 0; i < taskbrowser.recordCount(); i++)
    {
      CSAPHelperClass.printDebug("  *Creating Task-Record  " + (i + 1) + "*");
      IDataTransaction transaction = acc.newTransaction();
      try
      {
        IDataTableRecord callexrec = taskbrowser.getRecord(i).getTableRecord();
        task.qbeClear();
        String ttstaskpkey = callexrec.getSaveStringValue(Sapcallexchange.ttstaskpkey);
        task.qbeSetKeyValue(Task.pkey, ttstaskpkey);
        task.search();
        // wenn wir nichts anderes haben, dann pkey in den Logrecord schreiben
        String ttstaskid = "pkey " + callexrec.getSaveStringValue(Sapcallexchange.ttstaskpkey);
        try
        {
          if (task.recordCount() == 1)
          {
            IDataTableRecord taskrec = task.getRecord(0);

            // lieber die taskno in den Logrecord schreiben
            ttstaskid = taskrec.getSaveStringValue(Task.taskno);
            CSAPHelperClass.printDebug("***** " + ttstaskid);
            // //#/#/ Hier findet die tatsächliche Aktion statt
            CExportTaskRFC.createTask(oConman, taskrec);
            // //#/#///////////////////////////////////////////

            // Wenn keine SapProcessingException auftrat, dann Datensatz auf
            // Done stellen und Log schreiben
            callexrec.setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._Done);
            IDataTableRecord logrec = log.newRecord(transaction);
            logrec.setValue(transaction, Saplog.message, "AUFTRAG Datensatz wurde erzeugt");
            logrec.setValue(transaction, Saplog.modus, "Create");
            logrec.setValue(transaction, Saplog.ttstaskid, ttstaskid);
            logrec.setValue(transaction, Saplog.saptaskid, taskrec.getSaveStringValue(Task.sap_auftrag));
            transaction.commit();
            CSAPHelperClass.printDebug("  *Task-Record Created " + (i + 1) + "*");
          }
          else
          {
            throw new SapProcessingException("Auftrag mit pkey " + callexrec.getSaveStringValue(Sapcallexchange.ttstaskpkey) + " konnte nicht angelegt werden!");
          }
        }
        catch (SapProcessingException e)
        {
          // Log-Meldung mit Stacktrace ins catalina.out schreiben
          logger.error("sapTaskCreate() failed", e);

          // Wenn eine Exception auftrat, dann Datensatz auf Error stellen
          // und Log schreiben
          callexrec.setIntValue(transaction, Sapcallexchange.errorstatus, 1);
          callexrec.setStringValueWithTruncation(transaction, Sapcallexchange.message, e.getMessage());
          IDataTableRecord logrec = log.newRecord(transaction);
          logrec.setStringValueWithTruncation(transaction, Saplog.message, e.getMessage());
          logrec.setValue(transaction, Saplog.modus, "Create");
          logrec.setValue(transaction, Saplog.ttstaskid, ttstaskid);
          logrec.setIntValue(transaction, Saplog.errorstatus, 1);
          transaction.commit();
          CSAPHelperClass.printDebug("  *Task-Record NOT Created " + (i + 1) + "*");
        }
      }
      finally
      {
        transaction.close();
      }
    }// End for
  }

  /**
   * Methode zur Änderung von Aufträgen in SAP. Diese Methode arbeitet die
   * entsprechenden Eintrage
   * ("action","update"),("errorstatus","0"),("exc_table","task"),
   * (type,"export") in der Tabelle Sapcallexchange ab, und ändert die
   * entsprechenden Einträge in SAP
   * 
   * @param Context,
   *          aktueller Context
   * @exception Exception
   */
  // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
  // SAP - Änderung von Störstellenlisteneintägen
  // --------------------------------------------------------------------------------
  public static void sapTaskUpdate(TaskContextSystem context) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable callex = acc.getTable(Sapcallexchange.NAME);
    IDataTable task = acc.getTable(Task.NAME);
    IDataTable log = acc.getTable(Saplog.NAME);
    callex.clear();
    // Nur die geänderten Records, die nicht fehlerhaft sind
    callex.qbeSetValue(Sapcallexchange.action, "update");
    callex.qbeSetValue(Sapcallexchange.errorstatus, "0");
    callex.qbeSetValue(Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
    callex.qbeSetValue(Sapcallexchange.exc_table, "task");
    callex.qbeSetValue(Sapcallexchange.type, "export");
    callex.search();
    ConnManager oConman = new ConnManager();
    CSAPHelperClass.printDebug(" **SAPExchange:" + callex.recordCount() + " Task-Records to update **");
    for (int i = 0; i < callex.recordCount(); i++)
    {
      IDataTransaction transaction = acc.newTransaction();
      try
      {
        IDataTableRecord callexrec = callex.getRecord(i);
        task.qbeClear();
        String ttstaskpkey = callexrec.getSaveStringValue(Sapcallexchange.ttstaskpkey);
        task.qbeSetKeyValue(Task.pkey, ttstaskpkey);
        task.search();
        // wenn wir nichts anderes haben, dann pkey in den Logrecord schreiben
        String ttstaskid = "pkey " + callexrec.getSaveStringValue(Sapcallexchange.ttstaskpkey);
        try
        {
          if (task.recordCount() == 1)
          {
            IDataTableRecord taskrec = task.getRecord(0);

            // lieber die taskno in den Logrecord schreiben
            ttstaskid = taskrec.getSaveStringValue(Task.taskno);

            CSAPHelperClass.printDebug("  *Updating Task-Record  " + (i + 1) + "*");

            // //#/#/ Hier findet die tatsächliche Aktion statt
            CExportTaskRFC.updateTask(oConman, taskrec);
            // //#/#///////////////////////////////////////////

            // Wenn keine SapProcessingException auftrat, dann Datensatz auf
            // Done stellen
            // und Log schreiben
            callexrec.setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._Done);
            IDataTableRecord logrec = log.newRecord(transaction);
            logrec.setValue(transaction, Saplog.message, "AUFTRAG Datensatz wurde geändert");
            logrec.setValue(transaction, Saplog.modus, "Update");
            logrec.setValue(transaction, Saplog.ttstaskid, ttstaskid);
            logrec.setValue(transaction, Saplog.saptaskid, taskrec.getSaveStringValue(Task.sap_auftrag));
            transaction.commit();
            CSAPHelperClass.printDebug("  *Task-Record Updated " + (i + 1) + "*");
          }
          else
          {
            throw new SapProcessingException("Auftrag mit pkey " + ttstaskpkey + " konnte nicht geändert werden!");
          }
        }
        catch (SapProcessingException e)
        {
          // Log-Meldung mit Stacktrace ins catalina.out schreiben
          logger.error("sapTaskUpdate() failed", e);

          // Wenn eine Exception auftrat, dann Datensatz auf Error stellen
          // und Log schreiben
          callexrec.setIntValue(transaction, Sapcallexchange.errorstatus, 1);
          callexrec.setStringValueWithTruncation(transaction, Sapcallexchange.message, e.getMessage());
          IDataTableRecord logrec = log.newRecord(transaction);
          logrec.setStringValueWithTruncation(transaction, Saplog.message, e.getMessage());
          logrec.setValue(transaction, Saplog.modus, "Update");
          logrec.setValue(transaction, Saplog.ttstaskid, ttstaskid);
          logrec.setIntValue(transaction, Saplog.errorstatus, 1);
          transaction.commit();
          CSAPHelperClass.printDebug("  *Task-Record NOT Updated " + (i + 1) + "*");
        }
      }
      finally
      {
        transaction.close();
      }
    }
  }

  /**
   * Methode zum Import von Aufträgen aus SAP. Diese Methode liest die
   * Austauschtabelle in SAP (RFC Tabelle) und schreibt die Informationen in die
   * TTS Austauschtabelle /sapcallexchange) und versucht anschließend die
   * Änderungen in die Tabelle Task zurückzuschreiben. Der Weg über die
   * Autauschtabelle wurde gewählt um bei einer Recordlock-Exception einfach den
   * Import zu wiederholen.
   * 
   * @param Context,
   *          aktueller Context
   * @exception Exception
   */
  // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
  // SAP - Import von Aufträgen
  // --------------------------------------------------------------------------------
  public static void sapTaskImport(TaskContextSystem context) throws Exception
  {
    ConnManager oConman = new ConnManager();
    // //#/#/ Holen der Austausch-Auftragstabelle aus SAP und schreiben in
    // unsere Austauschtabelle
    CImportCalls_Tasks.getSAPOrders(oConman);
    // //#/#///////////////////////////////////////////

    // Verarbeitung
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable callex = acc.getTable(Sapcallexchange.NAME);
    IDataTable task = acc.getTable(Task.NAME);
    IDataTable log = acc.getTable(Saplog.NAME);
    // Nur die zu importierenden Records, die nicht fehlerhaft sind
    // Evt sowohl neu als auch update
    callex.qbeSetValue(Sapcallexchange.action, "create|import");
    callex.qbeSetValue(Sapcallexchange.type, Sapcallexchange.type_ENUM._import);
    callex.qbeSetKeyValue("errorstatus", "0");
    callex.qbeSetValue(Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
    callex.qbeSetValue("exc_table", "task");
    callex.search();
    // Es gibt Importdatensätze
    CSAPHelperClass.printDebug(" **SAPExchange:" + callex.recordCount() + " Order-UpdateRecords to import **");
      for (int i = 0; i < callex.recordCount(); i++)
      {
        IDataTableRecord callexrec = callex.getRecord(i);
        IDataTransaction transaction = acc.newTransaction();
        IDataTransaction transaction2 = acc.newTransaction();
        CSAPHelperClass.printDebug("  *Importing Order-Record  " + (i + 1) + "*");
        boolean commitReached = false;
        try
        {
          // Neu oder Update ermitteln
          String toProcess = "error";
          if (callexrec.getValue(Sapcallexchange.action).equals("create"))
          {
            // wenn es eine neuer datensatz ist können wir weitermachen
            toProcess = "new";
          }
          else
          {
            // Es ist kein neuer Datensatz, also suchen wir einen bestehenden
            // ...
            task.qbeClear();
            task.qbeSetKeyValue(Task.sap_auftrag, callexrec.getValue(Sapcallexchange.saptaskid));
            task.search();
            if (task.recordCount() == 1)
            {
              // ... wir haben einen Satz gefunden und können weitermachen
              toProcess = "update";
            }
          }
         

          if (!toProcess.equals("error"))
          {
            IDataTableRecord taskrec;
            if (toProcess.equals("new"))
            {
              // wir ezeugen einen neuen Satz
              taskrec = task.newRecord(transaction);
              // und setzen die Initialwerte
              // Call
              taskrec.setValue(transaction, Task.calltask, callexrec.getSaveStringValue(Sapcallexchange.ttscallid));
              // SM Nr in Call zurückschreiben
              IDataTableRecord callrec = taskrec.getLinkedRecord(Call.NAME);
              // if
              // (!"".equals(callexrec.getSaveStringValue(Sapcallexchange.sapsmid)))
              // {
              // callrec.setValue(transaction, Call.sap_sm_nr,
              // callexrec.getValue(Sapcallexchange.sapsmid));
              // }

              SQLDataSource caretaker = (SQLDataSource) DataSource.get("caretakerDataSource");
              Connection con = caretaker.getConnection();
              try
              {
                Statement statemet = con.createStatement();
                try
                {
                  String sPkey = callrec.getSaveStringValue(Calls.pkey);
                  String sSql = "UPDATE caretaker.calls SET sap_sm_nr='" + callexrec.getValue(Sapcallexchange.sapsmid) + "' WHERE pkey=" + sPkey;
                  statemet.executeUpdate(sSql);
                  con.commit();
                }
                finally
                {
                  statemet.close();
                }
              }

              finally
              {
                con.close();
              }

              // Ext System setzen
              IDataTable sapadmin = acc.getTable(Sapadmin.NAME);
              sapadmin.qbeClear();
              sapadmin.qbeSetValue(Sapadmin.active, "1");
              sapadmin.search();
              if (sapadmin.recordCount() != 1)
              {
                throw new Exception("Aktiver Verbindungsdatensatz zu SAP konnte nicht ermittelt werden!");
              }

              taskrec.setValue(transaction, Task.ext_system_key, sapadmin.getRecord(0).getSaveStringValue(Sapadmin.ext_systemsaptask_key));

            }
            else
            {
              
              // wir nehmen Änderungen am bestenden Satz vor;
              taskrec = task.getRecord(0);
            }
            if (!"".equals(callexrec.getSaveStringValue("imp_description")))
            {
              taskrec.setValue(transaction, Task.summary, callexrec.getValue("imp_description"));
            }
            else
            {
              if ("".equals(taskrec.getSaveStringValue(Task.summary)))
              {
                taskrec.setValue(transaction, Task.summary, "Es wurde kein Text aus SAP übergeben");
              }
            }
            System.out.println(callexrec.getValue(Sapcallexchange.saptaskid));
            taskrec.setValue(transaction, Task.description, callexrec.getValue("imp_longtext"));
            taskrec.setValue(transaction, Task.sap_auftrag, callexrec.getValue(Sapcallexchange.saptaskid));
            
            // Zeiten
            String gesAufwand = callexrec.getSaveStringValue(Sapcallexchange.imp_t_totaltimespent);

            String aufwandEH = callexrec.getSaveStringValue(Sapcallexchange.imp_t_tottimesp_uom);
            if ("H".equals(aufwandEH))
            {
              String[] results = gesAufwand.split("\\.");
              for (int j = 0; j < results.length; j++)
              {
                CSAPHelperClass.printDebug(results[j]);
              }
              if (results.length != 2)
              {
                throw new Exception("Wert für Rückmeldezeit ungültig. Format nicht gleich 00.0xx");
              }

              String h = results[0];
              String m = results[1];
              BigDecimal hundred = new BigDecimal("100");
              BigDecimal sixty = new BigDecimal("60");
              BigDecimal tMin = new BigDecimal(m);
              tMin = tMin.divide(hundred, 0);
              tMin = tMin.divide(hundred, 4, 0);
              tMin = tMin.multiply(sixty);
              taskrec.setValue(transaction, Task.totaltimespent_h, h);
              taskrec.setValue(transaction, Task.totaltimespent_m, tMin.toBigInteger().toString());
            }
            else if ("MIN".equals(aufwandEH))
            {
              String[] results = gesAufwand.split("\\.");
              for (int j = 0; j < results.length; j++)
              {
                CSAPHelperClass.printDebug(results[j]);
              }
              if (results.length != 2)
              {
                throw new Exception("Wert für Rückmeldezeit ungültig. Format nicht gleich 00.0xx");
              }

              String h = results[0];
              String m = results[1];
              BigDecimal sHour = new BigDecimal("60");
              BigDecimal gA = new BigDecimal(h);
              gA = gA.divide(sHour, 0, 1);
              String sAufwandStd = gA.toString();
              gA = gA.multiply(sHour);
              BigDecimal gAMin = new BigDecimal(gesAufwand);
              gAMin = gAMin.subtract(gA);
              if (!"000".equals(m))
              {
                gAMin = gAMin.add(new BigDecimal("1"));
              }
              gAMin = gAMin.divide(new BigDecimal("1"), 0, 0);
              String sAufwandMin = gAMin.toString();

              taskrec.setValue(transaction, Task.totaltimespent_h, sAufwandStd);
              taskrec.setValue(transaction, Task.totaltimespent_m, sAufwandMin);
            }
            else
            {
              // TODO Behandlung bei anderer Einheit
              throw new Exception("Einheit für Rückmeldezeit nicht gültig");
            }
            // Arbeitsbeginn und Ende setzen NUR wenn Staus geschlossen
            // Da Diese Informationen nicht in SAP vorhanden sind
            // setzen wir diese Zeiten von Hand
            if ("TABG".equals(callexrec.getSaveStringValue(Sapcallexchange.imp_callstatus)))
            {
              // Wenn Arbeitsbeginn noch nicht gesesetzt ist
              if (taskrec.hasNullValue(Task.taskstart))
              {
                // versuchen wir daterequested zu setzen
                if (!taskrec.hasNullValue(Task.daterequested))
                {
                  taskrec.setValue(transaction, Task.taskstart, taskrec.getValue(Task.daterequested));
                }
                // sonst setzten wir "now"
                else
                {
                  taskrec.setValue(transaction, Task.taskstart, "now");
                }
              }
              // Wenn Arbeitsende noch nicht gesesetzt ist
              if (taskrec.hasNullValue(Task.taskdone))
              {
                // versuchen wir dateresolved zu setzen
                if (!taskrec.hasNullValue(Task.dateresolved))
                {
                  taskrec.setValue(transaction, Task.taskdone, taskrec.getValue(Task.dateresolved));
                }
                // sonst setzten wir "now"
                else
                {
                  taskrec.setValue(transaction, Task.taskdone, "now");
                  taskrec.setValue(transaction, Task.dateresolved, "now");
                }
              }
            }
            // ... Linked Records
            // Object
            // Prüfen ob Techn.Platz gefüllt, dann den nehmen, sonst Equipment
            IDataTable object = acc.getTable(Taskobject.NAME);
            String sObject = "";
            if (!"".equals(callexrec.getSaveStringValue("imp_techplatz")))
            {
              sObject = callexrec.getSaveStringValue("imp_techplatz");
            }
            else
            {
              sObject = callexrec.getSaveStringValue("imp_equipment");
            }
            // Wenn kein Objekt vorhanden
            if ("".equals(sObject))
            {
              taskrec.setValue(transaction, Task.object_key, null);
            }
            else
            {
              // Objekt vorhanden: wir setzen das Objekt
              object.qbeClear();
              object.qbeSetKeyValue(Object.external_id, sObject);
              object.search();
              if (object.recordCount() == 1)
              {
                IDataTableRecord objrec = object.getRecord(0);
                taskrec.setLinkedRecord(transaction, objrec);
              }
              else
              {
                // Das Objekt ist nicht vorhanden
                throw new Exception("Objekt '" + sObject + "' konnte nicht eindeutig zugewiesen werden");
              }
            }
            // Status setzten
            String sImpStatus = callexrec.getSaveStringValue(Sapcallexchange.imp_callstatus);
            if (!"".equals(sImpStatus))
            {
              // SAP hat einen Status übergeben
              // Wir lesen aus dem externen System den Endstatus zur ermittling
              // der relevanten
              // Statusübergange
              String sEndStatus = "";
              if (!taskrec.hasLinkedRecord(Ext_system.NAME))
              {
                throw new Exception("Auftrag hat kein Abrechnungssystem");
              }
              else
              {
                IDataTableRecord extsys = taskrec.getLinkedRecord(Ext_system.NAME);
                sEndStatus = extsys.getSaveStringValue(Ext_system.endstatus);
              }
              List lValStat = Arrays.asList(new String[]
                { Task.taskstatus_ENUM._Dokumentiert, Task.taskstatus_ENUM._Abgerechnet });

              if ("FERT".equals(sImpStatus))
              {
                taskrec.setValue(transaction, Task.taskstatus, Task.taskstatus_ENUM._Fertig_gemeldet);
              }
              else if ("TABG".equals(sImpStatus) && lValStat.contains(sEndStatus))
              {
                // oder geschlossen
                taskrec.setValue(transaction, Task.taskstatus, Task.taskstatus_ENUM._Dokumentiert);
              }
              else if ("ABGS".equals(sImpStatus) && Task.taskstatus_ENUM._Abgerechnet.equals(sEndStatus))
              {
                taskrec.setValue(transaction, Task.taskstatus, Task.taskstatus_ENUM._Abgerechnet);
              }
            }
            // HWG Setzen
            IDataTable hwg = acc.getTable(Taskworkgroup.NAME);

            String sHWG = "";
            if (!"".equals(callexrec.getSaveStringValue(Sapcallexchange.imp_t_hwg)))
            {
              sHWG = callexrec.getSaveStringValue(Sapcallexchange.imp_t_hwg);
              hwg.qbeClear();
              hwg.qbeSetKeyValue(Taskworkgroup.hwg_name, sHWG);
              hwg.search();
              if (hwg.recordCount() == 1)
              {
                // Prüfen, ob HWG zum AK gehört
                // try
                // {
                IDataTableRecord hwgrec = hwg.getRecord(0);
                IDataAccessor acc1 = context.getDataAccessor();
                IDataTable call = acc1.getTable(Call.NAME);
                call.qbeClear();
                call.qbeSetKeyValue(Call.pkey, taskrec.getValue(Task.calltask));
                call.search();
                if (call.recordCount() == 1)
                {
                  IDataTableRecord callRec = call.getRecord(0);
                  String sAK = callRec.getSaveStringValue(Call.workgroupcall);
                  IDataTable wg_hwg = acc1.getTable(Workgrouphwg.NAME);
                  wg_hwg.qbeSetValue(Workgrouphwg.hwg_key, hwgrec.getValue(Workgroup.pkey));
                  wg_hwg.qbeSetValue(Workgrouphwg.workgroup_key, sAK);
                  wg_hwg.search();

                  if (wg_hwg.recordCount() == 0)
                  {
                    throw new Exception("Arbeitsgruppe des Auftrags gehört nicht zu AK der Meldung");
                  }
                }
                else
                {
                  throw new Exception("Call zu Auftrag konnte nicht ermittelt werden");
                }
                // }
                // catch (Exception e0)
                // {
                // throw new UserException(e0.getMessage());
                // }
                taskrec.setLinkedRecord(transaction, hwg.getRecord(0));
              }
              else
              {
                // Keine HWG gefunden
                throw new Exception("Handwerkergruppe '" + sHWG + "' konnte nicht ermittelt werden");
              }
            }
            // /Schreiben
            transaction.commit();
            commitReached = true;

            // / Quittieren
            callexrec.setValue(transaction2, Sapcallexchange.status, "Done");
            // Log erzeugen
            IDataTableRecord logrec = log.newRecord(transaction2);
            logrec.setValue(transaction2, Saplog.message, "Aufrag-Datensatz wurde importiert - " + toProcess);
            logrec.setValue(transaction2, Saplog.modus, "Import");
            logrec.setValue(transaction2, Saplog.ttstaskid, taskrec.getSaveStringValue(Task.taskno));
            logrec.setValue(transaction2, Saplog.saptaskid, taskrec.getSaveStringValue(Task.sap_auftrag));
            logrec.setValue(transaction2, Saplog.ttscallid, taskrec.getSaveStringValue(Task.calltask));

            transaction2.commit();
          }
          else
          {
            throw new Exception("Auftrag konnte nicht eindeutig zugewiesen werden");
          }
        }
        catch (Exception e)
        {
          if (commitReached)
          {
            // hier ist was beim Schreiben des Logrecords schief gegangen ->
            // also nicht noch mal probieren
            throw e;
          }

          // ein Fehler in der Abarbeitung und nicht beim Schreiben des
          // Log-Records

          // Log-Meldung mit Stacktrace ins catalina.out schreiben
          logger.error("sapTaskImport() failed", e);

          callexrec.setIntValue(transaction2, Sapcallexchange.errorstatus, 1);
          callexrec.setStringValueWithTruncation(transaction2, Sapcallexchange.message, e.getMessage());
          callexrec.setValue(transaction2, Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
          IDataTableRecord logrec = log.newRecord(transaction2);
          logrec.setStringValueWithTruncation(transaction2, Saplog.message, e.getMessage());
          logrec.setIntValue(transaction2, Saplog.errorstatus, 1);
          logrec.setValue(transaction2, Saplog.modus, "Import");
          logrec.setValue(transaction2, Saplog.ttstaskid, callexrec.getSaveStringValue(Sapcallexchange.ttstaskno));
          logrec.setValue(transaction2, Saplog.saptaskid, callexrec.getSaveStringValue(Sapcallexchange.saptaskid));
          logrec.setValue(transaction2, Saplog.ttscallid, callexrec.getSaveStringValue(Sapcallexchange.ttscallid));
          transaction2.commit();
        }
        finally
        {
          transaction.close();
          transaction2.close();
        }
        CSAPHelperClass.printDebug("  *Order-Record Imported " + (i + 1) + "*");
      }
    
  }

  /**
   * Methode zum Schließen von Aufträgen (Hauptaufträge oder Unteraufträge) in
   * SAP Diese Methode arbeitet die entsprechenden Eintrage
   * ("action","close"),("errorstatus","0"),("exc_table","task"),
   * (type,"export") in der Tabelle Sapcallexchange ab, und schließt daraus die
   * Aufträge
   * 
   * @param Context,
   *          aktueller Context
   * @exception Exception
   */
  // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
  // SAP - Schließen von Aufträgen
  // --------------------------------------------------------------------------------
  public static void sapTaskClose(TaskContextSystem context) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable callex = acc.getTable(Sapcallexchange.NAME);
    IDataTable task = acc.getTable(Task.NAME);
    IDataTable log = acc.getTable(Saplog.NAME);
    callex.clear();
    // Nur die neuen Records, die nicht fehlerhaft sind
    callex.qbeSetValue(Sapcallexchange.action, "close");
    callex.qbeSetValue(Sapcallexchange.errorstatus, "0");
    callex.qbeSetValue(Sapcallexchange.exc_table, "task");
    callex.qbeSetValue(Sapcallexchange.type, "export");
    callex.qbeSetValue(Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
    // Hack! Da nach Dokumentieren noch Änderungen gemacht werden, gibt es
    // Probleme in der Schnittstelle zu SAP
    // Deshalb helfen wir uns, indem wir nur Calls Schließen, bei denen die
    // Anweisung zum Schließen mindestens 5 Minuten alt ist
    callex.qbeSetValue(Sapcallexchange.datecreated, "<now -5min");
    // Ende Hack
    callex.search();
    ConnManager oConman = new ConnManager();
    CSAPHelperClass.printDebug(" **SAPExchange:" + callex.recordCount() + " Task-Records to close **");
    for (int i = 0; i < callex.recordCount(); i++)
    {
      CSAPHelperClass.printDebug("  *Closing Task-Record  " + (i + 1) + "*");
      IDataTransaction transaction = acc.newTransaction();
      try
      {
        IDataTableRecord callexrec = callex.getRecord(i);
        task.qbeClear();
        task.qbeSetKeyValue(Task.pkey, callexrec.getSaveStringValue(Sapcallexchange.ttstaskpkey));
        task.search();
        // wenn wir nichts anderes haben, dann pkey in den Logrecord schreiben
        String ttstaskid = "pkey " + callexrec.getSaveStringValue(Sapcallexchange.ttstaskpkey);
        String saptaskid = callexrec.getSaveStringValue(Sapcallexchange.saptaskid);
        try
        {
          if (task.recordCount() == 1)
          {
            IDataTableRecord taskrec = task.getRecord(0);

            // lieber die taskno in den Logrecord schreiben
            ttstaskid = taskrec.getSaveStringValue(Task.taskno);
            saptaskid = taskrec.getSaveStringValue(Task.sap_auftrag);

            // //#/#/ Hier findet die tatsächliche Aktion statt
            CExportTaskRFC.closeTask(oConman, taskrec);
            // //#/#///////////////////////////////////////////

            // Remove + Log
            // callexrec.delete(transaction);
            callexrec.setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._Done);
            IDataTableRecord logrec = log.newRecord(transaction);
            logrec.setValue(transaction, Saplog.message, "Datensatz wurde geschlossen");
            logrec.setValue(transaction, Saplog.modus, "Close");
            logrec.setValue(transaction, Saplog.ttstaskid, ttstaskid);
            logrec.setValue(transaction, Saplog.saptaskid, saptaskid);
            transaction.commit();
            CSAPHelperClass.printDebug("  *Task-Record Closed " + (i + 1) + "*");
          }
          else
          {
            throw new SapProcessingException("Auftrag mit pkey " + callexrec.getSaveStringValue(Sapcallexchange.ttstaskpkey)
                + " konnte nicht geschlossen werden!");
          }
        }
        catch (SapProcessingException e)
        {
          // Log-Meldung mit Stacktrace ins catalina.out schreiben
          logger.error("sapTaskClose() failed", e);

          callexrec.setIntValue(transaction, Sapcallexchange.errorstatus, 1);
          callexrec.setStringValueWithTruncation(transaction, Sapcallexchange.message, e.getMessage());
          IDataTableRecord logrec = log.newRecord(transaction);
          logrec.setStringValueWithTruncation(transaction, Saplog.message, e.getMessage());
          logrec.setValue(transaction, Saplog.modus, "Close");
          logrec.setValue(transaction, Saplog.ttstaskid, ttstaskid);
          logrec.setValue(transaction, Saplog.saptaskid, saptaskid);
          logrec.setIntValue(transaction, "errorstatus", 1);
          transaction.commit();
          CSAPHelperClass.printDebug("  *Task-Record NOT Closed " + (i + 1) + "*");
        }
      }
      finally
      {
        transaction.close();
      }
    }
  }

  /**
   * Methode zur Schießen von Störstellenlisteneintägen in SAP Diese Methode
   * arbeitet die entsprechenden Eintrage
   * ("action","close"),("errorstatus","0"),("exc_table","call") in der Tabelle
   * Sapcallexchange ab, und schießt daraus die Störstellenlisteneintäge
   * 
   * @param Context,
   *          aktueller Context
   * @exception Exception
   */
  // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
  // SAP - Schießen von Störstellenlisteneintägen
  // --------------------------------------------------------------------------------
  public static void sapCallClose(TaskContextSystem context) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable callex = acc.getTable(Sapcallexchange.NAME);
    IDataTable call = acc.getTable(Call.NAME);
    IDataTable log = acc.getTable(Saplog.NAME);
    callex.clear();
    // Nur die geänderten Records, die nicht fehlerhaft sind
    callex.qbeSetValue(Sapcallexchange.action, "close");
    callex.qbeSetValue(Sapcallexchange.errorstatus, "0");
    callex.qbeSetValue(Sapcallexchange.exc_table, "call");
    // Hack! Da Die FilescanSchnittstelle nach dem Schießen einer Meldung noch
    // 2 mal Änderungen macht (???) gibt es Probleme in der Schnittstelle zu SAP
    // Deshalb helfen wir uns, indem wir nur Calls Schließen, bei denen die
    // Anweisung zum Schließen mindestens 5 Minuten alt ist
    callex.qbeSetValue(Sapcallexchange.datecreated, "<now -5min");
    // Ende Hack
    callex.search();
    ConnManager oConman = new ConnManager();
    CSAPHelperClass.printDebug(" **SAPExchange:" + callex.recordCount() + " SSLE-Records to close **");
    for (int i = 0; i < callex.recordCount(); i++)
    {
      IDataTransaction transaction = acc.newTransaction();
      try
      {
        IDataTableRecord callexrec = callex.getRecord(i);
        call.qbeClear();
        call.qbeSetKeyValue(Call.pkey, callexrec.getSaveStringValue(Sapcallexchange.ttscallid));
        call.search();
        String sapssleid = callexrec.getSaveStringValue(Sapcallexchange.sapssleid);
        String sMess = "";
        try
        {
          if (call.recordCount() == 1)
          {
            IDataTableRecord callrec = call.getRecord(0);

            // TODO: vermutlich unnötig, da wir diese auch im Exchangerecord
            // haben
            sapssleid = callrec.getSaveStringValue(Call.sap_ssle_nr);

            CSAPHelperClass.printDebug("  *Closing SSLE-Record  " + (i + 1) + "*");
            if (callrec.hasNullValue(Call.sap_sm_nr)) //
            {

              if (!callrec.hasNullValue(Call.sap_ssle_nr))
              {
                // Es ist eine SSLE
                // //#/#/ Hier findet die tatsächliche Aktion statt
                CExportSSLERFC.closeSSLE(oConman, callrec);
                sMess = "SSLE Datensatz wurde geschlossen";
                // //#/#///////////////////////////////////////////
              }
              else
              {
                sMess = "Datensatz ist für SAP irrelevant";
              }
            }
            else
            {
              // Es ist eine Störmeldung

              // //#/#/ Hier findet die tatsächliche Aktion statt
              CExportSSLERFC.closeSM(oConman, callrec);
              sMess = "SM Datensatz wurde geschlossen";
              // //#/#///////////////////////////////////////////
            }

            // Remove + Log
            // /callexrec.delete(transaction);
            callexrec.setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._Done);
            IDataTableRecord logrec = log.newRecord(transaction);
            logrec.setValue(transaction, Saplog.message, sMess);
            logrec.setValue(transaction, Saplog.modus, "Close");
            logrec.setValue(transaction, Saplog.ttscallid, callrec.getSaveStringValue(Call.pkey));
            logrec.setValue(transaction, Saplog.sapssleid, sapssleid);
            CSAPHelperClass.printDebug("  *Record Closed or not relevant" + (i + 1) + "*");
            transaction.commit();
          }
          else
          {
            throw new SapProcessingException("Meldung mit pkey " + callexrec.getSaveStringValue(Sapcallexchange.ttscallid)
                + " konnte nicht geschlossen werden!");
          }
        }
        catch (SapProcessingException e)
        {
          // Log-Meldung mit Stacktrace ins catalina.out schreiben
          logger.error("sapCallClose() failed", e);

          callexrec.setIntValue(transaction, Sapcallexchange.errorstatus, 1);
          callexrec.setStringValueWithTruncation(transaction, Sapcallexchange.message, e.getMessage());
          IDataTableRecord logrec = log.newRecord(transaction);
          logrec.setStringValueWithTruncation(transaction, Saplog.message, e.getMessage());
          logrec.setValue(transaction, Saplog.modus, "Close");
          logrec.setValue(transaction, Saplog.ttscallid, callexrec.getSaveStringValue(Sapcallexchange.ttscallid));
          logrec.setIntValue(transaction, Saplog.errorstatus, 1);
          logrec.setValue(transaction, Saplog.sapssleid, sapssleid);
          transaction.commit();
        }
      }
      finally
      {
        transaction.close();
      }
    }
  }

  /**
   * Methode zur Löschen von Störstellenlisteneintägen in SAP
   * Störstellenlisteneinträge werden nur während der Migrationsphase gelöscht,
   * da aufgrund wechselnder AK (SAP AK - Kein SAP AK) der SSLE überflüssig
   * werden kann Diese Methode arbeitet die entsprechenden Eintrage
   * ("action","delete"),("errorstatus","0"),("exc_table","call") in der Tabelle
   * Sapcallexchange ab, und löscht daraus die Störstellenlisteneintäge
   * 
   * @param Context,
   *          aktueller Context
   * @exception Exception
   */
  // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
  // SAP - Schießen von Störstellenlisteneintägen
  // --------------------------------------------------------------------------------
  public static void sapCallDelete(TaskContextSystem context) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable callex = acc.getTable(Sapcallexchange.NAME);
    IDataTable call = acc.getTable(Call.NAME);
    IDataTable log = acc.getTable(Saplog.NAME);
    callex.clear();
    // Nur die geänderten Records, die nicht fehlerhaft sind
    callex.qbeSetValue(Sapcallexchange.action, "delete");
    callex.qbeSetValue(Sapcallexchange.errorstatus, "0");
    callex.qbeSetValue(Sapcallexchange.exc_table, "call");
    callex.search();
    ConnManager oConman = new ConnManager();
    CSAPHelperClass.printDebug(" **SAPExchange:" + callex.recordCount() + " SSLE-Records to delete **");
    for (int i = 0; i < callex.recordCount(); i++)
    {
      IDataTransaction transaction = acc.newTransaction();
      try
      {
        IDataTableRecord callexrec = callex.getRecord(i);
        call.qbeClear();
        call.qbeSetKeyValue(Call.pkey, callexrec.getSaveStringValue(Sapcallexchange.ttscallid));
        call.search();
        String sapssleid = callexrec.getSaveStringValue(Sapcallexchange.sapssleid);
        try
        {
          if (call.recordCount() == 1)
          {
            IDataTableRecord callrec = call.getRecord(0);

            // TODO: vermutlich unnötig, da wir diese auch im Exchangerecord
            // haben
            sapssleid = callrec.getSaveStringValue(Call.sap_ssle_nr);

            CSAPHelperClass.printDebug("  *Deleting SSLE-Record  " + (i + 1) + "*");

            // //#/#/ Hier findet die tatsächliche Aktion statt
            CExportSSLERFC.deleteSSLE(oConman, callrec);
            // //#/#///////////////////////////////////////////

            // Remove + Log

            // SSLE Nr in Call Löschen
            IDataTransaction transaction2 = acc.newTransaction();
            callrec.setValue(transaction2, Call.sap_ssle_nr, "");
            transaction2.commit();
            callexrec.setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._Done);
            IDataTableRecord logrec = log.newRecord(transaction);
            logrec.setValue(transaction, Saplog.message, "Datensatz wurde in SAP storniert");
            logrec.setValue(transaction, Saplog.modus, "Delete");
            logrec.setValue(transaction, Saplog.ttscallid, callrec.getSaveStringValue(Call.pkey));
            logrec.setValue(transaction, Saplog.sapssleid, sapssleid);
            CSAPHelperClass.printDebug("  *SSLE-Record deleted " + (i + 1) + "*");
            transaction.commit();
          }
          else
          {
            throw new Exception("Meldung mit pkey " + callexrec.getSaveStringValue(Sapcallexchange.ttscallid) + " konnte nicht gelöscht werden!");
          }
        }
        catch (SapProcessingException e)
        {
          // Log-Meldung mit Stacktrace ins catalina.out schreiben
          logger.error("sapCallDelete() failed", e);

          callexrec.setIntValue(transaction, Sapcallexchange.errorstatus, 1);
          callexrec.setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
          callexrec.setStringValueWithTruncation(transaction, Sapcallexchange.message, e.getMessage());
          IDataTableRecord logrec = log.newRecord(transaction);
          logrec.setStringValueWithTruncation(transaction, "message", e.getMessage());
          logrec.setValue(transaction, Saplog.modus, "Delete");
          logrec.setValue(transaction, Saplog.ttscallid, callexrec.getValue(Sapcallexchange.ttscallid));
          logrec.setValue(transaction, Saplog.sapssleid, sapssleid);
          logrec.setIntValue(transaction, Saplog.errorstatus, 1);
          transaction.commit();
        }
      }
      finally
      {
        transaction.close();
      }
    }
  }

  // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
  // SAP - Störstellenlisteneintrag-Nr in Call schreiben, wenn Call bei
  // Erstellung gesperrt war
  // --------------------------------------------------------------------------------
  public static void sapCallWriteBack(TaskContextSystem context) throws Exception
  {
    String comm2recached = "";
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable callex = acc.getTable(Sapcallexchange.NAME);
    IDataTable call = acc.getTable(Calls.NAME);
    IDataTable log = acc.getTable(Saplog.NAME);
    callex.clear();
    // Nur die writeback Records
    callex.qbeSetValue(Sapcallexchange.action, "writeback");
    callex.qbeSetValue(Sapcallexchange.exc_table, "call");
    callex.search();
    ConnManager oConman = new ConnManager();
    CSAPHelperClass.printDebug(" **SAPExchange:" + callex.recordCount() + " SSLE-Records to writeback **");
    for (int i = 0; i < callex.recordCount(); i++)
    {
      IDataTransaction transaction = acc.newTransaction();
      try
      {
        IDataTableRecord callexrec = callex.getRecord(i);
        call.qbeClear();
        call.qbeSetKeyValue(Call.pkey, callexrec.getSaveStringValue(Sapcallexchange.ttscallid));
        call.search();
        String sapssleid = callexrec.getSaveStringValue(Sapcallexchange.sapssleid);
        try
        {
          if (call.recordCount() == 1)
          {
            comm2recached = "NO";
            IDataTableRecord callrec = call.getRecord(0);
            CSAPHelperClass.printDebug("  *Writingback SSLE-Record  " + (i + 1) + "*");
            // SSLE Nr in Call Schreiben
            IDataTransaction transaction2 = acc.newTransaction();
            callrec.setValue(transaction2, Call.sap_ssle_nr, callexrec.getSaveStringValue(Sapcallexchange.sapssleid));
            transaction2.commit();
            comm2recached = "Yes";
            callexrec.setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._Done);
            IDataTableRecord logrec = log.newRecord(transaction);
            logrec.setValue(transaction, Saplog.message, "SSLE-Nr wurde zrückgeschrieben");
            logrec.setValue(transaction, Saplog.modus, "Writeback");
            logrec.setValue(transaction, Saplog.ttscallid, callrec.getSaveStringValue(Call.pkey));
            logrec.setValue(transaction, Saplog.sapssleid, sapssleid);
            CSAPHelperClass.printDebug("  *SSLE-Record written back " + (i + 1) + "*");
            transaction.commit();
          }
          else
          {
            throw new Exception("Meldung mit pkey " + callexrec.getSaveStringValue(Sapcallexchange.ttscallid) + " konnte nicht gefunden werden!");
          }
        }
        catch (Exception e)
        {
          // Log-Meldung mit Stacktrace ins catalina.out schreiben
          logger.error("sapCallWriteBack() failed", e);
          CSAPHelperClass.printDebug("  *WriteBackError");
          callexrec.setIntValue(transaction, Sapcallexchange.errorstatus, 1);
          callexrec.setValue(transaction, Sapcallexchange.status, Sapcallexchange.status_ENUM._ToDo);
          callexrec.setStringValueWithTruncation(transaction, Sapcallexchange.message, e.getMessage());
          // IDataTableRecord logrec = log.newRecord(transaction);
          // logrec.setStringValueWithTruncation(transaction, "message",
          // e.getMessage());
          // logrec.setValue(transaction, Saplog.modus, "");
          // logrec.setValue(transaction, Saplog.ttscallid,
          // callexrec.getValue(Sapcallexchange.ttscallid));
          // logrec.setValue(transaction, Saplog.sapssleid, sapssleid);
          // logrec.setIntValue(transaction, Saplog.errorstatus, 1);
          transaction.commit();
        }
      }
      finally
      {
        transaction.close();
      }
    }// EndFor
  }
}
