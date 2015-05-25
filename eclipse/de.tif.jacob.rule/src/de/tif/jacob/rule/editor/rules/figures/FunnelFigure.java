package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.rule.Constants;

/**
 *
 */
public class FunnelFigure extends RectangleFigure
{
  
  public FunnelFigure()
  {
    BorderLayout layout = new BorderLayout();
    setLayoutManager(layout);

    setSize(Constants.RULE_GRID_WIDTH*2, Constants.RULE_GRID_HEIGHT*2);
    setPreferredSize(Constants.RULE_GRID_WIDTH*2, Constants.RULE_GRID_HEIGHT*2);
    Label   icon = new Label();
    icon.setIcon(JacobDesigner.getImage("funnel_bo.png"));
    setBackgroundColor(Constants.COLOR_DECISION);

    add(icon, BorderLayout.CENTER);
  }
  
  public void paintFigure(Graphics g) 
  {
  	super.paintFigure(g);
  	Rectangle r = getBounds();

  	PointList points = new PointList();
  	int center = r.getCenter().y;
  	points.addPoint(r.x+r.width  -2,center-5);
  	points.addPoint(r.x+r.width-5-2,center);
  	points.addPoint(r.x+r.width  -2,center+5);
  	points.addPoint(r.x+r.width  -2,center-5);
  	
  	g.setLineWidth(1);
  	g.setForegroundColor(Constants.COLOR_DECISION_IN_BORDER);
    g.setBackgroundColor(Constants.COLOR_DECISION_IN);
  	g.fillPolygon(points);
  	g.drawPolygon(points);
  }
}
