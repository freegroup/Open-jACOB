/*
 * Created on Apr 26, 2004
 *
 */
package jacob.event.ui.chartdata;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import de.tif.jacob.screen.IClientContext;


/**
 *
 */
public class ChartdataSmily extends de.tif.jacob.screen.event.IOwnDrawElementEventHandler
{
  static public final transient String RCS_ID = "$Id: ChartdataSmily.java,v 1.2 2005/05/31 08:24:21 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  /*
   * will be called after a refresh() call
   */
  public void paint(IClientContext context, Graphics graphics, Dimension size)
  {
    // Setup the colors
    Color transparent = getTransparentColor();
    Color background  = Color.yellow;
    Color foreground  = Color.black;

    // Draw our pretty picture :-)

    graphics.setColor(transparent);
    graphics.fillRect(0, 0, size.width, size.height);

    graphics.setColor(background);
    graphics.fillOval(0, 0, size.width - 1, size.height - 1);

    graphics.setColor(foreground);
    graphics.drawOval(0, 0, size.width - 1, size.height - 1);

    int eyeHeight = size.height / 6;
    int eyeWidth = size.width / 6;

    graphics.fillOval(size.width / 2 - (int)(eyeWidth * 1.5), eyeHeight * 2, eyeWidth, eyeHeight);
    graphics.fillOval(size.width / 2 + (int)(eyeWidth * .5), eyeHeight * 2, eyeWidth, eyeHeight);

    graphics.drawArc(size.width / 2 - (int)(eyeWidth * 1.5), size.height / 2, eyeWidth * 3, size.height / 3, 180, 180);
    
  }
  
  /**
   * 
   * @return The transparent color for the image
   */
  public Color getTransparentColor()
  {
    return Color.white;
  }
}
