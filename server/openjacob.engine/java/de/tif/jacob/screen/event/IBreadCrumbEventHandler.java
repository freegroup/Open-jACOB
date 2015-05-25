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

import de.tif.jacob.screen.IBreadCrumb;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * Abstract event handler class for non-action buttons. Derived implementations
 * of this event handler class have to be used to "hook" application-specific
 * business logic to buttons.
 * 
 * @author Andreas Herz
 * @since 2.8.0
 */
public abstract class IBreadCrumbEventHandler extends IGroupMemberEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IBreadCrumbEventHandler.java,v 1.3 2009/02/09 15:35:36 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  /**
   * 
   */
  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    // redirect to a propper interface...
    this.onGroupStatusChanged(context, state,(IBreadCrumb) element);
  }

  public void onGroupStatusChanged(IClientContext context, GroupState state, IBreadCrumb element) throws Exception
  {
  }
  
  /**
   * Will be called if the user click on the bread crumb element.
   * 
   * @param context the current working context of the application
   * @param pathToSegment the left hand side path of the selected element. Always ends with the delimiter character of the breadcrumb
   * @param segment the clicked segment
   * @param emitter the emitter element of the event
   */
  public void onClick(IClientContext context, String pathToSegment, String segment, IBreadCrumb emitter) throws Exception
  {
  }
}
