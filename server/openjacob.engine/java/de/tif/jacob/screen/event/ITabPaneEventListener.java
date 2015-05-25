/*
 * Created on 14.08.2009
 *
 */
package de.tif.jacob.screen.event;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * 
   * @since 2.8.7
 */
public abstract class ITabPaneEventListener extends IGroupMemberEventHandler
{

  public final void onHide(IClientContext context, IGuiElement group) throws Exception
  {
    // redirect call to a method to a object specific signature
    this.onHide(context, (ITabPane)group);
  }

  public final void onShow(IClientContext context, IGuiElement group) throws Exception
  {
    // redirect call to a method to a object specific signature
    this.onShow(context, (ITabPane)group);
  }

  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement group) throws Exception
  {
    // redirect call to a method to a object specific signature
    this.onGroupStatusChanged(context, state, (ITabPane)group);
  }
  
  public final void onOuterGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    // redirect call to a method to a object specific signature
    this.onOuterGroupStatusChanged(context, state, (ITabPane) element);
  }

  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * <br>
   * <b>Note: This method will not be called if the user switch to another tab!!!!</b>
   * 
   * @param context
   *          The current client context
   * @param pane The pane which will be hidden
   * @since 2.8.7
   */
  public void onHide(IClientContext context, ITabPane pane) throws Exception
  {
  }

  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * <br>
   * <b>Note: This method will not be called if the user switch to another tab!</b>
   * 
   * @param context
   *          The current client context
   * @param group The group which will be shown
   * @since 2.8.7
   */
  public void onShow(IClientContext context, ITabPane pane) throws Exception
  {
  }

  /**
   * This event method will be called, if the status of the corresponding group
   * has been changed. Derived event handlers could overwrite this method, e.g.
   * to enable/disable GUI elements in relation to the group state. <br>
   * Possible group state values are defined in
   * {@link IGuiElement}:<br>
   * <ul>
   *     <li>{@link IGroup#UPDATE}</li>
   *     <li>{@link IGroup#NEW}</li>
   *     <li>{@link IGroup#SEARCH}</li>
   *     <li>{@link IGroup#SELECTED}</li>
   * </ul>
   * 
   * @param context
   *          The current client context
   * @param state
   *          The new group state
   * @param pane
   *          The corresponding GUI element to this event handler
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
  }  

  /**
   * This event method will be called if the status of the top level group
   * has been changed. The initiator of this event is always a outer group and 
   * not a TabPane or StackPane.
   * <ul>
   *     <li>{@link IGroup#UPDATE}</li>
   *     <li>{@link IGroup#NEW}</li>
   *     <li>{@link IGroup#SEARCH}</li>
   *     <li>{@link IGroup#SELECTED}</li>
   * </ul>
   * @param context
   *          The current client context
   * @param state
   *          The new group state
   * @param pane
   *          The corresponding GUI element to this event handler
   * @since 2.8.7
   */
  public void onOuterGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
  }
}
