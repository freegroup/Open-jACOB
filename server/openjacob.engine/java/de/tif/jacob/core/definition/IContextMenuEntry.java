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
 * This interface represents the definition of a group context menu entry.
 * 
 * @author Andreas Sonntag
 */
public interface IContextMenuEntry extends INamedObjectDefinition 
{
	/**
   * Returns the label of this context menu entry.
   * <p>
   * Note: The label could be internationalized, if the label starts with a '%'
   * character. In this case the label is interpreted as a key to the respective
   * application resource bundles for internationalization.
   * 
   * @return the context menu entry label
   */
  public String getLabel();
  
  /**
   * Returns the type of the underlying action to be performed when this context
   * meun entry is selected.
   * <p>
   * Note that this method is currently for internal use only!
   * 
   * @return the action type
   */
  public ActionType getActionType();
  
  /**
   * Returns the default visibility of this context menu entry.
   * 
   * @return the default visibility
   */
  public boolean isVisible();
}
