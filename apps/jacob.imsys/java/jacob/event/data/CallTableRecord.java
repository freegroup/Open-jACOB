/*
 * Created on 23.04.2004
 * 
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.common.Escalation;
import jacob.common.Notification;
import jacob.common.Util;
import jacob.common.Yan;
import jacob.common.data.DataUtils;
import jacob.common.data.Routing;
import jacob.exception.BusinessException;

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
    static public final transient String RCS_ID = "$Id: CallTableRecord.java,v 1.13 2006/05/15 13:50:57 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.13 $";

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

    // Set welches alle Status vor "Fertig gemeldet" enth�lt
    private static final Set allStatusBeforeFertigGemeldet = new HashSet();

    // Map{NewStatusString->Callback}
    private static final Map statusChangeCallbacks = new HashMap();

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

        allStatusBeforeFertigGemeldet.addAll(allStatusBeforeAngenommen);
        allStatusBeforeFertigGemeldet.add(ANGENOMMEN);

        // -------------------------------------------------------------------------------
        // Statuscallbacks initialisieren, d.h. Instanzen erzeugen und in Map
        // registrieren
        // -------------------------------------------------------------------------------

        // Verworfen-Callback
        statusChangeCallbacks.put(VERWORFEN, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
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

                return (allStatusBeforeAngenommen.contains(oldStatus) || ANGENOMMEN.equals(oldStatus));
            }
        });

        // R�ckruf-Callback
        statusChangeCallbacks.put(RUECKRUF, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {
                // RUECKRUF ist ein initialer Status, d.h. kann nu aus einem
                // neuen Call
                // hervorgehen
                if (oldStatus == null)
                {
                    callRecord.setValue(transaction, "datecallconnected", "now");
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
                // DURCHGESTELLT ist ein initialer Status oder geht aus RUECKRUF
                // hervor
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
                if (oldStatus == null || FEHLGEROUTET.equals(oldStatus) || DURCHGESTELLT.equals(oldStatus) || ANGENOMMEN.equals(oldStatus))
                {
                    // If there is no workgroup, set the status back to
                    // DURCHGESTELLT
                    if (!callRecord.hasLinkedRecord("callworkgroup"))
                    {
                        callRecord.setValue(transaction, STATUS, DURCHGESTELLT);
                        callRecord.setValue(transaction, "dateassigned", null);
                    }
                    else
                    {
                        callRecord.setValue(transaction, "dateassigned", "now");
                    }

                    // Frage an Mike:Ist dies noch notwendig? (Steht nicht in
                    // deinem
                    // Dokument!)
                    // Antwort Mike: Ja. So stellen wir sicher, dass eine SD
                    // Zeit
                    // vorhanden ist
                    if (callRecord.getintValue("sd_time") == 0 && callRecord.getValue("mastercall_key") == null)
                    {
                        Timestamp dDateConnected = callRecord.getTimestampValue("datecallconnected");
                        Timestamp dDateAssigned = callRecord.getTimestampValue("dateassigned");
                        if (dDateConnected == null || dDateAssigned == null)
                        {
                            callRecord.setLongValue(transaction, "sd_time", 0);
                        }
                        else
                        {
                            // Achtung: durch 1000 dividieren da wir aus
                            // Millisekunden Sekunden
                            // machen wollen
                            long nSD_Time = (dDateAssigned.getTime() - dDateConnected.getTime()) / 1000;
                            callRecord.setLongValue(transaction, "sd_time", nSD_Time);
                        }
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
                if (oldStatus == null || AK_ZUGEWIESEN.equals(oldStatus) || FEHLGEROUTET.equals(oldStatus))
                {
                    // Nur Problemmanager d�rfen auch fehlgeroutete Meldungen
                    // annehmen
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

                if (ANGENOMMEN.equals(oldStatus))
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

                    // Pr�fen, ob wir die Meldung auch gleich dokumentieren
                    // d�rfen
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
                if (FERTIG_GEMELDET.equals(oldStatus) || FERTIG_AKZEPTIERT.equals(oldStatus))
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
                if (DOKUMENTIERT.equals(oldStatus))
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
     * <li>3. If getAppProfileValue(iNetwork,"checkobjcategory") ="1" Then
     * pr�fen ob Objekt vorhanden ist sErrorMsg = "Sie m�ssen dem Objekt ein
     * Gewerk zuordnen um die Meldung zu dokumentieren"
     * 
     * @param callRecord
     *            die Meldung
     * @param throwBusinessException
     *            Wenn <code>true</code> wird eine entsprechende
     *            BusinessException geworfen, wenn die Bedingungen nicht erf�llt
     *            sind.
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
     * <li>AK/Problemmanager Handling mit Status AK_ZUGEWIESEN bzw.
     * FEHLGEROUTET
     * <li>T�tigkeit
     * <li>Gewerk
     * <li>Ort
     * <li>Call- und Objektpriorit�t Handling
     * <li>Agent
     * 
     * @param callRecord
     *            der Calldatensatz welcher ge�ndert werden soll
     * @param transaction
     *            die aktuelle Transaktion
     * @throws Exception
     *             bei irgendwelchen Fehlern
     */
    private static void checkCallLinks(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
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
        if (callRecord.isNew())
        {
            IDataTableRecord customer = callRecord.getLinkedRecord("customerint");
            callRecord.setValue(transaction, "callbackmethod", customer.getValue("communicatepref"));
            if (callRecord.getValue("mastercall_key") == null)
            {

                if (callRecord.getValue("accountingcodetext") == null)
                    callRecord.setValue(transaction, "accountingcodetext", customer.getValue("accountingcodecorr"));

                if (callRecord.getValue("affectedperson") == null)
                {
                    String value = customer.getStringValue("fullname") + " Tel: " + customer.getSaveStringValue("phonecorr");
                    callRecord.setValue(transaction, "affectedperson", value);
                }

                if (!callRecord.hasLinkedRecord("affectedperson"))
                {
                    IDataTable affectedperson = callRecord.getAccessor().getTable("affectedperson");
                    affectedperson.qbeClear();
                    affectedperson.qbeSetKeyValue("pkey", customer.getSaveStringValue("pkey"));
                    affectedperson.search();
                    if (affectedperson.recordCount()==1) //vielleicht ist der Melder ung�ltig!(Alis Conditions)
                      callRecord.setLinkedRecord(transaction, affectedperson.getRecord(0));
                }
            }
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
            // dann hole das Gewerk und �berpr�fe, ob f�r dieses ein Ort
            // notwendig
            // ist
            IDataTableRecord categoryRecord = callRecord.getLinkedRecord("category");
            IDataTableRecord processRecord = callRecord.getLinkedRecord("process");
            if ("Ja".equals(categoryRecord.getStringValue("locationrequired")) && "Ja".equals(processRecord.getStringValue("locationrequired")))
            {
                throw new BusinessException("F�r diese Meldung m�ssen Sie erst einen Meldungsort angeben");
            }
        }
        else
        {
            // Ort ist gesetzt

            // und hat sich ge�ndert (sollte nur bei der Meldungserfassung
            // vorkommen)
            if (callRecord.hasChangedValue("location_key"))
            {
                // dann pr�fe, ob es bereits einen anderen Call gibt, welcher
                // mit
                // diesem Ort verkn�pft ist
                Object locationKey = callRecord.getValue("location_key");
                if (callRecord.getTable().exists("location_key", locationKey))
                {
                    throw new BusinessException("Es mu� immer ein NEUER St�rungsort angegegeben werden. \\r\\nSt�rungsort nicht eindeutig in der Datenbank");
                }

                // pr�fe, ob der St�rungsort bereits gespeichert wurde
                // Anmerkung: Dies wird sehr h�ufig vergessen! Daher diese
                // Meldung
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
        // Achtung: Zuerst isNew() pr�fen, da getLinkedRecord() ein teurer
        // Aufruf
        // sein kann!
        if (callRecord.isNew() && callRecord.getLinkedRecord("process").getintValue("considerpriority") == 1)
        {
            // und ein Objekt vorhanden ist ..
            IDataTableRecord objectRecord = callRecord.getLinkedRecord("object");
            if (null != objectRecord)
            {
                // dann �bernehme die Objektpriorit�t sofern diese gr��er ist
                // Hack: compareTo() funktioniert nur, da die Priorit�tsstrings
                // immer
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
            // der Agent vor dem Aufruf von Commit explizit gesetzt wird.
            // Gleiches
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
    private static boolean checkSL_KSL(IDataTableRecord iCallRec, IDataTransaction transaction) throws Exception
    {
        Timestamp sDate_sl_overdue = iCallRec.getTimestampValue("date_sl_overdue");
        Timestamp sOlddate_sl_overdue = (Timestamp) iCallRec.getOldValue("date_sl_overdue");
        boolean bHasChangeddate_sl_overdue = iCallRec.hasChangedValue("date_sl_overdue");
        boolean bHasOlddate_sl_overdue = bHasChangeddate_sl_overdue && sOlddate_sl_overdue != null;

        String sSl_overdue = iCallRec.getStringValue("sl_overdue");
        String sOldsl_overdue = (String) iCallRec.getOldValue("sl_overdue");
        boolean bHasChangedsl_overdue = iCallRec.hasChangedValue("sl_overdue");
        boolean bHasOldsl_overdue = bHasChangedsl_overdue && sOldsl_overdue != null;

        // Ein Begr�ndung ohne sichtbare Zeichen wird wie eine fehlende
        // Begr�ndung
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
     * Nur wenn sich Gewerk(category) oder T�tigkeit (process) ge�ndert hat dann
     * bestimmt diese Methode den Servicelevel mit dem Vertrag des Ortes oder
     * Default-Vertrag
     * 
     * @param callRecord
     * @param transaction
     * @throws Exception
     */
    private static void findSL_KSL(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
    {
        if (!(callRecord.hasChangedValue("categorycall") || callRecord.hasChangedValue("process_key")))
        {
            // Weder das Gewerk noch die T�tigkeit haben sich ge�ndert
            return;
        }

        // First get the table from the network, and see if we have a record
        // selected.
        // Wenn kein Vertrag vorhanden ist, den Standardvertrag nehmen
        String sContractKey = null;
        if (callRecord.hasLinkedRecord("accountingcode"))
        {
            // wenn am Accountingcode einer h�ngt nehmen
            sContractKey = callRecord.getLinkedRecord("accountingcode").getStringValue("contract_key");
        }

        boolean bSetDefaultContract;
        boolean bHasContract;
        if (sContractKey == null)
        {
            bSetDefaultContract = true;
            bHasContract = false;
        }
        else
        {
            bSetDefaultContract = false;
            bHasContract = true;
        }

        // Standardvertrag
        Object sDefaultContractKey = DataUtils.getAppprofileValue(callRecord.getAccessor(), "contract_key");

        Object sCategoryKey = callRecord.getValue("categorycall");
        Object sProcessKey = callRecord.getValue("process_key");

        Object sCurrentCategoryKey = sCategoryKey;
        Object sCurrentContractKey = bHasContract ? sContractKey : sDefaultContractKey;

        IDataTableRecord iRoutingRecord = null;

        // ------------------------
        // Suchen des Routingsatzes
        // ------------------------
        IDataTable iCategoryTable = callRecord.getAccessor().getTable("category");
        IDataTable iRoutingTable = callRecord.getAccessor().getTable("contractsl");

        do
        {
            iRoutingTable.clear();
            iRoutingTable.qbeClear();
            iRoutingTable.qbeSetValue("category_key", sCurrentCategoryKey);
            iRoutingTable.qbeSetValue("process_key", sProcessKey);
            iRoutingTable.qbeSetValue("contract_key", sCurrentContractKey);
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
                        bSetDefaultContract = true;
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
    private static void writeSLOverdueToHistory(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
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
     * Folgende �berpr�fungen werden durchgef�hrt:
     * 
     * <li>Der Agent darf sich ab Status AK_ZUGEWIESEN nicht mehr �ndern.
     * <li>Der AK darf sich ab Status ANGENOMMEN nicht �ndern, es sei den der
     * neue AK geh�rt zur selben Organization bzw. Fremdfirma
     * <li>�ndert sich der AK und er wurde nicht mit dem Routing ermittelt,
     * dann "routinginfo" auf "von Hand geroutet" setzen
     * 
     * @param callRecord
     * @param transaction
     * @throws Exception
     */
    private static void checkValueChange(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
    {
        String status = callRecord.getStringValue(STATUS);
        // ------------------------------------------------------------
        // Default f�r autoclosed setzen wenn sich der AK �ndert.
        // ------------------------------------------------------------
        if (callRecord.hasChangedValue("workgroupcall") )
        {
          if (callRecord.hasLinkedRecord("callworkgroup"))
            callRecord.setValue(transaction,"autoclosed",callRecord.getLinkedRecord("callworkgroup").getValue("autoclosed"));
        }
        // ------------------------------------------------------------
        // Anderungen von einigen Feldern pr�fen.
        // ------------------------------------------------------------
        if (callRecord.isUpdated())
        {
            // Wenn sich der Agent ge�ndert hat und der Status nicht vor "AK
            // zugewiesen" ist
            if (callRecord.hasChangedValue("agentcall") && !allStatusBeforeAKZugewiesen.contains(status))
            {
                throw new BusinessException("�nderung von Agent ist nicht erlaubt.");
            }
            // Wenn sich der AK ge�ndert hat und der Status nicht vor "Fertig
            // gemeldet" ist
            // 
            if (callRecord.hasChangedValue("workgroupcall") && !allStatusBeforeFertigGemeldet.contains(status))
            {
                throw new BusinessException("�nderung des Auftragskoordinators ist nicht erlaubt.");
            }

            // Wenn sich der AK ge�ndert hat und der Status war angenommen, dann
            // neuen Ak akzeptieren
            // und Status af AK zugewiesen setzen
            // 
            if (callRecord.hasChangedValue("workgroupcall") && ANGENOMMEN.equals(status) && !callRecord.hasChangedValue(STATUS))
            {
                callRecord.setValue(transaction, STATUS, AK_ZUGEWIESEN);
                callRecord.setValue(transaction, "dateowned", null);
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
            // Anmerkung: Auch callback.check() kann eine BusinessException
            // werfen!
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
     *            der Meldungsdatensatz
     * @param transaction
     *            die Transaktion
     * @throws Exception
     *             bei einem schwerwiegenden Fehler
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
     * Pr�ft ob f�r die gegebene Meldung noch offene Auftr�ge vorhanden sind
     * 
     * @param callRecord
     *            die Meldung
     * @return <code>true</code> es sind noch offene Auftr�ge vorhanden.
     * @throws Exception
     *             bei einem schwerwiegenden Fehler
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
     *            die Meldung
     * @return <code>true</code> es sind noch undokumentierte Auftr�ge
     *         vorhanden.
     * @throws Exception
     *             bei einem schwerwiegenden Fehler
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
     * Pr�ft ob f�r die gegebene Meldung noch offene Untermeldungen vorhanden
     * sind
     * 
     * @param callRecord
     *            die Meldung
     * @return <code>true</code> es sind noch offene Untermeldungen vorhanden.
     * @throws Exception
     *             bei einem schwerwiegenden Fehler
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
     *            die Meldung
     * @return <code>true</code> es sind noch undokumentierte Untermeldungen
     *         vorhanden.
     * @throws Exception
     *             bei einem schwerwiegenden Fehler
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
     *            die (Unter-)Meldung
     * @throws Exception
     *             bei einem schwerwiegenden Fehler
     */
    private static void checkAutoClosed(IDataTableRecord iCallRec) throws Exception
    {
        String callStatus = iCallRec.getStringValue(STATUS);
        // auto dokumentiert der Untermeldung ist zu ber�cksichtigen
        if (iCallRec.hasLinkedRecord("callmaster") && (FERTIG_GEMELDET.equals(callStatus)))
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
     *            die Meldung
     * @return <code>true</code> die Meldung wurde fertig gemeldet, ansonsten
     *         <code>false</code>
     * @throws Exception
     *             sofern w�hrend der �berpr�fung ein schwerwiegender Fehler
     *             auftrat
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
     *            die Meldung
     * @throws Exception
     *             sofern w�hrend der �berpr�fung ein schwerwiegender Fehler
     *             auftrat
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
                        // Pech gehabt: die Meldung l��t sich nicht
                        // dokumentieren

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
     * Schreibt bei jeder �nderung eines Meldungsdatensatzes einen
     * entsprechenden Eintrag in die "callevent" Tabelle. Diese stellt eine
     * auswertbare Historie dar.
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
                iEventRec.setValue(transaction, "workgroup", record.getValue("name"));
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
            callRecord.setValue(transaction, "datereported", "now");

            // Servicedeskzeit berechnen
            int sd_time = 0;
            Timestamp datecallconnected = callRecord.getTimestampValue("datecallconnected");
            if (datecallconnected != null)
            {
                Timestamp datereported = callRecord.getTimestampValue("datereported");

                // Anmerkung: getTime() liefert Millisekundem zur�ck und mu�
                // deshalb
                // durch 1000 dividiert werden
                sd_time = (int) (datereported.getTime() - datecallconnected.getTime()) / 1000;
            }
            callRecord.setIntValue(transaction, "sd_time", sd_time);
        }

        // pr�fen ob die Koordinationszeit im richtigen Format ist
        TaskTableRecord.checkMinutes("Koordinationsaufwand", callRecord.getStringValue("coordinationtime_m"));
        TaskTableRecord.checkHours("Koordinationsaufwand", callRecord.getStringValue("coordinationtime_h"));

        // ------------------------------------------------------------
        // Diverse �berpr�fungen bzgl. gelinkter Datens�tze durchf�hren
        // ------------------------------------------------------------
        checkCallLinks(callRecord, transaction);

        if (!firstLevelClosedCall(callRecord))
        {
            // ------------------------------------------------------------
            // Die diverse Status�berg�nge pr�fen
            // -----------------------------------------------------------
            checkCallStatusChange(callRecord, transaction);

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
        // Util.TraceUser(callRecord, transaction);

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
                // dann lade den Datensatz neu von der Datenbank, da die Trigger
                // Felder
                // ge�ndert haben k�nnten.
                callTable.reloadSelectedRecord();
            }
        }

        // ------------------------------------------------------------
        // Falls m�glich, Hauptmeldung fertig melden
        // ------------------------------------------------------------
        checkAutoClosed(tableRecord);

        // ------------------------------------------------------------
        // Aktionsregeln (Benachrichtigngen) auswerten.
        // ------------------------------------------------------------
        if (!firstLevelClosedCall(tableRecord))
            Notification.checkCallAction(tableRecord);
        // ------------------------------------------------------------
        // Wenn SL verschoben (Benachrichtigngen) auswerten.
        // ------------------------------------------------------------
        if (tableRecord.hasChangedValue("sl_overdue"))
            notifyCustomer(tableRecord);
        // ------------------------------------------------------------
        // Wenn sich Priorit�t,Status,Arbeitsgruppe, voraus. Ende �ndern
        // dann Eskalationen auswerten.
        // ------------------------------------------------------------
        if (tableRecord.hasChangedValue("priority")||tableRecord.hasChangedValue("callstatus")
            ||tableRecord.hasChangedValue("estimateddone")||tableRecord.hasChangedValue("workgroupcall"))
            Escalation.check(tableRecord);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

}
