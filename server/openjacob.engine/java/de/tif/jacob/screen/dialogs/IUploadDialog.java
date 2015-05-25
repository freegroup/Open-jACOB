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
 * Base interface for file upload dialog implementations. <br>
 * File upload dialogs are used to transfer a client side file onto the server.
 * <p>
 * A new file upload dialog can be created by means of: <br>
 * <li>
 * {@link de.tif.jacob.screen.IClientContext#createUploadDialog(IUploadDialogCallback)}
 * 
 * @author Andreas Herz
 */
public interface IUploadDialog extends IDialog
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IUploadDialog.java,v 1.1 2007/01/19 09:50:45 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * {@inheritDoc}
   */
  public void show();
}
