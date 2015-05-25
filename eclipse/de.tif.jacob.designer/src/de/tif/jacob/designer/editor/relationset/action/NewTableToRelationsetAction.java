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
package de.tif.jacob.designer.editor.relationset.action;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import de.tif.jacob.designer.actions.AddTableAliasToRelationsetAction;
import de.tif.jacob.designer.actions.NewTableAction;
import de.tif.jacob.designer.editor.relationset.JacobRelationsetEditor;
import de.tif.jacob.designer.editor.relationset.action.dialog.NewTableDialog;
import de.tif.jacob.designer.editor.relationset.command.AddTableAliasCommand;
import de.tif.jacob.designer.editor.relationset.editpart.RelationsetEditPart;
import de.tif.jacob.designer.editor.relationset.figures.RelationsetFigure;
import de.tif.jacob.designer.model.PhysicalDataModel;
import de.tif.jacob.designer.model.RelationsetModel;

public class NewTableToRelationsetAction extends NewTableAction 
{
  RelationsetModel relationset;
  JacobRelationsetEditor editor;
 
  
	public void init() 
	{
    // Falls nur eine Datasource vorhanden ist, braucht man diese in dem
    // CreateDialog auch nicht anzeigen. Es wird dann die normale implementierung
    // der super-Klasse aufgerufen welche nur den Tabellennamen erfragt.
    //
    if(relationset.getJacobModel().getDatasourceModels().size()==1)
    {
      this.datasource   = (PhysicalDataModel)relationset.getJacobModel().getDatasourceModels().get(0);
      this.newTableName = null;
      super.init();
    }
    else
    {
      Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
      NewTableDialog dialog = new NewTableDialog(shell,relationset.getJacobModel());
      if(dialog.open()== Window.OK)
      {
        this.datasource = relationset.getJacobModel().getDatasourceModel(dialog.getDatasourceName());
        this.newTableName = dialog.getTableName();
      }
      else
      {
        // This Action is a singlton. In this case it is important to reset the 
        // members!!! The members can be set from a previous execution.
        this.datasource = null;
        this.newTableName = null;
      }
    }
	}
	
  public boolean openEditor()
  {
    return false;
  }
  	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) 
	{
	  editor = ((JacobRelationsetEditor)targetPart);
	  relationset = editor.getRelationsetModel();
	}

	public void selectionChanged(IAction action, ISelection selection) 
	{
    if(((IStructuredSelection)selection).getFirstElement()!=null)
    {
      RelationsetEditPart editPart = (RelationsetEditPart)((IStructuredSelection)selection).getFirstElement();
      relationset = editPart.getRelationsetModel();
    }
    else
      relationset=null;
	}

	public void run(IAction action) 
	{
		// create the new table and all required components
		//
		super.run(action);

		// Add the new created table alias in this relationset
		//
		if(getCreatedTableAlias()!=null)
		{
			new AddTableAliasCommand(relationset, getCreatedTableAlias(), new Point(RelationsetFigure.LAST_X,RelationsetFigure.LAST_Y)).execute();		
	  }
	}
}
