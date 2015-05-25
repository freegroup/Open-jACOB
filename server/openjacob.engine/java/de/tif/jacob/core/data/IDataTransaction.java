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

package de.tif.jacob.core.data;

import java.sql.Timestamp;
import java.util.Locale;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.security.IUser;

/**
 * A data transaction provides the ability to flush (commit) multiple record
 * modifications at once by means of calling {@link #commit()}. With a single
 * transaction record modifications in multiple data sources could be performed.
 * <p>
 * Note that the actual implementation does not support 2-phase commit
 * functionality so far. This means that there is no guarantee for atomic
 * modifications across multiple data sources, i.e. if commit fails not all
 * modifications might be rolled back completely. Nevertheless, this
 * functionality is guaranteed for modifications on a single data source which
 * supports transaction handling as most relational databases do.
 * <p>
 * There is no (explicit) rollback method nor functionality, since modifications
 * are not written to a data source until {@link #commit()} is called.
 * Nevertheless, a rollback is executed implicitly on data source transactions
 * obtained during commit, in case any underlying data source operation fails.
 * <p>
 * Data transaction can not be reused, i.e. data transactions are invalid after
 * {@link #commit()} or {@link #close()} has been invoked.
 * <p>
 * {@link #close()} should be called explicitly on data transactions obtained by
 * {@link IDataAccessor#newTransaction()} to ensure that transaction resources
 * (i.e. record locks) are released properly in error case. Please, look at the
 * following example how to do this correctly:
 * 
 * <pre>
 * IDataTransaction transaction = accessor.newTransaction();
 * try
 * {
 *   IDataTable requestTable = accessor.getTable(&quot;request&quot;);
 *   IDataTableRecord requestRecord = requestTable.newRecord(transaction);
 * 
 *   requestRecord.setValue(transaction, &quot;createtime&quot;, &quot;now&quot;);
 *   // do further modifications..
 * 
 *   // try to commit
 *   transaction.commit();
 * }
 * finally
 * {
 *   // in case of an exception close() will be called as well   
 *   transaction.close();
 * }
 * </pre>
 * 
 * <p>
 * A data transaction instance could be obtained by means of
 * <li>{@link IDataAccessor#newTransaction()},
 * <li>{@link IDataTableRecord#getCurrentTransaction()},
 * <li>{@link IDataTable#getTableTransaction()},
 * <li>{@link IDataTable#startNewTransaction()},
 * <li>{@link IDataTransaction#newEmbeddedTransaction(de.tif.jacob.core.data.IDataTransaction.EmbeddedTransactionMode)}.
 * 
 * @author andreas
 */
public interface IDataTransaction
{
  /**
   * Locks the given record within the context of this transaction. The lock
   * will be implicitly released again, if the transaction is either committed
   * or closed.
   * 
   * @param record
   *          the record to lock
   * @throws RecordLockedException
   *           if the record is already locked within the context of another
   *           transaction.
   */
  public void lock(IDataRecord record) throws RecordLockedException;
  
  /**
   * Prevents that the given record will be locked within the context of this
   * transaction. This is useful to update a record in the case, that another
   * transaction has already (pessimistically) locked the record.
   * <p>
   * 
   * Explanation: Subsequent calls of setter methods such like
   * {@link IDataTableRecord#setValue(IDataTransaction, String, Object)} will
   * never fail because the record is already locked.
   * 
   * @param record
   *          the record to omit locking
   * @since 2.7.4
   */
  public void omitLocking(IDataRecord record);
  
	/**
   * Flushes all modifications to the underlying data sources and commits them
   * as well.
   * 
   * @throws UserException
   *           This kind of exception (normally) indicates that the commit
   *           operation is aborted due to a failed consistency check.
   *           Consistency checks could be performed in table hooks as well and
   *           should always lead to a <code>UserException</code>, if the
   *           data to be committed is not consistent in a certain way. See also
   *           {@link DataTableRecordEventHandler#beforeCommitAction(IDataTableRecord, IDataTransaction)}.
   * @throws Exception
   *           in case of any other error
   */
	public void commit() throws UserException, Exception;

	/**
   * Closes the transaction which ensures that all transaction resources are
   * released properly.
   */
	public void close();
	
  /**
   * Returns the unique transaction id.
   * <p>
   * Note: This id is unique for the respective jACOB application server
   * instance, but not unique across a jACOB application server cluster!
   * 
   * @return the transaction id.
   */
  public long getId();
  
	/**
   * Checks whether this transaction is valid.
   * 
   * @return <code>true</code> if the transaction is valid, otherwise
   *         <code>false</code>
   */
	public boolean isValid();
	
