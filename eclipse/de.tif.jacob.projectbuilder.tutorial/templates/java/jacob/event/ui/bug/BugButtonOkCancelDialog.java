package jacob.event.ui.bug;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Sun Feb 27 13:28:06 CET 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * A simple button event handler which opens an OkCancel-Dialog for a user
  * request.
  * 
  * The callback instance of type [MyCallback] will be called if the user
  * pressed a button of the dialog.
  * 
  * @author andherz
  *
  */
public class BugButtonOkCancelDialog extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: BugButtonOkCancelDialog.java,v 1.1 2007/11/25 22:19:37 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  // The callback for the dialog
  //
  static class MyCallback implements IOkCancelDialogCallback
	{
		public void onOk(IClientContext context) throws Exception 
		{
			context.createMessageDialog("You have pressed the [ok] button").show();
		}

		public void onCancel(IClientContext context) throws Exception 
		{
			context.createMessageDialog("You have pressed the [cancel] button").show();
		}
	}
  
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
 		IOkCancelDialog dialog = context.createOkCancelDialog("Please press [ok] or [cancel]",new MyCallback());
 		dialog.show();
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
	}
}

