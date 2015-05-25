/*
 * Created on 21.06.2007
 *
 */
package de.tif.jacob.screen.event;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IMutableGroup;
import de.tif.jacob.screen.IGuiElement.GroupState;

public class IMutableGroupEventHandler extends IGroupEventHandler
{

  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGroup group) throws Exception
  {
    // redirect to more confortable function signature
    // => No casting required for the application programmer
    //
    onGroupStatusChanged(context, state, (IMutableGroup)group);
  }

  public final void onHide(IClientContext context, IGroup group) throws Exception
  {
    // redirect to more confortable function signature
    // => No casting required for the application programmer
    //
    onHide(context, (IMutableGroup)group);
  }

  public final void onShow(IClientContext context, IGroup group) throws Exception
  {
    // redirect to more confortable function signature
    // => No casting required for the application programmer
    //
    onShow(context, (IMutableGroup)group);
  }


  public void onGroupStatusChanged(IClientContext context, GroupState state, IMutableGroup group) throws Exception
  {
    // do nothing per default
  }

  public void onHide(IClientContext context, IMutableGroup group) throws Exception
  {
    // do nothing per default
  }

  public void onShow(IClientContext context, IMutableGroup group) throws Exception
  {
    // do nothing per default
  }
}
