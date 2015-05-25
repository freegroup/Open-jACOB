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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.index.IndexDataSource;
import de.tif.jacob.core.data.impl.index.event.IndexEventHandler;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.model.Index_queue;

/**
 * Asynchronous index update context implementation, which pipes updates to
 * <code>index_queue</code> table in jACOB repository.
 * 
 * @author Andreas Sonntag
 * @since 2.10
 */
public class AsynchronousIndexUpdateContext implements IIndexUpdateContext
{
  static public transient final String RCS_ID = "$Id: AsynchronousIndexUpdateContext.java,v 1.1 2010/07/13 17:55:23 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";

  private final static transient Log logger = LogFactory.getLog(AsynchronousIndexUpdateContext.class);

  private static final class PipeEntry
  {
    private final String event;
    private final String datasource;
    private final String table;
    private final String keyvalue;

    private PipeEntry(String event, ITableDefinition tableDef, String keyvalue)
    {
      this.event = event;
      this.datasource = tableDef.getDataSourceName();
      this.table = tableDef.getDBName();
      this.keyvalue = keyvalue;
    }
  }

  private final String datasourceIndexName;
  private final IndexEventHandler handler;
  private final List pipeEntries;

  public AsynchronousIndexUpdateContext(IApplicationDefinition applicationDef, String datasourceIndexName)
  {
    this.datasourceIndexName = datasourceIndexName;
    this.handler = IndexDataSource.getEventHandler(applicationDef, datasourceIndexName);
    this.pipeEntries = new ArrayList();
  }

  private void addToPipe(ITableDefinition tableDef, IDataKeyValue primaryKeyValue, String event)
  {
    List aliases = tableDef.getTableAliases();
    for (int i = 0; i < aliases.size(); i++)
    {
      if (this.handler.containsRecordsOfTableAlias((ITableAlias) aliases.get(i)))
      {
        IKey pkey = tableDef.getPrimaryKey();
        if (pkey == null)
        {
          logger.warn(tableDef.getDataSourceName() + "." + tableDef.getDBName() + " does not define a primary key");
        }
        else
        {
          this.pipeEntries.add(new PipeEntry(event, tableDef, pkey.convertKeyValueToString(primaryKeyValue)));
        }
        // and leave, because we add only once
        return;
      }
    }
  }

  public void addToIndex(IDataTableRecord tableRecord)
  {
    addToPipe(tableRecord.getTableAlias().getTableDefinition(), tableRecord.getPrimaryKeyValue(), Index_queue.event_ENUM._ins);
  }

  public void removeFromIndex(ITableAlias alias, IDataKeyValue primaryKeyValue)
  {
    addToPipe(alias.getTableDefinition(), primaryKeyValue, Index_queue.event_ENUM._del);
  }

  public void updateWithinIndex(IDataTableRecord tableRecord)
  {
    addToPipe(tableRecord.getTableAlias().getTableDefinition(), tableRecord.getPrimaryKeyValue(), Index_queue.event_ENUM._upd);
  }

  public void flush() throws Exception
  {
    if (this.pipeEntries.size() == 0)
      return;

    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable table = accessor.getTable(Index_queue.NAME);
    IDataTransaction trans = accessor.newTransaction();
    try
    {
      // to fetch multiple new ids with one call
      trans.setHintForNewRecordNumber(table.getTableAlias(), this.pipeEntries.size());

      for (int i = 0; i < this.pipeEntries.size(); i++)
      {
        PipeEntry pipeEntry = (PipeEntry) this.pipeEntries.get(i);

        IDataTableRecord record = table.newRecord(trans);
        record.setValue(trans, Index_queue.indexname, this.datasourceIndexName);
        record.setValue(trans, Index_queue.created, trans.getTimestamp());
        record.setValue(trans, Index_queue.datasource, pipeEntry.datasource);
        record.setValue(trans, Index_queue.tablename, pipeEntry.table);
        record.setValue(trans, Index_queue.keyvalue, pipeEntry.keyvalue);
        record.setValue(trans, Index_queue.event, pipeEntry.event);
      }
      trans.commit();
    }
    finally
    {
      trans.close();
    }

    if (logger.isDebugEnabled())
      logger.debug("Flushing " + this.pipeEntries.size() + " change entries for index '" + this.datasourceIndexName + "' to pipe");
  }

  public void close()
  {
    // nothing to do
  }
}
