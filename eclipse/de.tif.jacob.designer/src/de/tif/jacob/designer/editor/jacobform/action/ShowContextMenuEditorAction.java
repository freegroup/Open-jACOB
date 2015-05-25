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
 * Created on 10.01.2005
 *
 */
package de.tif.jacob.designer.editor.jacobform.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import de.tif.jacob.designer.editor.jacobform.editpart.ObjectEditPart;
import de.tif.jacob.designer.model.UIGroupModel;

/**
 *
 */
public final class ShowContextMenuEditorAction extends de.tif.jacob.designer.actions.ShowContextMenuEditorAction
{
  UIGroupModel model;
  
  public UIGroupModel getGroupModel()
  {
    return model;
  }
  
	public void selectionChanged(IAction action, ISelection selection) 
	{
    if(((IStructuredSelection)selection).getFirstElement()!=null)
    {
      ObjectEditPart editPart = (ObjectEditPart)((IStructuredSelection)selection).getFirstElement();
      model = (UIGroupModel)editPart.getModel();
        
    }
    else
      model=null;
	}
}
