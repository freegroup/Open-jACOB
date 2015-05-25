package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import de.tif.jacob.rule.Constants;

/**
 *
 */
public abstract class DecisionFigure extends RectangleFigure
{
  
  public DecisionFigure()
  {
    setLayoutManager(new FreeformLayout());
  }
  
  public void paintFigure(Graphics g) 
  {
  	super.paintFigure(g);
  	Rectangle r = getBounds();

  	PointList points = new PointList();
  	
  	points.addPoint(r.x  ,r.y+Constants.RULE_GRID_HEIGHT-5);
  	points.addPoint(r.x+5,r.y+Constants.RULE_GRID_HEIGHT);
  	points.addPoint(r.x  ,r.y+Constants.RULE_GRID_HEIGHT+5);
  	points.addPoint(r.x  ,r.y+Constants.RULE_GRID_HEIGHT-5);
  	
  	g.setLineWidth(1);
  	g.setForegroundColor(Constants.COLOR_DECISION_IN_BORDER);
    g.setBackgroundColor(Constants.COLOR_DECISION_IN);
  	g.fillPolygon(points);
  	g.drawPolygon(points);
  }
}
