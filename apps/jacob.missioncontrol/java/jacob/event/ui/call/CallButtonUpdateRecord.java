package jacob.event.ui.call;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Aug 09 15:43:28 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

import jacob.common.AppLogger;
import jacob.common.ENUM;

import org.apache.commons.logging.Log;

/**
 * This is an event handler for a update button.
 * 
 * @author mike
 *
 */
public class CallButtonUpdateRecord extends IActionButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: CallButtonUpdateRecord.java,v 1.3 2005/09/15 20:01:43 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * This event handler will be called if the corresponding button has been pressed.
   * You can prevent the execution of the update action if you return [false].<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return 'false' if you want to avoid the execution of the action else return [true]
   */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
		IDataTableRecord currentRecord = context.getSelectedRecord();

		if(context.getGroup().getDataStatus()== IGuiElement.SELECTED)
		{
		  // The record will be toggle from IGuiElement.SELECTED => IGuiElement.UPDATE
		  // (return false to prevent this)
		}
		else 
		{
		  // The record will be toggle from IGuiElement.UPDATE => IGuiElement.SELECTED
		  // (return false to prevent this)
		}
		return true;
	}

  /**
   * This event method will be called if the update action has been successfully done.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
	  // the record has been successfull switch to the update mode or has been successfull saved
	  //
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
         if (status == IGuiElement.SELECTED)
             button.setEnable(!ENUM.CALLSTATUS_CLOSED.equals(context.getSelectedRecord().getSaveStringValue("status")));
	}
}
