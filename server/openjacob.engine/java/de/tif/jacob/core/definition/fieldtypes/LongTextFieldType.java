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

import de.tif.jacob.core.data.impl.DataLongText;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBEScanner;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.LocalInputFieldDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.LongTextField;
import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextFieldSqlTypeType;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * Long text field type.
 * 
 * @author Andreas
 */
public final class LongTextFieldType extends TextFieldType
{
  static public final transient String RCS_ID = "$Id: LongTextFieldType.java,v 1.10 2010/08/11 22:32:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.10 $";
  
  private final boolean changeHeaderEnabled;
  
  public static final LongTextEditMode PREPEND  = LongTextEditMode.fromJacob(LongTextEditModeType.PREPEND);
  public static final LongTextEditMode APPEND   = LongTextEditMode.fromJacob(LongTextEditModeType.APPEND);
  public static final LongTextEditMode FULLEDIT = LongTextEditMode.fromJacob(LongTextEditModeType.FULLEDIT);

  /**
   * Flag to indicate that mapping of long text fields (i.e. long text values
   * are contained in jacob_text) is disabled in any case.
   * 
   * @see SQLDataSource#needsMappingOfLongText(FieldType)
   */
  private final boolean disableMapping;
  
  private final LongTextEditMode editMode;

  private final LongTextFieldSqlTypeType sqlType;
  
	/**
	 * 
	 */
	public LongTextFieldType(String defaultValue, boolean changeHeaderEnabled)
	{
		super(0, defaultValue, false, UNBOUND, false);
		this.changeHeaderEnabled = changeHeaderEnabled;
    this.disableMapping = false;
    this.editMode = LongTextEditMode.FULLEDIT;
    this.sqlType = null;
	}

  public LongTextFieldType(LongTextField type)
  {
    super(0, type.getDefault(), false, UNBOUND, false);
    this.changeHeaderEnabled = type.getChangeHeader();
    this.disableMapping = type.getDisableMapping();
    this.editMode = LongTextEditMode.fromJacob(type.getEditMode());
    this.sqlType = type.getSqlType();
  }
  
  public final String convertDataValueToString(Object value, Locale locale, int style)
  {
    return ((DataLongText) value).getValue();
  }
  
  public final boolean isConstrainable()
  {
    return true;
  }

  /**
   * Returns the default edit mode of a long text field 
   * or NULL if no edit mode has been defined in the application definition (JAD)
   * 
   * @return the editMode
   * @since 2.7.2
   */
  public LongTextEditMode getEditMode()
  {
    return editMode;
  }

  /**
   * @return Returns the disableMapping.
   */
  public final boolean isDisableMapping()
  {
    return disableMapping;
  }
  
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
    // do we allow text search on long text fields?
    if (dataSource.getAdjustment().getLongTextAdjustment().supportsLongTextSearch(this, dataSource))
      return super.createQBEExpression(dataSource, constraint);

    Object value = constraint.getValue();
    if (value instanceof String)
    {
      // Restrict search on long text values on LONGTEXTFIELD IS NULL or
      // LONGTEXTFIELD IS NOT NULL
      try
      {
        return QBEExpression.parse(QBEScanner.NULL_CHECK, (String) value);
      }
      catch (InvalidExpressionException ex)
      {
        // convert exception to more specific exception
        throw new InvalidExpressionException(ex.getExpression(), InvalidExpressionException.LONGTEXT_TYPE);
      }
    }
    else
    {
      throw new IllegalArgumentException(value.getClass().toString());
    }
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#getInitDataValue(de.tif.jacob.core.data.DataSource, de.tif.jacob.core.definition.ITableDefinition, de.tif.jacob.core.definition.ITableField)
	 */
	public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache)
  {
    // delegate request
    return dataSource.getAdjustment().getLongTextAdjustment().getInitDataValue(dataSource, table, field, getDefaultValue());
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValue(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    // delegate request
    context.getDataSource().getAdjustment().getLongTextAdjustment().setSQLValue(this, context, statement, index, dataValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    // delegate request
    context.getDataSource().getAdjustment().getLongTextAdjustment().setSQLValueForInsert(this, context, statement, index, dataValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    // delegate request
    context.getDataSource().getAdjustment().getLongTextAdjustment().setSQLValueForUpdate(this, context, statement, index, dataValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#convertObjectToDataValue(java.lang.Object, java.lang.Object)
	 */
	public Object convertObjectToDataValue(DataSource dataSource, Object object, Object oldValue, Locale locale) throws IllegalArgumentException
	{
	  // delegate request
    return dataSource.getAdjustment().getLongTextAdjustment().convertObjectToDataValue(object, oldValue);
	}

  public int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator)
  {
    DataLongText val1 = (DataLongText) dataValue1;
    DataLongText val2 = (DataLongText) dataValue2;

    if (val1.isNull())
      return val2.isNull() ? 0 : -1;
    if (val2.isNull())
      return 1;

    if (collator == null)
      return val1.getValue().compareTo(val2.getValue());
    return collator.compare(val1.getValue(), val2.getValue());
  }
  
  public Object convertDataValueToObject(Object dataValue)
  {
	  // return long text as string
		return ((DataLongText) dataValue).getValue();
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#convertSqlValueToDataValue(de.tif.jacob.core.data.sql.SQLDataSource, java.sql.ResultSet, int)
	 */
	public Object convertSQLValueToDataValue(SQLDataSource dataSource, ResultSet rs, int index) throws SQLException
  {
    // delegate request
    return dataSource.getAdjustment().getLongTextAdjustment().convertSQLValueToDataValue(this, dataSource, rs, index);
  }

	public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
    LongTextField type = new LongTextField();
    type.setDefault(getDefaultValue());
    type.setChangeHeader(isChangeHeaderEnabled());
    type.setEditMode(editMode.toJacob());
    if (this.disableMapping)
      type.setDisableMapping(true);
    tableField.getCastorTableFieldChoice().setLongText(type);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#createDefaultInputField(java.lang.String, de.tif.jacob.core.definition.guielements.Dimension, boolean, boolean, int, de.tif.jacob.core.definition.guielements.Caption, de.tif.jacob.core.definition.ITableAlias, de.tif.jacob.core.definition.ITableField)
	 */
	public LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
    // Not supported for use in mutable inform browser
    return null;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLType()
   */
  public int getSQLType(ISQLDataSource dataSource)
  {
    if (LongTextFieldSqlTypeType.CLOB.equals(this.sqlType))
      return Types.CLOB;
    if (LongTextFieldSqlTypeType.LONGVARCHAR.equals(this.sqlType))
      return Types.LONGVARCHAR;
    
	  // delegate request
    return dataSource.getAdjustment().getLongTextAdjustment().getSQLType(this, dataSource);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSize()
   */
  public int getSQLSize(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getLongTextAdjustment().getSQLSize(this, dataSource);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getDBDefaultValue()
   */
  public String getSQLDefaultValue(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getLongTextAdjustment().getSQLDefaultValue(this, dataSource);
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#isSortable()
	 */
	public boolean isSortable()
	{
		return false;
	}
	
	/**
	 * @return Returns the changeHeaderEnabled.
	 */
	public boolean isChangeHeaderEnabled()
	{
		return changeHeaderEnabled;
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
    buffer.append("LongTextFieldType[");
    buffer.append("changeHeaderEnabled = ").append(changeHeaderEnabled);
    buffer.append(", editMode = ").append(this.getEditMode());
    buffer.append("]");
    return buffer.toString();
  }
}
