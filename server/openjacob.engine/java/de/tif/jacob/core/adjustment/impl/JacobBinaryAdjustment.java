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

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.UUID;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.data.impl.sql.WrapperPreparedStatement;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class JacobBinaryAdjustment extends BinaryAdjustment
{
	static public final transient String RCS_ID = "$Id: JacobBinaryAdjustment.java,v 1.3 2009/01/14 13:37:12 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertSqlValueToDataValue(de.tif.jacob.core.data.impl.sql.SQLDataSource, java.sql.ResultSet, int)
	 */
	public Object convertSQLValueToDataValue(FieldType fieldType, SQLDataSource datasource, ResultSet rs, int index) throws SQLException
	{
		if (datasource.needsMappingOfBinary(fieldType))
		{
			String uuidString = rs.getString(index);
			if (rs.wasNull())
				return new JacobBinary(datasource.getName());
			return new JacobBinary(datasource.getName(), new UUID(uuidString));
		}
		return super.convertSQLValueToDataValue(fieldType, datasource, rs, index);
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertObjectToDataValue(java.lang.Object, java.lang.Object)
	 */
	public Object convertObjectToDataValue(Object object, Object oldValue) throws IllegalArgumentException
	{
		if (oldValue instanceof JacobBinary)
		{
			if (object == null || object instanceof byte[])
			{
				JacobBinary oldBinary = (JacobBinary) oldValue;
				
	      // clone the array because it is mutable!
        if (object != null)
          object = ((byte[]) object).clone();
        return new JacobBinary(oldBinary.getDataSourceName(), oldBinary.getKey(), (byte[]) object);
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
			return new JacobBinary(dataSource.getName());
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
			JacobBinary binary = (JacobBinary) dataValue;
			
			if (binary.isNull())
				statement.setNull(index, java.sql.Types.CHAR);
			else
			{
				binary.setKey(new UUID());
				
				// insert new record in jacob_binary first
				insertBinary(context, binary.getKey(), binary.getValue());

				// and link this record with jacob_binary record
				statement.setString(index, binary.getKey().toString());
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
			JacobBinary binary = (JacobBinary) dataValue;
			
			if (binary.isNull() && binary.isNew())
				statement.setNull(index, java.sql.Types.CHAR);
			else
			{
				if (binary.isNew())
				{
					// insert new record in jacob_binary first
					UUID key = new UUID();
					binary.setKey(key);
					insertBinary(context, key, binary.getValue());
				}
				else
				{
					// update record in jacob_binary first
					updateBinary(context, binary.getKey(), binary.getValue());
				}

				// and link this record with jacob_binary record
				statement.setString(index, binary.getKey().toString());
			}
		}
		else
		{
			super.setSQLValueForUpdate(fieldType, context, statement, index, dataValue);
		}
	}

	private static void insertBinary(SQLExecutionContext context, UUID key, byte[] binaryData) throws SQLException
	{
    String tablename = context.getCurrentRecord().getTable().getTableAlias().getTableDefinition().getDBName();
    String columnname = context.getCurrentDBFieldName();
    
		String sqlString = "INSERT INTO jacob_binary (id, tablename, columnname, bdata) VALUES (?, ?, ?, ?)";

		PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
		try
		{
			int i = 1;
			statement.setString(i++, key.toString());
      statement.setString(i++, tablename);
      statement.setString(i++, columnname);
      if (binaryData.length == 0)
      {
        statement.setNull(i++, java.sql.Types.LONGVARBINARY);
      }
      else
      {
        statement.setBinaryStream(i++, new ByteArrayInputStream(binaryData), binaryData.length);
        // Do not use the following statement since Oracle throws:
        // ORA-01461: Ein LONG-Wert kann nur zur Einf�gung in eine LONG-Spalte
        // gebunden werden
        // This seams to happen only for update statements, nevertheless we do
        // it for insert as well!
        //			statement.setBytes(i++, binaryData);
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

	private static void updateBinary(SQLExecutionContext context, UUID key, byte[] binaryData) throws SQLException
	{
		String sqlString = "UPDATE jacob_binary SET bdata=? WHERE id=?";

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
        // ORA-01461: Ein LONG-Wert kann nur zur Einf�gung in eine LONG-Spalte
        // gebunden werden
        // This seams to happen only for update statements, nevertheless we do
        // it for insert as well!
        //			statement.setBytes(i++, binaryData);
      }
			statement.setString(i++, key.toString());

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
    if (dataValue instanceof JacobBinary)
    {
      JacobBinary binary = (JacobBinary) dataValue;

      if (!binary.isNew())
      {
        String sqlString = "DELETE FROM jacob_binary WHERE id=?";

        PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
        try
        {
          int i = 1;
          statement.setString(i++, binary.getKey().toString());

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
    String sqlString = "DELETE FROM jacob_binary WHERE id IN (" + embeddedSelectFragment + ")";

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
  
  public int getSQLSize(FieldType fieldType, ISQLDataSource dataSource)
  {
		if (dataSource.needsMappingOfBinary(fieldType))
			return UUID.STRING_LENGTH;
    return super.getSQLSize(fieldType, dataSource);
  }
  
  public int getSQLType(FieldType fieldType, ISQLDataSource dataSource)
  {
		if (dataSource.needsMappingOfBinary(fieldType))
			return Types.CHAR;
    return super.getSQLType(fieldType, dataSource);
  }
	
}