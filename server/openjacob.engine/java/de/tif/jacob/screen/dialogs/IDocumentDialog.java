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

/**
 * Base interface for document dialog implementations. <br>
 * Document dialogs are used to view, download and print documents, which are
 * either loaded from a datasource or created on the fly. the respective row.
 * <p>
 * A new document dialog can be created by means of: <br>
 * <li>
 * {@link de.tif.jacob.screen.IClientContext#createDocumentDialog(DataDocumentValue)}
 * <li>
 * {@link de.tif.jacob.screen.IClientContext#createDocumentDialog(String, DataDocumentValue)}
 * <li>
 * {@link de.tif.jacob.screen.IClientContext#createDocumentDialog(String, String, byte[])}
 * 
 * @author Andreas Herz
 */
public interface IDocumentDialog extends IDialog
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IDocumentDialog.java,v 1.1 2007/01/19 09:50:44 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Enforces the printing of the document, i.e. a printer select dialog will be
   * popped up after the document has been downloaded.
   * <p>
   * Note: By default printing is not enforced.
   * 
   * @param flag
   *          <code>true</code> to enforce printing, otherwise
   *          <code>false</code>
   */
  public void enforcePrinting(boolean flag);

  /**
   * Returns whether printing of the document is enforced or not.
   * 
   * @return <code>true</code> if printing is enforced, otherwise
   *         <code>false</code>
   */
  public boolean isEnforcePrinting();
}
