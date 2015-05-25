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

package de.tif.jacob.core.data;

import de.tif.jacob.core.definition.ITableDefinition;

/**
 * The data record id unambiguously identifies a data record in any table.
 * <p>
 * 
 * A data record id instance could be obtained by means of
 * {@link IDataRecord#getId()}.
 * 
 * @author andreas
 */
public interface IDataRecordId
{
	/**
   * Returns the primary key value of the referenced record.
   * 
   * @return the primary key value or <code>null</code> if no primary key
   *         exists for this type of record.
	 */
	public IDataKeyValue getPrimaryKeyValue();

	/**
	 * Returns the table definition of the referenced record.
	 * 
	 * @return the table definition.
	 */
	public ITableDefinition getTableDefinition();
}
