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
 * This interface represents a relation. Relations are used to link two table
 * aliases to each other.
 * 
 * @author Andreas Sonntag
 */
public interface IRelation extends INamedObjectDefinition
{
  /**
   * Returns the from table alias. The from table alias defines the "starting
   * point" of the relation. For 1:N relations the from table alias is always
   * the one side.
   * 
   * @return the from table alias
   */
  public ITableAlias getFromTableAlias();

  /**
   * Returns the to table alias. The to table alias defines the "end point" of
   * the relation. For 1:N relations the to table alias is always the N side.
   * 
   * @return the from table alias
   */
  public ITableAlias getToTableAlias();
}
