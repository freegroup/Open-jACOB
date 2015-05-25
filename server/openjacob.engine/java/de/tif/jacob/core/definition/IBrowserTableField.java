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
 * This interface represents a table browser field. A table browser field is
 * related to a table field of a given table alias.
 * 
 * @author Andreas Sonntag
 */
public interface IBrowserTableField extends IBrowserField
{
  /**
   * Returns the related table alias.
   * 
   * @return the related table alias.
   */
  public ITableAlias getTableAlias();

  /**
   * Returns the related table field.
   * 
   * @return the related table field.
   */
  public ITableField getTableField();
  
  /**
   * The sort order of the browser table field. If no sort order exists,
   * {@link SortOrder#NONE} will be returned.
   * 
   * @return the sort order
   */
  public SortOrder getSortorder();
}
