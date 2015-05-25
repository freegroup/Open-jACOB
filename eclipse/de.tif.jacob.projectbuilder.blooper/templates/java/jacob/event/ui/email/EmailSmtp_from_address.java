/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Dec 07 15:33:09 CET 2006
 */
package jacob.event.ui.email;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;

import jacob.common.AppLogger;
import jacob.security.Role;
import jacob.security.UserFactory;

import org.apache.commons.logging.Log;


/**
 *
 * @author andherz
 */
 public class EmailSmtp_from_address extends ITextFieldEventHandler 
 {
	static public final transient String RCS_ID = "$Id: EmailSmtp_from_address.java,v 1.1 2007/11/25 22:10:31 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
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
   * @param status The new group status
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
		if(context.getUser().hasRole(Role.GUEST.getName()))
			((ISingleDataGuiElement)emitter).setValue("############");
	}
}
