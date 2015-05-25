package jacob.scheduler.system;

import jacob.model.Email;
import jacob.model.Storage_email;
import jacob.model.Storage_email_attachment;
import jacob.model.Storage_email_outbound;

import java.io.StringBufferInputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.SecondsIterator;

/**
 * @author andherz
 *
 */
public class EmailOutboundScheduler extends SchedulerTaskSystem
{
  static public final transient String RCS_ID = "$Id: EmailOutboundScheduler.java,v 1.4 2009/11/23 11:33:47 R.Spoor Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  final static int SCHEDULE_SECOND_INTERVALL = 10;
  final static int MAX_EMAIL_PER_RUN = 10;

  // Start the task every 1 minutes
  // for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
  //
  final ScheduleIterator iterator = new SecondsIterator(SCHEDULE_SECOND_INTERVALL);

  /**
   * Returns the Iterator which defines the run interval of this job.<br>
   *
   */
  public ScheduleIterator iterator()
  {
    return iterator;
  }

  /**
   * The run method of the job.<br>
   * The object
   * <code>context>/code> defines your current context in the jACOB application
   * server.<br>
	 * You can use it to access the database or other relevatn application data.<br>
   */
  public void run(TaskContextSystem context) throws Exception
  {
    IDataTable configurationTable = context.getDataAccessor().newAccessor().getTable(Email.NAME);
    configurationTable.search();
    if (configurationTable.getSelectedRecord() == null)
      return;

    String smtpHost = configurationTable.getSelectedRecord().getSaveStringValue(Email.smtp_server);
    String smtpUser = configurationTable.getSelectedRecord().getSaveStringValue(Email.smtp_user);
    String smtpPasswd = configurationTable.getSelectedRecord().getSaveStringValue(Email.smtp_password);
    boolean smtpDebug = configurationTable.getSelectedRecord().getbooleanValue(Email.smtp_debug);

    Properties props = System.getProperties();
    props.put("mail.smtp.host", smtpHost);
    props.put("mail.smtp.auth", "true");
    Session session = Session.getInstance(props, null);
    session.setDebug(smtpDebug);
    try
    {
      IDataTable storage_emailTable = context.getDataTable(Storage_email_outbound.NAME);
      storage_emailTable.qbeSetKeyValue(Storage_email.direction, Storage_email.direction_ENUM._out);
      storage_emailTable.qbeSetKeyValue(Storage_email.status, Storage_email.status_ENUM._Queued);
      storage_emailTable.setMaxRecords(MAX_EMAIL_PER_RUN);
      storage_emailTable.search(IRelationSet.LOCAL_NAME);

      for (int i = 0; i < storage_emailTable.recordCount(); i++)
      {
        IDataTransaction trans = context.getDataAccessor().newTransaction();
        try
        {
          IDataTableRecord storage_emailRecord = storage_emailTable.getRecord(i);
          try
          {
            IDataTableRecord parentEmailRecord = storage_emailRecord.getLinkedRecord(Storage_email.NAME);

            String from = storage_emailRecord.getSaveStringValue(Storage_email_outbound.from);
            String subject = storage_emailRecord.getSaveStringValue(Storage_email_outbound.subject);
            String[] to = storage_emailRecord.getSaveStringValue(Storage_email_outbound.to).split("[,]");
            Message message;
            if (parentEmailRecord != null)
            {
              // REPLY
              String orginalcontent = parentEmailRecord.getSaveStringValue(Storage_email.original_mime_message);
              message = new MimeMessage(session, new StringBufferInputStream(orginalcontent));
              message = message.reply(true);
            }
            else
            {
              // SEND
              message = new MimeMessage(session);
            }
            Multipart multipart = new MimeMultipart();

            // create the message part / the body itself
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(storage_emailRecord.getSaveStringValue(Storage_email_outbound.text_body).toString(),
            "text/html")));
            multipart.addBodyPart(messageBodyPart);

            // retrieve all attachments from the database
            //
            IDataTable storageEmailAttachmentTabel = context.getDataAccessor().newAccessor().getTable(Storage_email_attachment.NAME);
            storageEmailAttachmentTabel.qbeSetKeyValue(Storage_email_attachment.storage_email_key, storage_emailRecord.getValue(Storage_email_outbound.pkey));
            storageEmailAttachmentTabel.search(IRelationSet.LOCAL_NAME);
            for(int a=0; a<storageEmailAttachmentTabel.recordCount();a++)
            {
              IDataTableRecord attachment = storageEmailAttachmentTabel.getRecord(a);
              addAttachments(context, multipart, attachment.getDocumentValue(Storage_email_attachment.document));
            }

            // Put parts in message
            message.setContent(multipart);
            message.setSubject(subject);
            message.setFrom(new InternetAddress(from));

            Address[] toAddress = new InternetAddress[to.length];
            for (int ii = 0; ii < to.length; ii++)
              toAddress[ii] = new InternetAddress(to[ii]);
            message.setRecipients(RecipientType.TO, toAddress);

           // message.setRecipient(RecipientType.TO, new InternetAddress("a.boeken@tarragon-software.com"));

            // Get a Transport object
            Transport t = session.getTransport("smtp");
            message.setSentDate(new Date());

            // send the message
            t.connect(smtpHost, smtpUser, smtpPasswd);
            t.sendMessage(message, message.getAllRecipients());

            storage_emailRecord.setValue(trans, Storage_email_outbound.status, Storage_email_outbound.status_ENUM._Sent);
            storage_emailRecord.setValue(trans, Storage_email_outbound.error_message, null);
          }
          catch (MessagingException mex)
          {
            storage_emailRecord.setValue(trans, Storage_email_outbound.status, Storage_email_outbound.status_ENUM._Sent);
            storage_emailRecord.setValue(trans, Storage_email_outbound.error_message, mex.getMessage());
            mex.printStackTrace();
            Exception ex = null;
            if/* while */((ex = mex.getNextException()) != null)
            {
              ex.printStackTrace();
            }

          }
          catch (Exception mex)
          {
            storage_emailRecord.setValue(trans, Storage_email_outbound.status, Storage_email_outbound.status_ENUM._Sent);
            storage_emailRecord.setValue(trans, Storage_email_outbound.error_message, mex.getMessage());
          }
          trans.commit();
        }
        catch (Exception exc)
        {
          ExceptionHandler.handle(exc);
        }
        finally
        {
          trans.close();
        }
      }
    }
    catch (Exception exc)
    {
      ExceptionHandler.handle(exc);
    }
  }

  private void addAttachments(Context context, Multipart messagePart, DataDocumentValue attachment) throws Exception
  {
    String filename = attachment.getName();
    // Part two is attachment
    MimeBodyPart messageBodyPart = new MimeBodyPart();
    DataSource source = new ByteArrayDataSource(attachment.getContent(), de.tif.jacob.messaging.Message.getMimeType(filename));
    messageBodyPart.setDataHandler(new DataHandler(source));
    messageBodyPart.setFileName(filename);
    messagePart.addBodyPart(messageBodyPart);
  }
}
