package jacob.util.imap;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPTransport;

import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.security.IUser;

public class SendFactory 
{
	public static Message send(String server, String user, String password, String from, String[] to, String[] cc, String subject, String body, DataDocumentValue[] attachments) throws Exception
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

	    if(attachments!=null)
	    {
		    for(int i=0; i<attachments.length;i++)
		    {
		      String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(attachments[i].getName());
		 	    DataSource fds = new ByteArrayDataSource(attachments[i].getContent(),mimeType);
		 	    MimeBodyPart mbp = new MimeBodyPart();
		 	    mbp.setDataHandler(new DataHandler(fds));
			    mbp.setFileName(attachments[i].getName());
		
			    mp.addBodyPart(mbp);
		    }
	    }

	    // add the Multipart to the message
	    msg.setContent(mp);
	   
	    // send the message
    	t.connect(server,user,password);
	    t.sendMessage(msg, msg.getAllRecipients() );
	    return msg;
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
    return null;
	}
}
