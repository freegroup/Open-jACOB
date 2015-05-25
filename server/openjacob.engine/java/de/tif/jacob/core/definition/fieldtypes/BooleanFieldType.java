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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.data.impl.qbe.QBEBooleanLiteral;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBELiteralExpression;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.CheckBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.LocalInputFieldDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.BooleanField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas
 * 
 * @since 2.6
 */
public final class BooleanFieldType extends FieldType
{
  static public final transient String RCS_ID = "$Id: BooleanFieldType.java,v 1.17 2011/07/01 21:15:00 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.17 $";
  
  private static final Map trueLocaleMap = new HashMap();
  private static final Map falseLocaleMap = new HashMap();
  
  private static final Set trueSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
  private static final Set falseSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
  
  static
  {
    trueLocaleMap.put(Locale.ENGLISH.getLanguage(), "true");
    falseLocaleMap.put(Locale.ENGLISH.getLanguage(), "false");
    
    trueLocaleMap.put(Locale.GERMAN.getLanguage(), "Ja");
    falseLocaleMap.put(Locale.GERMAN.getLanguage(), "Nein");
    
    trueLocaleMap.put(Locale.FRENCH.getLanguage(), "vrai");
    falseLocaleMap.put(Locale.FRENCH.getLanguage(), "faux");
    
    trueLocaleMap.put("es", "sí");
    falseLocaleMap.put("es", "no");
    
    trueSet.addAll(trueLocaleMap.values());
    falseSet.addAll(falseLocaleMap.values());
  }

  private final Boolean defaultValue;

  public BooleanFieldType(Boolean defaultValue)
  {
    this.defaultValue = defaultValue;
  }

  public BooleanFieldType(BooleanField type)
  {
   	this.defaultValue = type.hasDefault() ? Boolean.valueOf(type.getDefault()) : null;
  }

  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#convertSqlValueToDataValue(de.tif.jacob.core.data.sql.SQLDataSource, java.sql.ResultSet, int)
   */
  public Object convertSQLValueToDataValue(SQLDataSource dataSource, ResultSet rs, int index) throws SQLException
  {
    // delegate request
    return dataSource.getAdjustment().getBooleanAdjustment().convertSQLValueToDataValue(this, dataSource, rs, index);
  }
  
