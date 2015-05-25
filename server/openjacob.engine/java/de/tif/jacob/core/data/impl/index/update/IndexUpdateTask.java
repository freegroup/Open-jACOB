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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.index.IndexDataSource;
import de.tif.jacob.core.data.impl.index.event.IndexEventHandler;
import de.tif.jacob.core.definition.IAdhocBrowserDefinition;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.model.Index_queue;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * Task to run for an index data source to update it according to entries in
 * table "index_queue", i.e. index update pipe.
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public final class IndexUpdateTask extends IndexTask
{
  static public final transient String RCS_ID = "$Id: IndexUpdateTask.java,v 1.1 2010/07/13 17:55:23 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  static private final transient Log logger = LogFactory.getLog(IndexUpdateTask.class);

  private final String taskName;
  private final String taskId;

  /**
   * Constructor
   * 
   * @param datasourceIndexName
   */
  protected IndexUpdateTask(String datasourceIndexName)
  {
    super(datasourceIndexName);
    this.taskName = getTaskName(datasourceIndexName);
    this.taskId = getTaskId(datasourceIndexName);
  }

  protected static String getTaskName(String datasourceIndexName)
  {
    return TASK_NAME_PREFIX + "Update:" + datasourceIndexName;
  }

  protected static String getTaskId(String datasourceIndexName)
  {
    return getTaskName(datasourceIndexName);
  }

  public String getName()
  {
    return this.taskName;
  }

  public ScheduleIterator iterator()
  {
    return new MinutesIterator(1);
  }

  /**
   * The identifier which indicates this task. Is required for task cancel
   * action.
   * 
   */
  public String getKey()
  {
    return this.taskId;
  }

  private static final int PKEY_FIELD = 0;
  private static final int DATASOURCE_FIELD = 1;
  private static final int EVENT_FIELD = 2;
  private static final int TABLE_FIELD = 3;
  private static final int KEYVALUE_FIELD = 4;

  protected void run(IndexDataSource indexDataSource) throws Exception
  {
    IApplicationDefinition indexUpdateApplication = indexDataSource.getDefaultIndexUpdateApplication();
    if (indexUpdateApplication == null)
      return;

    IndexEventHandler handler = indexDataSource.getEventHandler(indexUpdateApplication);

    // Fetch change entries from index queue for the given index datasource
    // with oldest entries first (this is why we use a data browser!)
    //
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable table = accessor.getTable(Index_queue.NAME);

    IAdhocBrowserDefinition browserDefinition = accessor.getApplication().createAdhocBrowserDefinition(Index_queue.NAME);
    browserDefinition.addBrowserField(Index_queue.NAME, Index_queue.pkey, SortOrder.ASCENDING, Index_queue.pkey); // PKEY_FIELD
    browserDefinition.addBrowserField(Index_queue.NAME, Index_queue.datasource, SortOrder.NONE, Index_queue.datasource); // DATASOURCE_FIELD
    browserDefinition.addBrowserField(Index_queue.NAME, Index_queue.event, SortOrder.NONE, Index_queue.event); // EVENT_FIELD
    browserDefinition.addBrowserField(Index_queue.NAME, Index_queue.tablename, SortOrder.NONE, Index_queue.tablename); // TABLE_FIELD
    browserDefinition.addBrowserField(Index_queue.NAME, Index_queue.keyvalue, SortOrder.NONE, Index_queue.keyvalue); // KEYVALUE_FIELD

    IDataBrowser index_queueBrowser = accessor.createBrowser(browserDefinition);

    int n = 0;
    IIndexUpdateContext indexUpdateContext = null;
    try
    {
      do
      {
        // Get next bundle of index update queue entries
        //
        accessor.qbeClearAll();
        table.qbeSetKeyValue(Index_queue.indexname, indexDataSource.getName());
        index_queueBrowser.setMaxRecords(1000);
        index_queueBrowser.search(accessor.getApplication().getLocalRelationSet());
        if (index_queueBrowser.recordCount() > 0)
        {
          // process index update queue entries
          //
          Context schedulerContext = Context.getCurrent();
          try
          {
            // The task must run in the context of the index updating
            // application
            //
            TaskContextSystem context = new TaskContextSystem(indexUpdateApplication);
            Context.setCurrent(context);

            if (indexUpdateContext == null)
              indexUpdateContext = handler.newIndexUpdateContext(context, indexDataSource);

            for (int i = 0; i < index_queueBrowser.recordCount(); i++)
            {
              IDataBrowserRecord index_queueRecord = index_queueBrowser.getRecord(i);

              ITableDefinition tableDef = indexUpdateApplication.getTableDefinition( //
                index_queueRecord.getStringValue(DATASOURCE_FIELD), //
                index_queueRecord.getStringValue(TABLE_FIELD));

              IKey pkey = tableDef.getPrimaryKey();
              if (pkey == null)
              {
                logger.warn("Can not handle index update entry since table '" + tableDef + "' does not define a primary key");
                continue;
              }
              IDataKeyValue pkeyvalue = pkey.convertStringToKeyValue(index_queueRecord.getStringValue(KEYVALUE_FIELD));

              List aliases = tableDef.getTableAliases();
              for (int j = 0; j < aliases.size(); j++)
              {
                ITableAlias alias = (ITableAlias) aliases.get(j);
                if (handler.containsRecordsOfTableAlias(alias))
                {
                  String event = index_queueRecord.getStringValue(EVENT_FIELD);
                  if (event.equals(Index_queue.event_ENUM._del))
                  {
                    indexUpdateContext.removeFromIndex(alias, pkeyvalue);
                  }
                  else
                  {
                    // constrain by pkey and search on alias
                    // Note: alias condition might be involved, so that we do
                    // not find anything!
                    //
                    IDataTable dataTable = context.getDataAccessor().getTable(alias);
                    dataTable.qbeClear();
                    dataTable.qbeSetKeyValue(pkey, pkeyvalue);
                    dataTable.search();
                    IDataTableRecord record = dataTable.getSelectedRecord();

                    if (event.equals(Index_queue.event_ENUM._ins))
                    {
                      if (record != null)
                        indexUpdateContext.addToIndex(record);
                    }
                    else if (event.equals(Index_queue.event_ENUM._upd))
                    {
                      if (record != null)
                        indexUpdateContext.updateWithinIndex(record);
                      else
                        indexUpdateContext.removeFromIndex(alias, pkeyvalue);
                    }
                    else
                    {
                      throw new IllegalArgumentException("Unknown event=" + event);
                    }
                  }
                }
              }
            }
            
            // and make changes to index persistent
            indexUpdateContext.flush();
            n += index_queueBrowser.recordCount();
          }
          finally
          {
            // switch back to the original Context
            //
            Context.setCurrent(schedulerContext);
          }

          // and delete processed bundle of index update queue entries
          //
          IDataTransaction trans = accessor.newTransaction();
          try
          {
            IDataBrowserRecord lastRecord = index_queueBrowser.getRecord(index_queueBrowser.recordCount() - 1);
            table.qbeSetValue(Index_queue.pkey, "<=" + lastRecord.getintValue(PKEY_FIELD));
            table.fastDelete(trans);
            trans.commit();
          }
          finally
          {
            trans.close();
          }
        }
      }
      while (index_queueBrowser.hasMoreRecords());
    }
    finally
    {
      if (indexUpdateContext != null)
        indexUpdateContext.close();
      
      if (n > 0 && logger.isInfoEnabled())
        logger.info(n + " index update queue entries processed for index '" + indexDataSource + "'");
    }
  }
}
