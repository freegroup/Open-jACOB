/*
 * Created on May 5, 2004
 *
 */
package jacob.event.ui.attachment;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * This event handler performs a search of all attachments of a Task.
 * 
 * Has the user select a Task before?:
 * 
 * YES -> - Get the pkey of the Task. - Search all attachment for this Task. -
 * Display the result set in the search browser.
 * 
 * NO -> - Prepare the AskDialog for the pkey and return. - In the next request
 * cycle the callback object with the user input will be called - Search all
 * attachments with the hands over pkey. - Display the result set in the search
 * browser.
 * 
 * @author Andreas Herz, Achim Böken
 *  
 */
public  class AttachmentTaskSearch extends IButtonEventHandler
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
				AttachmentTaskSearch.searchAndDisplayResult(context, pkey);
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
		IDataTable taskTable = context.getDataTable("task"); // any function related
 																										 // table
		// check if the user has select a call
		// 
		IDataTableRecord task = taskTable.getSelectedRecord();
		if (task == null)
		{
			// Performe a user input of an call pkey. In the next request cycle the
			// hands over
			// callback object (allPkeyAskCallback) will be called with the user
			// input
			//
      IAskDialog dialog = context.createAskDialog( "Bitte Auftragsnummer eingeben",new CallPkeyAskCallback());
			dialog.show();
      
			// stop with processing this method!!
			return;
		}
		// retrieve the pkey of the selected call
		//
		String taskKey = task.getStringValue("taskno");
		// search and display the result in the corresponding SearchBrowser
		//
		searchAndDisplayResult(context, taskKey);
	}
	/**
	 * Enable disable the button when state of the group has been changed
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)
	{
	}
  
	/**
	 * Helper Funktion.
	 * 
	 * This method search the attachments of the hands over task and display them
	 * in the corresponding browser.
	 * 
	 * @param context
	 * @param callPkey
	 * @author Andreas Herz
	 */
	protected static void searchAndDisplayResult(IClientContext context, String taskPkey) throws Exception
	{
		IDataAccessor accessor = context.getDataAccessor(); // current data
																											 // connection
		IDataBrowser browser = context.getDataBrowser();    // the current browser
    IDataTable taskTable = context.getDataTable("task");																										// the actions
																												// performes
		// delete the last search criteria
		//
		accessor.qbeClearAll();
		// set all search criteria
		//
    taskTable.qbeSetKeyValue("taskno",taskPkey);
		
		// do the search itself
		// 
		browser.search("r_attachmenttask",Filldirection.BOTH);

    // display the result set
		//
		context.getGUIBrowser().setData(context, browser);
	}
}
