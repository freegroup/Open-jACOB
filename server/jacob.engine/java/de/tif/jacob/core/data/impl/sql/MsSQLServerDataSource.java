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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.qbe.QBERelationConstraint;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.schema.Schema;
import de.tif.jacob.core.data.impl.schema.Table;
import de.tif.jacob.core.data.impl.schema.TableColumn;
import de.tif.jacob.core.data.impl.sql.reconfigure.DropColumnCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.DropIndexCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.DropPrimaryKeyCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.ModifyColumnCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.data.impl.sql.reconfigure.RenewPrimaryKeyCommand;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.schema.ISchemaColumnDefinition;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;

/**
 * MS SQL Server datasource implementation.
 * 
 * @author Andreas
 */
public final class MsSQLServerDataSource extends SQLDataSource
{
  static public transient final String RCS_ID = "$Id: MsSQLServerDataSource.java,v 1.5 2009-07-30 17:34:19 sonntag Exp $";
  static public transient final String RCS_REV = "$Revision: 1.5 $";

  static private final transient Log logger = LogFactory.getLog(MsSQLServerDataSource.class);

	/**
	 * SQL Server 2000 keywords
	 */
	private static final String[] KEYWORDS = {
      "ADD", "ALL", "ALTER", "AND", "ANY", "AS", "ASC", "AUTHORIZATION", //
      "BACKUP", "BEGIN", "BETWEEN", "BREAK", "BROWSE", "BULK", "BY", //
      "CASCADE", "CASE", "CHECK", "CHECKPOINT", "CLOSE", "CLUSTERED", "COALESCE", "COLLATE", "COLUMN", "COMMIT", "COMPUTE", "CONSTRAINT", "CONTAINS",
      "CONTAINSTABLE", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CURRENT", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", //
      "DATABASE", "DBCC", "DEALLOCATE", "DECLARE", "DEFAULT", "DELETE", "DENY", "DESC", "DISK", "DISTINCT", "DISTRIBUTED", "DOUBLE", "DROP", "DUMMY", "DUMP", //
      "ELSE", "END", "ERRLVL", "ESCAPE", "EXCEPT", "EXEC", "EXECUTE", "EXISTS", "EXIT", //
      "FETCH", "FILE", "FILLFACTOR", "FOR", "FOREIGN", "FREETEXT", "FREETEXTTABLE", "FROM", "FULL", "FUNCTION", //
      "GOTO", "GRANT", "GROUP", //
      "HAVING", "HOLDLOCK", //
      "IDENTITY", "IDENTITY_INSERT", "IDENTITYCOL", "IF", "IN", "INDEX", "INNER", "INSERT", "INTERSECT", "INTO", "IS", //
      "JOIN", //
      "KEY", //
      "KILL", //
      "LEFT", "LIKE", "LINENO", "LOAD", //
      "NATIONAL ", "NOCHECK", "NONCLUSTERED", "NOT", "NULL", "NULLIF", //
      "OF", "OFF", "OFFSETS", "ON", "OPEN", "OPENDATASOURCE", "OPENQUERY", "OPENROWSET", "OPENXML", "OPTION", "OR", "ORDER", "OUTER", "OVER", //
      "PERCENT", "PLAN", "PRECISION", "PRIMARY", "PRINT", "PROC", "PROCEDURE", "PUBLIC", //
      "RAISERROR", "READ", "READTEXT", "RECONFIGURE", "REFERENCES", "REPLICATION", "RESTORE", "RESTRICT", "RETURN", "REVOKE", "RIGHT", "ROLLBACK", "ROWCOUNT",
      "ROWGUIDCOL", "RULE", //
      "SAVE", "SCHEMA", "SELECT", "SESSION_USER", "SET", "SETUSER", "SHUTDOWN", "SOME", "STATISTICS", "SYSTEM_USER", //
      "TABLE", "TEXTSIZE", "THEN", "TO", "TOP", "TRAN", "TRANSACTION", "TRIGGER", "TRUNCATE", "TSEQUAL", //
      "UNION", "UNIQUE", "UPDATE", "UPDATETEXT", "USE", "USER", //
      "VALUES", "VARYING", "VIEW", //
      "WAITFOR", "WHEN", "WHERE", "WHILE", "WITH", "WRITETEXT" };
  
