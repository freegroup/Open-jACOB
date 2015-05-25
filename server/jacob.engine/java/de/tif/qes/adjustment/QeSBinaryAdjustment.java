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

package de.tif.qes.adjustment;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import de.tif.jacob.core.adjustment.impl.BinaryAdjustment;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.data.impl.sql.WrapperPreparedStatement;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.qes.QeSInternalDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QeSBinaryAdjustment extends BinaryAdjustment
{
  static public final transient String RCS_ID = "$Id: QeSBinaryAdjustment.java,v 1.5 2009-01-14 13:36:37 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertSqlValueToDataValue(de.tif.jacob.core.data.impl.sql.SQLDataSource, java.sql.ResultSet, int)
   */
  public Object convertSQLValueToDataValue(FieldType fieldType, SQLDataSource datasource, ResultSet rs, int index) throws SQLException
	{
		if (datasource.needsMappingOfBinary(fieldType))
		{
			long longValue = rs.getLong(index);
			if (rs.wasNull())
				return new QeSBinary(datasource.getName());
			return new QeSBinary(datasource.getName(), longValue);
		}
		return super.convertSQLValueToDataValue(fieldType, datasource, rs, index);
	}
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertObjectToDataValue(java.lang.Object, java.lang.Object)
	 */
	public Object convertObjectToDataValue(Object object, Object oldValue) throws IllegalArgumentException
	{
    if (oldValue instanceof QeSBinary)
    {
      if (object == null || object instanceof byte[])
      {
        QeSBinary oldBinary = (QeSBinary) oldValue;
        
        // clone the array because it is mutable!
        if (object != null)
          object = ((byte[]) object).clone();
        return new QeSBinary(oldBinary.getDataSourceName(), oldBinary.getKey(), (byte[]) object);
      }
      throw new IllegalArgumentException(object.getClass().toString());
    }
    return super.convertObjectToDataValue(object, oldValue);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#getInitDataValue(de.tif.jacob.core.data.IDataSource, de.tif.jacob.core.definition.ITableDefinition, de.tif.jacob.core.definition.ITableField)
   */
  public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, Object defaultValue)
  {
    if (((SQLDataSource) dataSource).needsMappingOfBinary(field.getType()))
    {
      return new QeSBinary(dataSource.getName());
    }
    return super.getInitDataValue(dataSource, table, field, defaultValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValueForInsert(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    if (context.getSQLDataSource().needsMappingOfBinary(fieldType))
    {
      QeSBinary binary = (QeSBinary) dataValue;
      
      if (binary.isNull())
        statement.setNull(index, java.sql.Types.INTEGER);
      else
      {
        long qwkey = QeSIntegerAdjustment.newKey( context.getDataSource(), QeSInternalDefinition.BINARY_TABLE_NAME, 1);
        binary.setKey(qwkey);
        
        // insert new record in qw_byte first
        insertQWByte(context, binary.getKey(), binary.getValue());

        // and link this record with qw_byte record
        statement.setLong(index, binary.getKey());
      }
    }
    else
    {
      super.setSQLValueForInsert(fieldType, context, statement, index, dataValue);
    }
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValueForUpdate(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForUpdate(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    if (context.getSQLDataSource().needsMappingOfBinary(fieldType))
    {
      QeSBinary binary = (QeSBinary) dataValue;
      
      if (binary.isNull() && binary.isNew())
        statement.setNull(index, java.sql.Types.INTEGER);
      else
      {
        if (binary.isNew())
        {
          // insert new record in qw_byte first
          long qwkey = QeSIntegerAdjustment.newKey( context.getDataSource(), QeSInternalDefinition.BINARY_TABLE_NAME, 1);
          binary.setKey(qwkey);
          insertQWByte(context, qwkey, binary.getValue());
        }
        else
        {
          // update record in qw_byte first
          updateQWByte(context, binary.getKey(), binary.getValue());
        }

        // and link this record with qw_byte record
        statement.setLong(index, binary.getKey());
      }
    }
    else
    {
      super.setSQLValueForUpdate(fieldType, context, statement, index, dataValue);
    }
  }

  private static void insertQWByte(SQLExecutionContext context, long qwkey, byte[] binaryData) throws SQLException
  {
    IDataTableRecord record = context.getCurrentRecord();
    String tablename = context.getCurrentRecord().getTable().getTableAlias().getTableDefinition().getDBName();
    String fieldname = context.getCurrentDBFieldName();
    long textkey = QeSLongTextAdjustment.getPkey(record);

    String sqlString = "INSERT INTO qw_byte (qwkey, tablename, fieldname, textkey, text) VALUES (?, ?, ?, ?, ?)";

    PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
    try
    {
      int i = 1;
      statement.setLong(i++, qwkey);
      statement.setString(i++, tablename);
      statement.setString(i++, fieldname);
      statement.setLong(i++, textkey);
      if (binaryData.length == 0)
      {
        statement.setNull(i++, java.sql.Types.LONGVARBINARY);
      }
      else
      {
        // Do not use the following statement since Oracle throws:
        // ORA-01461: Ein LONG-Wert kann nur zur Einfügung in eine LONG-Spalte
        // gebunden werden
        // This seams to happen only for update statements, nevertheless we do
        // it for insert as well!
        //      statement.setBytes(i++, binaryData);
        statement.setBinaryStream(i++, new ByteArrayInputStream(binaryData), binaryData.length);
      }
      
      // and execute statement
      int result = statement.executeUpdate();
      if (1 != result)
      {
        String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
        throw new de.tif.jacob.core.exception.SQLException(context.getSQLDataSource(), result + " records inserted (expected 1)!", sqlStatement);
      }
    }
    finally
    {
      statement.close();
    }
  }

  private static void updateQWByte(SQLExecutionContext context, long qwkey, byte[] binaryData) throws SQLException
  {
    String sqlString = "UPDATE qw_byte SET text=? WHERE qwkey=?";

    PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
    try
    {
      int i = 1;
      if (binaryData.length == 0)
      {
        statement.setNull(i++, java.sql.Types.LONGVARBINARY);
      }
      else
      {
        statement.setBinaryStream(i++, new ByteArrayInputStream(binaryData), binaryData.length);
        // Do not use the following statement since Oracle throws:
        // ORA-01461: Ein LONG-Wert kann nur zur Einfügung in eine LONG-Spalte
        // gebunden werden
        // This seams to happen only for update statements, nevertheless we do
        // it for insert as well!
        //      statement.setBytes(i++, binaryData);
      }
      statement.setLong(i++, qwkey);

      // and execute statement
      int result = statement.executeUpdate();
      if (1 != result)
      {
        String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
        throw new de.tif.jacob.core.exception.SQLException(context.getSQLDataSource(), result + " records updated (expected 1)!", sqlStatement);
      }
    }
    finally
    {
      statement.close();
    }
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#deleteFieldValue(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.lang.Object)
   */
  public void deleteFieldValue(SQLExecutionContext context, Object dataValue) throws SQLException
  {
    if (dataValue instanceof QeSBinary)
    {
      QeSBinary binary = (QeSBinary) dataValue;

      if (!binary.isNew())
      {
        String sqlString = "DELETE FROM qw_byte WHERE qwkey=?";

        PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
        try
        {
          int i = 1;
          statement.setLong(i++, binary.getKey());

          // and execute statement
          statement.executeUpdate();
        }
        finally
        {
          statement.close();
        }
      }
    }
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#deleteFieldValues(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.lang.String)
   */
  public void deleteFieldValues(SQLExecutionContext context, String embeddedSelectFragment) throws SQLException
  {
    String sqlString = "DELETE FROM qw_byte WHERE qwkey IN (" + embeddedSelectFragment + ")";

    PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
    try
    {
      // and execute statement
      statement.executeUpdate();
    }
    finally
    {
      statement.close();
    }
  }
  
  public int getSQLType(FieldType fieldType, ISQLDataSource dataSource)
  {
    if (dataSource.needsMappingOfBinary(fieldType))
      return Types.INTEGER;
    return super.getSQLType(fieldType, dataSource);
  }
}
