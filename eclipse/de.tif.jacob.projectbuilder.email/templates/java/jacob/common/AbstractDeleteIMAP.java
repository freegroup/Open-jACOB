/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Feb 05 16:26:28 CET 2006
 */
package jacob.common;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.UIDFolder;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Attachment;
import jacob.model.Configuration;
import jacob.model.Email;
import jacob.util.imap.FolderUtil;
import jacob.util.imap.StoreFactory;

import org.apache.commons.logging.Log;

import com.sun.mail.imap.IMAPFolder;

/**
 * The event handler for a delete button.<br>
 * 
 * @author andherz
 *
 */
public class AbstractDeleteIMAP extends IActionButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: AbstractDeleteIMAP.java,v 1.1 2007/11/25 22:12:37 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This event handler will be called, if the corresponding button has been pressed.
	 * You can prevent the execution of the DELETE action if you return <code>false</code>.<br>
	 * 
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
		try 
		{
			context.getUser().setProperty("newRecordToSelect",new Integer(context.getDataBrowser().getSelectedRecordIndex()+1));
			long  imapId  = context.getSelectedRecord().getlongValue(Email.msgid);
			String folderId= context.getSelectedRecord().getSaveStringValue(Email.folder);
			
			String imap_server    = (String)context.getUser().getProperty(Configuration.imap_server);
			String username  = (String)context.getUser().getProperty(Configuration.user);
			String password  = (String)context.getUser().getProperty(Configuration.password);
			Store store = StoreFactory.createStore(imap_server,username,password);

			Folder folder = store.getDefaultFolder();
			folder = folder.getFolder(folderId);
	    if (!folder.exists()) 
	    {
	        System.out.println(folderId + "  does not exist");
	    }

	    if (!(folder instanceof UIDFolder)) 
	    {
	        System.out.println( "This Provider or this folder does not support UIDs");
	    }
	    UIDFolder ufolder=(UIDFolder)folder;
	    
	    folder.open(Folder.READ_WRITE);
			Message m = ufolder.getMessageByUID(imapId);
			if(m!=null)
			{
		    // Die zu lýschende eMail wird, wenn gewýnscht, in den "delete" Folder kopiert.
		    // Es wird aber nicht von "delete" nach "delete" kopiert. D.h. wenn man eine eMail
				// aus dem "delete" Folder lýscht wird diese ganz gelýscht.
				//
				String trashFolderName =  (String)context.getUser().getProperty(Configuration.folder_trash);
				if(trashFolderName!=null && trashFolderName.length()>0 && !trashFolderName.equals(folderId))
				{
					IMAPFolder imapfolder = FolderUtil.ensureFolderAndOpen(imap_server,username,password,trashFolderName);
					folder.copyMessages(new Message[]{m},imapfolder);
					imapfolder.close(false);
				}
				// jetzt wird die original Nachricht gelýscht.
				folder.setFlags(new Message[]{m}, new Flags(Flags.Flag.DELETED), true);
			}

	    folder.close(true);
	    store.close();
		} 
		catch (Exception e) 
		{
			ExceptionHandler.handle(context,e);
			return false;
		}
		return true;
	}

	/**
	 * This event method will be called, if the DELETE action has been successfully executed.<br>
	 *  
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
		try 
		{
			context.getDataTable(Attachment.NAME).clear();
			
			Integer newIndex = (Integer)context.getProperty("newRecordToSelect");
			int index = newIndex.intValue();
			index = Math.max(context.getDataBrowser().recordCount()-1,index);
			context.getDataBrowser().setSelectedRecordIndex(index);
			context.getDataBrowser().propagateSelections();
		} 
		catch (Exception e) 
		{
			// ignore
		}
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
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
	}
}
