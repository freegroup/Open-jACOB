package jacob.event.ui.task;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Jun 14 11:33:39 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.common.Task;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * The Event handler for the TaskSetCanceled-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class TaskSetCanceled extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: TaskSetCanceled.java,v 1.3 2005/12/02 15:11:11 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

    // die unterschiedlichen Auftragsstatus
    public static final String NEU = "Neu";
    public static final String ANGELEGT = "Angelegt";
    public static final String FREIGEGEBEN = "Freigegeben";


    // Set welches alle Status vor "Fertig gemeldet" enthält
    public static final Set allStatusBeforeCancel = new HashSet();

    static
    {
        // -------------------------------------------------------------------------------
        // Statusmaps initialisieren
        // -------------------------------------------------------------------------------
        allStatusBeforeCancel.add(NEU);
        allStatusBeforeCancel.add(ANGELEGT);
        allStatusBeforeCancel.add(FREIGEGEBEN);
    }


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
          Task.setStatus(null,context,context.getSelectedRecord(), "Storniert");

      }
}

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button)
          throws Exception 
  {
      IOkCancelDialog dialog = context.createOkCancelDialog("Wollen Sie den Auftrag wirklich stornieren?",new TaskSetStatusCallback());
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
              // externes System steuert da stornieren
              if (currentRecord.hasLinkedRecord("ext_system"))
              {
                  IDataTableRecord extSystemRecord = context.getSelectedRecord().getLinkedRecord("ext_system");
                  button.setEnable(allStatusBeforeCancel.contains(taskstatus) && "Ja".equals(extSystemRecord.getSaveStringValue("allowcancel")));
              }
              else
              {
                  button.setEnable(false);
              }
       
          }
  }

}
