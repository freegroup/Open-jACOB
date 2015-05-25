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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.Collator;
import java.util.Locale;

import de.tif.jacob.core.data.impl.DataIncOrDecValue;
import de.tif.jacob.core.data.impl.DataSource;
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
import de.tif.jacob.core.definition.impl.jad.castor.FloatField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.ValueOutOfRangeException;
import de.tif.jacob.core.exception.ValueTooBigException;
import de.tif.jacob.core.exception.ValueTooSmallException;
import de.tif.jacob.i18n.I18N;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class FloatFieldType extends FieldType
{
  static public final transient String RCS_ID = "$Id: FloatFieldType.java,v 1.9 2010/08/11 22:32:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";

  private final Float defaultValue;
  private final Float minValue;
  private final Float maxValue;

	/**
	 * 
	 */
	public FloatFieldType(Float defaultValue, Float minValue, Float maxValue)
	{
    this.defaultValue = defaultValue;
    this.minValue = minValue;
    this.maxValue = maxValue;
	}
  
  public FloatFieldType(FloatField type)
  {
   	this.defaultValue = type.hasDefault() ? new Float(type.getDefault()) : null;
   	this.minValue = type.hasMin() ? new Float(type.getMin()) : null;
   	this.maxValue = type.hasMax() ? new Float(type.getMax()) : null;
  }
  
  public Object convertSQLValueToDataValue(SQLDataSource datasource, ResultSet rs, int index) throws SQLException
  {
    float value = rs.getFloat(index);
    if (rs.wasNull())
      return null;
    return new Float(value);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#convertDataValueToString(java.lang.Object, java.util.Locale)
   */
  public String convertDataValueToString(Object value, Locale locale, int style)
  {
    return I18N.toString((Float) value, locale, style);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#createQBEExpression(de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint)
   */
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
    // TODO: check this
    Object value =constraint.getValue(); 
    if (value instanceof String)
    {
      return QBEExpression.parseDecimal((String) value, constraint.getLocale());
    }
    else if (value instanceof Number)
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
	public Float getDefaultValue()
	{
		return defaultValue;
	}

	/**
   * Returns the max value.
   * 
   * @return max value or <code>null</code> if not existing
   */
  public Float getMaxValue()
  {
    return maxValue;
  }
  
	/**
   * Returns the min value.
   * 
   * @return min value or <code>null</code> if not existing
   */
  public Float getMinValue()
  {
    return minValue;
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#convertObjectToDataValue(java.lang.Object)
	 */
	public Object convertObjectToDataValue(DataSource dataSource, Object object, Object oldValue, Locale locale) throws InvalidExpressionException, IllegalArgumentException
	{
	  if (object instanceof DataIncOrDecValue)
    {
      return object;
    }
	  
	  Float value = convertObjectToDataValue(object, locale);
	  
	  // perform range checking
	  if (value != null)
	  {
	    if (this.minValue != null)
	    {
	      if (value.floatValue()<this.minValue.floatValue())
	      {
	        if (this.maxValue != null)
	          throw new ValueOutOfRangeException(value, minValue, maxValue);
	        else
	          throw new ValueTooSmallException(value, minValue);
	      }
	    }
	    if (this.maxValue != null)
	    {
	      if (value.floatValue()>this.maxValue.floatValue())
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
    Float val1 = (Float) dataValue1;
    Float val2 = (Float) dataValue2;

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
  
	private static Float convertObjectToDataValue(Object object, Locale locale) throws InvalidExpressionException, IllegalArgumentException
	{
    if (object == null)
    {
      return null;
    }
    if (object instanceof Float)
    {
      return (Float) object;
    }
    if (object instanceof String)
    {
      String expression = ((String) object).trim();
      if (expression.length() == 0)
        return null;
      return I18N.parseFloat(expression, locale);
    }
    if (object instanceof Double)
    {
      return new Float(((Double) object).floatValue());
    }
    if (object instanceof Integer)
    {
      return Float.valueOf(object.toString());
    }
    if (object instanceof Long)
    {
      return Float.valueOf(object.toString());
    }
    if (object instanceof BigDecimal)
    {
      return Float.valueOf(object.toString());
    }
    throw new IllegalArgumentException(object.getClass().toString());
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#getInitDataValue(de.tif.jacob.core.data.DataSource, de.tif.jacob.core.definition.ITableField)
	 */
	public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache)
	{
		return this.defaultValue;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValue(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    if (null == dataValue)
      statement.setNull(index, java.sql.Types.FLOAT);
    else
      statement.setFloat(index, ((Float) dataValue).floatValue());
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    if (null == dataValue)
      statement.setNull(index, java.sql.Types.FLOAT);
    else
      statement.setFloat(index, ((Float) dataValue).floatValue());
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    if (null == dataValue)
      statement.setNull(index, java.sql.Types.FLOAT);
    else if (dataValue instanceof DataIncOrDecValue)
      statement.setFloat(index, ((DataIncOrDecValue) dataValue).getIncOrDecBy().floatValue());
    else
      statement.setFloat(index, ((Float) dataValue).floatValue());
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#toJacob(de.tif.jacob.core.jad.castor.TableField)
	 */
  public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
    FloatField type = new FloatField();
    if (this.defaultValue != null)
      type.setDefault(this.defaultValue.floatValue());
    if (this.minValue != null)
      type.setMin(this.minValue.floatValue());
    if (this.maxValue != null)
      type.setMax(this.maxValue.floatValue());
    tableField.getCastorTableFieldChoice().setFloat(type);
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
    return Types.FLOAT;
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
    // irrelevant
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
