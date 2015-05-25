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
 * Created on 24.01.2005
 *
 */
package de.tif.jacob.designer.editor.relationset.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.RootEditPart;


/**
 *
 */
public class RelationFigure extends PolylineConnection
{
  static class RelationType {}
  public static RelationType RELATION_1_TO_MANY    = new RelationType();
  public static RelationType RELATION_0_TO_MANY    = new RelationType();
  public static RelationType RELATION_1_TO_1       = new RelationType();
  public static RelationType RELATION_1_TO_0       = new RelationType();
  public static RelationType RELATION_0_TO_1       = new RelationType();
//  public static RelationType RELATION_MANY_TO_MANY = new RelationType();

  private static PointList toNPointList;
  private static PointList to0PointList;
  private static PointList to1PointList;

  private static final double SCALE = 0.2;

  private static class ManhattanMidpointLocator extends ConnectionLocator
  {
    public ManhattanMidpointLocator(Connection c)
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


  public RelationFigure(RootEditPart editpart,RelationType type, String label) 
  {
    setConnectionRouter(new ManhattanConnectionRouter());

    setType(type);
//    Label relationshipLabel = new AlphaLabel(editpart, label);
// 
//    add(relationshipLabel, new ManhattanMidpointLocator(this));
  }
  
  public void setType(RelationType type)
  {
    if(type== RELATION_1_TO_MANY)
    {
      setTargetDecoration(getTo1Decoration());
      setSourceDecoration(getToNDecoration());
    }
    else if(type== RELATION_0_TO_MANY)
    {
      setTargetDecoration(getTo0Decoration());
      setSourceDecoration(getToNDecoration());
    }
    else if(type== RELATION_1_TO_0)
    {
      setTargetDecoration(getTo1Decoration());
      setSourceDecoration(getTo0Decoration());
    }
    else if(type== RELATION_1_TO_1)
    {
      setTargetDecoration(getTo1Decoration());
      setSourceDecoration(getTo1Decoration());
    }
    else if(type== RELATION_0_TO_1)
    {
      setTargetDecoration(getTo0Decoration());
      setSourceDecoration(getTo1Decoration());
    }
  }
  
	private static RotatableDecoration getToNDecoration()
  {
    PolygonDecoration decoration = new PolygonDecoration();
    decoration.setBackgroundColor(ColorConstants.white);
    decoration.setScale(SCALE, SCALE);
    decoration.setTemplate(toNPointList);
    return decoration;
  }

  private static RotatableDecoration getTo1Decoration()
  {
    PolylineDecoration decoration = new PolylineDecoration();
    decoration.setScale(SCALE, SCALE);
    decoration.setTemplate(to1PointList);
    return decoration;
  }

  private static RotatableDecoration getTo0Decoration()
  {
    PolygonDecoration decoration = new PolygonDecoration();
    decoration.setBackgroundColor(ColorConstants.white);
    decoration.setScale(SCALE, SCALE);
    decoration.setTemplate(to0PointList);
    return decoration;
  }

  static
  {
    //                                   /
    // Construct point list similar to: O-
    //                                   \
    toNPointList = new PointList();
    int i = -60;
    toNPointList.addPoint(i, 0);
    toNPointList.addPoint(i - 1, 6);
    toNPointList.addPoint(i - 4, 12);
    toNPointList.addPoint(i - 8, 16);
    toNPointList.addPoint(i - 14, 19);
    toNPointList.addPoint(i - 20, 20);
    toNPointList.addPoint(i - 26, 19);
    toNPointList.addPoint(i - 32, 16);
    toNPointList.addPoint(i - 36, 12);
    toNPointList.addPoint(i - 39, 6);
    toNPointList.addPoint(i - 40, 0);
    toNPointList.addPoint(i - 39, -6);
    toNPointList.addPoint(i - 36, -12);
    toNPointList.addPoint(i - 32, -16);
    toNPointList.addPoint(i - 26, -19);
    toNPointList.addPoint(i - 20, -20);
    toNPointList.addPoint(i - 14, -19);
    toNPointList.addPoint(i - 8, -16);
    toNPointList.addPoint(i - 4, -12);
    toNPointList.addPoint(i - 1, -6);
    toNPointList.addPoint(i, 0);
    toNPointList.addPoint(0, -30);
    toNPointList.addPoint(i, 0);
    toNPointList.addPoint(0, 30);
    toNPointList.addPoint(i, 0);

    //                                   
    // Construct point list similar to: O
    //                                   
    to0PointList = new PointList();
    i = -40;
    to0PointList.addPoint(i, 0);
    to0PointList.addPoint(i - 1, 6);
    to0PointList.addPoint(i - 4, 12);
    to0PointList.addPoint(i - 8, 16);
    to0PointList.addPoint(i - 14, 19);
    to0PointList.addPoint(i - 20, 20);
    to0PointList.addPoint(i - 26, 19);
    to0PointList.addPoint(i - 32, 16);
    to0PointList.addPoint(i - 36, 12);
    to0PointList.addPoint(i - 39, 6);
    to0PointList.addPoint(i - 40, 0);
    to0PointList.addPoint(i - 39, -6);
    to0PointList.addPoint(i - 36, -12);
    to0PointList.addPoint(i - 32, -16);
    to0PointList.addPoint(i - 26, -19);
    to0PointList.addPoint(i - 20, -20);
    to0PointList.addPoint(i - 14, -19);
    to0PointList.addPoint(i - 8, -16);
    to0PointList.addPoint(i - 4, -12);
    to0PointList.addPoint(i - 1, -6);
    to0PointList.addPoint(i, 0);

    //                                   
    // Construct point list similar to: |
    //                                   
    to1PointList = new PointList();
    i = -40;
    to1PointList.addPoint(i - 20, 20);
    to1PointList.addPoint(i - 20, -20);
  }}
