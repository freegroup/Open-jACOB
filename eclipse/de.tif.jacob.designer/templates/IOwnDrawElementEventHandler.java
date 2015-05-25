/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on {date}
 */
package {package};

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import de.tif.jacob.screen.*;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author {author}
 */
public class {class} extends de.tif.jacob.screen.event.IOwnDrawElementEventHandler
{
	static public final transient String RCS_ID = "$Id: IOwnDrawElementEventHandler.java,v 1.2 2007/11/11 22:24:49 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

  /**
   * This hook method will be called, if the own draw area has to be refreshed.
   * Therefore, the image within the own draw area has to be painted completely
   * by means of this method.
   * 
   * @param context
   *          The current client context
   * @param graphics
   *          The graphics object to be used for all draw operations
   * @param dimension
   *          The dimension of the own draw area
   */
	public void paint(IClientContext context, IOwnDrawElement image, Graphics graphics, Dimension size)
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
   * Returns the color which should be used as transparent color for the image.
   * 
   * @return The transparent color for the image or <code>null</code>, if the
   *         image does not contain a transparent color.
   */
	public Color getTransparentColor()
	{
		return Color.white;
	}
}
