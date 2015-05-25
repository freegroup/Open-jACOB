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

import java.util.Properties;

import de.tif.jacob.screen.IClientContext;

/**
 * Interface which has to be implemented by callback handlers for grid table
 * dialogs.
 * 
 * @see IGridTableDialog
 * 
 * @author Andreas Herz
 */
public interface IGridTableDialogCallback
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IGridTableDialogCallback.java,v 1.1 2007/01/19 09:50:44 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * This method will be invoked, if the user has selected a row in the grid
   * table dialog.
   * 
   * @param context
   *          The current client context
   * @param index
   *          the index of the row the user has clicked on
   * @param values
   *          the rows data values. Thereas the key value is the header label to
   *          access the respective data column value.
   * @see IBrowserDialog#setHeader(String[])
   * @see IBrowserDialog#setData(String[][])
   */
  public void onSelect(IClientContext context, int index, Properties values) throws Exception;
}
