/*
 * Created on 18.07.2009
 *
 */
package de.tif.jacob.components.dot_cloud_3d.impl;

import org.eclipse.jface.resource.ImageDescriptor;

import de.tif.jacob.components.dot_cloud_3d.Cloud3D;
import de.tif.jacob.components.dot_cloud_3d.Cloud3DEventHandler;
import de.tif.jacob.components.dot_cloud_3d.PluginId;
import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.core.Version;

public class Cloud3DPlugin extends IComponentPlugin
{
  private final static Version version = new Version(2,8,8);

  public Cloud3DPlugin()
  {
  }

  public String getName()
  {
    return "3D Dot Cloud";
  }

  public ImageDescriptor getPaletteImage()
  {
    return Activator.getDefault().getImageDescriptor("toolbar_cloud3d.png");
  }

  public String getJavaImplClass()
  {
    return Cloud3D.class.getName();
  }

  public Class getFigureClass()
  {
    return Cloud3DPluginFigure.class;
  }

  @Override
  public String getPluginId()
  {
    return PluginId.ID;
  }

  @Override
  public Class getEventHandlerTemplateClass()
  {
    return Cloud3DEventHandler.class;
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
