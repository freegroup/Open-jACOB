/*
 * Created on 12.03.2008
 *
 */
package de.tif.jacob.screen.event;

import de.tif.jacob.screen.IGuiElement;

/**
 * An event which indicates that a keystroke occurred in a component.<br>
 * This low-level event is generated by a component object (such as a text field)
 * when a key is pressed. 
 *
 * @since 2.7.2
 */
public final class KeyEvent
{
  // Enter key has been pressed
  public final static int VK_ENTER=1;
  
  private final int keyCode;
  private final IGuiElement emitter;
  
  public KeyEvent(int keyCode, IGuiElement emitter)
  {
    this.keyCode = keyCode;
    this.emitter = emitter;
  }

  public int getKeyCode()
  {
    return keyCode;
  }
  
  public IGuiElement getEmitter()
  {
    return this.emitter;
  }
}
