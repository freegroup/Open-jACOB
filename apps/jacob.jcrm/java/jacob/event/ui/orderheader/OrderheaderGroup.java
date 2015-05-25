/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Oct 13 16:04:37 CEST 2005
 */
package jacob.event.ui.orderheader;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupListenerEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author andreas
 */
 public class OrderheaderGroup extends IGroupListenerEventHandler 
 {
	static public final transient String RCS_ID = "$Id: OrderheaderGroup.java,v 1.1 2005/10/13 23:54:44 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
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
	 * @param emitter The corresponding GUI element of this event handler
	 * 
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
	  // new button of lineitem group should only be enabled, if this group is in the selected mode
		context.getForm().findByName("orderlineitemButtonNewRecord").setEnable(status == IGuiElement.SELECTED);
	}
}
