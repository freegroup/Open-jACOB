/*
 * Created on 23.02.2009
 *
 */
package de.tif.jacob.components.candystick;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupMemberEventHandler;

public abstract class CandystickEventHandler extends IGroupMemberEventHandler
{
  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    onGroupStatusChanged(context,state,(ICandystick)element);
  }
  
  public abstract void onGroupStatusChanged(IClientContext context, GroupState state, ICandystick element) throws Exception;
}
