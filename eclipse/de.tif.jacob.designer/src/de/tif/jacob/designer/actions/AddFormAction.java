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
package de.tif.jacob.designer.actions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UIFormModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIIFormContainer;

/**
 * 
 */
public abstract  class AddFormAction implements IObjectActionDelegate 
{
  /**
   * 
   * 
   * @return 
   */
  public abstract UIIFormContainer getFormContainerModel();
  
	/**
	 * 
	 * 
	 * @param action 
	 * @param targetPart 
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) 
	{
	}

	/**
	 * 
	 * 
	 * @param action 
	 * @param selection 
	 */
	public void selectionChanged(IAction action, ISelection selection) 
	{
	  
	}

	/**
	 * 
	 * 
	 * @param action 
	 */
	public final void run(IAction action)
  {
    if (getFormContainerModel() != null)
    {
      ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider()
          {
        public Image getImage(Object element)
        {
          return JacobDesigner.getImage(((UIFormModel)element).getImageName());
        }

        public String getText(Object element)
        {
          return ((UIFormModel) element).getName();
        }
      });
      List<UIFormModel> forms = new ArrayList<UIFormModel>(getFormContainerModel().getJacobModel().getJacobFormModels());
      forms.addAll(getFormContainerModel().getJacobModel().getExternalFormModels());
      
      dialog.setElements(forms.toArray());
      
      dialog.create();
      if(dialog.open()==Window.OK)
      {
        UIFormModel form= (UIFormModel)dialog.getFirstResult();
        if(form!=null)
        {
          getFormContainerModel().addElement(form);
        }
      }
    }
  }

}
