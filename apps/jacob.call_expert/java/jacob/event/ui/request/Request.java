/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Aug 15 00:00:28 CEST 2009
 */
package jacob.event.ui.request;


import java.awt.Color;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabPaneEventListener;



/**
 *
 * @author andherz
 */
public class Request extends ITabPaneEventListener
{
	static public final transient String RCS_ID = "$Id: Request.java,v 1.1 2009/08/16 18:08:36 A.Herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
    pane.setVisible(state == IGroup.SELECTED || state == IGroup.UPDATE || state == IGroup.NEW);
    pane.setBackgroundColor(new Color(216,214,217));
  }
}
