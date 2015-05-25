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
 * Interface which has to be implemented by callback handlers for ask dialogs,
 * i.e. single value request dialogs.
 * 
 * @see IAskDialog
 * @author Andreas Herz
 */
public interface IAskDialogCallback
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IAskDialogCallback.java,v 1.1 2007/01/19 09:50:44 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * This method will be invoked, if the user has entered a value and has
   * clicked on the OK button.
   * 
   * @param context
   *          The current client context
   * @param value
   *          The value entered. If no value has been entered, an empty string
   *          will be handed over.
   */
  public void onOk(IClientContext context, String value) throws Exception;

  /**
   * This method will be invoked, if the user has clicked on the cancel button.
   * 
   * @param context
   *          The current client context
   */
  public void onCancel(IClientContext context) throws Exception;
}
