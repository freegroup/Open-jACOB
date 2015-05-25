/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Sep 15 13:01:30 CEST 2006
 */
package jacob.common.gui.categoryprocess;

import de.tif.jacob.screen.IClientContext;
import jacob.common.gui.RemoveBrowserButtonEventHandler;


/**
 * The event handler for the CategoryprocessRemoveBrowser generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class CategoryprocessRemoveBrowser extends RemoveBrowserButtonEventHandler 
{

  /* (non-Javadoc)
   * @see jacob.common.gui.RemoveBrowserButtonEventHandler#getRecordEntityName(de.tif.jacob.screen.IClientContext)
   */
  protected String getRecordEntityName(IClientContext context)
  {
    return "Vertragsleistungseinträge";
  }
}
