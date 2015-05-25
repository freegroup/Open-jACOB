package jacob.event.ui.task;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Jun 23 13:33:42 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.common.Task;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * This is an event handler for the  New-Button.
 * 
 * @author mike
 *
 */
public class TaskNew extends IActionButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: TaskNew.java,v 1.1 2005/06/27 12:23:19 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();


  /**
   * This event handler will be called if the corresponding button has been pressed.
   * You can prevent the execution of the NEW action if you return [false].<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return 'false' if you want to avoid the execution of the action else return [true]
   */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
		return true;
	}

  /**
   * This event method will be called if the NEW action has been successfully done.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception
	{
        IDataTableRecord newtask = context.getSelectedRecord();
        IDataTransaction newtrans= newtask.getCurrentTransaction();
        Task.setDefault(context.getDataAccessor(),newtask,newtrans);
	}

  /**
   * @param context The current client context
   * @param status  The current state of the group
   * @param emitter  The corresbonding button/emitter to this event handler
   * 
   */
	public void onGroupStatusChanged(IClientContext context, GroupState status,	IGuiElement emitter) throws Exception 
	{
	  // you can enable/disable the delete button
	  //
	  //emitter.setEnable(true/false);
	}
}
