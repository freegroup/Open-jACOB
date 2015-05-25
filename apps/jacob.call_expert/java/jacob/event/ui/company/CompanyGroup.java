/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Aug 14 19:49:50 CEST 2009
 */
package jacob.event.ui.company;

import java.awt.Color;
import java.util.Iterator;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabPaneEventListener;

/**
 * 
 * @author andherz
 */
public class CompanyGroup extends ITabPaneEventListener
{
  static public final transient String RCS_ID  = "$Id: CompanyGroup.java,v 1.2 2009/10/02 09:44:46 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
    pane.setVisible(state == IGroup.SELECTED || state == IGroup.UPDATE || state == IGroup.NEW);
    pane.setBackgroundColor(new Color(216,214,217));
    //Inserted to clear the tab panes below. If i put the code in the lower tab-pane
    // the whole container woud be just white. (bug)
    //TODO: Achim see taht andreas fixes it!
/*    if (state == IGroup.NEW)
    {
      ITabContainer details = (ITabContainer) context.getGroup().findByName("companyContainer");
      for (Iterator i = details.getPanes().iterator(); i.hasNext();)
      {
        ITabPane childPane = (ITabPane)i.next();
        childPane.clear(context);
      }
    }*/

  }
}
