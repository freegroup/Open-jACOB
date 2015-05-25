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
 * Base interface for all dialog implementations.
 * <p>
 * Specific dialogs are created by the respective creation method of
 * {@link de.tif.jacob.screen.IClientContext}, e.g.
 * {@link de.tif.jacob.screen.IClientContext#createMessageDialog(String)}.
 * 
 * @author Andreas Herz
 */
public interface IDialog
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IDialog.java,v 1.1 2007/01/19 09:50:44 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Displayes (shows) the dialog by means of a popup client window.
   * <p>
   * Note: If neither one of the <code>show(..)</code> methods is invoked, the
   * dialog will <b>not </b> be shown to the user!
   * 
   * @param width
   *          preferred dialog width in pixels
   * @param height
   *          preferred dialog height in pixels
   */
  public void show(int width, int height);

  /**
   * Displayes (shows) the dialog by means of a popup client window.
   * <p>
   * Note: If neither one of the <code>show(..)</code> methods is invoked, the dialog will <b>not
   * </b> be shown to the user!
   */
  public void show();
}
