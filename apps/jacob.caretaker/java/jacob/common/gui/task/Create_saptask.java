package jacob.common.gui.task;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Jun 06 14:56:42 CEST 2007
 *
 */
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.sap.CExportSSLERFC;
import jacob.common.sap.CExportTaskRFC;
import jacob.common.sap.ConnManager;

import org.apache.commons.logging.Log;



 /**
  * The Event handler for the Create_saptask-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author e050_fwt-ant_o_test
  *
  */
public class Create_saptask extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Create_saptask.java,v 1.2 2007/08/06 15:37:53 achim Exp $";
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

		IDataTableRecord currentRecord = context.getSelectedRecord();
		ConnManager oConMan = new ConnManager();
		CExportTaskRFC.createTask(oConMan,currentRecord);
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
		// You can enable/disable the button in relation to your conditions.
	  //
	  //button.setEnable(true/false);
	}
}