  private static final String[] SQL_SERVER_2005_META_SCHEMAS = {
    "sys", "INFORMATION_SCHEMA"
  };
	
	/**
	 * Set containing all keywords and access is done by ignoring case.
	 */
  private static final Set keywords = new TreeSet(String.CASE_INSENSITIVE_ORDER);
  
  private static final Set metaschemasToIgnore = new HashSet();
  
  private static final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
  private static final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final DateFormat timeFormat = new SimpleDateFormat("1970-01-01 HH:mm:ss");

  // There is already an object named 'xxx' in the database.
  private static final int ERR_OBJECT_ALREADY_EXISTS = 2714;

  private String schemaPrefix;

  private Integer majorVersionNumber;

  static
  {
    // fill set with keywords
    //
    for (int i = 0; i < KEYWORDS.length; i++)
    {
      keywords.add(KEYWORDS[i]);
    }
    
    // fill set with meta schema names
    //
    for (int i = 0; i < SQL_SERVER_2005_META_SCHEMAS.length; i++)
    {
      metaschemasToIgnore.add(SQL_SERVER_2005_META_SCHEMAS[i]);
    }
  }
  
  /**
   * For testing purposes only
   * 
   * @param connectString
   * @param driverClassName
   * @param userName
   * @param password
   * @throws Exception
   */
  /*
  private MsSQLServerDataSource(String connectString, String userName, String password) throws Exception
  {
    super(null, connectString, "com.microsoft.jdbc.sqlserver.SQLServerDriver", userName, password);
  }
*/
  /**
   * @param parent
   */
  public MsSQLServerDataSource(AutoDetectSQLDataSource parent)
  {
    super(parent);
  }

  /**
   * @param record
   * @throws Exception
   */
  public MsSQLServerDataSource(IDataTableRecord record) throws Exception
  {
    super(record);
  }

