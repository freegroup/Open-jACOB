/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 12 12:58:54 CEST 2006
 */
package jacob.event.ui.jan_queue;

import jacob.common.ui.RemoveBrowserButtonEventHandler;
import de.tif.jacob.screen.IClientContext;


/**
 * The event handler for the DeleteBrowserContextMenuEntry generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class DeleteBrowserContextMenuEntry extends RemoveBrowserButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: DeleteBrowserContextMenuEntry.java,v 1.2 2010-11-17 17:16:03 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  protected String getRecordEntityName(IClientContext context)
  {
    return "jAN messages";
  }
}
