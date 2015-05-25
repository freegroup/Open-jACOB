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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A figure that has a bent corner in the top right hand. Typically used for sticky notes.
 */
public class BentCornerFigure	extends Figure
{

/**
 * The default amount of pixels subtracted from the figure's height and width to determine
 * the size of the corner.
 */
protected static int DEFAULT_CORNER_SIZE = 10;

private int cornerSize;

/**
 * Constructs an empty BentCornerFigure with default background color of 
 * ColorConstants.tooltipBackground and default corner size.
 */
public BentCornerFigure() 
{
	setBackgroundColor(ColorConstants.tooltipBackground);
	setForegroundColor(ColorConstants.tooltipForeground);
	setCornerSize(DEFAULT_CORNER_SIZE);
}

/**
 * Returns the size, in pixels, that the figure should use to draw its bent corner.
 * 
 * @return size of the corner
 */
public int getCornerSize() 
{
	return cornerSize;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics graphics) 
{
	Rectangle rect = getBounds().getCopy();

	graphics.translate(getLocation());

	// fill the note
	PointList outline = new PointList();
	
	outline.addPoint(0, 0);
	outline.addPoint(rect.width - cornerSize, 0);
	outline.addPoint(rect.width - 1, cornerSize);
	outline.addPoint(rect.width - 1, rect.height - 1);
	outline.addPoint(0, rect.height - 1);
	
	graphics.fillPolygon(outline); 
	
	// draw the inner outline
	PointList innerLine = new PointList();
	
	innerLine.addPoint(rect.width - cornerSize - 1, 0);
	innerLine.addPoint(rect.width - cornerSize - 1, cornerSize);
	innerLine.addPoint(rect.width - 1, cornerSize);
	innerLine.addPoint(rect.width - cornerSize - 1, 0);
	innerLine.addPoint(0, 0);
	innerLine.addPoint(0, rect.height - 1);
	innerLine.addPoint(rect.width - 1, rect.height - 1);
	innerLine.addPoint(rect.width - 1, cornerSize);
	
	graphics.drawPolygon(innerLine);
	
	graphics.translate(getLocation().getNegated());
}

/**
 * Sets the size of the figure's corner to the given offset.
 * 
 * @param newSize the new size to use.
 */
public void setCornerSize(int newSize) 
{
	cornerSize = newSize;
}

}