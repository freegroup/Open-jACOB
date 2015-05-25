/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Aug 13 20:11:46 CEST 2010
 */
package jacob.event.ui.article.edit.chapter01;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;
/**
 * 
 * @author andherz
 */
public class Chapter01Container extends ITabContainerEventHandler
{
  static public final transient String RCS_ID = "$Id: Chapter01Container.java,v 1.2 2010-08-14 16:59:18 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
  {
    element.hideTabStrip(true);
    element.setActivePane(context,0);
  }

}
