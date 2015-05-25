/*
 * Created on 01.03.2010
 *
 */
package de.tif.jacob.selectionaction.assign_records;

import de.tif.jacob.core.Version;
import de.tif.jacob.selectionactions.plugin.ISelectionActionPlugin;


public class SelectionActionAssignRecord extends ISelectionActionPlugin
{

  public String getJavaImplClass()
  {
    return "de.tif.jacob.selectionaction.assign_records.AssignNewRecordSelectionAction";
  }

  public String getLabel()
  {
    return "Assign Records";
  }


  public String getPluginId()
  {
    return Activator.PLUGIN_ID;
  }

  public String getPluginVersion()
  {
    return (String)Activator.getDefault().getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
  }

  public Version getRequiredJacobVersion()
  {
    return new Version(2,9,4);
  }
  
}
