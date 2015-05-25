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
package de.tif.jacob.designer.editor.util;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import de.tif.jacob.designer.editor.Constants;


public class ShadowBorder extends AbstractBorder
{
	private int width = 4;
	private Color [] colors; 
	
	public void setWidth(int width)
	{
		this.width = width;
		colors = null;
	}
  
	private Color [] getColors()
	{
		if(colors == null)
			createColors();
		return colors;
	}
  
	private void createColors()
	{
		Color ref1 = Constants.COLOR_WORKAREA;
		Color ref2 = Constants.COLOR_BORDER_DARK;
		colors = new Color[width];
		for(int i = 0; i<width; ++i)
			colors[width-1-i] = new Color(null, ref2.getRed()+ (ref1.getRed()-ref2.getRed())*i/(width-1),
				ref2.getGreen()+ (ref1.getGreen()-ref2.getGreen())*i/(width-1),
				ref2.getBlue()+ (ref1.getBlue()-ref2.getBlue())*i/(width-1));
	}
	public Insets getInsets(IFigure arg0)
	{
		return new Insets(0,0,width,width);
	}
  
	public void paint(IFigure figure, Graphics graphics, Insets arg2)
	{
		Rectangle bounds = figure.getBounds();
		Rectangle rectExt = bounds.getCropped(arg2);

		graphics.setBackgroundColor(Constants.COLOR_WORKAREA);
		graphics.fillRectangle(rectExt.x+rectExt.width-width, rectExt.y, width, width);
		graphics.fillRectangle(rectExt.x, rectExt.y+rectExt.height-width, width, width);
		
    
		int lVert = rectExt.height-width-1;
		int lHor =  rectExt.width-width-1;
		Point bottomRight  = new Point(rectExt.x+rectExt.width-1, rectExt.y+rectExt.height-1);
		Point topRight = bottomRight.getCopy().translate(0,-lVert);
		Point bottomLeft = bottomRight.getCopy().translate(-lHor,0);

		graphics.setForegroundColor(Constants.COLOR_HEADER);
		graphics.drawLine(bounds.getTopLeft(), bounds.getBottomLeft().getTranslated(0,-width));

		for(int i = 0; i<width;++i)
		{
			graphics.setForegroundColor(getColors()[i]);
			graphics.drawLine(bottomRight, topRight);
			graphics.drawLine(bottomRight, bottomLeft);
      bottomRight.translate(-1,-1);
      topRight.translate(-1,-1);
      bottomLeft.translate(-1,-1);
		}
    
	}
}
