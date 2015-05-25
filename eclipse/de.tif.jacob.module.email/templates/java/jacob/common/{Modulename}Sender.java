package jacob.common;

import jacob.model.{Modulename};

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;

public class {Modulename}Sender 
{

	static public final transient String RCS_ID = "$Id: {Modulename}Sender.java,v 1.1 2008/12/18 11:30:58 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	public void send(Context context, String to, String subject, String msg) throws Exception
	{
	  IDataTable configurationTable = context.getDataAccessor().newAccessor().getTable({Modulename}.NAME);
	  configurationTable.search();
	  if(configurationTable.getSelectedRecord()==null)
	  {
	  	logger.error("No valid configuration data found");
	  	return;
	  }
	  
	  String smtpHost    = configurationTable.getSelectedRecord().getSaveStringValue({Modulename}.smtp_server);
	  String smtpUser    = configurationTable.getSelectedRecord().getSaveStringValue({Modulename}.smtp_user);
	  String smtpFrom    = configurationTable.getSelectedRecord().getSaveStringValue({Modulename}.smtp_from_address);
	  String smtpPasswd  = configurationTable.getSelectedRecord().getSaveStringValue({Modulename}.smtp_password);

  
	  send(smtpHost, smtpUser, smtpPasswd, smtpFrom, new String[]{to}, new String[]{},subject, msg);
	}

	private static void send(String server, String user, String password, String from, String[] to, String[] cc, String subject, String body) throws Exception
	{

		Properties props = System.getProperties();
		props.put("mail.smtp.host", server);
		props.put("mail.smtp.auth", "true");
    Session session = Session.getInstance(props, null);
    session.setDebug(true);
    // Get a Transport object
    Transport t = session.getTransport("smtp");
    try 
    {
	    // create a message
	    MimeMessage msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(from));
	    InternetAddress[] toAddress = new InternetAddress[to.length];
	    for(int i=0;i < to.length;i++)
	    	toAddress[i]=new InternetAddress(to[i]);
	    
	    InternetAddress[] ccAddress = new InternetAddress[cc.length];
	    for(int i=0;i < cc.length;i++)
	    	ccAddress[i]=new InternetAddress(cc[i]);

	    // create the Multipart and its parts to it
	    Multipart mp = new MimeMultipart();
	    msg.setRecipients(Message.RecipientType.TO, toAddress);
	    msg.setRecipients(Message.RecipientType.CC, ccAddress);
	    msg.setSubject(subject);
	    msg.setSentDate(new Date());

	    // create and fill the first message part
	    MimeBodyPart mbp1 = new MimeBodyPart();
	    mbp1.setText(body);
	    mp.addBodyPart(mbp1);

	    // add the Multipart to the message
	    msg.setContent(mp);
	   
	    // send the message
    	t.connect(server,user,password);
	    t.sendMessage(msg, msg.getAllRecipients() );
    } 
    catch (MessagingException mex) 
    {
	    mex.printStackTrace();
	    Exception ex = null;
	    if/*while*/ ((ex = mex.getNextException()) != null) 
	    {
	    	ex.printStackTrace();
	    }
    }
	}
}
