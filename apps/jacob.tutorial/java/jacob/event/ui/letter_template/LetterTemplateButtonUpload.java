package jacob.event.ui.letter_template;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Mar 02 11:33:54 CET 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IUploadDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * The Event handler for the LetterTemplateButtonUpload-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andherz
  *
  */
public class LetterTemplateButtonUpload extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: LetterTemplateButtonUpload.java,v 1.2 2005/05/11 18:14:50 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  class MyCallback implements IUploadDialogCallback
	{
  	public void onOk(IClientContext context, String fileName, byte[] fileData)throws Exception 
		{
  		if(!fileName.toLowerCase().endsWith(".rtf") &&!fileName.toLowerCase().endsWith(".pdf"))
  		{
  			IMessageDialog dialog = context.createMessageDialog("Letter engine supports only 'rtf' and 'pdf' documents at the moment!");
  			dialog.show();
  			return;
  		}
  		
  		// ...This is the current record in the GUI group.
  		IDataTableRecord record = context.getSelectedRecord();
  		
  		// The record must be in the UPDATE or NEW mode. In this case the record has always 
  		// a valid transaction.
  		//
  		IDataTransaction trans =record.getCurrentTransaction();
  		
  		// add the document data to the record.
  		// (take a close look on the table 'document_store' in this application.
  		//
  		record.setDocumentValue(trans,"document",DataDocumentValue.create(fileName,fileData));
  		
  		// Don't commit the transaction. The commit will be done if the user press the
  		// [Save] Button in the group.
		}
  	
  	public void onCancel(IClientContext context) throws Exception
		{
  		// ignore
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
 		IUploadDialog dialog = context.createUploadDialog(new  MyCallback());
 		dialog.show();
  }

   
  /**
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

