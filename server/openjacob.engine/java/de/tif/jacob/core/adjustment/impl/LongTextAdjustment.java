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

package de.tif.jacob.core.adjustment.impl;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.adjustment.ILongTextFieldTypeAdjustment;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.data.impl.DataLongText;
import de.tif.jacob.core.data.impl.DataRecordReference;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.DataTableRecord;
import de.tif.jacob.core.data.impl.index.IndexDataSource;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;

/**
 * Base class for long text adjustments.
 * 
 * @author andreas
 */
public abstract class LongTextAdjustment implements ILongTextFieldTypeAdjustment
{

  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertObjectToDataValue(java.lang.Object, java.lang.Object)
   */
  public Object convertObjectToDataValue(Object object, Object oldValue) throws IllegalArgumentException
  {
    if (object instanceof DefaultLongText)
    {
      return object;
    }
    
    String str;
    if (object == null)
    {
      str = null;
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
    return new DefaultLongText(str);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertSqlValueToDataValue(de.tif.jacob.core.data.impl.sql.SQLDataSource, java.sql.ResultSet, int)
   */
  public Object convertSQLValueToDataValue(FieldType fieldType, SQLDataSource datasource, ResultSet rs, int index) throws SQLException
  {
    return new DefaultLongText(rs.getString(index));
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#getInitDataValue(de.tif.jacob.core.data.IDataSource, de.tif.jacob.core.definition.ITableDefinition, de.tif.jacob.core.definition.ITableField)
   */
  public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, Object defaultValue)
  {
    return new DefaultLongText(null);
  }
  
  public int getSQLType(FieldType fieldType, ISQLDataSource dataSource)
  {
    return dataSource.getDefaultLongTextSQLType();
  }
  
  public int getSQLSize(FieldType fieldType, ISQLDataSource dataSource)
  {
    // irrelevant
    return 0;
  }
  
  public int getSQLDecimalDigits(FieldType fieldType, ISQLDataSource dataSource)
  {
    // irrelevant
    return 0;
  }
  
  public final String getSQLDefaultValue(FieldType fieldType, ISQLDataSource dataSource)
  {
    // do not set default values on database level
    return null;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValue(de.tif.jacob.core.definition.FieldType, de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
   */
  public final void setSQLValue(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValueForInsert(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
   */
  public void setSQLValueForInsert(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
    DataLongText longText = (DataLongText) dataValue;
    
    if (longText.isNull())
      statement.setNull(index, java.sql.Types.LONGVARCHAR);
    else
    {
      String stringValue = longText.getValue();
      statement.setCharacterStream(index, new StringReader(stringValue), stringValue.length());
    }
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValueForUpdate(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
   */
  public void setSQLValueForUpdate(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
    DataLongText longText = (DataLongText) dataValue;
    
    if (longText.isNull())
      statement.setNull(index, java.sql.Types.LONGVARCHAR);
    else
    {
      String stringValue = longText.getValue();
      statement.setCharacterStream(index, new StringReader(stringValue), stringValue.length());
    }
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#deleteFieldValue(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.lang.Object)
   */
  public void deleteFieldValue(SQLExecutionContext context, Object dataValue) throws SQLException
  {
    // do nothing here
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#deleteFieldValues(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.lang.String)
   */
  public void deleteFieldValues(SQLExecutionContext context, String embeddedSelectFragment) throws SQLException
  {
    // do nothing here
  }
  
  public boolean supportsLongTextSearch(LongTextFieldType fieldType, DataSource dataSource)
  {
    if (dataSource instanceof SQLDataSource)
    {
      SQLDataSource sqlDatasource = (SQLDataSource) dataSource;

      if (sqlDatasource.needsMappingOfLongText(fieldType))
        return false;
      
      return sqlDatasource.supportsLongTextSearch(fieldType.getSQLType(sqlDatasource), fieldType.isCaseSensitive());
    }
    if (dataSource instanceof IndexDataSource)
    {
      return true;
    }
    return false;
  }

  /**
   * @author Andreas
   *
   * To change the template for this generated type comment go to
   * Window - Preferences - Java - Code Generation - Code and Comments
   */
  private static class DefaultLongText extends DataLongText
  {
    private DataRecordReference dataRecordReference;
    private String tableFieldName;
    
    public DefaultLongText(String longText)
    {
      super(longText);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.DataLongText#fetchLongText()
     */
    protected String fetchLongText()
    {
      DataAccessor accessor = new DataAccessor(Context.getCurrent().getApplicationDefinition());

      try
      {
      	IDataTableRecord record = accessor.selectRecord(this.dataRecordReference);
      	return record.getStringValue(this.tableFieldName);
      }
      catch (RuntimeException ex)
      {
      	throw ex;
      }
      catch (Exception ex)
      {
      	throw new RuntimeException(ex);
      }
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.DataLongText#toReference(de.tif.jacob.core.data.DataTableRecord, java.lang.String)
     */
    protected String toReference(IDataTableRecord record, String fieldName)
    {
      this.dataRecordReference = ((DataTableRecord) record).getReference();
      this.tableFieldName = fieldName;
      return this.toReferenceInternal();
    }

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.data.DataLongText#append(java.lang.String)
		 */
		protected DataLongText append(String text)
		{
			String oldValue = getValue();
			return new DefaultLongText(oldValue == null ? text : oldValue + text);
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.data.DataLongText#prepend(java.lang.String)
		 */
		protected DataLongText prepend(String text)
		{
			String oldValue = getValue();
			return new DefaultLongText(oldValue == null ? text : text + oldValue);
    }
  }
}
