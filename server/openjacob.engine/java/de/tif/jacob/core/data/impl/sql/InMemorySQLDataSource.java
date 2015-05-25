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
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.util.container.BlockingQueue;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class InMemorySQLDataSource extends HSQLDataSource
{
  static public transient final String RCS_ID = "$Id: InMemorySQLDataSource.java,v 1.4 2010/11/18 11:24:42 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.4 $";

  static private final transient Log logger = LogFactory.getLog(SQLDataSource.class);

  private final BlockingQueue connectionQueue;
  
  // Map{ITableFieldDefinition->NextId}
  private final Map idMap;
  
	/**
	 * @param dataSourceName
	 * @throws SQLException
	 */
	public InMemorySQLDataSource(String dataSourceName) throws Exception
	{
	  // use datasource name in connect string as database name
		super(dataSourceName, "jdbc:hsqldb:mem:"+dataSourceName, "org.hsqldb.jdbcDriver", "sa", "");
    
    // get one connection which will never be closed and put it in the queue
    this.connectionQueue = new BlockingQueue(1);
    this.connectionQueue.add(getInitialConnection());
    
    this.idMap = new HashMap();
	}

	/* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#isTransient()
   */
  public boolean isTransient()
  {
    return true;
  }

  private Connection getInitialConnection() throws SQLException
	{
	  initConnectivity();
	  
	  return DriverManager.getConnection(getConnectString(), "sa", "");
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#createConnection()
	 */
	protected Connection createConnection() throws SQLException
  {
	  checkDestroyed();
	  
    return new QueuedConnection((Connection) this.connectionQueue.remove());
  }
  
  private class QueuedConnection implements Connection
  {
    private Connection embedded;
    
    private QueuedConnection(Connection embedded)
    {
      this.embedded = embedded;
    }
    
    /* (non-Javadoc)
     * @see java.sql.Connection#close()
     */
    public void close() throws SQLException
    {
    	// already closed?
    	if (this.embedded == null)
    		return;
    	
      if (!connectionQueue.isEmpty())
        throw new RuntimeException("Fatal error: connection queue not empty");
      
      connectionQueue.add(this.embedded);
      this.embedded = null;
    }

		/* (non-Javadoc)
		 * @see java.sql.Connection#clearWarnings()
		 */
		public void clearWarnings() throws SQLException
		{
      this.embedded.clearWarnings();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#commit()
		 */
		public void commit() throws SQLException
		{
      this.embedded.commit();
    }

		/* (non-Javadoc)
		 * @see java.sql.Connection#createStatement()
		 */
		public Statement createStatement() throws SQLException
		{
			return this.embedded.createStatement();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createStatement(int, int, int)
		 */
		public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
		{
			return this.embedded.createStatement(resultSetType,  resultSetConcurrency,  resultSetHoldability);
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createStatement(int, int)
		 */
		public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
		{
			return this.embedded.createStatement(resultSetType,  resultSetConcurrency);
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getAutoCommit()
		 */
		public boolean getAutoCommit() throws SQLException
		{
			return this.embedded.getAutoCommit();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getCatalog()
		 */
		public String getCatalog() throws SQLException
		{
			return this.embedded.getCatalog();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getHoldability()
		 */
		public int getHoldability() throws SQLException
		{
			return this.embedded.getHoldability();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getMetaData()
		 */
		public DatabaseMetaData getMetaData() throws SQLException
		{
			return this.embedded.getMetaData();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getTransactionIsolation()
		 */
		public int getTransactionIsolation() throws SQLException
		{
			return this.embedded.getTransactionIsolation();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getTypeMap()
		 */
		public Map getTypeMap() throws SQLException
		{
			return this.embedded.getTypeMap();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getWarnings()
		 */
		public SQLWarning getWarnings() throws SQLException
		{
			return this.embedded.getWarnings();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#isClosed()
		 */
		public boolean isClosed() throws SQLException
		{
			return this.embedded == null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#isReadOnly()
		 */
		public boolean isReadOnly() throws SQLException
		{
			return this.embedded.isReadOnly();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#nativeSQL(java.lang.String)
		 */
		public String nativeSQL(String sql) throws SQLException
		{
			return this.embedded.nativeSQL(sql);
		}

		public Array createArrayOf(String typeName, Object[] elements) throws SQLException
    {
      return embedded.createArrayOf(typeName, elements);
    }

    public Blob createBlob() throws SQLException
    {
      return embedded.createBlob();
    }

    public Clob createClob() throws SQLException
    {
      return embedded.createClob();
    }

    public NClob createNClob() throws SQLException
    {
      return embedded.createNClob();
    }

    public SQLXML createSQLXML() throws SQLException
    {
      return embedded.createSQLXML();
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException
    {
      return embedded.createStruct(typeName, attributes);
    }

    public Properties getClientInfo() throws SQLException
    {
      return embedded.getClientInfo();
    }

    public String getClientInfo(String name) throws SQLException
    {
      return embedded.getClientInfo(name);
    }

    public boolean isValid(int timeout) throws SQLException
    {
      return embedded.isValid(timeout);
    }

    public boolean isWrapperFor(Class< ? > iface) throws SQLException
    {
      return embedded.isWrapperFor(iface);
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException
    {
      embedded.setClientInfo(properties);
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException
    {
      embedded.setClientInfo(name, value);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException
    {
      return embedded.unwrap(iface);
    }

    /* (non-Javadoc)
		 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
		 */
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
		{
			return this.embedded.prepareCall( sql,  resultSetType,  resultSetConcurrency,  resultSetHoldability);
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
		 */
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
		{
      return this.embedded.prepareCall( sql,  resultSetType,  resultSetConcurrency);
    }

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareCall(java.lang.String)
		 */
		public CallableStatement prepareCall(String sql) throws SQLException
		{
      return this.embedded.prepareCall( sql);
    }

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
		 */
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
		{
			return this.embedded.prepareStatement( sql,  resultSetType,  resultSetConcurrency,  resultSetHoldability);
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
		 */
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
		{
      return this.embedded.prepareStatement( sql,  resultSetType,  resultSetConcurrency);
    }

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
		 */
		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
		{
      return this.embedded.prepareStatement( sql,  autoGeneratedKeys);
    }

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
		 */
		public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
		{
      return this.embedded.prepareStatement( sql,  columnIndexes);
    }

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
		 */
		public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
		{
      return this.embedded.prepareStatement( sql,  columnNames);
    }

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String)
		 */
		public PreparedStatement prepareStatement(String sql) throws SQLException
		{
      return this.embedded.prepareStatement( sql);
    }

		/* (non-Javadoc)
		 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
		 */
		public void releaseSavepoint(Savepoint savepoint) throws SQLException
		{
      this.releaseSavepoint(savepoint);
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#rollback()
		 */
		public void rollback() throws SQLException
		{
      this.embedded.rollback();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#rollback(java.sql.Savepoint)
		 */
		public void rollback(Savepoint savepoint) throws SQLException
		{
      this.embedded.rollback(savepoint);
    }

		/* (non-Javadoc)
		 * @see java.sql.Connection#setAutoCommit(boolean)
		 */
		public void setAutoCommit(boolean autoCommit) throws SQLException
		{
      this.embedded.setAutoCommit(autoCommit);
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setCatalog(java.lang.String)
		 */
		public void setCatalog(String catalog) throws SQLException
		{
      this.embedded.setCatalog(catalog);
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setHoldability(int)
		 */
		public void setHoldability(int holdability) throws SQLException
		{
      this.embedded.setHoldability(holdability);
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setReadOnly(boolean)
		 */
		public void setReadOnly(boolean readOnly) throws SQLException
		{
			this.embedded.setReadOnly( readOnly);
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setSavepoint()
		 */
		public Savepoint setSavepoint() throws SQLException
		{
			return this.embedded.setSavepoint();
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setSavepoint(java.lang.String)
		 */
		public Savepoint setSavepoint(String name) throws SQLException
		{
      return this.embedded.setSavepoint(name);
    }

		/* (non-Javadoc)
		 * @see java.sql.Connection#setTransactionIsolation(int)
		 */
		public void setTransactionIsolation(int level) throws SQLException
		{
      this.embedded.setTransactionIsolation(level);
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setTypeMap(java.util.Map)
		 */
		public void setTypeMap(Map map) throws SQLException
		{
      this.embedded.setTypeMap(map);
		}
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#createConnection(java.lang.String, java.lang.String)
	 */
	protected Connection createConnection(String username, String password) throws SQLException
	{
		// just in case somebody tries to call
		throw new UnsupportedOperationException();
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#destroy()
   */
  public void destroy()
  {
    // already destroyed?
    if (isDestroyed())
      return;
    
    // close connection and shutdown to release memory 
    try
    {
      Connection connection = (Connection) this.connectionQueue.remove();
      try
      {
        Statement statement = connection.createStatement();
        statement.execute("SHUTDOWN");
        statement.close();
      }
      finally
      {
        connection.close();
      }
    }
    catch (Exception ex)
    {
      logger.warn("Destroy failed", ex);
    }
    
    super.destroy();
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataSource#newId(de.tif.jacob.core.definition.ITableDefinition, de.tif.jacob.core.definition.ITableField)
	 */
	public long newJacobIds(ITableDefinition table, ITableField field, int increment)
	{
		synchronized (this.idMap)
		{
			NextId nextId = (NextId) this.idMap.get(table);
			if (null == nextId)
			{
				nextId = new NextId();
				this.idMap.put(table, nextId);
			}
			return nextId.get();
		}
	}
	
  public boolean setNextJacobId(ITableDefinition table, ITableField field, long nextId) throws SQLException
  {
    synchronized (this.idMap)
    {
      NextId nextOldId = (NextId) this.idMap.get(table);
      if (null == nextOldId)
      {
        if (nextId == 1)
          return false;
        nextOldId = new NextId();
        this.idMap.put(table, nextOldId);
      }
      else
      {
        if (nextOldId.id == nextId)
          return false;
      }
      nextOldId.id = nextId;
      return true;
    }
  }

  /**
	 * @author Andreas
	 *
	 * Helper class to hold next id value.
	 */
	private static class NextId
	{
		private long id = 1;
		
		private long get()
		{
			return this.id++;
		}
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#setupAsJacobDatasource(de.tif.jacob.core.schema.ISchemaDefinition)
   */
	protected void setupAsJacobDatasource(ISchemaDefinition currentSchema) throws Exception
	{
		// do not do anything here and also do not call super implementation
	}
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.SQLDataSource#setupAsQuintusDatasource(de.tif.jacob.core.schema.ISchemaDefinition)
   */
  protected void setupAsQuintusDatasource(ISchemaDefinition currentSchema) throws Exception
  {
		// do not do anything here and also do not call super implementation
  }
}
