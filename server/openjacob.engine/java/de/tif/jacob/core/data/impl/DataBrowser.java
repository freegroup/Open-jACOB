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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataBrowserSearchIterateCallback;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataMultiUpdateTableRecord;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.qbe.QBEField;
import de.tif.jacob.core.data.impl.qbe.QBERelationConstraint;
import de.tif.jacob.core.data.impl.qbe.QBERelationGraph;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.data.internal.IDataBrowserRecordInternal;
import de.tif.jacob.core.data.internal.IDataTableRecordInternal;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IAdhocBrowserDefinition;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.definition.IBrowserTableField;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableAliasCondition;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.impl.aliascondition.AliasCondition;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.screen.ISelection;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataBrowser extends DataRecordSet implements IDataBrowserInternal
{
	static public transient final String RCS_ID = "$Id: DataBrowser.java,v 1.35 2010/10/18 09:55:03 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.35 $";

	static private final transient Log logger = LogFactory.getLog(DataBrowser.class);

	private final IBrowserDefinition definition;
	private Integer selectedRecordIndex = null;
	private QBERelationGraph relationGraph = null;
	private Filldirection filldirection = null;
	private IRelationSet relationSet = null;
	private boolean successfulSearchOrAddPerformed = false;
	private boolean searchPerformed = false;
	private long realRecordCount = -1; 
	private boolean performRecordCount = false;
	private IDataFieldConstraints lastSearchConstraints = null;
	private IDataBrowserSortStrategy lastSortStrategy = null;
  
  private final boolean doFilterSearchAction;
  
  /**
   * Sequence number wird benötig, da Records auch entfernt werden können und
   * seqNbr=recordCount() nicht eindeutig für neu hinzugefügte Records wäre.
   */
  private int recordSeqNbr = 0;
	
  protected DataBrowser(DataAccessor parent, IBrowserDefinition definition)
  {
    this(parent, definition, true);
  }
  
  protected DataBrowser(DataAccessor parent, IBrowserDefinition definition, boolean doFilterSearchAction)
  {
    super(parent);

    if (logger.isTraceEnabled())
      logger.trace(definition.getName());

    this.definition = definition;
    this.doFilterSearchAction = doFilterSearchAction;
  }
  
	/**
   * Internal method which is not intended for common use.
   * 
   * @param parent
   *          data accessor to use or <code>null</code> to use the data
   *          accessor of this browser
   * @return the adhoc data browser cloned
   */
  public DataAdhocBrowser cloneAsAdhocBrowser(IDataAccessor parent)
	{
	  if (parent == null)
	  {
	    parent = getAccessor();
	  }
	  else
	  {
	    // plausibility check
	    if (!parent.getApplication().equals(getAccessor().getApplication()))
	    {
	      throw new RuntimeException("Illegal accessor with different application definition");
	    }
	  }
	  
	  IAdhocBrowserDefinition adhocBrowserDef = parent.getApplication().createAdhocBrowserDefinition( this.definition);
	  DataBrowser adhocBrowser = new DataAdhocBrowser((DataAccessor) parent, adhocBrowserDef);
	  adhocBrowser.filldirection = this.filldirection;
	  adhocBrowser.relationSet = this.relationSet;
	  adhocBrowser.relationGraph = this.relationGraph;
	  adhocBrowser.lastSearchConstraints = this.lastSearchConstraints;
	  adhocBrowser.lastSortStrategy = this.lastSortStrategy;
	  
	  return (DataAdhocBrowser) adhocBrowser;
	}
  
	protected DataRecord instantiateRecord(DataSource dataSource, DataTableRecordEventHandler eventHandler, int[] primaryKeyIndices, Object[] values, int seqNbr)
  {
    DataBrowserRecord browserRecord = new DataBrowserRecord(this, primaryKeyIndices, values, this.recordSeqNbr++);
    try
    {
      if (eventHandler == null)
        return browserRecord;
      if (this.doFilterSearchAction && !eventHandler.filterSearchAction(browserRecord))
        return null;
      return browserRecord;
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

	protected DataTable getRelatedDataTable()
	{
		return (DataTable) getAccessor().getTable(getTableAlias().getName());
	}

	public final IBrowserDefinition getBrowserDefinition()
	{
		return this.definition;
	}

	protected int getFieldIndexByFieldName(String fieldName) throws NoSuchFieldException
	{
		return this.definition.getBrowserField(fieldName).getFieldIndex();
	}

	public String getName()
	{
		return this.definition.getName();
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecordSet#getTableAlias()
	 */
	public ITableAlias getTableAlias()
	{
		return this.definition.getTableAlias();
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataBrowser#getRelationSet()
	 */
	public IRelationSet getRelationSet()
	{
		return this.relationSet;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataBrowser#getFillDirection()
	 */
	public Filldirection getFillDirection()
	{
		return this.filldirection;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataBrowser#getRecord(int)
   */
  public IDataBrowserRecord getRecord(int index) throws IndexOutOfBoundsException
  {
    return (IDataBrowserRecord) getRecordInternal(index);
  }
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataBrowser#setSelectedRecordIndex(int)
	 */
	public void setSelectedRecordIndex(int index) throws IndexOutOfBoundsException
	{
		if (logger.isTraceEnabled())
			logger.trace("selectRecord() called for browser " + this +", index=" + index);

		checkRecordIndex(index);
		incrementChangeCounter();
		this.selectedRecordIndex = new Integer(index);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataBrowser#clearSelections()
	 */
	public void clearSelections()
	{
		if (logger.isTraceEnabled())
			logger.trace("clearSelections() called for browser " + this);

		incrementChangeCounter();
		this.selectedRecordIndex = null;
	}
	
	private static Collator getCollator(Locale locale)
  {
    if (locale == null)
      return null;

    Collator collator = Collator.getInstance(locale);
    // "a" and "A" should be identical but "a" and "ä" not.
    collator.setStrength(Collator.SECONDARY);
    return collator;
  }
	
	/**
   * Helper class to provide sorting of records in case sorting is not provides
   * by data source implementation.
   * 
   * @author Andreas Sonntag
   * @since 2.10
   */
  private final class RecordComparator implements Comparator
	{
	  private final IBrowserTableField[] tableFieldsToSort;
	  private final boolean hasSort;
	  private final Collator collator;
	  
	  private RecordComparator(Locale collatorLocale)
	  {
	    this.tableFieldsToSort = new IBrowserTableField[DataBrowser.this.definition.getFieldNumber()];
      boolean hasSort = false;
      for (int i = 0; i < tableFieldsToSort.length; i++)
      {
        IBrowserField browserField = DataBrowser.this.definition.getBrowserField(i);
        if (browserField instanceof IBrowserTableField)
        {
          IBrowserTableField browserTableField = (IBrowserTableField) browserField;
          if (!browserTableField.getSortorder().equals(SortOrder.NONE))
          {
            this.tableFieldsToSort[i] = browserTableField;
            hasSort = true;
          }
        }
      }
      this.hasSort = hasSort;
      this.collator = hasSort ? getCollator(collatorLocale) : null;
	  }

    public int compare(Object o1, Object o2)
    {
      IDataBrowserRecordInternal rec1 = (IDataBrowserRecordInternal) o1;
      IDataBrowserRecordInternal rec2 = (IDataBrowserRecordInternal) o2;

      for (int i = 0; i < this.tableFieldsToSort.length; i++)
      {
        IBrowserTableField browserTableField = this.tableFieldsToSort[i];
        if (browserTableField != null)
        {
          int res;
          if (browserTableField.getSortorder().equals(SortOrder.ASCENDING))
            res = browserTableField.getTableField().getType().sortDataValue(rec1.getValueInternal(i), rec2.getValueInternal(i), collator);
          else
            res = browserTableField.getTableField().getType().sortDataValue(rec2.getValueInternal(i), rec1.getValueInternal(i), collator);
          if (res != 0)
            return res;
        }
      }
      return 0;
    }
  }
	
  /**
   * Internal method to sort records in case data source implementation does not
   * provide sorting.
   * 
   * @since 2.10
   */
  public void sort()
  {
    RecordComparator comp = new RecordComparator(Context.getCurrent().getApplicationLocale());
    if (comp.hasSort)
      sort(comp);
  }

	/**
	 * Clears selections and all records retrieved.
	 */
	public void clear()
	{
	  if (logger.isTraceEnabled())
	    logger.trace("clear() called for browser " + this);

	  // clear all internal members
	  clearInternal();
	  
	  // and increment change counter
	  incrementChangeCounter();
	}
	
	protected void clearInternal()
	{
	  clearRecordsInternal();
	  this.selectedRecordIndex = null;
	  this.relationGraph = null;
	  this.successfulSearchOrAddPerformed = false;
	  this.searchPerformed = false;
	  this.realRecordCount = -1;
	  this.performRecordCount = false;
	  // attention: do not reset this.lastSearchConstraints, filldirection and relationSet!
	}
	
  /**
	 * This method returns the constraints of the last search performed on this
	 * data browser. <br>Attention: Internally last search constraints will not
	 * be reset, if clear() is called.
	 * 
	 * @return constraints of the last search or <code>null</code>, if no
	 *         search has been performed so far
	 */
	public IDataFieldConstraints getLastSearchConstraints()
	{
		return this.lastSearchConstraints;
	}

	protected void setRecords(List records, boolean hasMore)
	{
		clearRecordsInternal();
		addRecords(records, hasMore);
		clearSelections();
	}

	/**
	 * Appends a table record to this data browser
	 * 
	 * Attention: Internal Use!!!
	 * 
	 * @param record
	 *          the record to add
	 * @return the index of the location where the record has been added
	 */
	public int append(IDataTableRecord record)
	{
	  return add(recordCount(),record);
	}
	
	/**
	 * Adds a table record to this data browser
	 * 
	 * Attention: Internal Use!!!
	 * 
	 * @param record
	 *          the record to add
	 * @return the index of the location where the record has been added
	 */
  public int add(IDataTableRecord record, IRelationSet relationSet)
  {
    // IBIS: Hack to avoid Exception in WrapperRecord.getValue()
    this.relationGraph = new QBERelationGraph(relationSet, getTableAlias());
    return add(0,record);
  }
  
  public int add(IDataTableRecord record)
  {
    return add(0,record);
  }
  
	/**
	 * Adds a table record to this data browser
   * 
   * Attention: Internal Use!!!
	 * 
	 * @param record
	 *          the record to add
	 * @return the index of the location where the record has been added
	 */
	public final int add(int indexOfInsert, IDataTableRecord record)
	{
		if (logger.isTraceEnabled())
			logger.trace("add(): set=" + this);

		if (null == record)
			throw new NullPointerException("record is null");
		
		if (!getTableAlias().equals(record.getTableAlias()))
		{
	    if (!getTableAlias().getTableDefinition().equals(record.getTableAlias().getTableDefinition()))
        throw new IllegalArgumentException("Can not add record of table alias '" + record.getTableAlias() + "' to data browser of table alias '" + getTableAlias() + "'");
		}

		// IBIS: hack: create default relation graph
		if (this.relationGraph == null)
			this.relationGraph = new QBERelationGraph(getAccessor().getApplication().getDefaultRelationSet(), getTableAlias());
		
		int index = addRecord(indexOfInsert, new DataBrowserRecord(this, this.recordSeqNbr++, new WrapperRecordValueProvider(this.relationGraph, record, null)));
		
		// correct selected record index if needed
    if (this.selectedRecordIndex != null && this.selectedRecordIndex.intValue() >= index)
    {
      this.selectedRecordIndex = new Integer(this.selectedRecordIndex.intValue() + 1);
    }
		
		this.successfulSearchOrAddPerformed = true;
		incrementChangeCounter();
		return index;
	}

  public int addRecord(int indexOfInsert, DataRecord record)
  {
    // IBIS: Eventuell clonen? Diese Methode wird auch bei der Baumdarstellung
    // aufgerufen und der Anwendungsprogrammier kann Records eines anderen
    // DataAccessors zurückliefern.
    return super.addRecord(indexOfInsert, record);
  }

  public void removeSelected()
	{
		if (logger.isTraceEnabled())
			logger.trace("removeSelected(): set=" + this);

		if (this.selectedRecordIndex != null)
		{
			remove(this.selectedRecordIndex.intValue());
		}
	}

  public boolean removeRecord(IDataBrowserRecord record)
  {
    int index = getRecordIndexInternal(record);
    if (index < 0)
      return false;

    remove(index);
    return true;
  }

  public void remove(int index)
	{
		if (logger.isTraceEnabled())
			logger.trace("remove(): set=" + this +", index" + index);

		checkRecordIndex(index);
		removeRecord(index);
		if (this.selectedRecordIndex != null)
		{
			if (this.selectedRecordIndex.intValue() == index)
			{
				// clear selection
				this.selectedRecordIndex = null;
			}
			else if (this.selectedRecordIndex.intValue() > index)
			{
				this.selectedRecordIndex = new Integer(this.selectedRecordIndex.intValue() - 1);
			}
		}
		incrementChangeCounter();
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataBrowser#propagateSelections()
	 */
	public void propagateSelections() throws RecordNotFoundException
	{
		if (logger.isTraceEnabled())
			logger.trace("propagateSelections() called for browser " + this);

		if (this.selectedRecordIndex == null)
		{
			// no selections done
		  throw new RuntimeException("No records selected!");
		}

		//		incrementChangeCounter();

		DataBrowserRecord browserRecord = (DataBrowserRecord) getRecord(this.selectedRecordIndex.intValue());
		
		// records populated by means of checkForDataTableRecords()?
		if (!this.successfulSearchOrAddPerformed)
		{	
			// take over propagation information from data table
			DataTable dataTable = getRelatedDataTable();
			this.filldirection = dataTable.getFilldirection();
			this.relationGraph = dataTable.getRelationGraph();
			this.relationSet = this.relationGraph != null ? this.relationGraph.getRelationSet() : null; 
		}

		// IBIS: hack: create default relation graph for performance reasons already here
		if (this.relationGraph == null)
			this.relationGraph = new QBERelationGraph(getAccessor().getApplication().getDefaultRelationSet(), getTableAlias());
		
		IDataTableRecord tableRecord = getAccessorInternal().propagateRecord(this.relationGraph, this.filldirection, browserRecord);
		if (null == tableRecord)
		{
			return;// false;
		}

		// replace record value provider in browser to make updates visible
    browserRecord.setProvider(new WrapperRecordValueProvider(this.relationGraph, tableRecord, browserRecord.getProvider()));

		// Flag setzen, damit die Datensätze welche über checkForDataTableRecords() angezogen wurden, nicht wieder
		// verschwinden.
		this.successfulSearchOrAddPerformed = true;
		
//		return true;
	}
  
  /**
   * Attention: Internal Use!!!
   * 
   * @param index
   * @param transaction
   * @return
   * @throws RecordLockedException
   */
  public IDataTableRecordInternal getRecordToUpdate(IDataTransaction trans, int index) throws RecordLockedException, RecordNotFoundException
  {
    checkRecordIndex(index);
    return getRecordToUpdate(trans, (DataBrowserRecord) getRecord(index));
  }

  protected IDataTableRecordInternal getRecordToUpdate(IDataTransaction trans, DataBrowserRecord browserRecord) throws RecordLockedException, RecordNotFoundException
  {
    // plausibility check
    if (browserRecord.getBrowser() != this)
      throw new IllegalArgumentException();
    
    if (browserRecord.getProvider() instanceof WrapperRecordValueProvider)
    {
      IDataTableRecord tableRecord = browserRecord.getTableRecord();

      if (tableRecord.getCurrentTransaction() == trans)
      {
        return (DataTableRecord) tableRecord;
      }
    }

    IDataKeyValue primaryKey = browserRecord.getPrimaryKeyValue();
    if (null == primaryKey)
    {
      throw new RuntimeException("No update possible for records with no primary key defined");
    }

    // perform lock!
    DataTransaction transaction = (DataTransaction) trans;
    transaction.lock(browserRecord);

    // load record from data source
    DataTableRecord tableRecord = (DataTableRecord) getAccessorInternal().loadRecord(getTableAlias(), primaryKey);
    tableRecord.setCurrentTransaction(transaction);

    // replace record value provider in browser to make updates visible
    browserRecord.setProvider(new WrapperRecordValueProvider(this.relationGraph, tableRecord, browserRecord.getProvider()));

    return tableRecord;
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.DataRecordSet#getFieldDefinition(int)
	 */
	protected final ITableField getFieldDefinition(int fieldIndex)
	{
    IBrowserField browserField = this.definition.getBrowserField(fieldIndex);
    if (browserField instanceof IBrowserTableField)
    {
      IBrowserTableField browserTableField = (IBrowserTableField) browserField;
      return browserTableField.getTableField();
    }
    return null;
  }

	protected int getRecordSize()
	{
		return this.definition.getFieldNumber();
	}
  
  // IBIS: HACK: Diese beiden Methoden werden für VE 3.0 benötigt. 
  private boolean searchIFBDisabled = false;
  public final void disableSearchIFB()
  {
    this.searchIFBDisabled = true;
  }
  public final boolean isDisableSearchIFB()
  {
    return this.searchIFBDisabled;
  }

	/**
	 * Internal method
	 * 
	 * @param groupAliasName
	 * @throws InvalidExpressionException
	 */
	public void searchIFB(String groupAliasName) throws InvalidExpressionException
	{
		if (logger.isTraceEnabled())
			logger.trace("searchIFB(): browser=" + this +", groupAliasName=" + groupAliasName);

		setMaxRecords(Property.BROWSER_SYSTEM_MAX_RECORDS.getIntValue());
		ITableAlias tableAlias = getAccessor().getApplication().getTableAlias(groupAliasName);
		search(null, getAccessor().getApplication().getDefaultRelationSet(), Filldirection.BACKWARD, tableAlias, null, false);
	}

	public void search(String relationSetName) throws InvalidExpressionException
	{
		IRelationSet relationSet = getAccessor().getApplication().getRelationSet(relationSetName);
		search(relationSet);
	}

	public void searchWhere(String relationSetName, String whereClause) throws InvalidExpressionException
	{
		IRelationSet relationSet = getAccessor().getApplication().getRelationSet(relationSetName);
		searchWhere(relationSet, whereClause);
	}

	public void search(IRelationSet relationSet) throws InvalidExpressionException
	{
		search(relationSet, Filldirection.BACKWARD);
	}

	public void searchWhere(IRelationSet relationSet, String whereClause) throws InvalidExpressionException
	{
	  searchWhere(relationSet, Filldirection.BACKWARD, whereClause);
	}

	public void search(String relationSetName, Filldirection filldirection) throws InvalidExpressionException
	{
		IRelationSet relationSet = getAccessor().getApplication().getRelationSet(relationSetName);
		search(relationSet, filldirection);
	}

	public void searchWhere(String relationSetName, Filldirection filldirection, String whereClause) throws InvalidExpressionException
	{
		IRelationSet relationSet = getAccessor().getApplication().getRelationSet(relationSetName);
		searchWhere(relationSet, filldirection, whereClause);
	}

	public void search(IRelationSet relationSet, Filldirection filldirection) throws InvalidExpressionException
	{
		search(null, relationSet, filldirection, null, null, false);
	}

	public void searchWhere(IRelationSet relationSet, Filldirection filldirection, String whereClause) throws InvalidExpressionException
	{
		search(null, relationSet, filldirection, null, whereClause, false);
	}
  
  public int searchAndIterate(IDataBrowserSearchIterateCallback callback, IRelationSet relationSet) throws InvalidExpressionException
  {
    if (callback == null)
      throw new NullPointerException("callback is null");
    return search(callback, relationSet, null, null, null, false);
  }

  public int searchAndIterate(IDataBrowserSearchIterateCallback callback, String relationSetName) throws InvalidExpressionException
  {
    IRelationSet relationSet = getAccessor().getApplication().getRelationSet(relationSetName);
    return searchAndIterate(callback, relationSet);
  }

  public int searchWhereAndIterate(IDataBrowserSearchIterateCallback callback, IRelationSet relationSet, String whereClause) throws InvalidExpressionException
  {
    if (callback == null)
      throw new NullPointerException("callback is null");
    return search(callback, relationSet, null, null, whereClause, false);
  }

  public int searchWhereAndIterate(IDataBrowserSearchIterateCallback callback, String relationSetName, String whereClause) throws InvalidExpressionException
  {
    IRelationSet relationSet = getAccessor().getApplication().getRelationSet(relationSetName);
    return searchWhereAndIterate(callback, relationSet, whereClause);
  }

  /**
   * Perform a plain search, i.e. do not use any constraints, conditions, etc.
   * <p>
   * Note: This is used internally by the administration console
   * 
   * @throws InvalidExpressionException
   */
  public void searchPlain() throws InvalidExpressionException
  {
    search(null, getAccessor().getApplication().getLocalRelationSet(), Filldirection.NONE, null, null, true);
  }

  /**
   * Helper class to "cast" from IDataSearchIterateCallback to IDataBrowserSearchIterateCallback.
   * 
   * @author Andreas Sonntag
   */
  private static final class Callback implements IDataSearchIterateCallback
  {
    private final IDataBrowserSearchIterateCallback embedded;

    private Callback(IDataBrowserSearchIterateCallback embedded)
    {
      this.embedded = embedded;
    }

    public boolean onNextRecord(IDataRecord record) throws Exception
    {
      return this.embedded.onNextRecord((IDataBrowserRecord) record);
    }
  }

	private int search(IDataBrowserSearchIterateCallback callback, IRelationSet relationSet, Filldirection filldirection, ITableAlias ifbGroupAlias, String additionalWhere, boolean plain) throws InvalidExpressionException
	{
		if (logger.isTraceEnabled())
			logger.trace("search(): browser=" + this +", relSet=" + relationSet + ", filldirection=" + filldirection);

		ITableAlias browserAlias = this.definition.getTableAlias();
		QBESpecification spec = new QBESpecification(browserAlias);
		this.filldirection = filldirection;
		this.relationSet = relationSet;
		this.relationGraph = new QBERelationGraph(relationSet, browserAlias);

		ITableDefinition browserTable = browserAlias.getTableDefinition();

		// ----------------------------------------
		// determine field for query result records
		// ----------------------------------------
		for (int i = 0; i < this.definition.getFieldNumber(); i++)
		{
			IBrowserField browserField = this.definition.getBrowserField(i);
			if (browserField instanceof IBrowserTableField)
			{
				IBrowserTableField browserTableField = (IBrowserTableField) browserField;
				Iterator relIter = this.relationGraph.markAsResult(browserTableField.getTableAlias());
				if (null == relIter)
				{
					// table alias of foreign field is not linked by relation set -> keep
					// field empty
					spec.addRecordField(new QBEField(browserTableField, true));
				}
				else
				{
					spec.addRecordField(new QBEField(browserTableField, false));
					spec.addRelationConstraints(relIter);
				}
			}
			else
			{
				spec.addRecordField(new QBEField());
			}
		}

		// do not forget primary key to identify result records
		IKey primaryKey = browserTable.getPrimaryKey();
		if (null != primaryKey)
		{
			List keyFields = primaryKey.getTableFields();
			for (int i = 0; i < keyFields.size(); i++)
			{
				spec.addKeyField(new QBEField(this.definition.getTableAlias(), (ITableField) keyFields.get(i)));
			}
		}
    
		// ----------------------------------------
    // special handling for IFB browsers
    // ----------------------------------------
    boolean useAliasConditions = true;
    if (ifbGroupAlias != null)
    {
      DataTable table = (DataTable) getAccessor().getTable(ifbGroupAlias);
      IDataTableRecord selectedParentRecord = table.getSelectedRecord();

      // plausiblity check
      if (null == selectedParentRecord)
        throw new IllegalStateException("IFB-Search: No selected record of alias '" + ifbGroupAlias + "' existing!");

      // simply constrain IFB browser entries to primary key of parent record
      getAccessorInternal().qbeClearAll();
      table.qbeSetPrimaryKeyValue(selectedParentRecord.getPrimaryKeyValue());

      // do not use condition because alias is already constrained by primary
      // key
      useAliasConditions = false;
    }

    DataSource dataSource = DataSource.get(browserTable.getDataSourceName());
    
    // temporarly set unlimited records, if a callback is present
    int origMaxRecords = 0;
    if (callback != null)
      origMaxRecords = setMaxRecords(UNLIMITED_RECORDS);

    try
    {
      // ----------------------------------------
      // call EventHandler
      // ----------------------------------------
      DataTableRecordEventHandler eventHandler = null;
      // if plain, do not use any constraints and conditions
      if (plain == false)
      {
        try
        {
          IDataTable relatedDataTable = this.getAccessor().getTable(browserAlias);
          eventHandler = DataTableRecordEventHandler.get(relatedDataTable);
          eventHandler.beforeSearchAction(this, relatedDataTable, relationSet);
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

        // ----------------------------------------
        // determine field constraints
        // ----------------------------------------
        Iterator iter = this.relationGraph.getConnectedTableAliases().iterator();
        while (iter.hasNext())
        {
          ITableAlias alias = (ITableAlias) iter.next();

          if (logger.isTraceEnabled())
            logger.trace("search(): looking for field constraints of alias: " + alias.getName());

          if (getAccessorInternal().existsTable(alias.getName()))
          {
            DataTable table = (DataTable) getAccessor().getTable(alias.getName());
            table.addFieldConstraints(spec, this.relationGraph, useAliasConditions);
          }
        }

        // ----------------------------------------
        // handle alias conditions (e.g. "agent.status>0")
        // ----------------------------------------
        spec.addCondition(browserAlias, dataSource, this.relationGraph);

        AliasCondition cond = AliasCondition.parseWithPostProcessing(additionalWhere, getAccessor().getApplication(), getTableAlias());
        ITableAliasCondition additionalWhereCondition = cond == null ? null : cond.getResult(dataSource);
        if (additionalWhereCondition != null)
        {
          spec.addCondition(additionalWhereCondition, this.relationGraph);
        }
      }

      // ----------------------------------------
      // execute query
      // ----------------------------------------
      IDataSearchResult searchResult = dataSource.search(this, spec, callback == null ? null : new Callback(callback), eventHandler);
      setRecords(searchResult.getRecords(), searchResult.hasMore());
      if (eventHandler != null)
      {
        Iterator iter = searchResult.getRecords().iterator();
        while (iter.hasNext())
        {
          try
          {
            eventHandler.afterSearchAction((DataBrowserRecord) iter.next());
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
      }
      if (!dataSource.supportsSorting())
        sort();
      incrementChangeCounter();

      // ----------------------------------------
      // count records if desired and necessary
      // ----------------------------------------
      if (this.performRecordCount && searchResult.hasMore() && callback == null)
      {
        // we can not determine record count if filter is active
        if (eventHandler != null && eventHandler.isFilterSearchAction())
          this.realRecordCount = -1;
        else
          this.realRecordCount = dataSource.count(this, spec);
      }
      else if (searchResult.hasMore())
        this.realRecordCount = -1;
      else
        this.realRecordCount = searchResult.getRecordCount();

      // reset flag to avoid execution of expensive operation each time
      this.performRecordCount = false;

      // remember constraints of last search
      // for none IFB searches!!!
      if (ifbGroupAlias == null)
        this.lastSearchConstraints = spec;

      // set flag in case a successful search has been performed
      this.successfulSearchOrAddPerformed = callback == null && searchResult.getRecords().size() > 0;

      this.searchPerformed = true;

      return searchResult.getRecordCount();
    }
    finally
    {
      if (callback != null)
        setMaxRecords(origMaxRecords);
    }
	}

	public long getChangeCount()
	{
		checkForDataTableRecords();
		return this.changeCounter;
	}

	private void checkForDataTableRecords()
	{
		if (this.successfulSearchOrAddPerformed)
			return;

		DataTable dataTable = getRelatedDataTable();
		if (dataTable.changeCounter > this.changeCounter)
		{
			// check that new records are not inserted
			IDataTableRecord selectedRecord = dataTable.getSelectedRecord();
			if (selectedRecord == null || selectedRecord.isPersistent())
			{
				int recCount = dataTable.recordCount();
				if (recCount > 0)
				{
          // take over records from data table to data browser
				  if (logger.isDebugEnabled())
				    logger.debug("take over records from data table: browser=" + this);
				  
				  List records = new ArrayList(recCount);
					for (int i = 0; i < recCount; i++)
					{
						records.add(new DataBrowserRecord(this, this.recordSeqNbr++, new WrapperRecordValueProvider(null, dataTable.getRecord(i), null)));
					}
					if (logger.isDebugEnabled())
						logger.debug("checkForDataTableRecords(): browser=" + this +"; recNum=" + records.size());
					setRecords(records, false);
					
          // if only one record is there, than select it 
          if (recCount == 1)
          {
            setSelectedRecordIndex(0); 
          }
				}
			}

			this.changeCounter = dataTable.changeCounter;
		}
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("DataBrowser[").append(this.definition.getName()).append("]");
		return buffer.toString();
	}

	private final class WrapperRecordValueProvider implements IDataBrowserRecordValueProvider
	{
		//		private static final Integer NULL = new Integer(0);

    // TODO: temporärer HACK für Andherz 26.6.08 
    private final IDataBrowserRecordValueProvider orginal;
    
    private final QBERelationGraph relationGraph;
    private final DataTableRecord tableRecord;
    private DataTableRecord newTableRecord;
    private IDataKeyValue primaryKeyValue;
    
    private RecordNotFoundException lastRecordNotFoundException;
    
		//		private Object[] values;

		/**
		 * @param relationGraph
		 * @param tableRecord ACHTUNG: kann von einem anderen Alias derselben ITableDefinition sein.
		 * @param orginal
		 */
		protected WrapperRecordValueProvider(QBERelationGraph relationGraph, IDataTableRecord tableRecord, IDataBrowserRecordValueProvider orginal)
		{
      this.orginal = orginal;
			this.relationGraph = relationGraph;
			this.tableRecord = (DataTableRecord) tableRecord; 
      if (tableRecord.isNew())
			{
        // remember record since primary key might not be initialised
				this.newTableRecord = this.tableRecord;
			}
			else
			{
        // remember primary key since table record might not be up-to-date
				this.primaryKeyValue = tableRecord.getPrimaryKeyValue();
			}
			//			this.values = null;
		}

    public boolean isModifiedValue(int fieldIndex)
    {
      return this.orginal != null && this.orginal.isModifiedValue(fieldIndex);
    }

    public Object getValueInternal(int fieldIndex)
		{
      // TODO: temporärer HACK für Andherz (26.6.08) und for Lucene Score-Value not
      // to be updated in BrowserRecord by TableRecord (18.8.2010)
      //       Einmal über afterSearchAction() modifiziert -> immer berechneten Wert anzeigen
      if (this.orginal != null && this.orginal.isModifiedValue(fieldIndex))
      {
        return this.orginal.getValueInternal(fieldIndex);
      }
      
			//			// check whether there are any local modifications
			//			if (null != this.values && this.values[fieldIndex] != null)
			//			{
			//				return this.values[fieldIndex] == NULL ? null :
			// this.values[fieldIndex];
			//			}

			IBrowserField browserField = DataBrowser.this.getBrowserDefinition().getBrowserField(fieldIndex);
			if (browserField instanceof IBrowserTableField)
			{
				IBrowserTableField browserTableField = (IBrowserTableField) browserField;

				if (null == getPrimaryKeyValue() && this.newTableRecord == null)
					return null;

				try
				{
					IDataTableRecord fieldRecord = getTableRecord();
					if (null == this.relationGraph)
					{
						// IBIS: check for default relationGraph?
						if (!browserTableField.getTableAlias().equals(DataBrowser.this.getBrowserDefinition().getTableAlias()))
							return null;
					}
					else
					{
            List relationsToTraverse = this.relationGraph.getTraverseFromInitialRelations(browserTableField.getTableAlias(), true);
						if (null == relationsToTraverse)
							return null;
						for (int i = 0; i < relationsToTraverse.size(); i++)
						{
              QBERelationConstraint relation = (QBERelationConstraint) relationsToTraverse.get(i);
              IDataKeyValue keyValue = fieldRecord.getKeyValue(relation.getToForeignKey());
							if (null == keyValue)
								return null;
              fieldRecord = DataBrowser.this.parent.getCachedRecord(relation.getFromTableAlias().getTableDefinition(), keyValue);
							if (null == fieldRecord)
              {
                if (logger.isInfoEnabled())
                  logger.info("Cache mis: Loading record from database: alias=" + relation.getFromTableAlias().getName() + "; keyvalue=" + keyValue);
                fieldRecord = DataBrowser.this.parent.getTable(relation.getFromTableAlias()).loadRecord(keyValue);
              }
						}
					}
					return ((DataTableRecord) fieldRecord).getValueInternal(browserTableField.getTableField().getFieldIndex());
				}
				catch (RecordNotFoundException ex)
				{
					// IBIS: problem hiding: remove hack
					logger.warn("Linked record for browser field '" + browserField + "' not found: " + ex.getMessage());
				}
			}
			return null;
		}

    public void setValueInternal(int fieldIndex, Object value)
    {
      throw new UnsupportedOperationException();
    }

    public IDataKeyValue getPrimaryKeyValue()
		{
			if (this.newTableRecord != null)
			{
				// still new or new deleted?
				if (this.newTableRecord.isNew() || this.newTableRecord.isDeleted())
					return this.newTableRecord.getPrimaryKeyValue();

				this.primaryKeyValue = this.newTableRecord.getPrimaryKeyValue();
				this.newTableRecord = null;
			}
			
			// IBIS: remove hack see hack in IDataAccessor.relinkUpdatedRecordInCache
			// 
			try
			{
				return getTableRecord().getPrimaryKeyValue();
			}
			catch (RecordNotFoundException ex)
			{
				// should never be null
				return this.primaryKeyValue;
			}
		}
    
		public IDataTableRecord getTableRecord() throws RecordNotFoundException
		{
      if (this.newTableRecord != null)
      {
      	// still new or new deleted?
      	if (this.newTableRecord.isNew() || this.newTableRecord.isDeleted())
      		return checkCloneNeeded(this.newTableRecord);

        this.primaryKeyValue = this.newTableRecord.getPrimaryKeyValue();
        this.newTableRecord = null;
      }
      
      // otherwise get record from cache
      //
      DataTable dataTable = DataBrowser.this.getRelatedDataTable();
      // IBIS: avoid NullPointerException if table does not have a primary key
      IDataTableRecord tableRecord = dataTable.getCachedRecord(this.primaryKeyValue);
      if (null != tableRecord)
      {
        return tableRecord;
      }
      
      // record already deleted?
      if (lastRecordNotFoundException != null && this.primaryKeyValue.equals(this.lastRecordNotFoundException.getRecordId().getPrimaryKeyValue()))
      {
        throw this.lastRecordNotFoundException;
      }
      
      // try to load from database
      try
      {
        return dataTable.loadRecord(this.primaryKeyValue);
      }
      catch (RecordNotFoundException ex)
      {
        this.lastRecordNotFoundException = ex;
        throw ex;
      }
    }
		
		/**
		 * Clone record if table aliases are different
		 * @param record
		 * @return
		 */
		private DataTableRecord checkCloneNeeded(DataTableRecord record)
		{
		  ITableAlias tableAlias = DataBrowser.this.getTableAlias();
		  if (tableAlias.equals(record.getTableAlias()))
		    return record;
		  DataTable dataTable = (DataTable) DataBrowser.this.getAccessor().getTable(tableAlias);
		  return record.clone(dataTable);
		}

    public DataTableRecord getWrappedTableRecord()
    {
      return checkCloneNeeded(this.tableRecord);
    }
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataBrowser#getSelectedRecordIndex()
	 */
	public int getSelectedRecordIndex()
	{
		return selectedRecordIndex == null ? -1 : selectedRecordIndex.intValue();
	}

	/**
	 * Flag to indicate whether a search has been performed since the last clear.
	 * 
	 * @return <code>true</code> if search has been performed, otherwise <code>false</code>
	 */
	public boolean isSearchPerformed()
	{
		return this.searchPerformed || this.recordCount() > 0;
	}

	/**
	 * Returns the real number of records matching the search criterias of the
	 * last search operation.
	 * 
	 * @return the real record number or <code>-1</code> if no record counting
	 *         has been performed on executing of the last search operation.
	 * 
	 * @see #performRecordCount()
	 */
	public long getRealRecordCount()
	{
		return realRecordCount;
	}

	/**
	 * Sets the flag which indicates that a record count should be performed on
	 * the next search operation.
	 * 
	 * Note: This flag will be reset after the next search operation has been
	 * performed.
	 * 
	 * @see #getRealRecordCount()
	 */
	public void performRecordCount()
	{
		this.performRecordCount = true;
	}
	
	/**
	 * Internal method for checking, whether the specified record is already propagated.
	 * @param index
	 * @return
	 */
	public boolean isAlreadyPropagated(int index)
	{
		checkRecordIndex(index);
		IDataTableRecord selectedRecord = getAccessor().getTable(getTableAlias()).getSelectedRecord();
		if (selectedRecord != null)
		{
			IDataKeyValue primaryKey = selectedRecord.getPrimaryKeyValue();
			return primaryKey != null && primaryKey.equals(getRecord(index).getPrimaryKeyValue());
		}
		return false;
	}


  public IDataMultiUpdateTableRecord startMultiUpdate(ISelection selection, boolean isolated) throws Exception
  {
    DataMultiUpdateTableRecord multiUpdateRecord = new DataMultiUpdateTableRecord(this, selection.toList(), isolated);

    // Record nur zurückfüllen, wenn die Transaktion noch gültig ist, da bei
    // Alles-Oder-Nichts Transaktion bei einem Fehler die Transaktion gleich
    // geschlossen wird.
    if (multiUpdateRecord.getAssociatedTransaction().isValid())
    {
      ((DataTable) multiUpdateRecord.getTable()).setRecord(multiUpdateRecord);
      
      // IBIS: hack: create default relation graph for performance reasons already here
      if (this.relationGraph == null)
        this.relationGraph = new QBERelationGraph(getAccessor().getApplication().getDefaultRelationSet(), getTableAlias());
      
      // and propagate multi update record to fill foreign fields
      getAccessorInternal().propagateRecord(this.relationGraph, this.filldirection, multiUpdateRecord);
    }

    return multiUpdateRecord;
  }
  
  /**
   * Creates the new sort strategy.
   * <p>
   * For internal use only!
   * 
   * @return the new sort strategy
   */
  public IDataBrowserSortStrategy newGuiSortStrategy(Locale sortLocale)
  {
    this.lastSortStrategy = new SortStrategy(this, null, sortLocale);
    return this.lastSortStrategy;
  }
  
  /**
   * Creates the new sort strategy.
   * <p>
   * For internal use only!
   * 
   * @return the new sort strategy
   */
  public IDataBrowserSortStrategy newGuiSortStrategy()
  {
    this.lastSortStrategy = new SortStrategy(this, null, null);
    return this.lastSortStrategy;
  }
  
  /**
   * Temporary solution for sorting browser records by application programmer.
   * <p>
   * Note: Used for caretaker only!
   * 
   * @param comparator
   */
  public void setGuiSortStrategy(IDataBrowserComparator comparator)
  {
    this.lastSortStrategy = new SortStrategy(this, comparator, null);
  }

  /**
	 * Returns the current sort strategy.
	 * <p>
	 * For internal use only!
	 * 
	 * @return the current sort strategy
	 */
  public IDataBrowserSortStrategy getGuiSortStrategy()
  {
    if (this.lastSortStrategy == null)
      this.lastSortStrategy = new SortStrategy(this, null, null);
    return lastSortStrategy;
  }
  
  /**
   * @author Andreas
   *
   * To change the template for this generated type comment go to
   * Window - Preferences - Java - Code Generation - Code and Comments
   */
  private static class SortStrategy implements Comparator, IDataBrowserSortStrategy
  {
    private final IDataBrowserComparator optionalComparator;
  	// List {ColumnOrderEntry}
  	private final List columnSortOrderList;
  	
  	private final DataBrowser parent;
  	
  	private List sortedRecords;
  	
  	private int selectedIndex;
  	
  	private long changeCounter;
    
    private final Collator collator;
  	
  	/**
  	 * 
  	 */
  	private SortStrategy(DataBrowser parent, IDataBrowserComparator optionalComparator, Locale collatorLocale)
  	{
      this.optionalComparator = optionalComparator;
  	  this.parent = parent;
  		this.columnSortOrderList = new ArrayList();
  		this.sortedRecords = null;
  		this.changeCounter = -1;
  		this.selectedIndex = -1;
  		this.collator = getCollator(collatorLocale);
  	}
  	
  	public void add(int columnIndex, SortOrder sortOrder)
  	{
  		if (sortOrder == SortOrder.NONE)
  		{
  			// ignore
  			return;
  		}
  		FieldType fieldType = null;
      IBrowserField browserField = this.parent.getBrowserDefinition().getBrowserField(columnIndex);
      if (browserField instanceof IBrowserTableField)
        fieldType = ((IBrowserTableField) browserField).getTableField().getType();
      this.columnSortOrderList.add(new ColumnOrderEntry(columnIndex, sortOrder, fieldType));
  		this.sortedRecords = null;
 		}
  	
  	public int compare(Object o1, Object o2)
    {
      if (o1 == o2)
        return 0;

      IDataBrowserRecordInternal record1 = (IDataBrowserRecordInternal) o1;
      IDataBrowserRecordInternal record2 = (IDataBrowserRecordInternal) o2;

      if (this.optionalComparator != null)
      {
        int result = this.optionalComparator.compare(record1, record2);
        if (result != 0)
          return result;
      }
      else
      {
        for (int i = 0; i < this.columnSortOrderList.size(); i++)
        {
          ColumnOrderEntry orderEntry = (ColumnOrderEntry) this.columnSortOrderList.get(i);

          int result;
          if (orderEntry.fieldType == null || record1.isModifiedValue(orderEntry.columnIndex))
            result = compareValue(record1.getValueInternal(orderEntry.columnIndex), record2.getValueInternal(orderEntry.columnIndex));
          else
            result = orderEntry.fieldType.sortDataValue( //
              record1.getValueInternal(orderEntry.columnIndex), //
              record2.getValueInternal(orderEntry.columnIndex), this.collator);
          if (result != 0)
          {
            return orderEntry.sortOrder == SortOrder.ASCENDING ? result : -result;
          }
        }
      }

      return record1.getSeqNbr() - record2.getSeqNbr();
    }
  	
  	private int compareValue(Object value1, Object value2)
    {
      if (value1 == null)
      {
        if (value2 == null)
          return 0;
        
        return -1;
      }
      if (value2 == null)
        return 1;
      
      if (value1 instanceof Comparable)
      {
        if (this.collator != null && value1 instanceof String)
        {
          return this.collator.compare(value1, value2);
        }
        return ((Comparable) value1).compareTo(value2);
      }
      
      return 0;
    }
  	
  	private void recalculateSelectedIndex()
    {
      int parentSelectedIndex = this.parent.getSelectedRecordIndex();
      if (-1 == parentSelectedIndex)
      {
        this.selectedIndex = -1;
      }
      else
      {
        Object record = this.parent.getRecord(parentSelectedIndex);
        for (int i = 0; i < this.sortedRecords.size(); i++)
        {
          if (this.sortedRecords.get(i) == record)
          {
            this.selectedIndex = i;
            return;
          }
        }

        // should never occur
        throw new RuntimeException("Could not find selected record");
      }
    }
  	
  	private void checkSorting()
    {
      // do we have to perform a new sorting?
      long parentChangeCounter = this.parent.getChangeCount();
      if (this.sortedRecords == null || this.changeCounter != parentChangeCounter)
      {
        long start = System.currentTimeMillis();
        
        // perform sort
        this.sortedRecords = this.parent.cloneRecordList();
        Collections.sort(this.sortedRecords, this);
        
        // recalculate selected index
        recalculateSelectedIndex();

        this.changeCounter = parentChangeCounter;
        
        if (logger.isDebugEnabled())
          logger.debug(this.recordCount()+" records sorted in "+(System.currentTimeMillis()-start)+ "ms (selectedIndex="+getSelectedRecordIndex()+")");
      }
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.IDataBrowserSortStrategy#getRecord(int)
     */
    public IDataBrowserRecord getRecord(int index) throws IndexOutOfBoundsException
    {
      // no sort criterias or comparator present -> redirect to parent
      if (this.columnSortOrderList.isEmpty() && this.optionalComparator == null)
        return this.parent.getRecord(index);

      checkSorting();
      
      return (IDataBrowserRecord) this.sortedRecords.get(index);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.IDataBrowserSortStrategy#getSelectedRecordIndex()
     */
    public int getSelectedRecordIndex()
    {
      // no sort criterias or comparator present -> redirect to parent
      if (this.columnSortOrderList.isEmpty() && this.optionalComparator == null)
        return parent.getSelectedRecordIndex();
      
      checkSorting();
      
      return this.selectedIndex;
    }

    /**
     * IBIS: Bitte Kontrolle ob dies nicht eventuell performanter geht.
     * @param index
     */
  	public void setSelectedRecordIndex(int index)
    {
      // no sort criterias or comparator present -> redirect to parent
      if (columnSortOrderList.isEmpty() && this.optionalComparator == null)
      {
         parent.setSelectedRecordIndex(index);
         return;
      }

      Object record = sortedRecords.get(index);
      for (int i = 0; i < sortedRecords.size(); i++)
      {
        if (parent.getRecord(i) == record)
        {
          parent.setSelectedRecordIndex(i);
          selectedIndex=index;
          return;
        }
      }
      // should never occur
      throw new RuntimeException("Could not find selected record");
    }

    public IDataTableRecord getRecordToUpdate(IDataTransaction transaction, int index) throws RecordLockedException, RecordNotFoundException
    {
      // no sort criterias or comparator present -> redirect to parent
      if (columnSortOrderList.isEmpty() && this.optionalComparator == null)
         return parent.getRecordToUpdate(transaction, index);

      // IBIS: oder FREEGROUP: Mapping des Index von 'sorted' und 'real' Record verallgemeinern!!!!
      Object record = sortedRecords.get(index);
      for (int i = 0; i < sortedRecords.size(); i++)
      {
        if (parent.getRecord(i) == record)
          return parent.getRecordToUpdate(transaction, i);
      }
      // should never occur
      throw new RuntimeException("Could not find record with index ["+index+"]");
    }
    
  	/* (non-Javadoc)
     * @see de.tif.jacob.core.data.IDataBrowserSortStrategy#recordCount()
     */
    public int recordCount()
    {
      return parent.recordCount();
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.IDataBrowserSortStrategy#isColumnInvolved(int)
     */
    public SortOrder isColumnInvolved(int columnIndex)
    {
      for (int i = 0; i < this.columnSortOrderList.size(); i++)
       {
        ColumnOrderEntry orderEntry = (ColumnOrderEntry) this.columnSortOrderList.get(i);
        if (columnIndex == orderEntry.columnIndex)
          return orderEntry.sortOrder;
      }
      
      return SortOrder.NONE;
    }
  }
  
  /**
   * @author Andreas
   *
   * Helper class for column order.
   */
  private static class ColumnOrderEntry
  {
    private final int columnIndex;
    private final SortOrder sortOrder;
    private final FieldType fieldType;
    
  	private ColumnOrderEntry(int columnIndex, SortOrder sortOrder, FieldType fieldType)
  	{
  		this.columnIndex = columnIndex;
      this.sortOrder = sortOrder;
      this.fieldType = fieldType;
  	}
  }
}
