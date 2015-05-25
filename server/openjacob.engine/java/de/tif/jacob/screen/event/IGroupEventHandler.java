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
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * 
 * 
 * 
 * @author Andreas Herz
 */
public abstract class IGroupEventHandler extends GuiEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IGroupEventHandler.java,v 1.3 2011/01/17 08:08:31 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  /**
   * This event method will be called, if the status of the corresponding group
   * has been changed. Derived event handlers could overwrite this method, e.g.
   * to enable/disable GUI elements in relation to the group state. <br>
   * Possible group state values are defined in
   * {@link IGuiElement}:<br>
   * <ul>
   *     <li>{@link IGuiElement#UPDATE}</li>
   *     <li>{@link IGuiElement#NEW}</li>
   *     <li>{@link IGuiElement#SEARCH}</li>
   *     <li>{@link IGuiElement#SELECTED}</li>
   * </ul>
   * 
   * @param context
   *          The current client context
   * @param state
   *          The new group state
   * @param group
   *          The corresponding GUI element to this event handler
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IGroup group) throws Exception
  {
    // do nothing by default
  }

  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * @param context
   *          The current client context
   * @param group The group which will be shown
   */
  public void onShow(IClientContext context, IGroup group) throws Exception
  {
    // do nothing by default
  }

  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * @param context
   *          The current client context
   * @param group The group which will be hidden
   */
  public void onHide(IClientContext context, IGroup group) throws Exception
  {
    // do nothing by default
  }

  /**
   * Return the Eventhandler class of the search browser of this group or null
   * if no eventhandler exists.
   * 
   * @return null or the class of the corresponding search browser event handler.
   */
  public Class<?extends IBrowserEventHandler> getSearchBrowserEventHandlerClass()
  {
    return null;
  }
}
