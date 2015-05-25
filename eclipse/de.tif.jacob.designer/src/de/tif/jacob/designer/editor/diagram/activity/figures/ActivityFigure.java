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

import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;

import de.tif.jacob.designer.editor.util.ShadowBorder;

/**
 *
 */
public class ActivityFigure extends Label implements IDiagramElementFigure
{
  private static Color background = new Color(null,128,128,255);
  public ActivityFigure(String name)
  {
    System.out.println("ActivityFigure");
    setOpaque(true); // non-transparent figure
    setBackgroundColor(background);
	  setBorder(new ShadowBorder());
	  setText(name); 
  }
  
  public Label getLabel()
  {
    return this;
  }
}