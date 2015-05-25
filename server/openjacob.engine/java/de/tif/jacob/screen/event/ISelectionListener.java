/*
 * Created on 30.03.2008
 *
 */
package de.tif.jacob.screen.event;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;


/**
 * Interface for listening to selection changes.
 * This interface may be implemented by clients. 
 *
 */
public interface ISelectionListener
{

  /**
   * Notifies this listener that the selection has changed.
   * 
   * @param context The current context of the application
   * @param emitter The UI element which request the execution of this action
   * @param selection The selection
   * @throws Exception w
   */
  public void selectionChanged(IClientContext context, IGuiElement emitter, ISelection selection) throws Exception;
}
