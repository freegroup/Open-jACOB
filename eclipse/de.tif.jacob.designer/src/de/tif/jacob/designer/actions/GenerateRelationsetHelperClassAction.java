/*
   This file is part of Open-jACOB
   Copyright (C) 2005-2010 Andreas Herz | FreeGroup

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; version 2 of the License.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License     
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
   USA

*/
package de.tif.jacob.designer.actions;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.editpart.CaptionEditPart;
import de.tif.jacob.designer.jobs.GenerateBrowserClassesJob;
import de.tif.jacob.designer.jobs.GenerateI18NJob;
import de.tif.jacob.designer.jobs.GenerateRelationsetClassesJob;
import de.tif.jacob.designer.jobs.GenerateRolesJob;
import de.tif.jacob.designer.jobs.GenerateTableClassesJob;
import de.tif.jacob.designer.model.UICaptionModel;
import de.tif.jacob.designer.util.SimpleLogger;

/**
 * 
 */
public class GenerateRelationsetHelperClassAction implements IWorkbenchWindowActionDelegate
{
	
	/**
	 * 
	 */
	public GenerateRelationsetHelperClassAction() 
	{
	}

	/**
	 * 
	 * 
	 * @param action 
	 */
	public void run(IAction action) 
	{
  	 try
     {
        new GenerateRelationsetClassesJob().schedule();
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
	}

	/**
	 * @param action 
	 * @param selection 
	 */
	public void selectionChanged(IAction action, ISelection selection) 
	{
	}

	/**
	 * 
	 */
	public void dispose() 
	{
	}

	/**
	 * @param window 
	 */
	public void init(IWorkbenchWindow window) 
	{
		//this.window = window;
	}
}