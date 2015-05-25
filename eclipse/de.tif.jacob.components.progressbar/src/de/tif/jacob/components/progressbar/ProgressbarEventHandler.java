/*
 * Created on 23.02.2009
 *
 */
package de.tif.jacob.components.progressbar;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupMemberEventHandler;

public abstract class ProgressbarEventHandler extends IGroupMemberEventHandler
{
  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    onGroupStatusChanged(context,state,(IProgressbar)element);
  }
  
  
  public abstract void onGroupStatusChanged(IClientContext context, GroupState state, IProgressbar element) throws Exception;
}
