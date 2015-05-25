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
package de.tif.jacob.designer.views.applicationoutline.action;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.views.applicationoutline.TreeObject;

public final class SetTestRelationsetAction extends de.tif.jacob.designer.actions.SetTestRelationsetAction 
{
  JacobModel model=null;

  public JacobModel getJacobModel()
  {
    return model;
  }
  
  public RelationsetModel getRelationsetModel()
  {
    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider()
    {
      public Image getImage(Object element)
      {
        if(element instanceof RelationsetModel)
          return JacobDesigner.getImage(((ObjectModel)element).getImageName());
        return null;
      }

      public String getText(Object element)
      {
        if(element instanceof RelationsetModel)
          return ((RelationsetModel) element).getName();
        return element.toString();
      }
    });
    List relations = new ArrayList(model.getRelationsetModels());
    relations.add(0,"<<none>>");
    dialog.setElements(relations.toArray());
    
    dialog.create();
    if(dialog.open()==Window.OK)
    {
      if(dialog.getFirstResult() instanceof String)
        return null;
      else
       return (RelationsetModel)dialog.getFirstResult();
    }
    return null;
  }

  /**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) 
	{
    if(((IStructuredSelection)selection).getFirstElement()!=null)
      model =((TreeObject)((IStructuredSelection)selection).getFirstElement()).getJacobModel();
    else 
      model=null;
	}

}
