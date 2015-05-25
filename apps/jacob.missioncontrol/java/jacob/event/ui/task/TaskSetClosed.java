package jacob.event.ui.task;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Aug 08 13:24:30 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.common.ENUM;
import jacob.common.Task;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * The Event handler for the TaskSetDone-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class TaskSetClosed extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: TaskSetClosed.java,v 1.2 2005/09/22 14:59:50 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
		IDataTableRecord task = context.getSelectedRecord();
        Task.setStatus(null,context,task,ENUM.TASKSTATUS_CLOSED);

  }

   
  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
	 * @throws Exception
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
	    if (status == IGuiElement.SELECTED)
        {
            // nur der Owner der Meldung darf ein QA Auftrag schliessen
            IDataTableRecord task = context.getSelectedRecord();
            button.setEnable((ENUM.TASKSTATUS_DONE.equals(task.getSaveStringValue("status")) && task.getLinkedRecord("call").getStringValue("owner_key").equals(context.getUser().getKey())));
        }
	  //button.setEnable(true/false);
	}
}

