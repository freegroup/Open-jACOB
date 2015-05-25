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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;

import de.tif.jacob.designer.actions.ShowJacobFormEditorAction;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UIFormGroupModel;
import de.tif.jacob.designer.model.UIFormModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIIFormContainer;
import de.tif.jacob.designer.util.ModelTransfer;


public class TreeFormGroupObject extends TreeParent implements DblClickObject, ITreeFormContainerObject
{

  private final UIDomainModel  domain;
	public TreeFormGroupObject(TreeViewer viewer,TreeParent parent, JacobModel jacob, UIDomainModel domain, UIFormGroupModel group) 
	{
		super(viewer, parent, jacob, group, group.getName());

    this.domain = domain;
    
		jacobModel.addPropertyChangeListener(this);
	}

  public void addChildren()
  {
    for(UIFormModel form: getFormGroupModel().getFormModels())
    {
      TreeObject formNode = new TreeLinkedFormObject(viewer, this, jacobModel, getFormGroupModel(), form);
      addChild(formNode);
    }
  }
  

  public void dispose()
  {
		jacobModel.removePropertyChangeListener(this);
		
    super.dispose();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#sortingEnabled()
   */
  public boolean sortingEnabled()
  {
    return false;
  }

  public final String getLabel()
  {
    return getModel().getExtendedDescriptionLabel();
  }
  
  public String getName() 
	{
		return getFormGroupModel().getName();
	}

  /** 
	 *  Falls von aussen der Name geändert wird (das Label des TreeItem)
	 *  dann muss dies an dem Model nachgezogen werden (DirectEdit von TreeItems)
	 * 
	 */
	public void setName(String name) throws Exception
	{
	  getFormGroupModel().setName(name);
//  super.setName(name); will be done by the firePRopertyChange event of the model
	}

	public UIFormGroupModel getFormGroupModel()
  {
  	return (UIFormGroupModel)model;
  }
  
  public UIIFormContainer getFormContainerModel()
  {
    return (UIFormGroupModel)model;
  }

  public void propertyChange(PropertyChangeEvent ev)
	{
    super.propertyChange(ev);

    if(ev.getPropertyName()==ObjectModel.PROPERTY_TABLE_CREATED)
		  return;
		if(ev.getPropertyName()==ObjectModel.PROPERTY_ALIAS_CREATED)
		  return;
		if(ev.getPropertyName()==ObjectModel.PROPERTY_BROWSER_CREATED)
		  return;
		if(ev.getPropertyName()==ObjectModel.PROPERTY_FIELD_ADDED)
		  return;
	  refreshVisual(true);
	}

	public void dblClick()
  {
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