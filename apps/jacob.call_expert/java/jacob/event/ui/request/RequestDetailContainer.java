/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Feb 09 11:19:34 CET 2009
 */
package jacob.event.ui.request;

import jacob.common.BreadCrumbController_RequestCategory;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;

/**
 * 
 * @author andherz
 */
public class RequestDetailContainer extends ITabContainerEventHandler
{
  static public final transient String RCS_ID  = "$Id: RequestDetailContainer.java,v 1.1 2009/02/17 15:12:16 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
  {
    BreadCrumbController_RequestCategory.onGroupStatusChanged(context, state);
  }
}
