/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Dec 07 15:29:53 CET 2006
 */
package jacob.event.ui.email;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IPassword;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.*;

import jacob.common.AppLogger;
import jacob.security.Role;
import jacob.security.UserFactory;

import org.apache.commons.logging.Log;


/**
 *
 * @author andherz
 */
 public class EmailSmtp_password extends IPasswordFieldEventHandler 
 {
	static public final transient String RCS_ID = "$Id: EmailSmtp_password.java,v 1.1 2007/02/02 22:26:45 freegroup Exp $";
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
	 * @param passwordField The corresponding GUI element of this event handler
	 * 
	 */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IPassword passwordField) throws Exception
	{
		if(context.getUser().hasRole(Role.GUEST.getName()))
			passwordField.setValue("############");
	}
}