  /**
   * @param dataSourceName
   */
  public MsSQLServerDataSource(String dataSourceName) throws Exception
  {
    super(dataSourceName);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#changePassword(java.lang.String, java.lang.String)
   */
  public void changePassword(String username, String newPassword) throws SQLException, InvalidNewPasswordException
  {
    Connection con = getConnection();
    try
    {
      PreparedStatement stmt = con.prepareStatement("EXEC sp_password NULL, ?, ?");
      try
      {
        stmt.setString(1, newPassword);
        stmt.setString(2, username);
        stmt.execute();
      }
      finally
      {
        stmt.close();
      }
    }
    finally
    {
      con.close();
    }
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getRoles(java.lang.String)
   */
  public List getRoles(String username) throws SQLException
  {
    List roleNames = new ArrayList();

    String statement = "SELECT role.name FROM sysusers AS role "
        + "INNER JOIN sysmembers AS member ON role.uid = member.groupuid "
        + "INNER JOIN sysusers AS usr ON usr.uid = member.memberuid "
        + "WHERE usr.name='" + username + "'";

    Connection conn = getConnection();
    try
    {
      Statement stmt = conn.createStatement();
      try
      {
        ResultSet rset = stmt.executeQuery(statement);
        try
        {
          while (rset.next())
          {
            String roleName = rset.getString(1);
            roleNames.add(roleName);
          }
        }
        finally
        {
          rset.close();
        }
      }
      finally
      {
        stmt.close();
      }
    }
    finally
    {
      conn.close();
    }
    return roleNames;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.sql.SQLDataSource#getLowerFunctionName()
   */
  public String getLowerFunctionName()
  {
    return "LOWER";
  }

  public boolean supportsLowerFunctionForLongText()
  {
    // Kein Lower für ntext/text!
    return false;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#escapeDBName(java.lang.StringBuffer, java.lang.String)
   */
  public StringBuffer appendDBName(StringBuffer buffer, String dbName)
  {
    // the given name is a SQL server keyword?
    if (keywords.contains(dbName))
    {
      return buffer.append("[").append(dbName).append("]");
    }
    return buffer.append(dbName);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendDBComment(java.lang.StringBuffer, java.lang.String)
   */
  public StringBuffer appendDBComment(StringBuffer buffer, String comment)
  {
    return buffer.append("/* ").append(comment).append(" */");
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.sql.SQLDataSource#buildStoredProcedureStatement(java.sql.Connection, java.lang.String, int)
   */
  public CallableStatement buildStoredProcedureStatement(Connection connection, String procedureName, int argnum) throws SQLException
  {
    // hack: set to auto commit, because otherwise changes within stored
    // procedure will not be committed
    connection.setAutoCommit(true);

    StringBuffer buffer = new StringBuffer();
    buffer.append("{call ");
    buffer.append(procedureName);
    buffer.append("(");
    for (int i = 0; i < argnum; i++)
    {
      if (i != 0)
        buffer.append(", ");
      buffer.append("?");
    }
    buffer.append(")}");
    String callstr = buffer.toString();
    if (logger.isDebugEnabled())
      logger.debug(callstr);
    return connection.prepareCall(callstr);
  }

  /**
   * Builds count distinct part as follows:
   * <p>
   * count(DISTINCT expr)
   * <p>
   * Note: An expression list is not supported!
   * 
   * @param spec
   * @param sqlBuffer
   * @return <code>true</code> count distinct supported, otherwise
   *         <code>false</code>
   */
  protected boolean buildCountDistinct(QBESpecification spec, SQLStatementBuilder sqlBuffer)
  {
    IKey primaryKey = spec.getAliasToSearch().getTableDefinition().getPrimaryKey();
    if (null == primaryKey)
      return false;
    
    List keyFields = primaryKey.getTableFields();
    if (keyFields.size() != 1)
      return false;
    
    sqlBuffer.append("count(DISTINCT ");
    ITableField field = (ITableField) keyFields.get(0);
    if (spec.isMultiTableQuery())
    {
      sqlBuffer.appendDBName(this, spec.getAliasToSearch().getName());
      sqlBuffer.append(".");
    }
    sqlBuffer.appendDBName(this, field.getDBName());
    sqlBuffer.append(")");
    return true;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendJoinCondition(de.tif.jacob.core.data.impl.sql.SQLStatementBuilder,
   *      de.tif.jacob.core.data.impl.qbe.QBERelationConstraint,
   *      de.tif.jacob.core.definition.ITableField,
   *      de.tif.jacob.core.definition.ITableField)
   */
  public void appendJoinCondition(SQLStatementBuilder sqlQuery, QBERelationConstraint constraint, ITableField fromField, ITableField toField)
  {
    // Proprietary outer join syntax (ANSI joins are supported since MS SQL Server 2000)
    // Note: The * of the operators *= or =* indicates the required table of the outer join
    //
    sqlQuery.appendDBName(this, constraint.getToTableAlias().getName());
    sqlQuery.append(".");
    sqlQuery.appendDBName(this, toField.getDBName());
    if (!constraint.isInnerJoin() && !constraint.isForwardFlag())
    {
      // perform outer join
      sqlQuery.append("*");
    }
    sqlQuery.append("=");
    
    if (!constraint.isInnerJoin() && constraint.isForwardFlag())
    {
      // perform outer join
      sqlQuery.append("*");
    }
    sqlQuery.appendDBName(this, constraint.getFromTableAlias().getName());
    sqlQuery.append(".");
    sqlQuery.appendDBName(this, fromField.getDBName());
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer, java.sql.Date)
   */
  public String toQueryString(Date date)
  {
    synchronized (dateFormat)
    {
      return "CONVERT(datetime," + SQL.QUOTE + dateFormat.format(date) + SQL.QUOTE + ",102)";
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer, java.sql.Time)
   */
  public String toQueryString(Time time)
  {
    synchronized (timeFormat)
    {
      // Note: date will be set to 1.1.1970
      return "CONVERT(datetime," + SQL.QUOTE + timeFormat.format(time) + SQL.QUOTE + ",120)";
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer, java.sql.Timestamp)
   */
  public String toQueryString(Timestamp timestamp)
  {
    synchronized (timestampFormat)
    {
      return "CONVERT(datetime," + SQL.QUOTE + timestampFormat.format(timestamp) + SQL.QUOTE + ",120)";
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.sql.SQLDataSource#needsMappingOfLongText()
   */
  public boolean needsMappingOfLongText()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#needsMappingOfBinary()
   */
  public boolean needsMappingOfBinary()
  {
    return false;
  }

  public boolean useAnsiJoins()
  {
    // ANSI Joins are supported by MS SQL Server 2000 and above
    //
    // Note: The MS SQL Server "table1.field1*=table2.field2" outer join syntax,
    // is not 100% equivalent to ANSI joins because of different optional constraint
    // handling!
    return isVersion2000OrAbove();
  }

  public boolean isUniqueViolationErrorCode(int errorCode)
  {
    // 2601: Cannot insert duplicate key row in object 'xxx' with unique index 'xxx'.
    // 2627: Verletzung der UNIQUE KEY-Einschrï¿½nkung 'xxx'. Ein doppelter Schlï¿½ssel kann in das engine-Objekt nicht eingefï¿½gt werden.
    return errorCode == 2601 || errorCode == 2627;
  }

  public boolean isForeignConstraintViolationErrorCode(int errorCode)
  {
    // 547: DELETE statement conflicted with COLUMN REFERENCE constraint 'FK2_USER_COMPANY_FKEY'. The conflict occurred in database 'bkm', table 'user', column 'company_key'.
    return errorCode == 547;
  }

  private void createStoredProcedure(Connection connection, String procedure) throws SQLException
  {
    Statement statement = connection.createStatement();
    try
    {
      statement.execute(procedure);
    }
    catch (SQLException ex)
    {
      // procedure already exists?
      if (ex.getErrorCode() != ERR_OBJECT_ALREADY_EXISTS)
      {
        // a real problem -> rethrow
        throw ex;
      }
    }
    finally
    {
      statement.close();
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#supportsMultipleNullsForUniqueIndices()
   */
  public boolean supportsMultipleNullsForUniqueIndices()
  {
    // what a pity but not supported :-(
    return false;
  }

  public boolean supportsLongTextSearch(int longtextSqlType, boolean casesensitive)
  {
    // MS SQL Server unterstützt keine lower Funktion und sucht immer caseinsensitive
    // Siehe:
    // Der Argumentdatentyp text ist für das 1-Argument der lower-Funktion
    // ungültig.[SQLState:S0001][ErrorCode:8116][Statement:SELECT project.pkey,
    // project.name, project.status, project_leader.longname, project.location
    // FROM mssqltest..project project INNER JOIN mssqltest..[user]
    // project_leader ON project_leader.pkey=project.responsible_key WHERE
    // (LOWER(project.description) LIKE '%test%') ORDER BY project.name ASC]

    return !casesensitive;
  }

  protected boolean supportsSelectDistinct(ITableDefinition table)
  {
    List tableFields = table.getTableFields();
    for (int i = 0; i < tableFields.size(); i++)
    {
      ITableField tablefield = (ITableField) tableFields.get(i);
      switch (tablefield.getType().getSQLType(this))
      {
        case Types.LONGVARCHAR:
        case Types.CLOB:
        case Types.BINARY:
        case Types.LONGVARBINARY:
        case Types.BLOB:
          return false;
      }
    }
    return true;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#supportsIdGeneration()
   */
  public boolean supportsAutoKeyGeneration()
  {
    // IBIS: Geht im Moment nicht, da der JDBC Treiber getGeneratedKeys() nicht unterstï¿½tzt!
    return false;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#setupAsJacobDatasource(de.tif.jacob.core.schema.ISchemaDefinition)
   */
  protected void setupAsJacobDatasource(ISchemaDefinition currentSchema) throws Exception
  {
    // do default staff
    super.setupAsJacobDatasource(currentSchema);

    // and create stored procedures
    Connection connection = this.getConnection();
    try
    {
      if (currentSchema.hasProcedure("jacob_next_id"))
      {
        // alter stored procedure to fetch new numeric ids
        String procFile = isVersion2000OrAbove() ? "mssql_alter_jacob_next_id.sql" : "mssql7_alter_jacob_next_id.sql";
        createStoredProcedure(connection, getResourceContent(procFile));
      }
      else
      {
        // create stored procedure to fetch new numeric ids
        String procFile = isVersion2000OrAbove() ? "mssql_create_jacob_next_id.sql" : "mssql7_create_jacob_next_id.sql";
        createStoredProcedure(connection, getResourceContent(procFile));
      }
      
      // we must commit here
      connection.commit();
    }
    finally
    {
      connection.close();
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isInvalidUseridPasswordErrorCode(int)
   */
  public boolean isInvalidUseridPasswordErrorCode(int errorCode)
  {
    // Login failed for user 'xxx'
    return 18456 == errorCode || 18457 == errorCode;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isClientAbortErrorCode(int)
   */
  public boolean isClientAbortErrorCode(int errorCode)
  {
    // MS SQL server seams not to provide the ability of client side abort operations
    return false;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isTableNotExistsErrorCode(int)
   */
  public boolean isTableNotExistsErrorCode(int errorCode)
  {
    // Invalid object name 'xxx'.[SQLState:42S02][ErrorCode:208]
    return 208 == errorCode;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSchemaPrefix()
   */
  public synchronized String getSchemaPrefix()
  {
    if (this.schemaPrefix == null)
    {
      StringBuffer buffer = new StringBuffer();
      this.appendDBName(buffer, getReconfigureImpl().getMetaCatalogName());
      // Full qualified tablename in MSSQL is databasename.owner.tablename
      // Note: owner could be omitted and by means of getCatalog() we get the databasename
      //
      buffer.append("..");
      this.schemaPrefix = buffer.toString();
    }
    return this.schemaPrefix;
  }

  private boolean isVersion2000OrAbove()
  {
    int major = getMajorVersionNumber();

    // assume MS SQL Server 2000 or higher, if major version number could not be extracted 
    return major == -1 || major >= 8;
  }

  private synchronized int getMajorVersionNumber()
  {
    if (this.majorVersionNumber == null)
    {
      try
      {
        Connection connection = getConnection();
        try
        {
          String version = connection.getMetaData().getDatabaseProductVersion();
          this.majorVersionNumber = new Integer(extractMajorVersionNumber(version));
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
    return this.majorVersionNumber.intValue();
  }

  private static int extractMajorVersionNumber(String version)
  {
    if (logger.isInfoEnabled())
      logger.info("Extracting major version from: " + version);
    
    int c = version.indexOf('.');
    if (c != -1)
    {
      String majorVersion = version.substring(0, version.indexOf('.'));
      int startIndex = majorVersion.length();
      for (int i = majorVersion.length() - 1; i >= 0 && Character.isDigit(majorVersion.charAt(i)); i--)
      {
        if ('0' != majorVersion.charAt(i))
        {
          startIndex = i;
        }
      }
      if (startIndex < majorVersion.length())
      {
        try
        {
          return Integer.parseInt(majorVersion.substring(startIndex));
        }
        catch (Exception ex)
        {
          // ignore
        }
      }
    }

    // unknown version
    return -1;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSqlColumnType(int, int, int)
   */
  public String getSqlColumnType(int sqlType, int size, int decimalDigits)
  {
    switch (sqlType)
    {
      case Types.BOOLEAN:
      case Types.BIT:
        return "BIT";
        
      case Types.SMALLINT:
      case Types.INTEGER:
        return "INT";

      case Types.BIGINT:
        if (!isVersion2000OrAbove())
        {
          // BIGINT has been introduced by MS SQL Server 2000!
          return "INT";
        }
        return "BIGINT";

      // NUMERIC and DECIMAL seem to be equivalant!
      case Types.NUMERIC:
      case Types.DECIMAL:
        // IBIS: check size
        return "DECIMAL(18," + decimalDigits + ")";

      case Types.CHAR:
        return "CHAR(" + size + ")";

      case Types.VARCHAR:
        return "VARCHAR(" + size + ")";

      case Types.LONGVARCHAR:
      // special driver for MS SQL Server 7 delivers CLOB
      case Types.CLOB:
        return "TEXT";

      case Types.REAL:
        // in MSSQL REAL is 4-Byte
        return "REAL";

      case Types.FLOAT:
      case Types.DOUBLE:
        // in MSSQL FLOAT is 8-Byte
        return "FLOAT";

      case Types.BINARY:
      case Types.VARBINARY:
      case Types.LONGVARBINARY:
      // special driver for MS SQL Server 7 delivers BLOB
      case Types.BLOB:
        return "IMAGE";

      case Types.TIME:
      case Types.DATE:
      case Types.TIMESTAMP:
        return "DATETIME";
      
      // the following types are just listed to avoid exception
      case Types.TINYINT:
        return "TINYINT";      
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
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSQLStatementSeparator()
   */
  public String getSQLStatementSeparator()
  {
    return "\r\nGO\r\n\r\n";
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#needsPostpone()
   */
  protected boolean needsPostpone()
  {
    // MS SQL Server does not support to have multiple active prepared statements for
    // one connection at once -> postpone needed.
    // See: http://support.microsoft.com/default.aspx?scid=kb%3Ben-us%3B313181
    return true;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getReconfigureImpl()
   */
  public Reconfigure getReconfigureImpl()
  {
    return new MsSQLReconfigure(this);
  }

  /**
   * @author Andreas
   *
   * The MS SQL Server specific reconfigure behaviour
   */
  private static class MsSQLReconfigure extends Reconfigure
  {
    /**
     * @param dataSource
     */
    private MsSQLReconfigure(SQLDataSource dataSource)
    {
      super(dataSource);
    }

    protected TableColumn fetchTableColumn(ResultSet rs) throws Exception
    {
      String table_schem = rs.getString("TABLE_SCHEM");
      if (metaschemasToIgnore.contains(table_schem))
        return null;
      
      return super.fetchTableColumn(rs);
    }

    protected TableColumn fetchTableColumn(String table_name, String column_name, int data_type, String type_name, int column_size, int decimal_digits,
        String column_default, boolean is_nullable)
    {
      // MS SQL Server does escape default values as follows:
      // (1) or ('test default')
      // or even ((0)) for MS SQL Server 2005
      //
      if (column_default != null)
      {
        column_default = stripBrackets(column_default);
        column_default = stripBrackets(column_default);

        if (column_default.length() > 2 && column_default.charAt(0) == '\'' && column_default.charAt(column_default.length() - 1) == '\'')
        {
          column_default = column_default.substring(1, column_default.length() - 1);
        }
      }

      return new TableColumn(table_name, column_name, data_type, column_size, decimal_digits, column_default, is_nullable);
    }
    
    private static String stripBrackets(String column_default)
    {
      if (column_default.length() > 2 && column_default.charAt(0) == '(' && column_default.charAt(column_default.length() - 1) == ')')
      {
        column_default = column_default.substring(1, column_default.length() - 1);
      }
      return column_default;
    }

    protected Table fetchTable(ResultSet rs) throws Exception
    {
      String table_schem = rs.getString("TABLE_SCHEM");
      if (metaschemasToIgnore.contains(table_schem))
        return null;
      
      Table table = super.fetchTable(rs);

      if (Table.USER_TABLE == table.getType() && "dtproperties".equalsIgnoreCase(table.getDBName()))
      {
        // mark table as system table
        table.setType(Table.SYSTEM_TABLE);
      }

      return table;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newModifyColumnCommand(de.tif.jacob.core.schema.ISchemaColumnDefinition, de.tif.jacob.core.schema.ISchemaColumnDefinition, boolean, boolean, boolean)
     */
    protected ModifyColumnCommand newModifyColumnCommand(ISchemaColumnDefinition desiredColumn, ISchemaColumnDefinition currentColumn, boolean modifyType, boolean modifyDefault, boolean modifyRequired, boolean modifyAutoGenerated)
    {
      return new MsSQLModifyColumnCommand(desiredColumn, currentColumn, modifyType, modifyDefault, modifyRequired, modifyAutoGenerated);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newDropColumnCommand(de.tif.jacob.core.schema.ISchemaColumnDefinition)
     */
    protected DropColumnCommand newDropColumnCommand(ISchemaColumnDefinition currentColumn)
    {
      return new MsSQLDropColumnCommand(currentColumn);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newDropIndexCommand(de.tif.jacob.core.schema.ISchemaKeyDefinition)
     */
    protected DropIndexCommand newDropIndexCommand(ISchemaKeyDefinition currentIndex)
    {
      return new MsSQLDropIndexCommand(currentIndex);
    }
    
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newRenewPrimaryKeyCommand(de.tif.jacob.core.schema.ISchemaKeyDefinition, de.tif.jacob.core.schema.ISchemaKeyDefinition)
     */
    protected RenewPrimaryKeyCommand newRenewPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition,
        ISchemaKeyDefinition desiredPrimaryKeyDefinition)
    {
      return new MsSQLRenewPrimaryKeyCommand(currentPrimaryKeyDefinition, desiredPrimaryKeyDefinition);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newDropPrimaryKeyCommand(de.tif.jacob.core.schema.ISchemaKeyDefinition)
     */
    protected DropPrimaryKeyCommand newDropPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition)
    {
      return new MsSQLDropPrimaryKeyCommand(currentPrimaryKeyDefinition);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#fetchSchemaPostProcessing(java.sql.Connection, de.tif.jacob.core.data.impl.schema.Schema)
     */
    protected void fetchSchemaPostProcessing(Connection connection, Schema schema) throws Exception
    {
      super.fetchSchemaPostProcessing(connection, schema);
      
      //
      // collect all default constraint names
      Statement statement = connection.createStatement();
      try
      {
        String defaultConstraintsQuery = "SELECT DISTINCT so.name, tab.name, scl.name FROM sysobjects so " + // 
            "JOIN sysconstraints scn ON so.id = scn.constid " + // join constraint object
            "JOIN syscolumns scl ON scn.colid = scl.colid AND scn.id = scl.id " + // join column
            "JOIN sysobjects tab ON scn.id = tab.id " + // join table object
            "WHERE so.xtype='D'"; // filter for default constraints 

        ResultSet rs = statement.executeQuery(defaultConstraintsQuery);
        try
        {
          while (rs.next())
          {
            String constraintName = rs.getString(1);
            String tableName = rs.getString(2);
            String columnName = rs.getString(3);

            // remember default constraint name
            schema.getTable(tableName).getTableColumn(columnName).putProperty(DEFAULT_CONSTRAINT_PROPERTY, constraintName);
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

  private final static String DEFAULT_CONSTRAINT_PROPERTY = "DEFAULT_CONSTRAINT_PROPERTY";

  private static class MsSQLModifyColumnCommand extends ModifyColumnCommand
  {
    private MsSQLModifyColumnCommand(ISchemaColumnDefinition desiredColumn, ISchemaColumnDefinition currentColumn, boolean modifyType, boolean modifyDefault, boolean modifyRequired, boolean modifyAutoGenerated)
    {
      super(desiredColumn, currentColumn, modifyType, modifyDefault, modifyRequired, modifyAutoGenerated);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.schema.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List result = new ArrayList();

      if (this.modifyType || this.modifyRequired)
      {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ALTER TABLE ");
        dataSource.appendDBName(buffer, this.table.getDBName());
        buffer.append(" ALTER COLUMN ");
        dataSource.appendDBName(buffer, this.desiredColumn.getDBName());

        // type must always be specified!
        String dataType = dataSource.getSqlColumnType(this.desiredColumn.getSQLType(dataSource), this.desiredColumn.getSQLSize(dataSource), this.desiredColumn.getSQLDecimalDigits(dataSource));
        buffer.append(" ").append(dataType);

        // NULL/not NULL must always be specified?
//        if (this.modifyRequired)
        {
          buffer.append(this.desiredColumn.isRequired() ? " NOT NULL" : " NULL");
        }

        result.add(buffer.toString());
      }

      if (this.modifyDefault)
      {
        // check whether a default value has already been set
        if (this.currentColumn.getDBDefaultValue(dataSource) != null)
        {
          // default constraint must first be dropped before any modifications could be performed
          String defaultConstraintName = (String) ((TableColumn) this.currentColumn).getProperty(DEFAULT_CONSTRAINT_PROPERTY);

          StringBuffer buffer = new StringBuffer();
          buffer.append("ALTER TABLE ");
          dataSource.appendDBName(buffer, this.table.getDBName());
          buffer.append(" DROP CONSTRAINT ");
          buffer.append(defaultConstraintName);
          result.add(buffer.toString());
        }

        if (this.desiredColumn.getDBDefaultValue(dataSource) != null)
        {
          StringBuffer buffer = new StringBuffer();
          buffer.append("ALTER TABLE ");
          dataSource.appendDBName(buffer, this.table.getDBName());
          buffer.append(" ADD DEFAULT ");
          buffer.append(escapeDefaultValue(dataSource, this.desiredColumn.getSQLType(dataSource), this.desiredColumn.getDBDefaultValue(dataSource)));
          buffer.append(" FOR ");
          dataSource.appendDBName(buffer, this.desiredColumn.getDBName());
          result.add(buffer.toString());
        }
      }
      
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

  private static class MsSQLDropColumnCommand extends DropColumnCommand
  {
    /**
     * @param currentColumn
     */
    private MsSQLDropColumnCommand(ISchemaColumnDefinition currentColumn)
    {
      super(currentColumn);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List result = new ArrayList();

      // check whether a default value has already been set
      if (this.currentColumn.getDBDefaultValue(dataSource) != null)
      {
        // default constraint must first be dropped before the column itself could be dropped
        String defaultConstraintName = (String) ((TableColumn) this.currentColumn).getProperty(DEFAULT_CONSTRAINT_PROPERTY);

        StringBuffer buffer = new StringBuffer();
        buffer.append("ALTER TABLE ");
        dataSource.appendDBName(buffer, this.table.getDBName());
        buffer.append(" DROP CONSTRAINT ");
        buffer.append(defaultConstraintName);
        result.add(buffer.toString());
      }

      // and drop column itself
      StringBuffer buffer = new StringBuffer();
      buffer.append("ALTER TABLE ");
      dataSource.appendDBName(buffer, this.table.getDBName());
      buffer.append(" DROP COLUMN ");
      dataSource.appendDBName(buffer, this.currentColumn.getDBName());
      result.add(buffer.toString());
      
      // add inherited statements
      result.addAll(super.getSQLStatements(dataSource));

      return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.AlterTableSubCommand#getAlterTableDropFragment(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public String getAlterTableDropFragment(SQLDataSource dataSource)
    {
      return null;
    }
  }
  
  /**
   * MS SQL Server drop index implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   * 
   *  ALTER TABLE tbl_name DROP CONSTRAINT key_name
   *  or
   *  DROP INDEX tbl_name.index_name
   *  
   * </pre>
   * 
   * @author Andreas Sonntag
   */
  private static class MsSQLDropIndexCommand extends DropIndexCommand
  {
    /**
     * @param currentIndex
     */
    protected MsSQLDropIndexCommand(ISchemaKeyDefinition currentIndex)
    {
      super(currentIndex);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List statements = new ArrayList();

      StringBuffer buffer = new StringBuffer();
      if (this.currentIndex.isUnique())
      {
        buffer.append("ALTER TABLE ");
        dataSource.appendDBName(buffer, this.table.getDBName());
        buffer.append(" DROP CONSTRAINT ");
        dataSource.appendDBName(buffer, this.currentIndex.getDBName());
      }
      else
      {
        buffer.append("DROP INDEX ");
        dataSource.appendDBName(buffer, this.table.getDBName());
        buffer.append(".");
        dataSource.appendDBName(buffer, this.currentIndex.getDBName());
      }
      statements.add(buffer.toString());
      
      return statements;
    }
  }
  

  /**
   * MS SQL Server renew primary key implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   *       [ALTER TABLE tbl_name DROP CONSTRAINT key_name]
   *        ALTER TABLE tbl_name ADD CONSTRAINT key_name PRIMARY KEY(column [, column])
   * </pre>
   * 
   * @author Andreas Sonntag
   */
  private static class MsSQLRenewPrimaryKeyCommand extends RenewPrimaryKeyCommand
  {
    /**
     * @param currentPrimaryKeyDefinition
     * @param desiredPrimaryKeyDefinition
     */
    protected MsSQLRenewPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition, ISchemaKeyDefinition desiredPrimaryKeyDefinition)
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

      if (this.currentPrimaryKeyDefinition != null)
      {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ALTER TABLE ");
        dataSource.appendDBName(buffer, this.table.getDBName());
        buffer.append(" DROP CONSTRAINT ");
        dataSource.appendDBName(buffer, this.currentPrimaryKeyDefinition.getDBName());
        
        result.add(buffer.toString());
      }

      StringBuffer buffer = new StringBuffer();
      buffer.append("ALTER TABLE ");
      dataSource.appendDBName(buffer, this.table.getDBName());
      buffer.append(" ADD CONSTRAINT ");
      dataSource.appendDBName(buffer, this.desiredPrimaryKeyDefinition.getDBName());
      buffer.append(" PRIMARY KEY(");
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
   * MS SQL Server drop primary key implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   * 
   *   ALTER TABLE tbl_name DROP CONSTRAINT key_name
   *  
   * </pre>
   * 
   * @author Andreas Sonntag
   */
  private static class MsSQLDropPrimaryKeyCommand extends DropPrimaryKeyCommand
  {
    /**
     * @param currentPrimaryKeyDefinition
     */
    protected MsSQLDropPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition)
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
      buffer.append(" DROP CONSTRAINT ");
      dataSource.appendDBName(buffer, this.currentPrimaryKeyDefinition.getDBName());
      result.add(buffer.toString());

      return result;
    }
  }
}
