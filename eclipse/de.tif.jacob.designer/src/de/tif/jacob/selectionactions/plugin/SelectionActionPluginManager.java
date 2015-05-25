/*
 * Created on 11.01.2007
 *
 */
package de.tif.jacob.selectionactions.plugin;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import de.tif.jacob.designer.JacobDesigner;

public class SelectionActionPluginManager
{
  public static final String ID = "de.tif.jacob.designer.jacobSelectionActionPlugin";

  public enum Type
  {
    informbrowser,
    searchbrowser
  }
  

  public static Collection<ISelectionActionPlugin> getPlugins(Type type )
  {
    Collection<ISelectionActionPlugin> editors = new HashSet<ISelectionActionPlugin>();
    
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
        String group = element.getAttribute("browser_type");
        if(type.name().equals(group))
        {
          try
          {
            Object obj = element.createExecutableExtension("class");
            if(obj instanceof ISelectionActionPlugin)
            {
              editors.add((ISelectionActionPlugin)obj);
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
