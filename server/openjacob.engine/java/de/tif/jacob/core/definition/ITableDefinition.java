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

import java.util.Iterator;
import java.util.List;

/**
 * This interface represents the definition of data source table.
 * 
 * @author Andreas Sonntag
 */
public interface ITableDefinition extends INamedObjectDefinition
{
	/**
	 * Returns the name of this table.
	 * 
	 * @return the table name
	 */
	public String getName();

	/**
	 * Returns the name of this table within the physical database.
	 * 
	 * @return the database name.
	 */
	public String getDBName();

	/**
	 * Returns the primary key of this table.
	 * 
	 * @return the primary key or <code>null</code>, if the table has not
	 *         defined a primary key.
	 */
	public IKey getPrimaryKey();

	/**
	 * Returns the definition of the table field specified by name.
	 * 
	 * @param tableFieldName
	 *          the name of the table field
	 * @return the table field definition
	 * @throws NoSuchFieldException
	 *           if no table field of the specified name exists for this table
	 */
	public ITableField getTableField(String tableFieldName) throws NoSuchFieldException;
	
	/**
	 * Returns the definition of the table field specified by its database name.<p>
	 * 
	 * Note: Database field names are handled case insensitive!
	 * 
	 * @param dbTableFieldName
	 *          the database name of the table field
	 * @return the table field definition
	 * @throws NoSuchFieldException
	 *           if no table field of the specified name exists for this table
	 */
	public ITableField getTableFieldByDBName(String dbTableFieldName) throws NoSuchFieldException;
	
  /**
   * Checks whether this table contains a table field specified by its database name.<p>
   * 
   * Note: Database field names are handled case insensitive!
   * 
   * @param dbTableFieldName
   *          the database name of the table field
   * @return <code>true</code> if the table field belongs to this table,
   *         otherwise <code>false</code>
   * @since 2.8.7         
   */
  public boolean hasTableFieldByDBName(String dbTableFieldName);

  /**
   * Checks whether this table contains a table field of the given name.
   * 
   * @param tableFieldName
   *          the table field name to check
   * @return <code>true</code> if the table field belongs to this table,
   *         otherwise <code>false</code>
   */
  public boolean hasTableField(String tableFieldName);

	/**
	 * Checks whether this table contains the given table field.
	 * 
	 * @param tableField
	 *          the table field to check
	 * @return <code>true</code> if the table field belongs to this table,
	 *         otherwise <code>false</code>
	 */
	public boolean hasTableField(ITableField tableField);

	/**
	 * Returns the definitions of all table fields of this table.
	 * 
	 * @return <code>List</code> of {@link ITableField}
	 */
	public List getTableFields();

	/**
	 * Returns the key specified by name
	 * 
	 * @param keyName
	 *          the name of the key
	 * @return the specified key
	 */
	public IKey getKey(String keyName);

	/**
	 * Checks whether this table contains a key of the given name.
	 * 
	 * @param keyName
	 *          the key name to check
	 * @return <code>true</code> if the key belongs to this table,
	 *         otherwise <code>false</code>
	 */
	public boolean hasKey(String keyName);

	/**
	 * Checks whether this table contains the given key.
	 * 
	 * @param key
	 *          the key to check
	 * @return <code>true</code> if the key belongs to this table,
	 *         otherwise <code>false</code>
	 */
	public boolean hasKey(IKey key);

	/**
	 * Returns the keys defined for this table.
	 * 
	 * @return <code>Iterator</code> of {@link IKey}
	 */
	public Iterator getKeys();

  /**
   * Returns all table aliases ordered by name of this table.
   * 
   * @return List of {@link ITableAlias}
   * @since 2.10
   */
  public List getTableAliases();

	/**
	 * Returns the name of the data source of this table.
	 * 
	 * @return the data source name
	 */
	public String getDataSourceName();

	/**
	 * Checks whether this table is an internal table.
	 * 
	 * @return <code>true</code> for internal tables, otherwise <code>false</code>
	 */
	public boolean isInternal();
  
  /**
   * Checks whether locking is omitted for entries of this table.
   * <p>
   * Note: This feature should only be used, if modification performance is
   * really a matter and unsynchronized modification is not a matter.
   * 
   * @return <code>true</code> if locking is omitted, otherwise
   *         <code>false</code>
   * @since 2.6
   */
  public boolean isOmitLocking();

  /**
   * Returns [true] if the application programmer marked this table as
   * linked table. Usefull for "introspection" in an table hook.<br>
   * This mark will not be set automaticly. This must be done explicit
   * by the programmer in the jACOB Table Editor.
   * 
   * @return true if the table is a link table
   * @since 2.9.2
   */
  public boolean isMtoNTable();
  
  /** 
   * Returns <code>true</code>, if the records of this table are always deletable.<br>
   * The application programmer must introspect this flag in an TableHook.<br>
   * The core jACOB Engine didn't use this flag. It is only a hint for the app
   * programmer at the moment.
   * 
   * @return
   * @since 2.9.2
   */
  public boolean isRecordsAlwaysDeletable();
  
	/**
	 * Returns the table field which represents a record (row) of this table.
	 * <p>
	 * A representative field must not necessarily be unique, but should denote a
	 * record in a human understandable manner. E.g. the representative field of
	 * the table "person" is "fullname" not "personid".
	 * 
	 * @return the representative field or <code>null</code>, if no such field exists.
	 */
	public ITableField getRepresentativeField();

	/**
	 * Returns the history field (Long text field) which is used to keep the
	 * change history information.
	 * 
	 * @return the history field or <code>null</code> if no history field
	 *         exists.
	 */
	public ITableField getHistoryField();
}
