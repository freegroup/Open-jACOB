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
import java.sql.Types;
import java.util.Collections;
import java.util.Map;

import de.tif.jacob.core.adjustment.IDocumentFieldTypeAdjustment;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.UUID;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.data.impl.sql.SQLStatementBuilder;
import de.tif.jacob.core.data.impl.sql.WrapperPreparedStatement;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author andreas
 *  
 */
public class JacobDocumentAdjustment implements IDocumentFieldTypeAdjustment
{
  static public final transient String RCS_ID = "$Id: JacobDocumentAdjustment.java,v 1.8 2010/10/26 23:30:41 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.8 $";

  /**
   * Dummy field type to allow searches on document table
   */
  private static final ITableField DUMMY_JACOB_DOCUMENT_NAME_FIELD = new ITableField()
  {
    private FieldType type = new TextFieldType(200, null, false, TextFieldType.UNBOUND, false);

    public String getName()
    {
      return "name";
    }
    
    public Map getProperties()
    {
      return Collections.EMPTY_MAP;
    }
    
    public String getDescription()
    {
      return null;
    }

    public String getProperty(String name)
    {
      return null;
    }

    public String getDBName()
    {
      return "docname";
    }

    public String getLabel()
    {
      return null;
    }

    public int getFieldIndex()
    {
      return 0;
    }

    public ITableDefinition getTableDefinition()
    {
      return null;
    }

    public boolean isRequired()
    {
      return true;
    }

    public boolean isReadOnly()
    {
      return false;
    }

    public FieldType getType()
    {
      return this.type;
    }

    public boolean isEnabledForHistory()
    {
      return false;
    }

    public IKey getMatchingForeignKey()
    {
      return null;
    }

    public boolean isPrimary()
    {
      return false;
    }
  };
  
