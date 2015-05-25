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

import de.tif.jacob.designer.actions.ShowExternalFormEditorAction;
import de.tif.jacob.designer.actions.ShowJacobFormEditorAction;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIExternalFormModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.util.ModelTransfer;


public class TreeExternalFormObject extends TreeFormObject
{
	public TreeExternalFormObject(TreeViewer viewer,TreeParent parent, JacobModel jacob, UIExternalFormModel form) 
	{
		super(viewer, parent, jacob, form, form.getName());
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
	  refreshVisual();
	}

  
  public void dblClick()
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