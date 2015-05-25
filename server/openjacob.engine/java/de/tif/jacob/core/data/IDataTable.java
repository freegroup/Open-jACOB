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

import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;


/**
 * A data table represents a record set containing data table records, i.e.
 * instances of {@link IDataTableRecord}. The structure of each record
 * contained is determined by the {@link ITableDefinition} of the underlying
 * {@link ITableAlias}.
 * <p>
 * A data table instance could be obtained by means of
 * <li>{@link IDataAccessor#getTable(ITableAlias)},
 * <li>{@link IDataAccessor#getTable(String)}.
 * 
 * @author andreas
 */
public interface IDataTable extends IDataRecordSet
{
  /**
   * Returns the name of this data table, which equals to the name of the
   * underlying {@link ITableAlias}.
   * 
   * @return the data table name
   */
  public String getName();
  
  /**
   * Clears all records populated, clears the record cache and closes the
   * transaction associated with the selected record (i.e. the table
   * transaction), if existing.
   * <p>
   * 
   * Note that this method does not implicitly invoke {@link #qbeClear()}, i.e.
   * QBE constraints are not cleared.
   * 
   * @see #getTableTransaction()
   * @see #clearRecords()
   */
  public void clear();

  /**
   * Clears all records populated.
   * <p>
   * 
   * Note: In contrast to {@link #clear()} this method does not clear the record
   * cache nor close the transaction associated with the selected record (i.e.
   * the table transaction).
   * 
   * @since 2.8.2
   */
  public void clearRecords();

	/**
   * Removes all QBE constraints which have been set for this data table.
   * <p>
   * This has the effect that a subsequent search call will either not be
   * constrained at all or only be constrained by the condition of the underlying
   * table alias, in case such a condition exists.
   * <p>
   * 
   * Note that {@link #clear()} does not implicitly invoke this
   * method. Nevertheless, {@link IDataAccessor#clear()} executed on the parent
   * data accessor does clear QBE constraints of all data tables as well.
   */
  public void qbeClear();
	
  /**
   * Removes all QBE constraints which have been set for the given table field.
   * 
   * @param tableFieldName
   *          the name of the table field
   * @throws NoSuchFieldException
   *           if no such table field exists for this table
   * @since 2.7.2           
   */
  public void qbeClear(String tableFieldName) throws NoSuchFieldException;

  /**
   * Removes all QBE constraints which have been set for the given table field.
   * 
   * @param tableField
   *          the table field
   * @since 2.7.2           
   */
  public void qbeClear(ITableField tableField);
  
  /**
   * Marks all QBE constraints as optional.
   * <p>
   * This has the effect that a search call performed on a linked table alias
   * (either by searching on {@link IDataTable} or {@link IDataBrowser}) will
   * result into an outer join (instead of an inner join). For search calls
   * performed on the related table alias of this data table (see
   * {@link IDataRecordSet#getTableAlias()}) marking QBE constraints as
   * optional has no effect.
   * <p>
   * By default QBE constraints are not marked as optional. Calling
   * {@link #qbeClear()} or {@link IDataAccessor#qbeClearAll()} will reset the
   * optional flag, i.e. added QBE constraints will be mandatory.
   */
  public void qbeSetOptional();
  
  /**
   * Checks whether any constraint is set on any table field.
   * 
   * @return <code>true</code> if a constraint is set, otherwise <code>false</code>.
   */
  public boolean qbeHasConstraint();
  
  /**
   * Checks whether any constraint is set on the given table field.
   * 
   * @param tableField
   *          the table field
   * @return <code>true</code> if a constraint is set, otherwise
   *         <code>false</code>.
   * @since 2.7.2
   */
  public boolean qbeHasConstraint(ITableField tableField);

  /**
   * Checks whether any constraint is set on the given table field.
   * 
   * @param tableFieldName
   *          the name of the table field
   * @return <code>true</code> if a constraint is set, otherwise
   *         <code>false</code>.
   * @throws NoSuchFieldException
   *           if no such table field exists for this table
   * @since 2.7.2
   */
  public boolean qbeHasConstraint(String tableFieldName) throws NoSuchFieldException;
  
