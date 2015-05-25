/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 22:13:17 CEST 2010
 */
package jacob.event.ui.favorite;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;


/**
 *
 * @author andherz
 */
public class FavoriteMainContainer extends ITabContainerEventHandler
{
	static public final transient String RCS_ID = "$Id: FavoriteMainContainer.java,v 1.1 2010-08-17 20:46:53 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

    public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
    {
      element.hideTabStrip(true);
      if(state==IGroup.SELECTED)
        element.setActivePane(context,1);
      else
        element.setActivePane(context,0);
    }
}
