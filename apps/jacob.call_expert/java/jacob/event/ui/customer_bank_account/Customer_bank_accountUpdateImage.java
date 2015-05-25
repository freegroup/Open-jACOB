/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Oct 05 15:38:08 CEST 2009
 */
package jacob.event.ui.customer_bank_account;

import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CustomerBankAccount;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the onClick events of the user.
 * 
 * @author achim
 */
public class Customer_bank_accountUpdateImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    MasterDetailManager.onUpdateClicked(MasterDetailManager_CustomerBankAccount.INSTANCE, context);
  }
}
