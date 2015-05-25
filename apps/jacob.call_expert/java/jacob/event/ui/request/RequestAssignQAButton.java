/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Apr 02 12:39:28 CEST 2009
 */
package jacob.event.ui.request;

import jacob.common.AppLogger;
import jacob.common.UIUtil;
import jacob.common.tabcontainer.TabManagerQA;
import jacob.model.Contact_summary;
import jacob.model.Customer;
import jacob.model.Event;
import jacob.model.Question_answer;
import jacob.model.Request;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.IDataTransaction.EmbeddedTransactionMode;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the RequestAssignQAButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author achim
 */
public class RequestAssignQAButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: RequestAssignQAButton.java,v 1.8 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.8 $";

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
        IDataTable contact_summaryTable = context.getDataTable(Contact_summary.NAME);
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
        if (contact_summaryTable.getSelectedRecord() == null)
        {
            context.showTipDialog(context.getForm().findByName("contact_summarySearchButton"), "Contact required.");
            return;
        }
        Boolean isError = false;
        if (StringUtils.isEmpty(context.getGroup().getInputFieldValue("question_answerQuestion")))
        {
            context.getForm().findByName("question_answerQuestion").setErrorDecoration(context, "Enter a question first.");
            isError = true;

        }
        if (StringUtils.isEmpty(context.getGroup().getInputFieldValue("question_answerAnswer")))
        {
            context.getForm().findByName("question_answerAnswer").setErrorDecoration(context, "Enter an Answer first.");
            isError = true;

        }
        if (isError)
        {
            context.showTransparentMessage("Required Fields are missing");
            return;
        }

        IDataTable eventTable = context.getDataTable(Event.NAME);
        IDataTableRecord eventRecord = eventTable.getSelectedRecord();
        IDataTableRecord contactRecord = contact_summaryTable.getSelectedRecord();
        IDataTableRecord qaRecord;
        IDataTransaction eventTrans = null;
        IDataTransaction qaTrans = null;

        if (eventRecord != null && eventRecord.isNew())
        {
            eventTrans = eventRecord.getCurrentTransaction();
            qaTrans = eventTrans.newEmbeddedTransaction(EmbeddedTransactionMode.PREPEND);
            qaRecord = Question_answer.newRecord(context, qaTrans);
        }
        else
        {
            eventTrans = context.getDataAccessor().newTransaction();
            qaTrans = eventTrans;
            qaRecord = Question_answer.newRecord(context, qaTrans);

            eventRecord = Event.newRecord(context, eventTrans);
            eventRecord.setValue(eventTrans, Event.type, Event.type_ENUM._NewQA);
        }
        eventRecord.setValue(eventTrans, Event.question_answer_key, qaRecord.getValue(Question_answer.pkey));

        qaRecord.setValue(qaTrans, Question_answer.request_key, requestTable.getSelectedRecord().getValue(Request.pkey));
        qaRecord.setValue(qaTrans, Question_answer.contact_summary_key, contactRecord.getValue(Contact_summary.eduid));
        UIUtil.transferFromGuiToRecord(context, qaTrans, TabManagerQA.getQAPane(context), qaRecord);

        eventTrans.commit();
        qaRecord.getTable().clearRecords();
        requestTable.reloadSelectedRecord();
        context.showTransparentMessage("Question & Answer attached");
        context.getForm().findByName("question_answerQuestion").resetErrorDecoration(context);
        context.getForm().findByName("question_answerAnswer").resetErrorDecoration(context);
        eventTrans.close();

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
