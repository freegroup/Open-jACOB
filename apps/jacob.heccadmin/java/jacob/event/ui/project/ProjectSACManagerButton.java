/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Dec 11 17:31:01 CET 2008
 */
package jacob.event.ui.project;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.security.IUser;
import jacob.common.AppLogger;
import jacob.model.Project;
import org.apache.commons.logging.Log;
import com.hecc.jacob.ldap.Role;
import com.hecc.jacob.properties.Property;
import com.hecc.jacob.properties.PropertyAccessor;


/**
 * The event handler for the ProjectSACManagerButton generic button.<br>
 * The {@link #onClick(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * 
 * @author R.Spoor
 */
public class ProjectSACManagerButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ProjectSACManagerButton.java,v 1.1 2009/02/17 15:23:29 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The SAC Manager location property.
	 */
	static private final String SAC_MANAGER_PROPERTY = "com.hecc.sac.manager";
	
	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param emitter  The corresponding button to this event handler
	 */
	@Override
	public void onClick(IClientContext context, IGuiElement emitter) throws Exception
	{
	    PropertyAccessor accessor = PropertyAccessor.getInstance();
	    Property sacmanager = accessor.getProperty(
	        context, SAC_MANAGER_PROPERTY, context.getDataAccessor(), true
	    );
	    if (sacmanager != null && sacmanager.isSet())
	    {
	        String value = sacmanager.asString();
	        String tmp = value.toLowerCase();
	        if (tmp.endsWith(".php") || tmp.endsWith(".asp"))
	        {
	            // dynamic web page; add the project
	            IDataTableRecord record = context.getSelectedRecord();
	            if (record != null)
	            {
	                String project = record.getStringValue(Project.project);
	                value += "?project=" + project;
	            }
	        }
	        IUrlDialog dialog = context.createUrlDialog(value);
	        dialog.enableNavigation(false);
	        dialog.setModal(false);
	        dialog.enableScrollbar(true);
	        dialog.show(800, 600);
	    }
	    else
	    {
	        context.createMessageDialog("Could not launch SAC Manager").show();
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
        button.setEnable(user.hasRole(Role.ADMIN.getName()));
    }
}
