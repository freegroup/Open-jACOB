/*
 * Created on 29.07.2004
 *
 */
package jacob.common.gui.call;

import jacob.common.Call;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author achim
 *
 */
public class CallMyNewCalls extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CallMyNewCalls.java,v 1.3 2004/08/20 07:10:46 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";
	public void onAction(IClientContext context, IGuiElement button)throws Exception
	{
			Call.findByUserAndState(context, context.getUser(), "Durchgestellt|AK zugewiesen|Fehlgeroutet|Angenommen", "r_queues");
	}
  
	/**
	 * Enable disable the button when state of the group has been changed
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
	}
}