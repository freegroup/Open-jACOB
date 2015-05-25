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
 * This interface represents the definition of a browser, which could be used
 * for either search or inform browsers.
 * 
 * @author Andreas Sonntag
 */
public interface IBrowserDefinition extends INamedObjectDefinition
{
  /**
   * Returns the associated table alias.
   * 
   * @return the associated table alias
   */
  public ITableAlias getTableAlias();
  
  /**
   * Returns the definitions of all browser fields of this browser.
   * 
   * @return List of {@link IBrowserField}
   * @deprecated used {@link #getBrowserField(int)} and {@link #getFieldNumber()} instead.
   */
  public List getBrowserFields();
  
  /**
   * Returns the number of browser fields of this browser definition.
   * 
   * @return the number of browser fields
   * @since 2.7.2
   */
  public int getFieldNumber();

  /**
   * Returns the definition of the browser field specified by name.
   * 
   * @param index
   *          the browser field index
   * @return the browser field definition
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= index < {@link #getFieldNumber()}</code> is not
   *           fulfilled
   * @since 2.7.2
   */
  public IBrowserField getBrowserField(int index) throws IndexOutOfBoundsException;

  /**
   * Returns the definition of the browser field specified by name.
   * 
   * @param browserFieldName
   *          the name of the browser field
   * @return the browser field definition
   * @throws NoSuchFieldException
   *           if no browser field of the specified name exists for this browser
   */
  public IBrowserField getBrowserField(String browserFieldName) throws NoSuchFieldException;
  
  /**
   * Returns the connectBY key for a TreeBrowser.
   * 
   * @return the connectBY key
   */
  public IKey getConnectByKey();
 }
