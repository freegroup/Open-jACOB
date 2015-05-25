package jacob.event.ui.call;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Aug 05 15:12:45 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.common.Call;
import jacob.common.ENUM;
import jacob.event.data.CallTableRecord;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the CallSetDone-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author mike
 * 
 */
public class CallSetDone extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: CallSetDone.java,v 1.5 2005/09/22 13:59:36 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.5 $";

    // use this logger to write messages and NOT the System.println(..) ;-)
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * The user has been click on the corresponding button.
     * 
     * @param context
     *            The current client context
     * @param button
     *            The corresponding button to this event handler
     * @throws Exception
     */
    public void onAction(IClientContext context, IGuiElement button) throws Exception
    {
        IDataTableRecord currentRecord = context.getSelectedRecord();
        Call.setStatus(context, currentRecord, ENUM.CALLSTATUS_DONE);
    }

    /**
     * The status of the parent group (TableAlias) has been changed.<br>
     * <br>
     * This is a good place to enable/disable the button on relation to the
     * group state or the selected record.<br>
     * <br>
     * Possible values for the state is defined in IGuiElement<br>
     * <ul>
     * <li>IGuiElement.UPDATE</li>
     * <li>IGuiElement.NEW</li>
     * <li>IGuiElement.SEARCH</li>
     * <li>IGuiElement.SELECTED</li>
     * </ul>
     * 
     * @param context
     *            The current client context
     * @param status
     *            The new group state. The group is the parent of the
     *            corresponding event button.
     * @param button
     *            The corresponding button to this event handler
     * @throws Exception
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
    {
        if (status != IGuiElement.SELECTED)
            return;
        IDataTableRecord call = context.getSelectedRecord();
        String callstatus = call.getSaveStringValue("status");
        button.setEnable(!(CallTableRecord.checkForOpenTasks(call, "!" + ENUM.TASKSTATUS_CLOSED + "|" + ENUM.TASKSTATUS_DONE)
                || ENUM.CALLSTATUS_CLOSED.equals(callstatus)||ENUM.CALLSTATUS_DONE.equals(callstatus)));
    }
}
