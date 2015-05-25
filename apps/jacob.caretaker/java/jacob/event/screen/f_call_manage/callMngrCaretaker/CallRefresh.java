/*
 * Created on 17.06.2004
 * by mike
 *
 */
package jacob.event.screen.f_call_manage.callMngrCaretaker;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * 
 * @author mike
 *
 */
public class CallRefresh extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: CallRefresh.java,v 1.4 2004/07/14 07:39:45 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  /* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button)throws Exception 
	{
		context.refreshGroup();
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status,IGuiElement emitter) throws Exception 
	{
	}
}
