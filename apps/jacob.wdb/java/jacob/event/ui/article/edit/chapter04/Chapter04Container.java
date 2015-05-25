/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Aug 14 11:22:45 CEST 2010
 */
package jacob.event.ui.article.edit.chapter04;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;


/**
 *
 * @author andherz
 */
public class Chapter04Container extends ITabContainerEventHandler
{
	static public final transient String RCS_ID = "$Id: Chapter04Container.java,v 1.1 2010-08-14 16:59:18 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
  {
    element.hideTabStrip(true);
    element.setActivePane(context,0);
  }
}