  /*
  public Object convertSQLValueToDataValue(SQLDataSource datasource, ResultSet rs, int index) throws SQLException
  {
    int intValue = rs.getInt(index);
    if (rs.wasNull())
      return null;
    // boolean logic, i.e. FALSE if 0, otherwise TRUE
    return Boolean.valueOf(intValue != 0);
  }
  */
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#createQBEExpression(de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint)
   */
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
    Object value = constraint.getValue();
    if (value instanceof String)
    {
      return QBEExpression.parseBoolean((String) value);
    }
    else if (value instanceof Boolean)
    {
      return new QBELiteralExpression(new QBEBooleanLiteral((Boolean) value));
    }
    else if (value instanceof Long || value instanceof Integer)
    {
      // boolean logic, i.e. FALSE if 0, otherwise TRUE
      return new QBELiteralExpression(new QBEBooleanLiteral(((Number) value).longValue() == 0 ? Boolean.FALSE : Boolean.TRUE));
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
  public Boolean getDefaultValue()
  {
    return defaultValue;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#convertDataValueToString(java.lang.Object, java.util.Locale)
   */
  public String convertDataValueToString(Object value, Locale locale, int style)
  {
    return convertToString((Boolean) value, locale, style);
  }
  
  public static String convertToString(Boolean value, Locale locale, int style)
  {
    switch (style)
    {
      case IDataRecord.SHORT_STYLE:
        return value.booleanValue() ? "1" : "0";
        
      default:
        break;
    }
    
    if (locale != null && style != IDataRecord.RAW_STYLE)
    {
      Map map = Boolean.TRUE.equals(value) ? trueLocaleMap : falseLocaleMap;
      String str = (String) map.get(locale.getLanguage());
      if (str != null)
        return str;
    }

    // returns "true" or "false"
    return value.toString();
  }
  
	public Object convertObjectToDataValue(DataSource dataSource, Object object, Object oldValue, Locale locale) throws InvalidExpressionException, IllegalArgumentException
	{
	  return convertObjectToDataValue(object);
  }

  public Object convertDataValueToObject(Object dataValue)
  {
    // object is not mutable -> just pass thru
		return dataValue;
  }
  
  public int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator)
  {
    Boolean val1 = (Boolean) dataValue1;
    Boolean val2 = (Boolean) dataValue2;

    if (val1.booleanValue())
      return val2.booleanValue() ? 0 : 1;
    return val2.booleanValue() ? -1 : 0;
  }

  private static Boolean convertObjectToDataValue(Object object) throws InvalidExpressionException, IllegalArgumentException
  {
    if (object == null)
    {
      return null;
    }
    if (object instanceof Boolean)
    {
      return (Boolean) object;
    }
    if (object instanceof Integer)
    {
      return Boolean.valueOf(((Integer) object).intValue() != 0);
    }
    if (object instanceof String)
    {
      String expression = ((String) object).trim();
      if (expression.length() == 0)
        return null;

      // do not use Boolean.valueOf(expression) since this returns false even if
      // expression is "anythingNotTrue"
      if ("false".equalsIgnoreCase(expression))
        return Boolean.FALSE;
      if ("true".equalsIgnoreCase(expression))
        return Boolean.TRUE;
      
      if (falseSet.contains(expression))
        return Boolean.FALSE;
      if (trueSet.contains(expression))
        return Boolean.TRUE;
      
      try
      {
        return Boolean.valueOf(Long.valueOf(expression).longValue() != 0);
      }
      catch (NumberFormatException ex)
      {
        // handle below
      }

      throw new InvalidExpressionException((String) object);
    }
    if (object instanceof Long)
    {
      return Boolean.valueOf(((Long) object).longValue() != 0);
    }
    throw new IllegalArgumentException(object.getClass().toString());
  }

	/*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.FieldType#getInitDataValue(de.tif.jacob.core.data.DataSource,
   *      de.tif.jacob.core.definition.ITableField)
   */
	public final Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache) throws Exception
	{
		return this.defaultValue;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValue(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public final void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    if (null == dataValue)
    	statement.setNull(index, java.sql.Types.BOOLEAN);
    else
      statement.setInt(index, ((Boolean) dataValue).booleanValue() ? 1 : 0);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public final void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    if (null == dataValue)
      statement.setNull(index, java.sql.Types.BOOLEAN);
    else
      statement.setInt(index, ((Boolean) dataValue).booleanValue() ? 1 : 0);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public final void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    if (null == dataValue)
      statement.setNull(index, java.sql.Types.BOOLEAN);
    else
      statement.setInt(index, ((Boolean) dataValue).booleanValue() ? 1 : 0);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#toJacob(de.tif.jacob.core.jad.castor.TableField)
	 */
  public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
    BooleanField type = new BooleanField();
    if (this.defaultValue != null)
      type.setDefault(this.defaultValue.booleanValue());
    tableField.getCastorTableFieldChoice().setBoolean(type);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#createDefaultInputField(java.lang.String, de.tif.jacob.core.definition.guielements.Dimension, boolean, int, de.tif.jacob.core.definition.guielements.Caption, de.tif.jacob.core.definition.ITableAlias, de.tif.jacob.core.definition.ITableField)
	 */
	public LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
    return new CheckBoxInputFieldDefinition(name,null, null, null, position, visible, readonly, tabIndex, paneIndex, caption, localTableAlias, localTableField);
  }


  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLType()
   */
  public final int getSQLType(ISQLDataSource dataSource)
  {
    return Types.BOOLEAN;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLDecimalDigits(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public final int getSQLDecimalDigits(ISQLDataSource dataSource)
  {
    // irrelevant
    return 0;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLSize(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public final int getSQLSize(ISQLDataSource dataSource)
  {
    // irrelevant
    return 0;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getDBDefaultValue()
   */
  public final String getSQLDefaultValue(ISQLDataSource dataSource)
  {
    return this.defaultValue != null ? (this.defaultValue.booleanValue() ? "1" : "0") : null;
  }
}
