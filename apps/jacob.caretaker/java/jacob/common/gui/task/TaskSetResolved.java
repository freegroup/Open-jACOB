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
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author achim
 *
 */
public class TaskSetResolved extends IButtonEventHandler {
	public static class TaskSetResolvedCallback implements IOkCancelDialogCallback
	{
	
			/* (non-Javadoc)
		 * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
		 */
		public void onCancel(IClientContext context) throws Exception {
			

		}
		/* (non-Javadoc)
		 * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onOk(de.tif.jacob.screen.IClientContext)
		 */
		public void onOk(IClientContext context) throws Exception 
		{
			Task.setStatus(null,context,context.getSelectedRecord(), "Fertig gemeldet");

		}
}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button)
			throws Exception 
	{
		IOkCancelDialog dialog = context.createOkCancelDialog("Wollen Sie den Auftrag wirklich fertig melden?",new TaskSetResolvedCallback());
		dialog.show();
		
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
				button.setEnable(taskstatus.equals("In Arbeit")); //|| !taskstatus.equals("Dokumentiert"));
			}
	}

}
