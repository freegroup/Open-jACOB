/*
 * Created on 01.03.2010
 *
 */
package de.tif.jacob.selectionaction.remove_records;

import de.tif.jacob.core.Version;
import de.tif.jacob.selectionactions.plugin.ISelectionActionPlugin;

public class SelectionActionRemoveRecord extends ISelectionActionPlugin
{
  

  @Override
  public String getJavaImplClass()
  {
    return "de.tif.jacob.selectionaction.remove_records.RemoveRecordSelectionAction";
  }

  @Override
  public String getLabel()
  {
    return "Remove Records";
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
    return new Version(2,9,4);
  }
}
