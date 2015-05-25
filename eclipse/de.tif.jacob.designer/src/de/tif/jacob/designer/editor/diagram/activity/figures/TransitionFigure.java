/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/
/*
 * Created on 23.02.2005
 *
 */
package de.tif.jacob.designer.editor.diagram.activity.figures;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.RootEditPart;

/**
 *
 */
public class TransitionFigure extends PolylineConnection implements IDiagramElementFigure
{
  Label description;
  private static class LabelLocator extends ConnectionLocator
  {
    public LabelLocator(Connection c)
    {
      super(c);
    }

    protected Point getReferencePoint()
    {
      Connection conn = getConnection();
      Point p = Point.SINGLETON;
      int index = (conn.getPoints().size() - 1) / 2;
      Point p1 = conn.getPoints().getPoint(index);
      Point p2 = conn.getPoints().getPoint(index + 1);
      conn.translateToAbsolute(p1);
      conn.translateToAbsolute(p2);
      p.x = (p2.x - p1.x) / 2 + p1.x;
      p.y = (p2.y - p1.y) / 2 + p1.y;

      // if the 2 points are on a horizontal line,
      // the label should be located a little bit below!
      if (p1.y == p2.y)
        p.y += 10;

      return p;
    }
  }  

  public TransitionFigure(RootEditPart editpart, String label)
  {
    setTargetDecoration(new PolygonDecoration()); // arrow at target
    setConnectionRouter(new ManhattanConnectionRouter());
//    description = new AlphaLabel(editpart, label);
//    
//     add(description, new LabelLocator(this));
  }
  
  public Label getLabel()
  {
    return description;
  }
  
  public void setText(String text)
  {
    // TODO Auto-generated method stub
    description.setText(text);
  }
}
