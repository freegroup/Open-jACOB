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
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowExternalFormEditorAction;
import de.tif.jacob.designer.actions.ShowHtmlFormEditorAction;
import de.tif.jacob.designer.actions.ShowJacobFormEditorAction;
import de.tif.jacob.designer.actions.ShowOwnDrawFormEditorAction;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UIExternalFormModel;
import de.tif.jacob.designer.model.UIFormGroupModel;
import de.tif.jacob.designer.model.UIFormModel;
import de.tif.jacob.designer.model.UIHtmlFormModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIIFormContainer;
import de.tif.jacob.designer.model.UIMutableFormModel;
import de.tif.jacob.designer.views.applicationoutline.dnd.DragListener;


public class TreeLinkedFormObject  extends TreeModelObject implements DblClickObject
{
	private final UIIFormContainer  container;

	public TreeLinkedFormObject(TreeViewer viewer, TreeParent parent, JacobModel jacob,UIIFormContainer container,  UIFormModel form) 
	{
		super(viewer, parent, jacob,form, form.getName());
		
    this.container = container;
		getJacobModel().addPropertyChangeListener(this);
	}

  
  public void dispose()
  {
    getJacobModel().removePropertyChangeListener(this);
    super.dispose();
  }


  public final String getLabel()
  {
    return getModel().getExtendedDescriptionLabel();
  }
  
  public String getName() 
	{
		return getFormModel().getName();
	}

  /** 
	 *  Falls von aussen der Name geändert wird (das Label des TreeItem)
	 *  dann muss dies an dem Model nachgezogen werden (DirectEdit von TreeItems)
	 * 
	 */
	public void setName(String name) throws Exception
	{
	  getFormModel().setName(name);
//  super.setName(name); will be done by the firePRopertyChange event of the model
	}

	public UIFormModel getFormModel()
  {
  	return (UIFormModel)model;
  }
  
  public UIIFormContainer getFormContainer()
  {
    return container;
  } 
  
	public void propertyChange(PropertyChangeEvent ev)
	{
    super.propertyChange(ev);

    if(ev.getPropertyName()==ObjectModel.PROPERTY_TABLE_CREATED)
      return;
    if(ev.getPropertyName()==ObjectModel.PROPERTY_TABLE_CHANGED)
      return;
		if(ev.getPropertyName()==ObjectModel.PROPERTY_ALIAS_CREATED)
		  return;
		if(ev.getPropertyName()==ObjectModel.PROPERTY_BROWSER_CREATED)
		  return;
		if(ev.getPropertyName()==ObjectModel.PROPERTY_JACOBMODEL_CLOSED)
		  return;
		
	  refreshVisual();
	}

	public void dblClick()
  {
    if(getModel() instanceof UIJacobFormModel)
    {
  	  new ShowJacobFormEditorAction()
      {
        public UIJacobFormModel getFormModel()
        {
          return (UIJacobFormModel)model;
        }
      }.run(null);
    }
    else if(getModel() instanceof UIMutableFormModel)
    {
      new ShowOwnDrawFormEditorAction()
      {
        public UIMutableFormModel getFormModel()
        {
          return (UIMutableFormModel)model;
        }
      }.run(null);
    }
    else if(getModel() instanceof UIHtmlFormModel)
    {
      new ShowHtmlFormEditorAction()
      {
        public UIHtmlFormModel getFormModel()
        {
          return (UIHtmlFormModel)model;
        }
      }.run(null);
    }
    else
    {
      new ShowExternalFormEditorAction()
      {
        public UIExternalFormModel getFormModel()
        {
          return (UIExternalFormModel)model;
        }
      }.run(null);
    }
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
    // Es wird versucht ein linkedForm per DragDrop zu bewegen
    // -> Es wird seine Position innerhalb der Domain verändert
    //
    event.doit = true;
  }


}