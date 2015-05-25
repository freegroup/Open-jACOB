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


import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.editpolicies.HorizontalButtonBarLayoutEditPolicy;
import de.tif.jacob.designer.editor.jacobform.figures.HorizontalButtonBarFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ButtonFigure;
import de.tif.jacob.designer.editor.jacobform.figures.TabFigure;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.UIButtonBarModel;

public class HorizontalButtonBarEditPart extends GroupElementEditPart implements IStyleProvider
{
  public HorizontalButtonBarEditPart()
  {
    super(HorizontalButtonBarFigure.class);
  }

  public void createEditPolicies()
	{
    super.createEditPolicies();
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new HorizontalButtonBarLayoutEditPolicy());
  }
  
  
  public void propertyChange(PropertyChangeEvent ev)
	{
    if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_ADDED)
		  refreshChildren();
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED)
      refreshChildren();
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_CHANGED)
      refreshChildren();
    else
      super.propertyChange(ev);
	}

  public UIButtonBarModel getButtonBarModel()
  {
    return (UIButtonBarModel)getModel();
  }
  
  protected List getModelChildren()
  {
    return getButtonBarModel().getElements();
  }
  
  public void setHighlight(RelationsetModel relationset)
  {
    super.setHighlight(relationset);
  }

  public void consumeStyle(Map<String, Object> style)
  {
    UIButtonBarModel model = getButtonBarModel();

    Integer width = (Integer)style.get("element.width");
    Integer height = (Integer)style.get("element.height");
    Integer x = (Integer)style.get("element.x");
    Integer y = (Integer)style.get("element.y");

    if( x!=null && y !=null)
      model.setLocation(new Point(x,y));
    
    if(height!=null || width!=null)
    {
      Rectangle rect = model.getConstraint();
      if(height!=null)
        rect.height = height;
      if(width!=null)
        rect.width = width;
      model.setConstraint(rect);
    }
  }

  public void provideStyle(Map<String, Object> style, boolean withLocation)
  {
    UIButtonBarModel model = getButtonBarModel();
    style.put("element.height",model.getConstraint().height);
    style.put("element.width",model.getConstraint().width);
    if(withLocation)
    {
      style.put("element.x",model.getConstraint().x);
      style.put("element.y",model.getConstraint().y);
    }
  }
}