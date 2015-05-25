/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 02 15:46:52 CEST 2010
 */
package jacob.event.ui.account;

import java.util.List;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IPane;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;


/**
 *
 * @author andherz
 */
public class AccountContainer extends ITabContainerEventHandler
{
	static public final transient String RCS_ID = "$Id: AccountContainer.java,v 1.1 2010-09-17 08:42:21 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
  {
    element.hideTabStrip(true);
    element.setActivePane(context, 0);
    if(state==IGroup.NEW)
      element.setActivePane(context, 3);
    else if(state==IGroup.SEARCH)
      element.setActivePane(context,0);
    else if(state==IGroup.SELECTED)
      element.setActivePane(context,1);
    else if(state==IGroup.UPDATE)
      element.setActivePane(context,2);

    // Wichtig, damit beim Update nur das sichtbare Pane seine Daten in die 
    // Datenschicht "pustet".
    List<IPane> panes = element.getPanes();
    for (IPane pane : panes)
    {
       pane.setEnable(false); 
    }
    element.getActivePane().setEnable(true);
  }
}
