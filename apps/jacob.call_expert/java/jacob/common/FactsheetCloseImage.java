/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Mon Dec 01 12:21:18 CET 2008
 */
package jacob.common;


import jacob.common.tabcontainer.TabManagerRequest;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * Common Eventhandler for the cross in the upper right corner of the fact sheet
 * for Company, Customer and Request.
 *
 * @author andherz
 */
public class FactsheetCloseImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
    public void onClick(IClientContext context, IGuiElement element) throws Exception
    {
        TabManagerRequest.clear(context, context.getDataTable().getName());
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
        //image.setVisible(state == IGroup.SELECTED || state == IGroup.UPDATE);
        image.setVisible(state == IGroup.SELECTED);
    }
}

