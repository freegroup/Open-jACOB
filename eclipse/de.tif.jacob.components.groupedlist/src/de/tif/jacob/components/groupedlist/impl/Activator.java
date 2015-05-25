package de.tif.jacob.components.groupedlist.impl;

import org.osgi.framework.BundleContext;

import de.tif.jacob.components.groupedlist.PluginId;
import de.tif.jacob.components.plugin.AbstractActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends de.tif.jacob.components.plugin.AbstractActivator
{
  private static AbstractActivator plugin;
  
  /**
   * The constructor.
   */
  public Activator() 
  {
    plugin = this;
  }

  public String getPluginID()
  {
    return PluginId.ID;    
  }
    
  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static AbstractActivator getDefault() 
  {
    return plugin;
  }
  
  /*
   * (non-Javadoc)
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception 
  {
    plugin = null;
    super.stop(context);
  }
}