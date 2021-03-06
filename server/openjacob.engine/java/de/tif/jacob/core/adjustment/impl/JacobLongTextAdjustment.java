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

import java.io.StringReader;
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
public class JacobLongTextAdjustment extends LongTextAdjustment
{
	static public final transient String RCS_ID = "$Id: JacobLongTextAdjustment.java,v 1.3 2009/01/14 13:37:12 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertSqlValueToDataValue(de.tif.jacob.core.data.impl.sql.SQLDataSource, java.sql.ResultSet, int)
	 */
	public Object convertSQLValueToDataValue(FieldType fieldType, SQLDataSource datasource, ResultSet rs, int index) throws SQLException
	{
		if (datasource.needsMappingOfLongText(fieldType))
		{
			String uuidString = rs.getString(index);
			if (rs.wasNull())
				return new JacobLongText(datasource.getName());
			return new JacobLongText(datasource.getName(), new UUID(uuidString));
		}
		return super.convertSQLValueToDataValue(fieldType, datasource, rs, index);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertObjectToDataValue(java.lang.Object, java.lang.Object)
	 */
	public Object convertObjectToDataValue(Object object, Object oldValue) throws IllegalArgumentException
	{
		if (oldValue instanceof JacobLongText)
		{
			if (object instanceof JacobLongText)
			{
				// plausibility check
				JacobLongText oldLongText = (JacobLongText) oldValue;
				JacobLongText newLongText = (JacobLongText) object;
				if (oldLongText.getKey() != newLongText.getKey() || oldLongText.getDataSourceName() != newLongText.getDataSourceName())
				{
					throw new RuntimeException("Plausibility check failed");
				}
				return object;
			}
			
			String str;
			if (object == null)
			{
			  str = null;
			}
			else
			{
	      try
	      {
	      	str = object.toString();
	      }
	      catch (Exception ex)
	      {
	        throw new IllegalArgumentException(object.getClass().toString());
	      }
			}
			JacobLongText oldLongText = (JacobLongText) oldValue;
			return new JacobLongText(oldLongText.getDataSourceName(), oldLongText.getKey(), str);
		}
		return super.convertObjectToDataValue(object, oldValue);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#getInitDataValue(de.tif.jacob.core.data.IDataSource, de.tif.jacob.core.definition.ITableDefinition, de.tif.jacob.core.definition.ITableField)
	 */
	public Object getInitDataValue(DataSource datasource, ITableDefinition table, ITableField field, Object defaultValue)
	{
		if (((SQLDataSource) datasource).needsMappingOfLongText(field.getType()))
		{
			if (defaultValue == null)
				return new JacobLongText(datasource.getName());
			else
				return new JacobLongText(datasource.getName(), (String) defaultValue);
		}
		return super.getInitDataValue(datasource, table, field, defaultValue);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValueForInsert(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
		if (context.getSQLDataSource().needsMappingOfLongText(fieldType))
		{
			JacobLongText longText = (JacobLongText) dataValue;

			if (longText.isNull())
				statement.setNull(index, java.sql.Types.CHAR);
			else
			{
				longText.setKey(new UUID());

				// insert new record in jacob_text first
				insertText(context, longText.getKey(), longText.getValue());

				// and link this record with jacob_text record
				statement.setString(index, longText.getKey().toString());
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
		if (context.getSQLDataSource().needsMappingOfLongText(fieldType))
		{
			JacobLongText longText = (JacobLongText) dataValue;

			if (longText.isNull() && longText.isNew())
				statement.setNull(index, java.sql.Types.CHAR);
			else
			{
				if (longText.isNew())
				{
					// insert new record in jacob_text first
					UUID key = new UUID();
					longText.setKey(key);
					insertText(context, key, longText.getValue());
				}
				else
				{
					// update record in jacob_text first
					updateText(context, longText);
				}

				// and link this record with jacob_text record
				statement.setString(index, longText.getKey().toString());
			}
		}
		else
		{
			super.setSQLValueForUpdate(fieldType, context, statement, index, dataValue);
		}
	}

	private static void insertText(SQLExecutionContext context, UUID key, String text) throws SQLException
	{
    String tablename = context.getCurrentRecord().getTable().getTableAlias().getTableDefinition().getDBName();
    String columnname = context.getCurrentDBFieldName();
    
		String sqlString = "INSERT INTO jacob_text (id, tablename, columnname, text) VALUES (?, ?, ?, ?)";

		PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
		try
		{
			int i = 1;
			statement.setString(i++, key.toString());
      statement.setString(i++, tablename);
      statement.setString(i++, columnname);
			statement.setCharacterStream(i++, new StringReader(text), text.length());

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

	private static void updateText(SQLExecutionContext context, JacobLongText longText) throws SQLException
	{
		String sqlString;

		// IBIS: Pr�fen, ob es einen effizienteren Weg gibt die Historie zu schreiben ohne alles laden zu m�ssen.
//    if (longText.isAppendPrependMode())
//		{
//			sqlString = "UPDATE jacob_text SET text=?||text||? WHERE id=?";
//		}
//		else
		{
			sqlString = "UPDATE jacob_text SET text=? WHERE id=?";
		}

		PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
		try
		{
			int i = 1;
//			if (longText.isAppendPrependMode())
//			{
//				String prependText = longText.getPrependText();
//				String appendText = longText.getAppendText();
//				statement.setCharacterStream(i++, new StringReader(prependText), prependText.length());
//				statement.setCharacterStream(i++, new StringReader(appendText), appendText.length());
//			}
//			else
      String value = longText.getValue();
      if (value == null || value.length() == 0)
      {
        statement.setNull(i++, java.sql.Types.LONGVARCHAR);
      }
      else
      {
        statement.setCharacterStream(i++, new StringReader(value), value.length());
      }
			statement.setString(i++, longText.getKey().toString());

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
    if (dataValue instanceof JacobLongText)
    {
      JacobLongText longText = (JacobLongText) dataValue;

      if (!longText.isNew())
      {
        String sqlString = "DELETE FROM jacob_text WHERE id=?";

        PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
        try
        {
          int i = 1;
          statement.setString(i++, longText.getKey().toString());

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
    String sqlString = "DELETE FROM jacob_text WHERE id IN (" + embeddedSelectFragment + ")";

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
		if (dataSource.needsMappingOfLongText(fieldType))
			return UUID.STRING_LENGTH;
    return super.getSQLSize(fieldType, dataSource);
  }
  
  public int getSQLType(FieldType fieldType, ISQLDataSource dataSource)
  {
		if (dataSource.needsMappingOfLongText(fieldType))
			return Types.CHAR;
    return super.getSQLType(fieldType, dataSource);
  }
}
