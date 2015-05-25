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
import org.eclipse.jface.dialogs.Dialog;
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
import de.tif.jacob.designer.actions.dialog.RefactorRenameTableDialog;
import de.tif.jacob.designer.editor.alias.JacobAliasEditor;
import de.tif.jacob.designer.editor.alias.JacobAliasEditorInput;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * 
 */
public abstract  class RefactorRenameTableAction implements IObjectActionDelegate 
{
  
  /**
   * 
   * 
   * @return 
   */
  public abstract TableModel getTableModel();
  
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
	  final TableModel table = getTableModel();
    if (table != null)
    {
        // find a default name for the table alias
        //
        String defaultName = table.getName();
        
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        RefactorRenameTableDialog dialog = new RefactorRenameTableDialog(shell,table);
        dialog.create();
        if(dialog.open()==Window.OK)
        {
          String oldName = table.getName();
          table.setName(dialog.getValue());
          if(dialog.renameStandartAlias()==true)
          {
            TableAliasModel alias = table.getJacobModel().getTableAliasModel(oldName);
            if(alias!=null)
              alias.setName(dialog.getValue());
          }
          if(dialog.renameStandartBrowser()==true)
          {
            BrowserModel browser = table.getJacobModel().getBrowserModel(oldName+"Browser");
            if(browser!=null)
              browser.setName(dialog.getValue());
          }
        }
    }
  }
}
