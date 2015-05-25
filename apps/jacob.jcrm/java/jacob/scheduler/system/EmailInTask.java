package jacob.scheduler.system;

import jacob.common.AppLogger;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;

import com.sun.mail.imap.protocol.FLAGS;
import com.sun.mail.util.BASE64DecoderStream;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * Task to access mails from multiple mail stores. For each (proper) received
 * mail an incident will be created.
 * 
 * @author andreas
 */
public class EmailInTask extends SchedulerTaskSystem
{
  static public final transient String RCS_ID = "$Id: EmailInTask.java,v 1.3 2006/08/31 14:52:04 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  // use this to log relvant information....not the System.out.println(...) ;-)
  static protected final transient Log logger = AppLogger.getLogger();

  // start the task every minute
  private final ScheduleIterator iterator = new MinutesIterator(1);

  /**
   * Returns the Iterator which defines the run interval of this job. <br>
   */
  public ScheduleIterator iterator()
  {
    return iterator;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTaskSystem#run(de.tif.jacob.scheduler.TaskContextSystem)
   */
  public void run(TaskContextSystem context) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor();
    accessor.qbeClearAll();
    IDataTable mailinstoreTable = accessor.getTable("mailinstore");
    mailinstoreTable.qbeSetKeyValue("status", "active");
    IDataTable mailinstatusTable = accessor.getTable("mailinstatus");
    mailinstatusTable.qbeSetValue("nextaccess", "<now");
    mailinstatusTable.search("r_mailin");
    for (int i = 0; i < mailinstatusTable.recordCount(); i++)
    {
      IDataTableRecord mailinstatusRecord = mailinstatusTable.getRecord(i);

      // fetch mails for this store
      fetchMails(mailinstatusRecord, Integer.MAX_VALUE);
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
  
  private static String validateSubject(String subject)
  {
    if (subject != null)
    {
      subject = subject.trim();
      if (subject.length() > 0)
      {
        return subject;
      }
    }
    return "no subject";
  }
  
  private static Date validateSendDate(Date sendDate)
  {
    if (sendDate != null)
    {
      if (sendDate.getTime()<=System.currentTimeMillis())
      {
        return sendDate;
      }
    }
    return new Date();
  }
  
  private static String[] validateSenderName(String fullname)
  {
    if (fullname != null)
    {
      fullname = fullname.trim();
      if (fullname.length()>0)
      {
        int index = fullname.lastIndexOf(" ");
        if (index == -1)
        {
          // treat fullname as lastname
          return new String[] {fullname, null};
        }
        String lastname = fullname.substring(index+1);
        lastname = lastname.trim();
        String firstname = fullname.substring(0, index);
        firstname = firstname.trim();
        return new String[] {lastname, firstname};
      }
    }
    return new String[] {"unknown", null};
  }
  
  private static Store getConnectedStore(IDataTableRecord mailinstoreRecord) throws Exception
  {
    // Get a session object
    //
    Properties sysProperties = System.getProperties();
    Session session = Session.getDefaultInstance(sysProperties, null);
    session.setDebug(false);

    // Connect to host
    //
    String protocol = mailinstoreRecord.getStringValue("protocol");
    Store store = session.getStore(protocol);
    String host = mailinstoreRecord.getStringValue("host");
    int port = mailinstoreRecord.getintValue("port");
    String user = mailinstoreRecord.getStringValue("user");
    String password = mailinstoreRecord.getStringValue("password");
    store.connect(host, port, user, password);
    
    return store;
  }
  
  private static Folder getFolder(IDataTableRecord mailinstoreRecord, Store store) throws Exception
  {
    // Open the default folder
    //
    Folder folder = store.getDefaultFolder();
    if (folder == null)
      throw new Exception("No default mail folder");

    String foldername = mailinstoreRecord.getStringValue("folder");
    folder = folder.getFolder(foldername);
    if (folder == null)
      throw new Exception("Unable to get folder: " + foldername);
    
    return folder;
  }
  
  /**
   * Tests the given mail store.
   * 
   * @param mailinstoreRecord
   * @return the number of mails on the store
   * @throws Exception if the test fails
   */
  public static int testStore(IDataTableRecord mailinstoreRecord) throws Exception
  {
    // Get store and connect to it
    //
    Store store = getConnectedStore(mailinstoreRecord);
    
    try
    {
      // Get folder
      //
      Folder folder = getFolder(mailinstoreRecord, store);

      // Get messages
      //
      folder.open(Folder.READ_WRITE);
      try
      {
        return folder.getMessageCount();
      }
      finally
      {
        folder.close(false);
      }
    }
    finally
    {
      store.close();
    }
  }
  
  public static boolean fetchMails(IDataTableRecord mailinstatusRecord, int maxMailsToFetch) throws Exception
  {
    IDataTableRecord mailinstoreRecord = mailinstatusRecord.getLinkedRecord("mailinstore");

    boolean delete_inaccessible = mailinstoreRecord.getintValue("delete_inaccessible") == 1;
    
    int fetchedmails = 0;
    String error = null;
    try
    {
      // Get store and connect to it
      //
      Store store = getConnectedStore(mailinstoreRecord);

      try
      {
        // Get folder
        //
        Folder folder = getFolder(mailinstoreRecord, store);

        // Get messages
        //
        folder.open(Folder.READ_WRITE);
        try
        {
          int totalMessages = folder.getMessageCount();
          if (totalMessages > 0)
          {
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
              Message message = messages[i];
              try
              {
                // Determine sender email address & name
                //
                InternetAddress internetFrom = null;
                Address[] froms = message.getFrom();
                // Kann anscheinend auch NULL zurückliefern. Vermutlich irgend so eine
                // Spam-Mail
                if (froms != null)
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
                if (internetFrom == null)
                {
                  if (delete_inaccessible)
                    message.setFlag(FLAGS.Flag.DELETED, true);
                  
                  // skip this message
                  continue;
                }

                String senderAddress = validateEmailAddress(internetFrom.getAddress());
                String[] names = validateSenderName(internetFrom.getPersonal());
                if (senderAddress == null || names == null)
                {
                  if (delete_inaccessible)
                    message.setFlag(FLAGS.Flag.DELETED, true);
                  
                  // skip as well
                  continue;
                }

                String subject = validateSubject(message.getSubject());
                Date sendDate = validateSendDate(message.getSentDate());

                // Create new transaction to create a new incident
                //
                IDataAccessor accessor = mailinstatusRecord.getAccessor();
                IDataTransaction trans = accessor.newTransaction();
                try
                {
                  // Try to identify the contact by email address
                  //
                  IDataTable contactTable = accessor.getTable("contact");
                  contactTable.qbeClear();
                  contactTable.qbeSetKeyValue("email", senderAddress);
                  contactTable.search();
                  IDataTableRecord contactRecord;
                  if (contactTable.recordCount() == 0)
                  {
                    // Identification failed -> created new contact
                    contactRecord = contactTable.newRecord(trans);
                    contactRecord.setStringValueWithTruncation(trans, "lastname", names[0]);
                    contactRecord.setStringValueWithTruncation(trans, "firstname", names[1]);
                    contactRecord.setValue(trans, "email", senderAddress);
                  }
                  else
                  {
                    contactRecord = contactTable.getRecord(0);
                  }

                  // Create the new incident
                  //
                  IDataTableRecord incident = accessor.getTable("incident").newRecord(trans);
                  incident.setStringValueWithTruncation(trans, "description", subject);
                  incident.setValue(trans, "datecreated", sendDate);
                  incident.setValue(trans, "agent_key", mailinstoreRecord.getValue("agent_key"));
                  incident.setValue(trans, "workgroup_key", mailinstoreRecord.getValue("workgroup_key"));
                  incident.setValue(trans, "contact_key", contactRecord.getValue("pkey"));

                  // Append attachments as documents to the incident
                  //
                  int[] nbrs = new int[1];
                  boolean htmlattach = "attach".equals(mailinstoreRecord.getValue("htmlcontentmode"));
                  byte[] buffer = new byte[1024 * mailinstoreRecord.getintValue("maxattachmentsize")];
                  appendAttachmentsToIncident(incident, message, nbrs, htmlattach, buffer);

                  // And last but not least commit the stuff
                  //
                  trans.commit();
                  fetchedmails++;

                  // mark mail to be deleted on the mail server
                  message.setFlag(FLAGS.Flag.DELETED, true);

                  // leave loop if maximum number of mails fetched
                  if (maxMailsToFetch == fetchedmails)
                    break;
                }
                finally
                {
                  trans.close();
                }
              }
              catch (MessagingException ex)
              {
                // there seems to something wrong with this message -> skip it
                // and try next
                logger.warn("Retrieving mail failed: " + ex.toString());
                error = ex.toString();
                
                if (delete_inaccessible)
                  message.setFlag(FLAGS.Flag.DELETED, true);
              }
            }
          }
        }
        finally
        {
          folder.close(true);
        }
      }
      finally
      {
        store.close();
      }
    }
    catch (Exception ex)
    {
      logger.error("Fetching mails failed", ex);
      error = ex.toString();
    }

    // update status about mail reader access
    //
    IDataTransaction trans = mailinstatusRecord.getAccessor().newTransaction();
    try
    {
      mailinstatusRecord.setIntValue(trans, "fetchedmails", fetchedmails);
      mailinstatusRecord.setStringValueWithTruncation(trans, "error", error);
      mailinstatusRecord.setValue(trans, "lastaccess", "now");
      mailinstatusRecord.setValue(trans, "nextaccess", "now+" + mailinstoreRecord.getintValue("interval") + "min");
      trans.commit();
    }
    finally
    {
      trans.close();
    }

    return error == null;
  }

  /**
   * Appends attachments as documents to the given incident record. The first
   * plain text part is set as notes value to the incident.
   * 
   * @param incident
   *          the incident record
   * @param part
   *          the message part to look for attachments
   * @param nbrs
   *          attachment number counter, i.e. <code>int[1]</code>
   * @param htmlattach
   *          if <code>true</code> html attachments are attached, otherwise
   *          they are skipped
   * @param buffer
   *          the attachment buffer. The size of the buffer determines the
   *          maximum attachment size, i.e. if 0 no attachments are added at all
   * @throws Exception
   *           on any problem
   */
  private static void appendAttachmentsToIncident(IDataTableRecord incident, Part part, int[] nbrs, boolean htmlattach, byte[] buffer) throws Exception
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
          appendAttachmentsToIncident(incident, bodypart, nbrs, htmlattach, buffer);
        }
      }
      catch (MessagingException ex)
      {
        // hack: for some message multipart.getCount() throws an exception
        if (logger.isWarnEnabled())
          logger.warn("Accessing multipart of incident " + incident.getValue("pkey") + " failed");
      }
    }
    else
    {
      String contentType = part.getContentType();
      if (contentType != null)
      {
        // for checking received mime type, which is case insensitive
        contentType = contentType.toLowerCase();
        
        DataDocumentValue docfile = null;
        if (contentType.indexOf("text/plain") != -1)
        {
          if (content instanceof String)
          {
            if (incident.hasNullValue("notes"))
            {
              // the first occurrence of plain text is treated as notes
              incident.setValue(incident.getCurrentTransaction(), "notes", content);
              return;
            }

            // check maximum content length
            String textContent = (String) content;
            if (buffer.length < textContent.length() || textContent.length() == 0)
              return;

            docfile = DataDocumentValue.create("content" + (++nbrs[0]) + ".txt", textContent.getBytes());
          }
        }
        else if (contentType.indexOf("text/html") != -1)
        {
          // no logging message in case html is skipped
          if (!htmlattach)
            return;
          
          if (content instanceof String)
          {
            // check maximum content length
            String textContent = (String) content;
            if (buffer.length < textContent.length() || textContent.length() == 0)
              return;

            docfile = DataDocumentValue.create("content" + (++nbrs[0]) + ".html", textContent.getBytes());
          }
        }
        else
        {
          // check whether attachments should be skipped at all
          if (buffer.length == 0)
            return;
          
          if (content instanceof BASE64DecoderStream)
          {
            String filename = part.getFileName();
            if (filename != null)
            {
              BASE64DecoderStream base64 = (BASE64DecoderStream) content;
              int size = base64.read(buffer);
              if (size > 0 )
              {
                // check maximum content length
                if (size >= buffer.length)
                  return;
                
                docfile = DataDocumentValue.create(filename, buffer, size);
              }
            }
          }
        }

        // if everything is ok -> we create a new attachment to our incident
        if (docfile != null)
        {
          IDataTransaction trans = incident.getCurrentTransaction();
          IDataTableRecord documentRecord = incident.getAccessor().getTable("document").newRecord(trans);
          documentRecord.setValue(trans, "incident_key", incident.getValue("pkey"));
          documentRecord.setValue(trans, "creator", "system");
          documentRecord.setValue(trans, "docfile", docfile);
        }
        else
        {
          String contentClass = content == null ? "null" : content.getClass().toString();

          if (logger.isWarnEnabled())
            logger.warn("Attachment of incident " + incident.getValue("pkey") + " skipped: contentType:" + contentType + "; contentClass:" + contentClass
                + "; size:" + part.getSize() + "; filename:" + part.getFileName());
        }
      }
    }
  }
}
