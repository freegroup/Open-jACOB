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
import de.tif.jacob.screen.IRadioButtonGroup;

/**
 * Abstract event handler class for radio button groups. Derived implementations of this
 * event handler class have to be used to "hook" application-specific business
 * logic to radio buttons.
 * 
 * @author Andreas Herz
 */
public abstract class IRadioButtonGroupEventHandler extends IGroupMemberEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IRadioButtonGroupEventHandler.java,v 1.4 2009/05/22 13:28:28 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  /**
   * This hook method will be called, if the user selects a new item in the
   * radio button group.
   * <p>
   * 
   * Then overwriting this method, this is a good place for enable/disable other
   * GUI elements.
   * <p>
   * 
   * Note: This method will only be invoked, if the corresponding group state is
   * either {@link de.tif.jacob.screen.IGuiElement#NEW} or
   * {@link de.tif.jacob.screen.IGuiElement#UPDATE}.
   * 
   * @param context
   *          the current client context
   * @param radioButtons
   *          the radio button group itself
   */
  public abstract void onSelect(IClientContext context, IRadioButtonGroup radioButtons) throws Exception;
}
