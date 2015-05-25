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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.model.ApplicationModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;


public class TreeApplicationParent extends TreeParent
{
	public TreeApplicationParent(TreeViewer viewer, TreeParent parent, JacobModel jacob) 
	{
		super(viewer, parent, jacob,jacob.getApplicationModel(), jacob.getApplicationModel().getLabel());
		
		jacobModel.addPropertyChangeListener(this);
	}

  public void dispose()
  {
    jacobModel.removePropertyChangeListener(this);
    super.dispose();
  }

  public ApplicationModel getApplicationModel()
  {
  	return (ApplicationModel)getModel();
  }
  
	
	public void addChildren()
	{
    TreeParent composedNode = new TreeComposedApplicationParent(viewer,this, jacobModel);
    addChild(composedNode);

    TreeParent componentsNode = new TreeConceptualDataModelParent(viewer,this, jacobModel);
    addChild(componentsNode);
    
    addChild(new TreeFormParent(viewer,this, jacobModel));

    addChild(new TreeSchedulerParent(viewer,this, jacobModel));

    addChild(new TreeEntryPointParent(viewer,this, jacobModel));
        
    addChild(new TreeDiagramParent(viewer, this, jacobModel));

    addChild(new TreeI18NParent(viewer, this, jacobModel));
    
    addChild(new TreeUserRoleParent(viewer, this, jacobModel));
	}
	
	public void propertyChange(PropertyChangeEvent ev)
	{
    super.propertyChange(ev);
		if(ev.getPropertyName()==ObjectModel.PROPERTY_LABEL_CHANGED)
		  refreshVisual();
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_JACOBMODEL_CHANGED)
		  refreshVisual();
	}

  public String getName()
  {
    return getApplicationModel().getLabel();
  }
  
  public void setName(String name)
  {
    getApplicationModel().setLabel(name);
  }
  
  /**
   * No direct edit in the tree
   */
	public boolean hasDirectEdit()
	{
	  return true;
	}
}