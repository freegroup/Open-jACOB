/*
 * Created on 22.07.2009
 *
 */
package de.tif.jacob.properties.informbrowser_actionrepresentation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.Version;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIBreadCrumbModel;
import de.tif.jacob.designer.model.UIDBInformBrowserModel;
import de.tif.jacob.properties.plugin.IPropertyPlugin;

public class InformBrowserPropertyPlugin extends IPropertyPlugin
{
  String [] style={"Combobox","Button Bar"};
  final static String ACTION_REPRESENTATION ="action representation";
  
  public InformBrowserPropertyPlugin()
  {
  }


  public IPropertyDescriptor[] getPropertyDescriptors(ObjectModel model, IPropertyDescriptor[] superDescriptors)
  {
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ACTION_REPRESENTATION, "Action Representation Style",   style, ObjectModel.PROPERTYGROUP_STYLE);

    return descriptors;
  }


  @Override
  public Object getPropertyValue(ObjectModel model, Object propName)
  {
    if (propName == ACTION_REPRESENTATION)
        return new Integer(style[1].equals(((UIDBInformBrowserModel)model).getCastorStringProperty("action_representation"))?1:0);
    
    return super.getPropertyValue(model, propName);
  }

  @Override
  public boolean setPropertyValue(ObjectModel model, Object propName, Object val)
  {
    if(propName == ACTION_REPRESENTATION)
    {
      ((UIDBInformBrowserModel)model).setCastorProperty("action_representation",style[((Integer)val).intValue()] );
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
