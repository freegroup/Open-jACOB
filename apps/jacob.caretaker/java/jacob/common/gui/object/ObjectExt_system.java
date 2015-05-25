/*
 * Created on 14.12.2004
 * by mike
 *
 */
package jacob.common.gui.object;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;

/**
 * 
 * @author mike
 *
 */
public class ObjectExt_system extends IForeignFieldEventHandler
{

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
		emitter.setEnable(!(status == IGuiElement.UPDATE || status == IGuiElement.SELECTED || status == IGuiElement.NEW) );

	}
}
