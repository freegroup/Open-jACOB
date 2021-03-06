/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jan 09 11:29:12 CET 2006
 */
package jacob.event.ui.task;

import jacob.common.AppLogger;
import jacob.model.Task;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the TaskSetClosed record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author mike
 */
public class TaskSetClosed extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: TaskSetClosed.java,v 1.2 2006/11/10 07:51:52 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
   public void onAction(IClientContext context, IGuiElement button) throws Exception
    {
      IDataTableRecord currentRecord = context.getSelectedRecord();
      IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
      try
      {
        currentRecord.setValue(currentTransaction, Task.taskstatus, Task.taskstatus_ENUM._Closed);

        currentTransaction.commit();
      }
      finally
      {
        currentTransaction.close();
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
      if (status != IGuiElement.SELECTED)
        return;
      IDataTableRecord task = context.getSelectedRecord();
      String taskstatus = task.getSaveStringValue(Task.taskstatus);
      button.setEnable(Task.taskstatus_ENUM._QA.equals(taskstatus));
    }
  }
