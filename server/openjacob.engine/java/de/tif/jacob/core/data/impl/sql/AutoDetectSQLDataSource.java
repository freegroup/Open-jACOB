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
/*
 * Created on 29.08.2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data.impl.sql;

import java.lang.reflect.Constructor;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.Service;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.DataRecordSet;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.DataSourceProvider;
import de.tif.jacob.core.data.impl.qbe.QBERelationConstraint;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.exception.UnavailableDatasourceException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.core.schema.ISchemaDefinition;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public final class AutoDetectSQLDataSource extends SQLDataSource
{
  static public transient final String RCS_ID = "$Id: AutoDetectSQLDataSource.java,v 1.9 2009/09/16 10:48:50 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.9 $";
  
  private final static Log logger = LogFactory.getLog(DataSource.class);
  
  private final static Class DATASOURCE_BY_AUTODETECT_ARGS[] = { AutoDetectSQLDataSource.class };
  
	private SQLDataSource autoDetectedDataSource;

	/**
	 * @param record
	 * @throws Exception
	 */
	public AutoDetectSQLDataSource(IDataTableRecord record) throws Exception
	{
	  super(record);
	}
	
	/**
	 * @param dataSourceName
	 * @throws Exception
	 */
	public AutoDetectSQLDataSource(String dataSourceName) throws Exception
	{
		super(dataSourceName);
	}

	private SQLDataSource determineDataSource()
  {
    try
    {
      Connection connection = getConnection();
      try
      {
        // get meta information and detect database
        DatabaseMetaData meta = connection.getMetaData();
        String databaseProductName = meta.getDatabaseProductName();

        Iterator iter = Service.providers(DataSourceProvider.class);
        while (iter.hasNext())
        {
          DataSourceProvider provider = (DataSourceProvider) iter.next();
          Class clazz = provider.getDataSourceClassByMeta(meta);
          if (clazz != null)
          {
            logger.info("Instantiating auto detected datasource class " + clazz.getName() + " ..");
            Constructor constructor = clazz.getConstructor(DATASOURCE_BY_AUTODETECT_ARGS);
            return (SQLDataSource) constructor.newInstance(new Object[] { this });
          }
        }

        if (logger.isWarnEnabled())
          logger.warn("No provider for database product '" + databaseProductName + "' found");

        throw new UnavailableDatasourceException(getName(), databaseProductName);
      }
      finally
      {
        connection.close();
      }
    }
    catch (UserRuntimeException ex)
    {
      // nicht den Exceptiontyp ändern, wegen UnavailableDatasourceException, UndefinedDatasourceException, etc.
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException("Auto detecting of data source '" + getName() + "' failed", ex);
    }
  }

	/**
	 * @return Returns the autoDetectedDataSource.
	 */
	protected synchronized SQLDataSource getAutoDetectedDataSource()
	{
	  if (this.autoDetectedDataSource == null)
			this.autoDetectedDataSource = determineDataSource();
		return autoDetectedDataSource;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#isTypeOf(java.lang.Class)
   */
  public boolean isTypeOf(Class databaseClassImplementation)
  {
    return getAutoDetectedDataSource().isTypeOf(databaseClassImplementation);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#isTransient()
   */
  public boolean isTransient()
  {
    return getAutoDetectedDataSource().isTransient();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#convertToSQL(java.lang.String, boolean)
   */
  public synchronized String convertToSQL(String input, boolean doQuoting)
  {
    // Plausibility check to avoid an endless recursion
    if (null == this.autoDetectedDataSource)
    {
      throw new IllegalStateException();
    }
    return this.autoDetectedDataSource.convertToSQL(input, doQuoting);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#changePassword(java.lang.String, java.lang.String)
   */
  public void changePassword(String username, String newPassword) throws SQLException, InvalidNewPasswordException
  {
    getAutoDetectedDataSource().changePassword(username, newPassword);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getRoles(java.lang.String)
   */
  public List getRoles(String username) throws SQLException
  {
    return getAutoDetectedDataSource().getRoles(username);
  }
  
	public String getLowerFunctionName()
	{
		return getAutoDetectedDataSource().getLowerFunctionName();
	}

  public boolean supportsLowerFunctionForLongText()
  {
    return getAutoDetectedDataSource().supportsLowerFunctionForLongText();
  }

	public boolean needsMappingOfLongText()
	{
		return getAutoDetectedDataSource().needsMappingOfLongText();
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#needsMappingOfBinary()
   */
  public boolean needsMappingOfBinary()
  {
		return getAutoDetectedDataSource().needsMappingOfBinary();
  }
  
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer,
	 *      java.sql.Timestamp)
	 */
	public String toQueryString(Timestamp timestamp)
	{
		return getAutoDetectedDataSource().toQueryString(timestamp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer,
	 *      java.sql.Date)
	 */
	public String toQueryString(Date date)
	{
		return getAutoDetectedDataSource().toQueryString(date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendToQuery(java.lang.StringBuffer,
	 *      java.sql.Time)
	 */
	public String toQueryString(Time time)
	{
		return getAutoDetectedDataSource().toQueryString(time);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#supportsForeignKeyConstraints()
   */
  public boolean supportsForeignKeyConstraints()
  {
		return getAutoDetectedDataSource().supportsForeignKeyConstraints();
  }
  
  public boolean supportsStoredProcedures()
  {
		return getAutoDetectedDataSource().supportsStoredProcedures();
  }
  
  public boolean supportsLongTextSearch(int longtextSqlType, boolean casesensitive)
  {
    return getAutoDetectedDataSource().supportsLongTextSearch(longtextSqlType, casesensitive);
  }

  public StringBuffer appendDBName(StringBuffer buffer, String dbName)
  {
    return getAutoDetectedDataSource().appendDBName(buffer, dbName);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendDBComment(java.lang.StringBuffer, java.lang.String)
   */
  public StringBuffer appendDBComment(StringBuffer buffer, String comment)
  {
    return getAutoDetectedDataSource().appendDBComment(buffer, comment);
  }
  
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#buildStoredProcedureStatement(java.sql.Connection,
	 *      java.lang.String, int)
	 */
	public CallableStatement buildStoredProcedureStatement(Connection connection, String procedureName, int argnum) throws SQLException
	{
		return getAutoDetectedDataSource().buildStoredProcedureStatement(connection, procedureName, argnum);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendJoinCondition(de.tif.jacob.core.data.impl.sql.SQLStatementBuilder, de.tif.jacob.core.data.impl.qbe.QBERelationConstraint, de.tif.jacob.core.definition.ITableField, de.tif.jacob.core.definition.ITableField)
	 */
	public void appendJoinCondition(SQLStatementBuilder sqlQuery, QBERelationConstraint constraint, ITableField fromField, ITableField toField)
	{
		getAutoDetectedDataSource().appendJoinCondition(sqlQuery, constraint, fromField, toField);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#useAnsiJoins()
	 */
	public boolean useAnsiJoins()
	{
		return getAutoDetectedDataSource().useAnsiJoins();
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#supportsForUpdate()
   */
  public boolean supportsForUpdate()
  {
		return getAutoDetectedDataSource().supportsForUpdate();
  }
  
	/* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#supportsMultipleNullsForUniqueIndices()
   */
  public boolean supportsMultipleNullsForUniqueIndices()
  {
    return getAutoDetectedDataSource().supportsMultipleNullsForUniqueIndices();
  }

  protected boolean supportsSelectDistinct(ITableDefinition table)
  {
    return getAutoDetectedDataSource().supportsSelectDistinct(table);
  }

  public boolean isUniqueViolationErrorCode(int errorCode)
	{
		return getAutoDetectedDataSource().isUniqueViolationErrorCode(errorCode);
	}

  public boolean isForeignConstraintViolationErrorCode(int errorCode)
  {
    return getAutoDetectedDataSource().isForeignConstraintViolationErrorCode(errorCode);
  }

  /* (non-Javadoc)
	 * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isClientAbortErrorCode(int)
	 */
	public synchronized boolean isClientAbortErrorCode(int errorCode)
  {
    // Note: Avoid calling <code>getAutoDetectedDataSource()</code> here,
    // because
    // this method is called in the constructor of
    // <code>de.tif.jacob.core.data.impl.sql.SQLException</code>
    // and might otherwise lead to an endless recursion!!!
    if (null == this.autoDetectedDataSource)
    {
      return false;
    }
    return this.autoDetectedDataSource.isClientAbortErrorCode(errorCode);
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isInvalidUseridPasswordErrorCode(int)
	 */
	public boolean isInvalidUseridPasswordErrorCode(int errorCode)
	{
		return getAutoDetectedDataSource().isInvalidUseridPasswordErrorCode(errorCode);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#isTableNotExistsErrorCode(int)
   */
  public boolean isTableNotExistsErrorCode(int errorCode)
  {
		return getAutoDetectedDataSource().isTableNotExistsErrorCode(errorCode);
  }
  
	public long newJacobIds(ITableDefinition table, ITableField field, int increment) throws SQLException
	{
		return getAutoDetectedDataSource().newJacobIds(table, field, increment);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#setNextJacobId(de.tif.jacob.core.definition.ITableDefinition, de.tif.jacob.core.definition.ITableField, long)
   */
  public boolean setNextJacobId(ITableDefinition table, ITableField field, long nextId) throws SQLException
  {
    return getAutoDetectedDataSource().setNextJacobId(table, field, nextId);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#supportsIdGeneration()
   */
  public boolean supportsAutoKeyGeneration()
  {
		return getAutoDetectedDataSource().supportsAutoKeyGeneration();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#beforeExecuteCount(de.tif.jacob.core.data.impl.sql.SQLStatementBuilder)
   */
  protected void beforeExecuteCount(SQLStatementBuilder queryStatement) throws SQLException
  {
    getAutoDetectedDataSource().beforeExecuteCount(queryStatement);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#beforeExecuteQuery(de.tif.jacob.core.data.impl.DataRecordSet, de.tif.jacob.core.data.impl.qbe.QBESpecification, de.tif.jacob.core.data.impl.sql.SQLStatementBuilder, de.tif.jacob.core.data.event.DataTableRecordEventHandler)
   */
  protected void beforeExecuteQuery(DataRecordSet recordSet, QBESpecification spec, SQLStatementBuilder queryStatement, DataTableRecordEventHandler eventHandler) throws SQLException
  {
    getAutoDetectedDataSource().beforeExecuteQuery(recordSet, spec, queryStatement, eventHandler);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#buildCountDistinct(de.tif.jacob.core.data.impl.qbe.QBESpecification, de.tif.jacob.core.data.impl.sql.SQLStatementBuilder)
   */
  protected boolean buildCountDistinct(QBESpecification spec, SQLStatementBuilder sqlBuffer)
  {
    return getAutoDetectedDataSource().buildCountDistinct(spec, sqlBuffer);
  }

  /*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#appendInverseJoinCondition(java.lang.StringBuffer,
	 *      de.tif.jacob.core.data.impl.qbe.QBERelationConstraint)
	 */
	public void appendInverseJoinCondition(SQLStatementBuilder sqlQuery, QBERelationConstraint constraint)
	{
		getAutoDetectedDataSource().appendInverseJoinCondition(sqlQuery, constraint);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#setupAsJacobDatasource(de.tif.jacob.core.schema.ISchemaDefinition)
   */
	protected void setupAsJacobDatasource(ISchemaDefinition currentSchema) throws Exception
	{
		getAutoDetectedDataSource().setupAsJacobDatasource(currentSchema);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#setupAsQuintusDatasource(de.tif.jacob.core.schema.ISchemaDefinition)
   */
  protected void setupAsQuintusDatasource(ISchemaDefinition currentSchema) throws Exception
  {
		getAutoDetectedDataSource().setupAsQuintusDatasource(currentSchema);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSchemaPrefix()
   */
  public String getSchemaPrefix()
  {
    return getAutoDetectedDataSource().getSchemaPrefix();
  }
  
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSqlColumnType(int, int, int)
   */
  public String getSqlColumnType(int sqlType, int size, int decimalDigits)
  {
    return getAutoDetectedDataSource().getSqlColumnType(sqlType, size, decimalDigits);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getSQLStatementSeparator()
   */
  public String getSQLStatementSeparator()
  {
    return getAutoDetectedDataSource().getSQLStatementSeparator();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#getReconfigureImpl()
   */
  public Reconfigure getReconfigureImpl()
  {
    return getAutoDetectedDataSource().getReconfigureImpl();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#needsPostpone()
   */
  protected boolean needsPostpone()
  {
    return getAutoDetectedDataSource().needsPostpone();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.ISQLDataSource#getDefaultBinarySQLType()
   */
  public int getDefaultBinarySQLType()
  {
    return getAutoDetectedDataSource().getDefaultBinarySQLType();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.ISQLDataSource#getDefaultLongTextSQLType()
   */
  public int getDefaultLongTextSQLType()
  {
    return getAutoDetectedDataSource().getDefaultLongTextSQLType();
  }
}
