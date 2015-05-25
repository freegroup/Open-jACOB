/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Aug 15 13:20:10 CEST 2007
 */
package jacob.common.gui.task;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.Task;

import org.apache.commons.logging.Log;


/**
 * The event handler for the SapTaskDocumented record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class SapTaskDocumented extends IButtonEventHandler 
{

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button)throws Exception 
  {
      Task.setDocumented(null, context.getSelectedRecord());  
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
        button.setEnable(taskstatus.equals("Fertig gemeldet"));
      }
  }
}

