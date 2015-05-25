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

package de.tif.jacob.core.data.impl.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.Property;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.DataRecordSet;
import de.tif.jacob.core.data.impl.qbe.QBERelationConstraint;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.schema.Schema;
import de.tif.jacob.core.data.impl.schema.Table;
import de.tif.jacob.core.data.impl.schema.TableColumn;
import de.tif.jacob.core.data.impl.sql.reconfigure.AddColumnCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.CreateRelationCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.CreateTableCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.DropColumnCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.DropIndexCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.DropPrimaryKeyCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.DropRelationCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.ModifyColumnCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.data.impl.sql.reconfigure.RenewPrimaryKeyCommand;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.schema.ISchemaColumnDefinition;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;
import de.tif.jacob.core.schema.ISchemaRelationDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;

/**
 * MySQL datasource implementation.
 * 
 * @author Andreas Sonntag
 */
public final class MySQLDataSource extends SQLDataSource
{
  static public transient final String RCS_ID = "$Id: MySQLDataSource.java,v 1.8 2010-01-20 02:00:20 sonntag Exp $";
	static public transient final String RCS_REV = "$Revision: 1.8 $";
  
  private static final Version V4_1 = new Version(4, 1);
  
  private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    
  private static final String ESCAPE = "`";
  
  private String schemaPrefix;

  private Version versionNumber;
  
  /**
   * @param connectString
   * @param driverClassName
   * @param userName
   * @param password
   * @throws Exception
   */
  public MySQLDataSource(String connectString, String driverClassName, String userName, String password) throws Exception
  {
    super(null, connectString, driverClassName, userName, password);
  }

  /**
   * @param parent
   */
  public MySQLDataSource(AutoDetectSQLDataSource parent)
  {
    super(parent);
  }

  /**
   * @param record
   * @throws Exception
   */
  public MySQLDataSource(IDataTableRecord record) throws Exception
  {
    super(record);
  }

  /**
   * @param dataSourceName
   * @throws Exception
   */
  public MySQLDataSource(String dataSourceName) throws Exception
  {
    super(dataSourceName);
  }
  
  private synchronized Version getVersionNumber()
  {
    if (this.versionNumber == null)
    {
      try
      {
        Connection connection = getConnection();
        try
        {
          int major = connection.getMetaData().getDatabaseMajorVersion();
          int minor = connection.getMetaData().getDatabaseMinorVersion();
          this.versionNumber = new Version(major, minor);
        }
        finally
        {
          connection.close();
        }
      }
      catch (SQLException ex)
      {
        throw new RuntimeException(ex);
      }
    }
    return this.versionNumber;
  }

