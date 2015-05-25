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
package de.tif.jacob.designer.editor.jacobform.figures;

import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.SchemeBorder.Scheme;
import org.eclipse.swt.graphics.Color;

public class Colors
{
//  public static Color notimplementedColor = new Color(null, 236, 236, 255);
  public static Color implementedColor    = new Color(null, 255, 255, 206);
  public static Color COLOR_PANE          = new Color(null, 238, 238, 238);
  public static Color COLOR_HEADER        = new Color(null, 204, 204, 204);
  public static Color COLOR_BORDER        = new Color(null, 152, 152, 152);
  public static Color COLOR_DISABLEFONT   = new Color(null, 179, 179, 179);
  public static Color COLOR_FONT          = new Color(null, 33,  33,  44);
  
  public static Color COLOR_BORDER_DARK   = new Color(null, 93,  93,  94);
  public static Color COLOR_BORDER_BRIGHT = new Color(null, 152, 152, 152);
  public static final SchemeBorder BORDER= new SchemeBorder(new Scheme(
      new Color[]{Colors.COLOR_BORDER_DARK},
      new Color[]{Colors.COLOR_BORDER_BRIGHT}));
}