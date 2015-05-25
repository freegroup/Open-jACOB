/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Apr 02 14:16:50 CEST 2009
 */
package jacob.event.ui.request;
import java.sql.Timestamp;
import jacob.common.AppLogger;
import jacob.common.UIUtil;
import jacob.common.tabcontainer.TabManagerQA;
import jacob.model.Customer;
import jacob.model.Event;
import jacob.model.Followup;
import jacob.model.Request;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.DatetimeUtil;
/**
 * The event handler for the RequestAssignFollowUpButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author achim
 */
public class RequestAssignFollowUpButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: RequestAssignFollowUpButton.java,v 1.7 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.7 $";
    /**
     * Use this logger to write messages and NOT the
     * <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * The user has clicked on the corresponding button.<br>
     * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
     * if the button has not the [selected] flag.<br>
     * The selected flag assures that the event can only be fired,<br>
     * if <code>selectedRecord!=null</code>.<br>
     *
     * @param context
     *          The current client context
     * @param button
     *          The corresponding button to this event handler
     * @throws Exception
     */
    public void onClick(IClientContext context, IGuiElement emitter) throws Exception
    {
        //Check before create record
        //
        Boolean isError = false;
        IDataTable requestTable = context.getDataTable(Request.NAME);
        if (requestTable.getSelectedRecord() == null)
        {
            context.showTipDialog(context.getForm().findByName("requestSearchButton"), "Request required.");
            return;
        }
        IDataTable customerTable = context.getDataTable(Customer.NAME);
        if (customerTable.getSelectedRecord() == null)
        {
            context.showTipDialog(context.getForm().findByName("customerSearchButton"), "Customer required.");
            return;
        }
        if (StringUtils.isEmpty(context.getGroup().getInputFieldValue("followupAgent_followup")))
        {
            context.getForm().findByName("followupAgent_followup").setErrorDecoration(context, "Enter an Agent for Follow Up first.");
            isError = true;
        }

        String duedate = context.getGroup().getInputFieldValue("followupDuedate");
        if (StringUtils.isEmpty(duedate))
        {
            context.getForm().findByName("followupDuedate").setErrorDecoration(context, "Enter a Duedate for Follow Up first.");
            isError = true;
        }
        else
        {
            // not empty, check for the past
            Timestamp ts = DatetimeUtil.convertToTimestamp(duedate);
            if (ts.getTime() < System.currentTimeMillis())
            {
                context.getForm().findByName("followupDuedate").setErrorDecoration(context, "The Duedate cannot be in the past.");
                isError = true;
            }
        }
        if (StringUtils.isEmpty(context.getGroup().getInputFieldValue("followupFu_notes")))
        {
            context.getForm().findByName("followupFu_notes").setErrorDecoration(context, "Enter Notes for Follow Up first.");
            isError = true;
        }
        if (isError)
        {
            context.showTransparentMessage("Required Fields are missing or invalid");
            return;
        }


        IDataTable eventTable = context.getDataTable(Event.NAME);
        IDataTableRecord eventRecord = eventTable.getSelectedRecord();
        IDataTransaction trans = context.getDataAccessor().newTransaction();
        IDataTableRecord fuRecord = Followup.newRecord(context, trans);
        try
        {
            fuRecord.setValue(trans, Followup.request_key, requestTable.getSelectedRecord().getValue(Request.pkey));
            eventRecord = Event.newRecord(context, trans);
            eventRecord.setValue(trans, Event.type, Event.type_ENUM._NewFollowUp);
            eventRecord.setValue(trans, Event.followup_key, fuRecord.getValue(Followup.pkey));
            UIUtil.transferFromGuiToRecord(context, trans, TabManagerQA.getFollowupPane(context), fuRecord);
            trans.commit();
        }
        finally
        {
            trans.close();
            fuRecord.getTable().clearRecords();
            requestTable.reloadSelectedRecord();
        }
        context.showTransparentMessage("Follow Up created");
        context.getForm().findByName("followupAgent_followup").resetErrorDecoration(context);
        context.getForm().findByName("followupDuedate").resetErrorDecoration(context);
        context.getForm().findByName("followupFu_notes").resetErrorDecoration(context);
    }

    /**
     * The status of the parent group (TableAlias) has been changed.<br>
     * <br>
     * This is a good place to enable/disable the button on relation to the group
     * state or the selected record.<br>
     * <br>
     * Possible values for the different states are defined in IGuiElement<br>
     * <ul>
     * <li>IGuiElement.UPDATE</li>
     * <li>IGuiElement.NEW</li>
     * <li>IGuiElement.SEARCH</li>
     * <li>IGuiElement.SELECTED</li>
     * </ul>
     *
     * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
     * if the button has not the [selected] flag.<br>
     * The selected flag assures that the event can only be fired,<br>
     * if <code>selectedRecord!=null</code>.<br>
     *
     * @param context
     *          The current client context
     * @param status
     *          The new group state. The group is the parent of the corresponding
     *          event button.
     * @param button
     *          The corresponding button to this event handler
     * @throws Exception
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
    {
        //emitter.setVisible(status == IGroup.SELECTED);
    }
}
