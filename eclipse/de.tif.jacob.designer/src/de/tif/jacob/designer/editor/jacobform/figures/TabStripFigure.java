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
 * Created on 06.01.2006
 *
 */
package de.tif.jacob.designer.editor.jacobform.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.geometry.Insets;
import de.tif.jacob.designer.editor.Constants;

public class TabStripFigure extends Figure
{
  public TabStripFigure()
  {
    FlowLayout layout = new FlowLayout();
    layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
    layout.setStretchMinorAxis(false);
    layout.setHorizontal(true);
    layout.setMajorSpacing(0);
    layout.setMinorSpacing(0);
    setLayoutManager(layout);
    setOpaque(false);
  }
}
