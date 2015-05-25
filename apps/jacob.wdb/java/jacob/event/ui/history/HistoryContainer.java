/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 14:21:32 CEST 2010
 */
package jacob.event.ui.history;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;
/**
 * 
 * @author andherz
 */
public class HistoryContainer extends ITabContainerEventHandler
{
  static public final transient String RCS_ID = "$Id: HistoryContainer.java,v 1.2 2010-08-17 20:46:53 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
  {
    element.hideTabStrip(true);
    element.setActivePane(context,0);
    
    if(context.getSelectedRecord()!=null)
        element.setActivePane(context, 1);
  }
  
  
}
