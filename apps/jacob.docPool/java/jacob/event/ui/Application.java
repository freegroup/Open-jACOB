/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 02 14:49:34 CEST 2010
 */
package jacob.event.ui;

import jacob.common.ui.SearchToolbar;
import jacob.security.Role;

import java.util.Iterator;

import de.tif.jacob.core.Property;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IToolbarButtonDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IApplicationEventHandler;
import de.tif.jacob.screen.impl.IApplicationFactory;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;
import de.tif.jacob.urlredirect.RedirectManager;

/**
 *
 * @author andherz
 */
public class Application extends IApplicationEventHandler
{
	static public final transient String RCS_ID = "$Id: Application.java,v 1.4 2010-09-21 11:28:20 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";

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
	public void onStartup(IApplicationDefinition appDef)  throws Exception
	{
	  RedirectManager.installForward(appDef,"(/dokument).*", "/cmdenter?entry=Download&app=docPool");
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
	    IApplicationFactory factory = ((ClientSession)((ClientContext)context).getSession()).getApplicationFactory();
	    
      context.setPropertyForWindow("toolbar",app.getToolbar());
	    SearchToolbar toolbar = new SearchToolbar(app, app.getApplicationDefinition().getToolbarDefinition());
	    app.setToolbar(toolbar);
	    Iterator toolbarButtonIter = app.getApplicationDefinition().getToolbarDefinition().getToolbarButtons().iterator();
	    while (toolbarButtonIter.hasNext())
	    {
	      IToolbarButtonDefinition button = (IToolbarButtonDefinition) toolbarButtonIter.next();
	      factory.createToolbarButton(app, toolbar, button);
	    }
	
	    Property.DATA_ACCESSOR_SCOPE.setValue(app.getName(), "form");
		   
		  try
	    {
	      if(context.getUser().hasRole(Role.PASSWORD))
	      {
	        app.setNavigationVisible(false);
	        app.setToolbarVisible(false);
	      }
	      else
	      {
	        context.setCurrentForm("documents", "documents_overview");
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	}
}
