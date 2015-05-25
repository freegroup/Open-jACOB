/*
 * Created on 06.12.2005
 *
 */
package de.tif.jacob.ruleengine.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.messaging.Message;
import de.tif.jacob.ruleengine.BusinessObject;
import de.tif.jacob.ruleengine.RuleEngine;


public class Email extends BusinessObject
{
  static private IEmailSender emailSender = null;
  
  // use the common logger for the rule engine!!! (Engine.class)
  static private final Log logger = LogFactory.getLog(RuleEngine.class);

  public void send(String to, String subject, String message) throws Exception
  {
    if (logger.isDebugEnabled())
      logger.debug("Send message to:" + to + "\n" + subject + "\n" + message);

    if(emailSender==null)
      Message.sendEMail(to,subject+"\n"+message);
    else
      emailSender.send(getContext(), to,subject,message);
  }
  
  public static void setEmailSender(IEmailSender sender)
  {
    emailSender = sender;
  }
}