  /**
	 * Sets the QBE values for all table fields of the table's primary key to the
	 * given primary key value.
	 * <p>
	 * 
	 * @param primaryKeyValue
	 *          the primary key value
	 */
	public void qbeSetPrimaryKeyValue(IDataKeyValue primaryKeyValue);

  /**
   * Sets the QBE constraint values for all table fields of the given key to the
   * given key value.
   * <p>
   * 
   * Note: Setting <code>keyValue</code> to <code>null</code> constrains a
   * search in such a way that all key fields have to be <code>null</code>.
   * 
   * @param key
   *          the key
   * @param keyValue
   *          the key value or <code>null</code>
   */
  public void qbeSetKeyValue(IKey key, IDataKeyValue keyValue);
  
  /**
   * Sets a QBE constraint value for the given table field. In contrast to
   * {@link #qbeSetValue(String, Object)} search modifiers are not considered,
   * i.e. for <code>String</code> values searching is always done on exact
   * match basis.
   * <p>
   * 
   * Note: Setting <code>value</code> to <code>null</code> is identical with
   * <code>qbeSetValue(tableFieldName, "NULL")</code>.
   * 
   * @param tableFieldName
   *          the name of the table field
   * @param value
   *          the QBE value to set or <code>null</code>
   * @throws NoSuchFieldException
   *           if no such table field exists for this table
   */
  public void qbeSetKeyValue(String tableFieldName, Object value) throws NoSuchFieldException;
  
  /**
   * Sets a QBE constraint value for the given table field. In contrast to
   * {@link #qbeSetValue(ITableField, Object)} search modifiers are not
   * considered, i.e. for <code>String</code> values searching is always done
   * on exact match basis.
   * <p>
   * 
   * Note: Setting <code>value</code> to <code>null</code> is identical with
   * <code>qbeSetValue(tableField, "NULL")</code>.
   * 
   * @param tableField
   *          the table field
   * @param value
   *          the QBE value to set or <code>null</code>
   */
  public void qbeSetKeyValue(ITableField tableField, Object value);
  
