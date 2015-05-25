/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jan 09 14:18:57 CET 2006
 */
package jacob.event.ui.task;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Call;
import jacob.model.Task;
import jacob.model.TaskAgent;

import org.apache.commons.logging.Log;


/**
 * The event handler for the TaskButtonNewRecord new button.<br>
 * 
 * @author mike
 */
public class TaskButtonNewRecord extends IActionButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: TaskButtonNewRecord.java,v 1.2 2006/01/22 17:20:12 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This event handler will be called, if the corresponding button has been pressed.
	 * You can prevent the execution of the NEW action if you return <code>false</code>.<br>
	 * 
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
		return true;
	}

	/**
	 * This event method will be called, if the NEW action has been successfully executed.<br>
	 *  
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord newtask = context.getSelectedRecord();
    IDataTransaction newtrans = newtask.getCurrentTransaction();

    newtask.setValue(newtrans, Task.dateowned, null);
    newtask.setValue(newtrans, Task.date_qa, null);
    newtask.setValue(newtrans, Task.dateclosed, null);
    IDataTable call = context.getDataTable(Call.NAME);
    if (call.recordCount() == 1)
      newtask.setLinkedRecord(newtrans, call.getRecord(0));

    // link record with current agent
    IDataTable agent = context.getDataAccessor().getTable(TaskAgent.NAME);
    agent.qbeClear();
    agent.qbeSetValue(TaskAgent.pkey, context.getUser().getKey());
    agent.search();
    if (agent.recordCount() == 1)
      newtask.setLinkedRecord(newtrans, agent.getRecord(0));
  }

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{

	}
}
