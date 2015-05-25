/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Fri Oct 02 18:34:47 CEST 2009
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
public class Company_AddressDeleteImage extends IStaticImageEventHandler  implements IOnClickEventHandler
{
    /**
     * The event handler if the group status has been changed.<br>
     *
     * @param context The current work context of the jACOB application.
     * @param status  The new state of the group.
     * @param emitter The emitter of the event.
     */
    public void onClick(IClientContext context, IGuiElement element) throws Exception
    {
        MasterDetailManager.onDeleteClicked(MasterDetailManager_CompanyAddress.INSTANCE, context);
    }
}

