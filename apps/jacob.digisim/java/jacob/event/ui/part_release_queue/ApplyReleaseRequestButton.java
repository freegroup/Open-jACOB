/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Feb 02 15:28:40 CET 2007
 */
package jacob.event.ui.part_release_queue;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Owner;
import jacob.model.Part;
import jacob.model.Public_part;
import jacob.model.Queued_part;

import org.apache.commons.logging.Log;


/**
 * The event handler for the ApplyReleaseRequestButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class ApplyReleaseRequestButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ApplyReleaseRequestButton.java,v 1.3 2007/08/23 05:13:05 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

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

		IDataTransaction transaction = context.getDataAccessor().newTransaction();
		try
		{
			IDataTableRecord queuePart    = currentRecord.getLinkedRecord(Queued_part.NAME);
			IDataTableRecord origPart     = queuePart.getLinkedRecord(Part.NAME);
			IDataTableRecord existingPart = getExistingRecord(context,queuePart);

			if(existingPart!=null)
			{
				// Falls der bestehende Record nicht von dem gleichen Owner ist wird die Releas�bernahme
				// abgelehnt. Grund Ein fremder User darf nicht einfach Objekte eines anderen �berb�geln
				//
				if(existingPart.getSaveStringValue(Part.owner_key).equals(queuePart.getSaveStringValue(Part.owner_key)))
				{
					existingPart.setValue(transaction, Part.code, queuePart.getValue(Part.code));
					existingPart.setValue(transaction, Part.comment, queuePart.getValue(Part.comment));
					existingPart.setValue(transaction, Part.resource_image, queuePart.getValue(Part.resource_image));
					existingPart.setValue(transaction, Part.tool_image, queuePart.getValue(Part.tool_image));
          existingPart.setValue(transaction, Part.history, queuePart.getValue(Part.history));
          existingPart.setValue(transaction, Part.type, queuePart.getValue(Part.type));
					currentRecord.delete(transaction);
					queuePart.delete(transaction);
				}
				else
				{
					alert("Version conflict. There is already a visible objekt with the same name.");
				}
			}
			else
			{
				String mandatorId = queuePart.getSaveStringValue(Queued_part.mandator_id);
				mandatorId = mandatorId.substring(0, mandatorId.lastIndexOf('/'));

				// Den Record nicht mehr als queued sondern als Release markieren
				//
				queuePart.setValue(transaction, Queued_part.state, Queued_part.state_ENUM._released);
				
				// Eintrage welches Element von diesem Record das Ursprungselement ist.
				//
				queuePart.setValue(transaction, Queued_part.part_key2, null); //Relation queued_part => part
				queuePart.setValue(transaction, Queued_part.part_key, origPart.getValue(Part.pkey)); // Relation released_part => part
	
				// eintragen wer das Release genehmigt hat
				//
				queuePart.setValue(transaction, Queued_part.assignee_key, context.getUser().getKey());
	
				// In beiden Records eintragen wann das Release �bernommen worden ist
				//
				queuePart.setValue(transaction, Queued_part.last_release, "now");
				origPart.setValue(transaction, Queued_part.last_release, "now");
				
				// MandatorId des released Part anpassen => es wird f�r alle in dem selben Raum sichtbar
				//
				queuePart.setStringValue(transaction, Queued_part.mandator_id, mandatorId);
				currentRecord.delete(transaction);
			}
			transaction.commit();
			context.getGroup().clear(context);
		}
		finally
		{
			transaction.close();
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
	  ((IButton)button).setIcon(Icon.accept);
	}
	
	private IDataTableRecord getExistingRecord(IClientContext context, IDataTableRecord record) throws Exception
	{
		String partName = record.getStringValue(Part.name);
		
		// es wird mit dem Alias "public_part" gesucht ob bereits ein Record vorhanden ist.
		// Falls ja, wird gepr�ft ob dieser vom selben Benutzer stammt. Nur der owner eines
		// Part darf dies dann �berschreiben.
		//
		IDataTable partTable = context.getDataAccessor().newAccessor().getTable(Public_part.NAME);
		partTable.qbeSetKeyValue(Public_part.name, partName);
		partTable.search();
		
		// Wenn es mehr als ein sichtbares Objekt mit dem gleichen Namen gibt, dann
		// ist etwas im Argen.
		//
		if(partTable.recordCount()>1)
			throw new UserException("More than one part with the same name ["+partName+"] visible.");
		
		// Es gibt ein sichtbares Objekt => pr�fen ob es vom gleichen owner ist
		//
		if(partTable.recordCount()==1)
			return partTable.getSelectedRecord();
		
		return null;
	}
}

