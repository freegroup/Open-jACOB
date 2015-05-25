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
package de.tif.jacob.blooper.dialogs;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import de.tif.jacob.blooper.BlooperPlugin;
import de.tif.jacob.blooper.model.Application;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "txt". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */
public class ConnectBugtrackerWizard extends Wizard implements INewWizard
{
	private ApplicationSelectionPage appPage;
	private ConnectionPage           connectionPage;
	private IProject                 project;
  
  String serverUrl = "";
  String username  = "";
  String password  = "";
  
	/**
	 * 
	 * Constructor for HolidayMainWizard.
	 * @param project 
	 */
	public ConnectBugtrackerWizard(IProject project)
	{
		super();
    this.project = project;
//    setDefaultPageImageDescriptor(JacobDesigner.getImageDescriptor("wizard_group.gif"));
	}
	
	public void addPages()
	{
    connectionPage = new ConnectionPage();
    appPage        = new ApplicationSelectionPage();
		addPage(connectionPage);
    addPage(appPage);
	}
	
	
	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) 
	{
	}

	public boolean canFinish()
	{
		return serverUrl.length()>0 && username.length()>0 && getApplicationName().length()>0;
	}
	
	public boolean performFinish() 
	{
    try
    {
      project.setPersistentProperty(BlooperPlugin.PROPERTY_CONNECTED,"true");
      project.setPersistentProperty(BlooperPlugin.PROPERTY_PASSWORD,password);
      project.setPersistentProperty(BlooperPlugin.PROPERTY_URL,serverUrl);
      project.setPersistentProperty(BlooperPlugin.PROPERTY_USER,username);
      project.setPersistentProperty(BlooperPlugin.PROPERTY_APPLICATION,getApplicationName());
      Application.create(project, getApplicationName());
    }
    catch (Exception e)
    {
      try
      {
        project.setPersistentProperty(BlooperPlugin.PROPERTY_CONNECTED,null);
        project.setPersistentProperty(BlooperPlugin.PROPERTY_PASSWORD,null);
        project.setPersistentProperty(BlooperPlugin.PROPERTY_URL,null);
        project.setPersistentProperty(BlooperPlugin.PROPERTY_USER,null);
        project.setPersistentProperty(BlooperPlugin.PROPERTY_APPLICATION,null);
      }
      catch (Exception e1)
      {
        // ignore
      }
      return false;
    }
		return true;
	}

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
    if(this.getContainer().getCurrentPage()!=null)
      this.getContainer().updateButtons();
  }

  public String getServerUrl()
  {
    return serverUrl;
  }

  public void setServerUrl(String serverUrl)
  {
    this.serverUrl = serverUrl;
    if(this.getContainer().getCurrentPage()!=null)
      this.getContainer().updateButtons();
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
    if(this.getContainer().getCurrentPage()!=null)
      this.getContainer().updateButtons();
  }

  public String getApplicationName()
  {
    return appPage.getApplicationName();
  }

  public IProject getProject()
  {
    return project;
  }
  
  protected void updateButtons()
  {
    if(this.getContainer().getCurrentPage()!=null)
      this.getContainer().updateButtons();
  }
}
