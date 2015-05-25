package jacob.event.ui.call;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Jun 14 15:09:44 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.Call;
import jacob.common.data.DataUtils;

import org.apache.commons.logging.Log;



 /**
  * The Event handler for the CallSubCall-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CallSubCall extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CallSubCall.java,v 1.2 2006/05/15 13:50:56 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
        IDataAccessor accessor = context.getDataAccessor();
        IDataTableRecord oldcall = context.getSelectedRecord();
        
        if (oldcall==null)
          return;
        
        IDataTable callTable = context.getDataTable();

        IDataTransaction newTrans = callTable.startNewTransaction();
        IDataTableRecord newcall = null;

        
        // Wenn der Mastercall einen Location-Datensatz hat
        if (oldcall.hasLinkedRecord("location"))
        {       
                IDataTableRecord oldLocationRecord = oldcall.getLinkedRecord("location");
                IDataTableRecord newLocationRecord = accessor.cloneRecord(newTrans, oldLocationRecord, "location");
                newcall = callTable.newRecord(newTrans);
                newcall.setLinkedRecord(newTrans,newLocationRecord);            

        }
        else
        {
            newcall = callTable.newRecord(newTrans);
        }
        // linktable ist notwendig, da der Melder Record bei SetDefault benutzt wird!
        //newcall.setValue(newTrans,"employeecall",context.getUser().getKey());
        String userKey = context.getUser().getKey();
        DataUtils.linkTable(context,newTrans,newcall,"employeecall","customerint","pkey",userKey);
        
        if (newcall.hasLinkedRecord("customerint") )
        {
            newcall.setValue(newTrans,"callbackmethod",newcall.getLinkedRecord("customerint").getValue("communicatepref"));
        }

        newcall.setValue(newTrans,"accountingcode_key", oldcall.getValue("accountingcode_key"));
        newcall.setValue(newTrans,"accountingcodetext", oldcall.getValue("accountingcodetext"));
        newcall.setValue(newTrans,"affectedperson", oldcall.getValue("affectedperson"));
        newcall.setValue(newTrans,"affectedperson_key", oldcall.getValue("affectedperson_key"));
        Call.setDefault(context,newcall,newTrans);
        newcall.setValue(newTrans,"mastercall_key",oldcall.getValue("pkey"));
        newcall.setValue(newTrans,"priority",oldcall.getValue("priority"));
        newcall.setValue(newTrans,"origin","selbst");
        newcall.setValue(newTrans,"categorycall", oldcall.getValue("categorycall"));
        newcall.setValue(newTrans,"object_key", oldcall.getValue("object_key"));
        newcall.setValue(newTrans,"process_key", oldcall.getValue("process_key"));
        accessor.propagateRecord(newcall,accessor.getApplication().getRelationSet("r_call"), Filldirection.BOTH);
        context.setCurrentForm("UTcallEntryCaretaker");
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
        if(status == IGuiElement.SELECTED)
        {
            IDataTableRecord currentRecord = context.getSelectedRecord();
            String callstatus = currentRecord.getStringValue("callstatus");
            String mastercall = currentRecord.getStringValue("mastercall_key");
            button.setEnable(callstatus.equals("Angenommen") && mastercall == null);
        }
	}
}

