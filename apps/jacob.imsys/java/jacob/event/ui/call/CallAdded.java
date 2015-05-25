/*
 * Created on 16.06.2004
 *
 */
package jacob.event.ui.call;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author achim
 *
 */
public final class CallAdded extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: CallAdded.java,v 1.1 2005/06/03 15:18:53 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button)throws Exception 
	{
		context.clearDomain();
		context.setCurrentForm("UTorgcustomerInt");
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState state,	IGuiElement emitter) throws Exception 
	{
	  emitter.setEnable(state!= IGuiElement.NEW && state!= IGuiElement.UPDATE);
	}
}
