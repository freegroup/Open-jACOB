/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 28 01:05:28 CEST 2006
 */
package jacob.common.gui.tc_customer;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupListenerEventHandler;


/**
 *
 * @author andreas
 */
 public class Tc_customerGroup extends IGroupListenerEventHandler
 {
	static public final transient String RCS_ID = "$Id: Tc_customerGroup.java,v 1.2 2006/08/02 17:31:27 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
  }

  public void onHide(IClientContext context, IGroup group) throws Exception 
  {
  }
  
  public void onShow(IClientContext context, IGroup group) throws Exception 
  {
  }
}
