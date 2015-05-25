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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.exception.DesignerException;
import de.tif.jacob.designer.model.ApplicationModel;
import de.tif.jacob.designer.model.UIDomainModel;

/**
 * 
 */
public abstract class NewDomainAction extends AbstractNotifyAction 
{
  
  /**
   * 
   * 
   * @return 
   */
  public abstract ApplicationModel getApplicationModel();
  
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
	  if(getApplicationModel()!=null)
	  {
	    try
      {
	      Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	      InputDialog dialog = new InputDialog(shell, "Create a Domain", "Enter the name for the domain",
	          												"newDomain",
													          new IInputValidator()
													          {
													            public String isValid(String newText)
													            {
													              if(!StringUtils.containsOnly(newText,"abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_".toCharArray()))
													                return "Invalid character in model name.";

													              if(getApplicationModel().getDomain(newText)!=null)
													                return "Model already exists. Please use another name,";
													              
													              return null;
													            }
													          });
	      dialog.create();
	      if(dialog.open()==Window.OK)
	      {
	        UIDomainModel domain = new UIDomainModel(getApplicationModel().getJacobModel(), dialog.getValue());
	        getApplicationModel().addElement(domain);
          setSucceed(action,true);
	      }
        else
        {
          setSucceed(action,false);
        }
      }
      catch (DesignerException e)
      {
        setSucceed(action,false);
        JacobDesigner.showException(e);
      }
	  }
	}
}
