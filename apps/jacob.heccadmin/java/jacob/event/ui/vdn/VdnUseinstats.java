/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jan 12 11:39:54 CET 2009
 */
package jacob.event.ui.vdn;

import jacob.common.AppLogger;
import jacob.model.Vdn;
import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 *
 * @author R.Spoor
 */
public class VdnUseinstats extends IComboBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: VdnUseinstats.java,v 1.1 2009/02/17 15:23:41 R.Spoor Exp $";
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
    @Override
	public void onSelect(IClientContext context, IComboBox emitter) throws Exception
	{
        String value = emitter.getValue();
        boolean selected = Vdn.useinstats_ENUM._yes.equals(value);
        IGroup group = context.getGroup();
        ISingleDataGuiElement element = (ISingleDataGuiElement)group.findByName("vdnSlapercentage");
        element.setRequired(selected);
        element = (ISingleDataGuiElement)group.findByName("vdnSlaseconds");
        element.setRequired(selected);
        element = (ISingleDataGuiElement)group.findByName("vdnHanguptime");
        element.setRequired(selected);
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
	 * @param state   The new state of the group.
	 * @param emitter The emitter of the event.
	 */
	@Override
	public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IGuiElement emitter) throws Exception
	{
		IComboBox comboBox = (IComboBox)emitter;
		onSelect(context, comboBox);
	}
}
