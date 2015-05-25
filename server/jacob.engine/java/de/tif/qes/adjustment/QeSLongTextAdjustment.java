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

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import de.tif.jacob.core.adjustment.impl.LongTextAdjustment;
import de.tif.jacob.core.data.IDataKeyValue;
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
public class QeSLongTextAdjustment extends LongTextAdjustment
{
	static public final transient String RCS_ID = "$Id: QeSLongTextAdjustment.java,v 1.5 2009-01-14 13:36:37 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.5 $";

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertSqlValueToDataValue(de.tif.jacob.core.data.impl.sql.SQLDataSource, java.sql.ResultSet, int)
	 */
	public Object convertSQLValueToDataValue(FieldType fieldType, SQLDataSource datasource, ResultSet rs, int index) throws SQLException
	{
		if (datasource.needsMappingOfLongText(fieldType))
		{
			long longValue = rs.getLong(index);
			if (rs.wasNull())
				return new QeSLongText(datasource.getName());
			return new QeSLongText(datasource.getName(), longValue);
		}
		return super.convertSQLValueToDataValue(fieldType, datasource, rs, index);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertObjectToDataValue(java.lang.Object, java.lang.Object)
	 */
	public Object convertObjectToDataValue(Object object, Object oldValue) throws IllegalArgumentException
	{
		if (oldValue instanceof QeSLongText)
		{
			if (object instanceof QeSLongText)
			{
				// plausibility check
				QeSLongText oldLongText = (QeSLongText) oldValue;
				QeSLongText newLongText = (QeSLongText) object;
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
			QeSLongText oldLongText = (QeSLongText) oldValue;
			return new QeSLongText(oldLongText.getDataSourceName(), oldLongText.getKey(), str);
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
				return new QeSLongText(datasource.getName());
			else
				return new QeSLongText(datasource.getName(), (String) defaultValue);
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
			QeSLongText longText = (QeSLongText) dataValue;

			if (longText.isNull())
				statement.setNull(index, java.sql.Types.INTEGER);
			else
			{
				long qwkey = QeSIntegerAdjustment.newKey( context.getDataSource(), QeSInternalDefinition.LONGTEXT_TABLE_NAME, 1);
				longText.setKey(qwkey);

				// insert new record in qw_text first
				insertQWText(context, longText.getKey(), longText.getValue());

				// and link this record with qw_text record
				statement.setLong(index, longText.getKey());
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
			QeSLongText longText = (QeSLongText) dataValue;

			if (longText.isNull() && longText.isNew())
				statement.setNull(index, java.sql.Types.INTEGER);
			else
			{
				if (longText.isNew())
				{
					// insert new record in qw_text first
					long qwkey = QeSIntegerAdjustment.newKey( context.getDataSource(), QeSInternalDefinition.LONGTEXT_TABLE_NAME, 1);
					longText.setKey(qwkey);
					insertQWText(context, qwkey, longText.getValue());
				}
				else
				{
					// update record in qw_text first
					updateQWText(context, longText);
				}

				// and link this record with qw_text record
				statement.setLong(index, longText.getKey());
			}
		}
		else
		{
			super.setSQLValueForUpdate(fieldType, context, statement, index, dataValue);
		}
	}

	protected static long getPkey(IDataTableRecord record)
	{
		IDataKeyValue primaryKeyValue = record.getPrimaryKeyValue();
		if (primaryKeyValue != null && primaryKeyValue.numberOfFieldValues() == 1 && primaryKeyValue.getFieldValue(0) instanceof Number)
		{
			return ((Number) primaryKeyValue.getFieldValue(0)).longValue();
		}
		throw new RuntimeException("Record has no pkey: " + record);
	}

	private static void insertQWText(SQLExecutionContext context, long qwkey, String text) throws SQLException
	{
		IDataTableRecord record = context.getCurrentRecord();
		String tablename = context.getCurrentRecord().getTable().getTableAlias().getTableDefinition().getDBName();
		String fieldname = context.getCurrentDBFieldName();
		long textkey = getPkey(record);

		String sqlString = "INSERT INTO qw_text (qwkey, tablename, fieldname, textkey, text) VALUES (?, ?, ?, ?, ?)";

		PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
		try
		{
			int i = 1;
			statement.setLong(i++, qwkey);
			statement.setString(i++, tablename);
			statement.setString(i++, fieldname);
			statement.setLong(i++, textkey);
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

	private static void updateQWText(SQLExecutionContext context, QeSLongText longText) throws SQLException
	{
		String sqlString;

    // IBIS: Prüfen, ob es einen effizienteren Weg gibt die Historie zu schreiben ohne alles laden zu müssen.
//    if (longText.isAppendPrependMode())
//		{
//			sqlString = "UPDATE qw_text SET text=?||text||? WHERE qwkey=?";
//		}
//		else
		{
			sqlString = "UPDATE qw_text SET text=? WHERE qwkey=?";
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
			statement.setLong(i++, longText.getKey());

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
    if (dataValue instanceof QeSLongText)
    {
      QeSLongText longText = (QeSLongText) dataValue;

      if (!longText.isNew())
      {
        String sqlString = "DELETE FROM qw_text WHERE qwkey=?";

        PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
        try
        {
          int i = 1;
          statement.setLong(i++, longText.getKey());

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
    String sqlString = "DELETE FROM qw_text WHERE qwkey IN (" + embeddedSelectFragment + ")";

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
    if (dataSource.needsMappingOfLongText(fieldType))
      return Types.INTEGER;
    return super.getSQLType(fieldType, dataSource);
  }
}
