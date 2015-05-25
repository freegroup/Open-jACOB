/*
 * Created on 23.07.2004
 *
 */
package jacob.event.screen.f_call_entry.object;

import jacob.common.gui.GotoCall;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author achim
 *
 */
public final class CategoryGotoCall extends IButtonEventHandler {

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button)
			throws Exception 
	{
		GotoCall.fillData(context, "callEntryCaretaker");
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception

	{
		  	IDataTable call = context.getDataTable("call");
            button.setEnable(status == IGuiElement.SELECTED && call.recordCount() !=1);
	}

}
