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

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.util.LaunchAntInExternalVM;

/**
 * 
 */
public class HotdeployJacappAction implements IWorkbenchWindowActionDelegate 
{
//	private IWorkbenchWindow window;
	/**
 * 
 */
	public HotdeployJacappAction() 
	{
	}


  /**
	 * 
	 * 
	 * @param action 
	 */
	public void run(IAction action) 
	{
    // Bei dem Benutzer nachfragen ob auch alle Scripte/JavaClasses gespeicehrt werden
    // sollen. Wenn man dies nicht tut, dann hat man eventuell alte Sourcen im jacapp
    //
    JacobDesigner.getPlugin().getWorkbench().saveAllEditors(true);
	  
	 try
   {
      IRunnableWithProgress operation = new IRunnableWithProgress()
      {
        public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          try
          {
            JacobDesigner.getPlugin().saveCurrentModel();
            IFile f=JacobDesigner.getPlugin().getSelectedProject().getFile("build.xml");
            if (!f.isSynchronized(IResource.DEPTH_ONE))
              f.refreshLocal(IResource.DEPTH_ONE, monitor);
            LaunchAntInExternalVM.launchAntInExternalVM(f,monitor,true,"deploy_configure");
          }
          catch (Exception e)
          {
            JacobDesigner.showException(e);
          }
        }
      };
      new ProgressMonitorDialog(null).run(false, false, operation);
  }
  catch (Exception e)
  {
    JacobDesigner.showException(e);
  }
	 //AntHelper ant = new AntHelper(f);
	 //ant.execute(null);
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
	 */
	public void dispose() 
	{
	}

	/**
	 * 
	 * 
	 * @param window 
	 */
	public void init(IWorkbenchWindow window) 
	{
	//	this.window = window;
	}
}
