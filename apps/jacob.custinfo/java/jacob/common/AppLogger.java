/*
 * Created on Jul 16, 2004
 *
 */
package jacob.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * THE AppLogger.java is a gneerated file. If you want to change the methods, members...
 * see in AppLogger.template!!!!!
 *
 * DON'T EDIT the AppLogger.java file.
 *
 * @author Andreas Herz
 */
public class AppLogger
{
    /**
     * Provides a logger in relation to the application name and the version of the application
     *
     **/
  	public static Log getLogger()
  	{
  	  return LogFactory.getLog("jacob.custinfo.1.0");
  	}
}
