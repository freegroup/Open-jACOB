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
 * This interface represents a 1:N relation.
 * 
 * @author Andreas Sonntag
 */
public interface IOneToManyRelation extends IRelation
{
  /**
   * Returns the primary key of this relation, i.e. the primary key of the from
   * table alias.
   * 
   * @return the primary key
   */
  public IKey getFromPrimaryKey();

  /**
   * Returns the foreign key of this relation, i.e. the correspondng foreign key
   * of the to table alias.
   * 
   * @return the foreign key
   */
  public IKey getToForeignKey();

  /**
   * Checks whether this relation is mandatory on the one side, i.e. each entry
   * of the foreign table must be related to an entry of the primary table.
   * 
   * @return <code>true</code> if this relation is mandatory, otherwise
   *         <code>false</code> in case of an optional relation.
   */
  public boolean isMandatory();
}
