/*
 * Created on 18.08.2004
 * by mike
 *
 */
package jacob.common.gui.call;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * 
 * @author mike
 *
 */
public class CallAddTask extends IButtonEventHandler
{
static public final transient String RCS_ID = "$Id: CallAddTask.java,v 1.1 2004/08/18 11:17:55 mike Exp $";
static public final transient String RCS_REV = "$Revision: 1.1 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		// Sprung zu Auftrag anlegen und Maske vorbereiten!
		String priority = context.getSelectedRecord().getStringValue("priority");
		IDataTable task = context.getDataTable("task");
		if (task.recordCount()!=1)
		{
			context.getDomain().setInputFieldValue("taskadd","taskPriority",priority);
		}
		context.setCurrentForm("taskadd");

	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
	{
		if(status == IGuiElement.SELECTED)
		{
			IDataTableRecord currentRecord = context.getSelectedRecord();
			String callstatus = currentRecord.getStringValue("callstatus");
			button.setEnable(callstatus.equals("Angenommen"));
		}
	}
}
