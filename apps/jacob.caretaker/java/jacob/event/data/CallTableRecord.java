/*
 * Created on 23.04.2004
 * 
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.common.Util;
import jacob.common.Yan;
import jacob.common.data.DataUtils;
import jacob.common.data.TaskTableRecord;
import jacob.common.sap.CSAPHelperClass;
import jacob.common.sap.CWriteSAPExchange;
import jacob.common.sap.CheckCallChange;
import jacob.exception.BusinessException;
import jacob.model.Appprofile;
import jacob.model.Call;
import jacob.model.Calls;
import jacob.model.Contract;
import jacob.model.Location;
import jacob.model.Locationcontract;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.util.ObjectUtil;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
/**
 * @author achim
 * 
 */
public class CallTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: CallTableRecord.java,v 1.82 2008/07/08 16:14:46 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.82 $";
  static protected final transient Log logger = AppLogger.getLogger();
  public static final String STATUS = "callstatus";

  // die unterschiedlichen Meldungsstatus
  public static final String RUECKRUF = "R�ckruf";
  public static final String DURCHGESTELLT = "Durchgestellt";
  public static final String VERWORFEN = "Verworfen";
  public static final String AK_ZUGEWIESEN = "AK zugewiesen";
  public static final String FEHLGEROUTET = "Fehlgeroutet";
  public static final String ANGENOMMEN = "Angenommen";
  public static final String FERTIG_GEMELDET = "Fertig gemeldet";
  public static final String FERTIG_AKZEPTIERT = "Fertig akzeptiert";
  public static final String DOKUMENTIERT = "Dokumentiert";
  public static final String GESCHLOSSEN = "Geschlossen";

  // Set welches alle Status vor "AK zugewiesen" enth�lt
  private static final Set allStatusBeforeAKZugewiesen = new HashSet();

  // Set welches alle Status vor "Angenommen" enth�lt
  private static final Set allStatusBeforeAngenommen = new HashSet();

  // Map{NewStatusString->Callback}
  private static final Map statusChangeCallbacks = new HashMap();

  // Map[Notificationgroup->Callback]
  private static final Map actionNotificatorCallbacks = new HashMap();

  static
  {
    // -------------------------------------------------------------------------------
    // Statusmaps initialisieren
    // -------------------------------------------------------------------------------
    allStatusBeforeAKZugewiesen.add(RUECKRUF);
    allStatusBeforeAKZugewiesen.add(DURCHGESTELLT);
    allStatusBeforeAKZugewiesen.add(VERWORFEN);

    allStatusBeforeAngenommen.addAll(allStatusBeforeAKZugewiesen);
    allStatusBeforeAngenommen.add(AK_ZUGEWIESEN);
    allStatusBeforeAngenommen.add(FEHLGEROUTET);

    // -------------------------------------------------------------------------------
    // Aktionshandler f�r das Versenden von Meldungen
    // -------------------------------------------------------------------------------
    actionNotificatorCallbacks.put("folgende Adresse", new ActionNotificatorAddresse());
    actionNotificatorCallbacks.put("Kunde", new ActionNotificatorKunde());
    actionNotificatorCallbacks.put("Mitarbeiter", new ActionNotificatorMitarbeiter());
    actionNotificatorCallbacks.put("Arbeitsgruppe", new ActionNotificatorArbeitsgruppe());
    // MIKE: Aufraeumen und �berpruefen ob ActionNotificatorArbeitsgruppeNM
    // n�tig
    // actionNotificatorCallbacks.put("NM Meisterei", new
    // ActionNotificatorArbeitsgruppeNM());
    actionNotificatorCallbacks.put("NM Meisterei", new ActionNotificatorArbeitsgruppe());

    // -------------------------------------------------------------------------------
    // Statuscallbacks initialisieren, d.h. Instanzen erzeugen und in Map
    // registrieren
    // -------------------------------------------------------------------------------

    // Verworfen-Callback
    statusChangeCallbacks.put(VERWORFEN, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {
        return RUECKRUF.equals(oldStatus);
      }
    });

    // R�ckruf-Callback
    statusChangeCallbacks.put(RUECKRUF, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {
        // RUECKRUF ist ein initialer Status, d.h. kann nu aus einem neuen Call
        // hervorgehen
        if (oldStatus == null)
        {
          callRecord.setValue(transaction, "datecallconnected", "now");
          // MIKE: Workaround beheben
          callRecord.setValue(transaction, "agentcall", Util.getUserKey(Context.getCurrent()));
          return true;
        }
        return false;
      }
    });

    // DURCHGESTELLT-Callback
    statusChangeCallbacks.put(DURCHGESTELLT, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {
        // DURCHGESTELLT ist ein initialer Status oder geht aus RUECKRUF hervor
        if (oldStatus == null || RUECKRUF.equals(oldStatus))
        {
          // Check out what to do
          // If status is being set back to New, reset all dates
          if (oldStatus != null)
          {
            callRecord.setValue(transaction, "dateassigned", null);
            callRecord.setValue(transaction, "dateresolved", null);
          }
          return true;
        }
        return false;
      }
    });

    // AK zugewiesen-Callback
    statusChangeCallbacks.put(AK_ZUGEWIESEN, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {
        if (oldStatus == null || FEHLGEROUTET.equals(oldStatus) || DURCHGESTELLT.equals(oldStatus))
        {
          // If there is no workgroup, set the status back to DURCHGESTELLT
          if (!callRecord.hasLinkedRecord("callworkgroup"))
          {
            callRecord.setValue(transaction, STATUS, DURCHGESTELLT);
            callRecord.setValue(transaction, "dateassigned", null);
          }
          else
          {
            callRecord.setValue(transaction, "dateassigned", "now");
          }

          // Frage an Mike:Ist dies noch notwendig? (Steht nicht in deinem
          // Dokument!)
          // Antwort Mike: Ja. So stellen wir sicher, dass eine SD Zeit
          // vorhanden ist
          if (callRecord.getintValue("sd_time") == 0 && callRecord.getValue("mastercall_key") == null)
          {
            Timestamp dDateConnected = callRecord.getTimestampValue("datecallconnected");
            Timestamp dDateAssigned = callRecord.getTimestampValue("dateassigned");
            // Achtung: durch 1000 dividieren da wir aus Millisekunden Sekunden
            // machen wollen
            long nSD_Time = (dDateAssigned.getTime() - dDateConnected.getTime()) / 1000;
            callRecord.setLongValue(transaction, "sd_time", nSD_Time);
          }
          return true;
        }
        return false;
      }
    });

    // Fehlgeroutet-Callback
    statusChangeCallbacks.put(FEHLGEROUTET, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {
        return AK_ZUGEWIESEN.equals(oldStatus);
      }
    });

    // Angenommen-Callback
    statusChangeCallbacks.put(ANGENOMMEN, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {
        // MIKE: Hier muss der Scheduled Job ber�cksichtigt werden
        // (bCalledFromRuleServer)
        // 
        if (oldStatus == null || AK_ZUGEWIESEN.equals(oldStatus) || FEHLGEROUTET.equals(oldStatus)
        /*
         * || bCalledFromRuleServer
         */
        )
        {
          // Nur Problemmanager d�rfen auch fehlgeroutete Meldungen annehmen
          if (FEHLGEROUTET.equals(oldStatus) && !transaction.getUser().hasRole("CQ_PM"))
          {
            return false;
          }
          callRecord.setValue(transaction, "dateowned", "now");
          return true;
        }
        return false;
      }
    });

    // Fertig gemeldet-Callback
    statusChangeCallbacks.put(FERTIG_GEMELDET, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {
        // Frage an Mike: M�ssen wir hier auch das Flag bCalledFromRuleServer
        // ber�cksichtigen? Mike : Nein, da ja der Ruleserver abgel�st wird.
        if (ANGENOMMEN.equals(oldStatus) /* || bCalledFromRuleServer */
        )
        {
          // Sind noch offene Auftr�ge da?
          if (checkForOpenTasks(callRecord))
          {
            throw new BusinessException("Sie m�ssen erst alle assoziierten Auftr�ge fertig melden bevor Sie die Meldung fertig melden.");
          }

          // Untermeldungen pr�fen
          if (checkForOpenSubcalls(callRecord))
          {
            throw new BusinessException("Sie m�ssen erst alle assoziierten Untermeldungen fertig melden bevor Sie diese Meldung fertig melden.");
          }

          callRecord.setValue(transaction, "dateresolved", "now");

          // Pr�fen, ob wir die Meldung auch gleich dokumentieren d�rfen
          if (callRecord.hasLinkedRecord("callworkgroup"))
          {
            IDataTableRecord iCallWorkgroupRec = callRecord.getLinkedRecord("callworkgroup");

            if ("Ja".equals(iCallWorkgroupRec.getValue("autodocumented")))
            {
              if (checkCallDocumented(callRecord, false))
              {
                callRecord.setValue(transaction, "datedocumented", "now");
                callRecord.setValue(transaction, STATUS, DOKUMENTIERT);
              }
            }
          }

          return true;
        }
        return false;
      }
    });

    // Dokumentiert-Callback
    statusChangeCallbacks.put(DOKUMENTIERT, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {
        // Frage an Mike: M�ssen wir hier auch das Flag bCalledFromRuleServer
        // ber�cksichtigen? Mike : Nein, da ja der Ruleserver abgel�st wird.
        if (FERTIG_GEMELDET.equals(oldStatus) || FERTIG_AKZEPTIERT.equals(oldStatus) /*
                                                                                       * ||
                                                                                       * bCalledFromRuleServer
                                                                                       */
        )
        {
          checkCallDocumented(callRecord, true);
          callRecord.setValue(transaction, "datedocumented", "now");
          return true;
        }
        return false;
      }
    });

    // Geschlossen-Callback
    statusChangeCallbacks.put(GESCHLOSSEN, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {
        // Frage an Mike: M�ssen wir hier auch das Flag bCalledFromRuleServer
        // ber�cksichtigen? Mike : Nein, da ja der Ruleserver abgel�st wird.
        if (DOKUMENTIERT.equals(oldStatus) /* || bCalledFromRuleServer */
        )
        {
          callRecord.setValue(transaction, "dateclosed", "now");
          return true;
        }
        return false;
      }
    });

  }

  /**
   * Pr�ft, ob die gegebene Meldung dokumentiert werden kann.
   * <p>
   * Dokumentiert geht nur wenn:
   * <li>1. keine Untermeldung im Status "R�ckruf|Durchgestellt|AK
   * zugewiesen|Fehlgeroutet|Angenommen|Fertig gemeldet" existiert
   * <li>2. es existieren keine Auftr�ger im Status
   * "Neu|Angelegt|Freigegeben|In Arbeit|Fertig gemeldet"
   * <li>3. If getAppProfileValue(iNetwork,"checkobjcategory") ="1" Then pr�fen
   * ob Objekt vorhanden ist sErrorMsg = "Sie m�ssen dem Objekt ein Gewerk
   * zuordnen um die Meldung zu dokumentieren"
   * 
   * @param callRecord
   *          die Meldung
   * @param throwBusinessException
   *          Wenn <code>true</code> wird eine entsprechende BusinessException
   *          geworfen, wenn die Bedingungen nicht erf�llt sind.
   * @return
   * @throws Exception
   */
  private static boolean checkCallDocumented(IDataTableRecord callRecord, boolean throwBusinessException) throws Exception
  {
    if (checkForUndocumentedTasks(callRecord))
    {
      if (throwBusinessException)
        throw new BusinessException("Es m�ssen erst alle Auftr�ge dokumentiert sein um die Meldung zu dokumentieren.");
      return false;
    }

    if (checkForUndocumentedSubcalls(callRecord))
    {
      if (throwBusinessException)
        throw new BusinessException("Es m�ssen erst alle Untermeldungen dokumentiert sein um die Meldung zu dokumentieren.");
      return false;
    }

    if (callRecord.hasLinkedRecord("object") && "1".equals(DataUtils.getAppprofileValue(callRecord.getAccessor(), "checkobjcategory")))
    {
      IDataTableRecord iObjectRec = callRecord.getLinkedRecord("object");
      Object sObjCategoryKey = iObjectRec.getValue("objectcategory_key");

      if (sObjCategoryKey == null)
      {
        if (throwBusinessException)
          throw new BusinessException("Sie m�ssen dem Objekt ein Gewerk zuordnen um die Meldung zu dokumentieren.");
        return false;
      }
    }

    return true;
  }

  /**
   * pr�ft ob die Meldung im 1stLevel geschlossen wurde
   * 
   * @param callRecord
   * @return
   * @throws Exception
   */
  private static boolean firstLevelClosedCall(IDataTableRecord callRecord) throws Exception
  {
    String isclosed = callRecord.getStringValue("firstlevelclosed");
    if (isclosed == null)
      return false;
    return (isclosed.equals("1"));
  }

  /**
   * pr�ft ob die Meldung aus SAP importiert wird
   * 
   * @param callRecord
   * @return
   * @throws Exception
   */
  private static boolean sapImported(IDataTableRecord callRecord) throws Exception
  {
    return callRecord.getintValue(Call.sapimport) == 1;
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

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /**
   * Diese Methode fa�t �berpr�fung bzgl. verlinkten Datens�tzen eines Calls
   * zusammen:
   * <li>AK
   * <li>Melder
   * <li>AK/Problemmanager Handling mit Status AK_ZUGEWIESEN bzw. FEHLGEROUTET
   * <li>T�tigkeit
   * <li>Gewerk
   * <li>Ort
   * <li>Call- und Objektpriorit�t Handling
   * <li>Agent
   * 
   * @param callRecord
   *          der Calldatensatz welcher ge�ndert werden soll
   * @param transaction
   *          die aktuelle Transaktion
   * @throws Exception
   *           bei irgendwelchen Fehlern
   */
  public static void checkCallLinks(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {
    // f�r MBTech pr�fen ob ein Objekt im Updatemodus ist.
    if (callRecord.hasLinkedRecord("object"))
    {
      IDataTableRecord objectRec = callRecord.getLinkedRecord("object");
      if (objectRec.getCurrentTransaction() != null)
      {
        throw new BusinessException("Das Objekt der Meldung muss erst gespeichert werden");
      }
    }
    // �berpr�fung ob ein Melder gesetzt ist
    if (!callRecord.hasLinkedRecord("customerint"))
    {
      throw new BusinessException("F�r diese Meldung m�ssen Sie erst einen Melder ausw�hlen");
    }

    // �berpr�fung ob ein AK gesetzt ist
    if (!callRecord.hasLinkedRecord("callworkgroup"))
    {
      throw new BusinessException("F�r diese Meldung m�ssen Sie erst einen AK ausw�hlen");
    }

    // Wenn der Status "fehlgeroutet" ist und sich der AK ge�ndert hat ..
    String sStatus = callRecord.getStringValue("callstatus");
    boolean hasStatusChanged = callRecord.hasChangedValue("callstatus");
    if (FEHLGEROUTET.equals(sStatus) && callRecord.hasChangedValue("workgroupcall"))
    {
      // und zuvor schon "fehlgeroutet" war ..
      if (!hasStatusChanged)
      {
        // dann nehme an, da� der neue AK nicht der Problemmanager ist
        callRecord.setValue(transaction, "callstatus", AK_ZUGEWIESEN);
        callRecord.setValue(transaction, "dateassigned", "now");
      }
    }

    // Wenn ein AK zugeordnet ist ..
    if (callRecord.getValue("workgroupcall") != null)
    {
      // dann setzte Status auf AK_ZUGEWIESEN, wenn der Call
      // in einem der beiden initialen Status ist
      if ("Neu".equals(sStatus) || DURCHGESTELLT.equals(sStatus))
      {
        callRecord.setValue(transaction, "callstatus", AK_ZUGEWIESEN);
        callRecord.setValue(transaction, "dateassigned", "now");
      }
    }

    // �berpr�fung ob eine T�tigkeit gesetzt ist
    if (!callRecord.hasLinkedRecord("process"))
    {
      throw new BusinessException("F�r diese Meldung m�ssen Sie erst ein T�tigkeit ausw�hlen");
    }

    // �berpr�fung ob ein Gewerk gesetzt ist
    if (!callRecord.hasLinkedRecord("category"))
    {
      throw new BusinessException("F�r diese Meldung m�ssen Sie erst ein Gewerk ausw�hlen");
    }

    // Wenn ein Ort nicht gesetzt ist ..
    if (!callRecord.hasLinkedRecord("location"))
    {
      // dann hole das Gewerk und �berpr�fe, ob f�r dieses ein Ort notwendig
      // ist
      IDataTableRecord categoryRecord = callRecord.getLinkedRecord("category");
      if ("Ja".equals(categoryRecord.getStringValue("locationrequired")))
      {
        throw new BusinessException("F�r diese Meldung m�ssen Sie erst einen Meldungsort angeben");
      }
    }
    else
    {
      // Ort ist gesetzt

      // und hat sich ge�ndert (sollte nur bei der Meldungserfassung vorkommen)
      if (callRecord.hasChangedValue("location_key"))
      {
        // dann pr�fe, ob es bereits einen anderen Call gibt, welcher mit
        // diesem Ort verkn�pft ist
        Object locationKey = callRecord.getValue("location_key");
        if (callRecord.getTable().exists("location_key", locationKey))
        {
          throw new BusinessException("Es mu� immer ein NEUER St�rungsort angegegeben werden. \\r\\nSt�rungsort nicht eindeutig in der Datenbank");
        }

        // pr�fe, ob der St�rungsort bereits gespeichert wurde
        // Anmerkung: Dies wird sehr h�ufig vergessen! Daher diese Meldung
        // anstatt einer schwer
        // verst�ndlichen Datenbank-Exception erzeugen.
        // Achtung: Dies funktioniert nur solange die Meldung und der
        // St�rungsort in unterschiedlichen
        // Transaktionen gespeichert werden!
        IDataTableRecord locationRecord = callRecord.getAccessor().getTable("location").getSelectedRecord();
        // Exception nur wenn Location nicht in der selben Transaction
        if (locationRecord != null && locationRecord.isNew() && transaction != locationRecord.getCurrentTransaction())
        {

          throw new BusinessException("Es mu� zuerst der St�rungsort gespeichert werden");
        }
      }
    }

    // Wenn der Datensatz neu ist und an der T�tigkeit der Hinweis auf
    // Objekt-Priori�t �bernehmen gesetzt ist ..
    // Achtung: Zuerst isNew() pr�fen, da getLinkedRecord() ein teurer Aufruf
    // sein kann!
    if (callRecord.isNew() && callRecord.getLinkedRecord("process").getintValue("considerpriority") == 1)
    {
      // und ein Objekt vorhanden ist ..
      IDataTableRecord objectRecord = callRecord.getLinkedRecord("object");
      if (null != objectRecord)
      {
        // dann �bernehme die Objektpriorit�t sofern diese gr��er ist
        // Hack: compareTo() funktioniert nur, da die Priorit�tsstrings immer
        // mit einer aufsteigenden Ziffer anfangen (z.B. "1-Normal",
        // "2-Kritisch", etc.)!
        String objectPriority = objectRecord.getStringValue("priority");
        String callPriority = callRecord.getStringValue("priority");
        if (null != callPriority && null != objectPriority && objectPriority.compareTo(callPriority) > 0)
        {
          callRecord.setValue(transaction, "priority", objectPriority);
        }
      }
    }

    // Wenn der Agent noch nicht gesetzt ist ..
    if (null == callRecord.getValue("agentcall"))
    {
      // dann mache den aktuellen User zum Agenten (Erfasser) des Calls
      //
      // Anmerkung: getUser().getKey() wirft eine Exception, sofern die
      // �nderungen von einem Hintergrundjob durchgef�hrt werden. Dies
      // ist nicht schlimm, da ein Hintergrundjob daf�r sorgen mu�, da�
      // der Agent vor dem Aufruf von Commit explizit gesetzt wird. Gleiches
      // gilt �brigens auch f�r den Melder!

      callRecord.setValue(transaction, "agentcall", Util.getUserKey(Context.getCurrent()));
    }
  }

  /**
   * Diese Methode �berpr�ft ob eine Verschiebung des Servicelevels einer
   * Meldung vorliegt und erlaubt ist.
   * 
   * @param iCallRec
   * @param transaction
   * @return <code>true</code> wenn eine Verschiebung des Servicelevels
   *         erfolgte und der Kunde benachrichtigt werden soll, ansonsten
   *         <code>false</code>
   * @throws Exception
   */
  public static boolean checkSL_KSL(IDataTableRecord iCallRec, IDataTransaction transaction) throws Exception
  {
    Timestamp sDate_sl_overdue = iCallRec.getTimestampValue("date_sl_overdue");
    Timestamp sOlddate_sl_overdue = (Timestamp) iCallRec.getOldValue("date_sl_overdue");
    boolean bHasChangeddate_sl_overdue = iCallRec.hasChangedValue("date_sl_overdue");
    boolean bHasOlddate_sl_overdue = bHasChangeddate_sl_overdue && sOlddate_sl_overdue != null;

    String sSl_overdue = iCallRec.getStringValue("sl_overdue");
    String sOldsl_overdue = (String) iCallRec.getOldValue("sl_overdue");
    boolean bHasChangedsl_overdue = iCallRec.hasChangedValue("sl_overdue");
    boolean bHasOldsl_overdue = bHasChangedsl_overdue && sOldsl_overdue != null;

    // Ein Begr�ndung ohne sichtbare Zeichen wird wie eine fehlende Begr�ndung
    // behandelt
    if (sSl_overdue != null && sSl_overdue.trim().length() == 0)
    {
      sSl_overdue = null;
    }

    if (iCallRec.getValue("sl") != null && bHasChangedsl_overdue && iCallRec.getValue("sl_overdue") != null)
    {

      // Pr�fen, ob der Servicelevel bereits abgelaufen ist ..
      // Anmerkung: der SL ist in Sekunden vermerkt und getTime() liefert
      // Millisekunden zur�ck
      Date now = new Date();
      Timestamp datereported = iCallRec.getTimestampValue("datereported");
      int sl = iCallRec.getintValue("sl");
      if (now.getTime() > datereported.getTime() + 1000L * sl)
      {
        // and setze ge�nderte Werte zur�ck und breche mit Meldung ab
        iCallRec.setValue(transaction, "date_sl_overdue", sOlddate_sl_overdue);
        iCallRec.setValue(transaction, "sl_overdue", sOldsl_overdue);
        throw new BusinessException("Eine Verschiebung des Servicelevels ist nach dem Ablauf der Frist nicht mehr m�glich.");
      }
    }

    else
    {

      // Wenn sl_overdue ge�ndert wird, aber kein Servicelevel f�r diese
      // Meldung definiert ist ..
      if (iCallRec.getValue("sl") == null && iCallRec.hasChangedValue("sl_overdue") && iCallRec.getValue("sl_overdue") != null)
      {
        // and setze ge�nderte Werte zur�ck und breche mit Meldung ab
        iCallRec.setValue(transaction, "date_sl_overdue", sOlddate_sl_overdue);
        iCallRec.setValue(transaction, "sl_overdue", sOldsl_overdue);
        throw new BusinessException("Eine Verschiebung des Servicelevels ist nicht n�tig da kein SL definiert ist.");
      }
    }

    if ((bHasChangeddate_sl_overdue || bHasChangedsl_overdue) && !ANGENOMMEN.equals(iCallRec.getValue("callstatus")))
    {
      // and setze ge�nderte Werte zur�ck und breche mit Meldung ab
      iCallRec.setValue(transaction, "date_sl_overdue", sOlddate_sl_overdue);
      iCallRec.setValue(transaction, "sl_overdue", sOldsl_overdue);
      throw new BusinessException("Der Servicelevel darf nur im Status 'Angenommen' verschoben werden.");
    }

    if ((bHasChangeddate_sl_overdue && bHasOlddate_sl_overdue))
    {
      // and setze ge�nderte Werte zur�ck und breche mit Meldung ab
      iCallRec.setValue(transaction, "date_sl_overdue", sOlddate_sl_overdue);
      iCallRec.setValue(transaction, "sl_overdue", sOldsl_overdue);
      throw new BusinessException("Der Servicelevel darf nur einmal verschoben werden.");
    }
    if ((bHasChangedsl_overdue && bHasOldsl_overdue))
    {
      // and setze ge�nderte Werte zur�ck und breche mit Meldung ab
      iCallRec.setValue(transaction, "date_sl_overdue", sOlddate_sl_overdue);
      iCallRec.setValue(transaction, "sl_overdue", sOldsl_overdue);
      throw new BusinessException("Die Begr�ndung warum der Servicelevel verschoben wurde, darf nur einmal ge�ndert werden.");
    }

    // �berpr�fen, da� nicht eine Begr�ndung ohne Verzugszeitpunkt bzw.
    // umgekehrt eingegeben wurde
    if (sSl_overdue == null && sDate_sl_overdue != null)
    {
      throw new BusinessException("Sie m�ssen eine Begr�ndung f�r den Verzug des Servicelevels eingeben.");
    }
    if (sSl_overdue != null && sDate_sl_overdue == null)
    {
      throw new BusinessException("Eine Begr�ndung ohne Datum der Wiederherstellung ist unzul�ssig.");
    }

    // �berpr�fen, ob der Verzug des Servicelevels ge�ndert wurde und in der
    // Vergangenheit liegt
    if (bHasChangeddate_sl_overdue && sDate_sl_overdue != null)
    {
      Date now = new Date();
      if (sDate_sl_overdue.getTime() < now.getTime())
      {
        // and setze ge�nderte Werte zur�ck und breche mit Meldung ab
        iCallRec.setValue(transaction, "date_sl_overdue", sOlddate_sl_overdue);
        iCallRec.setValue(transaction, "sl_overdue", sOldsl_overdue);
        throw new BusinessException("Der Verzug des Servicelevels kann nicht in der Vergangenheit liegen.");
      }
    }

    // wenn eine Verschiebung vorliegt, dann Kunde benachrichtigen
    return bHasChangedsl_overdue;
  }

  /**
   * @param iNetwork
   * @param iLocationRec
   * @return
   */
  private static Object getContractFromLocation(IDataTableRecord iLocationRec) throws Exception
  {
    IDataAccessor accessor = iLocationRec.getAccessor().newAccessor();

    Object sSite_key = iLocationRec.getValue(Location.site_key);
    Object sSitepart_key = iLocationRec.getValue(Location.sitepart_key);
    Object sBuilding_key = iLocationRec.getValue(Location.building_key);
    Object sBuildingpart_key = iLocationRec.getValue(Location.buildingpart_key);

    final String RELATIONSET = "r_locationcontract";

    // Achtung: Wir ber�cksichtigen nur Eintr�ge von Vertr�gen, welche aktiv
    // sind.
    //
    IDataTable iContractTable = accessor.getTable(Contract.NAME);
    iContractTable.qbeSetKeyValue(Contract.contractstatus, Contract.contractstatus_ENUM._aktiv);

    IDataTable iLocationContractTable = accessor.getTable(Locationcontract.NAME);
    int oldMaxRecords = iLocationContractTable.getMaxRecords();
    try
    {
      // for performance reasons -> limited result set!
      iLocationContractTable.setMaxRecords(2);

      // Suche nach site, sitepart, building, buildingpart
      iLocationContractTable.clear();
      iLocationContractTable.qbeClear();
      iLocationContractTable.qbeSetKeyValue(Locationcontract.site_key, sSite_key);
      iLocationContractTable.qbeSetKeyValue(Locationcontract.sitepart_key, sSitepart_key);
      iLocationContractTable.qbeSetKeyValue(Locationcontract.building_key, sBuilding_key);
      iLocationContractTable.qbeSetKeyValue(Locationcontract.buildingpart_key, sBuildingpart_key);
      iLocationContractTable.search(RELATIONSET);
      if (iLocationContractTable.recordCount() != 1)
      {
        // Suche nach site, sitepart, building
        iLocationContractTable.clear();
        iLocationContractTable.qbeClear();
        iLocationContractTable.qbeSetKeyValue(Locationcontract.site_key, sSite_key);
        iLocationContractTable.qbeSetKeyValue(Locationcontract.sitepart_key, sSitepart_key);
        iLocationContractTable.qbeSetKeyValue(Locationcontract.building_key, sBuilding_key);
        iLocationContractTable.qbeSetValue(Locationcontract.buildingpart_key, "NULL");
        iLocationContractTable.search(RELATIONSET);
        if (iLocationContractTable.recordCount() != 1)
        {
          // Suche nach site, sitepart
          iLocationContractTable.clear();
          iLocationContractTable.qbeClear();
          iLocationContractTable.qbeSetKeyValue(Locationcontract.site_key, sSite_key);
          iLocationContractTable.qbeSetKeyValue(Locationcontract.sitepart_key, sSitepart_key);
          iLocationContractTable.qbeSetValue(Locationcontract.building_key, "NULL");
          iLocationContractTable.qbeSetValue(Locationcontract.buildingpart_key, "NULL");
          iLocationContractTable.search(RELATIONSET);
          if (iLocationContractTable.recordCount() != 1)
          {
            // Suche nach site
            iLocationContractTable.clear();
            iLocationContractTable.qbeClear();
            iLocationContractTable.qbeSetKeyValue(Locationcontract.site_key, sSite_key);
            iLocationContractTable.qbeSetValue(Locationcontract.sitepart_key, "NULL");
            iLocationContractTable.qbeSetValue(Locationcontract.building_key, "NULL");
            iLocationContractTable.qbeSetValue(Locationcontract.buildingpart_key, "NULL");
            iLocationContractTable.search(RELATIONSET);
          }
        }
      }

      // Haben wir einen passenden Eintrag gefunden?
      if (iLocationContractTable.recordCount() == 1)
        return iLocationContractTable.getRecord(0).getValue(Locationcontract.contract_key);

      // Standardvertrag holen
      return DataUtils.getAppprofileValue(accessor, Appprofile.contract_key);
    }
    finally
    {
      // set back original value
      iLocationContractTable.setMaxRecords(oldMaxRecords);
    }
  }

  /**
   * Nur wenn sich Gewerk(category) oder T�tigkeit (process) ge�ndert hat dann
   * bestimmt diese Methode den Servicelevel mit dem Vertrag des Ortes oder
   * Default-Vertrag
   * 
   * @param callRecord
   * @param transaction
   * @throws Exception
   */
  public static void findSL_KSL(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {
    if (!(callRecord.hasChangedValue("categorycall") || callRecord.hasChangedValue("process_key") || callRecord.hasChangedValue("workgroupcall")))
    {
      // Weder das Gewerk noch die T�tigkeit haben sich ge�ndert
      return;
    }
    // First get the table from the network, and see if we have a record
    // selected.
    // Wenn kein Vertrag vorhanden ist, den Standardvertrag nehmen
    Object sContractKey = null;
    if (callRecord.hasLinkedRecord("location"))
    {
      sContractKey = getContractFromLocation(callRecord.getLinkedRecord("location"));
    }

    boolean bHasContract = sContractKey != null;

    // Standardvertrag
    Object sDefaultContractKey = DataUtils.getAppprofileValue(callRecord.getAccessor(), "contract_key");

    Object sCategoryKey = callRecord.getValue("categorycall");
    Object sProcessKey = callRecord.getValue("process_key");
    Object sAKkey = callRecord.getValue("workgroupcall");

    Object sCurrentCategoryKey = sCategoryKey;
    Object sCurrentContractKey = bHasContract ? sContractKey : sDefaultContractKey;

    IDataTableRecord iRoutingRecord = null;

    // ------------------------
    // Suchen des Routingsatzes
    // ------------------------
    IDataTable iCategoryTable = callRecord.getAccessor().getTable("category");
    IDataTable iRoutingTable = callRecord.getAccessor().getTable("categoryprocess");

    do
    {
      iRoutingTable.clear();
      iRoutingTable.qbeClear();
      iRoutingTable.qbeSetValue("category_key", sCurrentCategoryKey);
      iRoutingTable.qbeSetValue("process_key", sProcessKey);
      iRoutingTable.qbeSetValue("workgroup_key", sAKkey);
      iRoutingTable.qbeSetValue("contract_key", sCurrentContractKey);
      iRoutingTable.qbeSetValue("sl", "!NULL");
      iRoutingTable.search();
      if (iRoutingTable.recordCount() > 0)
      {
        // We have a record Lets Select selected.
        iRoutingRecord = iRoutingTable.getRecord(0);
      }
      else
      {
        // Wenn keine Abdeckung schauen ob Vatergewerk Abdeckung hat
        sCurrentCategoryKey = iCategoryTable.getRecord(new IDataKeyValue(sCurrentCategoryKey)).getValue("parentcategory_key");
        if (sCurrentCategoryKey == null)
        {
          // Ebene 1 des Gewerkebaums erreicht
          if (bHasContract)
          {
            // wenn nicht der spezielle Vertrag vielleicht der
            // Standardvertrag
            bHasContract = false;
            sCurrentCategoryKey = sCategoryKey;
            sCurrentContractKey = sDefaultContractKey;
          }
          else
          {
            // Nichts gefunden :-(
            callRecord.setValue(transaction, "sl", null);
            callRecord.setValue(transaction, "ksl", null);

            // und tsch�ss
            return;
          }
        }
      }
    }
    while (iRoutingRecord == null);

    callRecord.setValue(transaction, "sl", iRoutingRecord.getValue("sl"));
    callRecord.setValue(transaction, "ksl", iRoutingRecord.getValue("ksl"));
  }

  /**
   * Nachricht �ber SL verschieben in History schreiben
   * 
   * @param callRecord
   * @param transaction
   * @throws Exception
   */
  public static void writeSLOverdueToHistory(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {
    IDataTableRecord iRelRec = callRecord.getLinkedRecord("customerint");

    if (iRelRec == null)
    {
      // sollte nie passieren!
      logger.warn("There ist no customer for notifyCustomer during Sl overdue!");

    }
    String sMethod = callRecord.getStringValue("callbackmethod");
    String sAddr = "";

    if ("Email".equals(sMethod) || "Telefon".equals(sMethod) || "Keine".equals(sMethod))
    {
      sAddr = "Email an:" + iRelRec.getStringValue("emailcorr");
    }
    else if ("FAX".equals(sMethod))
    {
      sAddr = "FAX an: " + iRelRec.getStringValue("faxcorr");
    }
    else if ("SMS".equals(sMethod))
    {
      sAddr = "SMS an:" + iRelRec.getStringValue("pager");
    }

    // Hinweis dass Kunde benachrichtigt wurde in Historie hinterlegen
    callRecord.appendToHistory(transaction, "Kunde[" + sAddr + "] wird �ber die Verschiebung des Servicelevels informiert.");

  }

  /**
   * Kunde �ber Servicelevelverschiebung informieren. daf�r wird ein neuer Typ
   * in der Dokumentevorlage definiert.
   * 
   * @param callRecord
   * @param transaction
   * @throws Exception
   */
  private static void notifyCustomer(IDataTableRecord callRecord) throws Exception
  {
    IDataTableRecord iRelRec = callRecord.getLinkedRecord("customerint");
    if (iRelRec == null)
    {
      // sollte nie passieren!
      logger.warn("There ist no customer for notifyCustomer during Sl overdue!");
      return;
    }

    // Dokumentenvorlage holen!
    IDataTable doc_template = callRecord.getAccessor().getTable("doc_template");
    doc_template.qbeClear();
    doc_template.qbeSetKeyValue("use_in", "SL");
    if (doc_template.search() == 0)
    {
      logger.warn("There ist no doc_template for notifyCustomer during Sl overdue!");
      return;
    }
    // Dokumentvorlagedatensatz holen
    IDataTableRecord docRec = doc_template.getRecord(0);

    String sMethod = callRecord.getStringValue("callbackmethod");

    // Frage an Mike : Warum bekommt der Kunde hier bei Telefon und Keine eine
    // Email?
    // Antwort Mike: SL- verschieben, hat nichts mit dem normalen
    // Kundenfeedback zu tun
    // und der Kunde bei einer Verschiebung benachrichtigt werden muss
    String stylesheet;
    String sAddr;
    if ("Email".equals(sMethod) || "Telefon".equals(sMethod) || "Keine".equals(sMethod))
    {
      sAddr = "email://" + iRelRec.getStringValue("emailcorr");
      stylesheet = docRec.getStringValue("email_xsl");
    }
    else if ("FAX".equals(sMethod))
    {
      sAddr = "rightfax://" + iRelRec.getStringValue("faxcorr");
      stylesheet = docRec.getStringValue("fax_xsl");
    }
    else if ("SMS".equals(sMethod))
    {
      sAddr = "sms://" + iRelRec.getStringValue("pager");
      stylesheet = docRec.getStringValue("sms_xsl");
    }
    else
    {
      logger.warn("The callback method " + sMethod + "  ist not correct processed");
      return;
    }

    // XML-Vorlage f�r die Meldung holen und mit den Datenbankfeldern
    // auff�llen und in YAN schreiben
    //
    Context context = Context.getCurrent();
    String template = docRec.getLinkedRecord("xml_template").getStringValue("xmltext");
    template = Yan.fillDBFields(context, callRecord, docRec, template, true, null);
    Yan.createInstance(context, template, sAddr, stylesheet);
  }

  /**
   * Benachrichtigungen (eMAil, Fax, SMS, ..) auswerten und ausf�hren
   * 
   * @param callRecord
   * @param transaction
   * @throws Exception
   */
  private static void checkAction(IDataTableRecord callRecord) throws Exception
  {
    IDataTransaction transaction = callRecord.getAccessor().newTransaction();

    try
    {
      // Falls sich eines dieser Felder ge�ndert hat, muss die Aktionsregeln
      // ausgewertet werden.
      //
      if (callRecord.hasChangedValue("callstatus") || callRecord.hasChangedValue("workgroupcall"))
      {
        // 1. Aktionsregel finden
        //
        IDataTable action = callRecord.getAccessor().getTable("callaction");
        action.qbeClear();
        action.qbeSetValue("targettable", "Meldung");

        // wenn Meldung autodokumentiert, dann muss Status Fertig gemeldet auch
        // ber�ckdichtigt werden
        String callstatus = callRecord.getSaveStringValue("callstatus");
        if ("Dokumentiert".equals(callstatus) && "Angenommen".equals(callRecord.getOldValue("callstatus")))
        {
          action.qbeSetValue("statusevent", "Fertig gemeldet|Dokumentiert");
        }
        else
        {
          action.qbeSetValue("statusevent", callstatus);
        }

        if (callRecord.getValue("priority") != null)
          action.qbeSetValue("priority", "NULL|" + callRecord.getSaveStringValue("priority"));
        else
          action.qbeSetValue("priority", "NULL");

        if (callRecord.getValue("categorycall") != null)
          action.qbeSetValue("categoryaction", "NULL|" + callRecord.getSaveStringValue("categorycall"));
        else
          action.qbeSetValue("categoryaction", "NULL");

        if (callRecord.getValue("workgroupcall") != null)
        {
          action.qbeSetValue("groupaction", "NULL|" + callRecord.getSaveStringValue("workgroupcall"));
          IDataTableRecord workgroup = callRecord.getLinkedRecord("callworkgroup");
          action.qbeSetValue("migration", workgroup.getSaveStringValue("migration"));
        }
        else
          action.qbeSetValue("groupaction", "NULL");

        if (action.search() > 0)
        {
          if (logger.isDebugEnabled())
            logger.debug(action.recordCount() + " Aktionsregeln gefunden.!!!!!");
          for (int i = 0; i < action.recordCount(); i++)
          {
            IDataTableRecord currentAction = action.getRecord(i);
            String recipienType = currentAction.getSaveStringValue("recipient");
            ActionNotificator notificator = (ActionNotificator) actionNotificatorCallbacks.get(recipienType);
            if (notificator != null)
              notificator.send(callRecord, currentAction, transaction);
            else
              throw new BusinessException("Unbekannte (nicht implementierte) Benachrichtigungsart [" + recipienType + "]");
          }
        }
      }
      transaction.commit();
    }
    finally
    {
      transaction.close();
    }
  }

  /**
   * Folgende �berpr�fungen werden durchgef�hrt:
   * 
   * <li>Der Agent darf sich ab Status AK_ZUGEWIESEN nicht mehr �ndern.
   * <li>Der AK darf sich ab Status ANGENOMMEN nicht �ndern, es sei den der
   * neue AK geh�rt zur selben Organization bzw. Fremdfirma
   * <li>�ndert sich der AK und er wurde nicht mit dem Routing ermittelt, dann
   * "routinginfo" auf "von Hand geroutet" setzen
   * 
   * @param callRecord
   * @param transaction
   * @throws Exception
   */
  public static void checkValueChange(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {

    // ------------------------------------------------------------
    // Default f�r autoclosed setzen wenn sich der AK �ndert.
    // ------------------------------------------------------------
    if (callRecord.hasChangedValue("workgroupcall"))
    {
      callRecord.setValue(transaction, "autoclosed", callRecord.getLinkedRecord("callworkgroup").getValue("autoclosed"));
    }
    // ------------------------------------------------------------
    // Anderungen von einigen Feldern pr�fen.
    // ------------------------------------------------------------
    if (callRecord.isUpdated())
    {
      String status = callRecord.getStringValue(STATUS);
      // Der �bergang X zu 'wird verfolgt' im warrentystatus ist nur f�r den
      // Systemuser
      // m�glich
      if (callRecord.hasChangedValue("warrentystatus"))
      {
        if ("wird verfolgt".equals(callRecord.getSaveStringValue("warrentystatus")) && !Util.isSystemUser(Context.getCurrent()))
        {
          callRecord.setValue(transaction, "warrentystatus", callRecord.getOldValue("arrentystatus"));
          throw new BusinessException("Der Gew�hrleistungsstatus 'wird verfolgt' darf nur vom System gesetzt werden.");
        }
      }
      // Wenn sich der Agent ge�ndert hat und der Status nicht vor "AK
      // zugewiesen" ist
      if (callRecord.hasChangedValue("agentcall") && !allStatusBeforeAKZugewiesen.contains(status))
      {
        throw new BusinessException("�nderung von Agent ist nicht erlaubt.");
      }

      // Wenn sich der AK ge�ndert hat und der Status nicht vor "Angenommen"
      // ist ..
      if (callRecord.hasChangedValue("workgroupcall") && !allStatusBeforeAngenommen.contains(status))
      {
        // die beiden AK Datens�tze laden
        Object sKey = callRecord.getValue("workgroupcall");
        Object sOldKey = callRecord.getOldValue("workgroupcall");
        IDataTable iGroupTable = callRecord.getAccessor().getTable("callworkgroup");
        IDataTableRecord workgroupRecord = iGroupTable.getRecord(new IDataKeyValue(sKey));
        IDataTableRecord oldWorkgroupRecord = iGroupTable.getRecord(new IDataKeyValue(sOldKey));

        // pr�fen ob beide AKs zur selben Organisation geh�ren
        boolean sameOrganization = ObjectUtil.equalsIgnoreNull(workgroupRecord.getValue("organization_key"), oldWorkgroupRecord.getValue("organization_key"));
        boolean sameOrgExternal = ObjectUtil.equalsIgnoreNull(workgroupRecord.getValue("orgexternal_key"), oldWorkgroupRecord.getValue("orgexternal_key"));
        if (!sameOrganization && !sameOrgExternal)
        {
          // IBIS: Das Wiederherstellen funktioniert leider nicht
          callRecord.setValue(transaction, "routinginfo", callRecord.getOldValue("routinginfo"));
          throw new BusinessException("�nderung von Auftragskoordinator ist nicht erlaubt.");
        }
      }
    }
  }

  private static void checkCallStatusChange(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {
    String newStatus = callRecord.getStringValue(STATUS);
    String oldStatus = (String) callRecord.getOldValue(STATUS);

    // Verhindern, dass geschlossene Meldungen �berhaupt modifiziert werden
    // k�nnen
    if (GESCHLOSSEN.equals(oldStatus))
    {
      throw new BusinessException("Geschlossene Meldungen d�rfen nicht mehr ge�ndert werden.");
    }

    // Wenn keine Status�nderung, dann hier schon wieder aussteigen
    if (!callRecord.hasChangedValue(STATUS))
    {
      return;
    }

    // Ansonsten die Status�berg�nge �berpr�fen
    StatusChangeCallback callback = (StatusChangeCallback) statusChangeCallbacks.get(newStatus);
    if (null == callback)
    {
      // kann nur passieren, sofern wir einen Status vergessen haben :-)
      throw new RuntimeException("Unknown status '" + newStatus + "' (no rule present)");
    }
    try
    {
      // Anmerkung: Auch callback.check() kann eine BusinessException werfen!
      if (!callback.check(callRecord, transaction, oldStatus))
      {
        throw new BusinessException("Der Status�bergang von '" + oldStatus + "' zu '" + newStatus + "' ist nicht erlaubt.");
      }
    }
    catch (BusinessException ex)
    {
      // alten Status wieder herstellen
      callRecord.setValue(transaction, STATUS, oldStatus);
      throw ex;
    }
  }

  /**
   * L�schen von allen Alert-Datens�tzen von der gegebenen Meldung.
   * 
   * @param callRecord
   *          der Meldungsdatensatz
   * @param transaction
   *          die Transaktion
   * @throws Exception
   *           bei einem schwerwiegenden Fehler
   */
  private static void deleteAlerts(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {
    IDataTable alertTable = callRecord.getAccessor().getTable("qw_alert");
    alertTable.qbeClear();
    alertTable.qbeSetKeyValue("tablename", "call");
    alertTable.qbeSetKeyValue("tablekey", callRecord.getValue("pkey"));
    alertTable.qbeSetValue("alerttype", "Warnung");
    alertTable.fastDelete(transaction);
  }

  /**
   * Da man mit einem Trigger nicht andere Datens�tze auf der selben Tabelle
   * modifizieren kann, muss mit Hilfe dieser Methode die Koordinationszeiten
   * der Untermeldung auf die Hauptmeldung �bertragen werden.
   * <p>
   * Um sicher zu gehen, werden die Zeiten aus allen Untermeldungen neu
   * berechnet
   * 
   * @param iCallRec
   *          die (Unter-)Meldung
   * @throws Exception
   *           bei einem schwerwiegenden Fehler
   */
  private static void updateCoordinationtime(IDataTableRecord iCallRec) throws Exception
  {
    // sofern die Meldung eine Untermeldung ist und sich die
    // Koordinationszeiten ge�ndert haben
    if (iCallRec.hasLinkedRecord("callmaster") && (iCallRec.hasChangedValue("coordinationtime") || iCallRec.hasChangedValue("totalcalltime")))
    {
      // Hauptmeldung holen
      IDataTableRecord iCallmasterRec = iCallRec.getLinkedRecord("callmaster");

      // einen neuen Accessor erzeugen, damit die Client-seitige Darstellung
      // nicht ver�ndert wird.
      IDataAccessor accessor = iCallRec.getAccessor().newAccessor();

      // alle Untermeldungen suchen
      IDataTable iCallduplicateTable = accessor.getTable("callduplicate");
      iCallduplicateTable.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
      iCallduplicateTable.qbeSetKeyValue("mastercall_key", iCallmasterRec.getValue("pkey"));
      iCallduplicateTable.search();
      int nTotalCoordTimeSubcall = 0;
      int nTotalCallTimeSubCall = 0;
      int nCoordTime = iCallmasterRec.getintValue("coordinationtime");
      int nTotalCallTime = iCallmasterRec.getintValue("totalcalltime");
      for (int i = 0; i < iCallduplicateTable.recordCount(); i++)
      {
        nTotalCoordTimeSubcall += iCallduplicateTable.getRecord(i).getintValue("coordinationtime");
        nTotalCallTimeSubCall += iCallduplicateTable.getRecord(i).getintValue("totalcalltime");
      }

      // die neu berechneten Werte der Hauptmeldung setzen
      IDataTransaction transaction = accessor.newTransaction();
      try
      {
        iCallmasterRec.setIntValue(transaction, "sumcoordtime", nCoordTime + nTotalCoordTimeSubcall);
        iCallmasterRec.setIntValue(transaction, "totalcalltime", nTotalCallTime + nTotalCallTimeSubCall);
        transaction.commit();
      }
      catch (RecordLockedException ex)
      {
        // Pech gehabt: die Hauptmeldung ist gerade gesperrt
      }
      finally
      {
        transaction.close();
      }
    }
  }

  /**
   * Pr�ft ob f�r die gegebene Meldung noch offene Auftr�ge vorhanden sind
   * 
   * @param callRecord
   *          die Meldung
   * @return <code>true</code> es sind noch offene Auftr�ge vorhanden.
   * @throws Exception
   *           bei einem schwerwiegenden Fehler
   */
  private static boolean checkForOpenTasks(IDataTableRecord callRecord) throws Exception
  {
    // einen neuen Accessor erzeugen, damit die Client-seitige Darstellung
    // nicht ver�ndert wird.
    IDataAccessor accessor = callRecord.getAccessor().newAccessor();

    // Sind noch offene Auftr�ge da?
    IDataTable iTaskTable = accessor.getTable("task");
    iTaskTable.qbeSetKeyValue("calltask", callRecord.getValue("pkey"));
    iTaskTable.qbeSetValue("taskstatus", "In Arbeit|Neu|Angelegt|Freigegeben");
    return iTaskTable.exists();
  }

  /**
   * Pr�ft ob f�r die gegebene Meldung noch undokumentierte Auftr�ge vorhanden
   * sind
   * 
   * @param callRecord
   *          die Meldung
   * @return <code>true</code> es sind noch undokumentierte Auftr�ge
   *         vorhanden.
   * @throws Exception
   *           bei einem schwerwiegenden Fehler
   */
  public static boolean checkForUndocumentedTasks(IDataTableRecord callRecord) throws Exception
  {
    // einen neuen Accessor erzeugen, damit die Client-seitige Darstellung
    // nicht ver�ndert wird.
    IDataAccessor accessor = callRecord.getAccessor().newAccessor();

    // Sind noch undokumentierte Auftr�ge da?
    IDataTable iTaskTable = accessor.getTable("task");
    iTaskTable.qbeSetKeyValue("calltask", callRecord.getValue("pkey"));
    iTaskTable.qbeSetValue("taskstatus", "Neu|Angelegt|Freigegeben|In Arbeit|Fertig gemeldet");
    return iTaskTable.exists();
  }

  /**
   * Pr�ft ob f�r die gegebene Meldung noch offene Untermeldungen vorhanden sind
   * 
   * @param callRecord
   *          die Meldung
   * @return <code>true</code> es sind noch offene Untermeldungen vorhanden.
   * @throws Exception
   *           bei einem schwerwiegenden Fehler
   */
  private static boolean checkForOpenSubcalls(IDataTableRecord callRecord) throws Exception
  {
    // einen neuen Accessor erzeugen, damit die Client-seitige Darstellung
    // nicht ver�ndert wird.
    IDataAccessor accessor = callRecord.getAccessor().newAccessor();

    // Sind noch offene Untermeldungen da?
    IDataTable iCallduplicateTable = accessor.getTable("callduplicate");
    iCallduplicateTable.qbeSetKeyValue("mastercall_key", callRecord.getValue("pkey"));
    iCallduplicateTable.qbeSetValue("callstatus", "R�ckruf|Durchgestellt|AK zugewiesen|Fehlgeroutet|Angenommen");
    return iCallduplicateTable.exists();
  }

  /**
   * Pr�ft ob f�r die gegebene Meldung noch undokumentierte Untermeldungen
   * vorhanden sind
   * 
   * @param callRecord
   *          die Meldung
   * @return <code>true</code> es sind noch undokumentierte Untermeldungen
   *         vorhanden.
   * @throws Exception
   *           bei einem schwerwiegenden Fehler
   */
  public static boolean checkForUndocumentedSubcalls(IDataTableRecord callRecord) throws Exception
  {
    // einen neuen Accessor erzeugen, damit die Client-seitige Darstellung
    // nicht ver�ndert wird.
    IDataAccessor accessor = callRecord.getAccessor().newAccessor();

    // Sind noch undokumentierte Untermeldungen da?
    IDataTable iCallduplicateTable = accessor.getTable("callduplicate");
    iCallduplicateTable.qbeSetKeyValue("mastercall_key", callRecord.getValue("pkey"));
    iCallduplicateTable.qbeSetValue("callstatus", "R�ckruf|Durchgestellt|AK zugewiesen|Fehlgeroutet|Angenommen|Fertig gemeldet");
    return iCallduplicateTable.exists();
  }

  /**
   * Ist die gegebene Meldung eine Untermeldung und im Status fertig gemeldet
   * oder Dokumentiert dann wird versucht die Hauptmeldung automatisch zu
   * schlie�en.
   * 
   * @param iCallRec
   *          die (Unter-)Meldung
   * @throws Exception
   *           bei einem schwerwiegenden Fehler
   */
  private static void checkAutoClosed(IDataTableRecord iCallRec) throws Exception
  {
    String callStatus = iCallRec.getStringValue(STATUS);
    // auto dokumentiert der Untermeldung ist zu ber�cksichtigen
    if (iCallRec.hasLinkedRecord("callmaster") && (FERTIG_GEMELDET.equals(callStatus) || DOKUMENTIERT.equals(callStatus)))
    {
      // Hauptmeldung holen und gegebenenfalls fertig melden
      // Callmaster ist im falschen Alias neuer Accessor mit call alias
      // Callmaster mit dem call_update Hook zu versorgen geht nicht, weil
      // callmaster ohne Business Regel gebraucht wird in
      // callTaskSynchronizer.java
      IDataAccessor accessor = iCallRec.getAccessor().newAccessor();
      IDataTable callTable = accessor.getTable("call");
      callTable.qbeClear();
      callTable.qbeSetKeyValue("pkey", iCallRec.getStringValue("mastercall_key"));
      callTable.search();
      IDataTableRecord iCallmasterRec = callTable.getRecord(0);
      tryToCloseCall(iCallmasterRec);
    }
    if (iCallRec.hasLinkedRecord("callmaster") && DOKUMENTIERT.equals(callStatus))
    {
      // Hauptmeldung holen und gegebenfals dokumentieren
      // Callmaster ist im falschen Alias neuer Accessor mit call alias
      IDataAccessor accessor = iCallRec.getAccessor().newAccessor();
      IDataTable callTable = accessor.getTable("call");
      callTable.qbeClear();
      callTable.qbeSetKeyValue("pkey", iCallRec.getStringValue("mastercall_key"));
      callTable.search();
      IDataTableRecord iCallmasterRec = callTable.getRecord(0);
      tryToDocumentCall(iCallmasterRec);
    }

  }

  /**
   * Diese Methode �berpr�ft, ob die gegebene Meldung fertig gemeldet werden
   * kann und f�hrt dies gegebenenfalls auch durch.
   * 
   * @param iCallmasterRec
   *          die Meldung
   * @return <code>true</code> die Meldung wurde fertig gemeldet, ansonsten
   *         <code>false</code>
   * @throws Exception
   *           sofern w�hrend der �berpr�fung ein schwerwiegender Fehler auftrat
   */
  public static boolean tryToCloseCall(IDataTableRecord iCallmasterRec) throws Exception
  {
    // Meldung mu� im Status angenommen sein und die automatische
    // Fertigmeldung mu� aktiviert sein
    if ("Ja".equals(iCallmasterRec.getValue("autoclosed")) && ANGENOMMEN.equals(iCallmasterRec.getStringValue(STATUS)))
    {
      // Sind noch offene Auftr�ge da?
      if (checkForOpenTasks(iCallmasterRec))
      {
        // es existieren noch Auftr�ge im Status In Arbeit
        return false;
      }
      // Untermeldungen pr�fen
      if (checkForOpenSubcalls(iCallmasterRec))
      {
        // es existieren noch Untermeldungen, welche noch nicht
        // fertiggemeldet sind
        return false;
      }

      // und nun Hauptmeldung fertig melden
      IDataTransaction transaction = iCallmasterRec.getAccessor().newTransaction();
      try
      {
        iCallmasterRec.setValue(transaction, STATUS, FERTIG_GEMELDET);
        iCallmasterRec.setValue(transaction, "dateresolved", "now");
        transaction.commit();
        return true;
      }
      catch (RecordLockedException ex)
      {
        logger.debug(ex.toString());
        // Pech gehabt: die Meldung ist gerade gesperrt
      }
      finally
      {
        transaction.close();
      }
    }
    return false;
  }

  /**
   * Diese Methode �berpr�ft, ob die gegebene Meldung automatisch dokumentiert
   * werden kann und f�hrt dies gegebenenfalls auch durch.
   * 
   * @param callRecord
   *          die Meldung
   * @throws Exception
   *           sofern w�hrend der �berpr�fung ein schwerwiegender Fehler auftrat
   */
  public static void tryToDocumentCall(IDataTableRecord callRecord) throws Exception
  {
    // nur wenn die Meldung automatisch geschlossen werden soll
    if (!("Ja".equals(callRecord.getValue("autoclosed")) && FERTIG_GEMELDET.equals(callRecord.getStringValue(STATUS))))
      return;
    if (!CallTableRecord.checkForUndocumentedSubcalls(callRecord) && !CallTableRecord.checkForUndocumentedTasks(callRecord))
    {
      // Pr�fen, ob wir die Meldung dokumentieren d�rfen
      if (callRecord.hasLinkedRecord("callworkgroup"))
      {
        IDataTableRecord iCallWorkgroupRec = callRecord.getLinkedRecord("callworkgroup");

        if ("Ja".equals(iCallWorkgroupRec.getValue("autodocumented")))
        {

          IDataTransaction transaction = callRecord.getAccessor().newTransaction();
          try
          {
            callRecord.setValue(transaction, "callstatus", "Dokumentiert");
            callRecord.setValue(transaction, "datedocumented", "now");
            transaction.commit();

          }
          catch (RecordLockedException ex)
          {
            logger.debug(ex.toString());
            // Pech gehabt: die Meldung ist gerade gesperrt
          }
          catch (BusinessException ex)
          {
            logger.debug(ex.toString());
            // Pech gehabt: die Meldung l��t sich nicht dokumentieren
          }
          finally
          {
            transaction.close();
          }
        }
      }
    }
  }

  /**
   * Schreibt die Verweildauern in die entsprechende Tabelle.
   * 
   * @param callRecord
   * @param transaction
   * @throws Exception
   */
  private void writeCallDuration(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {
    if (!callRecord.hasChangedValue("workgroupcall"))
    {
      // AK hat sich nicht ge�ndert
      return;
    }

    boolean oldDurationFound = false;
    boolean bClosing = GESCHLOSSEN.equals(callRecord.getValue(STATUS));
    Object sOldGrpKey = callRecord.getOldValue("workgroupcall");
    Object sNewGrpKey = callRecord.getValue("workgroupcall");

    IDataTable iDurationTbl = callRecord.getAccessor().getTable("q_callduration");

    // um sicherzustellen, dass wir �berall denselben Zeitstempel verwenden
    Date now = new Date();

    // F�r neue Records brauchen wir nicht zu suchen
    if (!callRecord.isNew())
    {
      iDurationTbl.qbeClear();

      iDurationTbl.qbeSetValue("callcallduration", callRecord.getValue("pkey"));
      iDurationTbl.qbeSetValue("groupcallduration", sOldGrpKey);
      iDurationTbl.qbeSetValue("date_until", "NULL"); // !! only these
      iDurationTbl.search();
      for (int i = 0; i < iDurationTbl.recordCount(); i++)
      {
        IDataTableRecord iDurationRec = iDurationTbl.getRecord(i);
        iDurationRec.setValue(transaction, "date_until", now);
        iDurationRec.setValue(transaction, "newgroup", sNewGrpKey);
        iDurationRec.setIntValue(transaction, "isclosed", bClosing ? 1 : 0);
        oldDurationFound = true;
      }
    }

    // Wenn bereits ein Duration-Datensatz auf geschlossen gesetzt wurde,
    // k�nnen wir aufh�ren
    if (oldDurationFound && bClosing)
      return;

    IDataTableRecord iDurationRec = iDurationTbl.newRecord(transaction);
    iDurationRec.setValue(transaction, "callcallduration", callRecord.getValue("pkey"));
    iDurationRec.setValue(transaction, "groupcallduration", sNewGrpKey);
    iDurationRec.setValue(transaction, "oldgroup", sOldGrpKey);
    iDurationRec.setValue(transaction, "date_from", now);
    if (bClosing)
    {
      iDurationRec.setValue(transaction, "date_until", now);
      iDurationRec.setIntValue(transaction, "isclosed", 1);
    }
  }

  /**
   * Schreibt bei jeder �nderung eines Meldungsdatensatzes einen entsprechenden
   * Eintrag in die "callevent" Tabelle. Diese stellt eine auswertbare Historie
   * dar.
   * 
   * @param callRecord
   * @param transaction
   * @throws Exception
   */
  private static void writeEventRecord(IDataTableRecord iCallRec, IDataTransaction transaction) throws Exception
  {
    // neuen Eventdatensatz erzeugen und initialisieren
    IDataTable iEventTbl = iCallRec.getAccessor().getTable("callevent");
    IDataTableRecord iEventRec = iEventTbl.newRecord(transaction);
    iEventRec.setValue(transaction, "callcallevent", iCallRec.getValue("pkey"));
    iEventRec.setValue(transaction, "eventdate", "now");
    iEventRec.setValue(transaction, "creator", "#" + Util.getUserLoginID(Context.getCurrent()));

    // Hat sich der Meldungspriorit�t ge�ndert?
    if (iCallRec.hasChangedValue("priority"))
    {
      iEventRec.setValue(transaction, "priority", iCallRec.getValue("priority"));
    }

    // Hat sich der Meldungsstatus ge�ndert?
    if (iCallRec.hasChangedValue("callstatus"))
    {
      iEventRec.setValue(transaction, "callstatus", iCallRec.getValue("callstatus"));
    }

    // Hat sich der Melder ge�ndert?
    if (iCallRec.hasChangedValue("employeecall"))
    {
      IDataTableRecord record = iCallRec.getLinkedRecord("customerint");
      if (null != record)
      {
        iEventRec.setValue(transaction, "customer", record.getValue("fullname"));
      }
    }

    // Hat sich der AK ge�ndert?
    if (iCallRec.hasChangedValue("workgroupcall"))
    {
      IDataTableRecord record = iCallRec.getLinkedRecord("callworkgroup");
      if (null != record)
      {
        iEventRec.setStringValueWithTruncation(transaction, "workgroup", record.getSaveStringValue("name"));
      }
    }

    // Hat sich das Gewerk ge�ndert?
    if (iCallRec.hasChangedValue("categorycall"))
    {
      IDataTableRecord record = iCallRec.getLinkedRecord("category");
      if (null != record)
      {
        iEventRec.setValue(transaction, "category", record.getValue("name"));
      }
    }

    // Hat sich die T�tigkeit ge�ndert?
    if (iCallRec.hasChangedValue("process_key"))
    {
      IDataTableRecord record = iCallRec.getLinkedRecord("process");
      if (null != record)
      {
        iEventRec.setValue(transaction, "processname", record.getValue("processname"));
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {
    // ------------------------------------------------------------
    // Felder bei neuen Calls initialisieren
    // ------------------------------------------------------------
    if (callRecord.isNew())
    {
      callRecord.setValue(transaction, "coordinationtime", "00:00:00");
      callRecord.setValue(transaction, "totaltasktimespent", "00:00:00");
      callRecord.setValue(transaction, "totaltaskdoc", "00:00:00");
      // Wenn nicht null, dann ist es von einem anderen Hook gesetzt!
      if (callRecord.getValue("datereported") == null)
      {
        callRecord.setValue(transaction, "datereported", "now");
      }

      // Servicedeskzeit berechnen
      int sd_time = 0;
      Timestamp datecallconnected = callRecord.getTimestampValue("datecallconnected");
      if (datecallconnected != null)
      {
        Timestamp datereported = callRecord.getTimestampValue("datereported");

        // Anmerkung: getTime() liefert Millisekundem zur�ck und mu� deshalb
        // durch 1000 dividiert werden
        sd_time = (int) (datereported.getTime() - datecallconnected.getTime()) / 1000;
      }
      callRecord.setIntValue(transaction, "sd_time", sd_time);
      callRecord.setValue(transaction, Calls.sap_sm_nr, null);
      callRecord.setValue(transaction, Calls.sap_ssle_nr, null);
    }

    // pr�fen ob die Koordinationszeit im richtigen Format ist
    TaskTableRecord.checkMinutes("Koordinationsaufwand", callRecord.getStringValue("coordinationtime_m"));
    TaskTableRecord.checkHours("Koordinationsaufwand", callRecord.getStringValue("coordinationtime_h"));
    // ------------------------------------------------------------
    // Die diverse Status�berg�nge pr�fen
    // -----------------------------------------------------------

    // SAP: Import s. && !sapImported
    if (!firstLevelClosedCall(callRecord) && !sapImported(callRecord))
      checkCallStatusChange(callRecord, transaction);

    // ------------------------------------------------------------
    // Diverse �berpr�fungen bzgl. gelinkter Datens�tze durchf�hren
    // ------------------------------------------------------------
    checkCallLinks(callRecord, transaction);

    if (!firstLevelClosedCall(callRecord) && !sapImported(callRecord))
    {

      // ------------------------------------------------------------
      // �nderungen bzgl. AK und Agent �berpr�fen
      // ------------------------------------------------------------
      checkValueChange(callRecord, transaction);

      // ------------------------------------------------------------
      // �berpr�fungen bzgl. Servicelevelverschiebung durchf�hren und
      // gegebenenfalls Kunde informieren (in History schreiben).
      // Nachricht selbst wird im afterCommitAction geschrieben
      // MIKE: SL Overdue ist nicht sauber!
      // ------------------------------------------------------------
      if (checkSL_KSL(callRecord, transaction))
      {
        writeSLOverdueToHistory(callRecord, transaction);
      }

      // ------------------------------------------------------------
      // SL und KSL in den Callrecord eintragen, sofern notwendig
      // ------------------------------------------------------------
      findSL_KSL(callRecord, transaction);
    }

    if (sapImported(callRecord))
    {
      // Untermeldungen pr�fen
      if (checkForOpenSubcalls(callRecord))
      {
        throw new BusinessException("Sie m�ssen erst alle assoziierten Untermeldungen fertig melden bevor Sie diese Meldung fertig melden.");
      }
      // AB: �nderung wg Timestamps
      if (callRecord.getValue(Call.dateresolved) == null)
      {
        callRecord.setValue(transaction, "dateresolved", "now");
      }

      // Pr�fen, ob wir die Meldung auch gleich dokumentieren d�rfen
      if (callRecord.hasLinkedRecord("callworkgroup"))
      {
        IDataTableRecord iCallWorkgroupRec = callRecord.getLinkedRecord("callworkgroup");

        if ("Ja".equals(iCallWorkgroupRec.getValue("autodocumented")))
        {
          if (checkCallDocumented(callRecord, false))
          {
            if (callRecord.getValue(Call.datedocumented) == null)
            {
              callRecord.setValue(transaction, "datedocumented", "now");
            }
            callRecord.setValue(transaction, STATUS, DOKUMENTIERT);
          }
        }
      }
    }

    // ------------------------------------------------------------
    // Zum Schlu� noch die Modifikationsinformationen setzen
    // ------------------------------------------------------------
    callRecord.setValue(transaction, "datemodified", "now");
    callRecord.setValue(transaction, "modifiedby", Util.getUserLoginID(Context.getCurrent()));

    // ------------------------------------------------------------
    // Gegebenenfalls unn�tige Alerts l�schen wenn sich Status oder
    // Arbeitsgruppe �ndert
    // ------------------------------------------------------------
    if ((callRecord.hasChangedValue(STATUS) || callRecord.hasChangedValue("workgroupcall")) && callRecord.isUpdated())
      deleteAlerts(callRecord, transaction);

    // ------------------------------------------------------------
    // Verweilzeiten bestimmen und sofern n�tig speichern
    // ------------------------------------------------------------
    writeCallDuration(callRecord, transaction);

    // ------------------------------------------------------------
    // Auswertbare Historie schreiben
    // ------------------------------------------------------------
    writeEventRecord(callRecord, transaction);
    // ------------------------------------------------------------
    // User in cclist schreiben
    // ------------------------------------------------------------
    Util.TraceUser(callRecord, transaction);

    // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
    // SAP - Wert�nderungen pr�fen und in die �bertragungstabelle Schreiben
    // ------------------------------------------------------------

    if (callRecord.isUpdated() && CheckCallChange.HasChanged(callRecord))
    {
      CSAPHelperClass.printDebug("SAP-IsUpdate");
      // System.out.println("ContextProperty
      // "+Context.getCurrent().getProperty("SAPimport"));
      // Die �nderung wurde nicht durch den scheduled Job "SAPExchange" erzeugt
      if (null == (Context.getCurrent().getProperty("SAPimport")))
      {
        // Die �nderung wurde nicht durch den scheduled Job "SAPExchange"
        // erzeugt,
        // daher m�ssen sie an SAP �bertragen werden
        CWriteSAPExchange.updateExchangeCall(callRecord);
        // Wenn der Call geschlossen wurde, auch in SAP schlie�en
        // Das ist ein anderer Funktionsaufruf, daher erst die �nderungen
        // �bertragen und dann schlie�en.
        if (CheckCallChange.IsClosed(callRecord))
        {
          CWriteSAPExchange.closeExchangeCall(callRecord);
        }
      }

    }

    // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
    // SAP - Neuen Call in die �bertragungstabelle Schreiben
    // ------------------------------------------------------------

    if (callRecord.isNew())
    {
      WriteNewSapRec(callRecord);
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
    // Wenn ein Calldatensatz neu erstellt oder ver�ndert wurde ..
    if (tableRecord.isNew() || tableRecord.isUpdated())
    {
      // und dieser mit dem aktuell angezeigten Datensatz �bereinstimmt
      IDataTable callTable = tableRecord.getTable();
      if (tableRecord.equals(callTable.getSelectedRecord()))
      {
        // dann lade den Datensatz neu von der Datenbank, da die Trigger Felder
        // ge�ndert haben k�nnten.
        callTable.reloadSelectedRecord();
      }
    }

    // ------------------------------------------------------------
    // Summenzeiten der Hauptmeldung gerade ziehen
    // ------------------------------------------------------------
    updateCoordinationtime(tableRecord);

    // ------------------------------------------------------------
    // Falls m�glich, Hauptmeldung fertig melden
    // ------------------------------------------------------------
    checkAutoClosed(tableRecord);

    // ------------------------------------------------------------
    // Aktionsregeln (Benachrichtigngen) auswerten.
    // ------------------------------------------------------------
    if (!firstLevelClosedCall(tableRecord))
      checkAction(tableRecord);
    // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
    // Wenn SL verschoben (Benachrichtigngen) auswerten.
    // ------------------------------------------------------------
    if (tableRecord.hasChangedValue("sl_overdue"))
      notifyCustomer(tableRecord);
  }

  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * Diese Klasse schreibt f�r einen neuen, oder neu und sofort abgeschlossenen
   * Call-Datensatz die SAP Austauschdatens�tze zu dem Call-Datensatz
   * 
   * @author Achim B�ken
   */
  public static void WriteNewSapRec(IDataTableRecord callRecord) throws Exception
  {

    // ...und einen AK hat
    if (callRecord.hasLinkedRecord("callworkgroup"))
    {
      IDataTableRecord ak = callRecord.getLinkedRecord("callworkgroup");
      // ...und der AK ein SAP-AK ist
      if (ak.getintValue("sap_ak") == 1)
      {
        CSAPHelperClass.printDebug("SAP-IsNew And SAP AK");
        CWriteSAPExchange.createExchangeCall(callRecord);
        if (firstLevelClosedCall(callRecord))
        {
          CWriteSAPExchange.closeExchangeCall(callRecord);
        }
      }
      else
      {
        CSAPHelperClass.printDebug("SAP-IsNew no SAP AK");
      }
    }

  }

  /**
   * Diese abstrakte Klasse definiert eine Callbackmethode, welche
   * Status�berg�nge �berpr�ft. F�r jeden (neuen) Status wird jeweils eine
   * andere Klasse (anonyme Klasse) implementiert.
   * 
   * @author Andreas Sonntag
   */
  private static abstract class StatusChangeCallback
  {
    /**
     * �berpr�fung ob der Status�bergang g�ltig ist.
     * 
     * @param tableRecord
     * @param transaction
     * @param oldStatus
     * @return <code>true</code> Status�bergang ist in Ordnung, ansonsten
     *         <code>false</code>
     * @throws Exception
     */
    abstract boolean check(IDataTableRecord tableRecord, IDataTransaction transaction, String oldStatus) throws Exception;
  }

  /**
   * Dies klasse wird in Abh�ngigkeit der Benachrichtungsart erzeugt und
   * gerufen. <br>
   * Die erzeugte Instance (singleton) ist dann f�r das versenden der
   * Benachrichtigungen verantwortlich.
   * 
   * @author Andreas Herz
   */
  private static interface ActionNotificator
  {
    public void send(IDataTableRecord callRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception;
  }

  private static int callSeverity(String callPriority)
  {
    if (callPriority.equals("1-Normal"))
      return 0;
    if (callPriority.equals("2-Kritisch"))
      return 1;

    if (callPriority.equals("3-Produktion"))
      return 2;
    if (callPriority.equals("4-Notfall"))
      return 3;
    return 0;

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

      // wenn kein Template gelinked ist, eine Warnung Ausgeben:
      if (docTemplate == null)
      {
        throw new UserException("Aktionsregel mit ID =" + callAction.getSaveStringValue("pkey")
            + " hat keine Dokumentenvorlage.\\r\\nInformieren Sie den Administrator.");
      }

      // Entsprechend des Meldungskanals das Stylesheet und die
      // Empf�ngeradresse holen
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
        bookmark.setValue(transaction, "addressee", callAction.getSaveStringValue("notificationaddr"));
        bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
        bookmark.setStringValueWithTruncation(transaction, "message", document);
        bookmark.setValue(transaction, "tablekey", callRecord.getStringValue("pkey"));
        bookmark.setValue(transaction, "tablename", "call");
        bookmark.setValue(transaction, "alerttype", "Warnung");
        bookmark.setValue(transaction, "dateposted", "now");
        bookmark.setIntValue(transaction, "severity", callSeverity(callRecord.getSaveStringValue("priority")));
        return;
      }
      else
      {
        throw new BusinessException("Unbekannte (nicht implementierte) R�ckmeldungsart [" + channel + "] f�r Meldung.");
      }

      // XML-Vorlage f�r die Meldung holen und mit den Datenbankfeldern
      // auff�llen
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

      // XML-Vorlage f�r die Meldung holen und mit den Datenbankfeldern
      // auff�llen
      //
      String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
      template = Yan.fillDBFields(context, callRecord, docTemplate, template, true, null);

      // Entsprechend des Meldungskanals das Stylesheet und die
      // Empf�ngeradresse holen
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
      bookmark.setValue(transaction, "addressee", DataUtils.getAppprofileValue(context, "custfeedbacklogin"));
      bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
      bookmark.setStringValueWithTruncation(transaction, "message", document);
      bookmark.setValue(transaction, "tablekey", callRecord.getStringValue("pkey"));
      bookmark.setValue(transaction, "tablename", "call");
      bookmark.setValue(transaction, "alerttype", "Warnung");
      bookmark.setValue(transaction, "dateposted", "now");
      bookmark.setIntValue(transaction, "severity", callSeverity(callRecord.getSaveStringValue("priority")));
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

      // XML-Vorlage f�r die Meldung holen und mit den Datenbankfeldern
      // auff�llen
      //
      String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
      template = Yan.fillDBFields(context, callRecord, docTemplate, template, true, null);

      // Entsprechend des Meldungskanals das Stylesheet und die
      // Empf�ngeradresse holen
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
      String address = agent.getStringValue("loginname");
      if (address == null)
        return;
      bookmark.setValue(transaction, "addressee", address);
      bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
      bookmark.setStringValueWithTruncation(transaction, "message", document);
      bookmark.setValue(transaction, "tablekey", callRecord.getStringValue("pkey"));
      bookmark.setValue(transaction, "tablename", "call");
      bookmark.setValue(transaction, "alerttype", "Warnung");
      bookmark.setValue(transaction, "dateposted", "now");
      bookmark.setIntValue(transaction, "severity", callSeverity(callRecord.getSaveStringValue("priority")));
    }
  }

  /**
   * 
   * 
   */
  static class ActionNotificatorArbeitsgruppe implements ActionNotificator
  {

    static void notifyGroupmember(Context context, IDataTableRecord callRecord, IDataTableRecord workgroupRecord, IDataTableRecord docTemplate,
        IDataTableRecord callAction, IDataTransaction transaction) throws Exception
    {
      // �berpr�fen ob gegebenfalls Eigenbenachrichtigung der Gruppe vorliegt
      String UserKey = Util.getUserKey(context);
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
      template = Yan.fillDBFields(context, callRecord, docTemplate, template, true, null);
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
          String document = callAction.getSaveStringValue("subject");
          document = Yan.fillDBFields(context, callRecord, docTemplate, document, true, null);
          IDataTable table = context.getDataTable("qw_alert");
          IDataTableRecord bookmark = table.newRecord(transaction);
          String address = employee.getStringValue("loginname");
          if (address == null)
            continue;
          bookmark.setValue(transaction, "addressee", address);
          bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
          bookmark.setStringValueWithTruncation(transaction, "message", document);
          bookmark.setValue(transaction, "tablekey", callRecord.getStringValue("pkey"));
          bookmark.setValue(transaction, "tablename", "call");
          bookmark.setValue(transaction, "alerttype", "Warnung");
          bookmark.setValue(transaction, "dateposted", "now");
          bookmark.setIntValue(transaction, "severity", callSeverity(callRecord.getSaveStringValue("priority")));
        }
      }
    }

    public void send(IDataTableRecord callRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception
    {
      Context context = Context.getCurrent();
      // Dokumentenvorlage zu der Actionsregel holen
      //
      IDataTableRecord docTemplate = callAction.getLinkedRecord("doc_template");
      IDataTableRecord workgroup = callRecord.getLinkedRecord("callworkgroup");

      // XML-Vorlage f�r die Meldung holen und mit den Datenbankfeldern
      // auff�llen
      //
      String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
      template = Yan.fillDBFields(context, callRecord, docTemplate, template, true, null);

      // Entsprechend des Meldungskanals das Stylesheet und die
      // Empf�ngeradresse holen
      //
      String channel = workgroup.getSaveStringValue("notifymethod");
      String stylesheet;
      String recipienAddress;
      String Address = workgroup.getSaveStringValue("notificationaddr");

      if ("Bearbeiter".equals(channel))
      {
        notifyGroupmember(context, callRecord, workgroup, docTemplate, callAction, transaction);
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
      String pkey = callRecord.getStringValue("pkey");
      String document = callAction.getSaveStringValue("subject");
      document = Yan.fillDBFields(context, callRecord, docTemplate, document, true, null);
      IDataTable table = context.getDataTable("qw_alert");
      IDataTableRecord bookmark = table.newRecord(transaction);
      bookmark.setValue(transaction, "addressee", DataUtils.getAppprofileValue(context, "custfeedbacklogin"));
      bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
      bookmark.setValue(transaction, "message", "AK der Meldung " + pkey + " konnte nicht benachrichtigt werden");
      bookmark.setValue(transaction, "tablekey", pkey);
      bookmark.setValue(transaction, "tablename", "call");
      bookmark.setValue(transaction, "alerttype", "Warnung");
      bookmark.setValue(transaction, "dateposted", "now");
      bookmark.setIntValue(transaction, "severity", callSeverity(callRecord.getSaveStringValue("priority")));
    }

  }

  /**
   * 
   * 
   */
  static class ActionNotificatorArbeitsgruppeNM implements ActionNotificator
  {
    public void send(IDataTableRecord callRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception
    {
      Context context = Context.getCurrent();
      // Dokumentenvorlage zu der Actionsregel holen
      //
      IDataTableRecord docTemplate = callAction.getLinkedRecord("doc_template");
      IDataTableRecord workgroup = callRecord.getLinkedRecord("callworkgroup");

      // XML-Vorlage f�r die Meldung holen und mit den Datenbankfeldern
      // auff�llen
      //
      String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
      template = Yan.fillDBFields(context, callRecord, docTemplate, template, true, null);

      // Entsprechend des Meldungskanals das Stylesheet und die
      // Empf�ngeradresse holen
      //
      String channel = workgroup.getSaveStringValue("notifymethod");
      String stylesheet;
      String recipienAddress;
      String Address = workgroup.getSaveStringValue("notificationaddr");

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
      String pkey = callRecord.getStringValue("pkey");
      String document = callAction.getSaveStringValue("subject");
      document = Yan.fillDBFields(context, callRecord, docTemplate, document, true, null);
      IDataTable table = context.getDataTable("qw_alert");
      IDataTableRecord bookmark = table.newRecord(transaction);
      bookmark.setValue(transaction, "addressee", DataUtils.getAppprofileValue(context, "custfeedbacklogin"));
      bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
      bookmark.setValue(transaction, "message", "NM AK der Meldung " + pkey + " konnte nicht benachrichtigt werden");
      bookmark.setValue(transaction, "tablekey", pkey);
      bookmark.setValue(transaction, "tablename", "call");
      bookmark.setValue(transaction, "alerttype", "Warnung");
      bookmark.setValue(transaction, "dateposted", "now");
      bookmark.setIntValue(transaction, "severity", callSeverity(callRecord.getSaveStringValue("priority")));
    }

  }
}