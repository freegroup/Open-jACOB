/*
 * Created on 06.12.2005
 *
 */
package de.tif.jacob.ruleengine.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.ruleengine.BusinessObject;
import de.tif.jacob.ruleengine.RuleEngine;
import de.tif.jacob.screen.IClientContext;


public class UserInformation extends BusinessObject
{
  // use the common logger for the rule engine!!! (Engine.class)
  static private final Log logger = LogFactory.getLog(RuleEngine.class);

  public void send(String message) throws Exception
  {
    if(logger.isDebugEnabled())
      logger.debug("UserInformation:"+message);
    
    if(getContext() instanceof IClientContext)
      ((IClientContext)getContext()).createMessageDialog(message).show();
    else
      throw new UserException(message);
  }
}
