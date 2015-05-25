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

import de.tif.jacob.core.definition.guielements.InputFieldDefinition;

/**
 * This interface represents a browser field. A browser field defines a row in a browser. 
 * 
 * @author Andreas Sonntag
 */
public interface IBrowserField extends INamedObjectDefinition
{
	/**
   * Returns the default visibility of this browser field.
   * 
	 * @return <code>true</code> if visible otherwise <code>false</code> 
	 */
	public boolean isVisible();
  
  /**
   * Returns whether this browser field can be configured by the user, i.e. show or hide.
   * 
   * @return <code>true</code> if configurable otherwise <code>false</code> 
   */
  public boolean isConfigureable();
 
  /**
   * Returns the label of this browser field.
   * <p>
   * Note: The label could be internationalized, if the label starts with a '%'
   * character. In this case the label is interpreted as a key to the respective
   * application resource bundles for internationalization.
   * 
   * @return the browser label
   */
	public String getLabel();
  
  /**
   * Returns the input field definition, which should be used to edit this field
   * then used in an inform browser.
   * <p>
   * Note that this method is currently for internal use only!
   * 
   * @return input field definition
   */
  public InputFieldDefinition getInputFieldDefinition();
  
  /**
   * Returns the column field index of this browser field.
   * 
   * @return the column field index
   */
  public int getFieldIndex();
}
