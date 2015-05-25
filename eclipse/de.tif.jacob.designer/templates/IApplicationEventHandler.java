/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on {date}
 */
package {package};

import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IApplicationEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.definition.IApplicationDefinition;

/**
 *
 * @author {author}
 */
public class {class} extends IApplicationEventHandler
{
	static public final transient String RCS_ID = "$Id: IApplicationEventHandler.java,v 1.2 2010/08/15 08:19:09 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

  /**
   * This hook method will be called, if the application is started up (i.e.
   * during server run up) or the state of the application is changed from
   * 'inactive' to 'productive'.
   * <p>
   * You can startup some additional worker threads. Be in mind - you have NO
   * access to your application database at this point.
   * <p>
   * 
   * If you throw an exception, the application will not be activated and hence
   * does not appear in the login screen. The application will be marked as
   * undeployed in the administration application.
   * <br>
   * <br>
   * <b>Be in mind - you didn't have access to your application database at this point.</b>
   * @since 2.10.0
   */
	public void onStartup(IApplicationDefinition appDef)  throws Exception
	{
	}
  
  /**
   * This hook method will be called, if the application is shutdowned or the
   * state of the application is changed from 'productive' to 'inactive'.
   * <p>
   * 
   * You should shutdown and free your additional allocated resources here.
   * <p>
   * <br>
   * <br>
   * <b>Be in mind - you didn't have access to your application database at this point.</b>
   * @since 2.10.0
   */
	public void onShutdown(IApplicationDefinition appDef) throws Exception
	{
	}
  
  /**
   * This hook method will be called, if an application GUI instance (i.e. the
   * application main window) has been created. <br>
   * For <b>each </b> browser window an application instance will be
   * instantiated.
   * <p>
   * 
   * You can access the application database at this point.
   * <p>
   * 
   * Note: It is possible that a logged in user has more than one
   * application instance. This method will be called for each instance.
   * 
   * @param context
   *          The current client context of the application
   * @param app
   *          The GUI application instance itself.
   */
	public void onCreate(IClientContext context, IApplication app)
	{
	}
}
