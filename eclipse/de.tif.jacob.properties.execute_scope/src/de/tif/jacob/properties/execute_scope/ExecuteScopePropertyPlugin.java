/*
 * Created on 16.07.2009
 *
 */
package de.tif.jacob.properties.execute_scope;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.AbstractActionEmitterModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UITabModel;
import de.tif.jacob.properties.plugin.IPropertyPlugin;

public class ExecuteScopePropertyPlugin extends IPropertyPlugin
{
  public ExecuteScopePropertyPlugin()
  {
    // TODO Auto-generated constructor stub
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
    return new Version(2,8,6);
  }

  @Override
  public IPropertyDescriptor[] getPropertyDescriptors(ObjectModel model, IPropertyDescriptor[] superDescriptors)
  {
    AbstractActionEmitterModel action = (AbstractActionEmitterModel)model;
    if(action.getCastorAction().getUpdateRecord()==null)
      return superDescriptors;
    
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    
    descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ObjectModel.ID_PROPERTY_EXECUTESCOPE, "Execute Scope", (String[]) ObjectModel.EXECUTE_SCOPES.toArray(new String[0]), ObjectModel.PROPERTYGROUP_ACTION);

    return descriptors;
  }

  @Override
  public Object getPropertyValue(ObjectModel model, Object propName)
  {
    if(propName == ObjectModel.ID_PROPERTY_EXECUTESCOPE)
    {
      String scope =((AbstractActionEmitterModel)model).getCastorAction().getUpdateRecord().getExecuteScope().toString();
      return new Integer(ObjectModel.EXECUTE_SCOPES.indexOf(scope));
    }
    return super.getPropertyValue(model, propName);
  }

  @Override
  public boolean setPropertyValue(ObjectModel model, Object propName, Object val)
  {
    if (propName == ObjectModel.ID_PROPERTY_EXECUTESCOPE)
    {
      UpdateRecordExecuteScopeType scope = UpdateRecordExecuteScopeType.valueOf((String)ObjectModel.EXECUTE_SCOPES.get(((Integer)val).intValue()));
      ((AbstractActionEmitterModel)model).getCastorAction().getUpdateRecord().setExecuteScope(scope);
      return true;
    }
    return super.setPropertyValue(model, propName, val);
  }
  
}
