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
 * Base interface for confirmatiom dialog implementations. <br>
 * Confirmation dialogs are used to request an explicit user acknowledgment for a certain operation.
 * <p>
 * A new confirmation dialog can be created by means of: <br>
 * <li>
 * {@link de.tif.jacob.screen.IClientContext#createOkCancelDialog(String, IOkCancelDialogCallback)}
 * <li>
 * {@link de.tif.jacob.screen.IClientContext#createOkCancelDialog(Message, IOkCancelDialogCallback)}
 * 
 * @author Andreas Herz
 */
public interface IOkCancelDialog extends IDialog
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IOkCancelDialog.java,v 1.3 2009/02/24 22:00:05 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.3 $";

  /**
   * {@inheritDoc}
   */
  public void show();
  
  /**
   * Override the default label of the <b>Ok</b> Button with a custom label.<br>
   * 
   * @since 2.8.4
   * @param context
   * @param label the new label for the <b>Ok</b> button
   */
  public void setOkButtonLabel(IClientContext context, String label);
  
  /**
   * Override the default label of the <b>Cancel</b> Button with a custom label.<br>
   * 
   * @since 2.8.4
   * @param context
   * @param label the new label for the <b>Cancel</b> button
   */
  public void setCancelButtonLabel(IClientContext context, String label);
}
