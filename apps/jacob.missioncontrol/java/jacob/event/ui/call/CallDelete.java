package jacob.event.ui.call;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Sep 15 13:59:35 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

/**
 * This is an event handler for a update button.
 * 
 * @author andreas
 *
 */
public class CallDelete extends IActionButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: CallDelete.java,v 1.1 2005/09/15 20:01:43 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.out.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();


  /**
   * This event handler will be called if the corresponding button has been pressed.
   * You can prevent the execution of the DELETE action if you return [false].<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return 'false' if you want to avoid the execution of the action else return [true]
   */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
		IDataTableRecord currentRecord = context.getSelectedRecord();
		
		return true;
	}

  /**
   * This event method will be called if the delete action has been successfully done.<br>
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
	public void onGroupStatusChanged(IClientContext context, GroupState status,	IGuiElement emitter) throws Exception 
	{
	  IDataTableRecord callRecord = context.getSelectedRecord();
	  emitter.setEnable(callRecord != null && callRecord.getintValue("draft")==1);
	}
}
