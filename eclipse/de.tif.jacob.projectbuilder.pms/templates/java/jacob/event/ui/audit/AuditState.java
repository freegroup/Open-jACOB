/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Oct 31 09:38:03 CET 2006
 */
package jacob.event.ui.audit;

import jacob.common.AppLogger;
import jacob.model.Audit;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 *
 * @author andherz
 */
public class AuditState extends IComboBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: AuditState.java,v 1.1 2007/11/25 22:17:54 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * Called, if the user changed the selection during the NEW or UPDATE state 
	 * of the related table record.
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param emitter The emitter of the event.
	 */
	public void onSelect(IClientContext context, IComboBox emitter) throws Exception
	{
	}

	/**
	 * The event handler if the group status has been changed.<br>
	 * This is a good place to enable/disable some combo box entries in relation to the state of the
	 * selected record.<br>
	 * <br>
	 * Note: You can only enable/disable <b>valid</b> enum values of the corresponding table field.<br>
	 * <br>
	 * <code>
	 *	 IComboBox comboBox =(IComboBox)emitter;
	 *	 // remove all combo box entries
	 *	 comboBox.enableOptions(false);
	 *   if(..some condition...)
	 *   {  
	 *     comboBox.enableOption("Duplicate",true);
	 *     comboBox.enableOption("Declined",true);
	 *   }
	 *   else if(...another condition...)
	 *   {  
	 *     comboBox.enableOption("Proved",true);
	 *     comboBox.enableOption("In progress",true);
	 *     comboBox.enableOption("QA",true);
	 *   }
	 *   else // enable all options
	 *   	comboBox.enableOptions(true);
	 * 
	 * </code>
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param status  The new state of the group.
	 * @param emitter The emitter of the event.
	 */
	public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IGuiElement emitter) throws Exception
	{
		IComboBox comboBox = (IComboBox) emitter;
		if(state != IGuiElement.UPDATE)
			return;
		
		comboBox.enableOptions(false);
		// Ein Abgesagtes Audit kann wieder reaktiviert werden
		//
		if(context.getSelectedRecord().getSaveStringValue(Audit.state).equals(Audit.state_ENUM._Abgesagt))
		{
			comboBox.enableOption(Audit.state_ENUM._Geplant,true);
		}
		// Wenn ein Audit mal abgeschlossen war, dann kann dieses nicht wieder reaktiviert werden
		//
		else if(context.getSelectedRecord().getSaveStringValue(Audit.state).equals(Audit.state_ENUM._Abgeschlossen))
		{
			
		}
		else
		{
			comboBox.enableOptions(true);
		}
	}
}
