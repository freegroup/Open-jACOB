/*
 * Created on May 5, 2004
 *
 */
package jacob.event.ui.call;

import jacob.common.Call;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * find all accepted calls of the current user
  * @author mike
  *
  */
public class CallMyAcceptedCalls extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CallMyAcceptedCalls.java,v 1.1 2005/06/03 15:18:53 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
		Call.findByUserAndState(context,context.getUser(),"Angenommen", "r_queues");
  }

   
 /**
	* Enable disable the button when state of the group has been changed
	*/
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
	}
}
