/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 28 08:58:41 CEST 2006
 */
package jacob.common.gui.tc_customer;

import jacob.common.tc.TC;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the Tc_customerAdvisoryTicketButton record selected
 * button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Tc_customerAdvisoryTicketButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Tc_customerAdvisoryTicketButton.java,v 1.1 2006/07/28 14:37:30 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    TC.createAdvisoryCall(context);
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}
