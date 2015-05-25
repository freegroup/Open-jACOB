/*
 * Created on 27.07.2009
 *
 */
package de.tif.jacob.properties.color;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.Version;
import de.tif.jacob.designer.editor.jacobform.misc.ColorPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.IntegerPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIStackContainerModel;
import de.tif.jacob.properties.plugin.IPropertyPlugin;

public class StackContainerColorsPropertyPlugin extends IPropertyPlugin
{
  public StackContainerColorsPropertyPlugin()
  {
  }


  public IPropertyDescriptor[] getPropertyDescriptors(ObjectModel model, IPropertyDescriptor[] superDescriptors)
  {
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 3];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length] = new ColorPropertyGroupingDescriptor(ObjectModel.ID_PROPERTY_BORDER_COLOR, "Border Color", ObjectModel.PROPERTYGROUP_STYLE);
    descriptors[superDescriptors.length+1] = new ColorPropertyGroupingDescriptor(ObjectModel.ID_PROPERTY_BACKGROUND_COLOR, "Background Color", ObjectModel.PROPERTYGROUP_STYLE);
    descriptors[superDescriptors.length+2] = new IntegerPropertyGroupingDescriptor(ObjectModel.ID_PROPERTY_BORDER_WIDTH, "Border Width", ObjectModel.PROPERTYGROUP_STYLE);

    return descriptors;
  }


  @Override
  public Object getPropertyValue(ObjectModel model, Object propName)
  {
    if (propName == ObjectModel.ID_PROPERTY_BORDER_COLOR)
      return ((UIStackContainerModel)model).getBorderColor().getRGB();
    
    if (propName == ObjectModel.ID_PROPERTY_BACKGROUND_COLOR)
      return ((UIStackContainerModel)model).getBackgroundColor().getRGB();
    
    if (propName == ObjectModel.ID_PROPERTY_BORDER_WIDTH)
      return Integer.toString(((UIStackContainerModel)model).getBorderWidth());

    return super.getPropertyValue(model, propName);
  }

  @Override
  public boolean setPropertyValue(ObjectModel model, Object propName, Object val)
  {
    if(propName == ObjectModel.ID_PROPERTY_BORDER_COLOR)
    {
      ((UIStackContainerModel)model).setBorderColor(new Color(null,(RGB)val));
      return true;
    }
    
    if(propName == ObjectModel.ID_PROPERTY_BACKGROUND_COLOR)
    {
      ((UIStackContainerModel)model).setBackgroundColor(new Color(null,(RGB)val));
      return true;
    }
    
    if (propName == ObjectModel.ID_PROPERTY_BORDER_WIDTH)
    {
      ((UIStackContainerModel)model).setBorderWith(Integer.parseInt(val.toString()));
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
