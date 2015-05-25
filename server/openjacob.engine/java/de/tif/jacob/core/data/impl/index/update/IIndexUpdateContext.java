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

package de.tif.jacob.core.data.impl.index.update;

import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.index.event.IndexEventHandler;
import de.tif.jacob.core.definition.ITableAlias;

/**
 * Interface for a context to update an index.
 * 
 * @see IndexEventHandler#newIndexUpdateContext(de.tif.jacob.core.Context,
 *      de.tif.jacob.core.data.impl.index.IndexDataSource)
 * @author Andreas Sonntag
 * @since 2.10
 */
public interface IIndexUpdateContext
{
  /**
   * Adds the content of the given table record to the index.
   * 
   * @param tableRecord
   *          the table record
   * @throws Exception
   *           in case of any problem
   */
  public void addToIndex(IDataTableRecord tableRecord) throws Exception;

  /**
   * Updates the content of the given table record within the index.
   * 
   * @param tableRecord
   *          the table record
   * @throws Exception
   *           in case of any problem
   */
  public void updateWithinIndex(IDataTableRecord tableRecord) throws Exception;

  /**
   * Removes all content of the given table record specified by table alias and
   * primary key value from the index.
   * 
   * @param alias
   *          the table alias
   * @param primaryKeyValue
   *          the primary key value of the table record which content should be
   *          removed from the index.
   * @throws Exception
   *           in case of any problem
   */
  public void removeFromIndex(ITableAlias alias, IDataKeyValue primaryKeyValue) throws Exception;

  /**
   * Flushes all previous changes persistently to the index.
   * 
   * @throws Exception
   *           in case of any problem flushing the changes to the index
   */
  public void flush() throws Exception;

  /**
   * Closes the context and frees all resources.
   */
  public void close();
}
