/*
 * Created on 27.06.2008
 *
 */
package de.tif.jacob.screen.event;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IInFormBrowser;
import de.tif.jacob.screen.IGuiElement.GroupState;

public abstract class IInformBrowserEventHandler extends IBrowserEventHandler
{
  /**
   * This event method will be called, if the status of the corresponding group
   * has been changed. Derived event handlers could overwrite this method, e.g.
   * to enable/disable GUI elements in relation to the group state. <br>
   * Possible group state values are defined in
   * {@link IGuiElement}:<br>
   * <ul>
   *     <li>{@link IGuiElement#UPDATE}</li>
   *     <li>{@link IGuiElement#NEW}</li>
   *     <li>{@link IGuiElement#SEARCH}</li>
   *     <li>{@link IGuiElement#SELECTED}</li>
   * </ul>
   * 
   * @param context
   *          The current client context
   * @param state
   *          The new group state
   * @param browser
   *          The corresponding GUI element to this event handler
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IInFormBrowser browser) throws Exception
  {
    // do nothing by default
  }

}
