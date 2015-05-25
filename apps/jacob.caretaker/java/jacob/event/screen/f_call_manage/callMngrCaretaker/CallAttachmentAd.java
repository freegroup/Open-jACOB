/*
 * Created on May 24, 2004
 *
 */
package jacob.event.screen.f_call_manage.callMngrCaretaker;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUploadDialog;

/**
 *
 */

public final class CallAttachmentAd extends jacob.common.gui.attachment.AttachmentAdd
{
  static public final transient String RCS_ID = "$Id: CallAttachmentAd.java,v 1.2 2004/07/12 06:56:06 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  /* 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement emitter) throws Exception
  {
    IDataTableRecord record =context.getDataTable("call").getSelectedRecord();

    IUploadDialog dialog=context.createUploadDialog(new AttachmentAddCallback(record,"callattachment"));
    dialog.show();
  }
}
