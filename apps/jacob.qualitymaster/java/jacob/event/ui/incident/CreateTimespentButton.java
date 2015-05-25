/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Oct 10 12:06:31 CEST 2008
 */
package jacob.event.ui.incident;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Budget;
import jacob.model.Employee;
import jacob.model.Incident;
import jacob.model.Incident_budget;
import jacob.model.Timespent;

import org.apache.commons.logging.Log;


/**
 * The event handler for the CreateTimespentButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class CreateTimespentButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CreateTimespentButton.java,v 1.2 2009-05-06 22:00:27 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
    IDataTableRecord currentRecord = context.getSelectedRecord();
    IDataTableRecord incidentBudgetRecord = currentRecord.getLinkedRecord(Incident_budget.NAME);
    
    context.getDataTable(Employee.NAME).clear();
    context.getDataTable(Budget.NAME).clear();

    IDataTable timespentTable = context.getDataTable(Timespent.NAME);
    IDataTransaction trans = timespentTable.startNewTransaction();
    IDataTableRecord timespentRecord = timespentTable.newRecord(trans);

    IDataTable createrTable = context.getDataTable(Employee.NAME);
    createrTable.qbeClear();
    createrTable.qbeSetKeyValue("pkey",context.getUser().getKey());
    createrTable.search();
    
    timespentRecord.setValue(trans, Timespent.category, Timespent.category_ENUM._Coding);
    timespentRecord.setValue(trans, Timespent.summary, currentRecord.getSaveStringValue(Incident.subject));

    //
    if(incidentBudgetRecord!=null)
    {
       IDataTable budgetTable = context.getDataTable(Budget.NAME);
       budgetTable.clear();
       budgetTable.qbeSetValue(Budget.pkey, incidentBudgetRecord.getValue(Incident_budget.pkey));
       budgetTable.search();
    }
    context.setCurrentForm("timespent");    
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
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
}

