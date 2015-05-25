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
import java.sql.SQLException;
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

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.qbe.QBERelationConstraint;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.schema.Schema;
import de.tif.jacob.core.data.impl.schema.Table;
import de.tif.jacob.core.data.impl.schema.TableColumn;
import de.tif.jacob.core.data.impl.sql.reconfigure.AlterTableCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.AlterTableSubCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.CreateTableCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.ModifyColumnCommand;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.data.impl.sql.reconfigure.ReconfigureWarning;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.schema.ISchemaColumnDefinition;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;
import de.tif.jacob.core.schema.ISchemaRelationDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;

/**
 * HSQL datasource implementation.
 * <p>
 * 
 * Tested with version 1.7.2 and 1.8.10
 * <p>
 * 
 * Specific behaviour:<br>
 * <li> Implicit indices are created for each foreign key constraint. The names
 * of these indices start with <code>"SYS_IDX_"</code> followed by a decimal number.
 * 
 * @author Andreas Sonntag
 */
public class HSQLDataSource extends SQLDataSource
{
	static public transient final String RCS_ID = "$Id: HSQLDataSource.java,v 1.8 2010/07/30 15:53:47 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.8 $";

	/**
	 * Additional HSQL reserved words
	 */
	private static final String[] HSQL_KEYWORDS = {
	  "BEFORE", "BIGINT", "BINARY", "CACHED",
    "CALL", // seems to be a keyword as well
    "DATETIME", "LIMIT", "LONGVARBINARY", "LONGVARCHAR", 
	  "OBJECT", "OTHER", "SAVEPOINT", "TEMP", "TEXT", "TOP", "TRIGGER", "TINYINT", 
	  "VARBINARY", "VARCHAR_IGNORECASE" };
	
	/**
	 * Set containing all keywords and access is done by ignoring case.
	 */
	private static final Set keywords = new TreeSet(String.CASE_INSENSITIVE_ORDER);
  
  private static final String ESCAPE = "\"";
  
  private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
  
  static
  {
    // fill set with keywords
    //
    for (int i = 0; i < SQL92_KEYWORDS.length; i++)
    {
      keywords.add(SQL92_KEYWORDS[i]);
    }
    for (int i = 0; i < HSQL_KEYWORDS.length; i++)
    {
      keywords.add(HSQL_KEYWORDS[i]);
    }
  }
  
  /**
   * Needed for InMemorySQLDataSource!
   * 
   * @param name
   * @param connectString
   * @param driverClassName
   * @param userName
   * @param password
   * @throws Exception
   */
  protected HSQLDataSource(String name, String connectString, String driverClassName, String userName, String password) throws Exception
  {
    super(name, connectString, driverClassName, userName, password);
  }
  
  /**
   * @param parent
   */
  public HSQLDataSource(AutoDetectSQLDataSource parent)
  {
    super(parent);
  }

	/**
	 * @param record
	 * @throws Exception
	 */
	public HSQLDataSource(IDataTableRecord record) throws Exception
	{
	  super(record);
	}
	
	/**
	 * @param dataSourceName
	 */
	public HSQLDataSource(String dataSourceName) throws Exception
	{
		super(dataSourceName);
	}

