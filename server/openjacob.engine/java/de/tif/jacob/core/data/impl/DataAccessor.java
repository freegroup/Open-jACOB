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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.ManagedResource;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBERelationConstraint;
import de.tif.jacob.core.data.impl.qbe.QBERelationGraph;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordAction;
import de.tif.jacob.core.data.impl.ta.TAInsertRecordAction;
import de.tif.jacob.core.data.impl.ta.TARecordAction;
import de.tif.jacob.core.data.impl.ta.TAUpdateRecordAction;
import de.tif.jacob.core.data.internal.IDataAccessorInternal;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IAdhocBrowserDefinition;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.IManyToManyRelation;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.IRelation;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * @author Andreas
 * 
 */
public final class DataAccessor implements IDataAccessorInternal
{
	static public final transient String RCS_ID = "$Id: DataAccessor.java,v 1.16 2010/07/13 17:55:23 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.16 $";

	static private final transient Log logger = LogFactory.getLog(DataAccessor.class);

	private final IApplicationDefinition application;
	private final Map browsers;
	private final Map tables;
	private long changeCounter;

  // Map<AbstractTableDefinition->Map<DataKeyValue->DataTableRecord>>
  private final Map keyValueToRecordCacheMap;
  
  /**
   * Needed internally for optional properties (lazy instantiation)
   */
  private Map properties;
  
  public DataAccessor(IApplicationDefinition application)
	{
		// check argument
		if (application == null)
			throw new NullPointerException();

		this.application = application;
		this.browsers = new HashMap();
		this.tables = new HashMap();
    this.keyValueToRecordCacheMap = new HashMap();
    this.changeCounter = 0;
	}
  