  /**
   * Sets a QBE constraint value for the given table field. Search modifiers could be used
   * by means of passing a appropriate <code>String</code> value to argument
   * <code>value</code>. The following modifiers are existing:<br>
   * <blockquote>
   * <pre>
   *  <i>common modifiers:</i>
   *          |         logical OR              Examples: <b>john|liz</b> or <b>1|2|6..10</b>
   *          |         leading logical OR      Examples: <b>|john</b> or <b>|6..10</b>
   *                    i.e. grouping logical OR    Note: Explanation see below!
   *          !         logical NOT             Examples: <b>!NULL</b> or <b>!completed</b>
   *          NULL      field is empty          Examples: <b>NULL|new</b> or <b>!NULL</b>
   * 
   *  <i>common modifiers (not for boolean values):</i>
   *          >         greater than            Examples: <b>&gt;8</b> or <b>&gt;d</b>
   *          >=        greater or equal than   Examples: <b>&gt;=1.1</b>
   *          <         less than               Examples: <b>&lt;-4.5</b> or <b>&lt;0|&gt;10</b>
   *          <=        less or equal than      Examples: <b>&lt;=0</b>
   * 
   *  <i>text modifiers:</i>
   *          =         enforce exact match     Examples: <b>=completed</b>
   *          ^         enforce left anchored   Examples: <b>^SELECT</b>
   *          $         enforce right anchored  Examples: <b>ing$</b>
   *          &         logical AND             Examples: <b>error&SQL</b>
   *          *         wildcard                Examples: <b>Bill*ates</b>
   *          ?         single char wildcard    Examples: <b>No.???</b>
   *          "         escape text literal     Examples: <b>"&lt;tag&gt;"</b> (interpret > and < as normal characters)
   * 
   *  <i>numerical modifiers:</i>
   *          ..        range                   Examples: <b>0..100</b>
   *          &         logical AND             Examples: <b>!4&!7</b> or <b>!4&!7&&lt;10</b>
   * 
   *  <i>boolean modifiers:</i>
   *          false     boolean false           Examples: <b>false|NULL</b>
   *          true      boolean true            Examples: <b>true</b>
   * 
   *  <i>datetime modifiers:</i>
   *          today     date value of today           Examples: <b>&lt;=today-7d</b>
   *          now       timestamp value of now        Examples: <b>&gt;now-1h</b>
   *          ..        range                         Examples: <b>now-24h..now</b>
   *          thisy     this year range               Examples: <b>thisy</b> or <b>thisy-1y</b> (previous year range)
   *          thisq     this quarter range            Examples: <b>thisq</b> or <b>thisq-3m</b> (previous quarter range)
   *          thism     this month range              Examples: <b>thisq</b> or <b>thisq-1m</b> (previous month range)
   *          thisw     this week range               Examples: <b>thisw</b> or <b>thisw-7d</b> (previous week range)
   *          ytd       this year to date range       Examples: <b>ytd</b> or <b>ytd-1y</b> (previous year to date range, e.g. 2004-01-01..2004-05-13)
   *          qtd       this quarter to date range    Examples: <b>qtd</b> or <b>qtd-3m</b> (previous quarter to date range, e.g. 2004-04-01..2004-06-03)
   *          mtd       this month to date range      Examples: <b>mtd</b> or <b>mtd-1m</b> (previous month to date range, e.g. 2004-05-01..2004-05-27)
   *          wtd       this week to date range       Examples: <b>wtd</b> or <b>wtd-7d</b> (previous week to date range)
   *          weekx     calendar week x of this year  Examples: <b>week1</b> or <b>week1..week10</b>
   *          [+-]xsec  add/sub x seconds             Examples: <b>now-30sec..now</b>
   *          [+-]xmin  add/sub x minutes             Examples: <b>now-10min</b>
   *          [+-]xh    add/sub x hours               Examples: <b>now-13h</b>
   *          [+-]xd    add/sub x days                Examples: <b>today-14d</b>
   *          [+-]xm    add/sub x months              Examples: <b>today-3m</b>
   *          [+-]xy    add/sub x years               Examples: <b>today-1y..today</b>
   * </pre>
   * </blockquote>
   * <p>
   * <b>Grouping logical OR</b><br>
   * By default QBE constraints of different table fields are logically ANDed,
   * if a search operation is performed. Nevertheless, by means of a grouping
   * logical OR QBE constraints could be grouped to one logical OR group.<br>
   * Example: A <code>call</code> table has a <code>status</code> and a
   * <code>created</code> table field. If you want to search for calls which
   * are either created today or are in status
   * <code>new</code>, you could do this by means of setting the QBE constraint value of 
   * <code>status</code> to <code>"|new"</code> and the QBE constraint value 
   * of <code>created</code> to <code>"|today"</code>.
   * 
   * @param tableFieldName
   *          the name of the table field
   * @param value
   *          the QBE value to set
   * @throws NoSuchFieldException
   *           if no such table field exists for this table
   */
  public void qbeSetValue(String tableFieldName, Object value) throws NoSuchFieldException;

