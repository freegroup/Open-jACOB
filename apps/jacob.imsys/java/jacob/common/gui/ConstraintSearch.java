/*
 * Created on 02.06.2005 by mike
 * 
 *
 */
package jacob.common.gui;

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * @author mike
 *
 */
public class ConstraintSearch extends IActionButtonEventHandler
{

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
     */
    public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
    {
        button.setSearchMode(true);
        return true;

    }

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
     */
    public void onSuccess(IClientContext context, IGuiElement button) throws Exception
    {
    }

}
