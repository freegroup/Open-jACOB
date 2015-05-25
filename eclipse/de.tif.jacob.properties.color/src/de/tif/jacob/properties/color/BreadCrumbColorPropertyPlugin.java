/*
 * Created on 21.07.2009
 *
 */
package de.tif.jacob.properties.color;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.Version;
import de.tif.jacob.designer.editor.jacobform.misc.ColorPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIBreadCrumbModel;
import de.tif.jacob.designer.model.UICaptionModel;
import de.tif.jacob.properties.plugin.IPropertyPlugin;

public class BreadCrumbColorPropertyPlugin extends IPropertyPlugin
{
  public BreadCrumbColorPropertyPlugin()
  {
  }

  public IPropertyDescriptor[] getPropertyDescriptors(ObjectModel model, IPropertyDescriptor[] superDescriptors)
  {
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length] = new ColorPropertyGroupingDescriptor(ObjectModel.ID_PROPERTY_COLOR, "Color", ObjectModel.PROPERTYGROUP_FONT);

    return descriptors;
  }


  @Override
  public Object getPropertyValue(ObjectModel model, Object propName)
  {
    if (propName == ObjectModel.ID_PROPERTY_COLOR)
      return ((UIBreadCrumbModel)model).getColor().getRGB();
    
    return super.getPropertyValue(model, propName);
  }

  @Override
  public boolean setPropertyValue(ObjectModel model, Object propName, Object val)
  {
    if(propName == ObjectModel.ID_PROPERTY_COLOR)
    {
      ((UIBreadCrumbModel)model).setColor(new Color(null,(RGB)val));
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
