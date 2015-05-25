/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Nov 25 16:06:59 CET 2008
 */
package jacob.entrypoint.gui;

import jacob.browser.CustomerBrowser;
import jacob.browser._contact_summaryOrderByDateDECBrowser;
import jacob.common.AppLogger;
import jacob.common.FormManager;
import jacob.common.GroupManagerCustomerHeader;
import jacob.model.Contact_summary;
import jacob.model.Customer_contact;
import jacob.relationset.CustomerCallRelationset;
import java.util.Properties;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.html.Form;
import de.tif.jacob.util.StringUtil;


/**
 * A GUI Entry Point is one way to open the system with a dedicated form.
 * <p>
 * You can access this entry point within an web browser with the URL:
 * http://localhost:8080/jacob/enter?entry=CTI_voice&app=CallExpert&user=admin&pwd=&param1=abc
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and password of the application.<li>
 *       2. Replace localhost:8080 with the real server name and port.<li>
 *       3. You can add any additional parameters to the url. The jACOB application server will provide them
 *          for you via the <code>properties.getProperty("...")</code> method.<li>
 *
 * @author {user}
 */
public class CTI_voice extends IGuiEntryPoint
{
    static public final transient String RCS_ID = "$Id: CTI_voice.java,v 1.4 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.4 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /*
     * The main method for the entry point.
     *
     *   type          (voice, email, customer_key, request_key)
     *   value         (value for type)
     *   mediatype     (voice, email)
     *   mediavalue    (value for mediatype, so actual phone number etc; can be "filtered")
     *   direction     (direction; may or may not start with capital letter)
     *   eduid         (EDU ID)
     *   line_key      (pkey of line table)
     *   product       This is a string value of the product, and can be used to backfill the product (not model) in the request.
     *
     * Email only:
     *   trackingid    (email tracking number)
     *   messageid     (email message ID; can be used to retrieve subject, body etc)
     */
    public void enter(IClientContext context, Properties props) throws Exception
    {
        //Backfill AIC-Contact
        IDataBrowser contactBrowser = context.getDataBrowser(_contact_summaryOrderByDateDECBrowser.NAME);
        contactBrowser.setMaxRecords(1);
        IDataTable contactTable = context.getDataTable(Contact_summary.NAME);
        contactTable.qbeClear();
        contactTable.qbeSetKeyValue(Contact_summary.eduid, props.getProperty("eduid"));
        contactBrowser.search(IRelationSet.LOCAL_NAME);
        if (contactBrowser.recordCount() == 1)
        {
            context.getDataAccessor().propagateRecord(contactBrowser.getRecord(0).getTableRecord(), Filldirection.BOTH);
        }

        ///Backfill
        String type = StringUtil.toSaveString(props.getProperty("type")).toLowerCase();
        if (type.equals("voice"))
        {
            handleTypeVoice(context, props);
        }
        else if (type.equals("email"))
        {
            handleTypeEmail(context, props);
        }
        else if (type.equals("customer_key"))
        {
            handleTypeCustomer(context, props);
        }
        else if (type.equals("request_key"))
        {
            handleTypeRequest(context, props);
        }
    }

    private void handleTypeRequest(IClientContext context, Properties props) throws Exception
    {
    }

    private void handleTypeCustomer(IClientContext context, Properties props) throws Exception
    {
    }

    private void handleTypeEmail(IClientContext context, Properties props) throws Exception
    {
    }

    private void handleTypeVoice(IClientContext context, Properties props) throws Exception
    {
        IDataBrowser customerBrowser = context.getDataAccessor().getBrowser(CustomerBrowser.NAME);
        IDataTable customerContactTable = context.getDataAccessor().getTable(Customer_contact.NAME);
        customerContactTable.qbeSetKeyValue(Customer_contact.contact, props.get("value"));
        customerBrowser.search(CustomerCallRelationset.NAME);
        if (customerBrowser.recordCount() == 1)
        {
            customerBrowser.setSelectedRecordIndex(0);
            customerBrowser.propagateSelections();
        }
        ((Form)context.getForm()).setCurrentBrowser(GroupManagerCustomerHeader.get(context).getBrowser());
        if (customerBrowser.recordCount() == 0)
        {
            context.createMessageDialog("Nix gefunden f�r ["+props.get("value")+"]").show();
        }
    }

    /**
     * Returns the domain for the GUI entry point.
     */
    public String getDomain()
    {
        return FormManager.CALLHANDLING_DOMAIN; // <== EDIT this value!!!!
    }

    /**
     * Returns the name of a form within the returned domain.
     */
    public String getForm()
    {
        return FormManager.CALLHANDLING_FORM; // <== EDIT this value!!!!
    }

    /**
     * @return <code>false</code>, if the GUI entry point has no left side navigation.
     */
    public boolean hasNavigation()
    {
        return false;
    }

    /**
     * @return <code>false</code>, if the GUI entry point has no search browser.
     */
    public boolean hasSearchBrowser()
    {
        return true;
    }

    /**
     * @return <code>false</code>, if the GUI entry point has no toolbar at the top.
     */
    public boolean hasToolbar()
    {
        return true;
    }
}
