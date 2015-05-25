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

package de.tif.jacob.core.data.index;

import java.util.Set;

import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.index.IndexDataSource;
import de.tif.jacob.core.data.impl.index.event.IndexEventHandler;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * Index utility class.
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public final class IndexUtil
{
  static public transient final String RCS_ID = "$Id: IndexUtil.java,v 1.1 2010/07/15 19:24:20 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Determines the data table record, which is referenced by an index record.
   * 
   * @param indexRecord
   *          the index record, which results from a search on an index data
   *          source
   * @return the referenced record
   * @throws RecordNotFoundException
   *           in case the referenced record can not be retrieved anymore
   */
  public static IDataTableRecord getReferencedTableRecord(IDataRecord indexRecord) throws RecordNotFoundException
  {
    return getIndexEventHandler(indexRecord).getReferencedTableRecord(indexRecord);
  }

  /**
   * Determines the table fields of the referenced table record (see
   * {@link #getReferencedTableRecord(IDataRecord)}), which have been added to
   * an index datasource and are responsible that the given index record has
   * been retrieved.
   * 
   * @param indexRecord
   *          the index record, which results from a search on an index data
   *          source
   * @return <code>Set</code> of {@link ITableField}
   * @throws Exception
   *           on any problem in case the matching table fields can not be
   *           determined
   */
  public static Set getMatchingTableFields(IDataRecord indexRecord) throws Exception
  {
    return getIndexEventHandler(indexRecord).getMatchingTableFields(indexRecord);
  }

  private static IndexEventHandler getIndexEventHandler(IDataRecord indexRecord)
  {
    return IndexDataSource.getEventHandler(indexRecord.getAccessor().getApplication(), indexRecord.getTableAlias().getTableDefinition().getDataSourceName());
  }
}
