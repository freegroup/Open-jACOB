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

import de.tif.jacob.screen.IClientContext;

/**
 * 
 * @see IMessageDialog
 * @author Andreas Herz
 */
public interface IMessageDialogCallback
{
  static public final String RCS_ID = "$Id: IMessageDialogCallback.java,v 1.1 2010/11/12 11:23:18 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * This method will be invoked, if the user has close the dialog.
   * 
   * @param context
   *          The current client context
   * @since 2.10
   */
  public void onClose(IClientContext context) throws Exception;

}
