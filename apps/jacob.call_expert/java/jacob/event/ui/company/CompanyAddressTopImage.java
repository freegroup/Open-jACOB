/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Feb 18 13:54:11 CET 2009
 */
package jacob.event.ui.company;


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
public class CompanyAddressTopImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
    public void onClick(IClientContext context, IGuiElement element) throws Exception
    {
        MasterDetailManager.onTopClicked(MasterDetailManager_CompanyAddress.INSTANCE, context);
    }

}

