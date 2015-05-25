package jacob.common.task;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.MultiPartEmail;

import jacob.model.EmailProperties;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;

public abstract class AbstractEmailProtocol extends SendProtocol
{
  static public final transient String RCS_ID = "$Id: AbstractEmailProtocol.java,v 1.3 2009-10-29 20:36:41 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private String server;
  private String user;
  private String password;
  private String defOriginatorAddr;
  private String defOriginator;
  private int maxRetries;
  private int retryTimespan;
  
  protected AbstractEmailProtocol(String protocolName)
  {
    super(protocolName);
  }

  /* (non-Javadoc)
   * @see jacob.common.task.AbstractSendProtocol#initialize(de.tif.jacob.core.Context)
   */
  protected boolean initialize(Context context) throws Exception
  {
    IDataTable configTable = context.getDataTable(EmailProperties.NAME);
    configTable.setMaxRecords(1);
    configTable.qbeClear();
    configTable.qbeSetValue(EmailProperties.state, EmailProperties.state_ENUM._active);
    if (configTable.search() > 0)
    {
      IDataTableRecord configuration = configTable.getRecord(0);

      this.server = configuration.getStringValue(EmailProperties.server);
      this.user = configuration.getStringValue(EmailProperties.user);
      this.password = configuration.getStringValue(EmailProperties.password);
      this.defOriginatorAddr = configuration.getStringValue(EmailProperties.deforiginatoraddr);
      this.defOriginator = configuration.getStringValue(EmailProperties.deforiginatoraddr);
      this.maxRetries = configuration.getintValue(EmailProperties.maxretries);
      this.retryTimespan = configuration.getintValue(EmailProperties.retrytimespan);

      return true;
    }

    return false;
  }

  protected final int repeatMaxRetries()
  {
    return this.maxRetries;
  }

  protected final int repeatTimespanInMins()
  {
    return this.retryTimespan;
  }

  protected void setFrom(Email email, SendMessage message) throws Exception
  {
    // set sender info
    //
    if (message.getSenderAddr() != null)
    {
      if (message.getSender() != null)
        email.setFrom(message.getSenderAddr(), message.getSender());
      else
        email.setFrom(message.getSenderAddr());
    }
    else
    {
      if (this.defOriginator != null)
        email.setFrom(this.defOriginatorAddr, this.defOriginator);
      else
        email.setFrom(this.defOriginatorAddr);
    }
  }
  
  private static final String CC = "CC:";
  private static final String BCC = "BCC:";

  protected void setTo(Email email, SendMessage message) throws Exception
  {
    // set recipients
    //
    for (int i = 0; i < message.getRecipientCount(); i++)
    {
      String recipient = message.getRecipientAddr(i);
      if (recipient.endsWith(">"))
      {
        try
        {
          int index = recipient.indexOf('<');
          String emailAddr = recipient.substring(index + 1, recipient.length() - 1);
          String emailName = recipient.substring(0, index).trim();
          String emailNameUpper = emailName.toUpperCase();
          if (emailNameUpper.startsWith(CC))
            email.addCc(emailAddr, emailName.substring(CC.length()).trim());
          else if (emailNameUpper.startsWith(BCC))
            email.addBcc(emailAddr, emailName.substring(BCC.length()).trim());
          else
            email.addTo(emailAddr, emailName);
        }
        catch (Exception ex)
        {
          throw new Exception("Invalid recipient address: " + recipient);
        }
      }
      else
      {
        email.addTo(recipient);
      }
    }
  }

  protected void setAttachments(MultiPartEmail email, SendMessage message) throws Exception
  {
    for (int i = 0; i < message.getAttachmentCount(); i++)
    {
      SendAttachment attachment = message.getAttachment(i);
      email.attach(attachment, attachment.getFilename(), "Dummy attachment description");
    }
  }

  protected void setSmtp(Email email) throws Exception
  {
    // set server info
    //
    if (this.user != null)
      email.setAuthentication(this.user, this.password);
    email.setHostName(this.server);
    // email.setDebug(true);
  }
}
