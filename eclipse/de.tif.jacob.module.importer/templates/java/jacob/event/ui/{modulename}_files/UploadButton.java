/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon May 04 17:41:45 CEST 2009
 */
package jacob.event.ui.{modulename}_files;

import jacob.model.{Modulename}_files;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUploadDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the UploadButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class UploadButton extends IButtonEventHandler 
{
	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onClick(IClientContext context, IGuiElement emitter) throws Exception
	{
	    IUploadDialog dialog = context.createUploadDialog(new IUploadDialogCallback()
	    {
	      public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
	      {
	        IDataTableRecord record = {Modulename}_files.newRecord(context);
	        record.setDocumentValue(record.getCurrentTransaction(), {Modulename}_files.excel, DataDocumentValue.create(fileName, fileData));
	        record.setValue(record.getCurrentTransaction(), {Modulename}_files.state, {Modulename}_files.state_ENUM._uploaded);
	        ButtonController.updateButtons(context);
	      }
	    
	      public void onCancel(IClientContext context) throws Exception
	      {
	      }
	    
	    });
	    dialog.show();
	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
	{
		ButtonController.updateButtons(context);
	}
}
