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

import de.tif.jacob.core.Context;
import de.tif.jacob.event.EventHandler;
import de.tif.jacob.i18n.Message;
import de.tif.jacob.screen.IClientContext;

/**
 * Abstract base event handler class for all GUI event handlers.
 * 
 * @author Andreas Herz
 */
public abstract class GuiEventHandler extends EventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: GuiEventHandler.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Shows the given alert message to the user.
   * 
   * @param message
   *          the alert message
   */
  public final void alert(Message message)
  {
    IClientContext currentClientContext = (IClientContext) Context.getCurrent();
    if (currentClientContext != null && currentClientContext.hasApplicationDefinition())
    {
      currentClientContext.createMessageDialog(message).show();
    }
  }

  /**
   * Shows the given alert message to the user.
   * 
   * @param message
   *          the alert message
   */
  public final void alert(String message)
  {
    IClientContext currentClientContext = (IClientContext) Context.getCurrent();
    if (currentClientContext != null && currentClientContext.hasApplicationDefinition())
    {
      currentClientContext.createMessageDialog(message).show();
    }
  }

  /**
   * Shows the given alert message to the user.
   * 
   * @param message
   *          the alert message
   * @param reason
   *          the alert reason
   */
  public final void alert(String message, Throwable reason)
  {
    IClientContext currentClientContext = (IClientContext) Context.getCurrent();
    if (currentClientContext != null && currentClientContext.hasApplicationDefinition())
    {
      currentClientContext.createMessageDialog(message + "[" + reason.toString() + "]").show();
    }
  }
}