  /**
   * Sets a QBE constraint value for the given table field. Search modifiers could be used
   * by means of passing a appropriate <code>String</code> value to argument
   * <code>value</code>. The following modifiers are existing: <br>
   * <blockquote>
   * <pre>
   *  <i>common modifiers:</i>
   *          |         logical OR              Examples: <b>john|liz</b> or <b>1|2|6..10</b>
   *          |         leading logical OR      Examples: <b>|john</b> or <b>|6..10</b>
   *                    i.e. grouping logical OR    Note: Explanation see below!
   *          !         logical NOT             Examples: <b>!NULL</b> or <b>!completed</b>
   *          NULL      field is empty          Examples: <b>NULL|new</b> or <b>!NULL</b>
   * 
   *  <i>common modifiers (not for boolean values):</i>
   *          >         greater than            Examples: <b>&gt;8</b> or <b>&gt;d</b>
   *          >=        greater or equal than   Examples: <b>&gt;=1.1</b>
   *          <         less than               Examples: <b>&lt;-4.5</b> or <b>&lt;0|&gt;10</b>
   *          <=        less or equal than      Examples: <b>&lt;=0</b>
   * 
   *  <i>text modifiers:</i>
   *          =         enforce exact match     Examples: <b>=completed</b>
   *          ^         enforce left anchored   Examples: <b>^SELECT</b>
   *          $         enforce right anchored  Examples: <b>ing$</b>
   *          &         logical AND             Examples: <b>error&SQL</b>
   *          *         wildcard                Examples: <b>Bill*ates</b>
   *          ?         single char wildcard    Examples: <b>No.???</b>
   *          "         escape text literal     Examples: <b>"&lt;tag&gt;"</b> (interpret > and < as normal characters)
   * 
   *  <i>numerical modifiers:</i>
   *          ..        range                   Examples: <b>0..100</b>
   *          &         logical AND             Examples: <b>!4&!7</b> or <b>!4&!7&&lt;10</b>
   * 
   *  <i>boolean modifiers:</i>
   *          false     boolean false           Examples: <b>false|NULL</b>
   *          true      boolean true            Examples: <b>true</b>
   * 
   *  <i>datetime modifiers:</i>
   *          today     date value of today           Examples: <b>&lt;=today-7d</b>
   *          now       timestamp value of now        Examples: <b>&gt;now-1h</b>
   *          ..        range                         Examples: <b>now-24h..now</b>
   *          thisy     this year range               Examples: <b>thisy</b> or <b>thisy-1y</b> (previous year range)
   *          thisq     this quarter range            Examples: <b>thisq</b> or <b>thisq-3m</b> (previous quarter range)
   *          thism     this month range              Examples: <b>thisq</b> or <b>thisq-1m</b> (previous month range)
   *          thisw     this week range               Examples: <b>thisw</b> or <b>thisw-7d</b> (previous week range)
   *          ytd       this year to date range       Examples: <b>ytd</b> or <b>ytd-1y</b> (previous year to date range, e.g. 2004-01-01..2004-05-13)
   *          qtd       this quarter to date range    Examples: <b>qtd</b> or <b>qtd-3m</b> (previous quarter to date range, e.g. 2004-04-01..2004-06-03)
   *          mtd       this month to date range      Examples: <b>mtd</b> or <b>mtd-1m</b> (previous month to date range, e.g. 2004-05-01..2004-05-27)
   *          wtd       this week to date range       Examples: <b>wtd</b> or <b>wtd-7d</b> (previous week to date range)
   *          weekx     calendar week x of this year  Examples: <b>week1</b> or <b>week1..week10</b>
   *          [+-]xsec  add/sub x seconds             Examples: <b>now-30sec..now</b>
   *          [+-]xmin  add/sub x minutes             Examples: <b>now-10min</b>
   *          [+-]xh    add/sub x hours               Examples: <b>now-13h</b>
   *          [+-]xd    add/sub x days                Examples: <b>today-14d</b>
   *          [+-]xm    add/sub x months              Examples: <b>today-3m</b>
   *          [+-]xy    add/sub x years               Examples: <b>today-1y..today</b>
   * </pre>
   * </blockquote>
   * <p>
   * <b>Grouping logical OR</b><br>
   * By default QBE constraints of different table fields are logically ANDed,
   * if a search operation is performed. Nevertheless, by means of a grouping
   * logical OR QBE constraints could be grouped to one logical OR group.<br>
   * Example: A <code>call</code> table has a <code>status</code> and a
   * <code>created</code> table field. If you want to search for calls which
   * are either created today or are in status
   * <code>new</code>, you could do this by means of setting the QBE constraint value of 
   * <code>status</code> to <code>"|new"</code> and the QBE constraint value 
   * of <code>created</code> to <code>"|today"</code>.
   * 
   * @param tableField
   *          the table field
   * @param value
   *          the QBE value to set
   */
  public void qbeSetValue(ITableField tableField, Object value);
  
  /**
   * Returns the data table record given by its index.
   * 
   * @param index
   *          the index of the record to get.
   * 
   * @return the desired record
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= index < {@link IDataRecordSet#recordCount()}</code>
   *           is not fulfilled
   */
	public IDataTableRecord getRecord(int index) throws IndexOutOfBoundsException;
	
