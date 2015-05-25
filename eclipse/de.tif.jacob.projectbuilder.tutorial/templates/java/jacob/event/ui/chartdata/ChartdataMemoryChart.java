/*
 * Created on Apr 26, 2004
 *
 */
package jacob.event.ui.chartdata;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import de.tif.jacob.screen.IClientContext;


/**
 *
 */
public class ChartdataMemoryChart extends de.tif.jacob.screen.event.IOwnDrawElementEventHandler
{
  static public final transient String RCS_ID = "$Id: ChartdataMemoryChart.java,v 1.1 2007/11/25 22:19:39 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  // Setup the colors
  //
  private final static Color transparent   = Color.blue;
  private final static Color barColor      = Color.gray;
  private final static Color fillColor     = new Color(204,204,255);
  private final static Color frameBrighter = transparent.brighter();
  private final static Color frameDarker   = transparent.darker();

  /*
   * will be called after a refresh() call
   */
  public void paint(IClientContext context, Graphics graphics, Dimension size)
  {
    fill3DRect(graphics, size.width, size.height);
    drawBar(graphics, 0.79f, size.width, size.height);
  }
  
  /**
   * Draw the percentage bar
   * 
   * @param g
   * @param percent
   * @param width
   * @param height
   */
  private void drawBar(Graphics g, float percent,int width, int height) 
  {
    int barEnd = (int)(width*percent);
    g.setColor(barColor);
    g.fillRect(2, 2, barEnd, height-4);
    
    // Find the size of this text so we can center it
    g.setColor(Color.black);
    percent = (int)(percent*100);
    FontMetrics fm   = g.getFontMetrics();  // metrics for this object
    Rectangle2D rect = fm.getStringBounds(""+percent+"%", g); // size of string

    int textHeight = (int)(rect.getHeight());
    int textWidth  = (int)(rect.getWidth());

    // Center text horizontally and vertically
    int x = (width  - textWidth)  / 2;
    int y = (height - textHeight) / 2  + fm.getAscent();

    g.drawString(""+percent+"%", x, y);
  }

  /**
   * Draw the frame of the progress bar.
   * 
   * @param g
   * @param width
   * @param height
   */
  private void fill3DRect(Graphics g,int width, int height) 
  {
    g.setColor(fillColor);
    g.fillRect(1, 1, width-2, height-2);
    g.setColor(frameDarker);
    g.drawLine(0, 0, 0, height);
    g.drawLine(1, 0, width-1, 0);
    g.setColor(frameBrighter);
    g.drawLine(1, height, width, height);
    g.drawLine(width, 0, width, height-1);
  }

  
  /**
   * 
   * @return The transparent color for the image
   */
  public Color getTransparentColor()
  {
    return transparent;
  }
}
