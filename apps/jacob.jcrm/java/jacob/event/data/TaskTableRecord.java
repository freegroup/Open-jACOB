/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jan 09 14:48:23 CET 2006
 */
package jacob.event.data;

import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.i18n.ApplicationMessage;
import jacob.common.AppLogger;
import jacob.common.ENUM;
import jacob.model.Call;
import jacob.model.CallContact;
import jacob.model.CallDocument;
import jacob.model.CallOwner;
import jacob.model.CallWorkgroup;
import jacob.model.Solution;
import jacob.model.Task;
import jacob.model.TaskOwner;
import jacob.model.Taskworkgroup;

import org.apache.commons.logging.Log;

/**
 *
 * @author mike
 */
public class TaskTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: TaskTableRecord.java,v 1.1 2006/01/12 09:36:23 mike Exp $";

  static public final transient String RCS_REV = "$Revision: 1.1 $";

  static protected final transient Log logger = AppLogger.getLogger();

  static protected final String STATUS = "taskstatus";

  // Map{NewStatusString->Callback}
  private static final Map statusChangeCallbacks = new HashMap();

  static
  {
    statusChangeCallbacks.put(ENUM.TASKSTATUS_NEW, new StatusChangeCallback()
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
        if (!taskRecord.hasLinkedRecord(Taskworkgroup.NAME))
          throw new UserException(new ApplicationMessage("TaskTableRecord.1", ENUM.TASKSTATUS_ASSIGNED));
        taskRecord.setValue(transaction, Task.date_qa, null);
        taskRecord.setValue(transaction, Task.dateowned, null);
        return true;

      }
    });
    statusChangeCallbacks.put(ENUM.TASKSTATUS_QA, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord taskRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {

        if (!(taskRecord.hasLinkedRecord(Taskworkgroup.NAME) && taskRecord.hasLinkedRecord(TaskOwner.NAME)))
          throw new UserException(new ApplicationMessage("TaskTableRecord.2", ENUM.TASKSTATUS_QA));

        taskRecord.setValue(transaction, Task.date_qa, "now");
        return true;

      }
    });
    statusChangeCallbacks.put(ENUM.TASKSTATUS_OWNED, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord taskRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {
        if (!(taskRecord.hasLinkedRecord(Taskworkgroup.NAME) && taskRecord.hasLinkedRecord(TaskOwner.NAME)))
          throw new UserException(new ApplicationMessage("TaskTableRecord.2", ENUM.CALLSTATUS_OWNED));
        taskRecord.setValue(transaction, Task.date_qa, null);
        return true;

      }
    });

    statusChangeCallbacks.put(ENUM.TASKSTATUS_CLOSED, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord taskRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {

        if (!ENUM.TASKSTATUS_QA.equals(oldStatus))
        {
          throw new UserException(new ApplicationMessage("callTableRecord.ClosedFaild"));
        }

        taskRecord.setValue(transaction, Task.dateclosed, "now");
        return true;

      }
    });
  }

  private static void checkTaskLinks(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
  {

    if (!taskRecord.hasLinkedRecord(Call.NAME))
    {
      throw new UserException(new ApplicationMessage("TaskTableRecord.NoCall"));
    }

    if (taskRecord.hasChangedValue(Task.taskworkgroup_key))
    {
      if (taskRecord.getValue(Task.taskworkgroup_key) != null)
      {
        taskRecord.setValue(transaction, Task.daterequested, "now");
      }
      else
      {
        taskRecord.setValue(transaction, Task.daterequested, null);
      }
    }
    // Wenn ein AK geändert wird Zeitstempel richtig setzen
    if (taskRecord.hasChangedValue(Task.taskowner_key))
    {
      if (taskRecord.getValue(Task.taskowner_key) != null)
      {
        taskRecord.setValue(transaction, Task.dateowned, "now");
      }
      else
      {
        taskRecord.setValue(transaction, Task.dateowned, null);
      }
    }

  }

  private static void setTasklStatus(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
  {
    String newStatus = taskRecord.getStringValue(STATUS);

    if (taskRecord.hasLinkedRecord(TaskOwner.NAME) && ENUM.allTaskStatusBeforeQA.contains(newStatus))
    {
      taskRecord.setValue(transaction, STATUS, ENUM.TASKSTATUS_OWNED);
    }
    else
    {

      if (taskRecord.hasLinkedRecord(Taskworkgroup.NAME) && ENUM.allTaskStatusBeforeQA.contains(newStatus))
      {
        taskRecord.setValue(transaction, STATUS, ENUM.TASKSTATUS_ASSIGNED);
      }
      else
      {
        if (ENUM.allTaskStatusBeforeQA.contains(newStatus))
        {
          taskRecord.setValue(transaction, STATUS, ENUM.TASKSTATUS_NEW);
        }
      }

    }
    // prüfen ob der neue Status gültig ist.
    checkCallStatusChange(taskRecord, transaction);
  }

  private static void checkCallStatusChange(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
  {
    String newStatus = taskRecord.getStringValue(STATUS);
    String oldStatus = (String) taskRecord.getOldValue(STATUS);

    // Wenn keine Statusänderung, dann hier schon wieder aussteigen
    if (!taskRecord.hasChangedValue(STATUS))
    {
      return;
    }

    // Ansonsten die Statusübergänge überprüfen
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
        throw new UserException(new ApplicationMessage("CallTableRecord.WrongStatusChange", oldStatus, newStatus));
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
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
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
    // Geschlossene Meldungen dürfen nicht mehr geändert werden
    // ------------------------------------------------------------

    if (ENUM.TASKSTATUS_CLOSED.equals((String) taskRecord.getOldValue(STATUS)))
    {
      throw new UserException(new ApplicationMessage("TaskTableRecord.Closed"));
    }
    // ------------------------------------------------------------
    // Diverse Überprüfungen bzgl. gelinkter Datensätze durchführen
    // ------------------------------------------------------------
    checkTaskLinks(taskRecord, transaction);

    // ------------------------------------------------------------
    // Die diverse Statusübergänge prüfen
    // -----------------------------------------------------------

    setTasklStatus(taskRecord, transaction);

  }

  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * Diese abstrakte Klasse definiert eine Callbackmethode, welche
   * Statusübergänge überprüft. Für jeden (neuen) Status wird jeweils eine
   * andere Klasse (anonyme Klasse) implementiert.
   * 
   * @author Mike Doering
   */
  private static abstract class StatusChangeCallback
  {
    /**
     * Überprüfung ob der Statusübergang gültig ist.
     * 
     * @param tableRecord
     * @param transaction
     * @param oldStatus
     * @return <code>true</code> Statusübergang ist in Ordnung, ansonsten
     *         <code>false</code>
     * @throws Exception
     */
    abstract boolean check(IDataTableRecord tableRecord, IDataTransaction transaction, String oldStatus) throws Exception;
  }
}
