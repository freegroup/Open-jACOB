package jacob.scheduler.system;


import jacob.common.AppLogger;
import jacob.common.EmailSender;
import jacob.model.Email_gateway;
import jacob.model.Employee;
import jacob.model.Incident;

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

/**
 * Example scheduled job for scanning a directory and import a file into 
 * the database (create a call).
 * 
 * @author andherz
 *
 */
public class EmailGateway extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: EmailGateway.java,v 1.2 2010-03-24 13:22:50 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";
	
	// use this to log relevant information....not the System.out.println(...)    ;-)
	static protected final transient Log logger = AppLogger.getLogger();

	// Start the task every 1 minutes
	// for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
	final ScheduleIterator iterator = new MinutesIterator(2);

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
	  IDataAccessor acc = context.getDataAccessor().newAccessor();
	  IDataTable confTable = acc.getTable(Email_gateway.NAME);
	  confTable.search();
	  for(int i=0; i< confTable.recordCount();i++)
	  {
	    IDataTableRecord confRecord = confTable.getRecord(i);
	    try
	    {
	      progressConfiguration(context, confRecord); 
	    }
	    catch(Exception e)
	    {
	      ExceptionHandler.handle(e);
	    }
	  }
	}
	
	public void progressConfiguration(TaskContextSystem context, IDataTableRecord configuration) throws Exception
	{
	  String INBOX    = "INBOX";
	  String POP_MAIL = "pop3";
	  
	  boolean debugOn = false;
	  
	  String pop3Host = configuration.getSaveStringValue(Email_gateway.pop3_server);
    String smtpHost = configuration.getSaveStringValue(Email_gateway.smtp_server);
	  String user     = configuration.getSaveStringValue(Email_gateway.pop3_user);
	  String password = configuration.getSaveStringValue(Email_gateway.pop3_password);
    String categoryKey = configuration.getSaveStringValue(Email_gateway.category_key);
    
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
			      Address from  = messages[i].getFrom()[0];
			      String  email = from.toString();

			      // get the 'Internet' eMail address if possible
			      if (from instanceof InternetAddress)
			        email = ((InternetAddress) from).getAddress();

			      createIncident(context,messages[i], email, categoryKey);
					  messages[i].setFlag(Flags.Flag.DELETED, true); 

		        EmailSender.send(smtpHost, user, password, user, new String[]{email}, new String[]{}, "Qualitymaster Notification","Incident has been created.\n\n===============================\n"+messages[i].getContent().toString());
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
  public void createIncident(TaskContextSystem context, Message message, String email, String categoryKey) throws Exception
  {
      IDataTable userTable = context.getDataTable(Employee.NAME);
      IDataTable incidentTable  = context.getDataTable(Incident.NAME);

      // search the related customer to an eMail address
      //
      userTable.clear();
      userTable.qbeSetKeyValue(Employee.email, email);
      userTable.search();
      if (userTable.recordCount()!=1)
        throw new UserException("Unknown user with sender email: ["+email+"]");

      String customerKey = userTable.getSelectedRecord().getSaveStringValue("pkey");

      // create a new record
      IDataTransaction trans = incidentTable.startNewTransaction();
      try
      {
        IDataTableRecord callrec = incidentTable.newRecord(trans);

        callrec.setValue(trans,Incident.creator_key,customerKey);                     // set the relation to the customer if any exists
        callrec.setValue(trans,Incident.subject,message.getSubject());                 // subject -> problem
        callrec.setValue(trans,Incident.description,message.getContent().toString());  // body    -> problemtext
        callrec.setValue(trans,Incident.category_key, categoryKey);
        callrec.setValue(trans,Incident.customer_key, customerKey);
        trans.commit();     
      }
      finally
			{
        trans.close();                                                          // cleanup the resources
      }
  }
}
