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
 * find all assigned calls of the current user
 * @author mike
 *
 */
public class CallMyAssignedCalls extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: CallMyAssignedCalls.java,v 1.1 2005/06/03 15:18:53 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
		// Show all assigned calls If you are Problemmanager then show fehlegroutet calls too
		if (context.getUser().hasRole("CQ_PM"))
			Call.findByUserAndState(context,context.getUser(),"AK zugewiesen|Fehlgeroutet", "r_queues");	
		else 
			Call.findByUserAndState(context,context.getUser(),"AK zugewiesen", "r_queues");
  }

  
  /**
   * Enable disable the button when state of the group has been changed
   */
   public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
   {
   }
  
}
