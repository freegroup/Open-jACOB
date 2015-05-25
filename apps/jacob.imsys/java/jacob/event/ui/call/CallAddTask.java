package jacob.event.ui.call;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Jun 14 14:45:50 CEST 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the CallAddTask-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author mike
 * 
 */
public class CallAddTask extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: CallAddTask.java,v 1.3 2005/12/02 15:11:11 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.3 $";

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
        IDataTableRecord call = context.getSelectedRecord();
        IDataTable task = context.getDataTable("task");
        if (task.recordCount() == 1) // Taskmaske freiputzen und Meldung
                                        // vorbelegen
        {

            context.clearDomain();
            IDataTable callTable = context.getDataTable();
            callTable.qbeSetPrimaryKeyValue(call.getPrimaryKeyValue());
            IDataBrowser callBrowser = context.getDataBrowser();
            callBrowser.search("r_call", Filldirection.BACKWARD);
            callBrowser.setSelectedRecordIndex(0);
            callBrowser.propagateSelections();

        }
        // Sprung zu Auftrag anlegen und Maske vorbereiten!
        String priority = call.getStringValue("priority");

        if (task.recordCount() != 1)
        {
            context.getDomain().setInputFieldValue("UTtask", "taskPriority", priority);
        }
        context.setCurrentForm("UTtask");
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
        if (status == IGuiElement.SELECTED)
        {
            IDataTableRecord currentRecord = context.getSelectedRecord();
            String callstatus = currentRecord.getStringValue("callstatus");
            button.setEnable(callstatus.equals("Angenommen"));
        }
    }
}
