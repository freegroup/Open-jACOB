/*
 * Created on 18.07.2009
 *
 */
package de.tif.jacob.components.searchbrowser_contextmenu.impl;

import org.eclipse.jface.resource.ImageDescriptor;

import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.components.searchbrowser_contextmenu.PluginId;
import de.tif.jacob.components.searchbrowser_contextmenu.SearchBrowserContextMenu;
import de.tif.jacob.components.searchbrowser_contextmenu.SearchBrowserContextMenuEventHandler;
import de.tif.jacob.core.Version;

public class SearchBrowserContextMenuPlugin extends IComponentPlugin
{
  private final static Version version = new Version(2,8,5);

  public SearchBrowserContextMenuPlugin()
  {
  }

  public String getName()
  {
    return "Browser Context Menu";
  }

  public ImageDescriptor getPaletteImage()
  {
    return Activator.getDefault().getImageDescriptor("tool_plugin.png");
  }

  public String getJavaImplClass()
  {
    return SearchBrowserContextMenu.class.getName();
  }

  public Class getFigureClass()
  {
    return SearchBrowserContextMenuFigure.class;
  }

  @Override
  public String getPluginId()
  {
    return PluginId.ID;
  }

  @Override
  public Class getEventHandlerTemplateClass()
  {
    return SearchBrowserContextMenuEventHandler.class;
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
