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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WrapperCallableStatement extends WrapperPreparedStatement implements CallableStatement
{
  static public transient final String RCS_ID = "$Id: WrapperCallableStatement.java,v 1.2 2010/11/18 11:24:42 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
	/**
	 * @param connection
	 * @param embedded
	 */
	WrapperCallableStatement(WrapperConnection connection, CallableStatement embedded, String sql) throws SQLException
	{
		super(connection, embedded, sql);
	}

  private CallableStatement getEmbedded()
  {
    return (CallableStatement) this.embedded;
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getArray(int)
	 */
	public Array getArray(int i) throws SQLException
	{
		checkClosed();
		return getEmbedded().getArray(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getArray(java.lang.String)
	 */
	public Array getArray(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getArray(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getBigDecimal(int, int)
	 */
	public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException
	{
		checkClosed();
		return getEmbedded().getBigDecimal(parameterIndex, scale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getBigDecimal(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getBigDecimal(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getBlob(int)
	 */
	public Blob getBlob(int i) throws SQLException
	{
		checkClosed();
		return getEmbedded().getBlob(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getBlob(java.lang.String)
	 */
	public Blob getBlob(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getBlob(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getBoolean(int)
	 */
	public boolean getBoolean(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getBoolean(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getBoolean(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getByte(int)
	 */
	public byte getByte(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getByte(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getByte(java.lang.String)
	 */
	public byte getByte(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getByte(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getBytes(int)
	 */
	public byte[] getBytes(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getBytes(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getBytes(java.lang.String)
	 */
	public byte[] getBytes(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getBytes(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getClob(int)
	 */
	public Clob getClob(int i) throws SQLException
	{
		checkClosed();
		return getEmbedded().getClob(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getClob(java.lang.String)
	 */
	public Clob getClob(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getClob(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getDate(int, java.util.Calendar)
	 */
	public Date getDate(int parameterIndex, Calendar cal) throws SQLException
	{
		checkClosed();
		return getEmbedded().getDate(parameterIndex, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getDate(int)
	 */
	public Date getDate(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getDate(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getDate(java.lang.String,
	 *      java.util.Calendar)
	 */
	public Date getDate(String parameterName, Calendar cal) throws SQLException
	{
		checkClosed();
		return getEmbedded().getDate(parameterName, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getDate(java.lang.String)
	 */
	public Date getDate(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getDate(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getDouble(int)
	 */
	public double getDouble(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getDouble(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getDouble(java.lang.String)
	 */
	public double getDouble(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getDouble(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getFloat(int)
	 */
	public float getFloat(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getFloat(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getFloat(java.lang.String)
	 */
	public float getFloat(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getFloat(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getInt(int)
	 */
	public int getInt(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getInt(parameterIndex);
	}

	@Override
  public Reader getCharacterStream(int parameterIndex) throws SQLException
  {
    checkClosed();
    return getEmbedded().getCharacterStream(parameterIndex);
  }

  @Override
  public Reader getCharacterStream(String parameterName) throws SQLException
  {
    checkClosed();
    return getEmbedded().getCharacterStream(parameterName);
 }

  @Override
  public Reader getNCharacterStream(int parameterIndex) throws SQLException
  {
    checkClosed();
    return getEmbedded().getNCharacterStream(parameterIndex);
  }

  @Override
  public Reader getNCharacterStream(String parameterName) throws SQLException
  {
    checkClosed();
    return getEmbedded().getNCharacterStream(parameterName);
  }

  @Override
  public NClob getNClob(int parameterIndex) throws SQLException
  {
    checkClosed();
    return getEmbedded().getNClob(parameterIndex);
  }

  @Override
  public NClob getNClob(String parameterName) throws SQLException
  {
    checkClosed();
    return getEmbedded().getNClob(parameterName);
  }

  @Override
  public String getNString(int parameterIndex) throws SQLException
  {
    checkClosed();
    return getEmbedded().getNString(parameterIndex);
  }

  @Override
  public String getNString(String parameterName) throws SQLException
  {
    checkClosed();
    return getEmbedded().getNString(parameterName);
 }

  @Override
  public RowId getRowId(int parameterIndex) throws SQLException
  {
    checkClosed();
    return getEmbedded().getRowId(parameterIndex);
 }

  @Override
  public RowId getRowId(String parameterName) throws SQLException
  {
    checkClosed();
    return getEmbedded().getRowId(parameterName);
 }

  @Override
  public SQLXML getSQLXML(int parameterIndex) throws SQLException
  {
    checkClosed();
    return getEmbedded().getSQLXML(parameterIndex);
  }

  @Override
  public SQLXML getSQLXML(String parameterName) throws SQLException
  {
    checkClosed();
    return getEmbedded().getSQLXML(parameterName);
  }

  @Override
  public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException
  {
    checkClosed();
     getEmbedded().setAsciiStream(parameterName,x,length);
  }

  @Override
  public void setAsciiStream(String parameterName, InputStream x) throws SQLException
  {
    checkClosed();
    getEmbedded().setAsciiStream(parameterName,x);
 }

  @Override
  public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setBinaryStream(parameterName,x,length);
 }

  @Override
  public void setBinaryStream(String parameterName, InputStream x) throws SQLException
  {
    checkClosed();
    getEmbedded().setBinaryStream(parameterName,x);
 }

  @Override
  public void setBlob(String parameterName, Blob x) throws SQLException
  {
    checkClosed();
    getEmbedded().setBlob(parameterName,x);
  }

  @Override
  public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setBlob(parameterName,inputStream,length);
  }

  @Override
  public void setBlob(String parameterName, InputStream inputStream) throws SQLException
  {
    checkClosed();
    getEmbedded().setBlob(parameterName,inputStream);
  }

  @Override
  public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setCharacterStream(parameterName,reader, length);
  }

  @Override
  public void setCharacterStream(String parameterName, Reader reader) throws SQLException
  {
    checkClosed();
    getEmbedded().setCharacterStream(parameterName,reader);
  }

  @Override
  public void setClob(String parameterName, Clob x) throws SQLException
  {
    checkClosed();
    getEmbedded().setClob(parameterName,x);
  }

  @Override
  public void setClob(String parameterName, Reader reader, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setClob(parameterName,reader, length);
  }

  @Override
  public void setClob(String parameterName, Reader reader) throws SQLException
  {
    checkClosed();
    getEmbedded().setClob(parameterName,reader);
 }

  @Override
  public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setNCharacterStream(parameterName,value, length);
 }

  @Override
  public void setNCharacterStream(String parameterName, Reader value) throws SQLException
  {
    checkClosed();
    getEmbedded().setNCharacterStream(parameterName,value);
  }

  @Override
  public void setNClob(String parameterName, NClob value) throws SQLException
  {
    checkClosed();
    getEmbedded().setNClob(parameterName,value);
  }

  @Override
  public void setNClob(String parameterName, Reader reader, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setNClob(parameterName,reader,length);
  }

  @Override
  public void setNClob(String parameterName, Reader reader) throws SQLException
  {
    checkClosed();
    getEmbedded().setNClob(parameterName,reader);
  }

  @Override
  public void setNString(String parameterName, String value) throws SQLException
  {
    checkClosed();
    getEmbedded().setNString(parameterName,value);
  }

  @Override
  public void setRowId(String parameterName, RowId x) throws SQLException
  {
    checkClosed();
    getEmbedded().setRowId(parameterName,x);
 }

  @Override
  public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException
  {
    checkClosed();
    getEmbedded().setSQLXML(parameterName,xmlObject);
  }

  @Override
  public boolean isWrapperFor(Class< ? > iface) throws SQLException
  {
    return getEmbedded().isWrapperFor(iface);
 }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    return getEmbedded().unwrap(iface);
 }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setAsciiStream(parameterIndex,x,length);
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException
  {
    checkClosed();
    getEmbedded().setAsciiStream(parameterIndex,x);
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setBinaryStream(parameterIndex, x,length);
 }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException
  {
    checkClosed();
    getEmbedded().setBinaryStream(parameterIndex,x);
 }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setBlob(parameterIndex, inputStream, length);
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException
  {
    checkClosed();
    getEmbedded().setBlob(parameterIndex,inputStream);
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setCharacterStream(parameterIndex,reader, length);
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException
  {
    checkClosed();
    getEmbedded().setCharacterStream(parameterIndex, reader);
  }

  @Override
  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setClob(parameterIndex,reader,length);
  }

  @Override
  public void setClob(int parameterIndex, Reader reader) throws SQLException
  {
    checkClosed();
    getEmbedded().setClob(parameterIndex,reader);
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setNCharacterStream(parameterIndex,value,length);
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException
  {
    checkClosed();
    getEmbedded().setNCharacterStream(parameterIndex,value);
 }

  @Override
  public void setNClob(int parameterIndex, NClob value) throws SQLException
  {
    checkClosed();
    getEmbedded().setNClob(parameterIndex,value);
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException
  {
    checkClosed();
    getEmbedded().setNClob(parameterIndex,reader, length);
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader) throws SQLException
  {
    checkClosed();
    getEmbedded().setNClob(parameterIndex,reader);
  }

  @Override
  public void setNString(int parameterIndex, String value) throws SQLException
  {
    checkClosed();
    getEmbedded().setNString(parameterIndex,value);
  }

  @Override
  public void setRowId(int parameterIndex, RowId x) throws SQLException
  {
    checkClosed();
    getEmbedded().setRowId(parameterIndex,x);
  }

  @Override
  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException
  {
    checkClosed();
    getEmbedded().setSQLXML(parameterIndex,xmlObject);
 }

  @Override
  public boolean isClosed() throws SQLException
  {
    return getEmbedded().isClosed();
  }

  @Override
  public boolean isPoolable() throws SQLException
  {
    return getEmbedded().isPoolable();
  }

  @Override
  public void setPoolable(boolean poolable) throws SQLException
  {
    checkClosed();
    getEmbedded().setPoolable(poolable);
  }

  /*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getInt(java.lang.String)
	 */
	public int getInt(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getInt(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getLong(int)
	 */
	public long getLong(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getLong(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getLong(java.lang.String)
	 */
	public long getLong(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getLong(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getObject(int, java.util.Map)
	 */
	public Object getObject(int i, Map map) throws SQLException
	{
		checkClosed();
		return getEmbedded().getObject(i, map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getObject(int)
	 */
	public Object getObject(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getObject(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getObject(java.lang.String, java.util.Map)
	 */
	public Object getObject(String parameterName, Map map) throws SQLException
	{
		checkClosed();
		return getEmbedded().getObject(parameterName, map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getObject(java.lang.String)
	 */
	public Object getObject(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getObject(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getRef(int)
	 */
	public Ref getRef(int i) throws SQLException
	{
		checkClosed();
		return getEmbedded().getRef(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getRef(java.lang.String)
	 */
	public Ref getRef(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getRef(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getShort(int)
	 */
	public short getShort(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getShort(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getShort(java.lang.String)
	 */
	public short getShort(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getShort(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getString(int)
	 */
	public String getString(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getString(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getString(java.lang.String)
	 */
	public String getString(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getString(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getTime(int, java.util.Calendar)
	 */
	public Time getTime(int parameterIndex, Calendar cal) throws SQLException
	{
		checkClosed();
		return getEmbedded().getTime(parameterIndex, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getTime(int)
	 */
	public Time getTime(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getTime(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getTime(java.lang.String,
	 *      java.util.Calendar)
	 */
	public Time getTime(String parameterName, Calendar cal) throws SQLException
	{
		checkClosed();
		return getEmbedded().getTime(parameterName, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getTime(java.lang.String)
	 */
	public Time getTime(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getTime(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getTimestamp(int, java.util.Calendar)
	 */
	public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException
	{
		checkClosed();
		return getEmbedded().getTimestamp(parameterIndex, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getTimestamp(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getTimestamp(java.lang.String,
	 *      java.util.Calendar)
	 */
	public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException
	{
		checkClosed();
		return getEmbedded().getTimestamp(parameterName, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getTimestamp(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getURL(int)
	 */
	public URL getURL(int parameterIndex) throws SQLException
	{
		checkClosed();
		return getEmbedded().getURL(parameterIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getURL(java.lang.String)
	 */
	public URL getURL(String parameterName) throws SQLException
	{
		checkClosed();
		return getEmbedded().getURL(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#registerOutParameter(int, int, int)
	 */
	public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException
	{
		checkClosed();
		getEmbedded().registerOutParameter(parameterIndex, sqlType, scale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#registerOutParameter(int, int,
	 *      java.lang.String)
	 */
	public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException
	{
		checkClosed();
		getEmbedded().registerOutParameter(paramIndex, sqlType, typeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#registerOutParameter(int, int)
	 */
	public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException
	{
		checkClosed();
		getEmbedded().registerOutParameter(parameterIndex, sqlType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
	 *      int, int)
	 */
	public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException
	{
		checkClosed();
		getEmbedded().registerOutParameter(parameterName, sqlType, scale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
	 *      int, java.lang.String)
	 */
	public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException
	{
		checkClosed();
		getEmbedded().registerOutParameter(parameterName, sqlType, typeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
	 *      int)
	 */
	public void registerOutParameter(String parameterName, int sqlType) throws SQLException
	{
		checkClosed();
		getEmbedded().registerOutParameter(parameterName, sqlType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
	 *      java.io.InputStream, int)
	 */
	public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException
	{
		checkClosed();
		getEmbedded().setAsciiStream(parameterName, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setBigDecimal(java.lang.String,
	 *      java.math.BigDecimal)
	 */
	public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException
	{
		checkClosed();
		getEmbedded().setBigDecimal(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
	 *      java.io.InputStream, int)
	 */
	public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException
	{
		checkClosed();
		getEmbedded().setBinaryStream(parameterName, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setBoolean(java.lang.String, boolean)
	 */
	public void setBoolean(String parameterName, boolean x) throws SQLException
	{
		checkClosed();
		getEmbedded().setBoolean(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setByte(java.lang.String, byte)
	 */
	public void setByte(String parameterName, byte x) throws SQLException
	{
		checkClosed();
		getEmbedded().setByte(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setBytes(java.lang.String, byte[])
	 */
	public void setBytes(String parameterName, byte[] x) throws SQLException
	{
		checkClosed();
		getEmbedded().setBytes(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
	 *      java.io.Reader, int)
	 */
	public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException
	{
		checkClosed();
		getEmbedded().setCharacterStream(parameterName, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date,
	 *      java.util.Calendar)
	 */
	public void setDate(String parameterName, Date x, Calendar cal) throws SQLException
	{
		checkClosed();
		getEmbedded().setDate(parameterName, x, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date)
	 */
	public void setDate(String parameterName, Date x) throws SQLException
	{
		checkClosed();
		getEmbedded().setDate(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setDouble(java.lang.String, double)
	 */
	public void setDouble(String parameterName, double x) throws SQLException
	{
		checkClosed();
		getEmbedded().setDouble(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setFloat(java.lang.String, float)
	 */
	public void setFloat(String parameterName, float x) throws SQLException
	{
		checkClosed();
		getEmbedded().setFloat(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setInt(java.lang.String, int)
	 */
	public void setInt(String parameterName, int x) throws SQLException
	{
		checkClosed();
		getEmbedded().setInt(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setLong(java.lang.String, long)
	 */
	public void setLong(String parameterName, long x) throws SQLException
	{
		checkClosed();
		getEmbedded().setLong(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int,
	 *      java.lang.String)
	 */
	public void setNull(String parameterName, int sqlType, String typeName) throws SQLException
	{
		checkClosed();
		getEmbedded().setNull(parameterName, sqlType, typeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int)
	 */
	public void setNull(String parameterName, int sqlType) throws SQLException
	{
		checkClosed();
		getEmbedded().setNull(parameterName, sqlType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setObject(java.lang.String,
	 *      java.lang.Object, int, int)
	 */
	public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException
	{
		checkClosed();
		getEmbedded().setObject(parameterName, x, targetSqlType, scale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setObject(java.lang.String,
	 *      java.lang.Object, int)
	 */
	public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException
	{
		checkClosed();
		getEmbedded().setObject(parameterName, x, targetSqlType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setObject(java.lang.String,
	 *      java.lang.Object)
	 */
	public void setObject(String parameterName, Object x) throws SQLException
	{
		checkClosed();
		getEmbedded().setObject(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setShort(java.lang.String, short)
	 */
	public void setShort(String parameterName, short x) throws SQLException
	{
		checkClosed();
		getEmbedded().setShort(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setString(java.lang.String,
	 *      java.lang.String)
	 */
	public void setString(String parameterName, String x) throws SQLException
	{
		checkClosed();
		getEmbedded().setString(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time,
	 *      java.util.Calendar)
	 */
	public void setTime(String parameterName, Time x, Calendar cal) throws SQLException
	{
		checkClosed();
		getEmbedded().setTime(parameterName, x, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time)
	 */
	public void setTime(String parameterName, Time x) throws SQLException
	{
		checkClosed();
		getEmbedded().setTime(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
	 *      java.sql.Timestamp, java.util.Calendar)
	 */
	public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException
	{
		checkClosed();
		getEmbedded().setTimestamp(parameterName, x, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
	 *      java.sql.Timestamp)
	 */
	public void setTimestamp(String parameterName, Timestamp x) throws SQLException
	{
		checkClosed();
		getEmbedded().setTimestamp(parameterName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#setURL(java.lang.String, java.net.URL)
	 */
	public void setURL(String parameterName, URL val) throws SQLException
	{
		checkClosed();
		getEmbedded().setURL(parameterName, val);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#wasNull()
	 */
	public boolean wasNull() throws SQLException
	{
		checkClosed();
		return getEmbedded().wasNull();
	}

}
