/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.attachment;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * This event handler performs a search of all attachments of an call.
 * 
 * Has the user select a call before?:
 * 
 * YES -> - Get the pkey of the call. - Search all attachment for this call. -
 * Display the result set in the search browser.
 * 
 * NO -> - Prepare the AskDialog for the pkey and return. - In the next request
 * cycle the callback object with the user input will be called - Search all
 * attachments with the hands over pkey. - Display the result set in the search
 * browser.
 * 
 * @author Andreas Herz
 *  
 */
public  class AskAttachmentCallSearch extends IButtonEventHandler
{
	/**
	 * Callback class for the AskDialog of an call pkey.
	 */
	public static class CallPkeyAskCallback implements IAskDialogCallback
	{
		/* Do noting if the users cancel the AskDialog */
		public void onCancel(IClientContext context)
		{
		}
		/* Called if the user press [ok] in the AskDialog */
		public void onOk(IClientContext context, String pkey)
		{
			try
			{
				AskAttachmentCallSearch.searchAndDisplayResult(context, pkey);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.ClientContext,
	 *      de.tif.jacob.screen.Button) @author Andreas Herz
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTable callTable = context.getDataTable("call"); // any function related
 																										 // table
		// check if the user has select a call
		// 
		IDataTableRecord call = callTable.getSelectedRecord();
		if (call == null)
		{
			// Performe a user input of an call pkey. In the next request cycle the
			// hands over
			// callback object (allPkeyAskCallback) will be called with the user
			// input
			//
      IAskDialog dialog = context.createAskDialog( "Bitte Meldungsnummer eingeben",new CallPkeyAskCallback());
			dialog.show();
      
			// stop with processing this method!!
			return;
		}
		// retrieve the pkey of the selected call
		//
		String callKey = call.getStringValue("pkey");
		// search and display the result in the corresponding SearchBrowser
		//
		searchAndDisplayResult(context, callKey);
	}

	/*
	 *  (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)
	{
	}
  
	/**
	 * Helper Funktion.
	 * 
	 * This method search the attachments of the hands over call and display them
	 * in the corresponding browser.
	 * 
	 * @param context 
	 * @param callPkey
	 */
	protected static void searchAndDisplayResult(IClientContext context, String callPkey) throws Exception
	{
		IDataAccessor accessor = context.getDataAccessor(); // current data
																											 // connection
		IDataBrowser browser = context.getDataBrowser();    // the current browser
		IDataTable attachmentTable = context.getDataTable(); // the table in which
																												// the actions
																												// performes
		// delete the last search criteria
		//
		accessor.qbeClearAll();
		// set all search criteria
		//
		attachmentTable.qbeSetValue("callattachment", callPkey);
		// do the search itself
		//
		browser.search(IRelationSet.LOCAL_NAME);

    // display the result set
		//
		context.getGUIBrowser().setData(context, browser);
	}
}
