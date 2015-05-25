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

import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IAdhocBrowserDefinition;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * A data accessor is the central "entry point" for accessing data from data
 * sources. Each data accessor instance provides caching of data which has been
 * retrieved by one of the searches methods provided by {@link IDataTable} or
 * {@link IDataBrowser}.
 * 
 * @author andreas
 */
public interface IDataAccessor
{
  /**
   * Performs a complete reset of this data accessor including all underlying
   * {@link IDataTable} and {@link IDataBrowser} instances. This includes
   * clearing all QBE constraints, all cached records and any record selections.
   * 
   * @see IDataTable#qbeClear()
   * @see IDataRecordSet#clear()
   */
  public void clear();

  /**
   * Clears all underlying {@link IDataTable} instances, but keeps
   * {@link IDataBrowser} instances intact. This includes clearing all QBE
   * constraints and all cached (table) records.
   * 
   * @see IDataTable#qbeClear()
   * @see IDataRecordSet#clear()
   * @since 2.7.4
   */
  public void clearTables();

  /**
   * Clears all underlying {@link IDataTable} instances, which are involved in
   * the given relation set. This includes clearing respective QBE constraints
   * and cached (table) records.
   * 
   * @param relationSetName
   *          the relation set name
   * @since 2.7.4
   */
  public void clearTables(String relationSetName);

  /**
   * Clears all underlying {@link IDataTable} instances, which are involved in
   * the given relation set. This includes clearing respective QBE constraints
   * and cached (table) records.
   * 
   * @param relationSet
   *          the relation set
   * @since 2.7.4
   */
  public void clearTables(IRelationSet relationSet);
  
	/**
   * Performs a complete reset of all QBE constraints of any underlying
   * {@link IDataTable}instance.
   * 
   * @see IDataTable#qbeClear()
   */
	public void qbeClearAll();
	
  /**
   * Clears the QBE constraints of all underlying {@link IDataTable} instances,
   * which are involved in the given relation set.
   * 
   * @param relationSetName
   *          the relation set name
   * @since 2.7.4
   */
  public void qbeClear(String relationSetName);

  /**
   * Clears the QBE constraints of all underlying {@link IDataTable} instances,
   * which are involved in the given relation set.
   * 
   * @param relationSet
   *          the relation set
   * @since 2.7.4
   */
  public void qbeClear(IRelationSet relationSet);
  
  /**
   * Checks whether any constraint exists for the given table alias and all
   * related table aliases specified by the given relation set.
   * 
   * @param alias
   *          the table alias to check for constraints and used as starting
   *          point for related table aliases.
   * @param relationSet
   *          the relation set to check for constraints.
   * @return <code>true</code> if at least one constraint exists, otherwise
   *         <code>false</code>.
   */
	public boolean qbeHasConstraint(ITableAlias alias, IRelationSet relationSet);
	
	/**
   * Checks whether any constraint exists for the given table alias and all
   * related table aliases specified by the given relation set.
   * <p>
   * This method is equivalent to
   * {@link #qbeHasConstraint(ITableAlias, IRelationSet)} except that table
   * alias and relation set are specified by name.
   * 
   * @param aliasName
   *          the name of the table alias to check for constraints and used as
   *          starting point for related table aliases.
   * @param relationSetName
   *          the name of the relation set to check for constraints.
   * @return <code>true</code> if at least one constraint exists, otherwise
   *         <code>false</code>.
   */
	public boolean qbeHasConstraint(String aliasName, String relationSetName);
	
  /**
   * Creates a new data browser for the given adhoc browser definition.
   * <p>
   * Note: The returned data browser is not cleared when {@link #clear()} is
   * performed.
   * 
   * @param adhocBrowserDefinition the adhoc browser definition
   * @return the newly created data browser
   * 
   * @see IApplicationDefinition#createAdhocBrowserDefinition(IBrowserDefinition)
   * @see IApplicationDefinition#createAdhocBrowserDefinition(ITableAlias)
   */
  public IDataBrowser createBrowser(IAdhocBrowserDefinition adhocBrowserDefinition);
  
  /**
   * Gets the data browser of the given browser definition.
   * 
   * @param browserDefinition
   *          the browser definition
   * @return the desired data browser
   * @throws RuntimeException if the given browser definition is a {@link IAdhocBrowserDefinition}
   */
  public IDataBrowser getBrowser(IBrowserDefinition browserDefinition) throws RuntimeException;
  
	/**
   * Gets the data browser specified by name.
   * <p>
   * The name of the data browser is equivalent to the name of the underlying
   * {@link IBrowserDefinition}.
   * 
   * @param name
   *          the name of data browser
   * @return the desired data browser
   * @throws RuntimeException
   *           if no data browser with the given name exists
   */
  public IDataBrowser getBrowser(String name) throws RuntimeException;
  
	/**
   * Gets the data table specified by name.
   * <p>
   * The name of the data table is equivalent to the name of the underlying
   * {@link ITableAlias}.
   * 
   * @param name
   *          the name of the data table.
   * @return the desired data table
   * @throws RuntimeException
   *           if no data table with the given name exists
   */
  public IDataTable getTable(String name) throws RuntimeException;
  
  /**
   * Gets the data table of the given table alias.
   * 
   * @param alias
   *          the table alias
   * @return the desired data table
   */
  public IDataTable getTable(ITableAlias alias);
  
