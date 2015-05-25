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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.designer.editor.jacobform.JacobFormEditor;
import de.tif.jacob.designer.editor.jacobform.JacobFormEditorInput;
import de.tif.jacob.designer.model.UIFormModel;
import de.tif.jacob.designer.model.UIJacobFormModel;

/**
 * 
 */
public abstract class ShowJacobFormEditorAction implements IObjectActionDelegate 
{
  
  /**
   * 
   * 
   * @return 
   */
  public abstract UIJacobFormModel getFormModel();
  
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
      // open the new form in the editor
      //
      try
      {
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
        JacobFormEditorInput editorInput = new JacobFormEditorInput(getFormModel());
        page.openEditor(editorInput, JacobFormEditor.ID, true);
      }
      catch (PartInitException e)
      {
        e.printStackTrace();
      }
  }
}
