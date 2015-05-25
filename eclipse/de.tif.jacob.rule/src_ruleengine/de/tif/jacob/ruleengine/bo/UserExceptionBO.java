/*
 * Created on 06.12.2005
 *
 */
package de.tif.jacob.ruleengine.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.messaging.Message;
import de.tif.jacob.ruleengine.BusinessObject;
import de.tif.jacob.ruleengine.RuleEngine;


public class UserExceptionBO extends BusinessObject
{
  // use the common logger for the rule engine!!! (Engine.class)
  static private final Log logger = LogFactory.getLog(RuleEngine.class);

  public void send(String message) throws Exception
  {
     throw new UserException(message);
  }
}
