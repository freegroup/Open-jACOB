/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 20:38:02 CEST 2010
 */
package jacob.event.ui.search;
import jacob.model.Globalcontent;
import jacob.model.News;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;
/**
 * 
 * @author andherz
 */
public class GlobalcontentContainer extends ITabContainerEventHandler
{
  static public final transient String RCS_ID = "$Id: GlobalcontentContainer.java,v 1.4 2010-09-26 02:33:45 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
  {
    element.hideTabStrip(true);
    IDataTableRecord record = context.getSelectedRecord();
    if (record != null)
    {
      if (News.NAME.equals(record.getStringValue(Globalcontent.tablealias)))
        element.setActivePane(context, 2);
      else
        element.setActivePane(context, 1);
    }
    else
      element.setActivePane(context, 0);
  }
}
