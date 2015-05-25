/*
 * Created on 20.02.2009
 *
 */
package de.tif.jacob.components.button_search_ext.impl;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.components.button_search_ext.PluginId;
import de.tif.jacob.components.button_search_ext.SearchButton;
import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.components.plugin.PluginComponentManager.Group;
import de.tif.jacob.core.Version;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIPluginComponentModel;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.util.StringUtil;

public class SearchButtonComponentPlugin extends IComponentPlugin
{
  private final static Version version = new Version(2,10,0);

  public SearchButtonComponentPlugin()
  {
  }

  public String getName()
  {
    return "Search (memento)";
  }

  public IPropertyDescriptor[] getPropertyDescriptors(UIPluginComponentModel model, IPropertyDescriptor[] superDescriptors)
  {
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(SearchButton.PROPERTY_RELATIONSET, "Relationset",(String[])model.getJacobModel().getRelationsetNames().toArray(new String[0]), ObjectModel.PROPERTYGROUP_ACTION);
    descriptors[superDescriptors.length + 1] = new ComboBoxPropertyGroupingDescriptor(SearchButton.PROPERTY_FILLDIRECTION, "Filldirection", (String[]) ObjectModel.FILLDIRECTIONS.toArray(new String[0]), ObjectModel.PROPERTYGROUP_ACTION);

    return descriptors;
  }

  public  Group getType()
  {
    return Group.button;
  }
  
  /**
   * 
   */
  public boolean setPropertyValue(UIPluginComponentModel model, Object propName, Object val)
  {
    if (propName == SearchButton.PROPERTY_RELATIONSET)
    {
      model.setCastorProperty(SearchButton.PROPERTY_RELATIONSET, (String) (model.getJacobModel().getRelationsetNames().get(((Integer) val).intValue())));
      return true;
    }
    else if (propName == SearchButton.PROPERTY_FILLDIRECTION)
    {
      model.setCastorProperty(SearchButton.PROPERTY_FILLDIRECTION, (String) ObjectModel.FILLDIRECTIONS.get(((Integer) val).intValue()));
      return true;
    }
    return false;
  }

  public Object getPropertyValue(UIPluginComponentModel model, Object propName)
  {
    if (propName == SearchButton.PROPERTY_RELATIONSET)
    {
      String value = model.getCastorStringProperty(SearchButton.PROPERTY_RELATIONSET);
      if(value==null || value.length()==0)
        value = SearchButton.DEFAULT_RELATIONSET;
      return new Integer(model.getJacobModel().getRelationsetNames().indexOf(value));
    }
    if (propName == SearchButton.PROPERTY_FILLDIRECTION)
    {
      String value = model.getCastorStringProperty(SearchButton.PROPERTY_FILLDIRECTION);
      if(value==null || value.length()==0)
        value = SearchButton.DEFAULT_FILLDIRECTION;
      
      return new Integer(ObjectModel.FILLDIRECTIONS.indexOf(value));
    }
    return null;
  }
  
  public ImageDescriptor getPaletteImage()
  {
    return Activator.getDefault().getImageDescriptor("toolbar_button.png");
  }

  public String getJavaImplClass()
  {
    return SearchButton.class.getName();
  }

  public Class getFigureClass()
  {
    return SearchButtonFigure.class;
  }

  @Override
  public String getPluginId()
  {
    return PluginId.ID;
  }

  @Override
  public Class getEventHandlerTemplateClass()
  {
    return IActionButtonEventHandler.class;
  }

  @Override
  public Version getRequiredJacobVersion()
  {
    return version;
  }

  @Override
  public String getPluginVersion()
  {
    return (String)Activator.getDefault().getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
  }


  public void renameRelationsetReference(UIPluginComponentModel model, String from, String to)
  {
    String value = model.getCastorStringProperty(SearchButton.PROPERTY_RELATIONSET);

    if( StringUtil.saveEquals(value,from))
      model.setCastorProperty(SearchButton.PROPERTY_RELATIONSET,to);
  }
  
}
