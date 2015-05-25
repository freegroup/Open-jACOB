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
public interface IDomainDefinition extends INamedObjectDefinition
{
  /**
   * Returns the names of all role names, which have been granted access to this domain.
   * 
   * @return List of <code>String</code>
   */
  public List getRoleNames();
  
  /**
   * Returns the list of all form definitions of this domain.
   * 
   * @return List of {@link IFormDefinition}
   */
  public List getFormDefinitions();
  
  /**
   * Returns the list of all form group definitions of this domain.
   * 
   * @return List of {@link IFormGroupDefinition}
   */
  public List getFormGroupDefinitions();

  /**
   * Returns the title of this domain.
   * <p>
   * Note: The title could be internationalized, if the title starts with a '%'
   * character. In this case the title is interpreted as a key to the respective
   * application resource bundles for internationalization.
   * 
   * @return the domain title.
   */
  public String getTitle();
  
  /**
   * Returns the data scope of this domain definition.
   * 
   * @return the data scope or <code>null</code>, if this domain does not
   *         overwrite the default data scope definition of the application.
   * @see IApplicationDefinition#getDataScope()
   * @since 2.7.4
   */
  public DataScope getDataScope();

	/**
   * Returns the default visibility of this domain.
   * 
	 * @return <code>true<code> if visible otherwise <code>false<code> 
	 */
  public boolean isVisible();
  
  /**
   * All forms in this domain are visible if this method returns [true].
   * In the other case you must click on the domain to expand the UI navigation.
   * 
   * 
   * @return <code>true<code> if the user can expand/collapse the domain navigation entry 
   */
  public boolean canCollapse();
}
