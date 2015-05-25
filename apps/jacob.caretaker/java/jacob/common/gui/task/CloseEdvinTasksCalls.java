/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jan 24 08:53:11 CET 2008
 */
package jacob.common.gui.task;

import java.util.Date;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Call;
import jacob.model.Sapadmin;
import jacob.model.Task;

import org.apache.commons.logging.Log;

/**
 * The event handler for the CloseEdvinTasksCalls generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class CloseEdvinTasksCalls extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: CloseEdvinTasksCalls.java,v 1.1 2008/01/25 10:15:56 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * The user has clicked on the corresponding button.<br>
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // Ext System setzen
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable sapadmin = acc.getTable(Sapadmin.NAME);
    sapadmin.qbeClear();
    sapadmin.qbeSetValue(Sapadmin.active, "1");
    sapadmin.search();
    if (sapadmin.recordCount() != 1)
    {
      throw new Exception("Aktiver Verbindungsdatensatz zu SAP konnte nicht ermittelt werden!");
    }

    String sExtSystem = sapadmin.getRecord(0).getSaveStringValue(Sapadmin.ext_systemsaptask_key);
    // Relevante Aufträge finden
    IDataAccessor accTask = context.getDataAccessor().newAccessor();
    IDataTable taskTable = accTask.getTable(Task.NAME);
    taskTable.setMaxRecords(taskTable.UNLIMITED_RECORDS);
    taskTable.qbeClear();
    taskTable.qbeSetValue(Task.taskstatus, "Neu|Angelegt|Freigegeben|Storniert|In Arbeit|Fertig gemeldet");
    taskTable.qbeSetValue(Task.ext_system_key, "!" + sExtSystem);
    taskTable.search();
    alert(taskTable.recordCount() + " wurden gefunden");
    IDataTransaction transaction = accTask.newTransaction();
    IDataTransaction transaction2 = accTask.newTransaction();
    // for (int i = 0; i < taskTable.recordCount(); i++)
    System.out.println("VorFor" + taskTable.recordCount());
    try
    {
      for (int i = 0; i < 10; i++)
      {
        IDataTableRecord task = taskTable.getRecord(i);
        task.setValue(transaction, Task.taskstatus, Task.taskstatus_ENUM._Dokumentiert);
        if (task.hasNullValue(Task.dateresolved))
        {
          task.setValue(transaction, Task.dateresolved, "now");
        }
        if (task.hasNullValue(Task.datedocumented))
        {
          task.setValue(transaction, Task.datedocumented, "now");
        }
        if (task.hasNullValue(Task.taskstart))
        {
          if (!task.hasNullValue(Task.taskdone))
          {
            task.setValue(transaction, Task.taskstart, task.getValue(Task.taskdone));
          }
          else
          {
            task.setValue(transaction, Task.taskstart, "now -1 min");
          }
        }
        if (task.hasNullValue(Task.taskdone))
        {
          task.setValue(transaction, Task.taskdone, "now");
        }
        // Hack wg statusübergang
        task.setValue(transaction, Task.sap_auftrag, "Wg Migration dokumentiert" + task.getSaveStringValue(Task.pkey));
        Date start = task.getTimestampValue("taskstart");
        Date end = task.getTimestampValue("taskdone");
        System.out.println("NR" + task.getSaveStringValue(Task.taskno));
        System.out.println("pkey" + task.getSaveStringValue(Task.pkey));
        System.out.println(start.toString());
        System.out.println(end.toString());
        System.out.println("XXXXXXXXXXXXXX");
        // if (task.hasNullValue(Task.dateclosed))
        // {
        // task.setValue(transaction, Task.dateclosed, "now");
        // }
      }
      transaction.commit();
      System.out.println("NachCommit" + taskTable.recordCount());
      // Versuchen Call zu schließen
      IDataAccessor accCheck = acc.newAccessor();

      for (int j = 0; j < 10; j++)
      {
        // Offene Aufträge prüfen

        IDataTableRecord taskrec = taskTable.getRecord(j);
        IDataTable checkTask = accCheck.getTable(Task.NAME);
        checkTask.qbeClear();
        checkTask.qbeSetValue(Task.taskstatus, "Neu|Angelegt|Freigegeben|Storniert|In Arbeit|Fertig gemeldet");
        checkTask.qbeSetValue(Task.calltask, taskrec.getValue(Task.calltask));
        checkTask.search();
        System.out.println("offene Aufträge " + checkTask.recordCount());
        if (checkTask.recordCount() == 0)
        {
          // keine offenen Tasks --> Call schließen
          IDataTableRecord call = taskrec.getLinkedRecord(Call.NAME);
          call.setValue(transaction2, Call.callstatus, Call.callstatus_ENUM._Dokumentiert);
          if (call.hasNullValue(Call.datedocumented))
          {
            call.setValue(transaction2, Call.datedocumented, "now");
          }
          if (call.hasNullValue(Call.dateresolved))
          {
            call.setValue(transaction2, Call.dateresolved, "now");
          }
          // Hack wg. Statusübergang
          call.setIntValue(transaction2, Call.sapimport, 1);
        }
      }
      transaction2.commit();
    }

    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      transaction.close();
      transaction2.close();
    }
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context
   *          The current client context
   * @param status
   *          The new group state. The group is the parent of the corresponding
   *          event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    // You can enable/disable the button in relation to your conditions.
    //
    // button.setEnable(true/false);
  }
}
