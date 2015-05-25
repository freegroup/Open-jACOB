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
import de.tif.jacob.screen.IForm;

/**
 * 
 * FOR INTERNAL USE AT THE MOMENT
 * 
 * @author Andreas Herz
 */
public abstract class IFormEventHandler extends GuiEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IFormEventHandler.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * @param context
   *          The current client context
   * @param form The hidden form
   */
  public void onShow(IClientContext context, IForm form) throws Exception
  {
    // do nothing by default
  }

  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * @param context
   *          The current client context
   * @param form The hidden form
   */
  public void onHide(IClientContext context, IForm form) throws Exception
  {
    // do nothing by default
  }
}
