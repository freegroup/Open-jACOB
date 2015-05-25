/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.call;

/**
 * @author mike
 *  
 */
import jacob.common.Call;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
/**
 * find all open calls of the current user
 * 
 * @author mike
 *  
 */
public class CallMyCalls extends IButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: CallMyCalls.java,v 1.5 2004/07/30 17:31:52 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.5 $";
	public void onAction(IClientContext context, IGuiElement button)throws Exception
	{
			Call.findByUserAndState(context, context.getUser(), "!Geschlossen", "r_queues");
	}
  
	/**
	 * Enable disable the button when state of the group has been changed
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
	}
}
