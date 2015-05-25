/*
 * Created on 04.03.2010
 *
 */
package de.tif.jacob.properties.ensure_visible;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.AbstractActionEmitterModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.ObjectWithPropertyModel;
import de.tif.jacob.designer.model.UIStackContainerModel;
import de.tif.jacob.properties.plugin.IPropertyPlugin;

public class StackEnsureVisiblePropertyPlugin extends IPropertyPlugin
{
  private final static String ID_PROPERTY_ENSURE_VISIBLE="ensure visible";
  
  public StackEnsureVisiblePropertyPlugin()
  {
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

  @Override
  public Version getRequiredJacobVersion()
  {
    return new Version(2,9,4);
  }

  @Override
  public IPropertyDescriptor[] getPropertyDescriptors(ObjectModel model, IPropertyDescriptor[] superDescriptors)
  {
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    
    descriptors[superDescriptors.length] = new CheckboxPropertyDescriptor(ID_PROPERTY_ENSURE_VISIBLE, "Ensure Visible", ObjectModel.PROPERTYGROUP_LOCATION);

    return descriptors;
  }

  @Override
  public Object getPropertyValue(ObjectModel model, Object propName)
  {
    if(propName == ID_PROPERTY_ENSURE_VISIBLE)
    {
      return (((UIStackContainerModel)model).getElements().get(0).getCastorBooleanProperty("ensure_visible",false));
    }
    return super.getPropertyValue(model, propName);
  }

  @Override
  public boolean setPropertyValue(ObjectModel model, Object propName, Object val)
  {
    if (propName == ID_PROPERTY_ENSURE_VISIBLE)
    {
      boolean ensureVsible = ((Boolean) val).booleanValue();
      if(ensureVsible)
        ((UIStackContainerModel)model).getElements().get(0).setCastorProperty("ensure_visible","true");
      else
        ((UIStackContainerModel)model).getElements().get(0).setCastorProperty("ensure_visible",null);
      
      return true;
    }
    return super.setPropertyValue(model, propName, val);
  }
}
