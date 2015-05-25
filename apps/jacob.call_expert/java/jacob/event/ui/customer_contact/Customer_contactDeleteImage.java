/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Oct 05 15:05:34 CEST 2009
 */
package jacob.event.ui.customer_contact;


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
 * @author achim
 */
public class Customer_contactDeleteImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    MasterDetailManager.onDeleteClicked(MasterDetailManager_CustomerContact.INSTANCE, context);
  }
}

