/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 12 22:27:24 CEST 2006
 */
package jacob.event.ui.messagetask;

import jacob.common.task.SendMessageTask;
import jacob.model.Messagetask;
import jacob.model.TaskProperties;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;


/**
 *
 * @author andreas
 */
 public class MessagetaskInterval extends ITextFieldEventHandler 
 {
	static public final transient String RCS_ID = "$Id: MessagetaskInterval.java,v 1.2 2010-11-17 17:16:03 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
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
   * @param status The new group status
	 * @param emitter The corresponding GUI element of this event handler
	 */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IText emitter) throws Exception
  {
    IDataTableRecord messagetaskRec = context.getSelectedRecord();
    IDataTableRecord taskPropRec = null;
    if (messagetaskRec != null)
    {
      IDataTable taskProps = context.getDataAccessor().getTable(TaskProperties.NAME);
      taskProps.qbeClear();
      taskProps.qbeSetKeyValue(TaskProperties.taskname, messagetaskRec.getStringValue(Messagetask.name));
      taskProps.search();
      taskPropRec = taskProps.getSelectedRecord();
    }

    ISingleDataGuiElement intervalText = (ISingleDataGuiElement) emitter;

    if (IGuiElement.SELECTED == status || IGuiElement.UPDATE == status)
    {
      if (taskPropRec == null)
        intervalText.setValue(Integer.toString(SendMessageTask.DEFAULT_TASK_INTERVAL));
      else
        intervalText.setValue(taskPropRec.getStringValue(TaskProperties.taskinterval));
      intervalText.setEnable(IGuiElement.UPDATE == status);
      intervalText.setRequired(IGuiElement.UPDATE == status);
    }
    else
    {
      intervalText.setEnable(false);
      intervalText.setRequired(false);
    }
  }

}
