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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.tif.jacob.core.exception.RequestCanceledException;

/**
 * @author andreas
 * 
 * This class postpones the creation of a "real" PreparedStatement upon calling
 * an execute method. The reason for this class is that some databases (or JDBC
 * drivers) do not support to have multiple active prepared statements for one
 * connection at once.
 * <p>
 * For example MS SQL server would throw the following error: <br>
 * Cannot Start a Cloned Connection While in Manual Transaction Mode
 * <p>
 * http://support.microsoft.com/default.aspx?scid=kb%3Ben-us%3B313181
 */
public class PostponedPreparedStatement implements PreparedStatement
{
  private Connection connection;
  private String sql;
  private PreparedStatement embedded;
  private List postponedOperations = new ArrayList();
  private boolean canceled = false;
  
  private static abstract class PostponedParameterOperation
  {
    final int parameterIndex;
    
    PostponedParameterOperation(int parameterIndex)
    {
      this.parameterIndex = parameterIndex;
    }
    
    abstract void accomplish(PreparedStatement statement) throws SQLException;
  }
  
  protected PostponedPreparedStatement(Connection connection, String sql)
  {
    this.connection = connection;
    this.sql = sql;
    
    // we postpone the creation of the statement!
    this.embedded = null;
  }
  
  private PreparedStatement getRealPreparedStatement() throws SQLException
  {
    if (this.embedded == null)
    {
      // create real statement
      this.embedded = this.connection.prepareStatement(this.sql);
      
      // and execute postponed operations
      for (int i=0; i< this.postponedOperations.size(); i++)
      {
        ((PostponedParameterOperation)this.postponedOperations.get(i)).accomplish(this.embedded);
      }
      
      // make sure not to execute again
      this.postponedOperations.clear();
    }
    
    return this.embedded;
  }

	private void checkClosed() throws SQLException
	{
		if (this.connection == null)
			throw new SQLException("Statement is closed");
	}

	private void checkCanceled() throws SQLException
	{
		if (this.canceled)
			throw new RequestCanceledException();
	}

