package jacob.common;

import java.awt.Color;
import java.util.Iterator;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.GuiHtmlElement;

public class AbstractGroupManager
{
    protected static void enableChildren(IClientContext context, IGroup group, boolean enableFlag)
    {
        try
        {
            Iterator<GuiHtmlElement> iter = group.getChildren().iterator();
            while (iter.hasNext())
            {
                GuiHtmlElement object = iter.next();
                object.setEnable(enableFlag);
                object.setColor(enableFlag ? null : Color.LIGHT_GRAY);
                object.calculateHTML((ClientContext) context);
            }
        }
        catch (Exception e)
        {
            ExceptionHandler.handle(context, e);
        }
    }
}
