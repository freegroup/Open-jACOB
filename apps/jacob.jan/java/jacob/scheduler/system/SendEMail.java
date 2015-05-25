package jacob.scheduler.system;

import jacob.common.task.AbstractEmailProtocol;
import jacob.common.task.SendMessage;
import jacob.common.task.SendMessageTask;
import jacob.common.task.SendProtocol;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

import de.tif.jacob.messaging.Message;

/**
 * 
 * @author Andreas Sonntag
 */
public class SendEMail extends SendMessageTask
{
  static public final transient String RCS_ID = "$Id: SendEMail.java,v 1.4 2009-07-10 17:09:05 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  /**
   * Note: Protocol task mast be public! 
   * 
   * @author Andreas Sonntag
   */
  public static class Protocol extends AbstractEmailProtocol
  {
    public Protocol()
    {
      super(Message.EMAIL_PROTOCOL_NAME);
    }

    protected void send(SendMessage message) throws Exception
    {
      if (message.getAttachmentCount() == 0)
      {
        SimpleEmail email = new SimpleEmail();
        setAndSend(email, message);
      }
      else
      {
        MultiPartEmail email = new MultiPartEmail();
        setAttachments(email, message);
        setAndSend(email, message);
      }
    }

    private void setAndSend(Email email, SendMessage message) throws Exception
    {
      setTo(email, message);
      setFrom(email, message);
      
      // TODO: check hardcoding
      email.setCharset("ISO-8859-1");

      // set subject and message
      //
      String msg = message.getText();
      int index = msg.indexOf("\n");
      if (index >= 0 && (index + 1 < msg.length()))
      {
        email.setSubject(msg.substring(0, index).trim());
        email.setMsg(msg.substring(index + 1));
      }
      else
      {
        email.setMsg(msg);
      }

      setSmtp(email);

      // and really send the thing
      email.send();
    }
  }

  protected SendProtocol getProtocol()
  {
    return new Protocol();
  }
}
