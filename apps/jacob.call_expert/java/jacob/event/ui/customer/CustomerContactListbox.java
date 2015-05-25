/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Nov 13 19:05:30 CET 2008
 */
package jacob.event.ui.customer;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITableListBoxEventHandler;

/**
 *
 * @author achim
 */
public class CustomerContactListbox extends ITableListBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: CustomerContactListbox.java,v 1.1 2009/02/17 15:12:13 A.Boeken Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * Called, if the user clicks on one entry n the TableListBox
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param emitter The emitter of the event.
   * @param selectedRecord The entry which the user has been selected
	 */
	public void onSelect(IClientContext context, ITableListBox emitter, IDataTableRecord selectedRecord) throws Exception
	{
    // your code here

	}

	/**
	 * The event handler if the group status has been changed.<br>
	 * This is a good place to enable/disable some combo box entries in relation to the state of the
	 * selected record.<br>
	 * <br>
	 * Note: You can only enable/disable <b>valid</b> enum values of the corresponding table field.<br>
	 * <br>
	 * <code>
	 *	 IListBox listBox =(IListBox)emitter;
	 *	 // remove all list box entries
	 *	 listBox.enableOptions(false);
	 *   if(..some condition...)
	 *   {  
	 *     listBox.enableOptions("Duplicate",true);
	 *     listBox.enableOptions("Declined",true);
	 *   }
	 *   else if(...another condition...)
	 *   {  
	 *     listBox.enableOptions("Proved",true);
	 *     listBox.enableOptions("In progress",true);
	 *     listBox.enableOptions("QA",true);
	 *   }
	 *   else // enable all options
	 *   	listBox.enableOptions(true);
	 * 
	 * </code>
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param status  The new state of the group.
	 * @param listBox The emitter of the event.
	 */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ITableListBox listBox) throws Exception
	{
    listBox.setEnable(true);
	}
}
