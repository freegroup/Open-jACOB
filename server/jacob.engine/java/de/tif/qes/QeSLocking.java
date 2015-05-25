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

package de.tif.qes;

import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import de.tif.jacob.core.adjustment.impl.Locking;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.DataTransaction;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.UndefinedDatasourceException;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.Scheduler;
import de.tif.jacob.scheduler.SchedulerTask;
import de.tif.jacob.scheduler.iterators.SecondsIterator;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QeSLocking extends Locking
{
	static public final transient String RCS_ID = "$Id: QeSLocking.java,v 1.2 2010-08-11 10:39:15 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";
	
	public static final String LOCK_TABLENAME = "qw_locks";

	private static final int time_lock_clean = 180;
	private static final int time_lock_create = 180;
	private static final int BACKGROUND_SLEEP_SECS = 30;
	private static final int try_insert = 5;

	private static Exception error;

	// Set<SQLDataSource>
	private static final Set dataSourcesToClean = new HashSet();

	// Set<RefreshEntry>
	private static final Set refreshEntries = new HashSet();

	public static final Scheduler scheduler = new Scheduler();
	
	static {
		try
		{
			// start background task to clean up and refresh locks
			scheduler.schedule(new LockRefresh(), null);
		}
		catch (Exception th)
		{
			logger.fatal("Initialization failed!", th);
			error = th;
		}
	}

	/**
	 *  
	 */
	public QeSLocking()
	{
		// just to make sure that no instances are created, if static
		// initialisation has failed
		if (null != error)
		{
			throw new RuntimeException("Initialization failed!", error);
		}
	}

	private static boolean examineLockResult(String user, String host, long pid, String result, IDataRecordId recordId) throws RecordLockedException
	{
		// Examine lock result: Return / /USER/HOST/PID/
		StringTokenizer st = new StringTokenizer(result, "/");
		if (st.hasMoreTokens())
		{
			if (st.nextToken().startsWith("Return"))
			{
				if (st.hasMoreTokens())
				{
					st.nextToken();
					if (st.hasMoreTokens())
					{
						String retUser = st.nextToken();
						if (st.hasMoreTokens())
						{
							String retHost = st.nextToken();
							if (st.hasMoreTokens())
							{
								String retPid = st.nextToken();
								if (user.equals(retUser) && host.equals(retHost) && Long.toString(pid).equals(retPid))
								{
									// locking was successful
									return true;
								}

								throw new RecordLockedException(recordId, retUser); //, "host:" + retHost + ", pid:" + retPid);
							}
						}
					}
				}
			}
		}

		throw new RuntimeException("Lock failed: Unexcepted result: " + result);
	}

	private static void cleanLocks(SQLDataSource sqlDataSource) throws SQLException
	{
	  // skip lock handling for datasources with no stored procedure support
	  if (!sqlDataSource.supportsStoredProcedures())
	    return;
	  
		Connection connection = sqlDataSource.getConnection();
		try
		{
			CallableStatement statement = sqlDataSource.buildStoredProcedureStatement(connection, "q_clean_locks", 2);
			try
			{
				//      a_time_lock INTEGER,
				//      a_status OUT INTEGER

				statement.setInt(1, time_lock_clean);
				statement.registerOutParameter(2, java.sql.Types.INTEGER);

				if (logger.isDebugEnabled())
					logger.debug("Trying to clean locks");

				int resInt = statement.executeUpdate();
				int result = statement.getInt(2);

				if (logger.isDebugEnabled())
					logger.debug("Got q_clean_locks result = " + result);
			}
			finally
			{
				statement.close();
			}
		}
		finally
		{
			connection.close();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.ILocking#doLocking(de.tif.jacob.core.data.impl.DataSource, de.tif.jacob.core.data.IDataRecordId)
	 */
	public boolean doLocking(DataSource dataSource, IDataRecordId recordId)
  {
    if (dataSource instanceof SQLDataSource)
    {
      if (((SQLDataSource) dataSource).supportsStoredProcedures())
      {
        // do not create lock entries for modifications on lock table itself
        return !QeSLocking.LOCK_TABLENAME.equalsIgnoreCase(recordId.getTableDefinition().getDBName());
      }
    }
    return false;
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
        String tablename = recordId.getTableDefinition().getDBName();
        String keyvalue = recordId.getPrimaryKeyValue().toString();
        
        String sqlString = "SELECT username, locktime FROM qw_locks WHERE tablename=? AND keyvalue=?";
        PreparedStatement statement = connection.prepareStatement(sqlString);
        try
        {
          statement.setString(1, tablename);
          statement.setString(2, keyvalue);

          // and execute query to fetch next id, if existing
          ResultSet rs = statement.executeQuery();
          try
          {
            if (rs.next())
            {
              String userid = rs.getString(1);
              Timestamp created = rs.getTimestamp(2);
              throw new RecordLockedException(recordId, userid);
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
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.ILocking#lock(de.tif.jacob.core.data.IDataTransaction, de.tif.jacob.core.data.impl.DataSource, de.tif.jacob.core.data.IDataRecordId)
	 */
	public void lock(IDataTransaction trans, DataSource dataSource, IDataRecordId recordId) throws RecordLockedException
	{
	  DataTransaction transaction = (DataTransaction) trans;
		SQLDataSource sqlDataSource = (SQLDataSource) dataSource;

	  // skip lock handling for datasources with no stored procedure support
	  if (!sqlDataSource.supportsStoredProcedures())
	    return;
	  
		try
		{
			Connection connection = sqlDataSource.getConnection();
			try
			{
				CallableStatement statement = sqlDataSource.buildStoredProcedureStatement(connection, "q_create_lock", 8);
				try
				{
					//          a_table VARCHAR2,
					//          a_key VARCHAR2,
					//          a_user VARCHAR2,
					//          a_host VARCHAR2,
					//          a_pid INTEGER,
					//          a_time_lock INTEGER,
					//          a_try_insert INTEGER,
					//          a_lock OUT VARCHAR2

					String table = recordId.getTableDefinition().getDBName();
					String key = recordId.getPrimaryKeyValue().toString();
					String user = transaction.getUser().getLoginId();
					String host = getHostname();
					// use transaction id as PID
					// BUG REPORT 566 from HECC: java.lang.RuntimeException: com.microsoft.sqlserver.jdbc.SQLServerException: Error converting data type bigint to int.
					// Note: qw_locks table is int and not long(int)
					//
          int pid = (int) transaction.getId();
					statement.setString(1, table);
					statement.setString(2, key);
					statement.setString(3, user);
					statement.setString(4, host);
					statement.setInt(5, pid);
					statement.setInt(6, time_lock_create);
					statement.setInt(7, try_insert);
					statement.registerOutParameter(8, java.sql.Types.VARCHAR);

					if (logger.isDebugEnabled())
						logger.debug("Trying to lock table record=" + table + ":" + key + " for user " + user);

					int resInt = statement.executeUpdate();
					String result = statement.getString(8);

					if (logger.isDebugEnabled())
						logger.debug("Got q_create_lock result = " + result);

					examineLockResult(user, host, pid, result, recordId);

					// create entries for background task
					synchronized (QeSLocking.class)
					{
						dataSourcesToClean.add(sqlDataSource);
						refreshEntries.add(new RefreshEntry(transaction, sqlDataSource.getName()));
					}
				}
				finally
				{
					statement.close();
				}
			}
			finally
			{
				connection.close();
			}
		}
		catch (SQLException ex)
		{
			// Hack: MS SQL-Server q_create() throws an exception (error=2627): Cannot insert duplicate key row in object '%.*ls' with unique index '%.*ls'.
			// if the lock is obtained by somebody else.
			// Note: No idea how to hold off the stored procedure to not immediately returning.
			if (2627 == ex.getErrorCode())
			{
				throw new RecordLockedException(recordId, "Unknown");
			}
			logger.error("Creating lock failed!", ex);
			throw new RuntimeException(ex.toString());
		}
		catch (UnknownHostException ex)
		{
			logger.error("Creating lock failed!", ex);
			throw new RuntimeException(ex.toString());
		}
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.adjustment.ILocking#unlock(de.tif.jacob.core.data.IDataTransaction, de.tif.jacob.core.data.impl.DataSource, de.tif.jacob.core.data.IDataRecordId)
	 */
	public void unlock(IDataTransaction trans, DataSource dataSource, IDataRecordId recordId) throws Exception
	{
	  DataTransaction transaction = (DataTransaction) trans;
	  SQLDataSource sqlDataSource = (SQLDataSource) dataSource;

	  // skip lock handling for datasources with no stored procedure support
	  if (!sqlDataSource.supportsStoredProcedures())
	    return;
	  
		try
		{
			Connection connection = sqlDataSource.getConnection();
			try
			{
				CallableStatement statement = sqlDataSource.buildStoredProcedureStatement(connection, "q_drop_lock", 6);
				try
				{
					//          a_table VARCHAR2,
					//          a_key VARCHAR2,
					//          a_user VARCHAR2,
					//          a_host VARCHAR2,
					//          a_pid INTEGER,
					//          a_lock OUT VARCHAR2

					String table = recordId.getTableDefinition().getDBName();
					String key = recordId.getPrimaryKeyValue().toString();
					String user = transaction.getUser().getLoginId();
					String host = getHostname();
          // use transaction id as PID
          // BUG REPORT 566 from HECC: java.lang.RuntimeException: com.microsoft.sqlserver.jdbc.SQLServerException: Error converting data type bigint to int.
          // Note: qw_locks table is int and not long(int)
          //
          int pid = (int) transaction.getId();
					statement.setString(1, table);
					statement.setString(2, key);
					statement.setString(3, user);
					statement.setString(4, host);
					statement.setInt(5, pid);
					statement.registerOutParameter(6, java.sql.Types.VARCHAR);

					if (logger.isDebugEnabled())
						logger.debug("Trying to unlock table record=" + table + ":" + key + " for user " + user);

					int resInt = statement.executeUpdate();
					String result = statement.getString(6);

					if (logger.isDebugEnabled())
						logger.debug("Got q_drop_lock result = " + result);
				}
				finally
				{
					statement.close();
				}
			}
			finally
			{
				connection.close();
			}
		}
		catch (SQLException ex)
		{
			logger.error("Creating lock failed!", ex);
			throw new RuntimeException(ex.toString());
		}
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.ILocking#getCurrentLocks(de.tif.jacob.core.data.impl.DataSource)
   */
  public List getCurrentLocks(DataSource dataSource) throws Exception
  {
	  SQLDataSource sqlDataSource = (SQLDataSource) dataSource;
    Connection connection = sqlDataSource.getConnection();
    try
    {
      List result = new ArrayList();

      // fetch info concerning existing locks
      //
      String sqlString = "SELECT tablename, keyvalue, username, locktime FROM qw_locks";
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
            String userid = rs.getString(3);
            Timestamp created = rs.getTimestamp(4);
            result.add(new RecordLock(tablename, keyvalue, created, null, userid, null));
          }
        }
        finally
        {
          rs.close();
        }
      }
      catch (SQLException ex)
      {
        // just in case qw_locks does not exist
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
      String sqlString = "DELETE FROM qw_locks WHERE tablename=? AND keyvalue=?";
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
  
	/**
	 * @author Andreas
	 *
	 * To change the template for this generated type comment go to
	 * Window - Preferences - Java - Code Generation - Code and Comments
	 */
	private static class LockRefresh extends SchedulerTask
	{
		private final Set namesOfDataSourcesWithProblems = new HashSet();

    public ScheduleIterator iterator()
    {
      return  new SecondsIterator(BACKGROUND_SLEEP_SECS);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.scheduler.SchedulerTask#runPerInstance()
     */
    public boolean runPerInstance()
    {
      return true;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.scheduler.SchedulerTask#getScope()
     */
    public Scope getScope()
    {
      return NODE_SCOPE;
    }
    
		/* (non-Javadoc)
		 * @see de.tif.jacob.scheduler.SchedulerTask#run()
		 */
		public void run() throws Exception
		{
			// fetch entries for background task
			// remark: copy entries to avoid blocking of other threads for longer
			// time
			List cleanList = null;
			List refreshList = null;
			synchronized (QeSLocking.class)
			{
				if (!dataSourcesToClean.isEmpty())
				{
					cleanList = new ArrayList(dataSourcesToClean.size());
					cleanList.addAll(dataSourcesToClean);
				}

				if (!refreshEntries.isEmpty())
				{
					refreshList = new ArrayList(refreshEntries.size());
					refreshList.addAll(refreshEntries);
				}
			}

			namesOfDataSourcesWithProblems.clear();

			// process clean list first
			//
			if (cleanList != null)
			{
				if (logger.isDebugEnabled())
					logger.debug("Cleaning locks for " + cleanList.size() + " datasources");

				for (int i = 0; i < cleanList.size(); i++)
				{
					SQLDataSource dataSource = (SQLDataSource) cleanList.get(i);
					
					// database has been already destroyed, i.e. happens if datasource has
					// been deleted or changed?
					//
					if (dataSource.isDestroyed())
					{
						synchronized (QeSLocking.class)
						{
						  dataSourcesToClean.remove(dataSource);
						}
					  continue;
					}
					
					if (namesOfDataSourcesWithProblems.contains(dataSource.getName()))
					{
						// skip datasource for this loop
						continue;
					}
					
					// clean locks
					try
					{
						cleanLocks(dataSource);
					}
					catch (SQLException ex)
					{
						logger.error("Cleaning locks failed for " + dataSource + ": " + ex.toString());
						namesOfDataSourcesWithProblems.add(dataSource.getName());
					}
				}
			}

			// process refresh list then
			//
			if (refreshList != null)
			{
				if (logger.isDebugEnabled())
					logger.debug("Refreshing locks for " + refreshList.size() + " transactions");

				for (int i = 0; i < refreshList.size(); i++)
				{
					RefreshEntry refreshEntry = (RefreshEntry) refreshList.get(i);
					if (namesOfDataSourcesWithProblems.contains(refreshEntry.getSqlDataSourceName()))
					{
						// skip datasource for this loop
						continue;
					}
					try
					{
						if (DataTransaction.isActive(refreshEntry.transactionId))
						{
							refreshEntry.refreshLocks();
						}
						else
						{
							// remove entry if transaction is not active any more, i.e. no
							// further locks could be created for this transaction
							synchronized (QeSLocking.class)
							{
								refreshEntries.remove(refreshEntry);
							}
						}
					}
					catch (SQLException ex)
					{
						logger.error("Refreshing locks failed for " + refreshEntry.getSqlDataSourceName() + ": " + ex.toString());
						namesOfDataSourcesWithProblems.add(refreshEntry.getSqlDataSourceName());
					}
				}
			}
		}
	}

	/**
	 * Refresh entry for active locks.
	 * 
	 * @author Andreas Sonntag
	 */
	private static class RefreshEntry
	{
		/**
     * Remark: do not remember transaction object itself, since this would avoid
     * to DataTransaction.finalize() to be called
     */
		private final long transactionId;
		private final String user;
		private final String sqlDataSourceName;

		private RefreshEntry(DataTransaction transaction, String sqlDataSourceName)
		{
			this.transactionId = transaction.getId();
			this.user = transaction.getUser().getLoginId();
			this.sqlDataSourceName = sqlDataSourceName;
		}

		private void refreshLocks() throws SQLException, UnknownHostException
		{
		  // fetch datasource
		  //
		  SQLDataSource sqlDataSource;
      try
      {
        sqlDataSource = (SQLDataSource) DataSource.get(this.sqlDataSourceName);
      }
      catch (UndefinedDatasourceException ex)
      {
        // ignore temporary unavailability
        return;
      }
		  
		  // skip lock handling for datasources with no stored procedure support
		  if (!sqlDataSource.supportsStoredProcedures())
		    return;
		  
			Connection connection = sqlDataSource.getConnection();
			try
			{
				CallableStatement statement = sqlDataSource.buildStoredProcedureStatement(connection, "q_refresh_locks", 4);
				try
				{
					//      a_user VARCHAR2,
					//      a_host VARCHAR2,
					//      a_pid INTEGER,
					//      a_status OUT INTEGER

					String host = getHostname();
          // use transaction id as PID
          // BUG REPORT 566 from HECC: java.lang.RuntimeException: com.microsoft.sqlserver.jdbc.SQLServerException: Error converting data type bigint to int.
          // Note: qw_locks table is int and not long(int)
          //
					int pid = (int) this.transactionId;
					statement.setString(1, this.user);
					statement.setString(2, host);
					statement.setInt(3, pid);
					statement.registerOutParameter(4, java.sql.Types.INTEGER);

					if (logger.isDebugEnabled())
						logger.debug("Trying to refresh locks for transaction " + pid + " of user " + user);

					int resInt = statement.executeUpdate();
					int result = statement.getInt(4);

					if (logger.isDebugEnabled())
						logger.debug("Got q_refresh_locks result = " + result);
				}
				finally
				{
					statement.close();
				}
			}
			finally
			{
				connection.close();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object anObject)
		{
			if (this == anObject)
			{
				return true;
			}
			if (anObject instanceof RefreshEntry)
			{
				RefreshEntry another = (RefreshEntry) anObject;

				return this.transactionId == another.transactionId && this.sqlDataSourceName.equals(another.sqlDataSourceName);
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode()
		{
			return 31 * (int) this.transactionId + this.sqlDataSourceName.hashCode();
		}

		/**
		 * @return Returns the sqlDataSource.
		 */
		String getSqlDataSourceName()
		{
			return this.sqlDataSourceName;
		}

		/**
		 * @return Returns the transactionId.
		 */
		long getTransactionId()
		{
			return transactionId;
		}

	}

}
