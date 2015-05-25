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
/*
 * Created on 23.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.definition;

import java.util.Set;

/**
 * This interface represents a table alias condition. A table alias condition is
 * used to restrict queries performed on a
 * {@link de.tif.jacob.core.data.IDataTable} or
 * {@link de.tif.jacob.core.data.IDataBrowser} in such a way that only those
 * records are returned which semantically match to the corresponding table
 * alias.
 * 
 * @see ITableAlias#getCondition()
 * 
 * @author Andreas Sonntag
 */
public interface ITableAliasCondition
{
	/**
   * Returns the alias condition itself as a String.
   * 
   * @return the alias condition as String
   */
  public String toString();

  /**
   * Returns a set containing all foreign table aliases, which are referred
   * within this alias condition.
   * 
   * @return <code>Set</code> of {@link ITableAlias}
   */
  public Set getForeignAliases();
  
  /**
   * Determines whether the given alias should be treated as optional. This has
   * the effect that search calls considering this condition will result into an
   * outer join (instead of an inner join) regarding the given foreign table
   * alias.
   * 
   * @param alias
   *          the foreign table alias to check
   * @return <code>true</code> if the foreign table alias is optional,
   *         otherwise <code>false</code>.
   */
  public boolean isOptionalForeignAlias(ITableAlias alias);
}
