package jacob.event.ui.incidentEntry;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Aug 31 19:55:48 CEST 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.security.IUser;

/**
 * This is an event handler for the  New-Button.
 * 
 * @author andherz
 *
 */
public class IncidentEntryButtonNewRecord extends IActionButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: IncidentEntryButtonNewRecord.java,v 1.3 2006/02/24 02:16:15 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

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
    IDataTable table = context.getDataTable("customer");
    IUser user = context.getUser();
    table.qbeClear();
    table.qbeSetValue("pkey", user.getKey());
    table.search();

    
    return true;
	}

  /**
   * This event method will be called if the NEW action has been successfully done.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
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