  protected final String filterConnectString(String s)
  {
    if (s == null)
      return null;
    return StringUtils.replace(s, WEB_APPDIR_REPLACEMENT, Bootstrap.getApplicationRootPath());
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#changePassword(java.lang.String, java.lang.String)
   */
  public final void changePassword(String username, String newPassword) throws SQLException, InvalidNewPasswordException
  {
    throw new UnsupportedOperationException("Change password not supported!");
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getRoles(java.lang.String)
   */
  public final List getRoles(String username) throws SQLException
  {
    throw new UnsupportedOperationException("Getting user roles not supported!");
  }
  
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.sql.SQLDataSource#getLowerFunctionName()
	 */
	public final String getLowerFunctionName()
	{
		return "LOWER";
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#escapeDBName(java.lang.StringBuffer, java.lang.String)
   */
  public final StringBuffer appendDBName(StringBuffer buffer, String dbName)
  {
    // Note: HSQL has a strange behaviour is such a way that for instance a table
    // created with name "mytable" is something else than mytable (without quotes).
    // Therefore, to avoid escaping internal statements like
    // "SELECT nextid FROM jacob_ids WHERE tablename=? FOR UPDATE"
    // we only escape potential keywords.
    
    // the given name is a SQL server keyword?
    if (keywords.contains(dbName))
    {
      return buffer.append(ESCAPE).append(dbName).append(ESCAPE);
    }
    return buffer.append(dbName);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendDBComment(java.lang.StringBuffer, java.lang.String)
   */
  public final StringBuffer appendDBComment(StringBuffer buffer, String comment)
  {
    return buffer.append("/* ").append(comment).append(" */");
  }
  
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.sql.SQLDataSource#needsMappingOfLongText()
	 */
	public final boolean needsMappingOfLongText()
	{
		return false;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#needsMappingOfBinary()
   */
  public final boolean needsMappingOfBinary()
  {
    return false;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer, java.sql.Date)
   */
  public final String toQueryString(Date date)
  {
    synchronized (dateFormat)
		{
			return SQL.QUOTE + dateFormat.format(date) + SQL.QUOTE;
		}
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer, java.sql.Time)
   */
  public final String toQueryString(Time time)
  {
    synchronized (timeFormat)
		{
			return SQL.QUOTE + timeFormat.format(time) + SQL.QUOTE;
		}
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer, java.sql.Timestamp)
   */
  public final String toQueryString(Timestamp timestamp)
  {
    synchronized (timestampFormat)
    {
    	return SQL.QUOTE+timestampFormat.format(timestamp)+SQL.QUOTE;
    }
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#supportsStoredProcedures()
   */
  public final boolean supportsStoredProcedures()
  {
    return false;
  }
  
  public boolean supportsLongTextSearch(int longtextSqlType, boolean casesensitive)
  {
    return true;
  }

  public final boolean supportsAutoKeyGeneration()
  {
    return false;
  }
  
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.sql.SQLDataSource#buildStoredProcedureStatement(java.sql.Connection,
	 *      java.lang.String, int)
	 */
	public final CallableStatement buildStoredProcedureStatement(Connection connection, String procedureName, int argnum) throws SQLException
	{
    throw new UnsupportedOperationException("Stored procedures are not supported");
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
  protected final boolean buildCountDistinct(QBESpecification spec, SQLStatementBuilder sqlBuffer)
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
	public final void appendJoinCondition(SQLStatementBuilder sqlQuery, QBERelationConstraint constraint, ITableField fromField, ITableField toField)
	{
		// should never be called, because we use ANSI joins here
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.sql.SQLDataSource#useAnsiJoins()
	 */
	public final boolean useAnsiJoins()
	{
		return true;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#supportsForUpdate()
   */
  public final boolean supportsForUpdate()
  {
    return false;
  }
  
	public final boolean isUniqueViolationErrorCode(int errorCode)
	{
		// Unique constraint violation: XXX in statement [INSERT INTO ...][SQLState:23000][ErrorCode:-104]
		return errorCode == -104;
	}

  public final boolean isForeignConstraintViolationErrorCode(int errorCode)
  {
    // Integrity constraint violation FK9_SCHWEISSZ_ORDER_ROBOTER_CO table:
    // SCHWEISSZ_ORDER in statement [DELETE FROM "group" WHERE
    // pkey=?][SQLState:23000][ErrorCode:-8]
    return errorCode == -8;
  }

	public final boolean isInvalidUseridPasswordErrorCode(int errorCode)
	{
		// User not found: SAFFF[SQLState:S1000][ErrorCode:-37]
	  // Access is denied[SQLState:S1000][ErrorCode:-33]
		return errorCode == -37 || errorCode == -33;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isClientAbortErrorCode(int)
   */
  public final boolean isClientAbortErrorCode(int errorCode)
  {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isTableNotExistsErrorCode(int)
   */
  public final boolean isTableNotExistsErrorCode(int errorCode)
  {
    // Table not found: ...[SQLState:S0002][ErrorCode:-22]
    return errorCode == -22;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSchemaPrefix()
   */
  public final String getSchemaPrefix()
  {
    return "";
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSQLStatementSeparator()
   */
  public final String getSQLStatementSeparator()
  {
    return "\n\n";
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSqlColumnType(int, int, int)
   */
  public final String getSqlColumnType(int sqlType, int size, int decimalDigits)
  {
    switch (sqlType)
    {
      case Types.BOOLEAN:
      case Types.BIT:
        return "BOOLEAN";
      
      case Types.SMALLINT:
      case Types.INTEGER:
        return "INTEGER";
      
      case Types.BIGINT:
        return "BIGINT";
      
      case Types.DECIMAL:
        // IBIS: check size
        return "DECIMAL(38," + decimalDigits + ")";
      
      case Types.CHAR:
        return "CHAR(" + size + ")";
      
      case Types.VARCHAR:
        return "VARCHAR(" + size + ")";
      
      case Types.LONGVARCHAR:
        return "LONGVARCHAR";
      
      case Types.REAL:
        return "REAL";

      case Types.FLOAT:
        return "FLOAT";
      
      case Types.DOUBLE:
        return "DOUBLE";
      
      case Types.BINARY:
      case Types.VARBINARY:
      case Types.LONGVARBINARY:
        return "LONGVARBINARY";
      
      case Types.TIME:
        return "TIME";
      
      case Types.DATE:
        return "DATE";
      
      case Types.TIMESTAMP:
        return "TIMESTAMP";
    }
    
    throw new RuntimeException("Unsupported SQL type: " + sqlType);
  }

  public final int getDefaultBinarySQLType()
  {
    return Types.LONGVARBINARY;
  }

  public final int getDefaultLongTextSQLType()
  {
    return Types.LONGVARCHAR;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getReconfigureImpl()
   */
  public final Reconfigure getReconfigureImpl()
  {
    return new HSQLReconfigure(this);
  }
  
  /**
   * HSQL-specific reconfigure implementation.
   *  
   * @author Andreas Sonntag
   */
  private static class HSQLReconfigure extends Reconfigure
  {
    /**
     * @param dataSource
     */
    private HSQLReconfigure(SQLDataSource dataSource)
    {
      super(dataSource);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#fetchTableColumn(java.lang.String, java.lang.String, int, java.lang.String, int, int, java.lang.String, boolean)
     */
    protected TableColumn fetchTableColumn(String table_name, String column_name, int data_type, String type_name, int column_size, int decimal_digits,
        String column_default, boolean is_nullable)
    {
      // HSQL JDBC driver does return a (var)char default value with single
      // quotes at the beginning and the end.
      // Example: "'open'".
      //
      if (column_default != null)
      {
        // strip away ' character
        if (column_default.length() > 2 && column_default.charAt(0) == '\'' && column_default.charAt(column_default.length() - 1) == '\'')
        {
          column_default = column_default.substring(1, column_default.length() - 1);
        }
        if (data_type == java.sql.Types.BOOLEAN)
        {
          if ("false".equalsIgnoreCase(column_default))
            column_default = "0";
          else if ("true".equalsIgnoreCase(column_default))
            column_default = "1";
        }
      }

      return super.fetchTableColumn(table_name, column_name, data_type, type_name, column_size, decimal_digits, column_default, is_nullable);
    }
    
    private static final String SYS_IDX_PREFIX = "SYS_IDX_";
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#addTableIndexPart(de.tif.jacob.core.data.impl.schema.Table, java.lang.String, boolean, short, java.lang.String)
     */
    protected void addTableIndexPart(Table table, String indexName, boolean unique, short ordinalPosition, String columnName)
    {
      // skip system indices which are implicitely created for each foreign key constraint!
      // Attention: Unique index names look as follows: SYS_IDX_UK3_ORGANIZATION_ROOTUKEY_4 (Example)
      if (indexName.startsWith(SYS_IDX_PREFIX) && !unique)
        return;
      
      super.addTableIndexPart(table, indexName, unique, ordinalPosition, columnName);
    }
    
    private static boolean equals(ISchemaKeyDefinition key1, ISchemaKeyDefinition key2)
    {
      Iterator iter1 = key1.getSchemaColumnNames();
      Iterator iter2 = key2.getSchemaColumnNames();
      while (true)
      {
        boolean hasNext1 = iter1.hasNext();
        boolean hasNext2 = iter2.hasNext();
        if (hasNext1 && hasNext2)
        {
          String next1 = ((String) iter1.next()).toLowerCase();
          String next2 = ((String) iter2.next()).toLowerCase();
          if (!next1.equals(next2))
            return false;
        }
        else
        {
          return !hasNext1 && !hasNext2;
        }
      }
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#fetchSchemaPostProcessing(java.sql.Connection, de.tif.jacob.core.data.impl.schema.Schema)
     */
    protected void fetchSchemaPostProcessing(Connection connection, Schema schema) throws Exception
    {
      Iterator iter = schema.getTables();
      while (iter.hasNext())
      {
        Table table = (Table) iter.next();
        
        ISchemaKeyDefinition primaryKeyDef = table.getSchemaPrimaryKeyDefinition();
        if (primaryKeyDef !=null)
        {
          Iterator iter2 = table.getSchemaIndexDefinitions();
          while (iter2.hasNext())
          {
            ISchemaKeyDefinition indexDef = (ISchemaKeyDefinition) iter2.next();
            if (indexDef.getDBName().startsWith(SYS_IDX_PREFIX)&& equals(primaryKeyDef, indexDef))
            {
              // Ab HSQL 1.8.x wird ein impliziter Index für jeden Primarykey erzeugt, aber den wollen wir hier nicht haben 
              table.dropIndex(indexDef.getDBName());
              
              // danach aussteigen, da es nur maximal einen geben sollte und ansonsten der Iterator kaput ist
              break;
            }
          }
        }
      }
      
      // Default handling
      super.fetchSchemaPostProcessing(connection, schema);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newCreateTableCommand(de.tif.jacob.core.schema.ISchemaTableDefinition)
     */
    protected CreateTableCommand newCreateTableCommand(ISchemaTableDefinition tableDefinition)
    {
      return new HSQLCreateTableCommand(tableDefinition);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newAlterTableCommand(de.tif.jacob.core.schema.ISchemaTableDefinition, java.util.List)
     */
    protected AlterTableCommand newAlterTableCommand(ISchemaTableDefinition tableDefinition, List alterTableCommands)
    {
      return new HSQLAlterTableCommand(tableDefinition, alterTableCommands);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#newModifyColumnCommand(de.tif.jacob.core.schema.ISchemaColumnDefinition, de.tif.jacob.core.schema.ISchemaColumnDefinition, boolean, boolean, boolean)
     */
    protected ModifyColumnCommand newModifyColumnCommand(ISchemaColumnDefinition desiredColumn, ISchemaColumnDefinition currentColumn, boolean modifyType,
        boolean modifyDefault, boolean modifyRequired, boolean modifyAutoGenerated)
    {
      return new HSQLModifyColumnCommand(desiredColumn, currentColumn, modifyType, modifyDefault, modifyRequired);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure#dropIndex(de.tif.jacob.core.schema.ISchemaKeyDefinition, de.tif.jacob.core.schema.ISchemaTableDefinition)
     */
    protected boolean dropIndex(ISchemaKeyDefinition currentIndex, ISchemaTableDefinition currentTableDefinition, ISchemaTableDefinition desiredTableDefinition)
    {
      // HSQL always creates an index for each foreign key relation
      // Therefore, ignore these indices, which will implicitly be dropped
      // if the relation is dropped.
      //
      Iterator iter = currentTableDefinition.getSchemaRelationDefinitions();
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
  }
  
  /**
   * @author Andreas
   *
   * To change the template for this generated type comment go to
   * Window - Preferences - Java - Code Generation - Code and Comments
   */
  private static class HSQLCreateTableCommand extends CreateTableCommand
  {
    /**
     * @param tableDefinition
     */
    private HSQLCreateTableCommand(ISchemaTableDefinition tableDefinition)
    {
      super(tableDefinition);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.CreateTableCommand#addResetPrivilegesStatements(java.util.List)
     */
// Verhalten hat sich mit aktueller HSQL geändert
// Siehe Qualitymaster Bug: 506
// http://hsqldb.org/doc/guide/ch09.html#revoke-section
//    
//    protected void addResetPrivilegesStatements(SQLDataSource dataSource, List statements)
//    {
//      // Attention: HSQL uses "... TO PUBLIC" instead of "... FROM PUBLIC" 
//      StringBuffer buffer = new StringBuffer();
//      buffer.append("REVOKE ALL ON ");
//      dataSource.appendDBName(buffer, this.tableDefinition.getDBName());
//      buffer.append(" TO PUBLIC");
//      statements.add(buffer.toString());
//      
//      buffer = new StringBuffer();
//      buffer.append("GRANT SELECT, UPDATE, INSERT, DELETE ON ");
//      dataSource.appendDBName(buffer, this.tableDefinition.getDBName());
//      buffer.append(" TO PUBLIC");
//      statements.add(buffer.toString());
//    }
  }
  
  /**
   * TODO To change the template for this generated type comment go to
   * Window - Preferences - Java - Code Style - Code Templates
   * 
   * @author Andreas Sonntag
   */
  private static class HSQLAlterTableCommand extends AlterTableCommand
  {
    /**
     * @param tableDefinition
     * @param alterTableCommands
     */
    protected HSQLAlterTableCommand(ISchemaTableDefinition tableDefinition, List alterTableCommands)
    {
      super(tableDefinition, alterTableCommands);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.sql.reconfigure.AlterTableCommand#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List statements = new ArrayList();

      // scan alter table subcommands
      for (int i=0; i < this.alterTableCommands.size(); i++)
      {
        AlterTableSubCommand command = (AlterTableSubCommand) this.alterTableCommands.get(i);
        
        setTable(command);
        
        // check for alter add fragments
        String addFragment = command.getAlterTableAddFragment(dataSource);
        if (addFragment != null)
        {
          StringBuffer alterTableBuffer = new StringBuffer();
          alterTableBuffer.append("ALTER TABLE ");
          dataSource.appendDBName(alterTableBuffer, this.tableDefinition.getDBName());
          alterTableBuffer.append(" ADD ").append(addFragment);
          statements.add(alterTableBuffer.toString());
        }
        
        // check for alter modify fragments
        String modifyFragment = command.getAlterTableModifyFragment(dataSource);
        if (modifyFragment != null)
        {
          StringBuffer alterTableBuffer = new StringBuffer();
          alterTableBuffer.append("ALTER TABLE ");
          dataSource.appendDBName(alterTableBuffer, this.tableDefinition.getDBName());
          alterTableBuffer.append(" ALTER COLUMN ").append(modifyFragment);
          statements.add(0, alterTableBuffer.toString());
        }
        
        // check for alter drop fragments
        String dropFragment = command.getAlterTableDropFragment(dataSource);
        if (dropFragment != null)
        {
          StringBuffer alterTableBuffer = new StringBuffer();
          alterTableBuffer.append("ALTER TABLE ");
          dataSource.appendDBName(alterTableBuffer, this.tableDefinition.getDBName());
          alterTableBuffer.append(" DROP ").append(dropFragment);
          statements.add(alterTableBuffer.toString());
        }
        
        // check for sub statements
        statements.addAll(command.getSQLStatements(dataSource));
      }
      
      return statements;
    }
  }

  /**
   * HSQL modify column implementation.
   * <p>
   * The following syntax is used:
   * 
   * <pre>
   *   ALTER TABLE tablename ALTER COLUMN columnname {DROP DEFAULT | SET DEFAULT defaultvalue} 
   * </pre>
   * 
   * <p>
   * Attention : Modifying NULL/NOT_NULL and column type is not supported!?
   * 
   * 
   * @author Andreas Sonntag
   */
  private static class HSQLModifyColumnCommand extends ModifyColumnCommand
  {
    private HSQLModifyColumnCommand(ISchemaColumnDefinition desiredColumn, ISchemaColumnDefinition currentColumn, boolean modifyType, boolean modifyDefault,
        boolean modifyRequired)
    {
      super(desiredColumn, currentColumn, modifyType, modifyDefault, modifyRequired, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.schema.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
     */
    public List getSQLStatements(SQLDataSource dataSource)
    {
      List result = new ArrayList();

      if (this.modifyType || this.modifyRequired)
      {
        // IBIS: Todo not supported
        StringBuffer buffer = new StringBuffer();
        buffer.append("Can not modify ");
        if (this.modifyType)
        {
          buffer.append("type");
          if (this.modifyRequired)
          {
            buffer.append(" and ");
          }
        }
        if (this.modifyRequired)
        {
          buffer.append("NULL/NOT_NULL");
        }
        buffer.append(" of column ");
        buffer.append(this.desiredColumn.getDBName());
        buffer.append(" of table ");
        buffer.append(this.desiredColumn.getDBTableName());
        result.add(new ReconfigureWarning(buffer.toString()));
      }

      if (this.modifyDefault)
      {
        StringBuffer buffer = new StringBuffer();
        if (this.desiredColumn.getDBDefaultValue(dataSource) != null)
        {
          buffer.append("ALTER TABLE ");
          dataSource.appendDBName(buffer, this.table.getDBName());
          buffer.append(" ALTER COLUMN ");
          dataSource.appendDBName(buffer, this.desiredColumn.getDBName());
          buffer.append(" SET DEFAULT ");
          buffer.append(escapeDefaultValue(dataSource, this.desiredColumn.getSQLType(dataSource), this.desiredColumn.getDBDefaultValue(dataSource)));
        }
        else
        {
          buffer.append("ALTER TABLE ");
          dataSource.appendDBName(buffer, this.table.getDBName());
          buffer.append(" ALTER COLUMN ");
          dataSource.appendDBName(buffer, this.desiredColumn.getDBName());
          buffer.append(" DROP DEFAULT");
        }
        result.add(buffer.toString());
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
}
