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
package de.tif.jacob.core.adjustment.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import de.tif.jacob.core.adjustment.IBooleanFieldTypeAdjustment;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.BooleanFieldType;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class BooleanAdjustment implements IBooleanFieldTypeAdjustment
{

 
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertObjectToDataValue(java.lang.Object, java.lang.Object)
   */
  public final Object convertObjectToDataValue(Object object, Object oldValue)
  {
    // we should never reach this point
    throw new IllegalStateException();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertSqlValueToDataValue(de.tif.jacob.core.data.impl.sql.SQLDataSource, java.sql.ResultSet, int)
   */
  public Object convertSQLValueToDataValue(FieldType fieldType, SQLDataSource datasource, ResultSet rs, int index) throws SQLException
  {
    int intValue = rs.getInt(index);
    if (rs.wasNull())
      return null;
    // boolean logic, i.e. FALSE if 0, otherwise TRUE
    return Boolean.valueOf(intValue != 0);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#getInitDataValue(de.tif.jacob.core.data.IDataSource, de.tif.jacob.core.definition.ITableDefinition, de.tif.jacob.core.definition.ITableField, java.lang.Object)
   */
  public final Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, Object defaultValue)
  {
    // we should never reach this point
    throw new IllegalStateException();
  }
  
  public final int getSQLDecimalDigits(FieldType fieldType, ISQLDataSource dataSource)
  {
    // irrelevant
    return 0;
  }
  
  public int getSQLSize(FieldType fieldType, ISQLDataSource dataSource)
  {
    // irrelevant
    return 0;
  }
  
  public int getSQLType(FieldType fieldType, ISQLDataSource dataSource)
  {
    return Types.BOOLEAN;
  }
  
  public String getSQLDefaultValue(FieldType fieldType, ISQLDataSource dataSource)
  {
    return ((BooleanFieldType) fieldType).getDefaultValue().toString();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValue(de.tif.jacob.core.definition.FieldType, de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
   */
  public void setSQLValue(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
    statement.setString(index, (String) dataValue); 
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValueForInsert(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
   */
  public void setSQLValueForInsert(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
    statement.setString(index, (String) dataValue); 
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValueForUpdate(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
   */
  public void setSQLValueForUpdate(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
    statement.setString(index, (String) dataValue); 
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#deleteFieldValue(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.lang.Object)
   */
  public void deleteFieldValue(SQLExecutionContext context, Object dataValue) throws SQLException
  {
    // do nothing here
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#deleteFieldValues(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.lang.String)
   */
  public void deleteFieldValues(SQLExecutionContext sqlContext, String embeddedSelectFragment)
  {
    // do nothing here
  }
}
