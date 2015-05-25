package de.tif.jacob.projectbuilder.verside;

import de.tif.jacob.projectbuilder.wizards.NewProjectWizard;




/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "txt". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */
public class ProjectWizard extends NewProjectWizard
{
  public String getProjectDefaultName()
  {
    return "verside";
  }
  
  @Override
  public String getPluginId()
  {
    return Activator.PLUGIN_ID;
  }
  
}