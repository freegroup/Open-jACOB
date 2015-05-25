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

import de.tif.jacob.core.data.impl.DataBinary;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBEScanner;
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
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.BinaryField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.types.BinaryFieldSqlTypeType;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class BinaryFieldType extends FieldType
{
  static public final transient String RCS_ID = "$Id: BinaryFieldType.java,v 1.6 2010/08/11 22:32:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";

  /**
   * Flag to indicate that mapping of binary fields (i.e. binary values
   * are contained in jacob_binary) is disabled in any case.
   * 
   * @see SQLDataSource#needsMappingOfBinary(FieldType)
   */
  private final boolean disableMapping;

  private final BinaryFieldSqlTypeType sqlType;

  public BinaryFieldType()
  {
    this.disableMapping = false;
    this.sqlType = null;
  }

  public BinaryFieldType(BinaryField type)
  {
    this.disableMapping = type.getDisableMapping();
    this.sqlType = type.getSqlType();
  }
  
  // IBIS: Prüfen ob wir dann noch diese Methode brauchen!
  public boolean isConstrainable()
  {
    return true;
  }
  
  /**
   * @return Returns the disableMapping.
   */
  public final boolean isDisableMapping()
  {
    return disableMapping;
  }
  
	public boolean isSortable()
	{
		return false;
	}

  public String convertDataValueToString(Object value, Locale locale, int style)
  {
    throw new UnsupportedOperationException();
  }
  
  public int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator)
  {
    // no sorting provided
    return 0;
  }

  /* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValue(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    // delegate request
    context.getDataSource().getAdjustment().getBinaryAdjustment().setSQLValue(this, context, statement, index, dataValue);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#createQBEExpression(de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint)
   */
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
    Object value = constraint.getValue();
    if (value instanceof String)
    {
      // Restrict search on long text values on BINARYFIELD IS NULL or
      // BINARYFIELD IS NOT NULL
      try
      {
        return QBEExpression.parse(QBEScanner.NULL_CHECK, (String) value);
      }
      catch (InvalidExpressionException ex)
      {
        // convert exception to more specific exception
        throw new InvalidExpressionException(ex.getExpression(), InvalidExpressionException.BINARY_TYPE);
      }
    }
    else
    {
      throw new IllegalArgumentException(value.getClass().toString());
    }
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#getInitDataValue(de.tif.jacob.core.data.DataSource, de.tif.jacob.core.definition.ITableField)
	 */
	public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache)
	{
	  // delegate request
    return dataSource.getAdjustment().getBinaryAdjustment().getInitDataValue(dataSource, table, field, null);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#toJacob(de.tif.jacob.core.jad.castor.TableField)
	 */
	public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
	  BinaryField type = new BinaryField();
    if (this.disableMapping)
      type.setDisableMapping(true);
    if (this.sqlType!=null)
      type.setSqlType(this.sqlType);
    tableField.getCastorTableFieldChoice().setBinary(type);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#convertObjectToDataValue(java.lang.Object, java.lang.Object)
	 */
	public Object convertObjectToDataValue(DataSource dataSource, Object object, Object oldValue, Locale locale)
	{
	  // delegate request
    return dataSource.getAdjustment().getBinaryAdjustment().convertObjectToDataValue(object, oldValue);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#convertDataValueToObject(java.lang.Object)
   */
  public Object convertDataValueToObject(Object dataValue)
  {
	  // return binary as byte[]
		return ((DataBinary) dataValue).getValue();
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#convertSqlValueToDataValue(de.tif.jacob.core.data.sql.SQLDataSource, java.sql.ResultSet, int)
	 */
	public Object convertSQLValueToDataValue(SQLDataSource dataSource, ResultSet rs, int index) throws SQLException
	{
	  // delegate request
    return dataSource.getAdjustment().getBinaryAdjustment().convertSQLValueToDataValue(this, dataSource, rs, index);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
    // delegate request
    context.getDataSource().getAdjustment().getBinaryAdjustment().setSQLValueForInsert(this, context, statement, index, dataValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    // delegate request
    context.getDataSource().getAdjustment().getBinaryAdjustment().setSQLValueForUpdate(this, context, statement, index, dataValue);
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#createDefaultInputField(java.lang.String, de.tif.jacob.core.definition.guielements.Dimension, boolean, int, de.tif.jacob.core.definition.guielements.Caption, de.tif.jacob.core.definition.ITableAlias, de.tif.jacob.core.definition.ITableField)
	 */
	public LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
    throw new UnsupportedOperationException("GUI field [" + name + "] of table field [" + localTableAlias.getName() + "." + localTableField.getName()
        + "] is unable to create a default UI representation");
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLType()
   */
  public int getSQLType(ISQLDataSource dataSource)
  {
    if (BinaryFieldSqlTypeType.BLOB.equals(this.sqlType))
      return Types.BLOB;
    if (BinaryFieldSqlTypeType.LONGVARBINARY.equals(this.sqlType))
      return Types.LONGVARBINARY;
    
	  // delegate request
    return dataSource.getAdjustment().getBinaryAdjustment().getSQLType(this, dataSource);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLSize(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public int getSQLSize(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getBinaryAdjustment().getSQLSize(this, dataSource);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLDecimalDigits(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public int getSQLDecimalDigits(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getBinaryAdjustment().getSQLDecimalDigits(this, dataSource);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getDBDefaultValue(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public String getSQLDefaultValue(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getBinaryAdjustment().getSQLDefaultValue(this, dataSource);
  }
}
