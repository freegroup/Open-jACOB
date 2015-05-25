/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import java.util.HashMap;
import java.util.Map;

import jacob.common.AppLogger;
import jacob.common.ENUM;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserRuntimeException;

/**
 * 
 * @author mike
 */
public class CallTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: CallTableRecord.java,v 1.7 2005/11/02 12:47:08 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.7 $";

    static protected final transient Log logger = AppLogger.getLogger();

    static protected final String STATUS = "status";

    // Map{NewStatusString->Callback}
    private static final Map statusChangeCallbacks = new HashMap();

    static
    {
        statusChangeCallbacks.put(ENUM.CALLSTATUS_OPEN, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {
                return true;

            }
        });
        statusChangeCallbacks.put(ENUM.CALLSTATUS_ASSIGNED, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {
                if (!callRecord.hasLinkedRecord("workgroup"))
                    throw new UserException("Eine Meldung im Status " + ENUM.CALLSTATUS_ASSIGNED + " ben�tigt eine Gruppe");
                return true;

            }
        });
        statusChangeCallbacks.put(ENUM.CALLSTATUS_SCHEDULED, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {

                if (!(callRecord.hasLinkedRecord("workgroup") && callRecord.hasLinkedRecord("owner") && callRecord.getValue("solution") != null && callRecord
                        .getValue("suggestedfixdate") != null))
                    throw new UserException("Eine Meldung im Status " + ENUM.CALLSTATUS_SCHEDULED + " ben�tigt eine Gruppe und Bearbeiter\\n" + "sowie eine L�sung und ein Termin.");

                return true;

            }
        });
        statusChangeCallbacks.put(ENUM.CALLSTATUS_OWNED, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {
                if (!(callRecord.hasLinkedRecord("workgroup") && callRecord.hasLinkedRecord("owner")))
                    throw new UserException("Eine Meldung im Status " + ENUM.CALLSTATUS_OWNED + " ben�tigt eine Gruppe und Bearbeiter.");

                return true;

            }
        });
        statusChangeCallbacks.put(ENUM.CALLSTATUS_DONE, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {
                String statusConstraint = ENUM.TASKSTATUS_OPEN + "|" + ENUM.TASKSTATUS_ASSIGNED + "|" + ENUM.TASKSTATUS_OWNED + "|" + ENUM.TASKSTATUS_SCHEDULED;

                if (checkForOpenTasks(callRecord,statusConstraint))
                {
                    if (ENUM.CALLSTATUS_DONE.equals(oldStatus))
                    {
                        callRecord.setValue(transaction, STATUS, ENUM.CALLSTATUS_OWNED);
                    }
                    else
                        throw new UserException("Sie m�ssen erst alle assoziierten Auftr�ge fertig melden bevor Sie die Meldung fertig melden.");
                }
                if (callRecord.getValue("owner_key") == null)
                {
                    throw new UserException("Eine fertig gemeldete Meldung ben�tigt einen Bearbeiter");
                }
                callRecord.setValue(transaction, "date_qa", "now");
                return true;

            }
        });
        statusChangeCallbacks.put(ENUM.CALLSTATUS_CLOSED, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {

                // ist die Meldung Fertig gemeldet?
                if (!ENUM.CALLSTATUS_DONE.equals(oldStatus))
                {
                    throw new UserException("Nur fertig gemeldete Meldungen darf man schlie�en.");
                }
                // Sind noch offene Auftr�ge da?
                if (checkForOpenTasks(callRecord, "!" + ENUM.TASKSTATUS_CLOSED))
                {
                    throw new UserException("Sie m�ssen erst alle assoziierten Auftr�ge abschliessen bevor Sie die Meldung schlie�en.");
                }
                if (callRecord.getValue("owner_key") == null)
                {
                    throw new UserException("Eine geschlossene Meldung ben�tigt einen Bearbeiter");
                }
                callRecord.setValue(transaction, "datedone", "now");
                return true;

            }
        });
    }

    private static void checkCallLinks(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
    {

        // �berpr�fung ob ein Melder gesetzt ist

        if (!callRecord.hasLinkedRecord("caller"))
        {
            throw new UserException("F�r diese Meldung m�ssen Sie erst einen Melder ausw�hlen");
        }
        // �berpr�fung ob ein Gewerk gesetzt ist
        if (!callRecord.hasLinkedRecord("category"))
        {
            throw new UserException("F�r diese Meldung m�ssen Sie erst ein Objekt ausw�hlen");
        }

        // Wenn der Agent noch nicht gesetzt ist ..
        if (null == callRecord.getValue("agent_key"))
        {
            // dann mache den aktuellen User zum Agenten (Erfasser) des Calls
            //
            // Anmerkung: getUser().getKey() wirft eine Exception, sofern die
            // �nderungen von einem Hintergrundjob durchgef�hrt werden. Dies
            // ist nicht schlimm, da ein Hintergrundjob daf�r sorgen mu�, da�
            // der Agent vor dem Aufruf von Commit explizit gesetzt wird.
            // Gleiches
            // gilt �brigens auch f�r den Melder!

            callRecord.setValue(transaction, "agent_key", Context.getCurrent().getUser().getKey());
        }

        // Wenn ein AK ge�ndert wird Zeitstempel richtig setzen
        if (callRecord.hasChangedValue("workgroup_key"))
        {
            if (callRecord.getValue("workgroup_key") != null)
            {
                callRecord.setValue(transaction, "dateassigned", "now");
            }
            else
            {
                callRecord.setValue(transaction, "dateassigned", null);
            }
        }
        // Wenn ein AK ge�ndert wird Zeitstempel richtig setzen
        if (callRecord.hasChangedValue("owner_key"))
        {
            if (callRecord.getValue("owner_key") != null)
            {
                callRecord.setValue(transaction, "dateowned", "now");
            }
            else
            {
                callRecord.setValue(transaction, "dateowned", null);
            }
        }

    }

    private static void setCallStatus(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
    {
        String newStatus = callRecord.getStringValue(STATUS);
        // Hat der Datensatz einen Status CALLSTATUS_SCHEDULED?
        if (callRecord.getValue("solution") != null && callRecord.getValue("suggestedfixdate") != null && callRecord.hasLinkedRecord("owner")
                && ENUM.allCallStatusBeforeDone.contains(newStatus))
        {
            callRecord.setValue(transaction, STATUS, ENUM.CALLSTATUS_SCHEDULED);
        }
        else
        {
            // Hat der Datensatz einen Status CALLSTATUS_OWNED?
            if (callRecord.hasLinkedRecord("owner") && ENUM.allCallStatusBeforeDone.contains(newStatus))
            {
                callRecord.setValue(transaction, STATUS, ENUM.CALLSTATUS_OWNED);
            }
            else
            {
                // Hat der Datensatz einen Status CALLSTATUS_ASSIGEND?
                if (callRecord.hasLinkedRecord("workgroup") && ENUM.allCallStatusBeforeDone.contains(newStatus))
                {
                    callRecord.setValue(transaction, STATUS, ENUM.CALLSTATUS_ASSIGNED);
                }
                else
                {
                    if (ENUM.allCallStatusBeforeDone.contains(newStatus))
                    {
                        // Datensatz ist im Status CALLSTATUS_OPEN
                        callRecord.setValue(transaction, STATUS, ENUM.CALLSTATUS_OPEN);
                    }
                }
            }
        }
        // pr�fen ob der neue Status g�ltig ist.
        checkCallStatusChange(callRecord, transaction);
    }

    private static void checkCallStatusChange(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
    {
        String newStatus = callRecord.getStringValue(STATUS);
        String oldStatus = (String) callRecord.getOldValue(STATUS);

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
                throw new UserException("Der Status�bergang von '" + oldStatus + "' zu '" + newStatus + "' ist nicht erlaubt.");
            }
        }
        catch (UserException ex)
        {
            // alten Status wieder herstellen
            callRecord.setValue(transaction, STATUS, oldStatus);
            throw ex;
        }
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
    public static boolean checkForOpenTasks(IDataTableRecord callRecord, String constraint) throws Exception
    {
        // einen neuen Accessor erzeugen, damit die Client-seitige Darstellung
        // nicht ver�ndert wird.
        IDataAccessor accessor = callRecord.getAccessor().newAccessor();

        // Sind noch offene Auftr�ge da?
        IDataTable iTaskTable = accessor.getTable("task");
        iTaskTable.qbeSetKeyValue("call_key", callRecord.getValue("pkey"));
        iTaskTable.qbeSetValue(STATUS, constraint);
        return iTaskTable.exists();
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

        if (iCallRec.hasChangedValue(STATUS))
        {
            // neuen Eventdatensatz erzeugen und initialisieren
            IDataTable iEventTbl = iCallRec.getAccessor().getTable("callevent");
            IDataTableRecord iEventRec = iEventTbl.newRecord(transaction);
            iEventRec.setValue(transaction, "call_key", iCallRec.getValue("pkey"));
            iEventRec.setValue(transaction, "datecreated", "now");
            // iEventRec.setValue(transaction, "creator",
            // Context.getCurrent().getUser().getKey());

            iEventRec.setValue(transaction, "newstatus", iCallRec.getValue(STATUS));
            iEventRec.setValue(transaction, "newstatus", iCallRec.getOldValue(STATUS));
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
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
        IDataTable taskTable = tableRecord.getAccessor().getTable("task");
        taskTable.qbeClear();
        taskTable.qbeSetKeyValue("call_key", tableRecord.getValue("pkey"));
        if (taskTable.exists())
            throw new UserRuntimeException("Meldung kann nicht gel�scht werden, da Auftr�ge vorhanden sind");

        // delete all assigned call events as well
        //
        IDataTable callEventTable = tableRecord.getAccessor().getTable("callevent");
        callEventTable.qbeClear();
        callEventTable.qbeSetKeyValue("call_key", tableRecord.getValue("pkey"));
        callEventTable.fastDelete(transaction);

        // delete all assigned documents as well
        //
        IDataTable documentTable = tableRecord.getAccessor().getTable("documents");
        documentTable.qbeClear();
        documentTable.qbeSetKeyValue("call_key", tableRecord.getValue("pkey"));
        documentTable.searchAndDelete(transaction);
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
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
     *      de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
    {
        // ------------------------------------------------------------
        // Geschlossene Meldungen d�rfen nicht mehr ge�ndert werden
        // ------------------------------------------------------------

        if (ENUM.CALLSTATUS_CLOSED.equals((String)callRecord.getOldValue(STATUS)))
        {
            throw new UserException("Geschlossene Meldungen d�rfen nicht mehr ge�ndert werden.");
        }
        // ------------------------------------------------------------
        // Diverse �berpr�fungen bzgl. gelinkter Datens�tze durchf�hren
        // ------------------------------------------------------------
        checkCallLinks(callRecord, transaction);

        // ------------------------------------------------------------
        // Die diverse Status�berg�nge pr�fen
        // -----------------------------------------------------------

        setCallStatus(callRecord, transaction);

        // ------------------------------------------------------------
        // Auswertbare Historie schreiben
        // ------------------------------------------------------------
        writeEventRecord(callRecord, transaction);
        // ------------------------------------------------------------
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
