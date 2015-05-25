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
 * This interface represents a N:M relation. Many to many relations are always
 * buildup by means of two 1:N relations linking the two table aliases via an
 * intermediate table (alias).
 * 
 * @author Andreas Sonntag
 */
public interface IManyToManyRelation extends IRelation
{
  /**
   * Returns the table alias of the intermediate table.
   * 
   * @return the intermediate table alias
   */
  public ITableAlias getIntermediateTableAlias();

  /**
   * Returns the from 1:N relation, i.e. the relation linking the from table
   * alias with the intermediate table alias.
   * 
   * @return the from relation.
   */
  public IOneToManyRelation getFromRelation();

  /**
   * Returns the to 1:N relation, i.e. the relation linking the to table alias
   * with the intermediate table alias.
   * 
   * @return the from relation.
   */
  public IOneToManyRelation getToRelation();
}
