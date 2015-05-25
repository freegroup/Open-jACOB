package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import de.tif.jacob.rule.Constants;

/**
 *
 */
public class BooleanDecisionFigure extends DecisionFigure
{
  
  public BooleanDecisionFigure()
  {
    setLayoutManager(new FreeformLayout());
    setPreferredSize(Constants.RULE_GRID_WIDTH*2, Constants.RULE_GRID_HEIGHT*2);
  }
  
  public void paintFigure(Graphics g) 
  {
  	Rectangle r = getBounds();

  	PointList points = new PointList();
  	
  	points.addPoint((r.x+r.right())/2,r.y);
  	points.addPoint(r.right(),(r.y+r.bottom())/2);
  	points.addPoint((r.x+r.right())/2,r.bottom());
  	points.addPoint(r.x,(r.y+r.bottom())/2);
  	
  	g.setLineWidth(1);
  	g.setForegroundColor(Constants.COLOR_BORDER_DARK);
    g.setBackgroundColor(Constants.COLOR_DECISION);
  	g.fillPolygon(points);
  	g.drawPolygon(points);
  }
}
