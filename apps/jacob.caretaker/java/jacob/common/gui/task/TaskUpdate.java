/*
 * Created on Jul 29, 2004
 *
 */
package jacob.common.gui.task;

import jacob.common.Task;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 *
 */
public class TaskUpdate extends IActionButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: TaskUpdate.java,v 1.8 2004/08/20 16:12:19 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.8 $";
  /* 
   * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    return true;
  }

  /* 
   * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onSuccess(IClientContext context, IGuiElement button)
  {
  }

  /* 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged( IClientContext context, GroupState status,  IGuiElement button)  throws Exception
  {
		if(status == IGuiElement.SELECTED)
		{
			
			IDataTableRecord currentRecord = context.getSelectedRecord();
			String taskstatus = currentRecord.getStringValue("taskstatus");
			button.setEnable(!taskstatus.equals("Abgerechnet") && (!taskstatus.equals("Abgeschlossen")));
		}
  }
}
