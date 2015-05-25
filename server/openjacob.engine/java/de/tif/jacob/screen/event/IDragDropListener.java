/*
 * Created on 20.06.2008
 *
 */
package de.tif.jacob.screen.event;

import de.tif.jacob.core.Context;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;

public interface IDragDropListener
{
  /**
   * The actual drop has occurred.
   * 
   * @param context the current working context of the user
   * @param dragObject the dragging object
   * @param dropObject the drop target
   * @throws Exception
   */
  public void drop(IClientContext context, IGuiElement dropElement, Object dragObject, Object dropObject) throws Exception;
  
  /**
   * Checks if the drop on the current target is valid.
   * 
   * @param context the current working context of the user
   * @param dragObject the dragging object
   * @param targetObject the drop target
   * @throws Exception
   * @return
   */
  public boolean validateDrop(Context context, IGuiElement targetElement, Object dragObject, Object targetObject) throws Exception;

  /**
   * Provides the listener an opportunity to set the feedback when hovering over a target.
   * @param context the current working context of the user
   * @param dragObject the dragging object
   * @param hoverObject the drop target
   * @throws Exception
   * @return an icon decoration for the drag drop operation. <code>null</code> is not a valid value.
   */
  public Icon getFeedback(Context context, IGuiElement hoverElement, boolean canDrop, Object dragObject, Object hoverObject) throws Exception;
}
