/*
 * Created on 22.07.2009
 *
 */
package de.tif.jacob.properties.breadcrumb_decoration;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.Version;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIBreadCrumbModel;
import de.tif.jacob.properties.plugin.IPropertyPlugin;

public class BreadcrumbDecorationPropertyPlugin extends IPropertyPlugin
{
  public BreadcrumbDecorationPropertyPlugin()
  {
  }


  public IPropertyDescriptor[] getPropertyDescriptors(ObjectModel model, IPropertyDescriptor[] superDescriptors)
  {
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length] = new CheckboxPropertyDescriptor(ObjectModel.ID_PROPERTY_BORDER, "Border", ObjectModel.PROPERTYGROUP_STYLE);

    return descriptors;
  }


  @Override
  public Object getPropertyValue(ObjectModel model, Object propName)
  {
    if (propName == ObjectModel.ID_PROPERTY_BORDER)
    {
      Object property =((UIBreadCrumbModel)model).getCastorStringProperty("avoidDecoration");
      return property==null;
    }
    
    return super.getPropertyValue(model, propName);
  }

  @Override
  public boolean setPropertyValue(ObjectModel model, Object propName, Object val)
  {
    if(propName == ObjectModel.ID_PROPERTY_BORDER)
    {
      boolean decoration= (Boolean)val;
      if(decoration)
        ((UIBreadCrumbModel)model).setCastorProperty("avoidDecoration",null);
      else
        ((UIBreadCrumbModel)model).setCastorProperty("avoidDecoration",Boolean.TRUE.toString());
      return true;
    }
    return super.setPropertyValue(model, propName, val);
  }
  

  @Override
  public Version getRequiredJacobVersion()
  {
    return new Version(2,8,6);
  }
  
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
}
