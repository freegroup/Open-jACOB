/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Jul 01 09:45:42 CEST 2009
 */
package jacob.event.ui.followup;

import jacob.common.AppLogger;
import jacob.common.FormManager;
import jacob.model.Followup;
import jacob.model.Request;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the FollowupSetDoneButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author andherz
 */
public class FollowupSetDoneButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: FollowupSetDoneButton.java,v 1.4 2009/11/23 11:33:44 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.4 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * The user has clicked on the corresponding button.<br>
     * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
     *             if the button has not the [selected] flag.<br>
     *             The selected flag assures that the event can only be fired,<br>
     *             if <code>selectedRecord!=null</code>.<br>
     *
     * @param context The current client context
     * @param button  The corresponding button to this event handler
     * @throws Exception
     */
    public void onClick(IClientContext context, IGuiElement emitter) throws Exception
    {
        IDataTableRecord currentRecord = context.getSelectedRecord();
        IDataTransaction transaction = context.getDataAccessor().newTransaction();
        try
        {
            currentRecord.setValue(transaction, Followup.status, Followup.status_ENUM._done);
            transaction.commit();
            FormManager.showRequest(context, currentRecord.getLinkedRecord(Request.NAME));
            context.showTransparentMessage("Follow Up has been finished");
        }
        finally
        {
            transaction.close();
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
     * @throws Exception
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
    {
        emitter.setVisible(false);
        if(status != IGroup.SELECTED)
            return;

        boolean isProgress = context.getSelectedRecord().getSaveStringValue(Followup.status).equals(Followup.status_ENUM._in_Progress);
        boolean isOpen     = context.getSelectedRecord().getSaveStringValue(Followup.status).equals(Followup.status_ENUM._open);
        emitter.setVisible(isProgress || isOpen);
    }
}
