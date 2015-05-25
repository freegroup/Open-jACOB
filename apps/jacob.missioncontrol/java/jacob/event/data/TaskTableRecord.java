/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.common.ENUM;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.UserException;

/**
 * 
 * @author mike
 */
public class TaskTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: TaskTableRecord.java,v 1.7 2005/11/02 12:47:08 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.7 $";

    static protected final transient Log logger = AppLogger.getLogger();

    public static final String STATUS = "status";

    // Map{NewStatusString->Callback}
    private static final Map statusChangeCallbacks = new HashMap();

    static
    {
        statusChangeCallbacks.put(ENUM.TASKSTATUS_OPEN, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord taskRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {
                return true;

            }
        });
        statusChangeCallbacks.put(ENUM.TASKSTATUS_ASSIGNED, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord taskRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {
                if (!taskRecord.hasLinkedRecord("thirdlevel"))
                    throw new UserException("Ein Auftrag im Status " + ENUM.TASKSTATUS_ASSIGNED + " ben�tigt einen Thirdlevel");
                return true;

            }
        });
        statusChangeCallbacks.put(ENUM.TASKSTATUS_SCHEDULED, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord taskRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {
                if (!(taskRecord.hasLinkedRecord("thirdlevel") && taskRecord.hasLinkedRecord("taskowner") && taskRecord.getValue("solution") != null && taskRecord
                        .getValue("suggestedfixdate") != null))
                    throw new UserException("Ein Auftrag im Status " + ENUM.TASKSTATUS_SCHEDULED + " ben�tigt einen Thirdlevel und Bearbeiter\\n" + "sowie eine L�sung mit Termin.");

                return true;

            }
        });
        statusChangeCallbacks.put(ENUM.TASKSTATUS_OWNED, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord taskRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {
                if (!(taskRecord.hasLinkedRecord("thirdlevel") && taskRecord.hasLinkedRecord("taskowner")))
                    throw new UserException("Ein Auftrag im Status " + ENUM.CALLSTATUS_OWNED + " ben�tigt einen Thirdlevel und einen Bearbeiter.");

                return true;

            }
        });
        statusChangeCallbacks.put(ENUM.TASKSTATUS_DONE, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord taskRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {

                if (taskRecord.getValue("taskowner_key") == null)
                {
                    throw new UserException("Eine fertig gemeldeter Auftrag ben�tigt einen Bearbeiter");
                }

                taskRecord.setValue(transaction, "date_qa", "now");
                return true;

            }
        });
        statusChangeCallbacks.put(ENUM.TASKSTATUS_CLOSED, new StatusChangeCallback()
        {
            boolean check(IDataTableRecord taskRecord, IDataTransaction transaction, String oldStatus) throws Exception
            {
                if (!ENUM.TASKSTATUS_DONE.equals(oldStatus))
                {
                    throw new UserException("Nur fertig gemeldete Auftr�ge darf man schlie�en.");
                }
                if (taskRecord.getValue("taskowner_key") == null)
                {
                    throw new UserException("Eine abgeschlossener Auftrag ben�tigt einen Bearbeiter");
                }

                taskRecord.setValue(transaction, "datedone", "now");
                return true;

            }
        });
    }

    private static void checkTaskLinks(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
    {

        // �berpr�fung ob ein Melder gesetzt ist
        if (!taskRecord.hasLinkedRecord("call"))
        {
            throw new UserException("F�r diesen Auftrag m�ssen Sie erst eine Meldung ausw�hlen");
        }
        else
        {
            IDataTableRecord callRecord = taskRecord.getLinkedRecord("call");
            if (ENUM.CALLSTATUS_CLOSED.equals(callRecord.getSaveStringValue("status"))&& taskRecord.isNew())
                throw new UserException("F�r abgeschlossene Meldungen d�rfen keine Auftr�ge angelegt werden.");  
        }

        // Wenn ein thirdlevel zugeordnet ist ..
        if (taskRecord.hasChangedValue("thirdlevel_key"))
        {
            if (taskRecord.getValue("thirdlevel_key") != null)
            {
                taskRecord.setValue(transaction, "dateassigned", "now");
            }
            else
            {
                taskRecord.setValue(transaction, "dateassigned", null);
            }
        }

        // Wenn ein Owner zugeordnet ist ..
        if (taskRecord.hasChangedValue("taskowner_key"))
        {
            if (taskRecord.getValue("taskowner_key") != null)
            {
                taskRecord.setValue(transaction, "dateowned", "now");
            }
            else
            {
                taskRecord.setValue(transaction, "dateowned", null);
            }
        }

    }

    private static void setTaskStatus(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
    {
        String newStatus = taskRecord.getStringValue(STATUS);
        // Hat der Datensatz einen Status CALLSTATUS_SCHEDULED?
        if (taskRecord.getValue("solution") != null && taskRecord.getValue("suggestedfixdate") != null && taskRecord.hasLinkedRecord("taskowner")
                && ENUM.allTaskStatusBeforeDone.contains(newStatus))
        {
            taskRecord.setValue(transaction, STATUS, ENUM.TASKSTATUS_SCHEDULED);
        }
        else
        {
            // Hat der Datensatz einen Status CALLSTATUS_OWNED?
            if (taskRecord.hasLinkedRecord("taskowner") && ENUM.allTaskStatusBeforeDone.contains(newStatus))
            {
                taskRecord.setValue(transaction, STATUS, ENUM.TASKSTATUS_OWNED);
            }
            else
            {
                // Hat der Datensatz einen Status CALLSTATUS_ASSIGEND?
                if (taskRecord.hasLinkedRecord("thirdlevel") && ENUM.allTaskStatusBeforeDone.contains(newStatus))
                {
                    taskRecord.setValue(transaction, STATUS, ENUM.TASKSTATUS_ASSIGNED);
                }
                else
                {
                    if (ENUM.allTaskStatusBeforeDone.contains(newStatus))
                    {
                        // Datensatz ist im Status CALLSTATUS_OPEN
                        taskRecord.setValue(transaction, STATUS, ENUM.TASKSTATUS_OPEN);
                    }
                }
            }
        }
        // pr�fen ob der neue Status g�ltig ist.
        checkTaskStatusChange(taskRecord, transaction);
    }

    private static void checkTaskStatusChange(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
    {
        String newStatus = taskRecord.getStringValue(STATUS);
        String oldStatus = (String) taskRecord.getOldValue(STATUS);

        // Verhindern, dass geschlossene Meldungen �berhaupt modifiziert werden
        // k�nnen
        if (ENUM.TASKSTATUS_CLOSED.equals(oldStatus))
        {
            throw new UserException("Geschlossene Auftr�ge d�rfen nicht mehr ge�ndert werden.");
        }

        // Wenn keine Status�nderung, dann hier schon wieder aussteigen
        if (!taskRecord.hasChangedValue(STATUS))
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
            if (!callback.check(taskRecord, transaction, oldStatus))
            {
                throw new UserException("Der Status�bergang von '" + oldStatus + "' zu '" + newStatus + "' ist nicht erlaubt.");
            }
        }
        catch (UserException ex)
        {
            // alten Status wieder herstellen
            taskRecord.setValue(transaction, STATUS, oldStatus);
            throw ex;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    public void afterCommitAction(IDataTableRecord taskRecord) throws Exception
    {
        String taskstatus = taskRecord.getStringValue(STATUS);
        // Meldung holen
        IDataTableRecord callRecord = taskRecord.getLinkedRecord("call");
        String callStatus = callRecord.getSaveStringValue(STATUS);
        if (ENUM.TASKSTATUS_DONE.equals(taskstatus) || ENUM.TASKSTATUS_CLOSED.equals(taskstatus))
        {
         
            // und versuchen DONE zu setzen
            String statusConstraint = ENUM.TASKSTATUS_OPEN + "|" + ENUM.TASKSTATUS_ASSIGNED + "|" + ENUM.TASKSTATUS_OWNED + "|" + ENUM.TASKSTATUS_SCHEDULED;
            if (!CallTableRecord.checkForOpenTasks(callRecord, statusConstraint) && !ENUM.CALLSTATUS_DONE.equals(callStatus) && !ENUM.CALLSTATUS_CLOSED.equals(callStatus))
            {
                {

                    IDataTransaction transaction = callRecord.getAccessor().newTransaction();
                    try
                    {
                        callRecord.setValue(transaction, STATUS, ENUM.CALLSTATUS_DONE);
                        transaction.commit();

                    }
                    catch (RecordLockedException ex)
                    {

                        logger.debug(ex.toString());
                        // Pech gehabt: die Meldung ist gerade gesperrt
                    }
                    catch (UserException ex)
                    {
                        logger.warn(ex.toString());
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
        else
        {
            // wenn neue Auftrag und Status der Meldung ist DONE, dann
            // Meldungsstatus OWNED setzen
            if (ENUM.CALLSTATUS_DONE.equals(callStatus))
            {
                IDataTransaction transaction = callRecord.getAccessor().newTransaction();
                try
                {
                    callRecord.setValue(transaction, STATUS, ENUM.CALLSTATUS_OWNED);
                    transaction.commit();

                }
                catch (RecordLockedException ex)
                {
                    logger.debug(ex.toString());
                    // Pech gehabt: die Meldung ist gerade gesperrt
                }
                catch (UserException ex)
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

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
     *      de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
    {
        // ------------------------------------------------------------
        // Geschlossene Meldungen d�rfen nicht mehr ge�ndert werden
        // ------------------------------------------------------------

        if (ENUM.TASKSTATUS_CLOSED.equals((String) taskRecord.getOldValue(STATUS)))
        {
            throw new UserException("Geschlossene Auftr�ge d�rfen nicht mehr ge�ndert werden.");
        }

        // ------------------------------------------------------------
        // Diverse �berpr�fungen bzgl. gelinkter Datens�tze durchf�hren
        // ------------------------------------------------------------
        checkTaskLinks(taskRecord, transaction);

        // ------------------------------------------------------------
        // Die diverse Status�berg�nge pr�fen
        // -----------------------------------------------------------

        setTaskStatus(taskRecord, transaction);

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
