package jacob.event.ui.task;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Jun 23 14:44:58 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUploadDialog;



 /**
  * The Event handler for the TaskAddAttachment-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class TaskAddAttachment extends jacob.common.gui.attachment.AttachmentAdd
{
    static public final transient String RCS_ID = "$Id: TaskAddAttachment.java,v 1.1 2005/06/27 12:23:19 mike Exp $";
    static public final transient String RCS_REV = "$Revision: 1.1 $";
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
