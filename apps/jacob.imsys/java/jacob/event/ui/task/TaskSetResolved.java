package jacob.event.ui.task;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Jun 14 11:24:32 CEST 2005
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
  * The Event handler for the TaskSetResolved-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class TaskSetResolved extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: TaskSetResolved.java,v 1.1 2005/06/14 09:53:51 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();
  
    public static class TaskSetResolvedCallback implements IOkCancelDialogCallback
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
            Task.setStatus(null,context,context.getSelectedRecord(), "Fertig gemeldet");

        }
}

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
     */
    public void onAction(IClientContext context, IGuiElement button)
            throws Exception 
    {
        IOkCancelDialog dialog = context.createOkCancelDialog("Wollen Sie den Auftrag wirklich fertig melden?",new TaskSetResolvedCallback());
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
                button.setEnable(taskstatus.equals("In Arbeit")); 
            }
    }

}
