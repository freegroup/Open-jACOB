/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Mar 02 18:01:45 CET 2006
 */
package jacob.event.ui.engine;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 *
 * @author andreas
 */
public class EngineLicense_type extends IComboBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: EngineLicense_type.java,v 1.1 2006/03/07 19:20:35 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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

	public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IGuiElement emitter) throws Exception
	{
    // do not allow to change license type in update mode
		emitter.setEnable(state == IGuiElement.NEW || state == IGuiElement.SEARCH);
	}
}
