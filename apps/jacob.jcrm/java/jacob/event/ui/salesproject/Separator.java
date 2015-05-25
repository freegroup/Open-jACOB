/*
 * Created on Apr 26, 2004
 *
 */
package jacob.event.ui.salesproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import de.tif.jacob.screen.IClientContext;


/**
 *
 */
public class Separator extends de.tif.jacob.screen.event.IOwnDrawElementEventHandler
{
  static public final transient String RCS_ID = "$Id: Separator.java,v 1.3 2005/10/18 07:29:09 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  /**
   * Separator height
   */
  private static final int HEIGHT = 3;
  
  /*
   * will be called after a refresh() call
   */
  public void paint(IClientContext context, Graphics graphics, Dimension size) throws Exception
  {
    // draw the background
    graphics.setColor(Color.white);
    graphics.fillRect(0, 0, size.width, size.height);
    
    graphics.setColor(Color.black);
    graphics.fillRect(0, (size.height-HEIGHT)/2, size.width, HEIGHT);
  }
  
  /**
   * 
   * @return The transparent color for the image
   */
  public Color getTransparentColor() throws Exception
  {
    return Color.white;
  }
}
