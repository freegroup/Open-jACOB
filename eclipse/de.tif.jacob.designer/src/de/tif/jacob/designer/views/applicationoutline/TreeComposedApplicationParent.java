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
import java.util.Iterator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.ApplicationModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIDomainModel;


public class TreeComposedApplicationParent extends TreeParent
{
	public TreeComposedApplicationParent(TreeViewer viewer, TreeParent parent, JacobModel jacob) 
	{
		super(viewer,parent, jacob,jacob.getApplicationModel(),  "Composed Application UI");
	}

	public void addChildren()
	{
    ApplicationModel applicationModel = jacobModel.getApplicationModel();
    Iterator domainIter = applicationModel.getDomainModels().iterator();
    while (domainIter.hasNext())
    {
      UIDomainModel domain = (UIDomainModel) domainIter.next();
      TreeParent domainNode = new TreeDomainObject(viewer, this, jacobModel, domain);
      addChild(domainNode);
    }
	}

  public ApplicationModel getApplicationModel()
  {
  	return (ApplicationModel)getModel();
  }
  
  
	public void propertyChange(PropertyChangeEvent ev)
	{
    super.propertyChange(ev);
		if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED)
		{
		  if(ev.getOldValue() instanceof UIDomainModel)
		  {
		    // durch alle Kinder gehen und ein entsprechenden Knoten aushägen
		    //
		    TreeObject[] children = getChildren();
		    for(int i=0 ;i<children.length;i++)
		    {
		      if(children[i] instanceof TreeDomainObject && ((TreeDomainObject)children[i]).getDomainModel()==ev.getOldValue())
		      {
		        children[i].dispose();
		        removeChild(children[i]);
		        break;
		      }
		    }
			  refreshVisual();
		  }
		}
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_ADDED)
		{
		  refreshVisual(true);
		}
	}

  /**
   * No direct edit in the tree
   */
	public boolean hasDirectEdit()
	{
	  return false;
	}
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#getImage()
   */
  public final Image getImage()
  {
    String name = "ComposedApplicationUI.png";
    
    if(error!=null)
      return JacobDesigner.getImage(name,JacobDesigner.DECORATION_ERROR);
    if(warning!=null)
      return JacobDesigner.getImage(name,JacobDesigner.DECORATION_WARNING);
    if(info!=null)
      return JacobDesigner.getImage(name,JacobDesigner.DECORATION_INFO);
    
    return JacobDesigner.getImage(name,JacobDesigner.DECORATION_NONE);
  }
  
}