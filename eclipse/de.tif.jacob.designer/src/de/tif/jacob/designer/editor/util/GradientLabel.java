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
 * Created on 09.11.2005
 *
 */
package de.tif.jacob.designer.editor.util;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Image;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.Constants;

public class GradientLabel extends Label
{
	Insets INSETS = new Insets(2,8,2,8);
	
  public GradientLabel(Image i) {
		super(i);
    setOpaque(false);
	}

	public GradientLabel(String s) {
		super(s);
    setOpaque(false);
	}

	public GradientLabel()
  {
    setOpaque(false);
  }
  
	public Insets getInsets() {
		return INSETS;
	}
	
	protected void paintFigure(Graphics graphics) 
  {
    graphics.pushState();
    graphics.setForegroundColor(Constants.COLOR_PANE);
    graphics.setBackgroundColor(Constants.COLOR_HEADER);
//    graphics.setForegroundColor(Constants.COLOR_HEADER);
//    graphics.setBackgroundColor(Constants.COLOR_PANE);
    graphics.fillGradient(getBounds(),true);
    graphics.popState();
    graphics.setForegroundColor(Constants.COLOR_FONT);
    super.paintFigure(graphics);
  }
}
