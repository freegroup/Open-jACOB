package jacob.event.ui;

import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IApplicationEventHandler;

/**
 *
 */
public class Application extends IApplicationEventHandler
{
  static public final transient String RCS_ID = "$Id: Application.java,v 1.6 2009-12-24 10:02:22 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";

  /**
   * Called from the jACOB framework if the application will be enabled.
   * You can startup some additional worker threads.
   * Be in mind - you have NO access to your application database at this point.<br>
   * 
   * If you throw an exception the application will not be enabled in the login
   * screen and all application resource will be remove.<br>
   * The application will switch to the 'inactive' state and must be enabled in
   * the administration application.
   */
  public void onStartup()  throws Exception
  {
  }
  
  /** 
   * Called from the jACOB framework if the application shutdown or the
   * state of the application will be changed from 'productive' to 'inactive'.
   * 
   * Shutdown and free your additional allocated resources here.
   * 
   * Be in mind - you have NO access to your application database at this point. 
   */
  public void onShutdown() throws Exception
  {
  }
  
  /**
   * Will be called if an application instance has been created.<br>
   * For <b>each</b> browser window an application instance will be instantiated.
   * 
   * You can access the application database at this point.
   * 
   * @param context The current working context of the application
   * @param app The application object itself.
   */
  public void onCreate(IClientContext context, IApplication app)
  {
    try
    {
      if ("ReportWriter".equals(context.getDomain().getName()))
      {
        // Ensure that ReportWriter_configure is the initial form
        if (context.getForm().getName().startsWith("ReportWriter_"))
          context.setCurrentForm("ReportWriter", "ReportWriter_configure");
      }
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }
}
