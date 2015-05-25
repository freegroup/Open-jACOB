/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Feb 04 20:22:37 CET 2007
 */
package jacob.event.ui.my_circuit;

import java.awt.Color;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Rule;

import org.apache.commons.logging.Log;


/**
 * The event handler for the My_circuitRecordEditCircuitButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class My_circuitRecordEditCircuitButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: My_circuitRecordEditCircuitButton.java,v 1.3 2007/08/23 05:13:05 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTableRecord currentRecord = context.getSelectedRecord();
		IUrlDialog dialog= context.createUrlDialog("./application/"+AppLogger.NAME+"/"+AppLogger.VERSION+"/circuit/edit.jsp?pkey="+currentRecord.getSaveStringValue(Rule.pkey));
		dialog.enableScrollbar(true);
		dialog.show(750,600);
	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement element) throws Exception
	{
    IButton button = (IButton)element;
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
    if(status == IGuiElement.SELECTED)
    {
      button.setBackgroundColor(new Color(203,229,203));
      button.setColor(Color.black);
    }
    else
    {
      button.setBackgroundColor(null);
      button.setColor(null);
    }
	}
}

