/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jan 11 15:19:12 CET 2006
 */
package jacob.event.ui.opencalls;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 * The event handler for the OpenCallsRefresh generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author mike
 */
public class OpenCallsRefresh extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: OpenCallsRefresh.java,v 1.1 2006/01/12 09:36:24 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		context.clearGroup();
    context.getDataTable().clear();		
    IDataBrowser browser = context.getDataBrowser();
    browser.clear();
    browser.search(IRelationSet.LOCAL_NAME);
    if (browser.recordCount() > 0)
    {
      browser.setSelectedRecordIndex(0);
      browser.propagateSelections();
    }
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
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
}
