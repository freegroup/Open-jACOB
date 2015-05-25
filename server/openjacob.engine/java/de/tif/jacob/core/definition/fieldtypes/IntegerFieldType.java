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
import de.tif.jacob.core.definition.impl.jad.castor.IntegerField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.ValueOutOfRangeException;
import de.tif.jacob.core.exception.ValueTooBigException;
import de.tif.jacob.core.exception.ValueTooSmallException;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class IntegerFieldType extends FieldType
{
  static public final transient String RCS_ID = "$Id: IntegerFieldType.java,v 1.10 2010/08/11 22:32:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.10 $";

  private final Integer defaultValue;
  private final Integer minValue;
  private final Integer maxValue;
  private final boolean autoIncrement;
  
  /**
   * Increment is performed by database or not.
   */
  private final boolean dbIncrement;
  
	/**
	 * 
	 */
  public IntegerFieldType(Integer defaultValue, Integer minValue, Integer maxValue, boolean autoIncrement)
  {
    this.defaultValue = defaultValue;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.autoIncrement = autoIncrement;
    this.dbIncrement = false;
  }

  public IntegerFieldType(IntegerField type)
  {
   	this.defaultValue = type.hasDefault() ? new Integer(type.getDefault()) : null;
   	this.minValue = type.hasMin() ? new Integer(type.getMin()) : null;
   	this.maxValue = type.hasMax() ? new Integer(type.getMax()) : null;
    this.autoIncrement = type.getAutoincrement();
    this.dbIncrement = type.getDbincrement();
  }

  public final Object convertSQLValueToDataValue(SQLDataSource datasource, ResultSet rs, int index) throws SQLException
  {
    int intValue = rs.getInt(index);
    if (rs.wasNull())
      return null;
    return new Integer (intValue);
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
  public final Integer getDefaultValue()
  {
    return defaultValue;
  }

	/**
   * Returns the max value.
   * 
   * @return max value or <code>null</code> if not existing
   */
  public final Integer getMaxValue()
  {
    return maxValue;
  }
  
	/**
   * Returns the min value.
   * 
   * @return min value or <code>null</code> if not existing
   */
  public final Integer getMinValue()
  {
    return minValue;
  }
  
  public int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator)
  {
    Integer val1 = (Integer) dataValue1;
    Integer val2 = (Integer) dataValue2;

    return val1.compareTo(val2);
  }
  
  public String convertDataValueToString(Object value, Locale locale, int style)
  {
    return value.toString();
  }
  
	/* (non-Javadoc)
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
	  
	  Integer value = convertObjectToDataValue(object);
	  
	  // perform range checking
	  //
	  if (value != null)
	  {
	    if (this.minValue != null)
	    {
	      if (value.intValue()<this.minValue.intValue())
	      {
	        if (this.maxValue != null)
	          throw new ValueOutOfRangeException(value, minValue, maxValue);
	        else
	          throw new ValueTooSmallException(value, minValue);
	      }
	    }
	    if (this.maxValue != null)
	    {
	      if (value.intValue()>this.maxValue.intValue())
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

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#convertDataValueToObject(java.lang.Object)
   */
  public Object convertDataValueToObject(Object dataValue)
  {
	  if (dataValue instanceof DataIncOrDecValue)
    {
      throw new RuntimeException("Incremented value has not been determined so far");
    }
	  
    // object is not mutable -> just pass thru
		return dataValue;
  }
  
	private static Integer convertObjectToDataValue(Object object) throws InvalidExpressionException, IllegalArgumentException
	{
    if (object == null)
    {
      return null;
    }
    if (object instanceof Integer)
    {
      return (Integer) object;
    }
    if (object instanceof String)
		{
			String expression = ((String) object).trim();
      if (expression.length() == 0)
        return null;
			try
			{
				return Integer.valueOf(expression);
			}
			catch (NumberFormatException ex)
			{
				throw new InvalidExpressionException(expression);
			}
		}
    if (object instanceof Long)
    {
      return new Integer(((Long) object).intValue());
    }
    throw new IllegalArgumentException(object.getClass().toString());
  }

  public final Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache) throws Exception
  {
    if (this.autoIncrement)
      return dataSource.getAdjustment().getIntegerAdjustment().newKey(dataSource, table, field, this.dbIncrement, newKeysCache);
    return this.defaultValue;
  }

  public final boolean setNextAutoKey(DataSource dataSource, ITableDefinition table, ITableField field, int nextKey) throws Exception
  {
    if (this.autoIncrement)
      return dataSource.getAdjustment().getIntegerAdjustment().setNextKey(dataSource, table, field, this.dbIncrement, nextKey);
    return false;
  }
  
  public final boolean isAutoGenerated()
  {
    return this.autoIncrement;
  }

	public final void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    if (null == dataValue)
    	statement.setNull(index, java.sql.Types.INTEGER);
    else
    	statement.setInt(index, ((Integer) dataValue).intValue());
	}

	public final void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    if (null == dataValue)
      statement.setNull(index, java.sql.Types.INTEGER);
    else
    {
      // Note: Cast to Number because of DataAutoKeyNumberValue instance 
      statement.setInt(index, ((Number) dataValue).intValue());
    }
  }

	public final void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    if (null == dataValue)
      statement.setNull(index, java.sql.Types.INTEGER);
    else if (dataValue instanceof DataIncOrDecValue)
      statement.setInt(index, ((DataIncOrDecValue) dataValue).getIncOrDecBy().intValue());
    else
    {
      // Note: Cast to Number because of DataAutoKeyNumberValue instance 
      statement.setInt(index, ((Number) dataValue).intValue());
    }
  }

  public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
    IntegerField type = new IntegerField();
    type.setAutoincrement(this.autoIncrement);
    type.setDbincrement(this.dbIncrement);
    if (this.defaultValue != null)
      type.setDefault(this.defaultValue.intValue());
    if (this.minValue != null)
      type.setMin(this.minValue.intValue());
    if (this.maxValue != null)
      type.setMax(this.maxValue.intValue());
    tableField.getCastorTableFieldChoice().setInteger(type);
  }

  public boolean isDBAutoGenerated(ISQLDataSource dataSource)
  {
    return autoIncrement && dbIncrement && dataSource.supportsAutoKeyGeneration();
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#createDefaultInputField(java.lang.String, de.tif.jacob.core.definition.guielements.Dimension, boolean, int, de.tif.jacob.core.definition.guielements.Caption, de.tif.jacob.core.definition.ITableAlias, de.tif.jacob.core.definition.ITableField)
	 */
	public LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
    return new TextInputFieldDefinition(name,null, null, null, position, visible, readonly, false, tabIndex, paneIndex, caption, localTableAlias, localTableField, null, null);
  }

  public final int getSQLType(ISQLDataSource dataSource)
  {
    return Types.INTEGER;
  }
  
  public final int getSQLDecimalDigits(ISQLDataSource dataSource)
  {
    // irrelevant
    return 0;
  }
  
  public final int getSQLSize(ISQLDataSource dataSource)
  {
    // irrelevant
    return 0;
  }
  
  public final String getSQLDefaultValue(ISQLDataSource dataSource)
  {
    return this.defaultValue != null ? this.defaultValue.toString() : null;
  }
}
