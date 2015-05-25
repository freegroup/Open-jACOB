/*
 * Created on 13.10.2010
 *
 */
package de.tif.jacob.properties.spotlightsearch;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.Version;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.ObjectWithPropertyModel;
import de.tif.jacob.designer.model.UIStackContainerModel;
import de.tif.jacob.properties.plugin.IPropertyPlugin;
import de.tif.jacob.util.StringUtil;

public class SpotlightPropertyExtender extends IPropertyPlugin
{
  private static final String SPOTLIGHT_PROPERTY = "spotlight";
  
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

  @Override
  public Version getRequiredJacobVersion()
  {
    return new Version(2,10,0);
  }

  @Override
  public IPropertyDescriptor[] getPropertyDescriptors(ObjectModel model, IPropertyDescriptor[] superDescriptors)
  {
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    
    descriptors[superDescriptors.length] = new TextPropertyGroupingDescriptor(SPOTLIGHT_PROPERTY, "Spotlight Search Phrase", ObjectModel.PROPERTYGROUP_FEATURES);
 
    return descriptors;
  }

  @Override
  public Object getPropertyValue(ObjectModel model, Object propName)
  {
    if(propName == SPOTLIGHT_PROPERTY)
      return StringUtil.toSaveString( ((ObjectWithPropertyModel)model).getCastorStringProperty("spotlight_search"));
    
    return super.getPropertyValue(model, propName);
  }

  @Override
  public boolean setPropertyValue(ObjectModel model, Object propName, Object val)
  {
    if(propName == SPOTLIGHT_PROPERTY)
    {
      String value = (String)val;
      if(value.length()==0)
        value=null;
      ((ObjectWithPropertyModel)model).setCastorProperty("spotlight_search",value);
      return true;
    }

    return super.setPropertyValue(model, propName, val);
  }
}
