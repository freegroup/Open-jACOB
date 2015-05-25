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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WrapperConnection implements Connection
{
  static public transient final String RCS_ID = "$Id: WrapperConnection.java,v 1.3 2010/11/18 11:24:42 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";
  
  private Connection embedded;
  private final SQLDataSource dataSource;
  private boolean postpone = false;

	protected WrapperConnection(SQLDataSource dataSource, Connection embedded) throws SQLException
	{
    // we need transaction control, i.e. switch off autocommit
	  if (embedded.getAutoCommit())
	    embedded.setAutoCommit(false);
	  
	  // check transaction isolation
	  // Note: we do not like repeatable reads
	  //
	  switch (embedded.getTransactionIsolation())
    {
//      case Connection.TRANSACTION_READ_UNCOMMITTED:
      case Connection.TRANSACTION_REPEATABLE_READ:
      case Connection.TRANSACTION_SERIALIZABLE:
        // prevent dirty reads, but no repeatable reads
        try
        {
          embedded.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        }
        catch (SQLException ex)
        {
          // just in case this is not supported
        }

        // make sure to clear read cache
        embedded.rollback();
        break;
    }
    
    this.embedded = embedded;
    this.dataSource = dataSource;
	  
	  synchronized(this.dataSource)
	  {
	    dataSource.internalConnections++;
	  }
	}
	
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.InternalConnection#setPostpone(boolean)
   */
  public void setPostpone(boolean postpone)
  {
    this.postpone = postpone;
  }
  
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#close()
	 */
	public void close() throws SQLException
	{
		// already closed?
		if (this.embedded == null)
			return;

    if (this.dataSource.isJNDI())
    {
      // Do rollback to handle WebSphere problem.
      try
      {
        if (!this.embedded.isReadOnly() && !this.embedded.getAutoCommit())
          this.embedded.rollback();
        this.embedded.clearWarnings();
      }
      catch (Exception ex)
      {
        if (SQL.logger.isWarnEnabled())
          SQL.logger.warn("Rollback or clear warnings failed", ex);
      }
    }
    
		this.embedded.close();
		this.embedded = null;
		
	  synchronized(this.dataSource)
	  {
	    dataSource.internalConnections--;
	  }
	}

	private void checkClosed() throws SQLException
	{
		if (this.embedded == null)
			throw new SQLException("Connection is closed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#clearWarnings()
	 */
	public void clearWarnings() throws SQLException
	{
		checkClosed();
		this.embedded.clearWarnings();
	}

	
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException
  {
    checkClosed();
    return embedded.createArrayOf(typeName, elements);
  }

  public Blob createBlob() throws SQLException
  {
    checkClosed();
    return embedded.createBlob();
  }

  public Clob createClob() throws SQLException
  {
    checkClosed();
    return embedded.createClob();
  }

  public NClob createNClob() throws SQLException
  {
    checkClosed();
    return embedded.createNClob();
  }

  public SQLXML createSQLXML() throws SQLException
  {
    checkClosed();
    return embedded.createSQLXML();
  }

  public Struct createStruct(String typeName, Object[] attributes) throws SQLException
  {
    checkClosed();
    return embedded.createStruct(typeName, attributes);
  }

  public Properties getClientInfo() throws SQLException
  {
    checkClosed();
    return embedded.getClientInfo();
  }

  public String getClientInfo(String name) throws SQLException
  {
    checkClosed();
    return embedded.getClientInfo(name);
  }

  public boolean isValid(int timeout) throws SQLException
  {
    checkClosed();
    return embedded.isValid(timeout);
  }

  public void setClientInfo(Properties properties) throws SQLClientInfoException
  {
    embedded.setClientInfo(properties);
  }

  public void setClientInfo(String name, String value) throws SQLClientInfoException
  {
    embedded.setClientInfo(name, value);
  }

  public boolean isWrapperFor(Class< ? > iface) throws SQLException
  {
    checkClosed();
    return embedded.isWrapperFor(iface);
  }

  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    checkClosed();
    return embedded.unwrap(iface);
  }

  /*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#commit()
	 */
	public void commit() throws SQLException
	{
		checkClosed();
		this.embedded.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createStatement()
	 */
	public Statement createStatement() throws SQLException
	{
		checkClosed();
		return new WrapperStatement(this, this.embedded.createStatement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
	{
		checkClosed();
		return new WrapperStatement(this, this.embedded.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
	{
		checkClosed();
		return new WrapperStatement(this, this.embedded.createStatement(resultSetType, resultSetConcurrency));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getAutoCommit()
	 */
	public boolean getAutoCommit() throws SQLException
	{
		checkClosed();
		return this.embedded.getAutoCommit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getCatalog()
	 */
	public String getCatalog() throws SQLException
	{
		checkClosed();
		return this.embedded.getCatalog();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getHoldability()
	 */
	public int getHoldability() throws SQLException
	{
		checkClosed();
		return this.embedded.getHoldability();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getMetaData()
	 */
	public DatabaseMetaData getMetaData() throws SQLException
	{
		checkClosed();
		return this.embedded.getMetaData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	public int getTransactionIsolation() throws SQLException
	{
		checkClosed();
		return this.embedded.getTransactionIsolation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getTypeMap()
	 */
	public Map getTypeMap() throws SQLException
	{
		checkClosed();
		return this.embedded.getTypeMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException
	{
		checkClosed();
		return this.embedded.getWarnings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#isClosed()
	 */
	public boolean isClosed() throws SQLException
	{
		return this.embedded == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#isReadOnly()
	 */
	public boolean isReadOnly() throws SQLException
	{
		checkClosed();
		return this.embedded.isReadOnly();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
	public String nativeSQL(String sql) throws SQLException
	{
		checkClosed();
		return this.embedded.nativeSQL(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
	{
		checkClosed();
		return new WrapperCallableStatement(this, this.embedded.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
	{
		checkClosed();
		return new WrapperCallableStatement(this, this.embedded.prepareCall(sql, resultSetType, resultSetConcurrency), sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	public CallableStatement prepareCall(String sql) throws SQLException
	{
		checkClosed();
		return new WrapperCallableStatement(this, this.embedded.prepareCall(sql), sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
	 */
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
	{
		checkClosed();
		return new WrapperPreparedStatement(this, this.embedded.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
	{
		checkClosed();
		return new WrapperPreparedStatement(this, this.embedded.prepareStatement(sql, resultSetType, resultSetConcurrency), sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
	{
		checkClosed();
		return new WrapperPreparedStatement(this, this.embedded.prepareStatement(sql, autoGeneratedKeys), sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
	{
		checkClosed();
		return new WrapperPreparedStatement(this, this.embedded.prepareStatement(sql, columnIndexes), sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String,
	 *      java.lang.String[])
	 */
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
	{
		checkClosed();
		return new WrapperPreparedStatement(this, this.embedded.prepareStatement(sql, columnNames), sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException
	{
		checkClosed();
		
    try
    {
  		// Note: the postpone feature is only needed internally for databases such like MS SQL Server
  		PreparedStatement embedded = this.postpone ? new PostponedPreparedStatement(this.embedded, sql) : this.embedded.prepareStatement(sql);
  		return new WrapperPreparedStatement(this, embedded, sql);
    }
    catch (de.tif.jacob.core.exception.SQLException e)
    {
      throw e;
    }
    catch (SQLException e)
    {
      // rethrow the exception and add the SQL statement to the exception
      throw new de.tif.jacob.core.exception.SQLException(getDataSource(), e, sql);
    }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException
	{
		checkClosed();
		this.releaseSavepoint(savepoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#rollback()
	 */
	public void rollback() throws SQLException
	{
		checkClosed();
		this.embedded.rollback();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	 */
	public void rollback(Savepoint savepoint) throws SQLException
	{
		checkClosed();
		this.embedded.rollback(savepoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	public void setAutoCommit(boolean autoCommit) throws SQLException
	{
		checkClosed();
		this.embedded.setAutoCommit(autoCommit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setCatalog(java.lang.String)
	 */
	public void setCatalog(String catalog) throws SQLException
	{
		checkClosed();
		this.embedded.setCatalog(catalog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setHoldability(int)
	 */
	public void setHoldability(int holdability) throws SQLException
	{
		checkClosed();
		this.embedded.setHoldability(holdability);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly) throws SQLException
	{
		checkClosed();
		this.embedded.setReadOnly(readOnly);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setSavepoint()
	 */
	public Savepoint setSavepoint() throws SQLException
	{
		checkClosed();
		return this.embedded.setSavepoint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	 */
	public Savepoint setSavepoint(String name) throws SQLException
	{
		checkClosed();
		return this.embedded.setSavepoint(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	public void setTransactionIsolation(int level) throws SQLException
	{
		checkClosed();
		this.embedded.setTransactionIsolation(level);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setTypeMap(java.util.Map)
	 */
	public void setTypeMap(Map map) throws SQLException
	{
		checkClosed();
		this.embedded.setTypeMap(map);
	}

	/**
	 * @return Returns the dataSource.
	 */
	protected SQLDataSource getDataSource()
	{
		return dataSource;
	}

}