	/**
   * Returns the user who has initiated this transaction.
   * 
   * @return the transaction user
   */
	public IUser getUser();
  
  /**
   * Returns the locale of the current application.
   * 
   * @return Locale of the current application
   * @see Context#getApplicationLocale()
   * @since 2.7.2
   */
  public Locale getApplicationLocale();
  
  /**
   * Returns the transaction timestamp.
   * <p>
   * Note: The timestamp is created upon the first request of this method, i.e.
   * subsequent requests will return the same timestamp.
   * 
   * @return The transaction timestamp
   * @since 2.7.2
   */
  public Timestamp getTimestamp();
  
  /**
   * Sets a property for transaction life time.
   * 
   * @param key
   *          the property key
   * @param value
   *          the property value or <code>null</code> to reset the property
   * @see #getProperty(Object)
   * @since 2.7.2
   */
  public void setProperty(Object key, Object value);

  /**
   * Gets a property value.
   * <p>
   * In contrast to {@link #setProperty(Object, Object)} this method can be
   * called after a transaction has been committed or closed.
   * <p>
   * Note: If a property value has been set by means
   * {@link #setProperty(Object, Object)}, this value will be returned.
   * Otherwise, if a property value exits for the current context (see
   * {@link Context#getProperty(Object)}), this value will be returned in a
   * subordinated manner.
   * 
   * @param key
   *          the property key
   * @return the requested property value or <code>null</code>
   * @see #setProperty(Object, Object)
   * @since 2.7.2
   */
  public Object getProperty(Object key);
  
  /**
   * Gives the transaction a hint of how many new records of a given table alias
   * will be created in the following by means of this transaction.
   * <p>
   * 
   * This method can improve the performance then inserting multiple records of
   * the same table alias within a single transaction.
   * <p>
   * 
   * Important note: <code>newRecordNumber</code> should be set as accurate as
   * possible to avoid "gaps" for record pkeys. For example: If
   * <code>newRecordNumber</code> is simply set to 100 and just 3 records are
   * created, the pkeys could be 1023, 1024, 1025, GAP, 1123.
   * 
   * @param alias
   *          the table alias for which records are created in the following
   * @param newRecordNumber
   *          the number of records of the given table alias
   * @since 2.8.1
   */
  public void setHintForNewRecordNumber(ITableAlias alias, int newRecordNumber);
  
  /**
   * Gives the transaction a hint of how many new records of a given table alias
   * will be created in the following by means of this transaction.
   * <p>
   * 
   * This method can improve the performance then inserting multiple records of
   * the same table alias within a single transaction.
   * <p>
   * 
   * Important note: <code>newRecordNumber</code> should be set as accurate as
   * possible to avoid "gaps" for record pkeys. For example: If
   * <code>newRecordNumber</code> is simply set to 100 and just 3 records are
   * created, the pkeys could be 1023, 1024, 1025, GAP, 1123.
   * 
   * @param alias
   *          the name of the table alias for which records are created in the following
   * @param newRecordNumber
   *          the number of records of the given table alias
   * @since 2.8.1
   */
  public void setHintForNewRecordNumber(String alias, int newRecordNumber);
  
  /**
   * Mode for embedded transactions.
   * 
   * @see IDataTransaction#newEmbeddedTransaction(de.tif.jacob.core.data.IDataTransaction.EmbeddedTransactionMode)
   * @author Andreas Sonntag
   * @since 2.8
   */
  public static final class EmbeddedTransactionMode
  {
    /**
     * Using this mode actions of an embedded transaction will be committed <b>before</b> its counterparts in the parent transaction.
     */
    public static final EmbeddedTransactionMode PREPEND = new EmbeddedTransactionMode("prepend");
    
    /**
     * Using this mode actions of an embedded transaction will be committed <b>after</b> its counterparts in the parent transaction.
     */
    public static final EmbeddedTransactionMode APPEND = new EmbeddedTransactionMode("append");

    private final String name;

    private EmbeddedTransactionMode(String name)
    {
      this.name = name;
    }

    public String toString()
    {
      return this.name;
    }
  }

  /**
   * Creates a new embedded transaction of this transaction (parent
   * transaction). Embedded transactions will be committed and closed implicitly
   * when {@link #commit()} or {@link #close()} is called on the parent
   * transaction. Nevertheless, committing or closing an embedded transaction,
   * will either commit or close its parent transaction and also underlying
   * embedded transactions of the embedded transaction (if existing) and so
   * forth.
   * 
   * @param mode
   *          the embedded transaction mode
   * @return the embedded transaction
   * @since 2.8
   */
  public IDataTransaction newEmbeddedTransaction(EmbeddedTransactionMode mode);
}
