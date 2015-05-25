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
 * Base interface for URL dialog implementations. <br>
 * URL dialogs are used to display jACOB (application) internal or external web
 * pages.
 * <p>
 * A new URL dialog can be created by means of: <br>
 * <li>{@link de.tif.jacob.screen.IClientContext#createUrlDialog(String)}
 * 
 * @author Andreas Herz
 */
public interface IUrlDialog extends IDialog
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IUrlDialog.java,v 1.2 2010/07/05 13:04:54 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.2 $";

  /**
   * Enables or disables client browser navigation, i.e. client browser menu and
   * toolbar are switched on or off. <br>
   * By default navigation is switched off!
   * 
   * @param navigation
   *          <code>true</code> to switch navigation on, otherwise
   *          <code>false</code>
   */
  public void enableNavigation(boolean navigation);

  /**
   * Enables or disables dialog modality. <br>
   * By default modality is switched off!
   * <p>
   * Note: Not all dialog implementation might support dialog modality!
   * 
   * @param modal
   *          <code>true</code> to switch modality on, otherwise
   *          <code>false</code>
   */
  public void setModal(boolean modal);
  
  /**
   * Enable the scrollbar of the new HTML window.
   * <code>false</code> is default.
   * 
   * @param scrollbar
   */
  public void enableScrollbar(boolean scrollbar);
  
  /**
   * Set this flag to true if the dialog should avoid to open a new windows. The existing window will
   * be used in this case. Usefull for jACOB application internal JSP pages. 
   * 
   * @param flag
   */
  public void useMainWindow(boolean flag);
}
