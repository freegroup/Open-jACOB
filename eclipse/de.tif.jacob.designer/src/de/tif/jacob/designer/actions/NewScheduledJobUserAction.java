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
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import de.tif.jacob.designer.actions.dialog.NewScheduledTaskWizard;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ScheduledJobUserModel;
import de.tif.jacob.designer.util.ClassFinder;

/**
 * 
 */
public abstract  class NewScheduledJobUserAction implements IObjectActionDelegate 
{
  
  /**
   * 
   * 
   * @return 
   */
  public abstract JacobModel getJacobModel();
  

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
    if (getJacobModel() != null)
    {
      NewScheduledTaskWizard wizard = new NewScheduledTaskWizard(getJacobModel(),NewScheduledTaskWizard.TYPE_USER);
      // Instantiates the wizard container with the wizard and opens it
      WizardDialog dialog = new WizardDialog(null, wizard);
      dialog.create();
      
			if(dialog.open()==Window.OK)
			{
			  String shortName = wizard.getJobName();
			  String template  = wizard.getTemplateName();
			  ScheduledJobUserModel job = new ScheduledJobUserModel(getJacobModel(),shortName, template+".java");
			  getJacobModel().addElement(job);
			  ClassFinder.createEventHandlerByName(job,true);
			  
			}
    }
  }
	
}
