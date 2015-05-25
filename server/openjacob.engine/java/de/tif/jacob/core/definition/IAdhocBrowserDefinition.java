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
 * This interface represents an ad-hoc browser definition. In contrast to normal
 * browser definition ad-hoc browser definition are mutual and not predefined at
 * design-time. Ad-hoc browser definitions could be used to create dynamic
 * result sets when performing a query on a table alias. To do something like
 * that a corresponding {@link de.tif.jacob.core.data.IDataBrowser} has to be
 * requested by means of
 * {@link de.tif.jacob.core.data.IDataAccessor#createBrowser(IAdhocBrowserDefinition)}.
 * 
 * @author Andreas Sonntag
 */
public interface IAdhocBrowserDefinition extends IBrowserDefinition
{
  /**
   * Returns the list of all browser fields.
   * <p>
   * Note: Browser fields are always of type {@link IBrowserTableField}
   * 
   * @return List of {@link IBrowserTableField}
   * 
   * @see IBrowserDefinition#getBrowserFields()
   * @deprecated use {@link #getBrowserField(int)} and {@link IBrowserDefinition#getFieldNumber()} instead.
   */
  public List getBrowserFields();

  /**
   * Removes the browser field located at the given index.
   * 
   * @param index
   *          the index of the browser field to remove
   * 
   * @return the browser table field which has just been removed
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= index < {@link #getFieldNumber()}</code> is not
   *           fulfilled
   */
  public IBrowserTableField removeBrowserField(int index) throws IndexOutOfBoundsException;

  /**
   * Returns the number of browser fields of this browser definition.
   * 
   * @return the number of browser fields
   */
  public int getFieldNumber();

  /**
   * Adds a new browser table field to this browser definition, which will be
   * appended to already existing browser fields.
   * 
   * @param tableAliasName
   *          the name of the table alias
   * @param tableFieldName
   *          the name of the table field
   * @param label
   *          the label of the browser field
   * @param sortOrder
   *          the sort order of the browser field
   * @throws NoSuchFieldException
   *           if no such table field exists for the given table alias
   * @deprecated use {@link #addBrowserField(String, String, SortOrder, String)}
   *             instead, which additionally returns a unique browser name
   */
  public void addBrowserField(String tableAliasName, String tableFieldName, String label, SortOrder sortOrder) throws NoSuchFieldException;

  /**
   * Adds a new browser table field to this browser definition, which will be
   * inserted at the given position index.
   * 
   * @param index
   *          the index to insert the new browser table field
   * @param tableAliasName
   *          the name of the table alias
   * @param tableFieldName
   *          the name of the table field
   * @param label
   *          the label of the browser field
   * @param sortOrder
   *          the sort order of the browser field
   * @throws NoSuchFieldException
   *           if no such table field exists for the given table alias
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= index < {@link #getFieldNumber()}</code> is not
   *           fulfilled
   * @deprecated use {@link #addBrowserField(int, String, String, SortOrder, String)}
   *             instead, which additionally returns a unique browser name
   */
  public void addBrowserField(int index, String tableAliasName, String tableFieldName, String label, SortOrder sortOrder) throws NoSuchFieldException,
      IndexOutOfBoundsException;

  /**
   * Adds a new browser table field to this browser definition, which will be
   * appended to already existing browser fields.
   * 
   * @param tableAliasName
   *          the name of the table alias
   * @param tableFieldName
   *          the name of the table field
   * @param sortOrder
   *          the sort order of the browser field
   * @param label
   *          the label of the browser field
   * @return the unique browser field name of the created/added browser field, which
   *         can be used by means of
   *         {@link IBrowserDefinition#getBrowserField(String)}
   * @throws NoSuchFieldException
   *           if no such table field exists for the given table alias
   * @since 2.7.2
   */
  public String addBrowserField(String tableAliasName, String tableFieldName, SortOrder sortOrder, String label) throws NoSuchFieldException;

  /**
   * Adds a new browser table field to this browser definition, which will be
   * inserted at the given position index.
   * 
   * @param index
   *          the index to insert the new browser table field
   * @param tableAliasName
   *          the name of the table alias
   * @param tableFieldName
   *          the name of the table field
   * @param sortOrder
   *          the sort order of the browser field
   * @param label
   *          the label of the browser field
   * @return the unique browser field name of the created/added browser field, which
   *         can be used by means of
   *         {@link IBrowserDefinition#getBrowserField(String)}
   * @throws NoSuchFieldException
   *           if no such table field exists for the given table alias
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= index < {@link #getFieldNumber()}</code> is not
   *           fulfilled
   * @since 2.7.2
   */
  public String addBrowserField(int index, String tableAliasName, String tableFieldName, SortOrder sortOrder, String label) throws NoSuchFieldException,
      IndexOutOfBoundsException;
}
