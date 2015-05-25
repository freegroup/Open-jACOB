/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Feb 07 18:25:59 CET 2006
 */
package jacob.event.ui;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IApplicationEventHandler;
import jacob.common.AppLogger;
import jacob.model.Configuration;
import jacob.model.Person;
import jacob.security.UserFactory;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class Application extends IApplicationEventHandler
{
	static public final transient String RCS_ID = "$Id: Application.java,v 1.1 2007/11/25 22:12:33 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public static final String INITIAL_INBOX   ="initialInbox";
	public static final String INITIAL_PRIVATE ="initialPrivate";
	public static final String INITIAL_COMMON  ="initialCommon";
	
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
  
	
  public void onLogin(IClientContext context, IApplication app) 
  {
  	// der Admin user hat keine eigene Konfiguration, da dieser kein
  	// eMail-Account hat.
  	//
  	if(UserFactory.ADMIN_USER_ID.equals(context.getUser().getLoginId()))
  		return;
  	
  	context.getUser().setProperty(INITIAL_COMMON,Boolean.TRUE);
  	context.getUser().setProperty(INITIAL_INBOX,Boolean.TRUE);
  	context.getUser().setProperty(INITIAL_PRIVATE,Boolean.TRUE);
    
  	try 
  	{
      // initial Browser und Form setzen
      IDataBrowser browser = context.getDataBrowser("emailBrowser");
      context.setCurrentForm("eMail","email");
      context.setDataBrowser(browser);
      
			IDataTable table = context.getDataTable(Configuration.NAME);
			table.qbeClear();
			if(table.search()!=1)
			{
				IDataTransaction trans = context.getDataAccessor().newTransaction();
				IDataTableRecord record = table.newRecord(trans);
				record.setValue(trans,Configuration.mandator_id,context.getUser().getMandatorId());
				trans.commit();
			}
			IDataTable person = context.getDataTable(Person.NAME);
			person.qbeSetKeyValue(Person.loginname,context.getUser().getLoginId());
			if(person.search()==1)
			{
				String password = person.getSelectedRecord().getSaveStringValue(Person.password);
				if(password.length()==0)
					context.createMessageDialog("You has set not password.").show();
			}
		} 
  	catch (Exception e) 
		{
  		ExceptionHandler.handle(context, e);
		}
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
  	// der Admin user hat keine eigene Konfiguration, da dieser kein
  	// eMail-Account hat.
  	//
  	if(UserFactory.ADMIN_USER_ID.equals(context.getUser().getLoginId()))
  	{
  		return;
  	}
  	context.getUser().setProperty("initialSearch",Boolean.TRUE);
    app.setToolbarVisible(false);
	}
}
