/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Mar 07 09:10:02 CET 2007
 */
package jacob.event.ui;

import jacob.common.AppLogger;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;

import com.aspose.slides.License;

import de.tif.jacob.core.Property;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IApplicationEventHandler;

/**
 *
 * @author andherz
 */
public class Application extends IApplicationEventHandler
{
	static public final transient String RCS_ID = "$Id: Application.java,v 1.3 2007/10/02 10:53:06 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();
	public static final String INITIAL_PPT = "MUST_PPT_SEARCH";
	public static final String INITIAL_COMPOSITION = "MUST_COMPOITION_SEARCH";
	

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
   */
	public void onStartup()  throws Exception
	{
    //
    // Open and set license for the PPT util classes
    //
     License lic = new License();
     try 
     {
    	 InputStream in = this.getClass().getResourceAsStream("Aspose.Slides.lic");
    	 lic.setLicense(in);
    	 in.close();
     } 
     catch (Exception e) 
     {
    	 ExceptionHandler.handle(e);
     }
	}
  
  /**
   * This hook method will be called, if the application is shutdowned or the
   * state of the application is changed from 'productive' to 'inactive'.
   * <p>
   * 
   * You should shutdown and free your additional allocated resources here.
   * <p>
   * 
   * Be in mind - you have NO access to your application database at this point.
   */
	public void onShutdown() throws Exception
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
		Property.DATA_ACCESSOR_SCOPE.setValue(app.getName(), "form");
    Property.USER_THEME_DEFAULT.setValue(app.getName(), "DaimlerChrysler");
//		app.setToolbarVisible(false);
	}
}
