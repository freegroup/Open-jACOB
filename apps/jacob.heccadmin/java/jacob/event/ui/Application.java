/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Feb 03 10:02:51 CET 2009
 */
package jacob.event.ui;

import java.security.GeneralSecurityException;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IApplicationEventHandler;
import de.tif.jacob.security.IRole;
import de.tif.jacob.security.IUser;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;
import com.hecc.jacob.ldap.Role;

/**
 *
 * @author R.Spoor
 */
public class Application extends IApplicationEventHandler
{
	static public final transient String RCS_ID = "$Id: Application.java,v 1.2 2009/02/19 15:02:13 R.Spoor Exp $";
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
	 */
	@Override
	public void onStartup() throws Exception
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
	@Override
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
	 * @param context The current client context of the application
	 * @param app The GUI application instance itself.
	 */
	@Override
	public void onCreate(IClientContext context, IApplication app)
	{
        IUser user = context.getUser();
        if (!hasRole(user, Role.ADMIN))
        {
            hideForm("contact", app);
            hideForm("loggingadmin", app);
            hideForm("numberadminadmin", app);
            hideForm("properties", app);

            if (!hasRole(user, Role.POWER_USER))
            {
                hideForm("contactfilter", app);
            }
        }
	}

	/**
	 * Returns whether or not a user has a role.
	 *
	 * @param user The user to check.
	 * @param role The role to check for.
	 * @return Whether or not the user has the role. If it cannot be determined
	 *         this will be regarded as <code>false</code>.
	 */
	private boolean hasRole(IUser user, IRole role)
	{
	    try
	    {
	        return user.hasRole(role.getName());
	    }
	    catch (GeneralSecurityException e)
	    {
	        return false;
	    }
	}

	/**
	 * Hides a form if it exists.
	 *
	 * @param name The name of the form to hide.
	 * @param app The application to use to find the form.
	 */
	private void hideForm(String name, IApplication app)
	{
	    IGuiElement form = app.findByName(name);
	    if (form != null)
	    {
	        form.setVisible(false);
	    }
	}
}
