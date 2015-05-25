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

import java.util.Set;

/**
 * This interface represents a relation set. Relation sets are used to restrict
 * existing QBE constrains then performing a query on a
 * {@link de.tif.jacob.core.data.IDataBrowser}. Additionally they are used to
 * determine how far a record propagation should be executed. See
 * {@link de.tif.jacob.core.data.IDataAccessor#propagateRecord(IDataRecord, IRelationSet, Filldirection)}.  
 * 
 * @author Andreas Sonntag
 */
public interface IRelationSet extends INamedObjectDefinition
{
  /**
   * The name of the default relation set.
   * 
   * @see IApplicationDefinition#getDefaultRelationSet()
   */
  public static final String DEFAULT_NAME = "default";
  
  /**
   * The name of the local relation set.
   * 
   * @see IApplicationDefinition#getLocalRelationSet()
   */
  public static final String LOCAL_NAME = "local";
  
	/**
	 * Returns all relations within this relation set.
	 * 
	 * @return Set of {@link IRelation}
	 */
	public Set getRelations();

	/**
	 * Returns all table aliases, which are related to the given alias.
	 * <p>
	 * Remark: The given initial table alias will be contained within the result
	 * set as well.
	 * 
	 * @param initialTableAlias
	 *          initial table alias to determine related table aliases of
	 * @return Set of {@link ITableAlias}
	 */
	public Set getRelatedTableAliases(ITableAlias initialTableAlias);
  
  /**
   * Checks whether the given relation set is the local relation set.
   * 
   * @return <code>true</code> if this is the local relation set otherwise <code>false</code> 
   */
  public boolean isLocal();
}
