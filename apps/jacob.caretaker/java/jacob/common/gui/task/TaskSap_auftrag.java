/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Feb 14 10:50:42 CET 2008
 */
package jacob.common.gui.task;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author achim
 */
 public class TaskSap_auftrag extends ITextFieldEventHandler 
 {
	static public final transient String RCS_ID = "$Id: TaskSap_auftrag.java,v 1.1 2008/02/19 14:49:59 achim Exp $";
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
   * @param status The new group status
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
    if (status != IGroup.SEARCH)
    {
      emitter.setEnable(false);
    }
    else 
    {
      emitter.setEnable(true);
    }
	}
}
