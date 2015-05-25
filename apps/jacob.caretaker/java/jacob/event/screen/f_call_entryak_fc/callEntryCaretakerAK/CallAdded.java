/*
 * Created on 16.06.2004
 *
 */
package jacob.event.screen.f_call_entryak_fc.callEntryCaretakerAK;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author achim
 *
 */
public final class CallAdded extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: CallAdded.java,v 1.5 2004/08/16 12:23:53 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  /* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button)throws Exception 
	{
		context.clearDomain();
		context.setCurrentForm("orgcustomerIntAK");
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState state,	IGuiElement emitter) throws Exception 
	{
	  emitter.setEnable(state!= IGuiElement.NEW && state!= IGuiElement.UPDATE);
	}
}
