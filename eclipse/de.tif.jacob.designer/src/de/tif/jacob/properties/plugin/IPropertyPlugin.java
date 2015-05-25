/*
 * Created on 20.02.2009
 *
 */
package de.tif.jacob.properties.plugin;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.Version;
import de.tif.jacob.designer.model.ObjectModel;

public abstract class IPropertyPlugin
{
  public abstract String  getPluginId();
  public abstract String  getPluginVersion();
  public abstract Version getRequiredJacobVersion();

  public IPropertyDescriptor[] getPropertyDescriptors(ObjectModel model, IPropertyDescriptor[] superProperties)
  {
    return superProperties;
  }

  public boolean setPropertyValue(ObjectModel model, Object propName, Object val)
  {
    return false;
  }
  
  public Object getPropertyValue(ObjectModel model, Object propName)
  {
    return null;
  }
}
