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
package de.tif.jacob.designer.editor.jacobform.dialogs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "txt". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */
public class NewGroupWizard extends Wizard implements INewWizard
{
	private AliasPage     namePage;
	private TemplatePage templatePage;
	private final JacobModel jacobModel;
	
	String templateName;
  Properties properties = new Properties();
  private TableAliasModel tableAlias;
  
	/**
	 * 
	 * Constructor for HolidayMainWizard.
	 */
	public NewGroupWizard(JacobModel jacobModel, TableAliasModel aliasModel)
	{
		super();
		this.jacobModel = jacobModel;
    this.tableAlias = aliasModel;
    setDefaultPageImageDescriptor(JacobDesigner.getImageDescriptor("wizard_group.gif"));
	}
	
	public void addPages()
	{
		namePage     = new AliasPage(jacobModel);
		templatePage = new TemplatePage(jacobModel);
		if(tableAlias==null)
		  addPage(namePage);
		addPage(templatePage);
	}
	
	public TableAliasModel getTableAliasModel()
	{
	  return tableAlias;
	}

	public Properties getTemplateProperties()
	{
	  return properties;
	}
	
	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) 
	{
	}

	public boolean canFinish()
	{

		return true;
	}
	
	public boolean performFinish() 
	{
    if(tableAlias==null)
      tableAlias = namePage.getAlias();
		templateName = templatePage.getTemplateName();

    URL url = JacobDesigner.getURL("templates/group/"+templateName+".properties");
    try
    {
      InputStream input = url.openStream();
      try
      {
        properties.load(input);
      }
      finally
      {
        input.close();
      }
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
		return true;
	}
}