  /**
   * Returns the data table record given by its primary key.
   * <p>
   * Note: In contrast to {@link #loadRecord(IDataKeyValue)} this method first
   * looks into the record cache. If the record is not already cached, it is
   * loaded from the data source.
   * <p>
   * Additional note: In contrast to {@link #setSelectedRecord(IDataKeyValue)}}
   * the loaded record does not become the selected record!
   * 
   * @param primaryKey
   *          the primary key value of the desired record
   * @return the desired record
   * @throws RecordNotFoundException
   *           if the record could not be retrieved from data source
   */
  public IDataTableRecord getRecord(IDataKeyValue primaryKey) throws RecordNotFoundException;
  
  /**
   * Sets the selected record of this data table given by its primary key.
   * 
   * @param primaryKey
   *          the primary key value of the record to become the selected record
   * @return the selected record
   * @throws RecordNotFoundException
   *           if the record could not be retrieved from data source
   */
  public IDataTableRecord setSelectedRecord(IDataKeyValue primaryKey) throws RecordNotFoundException;
  
	/**
   * Loads the data table record given by its primary key from the data source.
   * <p>
   * Note: In contrast to {@link #getRecord(IDataKeyValue)} this method always
   * retrieves the record from the physical data store, i.e. this method is more
   * expensive than {@link #getRecord(IDataKeyValue)}.
   * <p>
   * Additional note: In contrast to {@link #setSelectedRecord(IDataKeyValue)}}
   * the loaded record does not become the selected record!
   * 
   * @param primaryKey
   *          the primary key value of the desired record
   * @return the desired record
   * @throws RecordNotFoundException
   *           if the record could not be retrieved from data source
   */
  public IDataTableRecord loadRecord(IDataKeyValue primaryKey) throws RecordNotFoundException;
	
	/**
   * Returns the selected record of this table. A selected record exists if this
   * data table is exactly populated by one record.
   * 
   * @return the selected record or <code>null</code> if no selected record
   *         exists.
   */
	public IDataTableRecord getSelectedRecord();
	
	/**
	 * Refreshes the selected record by means of reloading it from data source.
	 * 
	 * @return the reloaded record or <code>null</code> if no selected record
	 *         exists
	 * @throws RecordNotFoundException
	 *           if the record has already been deleted from data source
	 */
	public IDataTableRecord reloadSelectedRecord() throws RecordNotFoundException;
	
  /**
   * Brings the selected record into the update mode, by means of executing the
   * following steps:
   * <li> locking the record
   * <li> reloading the record from database
   * <li> propagating the current record by use of the given relationset.
   * <p>
   * Attention: These are exactly the steps executed when pressing standard
   * update action button.
   * 
   * @param relationSetName
   *          the name of the relationset to use for propagation
   * @return the reloaded and locked record or <code>null</code> if no
   *         selected record exists
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws RecordNotFoundException
   *           if the record has already been deleted from data source
   * @since 2.7.2
   */
  public IDataTableRecord updateSelectedRecord(String relationSetName) throws RecordLockedException, RecordNotFoundException;

  /**
   * Brings the selected record into the update mode, by means of executing the
   * following steps:
   * <li> locking the record
   * <li> reloading the record from database
   * <li> propagating the current record by use of the given relationset.
   * <p>
   * Attention: These are exactly the steps executed when pressing standard
   * update action button.
   * 
   * @param relationSet
   *          the relationset to use for propagation
   * @return the reloaded and locked record or <code>null</code> if no
   *         selected record exists
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws RecordNotFoundException
   *           if the record has already been deleted from data source
   * @since 2.7.2
   */
  public IDataTableRecord updateSelectedRecord(IRelationSet relationSet) throws RecordLockedException, RecordNotFoundException;
  
