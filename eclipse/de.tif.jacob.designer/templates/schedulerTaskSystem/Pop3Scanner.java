package {package};


import jacob.common.AppLogger;
import jacob.config.Config;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.TaskContextUser;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * Example scheduled job for scanning a directory and import a file into 
 * the database (create a call).
 * 
 * @author {author}
 *
 */
public class {class} extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: Pop3Scanner.java,v 1.1 2007/05/18 16:13:49 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
	// use this to log relvant information....not the System.out.println(...)    ;-)
	static protected final transient Log logger = AppLogger.getLogger();

	// Start the task every 1 minutes
	// for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
	final ScheduleIterator iterator = new MinutesIterator(10);

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
	  
		Config conf = new Config();
	  String pop3Host = conf.getProperty("pop3.host");
	  String user     = conf.getProperty("pop3.user");
	  String password = conf.getProperty("pop3.password");

	  if(user==null || pop3Host==null || password==null)
	  {
	  	System.out.println("Please add required properties [pop3.host, pop3.user, pop3.password] to your config.properties file.");
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
    store.connect(pop3Host, -1, user, password);
    
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
					  createCall(context,messages[i]);
					  messages[i].setFlag(Flags.Flag.DELETED, true); 
					}
				} 
        catch (MessagingException e) 
				{
        	ExceptionHandler.handle(e);
				  //messages[i].setFlag(Flags.Flag.DELETED, true); delete the corrupt message or log them in a onother system 
				} 
    }
    
    folder.close(true);
    store.close();
	}
	
	/**
	 * Create a call with the hands over data.
	 * 
	 * ========================================================================
	 * ========================================================================
	 * Be in Mind:
	 *   This is only a example code. Fit the table names and attributes to your
	 *   data model an business logic!!! ...or delete this code ;-)
	 * 
	 * ========================================================================
	 * ========================================================================
	 * 
	 * @param context The current working context of the scheduled job
	 * @param message the data from the read eMail.
	 * @throws Exception
	 */
  public void createCall (TaskContextSystem context, Message message) throws Exception
  {
      Address from  = message.getFrom()[0];
      String  email = from.toString();

      // get the 'Internet' eMail address if possible
      if (from instanceof InternetAddress)
        email = ((InternetAddress) from).getAddress();

      IDataTable customer = context.getDataTable("customer");
      IDataTable call     = context.getDataTable("calls");

      // search the related customer to an eMail address
      //
      customer.clear();
      customer.qbeSetKeyValue("email", email);
      customer.search();
      String customerKey = null;
      if (customer.recordCount()==1)
        customerKey = customer.getSelectedRecord().getSaveStringValue("pkey");

      // create a new record
      IDataTransaction trans = call.startNewTransaction();
      try
      {
        IDataTableRecord callrec = call.newRecord(trans);

        callrec.setValue(trans,"customer_key",customerKey);                     // set the relation to the customer if any exists
        callrec.setValue(trans,"problem",message.getSubject());                 // subject -> problem
        callrec.setValue(trans,"problemtext",message.getContent().toString());  // body    -> problemtext
        trans.commit();                                                         // commit the record
      }
      finally
			{
        trans.close();                                                          // cleanup the resources
      }
  }
}
