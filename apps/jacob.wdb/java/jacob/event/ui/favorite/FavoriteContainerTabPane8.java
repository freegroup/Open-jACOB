/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 11:31:14 CEST 2010
 */
package jacob.event.ui.favorite;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IGuiElement.GroupState;
import jacob.common.ui.GalerieArticlePaneEventHandler;

/**
 * 
 * @author andherz
 */
public class FavoriteContainerTabPane8 extends GalerieArticlePaneEventHandler
{
  static public final transient String RCS_ID = "$Id: FavoriteContainerTabPane8.java,v 1.3 2010-09-26 01:47:17 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
    super.onOuterGroupStatusChanged(context, state, pane);
  }
}
