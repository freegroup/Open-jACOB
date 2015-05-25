/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Feb 02 12:09:57 CET 2007
 */
package jacob.event.ui.my_part;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Account;
import jacob.model.My_part;
import jacob.model.Part_release_queue;
import jacob.model.Room;
import jacob.model.Room_admin;

import org.apache.commons.logging.Log;


/**
 * The event handler for the My_partRecordRequestPublishingButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class My_partRecordRequestPublishingButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: My_partRecordRequestPublishingButton.java,v 1.1 2007/02/02 22:26:48 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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

		IDataTransaction trans = context.getDataAccessor().newTransaction();
			
		try 
		{
			IDataAccessor copyAccessor = context.getDataAccessor().newAccessor();
			IDataTableRecord newPart = copyAccessor.cloneRecord(trans, currentRecord);
			// Die Kopie bekommt einen Verweis auf sein Ursprungselement
			//
			newPart.setValue(trans, My_part.part_key2, currentRecord.getValue(My_part.pkey));

			// Es wird noch gekennzeichnet, damit es zu einem "queued" Part wird.
			//
			newPart.setValue(trans, My_part.state, My_part.state_ENUM._request_for_release);
			
			// Es wird ein neuer Eintrag in der Queue erstellt
			//
			IDataTableRecord queue = copyAccessor.getTable(Part_release_queue.NAME).newRecord(trans);
			
			// Der Code wird in die Queue kopiert. Dient nur zur besseren Vorschau für den Genehmiger
			//
			queue.setValue(trans, Part_release_queue.part_code, newPart.getSaveStringValue(My_part.code));
			
			// welches Element soll denn genehmig werden
			//
			queue.setValue(trans,Part_release_queue.part_key, newPart.getValue(My_part.pkey));
			
			// Der Genehmiger wird noch eingetragen
			// Dies ist immer der Raumadministrator von dem Raum in dem der Benutzer sich aufhält
			//
			IDataTable accountTable = copyAccessor.getTable(Account.NAME);
			accountTable.qbeSetKeyValue(Account.pkey, context.getUser().getKey());
			accountTable.search();
			
			IDataTableRecord room = accountTable.getSelectedRecord().getLinkedRecord(Room.NAME);
			IDataTableRecord admin = room.getLinkedRecord(Room_admin.NAME);
			queue.setValue(trans, Part_release_queue.assignee_key, admin.getValue(Room_admin.pkey));
			
			
			trans.commit();
			
			alert("Request for publishing has been send to room administrator");
		} 
		finally 
		{
			trans.close();
		}
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

