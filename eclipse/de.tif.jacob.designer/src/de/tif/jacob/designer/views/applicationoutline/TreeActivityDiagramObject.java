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
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.actions.ShowActivityDiagramEditorAction;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.diagram.activity.ActivityDiagramModel;


public class TreeActivityDiagramObject extends TreeModelObject implements DblClickObject
{
	
	public TreeActivityDiagramObject(TreeViewer viewer, TreeParent parent, JacobModel jacob, ActivityDiagramModel diagram) 
	{
		super(viewer, parent, jacob,diagram,  diagram.getName());
	}


  public final String getLabel()
  {
    return getModel().getExtendedDescriptionLabel();
  }
  
  public String getName() 
	{
		return getDiagramModel().getName();
	}

  /** 
	 *  Falls von aussen der Name geändert wird (das Label des TreeItem)
	 *  dann muss dies an dem Model nachgezogen werden (DirectEdit von TreeItems)
	 * 
	 */
	public void setName(String name) throws Exception
	{
	  getDiagramModel().setName(name);
	}

	public ActivityDiagramModel getDiagramModel()
  {
  	return (ActivityDiagramModel)model;
  }
  
  
	public void propertyChange(PropertyChangeEvent ev)
	{
    super.propertyChange(ev);
		if(ev.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
		  refreshVisual();
		if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_ADDED)
		  refreshVisual();
		if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED)
		  refreshVisual();
	}

	public void dblClick()
  {
	  new ShowActivityDiagramEditorAction()
    {
	    public ActivityDiagramModel getActivityDiagramModel()
	    {
	      return (ActivityDiagramModel)model;
	    }
    }.run(null);
  }
	
	public boolean hasDirectEdit()
	{
	  return true;
	}

	public boolean sortingEnabled()
	{
	  return true;
	}
	
}