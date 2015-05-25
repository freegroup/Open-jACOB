/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Jan 21 12:02:54 CET 2009
 */
package jacob.event.ui.company;


import jacob.common.tabcontainer.TabManagerCompanyAddress;
import jacob.model.Company;
import jacob.model.Company_address;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 *
 * @author achim
 */
public class CompanyCreateAddressStaticImage extends IStaticImageEventHandler  implements IOnClickEventHandler
{

    public void onClick(IClientContext context, IGuiElement element) throws Exception
    {
        IDataTableRecord companyRecord = context.getDataTable(Company.NAME).getSelectedRecord();
        IDataTransaction transaction = companyRecord.getCurrentTransaction();
        if(transaction==null)
            return;

        IDataTableRecord companyAddressRecord = Company_address.newRecord(context, transaction);
        companyAddressRecord.setValue(transaction, Company_address.company_key, companyRecord.getValue(Company.pkey));

        TabManagerCompanyAddress.showNewTab(context);
        context.getGroup().findByName("company_addressStreet").requestFocus();
    }

    /**
     * The event handler if the group status has been changed.<br>
     *
     * @param context The current work context of the jACOB application.
     * @param status  The new state of the group.
     * @param emitter The emitter of the event.
     */
    public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IStaticImage image) throws Exception
    {
        // Your code here.....
    }
}

