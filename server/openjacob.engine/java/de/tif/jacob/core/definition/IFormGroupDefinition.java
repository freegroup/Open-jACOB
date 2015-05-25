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
 * This interface represents a domain definition.
 *  
 * @author Andreas Sonntag
 */
public interface IFormGroupDefinition extends INamedObjectDefinition
{

  /**
   * Returns the list of all form definitions of this form group.
   * 
   * @return List of {@link IFormDefinition}
   */
  public List getFormDefinitions();
  

  /**
   * Returns the title of this group.
   * <p>
   * Note: The title could be internationalized, if the title starts with a '%'
   * character. In this case the title is interpreted as a key to the respective
   * application resource bundles for internationalization.
   * 
   * @return the form group title.
   */
  public String getTitle();
}
