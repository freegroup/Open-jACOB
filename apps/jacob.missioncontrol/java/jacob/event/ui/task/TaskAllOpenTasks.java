package jacob.event.ui.task;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Aug 08 11:58:17 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.common.ENUM;
import jacob.common.Task;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


 /**
  * The Event handler for the TaskAllOpenTasks-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class TaskAllOpenTasks extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: TaskAllOpenTasks.java,v 1.3 2005/09/15 20:01:44 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has been click on the corresponding button.<br>
 	 * Be in mind: The currentRecord can be null if the button has not the [selected] flag.<br>
 	 *             The selected flag warranted that the event can only be fired if the<br>
 	 *             selectedRecord!=null.<br>
 	 *
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
	Task.findByState(context,"!"+ENUM.TASKSTATUS_CLOSED,"call");
  }

   
  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
 	 * Be in mind: The currentRecord can be null if the button has not the [selected] flag.<br>
 	 *             The selected flag warranted that the event can only be fired if the<br>
 	 *             selectedRecord!=null.<br>
   *
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
	 * @throws Exception
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
	  //
	  //button.setEnable(true/false);
	}
}

