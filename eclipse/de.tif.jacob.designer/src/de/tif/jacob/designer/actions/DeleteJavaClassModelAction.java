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

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JavaClassModel;
import de.tif.jacob.designer.util.ClassFinder;

/**
 * 
 */
public abstract class DeleteJavaClassModelAction implements IObjectActionDelegate 
{
  
  /**
   * 
   * 
   * @return 
   */
  public abstract JavaClassModel getJavaClassModel();
  
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
	  try
    {
      JavaClassModel model = getJavaClassModel();
      IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
      if(model!=null)
      {
  	    if(MessageDialog.openConfirm(null,"Delete Java class","Delete the class ["+model.getHookClassName()+"] ?"))
  	      ClassFinder.deleteClass(model.getHookClassName(), myJavaProject);
      }
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
	}
}
