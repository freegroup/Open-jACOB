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

import de.tif.jacob.screen.ILabelProvider;

/**
 * Base interface for record tree implementations. <br>
 * Record tree dialogs are used to view the relation of an entity (record) to
 * further entities (records) as a tree structure.
 * <p>
 * A new record tree dialog can be created by means of: <br>
 * <li>
 * {@link de.tif.jacob.screen.IClientContext#createRecordTreeDialog(IGuiElement, IDataTableRecord, IRelationSet, Filldirection, IRecordTreeDialogCallback)}
 * 
 * @author Andreas Herz
 */
public interface IRecordTreeDialog extends IDialog
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IRecordTreeDialog.java,v 1.1 2007/01/19 09:50:44 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Sets a new label/image provider for this record tree dialog.
   * <p>
   * The <code>IRecordTreeDialog</code> implementation ensures that the given
   * label provider is connected to this dialog and the former label provider is
   * disconnected from this dialog.
   * 
   * @param provider
   *          the new label provider or <code>null</code> if none
   */
  public void setLabelProvider(ILabelProvider provider);

  /**
   * Set the behaviour of the dialog window, if the user clicks on an entity in
   * the tree. The default is <code>true</code>. This means, that the dialog
   * window will be closed automatically, if the user selects an entity in the
   * tree.<br>
   * <br>
   * The dialog window will not be closed, if the flag is <code>false</code>.<br>
   * 
   * @param closeOnClick
   */
  public void setAutoclose(boolean closeOnClick);

  /**
   * Returns the behaviour of the dialog window, if the user clicks on an entity
   * in the tree.
   * 
   * @return <code>true</code> if the dialog window is automatically closed,
   *         otherwise <code>false</code>.
   */
  public boolean getAutoclose();
}
