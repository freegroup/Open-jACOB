/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed May 06 21:30:39 CEST 2009
 */
package jacob.event.ui.budget;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andherz
 */
 public class BudgetGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: BudgetGroup.java,v 1.2 2009-07-01 16:38:16 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	@Override
   public Class getSearchBrowserEventHandlerClass() 
	{
      return BudgetBrowserEventHandler.class;
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
   * Will be called if the will be change the state from visible=>hidden.
   * 
   * This happends if the user switch the Domain or Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onHide(IClientContext context, IGroup group) throws Exception 
  {
    // insert your code here
  }
  
  /**
   * Will be called if the will be change the state from hidden=>visible.
   * 
   * This happends if the user switch to a Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onShow(IClientContext context, IGroup group) throws Exception 
  {
    // insert your code here
  }
}