	/**
	 * Creates a new data accessor instance.
	 *  
	 * @return the new accessor instance.
	 */
	public IDataAccessor newAccessor();
  
	/**
   * Creates a new data transaction.
   * <p>
   * Note: To create a new embedded transaction call
   * {@link IDataTransaction#newEmbeddedTransaction(de.tif.jacob.core.data.IDataTransaction.EmbeddedTransactionMode)}.
   * 
   * @return the new transaction.
   */
  public IDataTransaction newTransaction();
  
	/**
	 * Returns the application definition used for this accessor.
	 * 
	 * @return the application definition.
	 */
	public IApplicationDefinition getApplication();
	
  /**
	 * Propagates the given record, i.e. "refreshes" the record itself and all
	 * records which are linked to each other by means of any defined relation.
	 * The fill direction determines in which direction(s) the propagation should
	 * be processed. The starting point of the propagation is always the given
	 * record.
	 * <p>
	 * Note: This method has identical behaviour than calling
	 * {@link #propagateRecord(IDataRecord, IRelationSet, Filldirection)} in case
	 * the relation set is the default relation set.
	 * 
	 * @param record
	 *          the record to propagate
	 * @param filldirection
	 *          the fill direction to propagate
	 * @return <code>true</code> if the propagation had been successful, <code>false</code>
	 *         if the given record could not be propagate, because a primary key
	 *         is missing
	 * @throws RecordNotFoundException
	 *           If the given record could not be reloaded from the database.
	 */
	public boolean propagateRecord(IDataRecord record, Filldirection filldirection) throws RecordNotFoundException;
	
  /**
	 * Propagates the given record, i.e. "refreshes" the record itself and all
	 * records which are linked to each other by means of relations contained in
	 * the specified relation set. The fill direction determines in which
	 * direction(s) the propagation should be processed. The starting point of
	 * the propagation is always the given record.
	 * 
	 * @param record
	 *          the record to propagate
	 * @param relationSet
	 *          the relation set to propagate
	 * @param filldirection
	 *          the fill direction to propagate
	 * @return <code>true</code> if the propagation had been successful, <code>false</code>
	 *         if the given record could not be propagate, because a primary key
	 *         is missing
	 * @throws RecordNotFoundException
	 *           If the given record could not be reloaded from the database.
	 */
	public boolean propagateRecord(IDataRecord record, IRelationSet relationSet, Filldirection filldirection) throws RecordNotFoundException;
	
  /**
	 * Propagates the given record, i.e. "refreshes" the record itself and all
	 * records which are linked to each other by means of relations contained in
	 * the specified relation set. The fill direction determines in which
	 * direction(s) the propagation should be processed. The starting point of
	 * the propagation is always the given record.
	 * 
	 * @param record
	 *          the record to propagate
	 * @param relationSetName
	 *          the name of the relation set to propagate
	 * @param filldirection
	 *          the fill direction to propagate
	 * @return <code>true</code> if the propagation had been successful, <code>false</code>
	 *         if the given record could not be propagate, because a primary key
	 *         is missing
	 * @throws RecordNotFoundException
	 *           If the given record could not be reloaded from the database.
	 */
	public boolean propagateRecord(IDataRecord record, String relationSetName, Filldirection filldirection) throws RecordNotFoundException;
	
	/**
	 * Same behaviour than
	 * {@link #cloneRecord(IDataTransaction, IDataTableRecord, ITableAlias)}
	 * except that the alias to clone the record for equals to the related alias of <code>recordToClone</code>.
	 * 
   * @param trans
   *          the transaction in which context this operation should be executed
	 * @param recordToClone
	 *          the record to clone
	 * @return the new/cloned record
	 */
	public IDataTableRecord cloneRecord(IDataTransaction trans, IDataTableRecord recordToClone);
	
	/**
	 * Same behaviour than
	 * {@link #cloneRecord(IDataTransaction, IDataTableRecord, ITableAlias)}
	 * except that the alias to clone the record for is specified by name.
	 * 
   * @param trans
   *          the transaction in which context this operation should be executed
	 * @param recordToClone
	 *          the record to clone
	 * @param aliasName
	 *          the name of the table alias to clone the record for, which must
	 *          point to the same physical table than the related alias of <code>recordToClone</code>!
	 * @return the new/cloned record
	 */
	public IDataTableRecord cloneRecord(IDataTransaction trans, IDataTableRecord recordToClone, String aliasName);
	
	/**
   * Clones the given table record, i.e. create a new table record for the
   * specified table alias and copies the field values from the record to clone
   * to the new record.
   * <p>
   * After this method has been called the returned record becomes the selected
   * record of the data table referenced by <code>alias</code> of this
   * accessor. Hence, previously populated records of that data table will be
   * cleared!
   * <p>
   * Call {@link IDataTransaction#commit()} to save this record to the data
   * source.
   * <p>
   * Note: The values of the primary key and the history field (see
   * {@link ITableDefinition#getHistoryField()}, if existing, are not copied!
   * 
   * @param trans
   *          the transaction in which context this operation should be executed
   * @param recordToClone
   *          the record to clone
   * @param alias
   *          the table alias to clone the record for, which must point to the
   *          same physical table than the related alias of
   *          <code>recordToClone</code>!
   * @return the new/cloned record
   * 
   * @see IDataTable#getSelectedRecord()
   */
  public IDataTableRecord cloneRecord(IDataTransaction trans, IDataTableRecord recordToClone, ITableAlias alias);
}
