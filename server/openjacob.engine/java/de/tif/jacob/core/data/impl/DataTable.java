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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTableSearchIterateCallback;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.qbe.QBEField;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBERelationConstraint;
import de.tif.jacob.core.data.impl.qbe.QBERelationGraph;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordsAction;
import de.tif.jacob.core.data.impl.ta.TARecordAction;
import de.tif.jacob.core.data.internal.IDataTableInternal;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableAliasCondition;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserRuntimeException;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class DataTable extends DataRecordSet implements IDataTableInternal
{
	static public transient final String RCS_ID = "$Id: DataTable.java,v 1.29 2010/10/26 23:30:41 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.29 $";

	static private final transient Log logger = LogFactory.getLog(DataTable.class);

	private final ITableAlias alias;
  
	// Note: Needed for browsers filled by
	// DataBrowser.checkForDataTableRecords() and for updateSelectedRecord()
	private QBERelationGraph relationGraph;
	private IRelationSet relationSet;
	private Filldirection filldirection;
	
  /**
   * Map<DataKeyValue->DataTableRecord>
   */
  private final Map keyValueToRecordMap;
  
	/**
	 * Map{ITableField->QBEFieldConstraint}
	 */
	private Map qbeConstraintMap;
	
	private boolean qbeOptional;

	private IDataTransaction tableTransaction;
	
	// all DataBrowsers of the same table alias
	private Set browsers;
	
	private DataRecordMode oldSelectedRecordMode = DataRecordMode.NORMAL;

	/**
	 * @param parent
	 * @param alias
	 */
	protected DataTable(DataAccessor parent, ITableAlias alias)
	{
		super(parent);
		this.alias = alias;
		this.tableTransaction = null;
		this.filldirection = null;
		this.relationGraph = null;
		this.relationSet = null;
		this.keyValueToRecordMap = new HashMap();
    this.qbeConstraintMap = new HashMap();
    this.qbeOptional = false;
  }
	
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataRecordSet#getChangeCount()
   */
  public long getChangeCount()
  {
    // Hack um den change counter zu verändern, wenn ein Record vom update in den selected Modus
    // (normal Modus) geschaltet wird, aber keine Änderung (d.h. keine TARecordAction) wirksam war.
    //
    DataRecord selectedRecord = (DataRecord) getSelectedRecord();
    DataRecordMode mode = selectedRecord == null ? DataRecordMode.NORMAL : selectedRecord.getMode();
    if (oldSelectedRecordMode != mode)
    {
      incrementChangeCounter();
      oldSelectedRecordMode = mode;
    }
    
    return super.getChangeCount();
  }
  
	protected void add(IDataBrowser browser)
	{
	  if (this.browsers == null)
	  {
	    this.browsers = new HashSet();
	  }
	  this.browsers.add(browser);
	}
	
	protected void refreshBrowsers()
	{
	  if (this.browsers != null)
	   {
	    Iterator iter = this.browsers.iterator();
	    while (iter.hasNext())
	     {
	      DataBrowser browser = (DataBrowser) iter.next();
	      browser.incrementChangeCounter();
	    }
	  }
	}
	
	protected void clearBrowsers()
	{
	  if (this.browsers != null)
	   {
	    Iterator iter = this.browsers.iterator();
	    while (iter.hasNext())
	     {
	      DataBrowser browser = (DataBrowser) iter.next();
	      browser.clearInternal();
	    }
	  }
	}
	
	/**
	 * Returns the name of this table, i.e. the name of a data table equals to
	 * the name of the related table alias.
	 * 
	 * @return the data table name.
	 */
	public String getName()
	{
		return this.alias.getName();
	}

	protected DataRecord instantiateRecord(DataSource dataSource, DataTableRecordEventHandler eventHandler, int[] primaryKeyIndices, Object[] values, int seqNbr)
  {
    DataTableRecord tableRecord = new DataTableRecord(this, dataSource, primaryKeyIndices, values);
    try
    {
      if (eventHandler == null || eventHandler.filterSearchAction(tableRecord))
        return tableRecord;
      return null;
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

	protected IDataTableRecord getCachedRecord(IDataKeyValue primaryKeyValue)
	{
    if (null == primaryKeyValue)
      throw new NullPointerException("Unable to retrieve record from table alias '" + getName() + "': primaryKeyValue is null");
    
    if (logger.isTraceEnabled())
      logger.trace("get: "+getName()+"."+primaryKeyValue);
    
    return (DataTableRecord) this.keyValueToRecordMap.get(primaryKeyValue);
  }

  /**
   * Will be called by DataAccessor only!
   * 
   * @param primaryKeyValue
   * @param record
   */
  protected void addRecordToCache(IDataKeyValue primaryKeyValue, IDataTableRecord record)
  {
    if (logger.isTraceEnabled())
      logger.trace("add: " + record.getTable().getTableAlias() + "." + primaryKeyValue);

    this.keyValueToRecordMap.put(primaryKeyValue, record);
  }

  /**
   * Will be called by DataAccessor only!
   * 
   * @param primaryKeyValue
   * @param record
   */
  protected void deleteRecordFromCache(IDataKeyValue primaryKeyValue, IDataTableRecord record)
  {
    if (logger.isTraceEnabled())
      logger.trace("delete: " + record.getTable().getTableAlias() + "." + primaryKeyValue);

    this.keyValueToRecordMap.remove(primaryKeyValue);
  }

  public ITableAlias getTableAlias()
	{
		return this.alias;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTable#qbeSetPrimaryKeyValue(de.tif.jacob.core.data.IDataKeyValue)
	 */
	public void qbeSetPrimaryKeyValue(IDataKeyValue primaryKeyValue)
	{
		ITableDefinition tableDefinition = this.alias.getTableDefinition();
		qbeSetKeyValue(tableDefinition.getPrimaryKey(), primaryKeyValue);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTable#qbeSetKeyValue(de.tif.jacob.core.definition.IKey, de.tif.jacob.core.data.IDataKeyValue)
   */
  public void qbeSetKeyValue(IKey key, IDataKeyValue keyValue)
	{
		List keyFields = key.getTableFields();
		for (int i = 0; i < keyFields.size(); i++)
		{
			ITableField keyField = (ITableField) keyFields.get(i);

			if (null == keyValue)
				qbeSetKeyValue(keyField, null);
			else
			  // convert to external value, e.g. DataLongText -> String
				qbeSetKeyValue(keyField, keyField.getType().convertDataValueToObject(keyValue.getFieldValue(i)));
		}
	}
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTable#qbeSetKeyValue(java.lang.String, java.lang.Object)
   */
  public void qbeSetKeyValue(String tableFieldName, Object value) throws NoSuchFieldException
  {
  	qbeSetKeyValue(this.alias.getTableDefinition().getTableField(tableFieldName), value);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTable#qbeSetKeyValue(de.tif.jacob.core.definition.ITableField, java.lang.Object)
   */
  public void qbeSetKeyValue(ITableField tableField, Object value)
  {
    qbeSetKeyValue(tableField, value, null);
  }
  
  public void qbeSetKeyValue(ITableField tableField, Object value, Locale locale)
  {
    // check for null and empty string
    if (value == null || ((value instanceof String) && ((String) value).length() == 0))
      qbeSetValue(tableField, "NULL");
    else
      qbeSetValue(tableField, value, true, locale, null);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTable#qbeSetValue(java.lang.String, java.lang.Object)
   */
  public void qbeSetValue(String tableFieldName, Object value) throws NoSuchFieldException
  {
  	qbeSetValue(this.alias.getTableDefinition().getTableField(tableFieldName), value, false, null, null);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTable#qbeSetValue(de.tif.jacob.core.definition.ITableField, java.lang.Object)
   */
  public void qbeSetValue(ITableField tableField, Object value)
  {
  	qbeSetValue(tableField, value, false, null, null);
  }
  
  public void qbeSetValue(ITableField tableField, Object value, Locale locale)
  {
  	qbeSetValue(tableField, value, false, locale, null);
  }
  
  /**
	 * Sets a QBE value for the given table field an registers the name of the
	 * gui element which provides the value.
	 * <p>
	 * Note: This method is used internally to properly backfill report
	 * constraints to the screen.
	 * 
	 * @param guiElementName
	 *          the name of the gui element
	 * @param tableField
	 *          the table field
	 * @param value
	 *          the QBE value to set
   * @param locale
   *          the locale used to parse the QBE constraint value or
   *          <code>null</code>
	 */
	public void qbeSetGuiElementValue(String guiElementName, ITableField tableField, Object value, Locale locale)
	{
		qbeSetValue(tableField, value, false, locale, guiElementName);
	}
  
	/**
	 * General internal QBE constraint set method.
	 * 
   * @param tableField
   *          the constrained table field
   * @param value
   *          the QBE constraint value
   * @param exactMatch
   *          <code>true</code> means <code>value</code> should be treated
   *          as exact match value, otherwise <code>false</code>
   * @param locale
   *          the locale used to parse the QBE constraint value or
   *          <code>null</code>
   * @param guiElementName
   *          the name of the constrained gui element or <code>null</code>
	 */
	private void qbeSetValue(ITableField tableField, Object value, boolean exactMatch, Locale locale, String guiElementName)
	{
		// argument checks
		if (tableField == null)
			throw new NullPointerException("tableField is null");
		
		if (!this.alias.getTableDefinition().hasTableField(tableField))
			throw new RuntimeException(tableField + " is not a table field of "+ this.alias);
		
		// treat null as unconstrained
		// IBIS: throw exception if value is null?
    if (value == null)
      return;
    
    // treat empty strings or strings containing only white space as null!
		if ((value instanceof String) && ((String) value).trim().length() == 0)
		{
			return;
		}
		
//		// IBIS: Hack remove quickly: avoid search with reference
//		try
//		{
//			if (tableField.getType() instanceof LongTextFieldType || tableField.getType() instanceof BinaryFieldType)
//			{
//				// just allow : !NULL , NULL, etc.
//				if ((value instanceof String) && ((String) value).length() > 8)
//				{
//					return;
//				}
//			}
//		}
//		catch (Exception ex)
//		{
//		}

		if (logger.isDebugEnabled())
			logger.debug("qbeSetValue(): alias " + alias + ": " + tableField + "=" + value+".");

    QBEFieldConstraint qbeConstraint = new QBEFieldConstraint(this.alias, tableField, value, exactMatch, locale, guiElementName);
    QBEFieldConstraint oldQbeConstraint = (QBEFieldConstraint) this.qbeConstraintMap.get(tableField);
		if (null == oldQbeConstraint)
		{
			this.qbeConstraintMap.put(tableField, qbeConstraint);
		}
		else
		{
      oldQbeConstraint.add(qbeConstraint);
		}
	}
  
  protected void setFieldConstraint(QBEFieldConstraint constraint)
  {
    this.qbeConstraintMap.put(constraint.getTableField(), constraint);
  }

	protected void addFieldConstraints(QBESpecification spec, QBERelationGraph relationGraph)
  {
    addFieldConstraints(spec, relationGraph, true);
  }
	
	protected void addFieldConstraints(QBESpecification spec, QBERelationGraph relationGraph, boolean useCondition)
	{
		if (logger.isTraceEnabled())
			logger.trace("addFieldConstraints() called for table " + this);

		boolean isLocalAlias = this.alias.equals(spec.getAliasToSearch());
		
		boolean fieldConstraintsAdded = false;
		boolean fieldNonOrGroupConstraintAdded = false;
		Iterator iter = this.qbeConstraintMap.keySet().iterator();
		while (iter.hasNext())
		{
			ITableField field = (ITableField) iter.next();
			QBEFieldConstraint qbeConstraint = (QBEFieldConstraint) this.qbeConstraintMap.get(field);

			// Checks whether the given field is part of the primary key and should
			// be NULL ..
      if (!isLocalAlias && field.isPrimary() && qbeConstraint.isNullCheck())
      {
        // in this case the NULL condition is interpreted as an inverse
        // relation constraint, e.g.
        // NOT EXISTS (SELECT * FROM workgrouphwg workgrouphwg WHERE
        // workgrouphwg.workgroup_key=workgroup.pkey)
        // instead of
        // workgrouphwg.hwg_key IS NULL
        // which would never be the case
        QBERelationConstraint inverseRelation = relationGraph.getRelationToInitial(this.alias);
        spec.addInverse(inverseRelation, qbeConstraint);
        spec.addRelationConstraints(relationGraph.markAsConstrained(inverseRelation.getFromTableAlias()));
        continue;
      }

			// mark constraint as optional
			if (this.qbeOptional && !isLocalAlias)
			  qbeConstraint.setOptional();
			  
			spec.add(qbeConstraint);
			fieldConstraintsAdded = true;
			
			if (!fieldNonOrGroupConstraintAdded && !qbeConstraint.isOrGroupCheck())
        fieldNonOrGroupConstraintAdded = true;
		}

    // TODO: Outerjoin wenn keine Constraints aber condition da ist!!!
		if (relationGraph != null && fieldConstraintsAdded)
		{
		  // local alias conditions are added anyhow (but for backfill they are omitted!)
		  if (!isLocalAlias && useCondition && !isPrimaryKeyConstrained())
        spec.addCondition(this.alias, DataSource.get(this.alias.getTableDefinition().getDataSourceName()), relationGraph);
      
		  // Kein Innerjoin erzwingen, wenn nur OR-Group-Konstraints vorhanden (d.h. Konstraints welche mit "|" beginnen) 
			spec.addRelationConstraints(relationGraph.markAsConstrained(this.alias, this.qbeOptional || !fieldNonOrGroupConstraintAdded));
		}
	}

	public IDataTableRecord getRecord(int index) throws IndexOutOfBoundsException
	{
		return (IDataTableRecord) getRecordInternal(index);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTable#reloadSelectedRecord()
	 */
	public IDataTableRecord reloadSelectedRecord() throws RecordNotFoundException
	{
		IDataTableRecord record = getSelectedRecord();
		if (null == record)
		{
			return null;
		}
		IDataKeyValue keyValue = record.getPrimaryKeyValue();
		if (null == keyValue)
		{
			// we can not reload records with no primary key defined
			return record;
		}

		// refresh record from data source
		DataTableRecord reloadedRecord = (DataTableRecord) loadRecord(keyValue);

		// and overwrite old selected record
		setRecord(0, reloadedRecord);

		incrementChangeCounter();

		return reloadedRecord;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTable#getSelectedRecord()
	 */
	public IDataTableRecord getSelectedRecord()
	{
		if (this.recordCount() == 1)
		{
			return getRecord(0);
		}
		return null;
	}

	private void closeTransaction()
	{
		if (null != this.tableTransaction)
		{
			this.tableTransaction.close();
			this.tableTransaction = null;
		}
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTable#startNewTransaction()
	 */
	public IDataTransaction startNewTransaction()
	{
		closeTransaction();
		this.tableTransaction = new DataTransaction(getAccessor().getApplication());
		return this.tableTransaction;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTable#getTableTransaction()
	 */
	public IDataTransaction getTableTransaction()
  {
    IDataTransaction trans = this.tableTransaction;

    if (trans == null)
    {
      // AS 20.11.2008: Wegen Abwärtskompatibilität ggfs. die Transaktion eines
      // neuen Records zurückgeben.
      IDataTableRecord selectedRecord = getSelectedRecord();
      if (selectedRecord != null && selectedRecord.isNew())
        trans = selectedRecord.getCurrentTransaction();
    }

    if (trans != null && trans.isValid())
      return trans;

    return null;
  }

  public IDataTableRecord updateSelectedRecord(String relationSetName) throws RecordLockedException, RecordNotFoundException
  {
    return updateSelectedRecord(getAccessor().getApplication().getRelationSet(relationSetName));
  }
  
  public IDataTableRecord updateSelectedRecord(IRelationSet relationSet) throws RecordLockedException, RecordNotFoundException
  {
    IDataTableRecord selectedRecord = getSelectedRecord();
    if (selectedRecord == null)
      return null;
    if (selectedRecord.isNewOrUpdated())
      return selectedRecord;
    QBERelationGraph relationGraphToUse = new QBERelationGraph(relationSet, getTableAlias());
    return updateSelectedRecord(startNewTransaction(), relationGraphToUse, Filldirection.BACKWARD);
  }
  
  /**
   * Internal method for ActionTypeHandler.
   * 
   * @param trans
   * @return
   * @throws RecordLockedException
   * @throws RecordNotFoundException
   */
  public IDataTableRecord updateSelectedRecord(IDataTransaction trans) throws RecordLockedException, RecordNotFoundException
  {
    QBERelationGraph relationGraphToUse = getRelationGraph();
    if (relationGraphToUse == null || relationGraphToUse.getRelationSet().isLocal())
      relationGraphToUse = new QBERelationGraph(this.getAccessor().getApplication().getDefaultRelationSet(), this.getTableAlias());
    return updateSelectedRecord(trans, relationGraphToUse, Filldirection.BACKWARD);
  }
  
  private IDataTableRecord updateSelectedRecord(IDataTransaction trans, QBERelationGraph relationGraphToUse, Filldirection filldirectionToUse) throws RecordLockedException, RecordNotFoundException
	{
		if (logger.isTraceEnabled())
			logger.trace("updateSelectedRecord() called for table=" + this);

		DataTableRecord record = (DataTableRecord) getSelectedRecord();
		if (null == record)
		{
			throw new RuntimeException("No record to update existing");
		}

		if (record.isNew())
		{
			return record;
		}

    // check for primary key
		IDataKeyValue primaryKey = record.getPrimaryKeyValue();
		if (null == primaryKey)
		{
			throw new RuntimeException("No update possible for records with no primary key defined");
		}

		// perform lock!
		DataTransaction transaction = (DataTransaction) trans;
		transaction.lock(record);

		// reload record from data source
		DataTableRecord reloadedRecord = (DataTableRecord) loadRecord(primaryKey);
		
		// --------------------------------------------------------------------------------------
		// and backfill the record to also refresh related records, which might have been changed
		// --------------------------------------------------------------------------------------
		getAccessorInternal().doBackwardFill(relationGraphToUse, filldirectionToUse, getTableAlias(), reloadedRecord);
		
		// replace record by newly retrieved record
		clearRecordsInternal();
		addRecord(0, reloadedRecord);
    incrementChangeCounter();

    if (this.tableTransaction != transaction)
    {
      closeTransaction();
      
      // AS 20.11.2008: Problem von andherz, wenn in einem TabContainer mit
      // unterschiedlichen Aliasen ein Record wieder verworfen werden soll, mit
      // IDataTableRecord.delete() und IDataTable.clear() und ansonsten die
      // Transaktion geschlossen wird.
      
      // do NOT make the transaction the table transaction
      // this.tableTransaction = transaction;
    }
    
		// and return the record
		reloadedRecord.setCurrentTransaction(transaction);
		return reloadedRecord;
	}

	public IDataTableRecord getRecord(IDataKeyValue primaryKey) throws RecordNotFoundException
  {
    // check whether the selected record is the desired one
    IDataTableRecord record = getSelectedRecord();
    if (record != null && primaryKey.equals(record.getPrimaryKeyValue()))
      return record;

    // lookup in cache then
    record = this.getCachedRecord(primaryKey);
    if (record != null)
      return record;

    // finally try to load from data source
    return loadRecord(primaryKey);
  }
	
	public IDataTableRecord loadRecord(IDataKeyValue primaryKey) throws RecordNotFoundException
	{
    if (logger.isTraceEnabled())
      logger.trace("loadRecord(): table=" + this +"; primaryKey=" + primaryKey);

		qbeClear();
    qbeSetPrimaryKeyValue(primaryKey);
    try
    {
      IDataSearchResult result = performRecordSearch(null, null, false);
      if (result.getRecords().size() == 1)
      {
        return (IDataTableRecord) result.getRecords().get(0);
      }
    }
    catch (InvalidExpressionException e)
    {
      // should never occur
      throw new RuntimeException(e);
    }
    
    throw new RecordNotFoundException(new DataRecordId(getTableAlias().getTableDefinition(), primaryKey));
  }
  
  public IDataTableRecord setSelectedRecord(IDataKeyValue primaryKey) throws RecordNotFoundException
  {
    // check whether the selected record is already the desired one
    IDataTableRecord selectedRecord = getSelectedRecord(); 
    if (selectedRecord != null && primaryKey.equals(selectedRecord.getPrimaryKeyValue()))
      return selectedRecord;
    
    // load from database
    //
    DataTableRecord record = (DataTableRecord) loadRecord(primaryKey);
    
    // make the record the selected record
    //
    clearRecordsInternal();
    addRecord(0, record);
    incrementChangeCounter();

    return record;
  }

  public void setSelectedRecord(IDataTableRecord record) throws Exception
  {
    // argument checks
    if (record == null)
      throw new NullPointerException("Invalid argument. Hands over record must be != null");
    
    if(record.isNewOrUpdated())
    {
      // make the record the selected record
      //
      clearRecords();
      addRecord(0, (DataRecord) record);
      incrementChangeCounter();
    }
    else
    {
      setSelectedRecord(record.getPrimaryKeyValue());
    }
  }
  
  public void clear()
  {
    if (logger.isTraceEnabled())
      logger.trace("clear() called for table " + this);

    this.filldirection = null;
    this.relationGraph = null;
    this.relationSet = null;

    // Clear record cache 
    this.keyValueToRecordMap.clear();

    closeTransaction();
    clearRecordsInternal();
    incrementChangeCounter();
  }

  public void clearRecords()
  {
    if (logger.isTraceEnabled())
      logger.trace("clearRecords() called for table " + this);

    this.filldirection = null;
    this.relationGraph = null;
    this.relationSet = null;

    // Do not clear record cache 
    // this.keyValueToRecordMap.clear();

    // Do not close transaction
    this.tableTransaction = null;
    clearRecordsInternal();
    incrementChangeCounter();
  }

	public void qbeClear()
	{
		if (logger.isTraceEnabled())
			logger.trace("qbeClear() called for table " + this);

		this.qbeConstraintMap.clear();
		this.qbeOptional = false;
	}
  
  public void qbeClear(ITableField tableField)
  {
    // argument checks
    if (tableField == null)
      throw new NullPointerException("tableField is null");
    
    if (!this.alias.getTableDefinition().hasTableField(tableField))
      throw new RuntimeException(tableField + " is not a table field of "+ this.alias);
    
    if (logger.isDebugEnabled())
      logger.debug("qbeClear(): alias " + alias + ": " + tableField+".");

    this.qbeConstraintMap.remove(tableField);
  }

  public void qbeClear(String tableFieldName) throws NoSuchFieldException
  {
    qbeClear(this.alias.getTableDefinition().getTableField(tableFieldName));
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTable#qbeSetOptional()
   */
  public void qbeSetOptional()
  {
		this.qbeOptional = true;
  }
  
	public boolean qbeHasConstraint()
	{
		return !this.qbeConstraintMap.isEmpty();
	}

  public boolean qbeHasConstraint(ITableField tableField)
  {
    // argument checks
    if (tableField == null)
      throw new NullPointerException("tableField is null");
    
    if (!this.alias.getTableDefinition().hasTableField(tableField))
      throw new RuntimeException(tableField + " is not a table field of "+ this.alias);
    
    if (logger.isDebugEnabled())
      logger.debug("qbeHasConstraint(): alias " + alias + ": " + tableField+".");

    return this.qbeConstraintMap.containsKey(tableField);
  }

  public boolean qbeHasConstraint(String tableFieldName) throws NoSuchFieldException
  {
    return qbeHasConstraint(this.alias.getTableDefinition().getTableField(tableFieldName));
  }

  public long count() throws InvalidExpressionException
  {
    return performRecordCount(null);
  }

  public long count(IRelationSet relationSet) throws InvalidExpressionException
  {
    return performRecordCount(relationSet);
  }

  public long count(String relationSetName) throws InvalidExpressionException
  {
    IRelationSet relationSet = getAccessor().getApplication().getRelationSet(relationSetName);
    return count(relationSet);
  }

  /* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTable#exists()
	 */
	public boolean exists() throws InvalidExpressionException
	{
		return performRecordCount(null) > 0;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTable#exists(java.lang.String, java.lang.Object)
	 */
	public boolean exists(String tableFieldName, Object value) throws InvalidExpressionException, NoSuchFieldException
	{
		return exists(this.alias.getTableDefinition().getTableField(tableFieldName), value);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTable#exists(de.tif.jacob.core.definition.ITableField, java.lang.Object)
	 */
	public boolean exists(ITableField tableField, Object value) throws InvalidExpressionException
	{
		// remember all QBE settings
		Map tempMap = this.qbeConstraintMap;

		try
		{
			// reinit qbe collection
			this.qbeConstraintMap = new HashMap();

			// set search constraints
			qbeSetValue(tableField, value, true, null, null);

			// and check for existence of records
			return exists();
		}
		finally
		{
			// restore QBE settings
			this.qbeConstraintMap = tempMap;
		}
	}

	private long performRecordCount(IRelationSet relationSet) throws InvalidExpressionException
	{
		if (logger.isTraceEnabled())
      logger.trace("performRecordCount(): table " + this + ", relationSet " + relationSet);

    DataSource dataSource = DataSource.get(this.alias.getTableDefinition().getDataSourceName());
		QBESpecification spec = new QBESpecification(this.alias);

    // ----------------------------------------
    // handle relation constraints for alias conditions (e.g. "agent.status>0")
    // ----------------------------------------
    ITableAliasCondition tableAliasCondition = this.alias.getCondition();
    if (tableAliasCondition != null)
    {
      QBERelationGraph relationGraph = null;
      if (tableAliasCondition.getForeignAliases().size() > 0)
      {
        // IBIS: Nicht immer wieder QBERelationGraph konstruieren
        relationGraph = new QBERelationGraph(getAccessor().getApplication().getDefaultRelationSet(), this.alias);
      }
      spec.addCondition(this.alias, dataSource, relationGraph);
    }

    // ----------------------------------------
    // determine field constraints
    // ----------------------------------------
    if (relationSet == null)
    {
      addFieldConstraints(spec, null);
    }
    else
    {
      // IBIS: besserer Mechanismus für dieses RelationGraph Gedönse
      QBERelationGraph relationGraph = new QBERelationGraph(relationSet, this.alias);
      Iterator iter = relationGraph.getConnectedTableAliases().iterator();
      while (iter.hasNext())
      {
        ITableAlias alias = (ITableAlias) iter.next();

        if (logger.isTraceEnabled())
          logger.trace("performRecordSearch(): looking for field constraints of alias: " + alias.getName());

        if (getAccessorInternal().existsTable(alias.getName()))
        {
          DataTable table = (DataTable) getAccessor().getTable(alias.getName());
          table.addFieldConstraints(spec, relationGraph);
        }
      }
    }

		// ----------------------------------------
		// execute query
		// ----------------------------------------
		return dataSource.count(this, spec);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTable#searchAndDelete(de.tif.jacob.core.data.IDataTransaction)
	 */
	public int searchAndDelete(IDataTransaction trans) throws InvalidExpressionException, RecordLockedException
	{
		int oldSetting = setMaxRecords(DataRecordSet.UNLIMITED_RECORDS);
    try
    {
      int recordCount = search(null, null, true, true);
      for (int i = 0; i < recordCount(); i++)
      {
        getRecord(i).delete(trans);
      }
      return recordCount;
    }
    finally
    {
      // restore old setting
      setMaxRecords(oldSetting);
    }
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTable#fastDelete(de.tif.jacob.core.data.IDataTransaction)
	 */
	public void fastDelete(IDataTransaction trans)
	{
		if (logger.isDebugEnabled())
			logger.debug("fastDelete(): table " + this);

    ITableDefinition tableDefinition = this.alias.getTableDefinition();
    DataSource dataSource = DataSource.get(tableDefinition.getDataSourceName());

		QBESpecification spec = new QBESpecification(this.alias);
	  spec.addCondition(this.alias, dataSource, null);

		// ----------------------------------------
		// determine field constraints
		// ----------------------------------------
		addFieldConstraints(spec, null);

		// ----------------------------------------
		// and register delete operation
		// ----------------------------------------
		((DataTransaction) trans).addAction(new TADeleteRecordsAction(tableDefinition.getDataSourceName(), spec));
	}

	public int search() throws InvalidExpressionException
	{
		return search(null, null, true, true);
	}


  public int search(IRelationSet relationSet) throws InvalidExpressionException
  {
		return search(relationSet, null, true, true);
  }
  
  public int search(String relationSetName) throws InvalidExpressionException
  {
		IRelationSet relationSet = getAccessor().getApplication().getRelationSet(relationSetName);
		return search(relationSet);
  }
  
	protected int searchUnconditional() throws InvalidExpressionException
	{
		return search(null, null, true, false);
	}

  public int searchAndIterate(IDataTableSearchIterateCallback callback, IRelationSet relationSet) throws InvalidExpressionException
  {
    if (callback == null)
      throw new NullPointerException("callback is null");
    return search(relationSet, callback, true, true);
  }

  public int searchAndIterate(IDataTableSearchIterateCallback callback, String relationSetName) throws InvalidExpressionException
  {
    IRelationSet relationSet = getAccessor().getApplication().getRelationSet(relationSetName);
    return searchAndIterate(callback, relationSet);
  }

  public int searchAndIterate(IDataTableSearchIterateCallback callback) throws InvalidExpressionException
  {
    if (callback == null)
      throw new NullPointerException("callback is null");
    return search(null, callback, true, true);
  }

  /**
	 * Internal search method.
	 * 
	 * @param relationSet relation set or <code>null</code>
   * @param callback callback or <code>null</code>
   * @param doClear
	 * @param useCondition
	 * @return the number of records retrieved
	 * @throws InvalidExpressionException
	 */
	private int search(IRelationSet relationSet, IDataTableSearchIterateCallback callback, boolean doClear, boolean useCondition) throws InvalidExpressionException
	{
		if (doClear)
    {
      this.tableTransaction = null;
			clearRecordsInternal();
    }

		// temporarly set unlimited records, if a callback is present
		int origMaxRecords = 0;
    if (callback != null)
      origMaxRecords = setMaxRecords(UNLIMITED_RECORDS);
    try
    {
      IDataSearchResult searchResult = performRecordSearch(relationSet, callback, useCondition);
      addRecords(searchResult.getRecords(), searchResult.hasMore());
      incrementChangeCounter();
      return searchResult.getRecordCount();
    }
    finally
    {
      if (callback != null)
        setMaxRecords(origMaxRecords);
    }
	}
	
	/**
	 * Helper class to "cast" from IDataSearchIterateCallback to IDataTableSearchIterateCallback.
	 * 
	 * @author Andreas Sonntag
	 */
	private static final class Callback implements IDataSearchIterateCallback
  {
    private final IDataTableSearchIterateCallback embedded;

    private Callback(IDataTableSearchIterateCallback embedded)
    {
      this.embedded = embedded;
    }

    public boolean onNextRecord(IDataRecord record) throws Exception
    {
      return this.embedded.onNextRecord((IDataTableRecord) record);
    }
  }

	private IDataSearchResult performRecordSearch(IRelationSet relationSet, IDataTableSearchIterateCallback callback, boolean useCondition) throws InvalidExpressionException
	{
		if (logger.isTraceEnabled())
			logger.trace("performSearch(): table " + this);

		ITableAlias tableAlias = this.alias;
		QBESpecification spec = new QBESpecification(tableAlias);

		ITableDefinition tableDefinition = this.alias.getTableDefinition();
    DataSource dataSource = DataSource.get(tableDefinition.getDataSourceName());

		// ----------------------------------------
		// determine field for query result records
		// ----------------------------------------
		List tableFields = tableDefinition.getTableFields();
		for (int i = 0; i < tableFields.size(); i++)
		{
			ITableField tableField = (ITableField) tableFields.get(i);
			spec.addRecordField(new QBEField(tableAlias, tableField));
		}

		// IBIS: not nice -> try to avoid
		// do not forget primary key to identify result records
		IKey primaryKey = tableDefinition.getPrimaryKey();
		if (null != primaryKey)
		{
			List keyFields = primaryKey.getTableFields();
			for (int i = 0; i < keyFields.size(); i++)
			{
				spec.addKeyField(new QBEField(tableAlias, (ITableField) keyFields.get(i)));
			}
		}
		// IBIS: not nice -> try to avoid

    // ----------------------------------------
    // Call EventHandler
    // ----------------------------------------
    DataTableRecordEventHandler eventHandler = null;
    if (useCondition)
    {
      try
      {
        eventHandler = DataTableRecordEventHandler.get(this);
        eventHandler.beforeSearchAction(this);
      }
      catch (UserException ex)
      {
        throw new UserRuntimeException(ex);
      }
      catch (UserRuntimeException ex)
      {
        throw ex;
      }
      catch (Exception ex)
      {
        throw new RuntimeException("Error in user hook", ex);
      }
    }

		// ----------------------------------------
    // handle relation constraints for alias conditions (e.g. "agent.status>0")
    // ----------------------------------------
    ITableAliasCondition tableAliasCondition = useCondition ? tableAlias.getCondition() : null;
    if (tableAliasCondition != null)
    {
      QBERelationGraph relationGraph = null;
      if (tableAliasCondition.getForeignAliases().size() > 0)
      {
        // IBIS: Nicht immer wieder QBERelationGraph konstruieren
        relationGraph = new QBERelationGraph(getAccessor().getApplication().getDefaultRelationSet(), tableAlias);
      }
      spec.addCondition(this.alias, dataSource, relationGraph);
    }

		// ----------------------------------------
		// determine field constraints
		// ----------------------------------------
		if (relationSet == null)
		{
			addFieldConstraints(spec, null);
		}
		else
		{
		  // IBIS: besserer Mechanismus für dieses RelationGraph Gedönse
		  QBERelationGraph relationGraph = new QBERelationGraph(relationSet, this.alias);
			Iterator iter = relationGraph.getConnectedTableAliases().iterator();
			while (iter.hasNext())
			{
				ITableAlias alias = (ITableAlias) iter.next();

				if (logger.isTraceEnabled())
					logger.trace("performRecordSearch(): looking for field constraints of alias: " + alias.getName());

				if (getAccessorInternal().existsTable(alias.getName()))
				{
					DataTable table = (DataTable) getAccessor().getTable(alias.getName());
					table.addFieldConstraints(spec, relationGraph);
				}
			}
		}

		// ----------------------------------------
		// execute query
		// ----------------------------------------
    IDataSearchResult searchResult = dataSource.search(this, spec, callback == null ? null : new Callback(callback), eventHandler);
		List records = searchResult.getRecords();

		// and do not forget to add records to cache
		for (int i = 0; i < records.size(); i++)
		{
		  getAccessorInternal().addRecordToCache((IDataTableRecord) records.get(i));
		}

		return searchResult;
	}

	protected ITableField getFieldDefinition(int fieldIndex)
	{
		ITableDefinition tableDefinition = this.alias.getTableDefinition();
		return (ITableField) tableDefinition.getTableFields().get(fieldIndex);
	}

	protected int getFieldIndexByFieldName(String fieldName) throws NoSuchFieldException
	{
		ITableDefinition tableDefinition = this.alias.getTableDefinition();
		return tableDefinition.getTableField(fieldName).getFieldIndex();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.DataRecordSet#getRecordSize()
	 */
	protected int getRecordSize()
	{
		return this.alias.getTableDefinition().getTableFields().size();
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTable#newRecord(de.tif.jacob.core.data.IDataTransaction)
	 */
  public IDataTableRecord newRecord(IDataTransaction transaction)
  {
    if (logger.isTraceEnabled())
      logger.trace("newRecord() called for table " + this);

    // determine respective data source
    //
    ITableDefinition tableDefinition = this.alias.getTableDefinition();
    DataSource dataSource = DataSource.get(tableDefinition.getDataSourceName());

    // create and initialize new record
    //
    DataTableRecord record = new DataTableRecord(this, dataSource);
    TARecordAction action = record.initializeOnNew((DataTransaction) transaction);
    
    // make the new record the selected record
    //
    clearRecordsInternal();
    addRecord(0, record);
    incrementChangeCounter();

    if (this.tableTransaction != transaction)
    {
      closeTransaction();
      
      // AS 20.11.2008: Problem von andherz, wenn in einem TabContainer mit
      // unterschiedlichen Aliasen ein Record wieder verworfen werden soll, mit
      // IDataTableRecord.delete() und IDataTable.clear() und ansonsten die
      // Transaktion geschlossen wird.
      
      // do NOT make the transaction the table transaction
      // this.tableTransaction = transaction;
    }
    
    // invoke user specific hook
    //
    try
    {
      record.getEventHandler().afterNewAction(record, transaction);
    }
    catch (UserException ex)
    {
      ((DataTransaction) transaction).removeAction(action);
      throw new UserRuntimeException(ex);
    }
    catch (UserRuntimeException ex)
    {
      ((DataTransaction) transaction).removeAction(action);
      throw ex;
    }
    catch (Exception ex)
    {
      ((DataTransaction) transaction).removeAction(action);
      throw new RuntimeException("Error in user hook", ex);
    }
    
    return record;
  }

  /**
   * Makes the given multiple update record the seklected record.
   * 
   * @param record
   */
  protected void setRecord(DataMultiUpdateTableRecord record)
  {
    if (logger.isTraceEnabled())
      logger.trace("setRecord() called for table " + this);

    // plausibiltiy check
    //
    if (record.getTable()!=this)
      throw new IllegalStateException();
    
    // make the multi update record the selected record
    //
    clearRecordsInternal();
    addRecord(0, record);
    incrementChangeCounter();

    if (this.tableTransaction != record.getCurrentTransaction())
    {
      closeTransaction();
      this.tableTransaction = record.getCurrentTransaction();
    }
  }

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("DataTable[").append(alias.getName()).append("]");
		return buffer.toString();
	}
  
	/**
	 * @return Returns the filldirection.
	 */
	protected Filldirection getFilldirection()
	{
		return filldirection;
	}

	/**
	 * @param filldirection The filldirection to set.
	 */
	protected void setFilldirection(Filldirection filldirection)
	{
		this.filldirection = filldirection;
	}

	/**
	 * @return Returns the relationGraph.
	 */
	protected QBERelationGraph getRelationGraph()
	{
		if (this.relationGraph == null && this.relationSet != null)
		{
			// now, we really need relationGraph -> construct it 
			this.relationGraph = new QBERelationGraph(this.relationSet, this.alias);
		}
		return relationGraph;
	}

	/**
	 * @param relationGraph The relationGraph to set.
	 */
	protected void setRelationGraph(QBERelationGraph relationGraph)
	{
		if (relationGraph.getInitialTableAlias().equals(this.alias))
			this.relationGraph = relationGraph;
		else
		{
			// lazy evaluation of relationGraph
			this.relationGraph = null;
			this.relationSet = relationGraph.getRelationSet();
		}
	}

  protected void qbeBackfillKeyRecord()
  {
    if (isPrimaryKeyConstrained())
    {
      // and search the one and only record
      try
      {
        searchUnconditional();
      }
      catch (InvalidExpressionException ex)
      {
        throw new RuntimeException("Unexpected exception", ex);
      }
    }
  }

  private boolean isPrimaryKeyConstrained()
  {
    IKey primaryKey = alias.getTableDefinition().getPrimaryKey();
    
    if (primaryKey == null)
      return false;
    
    List keyFields = primaryKey.getTableFields();
    for (int i = 0; i < keyFields.size(); i++)
    {
      ITableField field = (ITableField) keyFields.get(i);

      QBEFieldConstraint qbeConstraint = (QBEFieldConstraint) this.qbeConstraintMap.get(field);
      if (qbeConstraint != null && qbeConstraint.isExactMatch() && qbeConstraint.getQbeValue() != null)
      {
        // check next
        continue;
      }

      // at least one primary key field is not constrained with a qbe key value
      return false;
    }

    return true;
  }

}
