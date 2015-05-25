/*******************************************************************************
 *    This file is part of jACOB
 *    Copyright (C) 2005-2009 Tarragon GmbH
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
import java.sql.DatabaseMetaData;
import java.sql.Date;
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
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.qbe.QBERelationConstraint;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.schema.Table;
import de.tif.jacob.core.data.impl.schema.TableColumn;
import de.tif.jacob.core.data.impl.sql.reconfigure.AlterTableCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.DropColumnCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.DropPrimaryKeyCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.DropTableCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.ModifyColumnCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.data.impl.sql.reconfigure.RenewPrimaryKeyCommand;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.schema.ISchemaColumnDefinition;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;
import de.tif.jacob.util.StringUtil;

/**
 * Oracle data source implementation.
 * 
 * Tested with version 8.1.7, but should work with 9.x and 10.x as well.
 * 
 * @author Andreas Sonntag
 */
public abstract class OracleDataSource extends SQLDataSource
{
  static public transient final String RCS_ID = "$Id: OracleDataSource.java,v 1.8 2010-11-11 12:56:03 sonntag Exp $";
	static public transient final String RCS_REV = "$Revision: 1.8 $";
	
  static private final transient Log logger = LogFactory.getLog(OracleDataSource.class);

	/**
	 * Oracle 8.1.5 keywords!
	 */
	private static final String[] KEYWORDS = {
	    "ACCESS", "ADD", "ALL", "ALTER", "AND", "ANY", "AS", "ASC", "AUDIT", //
      "BETWEEN", "BY", //
      "CHAR", "CHECK", "CLUSTER", "COLUMN", "COMMENT", "COMPRESS", "CONNECT", "CREATE", "CURRENT", //
      "DATE", "DECIMAL", "DEFAULT", "DELETE", "DESC", "DISTINCT", "DROP", //
      "ELSE", "EXCLUSIVE", "EXISTS", //
      "FILE", "FLOAT", "FOR", "FROM", //
      "GRANT", "GROUP", //
      "HAVING", //
      "IDENTIFIED", "IMMEDIATE", "IN", "INCREMENT", "INDEX", "INITIAL", "INSERT", "INTEGER", "INTERSECT", "INTO", "IS", //
      "LEVEL", "LIKE", "LOCK", "LONG", //
      "MAXEXTENTS", "MINUS", "MLSLABEL", "MODE", "MODIFY", //
      "NOAUDIT", "NOCOMPRESS", "NOT", "NOWAIT", "NULL", "NUMBER", //
      "OF", "OFFLINE", "ON", "ONLINE", "OPTION", "OR", "ORDER", //
      "PCTFREE", "PRIOR", "PRIVILEGES", "PUBLIC", //
      "RAW", "RENAME", "RESOURCE", "REVOKE", "ROW", "ROWID", "ROWNUM", "ROWS", //
      "SELECT", "SESSION", "SET", "SHARE", "SIZE", "SMALLINT", "START", "SUCCESSFUL", "SYNONYM", "SYSDATE", //
      "TABLE", "THEN", "TO", "TRIGGER", //
      "UID", "UNION", "UNIQUE", "UPDATE", "USER", //
      "VALIDATE", "VALUES", "VARCHAR", "VARCHAR2", "VIEW", //
      "WHENEVER", "WHERE", "WITH" };
	
	/**
	 * Set containing all keywords and access is done by ignoring case.
	 */
	private static final Set keywords = new TreeSet(String.CASE_INSENSITIVE_ORDER);
  
  private static final String ESCAPE = "\"";
  
  private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
  private static final DateFormat timestampFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
  private static final DateFormat timeFormat = new SimpleDateFormat("1900/01/01 HH:mm:ss");
  
  // ORA-00001: Verstoﬂ gegen Eindeutigkeit, Regel (JACOB.UK_ADMINISTRATOR_LOGINIDIDX)
  private static final int ORA_00001 = 1;
  
  // ORA-00955: Es gibt bereits ein Objekt mit diesem Namen
  private static final int ORA_00955 = 955;
  
  // ORA-00942: Tabelle oder View nicht vorhanden
  private static final int ORA_00942 = 942;
  
  // ORA-01017: invalid username/password; logon denied 
  private static final int ORA_01017 = 1017;
  
