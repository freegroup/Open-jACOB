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

package de.tif.jacob.core.definition;

/**
 * This interface represents the definition of a toolbar button.
 * 
 * @author Andreas Sonntag
 */
public interface IToolbarButtonDefinition
{
  /**
   * Returns the name of this toolbar button.
   * 
   * @return the toolbar button name.
   */
  public String getName();

  /**
   * Returns the label of this toolbar button.
   * 
   * @return the toolbar button label.
   */
  public String getLabel();

  /**
   * Returns the type of the underlying action to be performed when this toolbar
   * button is pressed.
   * <p>
   * Note that this method is currently for internal use only!
   * 
   * @return the action type
   */
  public ActionType getActionType();

  /**
   * Returns whether the toolbar button should be visible by default.
   * 
   * @return <code>true</code> if the toolbar button should by visible by
   *         default, otherwise <code>false</code>.
   */
  public boolean isVisible();
}
