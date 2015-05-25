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
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.Collator;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBELiteralExpression;
import de.tif.jacob.core.data.impl.qbe.QBETimeLiteral;
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
import de.tif.jacob.core.definition.guielements.TimeInputFieldDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.TimeField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.util.DatetimeUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class TimeFieldType extends FieldType
{
  static public final transient String RCS_ID = "$Id: TimeFieldType.java,v 1.12 2010/08/11 22:32:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.12 $";
  
  private final String defaultValue;
  
	/**
	 * 
	 */
	public TimeFieldType(String defaultValue)
	{
    this.defaultValue = defaultValue;
  }

  public TimeFieldType(TimeField type)
  {
    this.defaultValue = type.getDefault();
  }

	public String convertDataValueToString(Object value, Locale locale, int style)
	{
    if (style == IDataRecord.RAW_STYLE && value != null)
      return Long.toString(((Time) value).getTime()) + "L";
    
    return DatetimeUtil.convertTimeToString((Time) value, locale);
	}
	
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
    Object value = constraint.getValue();
    if (value instanceof String)
    {
      return QBEExpression.parseTime((String) value);
    }
    else if (value instanceof Time)
    {
      return new QBELiteralExpression(new QBETimeLiteral((Time) value));
    }
    else
    {
      throw new IllegalArgumentException(value.getClass().toString());
    }
  }

  public Object convertSQLValueToDataValue(SQLDataSource datasource, ResultSet rs, int index) throws SQLException
  {
    return rs.getTime(index);
  }
  
  /**
   * Adjust the time value by means of reseting date part.
   * 
   * @param time millis since 1.1.1970
   * @return the adjusted time
   */
  private Time adjust(long time)
  {
    // TODO: initialize Locale and TimeZone
    Calendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(time);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.MONTH, 0);
    calendar.set(Calendar.YEAR, 1970);
    return new Time(calendar.getTimeInMillis());
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#convertObjectToDataValue(java.lang.Object)
	 */
	public Object convertObjectToDataValue(DataSource dataSource, Object object, Object oldValue, Locale locale) throws InvalidExpressionException, IllegalArgumentException
	{
    if (object == null)
    {
      return null;
    }
    if (object instanceof Time)
    {
      // clone the object because it is mutable!
      return new Time(((Time) object).getTime());
    }
    if (object instanceof String)
    {
      String expression = ((String) object).trim();
      if (expression.length() == 0)
        return null;
      
      // handle raw string
      //
      if (expression.endsWith("L"))
      {
        try
        {
          return new Time(Long.parseLong(expression.substring(0, expression.length() - 1)));
        }
        catch (NumberFormatException ex)
        {
        }
      }
      
      return DatetimeUtil.convertToTime(expression);
    }
    if (object instanceof Timestamp)
    {
      return adjust(((Timestamp) object).getTime());
    }
    if (object instanceof Long)
    {
      return adjust(((Long) object).longValue());
    }
    if (object instanceof java.sql.Date)
    {
      throw new IllegalArgumentException(object.getClass().toString());
    }
    else if (object instanceof java.util.Date)
    {
      return adjust(((java.util.Date) object).getTime());
    }
    throw new IllegalArgumentException(object.getClass().toString());
  }

  public Object convertDataValueToObject(Object dataValue)
  {
    // object is mutable -> clone
		return ((Time) dataValue).clone();
  }
  
  public int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator)
  {
    Time val1 = (Time) dataValue1;
    Time val2 = (Time) dataValue2;

    long diff = val1.getTime() - val2.getTime();
    return diff == 0 ? 0 : (diff > 0 ? 1 : -1);
  }
  
	public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache) throws Exception
	{
    return DatetimeUtil.convertToTime(this.defaultValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValue(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    statement.setTime(index, (Time) dataValue); 
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    statement.setTime(index, (Time) dataValue); 
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    statement.setTime(index, (Time) dataValue); 
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#toJacob(de.tif.jacob.core.jad.castor.TableField)
	 */
  public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
    TimeField type = new TimeField();
    type.setDefault(this.defaultValue);
    tableField.getCastorTableFieldChoice().setTime(type);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#createDefaultInputField(java.lang.String, de.tif.jacob.core.definition.guielements.Dimension, boolean, int, de.tif.jacob.core.definition.guielements.Caption, de.tif.jacob.core.definition.ITableAlias, de.tif.jacob.core.definition.ITableField)
	 */
	public LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
		return new TimeInputFieldDefinition(name, null, null, null, position, visible, readonly, tabIndex, paneIndex, caption, localTableAlias, localTableField, null);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLType()
   */
  public int getSQLType(ISQLDataSource dataSource)
  {
    return Types.TIME;
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
   * @see de.tif.jacob.core.definition.FieldType#getDBDefaultValue(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public String getSQLDefaultValue(ISQLDataSource dataSource)
  {
    // no defaults on data source level
    return null;
  }
}
