/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Mon Oct 05 14:57:00 CEST 2009
 */
package jacob.event.ui.company_bank_account;


import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CompanyBankAccount;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 *
 * @author achim
 */
public class Company_bank_accountDeleteImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
    public void onClick(IClientContext context, IGuiElement element) throws Exception
    {
        MasterDetailManager.onDeleteClicked(MasterDetailManager_CompanyBankAccount.INSTANCE, context);

    }
}

