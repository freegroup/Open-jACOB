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

package de.tif.jacob.core.data.impl.index.event;

import java.util.Set;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.index.IndexDataSource;
import de.tif.jacob.core.data.impl.index.update.IIndexUpdateContext;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * Abstract index event handler.
 * <p>
 * 
 * Instances of this class are used to update, (re)build and optimize a search
 * index, which will be used by {@link IndexDataSource} implementations to
 * perform queries.
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public abstract class IndexEventHandler
{
  static public transient final String RCS_ID = "$Id: IndexEventHandler.java,v 1.3 2010/07/15 19:24:20 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";

  /**
   * Checks whether the index contains records of a given table alias.
   * 
   * @param tableAlias the table alias
   * @return <code>true</code> if the index contains records of the given table alias, otherwise <code>false</code> 
   */
  public abstract boolean containsRecordsOfTableAlias(ITableAlias tableAlias);
  
  /**
   * Determines the data table record, which is referenced by an index record.
   * <p>
   * 
   * Note: The returned record is the record (at least the one with the same
   * primary key value), which has been originally added to the index by means
   * of {@link IIndexUpdateContext#addToIndex(IDataTableRecord)}.
   * 
   * @param indexRecord
   *          the index record, which results from a search on an index data
   *          source
   * @return the referenced record
   * @throws RecordNotFoundException
   *           in case the referenced record can not be retrieved anymore
   */
  public abstract IDataTableRecord getReferencedTableRecord(IDataRecord indexRecord) throws RecordNotFoundException;
  
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
  public abstract Set getMatchingTableFields(IDataRecord indexRecord) throws Exception;
  
  /**
   * Returns a new index update context which is needed especially when an index
   * is (incrementally) updated and (re)build.
   * 
   * @param context
   *          the applications current context
   * @param dataSource
   *          the index data source
   * @return a new index update context
   * @throws Exception
   *           on any severe problem
   * @throws UnsupportedOperationException
   *           if updating of an index is not supported in the corresponding
   *           application. This might be the case for two different
   *           applications sharing the same index, i.e. the one application just
   *           reading the index (reconfigure flag = none) should throw this
   *           exception.
   */
  public abstract IIndexUpdateContext newIndexUpdateContext(Context context, IndexDataSource dataSource) throws Exception, UnsupportedOperationException;
}
