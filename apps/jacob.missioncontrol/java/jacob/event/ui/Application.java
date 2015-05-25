package jacob.event.ui;

import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.event.IApplicationEventHandler;

/**
 * 
 */
public class Application extends IApplicationEventHandler
{
    static public final transient String RCS_ID = "$Id: Application.java,v 1.1 2005/11/02 12:47:09 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.1 $";

    /**
     * Called from the jACOB framework if the application will be enabled. You
     * can startup some additional worker threads. Be in mind - you have NO
     * access to your application database at this point.<br>
     * 
     * If you throw an exception the application will not be enabled in the
     * loggin screen and all application resource will be remove.<br>
     * The application will switch to the 'inactive' state und must be enabled
     * in the administration application.
     */
    public void onStartup() throws Exception
    {
    }

    /**
     * Called from the jACOB framework if the application shutdown or the state
     * of the application will be changed from 'productive' to 'inactive'.
     * 
     * Shutdown and free your additional allocated resources here.
     * 
     * Be in mind - you have NO access to your application database at this
     * point.
     */
    public void onShutdown() throws Exception
    {
    }

    /**
     * Will be called if an application instance has been created.<br>
     * For <b>each</b> browser window an application instance will be
     * instanciated.
     * 
     * You can access the application database at this point.
     * 
     * @param context
     *            The current working context of the application
     * @param app
     *            The application object itself.
     */
    public void onCreate(IClientContext context, IApplication app)
    {
        try
        {
            IDomain domain = context.getDomain("callEntry");
            IForm customer = (IForm) domain.findByName("customer");
            // TODO: I18N nicht verwenden! später durcjh passendes IGuiElement.getI18NLabel(IClientContext context); ersetzen
            String dummy = customer.getLabel().substring(1); // % Zeichen abschneiden
            customer.setLabel("1." + I18N.getLocalized(dummy,context));
            
            IForm productionCell = (IForm) domain.findByName("productionCell");
            dummy = productionCell.getLabel().substring(1);
            productionCell.setLabel("2." +I18N.getLocalized( dummy,context));
            
            IForm call = (IForm) domain.findByName("call");
            dummy = call.getLabel().substring(1);
            call.setLabel("3." + I18N.getLocalized(dummy,context));
            
            IForm location = (IForm) domain.findByName("location");
            dummy = location.getLabel().substring(1);
            location.setLabel("4." + I18N.getLocalized(dummy,context));
            
            

        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
    }
}