  public void actualizeRecordCache(TARecordAction recordAction)
  {
  	DataTableRecord record = recordAction.getRecord();

  	// increment change counters on respective data table and browsers
  	DataTable table = (DataTable) record.getTable(); 
  	table.incrementChangeCounter();
  	table.refreshBrowsers();

  	// actualise record cache
  	if (recordAction instanceof TAInsertRecordAction)
  	{
  	  record.actualizeAfterCommit(recordAction);
  	  
  		addRecordToCache(record);
  	}
  	else if (recordAction instanceof TAUpdateRecordAction)
  	{
  	  record.actualizeAfterCommit(recordAction);
  	  
  		IDataKeyValue oldPrimaryKeyValue = record.getOldPrimaryKeyValue();
  		if (oldPrimaryKeyValue != null)
  		{
  			// since the primary key has been modified
  			// the old cache entry must be relinked and a new one has to be created
  			relinkUpdatedRecordInCache(record, oldPrimaryKeyValue);
  			addRecordToCache(record);
  		}
  	}
  	else if (recordAction instanceof TADeleteRecordAction)
  	{
  		record.markAsDeleted();
  		
  		// delete record from parent
  		table.removeRecord(record);
  		
  		// and from cache
  		deleteRecordFromCache(record);
  	}
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataAccessor#getBrowser(de.tif.jacob.core.definition.IBrowserDefinition)
   */
  public IDataBrowser getBrowser(IBrowserDefinition browserDefinition) throws RuntimeException
  {
    return getBrowser(browserDefinition.getName());
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataAccessor#getBrowser(java.lang.String)
   */
  public IDataBrowser getBrowser(String name)
  {
    IDataBrowser browser = (IDataBrowser) this.browsers.get(name);
    if (null == browser)
    {
      browser = new DataBrowser(this, this.application.getBrowserDefinition(name));
      this.browsers.put(name, browser);
      
      // and register browser at related data table
      getTableInternal(browser.getTableAlias().getName()).add(browser);
    }
    return browser;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataAccessor#createBrowser(de.tif.jacob.core.definition.IAdhocBrowserDefinition)
   */
  public IDataBrowser createBrowser(IAdhocBrowserDefinition browserDefinition)
  {
    return new DataBrowser(this, browserDefinition);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataAccessor#selectRecord(de.tif.jacob.core.data.DataRecordReference)
   */
  public IDataTableRecord selectRecord(DataRecordReference recordReference) throws RecordNotFoundException
	{
    ITableAlias tableAlias = this.application.getTableAlias(recordReference.getTableAliasName());
		return searchTableRecord(tableAlias, recordReference.getPrimaryKeyValue(), false);
	}

	protected DataTableRecord searchTableRecord(ITableAlias tableAlias, IDataKeyValue primaryKeyValue, boolean unconditional) throws RecordNotFoundException
	{
		try
		{
			DataTable table = getTableInternal(tableAlias.getName());
			table.qbeClear();
			table.qbeSetPrimaryKeyValue(primaryKeyValue);
			int maxRecords = table.getMaxRecords();
			// we expect 1 record but we request 2 to make sure that there is only one
			table.setMaxRecords(2);
      try
      {
        if (unconditional)
          table.searchUnconditional();
        else
          table.search();
      }
      finally
      {
        // restore previous setting
        table.setMaxRecords(maxRecords);
      }
			DataTableRecord tableRecord = (DataTableRecord) table.getSelectedRecord();
			if (null == tableRecord)
			{
				// record has been deleted in the mean while
			  
			  // IBIS: Hier kann fälschlicherweise eine Exception auftauchen, da ein Record, welcher unter 
			  // verschiedenen Tablealiasen angesprochen wird, noch nicht committed ist. Beispiel:
			  // HWG Stammdaten in Caretaker. Lösung: ein Caching von DataRecords auch auf Tabellenebene
			  // und nicht nur auf Aliasebene. Macht aber Probleme bei der Implementierung!!!
				throw new RecordNotFoundException(new DataRecordId(table.getTableAlias().getTableDefinition(), primaryKeyValue));
			}
			return tableRecord;
		}
		catch (InvalidExpressionException ex)
		{
			throw new RuntimeException("Unexpected exception", ex);
		}
	}

	protected IKey getForeignKey(ITableAlias tableAlias, ITableAlias foreignTableAlias)
	{
		if (logger.isTraceEnabled())
			logger.trace("Getting foreignKey of " + tableAlias + " to link " + foreignTableAlias);

		// IBIS: enhance performance by not iterating
		IRelationSet relationSet = this.application.getDefaultRelationSet();
		Iterator iter = relationSet.getRelations().iterator();
		while (iter.hasNext())
		{
			IRelation relation = (IRelation) iter.next();
			if (relation.getToTableAlias().equals(tableAlias) && relation.getFromTableAlias().equals(foreignTableAlias) && relation instanceof IOneToManyRelation)
			{
				if (logger.isTraceEnabled())
					logger.trace("Found " + relation);
				return ((IOneToManyRelation) relation).getToForeignKey();
			}
		}
		throw new RuntimeException("Alias " + tableAlias + " has no foreign key to link " + foreignTableAlias);
	}

	protected boolean existsTable(String name)
	{
		return this.tables.containsKey(name);
	}

	protected long getNextChangeCounter()
	{
		return ++this.changeCounter;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataAccessor#getTable(java.lang.String)
   */
  public IDataTable getTable(String name)
  {
    return getTableInternal(name);
  }

  protected DataTable getTableInternal(String name)
  {
    DataTable table = (DataTable) this.tables.get(name);
    if (null == table)
    {
      table = new DataTable(this, this.application.getTableAlias(name));
      this.tables.put(name, table);
    }
    return table;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataAccessor#getTable(de.tif.jacob.core.definition.ITableAlias)
   */
  public IDataTable getTable(ITableAlias alias)
  {
    return getTable(alias.getName());
  }

  public DataTable getTableInternal(ITableAlias alias)
  {
    return getTableInternal(alias.getName());
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataAccessor#clear()
   */
  public void clear()
	{
		if (logger.isTraceEnabled())
			logger.trace("clear()");

    // clear all browsers
    Iterator iter = this.browsers.values().iterator();
    while (iter.hasNext())
    {
      ((IDataBrowser) iter.next()).clear();
    }
    
    // clear all tables
    clearTables();
    
    // clear properties if existing
    if (this.properties != null)
    {
      iter = this.properties.values().iterator();
      while (iter.hasNext())
      {
        Object object = iter.next();
        
        // if we have a managed resource glued to this accessor,
        // release it immediately (do not wait until the corresponding
        // life cycle expires).
        if (object instanceof ManagedResource)
        {
          ((ManagedResource) object).release();
        }
      }
      this.properties.clear();
    }
	}
  
  public void clearTables()
  {
    // clear all tables (Remark: call DataTable.clear() to release transactions, locks, etc.)
    Iterator iter = this.tables.values().iterator();
    while (iter.hasNext())
    {
      IDataTable table = (IDataTable) iter.next();
      table.clear();
      table.qbeClear();
    }
    
    // clear record cache as well
    this.keyValueToRecordCacheMap.clear();
  }
  
  public void clearTables(String relationSetName)
  {
    clearTables(this.application.getRelationSet(relationSetName), false);
  }
  
  public void clearTables(IRelationSet relationSet)
  {
    clearTables(relationSet, false);
  }

  private void clearTables(IRelationSet relationSet, boolean onlyQbe)
  {
    for (Iterator iter = relationSet.getRelations().iterator(); iter.hasNext();)
    {
      IRelation relation = (IRelation) iter.next();
      
      if (relation instanceof IManyToManyRelation)
      {
        IManyToManyRelation mrelation = (IManyToManyRelation) relation;
        clearTables(mrelation.getFromRelation(), onlyQbe);
        clearTables(mrelation.getToRelation(), onlyQbe);
      }
      else
      {
        clearTables(relation, onlyQbe);
      }
    }
  }
  
  private void clearTables(IRelation relation, boolean onlyQbe)
  {
    clearTableWithCache(relation.getFromTableAlias(), onlyQbe);
    clearTableWithCache(relation.getToTableAlias(), onlyQbe);
  }
  
  private void clearTableWithCache(ITableAlias tableAlias, boolean onlyQbe)
  {
    DataTable table = getTableInternal(tableAlias);
    if (onlyQbe)
    {
      table.qbeClear();
    }
    else
    {
      table.clear();
      table.qbeClear();

      ITableDefinition tabledef = tableAlias.getTableDefinition();
      if (this.keyValueToRecordCacheMap.get(tabledef) != null)
      {
        this.keyValueToRecordCacheMap.put(tabledef, null);
      }
    }
  }
  
  public void qbeSetConstraints(IDataFieldConstraints constraints)
  {
    if (constraints != null)
    {  
      // IBIS: This works only because QBESpecification is the only implementation
      // of IDataFieldConstraints
      QBESpecification qbeSpecification = (QBESpecification) constraints;
      
      Iterator iter = qbeSpecification.getAllFieldConstraints();
    	while (iter.hasNext())
      {
        QBEFieldConstraint constraint = (QBEFieldConstraint) iter.next();
    		DataTable table = getTableInternal(constraint.getTableAlias().getName());
        table.setFieldConstraint(constraint);
      }
    }
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataAccessor#qbeClearAll()
	 */
	public void qbeClearAll()
	{
		if (logger.isTraceEnabled())
			logger.trace("qbeClearAll()");

		Iterator iter = this.tables.values().iterator();
		while (iter.hasNext())
		{
			((IDataTable) iter.next()).qbeClear();
		}
	}

  public void qbeClear(String relationSetName)
  {
    clearTables(this.application.getRelationSet(relationSetName), true);
  }
  
  public void qbeClear(IRelationSet relationSet)
  {
    clearTables(relationSet, true);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataAccessor#getApplication()
	 */
	public IApplicationDefinition getApplication()
	{
		return application;
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataAccessor#newAccessor()
	 */
	public IDataAccessor newAccessor()
	{
		return new DataAccessor(this.application);
	}
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataAccessor#newTransaction()
   */
  public IDataTransaction newTransaction()
  {
    // IBIS: Mechanismus für sicheren Close einer Anwendertransaktion fehlt?
    return new DataTransaction(this.application);
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataAccessor#qbeHasConstraint(de.tif.jacob.core.definition.ITableAlias, de.tif.jacob.core.definition.IRelationSet)
	 */
	public boolean qbeHasConstraint(ITableAlias tableAlias, IRelationSet relationSet)
	{
		if (logger.isTraceEnabled())
			logger.trace("qbeHasConstraint(): tableAlias=" + tableAlias + "; relationSet=" + relationSet);
		
		if (qbeHasTableConstraint(tableAlias.getName()))
		{
			return true;
		}

	  // IBIS: Hier sollten eigentlich nur die verbundenen aliase überprüft werden.
		Iterator iter = relationSet.getRelations().iterator();
		while (iter.hasNext())
		{
			IRelation relation = (IRelation) iter.next();
			if (qbeHasTableConstraint(relation.getFromTableAlias().getName()))
			{
				return true;
			}
			if (qbeHasTableConstraint(relation.getToTableAlias().getName()))
			{
				return true;
			}
			if (relation instanceof IManyToManyRelation)
			{
				IManyToManyRelation manyRelation = (IManyToManyRelation) relation;
				if (qbeHasTableConstraint(manyRelation.getIntermediateTableAlias().getName()))
				{
					return true;
				}
			}
		}
		return false;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataAccessor#qbeHasConstraint(java.lang.String, java.lang.String)
   */
  public boolean qbeHasConstraint(String aliasName, String relationSetName)
  {
    ITableAlias alias = this.application.getTableAlias(aliasName);
    IRelationSet relationSet = this.application.getRelationSet(relationSetName);

    return qbeHasConstraint(alias, relationSet);
  }
  
  private boolean qbeHasTableConstraint(String tableAliasName)
	{
		if (existsTable(tableAliasName))
		{
			IDataTable table = getTable(tableAliasName);
			return table.qbeHasConstraint();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataAccessor#propagateRecord(de.tif.jacob.core.data.DataRecord, de.tif.jacob.core.definition.Filldirection)
	 */
	public boolean propagateRecord(IDataRecord record, Filldirection filldirection) throws RecordNotFoundException
	{
	  DataRecord rec = (DataRecord) record;
		QBERelationGraph relationGraph = new QBERelationGraph(this.application.getDefaultRelationSet(), rec.getParent().getTableAlias());
		return null != propagateRecord(relationGraph, filldirection, rec);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataAccessor#propagateRecord(de.tif.jacob.core.data.DataRecord, de.tif.jacob.core.definition.IRelationSet, de.tif.jacob.core.definition.Filldirection)
	 */
	public boolean propagateRecord(IDataRecord record, IRelationSet relationSet, Filldirection filldirection) throws RecordNotFoundException
	{
	  DataRecord rec = (DataRecord) record;
		QBERelationGraph relationGraph = new QBERelationGraph(relationSet, rec.getParent().getTableAlias());
		return null != propagateRecord(relationGraph, filldirection, rec);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataAccessor#propagateRecord(de.tif.jacob.core.data.IDataRecord, java.lang.String, de.tif.jacob.core.definition.Filldirection)
   */
  public boolean propagateRecord(IDataRecord record, String relationSetName, Filldirection filldirection) throws RecordNotFoundException
  {
    return propagateRecord(record, this.application.getRelationSet(relationSetName), filldirection);
  }
  
  public IDataTableRecord loadRecord(ITableAlias tableAlias, IDataKeyValue primaryKey) throws RecordNotFoundException
	{
		return getTableInternal(tableAlias.getName()).loadRecord(primaryKey);
	}
  
  public IDataTableRecord loadRecord(ITableDefinition tableDefinition, IDataKeyValue primaryKey) throws RecordNotFoundException
  {
  	// since a table definition is also a table alias we could do that
  	return getTableInternal(tableDefinition.getName()).loadRecord(primaryKey);
  }
  
  private IDataTableRecord getRecordToPropagate(DataRecord record) throws RecordNotFoundException
  {
  	// --------------------------------------------------------
  	// check whether an uncommitted record should be propagated
  	// --------------------------------------------------------
  	if (record instanceof DataBrowserRecord)
  	{
  		DataTableRecord tableRecord = ((DataBrowserRecord) record).getProvider().getWrappedTableRecord();
      if (tableRecord != null && tableRecord.isNew())
  		{
        // Hack: Falls unterschiedliche Accessor, muß der Record gecloned
        // werden, da der Record auch einen Verweis auf seinen Parent
        // (DataTable) hat und diese sonst dem falschen Accessor zurückliefert.
        if (!this.equals(record.getAccessor()))
        {
          DataTable table = getTableInternal(record.getTableAlias().getName());
          tableRecord = tableRecord.clone(table);
          List res = new ArrayList();
          res.add(tableRecord);
          table.addRecords(res, false);
          table.incrementChangeCounter();
        }
  			return tableRecord;
  		}
  	}
    else if (record instanceof DataTableRecord)
    {
      DataTableRecord tableRecord = (DataTableRecord) record;
      if (tableRecord.isNew())
      {
        // Hack: Falls unterschiedliche Accessor, muß der Record gecloned
        // werden, da der Record auch einen Verweis auf seinen Parent
        // (DataTable) hat und diese sonst dem falschen Accessor zurückliefert.
        if (!this.equals(record.getAccessor()))
        {
          DataTable table = getTableInternal(record.getTableAlias().getName());
          tableRecord = tableRecord.clone(table);
          List res = new ArrayList();
          res.add(tableRecord);
          table.addRecords(res, false);
          table.incrementChangeCounter();
        }
        return tableRecord;
      }
    }
    else if (record instanceof DataMultiUpdateTableRecord)
    {
      return (DataMultiUpdateTableRecord) record;
    }
  	
  	// get primary key value of the given record
  	IDataKeyValue primaryKeyValue = record.getPrimaryKeyValue();
  	if (null == primaryKeyValue)
  	{
  		// table does not have a primary key defined
  		logger.warn("Propagation aborted: No primary key defined for table " + record.getParent().getTableAlias().getTableDefinition().getName());
  		return null;
  	}

  	ITableAlias tableAlias = record.getTableAlias();
  	DataTableRecord tableRecord = searchTableRecord(tableAlias, primaryKeyValue, true);
  	
  	// a propagated record should refresh respective browsers (A. Boeken 30.8.2004)
  	((DataTable) tableRecord.getTable()).refreshBrowsers();
  	
  	return tableRecord;
  }
  
  /**
	 * Attention: For internal use only!!!
	 * 
   * @param relationGraph
   * @param filldirection
   * @param record
   * @return the table record which has been propagated or <code>null</code> if no record exists
   */
  public IDataTableRecord propagateRecord(QBERelationGraph relationGraph, Filldirection filldirection, DataRecord record) throws RecordNotFoundException
	{
		if (logger.isTraceEnabled())
			logger.trace("propagateRecord() called for record: " + record+", filldirection: "+filldirection);

		IDataTableRecord tableRecord = getRecordToPropagate(record);
		if (null == tableRecord)
		{
			return null;
		}

		// hack: needed for browsers filled by
		// DataBrowser.checkForDataTableRecords()
		if (filldirection == null)
			filldirection = Filldirection.BACKWARD;
		if (relationGraph == null)
			relationGraph = new QBERelationGraph(this.application.getDefaultRelationSet(), record.getParent().getTableAlias());
    
		// remember relation graph and filldirection
		DataTable table = getTableInternal(record.getTableAlias().getName());
		table.setRelationGraph(relationGraph);
		table.setFilldirection(filldirection);
		
		// check which directions to traverse
		// remark: both or none is possible!

		if (filldirection.isBackward())
		{
			doBackwardFill(relationGraph, filldirection, record.getTableAlias(), tableRecord);
		}

		if (filldirection.isForward())
		{
			doForwardFill(relationGraph, filldirection, record.getTableAlias(), tableRecord, true);
		}

		if (logger.isTraceEnabled())
			logger.trace("Propagation finished.");

		return tableRecord;
	}

  protected void doBackwardFill(QBERelationGraph relationGraph, Filldirection filldirection, ITableAlias tableAlias, IDataTableRecord newTableRecord) throws RecordNotFoundException
	{
		if (logger.isDebugEnabled())
			logger.debug("doBackwardFill(): Starting for tableAlias=" + tableAlias.getName());

		Iterator iter = relationGraph.getBackwardRelations(tableAlias);
		while (iter.hasNext())
		{
			QBERelationConstraint relation = (QBERelationConstraint) iter.next();
			ITableAlias backwardTableAlias = relation.getFromTableAlias();
			IKey foreignKey = relation.getToForeignKey();

			if (newTableRecord != null)
			{
				IDataKeyValue backwardPrimaryKeyValue = newTableRecord.getKeyValue(foreignKey);
				if (null != backwardPrimaryKeyValue)
				{
					DataTable backwardTable = getTableInternal(backwardTableAlias.getName());
					IDataTableRecord oldBackwardTableRecord = backwardTable.getSelectedRecord();
					
					// do not proceed with backward filling, if the requested backward record is already existing
					// (Note: save some expensive database requests :-)
					if (null == oldBackwardTableRecord || !backwardPrimaryKeyValue.equals(oldBackwardTableRecord.getPrimaryKeyValue()))
					{
						oldBackwardTableRecord = searchTableRecord(backwardTableAlias, backwardPrimaryKeyValue, true);

						// remember relation graph and filldirection
						backwardTable.setRelationGraph(relationGraph);
						backwardTable.setFilldirection(filldirection);
					}

					if (filldirection.isForward())
					{
						// traverse in forward direction which is not recursive
						doForwardFill(relationGraph, filldirection, backwardTableAlias, oldBackwardTableRecord, false);
					}

					// recursive call to traverse in backward direction until no
					// further
					// connected records exist
					doBackwardFill(relationGraph, filldirection, backwardTableAlias, oldBackwardTableRecord);
					

					// and continue with next loop
					continue;
				}
			}

			// clear table to delete old propagations
			IDataTable table = getTable(backwardTableAlias.getName());
			table.clear();

			// proceed with backfilling to clear all connected tables
			if (filldirection.isForward())
			{
				// traverse in forward direction which is not recursive
				doForwardFill(relationGraph, filldirection, backwardTableAlias, null, false);
			}
			doBackwardFill(relationGraph, filldirection, backwardTableAlias, null);
		}

		if (logger.isDebugEnabled())
			logger.debug("doBackwardFill(): Finished for tableAlias=" + tableAlias.getName());
	}

  private void doForwardFill(QBERelationGraph relationGraph, Filldirection filldirection, ITableAlias tableAlias, IDataTableRecord tableRecord, boolean recursive) throws RecordNotFoundException
	{
		if (logger.isDebugEnabled())
			logger.debug("doForwardFill(): Starting for tableAlias=" + tableAlias.getName());

    // IBIS: Auch Rekursion doForwardFill -> doForwardFill!?
		Iterator iter = relationGraph.getForwardRelations(tableAlias);
		while (iter.hasNext())
		{
			QBERelationConstraint relation = (QBERelationConstraint) iter.next();
			if (logger.isDebugEnabled())
				logger.debug("doForwardFill(): relation=" + relation);
			ITableAlias forwardTableAlias = relation.getToTableAlias();

			IKey foreignKey = relation.getToForeignKey();
			if (null != tableRecord)
			{
				IDataKeyValue forwardForeignKeyValue = tableRecord.getPrimaryKeyValue();
				if (null != forwardForeignKeyValue)
				{
					// should always be the case!

					// populate connected table in forward direction
					// remark: this is not recursive!
					try
					{
						DataTable table = getTableInternal(forwardTableAlias.getName());
						table.qbeClear();
						table.qbeSetKeyValue(foreignKey, forwardForeignKeyValue);

						// Unlike a backfill search, we must perform a conditional search
						// then forward filling (needed for mandator feature!)
						table.search();
            
						// remember relation graph and filldirection
						table.setRelationGraph(relationGraph);
						table.setFilldirection(filldirection);
						
						// Clear browsers to display records retrieve by the search operation above
						// Note: via the IDataBrowser.checkForDataTableRecords() mechanism the browsers
						//       will be filled
						// We only do this in forward direction!
						table.clearBrowsers();
						
						if (recursive && filldirection.isBackward())
            {
              // we must proceed with backfilling if there is only one record!
              IDataTableRecord forwardRecord = table.getSelectedRecord();
              if (forwardRecord != null)
              {
                doBackwardFill(relationGraph, filldirection, forwardTableAlias, forwardRecord);
              }
              else
              {
                // clear backward aliases
                Iterator iter2 = relationGraph.getBackwardRelations(forwardTableAlias);
                while (iter2.hasNext())
                {
                  QBERelationConstraint relation2 = (QBERelationConstraint) iter2.next();
                  ITableAlias backwardTableAlias = relation2.getFromTableAlias();
                  
                  // do not clear table of original alias
                  if (backwardTableAlias.equals(tableAlias))
                    continue;

                  // clear table to delete old propagations
                  getTable(backwardTableAlias.getName()).clear();
                }
              }
            }
					}
					catch (InvalidExpressionException ex)
					{
						logger.error("Unexpected exception", ex);
						throw new RuntimeException(ex.toString());
					}

					continue;
				}
			}

			// clear table to delete old propagations
			IDataTable table = getTable(forwardTableAlias);
			table.clear();
		}

		if (logger.isDebugEnabled())
			logger.debug("doForwardFill(): Finished for tableAlias=" + tableAlias.getName());
	}
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataAccessor#cloneRecord(de.tif.jacob.core.data.IDataTransaction, de.tif.jacob.core.data.IDataTableRecord)
	 */
	public IDataTableRecord cloneRecord(IDataTransaction dataTransaction, IDataTableRecord recordToClone)
	{
		if (null == recordToClone)
			throw new NullPointerException("recordToClone is null");
		
		return cloneRecord(dataTransaction, recordToClone, recordToClone.getTableAlias());
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataAccessor#cloneRecord(de.tif.jacob.core.data.IDataTransaction, de.tif.jacob.core.data.IDataTableRecord, java.lang.String)
	 */
	public IDataTableRecord cloneRecord(IDataTransaction dataTransaction, IDataTableRecord recordToClone, String aliasName)
	{
		return cloneRecord(dataTransaction, recordToClone, this.application.getTableAlias(aliasName));
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataAccessor#cloneRecord(de.tif.jacob.core.data.IDataTransaction, de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.definition.ITableAlias)
	 */
	public IDataTableRecord cloneRecord(IDataTransaction dataTransaction, IDataTableRecord recordToClone, ITableAlias alias)
	{
		if (null == dataTransaction)
			throw new NullPointerException("dataTransaction is null");
		
		if (null == recordToClone)
			throw new NullPointerException("recordToClone is null");
		
		if (null == alias)
			throw new NullPointerException("alias is null");
		
		if (!alias.getTableDefinition().equals(recordToClone.getTableAlias().getTableDefinition()))
			throw new RuntimeException("A record of alias " + recordToClone.getTableAlias() + " can not be cloned for alias " + alias);
		
		if (logger.isDebugEnabled())
			logger.debug("cloning record: " + recordToClone);
		
		IDataTable table = getTable(alias);
		IDataTableRecord clonedRecord = table.newRecord(dataTransaction);
		List fields = alias.getTableDefinition().getTableFields();
		ITableField historyField = alias.getTableDefinition().getHistoryField();
		for (int i = 0; i < fields.size(); i++)
    {
      ITableField field = (ITableField) fields.get(i);

      // do not clone the history field
      if (field.equals(historyField))
        continue;

      // do not clone primary key, if already initialized (as autokey or in afterNewAction())
      if (field.isPrimary() && clonedRecord.getValue(field) != null)
        continue;

      Object valueToClone = recordToClone.getValue(field);
      try
      {
        clonedRecord.setValue(dataTransaction, field, valueToClone);
      }
      catch (RecordLockedException ex)
      {
        // should never occure
        throw new RuntimeException("Unexpected state", ex);
      }
      catch (InvalidExpressionException ex)
      {
        // should never occure
        throw new RuntimeException("Unexpected state", ex);
      }
    }

		return clonedRecord;
	}
	
	/**
	 * @param table
	 * @param primaryKeyValue
	 * @return
	 */
	public DataTableRecord getCachedRecord(ITableDefinition table, IDataKeyValue primaryKeyValue)
  {
    if (null == table)
      throw new NullPointerException("table is null");
    
    if (null == primaryKeyValue)
      throw new NullPointerException("Unable to retrieve record from table '" + table.getName() + "': primaryKeyValue is null");
    
    if (logger.isTraceEnabled())
      logger.trace("get: "+table.getName()+"."+primaryKeyValue);
    Map recordMap = (Map) this.keyValueToRecordCacheMap.get(table);
    if (recordMap == null)
      return null;
    return (DataTableRecord) recordMap.get(primaryKeyValue);
  }

  protected void addRecordToCache(IDataTableRecord record)
  {
    IDataKeyValue primaryKeyValue = record.getPrimaryKeyValue();
    if (null != primaryKeyValue)
    {  
      if (logger.isTraceEnabled())
        logger.trace("add: " + record.getTable().getTableAlias().getTableDefinition() + "." + primaryKeyValue);
      
      ITableDefinition table = record.getTable().getTableAlias().getTableDefinition();
      Map recordMap = (Map) this.keyValueToRecordCacheMap.get(table);
      if (recordMap == null)
      {
        recordMap = new HashMap();
        this.keyValueToRecordCacheMap.put(table, recordMap);
      }
      recordMap.put(primaryKeyValue, record);
      
      // update table alias cache as well
      ((DataTable) record.getTable()).addRecordToCache(primaryKeyValue, record);
    }
  }

  private void deleteRecordFromCache(IDataTableRecord record)
  {
    IDataKeyValue primaryKeyValue = record.getPrimaryKeyValue();
    if (null != primaryKeyValue)
    {  
      if (logger.isTraceEnabled())
        logger.trace("delete: " + record.getTable().getTableAlias().getTableDefinition() + "." + primaryKeyValue);
      
      ITableDefinition table = record.getTable().getTableAlias().getTableDefinition();
      Map recordMap = (Map) this.keyValueToRecordCacheMap.get(table);
      if (recordMap != null)
      {
        recordMap.remove(primaryKeyValue);
      }
      
      // update table alias cache as well
      ((DataTable) record.getTable()).deleteRecordFromCache(primaryKeyValue, record);
    }
  }

  private void relinkUpdatedRecordInCache(IDataTableRecord record, IDataKeyValue oldprimaryKeyValue)
  {
  	// IBIS: Remove this hack!
  	// This hack is needed for Browser.WrapperRecord to access records with modified primary keys
  	if (null != oldprimaryKeyValue)
  	{  
      if (logger.isTraceEnabled())
        logger.trace("add: " + record.getTable().getTableAlias().getTableDefinition() + "." + oldprimaryKeyValue);
      
  		ITableDefinition table = record.getTable().getTableAlias().getTableDefinition();
  		Map recordMap = (Map) this.keyValueToRecordCacheMap.get(table);
  		if (recordMap == null)
  		{
  			recordMap = new HashMap();
  			this.keyValueToRecordCacheMap.put(table, recordMap);
  		}
  		recordMap.put(oldprimaryKeyValue, record);
  	}
  }
  
  /**
   * @param constraints the search constraints to apply
   * @param locale the locale used to parse the given search constraints
   */
  public void applySearchConstraints(IDataFieldConstraints constraints, Locale locale)
  {
    // first reset everything
    qbeClearAll();
    clear();
    
    // and set the search constraints
    Iterator iter = constraints.getConstraints();
    while (iter.hasNext())
    {
      IDataFieldConstraint constraint = (IDataFieldConstraint) iter.next();
      
      DataTable table = getTableInternal(constraint.getTableAlias());
      if (constraint.isQbeKeyValue())
        table.qbeSetKeyValue(constraint.getTableField(), constraint.getQbeValue(), locale);
      else
        table.qbeSetValue(constraint.getTableField(), constraint.getQbeValue(), locale);
    }
    
    // and backfill unique records if possible
    iter = this.tables.values().iterator();
    while (iter.hasNext())
    {
      DataTable table = (DataTable) iter.next();
      table.qbeBackfillKeyRecord();
    }
  }
  
  public Object getProperty(String name)
	{
    if (null == name)
      throw new NullPointerException("name is null");
    
    if (this.properties == null)
		{
			return null;
		}
		return this.properties.get(name);
	}

	public void setProperty(String name, Object value)
	{
    if (null == name)
      throw new NullPointerException("name is null");
    
		if (this.properties == null)
		{
			this.properties = new HashMap();
		}
		this.properties.put(name, value);
	}
	
	/**
	 * Needed internally.
	 * 
	 * @return a unique id for this node.
	 */
	public String getId()
	{
	  return "" + this.hashCode();
	}
}
