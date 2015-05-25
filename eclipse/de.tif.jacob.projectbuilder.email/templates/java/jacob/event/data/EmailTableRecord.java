/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Feb 05 16:14:32 CET 2006
 */
package jacob.event.data;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.UIDFolder;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import jacob.common.AppLogger;
import jacob.model.Attachment;
import jacob.model.Configuration;
import jacob.model.Email;
import jacob.util.imap.StoreFactory;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class EmailTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: EmailTableRecord.java,v 1.1 2007/11/25 22:12:37 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		System.out.println("\t\tDelete attachments:"+tableRecord);
		IDataAccessor accessor = tableRecord.getAccessor().newAccessor();
		IDataTable attachmentTable = accessor.getTable(Attachment.NAME);
		attachmentTable.qbeSetValue(Attachment.email_key, tableRecord.getValue(Email.pkey));
		attachmentTable.qbeSetValue(Attachment.mandator_id, tableRecord.getValue(Email.mandator_id));
		attachmentTable.searchAndDelete(transaction);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction trans) throws Exception
	{
		// Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
		//
		if(tableRecord.isDeleted())
		{
			return;
		}

	
		// enter your code here
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
		if(tableRecord.isNew())
			return;
		System.out.println("set seen-flag on the IMPA side...");
		try 
		{
			long  imapId  = tableRecord.getlongValue(Email.msgid);
			String folderId= tableRecord.getSaveStringValue(Email.folder);
			
			// Record auch der IMAP Seite lýschen.
			IDataTable configTable = tableRecord.getAccessor().getTable(Configuration.NAME);
			// nur wenn es im Context eines eingeloggen Benutzer gemacht wird, liefert die Suche immer
			// genau einen Record zurýck (Constraint am tableAlias)
			//
			if(configTable.search()==1)
			{
				IDataTableRecord userConfiguration = configTable.getSelectedRecord();
				String server    = userConfiguration.getStringValue(Configuration.imap_server);
				String username  = userConfiguration.getStringValue(Configuration.user);
				String password  = userConfiguration.getStringValue(Configuration.password);
				Store store = StoreFactory.createStore(server,username,password);

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
					folder.setFlags(new Message[]{m}, new Flags(Flags.Flag.SEEN), true);

		    folder.close(true);
		    store.close();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
