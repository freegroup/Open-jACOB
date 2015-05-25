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
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import de.tif.jacob.designer.editor.jacobform.editpolicies.GroupDeletePolicy;
import de.tif.jacob.designer.editor.jacobform.editpolicies.LayoutEditPolicyTabContainerImpl;
import de.tif.jacob.designer.editor.jacobform.figures.ShadowBorder;
import de.tif.jacob.designer.editor.jacobform.figures.TabContainerFigure;
import de.tif.jacob.designer.editor.jacobform.figures.TabPanesFigure;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.UIDBInformBrowserModel;
import de.tif.jacob.designer.model.UIFormElementModel;
import de.tif.jacob.designer.model.UIGroupContainer;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UITabContainerModel;

public class TabContainerEditPart extends GroupElementEditPart implements GraphicalEditPart, IStyleProvider
{
  public TabContainerEditPart()
  {
    super(TabContainerFigure.class);
  }
  
  public void createEditPolicies()
	{
    super.createEditPolicies();
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new LayoutEditPolicyTabContainerImpl());
  }
  
	public void propertyChange(PropertyChangeEvent ev)
	{
    if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_ADDED)
		  refreshChildren();
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED)
		  refreshChildren();
    else
      super.propertyChange(ev);
	}

  public UITabContainerModel getUITabContainerModel()
  {
    return(UITabContainerModel)getModel();
  }
  
	public List getModelChildren()
	{
		return getUITabContainerModel().getElements();
	}
  
  public void setHighlight(RelationsetModel relationset)
  {
    super.setHighlight(relationset);
  }

  protected void setVisible(UIGroupModel group)
  {
    Iterator iter = getModelChildren().iterator();
    while(iter.hasNext())
    {
      ObjectModel model = (ObjectModel)iter.next();
      model.firePropertyChange(ObjectModel.PROPERTY_VISIBILITY_CHANGED,null,group);
    }
  }

  public void refreshVisuals()
  {
    super.refreshVisuals();
    // Falls der TabContainer keine Tab's enthält wird diesem ein
    // Rahmen verpasst und die Hintergrundfarbe angepasst
    //
    ((TabContainerFigure)getFigure()).setEmpty(getUITabContainerModel().getPanes().size()==0);
  }
  

  public void consumeStyle(Map<String, Object> style)
  {
    UITabContainerModel model = getUITabContainerModel();

    Integer height = (Integer)style.get("element.height");
    Integer width = (Integer)style.get("element.width");
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
    UITabContainerModel model = getUITabContainerModel();

    style.put("element.height",model.getConstraint().height);
    style.put("element.width",model.getConstraint().width);
    if(withLocation)
    {
      style.put("element.x",model.getConstraint().x);
      style.put("element.y",model.getConstraint().y);
    }
  }
}
