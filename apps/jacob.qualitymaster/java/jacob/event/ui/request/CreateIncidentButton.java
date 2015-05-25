/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Oct 09 11:13:57 CEST 2008
 */
package jacob.event.ui.request;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Creater;
import jacob.model.Customer;
import jacob.model.Incident;
import jacob.model.IncidentCategory;
import jacob.model.Milestone;
import jacob.model.Organization;
import jacob.model.Request;
import jacob.model.Request_incident;

import org.apache.commons.logging.Log;


/**
 * The event handler for the CreateIncidentButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class CreateIncidentButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CreateIncidentButton.java,v 1.2 2009-05-06 22:00:27 herz Exp $";
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
    IDataTableRecord requestRecord = context.getSelectedRecord();
    
    context.getDataTable(IncidentCategory.NAME).clear();
    context.getDataTable(Customer.NAME).clear();
    context.getDataTable(Organization.NAME).clear();
    context.getDataTable(Milestone.NAME).clear();
    

    IDataTable incidentTable = context.getDataTable(Incident.NAME);
    IDataTransaction trans = incidentTable.startNewTransaction();
    IDataTableRecord incidentRecord = incidentTable.newRecord(trans);

    IDataTable linkTable = context.getDataTable(Request_incident.NAME);
    IDataTableRecord link = linkTable.newRecord(trans);

    IDataTable customerTable = context.getDataTable(Customer.NAME);
    customerTable.qbeClear();
    customerTable.qbeSetKeyValue(Customer.pkey,context.getUser().getKey());
    customerTable.search();
    
    IDataTable incidentCategoryTable = context.getDataTable(IncidentCategory.NAME);
    incidentCategoryTable.qbeClear();
    incidentCategoryTable.qbeSetKeyValue(IncidentCategory.pkey,requestRecord.getValue(Request.category_key));
    incidentCategoryTable.search();

    link.setValue(trans,Request_incident.incident_key,incidentRecord.getValue(Incident.pkey));
    link.setValue(trans,Request_incident.request_key,requestRecord.getValue(Request.pkey));
    
    incidentRecord.setValue(trans, Incident.subject, requestRecord.getStringValue(Request.subject));
    incidentRecord.setValue(trans, Incident.subject, requestRecord.getStringValue(Request.subject));
    incidentRecord.setValue(trans, Incident.description, requestRecord.getStringValue(Request.description));
    incidentRecord.setValue(trans, Incident.priority, Incident.priority_ENUM._Medium);
    incidentRecord.setValue(trans, Incident.state, Incident.state_ENUM._New);
    
    // wenn der Status des Request auf QA oder DONE ist, dann wird der Status des Incident angepasst.
    if (Request.state_ENUM._Done.equals(requestRecord.getStringValue(Request.state)))
      incidentRecord.setValue(trans,Incident.state,Incident.state_ENUM._Done);
    else if (Request.state_ENUM._In_progress.equals(requestRecord.getStringValue(Request.state)))
      incidentRecord.setValue(trans,Incident.state,Incident.state_ENUM._Progress);
    else if (Request.state_ENUM._Proved.equals(requestRecord.getStringValue(Request.state)))
      incidentRecord.setValue(trans,Incident.state,Incident.state_ENUM._Progress);
    else if (Request.state_ENUM._Declined.equals(requestRecord.getStringValue(Request.state)))
      incidentRecord.setValue(trans,Incident.state,Incident.state_ENUM._Done);
    else if (Request.state_ENUM._Obsolete.equals(requestRecord.getStringValue(Request.state)))
      incidentRecord.setValue(trans,Incident.state,Incident.state_ENUM._Done);
    else if (Request.state_ENUM._QA.equals(requestRecord.getStringValue(Request.state)))
      incidentRecord.setValue(trans,Incident.state,Incident.state_ENUM._QA);

    context.setCurrentForm("incident");    
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

