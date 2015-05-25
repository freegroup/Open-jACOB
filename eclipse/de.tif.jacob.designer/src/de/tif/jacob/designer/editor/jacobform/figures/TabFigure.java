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
 * Created on Jul 13, 2004
 */
package de.tif.jacob.designer.editor.jacobform.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure.HighLightState;
import de.tif.jacob.designer.editor.util.ColorUtil;

/**
 */
public class TabFigure extends DecoratedLabelFigure
{
  int cornerSize = 10;

  public TabFigure()
	{
		setFont(Constants.FONT_NORMAL);
		setForegroundColor(Constants.COLOR_FONT);
    setBackgroundColor(ColorUtil.darker( Constants.COLOR_PANE,0.9f));
	}
  
  public Insets getInsets()
  {
    return new Insets(2,2,2,cornerSize);
  }

  protected void paintFigure(Graphics graphics) 
  {
    Rectangle rect = getBounds().getCopy();

    graphics.translate(getLocation());

    // fill the note
    PointList outline = new PointList();
    
    outline.addPoint(0, 0);
    outline.addPoint(rect.width - cornerSize, 0);
    outline.addPoint(rect.width - 1, cornerSize);
    outline.addPoint(rect.width - 1, rect.height );
    outline.addPoint(0, rect.height );
    
    graphics.fillPolygon(outline); 
    // draw the inner outline
    PointList innerLine = new PointList();
    
    innerLine.addPoint(0, 0);
    innerLine.addPoint(0, rect.height );
    innerLine.addPoint(rect.width - 1, rect.height);
    innerLine.addPoint(rect.width - 1, cornerSize);
    innerLine.addPoint(rect.width - 1-cornerSize, 0);
    innerLine.addPoint(0, 0);
    
    graphics.drawPolygon(innerLine);
    graphics.translate(getLocation().getNegated());
    
    super.paintFigure(graphics);
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


  public void setHighlight(HighLightState value)
  {
    setBackgroundColor(ColorUtil.darker( value.getColor(),0.9f));
  }
}