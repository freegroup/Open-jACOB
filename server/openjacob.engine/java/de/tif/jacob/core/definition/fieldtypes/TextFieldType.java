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

import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBELiteralExpression;
import de.tif.jacob.core.data.impl.qbe.QBETextLiteral;
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
import de.tif.jacob.core.definition.impl.jad.castor.TextField;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TextFieldType extends FieldType
{
	static public final transient String RCS_ID = "$Id: TextFieldType.java,v 1.8 2010/08/11 22:32:54 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.8 $";
	
	public final static int UNBOUND = 0;
	public final static int LEFT_BOUND = 1;
	public final static int RIGHT_BOUND = 2;

	private final boolean isCaseSensitive;
	private final int boundMode;
	private final boolean fixedLength;
	private final String defaultValue;
	private final int maxLength;
  
	/**
	 *  
	 */
	public TextFieldType(int maxLength, String defaultValue, boolean isCaseSensitive, int boundMode, boolean fixedLength)
	{
		this.maxLength = maxLength;
		this.defaultValue = "".equals(defaultValue) ? null : defaultValue;
		this.isCaseSensitive = isCaseSensitive;
		this.boundMode = boundMode;
		this.fixedLength = fixedLength;
	}
  
  public TextFieldType(TextField type)
  {
    this(type.getMaxLength(), type.getDefault(), type.getCaseSensitive(), convertSearchMode(type.getSearchMode()), type.getFixedLength());
  }
  
  private static int convertSearchMode(TextFieldSearchModeType mode)
  {
    if (mode == TextFieldSearchModeType.LEFTBOUND)
      return LEFT_BOUND;
    if (mode == TextFieldSearchModeType.RIGHTBOUND)
      return RIGHT_BOUND;
    return UNBOUND;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#createQBEExpression(de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint)
   */
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
    // fetch string
    Object value = constraint.getValue();
    String text;
    try
    {
      text = value.toString();
    }
    catch (Exception ex)
    {
      throw new IllegalArgumentException(value.getClass().toString());
    }

    // and build expression
    if (constraint.isExactMatch())
      return new QBELiteralExpression(new QBETextLiteral(text));

    return QBEExpression.parseText(text);
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.FieldType#getInitDataValue(de.tif.jacob.core.data.DataSource,
	 *      de.tif.jacob.core.definition.ITableDefinition,
	 *      de.tif.jacob.core.definition.ITableField)
	 */
	public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache)
	{
		return this.defaultValue;
	}

	/**
	 * @return Returns the isCaseSensitive.
	 */
	public boolean isCaseSensitive()
	{
		return isCaseSensitive;
	}

	/**
	 * @return Returns the isLeftAnchored.
	 */
	public boolean isLeftAnchored()
	{
		return this.boundMode == LEFT_BOUND;
	}

	public boolean isRightAnchored()
	{
		return this.boundMode == RIGHT_BOUND;
	}

  /**
   * @return Returns the fixedLength.
   */
  public boolean isFixedLength()
  {
    return fixedLength;
  }
  
	/**
	 * toString methode: creates a String representation of the object
	 * 
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	 *  
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("TextFieldType[");
		buffer.append("isCaseSensitive = ").append(isCaseSensitive);
		buffer.append(", boundMode = ").append(this.boundMode);
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * @return Returns the defaultValue.
	 */
	public final String getDefaultValue()
	{
		return defaultValue;
	}
  
  public int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator)
  {
    String val1 = (String) dataValue1;
    String val2 = (String) dataValue2;

    if (collator == null)
      return val1.compareTo(val2);
    return collator.compare(val1, val2);
  }
  
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
    String str;
    
		if (object == null)
		{
			return null;
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
    
    // treat empty strings as null!
    if (str.length() == 0)
    {  
      return null;
    }
    
    // check max length
    if (str.length() > this.maxLength)
    {
      throw new InvalidExpressionException(str, "Field too long");
    }
    return str;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#convertDataValueToObject(java.lang.Object)
   */
  public Object convertDataValueToObject(Object dataValue)
  {
    // object is not mutable -> just pass thru
		return dataValue;
  }
  
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.FieldType#convertSqlValueToDataValue(de.tif.jacob.core.data.sql.SQLDataSource,
	 *      java.sql.ResultSet, int)
	 */
	public Object convertSQLValueToDataValue(SQLDataSource datasource, ResultSet rs, int index) throws SQLException
	{
		return rs.getString(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext,
	 *      java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
		statement.setString(index, (String) dataValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext,
	 *      java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
		statement.setString(index, (String) dataValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValue(de.tif.jacob.core.data.sql.SQLExecutionContext,
	 *      java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
		statement.setString(index, (String) dataValue);
	}

	/**
	 * Returns the maximal length of this field type
	 * 
	 * @return maximal length or 0 for unlimited length
	 */
	public int getMaxLength()
	{
		return maxLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.FieldType#toJacob(de.tif.jacob.core.jad.castor.TableField)
	 */
  public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
		TextField type = new TextField();
		type.setCaseSensitive(this.isCaseSensitive);
		type.setDefault(this.defaultValue);
		type.setMaxLength(this.maxLength);
		type.setSearchMode(isLeftAnchored() ? TextFieldSearchModeType.LEFTBOUND : (isRightAnchored() ? TextFieldSearchModeType.RIGHTBOUND : TextFieldSearchModeType.UNBOUND));
		type.setFixedLength(this.fixedLength);
		tableField.getCastorTableFieldChoice().setText(type);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#createDefaultInputField(java.lang.String, de.tif.jacob.core.definition.guielements.Dimension, boolean, int, de.tif.jacob.core.definition.guielements.Caption, de.tif.jacob.core.definition.ITableAlias, de.tif.jacob.core.definition.ITableField)
	 */
	public LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
		return new TextInputFieldDefinition(name,null, null, null, position, visible, readonly, false, tabIndex, paneIndex, caption, localTableAlias, localTableField, null, null);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLType()
   */
  public int getSQLType(ISQLDataSource dataSource)
  {
    if (this.fixedLength)
      return Types.CHAR;
    return Types.VARCHAR;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSize()
   */
  public int getSQLSize(ISQLDataSource dataSource)
  {
    return this.maxLength;
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
   * @see de.tif.jacob.core.definition.FieldType#getDBDefaultValue()
   */
  public String getSQLDefaultValue(ISQLDataSource dataSource)
  {
    return this.defaultValue;
  }
  
}
