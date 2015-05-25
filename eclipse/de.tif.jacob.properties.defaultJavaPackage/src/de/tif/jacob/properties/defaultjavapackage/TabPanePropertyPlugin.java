/*
 * Created on 14.07.2009
 *
 */
package de.tif.jacob.properties.defaultjavapackage;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.Version;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.ObjectWithPropertyModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UITabModel;
import de.tif.jacob.designer.util.ClassFinder;
import de.tif.jacob.properties.plugin.IPropertyPlugin;

public class TabPanePropertyPlugin extends IPropertyPlugin
{
  private final static String DEFAULT_JAVA_PACKAGE_PROPERTY = "default_java_package";
  private final static String DEFAULT_JAVA_PACKAGE_LABEL    = "Children default Java Package";
  @Override
  public String getPluginId()
  {
    return Activator.PLUGIN_ID;
  }

  @Override
  public String getPluginVersion()
  {
    return (String)Activator.getDefault().getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
  }

  public IPropertyDescriptor[] getPropertyDescriptors(ObjectModel model, IPropertyDescriptor[] superDescriptors)
  {
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length]   = new TextPropertyGroupingDescriptor(DEFAULT_JAVA_PACKAGE_PROPERTY, DEFAULT_JAVA_PACKAGE_LABEL, "Java");

    return descriptors;
  }


  @Override
  public Object getPropertyValue(ObjectModel model, Object propName)
  {
    if(propName == DEFAULT_JAVA_PACKAGE_PROPERTY)
      return ((UITabModel)model).getUIGroupModel().getChildrenDefaultJavaPackage();
    return super.getPropertyValue(model, propName);
  }

  @Override
  public boolean setPropertyValue(ObjectModel model, Object propName, Object val)
  {
    if(propName==DEFAULT_JAVA_PACKAGE_PROPERTY)
    {
      String oldPackage = ((UITabModel)model).getUIGroupModel().getChildrenDefaultJavaPackage();
      if(oldPackage.equals(val))
        return true;
      
      try
      {
        ((UITabModel)model).getUIGroupModel().setChildrenDefaultJavaPackage((String)val);
        if(MessageDialog.openQuestion(null,"Rename Java Package","Move existing java package members too?"))
        {
          IProject project=JacobDesigner.getPlugin().getSelectedProject();
          IJavaProject myJavaProject = JavaCore.create(project);
  
          ClassFinder.renamePackage(oldPackage,(String)val, myJavaProject);
        }
        
      }
      catch(Exception exc)
      {
        JacobDesigner.showException(exc);
      }
      return true;
    }
    return super.setPropertyValue(model, propName, val);
  }

  @Override
  public Version getRequiredJacobVersion()
  {
    return new Version(2,8,6);
  }
}
