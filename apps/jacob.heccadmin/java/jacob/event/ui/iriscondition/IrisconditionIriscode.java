/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Dec 17 16:08:24 CET 2008
 */
package jacob.event.ui.iriscondition;

import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.*;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 * 
 * @author R.Spoor
 */
 public class IrisconditionIriscode extends ITextFieldEventHandler
 {
	static public final transient String RCS_ID = "$Id: IrisconditionIriscode.java,v 1.1 2009/02/17 15:23:46 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
	 * @param state The new group status
	 * @param text  The corresponding GUI element of this event handler
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
	{
        text.setEditable(state != IGuiElement.UPDATE && state != IGuiElement.SELECTED);
	}
}
