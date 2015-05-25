/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Sep 23 16:55:17 CEST 2009
 */
package jacob.event.ui.company_contact;


import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CompanyContact;
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
public class CompanyDeleteImage extends IStaticImageEventHandler  implements IOnClickEventHandler
{
    public void onClick(IClientContext context, IGuiElement emitter) throws Exception
    {
        MasterDetailManager.onDeleteClicked(MasterDetailManager_CompanyContact.INSTANCE, context);
    }
}

