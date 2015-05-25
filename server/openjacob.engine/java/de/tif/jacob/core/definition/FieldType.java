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

package de.tif.jacob.core.definition;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.Locale;

import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.LocalInputFieldDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class FieldType
{
  static public final transient String RCS_ID = "$Id: FieldType.java,v 1.6 2010/08/11 22:32:58 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";

  protected FieldType()
	{
		// nothing more to do
	}
  
  /**
   * Converts the given value to the internal data value.
   * 
   * @param dataSource
   *          the data source of the given store internal value to
   * @param value
   *          the value to convert
   * @param oldDataValue
   *          the old internal data value or <code>null</code>
   * @param locale
   *          the locale to use for parsing String values or <code>null</code>
   *          for default parsing
   * @return the resulting internal data value
   * @throws InvalidExpressionException
   *           if <code>value</code> could not be parsed correctly
   * @throws IllegalArgumentException
   *           if <code>value</code> has an invalid type
   */
  public abstract Object convertObjectToDataValue(DataSource dataSource, Object value, Object oldDataValue, Locale locale) throws InvalidExpressionException,
      IllegalArgumentException;

  /**
   * Converts the given internal data value to the external value returned by
   * {@link IDataRecord#getValue(int)}.
   * 
   * @param dataValue
   *          the internal data value
   * @return the external value
   */
  public abstract Object convertDataValueToObject(Object dataValue);

  public abstract Object convertSQLValueToDataValue(SQLDataSource datasource, ResultSet rs, int index)
    throws SQLException;

  /**
   * Converts the given data value to a String.
   * 
   * @param dataValue
   *          the data value
   * @param locale
   *          the locale to use or <code>null</code> for default
   * @param style
   *          the style to use see style constants of {@link IDataRecord}.
   * 
   * @return the desired string presentation
   */
  public abstract String convertDataValueToString(Object dataValue, Locale locale, int style);

  /**
   * Compares two given data values.
   * 
   * @param dataValue1
   *          the first data value
   * @param dataValue2
   *          the second data value
   * @param collator
   *          the collator for string comparison or <code>null</code>
   * @return a negative integer, zero, or a positive integer as the first data
   *         value is less than, equal to, or greater than the second.
   * @since 2.10
   */
  public final int sortDataValue(Object dataValue1, Object dataValue2, Collator collator)
  {
    if (dataValue1 == null)
      return dataValue2 == null ? 0 : -1;
    if (dataValue2 == null)
      return 1;
    return sortDataValueNotNull(dataValue1, dataValue2, collator);
  }
  
  protected abstract int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator);
  
  /**
   * Creates a QBE expression by means of parsing the given constraint.
   * @param dataSource
   *          the data source
   * @param constraint
   *          the constraint to parse
   * 
   * @return The parsed QBE expression or <code>null</code>, if not existing
   * @throws InvalidExpressionException
   *           constraint value can not be parsed
   * @throws IllegalArgumentException
   *           illegal type (class) of constraint value
   */
  public abstract QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException;
  
  public abstract void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException;
  public abstract void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException;
  public abstract void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException;
  
  public boolean isConstrainable()
  {
  	// default behaviour (might be overwritten!)
  	return true;
  }

  public boolean isSortable()
  {
  	// default behaviour (might be overwritten!)
  	return true;
  }
  
  public abstract Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache) throws Exception;
  
  public abstract void toJacob(CastorTableField tableField, ConvertToJacobOptions options);
  
  /**
   * Creates the default input field for this field type.
   * @param name
   * @param position
   * @param visible
   * @param tabIndex
   * @param paneIndex 
   * @param caption
   * @param localTableAlias
   * @param localTableField
   * 
   * @return the created input field or <code>null</code>, if no default exists.
   */
  public abstract LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField);
  
  /**
   * Returns the SQL type of this field.
   * 
   * @param dataSource
   *          the respective data source instance
   * @return SQL type from java.sql.Types
   */
  public abstract int getSQLType(ISQLDataSource dataSource);

  /**
   * Returns the size of the SQL type.
   * @param dataSource the respective data source instance
   * 
   * @return the size or 0 if irrelevant or default
   */
  public abstract int getSQLSize(ISQLDataSource dataSource);

  /**
   * Returns the number of decimal digits of the SQL type.
   * @param dataSource the respective data source instance
   * 
   * @return the number of decimal digits or 0 if irrelevant or default
   */
  public abstract int getSQLDecimalDigits(ISQLDataSource dataSource);

  /**
   * Returns the default value to be set in database.
   * @param dataSource the respective data source instance
   * 
   * @return the default value or <code>null</code> if not existing
   */
  public abstract String getSQLDefaultValue(ISQLDataSource dataSource);
  
  /**
   * Checks whether the given SQL data source provides auto generated keys for
   * this field on creation of a new record.
   * 
   * @param dataSource
   *          the SQL data source
   * @return <code>true</code> if auto generated keys are provided, otherwise
   *         <code>false</code>.
   */
  public boolean isDBAutoGenerated(ISQLDataSource dataSource)
  {
  	// default behaviour (might be overwritten!)
  	return false;
  }
}
