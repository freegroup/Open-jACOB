package jacob.event.ui.bug;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Sun Feb 27 19:29:37 CET 2005
 *
 */
import java.util.Properties;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;



 /**
  * The Event handler for the BugButtonGridDialog-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andherz
  *
  */
public class BugButtonGridDialog extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: BugButtonGridDialog.java,v 1.1 2005/03/17 12:07:53 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  class MyCallback implements IGridTableDialogCallback
	{
		public void onSelect(IClientContext context, int index, Properties values) throws Exception 
		{
			IMessageDialog dialog = context.createMessageDialog("You have clicked on row ["+index+"]\\r\\n"+values.toString());
			dialog.show();
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
 		String[]   header = new String[]{"Column 0","Column 1","Column 2" };
 		String [][] data = new String[][]{
 				                              {"column 0,0","column 1,0","column 2,0"},
 				                              {"column 0,1","column 1,1","column 2,1"},
																			{"column 0,2","column 1,2","column 2,2"}
 																			};
 		IGridTableDialog dialog =context.createGridTableDialog(button,new MyCallback());
 		dialog.setData(data);
 		dialog.setHeader(header);
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
		// You can enable/disable the button in relation to your conditions.
	  //
	  //button.setEnable(true/false);
	}
}