	/**
   * Creates a new record in the underlying data source.
   * <p>
   * After this method has been called the returned record becomes the selected
   * record of this table. Hence, previously populated records, which have been
   * retrieved by one of the search methods, will be cleared!
   * <p>
   * Call {@link IDataTransaction#commit()} to save this record to the data
   * source.
   * 
   * @param trans
   *          the transaction in which context this operation should be executed
   * @return the record created
   * @see #getSelectedRecord()
   */
  public IDataTableRecord newRecord(IDataTransaction trans);
	
	/**
   * Counts the records which match the current QBE settings of this table. If
   * no QBE constraints are existing, the method returns the number of records
   * of the underlying table alias. Table alias conditions are always
   * considered.
   * <p>
   * Attention: This method if much more efficient than first searching and then
   * counting the records with {@link IDataRecordSet#recordCount()}. And also
   * more accurate since the number of records could have been limited due to
   * max record setting.
   * 
   * @return the number of records matching the QBE constraints.
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   */
  public long count() throws InvalidExpressionException;
  
  /**
   * Same behaviour than {@link #count()} except that
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing <br>
   * are also considered.
   * 
   * @param relationSet
   *          the relation set
   * @return the number of records matching the QBE constraints.
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @since 2.6
   */
  public long count(IRelationSet relationSet) throws InvalidExpressionException;

  /**
   * Same behaviour than {@link #count()} except that
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing <br>
   * are also considered.
   * 
   * @param relationSetName
   *          the name of the relation set
   * @return the number of records matching the QBE constraints.
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @since 2.6
   */
  public long count(String relationSetName) throws InvalidExpressionException;

	/**
   * Checks whether at least one record exists which matches the current QBE
   * settings. If no QBE constraints are existing, the method checks whether the
   * underlying table alias has records at all. Table alias conditions are
   * always considered.
   * 
   * @return <code>true</code> one or more records are existing matching the
   *         current QBE settings, otherwise <code>false</code>.
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   */
	public boolean exists() throws InvalidExpressionException;
	
	/**
   * Checks whether at least one record exists which matches the given field
   * value expression. Unlike {@link #exists()} the current QBE settings are
   * <b>not </b> taken into account for this operation. Nevertheless, table
   * alias conditions are always considered.
   * 
   * @param tableFieldName
   *          the field name
   * @param value
   *          the field value expression
   * @return <code>true</code> one or more records are existing matching the
   *         given field value expression, otherwise <code>false</code>.
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @throws NoSuchFieldException
   *           if no such table field exists for this table
   */
	public boolean exists(String tableFieldName, Object value) throws InvalidExpressionException, NoSuchFieldException;
	
	/**
   * Checks whether at least one record exists which matches the given field
   * value expression. Unlike {@link #exists()} the current QBE settings are
   * <b>not </b> taken into account for this operation. Nevertheless, table
   * alias conditions are always considered.
   * 
   * @param tableField
   *          the table field
   * @param value
   *          the field value expression
   * @return <code>true</code> one or more records are existing matching the
   *         given field value expression, otherwise <code>false</code>.
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   */
	public boolean exists(ITableField tableField, Object value) throws InvalidExpressionException;
	
  /**
   * Populates the data table by means of performing a search operation,
   * which will be constrained by
   * <li>QBE constraints which have been set for this table
   * <li>the condition of the related table alias, if existing.
   * <p>
   * Note that the number of records retrieved from data source is limited to
   * the current maximum record setting.
   * 
   * @return the number of records retrieved
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @see IDataRecordSet#getMaxRecords()
   * @see IDataRecordSet#setMaxRecords(int)
   */
  public int search() throws InvalidExpressionException;
	
  /**
   * Populates the data table by means of performing a search operation,
   * which will be constrained by
   * <li>QBE constraints which have been set for this table,
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing.
   * 
   * <p>
   * Note that the number of records retrieved from data source is limited to
   * the current maximum record setting.
   * 
   * @param relationSetName
   *          the name of the relation set
   * @return the number of records retrieved
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @see IDataRecordSet#getMaxRecords()
   * @see IDataRecordSet#setMaxRecords(int)
   */
  public int search(String relationSetName) throws InvalidExpressionException;

