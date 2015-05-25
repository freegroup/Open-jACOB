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
package de.tif.jacob.designer.editor.jacobform.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;


public class ShadowBorder extends AbstractBorder
{
	private int width = 3;
	private Color [] colors; 
	private final Color startColor = ColorConstants.darkGray;
  private final Color endColor;
  
  public ShadowBorder()
  {
    this(ColorConstants.lightGray);
  }
  
  public ShadowBorder(Color backgroundcolor)
  {
    endColor = backgroundcolor;
  }
  
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
		colors = new Color[width];
		for(int i = 0; i<width; ++i)
			colors[width-1-i] = new Color(null, startColor.getRed()+ (endColor.getRed()-startColor.getRed())*i/(width-1),
          startColor.getGreen()+ (endColor.getGreen()-startColor.getGreen())*i/(width-1),
          startColor.getBlue()+ (endColor.getBlue()-startColor.getBlue())*i/(width-1));
	}
	public Insets getInsets(IFigure arg0)
	{
		return new Insets(0,0,width,width);
	}
  
	public void paint(IFigure figure, Graphics graphics, Insets arg2)
	{
		Rectangle bounds = figure.getBounds();
		Rectangle rectExt = bounds.getCropped(arg2);
		Color backg = endColor;//.getParent().getBackgroundColor();
		graphics.setBackgroundColor(backg);
		graphics.fillRectangle(rectExt.x+rectExt.width-width, rectExt.y, width, width);
		graphics.fillRectangle(rectExt.x, rectExt.y+rectExt.height-width, width, width);
		
    
		int lVert = rectExt.height-width-1;
		int lHor =  rectExt.width-width-1;
		Point coinInfDroit = new Point(rectExt.x+rectExt.width-1, rectExt.y+rectExt.height-1);
		Point coinSupDroit = coinInfDroit.getCopy().translate(0,-lVert);
		Point coinInfGauche = coinInfDroit.getCopy().translate(-lHor,0);
		for(int i = 0; i<width;++i)
		{
			graphics.setForegroundColor(getColors()[i]);
			graphics.drawLine(coinInfDroit, coinSupDroit);
			graphics.drawLine(coinInfDroit, coinInfGauche);
			coinInfDroit.translate(-1,-1);
			coinSupDroit.translate(-1,-1);
			coinInfGauche.translate(-1,-1);
		}
    
	}
}
