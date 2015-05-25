package jacob.event.ui.document_store;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Sun Feb 27 21:17:01 CET 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUploadDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * The Event handler for the Document_storeButtonUpload-Button.<br>
  * The onAction will be called, if the user clicks on this button.<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andherz
  *
  */
public class Document_storeButtonUpload extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Document_storeButtonUpload.java,v 1.1 2007/11/25 22:19:39 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  class MyCallback implements IUploadDialogCallback
	{
  	public void onOk(IClientContext context, String fileName, byte[] fileData)throws Exception 
		{
  		// ...This is the current record in the GUI group.
  		IDataTableRecord record = context.getSelectedRecord();
  		
  		// The record must be in the UPDATE or NEW mode. In this case the record has always 
  		// a valid transaction.
  		//
  		IDataTransaction trans =record.getCurrentTransaction();
  		
  		// add the document data to the record.
  		// (take a close look on the table 'document_store' in this application.
  		//
  		record.setDocumentValue(trans, "data", DataDocumentValue.create(fileName, fileData));
  		
  		// Don't commit the transaction. The commit will be done if the user press the
  		// [Save] Button in the group.
		}
  	
  	public void onCancel(IClientContext context) throws Exception
		{
  		// ignore
		}
	}
  
	/**
	 * Show a dialog for the upload of the document.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
 		IUploadDialog dialog = context.createUploadDialog(new  MyCallback());
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
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
	 * @throws Exception
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
	  button.setEnable(status==IGuiElement.NEW || status==IGuiElement.UPDATE);
	}
}

