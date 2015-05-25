/*
 * Created on 22.02.2005
 *
 */
package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import de.tif.jacob.rule.Constants;

/**
 *
 */
public class BooleanFigure extends RectangleFigure
{
  public BooleanFigure()
  {
    setSize(new Dimension(10,10));
  	setForegroundColor(Constants.COLOR_FONT);
  }
  
  public void paintFigure(Graphics g) 
  {
    Rectangle r = getBounds().getCopy();
    PointList points = new PointList();
    
    points.addPoint(r.x+r.width/2,r.y);
    points.addPoint(r.right(),r.y+r.height/2);
    points.addPoint(r.x+r.width/2,r.bottom());
    points.addPoint(r.x,r.y+r.height/2);
    
    g.setLineWidth(1);
    g.setForegroundColor(getForegroundColor());
    g.setBackgroundColor(getBackgroundColor());
    g.fillPolygon(points);
    g.drawPolygon(points);
  }

}


