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

import de.tif.jacob.core.Version;

/**
 * Internal class to abstract an application provider, i.e. an instance which
 * could provide an application definition (design) from an external source,
 * e.g. from a JAD file or from ADL/ADF files.
 * 
 * @author Andreas Sonntag
 */
public interface IApplicationProvider
{
  /**
   * Returns the default application definition, if existing
   * 
   * @return the default application definition or <code>null</code> if not
   *         existing
   */
  public IApplicationDefinition getDefaultApplication();

  /**
   * Returns the application definition specified by a given name and version.
   * 
   * @param applicationName
   *          name of the application
   * @param version
   *          the version of the application
   * @return the application definition of the given name and version
   * @throws RuntimeException
   *           if no application with the given name and version exists
   */
  public IApplicationDefinition getApplication(String applicationName, Version version) throws RuntimeException;
}
