/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Apr 02 12:30:43 CEST 2009
 */
package jacob.event.ui.event;

import jacob.common.AppLogger;
import jacob.common.UIUtil;
import jacob.model.Contact_summary;
import jacob.model.Event;
import jacob.model.Question_answer;
import jacob.model.Request;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.IDataTransaction.EmbeddedTransactionMode;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;


/**
 * The event handler for the EventFinishButton update button.<br>
 *
 * @author achim
 */
public class EventFinishButton extends IActionButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: EventFinishButton.java,v 1.2 2009/11/23 11:33:44 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

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
        IDataTable contact_summaryTable = context.getDataTable(Contact_summary.NAME);
        IDataTable requestTable = context.getDataTable(Request.NAME);
        IDataTable qaTable = context.getDataTable(Question_answer.NAME);

        if (contact_summaryTable.getSelectedRecord() == null || requestTable.getSelectedRecord() == null)
        {
            throw new UserException("Contact and Request required.");
        }

        IDataTable eventTable = context.getDataTable(Event.NAME);
        IDataTableRecord eventRecord = eventTable.getSelectedRecord();
        IDataTableRecord contactRecord = contact_summaryTable.getSelectedRecord();

        IDataTransaction trans = eventRecord.getCurrentTransaction();
        IDataTableRecord qaRecord =qaTable.getSelectedRecord();
        if(qaRecord==null)
        {
            IDataTransaction topTrans = trans.newEmbeddedTransaction(EmbeddedTransactionMode.PREPEND);
            qaRecord = Question_answer.newRecord(context, topTrans);
            qaRecord.setValue(topTrans, Question_answer.request_key, requestTable.getSelectedRecord().getValue(Request.pkey));
            qaRecord.setValue(topTrans, Question_answer.contact_summary_key, contactRecord.getValue(Contact_summary.eduid));
        }
        UIUtil.transferFromGuiToRecord(context, qaRecord.getCurrentTransaction(), button.getGroup(), qaRecord);

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
        IDataTable qaTable = context.getDataTable(Question_answer.NAME);
        qaTable.clearRecords();
        context.showTransparentMessage("Question & Answer attached..");
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
    public void onOuterGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
    {
        button.setVisible(IGroup.UPDATE == status || IGroup.NEW == status);
        button.setLabel("Finish");
    }
}
