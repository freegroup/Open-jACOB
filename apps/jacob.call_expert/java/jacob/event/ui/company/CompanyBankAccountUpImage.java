/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Feb 11 10:12:01 CET 2009
 */
package jacob.event.ui.company;


import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CompanyBankAccount;
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
public class CompanyBankAccountUpImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    MasterDetailManager.onUpClicked(MasterDetailManager_CompanyBankAccount.INSTANCE, context);
  }
}
