/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Oct 24 22:03:14 CEST 2006
 */
package jacob.event.ui.defect;

import jacob.common.AppLogger;
import jacob.model.Defect;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 *
 * @author andherz
 */
public class DefectState extends IComboBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: DefectState.java,v 1.1 2007/11/25 22:17:54 freegroup Exp $";
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
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param status  The new state of the group.
	 * @param emitter The emitter of the event.
	 */
	public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IGuiElement emitter) throws Exception
	{
		IComboBox comboBox = (IComboBox) emitter;
		if(state ==IGuiElement.UPDATE)
		{
			System.out.println("cccccccccccccccccccc");
			comboBox.enableOptions(false);
			// Falls der Record den Status "closed" hat, dann darf auch nur noch dieser Eintrag 
			// in der Box vorhanden sein
			//
			if(context.getSelectedRecord().getSaveStringValue(Defect.state).equals(Defect.state_ENUM._new))
			{
				comboBox.enableOption(Defect.state_ENUM._new, true);
				comboBox.enableOption(Defect.state_ENUM._open, true);
				comboBox.enableOption(Defect.state_ENUM._closed, true);
			}
			else if(context.getSelectedRecord().getSaveStringValue(Defect.state).equals(Defect.state_ENUM._open))
			{
				comboBox.enableOption(Defect.state_ENUM._open, true);
				comboBox.enableOption(Defect.state_ENUM._closed, true);
			}
			else if(context.getSelectedRecord().getSaveStringValue(Defect.state).equals(Defect.state_ENUM._closed))
			{
				comboBox.enableOption(Defect.state_ENUM._closed, true);
			}
		}
	}
}
