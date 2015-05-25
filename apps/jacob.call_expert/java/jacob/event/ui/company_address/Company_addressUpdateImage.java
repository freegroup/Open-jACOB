/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Fri Oct 02 18:49:21 CEST 2009
 */
package jacob.event.ui.company_address;


import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CompanyAddress;
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
public class Company_addressUpdateImage extends IStaticImageEventHandler implements IOnClickEventHandler
{


    public void onClick(IClientContext context, IGuiElement element) throws Exception
    {
        MasterDetailManager.onUpdateClicked(MasterDetailManager_CompanyAddress.INSTANCE, context);

    }
}

