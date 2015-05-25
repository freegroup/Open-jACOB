/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 02 15:44:09 CEST 2010
 */
package jacob.event.ui.account;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;

import jacob.common.AppLogger;
import jacob.model.Bo;
import jacob.relationset.BoRelationset;

import org.apache.commons.logging.Log;


/**
 *
 * @author andherz
 */
 public class AccountGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: AccountGroup.java,v 1.2 2010-07-16 14:26:14 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

  public Class getSearchBrowserEventHandlerClass()
  {
   
    return BrowserEventHandler.class;
  }

  /**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
   * @param status  The new status of the group.
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
	{
		// insert your code here
	}

  /**
   * Will be called, if there is a state change from visible=>hidden.
   * 
   * This happens, if the user switches the Domain or Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onHide(IClientContext context, IGroup group) throws Exception 
  {
    // insert your code here
  }
  
  /**
   * Will be called, if there is a state change from hidden=>visible.
   * 
   * This happens, if the user switches to a Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onShow(IClientContext context, IGroup group) throws Exception 
  {
    context.getDataAccessor().clear();
    context.getDataBrowser().search(IRelationSet.LOCAL_NAME, Filldirection.NONE);
    if(context.getDataBrowser().recordCount()>0)
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
  }
}
