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
package de.tif.jacob.designer.editor.diagram.activity.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 *
 */
public class BranchMergeFigure extends RectangleFigure implements IDiagramElementFigure
{
  private static Color background = new Color(null,128,255,128);
	private Color [] colors; 
	private int width = 2;
  
  public BranchMergeFigure()
  {
  }
  
  public void paintFigure(Graphics g) 
  {
  	Rectangle r = getBounds().getCopy();

  	r.resize(-width,-width);
  	r.translate(width,width);
  	PointList points = new PointList();
  	
  	points.addPoint((r.x+r.right())/2,r.y);
  	points.addPoint(r.right(),(r.y+r.bottom())/2);
  	points.addPoint((r.x+r.right())/2,r.bottom());
  	points.addPoint(r.x,(r.y+r.bottom())/2);
  	
  	g.setLineWidth(2);
  	for(int i=0;i<width;i++)
  	{
    	g.setForegroundColor(getColors()[i]);
    	g.setBackgroundColor(getColors()[i]);
    	g.drawPolygon(points);
    	points.translate(-1,-1);
  	}
  	g.setLineWidth(1);
  	g.setForegroundColor(ColorConstants.darkGray);
    g.setBackgroundColor(background);
  	g.fillPolygon(points);
  	g.drawPolygon(points);
  }
  
  public Label getLabel()
  {
    return new Label();
  }

  private Color [] getColors()
	{
		if(colors == null)
			createColors();
		return colors;
	}
	
	private void createColors()
	{
		Color ref1 = ColorConstants.lightGray;
		Color ref2 = ColorConstants.gray;
		colors = new Color[width];
		for(int i = 0; i<width; ++i)
			colors[width-1-i] = new Color(null, ref2.getRed()+ (ref1.getRed()-ref2.getRed())*i/(width-1),
				ref2.getGreen()+ (ref1.getGreen()-ref2.getGreen())*i/(width-1),
				ref2.getBlue()+ (ref1.getBlue()-ref2.getBlue())*i/(width-1));
	}
	
	
  public void setText(String text)
  {
  }
}
