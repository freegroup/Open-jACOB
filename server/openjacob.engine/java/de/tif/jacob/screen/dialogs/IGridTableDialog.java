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

package de.tif.jacob.screen.dialogs;

import de.tif.jacob.screen.ISingleDataGuiElement;

/**
 * Base interface for grid table dialog implementations. <br>
 * Grid table dialogs are used to display data of entities. Thereas, each entity
 * occupies one row and the user might select the entity by means of clicking in
 * the respective row.
 * <p>
 * A new grid table dialog can be created by means of: <br>
 * <li>
 * {@link de.tif.jacob.screen.IClientContext#createGridTableDialog(IGuiElement)}
 * <li>
 * {@link de.tif.jacob.screen.IClientContext#createGridTableDialog(IGuiElement, IGridTableDialogCallback)}
 * 
 * @author Andreas Herz
 */
public interface IGridTableDialog extends IBrowserDialog
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IGridTableDialog.java,v 1.1 2007/01/19 09:50:45 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Connects a column with a GUI element in the current group. If the user
   * clicks on a row, the corresponding value will be copied to the respective
   * GUI element.
   * 
   * @param column
   *          the column index beginning with 0
   * @param element
   *          the GUI element to be connect
   */
  public void connect(int column, ISingleDataGuiElement element);
}
