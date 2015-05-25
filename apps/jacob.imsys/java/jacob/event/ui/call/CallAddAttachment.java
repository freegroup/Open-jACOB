package jacob.event.ui.call;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Jun 03 16:37:09 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUploadDialog;



 /**
  * The Event handler for the CallAddAttachment-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CallAddAttachment  extends jacob.common.gui.attachment.AttachmentAdd
{
    static public final transient String RCS_ID = "$Id: CallAddAttachment.java,v 1.1 2005/06/03 15:18:53 mike Exp $";
    static public final transient String RCS_REV = "$Revision: 1.1 $";
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

