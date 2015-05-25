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
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IMutableComboBox;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * Abstract event handler class for mutable list boxes. Derived implementations of this
 * event handler class have to be used to "hook" application-specific business
 * logic to mutable list boxes.
 * 
 * @author Andreas Herz
 */
public abstract class IMutableComboBoxEventHandler extends IComboBoxEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IMutableComboBoxEventHandler.java,v 1.3 2008/12/01 21:06:35 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  
  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement combobox) throws Exception
  {
    // redirect zu der Methode mit einem Interface welches vom AppProgrammiere
    // später kein casting erfordert
    //
    onGroupStatusChanged(context, state, (IMutableComboBox) combobox);
  }

  /**
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
   * @param comboBox
   *          The corresponding GUI element to this event handler
   */
  public abstract void onGroupStatusChanged(IClientContext context, GroupState state, IMutableComboBox comboBox) throws Exception;

  
  public final void onSelect(IClientContext context, IComboBox comboBox) throws Exception
  {
    onSelect(context, (IMutableComboBox) comboBox);
  }
  
  /**
   * This hook method will be called, if the user selects a new item in the
   * mutable list box.
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
   *          the mutable listbox itself
   */
  public abstract void onSelect(IClientContext context, IMutableComboBox comboBox) throws Exception;
}
