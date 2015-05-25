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
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.DragEditPartsTracker;

import de.tif.jacob.designer.editor.jacobform.editpolicies.GroupDeletePolicy;
import de.tif.jacob.designer.editor.jacobform.editpolicies.LayoutEditPolicyTabContainerImpl;
import de.tif.jacob.designer.editor.jacobform.figures.TabContainerFigure;
import de.tif.jacob.designer.editor.jacobform.figures.TabPanesFigure;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.UIFormElementModel;
import de.tif.jacob.designer.model.UIGroupContainer;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UITabContainerModel;
import de.tif.jacob.designer.model.UITabPanesModel;

public class TabPanesEditPart extends ObjectEditPart implements GraphicalEditPart
{
  public IFigure createFigure()
  {
    return new TabPanesFigure();
  }

  
	public void propertyChange(PropertyChangeEvent ev)
	{
    if(ev.getPropertyName()==ObjectModel.PROPERTY_VISIBILITY_CHANGED)
    {
      Iterator iter = getModelChildren().iterator();
      while(iter.hasNext())
      {
        ObjectModel model = (ObjectModel)iter.next();
        model.firePropertyChange(ObjectModel.PROPERTY_VISIBILITY_CHANGED,ev.getOldValue(),ev.getNewValue());
      }
    }
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_ADDED)
      refreshChildren();
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED)
		  refreshChildren();
    else
      super.propertyChange(ev);
	}

	public List getModelChildren()
	{
    UITabPanesModel model = (UITabPanesModel) getModel();
		return model.getElements();
	}
  
  public void refreshVisuals()
  {
   ObjectEditPart parent = (ObjectEditPart)getParent();
   IFigure parentContentPane = parent.getContentPane();
   if(parentContentPane!=null)
   {
      LayoutManager parentLayoutManager = parentContentPane.getLayoutManager();
      parentLayoutManager.setConstraint(getFigure(), BorderLayout.CENTER);
      parentContentPane.revalidate();
   }
  }
  /**
   * Das TabPane ist nicht löschbar/selectierbar. Dies wird an den TabContainer
   * weiter geleitet.
   */
  public DragTracker getDragTracker(Request request)
  {
     return new DragEditPartsTracker(this.getParent());
  }
   
}
