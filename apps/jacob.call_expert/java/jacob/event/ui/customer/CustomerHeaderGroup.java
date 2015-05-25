/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Nov 05 15:16:33 CET 2008
 */
package jacob.event.ui.customer;

import jacob.common.AppLogger;
import jacob.common.GroupManagerCompanyHeader;
import jacob.common.GroupManagerRequestHeader;
import jacob.model.Company;
import jacob.model.Customer;
import jacob.model.Request;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;

/**
 *
 * @author achim
 */
public class CustomerHeaderGroup extends IGroupEventHandler
{
    static public final transient String RCS_ID = "$Id: CustomerHeaderGroup.java,v 1.5 2009/11/23 11:33:42 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.5 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     *
     *
     * @param context
     *          The current client context
     * @param status
     *          The new status of the group.
     * @param emitter
     *          The corresponding GUI element of this event handler
     */
    public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
    {
        // This must be done to enforce e refresh of the "link" icons in the UI Company-Group.
        // In the other case the Company-Record is not refreshed (jACOB cache) and the new state
        // of the link will not be displayed.
        {
            IDataTableRecord company = context.getDataTable(Company.NAME).getSelectedRecord();
            context.getDataTable(Company.NAME).clear();
            if (company != null)
            {
                context.getDataTable(Company.NAME).setSelectedRecord(company.getPrimaryKeyValue());
                //Hack s. o.
                IDataTableRecord customerRecord = context.getDataTable(Customer.NAME).getSelectedRecord();
                if(customerRecord != null)
                {
                    String companyPkey = context.getSelectedRecord().getSaveStringValue(Company.pkey);
                    String companyFkey = customerRecord.getSaveStringValue(Customer.company_key);
                    IGuiElement imageLinked = context.getForm().findByName("companyLinkImage");
                    imageLinked.setVisible(!companyPkey.equals(companyFkey));
                    IGuiElement imageUnlinked = context.getForm().findByName("companyUnlinkImage");
                    imageUnlinked.setVisible(companyPkey.equals(companyFkey));
                }
            }
        }


        IDataTableRecord requestRecord = context.getDataTable(Request.NAME).getSelectedRecord();
        if (!(requestRecord != null && requestRecord.isNewOrUpdated()))
        {
            // No request in update mode. In this case we enable/disable the other groups in relation
            // to the state of this group
            //
            GroupManagerCompanyHeader.enableChildren(context, status == IGroup.SEARCH || status == IGroup.SELECTED);
            GroupManagerRequestHeader.enableChildren(context, status == IGroup.SEARCH || status == IGroup.SELECTED);
        }
    }

    @Override
    public Class getSearchBrowserEventHandlerClass()
    {
        return CustomerBrowser.class;
    }
}
