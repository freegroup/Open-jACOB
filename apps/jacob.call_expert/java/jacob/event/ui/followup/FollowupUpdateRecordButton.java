/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Jul 01 13:29:50 CEST 2009
 */
package jacob.event.ui.followup;

import jacob.common.AppLogger;
import jacob.model.Followup;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;


/**
 * The event handler for the FollowupUpdateRecordButton update button.<br>
 *
 * @author andherz
 */
public class FollowupUpdateRecordButton extends IActionButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: FollowupUpdateRecordButton.java,v 1.3 2009/11/23 11:33:44 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.3 $";

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
    public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
    {
        IDataTableRecord currentRecord = context.getSelectedRecord();

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
        button.setVisible(false);
        if(status == IGroup.SEARCH)
            return;

        boolean isDone = context.getSelectedRecord().getSaveStringValue(Followup.status).equals(Followup.status_ENUM._done);
        button.setVisible(!isDone);
    }
}
