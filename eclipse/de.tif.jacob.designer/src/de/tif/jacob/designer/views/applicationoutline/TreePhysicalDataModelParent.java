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
 * Created on Sep 3, 2004
 *
 */
package de.tif.jacob.designer.views.applicationoutline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.eclipse.jface.viewers.TreeViewer;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.PhysicalDataModel;

public class TreePhysicalDataModelParent extends TreeParent
{
  public TreePhysicalDataModelParent(TreeViewer viewer, TreeParent parent, JacobModel jacob) 
  {
    super(viewer, parent, jacob, jacob.getPhysicalDataModels(), "Physical Data Models");

    this.jacobModel.addPropertyChangeListener(this);
  }
  
  public void dispose()
  {
    jacobModel.removePropertyChangeListener(this);
    super.dispose();
  }

  public void addChildren()
  {
    List datasources = jacobModel.getDatasourceModels();
    for (int i = 0; i < datasources.size(); i++)
    {
      PhysicalDataModel datasource = (PhysicalDataModel) datasources.get(i);
      TreePhysicalDataModelObject datasourceNode = new TreePhysicalDataModelObject(viewer,this, jacobModel, datasource);
      addChild(datasourceNode);
    }
  }
  
	public void propertyChange(PropertyChangeEvent ev)
	{
    super.propertyChange(ev);

    if(ev.getPropertyName()==ObjectModel.PROPERTY_DATASOURCE_CREATED)
		{
		  PhysicalDataModel model = (PhysicalDataModel)ev.getNewValue();
	    addChild(new TreePhysicalDataModelObject(viewer,this, getJacobModel(),model));
		  refreshVisual();
		}
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_DATASOURCE_DELETED)
		{
		  refreshVisual(true);
		}
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
		{
		  refreshVisual();
		}
	}
}