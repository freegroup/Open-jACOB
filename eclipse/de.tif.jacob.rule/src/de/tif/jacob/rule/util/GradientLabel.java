/*
 * Created on 09.11.2005
 *
 */
package de.tif.jacob.rule.util;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Image;
import de.tif.jacob.rule.Constants;

public class GradientLabel extends Label
{
	Insets INSETS = new Insets(2,8,2,8);
	
  public GradientLabel(Image i) {
		super(i);
    setOpaque(false);
	}

	public GradientLabel(String s) {
		super(s);
    setOpaque(false);
	}

	public GradientLabel()
  {
    setOpaque(false);
  }
  
	public Insets getInsets() {
		return INSETS;
	}
	
	protected void paintFigure(Graphics graphics) 
  {
    graphics.pushState();
    graphics.setForegroundColor(Constants.COLOR_PANE);
    graphics.setBackgroundColor(Constants.COLOR_HEADER);
//    graphics.setForegroundColor(Constants.COLOR_HEADER);
//    graphics.setBackgroundColor(Constants.COLOR_PANE);
    graphics.fillGradient(getBounds(),true);
    graphics.popState();
    graphics.setForegroundColor(Constants.COLOR_FONT);
    super.paintFigure(graphics);
  }
}
