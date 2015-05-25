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
import de.tif.jacob.core.definition.impl.jad.castor.DecimalField;
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
public final class DecimalFieldType extends FieldType
{
  static public final transient String RCS_ID = "$Id: DecimalFieldType.java,v 1.9 2010/08/11 22:32:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";
  
  public static final short MIN_SCALE = 0;
  public static final short MAX_SCALE = 18;
  
  // IBIS: Rundungsmethode als Eigenschaft des Feldes machen, i.e. wird im Design festgelegt
  /**
   * Default: Kaufmännisches Runden
   */
  public static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
  
  private final BigDecimal defaultValue;
  private final BigDecimal minValue;
  private final BigDecimal maxValue;  
  private final short scale;
  
	/**
	 * 
	 */
	public DecimalFieldType(short scale, BigDecimal defaultValue, BigDecimal minValue, BigDecimal maxValue)
  {
    // note: set this.scale first, because adjustScale() depends on it!
    this.scale = checkScale(scale);
    this.defaultValue = adjustScale(defaultValue, DEFAULT_ROUNDING_MODE);
    this.minValue = adjustScale(minValue, DEFAULT_ROUNDING_MODE);
    this.maxValue = adjustScale(maxValue, DEFAULT_ROUNDING_MODE);
  }
  
  public DecimalFieldType(DecimalField type)
  {
    // note: set this.scale first, because adjustScale() depends on it!
    this.scale = checkScale(type.getScale());
    this.defaultValue = adjustScale(type.getDefault(), DEFAULT_ROUNDING_MODE);
   	this.minValue = adjustScale(type.getMin(), DEFAULT_ROUNDING_MODE);
   	this.maxValue = adjustScale(type.getMax(), DEFAULT_ROUNDING_MODE);
  }
  
  private static short checkScale(short scale)
  {
    if (scale < MIN_SCALE || scale > MAX_SCALE)
      throw new RuntimeException("Invalid scale " + scale + " not in range " + MIN_SCALE + ".." + MAX_SCALE);
    return scale;
  }
  
	/**
   * Returns the default value.
   * 
   * @return default value or <code>null</code> if not existing
   */
  public final BigDecimal getDefaultValue()
  {
    return defaultValue;
  }

	/**
   * Returns the max value.
   * 
   * @return max value or <code>null</code> if not existing
   */
  public final BigDecimal getMaxValue()
  {
    return maxValue;
  }
  
	/**
   * Returns the min value.
   * 
   * @return min value or <code>null</code> if not existing
   */
  public final BigDecimal getMinValue()
  {
    return minValue;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#convertDataValueToString(java.lang.Object, java.util.Locale)
   */
  public String convertDataValueToString(Object value, Locale locale, int style)
  {
    return I18N.toString((BigDecimal) value, locale, style);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#createQBEExpression(de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint)
   */
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
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

  public int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator)
  {
    BigDecimal val1 = (BigDecimal) dataValue1;
    BigDecimal val2 = (BigDecimal) dataValue2;

    return val1.compareTo(val2);
  }
  
	public Object convertObjectToDataValue(DataSource dataSource, Object object, Object oldValue, Locale locale) throws InvalidExpressionException
	{
	  if (object instanceof DataIncOrDecValue)
    {
      return object;
    }
	  
	  BigDecimal value = convertObjectToDataValue(object, locale);
	  
	  // scaling if necessary
	  value = adjustScale(value, DEFAULT_ROUNDING_MODE);
	  
	  // perform range checking
	  //
	  if (value != null)
	  {
	    // check minimum
	    if (this.minValue != null)
	    {
	      if (value.compareTo(this.minValue) < 0)
	      {
	        if (this.maxValue != null)
	          throw new ValueOutOfRangeException(value, minValue, maxValue);
	        else
	          throw new ValueTooSmallException(value, minValue);
	      }
	    }
	    
	    // check maximum
	    if (this.maxValue != null)
	    {
	      if (value.compareTo(this.maxValue) > 0)
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
  
  public BigDecimal adjustScale(BigDecimal value, int roundingMode) throws ArithmeticException
  {
    if (value != null && value.scale() != this.scale)
    {
      return value.setScale(this.scale, roundingMode);
    }
    return value;
  }
  
	private static BigDecimal convertObjectToDataValue(Object object, Locale locale) throws InvalidExpressionException, IllegalArgumentException
	{
    if (object == null)
    {
      return null;
    }
    if (object instanceof BigDecimal)
    {
      return (BigDecimal) object;
    }
    if (object instanceof String)
    {
      String expression = ((String) object).trim();
      if (expression.length() == 0)
        return null;
      return I18N.parseDecimal(expression, locale);
    }
    if (object instanceof Integer)
    {
      return BigDecimal.valueOf(((Integer) object).longValue());
    }
    if (object instanceof Long)
    {
      return BigDecimal.valueOf(((Long) object).longValue());
    }
    if (object instanceof Float)
    {
      return new BigDecimal(((Float) object).doubleValue());
    }
    if (object instanceof Double)
    {
      return new BigDecimal(((Double) object).doubleValue());
    }
    throw new IllegalArgumentException(object.getClass().toString());
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#convertSqlValueToDataValue(de.tif.jacob.core.data.sql.SQLDataSource, java.sql.ResultSet, int)
	 */
	public Object convertSQLValueToDataValue(SQLDataSource datasource, ResultSet rs, int index) throws SQLException
  {
    // scaling if necessary (do not trust database :-)
    return adjustScale(rs.getBigDecimal(index), DEFAULT_ROUNDING_MODE);
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
    statement.setBigDecimal(index, (BigDecimal) dataValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    statement.setBigDecimal(index, (BigDecimal) dataValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
    if (dataValue instanceof DataIncOrDecValue)
    {
      Number incrementBy = ((DataIncOrDecValue) dataValue).getIncOrDecBy();
      if (incrementBy instanceof BigDecimal)
        statement.setBigDecimal(index, (BigDecimal) incrementBy);
      else if (incrementBy instanceof Float || incrementBy instanceof Double)
        statement.setBigDecimal(index, new BigDecimal(incrementBy.doubleValue()));
      else
        statement.setBigDecimal(index, BigDecimal.valueOf(incrementBy.longValue()));
    }
    else
      statement.setBigDecimal(index, (BigDecimal) dataValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#toJacob(de.tif.jacob.core.jad.castor.TableField)
	 */
  public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
    DecimalField type = new DecimalField();
    type.setDefault(this.defaultValue);
    type.setMin(this.minValue);
    type.setMax(this.maxValue);
    type.setScale(this.scale);
    tableField.getCastorTableFieldChoice().setDecimal(type);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#createDefaultInputField()
	 */
	public LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
		return new TextInputFieldDefinition(name, null,null, null, position, visible, readonly, false, tabIndex, paneIndex, caption, localTableAlias, localTableField, null, null);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLType()
   */
  public int getSQLType(ISQLDataSource dataSource)
  {
    return Types.DECIMAL;
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
   * @see de.tif.jacob.core.definition.FieldType#getDecimalDigits()
   */
  public int getSQLDecimalDigits(ISQLDataSource dataSource)
  {
    return this.scale;
  }
  
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getDBDefaultValue()
   */
  public String getSQLDefaultValue(ISQLDataSource dataSource)
  {
    return this.defaultValue != null ? this.defaultValue.toString() : null;
  }
}
