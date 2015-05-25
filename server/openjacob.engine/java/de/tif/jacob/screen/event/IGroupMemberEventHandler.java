/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.screen.event;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * Abstract event handler class for GUI elements which listen for events related
 * to its corresponding group.
 * 
 * @author Andreas Herz
 */
public abstract class IGroupMemberEventHandler extends GuiEventHandler
{
	/**
	 * The internal revision control system id.
	 */
  static public final transient String RCS_ID = "$Id: IGroupMemberEventHandler.java,v 1.2 2009/04/03 07:26:06 achim_boeken Exp $";
  
	/**
	 * The internal revision control system id in short form.
	 */
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   * This event method will be called, if the status of the corresponding group
   * has been changed. It can be a TabPane or the top level group.<br>
   * Note: A TabPane doesn't receive the <b>onGroupStatusChanged</b> event of a top level group.<br>
   * Derived event handlers could overwrite this method, e.g.
   * to enable/disable GUI elements in relation to the group state. <br>
   * Possible group state values are defined in
   * {@link IGuiElement}:<br>
	 * <ul>
	 *     <li>{@link IGuiElement#UPDATE}</li>
	 *     <li>{@link IGuiElement#NEW}</li>
	 *     <li>{@link IGuiElement#SEARCH}</li>
	 *     <li>{@link IGuiElement#SELECTED}</li>
	 * </ul>
   * @see #onOuterGroupStatusChanged(IClientContext, GroupState, IGuiElement)
   * @param context
   *          The current client context
   * @param state
   *          The new group state
   * @param element
   *          The corresponding GUI element to this event handler
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    // do nothing by default
  }


  /**
   * This event method will be called if the status of the top level group
   * has been changed. The initiator of this event is always a outer group and 
   * not a TabPane or StackPane.
   * <ul>
   *     <li>{@link IGuiElement#UPDATE}</li>
   *     <li>{@link IGuiElement#NEW}</li>
   *     <li>{@link IGuiElement#SEARCH}</li>
   *     <li>{@link IGuiElement#SELECTED}</li>
   * </ul>
   * @see #onGroupStatusChanged(IClientContext, GroupState, IGuiElement)
   * 
   * @param context
   *          The current client context
   * @param state
   *          The new group state
   * @param element
   *          The corresponding GUI element to this event handler
   * @since 2.8.5
   */
  public void onOuterGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    // do nothing by default
  }

  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * @param context
   *          The current client context
   * @param element
   *          The hidden element
   */
  public void onHide(IClientContext context, IGuiElement element) throws Exception
  {
    // do nothing by default
  }
  
  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * @param context
   *          The current client context
   * @param element
   *          The element to show
   */
  public void onShow(IClientContext context, IGuiElement element) throws Exception
  {
    // do nothing by default
  }
}
