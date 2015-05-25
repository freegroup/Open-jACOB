/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Nov 25 16:06:59 CET 2008
 */
package jacob.entrypoint.gui;

import jacob.browser._contact_summaryOrderByDateDECBrowser;
import jacob.common.AppLogger;
import jacob.common.StorageEmailManager;
import jacob.model.Contact_summary;
import jacob.relationset.EmailRelationset;
import java.util.Properties;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.util.StringUtil;


/**
 * A GUI Entry Point is one way to open the system with a dedicated form.
 * <p>
 * You can access this entry point within an web browser with the URL:
 * http://localhost:8080/jacob/enter?entry=CTI_email&app=CallExpert&user=admin&pwd=&param1=abc
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and password of the application.<li>
 *       2. Replace localhost:8080 with the real server name and port.<li>
 *       3. You can add any additional parameters to the url. The jACOB application server will provide them
 *          for you via the <code>properties.getProperty("...")</code> method.<li>
 *
 * @author {user}
 */
public class CTI_email extends IGuiEntryPoint
{
    static public final transient String RCS_ID = "$Id: CTI_email.java,v 1.8 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.8 $";

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
        if(contactBrowser.recordCount()==1)
        {
            context.getDataAccessor().propagateRecord(contactBrowser.getRecord(0).getTableRecord(), Filldirection.BOTH);
        }
        else
        {
            System.out.println("Keinen AIC Kontakt mit der ID:"+props.getProperty("eduid"));
        }
        ///Backfill
        String type = StringUtil.toSaveString(props.getProperty("type")).toLowerCase();

        if (type.equals("email"))
        {
            handleTypeEmail(context, props);
        }
        else
        {
            throw new UserException("Invalid entrypoint [" + this.getClass().getName() + "] call for mediatype="+type);
        }
    }


    private void handleTypeEmail(IClientContext context, Properties props) throws Exception
    {
        String messageId = props.getProperty("messageid");
        IDataTableRecord internalMessage = StorageEmailManager.findByExternalId(context, messageId);
        context.getDataAccessor().propagateRecord(internalMessage,EmailRelationset.NAME, Filldirection.BOTH);
    }

    /**
     * Returns the domain for the GUI entry point.
     */
    public String getDomain()
    {
        return "<inboundDomain>";
    }

    /**
     * Returns the name of a form within the returned domain.
     */
    public String getForm()
    {
        return "emailInboundForm";
    }

    /**
     * @return <code>false</code>, if the GUI entry point has no left side navigation.
     */
    public boolean hasNavigation()
    {
        return true;
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
