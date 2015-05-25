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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.relationset.JacobRelationsetEditor;
import de.tif.jacob.designer.editor.relationset.command.AddTableAliasCommand;
import de.tif.jacob.designer.editor.relationset.editpart.TableAliasEditPart;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.TableAliasModel;

public final class ExpandTableAliasAction  implements IObjectActionDelegate 
{
  TableAliasEditPart editPart;
  
	public void setActivePart(IAction action, IWorkbenchPart targetPart) 
	{
	}

	public void selectionChanged(IAction action, ISelection selection) 
	{
    if(((IStructuredSelection)selection).getFirstElement()!=null)
    {
      editPart = (TableAliasEditPart)((IStructuredSelection)selection).getFirstElement();
    }
    else
      editPart = null;
	}
	
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public final void run(IAction action)
  {
//      editPart.expand();
  }
}

