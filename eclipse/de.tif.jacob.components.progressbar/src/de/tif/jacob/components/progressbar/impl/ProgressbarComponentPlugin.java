/*
 * Created on 20.02.2009
 *
 */
package de.tif.jacob.components.progressbar.impl;

import org.eclipse.jface.resource.ImageDescriptor;

import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.components.progressbar.PluginId;
import de.tif.jacob.components.progressbar.Progressbar;
import de.tif.jacob.components.progressbar.ProgressbarEventHandler;
import de.tif.jacob.core.Version;

public class ProgressbarComponentPlugin extends IComponentPlugin
{
  private final static Version version = new Version(2,8,5);

  public ProgressbarComponentPlugin()
  {
  }

  public String getName()
  {
    return "Progressbar";
  }

  public ImageDescriptor getPaletteImage()
  {
    return Activator.getDefault().getImageDescriptor("tool_progressbar.gif");
  }

  public String getJavaImplClass()
  {
    return Progressbar.class.getName();
  }

  public Class getFigureClass()
  {
    return ProgressbarFigure.class;
  }

  @Override
  public String getPluginId()
  {
    return PluginId.ID;
  }

  @Override
  public Class getEventHandlerTemplateClass()
  {
    return ProgressbarEventHandler.class;
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