  /**
   * Populates the data table by means of performing a search operation,
   * which will be constrained by
   * <li>QBE constraints which have been set for this table,
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
   * @return the number of records retrieved
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @see IDataRecordSet#getMaxRecords()
   * @see IDataRecordSet#setMaxRecords(int)
   */
  public int search(IRelationSet relationSet) throws InvalidExpressionException;

  /**
   * Performs a search operation, which will be constrained by <li>QBE
   * constraints which have been set for this table <li>the condition of the
   * related table alias, if existing.
   * <p>
   * For each record retrieved
   * {@link IDataTableSearchIterateCallback#onNextRecord(IDataTableRecord)} will
   * be called, but <b>no</b> population of this data table takes place, i.e.
   * {@link #recordCount()} will return <code>0</code> in any case. This method
   * could be used to process a large amount of records without running into a
   * {@link OutOfMemoryError}.
   * <p>
   * Note that the number of records retrieved from data source is <b>not<b>
   * limited to the current maximum record setting.
   * 
   * @param callback
   *          the callback to be invoked for each retrieved record
   * @return the number of records retrieved
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @since 2.10
   */
  public int searchAndIterate(IDataTableSearchIterateCallback callback) throws InvalidExpressionException;
  
  /**
   * Performs a search operation, which will be constrained by
   * <li>QBE constraints which have been set for this table,
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing.
   * <p>
   * For each record retrieved
   * {@link IDataTableSearchIterateCallback#onNextRecord(IDataTableRecord)} will
   * be called, but <b>no</b> population of this data table takes place, i.e.
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
  public int searchAndIterate(IDataTableSearchIterateCallback callback, String relationSetName) throws InvalidExpressionException;
  
  /**
   * Performs a search operation, which will be constrained by
   * <li>QBE constraints which have been set for this table,
   * <li>QBE constraints of related data tables determined by the given
   * relation set and
   * <li>the conditions of the related table aliases, if existing.
   * <p>
   * For each record retrieved
   * {@link IDataTableSearchIterateCallback#onNextRecord(IDataTableRecord)} will
   * be called, but <b>no</b> population of this data table takes place, i.e.
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
  public int searchAndIterate(IDataTableSearchIterateCallback callback, IRelationSet relationSet) throws InvalidExpressionException;
  
  /**
   * Deletes records of this table by means of retrieving them first.</br> To
   * determine the records to be deleted, appropriate QBE constraints have to be
   * set in advance! Table alias conditions are always considered.
   * <p>
   * In contrast to {@link #fastDelete(IDataTransaction)} records are deleted
   * after locking them first, i.e. the operation will fail if at least one
   * record has already been locked by means of another user. For each record
   * deleted the appropriate hooks will be called when performing this
   * operation. This means that this method has a significantly higher overhead
   * than {@link #fastDelete(IDataTransaction)}.
   * <p>
   * To perform this operation {@link IDataTransaction#commit()} has to be
   * called.
   * <p>
   * Note that the number of records deleted from data source is <b>not</b>
   * limited to the current maximum record setting.
   * 
   * @param trans
   *          the transaction in which context this operation should be executed
   * @return the number of records deleted
   * @throws InvalidExpressionException
   *           if any given QBE constraint contains an invalid expression
   * @throws RecordLockedException
   *           if at least one record is currently locked by another user.
   */
  public int searchAndDelete(IDataTransaction trans) throws InvalidExpressionException, RecordLockedException;
  
	/**
   * Deletes records of this table without retrieving them.</br> To determine
   * the records to be deleted, appropriate QBE constraints have to be set in
   * advance! Table alias conditions are always considered.
   * <p>
   * In contrast to {@link #searchAndDelete(IDataTransaction)} records are
   * deleted without trying to lock them first, i.e. locked records are deleted
   * as well if they match the search conditions. There will also no hooks be
   * called when performing this operation.
   * 
   * To perform this operation {@link IDataTransaction#commit()} has to be
   * called.
   * 
   * @param trans
   *          the transaction in which context this operation should be executed
   */
	public void fastDelete(IDataTransaction trans);
	
	public IDataTransaction startNewTransaction();
	
	public IDataTransaction getTableTransaction();
}
