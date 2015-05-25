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

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.lang.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.postgresql.util.PSQLException;

import de.tif.jacob.core.Property;
import de.tif.jacob.core.adjustment.IDataSourceAdjustment;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.DataExecutionContext;
import de.tif.jacob.core.data.impl.DataIncOrDecValue;
import de.tif.jacob.core.data.impl.DataRecord;
import de.tif.jacob.core.data.impl.DataRecordSet;
import de.tif.jacob.core.data.impl.DataSearchResult;
import de.tif.jacob.core.data.impl.IDataSearchResult;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.DataTableRecord;
import de.tif.jacob.core.data.impl.DataTransaction;
import de.tif.jacob.core.data.impl.IDataAutoKeyValue;
import de.tif.jacob.core.data.impl.IDataSearchIterateCallback;
import de.tif.jacob.core.data.impl.misc.InvalidFieldExpressionException;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEField;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBERelationConstraint;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.qbe.QBEUserConstraint;
import de.tif.jacob.core.data.impl.sql.reconfigure.CommandList;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordAction;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordsAction;
import de.tif.jacob.core.data.impl.ta.TAInsertRecordAction;
import de.tif.jacob.core.data.impl.ta.TAUpdateRecordAction;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableAliasCondition;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.fieldtypes.BinaryFieldType;
import de.tif.jacob.core.definition.fieldtypes.DocumentFieldType;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;
import de.tif.jacob.core.definition.impl.JacobInternalBaseDefinition;
import de.tif.jacob.core.definition.impl.JacobInternalDefinition;
import de.tif.jacob.core.exception.ForeignConstraintViolationException;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.exception.InvalidUseridPasswordException;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.core.exception.UniqueViolationException;
import de.tif.jacob.core.exception.UniqueViolationFieldException;
import de.tif.jacob.core.exception.UniqueViolationFieldsException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.util.db.Database;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class SQLDataSource extends DataSource implements ISQLDataSource
{
  static public transient final String RCS_ID = "$Id: SQLDataSource.java,v 1.19 2011/07/01 21:23:19 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.19 $";

  static private final transient Log logger = LogFactory.getLog(SQLDataSource.class);

  /**
   * ANSI SQL 92 reserved words
   */
  protected static final String[] SQL92_KEYWORDS = {
      "ABSOLUTE", "ACTION", "ADD", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "AS", "ASC", "ASSERTION", "AT", "AUTHORIZATION", "AVG", "BEGIN", "BETWEEN", "BIT", "BIT_LENGTH", "BOTH", "BY", "CASCADE", "CASCADED", "CASE", "CAST",
      "CATALOG", "CHAR", "CHARACTER", "CHAR_LENGTH", "CHARACTER_LENGTH", "CHECK", "CLOSE", "COALESCE", "COLLATE", "COLLATION", "COLUMN", "COMMIT", "CONNECT", "CONNECTION", "CONSTRAINT", "CONSTRAINTS", "CONTINUE", "CONVERT",
      "CORRESPONDING", "COUNT", "CREATE", "CROSS", "CURRENT", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DEFERRABLE", "DEFERRED",
      "DELETE", "DESC", "DESCRIBE", "DESCRIPTOR", "DIAGNOSTICS", "DISCONNECT", "DISTINCT", "DOMAIN", "DOUBLE", "DROP", "ELSE", "END", "END-EXEC", "ESCAPE", "EXCEPT", "EXCEPTION", "EXEC", "EXECUTE", "EXISTS", "EXTERNAL", "EXTRACT", "FALSE",
      "FETCH", "FIRST", "FLOAT", "FOR", "FOREIGN", "FOUND", "FROM", "FULL", "GET", "GLOBAL", "GO", "GOTO", "GRANT", "GROUP", "HAVING", "HOUR", "IDENTITY", "IMMEDIATE", "IN", "INDICATOR", "INITIALLY", "INNER", "INPUT", "INSENSITIVE",
      "INSERT", "INT", "INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS", "ISOLATION", "JOIN", "KEY", "LANGUAGE", "LAST", "LEADING", "LEFT", "LEVEL", "LIKE", "LOCAL", "LOWER", "MATCH", "MAX", "MIN", "MINUTE", "MODULE", "MONTH", "NAMES",
      "NATIONAL", "NATURAL", "NCHAR", "NEXT", "NO", "NOT", "NULL", "NULLIF", "NUMERIC", "OCTET_LENGTH", "OF", "ON", "ONLY", "OPEN", "OPTION", "OR", "ORDER", "OUTER", "OUTPUT", "OVERLAPS", "PAD", "PARTIAL", "POSITION", "PRECISION",
      "PREPARE", "PRESERVE", "PRIMARY", "PRIOR", "PRIVILEGES", "PROCEDURE", "PUBLIC", "READ", "REAL", "REFERENCES", "RELATIVE", "RESTRICT", "REVOKE", "RIGHT", "ROLLBACK", "ROWS", "SCHEMA", "SCROLL", "SECOND", "SECTION", "SELECT",
      "SESSION", "SESSION_USER", "SET", "SIZE", "SMALLINT", "SOME", "SPACE", "SQL", "SQLCODE", "SQLERROR", "SQLSTATE", "SUBSTRING", "SUM", "SYSTEM_USER", "TABLE", "TEMPORARY", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR",
      "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSACTION", "TRANSLATE", "TRANSLATION", "TRIM", "TRUE", "UNION", "UNIQUE", "UNKNOWN", "UPDATE", "UPPER", "USAGE", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING", "VIEW", "WHEN",
      "WHENEVER", "WHERE", "WITH", "WORK", "WRITE", "YEAR", "ZONE" };

  static public final SQLDataSource TEST_INSTANCE = new TestDataSource();

  private final AutoDetectSQLDataSource parent;

  private boolean initialized;

  /**
   * Number of internal connections, i.e. number of WrapperConnection
   */
  int internalConnections = 0;

  /**
   * Number of connections, which have been borrowed by the connection pool.
   */
  private int borrowedConnections = 0;

  // SQL members
  //
  private final String connectString;
  private final String userName;
  private final String password;
  private final String driverClassName;
  private Driver driver;

  private final boolean pooling;
  private final String validationQuery;
  private final int maxActiveCons;
  private final int maxIdleCons;
  private final int maxConnectionWait;
  private ObjectPool connectionPool;

  // JNDI members
  //
  private final String jndiName;
  private String jndiConnectionUrl;
  private javax.sql.DataSource jndiDataSource;

  /**
   * Currently used for HSQL to dynamically build absolute database path.
   * 
   * @param connectString
   * @return
   */
  protected String filterConnectString(String connectString)
  {
    return connectString;
  }

  /**
   * Just for test instance!
   */
  private SQLDataSource()
  {
    super((String) null);

    this.initialized = true;
    this.parent = null;
    this.connectString = null;
    this.driverClassName = null;
    this.userName = null;
    this.password = null;
    this.jndiName = null;

    this.pooling = false;
    this.validationQuery = null;
    this.maxActiveCons = 0;
    this.maxIdleCons = 0;
    this.maxConnectionWait = 0;
  }

  /**
   * @param name
   * @param connectString
   * @param driverClassName
   * @param userName
   * @param password
   * @throws Exception
   */
  protected SQLDataSource(String name, String connectString, String driverClassName, String userName, String password) throws Exception
  {
    super(name);

    this.initialized = driverClassName == null;
    this.parent = null;
    this.connectString = filterConnectString(connectString);
    this.driverClassName = driverClassName;
    this.userName = userName;
    this.password = password;
    this.jndiName = null;

    this.pooling = false;
    this.validationQuery = null;
    this.maxActiveCons = 0;
    this.maxIdleCons = 0;
    this.maxConnectionWait = 0;

    if (null == this.connectString)
      throw new RuntimeException("No connect information found - connectString is missing!");
  }

  /**
   * Constructor used by auto detect SQL datasource for embedded datasource
   * instance
   * 
   * @param parent
   */
  protected SQLDataSource(AutoDetectSQLDataSource parent)
  {
    super((String) null);

    this.initialized = true;
    this.parent = parent;
    this.connectString = null;
    this.driverClassName = null;
    this.userName = null;
    this.password = null;
    this.jndiName = null;

    this.pooling = false;
    this.validationQuery = null;
    this.maxActiveCons = 0;
    this.maxIdleCons = 0;
    this.maxConnectionWait = 0;
  }

  /**
   * Constructor used for data sources defined in jACOB configuration database.
   * 
   * @param record
   * @throws Exception
   */
  protected SQLDataSource(IDataTableRecord record) throws Exception
  {
    super(record);

    this.parent = null;

    //
    // Do we have data sources maintained by Webserver?
    if ("Webserver".equals(record.getStringValue("location")))
    {
      // Yes
      this.initialized = false;
      this.jndiName = record.getStringValue("jndiName");
      this.connectString = null;
      this.driverClassName = null;
      this.userName = null;
      this.password = null;

      this.pooling = false;
      this.validationQuery = null;
      this.maxActiveCons = 0;
      this.maxIdleCons = 0;
      this.maxConnectionWait = 0;

      if (null == this.jndiName)
        throw new RuntimeException("No connect information found - JNDI name is missing!");
    }
    else
    {
      // No - connections are maintained by jACOB internally
      this.connectString = filterConnectString(record.getStringValue("connectString"));
      this.driverClassName = record.getStringValue("jdbcDriverClass");
      this.userName = record.getStringValue("userName");
      this.password = record.getStringValue("password");
      this.jndiName = null;
      this.initialized = this.driverClassName == null;

      this.pooling = "Yes".equals(record.getValue("configurePool"));
      this.validationQuery = record.getStringValue("validationQuery");
      this.maxActiveCons = record.getintValue("maxActiveCons");
      this.maxIdleCons = record.getintValue("maxIdleCons");
      this.maxConnectionWait = record.getintValue("maxConnectionWait");

      if (null == this.connectString)
        throw new RuntimeException("No connect information found - connectString is missing!");
    }
  }

  /**
   * Constructor used for predefined data sources, i.e. data sources defined in
   * common.properties.
   * 
   * @param dataSourceName
   * @throws Exception
   */
  protected SQLDataSource(String dataSourceName) throws Exception
  {
    super(dataSourceName);

    this.parent = null;

    this.pooling = false;
    this.validationQuery = null;
    this.maxActiveCons = 0;
    this.maxIdleCons = 0;
    this.maxConnectionWait = 0;

    this.jndiName = conf.getProperty("datasource." + dataSourceName + ".jndiName");

    //
    // Do we have data sources maintained by Webserver?
    if (null != this.jndiName)
    {
      // Yes
      this.initialized = false;
      this.connectString = null;
      this.driverClassName = null;
      this.userName = null;
      this.password = null;
    }
    else
    {
      this.connectString = filterConnectString(conf.getProperty("datasource." + dataSourceName + ".connectString"));
      this.driverClassName = conf.getProperty("datasource." + dataSourceName + ".driverClassName");
      this.userName = null;
      this.password = null;
      this.initialized = this.driverClassName == null;

      if (null == connectString)
        throw new RuntimeException("No connect information found - you have to define either property jndiName or connectString!");
    }
  }

  /**
   * Connection factory to create connections by this datasource.
   * 
   * @author Andreas Sonntag
   */
  private class MyConnectionFactory implements ConnectionFactory
  {
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.dbcp.ConnectionFactory#createConnection()
     */
    public Connection createConnection() throws SQLException
    {
      return new WrapperConnection(SQLDataSource.this, SQLDataSource.this.createConnection());
    }
  }

  /**
   * Poolable connection factory to make MyPoolableConnection.
   * 
   * @author Andreas Sonntag
   */
  private class MyPoolableConnectionFactory extends PoolableConnectionFactory
  {
    private MyPoolableConnectionFactory(ConnectionFactory connFactory, ObjectPool pool, KeyedObjectPoolFactory stmtPoolFactory, String validationQuery, boolean defaultReadOnly, boolean defaultAutoCommit)
    {
      super(connFactory, pool, stmtPoolFactory, validationQuery, defaultReadOnly, defaultAutoCommit);
    }

    public synchronized Object makeObject() throws Exception
    {
      return new MyPoolableConnection(_connFactory.createConnection(), _pool);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.pool.PoolableObjectFactory#activateObject(java.lang.Object)
     */
    public void activateObject(Object arg0) throws Exception
    {
      super.activateObject(arg0);

      synchronized (SQLDataSource.this)
      {
        SQLDataSource.this.borrowedConnections++;
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.pool.PoolableObjectFactory#passivateObject(java.lang.Object)
     */
    public void passivateObject(Object arg0) throws Exception
    {
      synchronized (SQLDataSource.this)
      {
        SQLDataSource.this.borrowedConnections--;
      }

      super.passivateObject(arg0);
    }
  }

  /**
   * Poolable connection which handles close() correctly even if the pool has
   * already been closed (due to a destroyed datasource).
   * 
   * @author Andreas Sonntag
   */
  private class MyPoolableConnection extends PoolableConnection
  {
    private MyPoolableConnection(Connection arg0, ObjectPool arg1)
    {
      super(arg0, arg1);
    }

    public synchronized void close() throws SQLException
    {
      // if the pool is already closed than really close the connection, because
      // otherwise we would get a IllegalStateException("Pool not open")!
      //
      if (SQLDataSource.this.connectionPool == null)
      {
        super.reallyClose();
        return;
      }

      super.close();
    }
  }

  /**
   * Checks whether the datasource is a JNDI datasource.
   * 
   * @return <code>true</code> if JNDI, otherwise <code>false</code>
   */
  protected final boolean isJNDI()
  {
    if (this.parent != null)
      return this.parent.isJNDI();
    return this.jndiName != null;
  }

  /**
   * Initialize connectivity, i.e. do some sort of actions before getting an
   * internal connection the first time.
   * <p>
   * 
   * Note: This should not be done within the constructor.
   * 
   * @throws SQLException
   */
  protected final synchronized void initConnectivity() throws SQLException
  {
    if (this.initialized)
      return;

    if (this.jndiName == null)
    {
      // init connectivity via JDBC
      //

      if (driverClassName != null)
      {
        // instantiate driver
        //
        try
        {
          logger.info("Trying to register driver " + driverClassName + " ..");
          this.driver = (Driver) Class.forName(driverClassName).newInstance();
          DriverManager.registerDriver(driver);
        }
        catch (Exception ex)
        {
          logger.fatal("Driver registration failed!", ex);
          throw new de.tif.jacob.core.exception.SQLException(this, ex, "initialize");
        }
      }
    }
    else
    {
      // init connectivity via JNDI
      //
      try
      {
        this.jndiDataSource = (javax.sql.DataSource) jndiLookup(jndiName);
      }
      catch (Exception ex)
      {
        logger.fatal("Accessing data source '" + jndiName + "' failed", ex);
        throw new de.tif.jacob.core.exception.SQLException(this, ex, "initialize");
      }
    }

    this.initialized = true;
  }

  /**
   * Initialize the internal connection pool.
   * <p>
   * 
   * Note: This should not be done within the constructor.
   * 
   * @throws SQLException
   */
  private synchronized void initPool() throws SQLException
  {
    // already done?
    if (this.connectionPool != null)
      return;

    // create connection pool
    //
    try
    {
      ObjectPool connectionPool = new GenericObjectPool(null, this.maxActiveCons, GenericObjectPool.WHEN_EXHAUSTED_BLOCK, this.maxConnectionWait, this.maxIdleCons);

      ConnectionFactory connectionFactory = new MyConnectionFactory();

      // Now we'll create the PoolableConnectionFactory, which wraps
      // the "real" Connections created by the ConnectionFactory with
      // the classes that implement the pooling functionality.
      //
      new MyPoolableConnectionFactory(connectionFactory, connectionPool, null, this.validationQuery, false, false);

      this.connectionPool = connectionPool;
    }
    catch (Exception ex)
    {
      logger.fatal("Pool creation failed!", ex);
      throw new de.tif.jacob.core.exception.SQLException(this, ex, "initialize");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.IDataSource#destroy()
   */
  public void destroy()
  {
    if (this.jndiDataSource != null)
    {
      // IBIS: Nothing more to do?
      this.jndiDataSource = null;
    }

    if (this.connectionPool != null)
    {
      try
      {
        this.connectionPool.close();
      }
      catch (Exception ex)
      {
        throw new RuntimeException("Closing pool failed", ex);
      }
      this.connectionPool = null;
    }

    super.destroy();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.impl.DataSource#printInfo(java.io.PrintWriter)
   */
  public synchronized final void printInfo(PrintWriter writer)
  {
    super.printInfo(writer);

    writer.print("\tconnections: ");
    writer.print(this.internalConnections);
    if (this.pooling)
    {
      writer.print(" (borrowed: ");
      writer.print(this.borrowedConnections);
      writer.print(")");
    }
    writer.println();
  }

  public String convertToSQL(String input, boolean doQuoting)
  {
    return SQL.convertToSQL(input, doQuoting);
  }

  /**
   * @return
   */
  public abstract String getLowerFunctionName();

  public boolean supportsLowerFunctionForLongText()
  {
    return true;
  }

  /**
   * Determines whether this data source needs mapping of long text values in
   * general. Nevertheless, for some fields mapping could be disabled.
   * 
   * @return <code>true</code> mapping is needed, otherwise <code>false</code>
   * @see #needsMappingOfBinary(FieldType)
   */
  public abstract boolean needsMappingOfLongText();

  /**
   * Determines whether for the given field type mapping of long text values
   * (e.g. values are in jacob_text table) is needed.
   * 
   * @param fieldType
   *          the field type to check mapping
   * @return <code>true</code> mapping is needed, otherwise <code>false</code>
   */
  public final boolean needsMappingOfLongText(FieldType fieldType)
  {
    return needsMappingOfLongText() && fieldType instanceof LongTextFieldType && !((LongTextFieldType) fieldType).isDisableMapping();
  }

  /**
   * Determines whether this data source needs mapping of binary values in
   * general. Nevertheless, for some fields mapping could be disabled.
   * 
   * @return <code>true</code> mapping is needed, otherwise <code>false</code>
   * @see #needsMappingOfBinary(FieldType)
   */
  public abstract boolean needsMappingOfBinary();

  /**
   * Determines whether for the given field type mapping of binary values (e.g.
   * values are in jacob_binary table) is needed.
   * 
   * @param fieldType
   *          the field type to check mapping
   * @return <code>true</code> mapping is needed, otherwise <code>false</code>
   */
  public final boolean needsMappingOfBinary(FieldType fieldType)
  {
    return needsMappingOfBinary() && fieldType instanceof BinaryFieldType && !((BinaryFieldType) fieldType).isDisableMapping();
  }

  public abstract String toQueryString(Timestamp timestamp);

  public abstract String toQueryString(Date date);

  public abstract String toQueryString(Time time);

  /**
   * Checks whether this datasource supports foreign key constraints.
   * 
   * @return <code>true</code> foreign key constraints are supported,
   *         otherwise <code>false</code>
   */
  public boolean supportsForeignKeyConstraints()
  {
    return true;
  }

  /**
   * Checks whether this datasource supports stored procedures.
   * 
   * @return <code>true</code> stored procedures are supported, otherwise
   *         <code>false</code>
   */
  public boolean supportsStoredProcedures()
  {
    return true;
  }
  
  public abstract boolean supportsLongTextSearch(int longtextSqlType, boolean casesensitive);

  public final boolean supportsLongTextSearch(int longtextSqlType)
  {
    // supports at least casesensitive or caseinsensitive search?
    return supportsLongTextSearch(longtextSqlType, false) || supportsLongTextSearch(longtextSqlType, true);
  }

  public final boolean supportsSorting()
  {
    return true;
  }

  public final String adjustAliasName(String aliasName)
  {
    return appendDBName(new StringBuffer(32), aliasName).toString();
  }

  /**
   * Appends the given database object name to a given buffer. If needed the
   * database object name, will be escaped accordingly to avoid conflicts with
   * database keywords.
   * 
   * @param buffer
   *          the buffer to append to
   * @param dbName
   *          database object name
   * @return the given buffer
   */
  public abstract StringBuffer appendDBName(StringBuffer buffer, String dbName);

  /**
   * Appends the given database comment to a given buffer. The comment has to be
   * escaped in such a way that it is not treated as a statement.
   * 
   * @param buffer
   *          the buffer to append to
   * @param comment
   *          the comment
   * @return the given buffer
   */
  public abstract StringBuffer appendDBComment(StringBuffer buffer, String comment);

  /**
   * Builds a stored procedure call.
   * 
   * Remark: The syntax of calling stored procedures is database dependent.
   * 
   * @param connection
   *          the connection
   * @param procedureName
   *          the name of the stored procedure to call
   * @param argnum
   *          the number of arguments of this stored procedure (input and
   *          output)
   * @return a callable statement
   * @throws SQLException
   *           on any error
   * @throws UnsupportedOperationException
   *           if stored procedures are not supported
   * @see #supportsStoredProcedures()
   */
  public abstract CallableStatement buildStoredProcedureStatement(Connection connection, String procedureName, int argnum) throws SQLException, UnsupportedOperationException;

  protected final void checkDestroyed() throws SQLException
  {
    if (isDestroyed())
      throw new SQLException("Datasource has already been destroyed");
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.IDataSource#getAdjustment()
   */
  public final IDataSourceAdjustment getAdjustment()
  {
    if (this.parent != null)
      return this.parent.getAdjustment();
    return super.getAdjustment();
  }

  /**
   * @return
   * @throws SQLException
   */
  public final Connection getConnection() throws SQLException
  {
    // by default: switch off postpone functionality
    return getConnection(false);
  }

  public final Connection getConnection(boolean activatedPostpone) throws SQLException
  {
    Connection connection;
    WrapperConnection internalConnection;

    if (this.pooling)
    {
      checkDestroyed();

      // init pool, if not already done
      initPool();

      try
      {
        // we know that the connection returned is a PoolableConnection which
        // contains an WrapperConnection
        //
        connection = (Connection) this.connectionPool.borrowObject();
        internalConnection = (WrapperConnection) ((PoolableConnection) connection).getDelegate();
      }
      catch (Exception e)
      {
        // rethrow the exception and add the SQL statement to the exception
        throw new de.tif.jacob.core.exception.SQLException(this, e, "connect");
      }
    }
    else
    {
      // no pooling -> create an internal connection
      internalConnection = new WrapperConnection(this, createConnection());
      connection = internalConnection;
    }

    // only switch to postpone mode, if activated and needed!
    internalConnection.setPostpone(activatedPostpone && needsPostpone());

    return connection;
  }

  protected boolean needsPostpone()
  {
    // by default: do not activate, i.e.
    return false;
  }

  public boolean avoidSubqueries()
  {
    return false;
  }

  protected Connection createConnection() throws SQLException
  {
    // get connection from autodetect wrapper data source
    if (this.parent != null)
      return this.parent.createConnection();

    try
    {
      checkDestroyed();

      // init connectivity, if not already done
      initConnectivity();

      if (this.connectString != null)
      {
        if (this.driver != null)
        {
          // connect directly by using driver class
          // Advantage: No blocking by means of synchronized methods
          // of DriverManager!
          //
          java.util.Properties info = new java.util.Properties();
          if (this.userName != null)
          {
            info.put("user", this.userName);
          }
          if (this.password != null)
          {
            info.put("password", this.password);
          }
          Connection con = this.driver.connect(this.connectString, info);
          if (con == null)
          {
            throw new SQLException("Could not connect to: " + this.connectString);
          }
          return con;
        }
        else
        {
          // connect directly by using driver manager
          // Disadvantage: One datasource could block all others because of
          // synchronized methods of DriverManager!
          //
          DriverManager.setLoginTimeout(10);

          if (this.userName != null)
            return DriverManager.getConnection(this.connectString, this.userName, this.password);
          else
            return DriverManager.getConnection(this.connectString);
        }
      }
      else
      {
        return this.jndiDataSource.getConnection();
      }
    }
    catch (SQLException e)
    {
      // rethrow the exception and add the SQL statement to the exception
      throw new de.tif.jacob.core.exception.SQLException(this, e, "connect");
    }
  }

  public final Connection getConnection(String username, String password) throws SQLException, InvalidUseridPasswordException
  {
    return new WrapperConnection(this, createConnection(username, password));
  }

  /**
   * Fetches the database roles (groups) the given database user is assigned to.
   * 
   * @param username
   *          the login name of the user
   * @return <code>List</code> of <code>String</code>
   * @throws SQLException
   *           on anz problem
   */
  public abstract List getRoles(String username) throws SQLException;

  /**
   * Changes the password of the given database user.
   * 
   * @param username
   *          the name of the database user
   * @param newPassword
   *          the new password
   * @throws SQLException
   *           on any problem
   * @throws InvalidNewPasswordException
   *           the given password is not a valid password
   */
  public abstract void changePassword(String username, String newPassword) throws SQLException, InvalidNewPasswordException;

  protected Connection createConnection(String username, String password) throws SQLException, InvalidUseridPasswordException
  {
    // get connection from autodetect wrapper data source
    if (this.parent != null)
      return this.parent.createConnection(username, password);

    if (username == null || username.trim().length() == 0)
    {
      // empty userid makes no sense
      throw new InvalidUseridPasswordException();
    }

    try
    {
      checkDestroyed();

      // init connectivity, if not already done
      initConnectivity();

      if (this.connectString != null)
      {
        // IBIS: hack: find out if datasource has an underlying connection pool
        final String poolConnectString = "jdbc:apache:commons:dbcp:";
        if (this.connectString.startsWith(poolConnectString))
        {
          String poolname = this.connectString.substring(poolConnectString.length());
          return Database.getConnection(poolname, username, password);
        }
        else
        {
          if (this.driver != null)
          {
            // connect directly by using driver class
            // Advantage: No blocking by means of synchronized methods
            // of DriverManager!
            //
            java.util.Properties info = new java.util.Properties();
            info.put("user", username);
            if (password != null)
            {
              info.put("password", password);
            }
            Connection con = this.driver.connect(this.connectString, info);
            if (con == null)
            {
              throw new SQLException("Could not connect to: " + this.connectString);
            }
            return con;
          }
          else
          {
            // connect directly by using driver manager
            // Disadvantage: One datasource could block all others because of
            // synchronized methods of DriverManager!
            //
            DriverManager.setLoginTimeout(10);
            return DriverManager.getConnection(this.connectString, username, password);
          }
        }
      }
      else
      {
        // Note: It is not possible to call
        // this.jndiDataSource.getConnection(username, password),
        // because this might fail since JNDI data sources could handle pooled
        // connections
        // (e.g. Apache implementation throws an exception!)
        return DriverManager.getConnection(getJndiConnectionUrl(), username, password);
      }
    }
    catch (SQLException e)
    {
      if (isInvalidUseridPasswordErrorCode(e.getErrorCode()))
        throw new InvalidUseridPasswordException();

      // rethrow the exception and add the SQL statement to the exception
      throw new de.tif.jacob.core.exception.SQLException(this, e, "connect");
    }
  }

  private synchronized String getJndiConnectionUrl() throws SQLException
  {
    if (null == this.jndiConnectionUrl)
    {
      Connection pooledConnection = getConnection();
      try
      {
        this.jndiConnectionUrl = pooledConnection.getMetaData().getURL();
      }
      finally
      {
        pooledConnection.close();
      }
    }
    return this.jndiConnectionUrl;
  }

  protected final IDataSearchResult search(DataRecordSet recordSet, QBESpecification spec, IDataSearchIterateCallback callback, DataTableRecordEventHandler eventHandler) throws InvalidExpressionException
  {
    SQLStatementBuilder queryStatement = buildQueryStatement(spec, false, (recordSet instanceof IDataTable) ? recordSet.getTableAlias() : null);

    try
    {
      return executeQuery(recordSet, spec, queryStatement, callback, eventHandler);
    }
    catch (SQLException e)
    {
      // convert exception (pass over message because otherwise we would not get
      // detailed SQL info)
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  protected final long count(DataRecordSet recordSet, QBESpecification spec) throws InvalidExpressionException
  {
    SQLStatementBuilder queryStatement = buildQueryStatement(spec, true, (recordSet instanceof IDataTable) ? recordSet.getTableAlias() : null);

    try
    {
      return executeCount(queryStatement);
    }
    catch (SQLException e)
    {
      // convert exception (pass over message because otherwise we would not get
      // detailed SQL info)
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * Builds count distinct part as follows:
   * <p>
   * count(DISTINCT expr[, expr ..])
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
    sqlBuffer.append("count(DISTINCT ");
    List keyFields = primaryKey.getTableFields();
    for (int i = 0; i < keyFields.size(); i++)
    {
      if (i != 0)
        sqlBuffer.append(", ");
      ITableField field = (ITableField) keyFields.get(i);
      if (spec.isMultiTableQuery())
      {
        sqlBuffer.appendDBName(this, spec.getAliasToSearch().getName());
        sqlBuffer.append(".");
      }
      sqlBuffer.appendDBName(this, field.getDBName());
    }
    sqlBuffer.append(")");
    return true;
  }
  
  protected boolean supportsSelectDistinct(ITableDefinition table)
  {
    return true;
  }
  
  private SQLStatementBuilder buildQueryStatement(QBESpecification spec, boolean countOnly, ITableAlias dataTableQueryAlias) throws InvalidExpressionException
  {
      return buildQueryStatement(spec, countOnly, dataTableQueryAlias, null);
  }
  
  /**
   * @param spec
   * @param countOnly
   * @param dataTableQueryAlias alias if a data table query is performed, otherwise <code>null</code>
   * @return
   * @throws InvalidExpressionException
   */
  private SQLStatementBuilder buildQueryStatement(QBESpecification spec, boolean countOnly, ITableAlias dataTableQueryAlias, Boolean distinct) throws InvalidExpressionException
  {
    // --------------------------
    // build SQL select statement
    // --------------------------
    List resultFields = spec.getFieldsToQuery();
    SQLStatementBuilder sqlBuffer = new SQLStatementBuilder(this, spec.isMultiTableQuery());
    sqlBuffer.append("SELECT ");

    boolean performSubqueryInsteadOfDistinct = false;
    
    if (countOnly)
    {
      if (spec.isDistinct() && buildCountDistinct(spec, sqlBuffer))
      {
        // already appended, because buildCountDistinct() returned true
      }
      else
        sqlBuffer.append("count(*)");
    }
    else
    {
      if (spec.isDistinct())
      {
        // check whether we have to perform a subquery instead of distinct
        // because not all databases allow distinct queries on each column type
        // (e.g. Oracle on CLOB, BLOB)
        if (dataTableQueryAlias != null && dataTableQueryAlias.getTableDefinition().getPrimaryKey() != null && !supportsSelectDistinct(dataTableQueryAlias.getTableDefinition()))
          performSubqueryInsteadOfDistinct = true;
        else
          sqlBuffer.append("DISTINCT ");
      }
      boolean first = true;
      Iterator iter = resultFields.iterator();
      while (iter.hasNext())
      {
        QBEField field = (QBEField) iter.next();

        // consider empty fields
        if (field.isKeepEmpty())
          continue;

        if (first)
        {
          first = false;
        }
        else
        {
          sqlBuffer.append(", ");
        }

        if (spec.isMultiTableQuery() && !performSubqueryInsteadOfDistinct)
        {
          sqlBuffer.appendDBName(this, field.getTableAlias().getName());
          sqlBuffer.append(".");
        }
        sqlBuffer.appendDBName(this, field.getTableField().getDBName());
      }
    }
    
    // Perform subquery like:
    //
    //    SELECT  
    //    pkey, history, name, ..
    //    FROM oracletest."GROUP"
    //    WHERE (pkey) IN 
    //    (
    //    SELECT anygroup.pkey
    //    FROM oracletest.user_to_group user_to_group, oracletest."GROUP" anygroup 
    //    WHERE user_to_group.group_key=anygroup.pkey AND (user_to_group.user_key=1)
    //    )
    //
    if (performSubqueryInsteadOfDistinct)
    {
      ITableDefinition dataTableQueryTable = dataTableQueryAlias.getTableDefinition();
      
      sqlBuffer.append(" FROM ");
      sqlBuffer.append(getSchemaPrefix(dataTableQueryTable));
      sqlBuffer.appendDBName(this, dataTableQueryTable.getDBName());
      sqlBuffer.append(" WHERE (");
      List primaryKeyFields = dataTableQueryTable.getPrimaryKey().getTableFields();
      for (int i = 0; i < primaryKeyFields.size(); i++)
      {
        if (i > 0)
          sqlBuffer.append(", ");
        
        ITableField primaryKeyField = (ITableField) primaryKeyFields.get(i);
        sqlBuffer.appendDBName(this, primaryKeyField.getDBName());
      }
      
      sqlBuffer.append(") IN (SELECT ");
      for (int i = 0; i < primaryKeyFields.size(); i++)
      {
        if (i > 0)
          sqlBuffer.append(", ");
        
        ITableField primaryKeyField = (ITableField) primaryKeyFields.get(i);
        if (spec.isMultiTableQuery())
        {
          sqlBuffer.appendDBName(this, dataTableQueryAlias.getName());
          sqlBuffer.append(".");
        }
        sqlBuffer.appendDBName(this, primaryKeyField.getDBName());
      }
    }

    sqlBuffer.append(" FROM ");

    // --------------------------------------------------
    // perform ANSI joins if supported by the data source
    // --------------------------------------------------
    if (useAnsiJoins() && spec.isMultiTableQuery())
    {
      // process relation constraints (joins)
      boolean first = true;
      Iterator iter = spec.getAnsiOrderedRelationConstraints();
      while (iter.hasNext())
      {
        QBERelationConstraint constraint = (QBERelationConstraint) iter.next();

        ITableAlias fromAlias = constraint.getFromTableAlias();
        ITableDefinition fromTable = fromAlias.getTableDefinition();
        ITableAlias toAlias = constraint.getToTableAlias();
        ITableDefinition toTable = toAlias.getTableDefinition();

        if (first)
        {
          if (constraint.isForwardFlag())
          {
            sqlBuffer.append(getSchemaPrefix(fromTable));
            sqlBuffer.appendDBName(this, fromTable.getDBName());
            sqlBuffer.append(" ");
            sqlBuffer.appendDBName(this, fromAlias.getName());
          }
          else
          {
            sqlBuffer.append(getSchemaPrefix(toTable));
            sqlBuffer.appendDBName(this, toTable.getDBName());
            sqlBuffer.append(" ");
            sqlBuffer.appendDBName(this, toAlias.getName());
          }

          first = false;
        }

        if (constraint.isInnerJoin())
        {
          sqlBuffer.append(" INNER JOIN ");
        }
        else
        {
          // Since the required table is always on the left hand-side,
          // we do need right outer joins here!
          //
          sqlBuffer.append(" LEFT OUTER JOIN ");
        }

        if (constraint.isForwardFlag())
        {
          sqlBuffer.append(getSchemaPrefix(toTable));
          sqlBuffer.appendDBName(this, toTable.getDBName());
          sqlBuffer.append(" ");
          sqlBuffer.appendDBName(this, toAlias.getName());
        }
        else
        {
          sqlBuffer.append(getSchemaPrefix(fromTable));
          sqlBuffer.appendDBName(this, fromTable.getDBName());
          sqlBuffer.append(" ");
          sqlBuffer.appendDBName(this, fromAlias.getName());
        }

        sqlBuffer.append(" ON ");

        List fromKeyFields = constraint.getFromPrimaryKey().getTableFields();
        List toKeyFields = constraint.getToForeignKey().getTableFields();
        if (fromKeyFields.size() != toKeyFields.size())
        {
          throw new RuntimeException("Unmatching keys");
        }
        for (int i = 0; i < fromKeyFields.size(); i++)
        {
          ITableField fromField = (ITableField) fromKeyFields.get(i);
          ITableField toField = (ITableField) toKeyFields.get(i);
          if (i != 0)
          {
            sqlBuffer.append(" AND ");
          }
          sqlBuffer.appendDBName(this, fromAlias.getName());
          sqlBuffer.append(".");
          sqlBuffer.appendDBName(this, fromField.getDBName());
          sqlBuffer.append("=");
          sqlBuffer.appendDBName(this, toAlias.getName());
          sqlBuffer.append(".");
          sqlBuffer.appendDBName(this, toField.getDBName());
        }
      }
    }
    else
    {
      Iterator iter = spec.getTableAliasesToQuery();
      while (iter.hasNext())
      {
        ITableAlias alias = (ITableAlias) iter.next();

        sqlBuffer.append(getSchemaPrefix(alias.getTableDefinition()));
        sqlBuffer.appendDBName(this, alias.getTableDefinition().getDBName());

        // Note: Alias names for query are necessary for user constraints,
        // because these might contain
        // alias names as well!
        if (spec.isMultiTableQuery() || spec.hasInverseRelationConstraints() || spec.getUserConstraints().size() > 0)
        {
          sqlBuffer.append(" ");
          sqlBuffer.appendDBName(this, alias.getName());
        }

        if (iter.hasNext())
        {
          sqlBuffer.append(", ");
        }
      }

      // -------------------------------------
      // process relation constraints (joins)
      // -------------------------------------
      iter = spec.getRelationConstraints();
      while (iter.hasNext())
      {
        QBERelationConstraint constraint = (QBERelationConstraint) iter.next();

        List fromKeyFields = constraint.getFromPrimaryKey().getTableFields();
        List toKeyFields = constraint.getToForeignKey().getTableFields();
        if (fromKeyFields.size() != toKeyFields.size())
        {
          throw new RuntimeException("Unmatching keys");
        }
        for (int i = 0; i < fromKeyFields.size(); i++)
        {
          ITableField fromField = (ITableField) fromKeyFields.get(i);
          ITableField toField = (ITableField) toKeyFields.get(i);

          sqlBuffer.appendWhereOrAnd();
          appendJoinCondition(sqlBuffer, constraint, fromField, toField);
        }
      }
    }

    // -------------------------------------
    // process field constraints, inverse relations and user condition
    // -------------------------------------
    appendQueryWhere(spec, sqlBuffer);

    // finish subquery
    //
    if (performSubqueryInsteadOfDistinct)
    {
      sqlBuffer.append(")");
    }
    
    // -------------------------------------
    // process orderby clause
    // -------------------------------------
    if (!countOnly)
    {
      boolean first = true;
      Iterator iter = resultFields.iterator();
      while (iter.hasNext())
      {
        QBEField field = (QBEField) iter.next();

        // consider empty fields
        if (field.isKeepEmpty())
          continue;

        if (SortOrder.NONE != field.getSortOrder())
        {
          if (first)
          {
            sqlBuffer.append(" ORDER BY ");
            first = false;
          }
          else
          {
            sqlBuffer.append(", ");
          }

          if (spec.isMultiTableQuery() && !performSubqueryInsteadOfDistinct)
          {
            sqlBuffer.appendDBName(this, field.getTableAlias().getName());
            sqlBuffer.append(".");
          }
          sqlBuffer.appendDBName(this, field.getTableField().getDBName());
          sqlBuffer.append(" ");
          sqlBuffer.append(field.getSortOrder().getSqlKeyword());
        }
      }
    }

    return sqlBuffer;
  }

  public abstract String getSchemaPrefix();

  /**
   * Internal method to fetch schema prefix of other SQL data sources, which is
   * needed to execute queries across multiple schemas within the same database
   * instance.
   * 
   * @param tableDefinition
   *          the table definition
   * @return the requested schema prefix
   */
  private String getSchemaPrefix(ITableDefinition tableDefinition)
  {
    // shortcut to avoid synchronized DataSource.get() and possible
    // deadlocks in rare cases (initialisation of a new datasource).
    //
    if (getName().equals(tableDefinition.getDataSourceName()))
      return getSchemaPrefix();

    DataSource dataSource = DataSource.get(tableDefinition.getDataSourceName());
    if (dataSource instanceof SQLDataSource)
    {
      return ((SQLDataSource) dataSource).getSchemaPrefix();
    }
    return "";
  }

  private void appendExpression(SQLStatementBuilder builder, QBEExpression expr, ITableAlias alias, ITableField field, boolean optional) throws InvalidExpressionException
  {
    if (optional)
      builder.append("(");

    builder.append("(");
    builder.setTableField(alias, field);
    expr.makeConstraint(builder, false);
    builder.append(")");

    // for optional constraints the constrained field could be NULL as well
    //
    if (optional)
    {
      builder.append(" OR ");
      if (builder.useAliasNames())
      {
        builder.appendDBName(this, alias.getName());
        builder.append(".");
      }
      builder.appendDBName(this, field.getDBName());
      builder.append(" IS NULL)");
    }
    builder.resetTableField();
  }

  /**
   * Helper class for collection of field constraints, which are grouped in an
   * alias OR group.
   * 
   * @author Andreas Sonntag
   */
  private final class AliasOrExpression
  {
    private final QBEExpression expr;
    private final ITableAlias alias;
    private final ITableField field;
    private final boolean optional;

    private AliasOrExpression(QBEExpression expr, ITableAlias alias, ITableField field, boolean optional)
    {
      this.expr = expr;
      this.alias = alias;
      this.field = field;
      this.optional = optional;
    }

    private void append(SQLStatementBuilder sqlBuffer) throws InvalidExpressionException
    {
      appendExpression(sqlBuffer, expr, alias, field, optional);
    }
  }

  private void appendQueryWhere(QBESpecification spec, SQLStatementBuilder sqlBuffer) throws InvalidExpressionException
  {
    // -------------------------------------
    // process field constraints
    // -------------------------------------

    // Map<Integer -> List<AliasOrExpression>>
    Map orGroupExpressionsMap = null;
    Iterator iter = spec.getFieldConstraints();
    while (iter.hasNext())
    {
      QBEFieldConstraint constraint = (QBEFieldConstraint) iter.next();

      ITableAlias alias = constraint.getTableAlias();
      ITableField field = constraint.getTableField();

      try
      {
        QBEExpression expr = field.getType().createQBEExpression(this, constraint);
        if (expr == null)
          continue;

        // collect OR-constraints
        //
        if (expr.getOrGroupNumber() != null)
        {
          if (orGroupExpressionsMap == null)
            orGroupExpressionsMap = new HashMap();

          List list = (List) orGroupExpressionsMap.get(expr.getOrGroupNumber());
          if (list == null)
          {
            list = new ArrayList();
            orGroupExpressionsMap.put(expr.getOrGroupNumber(), list);
          }
          list.add(new AliasOrExpression(expr, alias, field, constraint.isOptional()));
          continue;
        }

        sqlBuffer.appendWhereOrAnd();
        appendExpression(sqlBuffer, expr, alias, field, constraint.isOptional());
      }
      catch (InvalidExpressionException ex)
      {
        // exchange exception to add field info
        throw new InvalidFieldExpressionException(field, ex);
      }
    }

    // process OR-constraints
    //
    if (orGroupExpressionsMap != null)
    {
      Iterator iter2 = orGroupExpressionsMap.values().iterator();
      while (iter2.hasNext())
      {
        List list = (List) iter2.next();

        sqlBuffer.appendWhereOrAnd();
        sqlBuffer.append("(");
        for (int i = 0; i < list.size(); i++)
        {
          if (i != 0)
            sqlBuffer.append(" OR ");
          ((AliasOrExpression) list.get(i)).append(sqlBuffer);
        }
        sqlBuffer.append(")");
      }
    }

    // -------------------------------------
    // process inverse relations
    // -------------------------------------
    iter = spec.getInverseRelationConstraints();
    while (iter.hasNext())
    {
      QBERelationConstraint constraint = (QBERelationConstraint) iter.next();
      sqlBuffer.appendWhereOrAnd();
      appendInverseJoinCondition(sqlBuffer, constraint);
    }

    // -------------------------------------
    // process user where clauses
    // -------------------------------------
    List userConstraints = spec.getUserConstraints();
    for (int i = 0; i < userConstraints.size(); i++)
    {
      QBEUserConstraint userConstraint = (QBEUserConstraint) userConstraints.get(i);
      sqlBuffer.appendWhereOrAnd();
      sqlBuffer.append("(").append(userConstraint.toString()).append(")");
    }
  }

  public void appendInverseJoinCondition(SQLStatementBuilder sqlQuery, QBERelationConstraint constraint)
  {
    sqlQuery.append("NOT EXISTS (SELECT * FROM ");
    sqlQuery.append(getSchemaPrefix(constraint.getToTableAlias().getTableDefinition()));
    sqlQuery.appendDBName(this, constraint.getToTableAlias().getTableDefinition().getDBName());
    sqlQuery.append(" ");
    sqlQuery.appendDBName(this, constraint.getToTableAlias().getName());
    sqlQuery.append(" WHERE ");
    boolean first = true;
    List fromKeyFields = constraint.getFromPrimaryKey().getTableFields();
    List toKeyFields = constraint.getToForeignKey().getTableFields();
    for (int i = 0; i < fromKeyFields.size(); i++)
    {
      ITableField fromField = (ITableField) fromKeyFields.get(i);
      ITableField toField = (ITableField) toKeyFields.get(i);

      if (first)
      {
        first = false;
      }
      else
      {
        sqlQuery.append(" AND ");
      }
      sqlQuery.appendDBName(this, constraint.getToTableAlias().getName());
      sqlQuery.append(".");
      sqlQuery.appendDBName(this, toField.getDBName());
      sqlQuery.append("=");
      sqlQuery.appendDBName(this, constraint.getFromTableAlias().getName());
      sqlQuery.append(".");
      sqlQuery.appendDBName(this, fromField.getDBName());
    }

    // do not forget possible alias condition
    ITableAliasCondition aliasCondition = constraint.getToTableAlias().getCondition(this);
    if (aliasCondition != null)
    {
      sqlQuery.append(" AND (").append(aliasCondition.toString()).append(")");
    }

    sqlQuery.append(")");
  }

  public abstract void appendJoinCondition(SQLStatementBuilder sqlQuery, QBERelationConstraint constraint, ITableField fromField, ITableField toField);

  public abstract boolean useAnsiJoins();

  protected void beforeExecuteCount(SQLStatementBuilder queryStatement) throws java.sql.SQLException
  {
    // do nothing by default
    // might be overwritten to change queryStatement
  }
  
  protected final long executeCount(SQLStatementBuilder queryStatement) throws java.sql.SQLException
  {
    beforeExecuteCount(queryStatement);
    
    String query = queryStatement.toString();
    
    Connection connection = getConnection();
    try
    {
      Statement statement = connection.createStatement();
      try
      {
        ResultSet rs = statement.executeQuery(query);
        try
        {
          // move cursor to the first and only record
          rs.next();

          // fetch count field
          return rs.getLong(1);
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
    catch (de.tif.jacob.core.exception.SQLException e)
    {
      // already a wrapped SQL exception
      throw e;
    }
    catch (SQLException e)
    {
      // rethrow the exception and add the SQL statement to the exception
      throw new de.tif.jacob.core.exception.SQLException(this, e, query);
    }
    finally
    {
      connection.close();
    }
  }

  protected void beforeExecuteQuery(DataRecordSet recordSet, QBESpecification spec, SQLStatementBuilder queryStatement, DataTableRecordEventHandler eventHandler) throws java.sql.SQLException
  {
    // do nothing by default
    // might be overwritten to change queryStatement
  }
  
  protected final IDataSearchResult executeQuery(DataRecordSet recordSet, QBESpecification spec, SQLStatementBuilder queryStatement, IDataSearchIterateCallback callback, DataTableRecordEventHandler eventHandler) throws java.sql.SQLException
  {
    beforeExecuteQuery(recordSet, spec, queryStatement, eventHandler);
    
    String query = queryStatement.toString();
    
    Connection connection = getConnection();
    try
    {
      Statement statement = connection.createStatement();
      try
      {
        DataSearchResult result = new DataSearchResult(callback == null);
        ResultSet rs = statement.executeQuery(query);
        try
        {
          List resultFields = spec.getFieldsToQuery();
          int maxRecords;
          if (callback != null)
          {
            maxRecords = Integer.MAX_VALUE;
          }
          else
          {
            maxRecords = recordSet.getMaxRecords();
            if (maxRecords == DataRecordSet.DEFAULT_MAX_RECORDS)
              maxRecords = Property.BROWSER_SYSTEM_MAX_RECORDS.getIntValue();
            if (maxRecords == DataRecordSet.UNLIMITED_RECORDS)
              maxRecords = Integer.MAX_VALUE;
          }
          int[] primaryKeyIndices = spec.getPrimaryKeyIndices();
          boolean hasMore = rs.next();
          while (hasMore && result.getRecordCount() < maxRecords)
          {
            Object[] values = new Object[resultFields.size()];
            int j = 1;
            for (int i = 0; i < values.length; i++)
            {
              QBEField field = (QBEField) resultFields.get(i);
              if (field.isKeepEmpty())
              {
                values[i] = null;
              }
              else
              {
                try
                {
                  values[i] = field.getTableField().getType().convertSQLValueToDataValue(this, rs, j++);
                }
                catch (SQLException ex)
                {
                  logger.error("convertSqlValueToDataValue(): Problem with field " + field, ex);
                  throw ex;
                }
                catch (IndexOutOfBoundsException ex)
                {
                  // Throw more detailed exception for corrupted Quintus Enumeration fields
                  logger.error("convertSqlValueToDataValue(): Problem with field " + field, ex);
                  throw new IndexOutOfBoundsException("Field " + field + ": " + ex.getMessage());
                }
              }
            }

            DataRecord record = instantiateRecord(recordSet, eventHandler, primaryKeyIndices, values, result.getRecordCount());

            // record filtered or not?
            if (record != null)
            {
              if (callback != null && !callback.onNextRecord(record))
                maxRecords = 0;
              else
                result.add(record);
            }

            // check for more records
            hasMore = rs.next();
          }
          result.setHasMore(hasMore);
          return result;
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
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (de.tif.jacob.core.exception.SQLException e)
    {
      // already a wrapped SQL exception
      throw e;
    }
    catch (SQLException e)
    {
      // rethrow the exception and add the SQL statement to the exception
      throw new de.tif.jacob.core.exception.SQLException(this, e, query);
    }
    catch (UserException e)
    {
      throw new UserRuntimeException(e);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      connection.close();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.impl.DataSource#newExecutionContext(de.tif.jacob.core.data.impl.DataTransaction)
   */
  protected final DataExecutionContext newExecutionContext(DataTransaction transaction) throws Exception
  {
    return new SQLExecutionContext(this, transaction);
  }

  protected final void executeInternal(DataExecutionContext context, TAInsertRecordAction action) throws Exception
  {
    DataTableRecord record = action.getRecord();
    ITableDefinition tableDefinition = record.getParent().getTableAlias().getTableDefinition();

    // -------------------
    // build SQL statement
    // -------------------
    StringBuffer sqlBuffer = new StringBuffer(250);
    sqlBuffer.append("INSERT INTO ");
    sqlBuffer.append(getSchemaPrefix(tableDefinition));
    appendDBName(sqlBuffer, tableDefinition.getDBName());
    sqlBuffer.append(" (");
    boolean first = true;
    for (int i = 0; i < record.getFieldNumber(); i++)
    {
      if (action.hasOldValue(i))
      {
        ITableField field = record.getFieldDefinition(i);
        if (field.getType().isDBAutoGenerated(this))
        {
          // skip
        }
        else
        {
          if (first)
          {
            first = false;
          }
          else
          {
            sqlBuffer.append(", ");
          }
          appendDBName(sqlBuffer, field.getDBName());
        }
      }
    }

    // check whether any modifications have to be performed
    if (first)
    {
      logger.warn("No values to insert");
      return;
    }

    sqlBuffer.append(") VALUES (");
    first = true;
    for (int i = 0; i < record.getFieldNumber(); i++)
    {
      if (action.hasOldValue(i))
      {
        ITableField tableField = record.getFieldDefinition(i);

        if (tableField.getType().isDBAutoGenerated(this))
        {
          // skip
        }
        else
        {
          if (first)
          {
            first = false;
            sqlBuffer.append('?');
          }
          else
          {
            sqlBuffer.append(", ?");
          }
        }
      }
    }
    sqlBuffer.append(')');

    // -------------------------------------
    // create prepared statement and fill it
    // -------------------------------------
    String sqlString = sqlBuffer.toString();

    SQLExecutionContext sqlContext = (SQLExecutionContext) context;
    try
    {
      PreparedStatement statement = sqlContext.getConnection().prepareStatement(sqlString);
      try
      {
        ITableField autogeneratedkeyTableField = null;
        int index = 1;
        for (int i = 0; i < record.getFieldNumber(); i++)
        {
          if (action.hasOldValue(i))
          {
            ITableField tableField = record.getFieldDefinition(i);

            if (tableField.getType().isDBAutoGenerated(this))
            {
              autogeneratedkeyTableField = tableField;
            }
            else
            {
              sqlContext.setCurrentDBFieldName(tableField.getDBName());
              tableField.getType().setSQLValueForInsert(sqlContext, statement, index++, record.getValueInternal(i));
            }
          }
        }

        // execute statement
        //
        // IBIS: Vermeiden, dass AUTOKEY in die Historie geschrieben wird.
        int result = statement.executeUpdate();

        // check execution result
        //
        if (1 != result)
        {
          String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
          throw new de.tif.jacob.core.exception.SQLException(this, result + " records inserted (expected 1)!", sqlStatement);
        }

        // handle auto generated database keys
        //
        if (autogeneratedkeyTableField != null)
        {
          ResultSet rs = statement.getGeneratedKeys();
          try
          {
            ResultSetMetaData rsmeta = rs.getMetaData();
            if (rs.next())
            {
              for (int col = 1; col <= rsmeta.getColumnCount(); col++)
              {
                String dbFieldName = rsmeta.getColumnName(col);

                IDataAutoKeyValue autoKeyValue;
                // Hack: MySQL does not deliver correct column name but
                // "GENERATED_KEY" instead
                if (rsmeta.getColumnCount() == 1)
                  autoKeyValue = (IDataAutoKeyValue) record.getValueInternal(autogeneratedkeyTableField.getFieldIndex());
                else
                  autoKeyValue = (IDataAutoKeyValue) record.getValueInternal(tableDefinition.getTableFieldByDBName(dbFieldName).getFieldIndex());

                autoKeyValue.setGeneratedValue(rs.getObject(col));
              }
            }

            // plausibility check, i.e. there should be only 1 row!
            if (rs.next())
              logger.warn("Unexpected auto key result set of table: " + tableDefinition.getDBName());
          }
          finally
          {
            rs.close();
          }
        }
      }
      catch (SQLException e)
      {
        if (isUniqueViolationErrorCode(e.getErrorCode()))
        {
          throw new UniqueViolationException(e.getLocalizedMessage());
        }

        throw e;
      }
      finally
      {
        statement.close();
      }
    }
    catch (UniqueViolationException ex)
    {
      // Try to throw a more detailed UniqueViolationFieldException..
      // NOTE: (For MSSQL Server) This must happen after statement.close()!!!
      //
      checkUniqueViolation(record, sqlContext, ex.getDetails());

      throw ex;
    }
  }

  private void checkUniqueViolation(DataTableRecord record, SQLExecutionContext sqlContext, String details) throws UniqueViolationException
  {
    try
    {
      // Alle unique Keys prüfen, ob noch ein Duplikat vorhanden ist
      //
      ITableDefinition tableDefinition = record.getTableAlias().getTableDefinition(); 
      Iterator iter = tableDefinition.getKeys();
      while (iter.hasNext())
      {
        IKey key = (IKey) iter.next();
        if (key.getType().isUnique())
        {
          boolean keyvalueHasChanged = false;

          // Constraints setzen
          List fields = key.getTableFields();
          for (int i = 0; i < fields.size(); i++)
          {
            ITableField field = (ITableField) fields.get(i);
            if (record.hasChangedValue(field.getFieldIndex()))
            {
              if (record.hasNullValue(field.getFieldIndex()))
              {
                // Value ist Null -> Key ignorieren
                break;
              }
              keyvalueHasChanged = true;
            }
          }

          if (keyvalueHasChanged && exists(record, key, sqlContext))
          {
            if (fields.size() == 1)
            {
              ITableField field = (ITableField) fields.get(0);
              throw new UniqueViolationFieldException(field, record.getStringValue(field.getFieldIndex()), details);
            }
            
            throw new UniqueViolationFieldsException(key, details);
          }
        }
      }
    }
    catch (UniqueViolationException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      logger.warn("Can not evaluate unique violation", ex);
    }
  }
  
  /**
   * Internal method to check whether a record already exists with the same key
   * value as the given record.
   * 
   * @param record
   * @param key
   * @param sqlContext
   * @return
   * @throws SQLException
   */
  private boolean exists(DataTableRecord record, IKey key, SQLExecutionContext sqlContext) throws SQLException
  {
    ITableDefinition tableDefinition = record.getTableAlias().getTableDefinition();
    StringBuffer sqlBuffer = new StringBuffer(256);
    sqlBuffer.append("SELECT count(*) FROM ");
    sqlBuffer.append(getSchemaPrefix(tableDefinition));
    appendDBName(sqlBuffer, tableDefinition.getDBName());
    sqlBuffer.append(" WHERE ");

    List keyFields = key.getTableFields();
    for (int i = 0; i < keyFields.size(); i++)
    {
      if (i != 0)
      {
        sqlBuffer.append(" AND ");
      }
      ITableField field = (ITableField) keyFields.get(i);
      appendDBName(sqlBuffer, field.getDBName()).append("=?");
    }

    IDataKeyValue keyValue = record.getKeyValue(key);

    // create prepared statement and fill it
    String sqlString = sqlBuffer.toString();
    PreparedStatement statement = sqlContext.getConnection().prepareStatement(sqlString);

    try
    {
      int index = 1;
      for (int i = 0; i < keyFields.size(); i++)
      {
        ITableField field = (ITableField) keyFields.get(i);
        sqlContext.setCurrentDBFieldName(field.getDBName());
        field.getType().setSQLValue(sqlContext, statement, index++, keyValue.getFieldValue(i));
      }

      ResultSet rs = statement.executeQuery();
      try
      {
        // move cursor to the first and only record
        rs.next();
        return rs.getLong(1) > 0;
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

  /**
   * Internal helper method for use at different locations.
   * 
   * @param sqlBuffer
   * @param tableDefinition
   */
  private void appendWherePrimary(StringBuffer sqlBuffer, ITableDefinition tableDefinition)
  {
    sqlBuffer.append(" WHERE ");
    List primaryKeyFields = tableDefinition.getPrimaryKey().getTableFields();
    for (int i = 0; i < primaryKeyFields.size(); i++)
    {
      if (i != 0)
      {
        sqlBuffer.append(" AND ");
      }
      ITableField field = (ITableField) primaryKeyFields.get(i);
      appendDBName(sqlBuffer, field.getDBName()).append("=?");
    }
  }

  /**
   * Internal helper method for use at different locations.
   * 
   * @param sqlContext
   * @param statement
   * @param record
   * @param index
   * @return
   * @throws SQLException
   */
  private static int setWherePrimaryKey(SQLExecutionContext sqlContext, PreparedStatement statement, DataTableRecord record, int index, boolean useOldPrimaryKey) throws SQLException
  {
    ITableDefinition tableDefinition = record.getParent().getTableAlias().getTableDefinition();
    List primaryKeyFields = tableDefinition.getPrimaryKey().getTableFields();
    for (int i = 0; i < primaryKeyFields.size(); i++)
    {
      ITableField field = (ITableField) primaryKeyFields.get(i);

      // Since modification of primary keys should be allowed as well
      // -> old key values have to be taken
      Object primaryFieldValue = useOldPrimaryKey ? record.getOldValue(field.getFieldIndex()) : record.getValue(field.getFieldIndex());

      sqlContext.setCurrentDBFieldName(field.getDBName());
      field.getType().setSQLValue(sqlContext, statement, index++, primaryFieldValue);
    }
    return index;
  }

  /**
   * Internal helper method for use at different locations.
   * 
   * @param tableDefinition
   * @param fieldsToIncrement
   * @param forUpdate
   * @return
   */
  private String createFieldsToIncrementSelectStatement(ITableDefinition tableDefinition, List fieldsToIncrement, boolean forUpdate)
  {
    StringBuffer sqlBuffer = new StringBuffer(128);
    boolean first = true;
    sqlBuffer.append("SELECT ");
    for (int i = 0; i < fieldsToIncrement.size(); i++)
    {
      if (first)
        first = false;
      else
        sqlBuffer.append(", ");
      ITableField field = (ITableField) fieldsToIncrement.get(i);
      appendDBName(sqlBuffer, field.getDBName());
    }
    sqlBuffer.append(" FROM ");
    sqlBuffer.append(getSchemaPrefix(tableDefinition));
    appendDBName(sqlBuffer, tableDefinition.getDBName());
    appendWherePrimary(sqlBuffer, tableDefinition);
    if (forUpdate)
      sqlBuffer.append(" FOR UPDATE");
    return sqlBuffer.toString();
  }

  protected final void executeInternal(DataExecutionContext context, TAUpdateRecordAction action) throws Exception
  {
    DataTableRecord record = action.getRecord();
    ITableDefinition tableDefinition = record.getParent().getTableAlias().getTableDefinition();
    List fieldsToIncrement = null;

    // --------------------------
    // build SQL update statement
    // --------------------------
    StringBuffer sqlBuffer = new StringBuffer(256);
    sqlBuffer.append("UPDATE ");
    sqlBuffer.append(getSchemaPrefix(tableDefinition));
    appendDBName(sqlBuffer, tableDefinition.getDBName());
    sqlBuffer.append(" SET ");
    boolean first = true;
    for (int i = 0; i < record.getFieldNumber(); i++)
    {
      if (action.hasOldValue(i))
      {
        if (first)
          first = false;
        else
          sqlBuffer.append(", ");

        ITableField field = record.getFieldDefinition(i);

        // regard increment/decrement as well
        //
        appendDBName(sqlBuffer, field.getDBName());
        Object dataValue = record.getValueInternal(i);
        if (dataValue instanceof DataIncOrDecValue)
        {
          // collect fields to increment
          if (fieldsToIncrement == null)
            fieldsToIncrement = new ArrayList();
          fieldsToIncrement.add(field);

          sqlBuffer.append("=");
          appendDBName(sqlBuffer, field.getDBName());
          sqlBuffer.append(((DataIncOrDecValue) dataValue).isDecrement() ? "-?" : "+?");
        }
        else
          sqlBuffer.append("=?");
      }
    }
    appendWherePrimary(sqlBuffer, tableDefinition);

    // ----------------------------------------------------
    // check whether any modifications have to be performed
    // ----------------------------------------------------
    if (first)
    {
      logger.warn("No values to update");
      return;
    }

    SQLExecutionContext sqlContext = (SQLExecutionContext) context;

    // -----------------------------------------------------------------
    // physically lock row in database and check whether the record is not
    // already pessimistically locked, if fields have to be incremented
    // -----------------------------------------------------------------
    if (fieldsToIncrement != null)
    {
      if (supportsForUpdate())
      {
        String sqlString = createFieldsToIncrementSelectStatement(tableDefinition, fieldsToIncrement, true);
        PreparedStatement statement = sqlContext.getConnection().prepareStatement(sqlString);
        try
        {
          setWherePrimaryKey(sqlContext, statement, record, 1, true);

          // execute query to fetch values to increment
          //
          ResultSet rs = statement.executeQuery();
          try
          {
            if (!rs.next())
            {
              throw new RecordNotFoundException(record.getOldId());
            }

            int j = 1;
            for (int i = 0; i < fieldsToIncrement.size(); i++)
            {
              ITableField field = (ITableField) fieldsToIncrement.get(i);

              DataIncOrDecValue dataIncrementValue = (DataIncOrDecValue) record.getValueInternal(field.getFieldIndex());
              Object valueToIncrement = field.getType().convertSQLValueToDataValue(this, rs, j++);
              if (valueToIncrement == null)
                throw new RuntimeException("Field " + field + " is null and cannot be incremented!");
              dataIncrementValue.setIncrementedValue(valueToIncrement, true);
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

      // update should fail, if record is physically locked by another
      // transaction
      sqlContext.getTransaction().checkLocked(record.getOldId());
    }

    // --------------------------------------------
    // create prepared update statement and fill it
    // --------------------------------------------
    String sqlString = sqlBuffer.toString();
    try
    {
      PreparedStatement statement = sqlContext.getConnection().prepareStatement(sqlString);
      try
      {
        int index = 1;
        for (int i = 0; i < record.getFieldNumber(); i++)
        {
          if (action.hasOldValue(i))
          {
            ITableField field = record.getFieldDefinition(i);

            sqlContext.setCurrentDBFieldName(field.getDBName());
            field.getType().setSQLValueForUpdate(sqlContext, statement, index++, record.getValueInternal(i));
          }
        }

        index = setWherePrimaryKey(sqlContext, statement, record, index, true);

        // and execute statement
        int result = statement.executeUpdate();

        if (1 != result)
        {
          String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
          throw new de.tif.jacob.core.exception.SQLException(this, result + " records updated (expected 1)!", sqlStatement);
        }
      }
      catch (SQLException e)
      {
        if (isUniqueViolationErrorCode(e.getErrorCode()))
        {
          throw new UniqueViolationException(e.getLocalizedMessage());
        }
        if (isForeignConstraintViolationErrorCode(e.getErrorCode()))
        {
          throw new ForeignConstraintViolationException(e.getLocalizedMessage());
        }
        throw e;
      }
      finally
      {
        statement.close();
      }
    }
    catch (UniqueViolationException ex)
    {
      // Try to throw a more detailed UniqueViolationFieldException..
      // NOTE: (For MSSQL Server) This must happen after statement.close()!!!
      //
      checkUniqueViolation(record, sqlContext, ex.getDetails());

      throw ex;
    }

    // -----------------------------------------------------------------
    // fetch incremented values from database
    // Note: This only works 100%, if database supports row locking by
    // means of "FOR UPDATE"
    // -----------------------------------------------------------------
    if (fieldsToIncrement != null && !supportsForUpdate())
    {
      sqlString = createFieldsToIncrementSelectStatement(tableDefinition, fieldsToIncrement, false);
      PreparedStatement statement = sqlContext.getConnection().prepareStatement(sqlString);
      try
      {
        setWherePrimaryKey(sqlContext, statement, record, 1, false);

        // execute query to fetch incremented values
        ResultSet rs = statement.executeQuery();
        try
        {
          if (!rs.next())
          {
            throw new RecordNotFoundException(record.getId());
          }

          int j = 1;
          for (int i = 0; i < fieldsToIncrement.size(); i++)
          {
            ITableField field = (ITableField) fieldsToIncrement.get(i);

            DataIncOrDecValue dataIncrementValue = (DataIncOrDecValue) record.getValueInternal(field.getFieldIndex());
            Object incrementedValue = field.getType().convertSQLValueToDataValue(this, rs, j++);
            if (incrementedValue == null)
              throw new RuntimeException("Field " + field + " is null and cannot be incremented!");
            dataIncrementValue.setIncrementedValue(incrementedValue, false);
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

  protected final void executeInternal(DataExecutionContext context, TADeleteRecordAction action) throws Exception
  {
    DataRecord record = action.getRecord();
    ITableDefinition tableDefinition = record.getParent().getTableAlias().getTableDefinition();

    SQLExecutionContext sqlContext = (SQLExecutionContext) context;

    // ensure that also (mapped) field values are deleted from internal tables
    //
    List fields = tableDefinition.getTableFields();
    for (int i = 0; i < fields.size(); i++)
    {
      ITableField field = (ITableField) fields.get(i);

      if (needsMappingOfBinary(field.getType()))
      {
        getAdjustment().getBinaryAdjustment().deleteFieldValue(sqlContext, record.getValueInternal(i));
      }
      if (needsMappingOfLongText(field.getType()))
      {
        getAdjustment().getLongTextAdjustment().deleteFieldValue(sqlContext, record.getValueInternal(i));
      }
      if (field.getType() instanceof DocumentFieldType)
      {
        getAdjustment().getDocumentAdjustment().deleteFieldValue(sqlContext, record.getValueInternal(i));
      }
    }

    StringBuffer sqlBuffer = new StringBuffer(250);
    sqlBuffer.append("DELETE FROM ");
    sqlBuffer.append(getSchemaPrefix(tableDefinition));
    appendDBName(sqlBuffer, tableDefinition.getDBName());
    sqlBuffer.append(" WHERE ");
    IDataKeyValue keyValue = record.getPrimaryKeyValue();
    List keyFields = tableDefinition.getPrimaryKey().getTableFields();
    for (int i = 0; i < keyFields.size(); i++)
    {
      if (i != 0)
      {
        sqlBuffer.append(" AND ");
      }
      ITableField field = (ITableField) keyFields.get(i);
      appendDBName(sqlBuffer, field.getDBName()).append("=?");
    }

    // create prepared statement and fill it
    String sqlString = sqlBuffer.toString();
    PreparedStatement statement = sqlContext.getConnection().prepareStatement(sqlString);

    try
    {
      int index = 1;
      for (int i = 0; i < keyFields.size(); i++)
      {
        ITableField field = (ITableField) keyFields.get(i);
        sqlContext.setCurrentDBFieldName(field.getDBName());
        field.getType().setSQLValue(sqlContext, statement, index++, keyValue.getFieldValue(i));
      }

      // and execute statement
      int result = statement.executeUpdate();

      if (1 != result)
      {
        String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
        throw new de.tif.jacob.core.exception.SQLException(this, result + " records deleted (expected 1)!", sqlStatement);
      }
    }
    catch (SQLException e)
    {
      int code = NumberUtils.stringToInt(e.getSQLState(), 0);

      if (isForeignConstraintViolationErrorCode(code))
      {
        throw new ForeignConstraintViolationException(e.getLocalizedMessage());
      }
      throw e;
    }
    finally
    {
      statement.close();
    }
  }

  /**
   * Checks whether the underlying datasource is MS SQL Server.
   * 
   * @return
   */
  // private boolean isMsSQLServer()
  // {
  // SQLDataSource ds = this;
  // if (ds instanceof AutoDetectSQLDataSource)
  // ds = ((AutoDetectSQLDataSource) ds).getAutoDetectedDataSource();
  // return ds instanceof MsSQLServerDataSource;
  // }
  protected final void executeInternal(DataExecutionContext context, TADeleteRecordsAction action) throws Exception
  {
    ITableAlias alias = action.getSpec().getAliasToSearch();
    ITableDefinition tableDefinition = alias.getTableDefinition();
    SQLExecutionContext sqlContext = (SQLExecutionContext) context;

    // ensure that also (mapped) field values are deleted from internal tables
    //
    List fields = tableDefinition.getTableFields();
    for (int i = 0; i < fields.size(); i++)
    {
      ITableField field = (ITableField) fields.get(i);

      if (needsMappingOfBinary(field.getType()))
      {
        getAdjustment().getBinaryAdjustment().deleteFieldValues(sqlContext, buildEmbeddedSelectFragment(field, action.getSpec()));
      }
      if (needsMappingOfLongText(field.getType()))
      {
        getAdjustment().getLongTextAdjustment().deleteFieldValues(sqlContext, buildEmbeddedSelectFragment(field, action.getSpec()));
      }
      if (field.getType() instanceof DocumentFieldType)
      {
        getAdjustment().getDocumentAdjustment().deleteFieldValues(sqlContext, buildEmbeddedSelectFragment(field, action.getSpec()));
      }
    }

    boolean useAliasNames = false; // action.getSpec().isMultiTableQuery() ||
    // action.getSpec().getUserConstraints().size()
    // > 0;
    SQLStatementBuilder sqlBuffer = new SQLStatementBuilder(this, useAliasNames);
    sqlBuffer.append("DELETE FROM ");
    sqlBuffer.append(getSchemaPrefix(tableDefinition));
    sqlBuffer.appendDBName(this, tableDefinition.getDBName());

    // Note: Alias names for query are necessary for user constraints, because
    // these might contain
    // alias names as well!
    //
    // Vorarbeiten für Aufgabe 57!!!!!
    //
    // if (useAliasNames)
    // {
    // if (isMsSQLServer())
    // {
    // // MS SQL Server Hack due to strange syntax:
    // // DELETE FROM joerg2..customer eds WHERE (eds.email IS NOT NULL)
    // // throws: Line 1: Incorrect syntax near
    // 'eds'.[SQLState:HY000][ErrorCode:170]
    // // but
    // // DELETE FROM joerg2..customer FROM joerg2..customer eds WHERE
    // (eds.email IS NOT NULL)
    // // works (at least for SQL Server 2000)!
    // //
    // sqlBuffer.append(" FROM ");
    // sqlBuffer.append(getSchemaPrefix(tableDefinition));
    // sqlBuffer.appendDBName(this, tableDefinition.getDBName());
    // }
    // sqlBuffer.append(" ");
    // sqlBuffer.appendDBName(this, alias.getName());
    // }
    appendQueryWhere(action.getSpec(), sqlBuffer);

    // create prepared statement and fill it
    String sqlString = sqlBuffer.toString();
    Statement statement = sqlContext.getConnection().createStatement();

    try
    {
      // execute statement
      int result = statement.executeUpdate(sqlString);

      if (logger.isDebugEnabled())
      {
        logger.debug(result + " records deleted by means of: " + sqlString);
      }
    }
    catch (SQLException e)
    {
      if (isForeignConstraintViolationErrorCode(e.getErrorCode()))
      {
        throw new ForeignConstraintViolationException(e.getLocalizedMessage());
      }
      throw e;
    }
    finally
    {
      statement.close();
    }
  }

  private String buildEmbeddedSelectFragment(ITableField field, QBESpecification spec) throws Exception
  {
    SQLStatementBuilder sqlBuffer = new SQLStatementBuilder(this, false);
    sqlBuffer.append("SELECT ").appendDBName(this, field.getDBName()).append(" FROM ");
    sqlBuffer.append(getSchemaPrefix(field.getTableDefinition()));
    sqlBuffer.appendDBName(this, field.getTableDefinition().getDBName());
    appendQueryWhere(spec, sqlBuffer);
    sqlBuffer.appendWhereOrAnd();
    sqlBuffer.appendDBName(this, field.getDBName()).append(" IS NOT NULL");

    // create prepared statement and fill it
    return sqlBuffer.toString();
  }

  /**
   * @return Returns the connectString.
   */
  protected final String getConnectString()
  {
    if (this.connectString == null)
      throw new IllegalStateException("Connect string can not be accessed for JNDI data sources");
    return connectString;
  }

  public abstract boolean isClientAbortErrorCode(int errorCode);

  public abstract boolean isUniqueViolationErrorCode(int errorCode);

  public abstract boolean isForeignConstraintViolationErrorCode(int errorCode);

  public abstract boolean isTableNotExistsErrorCode(int errorCode);

  public abstract boolean isInvalidUseridPasswordErrorCode(int errorCode);

  /**
   * Returns the database-specific statement separator.
   * 
   * @return the statement separator
   */
  public abstract String getSQLStatementSeparator();

  /**
   * @param sqlType
   * @param size
   * @param decimalDigits
   * @return
   */
  public abstract String getSqlColumnType(int sqlType, int size, int decimalDigits);

  public final boolean isQuintusAdjustment()
  {
    return getAdjustment().getClass().getName().equals(QUINTUS_ADJUSTMENT_CLASS);
  }

  public abstract Reconfigure getReconfigureImpl();

  public final void setup(ISchemaDefinition currentSchema) throws Exception
  {
    if (isQuintusAdjustment())
      setupAsQuintusDatasource(currentSchema);
    else
      setupAsJacobDatasource(currentSchema);
  }

  /**
   * @param currentSchema
   * @throws Exception
   */
  protected void setupAsJacobDatasource(ISchemaDefinition currentSchema) throws Exception
  {
    ISchemaDefinition definition = new JacobInternalDefinition(this);

    CommandList commandList = getReconfigureImpl().reconfigure(definition, currentSchema, false);
    commandList.execute(this);
  }

  protected void setupAsQuintusDatasource(ISchemaDefinition currentSchema) throws Exception
  {
    // create jacob base definition to create jacob internal tables which have
    // no Quintus equivalent
    //
    ISchemaDefinition definition = new JacobInternalBaseDefinition(this);

    CommandList commandList = getReconfigureImpl().reconfigure(definition, currentSchema, false);
    commandList.execute(this);
  }

  /**
   * Checks whether the database supports "FOR UPDATE" row locking.
   * 
   * @return <code>true</code> for update supported, otherwise
   *         <code>false</code>
   */
  public boolean supportsForUpdate()
  {
    // by default: yes
    return true;
  }

  /**
   * Checks whether the database supports multiple null values in a unique index
   * column.
   * 
   * @return <code>true</code> multiple null values are supported, otherwise
   *         <code>false</code>
   */
  public boolean supportsMultipleNullsForUniqueIndices()
  {
    // by default: yes
    return true;
  }

  public long newJacobIds(ITableDefinition table, ITableField field, int increment) throws SQLException
  {
    if (increment < 1)
      throw new IllegalArgumentException("increment is " + increment);
    
    if (logger.isDebugEnabled())
      logger.debug("Getting next key for table=" + table.getDBName());

    long next = -1;

    Connection connection = getConnection();
    try
    {
      if (supportsStoredProcedures())
      {
        // -------------------------------------
        // call stored procedure jacob_next_id()
        // -------------------------------------
        //
        CallableStatement statement = buildStoredProcedureStatement(connection, "jacob_next_id", 3);
        try
        {
          statement.setString(1, table.getDBName());
          statement.setInt(2, increment);
          statement.registerOutParameter(3, java.sql.Types.BIGINT);
          statement.executeUpdate();
          next = statement.getLong(3);
        }
        finally
        {
          statement.close();
        }
      }
      else
      {
        // -------------------------------------
        // do the same without stored procedure
        // -------------------------------------
        //

        // 1. execute query first
        //
        String sqlString;
        if (supportsForUpdate())
          sqlString = "SELECT nextid FROM jacob_ids WHERE tablename=? FOR UPDATE";
        else
          sqlString = "SELECT nextid FROM jacob_ids WHERE tablename=?";

        PreparedStatement statement = connection.prepareStatement(sqlString);
        try
        {
          statement.setString(1, table.getDBName());

          // and execute query to fetch next id, if existing
          ResultSet rs = statement.executeQuery();
          try
          {
            if (rs.next())
            {
              next = rs.getLong(1);
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

        // 2. and update/insert entry then
        //
        try
        {
          if (next == -1)
          {
            next = 1;
            sqlString = "INSERT INTO jacob_ids (tablename, nextid) VALUES (?, ?)";
            statement = connection.prepareStatement(sqlString);
            statement.setString(1, table.getDBName());
            statement.setLong(2, 1 + increment);
          }
          else
          {
            sqlString = "UPDATE jacob_ids SET nextid=? WHERE tablename=?";
            statement = connection.prepareStatement(sqlString);
            statement.setLong(1, next + increment);
            statement.setString(2, table.getDBName());
          }

          // and execute statement
          int result = statement.executeUpdate();
          if (1 != result)
          {
            String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
            throw new de.tif.jacob.core.exception.SQLException(this, result + " records inserted/updated (expected 1)!", sqlStatement);
          }
        }
        finally
        {
          statement.close();
        }

        // 3. and commit the stuff
        //
        connection.commit();
      }
    }
    catch (SQLException ex)
    {
      try
      {
        connection.rollback();
      }
      catch (SQLException ex2)
      {
        // ignore
        logger.warn("Rollback failed!", ex2);
      }
      throw ex;
    }
    finally
    {
      connection.close();
    }

    if (-1 == next)
    {
      throw new SQLException("Getting next id from jacob_ids failed");
    }
    if (logger.isDebugEnabled())
      logger.debug("Got next key=" + next + " for table=" + table.getDBName());

    return next;
  }

  public boolean setNextJacobId(ITableDefinition table, ITableField field, long nextId) throws SQLException
  {
    if (logger.isDebugEnabled())
      logger.debug("Setting next key " + nextId + " for table=" + table.getDBName());

    long oldNextId = -1;

    Connection connection = getConnection();
    try
    {
      // 1. execute query first
      //
      String sqlString;
      if (supportsForUpdate())
        sqlString = "SELECT nextid FROM jacob_ids WHERE tablename=? FOR UPDATE";
      else
        sqlString = "SELECT nextid FROM jacob_ids WHERE tablename=?";

      PreparedStatement statement = connection.prepareStatement(sqlString);
      try
      {
        statement.setString(1, table.getDBName());

        // and execute query to fetch next id, if existing
        ResultSet rs = statement.executeQuery();
        try
        {
          if (rs.next())
          {
            oldNextId = rs.getLong(1);
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

      // 2. and update/insert entry then
      //
      if (oldNextId != nextId)
      {
        try
        {
          if (oldNextId == -1)
          {
            oldNextId = 1;
            if (nextId != 1)
            {
              sqlString = "INSERT INTO jacob_ids (tablename, nextid) VALUES (?, ?)";
              statement = connection.prepareStatement(sqlString);
              statement.setString(1, table.getDBName());
              statement.setLong(2, nextId);
            }
          }
          else
          {
            sqlString = "UPDATE jacob_ids SET nextid=? WHERE tablename=?";
            statement = connection.prepareStatement(sqlString);
            statement.setLong(1, nextId);
            statement.setString(2, table.getDBName());
          }

          // and execute statement
          int result = statement.executeUpdate();
          if (1 != result)
          {
            String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
            throw new de.tif.jacob.core.exception.SQLException(this, result + " records inserted/updated (expected 1)!", sqlStatement);
          }
        }
        finally
        {
          statement.close();
        }
      }

      // 3. and commit the stuff
      //
      connection.commit();
    }
    catch (SQLException ex)
    {
      try
      {
        connection.rollback();
      }
      catch (SQLException ex2)
      {
        // ignore
        logger.warn("Rollback failed!", ex2);
      }
      throw ex;
    }
    finally
    {
      connection.close();
    }

    if (oldNextId != nextId)
      logger.info("Set next key=" + nextId + " (was " + oldNextId + ") for table=" + table.getDBName());
    else
      logger.info("Setting next key=" + nextId + " not necessary for table=" + table.getDBName());

    return oldNextId != nextId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.IDataSource#test()
   */
  public String test() throws Exception
  {
    StringBuffer buffer = new StringBuffer();

    // simply try to get a connection
    Connection con = getConnection();
    try
    {
      final String INACCESSIBLE = "unknown";

      DatabaseMetaData metaData = con.getMetaData();
      buffer.append("Database product name: ").append(metaData.getDatabaseProductName()).append("\r\n");
      buffer.append("Database product version: ").append(metaData.getDatabaseProductVersion()).append("\r\n");

      buffer.append("Database major version: ");
      try
      {
        buffer.append(metaData.getDatabaseMajorVersion());
      }
      catch (Throwable ex)
      {
        buffer.append(INACCESSIBLE);
      }
      buffer.append("\r\n");

      buffer.append("Database minor version: ");
      try
      {
        buffer.append(metaData.getDatabaseMinorVersion());
      }
      catch (Throwable ex)
      {
        buffer.append(INACCESSIBLE);
      }
      buffer.append("\r\n");

      buffer.append("Driver name: ").append(metaData.getDriverName()).append("\r\n");
      buffer.append("Driver version: ").append(metaData.getDriverVersion()).append("\r\n");

      buffer.append("Driver major version: ");
      try
      {
        buffer.append(metaData.getDriverMajorVersion());
      }
      catch (Throwable ex)
      {
        buffer.append(INACCESSIBLE);
      }
      buffer.append("\r\n");

      buffer.append("Driver minor version: ");
      try
      {
        buffer.append(metaData.getDriverMinorVersion());
      }
      catch (Throwable ex)
      {
        buffer.append(INACCESSIBLE);
      }
      buffer.append("\r\n");

      buffer.append("URL: ").append(metaData.getURL()).append("\r\n");
      buffer.append("User: ").append(metaData.getUserName()).append("\r\n");
      buffer.append("Catalog: ").append(con.getCatalog()).append("\r\n");

      // fetch catalogs
      //
      buffer.append("Catalogs: ");
      try
      {
        StringBuffer buf = new StringBuffer();
        ResultSet rs = metaData.getCatalogs();
        try
        {
          while (rs.next())
          {
            if (buf.length() > 0)
              buf.append(";");
            String table_cat = rs.getString("TABLE_CAT");
            buf.append(table_cat);
          }
        }
        finally
        {
          rs.close();
        }
        buffer.append(buf.toString());
      }
      catch (Throwable ex)
      {
        buffer.append(INACCESSIBLE);
      }
      buffer.append("\r\n");

      // fetch schemas
      //
      buffer.append("Schemas: ");
      try
      {
        StringBuffer buf = new StringBuffer();
        ResultSet rs = metaData.getSchemas();
        try
        {
          while (rs.next())
          {
            if (buf.length() > 0)
              buf.append(";");
            String table_cat = rs.getString("TABLE_CATALOG");
            String table_schem = rs.getString("TABLE_SCHEM");
            if (table_cat != null)
              buf.append(table_cat).append(".");
            buf.append(table_schem);
          }
        }
        finally
        {
          rs.close();
        }
        buffer.append(buf.toString());
      }
      catch (Throwable ex)
      {
        buffer.append(INACCESSIBLE);
      }
      buffer.append("\r\n");
      
      // fetch catalog term
      //
      buffer.append("Catalog term: ");
      try
      {
        buffer.append(metaData.getCatalogTerm());
      }
      catch (Throwable ex)
      {
        buffer.append(INACCESSIBLE);
      }
      buffer.append("\r\n");

      // fetch schema term
      //
      buffer.append("Schema term: ");
      try
      {
        buffer.append(metaData.getSchemaTerm());
      }
      catch (Throwable ex)
      {
        buffer.append(INACCESSIBLE);
      }
      buffer.append("\r\n");

      buffer.append("Transaction isolation: ").append(con.getTransactionIsolation()).append("\r\n");
    }
    finally
    {
      con.close();
    }

    return buffer.toString();
  }

  /**
   * Test data source
   * 
   * @author Andreas Sonntag
   */
  private static class TestDataSource extends SQLDataSource
  {
    public void appendJoinCondition(SQLStatementBuilder sqlQuery, QBERelationConstraint constraint, ITableField fromField, ITableField toField)
    {
    }

    public CallableStatement buildStoredProcedureStatement(Connection connection, String procedureName, int argnum) throws SQLException
    {
      return null;
    }

    public void changePassword(String username, String newPassword) throws SQLException, InvalidNewPasswordException
    {
    }

    public boolean supportsAutoKeyGeneration()
    {
      return false;
    }

    public boolean supportsLongTextSearch(int longtextSqlType, boolean casesensitive)
    {
      return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getRoles(java.lang.String)
     */
    public List getRoles(String username) throws SQLException
    {
      return Collections.EMPTY_LIST;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getLowerFunctionName()
     */
    public String getLowerFunctionName()
    {
      return "lower";
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#escapeDBName(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public StringBuffer appendDBName(StringBuffer buffer, String dbName)
    {
      return buffer.append(dbName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendDBComment(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public StringBuffer appendDBComment(StringBuffer buffer, String comment)
    {
      return buffer.append("/* ").append(comment).append(" */");
    }

    public boolean isInvalidUseridPasswordErrorCode(int errorCode)
    {
      return false;
    }

    public boolean isUniqueViolationErrorCode(int errorCode)
    {
      return false;
    }

    public boolean isForeignConstraintViolationErrorCode(int errorCode)
    {
      return false;
    }

    public boolean isTableNotExistsErrorCode(int errorCode)
    {
      return false;
    }

    public boolean needsMappingOfLongText()
    {
      return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#needsMappingOfBinary()
     */
    public boolean needsMappingOfBinary()
    {
      return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer,
     *      java.sql.Date)
     */
    public String toQueryString(Date date)
    {
      return date.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer,
     *      java.sql.Time)
     */
    public String toQueryString(Time time)
    {
      return time.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer,
     *      java.sql.Timestamp)
     */
    public String toQueryString(Timestamp timestamp)
    {
      return timestamp.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#useAnsiJoins()
     */
    public boolean useAnsiJoins()
    {
      return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isClientAbortErrorCode(int)
     */
    public boolean isClientAbortErrorCode(int errorCode)
    {
      return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSQLStatementSeparator()
     */
    public String getSQLStatementSeparator()
    {
      return "\r\n";
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSchemaPrefix()
     */
    public String getSchemaPrefix()
    {
      return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSqlColumnType(int,
     *      int, int)
     */
    public String getSqlColumnType(int sqlType, int size, int decimalDigits)
    {
      throw new UnsupportedOperationException();
    }

    public int getDefaultBinarySQLType()
    {
      throw new UnsupportedOperationException();
    }

    public int getDefaultLongTextSQLType()
    {
      throw new UnsupportedOperationException();
    }
    
    public Reconfigure getReconfigureImpl()
    {
      throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.IDataSource#setupAsJacobDatasource()
     */
    public void setupAsJacobDatasource() throws Exception
    {
      throw new UnsupportedOperationException();
    }
  }
}
