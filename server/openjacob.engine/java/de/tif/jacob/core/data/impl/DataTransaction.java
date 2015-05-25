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

package de.tif.jacob.core.data.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.ta.TAAction;
import de.tif.jacob.core.data.impl.ta.TADistributedSpecification;
import de.tif.jacob.core.data.impl.ta.TARecordAction;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.security.IUser;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataTransaction extends AbstractDataTransaction implements IDataNewKeysCache
{
	static public transient final String RCS_ID = "$Id: DataTransaction.java,v 1.18 2010/10/27 20:11:50 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.18 $";

	static private final transient Log logger = LogFactory.getLog(IDataTransaction.class);
  
  /**
   * Transaction resources which are shared by a transaction and its nested
   * transactions.
   * 
   * @author Andreas Sonntag
   */
  private static final class Resources
  {
    private Resources(IApplicationDefinition application)
    {
      this.acquiredLocks = new HashMap();
      this.application = application;
      this.user = Context.getCurrent().getUser();
    }
    
    private boolean closed = false;
    
    private final IApplicationDefinition application;
    
    private final IUser user;
    
    // Map<DataRecordId->DataRecordLock>
    private final Map acquiredLocks;

    // Set<DataRecordId>
    private Set omitLockingRecordIds;

    private Timestamp timestamp;
    private boolean resetTimestampOnSavepointRollback;
    
    /**
     * Flag to indicate that the savepoint has been activated, i.e. modification
     * should be rolled back upon setting the savepoint.
     */
    private boolean savepointEnabled = false;
    
    /**
     * Flag to indicate that this transaction is currently committed to avoid
     * endless recursion, e.g. an application programmer might accidently call
     * commit in
     * {@link DataTableRecordEventHandler#beforeCommitAction(IDataTableRecord, IDataTransaction)}.
     */
    private boolean commitActive = false;
    
    private boolean datasourcesCommitted = false;
    
    private Map properties;
    
    /**
     * Will only be set, if properties are changed after the savepoint has been
     * set, i.e. setting properties in
     * {@link DataTableRecordEventHandler#beforeCommitAction(IDataTableRecord, IDataTransaction)}
     * .
     */
    private Map propertiesOnSavepointRollback;
  }
  
	private final TADistributedSpecification transactionSpecification;

  private final Resources resources;
  
  private List prependedTransactions;
  private List appendedTransactions;

	/**
	 * Default constructor.
	 */
  public DataTransaction(IApplicationDefinition application)
  {
    this(new Resources(application));
  }
  
  private DataTransaction(Resources resources)
  {
    this.transactionSpecification = new TADistributedSpecification();
    this.resources = resources;
  }
  
  public IApplicationDefinition getApplication()
  {
    return this.resources.application;
  }
  
  public static boolean isActive(long transactionId)
  {
    return AbstractDataTransaction.isActive(transactionId);
  }
  
  /**
	 * Checks whether the given record is already locked by this transaction.
	 * 
	 * @param record
	 *          the record
	 * @return <code>true</code> if locked otherwise <code>false</code>
	 */
	protected final boolean isAlreadyLocked(DataRecord record)
	{
		if (isValid())
		{
			return this.resources.acquiredLocks.containsKey(record.getId());
		}
		return false;
	}
  
  public final void lock(IDataRecord record) throws RecordLockedException
	{
		if (!isValid())
			throw new IllegalStateException();

		IDataRecordId recordId = record.getId();
		if (recordId.getPrimaryKeyValue() == null)
		{
			// no locking possible for records without an primary key
			return;
		}

    if (this.resources.acquiredLocks.containsKey(recordId))
    {
      // record already locked within this transaction
      return;
    }

    if (this.resources.omitLockingRecordIds != null && this.resources.omitLockingRecordIds.contains(recordId))
    {
      // locking omitted for this record within this transaction
      return;
    }

		DataSource dataSource = DataSource.get(recordId.getTableDefinition().getDataSourceName());
		if (!dataSource.isLockingEnabled(recordId))
		{
			// no locking supported for this datasource
			return;
		}

		// and acquire lock
		this.resources.acquiredLocks.put(recordId, new DataRecordLock(this, dataSource, recordId));
	}

  public final void omitLocking(IDataRecord record)
  {
    if (!isValid())
      throw new IllegalStateException();

    IDataRecordId recordId = record.getId();
    if (recordId.getPrimaryKeyValue() == null)
    {
      // no locking possible for records without an primary key
      return;
    }

    if (this.resources.omitLockingRecordIds == null)
      this.resources.omitLockingRecordIds = new HashSet();

    this.resources.omitLockingRecordIds.add(recordId);
  }

  public final Object getProperty(Object key)
  {
    Object value;
    if (this.resources.properties == null)
      value = null;
    else
      value = this.resources.properties.get(key);

    // get property value from context, if not set for this transaction
    //
    if (value == null)
      value = Context.getCurrent().getProperty(key);

    return value;
  }

  public final Timestamp getTimestamp()
  {
    if (this.resources.timestamp == null)
    {
      this.resources.timestamp = new Timestamp(System.currentTimeMillis());
      
      // erste Anfrage erst unmittelbar vor oder während des Commits, d.h. wenn
      // Savepoint gesetzt? -> Reset timestamp on Savepointrollback
      this.resources.resetTimestampOnSavepointRollback = this.resources.savepointEnabled;
    }
    return this.resources.timestamp;
  }

  public final void setProperty(Object key, Object value)
  {
    if (!isValid())
      throw new IllegalStateException();

    if (this.resources.properties == null)
      this.resources.properties = new HashMap();
    
    if (this.resources.savepointEnabled && this.resources.propertiesOnSavepointRollback == null)
    {
      // save the properties before savepoint has been set
      this.resources.propertiesOnSavepointRollback = new HashMap();
      this.resources.propertiesOnSavepointRollback.putAll(this.resources.properties);
    }
    
    this.resources.properties.put(key,value);
  }

  public final void checkLocked(IDataRecordId recordId) throws RecordLockedException
	{
		if (!isValid())
			throw new IllegalStateException();

		if (recordId.getPrimaryKeyValue() == null)
		{
			// no locking possible for records without an primary key
			return;
		}

		if (this.resources.acquiredLocks.containsKey(recordId))
		{
			// record already locked within this transaction
			return;
		}

    if (this.resources.omitLockingRecordIds != null && this.resources.omitLockingRecordIds.contains(recordId))
    {
      // locking omitted for this record within this transaction
      return;
    }

		DataSource dataSource = DataSource.get(recordId.getTableDefinition().getDataSourceName());
		if (!dataSource.isLockingEnabled(recordId))
		{
			// no locking supported for this datasource
			return;
		}

		// check locked
		dataSource.checkLocked(this, recordId);
	}

  public final boolean isValid()
  {
    return !isClosed() && !this.resources.datasourcesCommitted;
  }

  public final boolean isClosed()
  {
    return this.resources.closed;
  }
  
  protected DataTransaction getRootTransaction()
  {
    return this;
  }

  protected final IDataTableRecord getRecord(ITableAlias tableAlias, IDataKeyValue primaryKey)
  {
    if (isClosed())
    {
      return null;
    }
    
    IDataTableRecord record = this.transactionSpecification.getRecord(tableAlias, primaryKey);
    if (record != null)
      return record;

    return getRootTransaction().getRecordFromEmbedded(tableAlias, primaryKey);
  }
  
  private IDataTableRecord getRecordFromEmbedded(ITableAlias tableAlias, IDataKeyValue primaryKey)
  {
    IDataTableRecord record = this.transactionSpecification.getRecord(tableAlias, primaryKey);
    if (record != null)
      return record;

    if (this.prependedTransactions != null)
    {
      for (int i = 0; i < this.prependedTransactions.size(); i++)
      {
        record = ((DataTransaction) this.prependedTransactions.get(i)).getRecordFromEmbedded(tableAlias, primaryKey);
        if (record != null)
          return record;
      }
    }
    if (this.appendedTransactions != null)
    {
      for (int i = 0; i < this.appendedTransactions.size(); i++)
      {
        record = ((DataTransaction) this.appendedTransactions.get(i)).getRecordFromEmbedded(tableAlias, primaryKey);
        if (record != null)
          return record;
      }
    }
    return null;
  }
  
  /**
   * Internal method for History!
   * 
   * @param tableDefinition
   * @param primaryKey
   * @return
   */
  public final IDataTableRecord getRecord(ITableDefinition tableDefinition, IDataKeyValue primaryKey)
  {
    if (isClosed())
    {
      return null;
    }

    IDataTableRecord record = this.transactionSpecification.getRecord(tableDefinition, primaryKey);
    if (record != null)
      return record;

    return getRootTransaction().getRecordFromEmbedded(tableDefinition, primaryKey);
  }
  
  private IDataTableRecord getRecordFromEmbedded(ITableDefinition tableDefinition, IDataKeyValue primaryKey)
  {
    IDataTableRecord record = this.transactionSpecification.getRecord(tableDefinition, primaryKey);
    if (record != null)
      return record;

    if (this.prependedTransactions != null)
    {
      for (int i = 0; i < this.prependedTransactions.size(); i++)
      {
        record = ((DataTransaction) this.prependedTransactions.get(i)).getRecordFromEmbedded(tableDefinition, primaryKey);
        if (record != null)
          return record;
      }
    }
    if (this.appendedTransactions != null)
    {
      for (int i = 0; i < this.appendedTransactions.size(); i++)
      {
        record = ((DataTransaction) this.appendedTransactions.get(i)).getRecordFromEmbedded(tableDefinition, primaryKey);
        if (record != null)
          return record;
      }
    }
    return null;
  }
  
  protected final TARecordAction getRecordAction(IDataTableRecord record)
	{
		if (isClosed())
		{
			return null;
		}
		return this.transactionSpecification.getRecordAction(record);
	}

	protected final void addAction(TAAction action)
	{
    if (null == action)
      throw new NullPointerException("action is null");
    if (this.resources.savepointEnabled)
      action.setEnableForSavepointRollback();
    if (logger.isTraceEnabled())
      logger.trace("Transaction "+this+": adding action: "+action);
		this.transactionSpecification.addAction(action, isSavepointEnabled());
	}

	protected final void removeAction(TAAction action)
	{
    if (logger.isTraceEnabled())
      logger.trace("Transaction "+this+": remove action: "+action);
    this.transactionSpecification.removeAction(action);
	}
  
  private void rollbackRecordChanges()
  {
    this.transactionSpecification.rollback();
    
    if (this.prependedTransactions != null)
    {
      for (int i = 0; i < this.prependedTransactions.size(); i++)
      {
        ((DataTransaction) this.prependedTransactions.get(i)).rollbackRecordChanges();
      }
    }
    if (this.appendedTransactions != null)
    {
      for (int i = 0; i < this.appendedTransactions.size(); i++)
      {
        ((DataTransaction) this.appendedTransactions.get(i)).rollbackRecordChanges();
      }
    }
  }

	public void close()
  {
    // Check if already closed
    if (this.resources.closed == false)
    {
      // Transaction successfully committed?
      if (!this.resources.datasourcesCommitted)
      {
        // No -> Restore record values
        rollbackRecordChanges();
      }

      // release all required locks
      Iterator iter = this.resources.acquiredLocks.values().iterator();
      while (iter.hasNext())
      {
        DataRecordLock recordLock = (DataRecordLock) iter.next();
        recordLock.unlock();
      }
      this.resources.acquiredLocks.clear();

      unregister();
      this.resources.closed = true;
    }
  }

	protected final List getActionsToExecute(String dataSourceName)
  {
    List actions = new ArrayList();
    getRootTransaction().addActionsToExecute(dataSourceName, actions);
    return actions;
  }
  
  private void addActionsToExecute(String dataSourceName, List actions)
  {
    if (this.prependedTransactions != null)
    {
      for (int i = 0; i < this.prependedTransactions.size(); i++)
      {
        ((DataTransaction) this.prependedTransactions.get(i)).addActionsToExecute(dataSourceName, actions);
      }
    }

    this.transactionSpecification.addActionsToExecute(dataSourceName, actions);

    if (this.appendedTransactions != null)
    {
      for (int i = 0; i < this.appendedTransactions.size(); i++)
      {
        ((DataTransaction) this.appendedTransactions.get(i)).addActionsToExecute(dataSourceName, actions);
      }
    }
  }
  
  private void checkOpen()
  {
    if (isClosed())
      throw new RuntimeException("Transaction has already been committed or closed");
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTransaction#commit()
	 */
	public void commit() throws UserException, Exception
  {
    checkOpen();

    if (this.resources.commitActive)
      throw new IllegalStateException("Transaction is already being committed");

    this.resources.commitActive = true;
    try
    {
      if (logger.isTraceEnabled())
        logger.trace("Transaction " + this + ":committing ..");

      // set savepoint because we want to rollback all additional
      // modifications (done in beforeCommitHook) in case of an
      // exception
      //
      setSavepoint();
      try
      {
        commitInternal();
      }
      catch (Exception ex)
      {
        if (isValid())
          rollbackToSavepoint();

        throw ex;
      }
    }
    finally
    {
      this.resources.commitActive = false;
    }

    if (logger.isTraceEnabled())
      logger.trace("Transaction " + this + ":commit finished.");
  }

  
  private void addDataSourceNames(Set dataSourceNames)
  {
    this.transactionSpecification.addDataSourceNames(dataSourceNames);
    
    if (this.prependedTransactions!=null)
    {
      for (int i=0; i<this.prependedTransactions.size();i++)
      {
        ((DataTransaction) this.prependedTransactions.get(i)).addDataSourceNames(dataSourceNames);
      }
    }
    
    if (this.appendedTransactions!=null)
    {
      for (int i=0; i<this.appendedTransactions.size();i++)
      {
        ((DataTransaction) this.appendedTransactions.get(i)).addDataSourceNames(dataSourceNames);
      }
    }
  }
  
  private void commitInternal() throws UserException, Exception
  {
    List executedDataSources = new ArrayList();
    try
    {
      List actionsForAfterCommit = new ArrayList();
      
      // STEP 1: invoke user specific before commit hooks
      //
      {
        Set dataSourceNames = new HashSet();
        addDataSourceNames(dataSourceNames);
        Iterator iter = dataSourceNames.iterator();
        while (iter.hasNext())
        {
          // fetch next datasource to execute
          String dataSourceName = (String) iter.next();
          List hookActions = getActionsToExecute(dataSourceName);
          for (int i = 0; i < hookActions.size(); i++)
          {
            TAAction action = (TAAction) hookActions.get(i);
            if (action instanceof TARecordAction)
            {
              TARecordAction recordAction = (TARecordAction) action;
              if (recordAction.isSkipCallingEventHandler())
                continue;

              DataTableRecord record = recordAction.getRecord();
              record.getEventHandler().beforeCommitAction(record, record.getCurrentTransaction());
            }
          }
          actionsForAfterCommit.add(hookActions);
        }
      }
      
      // STEP 2: execute actions on all data sources
      //
      Set dataSourceNames = new HashSet();
      // Note: The number of datasources returned could be different from step 1.
      // This is the case, if a record of a another datasource is modified in 
      // beforeCommitAction().
      addDataSourceNames(dataSourceNames);
      Iterator iter = dataSourceNames.iterator();
      while (iter.hasNext())
      {
        // fetch next datasource to execute
        String dataSourceName = (String) iter.next();
        DataSource dataSource = DataSource.get(dataSourceName);

        // execute all transaction actions (within the same transaction
        // context)
        dataSource.execute(this);

        // mark datasource as executed
        executedDataSources.add(dataSourceName);
      }

      // all data sources executed -> close transaction
      this.resources.datasourcesCommitted = true;

      // STEP 3: invoke user specific after commit hooks
      //
      try
      {
        for (int j = 0; j < actionsForAfterCommit.size(); j++)
        {
          List hookActions = (List) actionsForAfterCommit.get(j);

          for (int i = 0; i < hookActions.size(); i++)
          {
            TAAction action = (TAAction) hookActions.get(i);
            if (action instanceof TARecordAction)
            {
              TARecordAction recordAction = (TARecordAction) action;
              if (recordAction.isSkipCallingEventHandler())
                continue;

              DataTableRecord record = recordAction.getRecord();
              record.getEventHandler().afterCommitAction(record);
            }
          }
        }
      }
      catch (Exception ex)
      {
        Context context = Context.getCurrent();
        if (context instanceof IClientContext)
        {
          // do not rethrow exception but show problem to the user (i.e. no
          // error hiding)
          ExceptionHandler.handle((IClientContext) context, ex);
        }
        else
        {
          // release a warning because we do not know the context (i.e.
          // severity)
          if (logger.isWarnEnabled())
            logger.warn(ex.toString());

          // rethrow
          throw ex;
        }
      }
    }
    finally
    {
      if (this.resources.datasourcesCommitted)
      {
        // close transaction anyhow, even if after commit hooks (partly) failed
        close();
      }
      else
      {
        // not all data sources have been successfully executed (committed).
        // Therefore, mark the actions of the successfully executed datasources
        // as finished to avoid to execute them once more.
        // Note: This must be done after calling after commit hooks! Because
        // otherwise methods like record.isNew() would be unreliable!
        //
        for (int j = 0; j < executedDataSources.size(); j++)
        {
          String dataSourceName = (String) executedDataSources.get(j);
          markActionsAsExecuted(dataSourceName);
        }
      }
    }
	}
  
  private void markActionsAsExecuted(String dataSourceName)
  {
    this.transactionSpecification.markActionsAsExecuted(dataSourceName);
    
    if (this.prependedTransactions!=null)
    {
      for (int i=0; i<this.prependedTransactions.size();i++)
      {
        ((DataTransaction) this.prependedTransactions.get(i)).markActionsAsExecuted(dataSourceName);
      }
    }
    
    if (this.appendedTransactions!=null)
    {
      for (int i=0; i<this.appendedTransactions.size();i++)
      {
        ((DataTransaction) this.appendedTransactions.get(i)).markActionsAsExecuted(dataSourceName);
      }
    }
  }
  
	/**
   * @return Returns the savepointEnabled.
   */
  public final boolean isSavepointEnabled()
  {
    return this.resources.savepointEnabled;
  }
  
  public final void setSavepoint()
  {
    checkOpen();
    
    this.resources.savepointEnabled = true;
  }
  
  public final void rollbackToSavepoint()
  {
    checkOpen();
    
    if (this.resources.savepointEnabled)
    {
      getRootTransaction().rollbackToSavepointEmbedded();
      
      if (this.resources.timestamp != null && this.resources.resetTimestampOnSavepointRollback)
        this.resources.timestamp = null;

      // If transaction properties have been changed after setting the savepoint,
      // they have to be reset to the state before setting the savepoint.
      if (this.resources.propertiesOnSavepointRollback != null)
      {
        this.resources.properties = this.resources.propertiesOnSavepointRollback;
        this.resources.propertiesOnSavepointRollback = null;
      }

      this.resources.savepointEnabled = false;
    }
  }
  
  private void rollbackToSavepointEmbedded()
  {
    this.transactionSpecification.rollbackToSavepoint();
    
    if (this.prependedTransactions != null)
    {
      for (int i = 0; i < this.prependedTransactions.size(); i++)
      {
        ((DataTransaction) this.prependedTransactions.get(i)).rollbackToSavepointEmbedded();
      }
    }
    if (this.appendedTransactions != null)
    {
      for (int i = 0; i < this.appendedTransactions.size(); i++)
      {
        ((DataTransaction) this.appendedTransactions.get(i)).rollbackToSavepointEmbedded();
      }
    }
  }

	protected void finalize() throws Throwable
	{
		// call close to release all resources (i.e. locks)
		close();
	}

	/**
	 * @return Returns the user.
	 */
	
	public final IUser getUser()
	{
		return this.resources.user;
	}
  
  /**
   * Embedded transaction.
   * 
   * @author Andreas Sonntag
   */
  private static final class EmbeddedTransaction extends DataTransaction
  {
    private final DataTransaction parent;
    
    private EmbeddedTransaction(DataTransaction parent)
    {
      super(parent.resources);
      this.parent = parent;
    }
    
    public void close()
    {
      // close all connected transactions
      this.parent.close();
    }

    public void commit() throws UserException, Exception
    {
      // commit all connected transactions
      this.parent.commit();
    }

    protected DataTransaction getRootTransaction()
    {
      return this.parent.getRootTransaction();
    }

    protected void finalize() throws Throwable
    {
      // do nothing here since close() will be called by parent transaction
    }
  }

  public final IDataTransaction newEmbeddedTransaction(EmbeddedTransactionMode mode)
  {
    if (!isValid())
      throw new IllegalStateException();

    EmbeddedTransaction embedded = new EmbeddedTransaction(this);

    if (mode == EmbeddedTransactionMode.APPEND)
    {
      if (this.appendedTransactions == null)
        this.appendedTransactions = new ArrayList();
      this.appendedTransactions.add(embedded);
    }
    else if (mode == EmbeddedTransactionMode.PREPEND)
    {
      if (this.prependedTransactions == null)
        this.prependedTransactions = new ArrayList();
      this.prependedTransactions.add(embedded);
    }
    else
      // should never occur
      throw new IllegalArgumentException(mode.toString());

    return embedded;
  }
  
  /**
   * Cache entry for primary keys.
   * 
   * @author Andreas Sonntag
   */
  private static final class NewKeyCacheEntry
  {
    private int increment;
    private long nextKey;
    private int keyCount;
    
    private NewKeyCacheEntry(int increment)
    {
      this.increment = increment;
      this.keyCount = 0;
    }
  }
  
  /**
   * Map<ITableDefinition.getName()->NewKeyCacheEntry>
   */
  private Map newKeyCacheMap = null;

  public final void setHintForNewRecordNumber(String alias, int newRecordNumber)
  {
    setHintForNewRecordNumber(Context.getCurrent().getApplicationDefinition().getTableAlias(alias), newRecordNumber);
  }

  public final void setHintForNewRecordNumber(ITableAlias alias, int newRecordNumber)
  {
    if (this.newKeyCacheMap == null)
      this.newKeyCacheMap = new HashMap();

    NewKeyCacheEntry entry = (NewKeyCacheEntry) this.newKeyCacheMap.get(alias.getTableDefinition().getName());

    if (newRecordNumber > 1)
    {
      if (entry == null)
      {
        this.newKeyCacheMap.put(alias.getTableDefinition().getName(), new NewKeyCacheEntry(newRecordNumber));
      }
      else
      {
        // consider if keys are still in cache
        int increment = newRecordNumber - entry.keyCount;
        entry.increment = Math.max(increment, 1);
      }
    }
    else if (entry != null)
    {
      entry.increment = 1;
    }
  }

  public final long newKey(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCreator creator) throws Exception
  {
    // only treat primary key fields (pkeys)
    if (this.newKeyCacheMap != null && field.isPrimary() && table.getPrimaryKey().getTableFields().size() == 1)
    {
      NewKeyCacheEntry entry = (NewKeyCacheEntry) this.newKeyCacheMap.get(table.getName());
      if (entry != null)
      {
        if (entry.keyCount > 0)
        {
          entry.keyCount--;
          return entry.nextKey++;
        }
        else if (entry.increment > 1)
        {
          // get next N keys
          entry.nextKey = creator.createNewKeys(dataSource, table, field, entry.increment);
          entry.keyCount = entry.increment - 1;
          // reset again, i.e. setHintForNewRecordNumber() must be called each
          // time
          entry.increment = 1;
          return entry.nextKey++;
        }
      }
    }

    // default: do not cache keys and get them one by one
    return creator.createNewKeys(dataSource, table, field, 1);
  }
}