  // Keine Benutzer- oder Kennwortangabe in THIN-Treiber nicht unterst¸tzt
  private static final int ERROR_17443 = 17443;
  
  // ORA-01013: Benutzer veranlaﬂte Abbruch des laufenden Vorganges
  private static final int ORA_01013 = 1013;
  
  // ORA-02292: Verstoﬂ gegen Integrit‰tsregel (BKM.FK2_USER_COMPANY_FKEY).
  // Untergeordneter Datensatz gefunden.
  private static final int ORA_2292 = 2292;
  
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
  }
  
  /**
   * @param parent
   */
  public OracleDataSource(AutoDetectSQLDataSource parent)
  {
    super(parent);
  }

  /**
   * @param record
   * @throws Exception
   */
  public OracleDataSource(IDataTableRecord record) throws Exception
  {
    super(record);
  }

	/**
	 * @param name
	 */
	public OracleDataSource(String name) throws Exception
	{
		super(name);
	}

  /**
   * Determines the major database version number.
   * 
   * @return the major database version number or <code>-1</code>, if
   *         unknown.
   */
  private synchronized int getMajorVersionNumber()
  {
    if (this.majorVersionNumber == null)
    {
      try
      {
        Connection connection = getConnection();
        try
        {
          this.majorVersionNumber = new Integer(extractMajorVersionNumber(connection.getMetaData()));
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

  /**
   * Gets the major version number from Oracle meta data.
   * <p>
   * Handle version strings like: <br>
   * Oracle Database 10g Enterprise Edition Release 10.1.0.2.0 - Production<br>
   * Oracle8i Enterprise Edition Release 8.1.7.0.0 - Production<br>
   * 
   * @param meta database meta data
   * @return major version number or <code>-1</code>, if extract fails
   */
  public static int extractMajorVersionNumber(DatabaseMetaData meta) throws SQLException
  {
    try
    {
      return meta.getDatabaseMajorVersion();
    }
    catch (Throwable ex)
    {
      // will fail if JDBC driver does not support this method
      // NoSuchMethodError or NoSuchMethodException
    }
    
    String version = meta.getDatabaseProductVersion();
    
    if (logger.isDebugEnabled())
      logger.debug("Extracting major database version from: " + version);

    final String RELEASE = "Release ";

    int startIndex = version.indexOf(RELEASE);
    if (startIndex != -1)
    {
      startIndex += RELEASE.length();

      int endIndex = version.indexOf(".", startIndex);
      if (endIndex != -1)
      {
        String majorVersion = version.substring(startIndex, endIndex);
        try
        {
          return Integer.parseInt(majorVersion);
        }
        catch (Exception ex)
        {
          // ignore
        }
      }
    }

    if (logger.isWarnEnabled())
      logger.warn("Extracting major database version failed: " + version);

    // unknown version
    return -1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#changePassword(java.lang.String,
   *      java.lang.String)
   */
  public void changePassword(String username, String newPassword) throws SQLException, InvalidNewPasswordException
  {
    try
    {
      Connection con = getConnection();
      try
      {
        Statement stmt = con.createStatement();
        try
        {
          stmt.executeUpdate("alter user " + username + " identified by " + newPassword);
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
    catch (SQLException exc)
    {
      // ORA-00922: missing or invalid option -> Oracle does not accept the new
      // password
      // ORA-00988: missing or invalid password(s)
      if (exc.getErrorCode() == 922 || exc.getErrorCode() == 988)
        throw new InvalidNewPasswordException();
      throw exc;
    }
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getRoles(java.lang.String)
   */
  public List getRoles(String username) throws SQLException
  {
    List roleNames = new ArrayList();

    // select granted_role from sys.dba_role_privs where grantee ='Q'
    String statement = "select granted_role from sys.dba_role_privs where LOWER(grantee) ='" + username.toLowerCase() + "'";

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
            String roleName = rset.getString("GRANTED_ROLE").toLowerCase();
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
  
	public String getLowerFunctionName()
	{
		return "LOWER";
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#escapeDBName(java.lang.StringBuffer, java.lang.String)
   */
  public StringBuffer appendDBName(StringBuffer buffer, String dbName)
  {
    // Note: Object names in Oracle are all upper case, if not explicitly escaped.
    // Since we want to be compatible to old Quintus designs (usually lower case names)
    // and old Quintus database instances (upper case names), we only escape object
    // names if required (potential keyword).
    
    // the given name is an Oracle keyword?
    if (keywords.contains(dbName))
    {
      // since database object names are by default upper case -> convert to
      // upper case as well
      return buffer.append(ESCAPE).append(dbName.toUpperCase()).append(ESCAPE);
    }
    return buffer.append(dbName);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendDBComment(java.lang.StringBuffer, java.lang.String)
   */
  public StringBuffer appendDBComment(StringBuffer buffer, String comment)
  {
    return buffer.append("// ").append(comment);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendJoinCondition(de.tif.jacob.core.data.impl.sql.SQLStatementBuilder, de.tif.jacob.core.data.impl.qbe.QBERelationConstraint, de.tif.jacob.core.definition.ITableField, de.tif.jacob.core.definition.ITableField)
   */
  public void appendJoinCondition(SQLStatementBuilder sqlQuery, QBERelationConstraint constraint, ITableField fromField, ITableField toField)
  {
    // Proprietary Oracle outer join syntax (ANSI joins are supported since Oracle 9i)
    // Note: The (+) indicates the optional table of the join
    //
    sqlQuery.appendDBName(this, constraint.getToTableAlias().getName());
    sqlQuery.append(".");
    sqlQuery.appendDBName(this, toField.getDBName());
    if (!constraint.isInnerJoin() && constraint.isForwardFlag())
    {
      // perform outer join
      sqlQuery.append("(+)");
    }
    sqlQuery.append("=");
    sqlQuery.appendDBName(this, constraint.getFromTableAlias().getName());
    sqlQuery.append(".");
    sqlQuery.appendDBName(this, fromField.getDBName());
    if (!constraint.isInnerJoin() && !constraint.isForwardFlag())
    {
      // perform outer join
      sqlQuery.append("(+)");
    }
  }
  

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.sql.SQLDataSource#buildStoredProcedureStatement(java.sql.Connection, java.lang.String, int)
	 */
	public CallableStatement buildStoredProcedureStatement(Connection connection, String procedureName, int argnum) throws SQLException
	{
    StringBuffer buffer = new StringBuffer();
    buffer.append("BEGIN ");
    buffer.append(procedureName);
    buffer.append(" (");
    for (int i=0; i < argnum; i++)
    {
      if (i!=0)
        buffer.append(", ");
      buffer.append("?");
    }
    buffer.append("); END;");
    String callstr = buffer.toString();
    return connection.prepareCall(callstr);
  }

	/* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#buildCountDistinct(de.tif.jacob.core.data.impl.qbe.QBESpecification, de.tif.jacob.core.data.impl.sql.SQLStatementBuilder)
   */
  protected boolean buildCountDistinct(QBESpecification spec, SQLStatementBuilder sqlBuffer)
  {
    sqlBuffer.append("count(DISTINCT ");
    if (spec.isMultiTableQuery())
    {
      sqlBuffer.appendDBName(this, spec.getAliasToSearch().getName());
      sqlBuffer.append(".");
    }
    sqlBuffer.append("ROWID)");
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
    synchronized(dateFormat)
    {
    	return "to_date(" + SQL.QUOTE + dateFormat.format(date) + SQL.QUOTE + ", 'yyyy/mm/dd')";
    }
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer, java.sql.Time)
	 */
	public String toQueryString(Time time)
	{
    synchronized(timeFormat)
    {
      // Note: date will be set to 1.1.1900
      return "to_date(" + SQL.QUOTE + timeFormat.format(time) + SQL.QUOTE + ", 'yyyy/mm/dd hh24:mi:ss')";
    }
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer, java.sql.Timestamp)
	 */
	public String toQueryString(Timestamp timestamp)
	{
    synchronized(timestampFormat)
    {
    	return "to_date(" + SQL.QUOTE + timestampFormat.format(timestamp) + SQL.QUOTE + ", 'yyyy/mm/dd hh24:mi:ss')";
    }
  }

	/**
   * Creates or replaces a PL/SQL stored procedure.
   * 
   * @param con
   *          an SQL connection to the database
   * @param procedure
   *          the PL/SQL procedure to create or replace
   * @throws SQLException
   *           if the operation fails
   * 
   * @since 2.6
   */
  public static void createOrReplaceStoredProcedure(Connection con, String procedure) throws SQLException
	{
		// Note: Oracle does not like carriage returns in statement, therefore drop these!
		procedure = StringUtil.replace(procedure, "\r", "");

		Statement statement = con.createStatement();
		try
		{
			statement.execute(procedure);
		}
		catch (SQLException ex)
		{
			// procedure already exists?
			if (ex.getErrorCode() != ORA_00955)
			{
				// a real problem -> rethrow
				throw ex;
			}
		}
		finally
		{
		  con.clearWarnings();
			statement.close();
		}
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
      // create stored procedure to fetch new numeric ids
      createOrReplaceStoredProcedure(connection, getResourceContent("oracle_jacob_next_id.sql"));
    }
    finally
    {
      connection.close();
    }
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#supportsIdGeneration()
   */
  public boolean supportsAutoKeyGeneration()
  {
    // IBIS: Check whether implementation could be done by sequences
    return false;
  }
  
  public boolean supportsLongTextSearch(int longtextSqlType, boolean casesensitive)
  {
    if (longtextSqlType == Types.CLOB)
    {
      // IBIS: Wird Volltextsuche nicht auch schon bei Oracle 8 unterst¸tzt?
      return getMajorVersionNumber() >= 9;
    }
    return false;
  }

  public boolean useAnsiJoins()
  {
    return getMajorVersionNumber() >= 9;
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

  public boolean isUniqueViolationErrorCode(int errorCode)
	{
		return errorCode == ORA_00001;
	}

  public boolean isForeignConstraintViolationErrorCode(int errorCode)
  {
    return errorCode == ORA_2292;
  }

	public boolean isInvalidUseridPasswordErrorCode(int errorCode)
	{
		return errorCode == ORA_01017 || errorCode == ERROR_17443;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isClientAbortErrorCode(int)
   */
  public boolean isClientAbortErrorCode(int errorCode)
  {
    return errorCode == ORA_01013;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isTableNotExistsErrorCode(int)
   */
  public boolean isTableNotExistsErrorCode(int errorCode)
  {
    return errorCode == ORA_00942;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSchemaPrefix()
   */
  public synchronized String getSchemaPrefix()
  {
    if (this.schemaPrefix == null)
    {
      try
      {
    		Connection connection = this.getConnection();
    		try
        {
          // Full qualified tablename in Oracle is schemaname.tablename
          // Note: by means of getUserName() we get the schemaname
          String schemaName = connection.getMetaData().getUserName();
          if (needsEscape(schemaName))
          {
            this.schemaPrefix = ESCAPE + schemaName + ESCAPE + ".";
          }
          else
          {
            // toLowerCase() looks nicer :-)
            this.schemaPrefix = schemaName.toLowerCase() + ".";
          }
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
    return this.schemaPrefix;
  }
  
  private static boolean needsEscape(String schemaName)
  {
    // schema name is a keyword?
    if (keywords.contains(schemaName))
      return true;

    // or does not only contain alphanumeric characters
    for (int i = 0; i < schemaName.length(); i++)
    {
      char c = schemaName.charAt(i);

      if (Character.isLetterOrDigit(c))
        continue;
      if ('_' == c)
        continue;

      // Note: Schema name might contain characters like '-'
      return true;
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSqlColumnType(int,
   *      int, int)
   */
  public String getSqlColumnType(int sqlType, int size, int decimalDigits)
  {
    switch (sqlType)
    {
      case Types.BOOLEAN:
      case Types.BIT:
        return "NUMBER(1)";
      
      case Types.SMALLINT:
        if (isQuintusAdjustment())
        {
          // QDesigner maps enumeration fields to NUMBER(2)
          return "NUMBER(2)";
        }
        return "NUMBER(5)";
      
      case Types.INTEGER:
// IBIS : TEMP: Um erst einmal zuviele alter table zu verhindern         
//        if (isQuintusAdjustment())
//        {
//          // QDesigner maps INTEGER also to BIGINT
//          return "NUMBER(38)";
//        }
//        return "NUMBER(10)";
      
      case Types.BIGINT:
        return "NUMBER(38)";
      
      case Types.DECIMAL:
        return "NUMBER(38," + decimalDigits + ")";
        
      case Types.NUMERIC:
        return "NUMBER(" + size + ")";
      
      case Types.CHAR:
        return "CHAR(" + size + ")";
      
      case Types.VARCHAR:
        return "VARCHAR(" + size + ")";
      
      case Types.LONGVARCHAR:
        return "LONG";
      
      case Types.CLOB:
        return "CLOB";
      
      case Types.REAL:
        return "REAL";
      
      case Types.FLOAT:
      case Types.DOUBLE:
        return "FLOAT";
      
      case Types.VARBINARY:
        return "RAW(" + size + ")";
      
      case Types.BINARY:
      case Types.LONGVARBINARY:
        return "LONG RAW";
      
      case Types.BLOB:
        return "BLOB";
      
      case Types.TIME:
      case Types.DATE:
      case Types.TIMESTAMP:
        return "DATE";
    }
    
    throw new RuntimeException("Unsupported SQL type: " + sqlType);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSQLStatementSeparator()
   */
  public String getSQLStatementSeparator()
  {
    return ";\n\n";
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getReconfigureImpl()
   */
  public Reconfigure getReconfigureImpl()
  {
    return new OracleReconfigure(this);
  }
  
  /**
   * @author Andreas
   *
   * The Oracle specific reconfigure behaviour
   */
  private static class OracleReconfigure extends Reconfigure
  {
    /**
     * @param dataSource
     */
    private OracleReconfigure(SQLDataSource dataSource)
    {
      super(dataSource);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#getMetaCatalogName()
     */
    public String getMetaCatalogName()
    {
      // we do not use catalog name
      return null;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#getMetaSchemaPattern()
     */
    protected String getMetaSchemaPattern()
    {
      try
      {
    		Connection connection = this.dataSource.getConnection();
    		try
        {
          // Note: by means of getUserName() we get the schemaname
          return connection.getMetaData().getUserName();
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
    
    protected TableColumn fetchTableColumn(ResultSet rs) throws Exception
    {
      String table_name = rs.getString("TABLE_NAME");
      // Oracle 10 returns columns like: BIN$InGAqDoDTKzgRAAUOOsbfw==$0.CRMUSERNAME:12,VARCHAR2,100.0,null,false
      if (table_name.startsWith("BIN$"))
        return null;
      
      return super.fetchTableColumn(rs);
    }

    protected TableColumn fetchTableColumn(String table_name, String column_name, int data_type, String type_name, int column_size, int decimal_digits,
        String column_default, boolean is_nullable)
    {
      // Oracle seems to always return DECIMAL for numerical columns
      if (data_type == Types.DECIMAL && decimal_digits == 0)
      {
        if (this.dataSource.isQuintusAdjustment())
        {
          switch (column_size)
          {
            case 2: 
              // QDesigner maps enumeration fields to NUMBER(2)
              data_type = Types.SMALLINT; 
              break;
            case 38: 
              data_type = Types.INTEGER;
              break;
            default:
              data_type = Types.NUMERIC;
            	break;
          }
        }
        else
        {
          switch (column_size)
          {
            case 5: 
              data_type = Types.SMALLINT;
              break;
            case 10: 
              data_type = Types.INTEGER;
              break;
            case 38: 
              data_type = Types.BIGINT;
              break;
            default:
              data_type = Types.NUMERIC;
            	break;
          }
        }
      }
      
      // Oracle JDBC driver does not map FLOAT to Types.FLOAT!
      if (data_type == Types.OTHER && type_name.equalsIgnoreCase("FLOAT"))
      {
        data_type = Types.FLOAT;
      }
      
      // Oracle JDBC driver does return a default value of a column with a space
      // at the end. Example: "2 " or "'test' ".
      if (column_default != null)
      {
        // strip away obsolete spaces
        column_default = column_default.trim();
        
        // strip away ' character
        if (column_default.length() > 2 && column_default.charAt(0)=='\'' && column_default.charAt(column_default.length()-1)=='\'')
        {
          column_default = column_default.substring(1, column_default.length()-1);
        }
      }
      
      // If a default value is dropped by means of "ALTER TABLE education MODIFY(valid DEFAULT NULL)", 
      // column_default would be "NULL" and not <code>null</code>. This is strange but works.
      // Therefore treat "NULL" as <code>null</code>.
      // This behaviour has been discovered testing with Oracle 8.1.7!
      if ("NULL".equals(column_default))
      {
        column_default = null;
      }
      
      return new TableColumn(table_name, column_name, data_type, column_size, decimal_digits, column_default, is_nullable);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#fetchIndexInfo(java.sql.Connection, java.lang.String, java.lang.String, de.tif.jacob.core.data.impl.schema.Table)
     */
    protected void fetchIndexInfo(Connection connection, String catalogName, String schemaPattern, Table table) throws Exception
    {
      Statement statement = connection.createStatement();
      try
      {
        ResultSet rs = getIndexInfo(statement, schemaPattern, table.getDBName());
        try
        {
          while (rs.next())
          {
            addTableIndexPart(table, rs);
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

    /**
     * Oracle JDBC driver has a bug, because index info cannot be fetched for
     * table having a name which equals to an Oracle keyword!
     * 
     * @param statement
     * @param schemaPattern
     * @param tablename
     * @return
     * @throws SQLException
     */
    private ResultSet getIndexInfo(Statement statement, String schemaPattern, String tablename) throws SQLException
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append("analyze table ").append(schemaPattern).append(".");
      // This is the reason of the JDBC driver bug:
      // tablename is not escaped, if there is a conflict with a keyword!
      this.dataSource.appendDBName(buffer, tablename);
      buffer.append(" estimate statistics");
      statement.executeUpdate(buffer.toString());

      String s4 = "select null as table_cat,\n       owner as table_schem,\n       table_name,\n       0 as NON_UNIQUE,\n       null as index_qualifier,\n       null as index_name, 0 as type,\n       0 as ordinal_position, null as column_name,\n       null as asc_or_desc,\n       num_rows as cardinality,\n       blocks as pages,\n       null as filter_condition\nfrom all_tables\nwhere table_name = '"
          + tablename + "'\n";
      String s5 = "  and owner = '" + schemaPattern + "'\n";
      String s6 = "select null as table_cat,\n       i.owner as table_schem,\n       i.table_name,\n       decode (i.uniqueness, 'UNIQUE', 0, 1),\n       null as index_qualifier,\n       i.index_name,\n       1 as type,\n       c.column_position as ordinal_position,\n       c.column_name,\n       null as asc_or_desc,\n       i.distinct_keys as cardinality,\n       i.leaf_blocks as pages,\n       null as filter_condition\nfrom all_indexes i, all_ind_columns c\nwhere i.table_name = '"
          + tablename + "'\n";
      String s7 = "  and i.owner = '" + schemaPattern + "'\n";
      String s9 = "  and i.index_name = c.index_name\n  and i.table_owner = c.table_owner\n  and i.table_name = c.table_name\n  and i.owner = c.index_owner\n";
      String s10 = "order by non_unique, type, index_name, ordinal_position\n";
      String s11 = s4 + s5 + "union\n" + s6 + s7 + s9 + s10;
      return statement.executeQuery(s11);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newModifyColumnCommand(de.tif.jacob.core.schema.ISchemaColumnDefinition, de.tif.jacob.core.schema.ISchemaColumnDefinition, boolean, boolean, boolean)
     */
    protected ModifyColumnCommand newModifyColumnCommand(ISchemaColumnDefinition desiredColumn, ISchemaColumnDefinition currentColumn, boolean modifyType, boolean modifyDefault, boolean modifyRequired, boolean modifyAutoGenerated)
    {
      return new OracleModifyColumnCommand(desiredColumn, currentColumn, modifyType, modifyDefault, modifyRequired, modifyAutoGenerated);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newAlterTableCommand(de.tif.jacob.core.schema.ISchemaTableDefinition, java.util.List)
     */
    protected AlterTableCommand newAlterTableCommand(ISchemaTableDefinition tableDefinition, List alterTableCommands)
    {
      return new OracleAlterTableCommand(tableDefinition, alterTableCommands);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newDropTableCommand(de.tif.jacob.core.schema.ISchemaTableDefinition)
     */
    protected DropTableCommand newDropTableCommand(ISchemaTableDefinition tableDefinition)
    {
      return new OracleDropTableCommand(tableDefinition);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newDropColumnCommand(de.tif.jacob.core.schema.ISchemaColumnDefinition)
     */
    protected DropColumnCommand newDropColumnCommand(ISchemaColumnDefinition currentColumn)
    {
      return new OracleDropColumnCommand(currentColumn);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newRenewPrimaryKeyCommand(de.tif.jacob.core.schema.ISchemaKeyDefinition, de.tif.jacob.core.schema.ISchemaKeyDefinition)
     */
    protected RenewPrimaryKeyCommand newRenewPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition,
        ISchemaKeyDefinition desiredPrimaryKeyDefinition)
    {
      return new OracleRenewPrimaryKeyCommand(currentPrimaryKeyDefinition, desiredPrimaryKeyDefinition);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newDropPrimaryKeyCommand(de.tif.jacob.core.schema.ISchemaKeyDefinition)
     */
    protected DropPrimaryKeyCommand newDropPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition)
    {
      return new OracleDropPrimaryKeyCommand(currentPrimaryKeyDefinition);
    }
  }
  
  private static class OracleModifyColumnCommand extends ModifyColumnCommand
  {
    private OracleModifyColumnCommand(ISchemaColumnDefinition desiredColumn, ISchemaColumnDefinition currentColumn, boolean modifyType, boolean modifyDefault, boolean modifyRequired, boolean modifyAutoGenerated)
    {
      super(desiredColumn, currentColumn, modifyType, modifyDefault, modifyRequired, modifyAutoGenerated);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.schema.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append("ALTER TABLE ");
      dataSource.appendDBName(buffer, this.table.getDBName());
      buffer.append(" MODIFY(");
      dataSource.appendDBName(buffer, this.desiredColumn.getDBName());
      
      if (this.modifyType)
      {
        String dataType = dataSource.getSqlColumnType(
            this.desiredColumn.getSQLType(dataSource), 
            this.desiredColumn.getSQLSize(dataSource), 
            this.desiredColumn.getSQLDecimalDigits(dataSource));
        buffer.append(" ").append(dataType);
      }
      if (this.modifyDefault)
      {
        String def = this.desiredColumn.getDBDefaultValue(dataSource);
        buffer.append(" DEFAULT ").append(def == null ? "NULL" : escapeDefaultValue(dataSource, this.desiredColumn.getSQLType(dataSource), def));
      }
      if (this.modifyRequired)
      {
        buffer.append(this.desiredColumn.isRequired() ? " NOT NULL" : " NULL");
      }
      
      buffer.append(")");
      
      List result = new ArrayList();
      
      result.add(buffer.toString());
      
      // add inherited statements
      result.addAll(super.getSQLStatements(dataSource));
      
      return result;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.schema.Command#getAlterTableModifyFragment(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public String getAlterTableModifyFragment(SQLDataSource dataSource)
    {
      return null;
    }
  }
  
  private static class OracleAlterTableCommand extends AlterTableCommand
  {
    /**
     * @param tableDefinition
     * @param alterTableCommands
     */
    private OracleAlterTableCommand(ISchemaTableDefinition tableDefinition, List alterTableCommands)
    {
      super(tableDefinition, alterTableCommands);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.AlterTableCommand#doCascadeConstraints()
     */
    protected boolean doCascadeConstraints()
    {
      return true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.AlterTableCommand#useBrackets()
     */
    protected boolean useBrackets()
    {
      return true;
    }
  }
  
  /**
   * Oracle drop table implementation.
   * 
   * @author Andreas Sonntag
   */
  private static class OracleDropTableCommand extends DropTableCommand
  {
    /**
     * @param tableDefinition
     */
    private OracleDropTableCommand(ISchemaTableDefinition tableDefinition)
    {
      super(tableDefinition);
    }

    /**
     * Since Oracle 10 tables have to be purged otherwise dropped tables still
     * exist in the recycle bin.
     * <p>
     * The later makes problems:
     * 
     * <pre>
     *      =================================================
     *      ==== Source Exception ===========================
     *      =================================================
     *      Name: class de.tif.jacob.core.exception.SQLException
     *      Message: Datasource 'joergDatasource': ORA-01490: invalid ANALYZE command
     *      [SQLState:42000][ErrorCode:1490][Statement:analyze table JOERG.BIN$8OPyFa1PT0KwmyXIOS+mew==$0 estimate statistics]
     * 
     *      =================================================
     *      === Extended Stacktrace with CVS version ID =====
     *      =================================================
     *      java.sql.SQLException: ORA-01490: invalid ANALYZE command
     * 
     *      at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:169)
     *      at oracle.jdbc.ttc7.TTIoer.processError(TTIoer.java:208)
     *      at oracle.jdbc.ttc7.Oall7.receive(Oall7.java:543)
     *      at oracle.jdbc.ttc7.TTC7Protocol.doOall7(TTC7Protocol.java:1405)
     *      at oracle.jdbc.ttc7.TTC7Protocol.parseExecuteFetch(TTC7Protocol.java:822)
     *      at oracle.jdbc.driver.OracleStatement.executeNonQuery(OracleStatement.java:1602)
     *      at oracle.jdbc.driver.OracleStatement.doExecuteOther(OracleStatement.java:1527)
     *      at oracle.jdbc.driver.OracleStatement.doExecuteWithTimeout(OracleStatement.java:2045)
     *      at oracle.jdbc.driver.OracleStatement.executeUpdate(OracleStatement.java:752)
     *      at de.tif.jacob.core.data.impl.sql.WrapperStatement.executeUpdate(WrapperStatement.java:331)
     *      at de.tif.jacob.core.data.impl.sql.OracleDataSource$OracleReconfigure.getIndexInfo(OracleDataSource.java:760)  ($Id: OracleDataSource.java,v 1.8 2010-11-11 12:56:03 sonntag Exp $)
     *      at de.tif.jacob.core.data.impl.sql.OracleDataSource$OracleReconfigure.fetchIndexInfo(OracleDataSource.java:723)  ($Id: OracleDataSource.java,v 1.8 2010-11-11 12:56:03 sonntag Exp $)
     *      at de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure.fetchSchemaInformation(Reconfigure.java:1167)  ($Id: OracleDataSource.java,v 1.8 2010-11-11 12:56:03 sonntag Exp $)
     * </pre>
     * 
     * @param dataSource
     * @return
     */
    protected String appendToSQL92DropStatement(SQLDataSource dataSource)
    {
      if (dataSource instanceof AutoDetectSQLDataSource)
        dataSource = ((AutoDetectSQLDataSource) dataSource).getAutoDetectedDataSource();
      
      if (((OracleDataSource) dataSource).getMajorVersionNumber() >= 10)
      {
        return " PURGE";
      }
      return "";
    }
  }
  
  private static class OracleDropColumnCommand extends DropColumnCommand
  {
    /**
     * @param currentColumn
     */
    private OracleDropColumnCommand(ISchemaColumnDefinition currentColumn)
    {
      super(currentColumn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.AlterTableSubCommand#getAlterTableDropFragment(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public String getAlterTableDropFragment(SQLDataSource dataSource)
    {
      // simply return column name
      if (keywords.contains(this.currentColumn.getDBName()))
        return ESCAPE + this.currentColumn.getDBName() + ESCAPE;
      return this.currentColumn.getDBName();
    }
  }

  /**
   * Oracle renew primary key implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   *       ALTER TABLE tbl_name
   *      	  [DROP CONSTRAINT key_name]
   *          ADD(CONSTRAINT key_name PRIMARY KEY(column [, column]))
   * </pre>
   * 
   * @author Andreas Sonntag
   */
  private static class OracleRenewPrimaryKeyCommand extends RenewPrimaryKeyCommand
  {
    /**
     * @param currentPrimaryKeyDefinition
     * @param desiredPrimaryKeyDefinition
     */
    protected OracleRenewPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition, ISchemaKeyDefinition desiredPrimaryKeyDefinition)
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
      {
        buffer.append(" DROP CONSTRAINT ");
        dataSource.appendDBName(buffer, this.currentPrimaryKeyDefinition.getDBName());
        buffer.append(" ");
      }

      buffer.append(" ADD(CONSTRAINT ");
      dataSource.appendDBName(buffer, this.desiredPrimaryKeyDefinition.getDBName());
      buffer.append(" PRIMARY KEY(");
      Iterator columnNameIter = this.desiredPrimaryKeyDefinition.getSchemaColumnNames();
      for (int i = 0; columnNameIter.hasNext(); i++)
      {
        if (i != 0)
          buffer.append(",");
        dataSource.appendDBName(buffer, (String) columnNameIter.next());
      }
      buffer.append("))");

      result.add(buffer.toString());

      return result;
    }
  }
  
  /**
   * Oracle drop primary key implementation.
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
  private static class OracleDropPrimaryKeyCommand extends DropPrimaryKeyCommand
  {
    /**
     * @param currentPrimaryKeyDefinition
     */
    protected OracleDropPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition)
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
