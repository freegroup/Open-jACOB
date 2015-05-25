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

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.adjustment.IFieldTypeAdjustment;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.data.impl.DataBinary;
import de.tif.jacob.core.data.impl.DataRecordReference;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.DataTableRecord;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class BinaryAdjustment implements IFieldTypeAdjustment
{
	public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, Object defaultValue)
	{
    return new DefaultBinary(null);
  }
	
	public Object convertObjectToDataValue(Object object, Object oldValue) throws IllegalArgumentException
	{
    if (object == null || object instanceof byte[])
    {
      // clone the array because it is mutable!
      if (object != null)
        object = ((byte[]) object).clone();
      return new DefaultBinary((byte[]) object);
    }
    throw new IllegalArgumentException(object.getClass().toString());
  }
	
	public Object convertSQLValueToDataValue(FieldType fieldType, SQLDataSource datasource, ResultSet rs, int index) throws SQLException
	{
    return new DefaultBinary(rs.getBytes(index));
  }
	
  public int getSQLType(FieldType fieldType, ISQLDataSource dataSource)
  {
    return dataSource.getDefaultBinarySQLType();
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
    // no default supported for binary fields
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
    DataBinary binary = (DataBinary) dataValue;
    
    if (binary.isNull())
      statement.setNull(index, java.sql.Types.LONGVARBINARY);
    else
    {
      byte[] binaryData = binary.getValue();
      if (binaryData.length == 0)
      {
        statement.setNull(index, java.sql.Types.LONGVARBINARY);
      }
      else
      {
        statement.setBinaryStream(index, new ByteArrayInputStream(binaryData), binaryData.length);
        // Do not use the following statement since Oracle throws:
        // ORA-01461: Ein LONG-Wert kann nur zur Einfügung in eine LONG-Spalte
        // gebunden werden
        // This seams to happen only for update statements, nevertheless we do
        // it for insert as well!
        //      statement.setBytes(index, binaryData);
      }
    }
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValueForUpdate(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
   */
  public void setSQLValueForUpdate(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
    DataBinary binary = (DataBinary) dataValue;
    
    if (binary.isNull())
      statement.setNull(index, java.sql.Types.LONGVARBINARY);
    else
    {
      byte[] binaryData = binary.getValue();
      if (binaryData.length == 0)
      {
        statement.setNull(index, java.sql.Types.LONGVARBINARY);
      }
      else
      {
        statement.setBinaryStream(index, new ByteArrayInputStream(binaryData), binaryData.length);
        // Do not use the following statement since Oracle throws:
        // ORA-01461: Ein LONG-Wert kann nur zur Einfügung in eine LONG-Spalte
        // gebunden werden
        // This seams to happen only for update statements, nevertheless we do
        // it for insert as well!
        //      statement.setBytes(index, binaryData);
      }
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
  
  /**
   * @author Andreas
   *
   * To change the template for this generated type comment go to
   * Window - Preferences - Java - Code Generation - Code and Comments
   */
  private static class DefaultBinary extends DataBinary
  {
    private DataRecordReference dataRecordReference;
    private String tableFieldName;
    
    
    public DefaultBinary(byte[] binaryData)
    {
      super(binaryData);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.DataBinary#fetchBinary()
     */
    protected byte[] fetchBinary()
    {
      DataAccessor accessor = new DataAccessor(Context.getCurrent().getApplicationDefinition());

      try
      {
      	IDataTableRecord record = accessor.selectRecord(this.dataRecordReference);
      	return record.getBytesValue(this.tableFieldName);
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

    protected String toReference(IDataTableRecord record, String fieldName)
    {
      this.dataRecordReference = ((DataTableRecord) record).getReference();
      this.tableFieldName = fieldName;
      return this.toReferenceInternal();
    }
  }
  
}
