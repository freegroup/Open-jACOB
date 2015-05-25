/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jan 14 10:57:30 CET 2009
 */
package jacob.event.ui.event;

import jacob.common.AppLogger;

import java.awt.Color;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author achim
 */
 public class EventRequestGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: EventRequestGroup.java,v 1.1 2009/02/17 15:12:19 A.Boeken Exp $";
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
   * @param status  The new status of the group.
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
	{
		// insert your code here
	}

  /**
   * Will be called if the will be change the state from visible=>hidden.
   * 
   * This happends if the user switch the Domain or Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onHide(IClientContext context, IGroup group) throws Exception 
  {
    // insert your code here
  }
  
  /**
   * Will be called if the will be change the state from hidden=>visible.
   * 
   * This happends if the user switch to a Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onShow(IClientContext context, IGroup group) throws Exception 
  {
    group.setBackgroundColor(new Color(216,214,217));
  }
}
