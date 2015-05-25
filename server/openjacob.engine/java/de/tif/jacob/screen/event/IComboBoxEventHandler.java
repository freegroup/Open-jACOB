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
import de.tif.jacob.screen.IComboBox;

/**
 * Abstract event handler class for combo boxes. Derived implementations of this
 * event handler class have to be used to "hook" application-specific business
 * logic to combo boxes.
 * 
 * @author Andreas Herz
 */
public abstract class IComboBoxEventHandler extends IGroupMemberEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IComboBoxEventHandler.java,v 1.6 2008/03/19 14:03:44 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.6 $";

  /**
   * Filter the <b>display</b> data for the given combo box. This doesn't change the <b>value</b> of the
   * ComboBox.
   * 
   * @param context
   *          the current client context
   * @param comboBox
   *          the combobox itself
   * @param entry
   *          the original value of the combo box entry
   * @return the new value for combo box entry.
   * 
   * @since 2.7.2
   */
  public String filterEntry(IClientContext context, IComboBox comboBox, String entry) throws Exception
  {
    return entry;
  }

  /**
   * This hook method will be called, if the user selects a new item in the
   * combo box.
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
   * @param comboBox
   *          the combo box itself
   */
  public abstract void onSelect(IClientContext context, IComboBox comboBox) throws Exception;
}
