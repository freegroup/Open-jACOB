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
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.editpolicies.GroupDeletePolicy;
import de.tif.jacob.designer.editor.jacobform.editpolicies.LayoutEditPolicyTabContainerImpl;
import de.tif.jacob.designer.editor.jacobform.editpolicies.TabStripLayoutEditPolicy;
import de.tif.jacob.designer.editor.jacobform.figures.TabFigure;
import de.tif.jacob.designer.editor.jacobform.figures.TabPanesFigure;
import de.tif.jacob.designer.editor.jacobform.figures.TabStripFigure;
import de.tif.jacob.designer.editor.relationset.policy.TableAliasEditPolicy;
import de.tif.jacob.designer.editor.relationset.policy.TableContainerEditPolicy;
import de.tif.jacob.designer.editor.relationset.policy.TableLayoutEditPolicy;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UITabStripModel;

public class TabStripEditPart extends GroupElementEditPart implements GraphicalEditPart
{
  public TabStripEditPart()
  {
    super(TabStripFigure.class);
  }

  public void createEditPolicies()
	{
    super.createEditPolicies();
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new TabStripLayoutEditPolicy());
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

  private UITabStripModel getTabStripModel()
  {
    return (UITabStripModel)getModel();
  }
  
  protected List getModelChildren()
  {
    return getTabStripModel().getElements();
  }
  
  public void setHighlight(RelationsetModel relationset)
  {
    super.setHighlight(relationset);
  }

  public void refreshVisuals()
  {
    ObjectEditPart parent = (ObjectEditPart)getParent();
    IFigure parentContentPane = parent.getContentPane();
    if(parentContentPane!=null)
    {
      LayoutManager parentLayoutManager = parentContentPane.getLayoutManager();
      parentLayoutManager.setConstraint(getFigure(), BorderLayout.TOP);
      parentContentPane.revalidate();
    }
  }
  
  protected void setVisible(UIGroupModel group)
  {
    TabContainerEditPart container = (TabContainerEditPart)getParent();
    container.setVisible(group);
    
    Iterator iter = getChildren().iterator();
    while(iter.hasNext())
    {
      TabEditPart part = (TabEditPart)iter.next();
      ((TabFigure)part.getFigure()).setFont(group==part.getTabModel().getUIGroupModel()?Constants.FONT_NORMAL_BOLD:Constants.FONT_NORMAL);
      
    }
  }
}
