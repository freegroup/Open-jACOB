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
 * Created on 15.07.2005
 *
 */
package de.tif.jacob.designer.editor.relationset.action;

import java.util.List;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.designer.actions.NewTableAction;
import de.tif.jacob.designer.editor.relationset.JacobRelationsetEditor;
import de.tif.jacob.designer.editor.relationset.action.dialog.NewTableDialog;
import de.tif.jacob.designer.editor.relationset.editpart.RelationsetEditPart;
import de.tif.jacob.designer.model.PhysicalDataModel;
import de.tif.jacob.designer.model.RelationsetModel;

/**
 *
 */
public class AddTableAliasFromNewTableAction extends NewTableAction
{
  RelationsetModel relationset;
  
	public void setActivePart(IAction action, IWorkbenchPart targetPart) 
	{
	  JacobRelationsetEditor editor = ((JacobRelationsetEditor)targetPart);
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
  /* 
   * @see de.tif.jacob.designer.actions.NewTableAction#getDatasourceModel()
   */
  public void init()
  {
    List models = relationset.getJacobModel().getDatasourceModels();
    if(models.size()==1)
      datasource= (PhysicalDataModel)relationset.getJacobModel().getDatasourceModels().get(0);
    else
      datasource = null;
    super.init();
  }
}
