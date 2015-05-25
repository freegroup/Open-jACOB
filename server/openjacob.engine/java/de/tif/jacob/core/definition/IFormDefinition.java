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
 * This interface represents a form definition.
 *  
 * @author Andreas Sonntag
 */
public interface IFormDefinition extends INamedObjectDefinition
{
	/**
   * Returns the label of this form.
   * <p>
   * Note: The label could be internationalized, if the label starts with a '%'
   * character. In this case the label is interpreted as a key to the respective
   * application resource bundles for internationalization.
   * 
   * @return the browser label
   */
  public String getLabel();
  
  /**
   * Returns [true] whenever the TabStrip of the search browser should be hidden.
   * @since 2.8.0
   * @return <code>true</code> if the tap strip is hidden, otherwise <code>false</code> 
   */
  public boolean hideSearchBrowserTabStrip();
  
  /**
   * Returns the list of all group definitions of this form.
   * 
   * @return List of {@link IGroupDefinition}
   */
  public List getGroupDefinitions();

  /**
   * Returns the default visibility of this form.
   * 
   * @since 2.9
   * @return <code>true<code> if visible otherwise <code>false<code> 
   */
  public boolean isVisible();
}
