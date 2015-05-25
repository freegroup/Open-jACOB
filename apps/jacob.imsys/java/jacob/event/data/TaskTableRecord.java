/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.common.Escalation;
import jacob.common.Notification;
import jacob.common.Task;
import jacob.common.Util;
import jacob.common.Yan;
import jacob.common.data.DataUtils;
import jacob.exception.BusinessException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.definition.IRelationSet;

/**
 *
 * @author mike
 */
public class TaskTableRecord extends DataTableRecordEventHandler
{


    static public final transient String RCS_ID = "$Id: TaskTableRecord.java,v 1.9 2006/05/16 11:50:17 mike Exp $";
    static public final transient String RCS_REV = "$Revision: 1.9 $";
    
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

    // Set welches alle Status vor "Fertig gemeldet" enth�lt
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
    class ParameterContainer
    {
       private String initialStatus ="Neu";
       private String endStatus ="Abgerechnet";
       private boolean objectRequired = false;
       private boolean allowCancel = false;
       private boolean needExternalID = false;
    }
    private ParameterContainer getExternalSystemParameter(IDataTableRecord tableRecord) throws Exception
    {
        if (!tableRecord.hasLinkedRecord("ext_system"))
        {
            throw new BusinessException("Es ist kein g�ltiges ext.System gew�hlt");
        }
        IDataTableRecord ext_system = tableRecord.getLinkedRecord("ext_system");
        
        ParameterContainer parameter = new  ParameterContainer();
        parameter.initialStatus = ext_system.getSaveStringValue("initialstatus");
        parameter.endStatus = ext_system.getSaveStringValue("endstatus");
        parameter.allowCancel = "Ja".equals(ext_system.getSaveStringValue("allowcancel"));
        parameter.objectRequired = "Ja".equals(ext_system.getSaveStringValue("objectrequired"));
        parameter.needExternalID = "neu".equals(parameter.initialStatus);
        
        return parameter;
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
      throw new BusinessException("Die Minuten des " + desc + " d�rfen nur zwischen 0 und 59 liegen.");
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
      throw new BusinessException("Die Stunden des " + desc + " sind ung�ltig.");
    }
  }

  private static void checkTimeValues(IDataTableRecord taskRecord) throws Exception
  {
    // pr�fen ob die Dokumentationszeiten und Arbeitszeiten im richtigen Format sind
    checkMinutes("Dokumentationsaufwands", taskRecord.getStringValue("timedoc_m"));
    checkHours("Dokumentationsaufwands", taskRecord.getStringValue("timedoc_h"));

    checkMinutes("Arbeitsaufwands", taskRecord.getStringValue("totaltimespent_m"));
    checkHours("Arbeitsaufwands", taskRecord.getStringValue("totaltimespent_h"));

    checkMinutes("St�rdauer", taskRecord.getStringValue("disruption_m"));
    checkHours("St�rdauer", taskRecord.getStringValue("disruption_h"));

    checkMinutes("Anlagenausfall", taskRecord.getStringValue("productionloss_m"));
    checkHours("Anlagenausfall", taskRecord.getStringValue("productionloss_h"));
  }
  
    /**
     * Diese Methode �berpr�ft, ob ein neuer Auftrag �berhaupt angelegt werden
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
            // �berpr�fen, ob der Auftrag eine Meldung hat
            IDataTableRecord callRecord = taskRecord.getLinkedRecord("call");
            if (null == callRecord)
            {
                throw new BusinessException("Es ist keine Meldung ausgew�hlt.");
            }

            // �berpr�fen, ob die Meldung des Auftrages im Status ANGENOMMEN ist
            Object callStatus = callRecord.getValue("callstatus");
            if (CallTableRecord.GESCHLOSSEN.equals(callStatus))
            {
                throw new BusinessException("F�r geschlossene Meldungen d�rfen keine Auftr�ge angelegt werden.");
            }
            if (!CallTableRecord.ANGENOMMEN.equals(callStatus))
            {
                throw new BusinessException("Um Auftr�ge anzulegen, mu� die Meldung angenommen sein.");
            }  
            // pr�fen ob Sequenz g�ltig ist
            CheckSequence(taskRecord,null);
        }
    }
    /**
     * Diese Methode �berpr�ft, ob es eine niedrigere Sequenz au�er 0 gibt und damit
     * die Sequenz ung�ltig ist.
     * 
     * @param taskRecord
     *          der neue Auftragsdatensatz
     * @throws Exception
     *           Auftrag kann/darf nich angelegt werden
     */  
    private static void CheckSequence(IDataTableRecord taskRecord,IDataAccessor searchAccessor) throws Exception
    {
        int sequence = taskRecord.getintValue("sequence");
        if (sequence>1)
        {
            if (searchAccessor == null) searchAccessor = taskRecord.getAccessor().newAccessor();
            searchAccessor.clear();
            IDataTable task = searchAccessor.getTable("task");
            task.qbeSetValue("taskstatus","Neu|Angelegt|Freigegeben|In Arbeit"); // nur "!Storniert" ist eigentlich eine unn�tige Einschr�nkung
            task.qbeSetValue( "calltask",taskRecord.getValue("calltask"));
            task.qbeSetValue("workable", "Ja" );
            task.qbeSetValue("pkey", "!" +taskRecord.getSaveStringValue("pkey"));
            task.qbeSetValue("sequence", "1.." +Integer.toString(sequence-1));
            if (task.exists())
                throw new BusinessException("Die angegebene Sequenz ist ung�ltig.\nEs existiert mindestens ein ausf�hrbarer Auftrag mit niedrigerer Sequenz");

        }
    }    
    /**
     * Diese Methode �berpr�ft, ob der Auftrag ausf�hrbar ist.
     * Wenn die Seguenz 0 oder es keine offenen Auftr�ge mit einer h�heren Seguenz gibt,
     * dann ist der Auftrag ausf�hrbar.
     * Wird ein ausf�hrbarer Auftrag beendet, dan muss der n�chst niedrigere Auftrag ausf�hrbar werden.
     * 
     * @param taskRecord
     *          der neue Auftragsdatensatz
     * @throws Exception
     *           Auftrag kann/darf nich angelegt werden
     */  
    private static void WorkableStatus(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
    {
        // Workable Status �berpr�fen 0 und 1 
        int sequence = taskRecord.getintValue("sequence");
        if (sequence==0)
        {
            taskRecord.setValue(transaction,"workable","Ja");
            return;
        }
        int postSequence;
        if (sequence>99)
        {
            taskRecord.setValue(transaction,"sequence","99");
            sequence=99;
            postSequence = sequence;
        }
        else
        {
            postSequence = sequence+1;
        }
        // pr�fen ob Sequenz g�ltig 
        IDataAccessor searchAccessor = taskRecord.getAccessor().newAccessor();
        CheckSequence(taskRecord,searchAccessor);
        searchAccessor.clear();
        IDataTable task = searchAccessor.getTable("task");
        task.qbeSetValue("taskstatus","Neu|Angelegt|Freigegeben|In Arbeit"); // nur "!Storniert" ist eigentlich eine unn�tige Einschr�nkung
        task.qbeSetValue( "calltask",taskRecord.getValue("calltask"));
        task.qbeSetValue("pkey", "!" +taskRecord.getSaveStringValue("pkey"));
        task.qbeSetValue("sequence",Integer.toString(postSequence)+ "..99");
        if (task.exists())
        {
            taskRecord.setValue(transaction,"workable","Nein"); 
        }
        else
        {
            taskRecord.setValue(transaction,"workable","Ja");  
        }

        if(allStatusBeforeFertiggemeldet.contains(taskRecord.getSaveStringValue(STATUS)))
            return;
        // wenn dieser Auftrag fertig ist, schauen ob andere Auftr�ge ausf�hrbar werden
        // Achtung beforeComit der anderen Datens�tze wird NICHT gerufen! vgl. Doku ITableRecordEventhandler
        searchAccessor.clear();
        IDataBrowser taskbrowser = searchAccessor.getBrowser("taskBrowser");
        taskbrowser.searchWhere(IRelationSet.LOCAL_NAME,"task.pkey <> "  +taskRecord.getSaveStringValue("pkey") 
                + " And task.calltask = " +taskRecord.getSaveStringValue("calltask")
                +  " And task.sequence >0 And task.sequence <"
                + taskRecord.getSaveStringValue("sequence"));
        int maxSequence =0;
        for (int i = 0; i < taskbrowser.recordCount(); i++)
        {
            IDataTableRecord currentRecord = taskbrowser.getRecord(i).getTableRecord();
            if (currentRecord.getintValue("sequence")>maxSequence)
            {
                maxSequence = currentRecord.getintValue("sequence");
                currentRecord.setValue(transaction,"workable","Ja");
                
            }
            
        }

    }

    /**
     * Diese Methode f�hrt diverse �berpr�fungen bzgl. externes System und Objekt
     * des Auftrages durch.
     * 
     * @param taskRecord
     *          der Auftragsdatensatz
     * @throws Exception
     *           sofern die �berpr�fungen fehlschlagen
     */
    private static void checkTaskLinks( IDataTableRecord taskRecord) throws Exception
    {
        // �berpr�fen, ob ein externes System definiert ist
        if (!taskRecord.hasLinkedRecord("ext_system"))
        {
            throw new BusinessException("Das externe System ist Pflichteingabe.");
        }

        // �berpr�fen, ob das externe System ge�ndert wurde
        if (taskRecord.isUpdated() && taskRecord.hasChangedValue("ext_system_key"))
        {
            throw new BusinessException("Das externe System darf nicht ge�ndert werden.");
        }

        // Hat der Auftrag ein Objekt?
        if (taskRecord.hasLinkedRecord("taskobject"))
        {
            IDataTableRecord iObjectRec = taskRecord.getLinkedRecord("taskobject");

            // Sofern das Objekt dem Auftrag neu zugeordnet wurde, darf der Status
            // des Objektes nicht au�er Betrieb sein
            if ("au�er Betrieb".equals(iObjectRec.getValue("objstatus")) && taskRecord.hasChangedValue("object_key"))
            {
                throw new BusinessException("Auf ein Objekt im Status 'Au�er Betrieb' k�nnen keine Auftr�ge abgerechnet werden.");
            }

            // Abrechnungssystem von Objekt und Auftrag m�ssen �bereinstimmen
            // (Anmerkung: es gen�gt die Keys zu vergleichen!)
            if (!taskRecord.getValue("ext_system_key").equals(iObjectRec.getValue("ext_system_key")))
            {
                throw new BusinessException("Das in der Meldung enthaltene Objekt kann in diesem ext. System nicht abgerechnet werden.\\r\\nKontrollieren Sie bitte auch die HWG und die Auftragsart");
            }
        }

        // Die (unformatierte) Eingabe des EDVIN St�rbeginnes �berpr�fen
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
                throw new BusinessException("Edvin St�rbeginn mu� im Format HH:MM eingegeben werden und darf nicht gr��er als 23:59 sein");
            }
        }
    }

    /**
     * �berpr�ft, ob ein Status�bergang vorliegt und dieser erlaubt ist.
     * 
     * @param taskRecord
     *          der Auftragsdatensatz
     * @param transaction
     *          die Transaktion
     * @throws Exception
     *           sofern der Status�bergang nicht erlaubt bzw. m�glich ist
     */
    private void checkTaskStatusChange(ParameterContainer parameter,IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
    {
        // Initialen Status bei neuen Auftr�gen setzen
        if (taskRecord.isNew())
        {
            // Anmerkung: Das ein Abrechnungssystem vorhanden ist, haben wir schon in
            // checkTaskLinks() �berpr�ft!
            IDataTableRecord iExtSystemRec = taskRecord.getLinkedRecord("ext_system");
            taskRecord.setValue(transaction, "taskstatus", iExtSystemRec.getValue("initialstatus"));
        }

        // Diese Regel gilt bei allen Stati
        if (!taskRecord.hasLinkedRecord("taskobject"))
        {
            // Anmerkung: Das ein Abrechnungssystem vorhanden ist, haben wir schon in
            // checkTaskLinks() �berpr�ft!
            IDataTableRecord iExtSystemRec = taskRecord.getLinkedRecord("ext_system");
            if (parameter.objectRequired)
            {
                throw new BusinessException("Das Abrechnungssystem des Auftrags erfordert ein Objekt.\\r\\nW�hlen Sie bitte ein passendes Objekt aus.");
            }
        }

        Object newStatus = taskRecord.getValue("taskstatus");
        Object oldStatus = taskRecord.isNew() ? newStatus : taskRecord.getOldValue("taskstatus");
        boolean workable = "Ja".equals(taskRecord.getSaveStringValue("workable"));

        // Hier wird sichergestellt, da� Auftr�ge in einem der m�glichen Endstati
        // nicht mehr ge�ndert werden d�rfen
        if (ABGERECHNET.equals(oldStatus) || STORNIERT.equals(oldStatus) || ABGESCHLOSSEN.equals(oldStatus))
        {
            throw new BusinessException("Abgerechnete oder stornierte Auftr�ge d�rfen nicht mehr ge�ndert werden.");
        }

        // Hat sich der Status �berhaupt ge�ndert? (Anmerkung: Ist bei neuen
        // Auftr�gen immer der Fall)
        if (!taskRecord.hasChangedValue("taskstatus"))
        {
            return;
        }

        if (ANGELEGT.equals(newStatus))
        {
            if (NEU.equals(oldStatus) || ANGELEGT.equals(oldStatus))
            {
                if (parameter.needExternalID && taskRecord.getValue("extsystem_id") == null)
                {
                    throw new BusinessException("Es wird eine Id des Externen Systems ben�tigt.");

                }
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
            if (IN_ARBEIT.equals(oldStatus) ||STORNIERT.equals(oldStatus) || ANGELEGT.equals(oldStatus) || FREIGEGEBEN.equals(oldStatus) || NEU.equals(oldStatus))
            {
               
                if (!parameter.allowCancel)
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
                if (!workable)
                {
                    throw new BusinessException("Sie d�rfen diesen Auftrag nicht in Bearbeitung setzen, weil er noch nicht bearbeitbar ist.");  
                }
                taskRecord.setValue(transaction, "dateowned", "now");
                return;
            }
        }
        else if (FERTIG_GEMELDET.equals(newStatus))
        {
            if (FERTIG_GEMELDET.equals(oldStatus) || IN_ARBEIT.equals(oldStatus))
            {
                if (!workable)
                {
                    throw new BusinessException("Sie d�rfen diesen Auftrag nicht fertig melden, weil er noch nicht bearbeitbar ist.");  
                }
                taskRecord.setValue(transaction, "dateresolved", "now");
                if (null == taskRecord.getValue("taskdone"))
                    taskRecord.setValue(transaction, "taskdone", "now");
                taskRecord.setValue(transaction, "resolved_user", Util.getUserLoginID(Context.getCurrent()));

                // den AK der Meldung �ber die Fertigmeldung unterrichten
                Notification.notifyOwner(taskRecord, transaction);

                // Haben wir schon den Endstatus erreicht?

                if (FERTIG_GEMELDET.equals(parameter.endStatus))
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
                // �berpr�fen ob Auftrag richtig dokumentiert ist
                Task.checkDocumented(transaction, taskRecord);
                taskRecord.setValue(transaction, "datedocumented", "now");

                // Haben wir schon den Endstatus erreicht?

                if (DOKUMENTIERT.equals(parameter.endStatus))
                {
                    taskRecord.setValue(transaction, "taskstatus", ABGESCHLOSSEN);
                    taskRecord.setValue(transaction, "dateclosed", "now");
                }
                return;
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

        // Status�bergang ist nicht m�glich

        // kein g�ltiger Status�bergang
        throw new BusinessException("Der Status�bergang von '" + oldStatus + "' zu '" + newStatus + "' ist nicht erlaubt.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
     *      de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
    {
        try
        {    
            // ------------------------------------------------------------
            // die notwendigen Parameter des ext. Systems holen
            // ------------------------------------------------------------
            ParameterContainer parameter = getExternalSystemParameter(taskRecord);
            
            // ------------------------------------------------------------
            // �berpr�fungen f�r neu angelegten Auftrag durchf�hren
            // ------------------------------------------------------------
            checkNewTask(taskRecord);

            // ------------------------------------------------------------
            // �berpr�fungen  der Arbeitszeiten
            // ------------------------------------------------------------
            checkTimeValues(taskRecord);

            // ------------------------------------------------------------
            // �berpr�fungen von Abrechnungssystem und Objekt
            // ------------------------------------------------------------
            checkTaskLinks(taskRecord);

            // ------------------------------------------------------------
            // Die diverse Status�berg�nge pr�fen
            // ------------------------------------------------------------
            checkTaskStatusChange(parameter,taskRecord, transaction);
            // ------------------------------------------------------------
            // Die diverse Workablestatus  pr�fen/setzen
            // ------------------------------------------------------------
            WorkableStatus(taskRecord, transaction);
            

            // ------------------------------------------------------------
            // Zum Abschlu� noch einige Felder initialisieren
            // ------------------------------------------------------------
            if (taskRecord.isNew())
            {
                taskRecord.setValue(transaction, "daterequested", "now");
                taskRecord.setValue(transaction, "create_user", Util.getUserLoginID(Context.getCurrent()));
                if (null == taskRecord.getValue("disruption_start"))
                    taskRecord.setValue(transaction, "disruption_start", new SimpleDateFormat("HH:mm").format(new Date()));
            }
            taskRecord.setValue(transaction, "datemodified", "now");
            taskRecord.setValue(transaction, "change_user", Util.getUserLoginID(Context.getCurrent()));
            // ------------------------------------------------------------
            // User in cclist schreiben
            // ------------------------------------------------------------
            //Util.TraceUser(taskRecord, transaction);

        }
        catch (BusinessException ex)
        {
            // Nummer sicher gehen und Status zur�cksetzen
            if (taskRecord.isUpdated())
                taskRecord.setValue(transaction, "taskstatus", taskRecord.getOldValue("taskstatus"));

            throw ex;
        }
    }



    /**
     * Wenn der Auftrag im Status fertig gemeldet oder Dokumentiert ist dann wird
     * versucht die Meldung des Auftrages automatisch zu schlie�en, sofern dies
     * m�glich ist.
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
        // automatisch dokumentieren der Meldung wenn gew�nscht:
        if (DOKUMENTIERT.equals(taskstatus) || ABGESCHLOSSEN.equals(taskstatus) )
        {
            // Meldung holen
            IDataTableRecord callRecord = taskRecord.getLinkedRecord("call");
            // und versuchen fertig zu melden
            CallTableRecord.tryToCloseCall(callRecord);          
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
        // Wenn ein Taskdatensatz neu erstellt oder ver�ndert wurde ..
        if (tableRecord.isNew() || tableRecord.isUpdated())
        {
            // und dieser mit dem aktuell angezeigten Datensatz �bereinstimmt
            IDataTable taskTable = tableRecord.getTable();
            if (tableRecord.equals(taskTable.getSelectedRecord()))
            {
                // dann lade den Datensatz neu von der Datenbank, da die Trigger Felder
                // ge�ndert haben k�nnten.
              //tableRecord=taskTable.reloadSelectedRecord();
              taskTable.reloadSelectedRecord();
            }
        }

        // Aktionsregeln implementieren 
        // 
        Notification.checkTaskAction(tableRecord);

        // ------------------------------------------------------------
        // Falls m�glich, Meldung fertig melden
        // Wichtige Anmerkung: Eine Methode AutoCallDocumented() ist nicht
        // notwendig, da dies schon implizit in den Call-Hooks passiert.
        // ------------------------------------------------------------
        checkAutoClosed(tableRecord);
        
        // Wenn sich Priorit�t,Status,Arbeitsgruppe, voraus. Ende �ndern
        // dann Eskalationen auswerten.
        // ------------------------------------------------------------
        if (tableRecord.hasChangedValue("priority")||tableRecord.hasChangedValue("taskstatus")
            ||tableRecord.hasChangedValue("estimateddone")||tableRecord.hasChangedValue("workgrouptask"))
            Escalation.check(tableRecord);

    }

 
  
    
}
