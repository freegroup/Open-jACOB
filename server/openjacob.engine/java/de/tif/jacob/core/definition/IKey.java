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

import de.tif.jacob.core.data.IDataKeyValue;

/**
 * This interface represents an abstraction of data source table keys. Keys could
 * be either of the following: <br>
 * <li>a primary key
 * <li>a foreign key
 * <li>an unique or non-unique index
 * 
 * @author Andreas Sonntag
 */
public interface IKey extends INamedObjectDefinition
{
	/**
	 * Returns the name of this key.
	 * 
	 * @return the key name
	 */
  public String getName();
  
	/**
	 * Returns the name of this table within the physical database.
	 * 
	 * @return the database name.
	 */
  public String getDBName();
  
  /**
   * Returns the type of this key.
   * 
   * @return the key type
   */
  public KeyType getType();
  
  /**
   * Returns the corresponding table definition of this key.
   * 
   * @return the table definition
   */
  public ITableDefinition getTableDefinition();
  
	/**
	 * Returns the list of table fields this key exists of.
	 * 
	 * @return List of {@link ITableField}
	 */
	public List<ITableField> getTableFields();
  
  /**
   * Convenient method to check whether the given table field is part of this
   * key.
   * 
   * @param field
   *          the table field
   * @return <code>true</code> if the given table field is a part of this key,
   *         otherwise <code>false</code>
   */
  public boolean contains(ITableField field);
  
  /**
   * Converts a given key value string representation to a key value object.
   * <p>
   * Note: Such a key value string representation has to be obtained by calling
   * {@link #convertKeyValueToString(IDataKeyValue)}.
   * 
   * @param keyvalueStr
   *          the key value string representation
   * @return the key value object
   * @throws IllegalArgumentException
   *           if key and key value string are not compatible
   * @since 2.10
   */
  public IDataKeyValue convertStringToKeyValue(String keyvalueStr) throws IllegalArgumentException;

  /**
   * Converts a given key value to a string representation.
   * <p>
   * This string representation can be converted back by means of
   * {@link #convertStringToKeyValue(String)}.
   * <p>
   * Note: Do not use {@link IDataKeyValue#toString()}, if the resulting string
   * should be re-converted to a key value object!
   * 
   * @param keyvalue
   *          the key value object
   * @return the key value string representation
   * @throws IllegalArgumentException
   *           if key and key value are not compatible
   * @since 2.10
   */
  public String convertKeyValueToString(IDataKeyValue keyvalue) throws IllegalArgumentException;
}
