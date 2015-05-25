/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Mar 07 22:44:19 CET 2007
 */
package jacob.common.gui.groupmember;

import jacob.common.gui.RemoveBrowserButtonEventHandler;
import de.tif.jacob.screen.IClientContext;


/**
 * The event handler for the GroupmemberRemoveBrowser generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class GroupmemberRemoveBrowser extends RemoveBrowserButtonEventHandler 
{
  /* (non-Javadoc)
   * @see jacob.common.gui.RemoveBrowserButtonEventHandler#getRecordEntityName(de.tif.jacob.screen.IClientContext)
   */
  protected String getRecordEntityName(IClientContext context)
  {
    return "Gruppenmitgliedseinträge";
  }
}
