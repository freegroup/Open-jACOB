package jacob.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The AppLogger.java is a generated file! 
 *
 * !!! DO NOT EDIT NOR DELETE THIS FILE !!!
 * 
 * If you want to change the methods, members...
 * see in AppLogger.template!
 */
public class AppLogger
{
	  public final static String VERSION = "0.1";
	  public final static String NAME    = "ruletest";
	  
    /**
     * Provides a logger in relation to the application name and the version of the application.
     **/
  	public static Log getLogger()
  	{
  	    return LogFactory.getLog("jacob.ruletest.0.1");
  	}
}