  /**
   * MySQL has a slightly different escape syntax, i.e. <code>\</code> has to
   * be escaped as well!
   * 
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#convertToSQL(java.lang.String, boolean)
   */
  public String convertToSQL(String input, boolean doQuoting)
  {
    if (input == null || input.length() == 0)
      return SQL.NULL;

    if (input.indexOf('\\') == -1)
    {
      return super.convertToSQL(input, doQuoting);
    }
    
    StringBuffer erg = new StringBuffer(input.length() + 16);
    for (int i = 0; i < input.length(); i++)
    {
      char c = input.charAt(i);
      if (c == '\\')
      {
        erg.append("\\\\");
      }
      else
      {
        erg.append(c);
      }
    }
    return super.convertToSQL(erg.toString(), doQuoting);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#changePassword(java.lang.String, java.lang.String)
   */
  public void changePassword(String username, String newPassword) throws SQLException, InvalidNewPasswordException
  {
    throw new UnsupportedOperationException("Change password not supported!");
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getRoles(java.lang.String)
   */
  public List getRoles(String username) throws SQLException
  {
    throw new UnsupportedOperationException("Getting user roles not supported!");
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getLowerFunctionName()
   */
  public String getLowerFunctionName()
  {
		return "LOWER";
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#needsMappingOfLongText()
   */
  public boolean needsMappingOfLongText()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#avoidSubqueries()
   */
  public boolean avoidSubqueries()
  {
    // avoid subqueries because of performance reasons:
    // mysql> select *  FROM jacob_document WHERE id IN (SELECT `xml_document` FROM `bkm`.`schweissz_instance_export` WHERE (`schweissz_instance4Mapping_key`=4503));
    // Empty set (39.52 sec)
    // see also: http://www.xaprb.com/blog/2006/04/30/how-to-optimize-subqueries-and-joins-in-mysql/

    return true;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#supportsForeignKeyConstraints()
   */
  public boolean supportsForeignKeyConstraints()
  {
    // Since we enforce InnoDB storage type, foreign key constraints are supported since version 3.23.44
    // Nevertheless these need underlying indices which are automatically created since 4.1.2
    return true;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#escapeDBName(java.lang.StringBuffer, java.lang.String)
   */
  public StringBuffer appendDBName(StringBuffer buffer, String dbName)
  {
    return buffer.append(ESCAPE).append(dbName).append(ESCAPE);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendDBComment(java.lang.StringBuffer, java.lang.String)
   */
  public StringBuffer appendDBComment(StringBuffer buffer, String comment)
  {
    return buffer.append("/* ").append(comment).append(" */");
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#needsMappingOfBinary()
   */
  public boolean needsMappingOfBinary()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#toQueryString(java.sql.Timestamp)
   */
  public String toQueryString(Timestamp timestamp)
  {
    synchronized(timestampFormat)
    {
    	return SQL.QUOTE + timestampFormat.format(timestamp) + SQL.QUOTE;
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#toQueryString(java.sql.Date)
   */
  public String toQueryString(Date date)
  {
    synchronized(dateFormat)
    {
    	return SQL.QUOTE + dateFormat.format(date) + SQL.QUOTE;
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#toQueryString(java.sql.Time)
   */
  public String toQueryString(Time time)
  {
    synchronized(timeFormat)
    {
      return SQL.QUOTE + timeFormat.format(time) + SQL.QUOTE;
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#supportsStoredProcedures()
   */
  public boolean supportsStoredProcedures()
  {
    return false;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#supportsIdGeneration()
   */
  public boolean supportsAutoKeyGeneration()
  {
    return true;
  }
  
  public boolean supportsLongTextSearch(int longtextSqlType, boolean casesensitive)
  {
    // Funktioniert zumindest mit 5.0.18
    return true;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#buildStoredProcedureStatement(java.sql.Connection, java.lang.String, int)
   */
  public CallableStatement buildStoredProcedureStatement(Connection connection, String procedureName, int argnum) throws SQLException
  {
    throw new UnsupportedOperationException("Stored procedures are not supported");
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSchemaPrefix()
   */
  public synchronized String getSchemaPrefix()
  {
    if (this.schemaPrefix == null)
    {
      this.schemaPrefix = ESCAPE + getReconfigureImpl().getMetaCatalogName() + ESCAPE + ".";
    }
    return this.schemaPrefix;
  }

  protected void beforeExecuteQuery(DataRecordSet recordSet, QBESpecification spec, SQLStatementBuilder queryStatement, DataTableRecordEventHandler eventHandler) throws SQLException
  {
    int maxRecords = recordSet.getMaxRecords();
    if (maxRecords == DataRecordSet.DEFAULT_MAX_RECORDS)
      maxRecords = Property.BROWSER_SYSTEM_MAX_RECORDS.getIntValue();
    
    // do not set count limit if filtering is activated!
    if (maxRecords != DataRecordSet.UNLIMITED_RECORDS && !(eventHandler != null && eventHandler.isFilterSearchAction()))
      // set limit to one record more because we it is checked whether there are
      // more than maxRecords
      queryStatement.append(" LIMIT ").append(maxRecords + 1);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendJoinCondition(de.tif.jacob.core.data.impl.sql.SQLStatementBuilder, de.tif.jacob.core.data.impl.qbe.QBERelationConstraint, de.tif.jacob.core.definition.ITableField, de.tif.jacob.core.definition.ITableField)
   */
  public void appendJoinCondition(SQLStatementBuilder sqlQuery, QBERelationConstraint constraint, ITableField fromField, ITableField toField)
  {
		// should never be called, because we use ANSI joins here
		throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#useAnsiJoins()
   */
  public boolean useAnsiJoins()
  {
    return true;
  }

  public boolean isClientAbortErrorCode(int errorCode)
  {
    //Error: 1078 SQLSTATE: HY000 (ER_GOT_SIGNAL) ?????
    //Message: %s: Got signal %d. Aborting!

    // TODO Auto-generated method stub
    return false;
  }

  public boolean isTableNotExistsErrorCode(int errorCode)
  {
    // Table 'xxx' doesn't exist[SQLState:42S02][ErrorCode:1146]
    return errorCode == 1146;
  }
  
  public boolean isUniqueViolationErrorCode(int errorCode)
  {
    return errorCode == ER_DUP_ENTRY;
  }
  
  public boolean isForeignConstraintViolationErrorCode(int errorCode)
  {
    return errorCode == ER_1451 || errorCode == ER_1217;
  }

  /**
   * Message: Duplicate entry '%s' for key %d
   */
  private static final int ER_DUP_ENTRY = 1062;

  /**
   * Message: Cannot delete or update a parent row: a foreign key constraint
   * fails (`test/user`, CONSTRAINT `FK2_USER_COMPANY_FKEY` FOREIGN KEY
   * (`company_key`) REFERENCES `company`
   * (`pkey`))[SQLState:23000][ErrorCode:1451]
   * <p>
   * Note: mySQL 5.x
   */
  private static final int ER_1451 = 1451;

  /**
   * Message: Datasource 'bkmDataSource': Cannot delete or update a parent row:
   * a foreign key constraint
   * fails[SQLState:23000][ErrorCode:1217][Statement:DELETE FROM `bkm`.`company`
   * WHERE `pkey`=102]
   * <p>
   * Note: mySQL 4.1
   */
  private static final int ER_1217 = 1217;

  /**
   * Message: Access denied for user '%s'@'%s' to database '%s'
   */
  private static final int ER_DBACCESS_DENIED_ERROR = 1044;
  
  /**
   * Message: Access denied for user '%s'@'%s' (using password: %s)
   */
  private static final int ER_ACCESS_DENIED_ERROR = 1045;
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isInvalidUseridPasswordErrorCode(int)
   */
  public boolean isInvalidUseridPasswordErrorCode(int errorCode)
  {
    return errorCode == ER_DBACCESS_DENIED_ERROR || errorCode == ER_ACCESS_DENIED_ERROR;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSQLStatementSeparator()
   */
  public String getSQLStatementSeparator()
  {
    return ";\n\n";
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSqlColumnType(int, int, int)
   */
  public String getSqlColumnType(int sqlType, int size, int decimalDigits)
  {
    switch (sqlType)
    {
      case Types.BIT:
        return "BIT(" + size + ")";
        
      case Types.BOOLEAN:
        return getVersionNumber().compareTo(V4_1) >= 0 ? "BOOLEAN" : "TINYINT(1)";
        
      case Types.INTEGER:
        return "INT";

      case Types.BIGINT:
        return "BIGINT";

      case Types.NUMERIC:
      case Types.DECIMAL:
        // IBIS: check size
        return "DECIMAL(18," + decimalDigits + ")";

      case Types.CHAR:
        return "CHAR(" + size + ")";

      case Types.VARCHAR:
        return "VARCHAR(" + size + ")";

      case Types.LONGVARCHAR:
        return "LONGTEXT";

      case Types.REAL:
      case Types.FLOAT:
        return "FLOAT";
      
      case Types.DOUBLE:
        return "DOUBLE";

      case Types.LONGVARBINARY:
        return "LONGBLOB";

      case Types.TIME:
        return "TIME";
      
      case Types.DATE:
        return "DATE";
      
      case Types.TIMESTAMP:
        // do not use TIMESTAMP because: before MySQL 4.1.6, you cannot store a
        // literal NULL in a TIMESTAMP column; setting it to
        // NULL sets it to the current date and time. Because TIMESTAMP columns
        // behave this way, the
        // NULL and NOT NULL attributes do not apply in the normal way and are
        // ignored if you specify
        // them. DESCRIBE tbl_name always reports that a TIMESTAMP column can be
        // assigned
        // NULL values.
        return "DATETIME";
      
      // the following types are just listed to avoid exception
      case Types.TINYINT:
        return "TINYINT";
      case Types.SMALLINT:
      	return "SMALLINT";
      case Types.BINARY:
        return "BINARY";
      case Types.VARBINARY:
        return "VARBINARY";
    }

    throw new RuntimeException("Unsupported SQL type: " + sqlType);
  }

  public int getDefaultBinarySQLType()
  {
    return Types.LONGVARBINARY;
  }

  public int getDefaultLongTextSQLType()
  {
    return Types.LONGVARCHAR;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getReconfigureImpl()
   */
  public Reconfigure getReconfigureImpl()
  {
    return new MySQLReconfigure(this, getVersionNumber());
  }

  /**
   * MySQL reconfigure implementation.
   * 
   * @author Andreas Sonntag
   */
  private static class MySQLReconfigure extends Reconfigure
  {
    private final Version dbVersion;
    /**
     * @param dataSource
     */
    protected MySQLReconfigure(SQLDataSource dataSource, Version dbVersion)
    {
      super(dataSource);
      this.dbVersion = dbVersion;
    }

    /**
     * This checks whether storage engine InnoDB for transaction support is
     * present and activated. If not, an exception will be thrown, because jACOB
     * does not support databases without transaction support!
     */
    private void checkInnoDB(Connection connection) throws Exception
    {
      String innodbStatus = "Unknown";
      PreparedStatement statement = connection.prepareStatement("show variables like \"have_innodb\"");

      try
      {
        ResultSet rs = statement.executeQuery();
        try
        {
          while (rs.next())
          {
            if ("have_innodb".equals(rs.getString("Variable_name")))
            {
              // check whether InnoDB is supported
              innodbStatus = rs.getString("Value");
              if ("YES".equalsIgnoreCase(innodbStatus))
              {
                // everything allright -> just return
                return;
              }
            }
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
      
      throw new UserException("Storage engine InnoDB must be used for transaction support (have_innodb=" + innodbStatus + ")");
    }

    private void checkAutoIncrement(Connection connection, Schema schema) throws Exception
    {
      Iterator iter = schema.getTables();
      while (iter.hasNext())
      {
        Table table = (Table) iter.next();
        
        Statement statement = connection.createStatement();
        try
        {
          ResultSet rs = statement.executeQuery("show columns from " + ESCAPE + table.getDBName() + ESCAPE + " from " + ESCAPE + connection.getCatalog()
              + ESCAPE);
          try
          {
            while (rs.next())
            {
              String columnName = rs.getString("Field");
              String extra = rs.getString("Extra");
              if (extra != null && extra.indexOf("auto_increment") != -1)
              {
                // remember auto_increment
                table.getTableColumn(columnName).setAutoGeneratedKey(true);
              }
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
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#fetchSchemaPreProcessing(java.sql.Connection, de.tif.jacob.core.data.impl.schema.Schema)
     */
    protected void fetchSchemaPreProcessing(Connection connection, Schema schema) throws Exception
    {
      // Check that InnoDB storage engine is supported
      checkInnoDB(connection);

      super.fetchSchemaPreProcessing(connection, schema);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#fetchSchemaPostProcessing(java.sql.Connection, de.tif.jacob.core.data.impl.schema.Schema)
     */
    protected void fetchSchemaPostProcessing(Connection connection, Schema schema) throws Exception
    {
      super.fetchSchemaPostProcessing(connection, schema);
      
      // fetch auto_increment info
      checkAutoIncrement(connection, schema);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#fetchTableColumn(java.lang.String,
     *      java.lang.String, int, java.lang.String, int, int, java.lang.String,
     *      boolean)
     */
    protected TableColumn fetchTableColumn(String table_name, String column_name, int data_type, String type_name, int column_size, int decimal_digits,
        String column_default, boolean is_nullable)
    {
      // MySQL JDBC driver does not map LONGTEXT to Types.LONGVARCHAR!
      //
      if (data_type == Types.OTHER && type_name.equalsIgnoreCase("LONGTEXT"))
      {
        data_type = Types.LONGVARCHAR;
      }
      
      return super.fetchTableColumn(table_name, column_name, data_type, type_name, column_size, decimal_digits, column_default, is_nullable);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#dropIndex(de.tif.jacob.core.schema.ISchemaKeyDefinition, de.tif.jacob.core.schema.ISchemaTableDefinition, de.tif.jacob.core.schema.ISchemaTableDefinition)
     */
    protected boolean dropIndex(ISchemaKeyDefinition currentIndex, ISchemaTableDefinition currentTableDefinition, ISchemaTableDefinition desiredTableDefinition)
    {
      // MySQL creates implicit indices for foreign keys.
      // Therefore, ignore these indices, if there still 
      // exists a matching relation.
      //
      Iterator iter = desiredTableDefinition.getSchemaRelationDefinitions();
      while (iter.hasNext())
      {
        ISchemaRelationDefinition relation = (ISchemaRelationDefinition) iter.next();
        
        if (matches(currentIndex, relation))
        {
          // matching relation found -> ignore this index
          return false;
        }
      }
      return true;
    }
    
    private static boolean matches(ISchemaKeyDefinition keyDefinition, ISchemaRelationDefinition relationDefinition)
    {
      Iterator iter1 = keyDefinition.getSchemaColumnNames();
      Iterator iter2 = relationDefinition.getSchemaForeignColumnNames();
      while (iter1.hasNext() && iter2.hasNext())
      {
        String columnName1 = (String) iter1.next();
        String columnName2 = (String) iter2.next();
        if (!columnName1.equalsIgnoreCase(columnName2))
          return false; 
      }
      
      if (iter1.hasNext() || iter2.hasNext())
        return false;
      
      return true;
    }
    
    /**
     * Checks whether the given default value is something like: <br>
     * 0, 0.0, 0.000, 0000-00-00 00:00:00, 00:00:00, etc.
     * 
     * @param dbDefaultValue
     *          the default value
     * @return <code>true</code> if the given default value should be treated
     *         as NULL
     */
    private boolean isNumericalNullDefault(String dbDefaultValue)
    {
      if (dbDefaultValue != null)
      {
        for (int i = 0; i < dbDefaultValue.length(); i++)
        {
          char ch = dbDefaultValue.charAt(i);

          // if we detect one digit character unequal 0 than the default value
          // is not numerical NULL
          if ('0' != ch && Character.isDigit(ch))
            return false;
        }
      }
      return true;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#modifyColumnDefault(de.tif.jacob.core.schema.ISchemaColumnDefinition, de.tif.jacob.core.schema.ISchemaColumnDefinition)
     */
    protected boolean modifyColumnDefault(ISchemaColumnDefinition currentColumn, ISchemaColumnDefinition desiredColumn)
    {
      // MySQL sets implicit default values for required numerical columns
      //
      if (desiredColumn.isRequired() && null == desiredColumn.getDBDefaultValue(this.dataSource))
      {
        switch (desiredColumn.getSQLType(this.dataSource))
        {
          // only list numerical types which are used by jacob field types
          case Types.SMALLINT:
          case Types.INTEGER:
          case Types.BIGINT:
          case Types.DECIMAL:
          case Types.FLOAT:
          case Types.DOUBLE:
          case Types.TIME:
          case Types.DATE:
          case Types.TIMESTAMP:
            if (isNumericalNullDefault(currentColumn.getDBDefaultValue(this.dataSource)))
            {
              return false;
            }
            break;
        }
      }

      return super.modifyColumnDefault(currentColumn, desiredColumn);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#modifyColumnType(de.tif.jacob.core.schema.ISchemaColumnDefinition, de.tif.jacob.core.schema.ISchemaColumnDefinition)
     */
    protected boolean modifyColumnType(ISchemaColumnDefinition currentColumn, ISchemaColumnDefinition desiredColumn)
    {
      int desiredSQLSize = desiredColumn.getSQLSize(dataSource);
      int currentSQLSize = currentColumn.getSQLSize(dataSource);
      
      int desiredSQLType = desiredColumn.getSQLType(dataSource); 
      int currentSQLType = currentColumn.getSQLType(dataSource);
      
      // Handle implicit type conversions of MySQL from BOOLEAN to TINYINT(1) or BIT(1)
      //
      if (desiredSQLType == Types.BOOLEAN && (currentSQLType == Types.TINYINT || currentSQLType == Types.BIT) && 1 == currentSQLSize)
      {
        return false;
      }
      
      // Handle implicit type conversions of MySQL from VARCHAR to CHAR and vice versa
      //
      if (desiredSQLType != currentSQLType && desiredSQLSize == currentSQLSize)
      {
        switch (currentSQLType)
        {
          case Types.VARCHAR:
            if (desiredSQLType == Types.CHAR)
              return false;
            break;
            
          case Types.CHAR:
            if (desiredSQLType == Types.VARCHAR)
              return false;
            break;
        }
      }
      
      // Handle implicit type conversion of MySQL for CHAR/VARCHAR to TEXT
      //
      if (desiredSQLSize > 255)
      {
        switch (desiredSQLType)
        {
          case Types.VARCHAR:
          case Types.CHAR:
            if (currentSQLType == Types.LONGVARCHAR)
              return false;
            break;
        }
      }
      
      return super.modifyColumnType(currentColumn, desiredColumn);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newCreateTableCommand(de.tif.jacob.core.schema.ISchemaTableDefinition)
     */
    protected CreateTableCommand newCreateTableCommand(ISchemaTableDefinition tableDefinition)
    {
      return new MySQLCreateTableCommand(tableDefinition);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newModifyColumnCommand(de.tif.jacob.core.schema.ISchemaColumnDefinition, de.tif.jacob.core.schema.ISchemaColumnDefinition, boolean, boolean, boolean)
     */
    protected ModifyColumnCommand newModifyColumnCommand(ISchemaColumnDefinition desiredColumn, ISchemaColumnDefinition currentColumn, boolean modifyType,
        boolean modifyDefault, boolean modifyRequired, boolean modifyAutoGenerated)
    {
      return new MySQLModifyColumnCommand(desiredColumn, currentColumn, modifyType, modifyDefault, modifyRequired, modifyAutoGenerated);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newDropIndexCommand(de.tif.jacob.core.schema.ISchemaKeyDefinition)
     */
    protected DropIndexCommand newDropIndexCommand(ISchemaKeyDefinition currentIndex)
    {
      return new MySQLDropIndexCommand(currentIndex);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newCreateRelationCommand(de.tif.jacob.core.schema.ISchemaRelationDefinition, de.tif.jacob.core.schema.ISchemaTableDefinition)
     */
    protected CreateRelationCommand newCreateRelationCommand(ISchemaRelationDefinition desiredRelation, ISchemaTableDefinition primaryTable)
    {
      return new MySQLCreateRelationCommand(desiredRelation, primaryTable, this.dbVersion.compareTo(V4_1) < 0);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newDropRelationCommand(de.tif.jacob.core.schema.ISchemaRelationDefinition)
     */
    protected DropRelationCommand newDropRelationCommand(ISchemaRelationDefinition currentRelation)
    {
      return new MySQLDropRelationCommand(currentRelation, this.dbVersion.compareTo(V4_1) < 0);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newDropColumnCommand(de.tif.jacob.core.schema.ISchemaColumnDefinition)
     */
    protected DropColumnCommand newDropColumnCommand(ISchemaColumnDefinition currentColumn)
    {
      return new MySQLDropColumnCommand(currentColumn);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newAddColumnCommand(de.tif.jacob.core.schema.ISchemaColumnDefinition)
     */
    protected AddColumnCommand newAddColumnCommand(ISchemaColumnDefinition desiredColumn)
    {
      return new MySQLAddColumnCommand(desiredColumn);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newDropPrimaryKeyCommand(de.tif.jacob.core.schema.ISchemaKeyDefinition)
     */
    protected DropPrimaryKeyCommand newDropPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition)
    {
      return new MySQLDropPrimaryKeyCommand(currentPrimaryKeyDefinition);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newRenewPrimaryKeyCommand(de.tif.jacob.core.schema.ISchemaKeyDefinition, de.tif.jacob.core.schema.ISchemaKeyDefinition)
     */
    protected RenewPrimaryKeyCommand newRenewPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition,
        ISchemaKeyDefinition desiredPrimaryKeyDefinition)
    {
      return new MySQLRenewPrimaryKeyCommand(currentPrimaryKeyDefinition, desiredPrimaryKeyDefinition);
    }
  }

  /**
   * MySQL create table implementation.
   * 
   * @author Andreas Sonntag
   */
  private static class MySQLCreateTableCommand extends CreateTableCommand
  {
    /**
     * @param tableDefinition
     */
    private MySQLCreateTableCommand(ISchemaTableDefinition tableDefinition)
    {
      super(tableDefinition);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.CreateTableCommand#appendToSQL92CreateStatement(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    protected String appendToSQL92CreateStatement(SQLDataSource dataSource)
    {
      // enforce transaction safe tables
      return " ENGINE=InnoDB";
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.CreateTableCommand#appendToSQL92CreateColumnStatement(de.tif.jacob.core.data.impl.sql.SQLDataSource,
     *      de.tif.jacob.core.schema.ISchemaColumnDefinition)
     */
    protected String appendToSQL92CreateColumnStatement(SQLDataSource dataSource, ISchemaColumnDefinition column)
    {
      return column.isDBAutoGenerated(dataSource) ? " AUTO_INCREMENT" : "";
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.CreateTableCommand#addResetPrivilegesStatements(java.util.List)
     */
    protected void addResetPrivilegesStatements(SQLDataSource dataSource, List statements)
    {
      // skip Grant and Revoke for MySQL 
    }
  }
  
  /**
   * MySQL modify column implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   * 
   *  ALTER TABLE tbl_name
   * 	  MODIFY COLUMN col_name col_type null_notnull [DEFAULT literal] [AUTO_INCREMENT]
   *  
   * </pre>
   * 
   * @author Andreas Sonntag
   */
  private static class MySQLModifyColumnCommand extends ModifyColumnCommand
  {
    private MySQLModifyColumnCommand(ISchemaColumnDefinition desiredColumn, ISchemaColumnDefinition currentColumn, boolean modifyType, boolean modifyDefault,
        boolean modifyRequired, boolean modifyAutoGenerated)
    {
      super(desiredColumn, currentColumn, modifyType, modifyDefault, modifyRequired, modifyAutoGenerated);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.schema.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List result = new ArrayList();

      StringBuffer buffer = new StringBuffer();
      buffer.append("ALTER TABLE ");
      dataSource.appendDBName(buffer, this.table.getDBName());
      buffer.append(" MODIFY COLUMN ");
      dataSource.appendDBName(buffer, this.desiredColumn.getDBName());

      // everything must always be specified!
      String dataType = dataSource.getSqlColumnType(this.desiredColumn.getSQLType(dataSource), this.desiredColumn.getSQLSize(dataSource), this.desiredColumn
          .getSQLDecimalDigits(dataSource));
      buffer.append(" ").append(dataType);
      buffer.append(this.desiredColumn.isRequired() ? " NOT NULL" : " NULL");
      if (this.desiredColumn.getDBDefaultValue(dataSource) != null)
      {
        buffer.append(" DEFAULT ");
        buffer.append(escapeDefaultValue(dataSource, this.desiredColumn.getSQLType(dataSource), this.desiredColumn.getDBDefaultValue(dataSource)));
      }
      
      if (this.desiredColumn.isDBAutoGenerated(dataSource))
      {
        buffer.append(" AUTO_INCREMENT");
      }

      result.add(buffer.toString());

      // add inherited statements
      result.addAll(super.getSQLStatements(dataSource));

      return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.schema.Command#getAlterTableModifyFragment(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public String getAlterTableModifyFragment(SQLDataSource dataSource)
    {
      return null;
    }
  }

  /**
   * MySQL drop index implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   * 
   *  DROP INDEX index_name ON tbl_name
   *  
   * </pre>
   * 
   * @author Andreas Sonntag
   */
  private static class MySQLDropIndexCommand extends DropIndexCommand
  {
    /**
     * @param currentIndex
     */
    protected MySQLDropIndexCommand(ISchemaKeyDefinition currentIndex)
    {
      super(currentIndex);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List statements = new ArrayList();

      statements.add(makeSQL(dataSource, this.currentIndex.getDBName(), this.table.getDBName()));

      return statements;
    }
    
    protected static String makeSQL(SQLDataSource dataSource, String indexName, String tableName) 
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append("DROP INDEX ");
      dataSource.appendDBName(buffer, indexName);
      buffer.append(" ON ");
      dataSource.appendDBName(buffer, tableName);
      return buffer.toString();
    }
  }
  
  /**
   * MySQL create relation implementation.
   * <p>
   * The following syntax is used (an index will be implicitly created):
   * 
   * <pre>
   *  ALTER TABLE `bemerkung` ADD CONSTRAINT `FK2_BEMERKUNG_FIRMA_FKEY` FOREIGN KEY (`firma_key`) REFERENCES `firma` (`pkey`)
   * </pre>
   * 
   * but before mySQL 4.1 (an index is necessary):
   * 
   * <pre>
   *  ALTER TABLE `person` ADD INDEX `FK2_PERSON_FIRMA_FKEY` (`firma_key`)
   *  ALTER TABLE `person` ADD CONSTRAINT `FK2_PERSON_FIRMA_FKEY` FOREIGN KEY (`firma_key`) REFERENCES `firma` (`pkey`)
   * </pre>
   * 
   * 
   * @author Andreas Sonntag
   */
  private static class MySQLCreateRelationCommand extends CreateRelationCommand
  {
    private final boolean needsExplicitIndex;
    
    /**
     * @param desiredRelation
     * @param primaryTable
     * @param needsExplicitIndex
     */
    protected MySQLCreateRelationCommand(ISchemaRelationDefinition desiredRelation, ISchemaTableDefinition primaryTable, boolean needsExplicitIndex)
    {
      super(desiredRelation, primaryTable);
      this.needsExplicitIndex = needsExplicitIndex;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.CreateRelationCommand#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List statements = super.getSQLStatements(dataSource);

      if (this.needsExplicitIndex)
      {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ALTER TABLE ");
        dataSource.appendDBName(buffer, this.desiredRelation.getSchemaTableName());
        buffer.append(" ADD INDEX ");
        dataSource.appendDBName(buffer, this.desiredRelation.getSchemaForeignKeyName());
        buffer.append(" (");
        Iterator foreignColumnNameIter = this.desiredRelation.getSchemaForeignColumnNames();
        for (int j = 0; foreignColumnNameIter.hasNext(); j++)
        {
          if (j != 0)
            buffer.append(",");
          dataSource.appendDBName(buffer, (String) foreignColumnNameIter.next());
        }
        buffer.append(")");

        // add create index statement as first
        statements.add(0, buffer.toString());
      }

      return statements;
    }
  }
  
  /**
   * MySQL drop relation implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   * 
   *  ALTER TABLE `organization` DROP FOREIGN KEY `fk41_organization_ownerorg`
   *  
   * </pre>
   * 
   * @author Andreas Sonntag
   */
  private static class MySQLDropRelationCommand extends DropRelationCommand
  {
    private final boolean needsExplicitIndex;
    
    /**
     * @param currentRelation
     */
    protected MySQLDropRelationCommand(ISchemaRelationDefinition currentRelation, boolean needsExplicitIndex)
    {
      super(currentRelation);
      this.needsExplicitIndex = needsExplicitIndex;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.schema.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List statements = new ArrayList();

      StringBuffer buffer = new StringBuffer();
      buffer.append("ALTER TABLE ");
      dataSource.appendDBName(buffer, this.currentRelation.getSchemaTableName());
      buffer.append(" DROP FOREIGN KEY ");
      dataSource.appendDBName(buffer, this.currentRelation.getSchemaForeignKeyName());
      statements.add(buffer.toString());

      if (this.needsExplicitIndex)
      {
        // assume there is an index with the same name than the key
        statements.add(MySQLDropIndexCommand.makeSQL(dataSource, this.currentRelation.getSchemaForeignKeyName(), this.currentRelation.getSchemaTableName()));
      }

      return statements;
    }
  }
  
  /**
   * MySQL drop column implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   * 
   *   ALTER TABLE table_name DROP COLUMN column_name
   *  
   * </pre>
   * 
   * @author Andreas Sonntag
   */
  private static class MySQLDropColumnCommand extends DropColumnCommand
  {
    /**
     * @param currentColumn
     */
    protected MySQLDropColumnCommand(ISchemaColumnDefinition currentColumn)
    {
      super(currentColumn);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.AlterTableSubCommand#getAlterTableDropFragment(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public String getAlterTableDropFragment(SQLDataSource dataSource)
    {
      return null;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List result = new ArrayList();

      // drop column
      StringBuffer buffer = new StringBuffer();
      buffer.append("ALTER TABLE ").append(ESCAPE).append(this.table.getDBName()).append(ESCAPE).append(" DROP COLUMN ");
      buffer.append(ESCAPE).append(this.currentColumn.getDBName()).append(ESCAPE);
      result.add(buffer.toString());

      // add inherited statements
      result.addAll(super.getSQLStatements(dataSource));

      return result;
    }
  }
  
  /**
   * MySQL add column implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   *  ALTER TABLE tbl_name
   * 	  ADD COLUMN col_name col_type [NOT NULL] [DEFAULT literal] [AUTO_INCREMENT]
   * </pre>
   * 
   * @author Andreas Sonntag
   */
  private static class MySQLAddColumnCommand extends AddColumnCommand
  {
    /**
     * @param desiredColumn
     */
    protected MySQLAddColumnCommand(ISchemaColumnDefinition desiredColumn)
    {
      super(desiredColumn);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.AlterTableSubCommand#getAlterTableAddFragment(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public String getAlterTableAddFragment(SQLDataSource dataSource)
    {
      return null;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List result = new ArrayList();

      StringBuffer buffer = new StringBuffer();
      buffer.append("ALTER TABLE ").append(ESCAPE).append(this.table.getDBName()).append(ESCAPE);

      buffer.append(" ADD COLUMN ").append(ESCAPE).append(this.desiredColumn.getDBName()).append(ESCAPE);

      String dataType = dataSource.getSqlColumnType(this.desiredColumn.getSQLType(dataSource), this.desiredColumn.getSQLSize(dataSource), this.desiredColumn
          .getSQLDecimalDigits(dataSource));
      buffer.append(" ").append(dataType);

      // NULL is default
      if (this.desiredColumn.isRequired())
        buffer.append(" NOT NULL");

      if (this.desiredColumn.getDBDefaultValue(dataSource) != null)
      {
        buffer.append(" DEFAULT ");
        buffer.append(escapeDefaultValue(dataSource, this.desiredColumn.getSQLType(dataSource), this.desiredColumn.getDBDefaultValue(dataSource)));
      }

      if (this.desiredColumn.isDBAutoGenerated(dataSource))
        buffer.append(" AUTO_INCREMENT");

      result.add(buffer.toString());

      // add inherited statements
      result.addAll(super.getSQLStatements(dataSource));

      return result;
    }
  }
  
  /**
   * MySQL renew primary key implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   *       ALTER TABLE tbl_name
   *      	  [DROP PRIMARY KEY,]
   *          ADD CONSTRAINT key_name PRIMARY KEY(column [, column])
   * </pre>
   * 
   * @author Andreas Sonntag
   */
  private static class MySQLRenewPrimaryKeyCommand extends RenewPrimaryKeyCommand
  {
    /**
     * @param currentPrimaryKeyDefinition
     * @param desiredPrimaryKeyDefinition
     */
    protected MySQLRenewPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition, ISchemaKeyDefinition desiredPrimaryKeyDefinition)
    {
      super(currentPrimaryKeyDefinition, desiredPrimaryKeyDefinition);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List result = new ArrayList();

      StringBuffer buffer = new StringBuffer();
      buffer.append("ALTER TABLE ");
      dataSource.appendDBName(buffer, this.table.getDBName());

      if (this.currentPrimaryKeyDefinition != null)
        buffer.append(" DROP PRIMARY KEY,");

      buffer.append(" ADD CONSTRAINT ");
      dataSource.appendDBName(buffer, this.desiredPrimaryKeyDefinition.getDBName());
      buffer.append(" PRIMARY KEY (");
      Iterator columnNameIter = this.desiredPrimaryKeyDefinition.getSchemaColumnNames();
      for (int i = 0; columnNameIter.hasNext(); i++)
      {
        if (i != 0)
          buffer.append(",");
        dataSource.appendDBName(buffer, (String) columnNameIter.next());
      }
      buffer.append(")");

      result.add(buffer.toString());

      return result;
    }
  }
  
  /**
   * MySQL drop primary key implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   * 
   *  ALTER TABLE tbl_name DROP PRIMARY KEY
   *  
   * </pre>
   * 
   * @author Andreas Sonntag
   */
  private static class MySQLDropPrimaryKeyCommand extends DropPrimaryKeyCommand
  {
    /**
     * @param currentPrimaryKeyDefinition
     */
    protected MySQLDropPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition)
    {
      super(currentPrimaryKeyDefinition);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List result = new ArrayList();

      StringBuffer buffer = new StringBuffer();
      buffer.append("ALTER TABLE ");
      dataSource.appendDBName(buffer, this.table.getDBName());
      buffer.append(" DROP PRIMARY KEY");
      result.add(buffer.toString());

      return result;
    }
  }
}
