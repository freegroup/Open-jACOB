/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Dec 30 18:06:36 CET 2005
 */
package jacob.event.ui.call;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.Call;
import jacob.common.ENUM;
import jacob.event.data.CallTableRecord;
import jacob.model.Solution;

import org.apache.commons.logging.Log;


/**
 * The event handler for the CallSetQA record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author mike
 */
public class CallSetQA extends  IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: CallSetQA.java,v 1.2 2006/11/10 07:51:56 achim Exp $";

    static public final transient String RCS_REV = "$Revision: 1.2 $";

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
        IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
        try
        {
            currentRecord.setValue(currentTransaction, jacob.model.Call.callstatus,  jacob.model.Call.callstatus_ENUM._QA);
            
            currentTransaction.commit();
        }
        finally
        {
            currentTransaction.close();
        }
     
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
        String callstatus = call.getSaveStringValue(jacob.model.Call.callstatus);
        button.setEnable(!(CallTableRecord.checkForOpenTasks(call, "New|Assigned|Owned" ))&&
                ( ENUM.CALLSTATUS_OWNED.equals(callstatus)||ENUM.CALLSTATUS_QA.equals(callstatus) &&
                (call.getValue(jacob.model.Call.solutiontext) != null || call.getValue(jacob.model.Call.solution_key)!= null)));
    }
}
