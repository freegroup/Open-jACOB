/*
 * Created on 29.07.2004
 *
 */
package jacob.common.gui.task;

import jacob.common.Task;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author achim
 *
 */
public class TaskSetDocumented extends IButtonEventHandler 
{

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button)throws Exception 
	{
	    Task.setDocumented(null, context.getSelectedRecord());	
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
	{
			if(status == IGuiElement.SELECTED)
			{
				IDataTableRecord currentRecord = context.getSelectedRecord();
				String taskstatus = currentRecord.getStringValue("taskstatus");
				button.setEnable(taskstatus.equals("Fertig gemeldet"));
			}
	}
}
