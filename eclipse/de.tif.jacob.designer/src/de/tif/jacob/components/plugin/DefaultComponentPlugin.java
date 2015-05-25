/*
 * Created on 20.02.2009
 *
 */
package de.tif.jacob.components.plugin;

import org.eclipse.jface.resource.ImageDescriptor;

import de.tif.jacob.components.plugin.PluginComponentManager.Group;
import de.tif.jacob.core.Version;
import de.tif.jacob.designer.editor.jacobform.figures.ImageFigure;

public  class DefaultComponentPlugin extends IComponentPlugin
{
  private final static Version version = Version.parseVersion("2.8.4");
  
  public DefaultComponentPlugin()
  {
  }

  public  Group getType()
  {
    return Group.normal;
  }
  
  public String getName()
  {
    return "Default";
  }

  public ImageDescriptor getPaletteImage()
  {
    return null;
  }

  public String getJavaImplClass()
  {
    return null;
  }

  public Class getFigureClass()
  {
    return ImageFigure.class;
  }

  @Override
  public String getPluginId()
  {
    return "default.plugin";
  }

  @Override
  public Class getEventHandlerTemplateClass()
  {
    return null;
  }

  @Override
  public Version getRequiredJacobVersion()
  {
    return version;
  }

  @Override
  public String getPluginVersion()
  {
    return "1.0";
  }
}
