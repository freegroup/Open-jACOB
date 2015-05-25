/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Jan 22 15:06:11 CET 2009
 */
package jacob.event.ui.event;


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
public class MenuToggleImage extends IStaticImageEventHandler  implements IOnClickEventHandler
{
    public void onClick(IClientContext context, IGuiElement element) throws Exception
    {
        context.getApplication().setNavigationVisible(!context.getApplication().isNavigationVisible());
        context.getApplication().setToolbarVisible(!context.getApplication().isToolbarVisible());
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
        // Your code here.....
    }
}

