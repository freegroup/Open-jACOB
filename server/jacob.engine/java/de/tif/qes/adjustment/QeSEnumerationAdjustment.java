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

package de.tif.qes.adjustment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import de.tif.jacob.core.adjustment.impl.EnumerationAdjustment;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.data.impl.sql.SQLStatementBuilder;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.fieldtypes.EnumerationFieldType;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QeSEnumerationAdjustment extends EnumerationAdjustment
{
  static public final transient String RCS_ID = "$Id: QeSEnumerationAdjustment.java,v 1.3 2009-12-07 03:36:09 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
   */
  public void setSQLValueForInsert(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
  	// convert value from String to Integer 
  	Integer intValue = ((EnumerationFieldType) fieldType).mapEnumStrToInt((String)dataValue);
  	if (null == intValue)
  		statement.setNull(index, java.sql.Types.INTEGER);
  	else
  		statement.setInt(index, intValue.intValue());
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
   */
  public void setSQLValueForUpdate(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
  	// convert value from String to Integer 
  	Integer intValue = ((EnumerationFieldType) fieldType).mapEnumStrToInt((String)dataValue);
  	if (null == intValue)
  		statement.setNull(index, java.sql.Types.INTEGER);
  	else
  		statement.setInt(index, intValue.intValue());
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#setSqlValue(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
   */
  public void setSQLValue(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
  	// convert value from String to Integer 
  	dataValue = ((EnumerationFieldType) fieldType).mapEnumStrToInt((String)dataValue);
  	if (null == dataValue)
  		statement.setNull(index, java.sql.Types.INTEGER);
  	else
  		statement.setInt(index, ((Integer) dataValue).intValue());
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertSqlValueToDataValue(de.tif.jacob.core.data.impl.sql.SQLDataSource, java.sql.ResultSet, int)
   */
  public Object convertSQLValueToDataValue(FieldType fieldType, SQLDataSource datasource, ResultSet rs, int index) throws SQLException
	{
		int enumInt = rs.getInt(index);
		if (rs.wasNull())
			return null;
		return ((EnumerationFieldType) fieldType).getEnumeratedValue(enumInt);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IEnumerationFieldTypeAdjustment#appendSqlEnumValue(de.tif.jacob.core.definition.fieldtypes.EnumerationFieldType, de.tif.jacob.core.data.impl.sql.SQLStatementBuilder, java.lang.String)
   */
  public void appendSqlEnumValue(EnumerationFieldType fieldType, SQLDataSource dataSource, SQLStatementBuilder sqlQuery, String value) throws InvalidExpressionException
  {
    Integer enumInt = fieldType.mapEnumStrToInt(value);
    if (null == enumInt)
    {
      throw new InvalidExpressionException(value, "Is not an enum value of " + fieldType);
    }
    sqlQuery.append(enumInt.intValue());
  }

  public int getSQLType(FieldType fieldType, ISQLDataSource dataSource)
  {
    return Types.SMALLINT;
  }
  
  public int getSQLSize(FieldType fieldType, ISQLDataSource dataSource)
  {
    // irrelevant size
    return 0;
  }
  
  public String getSQLDefaultValue(FieldType fieldType, ISQLDataSource dataSource)
  {
    EnumerationFieldType enumerationFieldType = (EnumerationFieldType) fieldType;
    
  	// convert value from String to Integer 
  	Integer intValue = enumerationFieldType.mapEnumStrToInt(enumerationFieldType.getDefaultValue());
  	return intValue == null ? null : intValue.toString();
  }
}
