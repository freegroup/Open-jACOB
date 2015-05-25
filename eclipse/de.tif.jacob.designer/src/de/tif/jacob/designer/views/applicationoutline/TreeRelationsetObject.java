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
 * Created on 09.12.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.views.applicationoutline;

import java.beans.PropertyChangeEvent;

import org.eclipse.jface.viewers.TreeViewer;

import de.tif.jacob.designer.actions.ShowRelationsetEditorAction;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TreeRelationsetObject extends TreeModelObject implements DblClickObject
{
	protected TreeRelationsetObject(TreeViewer viewer, TreeParent parent, JacobModel jacob, RelationsetModel model) 
	{
		super(viewer, parent,  jacob, model,  model.getName());
	}
  
  public RelationsetModel getRelationsetModel()
  {
    return (RelationsetModel)model;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#sortingEnabled()
   */
  public boolean sortingEnabled()
  {
    return true;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#getSortingString()
   */
  public String getSortingString()
  {
    return getRelationsetModel().getName();
  }

  public final String getLabel()
  {
    return getModel().getExtendedDescriptionLabel();
  }
  
  public boolean hasDirectEdit()
  {
    return true;
  }

  public void propertyChange(PropertyChangeEvent ev)
  {
    super.propertyChange(ev);

    if(ev.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
		  refreshVisual();
    if(ev.getPropertyName() == ObjectModel.PROPERTY_ELEMENT_ADDED)
		  refreshVisual();
    if(ev.getPropertyName() == ObjectModel.PROPERTY_ELEMENT_REMOVED)
		  refreshVisual();
  }
  
	public String getName() 
	{
		return getRelationsetModel().getName();
	}

	public void setName(String name) throws Exception
	{
	  getRelationsetModel().setName(name);
//  super.setName(name); will be done by the firePRopertyChange event of the model
	}

  public void dblClick()
  {
    new ShowRelationsetEditorAction()
    {
      public RelationsetModel getRelationsetModel()
      {
        return (RelationsetModel)model;
      }
    }.run(null);
  }

}


