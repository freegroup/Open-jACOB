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

package de.tif.jacob.core.definition.fieldtypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.Collator;
import java.util.Locale;

import de.tif.jacob.core.data.impl.DataIncOrDecValue;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataAutoKeyValue;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBELiteralExpression;
import de.tif.jacob.core.data.impl.qbe.QBENumericLiteral;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.LocalInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TextInputFieldDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.LongField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.ValueOutOfRangeException;
import de.tif.jacob.core.exception.ValueTooBigException;
import de.tif.jacob.core.exception.ValueTooSmallException;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class LongFieldType extends FieldType
{
	static public final transient String RCS_ID = "$Id: LongFieldType.java,v 1.11 2010/08/11 22:32:54 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.11 $";

	private final Long defaultValue;
  private final Long minValue;
  private final Long maxValue;
  private final boolean autoIncrement;
  
  /**
   * Increment is performed by database or not.
   */
  private final boolean dbIncrement;

	/**
	 *  
	 */
	public LongFieldType(Long defaultValue, Long minValue, Long maxValue, boolean autoIncrement)
	{
		this.defaultValue = defaultValue;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.autoIncrement = autoIncrement; 
    this.dbIncrement = false;
	}
  
  public LongFieldType(LongField type)
  {
    this.defaultValue = type.hasDefault() ? new Long(type.getDefault()) : null;
    this.minValue = type.hasMin() ? new Long(type.getMin()) : null;
    this.maxValue = type.hasMax() ? new Long(type.getMax()) : null;
    this.autoIncrement = type.getAutoincrement();
    this.dbIncrement = type.getDbincrement();
  }
  
	public Object convertSQLValueToDataValue(SQLDataSource datasource, ResultSet rs, int index) throws SQLException
	{
		long longValue = rs.getLong(index);
		if (rs.wasNull())
			return null;
		return new Long(longValue);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#createQBEExpression(de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint)
   */
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
    Object value =constraint.getValue(); 
    if (value instanceof String)
    {
      return QBEExpression.parseInteger((String) value);
    }
    else if (value instanceof Long || value instanceof Integer)
    {
      return new QBELiteralExpression(new QBENumericLiteral((Number) value));
    }
    else
    {
      throw new IllegalArgumentException(value.getClass().toString());
    }
  }

	/**
   * Returns the default value.
   * 
   * @return default value or <code>null</code> if not existing
	 */
	public final Long getDefaultValue()
	{
		return defaultValue;
	}

	/**
   * Returns the max value.
   * 
   * @return max value or <code>null</code> if not existing
   */
  public final Long getMaxValue()
  {
    return maxValue;
  }
  
	/**
   * Returns the min value.
   * 
   * @return min value or <code>null</code> if not existing
   */
  public final Long getMinValue()
  {
    return minValue;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#convertDataValueToString(java.lang.Object, java.util.Locale)
   */
  public String convertDataValueToString(Object value, Locale locale, int style)
  {
    return value.toString();
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.FieldType#convertObjectToDataValue(java.lang.Object)
	 */
	public Object convertObjectToDataValue(DataSource dataSource, Object object, Object oldValue, Locale locale) throws InvalidExpressionException, IllegalArgumentException
	{
	  // do not overwrite auto generated keys
	  if (this.autoIncrement && oldValue instanceof IDataAutoKeyValue)
    {
      return oldValue;
    }
	  
	  // handle linked auto generated keys
	  if (object instanceof IDataAutoKeyValue)
	    return object;
	  
	  if (object instanceof DataIncOrDecValue)
    {
      Number incrementBy = ((DataIncOrDecValue) object).getIncOrDecBy();
      if (incrementBy instanceof Integer || incrementBy instanceof Long)
        return object;
      throw new IllegalArgumentException(incrementBy.getClass().toString());
    }
	  
	  Long value = convertObjectToDataValue(object);
	  
	  // perform range checking
	  if (value != null)
	  {
	    if (this.minValue != null)
	    {
	      if (value.longValue()<this.minValue.longValue())
	      {
	        if (this.maxValue != null)
	          throw new ValueOutOfRangeException(value, minValue, maxValue);
	        else
	          throw new ValueTooSmallException(value, minValue);
	      }
	    }
	    if (this.maxValue != null)
	    {
	      if (value.longValue()>this.maxValue.longValue())
	      {
	        if (this.minValue != null)
	          throw new ValueOutOfRangeException(value, minValue, maxValue);
	        else
	          throw new ValueTooBigException(value, maxValue);
	      }
	    }
	  }
	  
	  return value;
	}
	
  public int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator)
  {
    Long val1 = (Long) dataValue1;
    Long val2 = (Long) dataValue2;

    return val1.compareTo(val2);
  }
  
  public Object convertDataValueToObject(Object dataValue)
  {
	  if (dataValue instanceof DataIncOrDecValue)
    {
      throw new RuntimeException("Incremented value has not been determined so far");
    }
	  
    // object is not mutable -> just pass thru
		return dataValue;
  }
  
	private static Long convertObjectToDataValue(Object object) throws InvalidExpressionException, IllegalArgumentException
	{
		if (object == null)
		{
			return null;
		}
		if (object instanceof Long)
		{
			return (Long) object;
		}
		if (object instanceof String)
		{
      String expression = ((String) object).trim();
      if (expression.length() == 0)
        return null;
			try
			{
				return Long.valueOf(expression);
			}
			catch (NumberFormatException ex)
			{
				throw new InvalidExpressionException(expression);
			}
		}
    if (object instanceof Integer)
    {
      return new Long(((Integer) object).intValue());
    }
    if (object instanceof java.util.Date)
    {
      return new Long(((java.util.Date) object).getTime());
    }
		throw new IllegalArgumentException(object.getClass().toString());
	}

	public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache) throws Exception
	{
		if (this.autoIncrement)
			return dataSource.getAdjustment().getLongAdjustment().newKey(dataSource, table, field, this.dbIncrement, newKeysCache);
		return this.defaultValue;
	}

  public final boolean setNextAutoKey(DataSource dataSource, ITableDefinition table, ITableField field, long nextKey) throws Exception
  {
    if (this.autoIncrement)
      return dataSource.getAdjustment().getLongAdjustment().setNextKey(dataSource, table, field, this.dbIncrement, nextKey);
    return false;
  }
  
  public final boolean isAutoGenerated()
  {
    return this.autoIncrement;
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValue(de.tif.jacob.core.data.sql.SQLExecutionContext,
	 *      java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
		if (null == dataValue)
			statement.setNull(index, java.sql.Types.INTEGER);
		else
			statement.setLong(index, ((Long) dataValue).longValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext,
	 *      java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
		if (null == dataValue)
			statement.setNull(index, java.sql.Types.INTEGER);
		else
    {
      // Note: Cast to Number because of DataAutoKeyNumberValue instance 
			statement.setLong(index, ((Number) dataValue).longValue());
    }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext,
	 *      java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
		if (null == dataValue)
			statement.setNull(index, java.sql.Types.INTEGER);
    else if (dataValue instanceof DataIncOrDecValue)
      statement.setLong(index, ((DataIncOrDecValue) dataValue).getIncOrDecBy().longValue());
		else
    {
      // Note: Cast to Number because of DataAutoKeyNumberValue instance 
			statement.setLong(index, ((Number) dataValue).longValue());
    }
	}
  
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.FieldType#toJacob(de.tif.jacob.core.jad.castor.TableField)
	 */
  public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
		LongField type = new LongField();
    type.setAutoincrement(this.autoIncrement);
    type.setDbincrement(this.dbIncrement);
		if (this.defaultValue != null)
			type.setDefault(this.defaultValue.longValue());
    if (this.minValue != null)
      type.setMin(this.minValue.longValue());
    if (this.maxValue != null)
      type.setMax(this.maxValue.longValue());
		tableField.getCastorTableFieldChoice().setLong(type);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#isDBAutoGenerated(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public boolean isDBAutoGenerated(ISQLDataSource dataSource)
  {
    return autoIncrement && dbIncrement && dataSource.supportsAutoKeyGeneration();
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#createDefaultInputField(java.lang.String, de.tif.jacob.core.definition.guielements.Dimension, boolean, int, de.tif.jacob.core.definition.guielements.Caption, de.tif.jacob.core.definition.ITableAlias, de.tif.jacob.core.definition.ITableField)
	 */
	public LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
    return new TextInputFieldDefinition(name, null, null, null, position, visible, readonly, false, tabIndex, paneIndex, caption, localTableAlias, localTableField, null, null);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLType()
   */
  public int getSQLType(ISQLDataSource dataSource)
  {
    return Types.BIGINT;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLDecimalDigits(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public int getSQLDecimalDigits(ISQLDataSource dataSource)
  {
    // irrelevant
    return 0;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLSize(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public int getSQLSize(ISQLDataSource dataSource)
  {
    // default
    return 0;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getDBDefaultValue()
   */
  public String getSQLDefaultValue(ISQLDataSource dataSource)
  {
    return this.defaultValue != null ? this.defaultValue.toString() : null;
  }

}
