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
import java.util.List;

import de.tif.jacob.core.data.IDataRecord;

/**
 * Implementation class to provide search result information from data source
 * implementation to the data layer.
 * 
 * @author Andreas Sonntag
 */
public final class DataSearchResult implements IDataSearchResult
{
  private boolean hasMore = false;
  private final List records;
  private int recordCount = 0;

  public DataSearchResult(boolean collectRecords)
  {
    this.records = collectRecords ? new ArrayList() : null;
  }

  public void add(IDataRecord record)
  {
    this.recordCount++;
    if (this.records != null)
      this.records.add(record);
  }

  /**
   * @param hasMore
   *          the hasMore to set
   */
  public void setHasMore(boolean hasMore)
  {
    this.hasMore = hasMore;
  }

  /**
   * @return Returns the hasMore.
   */
  public final boolean hasMore()
  {
    return hasMore;
  }

  public int getRecordCount()
  {
    return this.recordCount;
  }

  /**
   * @return Returns the records.
   */
  public List getRecords()
  {
    return records == null ? Collections.EMPTY_LIST : records;
  }
}