  public void makeSQL(SQLDataSource datasource, ITableAlias alias, ITableField field, SQLStatementBuilder builder, QBEExpression embedded) throws InvalidExpressionException
  {
    if (embedded.isNullExpression())
    {
      // search whether an document field is present or not 
      embedded.makeConstraint(builder, false);
    }
    else
    {
      SQLStatementBuilder builder2 = new SQLStatementBuilder(datasource, false);
      builder2.setTableField(null, DUMMY_JACOB_DOCUMENT_NAME_FIELD);
      embedded.makeConstraint(builder2, false);

      builder.appendDBName(datasource, field.getDBName());
      builder.append(" IN (SELECT id FROM jacob_document WHERE ");
      builder.append(builder2.toString());
      builder.append(")");
    }
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertSqlValueToDataValue(de.tif.jacob.core.data.impl.sql.SQLDataSource,
   *      java.sql.ResultSet, int)
   */
  public Object convertSQLValueToDataValue(FieldType fieldType, SQLDataSource datasource, ResultSet rs, int index) throws SQLException
  {
    String uuidString = rs.getString(index);
    if (rs.wasNull())
      return new JacobDocument(datasource.getName());
    return new JacobDocument(datasource.getName(), new UUID(uuidString));
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#convertObjectToDataValue(java.lang.Object,
   *      java.lang.Object)
   */
  public Object convertObjectToDataValue(Object object, Object oldValue) throws IllegalArgumentException
  {
    if (object == null)
    {
      JacobDocument oldDocument = (JacobDocument) oldValue;
      return new JacobDocument(oldDocument.getDataSourceName(), oldDocument.getKey(), null, null);
    }
    else if (object instanceof DataDocumentValue)
    {
      JacobDocument oldDocument = (JacobDocument) oldValue;
      DataDocumentValue docValue = (DataDocumentValue) object;
      return new JacobDocument(oldDocument.getDataSourceName(), oldDocument.getKey(), docValue.getName(), docValue.getContent());
    }
    throw new IllegalArgumentException(object.getClass().getName());
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#getInitDataValue(de.tif.jacob.core.data.IDataSource,
   *      de.tif.jacob.core.definition.ITableDefinition,
   *      de.tif.jacob.core.definition.ITableField)
   */
  public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, Object defaultValue)
  {
    return new JacobDocument(dataSource.getName());
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValue(de.tif.jacob.core.definition.FieldType,
   *      de.tif.jacob.core.data.impl.sql.SQLExecutionContext,
   *      java.sql.PreparedStatement, int, java.lang.Object)
   */
  public final void setSQLValue(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValueForInsert(de.tif.jacob.core.data.impl.sql.SQLExecutionContext,
   *      java.sql.PreparedStatement, int, java.lang.Object)
   */
  public void setSQLValueForInsert(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue)
      throws SQLException
  {
    JacobDocument document = (JacobDocument) dataValue;

    if (document.isNull())
      statement.setNull(index, java.sql.Types.CHAR);
    else
    {
      document.setKey(new UUID());

      // insert new record in jacob_document first
      insertDocument(context, document.getKey(), document.getName(), document.getContent());

      // and link this record with jacob_document record
      statement.setString(index, document.getKey().toString());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#setSqlValueForUpdate(de.tif.jacob.core.data.impl.sql.SQLExecutionContext,
   *      java.sql.PreparedStatement, int, java.lang.Object)
   */
  public void setSQLValueForUpdate(FieldType fieldType, SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue)
      throws SQLException
  {
    JacobDocument document = (JacobDocument) dataValue;

    if (document.isNull())
    {
      if (!document.isNew())
      {
        deleteDocument(context, document.getKey());
        document.setKey(null);
      }
      statement.setNull(index, java.sql.Types.CHAR);
    }
    else
    {
      if (document.isNew())
      {
        // insert new record in jacob_document first
        UUID key = new UUID();
        document.setKey(key);
        insertDocument(context, key, document.getName(), document.getContent());
      }
      else
      {
        // update record in jacob_document first
        updateDocument(context, document.getKey(), document.getName(), document.getContent());
      }

      // and link this record with jacob_document record
      statement.setString(index, document.getKey().toString());
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#deleteFieldValue(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.lang.Object)
   */
  public void deleteFieldValue(SQLExecutionContext context, Object dataValue) throws SQLException
  {
    if (dataValue instanceof JacobDocument)
    {
      JacobDocument document = (JacobDocument) dataValue;

      if (!document.isNew())
      {
        deleteDocument( context, document.getKey());
      }
    }
  }
  
  private static void insertDocument(SQLExecutionContext context, UUID key, String documentName, byte[] documentContent) throws SQLException
  {
    String tablename = context.getCurrentRecord().getTable().getTableAlias().getTableDefinition().getDBName();
    String columnname = context.getCurrentDBFieldName();
    
    String sqlString = "INSERT INTO jacob_document (id, tablename, columnname, docname, docsize, doccontent) VALUES (?, ?, ?, ?, ?, ?)";

    PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
    try
    {
      int i = 1;
      statement.setString(i++, key.toString());
      statement.setString(i++, tablename);
      statement.setString(i++, columnname);
      statement.setString(i++, documentName);
      statement.setLong(i++, documentContent.length);
      if (documentContent.length == 0)
      {
        statement.setNull(i++, Types.LONGVARBINARY);
      }
      else
      {
        statement.setBinaryStream(i++, new ByteArrayInputStream(documentContent), documentContent.length);
  			// Do not use the following statement since Oracle throws:
  			// ORA-01461: Ein LONG-Wert kann nur zur Einfügung in eine LONG-Spalte gebunden werden
        // This seams to happen only for update statements, nevertheless we do it for insert as well!
        //			statement.setBytes(i++, documentContent);
      }

      // and execute statement
      int result = statement.executeUpdate();
      if (1 != result)
      {
        String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
        throw new de.tif.jacob.core.exception.SQLException(context.getSQLDataSource(), result + " records inserted (expected 1)!", sqlStatement);
      }
    }
    finally
    {
      statement.close();
    }
  }

  private static void updateDocument(SQLExecutionContext context, UUID key, String documentName, byte[] documentContent) throws SQLException
  {
    String sqlString = "UPDATE jacob_document SET docname=?, docsize=?, doccontent=? WHERE id=?";

    PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
    try
    {
      int i = 1;
      statement.setString(i++, documentName);
      statement.setLong(i++, documentContent.length);
      if (documentContent.length == 0)
      {
        statement.setNull(i++, Types.LONGVARBINARY);
      }
      else
      {
        statement.setBinaryStream(i++, new ByteArrayInputStream(documentContent), documentContent.length);
  			// Do not use the following statement since Oracle throws:
  			// ORA-01461: Ein LONG-Wert kann nur zur Einfügung in eine LONG-Spalte gebunden werden
        // This seams to happen only for update statements, nevertheless we do it for insert as well!
        //			statement.setBytes(i++, documentContent);
      }
      statement.setString(i++, key.toString());

      // and execute statement
      int result = statement.executeUpdate();
      if (1 != result)
      {
        String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
        throw new de.tif.jacob.core.exception.SQLException(context.getSQLDataSource(), result + " records updated (expected 1)!", sqlStatement);
      }
    }
    finally
    {
      statement.close();
    }
  }

  private static void deleteDocument(SQLExecutionContext context, Object key) throws SQLException
  {
    String sqlString = "DELETE FROM jacob_document WHERE id=?";

    PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
    try
    {
      int i = 1;
      statement.setString(i++, key.toString());

      // and execute statement
      statement.executeUpdate();
    }
    finally
    {
      statement.close();
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IFieldTypeAdjustment#deleteFieldValues(de.tif.jacob.core.data.impl.sql.SQLExecutionContext, java.lang.String)
   */
  public void deleteFieldValues(SQLExecutionContext context, String embeddedSelectFragment) throws SQLException
  {
    // Avoid subquery because of performance reasons?
    if (context.getSQLDataSource().avoidSubqueries())
    {
      // Yes, especially for mySQL this is needed
      PreparedStatement statement = context.getConnection().prepareStatement(embeddedSelectFragment);
      try
      {
        ResultSet rs = statement.executeQuery();
        try
        {
          while (rs.next())
          {
            deleteDocument(context, rs.getString(1));
          }
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        statement.close();
      }
    }
    else
    {
      String sqlString = "DELETE FROM jacob_document WHERE id IN (" + embeddedSelectFragment + ")";

      PreparedStatement statement = context.getConnection().prepareStatement(sqlString);
      try
      {
        // and execute statement
        statement.executeUpdate();
      }
      finally
      {
        statement.close();
      }
    }
  }
  
  public int getSQLDecimalDigits(FieldType fieldType, ISQLDataSource dataSource)
  {
    // irrelevant
    return 0;
  }

  public final String getSQLDefaultValue(FieldType fieldType, ISQLDataSource dataSource)
  {
    // no default supported for document fields
    return null;
  }

  public int getSQLSize(FieldType fieldType, ISQLDataSource dataSource)
  {
    return UUID.STRING_LENGTH;
  }

  public int getSQLType(FieldType fieldType, ISQLDataSource dataSource)
  {
    return Types.CHAR;
  }

}
