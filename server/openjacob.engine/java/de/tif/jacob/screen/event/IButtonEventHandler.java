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

/**
 * Abstract event handler class for non-action buttons. Derived implementations
 * of this event handler class have to be used to "hook" application-specific
 * business logic to buttons.
 * 
 * @author Andreas Herz
 */
public abstract class IButtonEventHandler extends IGroupMemberEventHandler implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IButtonEventHandler.java,v 1.4 2007/12/11 11:59:30 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  /**
   * The hook method will be called, if the user has been click on the element.
   * 
   * @param context
   *          the current client context
   * @param element
   *          the element itself
   * @since 2.7.2
   */
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    // for backward compatible reason we are calling the old method.
    //
    this.onAction(context, element);
  }
  
  /**
   * The hook method will be called, if the user has been click on the element.
   * 
   * @param context
   *          the current client context
   * @param button
   *          the button itself
   * @deprecated override onClick instead.
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
  }
}
