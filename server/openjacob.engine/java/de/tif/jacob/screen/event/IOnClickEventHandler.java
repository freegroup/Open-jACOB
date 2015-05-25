/*
 * Created on 04.12.2007
 *
 */
package de.tif.jacob.screen.event;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;

/**
 * Extended interface for images and caption elements.<br>
 * Implement this method if you want receive clickEvents.
 * 
 *
 */
public interface IOnClickEventHandler
{
  public void onClick(IClientContext context, IGuiElement element) throws Exception;
}
