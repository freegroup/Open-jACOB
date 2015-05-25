/*
 * Created on Jul 6, 2004
 *
 */
package jacob.event.screen.f_administration.category;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 *
 */
public class CategoryImportRouting extends IButtonEventHandler
{

  /* 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button)  throws Exception
  {
  }

  /* 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(  IClientContext context, IGuiElement.GroupState status, IGuiElement emitter)  throws Exception
  {
    emitter.setVisible(false);
  }
}
