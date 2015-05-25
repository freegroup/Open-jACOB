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
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowDomainEditorAction;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UIFormGroupModel;
import de.tif.jacob.designer.model.UIFormModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIIFormContainer;
import de.tif.jacob.designer.views.applicationoutline.dnd.DragListener;


public class TreeDomainObject extends TreeParent implements DblClickObject,	PropertyChangeListener, IAdaptable, ITreeFormContainerObject
{
  public TreeDomainObject(TreeViewer viewer,TreeParent parent,  JacobModel jacob, UIDomainModel domain) 
	{
		super(viewer, parent,jacob,domain,  domain.getName());
	}

	public void addChildren()
	{
    for(UIFormModel form: getDomainModel().getFormModels())
    {
      TreeObject formNode = new TreeLinkedFormObject(viewer, this, jacobModel, getDomainModel(), form);
      addChild(formNode);
    }
    
    for(UIFormGroupModel group: getDomainModel().getFormGroupModels())
    {
      TreeFormGroupObject groupNode = new TreeFormGroupObject(viewer, this, jacobModel, getDomainModel(), group);
      addChild(groupNode);
    }
	}
	
  public UIIFormContainer getFormContainerModel()
  {
    return (UIIFormContainer)getModel();
  }

  public UIDomainModel getDomainModel()
  {
    return (UIDomainModel)getModel();
  }
  
  public String getLabel()
  {
    return getModel().getExtendedDescriptionLabel();
  }
  
	public String getName() 
	{
		return getDomainModel().getName();
	}

 
  /** Falls von aussen der Name geändert wird (das Label des TreeItem)
	 * dann muss dies an dem Model nachgezogen werden (DirectEdit von TreeItems)
	 * 
	 */
	public void setName(String name) throws Exception
	{
	  getDomainModel().setName(name);
	}

  
  
	public void propertyChange(PropertyChangeEvent ev)
	{
    super.propertyChange(ev);
		if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED)
		  refreshVisual(true);
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_ADDED)
		  refreshVisual(true);
		// Muss als letztes geprüft werden. Wenn KEINE Elemente hinzugekommen/gelöscht worden sind, dann
		// muss nicht der ganze Baum neu aufgebaut werden. Es reicht dann das Icon und das Label neu zu berechnen
		//
		else if(ev.getSource()==getDomainModel())
		  refreshVisual();
	}

	
	public void dblClick()
  {
	    new ShowDomainEditorAction()
	    {
	      public UIDomainModel getDomainModel()
	      {
	        return TreeDomainObject.this.getDomainModel();
	      }
	    }.run(null);
  }
	
	public boolean hasDirectEdit()
	{
	  return true;
	}
	
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
   */
  public void dragStart(DragSourceEvent event)
  {
    event.doit = true;
  }
}