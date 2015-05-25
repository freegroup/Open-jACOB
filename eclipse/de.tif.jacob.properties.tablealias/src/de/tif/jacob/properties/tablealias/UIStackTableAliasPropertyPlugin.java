/*
 * Created on 17.07.2009
 *
 */
package de.tif.jacob.properties.tablealias;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ReadonlyTextPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.AbstractActionEmitterModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIStackContainerModel;
import de.tif.jacob.properties.plugin.IPropertyPlugin;

public class UIStackTableAliasPropertyPlugin extends IPropertyPlugin
{

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
    return new Version(2,8,6);
  }

  @Override
  public IPropertyDescriptor[] getPropertyDescriptors(ObjectModel model, IPropertyDescriptor[] superDescriptors)
  {
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    
    descriptors[superDescriptors.length] = new ReadonlyTextPropertyGroupingDescriptor(ObjectModel.ID_PROPERTY_TABLEALIAS, "Table Alias", ObjectModel.PROPERTYGROUP_DB);
    descriptors[superDescriptors.length+1] = new ReadonlyTextPropertyGroupingDescriptor(ObjectModel.ID_PROPERTY_TABLE, "Table", ObjectModel.PROPERTYGROUP_DB);

    return descriptors;
  }

  @Override
  public Object getPropertyValue(ObjectModel model, Object propName)
  {
    if(propName == ObjectModel.ID_PROPERTY_TABLEALIAS)
      return ((UIStackContainerModel)model).getElements().get(0).getTableAlias();
    
    if(propName == ObjectModel.ID_PROPERTY_TABLE)
      return ((UIStackContainerModel)model).getElements().get(0).getTableAliasModel().getTableModel().getName();
    
    return super.getPropertyValue(model, propName);
  }

  @Override
  public boolean setPropertyValue(ObjectModel model, Object propName, Object val)
  {
    return super.setPropertyValue(model, propName, val);
  }
}