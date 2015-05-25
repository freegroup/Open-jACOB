package jacob.event.ui.call;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Jun 15 16:04:58 CEST 2005
 *
 */
import java.util.Set;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the CallGMZtask-Button. <br>
 * The onAction will be calle if the user clicks on this button <br>
 * Insert your custom code in the onAction-method. <br>
 * 
 * @author mike
 * 
 */
public class CallGMZtask extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: CallGMZtask.java,v 1.2 2005/09/01 11:59:20 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.2 $";

    private static final String GMZKEY = "3";

    private static final String GMZTASKCODE = "GMZ";

    private static final String HWGKEY = "100544";

    // use this logger to write messages and NOT the System.println(..) ;-)
    static private final transient Log logger = AppLogger.getLogger();

    public static class AskCallback implements IAskDialogCallback
    {
        /* Do noting if the users cancel the AskDialog */
        public void onCancel(IClientContext context)
        {
        }

        /**
         * @param context
         * @param coordtime
         */
        /* Called if the user press [ok] in the AskDialog */
        public void onOk(IClientContext context, String gmzCode) throws Exception
        {
            IDataTableRecord callRec = context.getSelectedRecord();
            IDataTable taskTable = context.getDataTable("task");
            IDataTransaction trans = taskTable.startNewTransaction();
            try
            {
                IDataTableRecord taskRec = taskTable.newRecord(trans);
                taskRec.setValue(trans, "extsystem_id", gmzCode);
                taskRec.setValue(trans, "taskstatus", "In Arbeit");
                taskRec.setValue(trans, "summary", " GMZ Auftragsnr. " + gmzCode);
                taskRec.setValue(trans, "taskstatus", "In Arbeit");
                taskRec.setValue(trans, "ext_system_key", GMZKEY);
                taskRec.setValue(trans, "tasktype_key", GMZTASKCODE);
                taskRec.setValue(trans, "calltask", callRec.getValue("pkey"));
                taskRec.setValue(trans, "workgrouptask", HWGKEY);
                trans.commit();
                // den datensatz in der GUI refreshen!
                IRelationSet myRelation = context.getApplicationDefinition().getRelationSet("r_task");

                context.getDataAccessor().propagateRecord(taskRec, myRelation, Filldirection.BOTH);
                context.setCurrentForm("UTtask");
            }
            finally
            {
                trans.close();
            }
        }
    }

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
        IAskDialog dialog = context.createAskDialog("Bitte die GMZ-Auftragsnr. eingeben", "0", new AskCallback());
        dialog.show();
    }

    /**
     * The status of the parent group (TableAlias) has been changed. <br>
     * <br>
     * This is a good place to enable/disable the button on relation to the
     * group state or the selected record. <br>
     * <br>
     * Possible values for the state is defined in IGuiElement <br>
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
