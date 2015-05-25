package jacob.scheduler.system;

import jacob.common.task.AbstractEmailProtocol;
import jacob.common.task.SendMessage;
import jacob.common.task.SendMessageTask;
import jacob.common.task.SendProtocol;

import org.apache.commons.mail.HtmlEmail;

import de.tif.jacob.messaging.Message;

/**
 * @author andherz
 *
 */
public class SendHTMLEMail extends SendMessageTask
{
	static public final transient String RCS_ID = "$Id: SendHTMLEMail.java,v 1.3 2009-07-10 17:09:05 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";
	
  /**
   * Note: Protocol task mast be public! 
   * 
   * @author Andreas Sonntag
   */
  public static class Protocol extends AbstractEmailProtocol
  {
    public Protocol()
    {
      super(Message.EMAIL_HTML_PROTOCOL_NAME);
    }

    protected void send(SendMessage message) throws Exception
    {
      HtmlEmail email = new HtmlEmail();
      
      setAttachments(email, message);
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
        email.setHtmlMsg(msg.substring(index + 1));
      }
      else
      {
        email.setHtmlMsg(msg);
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
