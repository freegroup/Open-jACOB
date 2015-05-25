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
 * Created on 22.02.2005
 *
 */
package de.tif.jacob.designer.editor.diagram.activity.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 *
 */
public class EndFigure extends Ellipse implements IDiagramElementFigure
{
//  private static Color background = ColorConstants.darkBlue;
  private static Color red        = new Color(null, 255,128,128);

  int borderWidth = 20;
  
  public EndFigure()
  {
  }
  
  public void setText(String text)
  {
  }
  
  public Label getLabel()
  {
    return new Label();
  }
  
  public void paintFigure(Graphics g) 
  {
  	Rectangle r = getBounds().getCopy();

  	r.resize(-2,-2);
  	r.translate(1,1);
    g.setBackgroundColor(ColorConstants.white);
  	g.fillOval(r);
  	g.setForegroundColor(ColorConstants.gray);
  	g.drawOval(r);

  	r.resize(-borderWidth, -borderWidth);
  	r.translate(borderWidth/2, borderWidth/2);
    g.setBackgroundColor(red);
  	g.fillOval(r);
  	g.setForegroundColor(ColorConstants.gray);
  	g.drawOval(r);
  	
  }
}


