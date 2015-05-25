package jacob.event.ui.bug;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Jul 20 09:34:05 CEST 2005
 *
 */
import java.util.Map;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;



 /**
  * The Event handler for the DefaultName-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andherz
  *
  */
public class DefaultName extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ActionTypeGeneric.java,v 1.6 2005/01/03 14:59:37 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.6 $";

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
		IDataTableRecord currentRecord = context.getSelectedRecord();
    FormLayout layout = new FormLayout("10dlu,400dlu,200dlu,10dlu", // 4 columns
    															     "20dlu,p,2dlu,100dlu,20dlu"); // 5 rows
  	CellConstraints c=new CellConstraints();
		IFormDialog dialog = context.createFormDialog("Title",layout,new IFormDialogCallback()
    {
      public void onSubmit(IClientContext arg0, String arg1, Map arg2) throws Exception
      {
        DataDocumentValue document = (DataDocumentValue) arg2.get("file1");
        System.out.println("Filename:"+document.getName());
      }
    });
		
		dialog.addLabel("Information:",c.xy(1,1));
		dialog.addFileUpload("file1",c.xy(2,1));
		dialog.addSubmitButton("Save","save");
		dialog.setDebug(true);
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

