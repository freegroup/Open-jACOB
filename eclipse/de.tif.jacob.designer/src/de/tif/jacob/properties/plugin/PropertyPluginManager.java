/*
 * Created on 11.01.2007
 *
 */
package de.tif.jacob.properties.plugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.ObjectModel;

public class PropertyPluginManager
{
  public static final String ID = "de.tif.jacob.designer.jacobObjectModelPropertyPlugin";

  public static Collection<IPropertyPlugin> createComponentPlugin(ObjectModel model )
  {
    Collection<IPropertyPlugin> plugins = new HashSet<IPropertyPlugin>();
    String modelClassName = model.getClass().getName();
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
        String pluginModelClass = element.getAttribute("modelClass");
        boolean sameClass = modelClassName.equals(pluginModelClass);
        if(sameClass)
        {
          try
          {
            Object obj = element.createExecutableExtension("class");
            if(obj instanceof IPropertyPlugin)
            {
              plugins.add((IPropertyPlugin)obj);
            }
          }
          catch (Exception e)
          {
            JacobDesigner.showException(e);
          }
        }
        else // 2. try
        {
          String pluginParentModelClass = element.getAttribute("inheritClass");
          if(pluginParentModelClass!=null)
          {
            try
            {
                Object obj = element.createExecutableExtension("class");
                Class clazz = Class.forName(pluginParentModelClass);
                List<Class<?>> sup=ClassUtils.getAllSuperclasses(model.getClass());
                for (Class< ? > class1 : sup)
                {
                  if(class1.getName().equals(pluginParentModelClass))
                  {
                    plugins.add((IPropertyPlugin)obj);
                    break;
                  }
                }
             }
            catch(Exception e)
            {
              JacobDesigner.showException(e);
            }
          }
        }
      }
    }
    return plugins;
  }
}
