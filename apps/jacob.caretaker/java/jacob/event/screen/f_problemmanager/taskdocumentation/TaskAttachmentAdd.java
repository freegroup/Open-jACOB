/*
 * Created on May 24, 2004
 *
 */
package jacob.event.screen.f_problemmanager.taskdocumentation;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUploadDialog;

/**
 *
 */

public final class TaskAttachmentAdd extends jacob.common.gui.attachment.AttachmentAdd
{
  static public final transient String RCS_ID = "$Id: TaskAttachmentAdd.java,v 1.2 2004/07/12 06:56:05 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  /* 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement emitter) throws Exception
  {
    IDataTableRecord record =context.getDataTable("task").getSelectedRecord();

    IUploadDialog dialog=context.createUploadDialog(new AttachmentAddCallback(record,"task_key"));
    dialog.show();
  }
}
