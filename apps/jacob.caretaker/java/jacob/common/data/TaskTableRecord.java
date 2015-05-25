/*
 * Created on 09.08.2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package jacob.common.data;

import jacob.common.Task;
import jacob.common.Util;
import jacob.common.Yan;
import jacob.common.sap.CSAPHelperClass;
import jacob.common.sap.CWriteSAPExchange;
import jacob.event.data.CallTableRecord;
import jacob.exception.BusinessException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public abstract class TaskTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: TaskTableRecord.java,v 1.26 2008/07/24 13:15:02 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.26 $";

  public static final String STATUS = "taskstatus";

  // die unterschiedlichen Auftragsstatus
  public static final String NEU = "Neu";
  public static final String ANGELEGT = "Angelegt";
  public static final String FREIGEGEBEN = "Freigegeben";
  public static final String STORNIERT = "Storniert";
  public static final String IN_ARBEIT = "In Arbeit";
  public static final String FERTIG_GEMELDET = "Fertig gemeldet";
  public static final String DOKUMENTIERT = "Dokumentiert";
  public static final String ABGERECHNET = "Abgerechnet";
  public static final String ABGESCHLOSSEN = "Abgeschlossen";

  // Set welches alle Status vor "Fertig gemeldet" enthält
  public static final Set allStatusBeforeFertiggemeldet = new HashSet();

  static
  {
    // -------------------------------------------------------------------------------
    // Statusmaps initialisieren
    // -------------------------------------------------------------------------------
    allStatusBeforeFertiggemeldet.add(NEU);
    allStatusBeforeFertiggemeldet.add(ANGELEGT);
    allStatusBeforeFertiggemeldet.add(FREIGEGEBEN);
    allStatusBeforeFertiggemeldet.add(IN_ARBEIT);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public static void checkMinutes(String desc, String minValue) throws BusinessException
  {
    if (minValue != null)
    {
      try
      {
        int mins = Integer.parseInt(minValue);
        if (mins >= 0 && mins <= 59)
        {
          return;
        }
      }
      catch (Exception e)
      {
      }
      throw new BusinessException("Die Minuten des " + desc + " dürfen nur zwischen 0 und 59 liegen.");
    }
  }

  public static void checkHours(String desc, String hourValue) throws BusinessException
  {
    if (hourValue != null)
    {
      try
      {
        int hours = Integer.parseInt(hourValue);
        if (hours >= 0)
        {
          return;
        }
      }
      catch (Exception e)
      {
      }
      throw new BusinessException("Die Stunden des " + desc + " sind ungültig.");
    }
  }

  private static void checkTimeValues(IDataTableRecord taskRecord) throws Exception
  {
    // prüfen ob die Dokumentationszeiten und Arbeitszeiten im richtigen Format
    // sind
    checkMinutes("Dokumentationsaufwands", taskRecord.getStringValue("timedoc_m"));
    checkHours("Dokumentationsaufwands", taskRecord.getStringValue("timedoc_h"));

    checkMinutes("Arbeitsaufwands", taskRecord.getStringValue("totaltimespent_m"));
    checkHours("Arbeitsaufwands", taskRecord.getStringValue("totaltimespent_h"));

    checkMinutes("Stördauer", taskRecord.getStringValue("disruption_m"));
    checkHours("Stördauer", taskRecord.getStringValue("disruption_h"));

    checkMinutes("Anlagenausfall", taskRecord.getStringValue("productionloss_m"));
    checkHours("Anlagenausfall", taskRecord.getStringValue("productionloss_h"));
  }

  /**
   * Diese Methode überprüft, ob ein neuer Auftrag überhaupt angelegt werden
   * darf.
   * 
   * @param taskRecord
   *          der neue Auftragsdatensatz
   * @throws Exception
   *           Auftrag kann/darf nich angelegt werden
   */
  private static void checkNewTask(IDataTableRecord taskRecord) throws Exception
  {
    if (taskRecord.isNew())
    {
      // Überprüfen, ob der Auftrag eine Meldung hat
      IDataTableRecord callRecord = taskRecord.getLinkedRecord("call");
      if (null == callRecord)
      {
        throw new BusinessException("Es ist keine Meldung ausgewählt.");
      }

      // Überprüfen, ob die Meldung des Auftrages im Status ANGENOMMEN ist
      Object callStatus = callRecord.getValue("callstatus");
      if (CallTableRecord.GESCHLOSSEN.equals(callStatus))
      {
        throw new BusinessException("Für geschlossene Meldungen dürfen keine Aufträge angelegt werden.");
      }
      if (!CallTableRecord.ANGENOMMEN.equals(callStatus))
      {
        // Wenn SAP job
        // Dann kann es sein, dass im gleichen Joblauf der Call geshlossen
        // (dokumentiert) wurde. Wir müssen aber trotzdem einen geschlossenen
        // Auftrag akzeptieren.
        if (null != (Context.getCurrent().getProperty("SAPimport")))
        {
          CSAPHelperClass.printDebug("*SAP Closed Status: *" + taskRecord.getSaveStringValue(jacob.model.Task.taskstatus));
          if (!jacob.model.Task.taskstatus_ENUM._Dokumentiert.equals(taskRecord.getSaveStringValue(jacob.model.Task.taskstatus))
              && !jacob.model.Task.taskstatus_ENUM._Abgerechnet.equals(taskRecord.getSaveStringValue(jacob.model.Task.taskstatus)))
          {
            throw new BusinessException("Um Aufträge aus SAP anzulegen, muß die Meldung angenommen sein.");
          }

        }
        else
        {
          throw new BusinessException("Um Aufträge anzulegen, muß die Meldung angenommen sein.");
        }

      }

      // Anmerkung: taskno muß nicht berechnet werden, da dies der Trigger tut
      // und in afterCommitAction() der Datensatz neu gelesen
      // wird.
    }
  }

  /**
   * Diese Methode führt diverse Überprüfungen bzgl. externes System und Objekt
   * des Auftrages durch.
   * 
   * @param taskRecord
   *          der Auftragsdatensatz
   * @throws Exception
   *           sofern die Überprüfungen fehlschlagen
   */
  private static void checkTaskLinks(IDataTableRecord taskRecord) throws Exception
  {
    // Überprüfen, ob ein externes System definiert ist
    if (!taskRecord.hasLinkedRecord("ext_system"))
    {
      throw new BusinessException("Das externe System ist Pflichteingabe.");
    }

    // Überprüfen, ob das externe System geändert wurde
    if (taskRecord.isUpdated() && taskRecord.hasChangedValue("ext_system_key"))
    {
      throw new BusinessException("Das externe System darf nicht geändert werden.");
    }

    // Hat der Auftrag ein Objekt?
    if (taskRecord.hasLinkedRecord("taskobject"))
    {
      IDataTableRecord iObjectRec = taskRecord.getLinkedRecord("taskobject");

      // Sofern das Objekt dem Auftrag neu zugeordnet wurde, darf der Status
      // des Objektes nicht außer Betrieb sein
      if ("außer Betrieb".equals(iObjectRec.getValue("objstatus")) && taskRecord.hasChangedValue("object_key"))
      {
        throw new BusinessException("Auf ein Objekt im Status 'Außer Betrieb' können keine Aufträge abgerechnet werden.");
      }

      // Abrechnungssystem von Objekt und Auftrag müssen übereinstimmen
      // (Anmerkung: es genügt die Keys zu vergleichen!)
      if (!taskRecord.getValue("ext_system_key").equals(iObjectRec.getValue("ext_system_key")))
      {
        throw new BusinessException(
            "Das in der Meldung enthaltene Objekt kann in diesem ext. System nicht abgerechnet werden.\\r\\nKontrollieren Sie bitte auch die HWG und die Auftragsart");
      }
      // EDVIN Aufträge mit Objekt müssen eine gültige Kostenstelle haben
      IDataTableRecord ext_System = taskRecord.getLinkedRecord("ext_system");
      if ("EDVIN".equals(ext_System.getSaveStringValue("systemtype")) && iObjectRec.hasLinkedRecord("taskobjaccountingcode"))
      {
        if ("ungültig".equals(iObjectRec.getLinkedRecord("taskobjaccountingcode").getSaveStringValue("accountingstatus")))
          throw new BusinessException("Das Objekt hat eine ungültige Kostenstelle. \\r\\nEs kann kein EDVIN-Auftrag angelegt werden.");

      }
    }

    // Die (unformatierte) Eingabe des EDVIN Störbeginnes überprüfen
    String sValue = taskRecord.getStringValue("disruption_start");
    if (null != sValue && taskRecord.hasChangedValue("disruption_start"))
    {
      SimpleDateFormat hourMinFormat = new SimpleDateFormat("HH:mm");
      hourMinFormat.setLenient(false);
      boolean isOK = false;
      try
      {
        isOK = null != hourMinFormat.parse(sValue);
      }
      catch (Exception ex)
      {
        // ignore parse error
      }
      if (!isOK)
      {
        throw new BusinessException("Edvin Störbeginn muß im Format HH:MM eingegeben werden und darf nicht größer als 23:59 sein");
      }
    }
  }

  /**
   * Überprüft, ob ein Statusübergang vorliegt und dieser erlaubt ist.
   * 
   * @param taskRecord
   *          der Auftragsdatensatz
   * @param transaction
   *          die Transaktion
   * @throws Exception
   *           sofern der Statusübergang nicht erlaubt bzw. möglich ist
   */
  private void checkTaskStatusChange(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
  {
    // Initialen Status bei neuen Aufträgen setzen
    if (taskRecord.isNew())
    {
      // Anmerkung: Das ein Abrechnungssystem vorhanden ist, haben wir schon in
      // checkTaskLinks() überprüft!
      IDataTableRecord iExtSystemRec = taskRecord.getLinkedRecord("ext_system");
      // Wen SAP Job und Status ab Fertiggemeldet
      // Dann lassen wir den Status
      if (null != (Context.getCurrent().getProperty("SAPimport")))
      {
        if (!taskRecord.getSaveStringValue(jacob.model.Task.taskstatus).equals(jacob.model.Task.taskstatus_ENUM._Dokumentiert)
            && !taskRecord.getSaveStringValue(jacob.model.Task.taskstatus).equals(jacob.model.Task.taskstatus_ENUM._Abgerechnet)
            && !taskRecord.getSaveStringValue(jacob.model.Task.taskstatus).equals(jacob.model.Task.taskstatus_ENUM._Fertig_gemeldet))
        {
          //Sonst setzen wir initialstatus
          taskRecord.setValue(transaction, "taskstatus", iExtSystemRec.getValue("initialstatus"));
        }
        
      }
      else
      {
        CSAPHelperClass.printDebug("*SAP Taskrecord - no Sap job");
        taskRecord.setValue(transaction, "taskstatus", iExtSystemRec.getValue("initialstatus"));
      }

    }

    // Diese Regel gilt bei allen Stati
    if (!taskRecord.hasLinkedRecord("taskobject"))
    {
      // Anmerkung: Das ein Abrechnungssystem vorhanden ist, haben wir schon in
      // checkTaskLinks() überprüft!
      IDataTableRecord iExtSystemRec = taskRecord.getLinkedRecord("ext_system");
      if ("Ja".equals(iExtSystemRec.getValue("objectrequired")))
      {
        throw new BusinessException("Das Abrechnungssystem des Auftrags erfordert ein Objekt.\\r\\nWählen Sie bitte ein passendes Objekt aus.");
      }
    }

    Object newStatus = taskRecord.getValue("taskstatus");
    Object oldStatus = taskRecord.isNew() ? newStatus : taskRecord.getOldValue("taskstatus");

    // Hier wird sichergestellt, daß Aufträge in einem der möglichen Endstati
    // nicht mehr geändert werden dürfen
    if (ABGERECHNET.equals(oldStatus) || STORNIERT.equals(oldStatus) || ABGESCHLOSSEN.equals(oldStatus))
    {
      throw new BusinessException("Abgerechnete oder stornierte Aufträge dürfen nicht mehr geändert werden.");
    }

    // Hat sich der Status überhaupt geändert? (Anmerkung: Ist bei neuen
    // Aufträgen immer der Fall)
    if (!taskRecord.hasChangedValue("taskstatus"))
    {
      return;
    }

    if (ANGELEGT.equals(newStatus))
    {
      if (NEU.equals(oldStatus) || ANGELEGT.equals(oldStatus))
      {
        // MIKE: Das nachfolgende Gerumsel wird doch nur für Imsys gebraucht!
        // Oder?
        // If bNEEDEXTID And iTaskRec.GetValue("extsystem_id") ="" Then
        // iApp.UserMsg "Es wird eine Id des Externen Systems benötigt",
        // ebCritical
        // iTaskRec.SetValue "taskstatus", sINITIALSTATUS
        // End If

        // Antwort ja, muss aber dennoch gemacht werden.
        return;
      }
    }
    else if (FREIGEGEBEN.equals(newStatus))
    {
      if (FREIGEGEBEN.equals(oldStatus) || ANGELEGT.equals(oldStatus))
      {
        return;
      }
    }
    else if (STORNIERT.equals(newStatus))
    {
      if (STORNIERT.equals(oldStatus) || ANGELEGT.equals(oldStatus) || FREIGEGEBEN.equals(oldStatus) || NEU.equals(oldStatus))
      {
        IDataTableRecord iExtSystemRec = taskRecord.getLinkedRecord("ext_system");
        if ("Nein".equals(iExtSystemRec.getValue("allowcancel")))
        {
          throw new BusinessException("In diesem ext. System ist 'stornieren' nicht erlaubt.");
        }
        return;
      }
    }
    else if (IN_ARBEIT.equals(newStatus))
    {
      if (FREIGEGEBEN.equals(oldStatus) || IN_ARBEIT.equals(oldStatus))
      {
        taskRecord.setValue(transaction, "dateowned", "now");
        return;
      }
    }
    else if (FERTIG_GEMELDET.equals(newStatus))
    {
      if (FERTIG_GEMELDET.equals(oldStatus) || IN_ARBEIT.equals(oldStatus))
      {
        taskRecord.setValue(transaction, "dateresolved", "now");
        if (null == taskRecord.getValue("taskdone"))
          taskRecord.setValue(transaction, "taskdone", "now");
        taskRecord.setValue(transaction, "resolved_user", Util.getUserLoginID(Context.getCurrent()));

        // den AK der Meldung über die Fertigmeldung unterrichten
        notifyWorkgroup(taskRecord, transaction);

        // Haben wir schon den Endstatus erreicht?
        // Anmerkung: Vermutlich vorerst nur für Imsys.
        IDataTableRecord iExtSystemRec = taskRecord.getLinkedRecord("ext_system");
        if (FERTIG_GEMELDET.equals(iExtSystemRec.getValue("endstatus")))
        {
          taskRecord.setValue(transaction, "taskstatus", ABGESCHLOSSEN);
          taskRecord.setValue(transaction, "dateclosed", "now");
        }
        return;
      }
    }
    else if (DOKUMENTIERT.equals(newStatus))
    {
      if (FERTIG_GEMELDET.equals(oldStatus) || DOKUMENTIERT.equals(oldStatus))
      {
        // überprüfen ob Auftrag richtig dokumentiert ist
        Task.checkDocumented(transaction, taskRecord);
        taskRecord.setValue(transaction, "datedocumented", "now");

        // Haben wir schon den Endstatus erreicht?
        // Anmerkung: Vermutlich vorerst nur für Imsys.
        IDataTableRecord iExtSystemRec = taskRecord.getLinkedRecord("ext_system");
        if (DOKUMENTIERT.equals(iExtSystemRec.getValue("endstatus")))
        {
          taskRecord.setValue(transaction, "taskstatus", ABGESCHLOSSEN);
          taskRecord.setValue(transaction, "dateclosed", "now");
        }
        return;
      }
      else if (IN_ARBEIT.equals(oldStatus))
      {
        if (!taskRecord.hasNullValue(jacob.model.Task.sap_auftrag))
        {
          // Alles machen, was wir beim Übergang von IN_ARBEIT auf FERTIG_GEMELDET machen
          //
          taskRecord.setValue(transaction, "dateresolved", "now");
          if (null == taskRecord.getValue("taskdone"))
            taskRecord.setValue(transaction, "taskdone", "now");
          taskRecord.setValue(transaction, "resolved_user", "by sap");

          // den AK der Meldung über die Fertigmeldung unterrichten
          notifyWorkgroup(taskRecord, transaction);
          
          // und alles machen, was wir beim Übergang von FERTIG_GEMELDET auf DOKUMENTIERT machen
          //
          Task.checkDocumented(transaction, taskRecord);
          taskRecord.setValue(transaction, "datedocumented", "now");
          
          // Haben wir schon den Endstatus erreicht?
          // Anmerkung: Vermutlich vorerst nur für Imsys.
          IDataTableRecord iExtSystemRec = taskRecord.getLinkedRecord("ext_system");
          if (DOKUMENTIERT.equals(iExtSystemRec.getValue("endstatus")))
          {
            taskRecord.setValue(transaction, "taskstatus", ABGESCHLOSSEN);
            taskRecord.setValue(transaction, "dateclosed", "now");
          }
          return;
        }
      }
    }
    else if (ABGERECHNET.equals(newStatus))
    {
      if (DOKUMENTIERT.equals(oldStatus))
      {
        taskRecord.setValue(transaction, "accountdate", "now");
        return;
      }
    }
    else if (ABGESCHLOSSEN.equals(newStatus))
    {
      if (ABGERECHNET.equals(oldStatus))
      {
        taskRecord.setValue(transaction, "dateclosed", "now");
        return;
      }
    }

    // Statusübergang ist nicht möglich

    // kein gültiger Statusübergang
    throw new BusinessException("Der Statusübergang von '" + oldStatus + "' zu '" + newStatus + "' ist nicht erlaubt.");
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
  {
    String taskstatus = taskRecord.getStringValue("taskstatus");
    CSAPHelperClass.printDebug("* SAP Taskstatus  Before Commit Anfang: *" + taskstatus);
    try
    {
      // ------------------------------------------------------------
      // Überprüfungen für neu angelegten Auftrag durchführen
      // ------------------------------------------------------------
      checkNewTask(taskRecord);

      // ------------------------------------------------------------
      // Überprüfungen der Arbeitszeiten
      // ------------------------------------------------------------
      checkTimeValues(taskRecord);

      // ------------------------------------------------------------
      // Überprüfungen von Abrechnungssystem und Objekt
      // ------------------------------------------------------------
      checkTaskLinks(taskRecord);

      // ------------------------------------------------------------
      // Die diverse Statusübergänge prüfen
      // ------------------------------------------------------------
      checkTaskStatusChange(taskRecord, transaction);

      // ------------------------------------------------------------
      // Edvin 5 Felder (CODE) müssen immer validiert werden
      // ------------------------------------------------------------
      Task.validateEdvinCodes(taskRecord);

      // ------------------------------------------------------------
      // Zum Abschluß noch einige Felder initialisieren
      // ------------------------------------------------------------
      if (taskRecord.isNew())
      {
        taskRecord.setValue(transaction, "daterequested", "now");
        taskRecord.setValue(transaction, "create_user", Util.getUserLoginID(Context.getCurrent()));
        if (null == taskRecord.getValue("disruption_start"))
          taskRecord.setValue(transaction, "disruption_start", new SimpleDateFormat("HH:mm").format(new Date()));
        if (null == (Context.getCurrent().getProperty("SAPimport")))
        {
        taskRecord.setValue(transaction, jacob.model.Task.sap_auftrag, null);
        }
      }
      taskRecord.setValue(transaction, "datemodified", "now");
      taskRecord.setValue(transaction, "change_user", Util.getUserLoginID(Context.getCurrent()));
      // ------------------------------------------------------------
      // User in cclist schreiben
      // ------------------------------------------------------------
      Util.TraceUser(taskRecord, transaction);

    }
    catch (BusinessException ex)
    {
      // Nummer sicher gehen und Status zurücksetzen
      if (taskRecord.isUpdated())
        taskRecord.setValue(transaction, "taskstatus", taskRecord.getOldValue("taskstatus"));

      throw ex;
    }
    // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
    // SAP - Neuen Auftrag in die Übertragungstabelle Schreiben
    // ------------------------------------------------------------

    // Die Änderung wurde nicht durch den scheduled Job "SAPExchange" erzeugt,
    // daher müssen sie an SAP Übertragen werden
    if (taskRecord.isNew() && null == (Context.getCurrent().getProperty("SAPimport")))
    {
      // Handling während der Migrationsphase
      // --> Wenn AK = SAP AK, Dann .....

      CSAPHelperClass.printDebug("SAP-IsNewTask");
      if (taskRecord.getLinkedRecord("call").hasLinkedRecord("callworkgroup"))
      {

        IDataTableRecord ak = taskRecord.getLinkedRecord("call").getLinkedRecord("callworkgroup");
        // ...und der AK ein SAP-AK ist
        if (ak.getintValue("sap_ak") == 1)
        {
          CSAPHelperClass.printDebug("SAP-TaskIsNew And SAP AK");
          CWriteSAPExchange.createExchangeTask(taskRecord);
        }
        else
        {
          CSAPHelperClass.printDebug("SAP-TaskIsNew no SAP AK");
        }

      }

    }
    // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
    // SAP - Auftrag zum Update in SAP in die Übertragungstabelle Schreiben
    // ------------------------------------------------------------

    // Die Änderung wurde nicht durch den scheduled Job "SAPExchange" erzeugt,
    // daher müssen sie an SAP Übertragen werden
    if (taskRecord.isUpdated() && null == (Context.getCurrent().getProperty("SAPimport")))
    {
      // Handling während der Migrationsphase
      // --> Wenn AK = SAP AK, Dann .....

      CSAPHelperClass.printDebug("SAP-IsUpdateTask");
      if (taskRecord.getLinkedRecord("call").hasLinkedRecord("callworkgroup"))
      {

        IDataTableRecord ak = taskRecord.getLinkedRecord("call").getLinkedRecord("callworkgroup");
        // ...und der AK ein SAP-AK ist
        if (ak.getintValue("sap_ak") == 1)
        {
         // if (!taskRecord.getSaveStringValue(jacob.model.Task.sap_auftrag).equals(""))
         // {

            CSAPHelperClass.printDebug("SAP-TaskIsUpdate And SAP AK");
            CWriteSAPExchange.updateExchangeTask(taskRecord);
            if (taskRecord.getSaveStringValue(jacob.model.Task.taskstatus).equals(jacob.model.Task.taskstatus_ENUM._Abgeschlossen))
              //Hack, erst mal nur schließen, wenn auch abgeschlossen.
              // Nach Dokumentiet kann nicht mehr nach SAP geschrieben werden
//            if (taskRecord.getSaveStringValue(jacob.model.Task.taskstatus).equals(jacob.model.Task.taskstatus_ENUM._Dokumentiert)
//                || taskRecord.getSaveStringValue(jacob.model.Task.taskstatus).equals(jacob.model.Task.taskstatus_ENUM._Abgeschlossen))
            {
              CWriteSAPExchange.closeExchangeTask(taskRecord);
            }
         // }

        }
        else
        {
          CSAPHelperClass.printDebug("SAP-TaskIsUpdate no SAP AK");
        }

      }

    }
    taskstatus = taskRecord.getStringValue("taskstatus");
  }

  /**
   * Verschickt eine Meldung über die Fertigmeldung des Auftrages an den AK,
   * sofern Feedback erwünscht ist.
   * 
   * @param taskRecord
   *          der Auftrag
   * @param transaction
   *          die Transaktion, in deren Kontext die Fertigmeldung erfolgt
   * @throws Exception
   *           bei einem schwerwiegenden Fehler
   */
  private static void notifyWorkgroup(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
  {
    if ("Ja".equals(taskRecord.getValue("feedback")))
    {
      String sMsg = "Auftrag " + taskRecord.getValue("taskno") + " wurde Fertig gemeldet.";
      // Documentenforlage suchen
      IDataTable doc_templateTable = taskRecord.getAccessor().getTable("doc_template");
      doc_templateTable.qbeClear();
      doc_templateTable.qbeSetValue("use_in", "Auftragrückmeldung");
      if (doc_templateTable.search() > 0)
      {
        IDataTableRecord docTemplate = doc_templateTable.getRecord(0);
        IDataTable workgroupTable = taskRecord.getAccessor().getTable("callworkgroup");
        if (workgroupTable.recordCount() != 1)
          return;
        ActionNotificatorArbeitsgruppe.send(taskRecord, workgroupTable.getRecord(0), docTemplate, transaction, sMsg);
      }

    }
  }

  /**
   * Wenn der Auftrag im Status fertig gemeldet oder Dokumentiert ist dann wird
   * versucht die Meldung des Auftrages automatisch zu schließen, sofern dies
   * möglich ist.
   * 
   * @param taskRecord
   *          der Auftrag
   * @throws Exception
   *           bei einem schwerwiegenden Fehler
   */
  private static void checkAutoClosed(IDataTableRecord taskRecord) throws Exception
  {
    String taskstatus = taskRecord.getStringValue("taskstatus");

    if (FERTIG_GEMELDET.equals(taskstatus))
    {
      // Meldung holen
      IDataTableRecord callRecord = taskRecord.getLinkedRecord("call");

      // und versuchen fertig zu melden
      CallTableRecord.tryToCloseCall(callRecord);
    }
    // automatisch dokumentieren der Meldung wenn gewünscht:
    if (DOKUMENTIERT.equals(taskstatus))
    {
      // Meldung holen
      IDataTableRecord callRecord = taskRecord.getLinkedRecord("call");
      CallTableRecord.tryToDocumentCall(callRecord);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
    // Wenn ein Taskdatensatz neu erstellt oder verändert wurde ..
    if (tableRecord.isNew() || tableRecord.isUpdated())
    {
      // und dieser mit dem aktuell angezeigten Datensatz übereinstimmt
      IDataTable taskTable = tableRecord.getTable();
      if (tableRecord.equals(taskTable.getSelectedRecord()))
      {
        // dann lade den Datensatz neu von der Datenbank, da die Trigger Felder
        // geändert haben könnten.
        taskTable.reloadSelectedRecord();
      }
    }

    // Frage an Mike: Escalationen werden nur für Imsys gebraucht! Oder?
    // ScheduleEscalations iApp, iNetwork, iTaskTable, iTaskRec
    // Antwort: Ja, wir müssen irgendwann einen eigenen Eskalationsmechanismus
    // implementieren

    // MIKE: Aktionsregeln implementieren (Andreas Herz fragen)
    // RunActions iApp, iNetwork, iTaskTable, iTaskRec

    // ------------------------------------------------------------
    // Falls möglich, Meldung fertig melden
    // Wichtige Anmerkung: Eine Methode AutoCallDocumented() ist nicht
    // notwendig, da dies schon implizit in den Call-Hooks passiert.
    // ------------------------------------------------------------
    checkAutoClosed(tableRecord);

  }

  /**
   * Dies klasse wird in Abhängigkeit der Benachrichtungsart erzeugt und
   * gerufen. <br>
   * Die erzeugte Instance (singleton) ist dann für das versenden der
   * Benachrichtigungen verantwortlich.
   * 
   * @author Andreas Herz
   */
  private static interface ActionNotificator
  {
    public void send(IDataTableRecord callRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception;
  }

  /**
   * Es muss derjenige welche benachrichtigt werden der in der Aktionsregel
   * steht.
   * 
   */
  static class ActionNotificatorAddresse implements ActionNotificator
  {
    public void send(IDataTableRecord callRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception
    {
      Context context = Context.getCurrent();
      // Dokumentenvorlage zu der Actionsregel holen
      //
      IDataTableRecord docTemplate = callAction.getLinkedRecord("doc_template");

      // Entsprechend des Meldungskanals das Stylesheet und die
      // Empfängeradresse holen
      //
      String channel = callAction.getSaveStringValue("method");
      String stylesheet;
      String recipienAddress;
      if ("Email".equals(channel))
      {
        recipienAddress = "email://" + callAction.getSaveStringValue("notificationaddr");
        stylesheet = docTemplate.getStringValue("email_xsl");
      }
      else if ("FAX".equals(channel))
      {
        recipienAddress = "rightfax://" + callAction.getSaveStringValue("notificationaddr");
        stylesheet = docTemplate.getStringValue("fax_xsl");
      }
      else if ("Funkruf".equals(channel) || "SMS".equals(channel))
      {
        recipienAddress = "sms://" + callAction.getSaveStringValue("notificationaddr");
        stylesheet = docTemplate.getStringValue("sms_xsl");
      }
      else if ("Signal".equals(channel))
      {
        String document = callAction.getSaveStringValue("subject");
        document = Yan.fillDBFields(context, callRecord, docTemplate, document, true, null);
        IDataTable table = context.getDataTable("qw_alert");
        IDataTableRecord bookmark = table.newRecord(transaction);
        // FREEGROUP: Enumwerte nach integer parsen. Jetzt erstmal vergessen.
        // String prio = callRecord.getStringValue("priority");
        // if(prio==null)prio="0";
        bookmark.setValue(transaction, "addressee", callAction.getSaveStringValue("notificationaddr"));
        bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
        bookmark.setValue(transaction, "message", document);
        bookmark.setValue(transaction, "tablekey", callRecord.getStringValue("pkey"));
        bookmark.setValue(transaction, "tablename", "call");
        bookmark.setValue(transaction, "alerttype", "Warnung");
        bookmark.setValue(transaction, "dateposted", "now");
        bookmark.setValue(transaction, "severity", "1");
        return;
      }
      else
      {
        throw new BusinessException("Unbekannte (nicht implementierte) Rückmeldungsart [" + channel + "] für Meldung.");
      }

      // XML-Vorlage für die Meldung holen und mit den Datenbankfeldern
      // auffüllen
      //
      String document = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");

      document = Yan.fillDBFields(context, callRecord, docTemplate, document, true, null);
      Yan.createInstance(context, document, recipienAddress, stylesheet);
    }
  }

  /**
   * Der Kunde muss benachrichtigt werden, dass sich an seinem Ticket etwas
   * getan hat.
   * 
   */
  static class ActionNotificatorKunde implements ActionNotificator
  {
    public void send(IDataTableRecord callRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception
    {
      // Falls es sich um eine Untermeldung handelt, dann muss man den Kunden
      // noch nicht benachrichtigen.
      // Benachrichtigung kommt durch Hauptmeldung.
      //
      if (callRecord.getValue("mastercall_key") != null)
        return;

      Context context = Context.getCurrent();
      // Dokumentenvorlage zu der Actionsregel holen
      //
      IDataTableRecord docTemplate = callAction.getLinkedRecord("doc_template");
      IDataTableRecord customer = callRecord.getLinkedRecord("customerint");

      // XML-Vorlage für die Meldung holen und mit den Datenbankfeldern
      // auffüllen
      //
      String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
      template = Yan.fillDBFields(context, callRecord, docTemplate, template, true, null);

      // Entsprechend des Meldungskanals das Stylesheet und die
      // Empfängeradresse holen
      //
      String channel = callRecord.getSaveStringValue("callbackmethod");
      String stylesheet;
      String recipienAddress;
      if ("Email".equals(channel))
      {
        recipienAddress = "email://" + customer.getSaveStringValue("emailcorr");
        stylesheet = docTemplate.getStringValue("email_xsl");
        Yan.createInstance(context, template, recipienAddress, stylesheet);
        return;
      }
      if ("FAX".equals(channel))
      {
        recipienAddress = "rightfax://" + customer.getSaveStringValue("faxcorr");
        stylesheet = docTemplate.getStringValue("fax_xsl");
        Yan.createInstance(context, template, recipienAddress, stylesheet);
        return;
      }
      if ("Keine".equals(channel))
      {
        return;
      }
      if ("Funkruf".equals(channel) || "SMS".equals(channel))
      {
        recipienAddress = "sms://" + customer.getSaveStringValue("pager");
        stylesheet = docTemplate.getStringValue("sms_xsl");
        Yan.createInstance(context, template, recipienAddress, stylesheet);
        return;
      }

      String document = callAction.getSaveStringValue("subject");
      document = Yan.fillDBFields(context, callRecord, docTemplate, document, true, null);
      IDataTable table = context.getDataTable("qw_alert");
      IDataTableRecord bookmark = table.newRecord(transaction);
      // FREEGROUP: Enumwerte nach integer parsen. Jetzt erstmal vergessen.
      // String prio = callRecord.getStringValue("priority");
      // if(prio==null)prio="0";
      bookmark.setValue(transaction, "addressee", DataUtils.getAppprofileValue(context, "custfeedbacklogin"));
      bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
      bookmark.setValue(transaction, "message", document);
      bookmark.setValue(transaction, "tablekey", callRecord.getStringValue("pkey"));
      bookmark.setValue(transaction, "tablename", "call");
      bookmark.setValue(transaction, "alerttype", "Warnung");
      bookmark.setValue(transaction, "dateposted", "now");
      bookmark.setValue(transaction, "severity", "1");
    }
  }

  /**
   * 
   * 
   */
  static class ActionNotificatorMitarbeiter implements ActionNotificator
  {
    public void send(IDataTableRecord callRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception
    {
      Context context = Context.getCurrent();
      // Dokumentenvorlage zu der Actionsregel holen
      //
      IDataTableRecord docTemplate = callAction.getLinkedRecord("doc_template");
      IDataTableRecord agent = callRecord.getLinkedRecord("agent");

      // XML-Vorlage für die Meldung holen und mit den Datenbankfeldern
      // auffüllen
      //
      String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
      template = Yan.fillDBFields(context, callRecord, docTemplate, template, true, null);

      // Entsprechend des Meldungskanals das Stylesheet und die
      // Empfängeradresse holen
      //
      String channel = agent.getSaveStringValue("communicatepref");
      String stylesheet;
      String recipienAddress;
      if ("Email".equals(channel))
      {
        recipienAddress = "email://" + agent.getSaveStringValue("emailcorr");
        stylesheet = docTemplate.getStringValue("email_xsl");
        Yan.createInstance(context, template, recipienAddress, stylesheet);
        return;
      }
      if ("FAX".equals(channel))
      {
        recipienAddress = "rightfax://" + agent.getSaveStringValue("faxcorr");
        stylesheet = docTemplate.getStringValue("fax_xsl");
        Yan.createInstance(context, template, recipienAddress, stylesheet);
        return;
      }
      if ("Funkruf".equals(channel) || "SMS".equals(channel))
      {
        recipienAddress = "sms://" + agent.getSaveStringValue("pager");
        stylesheet = docTemplate.getStringValue("sms_xsl");
        Yan.createInstance(context, template, recipienAddress, stylesheet);
        return;
      }

      if ("Keine".equals(channel))
      {
        return;
      }

      String document = callAction.getSaveStringValue("subject");
      document = Yan.fillDBFields(context, callRecord, docTemplate, document, true, null);
      IDataTable table = context.getDataTable("qw_alert");
      IDataTableRecord bookmark = table.newRecord(transaction);
      // FREEGROUP: Enumwerte nach integer parsen. Jetzt erstmal vergessen.
      // String prio = callRecord.getStringValue("priority");
      // if(prio==null)prio="0";
      String address = agent.getStringValue("loginname");
      if (address == null)
        return;
      bookmark.setValue(transaction, "addressee", address);
      bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
      bookmark.setValue(transaction, "message", document);
      bookmark.setValue(transaction, "tablekey", callRecord.getStringValue("pkey"));
      bookmark.setValue(transaction, "tablename", "call");
      bookmark.setValue(transaction, "alerttype", "Warnung");
      bookmark.setValue(transaction, "dateposted", "now");
      bookmark.setValue(transaction, "severity", "1");
    }
  }

  /**
   * 
   * 
   */
  static class ActionNotificatorArbeitsgruppe
  {

    static void notifyGroupmember(Context context, IDataTableRecord taskRecord, IDataTableRecord workgroupRecord, IDataTableRecord docTemplate,
        IDataTransaction transaction, String subject) throws Exception
    {
      // Überprüfen ob gegebenfalls Eigenbenachrichtigung der Gruppe vorliegt
      // MIKE: Workaround mit UserKey lösen!
      String UserKey = Util.getUserKey(context); // context.getUser().getKey();
      IDataTable groupmemberTable = context.getDataTable("groupmember");
      if ("Nein".equals(workgroupRecord.getValue("notifyowngroup")) && UserKey != null)
      {
        groupmemberTable.qbeClear();
        groupmemberTable.clear();
        groupmemberTable.qbeSetKeyValue("workgroupgroup", workgroupRecord.getValue("pkey"));
        groupmemberTable.qbeSetKeyValue("employeegroup", UserKey);
        groupmemberTable.search();
        if (groupmemberTable.recordCount() > 0)
          return;
      }
      groupmemberTable.qbeClear();
      groupmemberTable.clear();
      groupmemberTable.qbeSetKeyValue("workgroupgroup", workgroupRecord.getValue("pkey"));
      groupmemberTable.qbeSetValue("employeegroup", "!" + UserKey);
      groupmemberTable.qbeSetValue("notifymethod", "!Keine");

      groupmemberTable.search();
      if (groupmemberTable.recordCount() < 1)
        return;

      String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
      template = Yan.fillDBFields(context, taskRecord, docTemplate, template, true, null);
      String recipienAddress;
      for (int i = 0; i < groupmemberTable.recordCount(); i++)
      {
        IDataTableRecord groupmember = groupmemberTable.getRecord(i);
        IDataTableRecord employee = groupmember.getLinkedRecord("employee");
        String channel = groupmember.getSaveStringValue("notifymethod");
        String stylesheet = groupmember.getStringValue("xsl_stylesheet");
        if ("Email".equals(channel))
        {
          recipienAddress = "email://" + employee.getSaveStringValue("emailcorr");
          if (stylesheet == null)
            stylesheet = docTemplate.getStringValue("email_xsl");
          Yan.createInstance(context, template, recipienAddress, stylesheet);
          continue;
        }
        if ("FAX".equals(channel))
        {
          recipienAddress = "rightfax://" + employee.getSaveStringValue("faxcorr");
          if (stylesheet == null)
            stylesheet = docTemplate.getStringValue("fax_xsl");
          Yan.createInstance(context, template, recipienAddress, stylesheet);
          continue;
        }
        if ("Funkruf".equals(channel) || "SMS".equals(channel))
        {
          recipienAddress = "sms://" + employee.getSaveStringValue("pager");
          ;
          if (stylesheet == null)
            stylesheet = docTemplate.getStringValue("sms_xsl");
          Yan.createInstance(context, template, recipienAddress, stylesheet);
          continue;
        }
        if ("Signal".equals(channel))
        {

          subject = Yan.fillDBFields(context, taskRecord, docTemplate, subject, true, null);
          IDataTable table = context.getDataTable("qw_alert");
          IDataTableRecord bookmark = table.newRecord(transaction);
          // FREEGROUP: Enumwerte nach integer parsen. Jetzt erstmal vergessen.
          // String prio = callRecord.getStringValue("priority");
          // if(prio==null)prio="0";
          String address = employee.getStringValue("loginname");
          if (address == null)
            continue;
          bookmark.setValue(transaction, "addressee", address);
          bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
          bookmark.setValue(transaction, "message", subject);
          bookmark.setValue(transaction, "tablekey", taskRecord.getStringValue("pkey"));
          bookmark.setValue(transaction, "tablename", "call");
          bookmark.setValue(transaction, "alerttype", "Warnung");
          bookmark.setValue(transaction, "dateposted", "now");
          bookmark.setValue(transaction, "severity", "1");
        }
      }
    }

    static public void send(IDataTableRecord taskRecord, IDataTableRecord workgroup, IDataTableRecord docTemplate, IDataTransaction transaction, String subject)
        throws Exception
    {
      Context context = Context.getCurrent();

      // XML-Vorlage für die Meldung holen und mit den Datenbankfeldern
      // auffüllen
      //
      String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
      template = Yan.fillDBFields(context, taskRecord, docTemplate, template, true, null);

      // Entsprechend des Meldungskanals das Stylesheet und die
      // Empfängeradresse holen
      //
      String channel = workgroup.getSaveStringValue("notifymethod");
      String stylesheet;
      String recipienAddress;
      String Address = workgroup.getSaveStringValue("notificationaddr");

      if ("Bearbeiter".equals(channel))
      {
        notifyGroupmember(context, taskRecord, workgroup, docTemplate, transaction, subject);
        return;
      }
      if ("Email".equals(channel))
      {
        recipienAddress = "email://" + Address;
        stylesheet = docTemplate.getStringValue("email_xsl");
        Yan.createInstance(context, template, recipienAddress, stylesheet);
        return;
      }
      if ("FAX".equals(channel))
      {
        recipienAddress = "rightfax://" + Address;
        stylesheet = docTemplate.getStringValue("fax_xsl");
        Yan.createInstance(context, template, recipienAddress, stylesheet);
        return;
      }
      if ("Funkruf".equals(channel) || "SMS".equals(channel))
      {
        recipienAddress = "sms://" + Address;
        stylesheet = docTemplate.getStringValue("sms_xsl");
        Yan.createInstance(context, template, recipienAddress, stylesheet);
        return;
      }
      if ("Keine".equals(channel))
      {
        return;
      }
      // if something wrong notify special Agent

      IDataTable table = context.getDataTable("qw_alert");
      IDataTableRecord bookmark = table.newRecord(transaction);
      bookmark.setValue(transaction, "addressee", DataUtils.getAppprofileValue(context, "custfeedbacklogin"));
      bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
      bookmark.setValue(transaction, "message", "AK konnte nicht benachrichtigt werden");
      bookmark.setValue(transaction, "tablekey", taskRecord.getStringValue("pkey"));
      bookmark.setValue(transaction, "tablename", "call");
      bookmark.setValue(transaction, "alerttype", "Warnung");
      bookmark.setValue(transaction, "dateposted", "now");
      bookmark.setValue(transaction, "severity", "1");
    }

  }

}
