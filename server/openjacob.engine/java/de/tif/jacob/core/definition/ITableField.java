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
 * This interface represents the definition of a field within a data source table.
 * 
 * @author Andreas Sonntag
 */
public interface ITableField extends INamedObjectDefinition
{
  /**
   * Returns the database name, i.e. the name of the respective database column,
   * of the table field.
   * 
   * @return the database name
   */
  public String getDBName();
  
  /**
   * Returns the label of this table field.
   * <p>
   * Note: The label could be internationalized, if the label starts with a '%'
   * character. In this case the label is interpreted as a key to the respective
   * application resource bundles for internationalization.
   * 
   * @return the label of the table field
   */
  public String getLabel();
  
  /**
   * Returns the column field index of this table field.
   * 
   * @return the column field index
   */
  public int getFieldIndex();
  
  /**
   * Returns the table definition of this table field.
   * 
   * @return the table definition
   */
  public ITableDefinition getTableDefinition();
  
  /**
   * Checks whether this table field is required or not. The value of a required
   * table should never be <code>null</code> or to be more precise, a table
   * record with empty required table field value could not be committed to the
   * data source.
   * 
   * @return <code>true</code> if required, otherwise <code>false</code>
   */
  public boolean isRequired();
  
  /**
   * Checks whether this table field is readonly or not. A readonly table field
   * could (by default) not be explicitly set nor changed by user interaction.
   * Nevertheless, it could be implicitly modified by means of jACOB hooks.
   * 
   * @return <code>true</code> if readonly, otherwise <code>false</code>
   */
  public boolean isReadOnly();
  
  /**
   * Returns the type of this table field.
   * <p>
   * Note that this method is currently for internal use only!
   * 
   * @return the table field type
   */
  public FieldType getType();
  
  /**
   * Checks whether this table field is enabled for history, i.e. changing the
   * field value should be logged in the change history of the corresponding
   * data table record.
   * 
   * @return <code>true</code> if enabled for history, otherwise
   *         <code>false</code>
   */
  public boolean isEnabledForHistory();
  
  /**
   * Convenient method to determine whether the given field is part of a foreign
   * key.
   * 
   * @return the foreign key this table field is part of or <code>null</code>
   *         if no such foreign key exists.
   */
  public IKey getMatchingForeignKey();
  
  /**
   * Convenient method to determine whether the given field belongs to
   * the primary key of the corresponding table.
   * 
   * @return <code>true</code> field is part of the primary key, otherwise <code>false</code> 
   */
  public boolean isPrimary();
}
