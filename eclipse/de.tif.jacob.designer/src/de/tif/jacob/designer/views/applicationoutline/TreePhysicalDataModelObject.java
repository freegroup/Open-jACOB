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
 * Created on 03.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.views.applicationoutline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.OverlayIcon;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.PhysicalDataModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TreePhysicalDataModelObject extends TreeParent
{
	public TreePhysicalDataModelObject(TreeViewer viewer, TreeParent parent, JacobModel jacob, PhysicalDataModel datasource) 
	{
		super(viewer, parent, jacob,datasource,  datasource.getName());
		
		this.jacobModel.addPropertyChangeListener(this);
	}

  public void dispose()
  {
    jacobModel.removePropertyChangeListener(this);
    super.dispose();
  }
  
  public void addChildren()
  {
    List tables = getDatasourceModel().getTableModels();
    for (int j = 0; j < tables.size(); j++)
    {
      TableModel table = (TableModel) tables.get(j);
      TreeTableParent tableNode = new TreeTableParent(viewer, this, jacobModel, table);
      addChild(tableNode);
    }
  }
  
  public PhysicalDataModel getDatasourceModel()
  {
    return (PhysicalDataModel)getModel();
  }
  

	public void propertyChange(PropertyChangeEvent ev)
	{
    super.propertyChange(ev);

    if(ev.getPropertyName()==ObjectModel.PROPERTY_TABLE_CREATED)
		{
		  TableModel table = (TableModel)ev.getNewValue();
		  if(table.getDatasourceModel()==model)
		  {
		    addChild(new TreeTableParent(viewer,this, getJacobModel(),table));
			  refreshVisual();
		  }
		}
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_TABLE_DELETED)
		{
		  refreshVisual(true);
		}
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_ADDED)
    {
      refreshVisual(true);
    }
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED)
    {
      refreshVisual(true);
    }
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
		{
		  refreshVisual();
		}
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_CHANGED && ev.getSource() == getModel())
    {
      refreshVisual();
    }
	}


	public String getName() 
	{
		return getDatasourceModel().getName();
	}


  /** 
	 *  Falls von aussen der Name geändert wird (das Label des TreeItem)
	 *  dann muss dies an dem Model nachgezogen werden (DirectEdit von TreeItems)
	 * 
	 */
	public void setName(String name) throws Exception
	{
	  getDatasourceModel().setName(name);
//  super.setName(name); will be done by the firePRopertyChange event of the model
	}

	/**
	 * No direct edit in the tree
	 */
	public boolean hasDirectEdit()
	{
	  return true;
	}
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#sortingEnabled()
   */
  public boolean sortingEnabled()
  {
    return true;
  }
}
