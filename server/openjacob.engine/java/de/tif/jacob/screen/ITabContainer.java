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

package de.tif.jacob.screen;

import java.util.List;

/**
 * @author Andreas Herz
 *
 */
public interface ITabContainer extends IContainer
{
  static public final String RCS_ID = "$Id: ITabContainer.java,v 1.11 2011/02/15 08:13:34 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.11 $";

  
  /**
   * Returns the current active (visible) tab of the tab container.
   * 
   * @return the active pane
   * @since 2.8
   */
  public ITabPane getActivePane();

  /**
   * Retrieve the tab panes of this group
   * 
   * @since 2.7.2
   * @return List[ITabPane]
   */
  public List<ITabPane> getPanes();
  
  /**
   * Retrieve the tab pane with the given index
   * 
   * @since 2.8
   * @return the TabPane with the given index
   */
  public ITabPane getPane(int index);

  /**
   * Retrieve the first tab pane with the given name. Multiple
   * panes with the same name can be stored in the TabContainer.
   * 
   * @since 2.8
   * @return the first pane with the given name or <code>null</code>, if no pane with the given name exists
   */
  public ITabPane getPane(String paneName);

  /**
   * Hides the TabStrip and the border of the TabContainer.
   * This method is useful for a hook-driven Tab handling.
   * 
   * @since 2.7.2
   */
  public void hideTabStrip(boolean flag);
}
