/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Dec 07 11:42:53 CET 2006
 */
package jacob.event.ui;

import jacob.common.AppLogger;
import jacob.common.EmailSender;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.ruleengine.bo.Email;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IApplicationEventHandler;

/**
 *
 * @author andherz
 */
public class Application extends IApplicationEventHandler
{
	static public final transient String RCS_ID = "$Id: Application.java,v 1.1 2007/11/25 22:10:32 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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
   */
	public void onStartup()  throws Exception
	{
		// set our own email sender in the rule engine. Don't use the build in 
		// messaging infrastructure
		//
		Email.setEmailSender(new EmailSender());
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
		// Es muss immer ein Configuration Record existieren
		//
		IDataTransaction trans = context.getDataAccessor().newTransaction();
		try
		{
			IDataTable table = context.getDataTable(jacob.model.Email.NAME);
			table.qbeClear();
			if(table.search()!=1)
			{
				IDataTableRecord record = table.newRecord(trans);
				trans.commit();
			}
		}
		catch(Exception exc)
		{
			ExceptionHandler.handleSmart(context, exc);
		}
		finally
		{
			trans.close();
		}
		context.createMessageDialog("Go to [administration->rule] and press the search button to edit/show some rules.").show();
	}
}
