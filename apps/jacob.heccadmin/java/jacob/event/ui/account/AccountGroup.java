/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jan 28 16:58:08 CET 2009
 */
package jacob.event.ui.account;

import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;
import de.tif.jacob.security.IUser;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;
import com.hecc.jacob.ldap.Role;


/**
 *
 * @author R.Spoor
 */
 public class AccountGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: AccountGroup.java,v 1.1 2009/02/17 15:23:40 R.Spoor Exp $";
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
	 * @param status  The new status of the group.
	 * @param group   The corresponding GUI element of this event handler
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
	{
	}

	/**
	 * Will be called if the will be change the state from visible=>hidden.
	 * 
	 * This happends if the user switch the Domain or Form which contains this group.
	 * 
	 * @param context The current client context
	 * @param group   The corresponding group for this event
	 */
	@Override
	public void onHide(IClientContext context, IGroup group) throws Exception 
	{
	}

	/**
	 * Will be called if the will be change the state from hidden=>visible.
	 * 
	 * This happends if the user switch to a Form which contains this group.
	 * 
	 * @param context The current client context
	 * @param group   The corresponding group for this event
	 */
	@Override
	public void onShow(IClientContext context, IGroup group) throws Exception 
	{
        IUser user = context.getUser();
        if (!user.hasRole(Role.ADMIN.getName()))
        {
            IGuiElement element = group.findByName("accountHistory");
            element.setVisible(false);
        }
	}
}
