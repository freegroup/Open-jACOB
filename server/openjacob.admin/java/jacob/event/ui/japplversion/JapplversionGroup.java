/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Feb 06 10:19:09 CET 2009
 */
package jacob.event.ui.japplversion;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;

/**
 *
 * @author andreas
 */
public final class JapplversionGroup extends IGroupEventHandler
{
  static public final transient String RCS_ID = "$Id: JapplversionGroup.java,v 1.1 2009/03/03 00:19:38 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public Class getSearchBrowserEventHandlerClass()
  {
    return JapplversionBrowserEventHandler.class;
  }

  public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
  {
  }

  public void onHide(IClientContext context, IGroup group) throws Exception
  {
  }

  public void onShow(IClientContext context, IGroup group) throws Exception
  {
  }
}
