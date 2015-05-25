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
 * Created on Aug 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.tif.jacob.designer.editor.jacobform.figures;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import de.tif.jacob.designer.editor.Constants;


/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class HeadingFigure extends LabelFigure
{
	private static final int SPACE=3;
	boolean leftAlign= false;
  public HeadingFigure()
  {
  	super();
  	setFont(Constants.FONT_NORMAL_BOLD);
  }
  
  public void setAlign(int align)
  {
    super.setAlign(align);
    leftAlign = PositionConstants.LEFT==align;
  }

  protected void paintFigure(Graphics graphics) 
  {
    graphics.pushState();
    graphics.setForegroundColor(Colors.COLOR_BORDER_DARK);
    Point center = getBounds().getCenter();
    Point left = getBounds().getLeft();
    Point right = getBounds().getRight();
    right.y=left.y=(center.y-1);
    Dimension dim = new Dimension();
    FigureUtilities.getTextExtents(getText(),getFont(),dim);
    if(leftAlign)
    	left.x = left.x+dim.width+SPACE;
    else
    	right.x = right.x-dim.width-SPACE;
    graphics.drawLine(left, right);
    left.y+=1;
    right.y+=1;
    graphics.setForegroundColor(Colors.COLOR_BORDER_BRIGHT);
    graphics.drawLine(left, right);
    graphics.popState();
    super.paintFigure(graphics);
  }
}