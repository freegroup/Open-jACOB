/*
 * Created on 06.12.2005
 *
 */
package de.tif.jacob.ruleengine.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.messaging.Message;
import de.tif.jacob.ruleengine.BusinessObject;
import de.tif.jacob.ruleengine.RuleEngine;


public class Alert extends BusinessObject
{
  // use the common logger for the rule engine!!! (Engine.class)
  static private final Log logger = LogFactory.getLog(RuleEngine.class);

  public void send(String userLoginId, String message) throws Exception
  {
    if(logger.isDebugEnabled())
      logger.debug("Send message to:"+userLoginId);
    
    Message.sendAlert(userLoginId,message);
  }
}
