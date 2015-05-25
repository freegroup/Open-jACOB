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

import java.util.List;

/**
 * This interface represents a group definition.
 *  
 * @author Andreas Sonntag
 */
public interface IGroupDefinition extends INamedObjectDefinition
{
  /**
   * Returns the table alias this group definition is related to.
   * 
   * @return the related table alias
   */
  public ITableAlias getTableAlias();
  
  /**
   * Returns the browser definition to be used for the related search browser of
   * this group definition.
   * 
   * @return the browser definition
   */
  public IBrowserDefinition getActiveBrowserDefinition();
  
	/**
   * Returns the label of this group definition.
   * <p>
   * Note: The label could be internationalized, if the label starts with a '%'
   * character. In this case the label is interpreted as a key to the respective
   * application resource bundles for internationalization.
   * 
   * @return the browser label
   */
  public String getLabel();
  
  /**
   * Returns all context menu entries of this group definition.
   * 
   * @return List of {@link IContextMenuEntry}
   */
  public List getContextMenuEntries();
  
  /**
   * Returns all selection action entries for the related search browser.
   * 
   * @return List of {@link ISelectionActionDefinition}
   */
  public List getSelectionActions();

  /**
   * Returns all child GUI element definitions of this group definition.
   * 
   * @return List of {@link IGUIElementDefinition}
   */
  public List getGUIElementDefinitions();
  

  /**
   * Indicates that the search browser should be hide if it contains
   * no data.
   *  
   * @return <code>true</code> if bordering is enabled, otherwise
   *         <code>false</code>.
   */
  public boolean hideEmptyBrowser();

}
