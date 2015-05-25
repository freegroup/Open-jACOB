package jacob.event.ui.customer;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Mar 02 13:04:18 CET 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataBrowser;
import de.tif.jacob.core.data.impl.DataTableRecord;
import de.tif.jacob.letter.LetterFactory;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * The Event handler for the CustomerButtonGenerateLetterAttachment-Button.<br>
  * The onAction will be called, if the user clicks on this button.<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andherz
  *
  */
public class CustomerButtonGenerateLetterAttachment extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CustomerButtonGenerateLetterAttachment.java,v 1.1 2007/11/25 22:19:37 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
		IDataTableRecord customerRecord = context.getSelectedRecord();
		DataTableRecord templateRecord = (DataTableRecord)context.getDataTable("letter_template").getSelectedRecord();
		if(templateRecord!=null)
		{
			// retrieve the rtf template
			//
			DataDocumentValue template = templateRecord.getDocumentValue("document");
			
			// Save the generated letter as attachment to the current customer
			//
			String lettername = templateRecord.getSaveStringValue("description");
			
			// get a letter generator for rtf documents
			//
			DataDocumentValue document = LetterFactory.transform(context, customerRecord, template, lettername);
			IDataTransaction trans = context.getDataAccessor().newTransaction();
			try 
			{
				// create a new record in the table 'letter'
				//
				IDataTableRecord letter = context.getDataTable("letter").newRecord(trans);

				// set the data in the field 'letter' (ok - bad name. table=letter and field=letter )
				//
				letter.setDocumentValue(trans, "letter", document);
				
				// and any description to the letter. It is also possible the prompt an input dialog
				// to the current agent and request a more meaningful description.( see context.createAskdialog(...) )
				//
				letter.setStringValue(trans,"description",lettername);
				
				// append the new record to the customer. Be in mind, that the 'letter' table must have a relation to the
				// 'customer' table!
				//
				letter.setLinkedRecord(trans,customerRecord);
				
				// commit the transaction
				//
				trans.commit();
				
				// Update the InformBrowser in the UI
				//
				DataBrowser dataBrowser = (DataBrowser)context.getDataBrowser("letterBrowser");
				dataBrowser.add(letter);
				((IBrowser)context.getGroup().findByName("letterBrowser")).setData(context,dataBrowser);
				
				// send a feedback to the user.
				// An exception will be automaticly display to the user if any error occoured.
				//
				IMessageDialog dialog = context.createMessageDialog("Letter has been succesfully created and assigned to the customer.");
				dialog.show();
			} 
			finally
			{
				trans.close();
			}
		}
		else
		{
			alert("Please select a 'LetterTemplate' first.");
		}
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
	}
}
