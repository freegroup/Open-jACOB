/*
 * Created on 15.01.2010
 *
 */
package de.tif.jacob.components.dot_cloud_3d;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupMemberEventHandler;



public abstract class Cloud3DEventHandler extends IGroupMemberEventHandler
{

  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    onGroupStatusChanged(context,state,(ICloud3D)element);
  }
  
  public void onGroupStatusChanged(IClientContext context, GroupState state, ICloud3D listbox) throws Exception
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
  public void onClick(IClientContext context, ICloud3D cloud, String pointId) throws Exception
  {
  }
}
