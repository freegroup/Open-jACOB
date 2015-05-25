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

import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;

/**
 * Abstract event handler class for check boxes. Derived implementations of this
 * event handler class have to be used to "hook" application-specific business
 * logic to check boxes.
 * 
 * @author Andreas Herz
 */
public abstract class ICheckBoxEventHandler extends IGroupMemberEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: ICheckBoxEventHandler.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * This hook method will be called, if the user marks (checks) the check box.
   * <p>
   * 
   * Then overwriting this method, this is a good place to enable/disable other
   * GUI elements such like text fields or combo boxes.
   * 
   * @param context
   *          the current client context
   * @param checkBox
   *          the check box itself
   */
  public abstract void onCheck(IClientContext context, ICheckBox checkBox) throws Exception;

  /**
   * This hook method will be called, if the user unmarks (unchecks) the check
   * box.
   * <p>
   * 
   * Then overwriting this method, this is a good place to enable/disable other
   * GUI elements such like text fields or combo boxes.
   * 
   * @param context
   *          the current client context
   * @param checkBox
   *          the check box itself
   */
  public abstract void onUncheck(IClientContext context, ICheckBox checkBox) throws Exception;
}
