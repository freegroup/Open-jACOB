/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Apr 02 13:55:25 CEST 2009
 */
package jacob.event.ui.agent;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;
import com.hecc.jacob.ldap.Role;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.security.IUser;


/**
 * The event handler for the AgentUpdateRecordButton update button.<br>
 *
 * @author R.Spoor
 */
public class AgentUpdateRecordButton extends IActionButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: AgentUpdateRecordButton.java,v 1.1 2009/04/03 07:12:13 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.1 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * This event handler will be called, if the corresponding button has been pressed.
     * You can prevent the execution of the UPDATE action if you return <code>false</code>.<br>
     *
     * @param context The current context of the application
     * @param button  The action button (the emitter of the event)
     * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
     */
    @Override
    public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
    {
        if (context.getGroup().getDataStatus()== IGuiElement.SELECTED)
        {
            // The record will be toggled from IGuiElement.SELECTED => IGuiElement.UPDATE
            // (return false to prevent this)
        }
        else
        {
            // The record will be toggled from IGuiElement.UPDATE => IGuiElement.SELECTED
            // (return false to prevent this)
        }
        return true;
    }

    /**
     * This event method will be called, if the UPDATE action has been successfully executed.<br>
     *
     * @param context The current context of the application
     * @param button  The action button (the emitter of the event)
     */
    @Override
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
