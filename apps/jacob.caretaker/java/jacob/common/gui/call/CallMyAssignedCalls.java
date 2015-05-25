/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.call;



import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;



/**
 * find all assigned calls of the current user
 * @author mike
 *
 */
public class CallMyAssignedCalls extends ICallAKBrowserSortedBySL 
{
  static public final transient String RCS_ID = "$Id: CallMyAssignedCalls.java,v 1.7 2006/05/09 14:48:08 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.7 $";
  
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
		// Show all assigned calls If you are Problemmanager then show fehlegroutet calls too
		if (context.getUser().hasRole("CQ_PM"))
			findByUserAndStateAndSortedbySL(context,context.getUser(),"AK zugewiesen|Fehlgeroutet", "r_queues");	
		else 
			findByUserAndStateAndSortedbySL(context,context.getUser(),"AK zugewiesen", "r_queues");

  }

  
  /**
   * Enable disable the button when state of the group has been changed
   */
   public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
   {
   }
  
}
