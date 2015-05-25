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

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;

/**
 * Abstract event handler class for search buttons. Derived implementations of
 * this event handler class have to be used to "hook" application-specific
 * business logic to search buttons.
 * 
 * @author Andreas Herz
 */
public abstract class ISearchActionEventHandler extends IActionButtonEventHandler
{
	/**
	 * The internal revision control system id.
	 */
  static public final transient String RCS_ID = "$Id: ISearchActionEventHandler.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";
  
	/**
	 * The internal revision control system id in short form.
	 */
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * This hook method can be used to modify QBE (Query By Example) constraints
   * before the search action is executed.
   * <p>
   * Example:
   * 
   * <pre>
   * if (context.getUser().hasRole(&quot;Guest&quot;))
   * {
   *   context.getDataAccessor().getTable(&quot;call&quot;).qbeSetValue(&quot;democall&quot;, &quot;1&quot;);
   * }
   * </pre>
   * 
   * @param context
   *          The current client context
   * @param button
   *          The action button (the emitter of the event)
   */
  public void modifyQbe(IClientContext context, IActionEmitter button) throws Exception
  {
    // do nothing by default
  }
}
