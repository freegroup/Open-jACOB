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

import de.tif.jacob.core.definition.ITableAlias;

/**
 * A data record set defines a container for instances of {@link IDataRecord}
 * and also represents the joint behaviour of instances of {@link IDataBrowser}
 * and {@link IDataTable}.
 *
 * @author andreas
 */
public interface IDataRecordSet
{
  /**
   * This constant should be used to set the maximum number of records to
   * unlimited.
   * <p>
   * 
   * Note that setting the maximum record number to unlimited could overburden
   * the system in respect of memory and CPU aspects, if the population is not
   * limited by means of proper search constraints!
   * 
   * @see #setMaxRecords(int)
   */
  public static final int UNLIMITED_RECORDS = 0;

  /**
   * This constant should be used to set the maximum number of records to the
   * default setting.
   * <p>
   * 
   * Note that the actual number of records retrieved for this default setting
   * depends on the value of the jACOB property
   * <code>browser.system.max.records</code>. This property could be changed by
   * means of the jACOB administration application.
   * 
   * @see #setMaxRecords(int)
   * @since 2.9
   */
  public static final int DEFAULT_MAX_RECORDS = -1;

  /**
   * Returns the name of this data record set.
   * 
   * @return the data record set name
   */
  public String getName();
  
	/**
	 * Clears all records populated.
	 */
  public void clear();

  /**
   * Returns the parent data accessor, i.e. if {@link IDataAccessor#clear()}is
   * called, this data record set would be cleared as well.
   * 
   * @return the parent data accessor
   */
  public IDataAccessor getAccessor();
  
  /**
   * Returns the number of records, which have been populated by means of a search operation.
   * 
   * @return the record number
   */
  public int recordCount();
  
	/**
   * Checks whether the result set of the last search has been limited by the
   * maximal record setting, i.e. there are more records on the respective data
   * source.
   * 
   * @return <code>true<code> more records available, otherwise <code>false<code>.
   */
  public boolean hasMoreRecords();
	
  /**
   * Returns the current maximum number of records, i.e. when populating the
   * number of records will be limited to this number.
   * 
   * @return the current maximum record setting, which might be
   *         {@link IDataRecordSet#UNLIMITED_RECORDS} or
   *         {@link IDataRecordSet#DEFAULT_MAX_RECORDS} as well
   * @see #UNLIMITED_RECORDS
   * @see #DEFAULT_MAX_RECORDS         
   */
  public int getMaxRecords();

  /**
   * Sets the maximum number of records. This number restricts the result set of
   * search operations performed on this record set.
   * 
   * @param maxRecords
   *          the maximum record number
   * @return the original setting of this value before calling this method
   * @throws IllegalArgumentException
   *           if <code>maxRecords</code> is neither greater than 1 nor
   *           {@link IDataRecordSet#UNLIMITED_RECORDS} nor
   *           {@link IDataRecordSet#DEFAULT_MAX_RECORDS}
   */
  public int setMaxRecords(int maxRecords) throws IllegalArgumentException;
  
  /**
   * Sets the maximum number of records to {@link #UNLIMITED_RECORDS}.
   * <p>
   * 
   * Note that setting the maximum record number to unlimited could overburden
   * the system in respect of memory and CPU aspects, if the population is not
   * limited by means of proper search constraints!
   * 
   * @return the original setting of this value before calling this method
   * @since 2.7.4
   */
  public int setUnlimitedRecords();
  
  /**
   * Sets the maximum number of records to {@link #DEFAULT_MAX_RECORDS}.
   * 
   * @return the original setting of this value before calling this method
   * @since 2.9
   */
  public int setDefaultMaxRecords();
  
  /**
   * Returns the underlying table alias of this record set.
   * 
   * @return the underlying table alias
   */
  public ITableAlias getTableAlias();
}
