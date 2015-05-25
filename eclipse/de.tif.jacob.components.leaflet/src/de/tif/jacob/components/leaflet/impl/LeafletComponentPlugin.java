/*
 * Created on 20.02.2009
 *
 */
package de.tif.jacob.components.leaflet.impl;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.components.leaflet.Leaflet;
import de.tif.jacob.components.leaflet.LeafletEventHandler;
import de.tif.jacob.components.leaflet.PluginId;
import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.core.Version;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIPluginComponentModel;
import de.tif.jacob.util.StringUtil;
public class LeafletComponentPlugin extends IComponentPlugin
{
  private final static Version version = new Version(2, 8, 5);

  public LeafletComponentPlugin()
  {
  }


  public IPropertyDescriptor[] getPropertyDescriptors(UIPluginComponentModel model, IPropertyDescriptor[] superDescriptors)
  {
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length] = new TextPropertyGroupingDescriptor(Leaflet.PROPERTY_MAPSERVER, Leaflet.PROPERTY_MAPSERVER, ObjectModel.PROPERTYGROUP_COMMON);

    return descriptors;
  }

  /**
   * 
   */
  public boolean setPropertyValue(UIPluginComponentModel model, Object propName, Object val)
  {
    if (propName == Leaflet.PROPERTY_MAPSERVER)
    {
      model.setCastorProperty(Leaflet.PROPERTY_MAPSERVER, (String)val);
      return true;
    }
    return false;
  }

  public Object getPropertyValue(UIPluginComponentModel model, Object propName)
  {
    if (propName == Leaflet.PROPERTY_MAPSERVER)
    {
      String url = StringUtil.toSaveString(model.getCastorStringProperty(Leaflet.PROPERTY_MAPSERVER));
      if(url.length()==0)
        return Leaflet.DEFAULT_URL;
    }
    return null;
  }
  
  public String getName()
  {
    return "Leaflet";
  }

  public ImageDescriptor getPaletteImage()
  {
    return Activator.getDefault().getImageDescriptor("tool_leaflet.png");
  }

  public String getJavaImplClass()
  {
    return Leaflet.class.getName();
  }

  public Class getFigureClass()
  {
    return LeafletFigure.class;
  }

  @Override
  public String getPluginId()
  {
    return PluginId.ID;
  }

  @Override
  public Class getEventHandlerTemplateClass()
  {
    return LeafletEventHandler.class;
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
