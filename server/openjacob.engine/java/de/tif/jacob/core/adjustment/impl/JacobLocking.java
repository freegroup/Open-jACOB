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

package de.tif.jacob.core.adjustment.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.WrapperPreparedStatement;
import de.tif.jacob.core.definition.impl.JacobInternalDefinition;
import de.tif.jacob.core.exception.RecordLockedException;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JacobLocking extends Locking
{
  static public final transient String RCS_ID = "$Id: JacobLocking.java,v 1.4 2009/03/27 00:45:06 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  private static final int NUMBER_OF_TRIES = 2;

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.adjustment.ILocking#doLocking(de.tif.jacob.core.data.impl.DataSource,
   *      de.tif.jacob.core.data.IDataRecordId)
   */
  public boolean doLocking(DataSource dataSource, IDataRecordId recordId)
  {
    // Currently no locking for transient datasources
    if (dataSource.isTransient())
      return false;
    
    if (recordId.getTableDefinition().isOmitLocking())
      return false;
    
    // no lock implementation for non SQL datasources so far
    if (!(dataSource instanceof SQLDataSource))
      return false;

    // do not create lock entries for modifications on lock table itself
    return !JacobInternalDefinition.LOCKS_TABLE_NAME.equalsIgnoreCase(recordId.getTableDefinition().getDBName());
  }

  /**
   * This method tries to lock a record by means of creating an entry in
   * jacob_locks table without using a stored procedure.
   * 
   * @param transaction
   *          the transaction
   * @param sqlDataSource
   *          the datasource
   * @param recordId
   *          the id of the record to lock
   * @throws RecordLockedException
   *           record is locked by someone else
   * @throws SQLException
   *           on any error
   */
  private static void lockNoProcedure(IDataTransaction transaction, SQLDataSource sqlDataSource, IDataRecordId recordId) throws RecordLockedException,
      SQLException
  {
    Connection connection = sqlDataSource.getConnection();
    try
    {

      String tablename = recordId.getTableDefinition().getDBName();
      String keyvalue = recordId.getPrimaryKeyValue().toString();
      String nodename = ClusterManager.getNodeName();

      Exception uniqueException = null;
      
      // try repeatedly to create the lock
      //
      int numberOfTries = NUMBER_OF_TRIES;
      do
      {
        // just try to insert lock entry
        //
        boolean alreadyLocked = false;
        String sqlString = "INSERT INTO jacob_locks (tablename, keyvalue, userid, username, nodename, created) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sqlString);
        try
        {
          statement.setString(1, adjustDBTablename4Locktable(tablename));
          statement.setString(2, keyvalue);
          statement.setString(3, transaction.getUser().getLoginId());
          statement.setString(4, transaction.getUser().getFullName());
          statement.setString(5, nodename);
          statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

          // and execute statement
          try
          {
            int result = statement.executeUpdate();
            if (1 != result)
            {
              // should never occur
              String sqlStatement = statement instanceof WrapperPreparedStatement ? ((WrapperPreparedStatement) statement).getNativeSQL() : sqlString;
              throw new de.tif.jacob.core.exception.SQLException(sqlDataSource, result + " records inserted (expected 1)!", sqlStatement);
            }
          }
          catch (SQLException e)
          {
            if (sqlDataSource.isUniqueViolationErrorCode(e.getErrorCode()))
            {
              alreadyLocked = true;
              
              uniqueException = e; 
            }
            else
            {
              // on any other problem -> rethrow
              throw e;
            }
          }
        }
        finally
        {
          statement.close();
        }

        if (alreadyLocked)
        {
          // fetch info concerning the user obtaining the lock,
          // i.e. information will be within RecordLockedException
          //
          checkLocked(connection, recordId);

          // lock has just been released again
          numberOfTries--;
        }
        else
        {
          // commit the lock
          //
          connection.commit();

          // and return!
          return;
        }
      }
      while (numberOfTries > 0);
      
      logger.warn("Could not lock record '" + recordId + "' after " + NUMBER_OF_TRIES + " tries", uniqueException);

      // should only happen in rare cases, if record is repeatedly locked and
      // unlocked by another transaction
      throw new RecordLockedException(recordId, "unknown");
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
  }

  /**
   * This method unlocks a record by means of deleting an entry in
   * jacob_locks table without using a stored procedure.
   * 
   * @param transaction
   *          the transaction
   * @param sqlDataSource
   *          the datasource
   * @param recordId
   *          the id of the record to lock
   * @throws SQLException
   *           on any error
   */
  private static void unlockNoProcedure(IDataTransaction transaction, SQLDataSource sqlDataSource, IDataRecordId recordId) throws SQLException
  {
    Connection connection = sqlDataSource.getConnection();
    try
    {

      String tablename = recordId.getTableDefinition().getDBName();
      String keyvalue = recordId.getPrimaryKeyValue().toString();
      String userid = transaction.getUser().getLoginId();
      String nodename = ClusterManager.getNodeName();

      // just try to delete lock entry
      //
      String sqlString = "DELETE FROM jacob_locks WHERE tablename=? AND keyvalue=? AND userid=? AND nodename=?";
      PreparedStatement statement = connection.prepareStatement(sqlString);
      try
      {
        statement.setString(1, adjustDBTablename4Locktable(tablename));
        statement.setString(2, keyvalue);
        statement.setString(3, userid);
        statement.setString(4, nodename);

        // and execute statement
        int result = statement.executeUpdate();
        if (1 != result)
        {
          logger.warn(" Lock '" + keyvalue + "' of table '" + tablename + "' on data source '" + sqlDataSource.getName() + "' by user '" + userid
              + "' on node '" + nodename + "' has already been released (result=" + result + ")");
        }
      }
      finally
      {
        statement.close();
      }

      // commit the lock
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
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.adjustment.ILocking#lock(de.tif.jacob.core.data.IDataTransaction,
   *      de.tif.jacob.core.data.impl.DataSource,
   *      de.tif.jacob.core.data.IDataRecordId)
   */
  public void lock(IDataTransaction transaction, DataSource dataSource, IDataRecordId recordId) throws RecordLockedException
  {
    SQLDataSource sqlDataSource = (SQLDataSource) dataSource;
    try
    {
      lockNoProcedure(transaction, sqlDataSource, recordId);
    }
    catch (SQLException e)
    {
      // convert exception
      throw new RuntimeException(e);
    }
  }
  
  private static void checkLocked(Connection connection, IDataRecordId recordId) throws RecordLockedException, SQLException
  {
    String tablename = recordId.getTableDefinition().getDBName();
    String keyvalue = recordId.getPrimaryKeyValue().toString();
    
    String sqlString = "SELECT userid, username, created FROM jacob_locks WHERE tablename=? AND keyvalue=?";
    PreparedStatement statement = connection.prepareStatement(sqlString);
    try
    {
      statement.setString(1, adjustDBTablename4Locktable(tablename));
      statement.setString(2, keyvalue);

      // and execute query to fetch next id, if existing
      ResultSet rs = statement.executeQuery();
      try
      {
        if (rs.next())
        {
          String userid = rs.getString(1);
          String username = rs.getString(2);
          // Timestamp created = rs.getTimestamp(3);
          throw new RecordLockedException(recordId, userid, username);
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

  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.ILocking#checkLocked(de.tif.jacob.core.data.IDataTransaction, de.tif.jacob.core.data.impl.DataSource, de.tif.jacob.core.data.IDataRecordId)
   */
  public void checkLocked(IDataTransaction transaction, DataSource dataSource, IDataRecordId recordId) throws RecordLockedException, RuntimeException
  {
    SQLDataSource sqlDataSource = (SQLDataSource) dataSource;
    try
    {
      Connection connection = sqlDataSource.getConnection();
      try
      {
        checkLocked(connection, recordId);
      }
      finally
      {
        connection.close();
      }
    }
    catch (SQLException e)
    {
      // convert exception
      throw new RuntimeException(e);
    }
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.adjustment.ILocking#unlock(de.tif.jacob.core.data.IDataTransaction,
   *      de.tif.jacob.core.data.impl.DataSource,
   *      de.tif.jacob.core.data.IDataRecordId)
   */
  public void unlock(IDataTransaction trans, DataSource dataSource, IDataRecordId recordId) throws Exception
  {
    SQLDataSource sqlDataSource = (SQLDataSource) dataSource;
    unlockNoProcedure(trans, sqlDataSource, recordId);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.ILocking#getCurrentLocks(de.tif.jacob.core.data.impl.DataSource)
   */
  public List getCurrentLocks(DataSource dataSource) throws Exception
  {
    // IBIS: Was tun mit z.B. "LDAP Locks"?
    if (!(dataSource instanceof SQLDataSource))
      return Collections.EMPTY_LIST;
    
	  SQLDataSource sqlDataSource = (SQLDataSource) dataSource;
    Connection connection = sqlDataSource.getConnection();
    try
    {
      List result = new ArrayList();

      // fetch info concerning existing locks
      //
      String sqlString = "SELECT tablename, keyvalue, nodename, userid, username, created FROM jacob_locks";
      Statement statement = connection.createStatement();
      try
      {
        // execute query to fetch all current locks
        ResultSet rs = statement.executeQuery(sqlString);
        try
        {
          while (rs.next())
          {
            String tablename = rs.getString(1);
            String keyvalue = rs.getString(2);
            String nodename = rs.getString(3);
            String userid = rs.getString(4);
            String username = rs.getString(5);
            Timestamp created = rs.getTimestamp(6);
            result.add(new RecordLock(tablename, keyvalue, created, nodename, userid, username));
          }
        }
        finally
        {
          rs.close();
        }
      }
      catch (SQLException ex)
      {
        // just in case jacob_locks does not exist so far
        if (sqlDataSource.isTableNotExistsErrorCode(ex.getErrorCode()))
        {
          // ignore
        }
        else
        {
          throw ex;
        }
      }
      finally
      {
        statement.close();
      }

      return result;
    }
    finally
    {
      connection.close();
    }
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.ILocking#removeLock(de.tif.jacob.core.data.impl.DataSource, java.lang.String, java.lang.String)
   */
  public void removeLock(DataSource dataSource, String tablename, String keyvalue) throws Exception
  {
    Connection connection = ((SQLDataSource) dataSource).getConnection();
    try
    {
      // just try to delete lock entry
      //
      String sqlString = "DELETE FROM jacob_locks WHERE tablename=? AND keyvalue=?";
      PreparedStatement statement = connection.prepareStatement(sqlString);
      try
      {
        statement.setString(1, tablename);
        statement.setString(2, keyvalue);

        // and execute statement
        int result = statement.executeUpdate();
        if (1 != result)
        {
          logger.info(" Lock '" + keyvalue + "' of table '" + tablename + "' on data source '" + dataSource.getName()
              + "' has already been released (result=" + result + ")");
        }
      }
      finally
      {
        statement.close();
      }

      // commit the lock
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
  }
}
