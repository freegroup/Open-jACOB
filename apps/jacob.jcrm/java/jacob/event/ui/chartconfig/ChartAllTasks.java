package jacob.event.ui.chartconfig;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Oct 18 11:45:46 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Chartconfig;

import org.apache.commons.logging.Log;



 /**
  * The Event handler for the ChartAllTasks-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author achim
  *
  */
public class ChartAllTasks extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id$";
	static public final transient String RCS_REV = "$Revision$";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has been click on the corresponding button.<br>
 	 * Be in mind: The currentRecord can be null if the button has not the [selected] flag.<br>
 	 *             The selected flag warranted that the event can only be fired if the<br>
 	 *             selectedRecord!=null.<br>
 	 *
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
  context.clearGroup();
  context.setPropertyForRequest("charttype","alltasks");
  context.getDataTable().search();
  String chartType = context.getSelectedRecord().getSaveStringValue(Chartconfig.activitydisplay);
  context.setPropertyForRequest("ct",chartType);
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
 	 * Be in mind: The currentRecord can be null if the button has not the [selected] flag.<br>
 	 *             The selected flag warranted that the event can only be fired if the<br>
 	 *             selectedRecord!=null.<br>
   *
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
	 * @throws Exception
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
	  //
	  //button.setEnable(true/false);
	}
}

