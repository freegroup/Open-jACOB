/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Feb 18 00:14:42 CET 2006
 */
package jacob.event.ui.incidentEntry;

import jacob.model.IncidentEntry;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;


/**
 * The event handler for the IncidentEntryButtonUpdateRecord update button.<br>
 * 
 * @author andreas
 */
public class IncidentEntryButtonUpdateRecord extends IActionButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: IncidentEntryButtonUpdateRecord.java,v 1.1 2006/02/24 02:16:15 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * This event handler will be called, if the corresponding button has been pressed.
	 * You can prevent the execution of the UPDATE action if you return <code>false</code>.<br>
	 * 
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
		return true;
	}

	/**
	 * This event method will be called, if the UPDATE action has been successfully executed.<br>
	 *  
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
	}

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
    boolean enable = status != IGuiElement.SEARCH;
    if (status == IGuiElement.SELECTED)
    {
      // Do not allow customer to modify a closed incident
      enable = !IncidentEntry.state_ENUM._Done.equals(context.getSelectedRecord().getValue(IncidentEntry.state));
    }
		button.setEnable(enable);
	}
}
