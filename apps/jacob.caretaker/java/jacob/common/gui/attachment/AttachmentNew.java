/*
 * Created on 09.07.2004
 *
 */
package jacob.common.gui.attachment;

import jacob.common.gui.attachment.AttachmentAdd;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDialog;

/**
 * @author achim
 *
 */
public  class AttachmentNew extends AttachmentAdd 
{
  static public final transient String RCS_ID = "$Id: AttachmentNew.java,v 1.1 2004/09/20 15:44:47 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  /* 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement emitter) throws Exception
  {
    IDataTableRecord record =context.getDataTable("call").getSelectedRecord();
    IDialog dialog;
    
    if (record!=null)
      dialog=context.createUploadDialog(new AttachmentAddCallback(record,"callattachment"));
    else
    	dialog=context.createMessageDialog("Bitte erste eine Meldung selektieren.");

    dialog.show();
  }
  
  /* 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState state, IGuiElement element)   throws Exception
  {
  	element.setEnable(state != IGuiElement.NEW && state != IGuiElement.UPDATE );
    	
  }
}
