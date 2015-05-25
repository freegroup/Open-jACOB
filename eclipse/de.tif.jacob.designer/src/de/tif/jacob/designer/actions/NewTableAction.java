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
import de.tif.jacob.designer.editor.table.JacobTableEditor;
import de.tif.jacob.designer.editor.table.JacobTableEditorInput;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.PhysicalDataModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * 
 */
public  class NewTableAction extends AbstractNotifyAction
{
	
	/**
	 * 
	 */
	private TableAliasModel createdTableAlias;
	
  /**
   * 
   */
  protected PhysicalDataModel datasource;
  
  /**
   * 
   */
  protected String            newTableName;
  


  /**
   * 
   */
  public NewTableAction()
  {
  }

  /**
   * 
   * 
   * @param datasource 
   */
  public NewTableAction(PhysicalDataModel datasource)
  {
    this.datasource = datasource;
  }
  
  /**
   * 
   */
  public void init()
  {
    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    InputDialog dialog = new InputDialog(shell, "Create Table", "Enter the name for the new table",
        "newTable",
        new IInputValidator()
        {
          public String isValid(String newText)
          {
            if(!StringUtils.containsOnly(newText,"abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_".toCharArray()))
              return "Invalid character in table name.";

            if(datasource.getJacobModel().getTableModel(newText)!=null)
              return "Table already exists. Please use another name,";
            
            return null;
          }
        });
    if(dialog.open()==Window.OK)
       newTableName = dialog.getValue();
  }

  
  /**
   * 
   * 
   * @return 
   */
  public boolean openEditor()
  {
    return true;
  }
  
  
  
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
	public void run(IAction action)
  {
    // reset the previous created tablealias. The object of this action is a singlton. In
    // this case it can happen, that this variable is set from a previous execution of this action
    //
    createdTableAlias = null;
    init();
    if (datasource != null)
    {
      if(newTableName!=null)
      {
	      TableModel table = new TableModel(datasource,newTableName);
	      table.getJacobModel().addElement(table);
	      
        // find a default name for the table alias.
        //
        int i=2;
        String defaultName = table.getName();
        while(table.getJacobModel().getTableAliasModel(defaultName)!=null)
          defaultName = table.getName()+ i++;
        
        createdTableAlias = new TableAliasModel(table,defaultName);
        table.getJacobModel().addElement(getCreatedTableAlias());

        // create a new browser for the table alias
        //
        i=2;
        defaultName = getCreatedTableAlias().getName()+"Browser";
        while(getCreatedTableAlias().getJacobModel().getBrowserModel(defaultName)!=null)
          defaultName = getCreatedTableAlias().getName()+(i++)+"Browser";
        
        BrowserModel browser = new BrowserModel(getCreatedTableAlias(),defaultName);
        table.getJacobModel().addElement(browser);
        
        if(openEditor()==true)
        {
	        try
		      {
		        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
		        JacobTableEditorInput editorInput = new JacobTableEditorInput( table);
		        JacobTableEditor editor = (JacobTableEditor) page.openEditor(editorInput, JacobTableEditor.ID, true);
		        editor.navigateToField((FieldModel)table.getFieldModels().get(0));
		      }
		      catch (PartInitException e)
		      {
		        JacobDesigner.showException(e);
		      }
        }
        setSucceed(action,true);
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

  /**
   * 
   * 
   * @return 
   */
  public TableAliasModel getCreatedTableAlias()
  {
    return createdTableAlias;
  }
}
