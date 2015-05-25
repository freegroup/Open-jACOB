/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on {date}
 */
package {package};

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IRadioButtonGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IRadioButtonGroupEventHandler;

/**
 *
 * @author {author}
 */
public class {class} extends IRadioButtonGroupEventHandler
{
	static public final transient String RCS_ID = "$Id: IRadioButtonGroupEventHandler.java,v 1.1 2007/05/18 16:13:26 freegroup Exp $";
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
	public void onSelect(IClientContext context, IRadioButtonGroup emitter) throws Exception
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
    IRadioButtonGroup radiobuttonGroup = (IRadioButtonGroup) emitter;
	}
}
