/*
 * Created on Apr 26, 2004
 *
 */
package jacob.event.ui.budget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IClientContext;


/**
 *
 */
public class ProgressBar extends de.tif.jacob.screen.event.IOwnDrawElementEventHandler
{
  static public final transient String RCS_ID = "$Id: ProgressBar.java,v 1.3 2006/02/24 02:16:17 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  // Setup the colors
  //
  private final static Color transparent   = Color.blue;
  private final static Color alertColor    = new Color(255,121 ,10);
  private final static Color fillColor     = new Color(204,204,255);
  private final static Color barColor      = fillColor.darker();// new Color(97,96 ,100);
  private final static Color frameBrighter = barColor.brighter();
  private final static Color frameDarker   = barColor.darker();
  private final static Color fontColor     = new Color(97,96,100);;

  /*
   * will be called after a refresh() call
   */
  public void paint(IClientContext context, Graphics graphics, Dimension size)
  {
  	try 
  	{
			IDataTableRecord record = context.getSelectedRecord();
			fill3DRect(graphics, size.width, size.height);

			if(record==null)
				return;
			
			long amount = record.getlongValue("amount");
			long used_amount = record.getlongValue("used_amount");
			
			double percentage = ((100.0/amount)*used_amount)/100.0;
			drawBar(graphics, percentage, size.width, size.height);
		} 
  	catch (Exception e) 
  	{
  		ExceptionHandler.handle(context,e);
		}
  }
  
  /**
   * Draw the percentage bar
   * 
   * @param g
   * @param percent
   * @param width
   * @param height
   */
  private void drawBar(Graphics g, double percent,int width, int height) 
  {
    int barEnd = (int)(width*Math.min(1.0, percent));
    if(percent>1.0)
    	g.setColor(alertColor);
    else
    	g.setColor(barColor);
    g.fillRect(2, 2, barEnd, height-4);
    
    // Find the size of this text so we can center it
    g.setColor(fontColor);
    percent = (int)(percent*100);
    FontMetrics fm   = g.getFontMetrics();  // metrics for this object
    
    String label = ""+percent+"%";
    Rectangle2D rect = fm.getStringBounds(label, g); // size of string

    int textHeight = (int)(rect.getHeight());
    int textWidth  = (int)(rect.getWidth());

    // Center text horizontally and vertically
    int x = (width  - textWidth)  / 2;
    int y = (height - textHeight) / 2  + fm.getAscent();

    g.drawString(label, x, y);
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
