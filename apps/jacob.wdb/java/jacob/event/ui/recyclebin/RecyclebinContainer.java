/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Oct 18 13:03:25 CEST 2010
 */
package jacob.event.ui.recyclebin;

import jacob.model.Recyclebin;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;


/**
 *
 * @author andreas
 */
public class RecyclebinContainer extends ITabContainerEventHandler
{
  static public final transient String RCS_ID = "$Id: RecyclebinContainer.java,v 1.1 2010-10-20 21:00:49 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
  {
    element.hideTabStrip(true);
    if (state == IGroup.SELECTED)
    {
      if (context.getSelectedRecord().getStringValue(Recyclebin.object_type).equals(Recyclebin.object_type_ENUM._menutree))
        element.setActivePane(context, 1);
      else
        element.setActivePane(context, 2);
    }
    else
    {
      element.setActivePane(context, 0);
    }
  }
}
