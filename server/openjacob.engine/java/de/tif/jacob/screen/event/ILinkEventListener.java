/*
 * Created on 04.12.2007
 *
 */
package de.tif.jacob.screen.event;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;

/**
 *
 * @since 2.10
 */
public interface ILinkEventListener
{
  /**
   * Called if the user activate a link
   * @since 2.10  
   **/
  public void onClick(IClientContext context, IGuiElement element, String link) throws Exception;
}
