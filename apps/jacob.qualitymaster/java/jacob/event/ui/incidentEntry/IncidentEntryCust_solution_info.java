/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Feb 18 00:11:47 CET 2006
 */
package jacob.event.ui.incidentEntry;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;


/**
 *
 * @author andreas
 */
 public class IncidentEntryCust_solution_info extends ITextFieldEventHandler 
 {
	static public final transient String RCS_ID = "$Id: IncidentEntryCust_solution_info.java,v 1.1 2006/02/24 02:16:15 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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
   * @param status The new group status
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
    // do not allow customer to edit customer info
    emitter.setEnable(status == IGuiElement.SEARCH);
	}
}
