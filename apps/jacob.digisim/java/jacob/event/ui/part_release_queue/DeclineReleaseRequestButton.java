/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Feb 04 20:18:24 CET 2007
 */
package jacob.event.ui.part_release_queue;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Part;
import jacob.model.Queued_part;

import org.apache.commons.logging.Log;


/**
 * The event handler for the DeclineReleaseRequestButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class DeclineReleaseRequestButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: DeclineReleaseRequestButton.java,v 1.2 2007/02/05 08:08:43 freegroup Exp $";
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
		IDataTransaction transaction = context.getDataAccessor().newTransaction();
		try
		{
			IDataTableRecord queuePart = currentRecord.getLinkedRecord(Queued_part.NAME);

			currentRecord.delete(transaction);
			queuePart.delete(transaction);
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
	  ((IButton)button).setIcon(Icon.delete);
	}
}

