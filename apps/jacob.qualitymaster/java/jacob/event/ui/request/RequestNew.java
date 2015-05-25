package jacob.event.ui.request;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Aug 26 15:31:42 CEST 2005
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
public class RequestNew extends IActionButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: RequestNew.java,v 1.2 2006/02/24 02:16:15 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

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
    IDataTable table = context.getDataTable("creater");
    IUser user = context.getUser();
    table.qbeClear();
    table.qbeSetValue("pkey", user.getKey());
    table.search();
    
    // Owner des Gewerk
    IDataTable categoryowner = context.getDataTable("categoryowner");
    if (categoryowner.recordCount()==1)
    {
        String ownerKey = categoryowner.getRecord(0).getStringValue("pkey");
        IDataTable owner = context.getDataTable("owner");
        owner.qbeClear();
        owner.qbeSetValue("pkey",ownerKey);
        owner.search();
    }
    
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
