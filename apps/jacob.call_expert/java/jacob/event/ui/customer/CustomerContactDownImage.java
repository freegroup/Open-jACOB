/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Feb 05 08:22:17 CET 2009
 */
package jacob.event.ui.customer;


import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CustomerContact;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class CustomerContactDownImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    MasterDetailManager.onDownClicked(MasterDetailManager_CustomerContact.INSTANCE, context);
  }
}

