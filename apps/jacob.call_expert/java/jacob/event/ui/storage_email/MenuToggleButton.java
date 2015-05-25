/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Mon Nov 23 12:17:46 CET 2009
 */
package jacob.event.ui.storage_email;


import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 *
 * @author R.Spoor
 */
public class MenuToggleButton extends IStaticImageEventHandler implements IOnClickEventHandler
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

