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
package de.tif.jacob.designer.editor.jacobform.editpart;


import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import de.tif.jacob.designer.editor.jacobform.figures.HeadingFigure;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.UIHeadingModel;
import de.tif.jacob.designer.model.UILabelModel;

public class HeadingEditPart extends LabelEditPart implements IStyleProvider
{
  public HeadingEditPart()
  {
    super(HeadingFigure.class);
  }

  public void consumeStyle(Map<String, Object> style)
  {
    UIHeadingModel model = (UIHeadingModel)getModel();

    Integer width = (Integer)style.get("element.width");
    Integer x = (Integer)style.get("element.x");
    Integer y = (Integer)style.get("element.y");

    if( x!=null && y !=null)
      model.setLocation(new Point(x,y));
    

    if(width!=null)
    {
      Rectangle rect = model.getConstraint();
      rect.width = width;
      model.setConstraint(rect);
    }
    
    super.consumeStyle(style);
  }

  public void provideStyle(Map<String, Object> style, boolean withLocation)
  {
    UIHeadingModel model = (UIHeadingModel)getModel();
    style.put("element.width",model.getConstraint().width);
    
    super.provideStyle(style, withLocation);
  }
}
