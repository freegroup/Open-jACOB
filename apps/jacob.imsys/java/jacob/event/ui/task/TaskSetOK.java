package jacob.event.ui.task;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Jun 14 11:29:43 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.common.Task;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * The Event handler for the TaskSetOK-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class TaskSetOK extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: TaskSetOK.java,v 1.2 2005/06/27 12:23:19 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

    public static class TaskSetStatusCallback implements IOkCancelDialogCallback
    {
    
            /* (non-Javadoc)
         * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
         */
        public void onCancel(IClientContext context) throws Exception {
            

        }
        /* (non-Javadoc)
         * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onOk(de.tif.jacob.screen.IClientContext)
         */
        public void onOk(IClientContext context) throws Exception 
        {
            Task.setStatus(null,context,context.getSelectedRecord(), "Freigegeben");

        }
}

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
     */
    public void onAction(IClientContext context, IGuiElement button)
            throws Exception 
    {
        IOkCancelDialog dialog = context.createOkCancelDialog("Wollen Sie den Auftrag wirklich 'Freigegeben' melden?",new TaskSetStatusCallback());
        dialog.show();
        
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
     */
    public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
    
    {
            if(status == IGuiElement.SELECTED)
            {
                IDataTableRecord currentRecord = context.getSelectedRecord();
                String taskstatus = currentRecord.getStringValue("taskstatus");
                button.setEnable(taskstatus.equals("Angelegt")); //|| !taskstatus.equals("Dokumentiert"));
            }
    }

}
