/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Dec 16 15:50:46 CET 2008
 */
package jacob.event.ui.vdn;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.security.IUser;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;
import com.hecc.jacob.ldap.Role;
import com.hecc.jacob.properties.Property;
import com.hecc.jacob.properties.PropertyAccessor;


/**
 * The event handler for the VdnIVRAdminButton generic button.<br>
 * The {@link #onClick(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author R.Spoor
 */
public class VdnIVRAdminButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: VdnIVRAdminButton.java,v 1.1 2009/02/17 15:23:40 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

    /**
     * The IVR Admin location property.
     */
    static private final String IVR_ADMIN_PROPERTY = "com.hecc.ivr.admin";

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param emitter The corresponding button to this event handler
	 */
	@Override
	public void onClick(IClientContext context, IGuiElement emitter) throws Exception
	{
        PropertyAccessor accessor = PropertyAccessor.getInstance();
        Property ivrAdmin = accessor.getProperty(
            context, IVR_ADMIN_PROPERTY, context.getDataAccessor(), true
        );
        if (ivrAdmin != null && ivrAdmin.isSet())
        {
            IUrlDialog dialog = context.createUrlDialog(ivrAdmin.asString());
            dialog.enableNavigation(false);
            dialog.setModal(false);
            dialog.enableScrollbar(true);
            dialog.show(800, 600);
        }
        else
        {
            context.createMessageDialog("Could not launch IVR Admin").show();
        }
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
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
     */
    @Override
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
    {
        IUser user = context.getUser();
        if (!user.hasRole(Role.ADMIN.getName()))
        {
            button.setVisible(false);
        }
    }
}
