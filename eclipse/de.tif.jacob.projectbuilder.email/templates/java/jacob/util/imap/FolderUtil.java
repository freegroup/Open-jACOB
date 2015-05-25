package jacob.util.imap;

import jacob.model.Configuration;
import jacob.model.Email;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.UIDFolder;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationEntry;

public class FolderUtil 
{
	/**
	 * Retrieve all available folders on the imap server
	 * 
	 * @param server
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Folder[] getFolders(String server, String user, String password) throws Exception
	{
		List result = new ArrayList();
		Store store = StoreFactory.createStore(server, user, password);
    try
    {
      Folder rf = store.getDefaultFolder();
      addFolder(result, rf, true);
    }
    finally
    {
      if(store!=null)
        store.close();
    }
		return (Folder[])result.toArray(new Folder[0]);
	}
	
	/**
	 * create the handsover folder if neccessary and return them
	 * 
	 * @param server
	 * @param user
	 * @param password
	 * @param folderName
	 * @return
	 * @throws Exception
	 */
	public static IMAPFolder ensureFolderAndOpen(String server, String user, String password, String folderName) throws Exception
	{
		IMAPStore store = StoreFactory.createStore(server, user, password);
    IMAPFolder rf = (IMAPFolder)store.getDefaultFolder();
    IMAPFolder folder = (IMAPFolder)rf.getFolder(folderName);
    if(!folder.exists())
    	folder.create(Folder.HOLDS_MESSAGES);
    folder.open(Folder.READ_WRITE);
		return folder;
	}
	

	private static void addFolder(List names, Folder folder, boolean recurse) throws Exception
  {
  	names.add(folder);
    if ((folder.getType() & Folder.HOLDS_FOLDERS) != 0)
    {
      if (recurse)
      {
        Folder[] f = folder.list();
        for (int i = 0; i < f.length; i++)
          addFolder(names, f[i], recurse);
      }
    }
  }

	public static String[] getUserFoldersFromDB(IClientContext context) throws Exception
	{
		// Die Navigation darf nicht den Rest beeinflussen
		//
		IDataAccessor acc = context.getDataAccessor().newAccessor(); 
		IDataTable folderTable = acc.getTable(jacob.model.Folder.NAME);
		if(folderTable.search()!=0)
		{
			List entries = new ArrayList();
			for(int i=0; i<folderTable.recordCount();i++)
			{
				IDataTableRecord folder = folderTable.getRecord(i);
//				String name     = folder.getStringValue(jacob.model.Folder.name);
				String fullname = folder.getStringValue(jacob.model.Folder.url);
				entries.add(fullname);
			}
			return (String[])entries.toArray(new String[0]);
		}
	
		return new String[]{};
	}
	
	public static void moveToFolder(IClientContext context, long imapId, String sourceFolderId, String targetFolderId)
	{
		try 
		{
			String imap_server    = (String)context.getUser().getProperty(Configuration.imap_server);
			String username  = (String)context.getUser().getProperty(Configuration.user);
			String password  = (String)context.getUser().getProperty(Configuration.password);

			IMAPFolder sourceFolder = FolderUtil.ensureFolderAndOpen(imap_server,username,password,sourceFolderId);
			IMAPFolder targetFolder = FolderUtil.ensureFolderAndOpen(imap_server,username,password,targetFolderId);
	    
			Message m = sourceFolder.getMessageByUID(imapId);
			if(m!=null)
			{
				// copy der nachricht im targetFolder anlegen
				sourceFolder.copyMessages(new Message[]{m},targetFolder);
				
				// jetzt wird die original Nachricht gelýscht.
				sourceFolder.setFlags(new Message[]{m}, new Flags(Flags.Flag.DELETED), true);
			}

			targetFolder.close(false);
	    sourceFolder.close(true);
	    
	    // Falls es auf der iMAP Seite geklappt hat,  wird der Datensatz auch noch in der jACOB-DB umgezogen.
	    //
			IDataAccessor acc = context.getDataAccessor().newAccessor(); 
			IDataTable emailTable = acc.getTable(Email.NAME);
			emailTable.qbeSetKeyValue(Email.msgid, imapId);
			if(emailTable.search()==1)
			{
				IDataTableRecord email = emailTable.getSelectedRecord();
				IDataTransaction trans=  acc.newTransaction();
				try 
				{
					email.setStringValue(trans, Email.folder, targetFolderId);
					trans.commit();
				} 
				catch (Exception e) 
				{
					ExceptionHandler.handleSmart(context,e);
				}
				finally
				{
					trans.close();
				}
			}
		} 
		catch (Exception e) 
		{
			ExceptionHandler.handle(context,e);
		}
		
	}
  
  
}
