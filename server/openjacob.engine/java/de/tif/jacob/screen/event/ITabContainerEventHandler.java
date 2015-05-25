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
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * Abstract event handler class for document fields. Derived implementations of
 * this event handler class have to be used to "hook" application-specific
 * business logic to document fields.
 * 
 * @author Andreas Herz
 */
public abstract class ITabContainerEventHandler extends IContainerEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: ITabContainerEventHandler.java,v 1.2 2009/02/12 20:28:32 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   */
  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    // Redirect this event to a method with a "nice" interface
    this.onGroupStatusChanged(context, state,(ITabContainer)element);
  }
  
 /*
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
  * @param element
  *          The corresponding GUI element to this event handler
  */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
  {
  }

}
