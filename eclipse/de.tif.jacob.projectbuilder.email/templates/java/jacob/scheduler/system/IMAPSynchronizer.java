package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.model.Attachment;
import jacob.model.Configuration;
import jacob.model.Email;
import jacob.model.Folder;
import jacob.util.imap.FolderUtil;
import jacob.util.imap.StoreFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.internet.InternetAddress;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * @author andherz
 *
 */
public class IMAPSynchronizer extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: IMAPSynchronizer.java,v 1.1 2007/11/25 22:12:37 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
	// use this to log relevant information....not the System.out.println(...)    ;-)
	//
	static protected final transient Log logger = AppLogger.getLogger();


	// Start the task every 1 minutes
	// for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
	//
	final ScheduleIterator iterator = new MinutesIterator(1);


	/**
	 * Returns the Iterator which defines the run interval of this job.<br>
	 * 
	 */
	public ScheduleIterator iterator()
	{
		return iterator;
	}

	/**
	 *         synchronizeUserAccounts(..)
	 *                |
	 *                V
	 *        synchronizeFolders(..)
	 *                |
	 *                V
	 *         synchronizeMails(..)
	 *         |                     |
	 *         V                     V
	 *  createEMailRecord(..)    synchronizeEMailRecord(...)
	 *         |
	 *         V
	 *   createBody(..)
	 *         |
	 *         | (optional)
	 *         V
	 *   handleMultipart()
	 */
	public void run(TaskContextSystem context) throws Exception
	{
		long start = System.currentTimeMillis();
		System.out.println("start synchronize...."+new Date().toString());
		try {
			synchronizeUserAccounts(context);
		} 
		finally
		{
			System.out.println("done in ["+(System.currentTimeMillis()-start)+"] ms");
		}
	}

	/**
	 * Iterator over all user accounts and try to syncronize the enclosing
	 * folders of the user account.
	 * 
	 * @param context
	 */
	private void synchronizeUserAccounts(TaskContextSystem context) throws Exception 
	{
//  	System.out.println("synchronizeUserAccounts");
		IDataTable users = context.getDataTable(Configuration.NAME);
		users.search();
		for(int i=0; i<users.recordCount();i++)
		{
			IDataTableRecord userConfiguration = users.getRecord(i);
			String mandator_id = userConfiguration.getStringValue(Configuration.mandator_id);
			String imap_server    = userConfiguration.getStringValue(Configuration.imap_server);
			String username  = userConfiguration.getStringValue(Configuration.user);
			String password  = userConfiguration.getStringValue(Configuration.password);
			if(imap_server!=null && username!=null && password!=null)
			{
				try 
				{
					synchronizeFolders(context, mandator_id,imap_server,username,password);
				} 
				catch (Throwable e) 
				{
					logger.warn("Unable to synchronize eMail account for ["+username+"] :"+e.getLocalizedMessage());
				}
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param mandator_id
	 * @param imap_server
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	private void synchronizeFolders(TaskContextSystem context, String mandator_id, String imap_server, String username, String password) throws Exception 
	{
//  	System.out.println("\tsynchronizeFolders:"+mandator_id);
		IDataAccessor accessor =  context.getDataAccessor().newAccessor();
		// Alle aktuellen Folder aus der Datenbank holen
		//
		IDataTable folderTable = accessor.getTable(Folder.NAME);
		folderTable.qbeSetKeyValue(Folder.mandator_id, mandator_id);
		folderTable.search();
		List folderNames = new ArrayList();
		for(int i=0; i<folderTable.recordCount();i++)
		{
			folderNames.add(folderTable.getRecord(i).getStringValue(Folder.name));
		}
		
		
		// Alle Folder aus dem IMAP-Server holen
		//
		javax.mail.Folder[] imapfolders = FolderUtil.getFolders(imap_server, username, password);
		
		
		// Fehlende IMAP Folder in der Datenbank anlegen.
		//
		IDataTransaction trans = accessor.newTransaction();
    try
    {
		for (int i = 0; i < imapfolders.length; i++) 
		{
			javax.mail.Folder folder = imapfolders[i];
			String imapName     = folder.getName();
			String imapFullName = folder.getFullName();
			if(imapName!=null && imapName.length()>0 && !folderNames.remove(imapName))
			{
				IDataTableRecord record = folderTable.newRecord(trans);
				record.setValue(trans, Folder.mandator_id,mandator_id);
				record.setValue(trans, Folder.name, imapName);
				record.setValue(trans, Folder.url, imapFullName);
			}
		}
		trans.commit();
    }
    finally
    {
		trans.close();
    }
		
		// Folder welche nicht mehr auf der imap Seite vorhanden sind, werden jetzt in der
		// Datenbank gel�scht - samt den zugeh�rigen Mails und attachment
		//
		trans = accessor.newTransaction();
    try
    {
		Iterator iter = folderNames.iterator();
		while (iter.hasNext()) 
		{
			String folderName = (String) iter.next();
			folderTable.qbeClear();
			folderTable.qbeSetKeyValue(Folder.name, folderName);
			folderTable.qbeSetKeyValue(Folder.mandator_id,mandator_id);
			folderTable.searchAndDelete(trans);
		}
		trans.commit();
    }
    finally
    {
		trans.close();
    }
		
		// Alle folder sind jetzt synchron mit dem IMAP Server. jetzt wird f�r
		// jeden Folder die fehlenden Mails geholt.
		//
		accessor.qbeClearAll();
		folderTable.qbeClear();
		folderTable.qbeSetKeyValue(Folder.mandator_id,mandator_id);
		folderTable.search();
		for(int i=0; i<folderTable.recordCount();i++)
		{
			String folderName = folderTable.getRecord(i).getSaveStringValue(Folder.name);
			try 
			{
				synchronizeMails(context, folderName, mandator_id, imap_server, username, password);
			} 
			catch (Exception e) 
			{
				logger.warn("Unable to synchronize folder ["+username+":"+folderName+"]");
			}
		}
	}
	

	/**
	 * 
	 * @param context
	 * @param folderRecord
	 * @param mandator_id
	 * @param imap_server
	 * @param username
	 * @param password
	 */
	private void synchronizeMails(TaskContextSystem context, String folderName, String mandator_id, String imap_server, String username, String password) throws Exception 
	{
//  	System.out.println("\t\tsynchronizeMails:"+folderName);
		IDataAccessor accessor = context.getDataAccessor().newAccessor();
		
		// Alle eMail vom IMAP Server f�r einen speziellen Folder holen
    //
		Store store = StoreFactory.createStore(imap_server, username, password);
    try
    {
		javax.mail.Folder folder =store.getFolder(folderName);
    try
    {
    if (!(folder instanceof UIDFolder)) 
    {
      logger.warn("This Provider ["+imap_server+"] or this folder ["+folderName+"] does not support UIDs");
      return;
    }

    UIDFolder ufolder = (UIDFolder)folder;
    folder.open(javax.mail.Folder.READ_ONLY);
    int totalMessages = folder.getMessageCount();

    
    // Leerer Folder auf der IMAP Seite => alle Nachrichten aus der Datenbank zu diesem User und Folder
    // k�nnen gel�scht werden.
    //
    if (totalMessages == 0) 
    {
    	logger.debug("\t\t\tDelete all in Database Folder:"+folderName);
      IDataTable emailTable = accessor.getTable(Email.NAME);
    	emailTable.qbeSetKeyValue(Email.mandator_id,mandator_id);
    	emailTable.qbeSetKeyValue(Email.folder,     folderName );
    	IDataTransaction trans = accessor.newTransaction();
    	try 
    	{
				// TableHook wird aufgerufen. Der Hoo l�scht dann auch alle zugeh�rigen 
				// Attachments einer eMail
				//
				emailTable.searchAndDelete(trans);
				trans.commit();
			}
    	finally
			{
      	trans.close();
			}
    	return;
    }
    
		// Attributes & Flags for ALL messages ..
		Message[] msgs = ufolder.getMessagesByUID(1, UIDFolder.LASTUID);

		// Use a suitable FetchProfile
		FetchProfile fp = new FetchProfile();
		fp.add(FetchProfile.Item.ENVELOPE);
		fp.add(FetchProfile.Item.FLAGS);
		fp.add("X-Mailer");
		folder.fetch(msgs, fp);

    IDataTable emailTable = accessor.getTable(Email.NAME);
    emailTable.qbeSetKeyValue(Email.mandator_id, mandator_id);
    emailTable.qbeSetKeyValue(Email.folder, folderName);
    emailTable.search();
    
    // Fehlende eMails in der Datenbank eintragen
    List emailUIDs = new ArrayList();
    for(int i=0;i<emailTable.recordCount();i++)
    {
    	emailUIDs.add(emailTable.getRecord(i).getLongValue(Email.msgid));
    }
    
    // Alle Meldungen von diesem Folder aus der Datenbank holen
		//
		for (int i = 0; i < msgs.length; i++) 
		{
		    Long imapUID = new Long(ufolder.getUID(msgs[i]));
		    // eMail ist nicht in der Datenbank vorhanden => einf�gen
		    //
		    if(!emailUIDs.remove(imapUID))
		    {
		    	try
		    	{
		    		createEMailRecord(context, mandator_id, folderName, imapUID, msgs[i]);
		    	}
		    	catch(Throwable th)
		    	{
		    		// ignore some span can't be importet correct
		    		logger.warn("Unable to import mail",th);
		    	}
		    }
		    // eMail ist in der Datenbnak vorhanden => Flags der Mail synchronizieren
		    //
		    else
		    {
		    	
		    }
		}
		
		// Alle gel�schten eMails auf dem IMAP in der Datenbank nachziehen
		//
		Iterator iter = emailUIDs.iterator();
		while (iter.hasNext()) 
		{
			Long element = (Long) iter.next();
    	removeEMailRecord(context, mandator_id, folderName, element);
		}
    }
    finally
    {
		folder.close(false);
//		if(store.isConnected())
//			store.close();
    }
    }
    finally
    {
		store.close();
	}
	}
	
	
	
	private void removeEMailRecord(TaskContextSystem context, String mandator_id, String folderName, Long imapUID) throws Exception 
	{
//  	System.out.println("\t\t\tdeleteEMailRecord :"+imapUID);
  	
		IDataAccessor accessor = context.getDataAccessor().newAccessor();
		IDataTransaction trans = accessor.newTransaction();
		try 
		{
			IDataTable emailTable = accessor.getTable(Email.NAME);
			emailTable.qbeSetKeyValue(Email.mandator_id,mandator_id);
			emailTable.qbeSetKeyValue(Email.folder,folderName);
			emailTable.qbeSetKeyValue(Email.msgid,imapUID);
			// Corresponding Attachments of the eMail will be deleted via TableHook on the 
			// Alias "email"
			//
			emailTable.searchAndDelete(trans);
			trans.commit();
		}
		finally
		{
			trans.close();
		}
	}

	/**
	 * 
	 * @param context
	 * @param mandator_id
	 * @param folder
	 * @param m
	 * @throws Exception
	 */	
  private void createEMailRecord(TaskContextSystem context, String mandator_id, String folder, Long imapUID, Message m) throws Exception 
  {
//  	System.out.println("\t\t\tcreateEMailRecord:"+imapUID);
    // Determine sender email address & name
    //
    InternetAddress internetFrom = null;
    Address[] froms = m.getFrom();
    if(froms!=null)
    {
      for (int j = 0; j < froms.length; j++)
      {
        Address from = froms[j];
  
        // get the 'Internet' eMail address if possible
        if (from instanceof InternetAddress)
        {
          internetFrom = (InternetAddress) from;
          break;
        }
      }
    }
    
    // skip this message
    if (internetFrom == null)
      return;

    String senderAddress = validateEmailAddress(internetFrom.getAddress());
    if(senderAddress==null)
    	senderAddress = "<spam>";
    
  	IDataAccessor accessor = context.getDataAccessor().newAccessor();
  	IDataTransaction trans = accessor.newTransaction();
  	try
    {
  	IDataTable emailTable = accessor.getTable(Email.NAME);
  	IDataTableRecord record = emailTable.newRecord(trans);

    record.setValue(trans,Email.mandator_id, mandator_id);
    record.setValue(trans,Email.folder, folder);
  	record.setValue(trans,Email.from, senderAddress);
    record.setValue(trans,Email.subject, m.getSubject());
    record.setValue(trans,Email.senddate, m.getSentDate());
    record.setValue(trans,Email.msgid, imapUID);
  	Flags.Flag[] sf = m.getFlags().getSystemFlags(); // get the system flags
  	for (int i = 0; i < sf.length; i++) 
  	{
  	    Flags.Flag f = sf[i];
  	    if (f == Flags.Flag.ANSWERED)
  	    	record.setIntValue(trans,Email.answered, 1);
  	    else if (f == Flags.Flag.DELETED)
  	    	record.setIntValue(trans,Email.deleted, 1);
  	    else if (f == Flags.Flag.DRAFT)
  	    	record.setIntValue(trans,Email.draft, 1);
  	    else if (f == Flags.Flag.FLAGGED)
  	    	record.setIntValue(trans,Email.flagged, 1);
  	    else if (f == Flags.Flag.RECENT)
  	    	record.setIntValue(trans,Email.recent, 1);
  	    else if (f == Flags.Flag.SEEN)
  	    	record.setIntValue(trans,Email.seen, 1);
  	}

    appendAttachmentsToEMail(mandator_id, record, m);
    
    trans.commit();
    }
    finally
    {
    	trans.close();
    }	
  }	
  

  

  private static void appendAttachmentsToEMail(String mandator_id, IDataTableRecord record, Part part) throws Exception
  {
  	try
  	{
    Object content = part.getContent();
    if (content instanceof Multipart)
    {
      Multipart multipart = (Multipart) content;
      try
      {
        for (int j = 0; j < multipart.getCount(); j++)
        {
          BodyPart bodypart = multipart.getBodyPart(j);
          appendAttachmentsToEMail(mandator_id, record, bodypart);
        }
      }
      catch (MessagingException ex)
      {
          logger.warn("Accessing multipart of incident  failed");
      }
    }
    else
    {
      String contentType = part.getContentType().toLowerCase();
//      System.out.println(contentType);
      if (contentType != null)
      {
        DataDocumentValue docfile = null;
        if (contentType.indexOf("text/plain") != -1)
        {
          if (content instanceof String)
          {
            if (record.hasNullValue(Email.body ))
            {
              // the first occurrence of plain text is treated as notes
              record.setValue(record.getCurrentTransaction(), Email.body, content);
              return;
            }

            // check maximum content length
            String textContent = (String) content;
            docfile = DataDocumentValue.create(part.getFileName(), textContent.getBytes());
          }
        }
        else if (contentType.indexOf("text/html") != -1)
        {
//        	System.out.println("HTML-eMail.");
          if (content instanceof String)
          {
//          	System.out.println("\tString...:"+part.getFileName());
            // check maximum content length
            String textContent = (String) content;
            docfile = DataDocumentValue.create("email_content.html", textContent.getBytes());
          }
          else
          {
//          	System.out.println("\tnothings...");
          }
        }
        else
        {
          if (content instanceof InputStream)
          {
            String filename = part.getFileName();
            if (filename != null)
            {
            	byte[] data = IOUtils.toByteArray((InputStream)content);
              docfile = DataDocumentValue.create(filename, data);
            }
          }
        }

        // if everything is ok -> we create a new attachment to our email
        if (docfile != null)
        {
          IDataTransaction trans = record.getCurrentTransaction();
          IDataTableRecord documentRecord = record.getAccessor().getTable(Attachment.NAME).newRecord(trans);
          documentRecord.setValue(trans, Attachment.email_key, record.getValue(Email.pkey));
          documentRecord.setValue(trans, Attachment.mandator_id, mandator_id);
          documentRecord.setValue(trans, Attachment.docfile, docfile);
        }
      }
    }
  	}
  	catch(Throwable th)
  	{
  		ExceptionHandler.handle(th);
  	}
  }      
  
  
  private static String validateEmailAddress(String address)
  {
    if (address != null)
    {
      address = address.trim();
      if (address.length() > 5 && address.indexOf("@") != -1)
      {
        return address.toLowerCase();
      }
    }
    return null;
  }
}



