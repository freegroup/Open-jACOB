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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.JacobFormEditor;
import de.tif.jacob.designer.editor.jacobform.JacobFormEditorInput;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIIFormContainer;

/**
 * 
 */
public abstract class NewLinkedJacobFormAction extends AbstractNotifyAction 
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
	 * @return 
	 */
	private JacobModel getJacobModel()
	{
	  return getFormContainerModel().getJacobModel();
	}
	
	/**
	 * 
	 * 
	 * @param action 
	 */
	public void run(IAction action)
  {
    if (getFormContainerModel() != null)
    {
      Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
      InputDialog nameDialog = new InputDialog(shell, "Create a form", "Enter the name for the form",
          												"newForm",
												          new IInputValidator()
												          {
												            public String isValid(String newText)
												            {
												              if(!StringUtils.containsOnly(newText,"abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_".toCharArray()))
												                return "Invalid character in form name.";

												              if(getJacobModel().getFormModel(newText)!=null)
												                return "Form already exists. Please use another name,";
												              
												              return null;
												            }
												          });

      nameDialog.create();
      if(nameDialog.open()==Window.OK)
      {
	      // create the new form and link them to the domain
	      //
	      UIJacobFormModel form = new UIJacobFormModel(getJacobModel(), nameDialog.getValue());
	      getJacobModel().addElement(form);
        getFormContainerModel().addElement(form);
	      
	      // open the new form in the editor
	      //
	      try
	      {
	        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
	        JacobFormEditorInput editorInput = new JacobFormEditorInput(form);
	        page.openEditor(editorInput, JacobFormEditor.ID, true);
	      }
	      catch (PartInitException e)
	      {
	        JacobDesigner.showException(e);
	      }
      }
      else
      {
        setSucceed(action,false);
      }
    }
    else
    {
      setSucceed(action,false);
    }
  }
}
