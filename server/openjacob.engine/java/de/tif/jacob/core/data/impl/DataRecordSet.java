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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.definition.ITableField;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class DataRecordSet implements IDataRecordSet
{
  static public transient final String        RCS_ID = "$Id: DataRecordSet.java,v 1.9 2010/07/13 17:55:23 ibissw Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.9 $";
  
  static private final transient Log logger = LogFactory.getLog(DataRecordSet.class);

  /**
   * List <DataRecord>
   */
  private List records;
  private boolean hasMoreRecords;
  
  protected long changeCounter;
  protected final DataAccessor parent;
  private int maxRecords;
  
	protected DataRecordSet(DataAccessor parent)
	{
    this.records = null;
    this.parent = parent;
    this.maxRecords = DEFAULT_MAX_RECORDS;
		this.changeCounter = 0;
	}
  
  public final IDataAccessor getAccessor()
  {
   return this.parent; 
  }

  protected final DataAccessor getAccessorInternal()
  {
   return this.parent; 
  }

  public final int getMaxRecords()
  {
    return this.maxRecords;
  }
  
  protected final void sort(Comparator comp)
  {
    Collections.sort(this.records, comp);
  }

	public final int setMaxRecords(int maxRecords) throws IllegalArgumentException
	{
		if (maxRecords <= 0 && maxRecords != UNLIMITED_RECORDS && maxRecords != DEFAULT_MAX_RECORDS)
			throw new IllegalArgumentException("" + maxRecords);

		int old = this.maxRecords;
		this.maxRecords = maxRecords;
		return old;
	}

  public final int setUnlimitedRecords()
  {
    int old = this.maxRecords;
    this.maxRecords = UNLIMITED_RECORDS;
    return old;
  }

  public int setDefaultMaxRecords()
  {
    int old = this.maxRecords;
    this.maxRecords = DEFAULT_MAX_RECORDS;
    return old;
  }

  protected abstract DataRecord instantiateRecord(DataSource dataSource, DataTableRecordEventHandler eventHandler, int[] primaryKeyIndices, Object[] values, int seqNbr);
  
  protected final void clearRecordsInternal()
  {
    this.records = null;
    this.hasMoreRecords = false;
  }

  protected void addRecords(List records, boolean hasMore)
  {
    if (logger.isDebugEnabled())
      logger.debug("addRecords(): set=" + this +", records=" + records.size());
    
    this.hasMoreRecords = hasMore;

    if (null == this.records)
    {
      this.records = records;
    }
    else
    {
      this.records.addAll(records);
    }
  }

  /**
   * For internal use only
   * 
   * @param indexOfInsert
   * @param record
   * @return
   */
  public int addRecord(int indexOfInsert, DataRecord record)
  {
    if (null == record)
      throw new NullPointerException();

    if (null == this.records)
    {
      this.records = new ArrayList();
    }
    this.records.add(indexOfInsert, record);
    return indexOfInsert;
  }
  
  protected void removeRecord(int index)
  {
    this.records.remove(index);
  }
  
  protected boolean removeRecord(DataRecord record)
  {
    if (this.records != null)
    	return this.records.remove(record);
    return false;
  }
  
  protected DataRecord getRecordInternal(int index) throws IndexOutOfBoundsException
  {
    checkRecordIndex(index);
    return (DataRecord) this.records.get(index);
  }

  /**
   * @param record
   * @return the index of the given record or <code>-1</code> if this data
   *         record set does not contain this record instance.
   */
  protected int getRecordIndexInternal(IDataRecord record)
  {
    return this.records.indexOf(record);
  }

  protected void setRecord(int index, DataRecord record)
  {
    this.records.set(index, record);
  }

  protected boolean isRecordIndex(int index)
  {
    return index >= 0 && index < recordCount();
  }

  protected void checkRecordIndex(int index)
  {
    if (!isRecordIndex(index))
    {
      throw new IndexOutOfBoundsException("Index [" + index+"] is not in range of [0;"+recordCount()+"[");
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecordSet#recordCount()
   */
  public int recordCount()
  {
    return this.records == null ? 0 : this.records.size();
  }
  
  protected List cloneRecordList()
  {
    if (this.records == null)
      return new ArrayList(0);
    return new ArrayList(this.records);
  }
  
  public long getChangeCount()
	{
		if (logger.isTraceEnabled())
			logger.trace("Change count " + this.changeCounter + " returned for " + this);
		return this.changeCounter;
	}

	protected final void incrementChangeCounter() 
	{
		this.changeCounter = this.parent.getNextChangeCounter();
    
		if (logger.isTraceEnabled())
			logger.trace("New change count " + this.changeCounter + " for " + this);
  }

	protected abstract int getFieldIndexByFieldName(String tableFieldName) throws NoSuchFieldException;

	protected abstract ITableField getFieldDefinition(int fieldIndex);

	protected abstract int getRecordSize();

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecordSet#hasMoreRecords()
	 */
	public final boolean hasMoreRecords()
	{
		return hasMoreRecords;
	}

}
