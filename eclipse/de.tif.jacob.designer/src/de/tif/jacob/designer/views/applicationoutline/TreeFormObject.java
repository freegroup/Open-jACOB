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
import de.tif.jacob.designer.model.UIFormModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.util.ModelTransfer;


public abstract class TreeFormObject extends TreeModelObject implements DblClickObject
{

  public TreeFormObject(TreeViewer viewer, TreeParent parent, JacobModel jacob, ObjectModel model, String name)
  {
    super(viewer, parent, jacob, model, name);
  }

  public void dispose()
  {
		jacobModel.removePropertyChangeListener(this);
		
    super.dispose();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#sortingEnabled()
   */
  public final boolean sortingEnabled()
  {
    return true;
  }


  public final String getLabel()
  {
    return getModel().getExtendedDescriptionLabel();
  }
  
  public final String getName() 
	{
		return getFormModel().getName();
	}

  /** 
	 *  Falls von aussen der Name geändert wird (das Label des TreeItem)
	 *  dann muss dies an dem Model nachgezogen werden (DirectEdit von TreeItems)
	 * 
	 */
	public final void setName(String name) throws Exception
	{
	  getFormModel().setName(name);
//  super.setName(name); will be done by the firePRopertyChange event of the model
	}

	public UIFormModel getFormModel()
  {
  	return (UIFormModel)model;
  }
  
  
	public final boolean hasDirectEdit()
	{
	  return true;
	}

  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
   */
  public final void dragStart(DragSourceEvent event)
  {
    event.doit = true;
  }
  
}