	private void checkPostponeState() throws SQLException
	{
		checkClosed();
		checkCanceled();
		if (this.embedded != null)
			throw new IllegalStateException();
	}

  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#addBatch()
   */
  public void addBatch() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#clearParameters()
   */
  public void clearParameters() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#execute()
   */
  public boolean execute() throws SQLException
  {
		checkClosed();
		checkCanceled();
    return getRealPreparedStatement().execute();
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#executeQuery()
   */
  public ResultSet executeQuery() throws SQLException
  {
		checkClosed();
		checkCanceled();
    return getRealPreparedStatement().executeQuery();
  }
  
  @Override
  public boolean isWrapperFor(Class< ? > iface) throws SQLException
  {
    return getRealPreparedStatement().isWrapperFor( iface);
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    return getRealPreparedStatement().unwrap(iface);
 }

  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#executeUpdate()
   */
  public int executeUpdate() throws SQLException
  {
		checkClosed();
		checkCanceled();
    return getRealPreparedStatement().executeUpdate();
  }
  
  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setAsciiStream(parameterIndex, x, length);
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setAsciiStream(parameterIndex, x);
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setBinaryStream(parameterIndex, x, length);
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setBinaryStream(parameterIndex, x);
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setBlob(parameterIndex, inputStream, length);
 }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setBlob(parameterIndex, inputStream);
 }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setCharacterStream(parameterIndex, reader, length);
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setCharacterStream(parameterIndex, reader);
  }

  @Override
  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setClob(parameterIndex, reader, length);
 }

  @Override
  public void setClob(int parameterIndex, Reader reader) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setClob(parameterIndex, reader);
 }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setNCharacterStream(parameterIndex, value, length);
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setNCharacterStream(parameterIndex, value);
  }

  @Override
  public void setNClob(int parameterIndex, NClob value) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setNClob(parameterIndex, value);
 }

  @Override
  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setNClob(parameterIndex, reader, length);
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setNClob(parameterIndex, reader);
  }

  @Override
  public void setNString(int parameterIndex, String value) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setNString(parameterIndex, value);
  }

  @Override
  public void setRowId(int parameterIndex, RowId x) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setRowId(parameterIndex, x);
 }

  @Override
  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setSQLXML(parameterIndex, xmlObject);
  }

  @Override
  public boolean isClosed() throws SQLException
  {
    return  getRealPreparedStatement().isClosed();
  }

  @Override
  public boolean isPoolable() throws SQLException
  {
    checkClosed();
    checkCanceled();
    return  getRealPreparedStatement().isPoolable();
   }

  @Override
  public void setPoolable(boolean poolable) throws SQLException
  {
    checkClosed();
    checkCanceled();
    getRealPreparedStatement().setPoolable(poolable);
   }

  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#getMetaData()
   */
  public ResultSetMetaData getMetaData() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#getParameterMetaData()
   */
  public ParameterMetaData getParameterMetaData() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
   */
  public void setArray(int i, Array x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
   */
  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  private static class BigDecimalOperation extends PostponedParameterOperation
  {
    final BigDecimal x;

    BigDecimalOperation(int parameterIndex, BigDecimal x)
    {
      super(parameterIndex);
      this.x = x;
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      statement.setBigDecimal(parameterIndex, x);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
   */
  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new BigDecimalOperation(parameterIndex, x));
  }
  
  private static class BinaryStreamOperation extends PostponedParameterOperation
  {
    final ByteArrayInputStream x;
    final int length;

    BinaryStreamOperation(int parameterIndex, InputStream x, int length)
    {
      super(parameterIndex);
      if (!(x instanceof ByteArrayInputStream))
      {
        // Hack: Current this is sufficient, because this class is only used internally and 
        // the engine always uses ByteArrayInputStream's
        throw new UnsupportedOperationException("Supported only for ByteArrayInputStream");
      }
      this.x = (ByteArrayInputStream) x;
      this.length = length;
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      statement.setBinaryStream(parameterIndex, x, length);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
   */
  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new BinaryStreamOperation(parameterIndex, x, length));
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
   */
  public void setBlob(int i, Blob x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setBoolean(int, boolean)
   */
  public void setBoolean(int parameterIndex, boolean x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setByte(int, byte)
   */
  public void setByte(int parameterIndex, byte x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  private static class BytesOperation extends PostponedParameterOperation
  {
    final byte[] x;

    BytesOperation(int parameterIndex, byte[] x)
    {
      super(parameterIndex);
      this.x = (byte[]) x.clone();
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      statement.setBytes(parameterIndex, x);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setBytes(int, byte[])
   */
  public void setBytes(int parameterIndex, byte[] x) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new BytesOperation(parameterIndex, x));
  }
  
  private static class CharacterStreamOperation extends PostponedParameterOperation
  {
    final StringReader x;
    final int length;

    CharacterStreamOperation(int parameterIndex, Reader x, int length)
    {
      super(parameterIndex);
      if (!(x instanceof StringReader))
      {
        // Hack: Current this is sufficient, because this class is only used internally and 
        // the engine always uses StringReader's
        throw new UnsupportedOperationException("Supported only for StringReader");
      }
      this.x = (StringReader) x;
      this.length = length;
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      statement.setCharacterStream(parameterIndex, x, length);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
   */
  public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new CharacterStreamOperation(parameterIndex, reader, length));
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
   */
  public void setClob(int i, Clob x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  private static class DateOperation extends PostponedParameterOperation
  {
    final Date x;
    final Calendar cal;

    DateOperation(int parameterIndex, Date x, Calendar cal)
    {
      super(parameterIndex);
      this.x = x;
      this.cal = cal == null ? null : (Calendar) cal.clone();
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      if (cal == null)
        statement.setDate(parameterIndex, x);
      else
        statement.setDate(parameterIndex, x, cal);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
   */
  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new DateOperation(parameterIndex, x, cal));
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
   */
  public void setDate(int parameterIndex, Date x) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new DateOperation(parameterIndex, x, null));
  }
  
  private static class DoubleOperation extends PostponedParameterOperation
  {
    final double x;

    DoubleOperation(int parameterIndex, double x)
    {
      super(parameterIndex);
      this.x = x;
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      statement.setDouble(parameterIndex, x);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setDouble(int, double)
   */
  public void setDouble(int parameterIndex, double x) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new DoubleOperation(parameterIndex, x));
  }
  
  private static class FloatOperation extends PostponedParameterOperation
  {
    final float x;

    FloatOperation(int parameterIndex, float x)
    {
      super(parameterIndex);
      this.x = x;
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      statement.setFloat(parameterIndex, x);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setFloat(int, float)
   */
  public void setFloat(int parameterIndex, float x) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new FloatOperation(parameterIndex, x));
  }
  
  private static class IntOperation extends PostponedParameterOperation
  {
    final int x;

    IntOperation(int parameterIndex, int x)
    {
      super(parameterIndex);
      this.x = x;
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      statement.setInt(parameterIndex, x);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setInt(int, int)
   */
  public void setInt(int parameterIndex, int x) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new IntOperation(parameterIndex, x));
  }
  
  private static class LongOperation extends PostponedParameterOperation
  {
    final long x;

    LongOperation(int parameterIndex, long x)
    {
      super(parameterIndex);
      this.x = x;
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      statement.setLong(parameterIndex, x);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setLong(int, long)
   */
  public void setLong(int parameterIndex, long x) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new LongOperation(parameterIndex, x));
  }
  
  private static class NullOperation extends PostponedParameterOperation
  {
    final int sqlType;
    final String typeName;

    NullOperation(int parameterIndex, int sqlType, String typeName)
    {
      super(parameterIndex);
      this.sqlType = sqlType;
      this.typeName = typeName;
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      if (this.typeName == null)
        statement.setNull(parameterIndex, sqlType);
      else
        statement.setNull(parameterIndex, sqlType, typeName);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
   */
  public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new NullOperation(paramIndex, sqlType, typeName));
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setNull(int, int)
   */
  public void setNull(int parameterIndex, int sqlType) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new NullOperation(parameterIndex, sqlType, null));
  }
  
  private static class ObjectOperation extends PostponedParameterOperation
  {
    private static final int DUMMY_INT = -542645;
    
    final Object x;
    final int targetSqlType;
    final int scale;

    ObjectOperation(int parameterIndex, Object x, int targetSqlType, int scale)
    {
      super(parameterIndex);
      this.x = x;
      this.targetSqlType = targetSqlType;
      this.scale = scale;
    }

    ObjectOperation(int parameterIndex, Object x, int targetSqlType)
    {
      super(parameterIndex);
      this.x = x;
      this.targetSqlType = targetSqlType;
      this.scale = DUMMY_INT;
    }

    ObjectOperation(int parameterIndex, Object x)
    {
      super(parameterIndex);
      this.x = x;
      this.targetSqlType = DUMMY_INT;
      this.scale = DUMMY_INT;
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      if (scale != DUMMY_INT)
        statement.setObject(parameterIndex, x, targetSqlType, scale);
      else if (targetSqlType != DUMMY_INT)
        statement.setObject(parameterIndex, x, targetSqlType);
      else
        statement.setObject(parameterIndex, x);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
   */
  public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new ObjectOperation(parameterIndex, x, targetSqlType, scale));
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
   */
  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new ObjectOperation(parameterIndex, x, targetSqlType));
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
   */
  public void setObject(int parameterIndex, Object x) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new ObjectOperation(parameterIndex, x));
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
   */
  public void setRef(int i, Ref x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setShort(int, short)
   */
  public void setShort(int parameterIndex, short x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  private static class StringOperation extends PostponedParameterOperation
  {
    final String x;

    StringOperation(int parameterIndex, String x)
    {
      super(parameterIndex);
      this.x = x;
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      statement.setString(parameterIndex, x);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setString(int, java.lang.String)
   */
  public void setString(int parameterIndex, String x) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new StringOperation(parameterIndex, x));
  }
  
  private static class TimeOperation extends PostponedParameterOperation
  {
    final Time x;
    final Calendar cal;

    TimeOperation(int parameterIndex, Time x, Calendar cal)
    {
      super(parameterIndex);
      this.x = x;
      this.cal = cal == null ? null : (Calendar) cal.clone();
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      if (cal == null)
        statement.setTime(parameterIndex, x);
      else
        statement.setTime(parameterIndex, x, cal);
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
   */
  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new TimeOperation(parameterIndex, x, cal));
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
   */
  public void setTime(int parameterIndex, Time x) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new TimeOperation(parameterIndex, x, null));
  }
  
  private static class TimestampOperation extends PostponedParameterOperation
  {
    final Timestamp x;
    final Calendar cal;

    TimestampOperation(int parameterIndex, Timestamp x, Calendar cal)
    {
      super(parameterIndex);
      this.x = x;
      this.cal = cal == null ? null : (Calendar) cal.clone();
    }

    void accomplish(PreparedStatement statement) throws SQLException
    {
      if (cal == null)
        statement.setTimestamp(parameterIndex, x);
      else
        statement.setTimestamp(parameterIndex, x, cal);
    }
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp,
   *      java.util.Calendar)
   */
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new TimestampOperation(parameterIndex, x, cal));
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
   */
  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException
  {
    checkPostponeState();
    this.postponedOperations.add(new TimestampOperation(parameterIndex, x, null));
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int)
   */
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
   */
  public void setURL(int parameterIndex, URL x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#addBatch(java.lang.String)
   */
  public void addBatch(String sql) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#cancel()
   */
  public void cancel() throws SQLException
  {
    // IBIS : Nochmals überprüfen, auch wegen thread-safety (eigentlich müßten
    // alle wesentlichen Methoden synchronized sein)!
    this.canceled = true;

    if (this.embedded != null)
    {
      this.embedded.cancel();
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#clearBatch()
   */
  public void clearBatch() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#clearWarnings()
   */
  public void clearWarnings() throws SQLException
  {
		checkClosed();
    if (this.embedded != null)
    {
      this.embedded.clearWarnings();
    }
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#close()
   */
  public void close() throws SQLException
  {
    // already closed?
    if (this.connection == null)
      return;

    if (this.embedded != null)
    {
      this.embedded.close();
      this.embedded = null;
    }
    this.connection = null;
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#execute(java.lang.String, int)
   */
  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#execute(java.lang.String, int[])
   */
  public boolean execute(String sql, int[] columnIndexes) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
   */
  public boolean execute(String sql, String[] columnNames) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#execute(java.lang.String)
   */
  public boolean execute(String sql) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#executeBatch()
   */
  public int[] executeBatch() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#executeQuery(java.lang.String)
   */
  public ResultSet executeQuery(String sql) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#executeUpdate(java.lang.String, int)
   */
  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
   */
  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
   */
  public int executeUpdate(String sql, String[] columnNames) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#executeUpdate(java.lang.String)
   */
  public int executeUpdate(String sql) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getConnection()
   */
  public Connection getConnection() throws SQLException
  {
    if (null == this.connection)
      throw new IllegalStateException();
    
    return this.connection;
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getFetchDirection()
   */
  public int getFetchDirection() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getFetchSize()
   */
  public int getFetchSize() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getGeneratedKeys()
   */
  public ResultSet getGeneratedKeys() throws SQLException
  {
    checkClosed();
    if (this.embedded == null)
      throw new IllegalStateException("An execute method has to be called in advance");
    return this.embedded.getGeneratedKeys();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getMaxFieldSize()
   */
  public int getMaxFieldSize() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getMaxRows()
   */
  public int getMaxRows() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getMoreResults()
   */
  public boolean getMoreResults() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getMoreResults(int)
   */
  public boolean getMoreResults(int current) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getQueryTimeout()
   */
  public int getQueryTimeout() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getResultSet()
   */
  public ResultSet getResultSet() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getResultSetConcurrency()
   */
  public int getResultSetConcurrency() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getResultSetHoldability()
   */
  public int getResultSetHoldability() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getResultSetType()
   */
  public int getResultSetType() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getUpdateCount()
   */
  public int getUpdateCount() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#getWarnings()
   */
  public SQLWarning getWarnings() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#setCursorName(java.lang.String)
   */
  public void setCursorName(String name) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#setEscapeProcessing(boolean)
   */
  public void setEscapeProcessing(boolean enable) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#setFetchDirection(int)
   */
  public void setFetchDirection(int direction) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#setFetchSize(int)
   */
  public void setFetchSize(int rows) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#setMaxFieldSize(int)
   */
  public void setMaxFieldSize(int max) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#setMaxRows(int)
   */
  public void setMaxRows(int max) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see java.sql.Statement#setQueryTimeout(int)
   */
  public void setQueryTimeout(int seconds) throws SQLException
  {
    throw new UnsupportedOperationException();
  }
}
