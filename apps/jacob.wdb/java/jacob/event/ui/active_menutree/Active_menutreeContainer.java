/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Aug 14 21:04:41 CEST 2010
 */
package jacob.event.ui.active_menutree;

import jacob.model.Article;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IStackContainerEventHandler;


/**
 *
 * @author andherz
 */
public class Active_menutreeContainer extends IStackContainerEventHandler
{
	static public final transient String RCS_ID = "$Id: Active_menutreeContainer.java,v 1.1 2010-08-15 00:36:50 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";


   @Override
  public void onOuterGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
     element.setVisible(context.getDataTable(Article.NAME).getSelectedRecord()!=null);
  }
}
