/*
 * Created on 12.03.2008
 *
 */
package de.tif.jacob.screen.event;

import de.tif.jacob.screen.IClientContext;

/**
 * The listener interface for receiving keyboard events (keystrokes).
 * A GUIElement must implement this interface that is interested in 
 * processing a hotkey event.
 *
 * @since 2.7.2
 */
public interface IHotkeyEventHandler
{
  /**
   * This method will be called if an hotkey has been detected.<br>
   * Default action of the form will not be called (e.g. Search, Save,...).
   * 
   * @param context
   *          The current client context
   * @param event
   *          The key event
   */
  public void keyPressed(IClientContext context, KeyEvent event) throws Exception;

  /**
   * The bitwise-or of the hot key constants. Only key events which are part of the mask will be send to the server.<br>
   * Return <b>KeyEvent.VK_ENTER</b> if you want receive the Enter key or <b>0</b>
   * if you want to receive nothing.<br>
   * <br>
   * This method will be called after each <b>onGroupStatusChanged</b> event.
   * 
   * @param context
   *          The current client context
   * @return The bitwise-or of the hot key constants which are defined in KeyEvent.
   * @see KeyEvent
   */
  public int getKeyMask(IClientContext context);
}
