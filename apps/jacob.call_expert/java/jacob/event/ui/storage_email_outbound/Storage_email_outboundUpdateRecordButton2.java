/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Jun 23 11:43:59 CEST 2009
 */
package jacob.event.ui.storage_email_outbound;

import jacob.common.AppLogger;
import jacob.common.FormManager;
import jacob.common.tabcontainer.TabManagerQA;
import jacob.model.Customer;
import jacob.model.Event;
import jacob.model.Question_answer;
import jacob.model.Request;
import jacob.model.Storage_email;
import jacob.resources.I18N;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * The event handler for the Storage_email_outboundUpdateRecordButton2 update
 * button.<br>
 *
 * @author achim
 */
public class Storage_email_outboundUpdateRecordButton2 extends IActionButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: Storage_email_outboundUpdateRecordButton2.java,v 1.6 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.6 $";

    /**
     * Use this logger to write messages and NOT the
     * <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * This event handler will be called, if the corresponding button has been
     * pressed. You can prevent the execution of the UPDATE action if you return
     * <code>false</code>.<br>
     *
     * @param context
     *          The current context of the application
     * @param button
     *          The action button (the emitter of the event)
     * @return Return <code>false</code>, if you want to avoid the execution of
     *         the action else return <code>true</code>.
     */
    public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
    {
        String question = context.getGroup().getInputFieldValue("OEquestion_answerQuestion");
        String answer = context.getGroup().getInputFieldValue("OEquestion_answerAnswer");
        String notes = context.getGroup().getInputFieldValue("OEquestion_answerNotes");
        if (StringUtils.isEmpty(question)||StringUtils.isEmpty(answer))
        {
            alert("Question or Answer not set");
            return false;
        }
        IDataTableRecord email = context.getSelectedRecord();
        IDataTransaction transaction = email.getCurrentTransaction();
        email.setValue(transaction, Storage_email.status, Storage_email.status_ENUM._Queued);
        email.setValue(transaction, Storage_email.emailmimetype, "text/html");
        IDataTable eventTable = context.getDataTable(Event.NAME);
        IDataTableRecord requestrec=context.getDataTable(Request.NAME).getSelectedRecord();
        IDataTableRecord customerrec=context.getDataTable(Customer.NAME).getSelectedRecord();


        //Set Q&A
        //
        IDataTableRecord qa = Question_answer.newRecord(context, transaction);
        qa.setValue(transaction, Question_answer.question, question);
        qa.setValue(transaction, Question_answer.answer, answer);
        qa.setValue(transaction, Question_answer.notes, notes);
        qa.setValue(transaction, Question_answer.request_key, requestrec.getValue(Request.pkey));

        // ensure that you create the event record once
        //
        IDataTableRecord eventrec = eventTable.getSelectedRecord();
        if(eventrec== null || eventrec.isNew()==false)
        {
            eventrec = Event.newRecord(context, transaction);
            eventrec.setValue(transaction, Event.direction, Event.direction_ENUM._out);
            eventrec.setValue(transaction, Event.media_type, Event.media_type_ENUM._email);
            eventrec.setLinkedRecord(transaction, requestrec);
            eventrec.setLinkedRecord(transaction, qa);
            eventrec.setLinkedRecord(transaction, context.getSelectedRecord());
            eventrec.setLinkedRecord(transaction, customerrec);
        }

        return true;
    }

    /**
     * This event method will be called, if the UPDATE action has been
     * successfully executed.<br>
     *
     * @param context
     *          The current context of the application
     * @param button
     *          The action button (the emitter of the event)
     */
    public void onSuccess(IClientContext context, IGuiElement button) throws Exception
    {
        IDataTableRecord requestRecord = context.getDataTable(Request.NAME).getSelectedRecord();

        context.getDomain().setVisible(false);
        FormManager.showRequest(context, requestRecord);
        TabManagerQA.showFollowUpTab(context);
        context.showTransparentMessage("eMail successfully sent");
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
     * @param context
     *          The current client context
     * @param status
     *          The new group state. The group is the parent of the corresponding
     *          event button.
     * @param button
     *          The corresponding button to this event handler
     * @throws Exception
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
    {
        button.setLabel(I18N.SEND_EMAIL.get(context));
    }
}
