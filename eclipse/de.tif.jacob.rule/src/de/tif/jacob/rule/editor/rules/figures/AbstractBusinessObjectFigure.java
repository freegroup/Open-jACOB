/*
 * Created on 22.02.2005
 *
 */
package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import de.tif.jacob.rule.Constants;

/**
 *
 */
public class AbstractBusinessObjectFigure extends Figure
{
  
  public AbstractBusinessObjectFigure()
  {
    setOpaque(false);
    setPreferredSize(Constants.RULE_GRID_WIDTH*3, Constants.RULE_GRID_HEIGHT*2);
    setBorder(new LineBorder(Constants.COLOR_BORDER_BRIGHT));
  }
  
  
  protected void paintFigure(Graphics graphics) 
  {
    graphics.pushState();
    graphics.setForegroundColor(Constants.COLOR_BO_END);
    graphics.setBackgroundColor(Constants.COLOR_BO_START);
    graphics.fillGradient(getBounds(),true);
    graphics.popState();
    super.paintFigure(graphics);
  }
  
}
