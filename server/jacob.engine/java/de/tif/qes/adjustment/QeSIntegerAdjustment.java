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

package de.tif.qes.adjustment;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.adjustment.IIntegerFieldTypeAdjustment;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.data.impl.IDataNewKeysCache.IDataNewKeysCreator;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.WrapperPreparedStatement;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class QeSIntegerAdjustment implements IIntegerFieldTypeAdjustment, IDataNewKeysCreator
{
	static public final transient String RCS_ID = "$Id: QeSIntegerAdjustment.java,v 1.3 2009-01-13 15:31:14 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	static private final transient Log logger = LogFactory.getLog(QeSIntegerAdjustment.class);

  public Object newKey(DataSource dataSource, ITableDefinition table, ITableField field, boolean dbIncrement, IDataNewKeysCache newKeysCache) throws Exception
  {
    return new Integer((int) newKeysCache.newKey(dataSource, table, field, this));
  }
  
  public long createNewKeys(DataSource dataSource, ITableDefinition table, ITableField field, int increment) throws Exception
  {
    return newKey(dataSource, table.getDBName(), increment);
  }

	protected static int newKey(DataSource dataSource, String dbTableName, int increment) throws SQLException
	{
    if (increment < 1)
      throw new IllegalArgumentException("increment is " + increment);
    
		if (logger.isDebugEnabled())
			logger.debug("Getting next key for table=" + dbTableName);

		// hack: it is always assumed that a serial field comes together with an
		// SQL datasource
		SQLDataSource sqlDataSource = (SQLDataSource) dataSource;

    int next = -1;
    
		Connection connection = sqlDataSource.getConnection();
		try
		{
			if (sqlDataSource.supportsStoredProcedures())
      {
        CallableStatement statement = sqlDataSource.buildStoredProcedureStatement(connection, "q_next_key", 3);
        try
        {
          statement.setString(1, dbTableName);
          statement.setInt(2, increment);
          statement.registerOutParameter(3, java.sql.Types.INTEGER);
          int resInt = statement.executeUpdate();
          if (0 == resInt)
          {
            // no modifications performed - error condition for MS SQL Server!
            throw new SQLException("Stored procedure q_next_key() failed: resInt = 0");
          }
          next = statement.getInt(3);
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
    		if (sqlDataSource.supportsForUpdate())
    		  sqlString = "SELECT keyvalue FROM qw_keys WHERE tablename=? FOR UPDATE";
    		else
    		  sqlString = "SELECT keyvalue FROM qw_keys WHERE tablename=?";
    		
    		PreparedStatement statement = connection.prepareStatement(sqlString);
    		try
    		{
    			statement.setString(1, dbTableName);

    			// and execute query to fetch next id, if existing
    			ResultSet rs = statement.executeQuery();
          try
          {
            if (rs.next())
            {
              next = rs.getInt(1) + 1;
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
    		
    		// 2. and update entry then
    		//
        if (next != -1)
        {
          // record existing
          //
          try
          {
            sqlString = "UPDATE qw_keys SET keyvalue=? WHERE tablename=?";
            statement = connection.prepareStatement(sqlString);
            statement.setInt(1, next - 1 + increment);
            statement.setString(2, dbTableName);

            // and execute statement
            int result = statement.executeUpdate();
            if (1 != result)
            {
              String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
              throw new de.tif.jacob.core.exception.SQLException(sqlDataSource, result + " records updated (expected 1)!", sqlStatement);
            }
          }
          finally
          {
            statement.close();
          }
        }
        
  			// 3. and commit the stuff (even if no update has been performed to release the lock)
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
      throw new SQLException("Getting next id for table " + dbTableName + " from qw_keys failed");
    }
    
    if (logger.isDebugEnabled())
      logger.debug("Got next key=" + next + " for table=" + dbTableName);
    
    return next;
	}

  public boolean setNextKey(DataSource dataSource, ITableDefinition table, ITableField field, boolean dbIncrement, int nextKey) throws Exception
  {
    return setNextKey(dataSource, table.getDBName(), nextKey);
  }
  
  protected static boolean setNextKey(DataSource dataSource, String dbTableName, int nextKey) throws SQLException
  {
    if (logger.isDebugEnabled())
      logger.debug("Setting next key " + nextKey + " for table=" + dbTableName);

    // hack: it is always assumed that a serial field comes together with an
    // SQL datasource
    SQLDataSource sqlDataSource = (SQLDataSource) dataSource;

    int oldNextKey = -1;

    Connection connection = sqlDataSource.getConnection();
    try
    {
      // 1. execute query first
      //
      String sqlString;
      if (sqlDataSource.supportsForUpdate())
        sqlString = "SELECT keyvalue FROM qw_keys WHERE tablename=? FOR UPDATE";
      else
        sqlString = "SELECT keyvalue FROM qw_keys WHERE tablename=?";

      PreparedStatement statement = connection.prepareStatement(sqlString);
      try
      {
        statement.setString(1, dbTableName);

        // and execute query to fetch next id, if existing
        ResultSet rs = statement.executeQuery();
        try
        {
          if (rs.next())
          {
            oldNextKey = rs.getInt(1) + 1;
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

      // 2. and update entry then
      //
      if (oldNextKey != nextKey)
      {
        if (oldNextKey != -1)
        {
          // record existing
          //
          try
          {
            sqlString = "UPDATE qw_keys SET keyvalue=? WHERE tablename=?";
            statement = connection.prepareStatement(sqlString);
            statement.setInt(1, nextKey - 1);
            statement.setString(2, dbTableName);

            // and execute statement
            int result = statement.executeUpdate();
            if (1 != result)
            {
              String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
              throw new de.tif.jacob.core.exception.SQLException(sqlDataSource, result + " records updated (expected 1)!", sqlStatement);
            }
          }
          finally
          {
            statement.close();
          }
        }
      }

      // 3. and commit the stuff (even if no update has been performed to release the lock)
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

    if (-1 == oldNextKey)
    {
      throw new SQLException("Setting next id for table " + dbTableName + " from qw_keys failed");
    }

    if (oldNextKey != nextKey)
    {
      logger.info("Set next key=" + nextKey + " (was " + oldNextKey + ") for table=" + dbTableName);
    }
    else
    {
      logger.info("Setting next key=" + nextKey + " not necessary for table=" + dbTableName);
    }

    return oldNextKey != nextKey;
  }
}
