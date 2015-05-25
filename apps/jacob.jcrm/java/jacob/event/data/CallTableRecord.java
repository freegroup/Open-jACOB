/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Dec 09 12:02:12 CET 2005
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.common.ENUM;
import jacob.model.Call;
import jacob.model.CallAgent;
import jacob.model.CallContact;
import jacob.model.CallDocument;
import jacob.model.CallOwner;
import jacob.model.CallWorkgroup;
import jacob.model.Incident;
import jacob.model.Servicelevel;
import jacob.model.Solution;
import jacob.model.Task;
import jacob.reminder.IReminder;
import jacob.reminder.ReminderFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.i18n.ApplicationMessage;

/**
 * 
 * @author mike
 */
public class CallTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: CallTableRecord.java,v 1.5 2006/11/10 07:51:58 achim Exp $";

  static public final transient String RCS_REV = "$Revision: 1.5 $";

  static protected final transient Log logger = AppLogger.getLogger();

  static protected final String STATUS = "callstatus";

  // Map{NewStatusString->Callback}
  private static final Map statusChangeCallbacks = new HashMap();

  static
  {
    statusChangeCallbacks.put(ENUM.CALLSTATUS_NEW, new StatusChangeCallback()
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
        if (!callRecord.hasLinkedRecord(CallWorkgroup.NAME))
          throw new UserException(new ApplicationMessage("CallTableRecord.1", ENUM.CALLSTATUS_ASSIGNED));
        callRecord.setValue(transaction, jacob.model.Call.date_qa, null);
        callRecord.setValue(transaction, jacob.model.Call.dateowned, null);
        return true;

      }
    });
    statusChangeCallbacks.put(ENUM.CALLSTATUS_QA, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {

        if (!(callRecord.hasLinkedRecord(CallWorkgroup.NAME) && callRecord.hasLinkedRecord(CallOwner.NAME) && (callRecord.getValue(Call.solutiontext) != null || callRecord
            .hasLinkedRecord(Solution.NAME)))
            || checkForOpenTasks(callRecord, "New|Assigned|Owned"))
          throw new UserException(new ApplicationMessage("CallTableRecord.2", ENUM.CALLSTATUS_QA));

        callRecord.setValue(transaction, jacob.model.Call.date_qa, "now");
        return true;

      }
    });
    statusChangeCallbacks.put(ENUM.CALLSTATUS_OWNED, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {
        if (!(callRecord.hasLinkedRecord(CallWorkgroup.NAME) && callRecord.hasLinkedRecord(CallOwner.NAME)))
          throw new UserException(new ApplicationMessage("CallTableRecord.3", ENUM.CALLSTATUS_OWNED));
        callRecord.setValue(transaction, jacob.model.Call.date_qa, null);
        return true;

      }
    });

    statusChangeCallbacks.put(ENUM.CALLSTATUS_CLOSED, new StatusChangeCallback()
    {
      boolean check(IDataTableRecord callRecord, IDataTransaction transaction, String oldStatus) throws Exception
      {

        if (!ENUM.CALLSTATUS_QA.equals(oldStatus))
        {
          throw new UserException(new ApplicationMessage("callTableRecord.ClosedFaild"));
        }

        if (checkForOpenTasks(callRecord, "!" + ENUM.TASKSTATUS_CLOSED))
        {
          throw new UserException(new ApplicationMessage("callTableRecord.OpenTasks"));
        }

        callRecord.setValue(transaction, jacob.model.Call.dateclosed, "now");
        return true;

      }
    });
  }

  private static void checkCallLinks(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {

    if (!callRecord.hasLinkedRecord(CallContact.NAME))
    {
      throw new UserException(new ApplicationMessage("CallTableRecord.NoContact"));
    }

    if (null == callRecord.getValue(Call.callagent_key))
    {
      callRecord.setValue(transaction, Call.callagent_key, Context.getCurrent().getUser().getKey());
    }

    // if
    if (callRecord.hasChangedValue(Call.callworkgroup_key))
    {
      if (callRecord.getValue(Call.callworkgroup_key) != null)
      {
        callRecord.setValue(transaction, Call.dateassigned, "now");
      }
      else
      {
        callRecord.setValue(transaction, Call.dateassigned, null);
      }
    }
    // Wenn ein AK geändert wird Zeitstempel richtig setzen
    if (callRecord.hasChangedValue(Call.callowner_key))
    {
      if (callRecord.getValue(Call.callowner_key) != null)
      {
        callRecord.setValue(transaction, Call.dateowned, "now");
      }
      else
      {
        callRecord.setValue(transaction, Call.dateowned, null);
      }
    }
    if (callRecord.hasLinkedRecord(Incident.NAME))
    {
      IDataTableRecord incident = callRecord.getLinkedRecord(Incident.NAME);
      String incidentStatus = incident.getSaveStringValue(Incident.status);
      if (!Incident.status_ENUM._Routed.equals(incidentStatus) && !Incident.status_ENUM._Closed .equals(incidentStatus))
      {
        incident.setValue(transaction,Incident.status,Incident.status_ENUM._Routed);
      }

      if (ENUM.CALLSTATUS_CLOSED.equals(callRecord.getValue(Call.callstatus)) && callRecord.hasChangedValue(Call.callstatus))
      {

        incident.setValue(transaction, Incident.status,Incident.status_ENUM._Closed );
      }
    }
  }

  private static void setCallStatus(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {
    String newStatus = callRecord.getStringValue(STATUS);

    if (callRecord.hasLinkedRecord(CallOwner.NAME) && ENUM.allCallStatusBeforeQA.contains(newStatus))
    {
      callRecord.setValue(transaction, STATUS, ENUM.CALLSTATUS_OWNED);
    }
    else
    {
      // Hat der Datensatz einen Status CALLSTATUS_ASSIGEND?
      if (callRecord.hasLinkedRecord(CallWorkgroup.NAME) && ENUM.allCallStatusBeforeQA.contains(newStatus))
      {
        callRecord.setValue(transaction, STATUS, ENUM.CALLSTATUS_ASSIGNED);
      }
      else
      {
        if (ENUM.allCallStatusBeforeQA.contains(newStatus))
        {
          // Datensatz ist im Status CALLSTATUS_NEW
          callRecord.setValue(transaction, STATUS, ENUM.CALLSTATUS_NEW);
        }
      }

    }
    // prüfen ob der neue Status gültig ist.
    checkCallStatusChange(callRecord, transaction);
  }

  private static void checkCallStatusChange(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {
    String newStatus = callRecord.getStringValue(STATUS);
    String oldStatus = (String) callRecord.getOldValue(STATUS);

    // Wenn keine Statusänderung, dann hier schon wieder aussteigen
    if (!callRecord.hasChangedValue(STATUS))
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
      if (!callback.check(callRecord, transaction, oldStatus))
      {
        throw new UserException(new ApplicationMessage("CallTableRecord.WrongStatusChange", oldStatus, newStatus));
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
   * Prüft ob für die gegebene Meldung noch offene Aufträge vorhanden sind
   * 
   * @param callRecord
   *          die Meldung
   * @return <code>true</code> es sind noch offene Aufträge vorhanden.
   * @throws Exception
   *           bei einem schwerwiegenden Fehler
   */
  public static boolean checkForOpenTasks(IDataTableRecord callRecord, String constraint) throws Exception
  {
    // einen neuen Accessor erzeugen, damit die Client-seitige Darstellung
    // nicht verändert wird.
    IDataAccessor accessor = callRecord.getAccessor().newAccessor();

    // Sind noch offene Aufträge da?
    IDataTable iTaskTable = accessor.getTable(Task.NAME);
    iTaskTable.qbeSetKeyValue(Task.call_key, callRecord.getValue(Call.pkey));
    iTaskTable.qbeSetValue(Task.taskstatus, constraint);
    return iTaskTable.exists();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord callRecord) throws Exception
  {
    // (de)activate reminder
    //
    if (callRecord.isDeleted())
    {
      if (!callRecord.hasNullValue("reminder"))
      {
        IReminder myReminder = ReminderFactory.getReminder(callRecord);
        myReminder.delete(callRecord);
      }
    }
    else
    {
      if (callRecord.hasChangedValue(Call.callstatus) || callRecord.hasChangedValue(Call.servicelevel_key) || callRecord.hasChangedValue(Call.callowner_key)
          || callRecord.hasChangedValue(Call.callowner_key))
      {
        IReminder myReminder = ReminderFactory.getReminder(callRecord);
        myReminder.delete(callRecord);

        if (callRecord.hasNullValue(Call.servicelevel_key) || Call.callstatus_ENUM._Closed.equals(callRecord.getStringValue(Call.callstatus))
            || Call.callstatus_ENUM._QA.equals(callRecord.getStringValue(Call.callstatus)))
        {
          return;
        }
        else
        {
          int interval = callRecord.getLinkedRecord(Servicelevel.NAME).getintValue(Servicelevel.delay);
          if (interval == 0)
            return;
          Calendar cal = Calendar.getInstance();
          cal.setTime(callRecord.getDateValue(Call.datereported));
          cal.add(Calendar.HOUR, interval);
          myReminder.setWhen(cal.getTime());
          if (callRecord.hasLinkedRecord(CallOwner.NAME))
          {
            myReminder.setMethod(IReminder.OWNER);
            myReminder.setRecipient(callRecord.getLinkedRecord(CallOwner.NAME));
          }
          else
          {
            if (callRecord.hasLinkedRecord(CallWorkgroup.NAME))
            {
              myReminder.setMethod(IReminder.WORKGROUP);
              myReminder.setRecipient(callRecord.getLinkedRecord(CallWorkgroup.NAME));
            }
            else
            {
              myReminder.setMethod(IReminder.OWNER);
              myReminder.setRecipient(callRecord.getLinkedRecord(CallAgent.NAME));
            }
          }
          myReminder
              .setMsg(ApplicationMessage.getLocalized("CallReminder.Text", callRecord.getStringValue(Call.pkey), callRecord.getStringValue(Call.problem)));
          myReminder.schedule();
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
  public void afterDeleteAction(IDataTableRecord callRecord, IDataTransaction transaction) throws Exception
  {
    IDataTable taskTable = callRecord.getAccessor().getTable(Task.NAME);
    taskTable.qbeClear();
    taskTable.qbeSetKeyValue(Task.call_key, callRecord.getValue(Call.pkey));
    if (taskTable.exists())
      throw new UserRuntimeException(new ApplicationMessage("CallTableRecord.DeleteError"));

    // delete all assigned documents as well
    //
    IDataTable documentTable = callRecord.getAccessor().getTable(CallDocument.NAME);
    documentTable.qbeClear();
    documentTable.qbeSetKeyValue(CallDocument.call_key, callRecord.getValue(Call.pkey));
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
    // Geschlossene Meldungen dürfen nicht mehr geändert werden
    // ------------------------------------------------------------

    if (ENUM.CALLSTATUS_CLOSED.equals((String) callRecord.getOldValue(STATUS)))
    {
      throw new UserException(new ApplicationMessage("CallTableRecord.4"));
    }
    // ------------------------------------------------------------
    // Diverse Überprüfungen bzgl. gelinkter Datensätze durchführen
    // ------------------------------------------------------------
    checkCallLinks(callRecord, transaction);

    // ------------------------------------------------------------
    // Die diverse Statusübergänge prüfen
    // -----------------------------------------------------------

    setCallStatus(callRecord, transaction);

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
   * @author Andreas Sonntag
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
