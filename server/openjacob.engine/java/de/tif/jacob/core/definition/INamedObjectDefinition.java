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

import java.util.Map;

/**
 * This interface is an abstract representation of named definition elements.
 * 
 * @author Andreas Sonntag
 */
public interface INamedObjectDefinition
{
  /**
   * Returns the name of this definition element.
   * 
   * @return the name
   */
  public String getName();

  /**
   * Returns the description of this definition element.
   * <p>
   * Note: The description could be internationalized, if the description starts with
   * a '%' character. In this case the description is interpreted as a key to the
   * respective application resource bundles for internationalization.
   * 
   * @return the element description or <code>null</code>, if no description
   *         exists for this element
   */
  public String getDescription();

  /**
   * Returns an additional property value of this definition element.
   * <br>
   * Note that this method is currently for internal use only!
   * 
   * @param name
   *          the name of the property
   * @return the value of the property or <code>null</code> if not existing
   */
  public String getProperty(String name);

  /**
   * Returns all additional property value of this definition element.
  * 
   * @since 2.10
  */
  public Map getProperties();
}
