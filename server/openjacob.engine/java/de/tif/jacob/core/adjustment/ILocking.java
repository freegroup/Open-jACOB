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
package de.tif.jacob.core.adjustment;

import java.util.Date;
import java.util.List;

import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.exception.RecordLockedException;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface ILocking
{
  /**
   * Interface to fetch information about existing record locks.
   * 
   * @author Andreas Sonntag
   */
  public interface IRecordLock
  {
    /**
     * @return timestamp of creation
     */
    public Date created();
    
    /**
     * The id of the user who created the lock.
     * 
     * @return id of the user
     */
    public String getUserId();
    
    /**
     * The name of the user who created the lock.
     * 
     * @return name of the user or <code>null</code>, if not known
     */
    public String getUserName();
    
    /**
     * The database name of the table.
     * 
     * @return table name
     */
    public String getTableName();
    
    /**
     * The key value of the locked record.
     * 
     * @return the key value
     */
    public String getKeyValue();
    
    /**
     * The name of the jACOB node performing the record lock.
     * 
     * @return the node name or <code>null</code>, if not known.
     */
    public String getNodeName();
  }
  
	/**
	 * Checks whether a record needs to be locked.
	 * <p>
	 * Note: By means of this method locking could be skipped for certain tables.
	 * 
	 * @param dataSource
	 *          the data source to execute lock
	 * @param recordId
	 *          the id of the record to lock
	 * @return <code>true</code> if the record has to be locked, otherwise <code>false</code>
	 */
  public boolean doLocking(DataSource dataSource, IDataRecordId recordId);

	/**
	 * Tries to lock the given record for the current user.
	 * 
	 * @param transaction
	 *          the transaction in which context the lock should be acquired
	 * @param dataSource
	 *          the data source to execute lock
	 * @param recordId
	 *          the id of the record to lock
   * @throws RecordLockedException
   *           if the record has already been locked by a different user
   * @throws RuntimeException
   *           if operations fails for any other reason
	 */
	public void lock(IDataTransaction transaction, DataSource dataSource, IDataRecordId recordId) throws RecordLockedException, RuntimeException;

	/**
   * Checks whether the given record is already locked by another transaction,
   * i.e. by a different user.
   * 
   * @param transaction
   *          the transaction in which context the lock should be checked
   * @param dataSource
   *          the data source to check lock for
   * @param recordId
   *          the id of the record to check for lock
   * @throws RecordLockedException
   *           if the record has already been locked by a different user
   * @throws RuntimeException
   *           if operations fails for any other reason
   */
  public void checkLocked(IDataTransaction transaction, DataSource dataSource, IDataRecordId recordId) throws RecordLockedException, RuntimeException;

	/**
	 * Tries to unlock the given record for the current user.
	 * 
	 * @param transaction
	 *          the transaction in which context the lock has been acquired
	 * @param dataSource
	 *          the data source to execute unlock
	 * @param recordId
	 *          the id of the record to unlock
	 * @throws Exception
	 *           if unlocking was not successful
	 */
	public void unlock(IDataTransaction transaction, DataSource dataSource, IDataRecordId recordId) throws Exception;
	
	/**
	 * Fetches the current record locks.
	 * 
	 * @param dataSource
	 * @return <code>List</code> of {@link IRecordLock}
	 * @throws Exception
	 *           a severe problem occurred
	 */
	public List getCurrentLocks(DataSource dataSource) throws Exception;
	
	/**
	 * Removes a record lock given by tablename and key value.
	 * 
	 * @param tablename table name
	 * @param keyvalue key value
	 * @throws Exception
	 *           a severe problem occurred
	 */
	public void removeLock(DataSource dataSource, String tablename, String keyvalue) throws Exception;
}
