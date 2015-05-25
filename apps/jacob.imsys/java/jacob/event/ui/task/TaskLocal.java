package jacob.event.ui.task;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Jun 23 14:36:34 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.common.Task;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ISearchActionEventHandler;

/**
 * This is an event handler for a update button.
 * 
 * @author mike
 *
 */
public class TaskLocal extends ISearchActionEventHandler 
{
  static public final transient String RCS_ID = "$Id: TaskLocal.java,v 1.1 2005/06/27 12:23:19 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();


  /**
   * This event handler will be called if the corresponding button has been pressed.
   * You can prevent the execution of the SEARCH action if you return [false].<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return 'false' if you want to avoid the execution of the action else return [true]
   */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
        // geht eigentlich nicht, da Local Releationset
        Task.setGroubmemberConstraint(context);
        button.setSearchMode(true);
		return true;
	}

  /**
   * This event method will be called if the search action has been successfully done.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
	}

  /**
   * The event handle if the status of the group has been changed.
   * This is a good place to enable/disable the button on relation to the
   * group state.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context The current client context
   * @param button  The corresbonding button to this event handler
   * 
   */
	public void onGroupStatusChanged(IClientContext context, GroupState status,	IGuiElement button) throws Exception 
	{
	  // you can enable/disable the update button
	  //
	  //button.setEnable(true/false);
	}
}
