/*
 * Created on 11.01.2007
 *
 */
package de.tif.jacob.components.plugin;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import de.tif.jacob.designer.JacobDesigner;

public class PluginComponentManager
{
  public static final String ID = "de.tif.jacob.designer.jacobUiPluginComponent";
  private static final IComponentPlugin DEFAULT = new DefaultComponentPlugin();;
  public enum Group
  {
    data,
    normal,
    button,
    container,
    chart
  }
  
  public static IComponentPlugin getComponentPlugin(String clazzName)
  {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    IExtensionPoint point =registry.getExtensionPoint(ID);
    IExtension[] extensions = point.getExtensions();
    for (int i = 0; i < extensions.length; i++)
    {
      IExtension extension = extensions[i];
      IConfigurationElement[] elements = extension.getConfigurationElements();
      for (int j = 0; j < elements.length; j++)
      {
        IConfigurationElement element = elements[j];
        try
        {
          IComponentPlugin comp= (IComponentPlugin)element.createExecutableExtension("class");
          if(comp.getJavaImplClass().equals(clazzName))
            return comp;
        }
        catch (CoreException e)
        {
          JacobDesigner.showException(e);
        }
      }
    }
    return DEFAULT;
  }

  public static Collection<IComponentPlugin> createComponentPlugin(Group type )
  {
    Collection<IComponentPlugin> editors = new HashSet<IComponentPlugin>();
    
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    IExtensionPoint point =registry.getExtensionPoint(ID);
    IExtension[] extensions = point.getExtensions();
    for (int i = 0; i < extensions.length; i++)
    {
      IExtension extension = extensions[i];
      IConfigurationElement[] elements = extension.getConfigurationElements();
      for (int j = 0; j < elements.length; j++)
      {
        IConfigurationElement element = elements[j];
        String group = element.getAttribute("group");
        if(type.name().equals(group))
        {
          try
          {
            Object obj = element.createExecutableExtension("class");
            if(obj instanceof IComponentPlugin)
            {
              editors.add((IComponentPlugin)obj);
            }
          }
          catch (Exception e)
          {
            JacobDesigner.showException(e);
          }
        }
      }
    }
    return editors;
  }
}
