/*
 * Created on 20.02.2009
 *
 */
package de.tif.jacob.components.candystick.impl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.components.candystick.Candystick;
import de.tif.jacob.components.candystick.CandystickEventHandler;
import de.tif.jacob.components.candystick.PluginId;
import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.core.Version;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIPluginComponentModel;
import de.tif.jacob.util.StringUtil;
public class CandystickComponentPlugin extends IComponentPlugin
{
  private final static Version version = new Version(2, 8, 5);

  public CandystickComponentPlugin()
  {
  }

  public IPropertyDescriptor[] getPropertyDescriptors(UIPluginComponentModel model, IPropertyDescriptor[] superDescriptors)
  {
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 4];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length]   = new ComboBoxPropertyGroupingDescriptor(Candystick.PROPERTY_ORIENTATION, Candystick.PROPERTY_ORIENTATION, Candystick.orientations, ObjectModel.PROPERTYGROUP_FEATURES);
    descriptors[superDescriptors.length+1] = new ComboBoxPropertyGroupingDescriptor(Candystick.PROPERTY_REFLECTION, Candystick.PROPERTY_REFLECTION,   Candystick.reflections, ObjectModel.PROPERTYGROUP_FEATURES);
    descriptors[superDescriptors.length+2] = new ComboBoxPropertyGroupingDescriptor(Candystick.PROPERTY_RADIUS, Candystick.PROPERTY_RADIUS,   Candystick.radius, ObjectModel.PROPERTYGROUP_FEATURES);
    descriptors[superDescriptors.length+3] = new TextPropertyGroupingDescriptor(Candystick.PROPERTY_TOOLTIP, Candystick.PROPERTY_TOOLTIP, ObjectModel.PROPERTYGROUP_COMMON);

    return descriptors;
  }

  /**
   * 
   */
  public boolean setPropertyValue(UIPluginComponentModel model, Object propName, Object val)
  {
    if (propName == Candystick.PROPERTY_ORIENTATION)
    {
      int index = (Integer)val;
      model.setCastorProperty(Candystick.PROPERTY_ORIENTATION, Candystick.orientations[index]);
      return true;
    }
    if (propName == Candystick.PROPERTY_REFLECTION)
    {
      int index = (Integer)val;
      model.setCastorProperty(Candystick.PROPERTY_REFLECTION, Candystick.reflections[index]);
      return true;
    }
    if (propName == Candystick.PROPERTY_RADIUS)
    {
      int index = (Integer)val;
      model.setCastorProperty(Candystick.PROPERTY_RADIUS, Candystick.radius[index]);
      return true;
    }
    if (propName == Candystick.PROPERTY_TOOLTIP)
    {
      model.setCastorProperty(Candystick.PROPERTY_TOOLTIP, (String)val);
      return true;
    }
    return false;
  }

  public Object getPropertyValue(UIPluginComponentModel model, Object propName)
  {
    if (propName == Candystick.PROPERTY_ORIENTATION)
    {
      String value = model.getCastorStringProperty(Candystick.PROPERTY_ORIENTATION);
      if(StringUtil.saveEquals(Candystick.orientations[0],value))
        return new Integer(0);
      return new Integer(1);
    }
    if (propName == Candystick.PROPERTY_REFLECTION)
    {
      String value = model.getCastorStringProperty(Candystick.PROPERTY_REFLECTION);
      if(value==null || value.length()==0)
        return new Integer(0);
      return Math.max(0,ArrayUtils.indexOf(Candystick.reflections, value));
    }
    if (propName == Candystick.PROPERTY_RADIUS)
    {
      String value = model.getCastorStringProperty(Candystick.PROPERTY_RADIUS);
      if(value==null || value.length()==0)
        return new Integer(Candystick.radius.length-1);
      return Math.max(0,ArrayUtils.indexOf(Candystick.radius, value));
    }
    if (propName == Candystick.PROPERTY_TOOLTIP)
    {
      return StringUtil.toSaveString(model.getCastorStringProperty(Candystick.PROPERTY_TOOLTIP));
    }
    return null;
  }

  public String getName()
  {
    return "Candystick";
  }

  public ImageDescriptor getPaletteImage()
  {
    return Activator.getDefault().getImageDescriptor("tool_candystick.png");
  }

  public String getJavaImplClass()
  {
    return Candystick.class.getName();
  }

  public Class getFigureClass()
  {
    return CandystickFigure.class;
  }

  @Override
  public String getPluginId()
  {
    return PluginId.ID;
  }

  @Override
  public Class getEventHandlerTemplateClass()
  {
    return CandystickEventHandler.class;
  }

  @Override
  public Version getRequiredJacobVersion()
  {
    return version;
  }

  @Override
  public String getPluginVersion()
  {
    return (String) Activator.getDefault().getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
  }
}
