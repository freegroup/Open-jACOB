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

package de.tif.jacob.screen.impl;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;

/**
 * Base interface for Browser actions. Added actions to a browser will be shown in the 
 * right hands side of the browser header.
 * 
 * @author Andras Herz
 */
public abstract class CalendarAction extends de.tif.jacob.screen.impl.AbstractAction
{
  public final void execute(IClientContext context, IGuiElement element, String value) throws Exception
  {
    // Cast auf ein Vernünftiges Interface
    this.execute(context, (HTTPCalendar)element, value);
  }

  /**
   * The action method. This will be called if the use clicks on the browser action icon.
   * 
   * @param context The current context if the application
   * @param browser The browser assigned to the action
   * @param value The value of the action. In the case of the action 'click' is this the selected row
   * @throws Exception w
   */
  public abstract void   execute(IClientContext context, HTTPCalendar calendar, String value) throws Exception;
}
