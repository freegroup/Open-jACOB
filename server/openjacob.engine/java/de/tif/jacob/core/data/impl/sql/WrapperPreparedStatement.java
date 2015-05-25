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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import de.tif.jacob.core.data.impl.sql.SQLMonitor.NativeSQLProvider;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WrapperPreparedStatement extends WrapperStatement implements PreparedStatement, NativeSQLProvider
{
	static public transient final String RCS_ID = "$Id: WrapperPreparedStatement.java,v 1.3 2010/11/18 11:24:42 freegroup Exp $";
	static public transient final String RCS_REV = "$Revision: 1.3 $";

	private final String sql;
	private final Map nativeArguments;
	/**
	 * @param connection
	 * @param embedded
	 */
	WrapperPreparedStatement(WrapperConnection connection, PreparedStatement statement, String sql) throws SQLException
	{
		super(connection, statement);
		this.sql = sql;
		this.nativeArguments = new HashMap();
	}

	private PreparedStatement getEmbedded()
	{
		return (PreparedStatement) this.embedded;
	}

	public void addBatch(String sql) throws SQLException
  {
	   checkClosed();
   embedded.addBatch(sql);
  }

  public void cancel() throws SQLException
  {
    checkClosed();
    embedded.cancel();
  }

  public void clearBatch() throws SQLException
  {
    checkClosed();
    embedded.clearBatch();
  }

  public void clearWarnings() throws SQLException
  {
    checkClosed();
   embedded.clearWarnings();
  }

  public void close() throws SQLException
  {
    checkClosed();
    embedded.close();
  }

  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
  {
    checkClosed();
    return embedded.execute(sql, autoGeneratedKeys);
  }

  public boolean execute(String sql, int[] columnIndexes) throws SQLException
  {
    checkClosed();
    return embedded.execute(sql, columnIndexes);
  }

  public boolean execute(String sql, String[] columnNames) throws SQLException
  {
    checkClosed();
    return embedded.execute(sql, columnNames);
  }

  public boolean execute(String sql) throws SQLException
  {
    checkClosed();
    return embedded.execute(sql);
  }

  public int[] executeBatch() throws SQLException
  {
    checkClosed();
    return embedded.executeBatch();
  }

  public ResultSet executeQuery(String sql) throws SQLException
  {
    checkClosed();
    return embedded.executeQuery(sql);
  }

  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
  {
    checkClosed();
    return embedded.executeUpdate(sql, autoGeneratedKeys);
  }

  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
  {
    checkClosed();
    return embedded.executeUpdate(sql, columnIndexes);
  }

  public int executeUpdate(String sql, String[] columnNames) throws SQLException
  {
    checkClosed();
    return embedded.executeUpdate(sql, columnNames);
  }

  public int executeUpdate(String sql) throws SQLException
  {
    checkClosed();
    return embedded.executeUpdate(sql);
  }

  public Connection getConnection() throws SQLException
  {
    checkClosed();
    return embedded.getConnection();
  }

  public int getFetchDirection() throws SQLException
  {
    checkClosed();
    return embedded.getFetchDirection();
  }

  public int getFetchSize() throws SQLException
  {
    checkClosed();
    return embedded.getFetchSize();
  }

  public ResultSet getGeneratedKeys() throws SQLException
  {
    checkClosed();
    return embedded.getGeneratedKeys();
  }

  public int getMaxFieldSize() throws SQLException
  {
    checkClosed();
    return embedded.getMaxFieldSize();
  }

  public int getMaxRows() throws SQLException
  {
    checkClosed();
    return embedded.getMaxRows();
  }

  public boolean getMoreResults() throws SQLException
  {
    checkClosed();
    return embedded.getMoreResults();
  }

  public boolean getMoreResults(int current) throws SQLException
  {
    checkClosed();
    return embedded.getMoreResults(current);
  }

  public int getQueryTimeout() throws SQLException
  {
    checkClosed();
    return embedded.getQueryTimeout();
  }

  public ResultSet getResultSet() throws SQLException
  {
    checkClosed();
    return embedded.getResultSet();
  }

  public int getResultSetConcurrency() throws SQLException
  {
    checkClosed();
    return embedded.getResultSetConcurrency();
  }

  public int getResultSetHoldability() throws SQLException
  {
    checkClosed();
    return embedded.getResultSetHoldability();
  }

  public int getResultSetType() throws SQLException
  {
    checkClosed();
    return embedded.getResultSetType();
  }

  public int getUpdateCount() throws SQLException
  {
    checkClosed();
    return embedded.getUpdateCount();
  }

  public SQLWarning getWarnings() throws SQLException
  {
    return embedded.getWarnings();
  }

  public boolean isClosed() throws SQLException
  {
    return embedded.isClosed();
  }

  public boolean isPoolable() throws SQLException
  {
    return embedded.isPoolable();
  }

  public boolean isWrapperFor(Class< ? > iface) throws SQLException
  {
    return embedded.isWrapperFor(iface);
  }

  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException
  {
    checkClosed();
   ( (PreparedStatement) embedded).setAsciiStream(parameterIndex, x, length);
  }

  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException
  {
    checkClosed();
    ( (PreparedStatement) embedded).setAsciiStream(parameterIndex, x);
  }

  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException
  {
    checkClosed();
    ( (PreparedStatement) embedded).setBinaryStream(parameterIndex, x, length);
  }

  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException
  {
    checkClosed();
    ( (PreparedStatement) embedded).setBinaryStream(parameterIndex, x);
  }

  public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException
  {
    checkClosed();
    ( (PreparedStatement) embedded).setBlob(parameterIndex, inputStream, length);
  }

  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException
  {
    checkClosed();
    ( (PreparedStatement) embedded).setBlob(parameterIndex, inputStream);
  }

  public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException
  {
    checkClosed();
    ( (PreparedStatement) embedded).setCharacterStream(parameterIndex, reader, length);
  }

  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException
  {
    checkClosed();
    ( (PreparedStatement) embedded).setCharacterStream(parameterIndex, reader);
  }

  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException
  {
    ( (PreparedStatement) embedded).setClob(parameterIndex, reader, length);
  }

  public void setClob(int parameterIndex, Reader reader) throws SQLException
  {
    ( (PreparedStatement) embedded).setClob(parameterIndex, reader);
  }

  public void setCursorName(String name) throws SQLException
  {
    ( (PreparedStatement) embedded).setCursorName(name);
  }

  public void setEscapeProcessing(boolean enable) throws SQLException
  {
    ( (PreparedStatement) embedded).setEscapeProcessing(enable);
  }

  public void setFetchDirection(int direction) throws SQLException
  {
    ( (PreparedStatement) embedded).setFetchDirection(direction);
  }

  public void setFetchSize(int rows) throws SQLException
  {
    ( (PreparedStatement) embedded).setFetchSize(rows);
  }

  public void setMaxFieldSize(int max) throws SQLException
  {
    ( (PreparedStatement) embedded).setMaxFieldSize(max);
  }

  public void setMaxRows(int max) throws SQLException
  {
    ( (PreparedStatement) embedded).setMaxRows(max);
  }

  public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException
  {
    ( (PreparedStatement) embedded).setNCharacterStream(parameterIndex, value, length);
  }

  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException
  {
    ( (PreparedStatement) embedded).setNCharacterStream(parameterIndex, value);
  }

  public void setNClob(int parameterIndex, NClob value) throws SQLException
  {
    ( (PreparedStatement) embedded).setNClob(parameterIndex, value);
  }

  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException
  {
    ( (PreparedStatement) embedded).setNClob(parameterIndex, reader, length);
  }

  public void setNClob(int parameterIndex, Reader reader) throws SQLException
  {
    ( (PreparedStatement) embedded).setNClob(parameterIndex, reader);
  }

  public void setNString(int parameterIndex, String value) throws SQLException
  {
    ( (PreparedStatement) embedded).setNString(parameterIndex, value);
  }

  public void setPoolable(boolean poolable) throws SQLException
  {
    embedded.setPoolable(poolable);
  }

  public void setQueryTimeout(int seconds) throws SQLException
  {
    embedded.setQueryTimeout(seconds);
  }

  public void setRowId(int parameterIndex, RowId x) throws SQLException
  {
    ( (PreparedStatement) embedded).setRowId(parameterIndex, x);
  }

  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException
  {
    ( (PreparedStatement) embedded).setSQLXML(parameterIndex, xmlObject);
  }

  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    return embedded.unwrap(iface);
  }

  private void setNativeArgument(int parameterIndex, Object argument)
	{
		this.nativeArguments.put(new Integer(parameterIndex), argument);
	}

	private String getNativeArgument(int parameterIndex)
	{
		Integer key = new Integer(parameterIndex);
		Object argument = this.nativeArguments.get(key);
		if (argument != null)
		{
			if (argument instanceof String)
			{
				return this.connection.getDataSource().convertToSQL((String) argument, true);
			}
			else if (argument instanceof Time)
			{
				return this.connection.getDataSource().toQueryString((Time) argument);
			}
			else if (argument instanceof Timestamp)
			{
				return this.connection.getDataSource().toQueryString((Timestamp) argument);
			}
			else if (argument instanceof Date)
			{
				return this.connection.getDataSource().toQueryString((Date) argument);
			}
			else
			{
				return argument.toString();
			}
		}
		return this.nativeArguments.containsKey(key) ? SQL.NULL : "?";
	}

	public String getNativeSQL()
	{
		StringBuffer buffer = new StringBuffer(this.sql.length() + 256);
		StringTokenizer tokenizer = new StringTokenizer(this.sql, "?");
		int parameterIndex = 1;
		boolean more = tokenizer.hasMoreTokens();
		while (more)
		{
			buffer.append(tokenizer.nextToken());
			more = tokenizer.hasMoreTokens();
			if (more)
				buffer.append(getNativeArgument(parameterIndex++));
		}
		// if the last char is the delimiter, then we have to handle this extra
		if (this.sql.charAt(this.sql.length() - 1) == '?')
			buffer.append(getNativeArgument(parameterIndex));
		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#addBatch()
	 */
	public void addBatch() throws SQLException
	{
		checkClosed();
		getEmbedded().addBatch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#clearParameters()
	 */
	public void clearParameters() throws SQLException
	{
		checkClosed();
		getEmbedded().clearParameters();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#execute()
	 */
	public boolean execute() throws SQLException
	{
		checkClosed();
		if (SQL.logger.isDebugEnabled())
			SQL.logger.debug(getNativeSQL());
		long start = System.currentTimeMillis();
		try
		{
			boolean result = getEmbedded().execute();
      SQLMonitor.log(SQLMonitor.OTHER_LOG_TYPE, start, this.connection.getDataSource(), this);
      nativeArguments.clear();
			return result;
		}
		catch (SQLException e)
		{
			// rethrow the exception and add the SQL statement to the exception
			throw new de.tif.jacob.core.exception.SQLException(this.connection.getDataSource(), e, getNativeSQL());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#executeQuery()
	 */
	public ResultSet executeQuery() throws SQLException
	{
		checkClosed();
		if (SQL.logger.isDebugEnabled())
			SQL.logger.debug(getNativeSQL());
		long start = System.currentTimeMillis();
		try
		{
			ResultSet result = getEmbedded().executeQuery();
      SQLMonitor.log(SQLMonitor.QUERY_LOG_TYPE, start, this.connection.getDataSource(), this);
      nativeArguments.clear();
			return result;
		}
		catch (SQLException e)
		{
			// rethrow the exception and add the SQL statement to the exception
			throw new de.tif.jacob.core.exception.SQLException(this.connection.getDataSource(), e, getNativeSQL());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#executeUpdate()
	 */
	public int executeUpdate() throws SQLException
	{
		checkClosed();
		if (SQL.logger.isDebugEnabled())
			SQL.logger.debug(getNativeSQL());
		long start = System.currentTimeMillis();
		try
		{
			int result = getEmbedded().executeUpdate();
      SQLMonitor.log(SQLMonitor.UPDATE_LOG_TYPE, start, this.connection.getDataSource(), this);
      nativeArguments.clear();
			return result;
		}
		catch (SQLException e)
		{
			// rethrow the exception and add the SQL statement to the exception
			throw new de.tif.jacob.core.exception.SQLException(this.connection.getDataSource(), e, getNativeSQL());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#getMetaData()
	 */
	public ResultSetMetaData getMetaData() throws SQLException
	{
		checkClosed();
		return getEmbedded().getMetaData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	public ParameterMetaData getParameterMetaData() throws SQLException
	{
		checkClosed();
		return getEmbedded().getParameterMetaData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
	 */
	public void setArray(int i, Array x) throws SQLException
	{
		checkClosed();
		getEmbedded().setArray(i, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream,
	 *      int)
	 */
	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException
	{
		checkClosed();
		getEmbedded().setAsciiStream(parameterIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
	 */
	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, x);
		getEmbedded().setBigDecimal(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream,
	 *      int)
	 */
	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException
	{
		checkClosed();
		getEmbedded().setBinaryStream(parameterIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
	 */
	public void setBlob(int i, Blob x) throws SQLException
	{
		checkClosed();
		getEmbedded().setBlob(i, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBoolean(int, boolean)
	 */
	public void setBoolean(int parameterIndex, boolean x) throws SQLException
	{
		checkClosed();
		getEmbedded().setBoolean(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setByte(int, byte)
	 */
	public void setByte(int parameterIndex, byte x) throws SQLException
	{
		checkClosed();
		getEmbedded().setByte(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBytes(int, byte[])
	 */
	public void setBytes(int parameterIndex, byte[] x) throws SQLException
	{
		checkClosed();
		getEmbedded().setBytes(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader,
	 *      int)
	 */
	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException
	{
		checkClosed();
		getEmbedded().setCharacterStream(parameterIndex, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
	 */
	public void setClob(int i, Clob x) throws SQLException
	{
		checkClosed();
		getEmbedded().setClob(i, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date,
	 *      java.util.Calendar)
	 */
	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, x);
		getEmbedded().setDate(parameterIndex, x, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
	 */
	public void setDate(int parameterIndex, Date x) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, x);
		getEmbedded().setDate(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setDouble(int, double)
	 */
	public void setDouble(int parameterIndex, double x) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, new Double(x));
		getEmbedded().setDouble(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setFloat(int, float)
	 */
	public void setFloat(int parameterIndex, float x) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, new Float(x));
		getEmbedded().setFloat(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setInt(int, int)
	 */
	public void setInt(int parameterIndex, int x) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, new Integer(x));
		getEmbedded().setInt(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setLong(int, long)
	 */
	public void setLong(int parameterIndex, long x) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, new Long(x));
		getEmbedded().setLong(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
	 */
	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException
	{
		checkClosed();
		setNativeArgument(paramIndex, null);
		getEmbedded().setNull(paramIndex, sqlType, typeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setNull(int, int)
	 */
	public void setNull(int parameterIndex, int sqlType) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, null);
		getEmbedded().setNull(parameterIndex, sqlType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
	 */
	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException
	{
		checkClosed();
		getEmbedded().setObject(parameterIndex, x, targetSqlType, scale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
	 */
	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException
	{
		checkClosed();
		getEmbedded().setObject(parameterIndex, x, targetSqlType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
	 */
	public void setObject(int parameterIndex, Object x) throws SQLException
	{
		checkClosed();
		getEmbedded().setObject(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
	 */
	public void setRef(int i, Ref x) throws SQLException
	{
		checkClosed();
		getEmbedded().setRef(i, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setShort(int, short)
	 */
	public void setShort(int parameterIndex, short x) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, new Short(x));
		getEmbedded().setShort(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setString(int, java.lang.String)
	 */
	public void setString(int parameterIndex, String x) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, x);
		getEmbedded().setString(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time,
	 *      java.util.Calendar)
	 */
	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, x);
		getEmbedded().setTime(parameterIndex, x, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
	 */
	public void setTime(int parameterIndex, Time x) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, x);
		getEmbedded().setTime(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp,
	 *      java.util.Calendar)
	 */
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, x);
		getEmbedded().setTimestamp(parameterIndex, x, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException
	{
		checkClosed();
		setNativeArgument(parameterIndex, x);
		getEmbedded().setTimestamp(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream,
	 *      int)
	 */
	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException
	{
		checkClosed();
		getEmbedded().setUnicodeStream(parameterIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
	 */
	public void setURL(int parameterIndex, URL x) throws SQLException
	{
		checkClosed();
		getEmbedded().setURL(parameterIndex, x);
	}

}