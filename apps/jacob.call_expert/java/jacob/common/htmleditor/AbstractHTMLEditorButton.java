package jacob.common.htmleditor;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


public abstract class AbstractHTMLEditorButton extends IStaticImageEventHandler  implements IOnClickEventHandler
{
    public abstract String getStartTag();
    public abstract String getEndTag();

    public final void onClick(IClientContext context, IGuiElement element) throws Exception
    {
        HTMLEditorHelper.insertTag(context, getStartTag(), getEndTag());
    }

    /**
     * The event handler if the group status has been changed.<br>
     *
     * @param context The current work context of the jACOB application.
     * @param status  The new state of the group.
     * @param emitter The emitter of the event.
     */
    public final void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IStaticImage image) throws Exception
    {
        image.setEnable(state.equals(IGroup.NEW)||state.equals(IGroup.UPDATE));
    }
}
