/*
 * Created on 20.02.2009
 *
 */
package de.tif.jacob.components.flotr.impl;

import org.eclipse.jface.resource.ImageDescriptor;

import de.tif.jacob.components.flotr.Flotr;
import de.tif.jacob.components.flotr.FlotrChartEventHandler;
import de.tif.jacob.components.flotr.PluginId;
import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.core.Version;

public class FlotrComponentPlugin extends IComponentPlugin
{
  private final static Version version = new Version(2,8,5);

  public FlotrComponentPlugin()
  {
  }

  public String getName()
  {
    return "Chart Ext";
  }

  public ImageDescriptor getPaletteImage()
  {
    return Activator.getDefault().getImageDescriptor("tool_flotr.png");
  }

  public String getJavaImplClass()
  {
    return Flotr.class.getName();
  }

  public Class getFigureClass()
  {
    return FlotrFigure.class;
  }

  @Override
  public String getPluginId()
  {
    return PluginId.ID;
  }

  @Override
  public Class getEventHandlerTemplateClass()
  {
    return FlotrChartEventHandler.class;
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
