/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Oct 04 11:35:39 CEST 2006
 */
package jacob.event.ui.email;

import java.util.Properties;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Email;
import jacob.util.imap.FolderUtil;

import org.apache.commons.logging.Log;


/**
 * The event handler for the EmailRecordMoveToButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class EmailRecordMoveToButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: EmailRecordMoveToButton.java,v 1.1 2007/11/25 22:12:38 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IGridTableDialog dialog =context.createGridTableDialog(button, new IGridTableDialogCallback() {
		
			public void onSelect(IClientContext context, int index, Properties values)	throws Exception 
			{
				String sourceFolder = context.getSelectedRecord().getStringValue(Email.folder);
				String targetFolder = values.getProperty("Folder");
				long  imapId  = context.getSelectedRecord().getlongValue(Email.msgid);
				FolderUtil.moveToFolder(context, imapId, sourceFolder, targetFolder);
				context.getDataTable().clear();
				
				// Den SearchBrowser ohne den verschobenen Record aufbauen
				//
				context.getDataAccessor().qbeClearAll();
		    IDataBrowser browser = context.getDataBrowser("emailBrowser");
				IDataTable emailTable = context.getDataTable(Email.NAME);
				
				emailTable.qbeSetValue(Email.folder,sourceFolder);
				browser.search("emailRelationset",Filldirection.BOTH);
				
				context.getGUIBrowser().setData(context,browser);
			}
		
		});
		dialog.setHeader(new String[]{"Folder"});
		String[] folders= FolderUtil.getUserFoldersFromDB(context);
		String[][] data= new String[folders.length][1];
		for (int i = 0; i < folders.length; i++) 
		{
			data[i][0]=folders[i];
		}
		dialog.setData(data);
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
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
}

