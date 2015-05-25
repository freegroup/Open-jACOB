/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 12 22:42:01 CEST 2006
 */
package jacob.event.ui.messagetask;

import jacob.model.Messagetask;
import jacob.model.TaskProperties;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;


/**
 * The event handler for the MessageTaskUpdateRecordButton update button.<br>
 * 
 * @author andreas
 */
public class MessageTaskUpdateRecordButton extends IActionButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: MessageTaskUpdateRecordButton.java,v 1.2 2010-11-17 17:16:03 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * This event handler will be called, if the corresponding button has been pressed.
	 * You can prevent the execution of the UPDATE action if you return <code>false</code>.<br>
	 * 
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    IDataTableRecord messagetaskRec = context.getSelectedRecord();

    if (context.getGroup().getDataStatus() == IGuiElement.SELECTED)
    {
      // The record will be toggled from IGuiElement.SELECTED =>
      // IGuiElement.UPDATE
    }
    else
    {
      // The record will be toggled from IGuiElement.UPDATE =>
      // IGuiElement.SELECTED
      IDataTransaction trans = messagetaskRec.getCurrentTransaction();
      IDataTable taskProps = context.getDataAccessor().getTable(TaskProperties.NAME);
      if (taskProps.getSelectedRecord() == null)
      {
        IDataTableRecord taskPropRec = taskProps.newRecord(trans);
        taskPropRec.setValue(trans, TaskProperties.taskname, messagetaskRec.getValue(Messagetask.name));
      }
      taskProps.getSelectedRecord().setStringValue(messagetaskRec.getCurrentTransaction(), TaskProperties.taskinterval,
          context.getGroup().getInputFieldValue("messagetaskInterval"));
    }
    return true;
  }

	/**
	 * This event method will be called, if the UPDATE action has been successfully executed.<br>
	 *  
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
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
