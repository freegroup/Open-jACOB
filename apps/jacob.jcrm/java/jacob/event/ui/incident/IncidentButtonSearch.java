/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Oct 14 00:58:30 CEST 2005
 */
package jacob.event.ui.incident;

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 * The event handler for the IncidentButtonSearch search button.<br>
 * 
 * @author andreas
 */
public class IncidentButtonSearch extends ISearchActionEventHandler 
{
	static public final transient String RCS_ID = "$Id: IncidentButtonSearch.java,v 1.2 2005/10/17 17:37:19 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This event handler will be called, if the corresponding button has been pressed.
	 * You can prevent the execution of the SEARCH action if you return <code>false</code>.<br>
	 * 
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
    // only from incident domain incidents could be routed
    if ("f_order".equals(context.getDomain().getName()))
    {
      // constrain search to routed incidents of proper type
      context.getDataTable().qbeSetValue("type", "Quote|Order");
    }
    else if ("f_contact_admin".equals(context.getDomain().getName()))
    {
      // constrain search to routed incidents of proper type
      context.getDataTable().qbeSetKeyValue("type", "Customer Management");
    }
    
		return true;
	}

	/**
	 * This event method will be called, if the SEARCH action has been successfully executed.<br>
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
	}
}
