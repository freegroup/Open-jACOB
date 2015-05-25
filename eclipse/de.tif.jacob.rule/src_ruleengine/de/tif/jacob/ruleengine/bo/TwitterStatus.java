/*
 * Created on 06.12.2005
 *
 */
package de.tif.jacob.ruleengine.bo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import de.tif.jacob.ruleengine.BusinessObject;
import de.tif.jacob.ruleengine.RuleEngine;

public class TwitterStatus extends BusinessObject
{
  // use the common logger for the rule engine!!! (Engine.class)
  static private final Log logger = LogFactory.getLog(RuleEngine.class);

  public void send(String twitterId, String twitterPassword, String message) throws Exception
  {
    System.out.println("sending...user:["+twitterId+"]  password:["+twitterPassword+"]  ["+message+"]");
    if (logger.isDebugEnabled())
      logger.debug("Set twitter status:" + twitterId);
    
    Twitter twitter = new TwitterFactory().getInstance(twitterId, twitterPassword);
    twitter.updateStatus(message);
  }
}
