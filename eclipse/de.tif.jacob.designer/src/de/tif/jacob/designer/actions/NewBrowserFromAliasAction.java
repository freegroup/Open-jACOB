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
import de.tif.jacob.designer.editor.browser.JacobBrowserEditor;
import de.tif.jacob.designer.editor.browser.JacobBrowserEditorInput;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * 
 */
public abstract  class NewBrowserFromAliasAction implements IObjectActionDelegate 
{
  
  /**
   * 
   * 
   * @return 
   */
  public abstract TableAliasModel getTableAliasModel();
  
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
	  return getTableAliasModel().getJacobModel();
	}
	
	/**
	 * 
	 * 
	 * @param action 
	 */
	public final void run(IAction action)
  {
    if (getTableAliasModel() != null)
    {
        // find a default name for the table alias
        //
        String defaultName = getTableAliasModel().getName()+"Browser";
	      Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	      InputDialog nameDialog = new InputDialog(shell, "Create a browser", "Enter the name for the browser",
	          												defaultName,
													          new IInputValidator()
													          {
													            public String isValid(String newText)
													            {
													              if(!StringUtils.containsOnly(newText,"abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_1234567890".toCharArray()))
													                return "Invalid character in browser name.";

													              if(getJacobModel().getBrowserModel(newText)!=null)
													                return "Browser already exists. Please use another name,";
													              
													              return null;
													            }
													          });

	      nameDialog.create();
	      if(nameDialog.open()==Window.OK)
	      {
	        BrowserModel browser = new BrowserModel(getTableAliasModel(), nameDialog.getValue());
	        getTableAliasModel().getJacobModel().addElement(browser);
	        
	        // open the table alias editor
	        //
	        try
	        {
	          IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	          IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
	          JacobBrowserEditorInput editorInput = new JacobBrowserEditorInput(browser);
	          page.openEditor(editorInput, JacobBrowserEditor.ID, true);
	        }
	        catch (PartInitException e)
	        {
	          JacobDesigner.showException(e);
	        }
	      }
     }
  }
}

