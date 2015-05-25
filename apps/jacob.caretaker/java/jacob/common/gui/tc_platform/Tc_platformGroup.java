/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 08 13:59:45 CEST 2006
 */
package jacob.common.gui.tc_platform;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupListenerEventHandler;


/**
 *
 * @author andreas
 */
 public class Tc_platformGroup extends IGroupListenerEventHandler 
 {
	static public final transient String RCS_ID = "$Id: Tc_platformGroup.java,v 1.1 2006/08/09 13:22:09 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement group) throws Exception
  {
    // ensure field is always enabled
    group.findByName("tc_orderListDate").setEnable(true);
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
