package jacob.scheduler.system;


import jacob.common.AppLogger;
import jacob.common.SendFactory;
import jacob.model.Configuration;
import jacob.model.Document;
import jacob.model.Whitelist;

import java.io.InputStream;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;
import de.tif.jacob.util.StringUtil;

/**
 * Example scheduled job for scanning a directory and import a file into 
 * the database (create a call).
 * 
 * @author andherz
 *
 */
public class EmailImport extends SchedulerTaskSystem
{
  static public final transient String RCS_ID = "$Id: EmailImport.java,v 1.4 2010/03/01 09:34:14 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  // use this to log relvant information....not the System.out.println(...)    ;-)
  static protected final transient Log logger = AppLogger.getLogger();

  // Start the task every 1 minutes
  // for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
  final ScheduleIterator iterator = new MinutesIterator(5);

  /**
   * Returns the Iterator which defines the run interval of this job.<br>
   */
  public ScheduleIterator iterator()
  {
    return iterator;
  }

  /**
   * MAIN METHOD OF THE JOB.
   * 
   * Example code snippet which scans a well define directory and printout the
   * content of all text documents.
   * 
   * Use this template to parse the files via a StringTokenizer/Castor/XMLBean....
   * and generate your required jobs or DB entries.
   * 
   */
  public void run(TaskContextSystem context) throws Exception
  {
    String INBOX    = "INBOX";
    String POP_MAIL = "pop3";
    
    boolean debugOn = false;
    IDataTable configurationTable = context.getDataTable(Configuration.NAME);
    configurationTable.search();
    if(configurationTable.getSelectedRecord()==null)
    {
      logger.error("No valid configuration data found");
      return;
    }
    
    String pop3Host    = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.pop3_server);
    String pop3User    = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.pop3_user);
    String pop3Passwd  = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.pop3_password);
    String smtpHost    = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.smtp_server);
    String smtpUser    = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.smtp_user);
    String smtpFrom    = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.from_email);
    String smtpPasswd  = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.smtp_password);
    String errorMsg    = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.error_email_text);

    if(pop3Host==null || pop3User==null || pop3Passwd==null)
    {
      logger.error("No valid configuration data found");
      return;
    }
    
    // Get a Session object
    //
    Properties sysProperties = System.getProperties();
    Session session = Session.getDefaultInstance(sysProperties, null);
    session.setDebug(debugOn);

    // Connect to host
    //
    Store store = session.getStore(POP_MAIL);
    store.connect(pop3Host, -1, pop3User, pop3Passwd);
    
    // Open the default folder
    //
    Folder folder = store.getDefaultFolder();
    if (folder == null)
        throw new UserException("No default mail folder");

    folder = folder.getFolder(INBOX);
    if (folder == null)
        throw new UserException("Unable to get folder: " + folder);

    // Get message count
    //
    folder.open(Folder.READ_WRITE);
    int totalMessages = folder.getMessageCount();
    if (totalMessages == 0)
    {
        logger.info(folder + " is empty");
        folder.close(false);
        store.close();
        return;
    }
    
    // Get attributes & flags for all messages
    //
    Message[] messages = folder.getMessages();
    FetchProfile fp = new FetchProfile();
    fp.add(FetchProfile.Item.ENVELOPE);
    fp.add(FetchProfile.Item.FLAGS);
    fp.add("X-Mailer");
    folder.fetch(messages, fp);

    // Process each message
    //
    for (int i = 0; i < messages.length; i++)
    {
        try 
        {
          if (!messages[i].isSet(Flags.Flag.SEEN))    
          {        
            Message message = messages[i];
            Address[] froms = message.getFrom();
            for (int j = 0; j < froms.length; j++)
            {
              Address from = froms[j];
              // get the 'Internet' eMail address if possible
              if (from instanceof InternetAddress)
              {
                InternetAddress internetFrom = (InternetAddress) from;
                String email =  internetFrom.getAddress();
                // falls die eMail Addresse nicht bekannt ist, dann wird diese übersprungen
                // (email whitelist)
                //
                
                if(validateEmailAddress(context, email))
                {
                  String tag = StringUtil.toSaveString(message.getSubject()).toLowerCase();
                  createDocumentRecord(context,tag, email, message);
                }
                else
                {
                  SendFactory.send(smtpHost, smtpUser, smtpPasswd, smtpFrom, new String[]{internetFrom.getAddress()}, new String[]{}, "jacob.docfinder", errorMsg);
                }
              }
            }
            messages[i].setFlag(Flags.Flag.DELETED, true); 
          }
        } 
        catch (Exception e) 
        {
          ExceptionHandler.handle(e);
          //messages[i].setFlag(Flags.Flag.DELETED, true); delete the corrupt message or log them in a onother system 
        } 
    }
    
    folder.close(true);
    store.close();
  }
  

  private void createDocumentRecord(TaskContextSystem context, String tag ,String email, Part part) throws Exception
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
          createDocumentRecord(context, tag,email, bodypart);
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
      if (contentType != null)
      {
        DataDocumentValue docfile = null;
        if (contentType.indexOf("text/plain") != -1)
        {
          //ignore text files
        }
        else if (contentType.indexOf("text/html") != -1)
        {
          //ignore html files
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

        // if everything is ok -> we create a new document with the corresponding tag
        if (docfile != null)
        {
          IDataTransaction trans = context.getDataAccessor().newTransaction();
          try {
            IDataTableRecord documentRecord =context.getDataTable(Document.NAME).newRecord(trans);
            documentRecord.setValue(trans, Document.tag, tag);
            documentRecord.setValue(trans, Document.owner_email, email);
            documentRecord.setValue(trans, Document.file, docfile);
            trans.commit();
          } 
          catch (Exception e) 
          {
            // ignore
          }
          finally
          {
            trans.close();
          }
        }
      }
    }
    }
    catch(Throwable th)
    {
      ExceptionHandler.handle(th);
    }
  }      
  
  private static boolean validateEmailAddress(TaskContextSystem context, String address) throws Exception
  {
    // Die eMail ist nur gltig wenn diese in der white list vorhanden ist
    //
    address=address.toLowerCase();
    logger.debug("Testing eMail address:"+address);
    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    IDataTable whitelistTable = accessor.getTable(Whitelist.NAME);
    whitelistTable.qbeSetKeyValue(Whitelist.email,address);
    if(whitelistTable.search()==0)
      return false;
    
    // Syntax der eMail muss auch korrekt sein
    //
    if (address != null)
    {
      address = address.trim();
      if (address.length() > 5 && address.indexOf("@") != -1)
      {
        return true;
      }
    }
    return false;
  }
}
