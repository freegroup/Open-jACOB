/*
 * Created on 23.02.2009
 *
 */
package de.tif.jacob.components.groupedlist;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupMemberEventHandler;

public abstract class GroupedListboxEventHandler extends IGroupMemberEventHandler
{
  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    onGroupStatusChanged(context,state,(IGroupedListbox)element);
  }
  
  public void onGroupStatusChanged(IClientContext context, GroupState state, IGroupedListbox listbox) throws Exception
  {
  }


  /**
   * Called if the user click on an entry in the listbox.<br>
   * 
   * Note: this event will <b>not</b> called if the user select an entry via the 
   * checkbox.<br>
   * <br>
   * 
   * 
   * @param context
   * @param listbox
   * @param entry
   * @throws Exception
   */
  public void onClick(IClientContext context, IGroupedListbox listbox, Object entry) throws Exception
  {
  }
}
