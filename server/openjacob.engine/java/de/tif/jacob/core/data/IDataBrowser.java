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
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * A data browser represents a record set containing data browser records, i.e.
 * instances of {@link IDataBrowserRecord}. The structure of each record
 * contained is determined by the {@link IBrowserDefinition} the data browser is
 * based on.
 * <p>
 * A data browser instance could be obtained by means of
 * <li>{@link IDataAccessor#getBrowser(IBrowserDefinition)},
 * <li>{@link IDataAccessor#getBrowser(String)},
 * <li>{@link IDataAccessor#createBrowser(IAdhocBrowserDefinition)}.
 * 
 * @author andreas
 */
public interface IDataBrowser extends IDataRecordSet, IBrowserRecordList
{
  /**
   * Returns the name of this data browser, which equals to the name of the
   * underlying {@link IBrowserDefinition}.
   * 
   * @return the data browser name
   */
  public String getName();
  
  /**
   * Returns the browser definition of this data browser.
   * 
   * @return the browser definition
   * @since 2.7.4
   */
  public IBrowserDefinition getBrowserDefinition();
  
  /**
   * Returns the index of the selected record
   * 
   * @return The index of the selected record or <code>-1</code>, if no
   *         record is selected.
   */
  public int getSelectedRecordIndex();

  /**
   * Selects the record specified by the given record index.
   * <p>
   * After this method has been called, the given record index would be returned
   * by {@link #getSelectedRecordIndex()}.
   * 
   * @param index
   *          the index of the record to select.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= index < {@link IDataRecordSet#recordCount()}</code>
   *           is not fulfilled
   */
  public void setSelectedRecordIndex(int index) throws IndexOutOfBoundsException;

  /**
   * Clears all record selections.
   */
  public void clearSelections();

  /**
   * Returns the data browser record given by its index.
   * 
   * @param index
   *          the index of the record to get.
   * 
   * @return the desired record
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= index < {@link IDataRecordSet#recordCount()}</code>
   *           is not fulfilled
   */
  public IDataBrowserRecord getRecord(int index) throws IndexOutOfBoundsException;

  /**
   * Removes a data browser record from this data browser.
   * 
   * @param record
   *          removes the given record (instance) from this data browser.
   * 
   * @return <code>true</code> if the record has been successfully removed,
   *         <code>false</code> otherwise, i.e. the record instance is not
   *         contained within this data browser.
   * @since 2.7.4
   */
  public boolean removeRecord(IDataBrowserRecord record);

  /**
   * Propagates the selections for the browser specified.
   * <p>
   * When a selection is propagated, then all data tables related to this data
   * browser are filled. Related data tables are determined by the relation set
   * and the fill direction used in the last search operation.
   * <p>
   * Note that propagation may be an expensive operation, i.e. causing many data
   * source accesses.
   * 
   * @throws RecordNotFoundException
   *           if any data table record could not be retrieved from a data
   *           source
   * @throws RuntimeException
   *           if no selections to propagate exist or any further error occurs
   *           during propagation
   */
  public void propagateSelections() throws RecordNotFoundException, RuntimeException;

  /**
   * Populates the data browser by means of performing a search operation,
   * which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing.
   * 
   * <p>
   * Note that the number of records retrieved from data source is limited to
   * the current maximum record setting.
   * <p>
   * This method is equivalent to {@link #search(IRelationSet, Filldirection)}
   * except that the relation set is specified by name and
   * {@link Filldirection#BACKWARD} is used by default.
   * 
   * @param relationSetName
   *          the name of the relation set
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @see IDataRecordSet#getMaxRecords()
   * @see IDataRecordSet#setMaxRecords(int)
   */
  public void search(String relationSetName) throws InvalidExpressionException;

  /**
   * Populates the data browser by means of performing a search operation,
   * which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing.
   * 
   * <p>
   * Note that the number of records retrieved from data source is limited to
   * the current maximum record setting.
   * <p>
   * This method is equivalent to {@link #search(IRelationSet, Filldirection)}
   * except that {@link Filldirection#BACKWARD} is used by default.
   * 
   * @param relationSet
   *          the relation set
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @see IDataRecordSet#getMaxRecords()
   * @see IDataRecordSet#setMaxRecords(int)
   */
  public void search(IRelationSet relationSet) throws InvalidExpressionException;

  /**
   * Populates the data browser by means of performing a search operation,
   * which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing.
   * 
   * <p>
   * Note that the number of records retrieved from data source is limited to
   * the current maximum record setting.
   * <p>
   * This method is equivalent to {@link #search(IRelationSet, Filldirection)}
   * except that the relation set is specified by name.
   * 
   * @param relationSetName
   *          the name of relation set
   * @param filldirection
   *          the fill direction to use for subsequent calls of
   *          {@link #propagateSelections()}.
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @see IDataRecordSet#getMaxRecords()
   * @see IDataRecordSet#setMaxRecords(int)
   */
  public void search(String relationSetName, Filldirection filldirection) throws InvalidExpressionException;

  /**
   * Populates the data browser by means of performing a search operation,
   * which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing.
   * 
   * <p>
   * Note that the number of records retrieved from data source is limited to
   * the current maximum record setting.
   * 
   * @param relationSet
   *          the relation set
   * @param filldirection
   *          the fill direction to use for subsequent calls of
   *          {@link #propagateSelections()}.
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @see IDataRecordSet#getMaxRecords()
   * @see IDataRecordSet#setMaxRecords(int)
   */
  public void search(IRelationSet relationSet, Filldirection filldirection) throws InvalidExpressionException;

  /**
   * Populates the data browser by means of performing a search operation,
   * which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing and
   * <li>the given where clause.
   * 
   * <p>
   * Note that the number of records retrieved from data source is limited to
   * the current maximum record setting.
   * <p>
   * This method is equivalent to {@link #searchWhere(IRelationSet, Filldirection, String)}
   * except that the relation set is specified by name and
   * {@link Filldirection#BACKWARD} is used by default.
   * 
   * @param relationSetName
   *          the name of the relation set
   * @param whereClause the SQL-like where clause, e.g. "employee.avail = 1 OR employee.ownerrole = 1"
   * @throws InvalidExpressionException
   *           if the where clause or any given QBE constraint contains an invalid expression
   * @see IDataRecordSet#getMaxRecords()
   * @see IDataRecordSet#setMaxRecords(int)
   */
  public void searchWhere(String relationSetName, String whereClause) throws InvalidExpressionException;

  /**
   * Populates the data browser by means of performing a search operation,
   * which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing and
   * <li>the given where clause.
   * 
   * <p>
   * Note that the number of records retrieved from data source is limited to
   * the current maximum record setting.
   * 
   * @param relationSet
   *          the name of the relation set
   * @param filldirection
   *          the fill direction to use for subsequent calls of
   *          {@link #propagateSelections()}.
   * @param whereClause the SQL-like where clause, e.g. "employee.avail = 1 OR employee.ownerrole = 1"
   * @throws InvalidExpressionException
   *           if the where clause or any given QBE constraint contains an invalid expression
   * @see IDataRecordSet#getMaxRecords()
   * @see IDataRecordSet#setMaxRecords(int)
   */
  public void searchWhere(IRelationSet relationSet, Filldirection filldirection, String whereClause) throws InvalidExpressionException;

  /**
   * Populates the data browser by means of performing a search operation,
   * which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing and
   * <li>the given where clause.
   * 
   * <p>
   * Note that the number of records retrieved from data source is limited to
   * the current maximum record setting.
   * <p>
   * This method is equivalent to {@link #searchWhere(IRelationSet, Filldirection, String)}
   * except that {@link Filldirection#BACKWARD} is used by default.
   * 
   * @param relationSet
   *          the relation set
   * @param whereClause the SQL-like where clause, e.g. "employee.avail = 1 OR employee.ownerrole = 1"
   * @throws InvalidExpressionException
   *           if the where clause or any given QBE constraint contains an invalid expression
   * @see IDataRecordSet#getMaxRecords()
   * @see IDataRecordSet#setMaxRecords(int)
   */
  public void searchWhere(IRelationSet relationSet, String whereClause) throws InvalidExpressionException;

  /**
   * Populates the data browser by means of performing a search operation,
   * which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing and
   * <li>the given where clause.
   * 
   * <p>
   * Note that the number of records retrieved from data source is limited to
   * the current maximum record setting.
   * <p>
   * This method is equivalent to {@link #searchWhere(IRelationSet, Filldirection, String)}
   * except that the relation set is specified by name.
   * 
   * @param relationSetName
   *          the name of the relation set
   * @param filldirection
   *          the fill direction to use for subsequent calls of
   *          {@link #propagateSelections()}.
   * @param whereClause the SQL-like where clause, e.g. "employee.avail = 1 OR employee.ownerrole = 1"
   * @throws InvalidExpressionException
   *           if the where clause or any given QBE constraint contains an invalid expression
   * @see IDataRecordSet#getMaxRecords()
   * @see IDataRecordSet#setMaxRecords(int)
   */
  public void searchWhere(String relationSetName, Filldirection filldirection, String whereClause) throws InvalidExpressionException;
  
  /**
   * Performs a search operation, which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing.
   * 
   * <p>
   * For each record retrieved
   * {@link IDataBrowserSearchIterateCallback#onNextRecord(IDataBrowserRecord)} will
   * be called, but <b>no</b> population of this data browser takes place, i.e.
   * {@link #recordCount()} will return <code>0</code> in any case. This method
   * could be used to process a large amount of records without running into a
   * {@link OutOfMemoryError}.
   * <p>
   * Note that the number of records retrieved from data source is <b>not<b>
   * limited to the current maximum record setting.
   * 
   * @param callback
   *          the callback to be invoked for each retrieved record
   * @param relationSetName
   *          the name of the relation set
   * @return the number of records retrieved
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @since 2.10
   */
  public int searchAndIterate(IDataBrowserSearchIterateCallback callback, String relationSetName) throws InvalidExpressionException;

  /**
   * Performs a search operation, which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing.
   * 
   * <p>
   * For each record retrieved
   * {@link IDataBrowserSearchIterateCallback#onNextRecord(IDataBrowserRecord)} will
   * be called, but <b>no</b> population of this data browser takes place, i.e.
   * {@link #recordCount()} will return <code>0</code> in any case. This method
   * could be used to process a large amount of records without running into a
   * {@link OutOfMemoryError}.
   * <p>
   * Note that the number of records retrieved from data source is <b>not<b>
   * limited to the current maximum record setting.
   * 
   * @param callback
   *          the callback to be invoked for each retrieved record
   * @param relationSet
   *          the relation set
   * @return the number of records retrieved
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @since 2.10
   */
  public int searchAndIterate(IDataBrowserSearchIterateCallback callback, IRelationSet relationSet) throws InvalidExpressionException;

  /**
   * Performs a search operation, which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing and
   * <li>the given where clause.
   * 
   * <p>
   * For each record retrieved
   * {@link IDataBrowserSearchIterateCallback#onNextRecord(IDataBrowserRecord)} will
   * be called, but <b>no</b> population of this data browser takes place, i.e.
   * {@link #recordCount()} will return <code>0</code> in any case. This method
   * could be used to process a large amount of records without running into a
   * {@link OutOfMemoryError}.
   * <p>
   * Note that the number of records retrieved from data source is <b>not<b>
   * limited to the current maximum record setting.
   * 
   * @param callback
   *          the callback to be invoked for each retrieved record
   * @param relationSetName
   *          the name of the relation set
   * @param whereClause the SQL-like where clause, e.g. "employee.avail = 1 OR employee.ownerrole = 1"
   * @return the number of records retrieved
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @since 2.10
   */
  public int searchWhereAndIterate(IDataBrowserSearchIterateCallback callback, String relationSetName, String whereClause) throws InvalidExpressionException;

  /**
   * Performs a search operation, which will be constrained by
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing and
   * <li>the given where clause.
   * 
   * <p>
   * For each record retrieved
   * {@link IDataBrowserSearchIterateCallback#onNextRecord(IDataBrowserRecord)} will
   * be called, but <b>no</b> population of this data browser takes place, i.e.
   * {@link #recordCount()} will return <code>0</code> in any case. This method
   * could be used to process a large amount of records without running into a
   * {@link OutOfMemoryError}.
   * <p>
   * Note that the number of records retrieved from data source is <b>not<b>
   * limited to the current maximum record setting.
   * 
   * @param callback
   *          the callback to be invoked for each retrieved record
   * @param relationSet
   *          the relation set
   * @param whereClause the SQL-like where clause, e.g. "employee.avail = 1 OR employee.ownerrole = 1"
   * @return the number of records retrieved
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @since 2.10
   */
  public int searchWhereAndIterate(IDataBrowserSearchIterateCallback callback, IRelationSet relationSet, String whereClause) throws InvalidExpressionException;

  /**
   * Returns the relation set of the last search operation.
   * 
   * @return the relation set or <code>null</code> if no search has been
   *         performed after last clear action.
   */
  public IRelationSet getRelationSet();

  /**
   * Returns the fill direction of the last search operation.
   * 
   * @return the fill direction or <code>null</code> if no search has been
   *         performed after last clear action.
   */
  public Filldirection getFillDirection();
}
