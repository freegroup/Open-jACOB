/*
 * Created on 17.07.2009
 *
 */
package de.tif.jacob.properties.buttonbar;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ReadonlyTextPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.AbstractActionEmitterModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.ObjectWithPropertyModel;
import de.tif.jacob.designer.model.UIStackContainerModel;
import de.tif.jacob.properties.plugin.IPropertyPlugin;

public class UIButtonBarModelPropertyPlugin extends IPropertyPlugin
{
  private static String PROPERTY = "hide_read_only_elements";
  
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
    
    descriptors[superDescriptors.length] = new CheckboxPropertyDescriptor(PROPERTY, "Hide Disabled Buttons", ObjectModel.PROPERTYGROUP_STYLE);

    return descriptors;
  }

  @Override
  public Object getPropertyValue(ObjectModel model, Object propName)
  {
    if(propName == PROPERTY)
      return (((ObjectWithPropertyModel)model).getCastorBooleanProperty("hideDisabledElements",false));

    return super.getPropertyValue(model, propName);
  }

  @Override
  public boolean setPropertyValue(ObjectModel model, Object propName, Object val)
  {
    if (propName == PROPERTY)
    {
      boolean visible = ((Boolean) val).booleanValue();
      if(visible)
        ((ObjectWithPropertyModel)model).setCastorProperty("hideDisabledElements","true");
      else
        ((ObjectWithPropertyModel)model).setCastorProperty("hideDisabledElements",null);
      
      return true;
    }
    return super.setPropertyValue(model, propName, val);
  }
}
