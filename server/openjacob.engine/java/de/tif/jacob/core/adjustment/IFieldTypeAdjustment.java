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
package de.tif.jacob.core.adjustment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface IFieldTypeAdjustment
{
  public Object convertSQLValueToDataValue(FieldType fieldType, SQLDataSource datasource, ResultSet rs, int index) throws SQLException;

	public Object convertObjectToDataValue(Object object, Object oldValue) throws IllegalArgumentException;

  public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, Object defaultValue);

  public void setSQLValue(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException;
  
	public void setSQLValueForInsert(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException;

	public void setSQLValueForUpdate(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException;

  public int getSQLType(FieldType fieldType, ISQLDataSource dataSource);
  
  public int getSQLSize(FieldType fieldType, ISQLDataSource dataSource);
  
  public int getSQLDecimalDigits(FieldType fieldType, ISQLDataSource dataSource);
  
  public String getSQLDefaultValue(FieldType fieldType, ISQLDataSource dataSource);

  public void deleteFieldValue(SQLExecutionContext context, Object dataValue) throws SQLException;
  
  /**
   * @param context
   * @param embeddedSelectFragment embedded select fragment, e.g. "SELECT abc FROM tab WHERE xyz=4"
   */
  public void deleteFieldValues(SQLExecutionContext context, String embeddedSelectFragment) throws SQLException;
}
