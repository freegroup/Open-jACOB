/*
 * Created on 18.07.2009
 *
 */
package de.tif.jacob.components.groupedlist.impl;

import org.eclipse.jface.resource.ImageDescriptor;

import de.tif.jacob.components.groupedlist.PluginId;
import de.tif.jacob.components.groupedlist.GroupedListbox;
import de.tif.jacob.components.groupedlist.GroupedListboxEventHandler;
import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.core.Version;

public class GroupedListboxPlugin extends IComponentPlugin
{
  private final static Version version = new Version(2,8,8);

  public GroupedListboxPlugin()
  {
  }

  public String getName()
  {
    return "Grouped Listbox";
  }

  public ImageDescriptor getPaletteImage()
  {
    return Activator.getDefault().getImageDescriptor("toolbar_groupedlistbox.png");
  }

  public String getJavaImplClass()
  {
    return GroupedListbox.class.getName();
  }

  public Class getFigureClass()
  {
    return GroupedListboxFigure.class;
  }

  @Override
  public String getPluginId()
  {
    return PluginId.ID;
  }

  @Override
  public Class getEventHandlerTemplateClass()
  {
    return GroupedListboxEventHandler.class;
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
}
