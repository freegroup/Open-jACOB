/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 27 14:43:28 CEST 2006
 */
package jacob.common.gui.tc_object;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupListenerEventHandler;


/**
 *
 * @author andreas
 */
 public class Tc_objectGroup extends IGroupListenerEventHandler
 {
	static public final transient String RCS_ID = "$Id: Tc_objectGroup.java,v 1.2 2006/08/02 17:31:27 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement group) throws Exception
  {
    ChartState.discardChartState(context);

    // ensure field is always enabled
    group.findByName("wishedTime").setEnable(state == IGuiElement.SELECTED);
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
