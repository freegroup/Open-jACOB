package jacob.event.ui.incident;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Aug 31 12:57:05 CEST 2005
 *
 */
import jacob.model.Incident;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * The Event handler for the DefaultName-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andherz
  *
  */
public class CreateRequestButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CreateRequestButton.java,v 1.7 2009-12-24 11:31:38 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.7 $";

	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
		IDataTableRecord currentRecord = context.getSelectedRecord();
     
		context.getDataTable("owner").clear();
//		context.getDataTable("category").clear();
		context.getDataTable("tester").clear();
		context.getDataTable("main_request").clear();
		context.getDataTable("request_attachment").clear();
		context.getDataTable("request_incident").clear();
		

    IDataTable requestTable = context.getDataTable("request");
    IDataTransaction trans = requestTable.startNewTransaction();
    IDataTableRecord record = requestTable.newRecord(trans);

    IDataTable linkTable = context.getDataTable("request_incident");
    IDataTableRecord link = linkTable.newRecord(trans);

    IDataTable createrTable = context.getDataTable("creater");
    createrTable.qbeClear();
    createrTable.qbeSetKeyValue("pkey",context.getUser().getKey());
    createrTable.search();
    
    link.setValue(trans,"incident_key",currentRecord.getValue("pkey"));
    link.setValue(trans,"request_key",record.getValue("pkey"));
    
    record.setValue(trans, "subject", currentRecord.getStringValue("subject"));
    record.setValue(trans, "description", currentRecord.getStringValue("description"));
    record.setValue(trans, "priority", currentRecord.getStringValue("priority"));
    
    // wenn der Status des Incidents noch neu ist, dann den Status auf in progress setzen
    if (Incident.state_ENUM._New.equals(currentRecord.getStringValue(Incident.state)))
    {
      currentRecord.setValue(trans,Incident.state,Incident.state_ENUM._Progress);
    }

    context.setCurrentForm("request");	  

  }

   
  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
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
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
	  //
	  //button.setEnable(true/false);
	}
}

