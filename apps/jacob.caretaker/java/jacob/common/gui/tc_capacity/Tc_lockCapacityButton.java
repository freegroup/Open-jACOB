/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 27 23:15:08 CEST 2006
 */
package jacob.common.gui.tc_capacity;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Tc_lockCapacityButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Tc_lockCapacityButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Tc_lockCapacityButton.java,v 1.2 2006/08/03 17:20:14 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
    Capacity.lock(context);
	}
   
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
	}
}
