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
import java.sql.Timestamp;
import java.sql.Types;
import java.text.Collator;
import java.util.Locale;

import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBELiteralExpression;
import de.tif.jacob.core.data.impl.qbe.QBETimestampLiteral;
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
import de.tif.jacob.core.definition.guielements.TimestampInputFieldDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.TimestampField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.util.DatetimeUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class TimestampFieldType extends FieldType
{
  static public final transient String RCS_ID = "$Id: TimestampFieldType.java,v 1.10 2010/08/11 22:32:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.10 $";

  private final String defaultValue;
  private final TimestampResolution resolution;
  
	/**
	 * 
	 */
	public TimestampFieldType(String defaultValue, TimestampResolution resolution)
	{
    this.defaultValue = defaultValue;
    this.resolution = resolution;
  }

  public TimestampFieldType(TimestampField type)
  {
    this.defaultValue = type.getDefault();
    this.resolution = TimestampResolution.fromJacob(type.getResolution());
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#createQBEExpression(de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint)
   */
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
    Object value = constraint.getValue();
    if (value instanceof String)
    {
      return QBEExpression.parseTimestamp((String) value);
    }
    else if (value instanceof Timestamp)
    {
      return new QBELiteralExpression(new QBETimestampLiteral((Timestamp) value));
    }
    else
    {
      throw new IllegalArgumentException(value.getClass().toString());
    }
  }

  public Object convertSQLValueToDataValue(SQLDataSource datasource, ResultSet rs, int index) throws SQLException
  {
    return this.resolution.adjust(rs.getTimestamp(index));
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
    if (object instanceof Timestamp)
    {
      // clone the object because it is mutable!
      return this.resolution.adjust(new Timestamp(((Timestamp) object).getTime()));
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
          return new Timestamp(Long.parseLong(expression.substring(0, expression.length() - 1)));
        }
        catch (NumberFormatException ex)
        {
        }
      }
      
      return this.resolution.adjust(DatetimeUtil.convertToTimestamp(expression));
    }
    if (object instanceof Long)
    {
      return this.resolution.adjust(new Timestamp(((Long) object).longValue()));
    }
    if (object instanceof java.util.Date)
    {
      return this.resolution.adjust(new Timestamp(((java.util.Date) object).getTime()));
    }
    throw new IllegalArgumentException(object.getClass().toString());
  }

  public Object convertDataValueToObject(Object dataValue)
  {
    // object is mutable -> clone
		return ((Timestamp) dataValue).clone();
  }
  
  public int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator)
  {
    Timestamp val1 = (Timestamp) dataValue1;
    Timestamp val2 = (Timestamp) dataValue2;

    long diff = val1.getTime() - val2.getTime();
    return diff == 0 ? 0 : (diff > 0 ? 1 : -1);
  }
  
	public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache) throws Exception
	{
    return this.resolution.adjust(DatetimeUtil.convertToTimestamp(this.defaultValue));
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValue(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    statement.setTimestamp(index, (Timestamp) dataValue); 
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    statement.setTimestamp(index, (Timestamp) dataValue); 
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    statement.setTimestamp(index, (Timestamp) dataValue); 
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#toJacob(de.tif.jacob.core.jad.castor.TableField)
	 */
  public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
    TimestampField type = new TimestampField();
    type.setDefault(this.defaultValue);
    type.setResolution(this.resolution.toJacob());
    tableField.getCastorTableFieldChoice().setTimestamp(type);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#convertDataValueToString(java.lang.Object)
	 */
	public String convertDataValueToString(Object value, Locale locale, int style)
	{
    if (style == IDataRecord.RAW_STYLE && value != null)
      return Long.toString(((Timestamp) value).getTime()) + "L";
    
		return this.resolution.toString((Timestamp) value, locale);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#createDefaultInputField(java.lang.String, de.tif.jacob.core.definition.guielements.Dimension, boolean, int, de.tif.jacob.core.definition.guielements.Caption, de.tif.jacob.core.definition.ITableAlias, de.tif.jacob.core.definition.ITableField)
	 */
	public LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
		return new TimestampInputFieldDefinition(name, null, null, null, position, visible, readonly, tabIndex, paneIndex, caption, localTableAlias, localTableField, null);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLType()
   */
  public int getSQLType(ISQLDataSource dataSource)
  {
    return Types.TIMESTAMP;
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
