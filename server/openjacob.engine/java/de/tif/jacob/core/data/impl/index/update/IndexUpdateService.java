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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.index.IndexDataSource;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UnavailableDatasourceException;
import de.tif.jacob.core.exception.UndefinedDatasourceException;
import de.tif.jacob.core.model.Datasource;
import de.tif.jacob.core.model.Enginetaskstatus;
import de.tif.jacob.scheduler.Scheduler;
import de.tif.jacob.scheduler.SchedulerTaskState;

/**
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public class IndexUpdateService extends BootstrapEntry
{
  static public final transient String RCS_ID = "$Id: IndexUpdateService.java,v 1.1 2010/07/13 17:55:23 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  static private final transient Log logger = LogFactory.getLog(IndexUpdateService.class);

  // Map{applicationId->Map{datasourceIndexName->IndexUpdateScheduler}}
  private static final Map indexSchedulers = new HashMap();
  
  private static class IndexUpdateScheduler extends Scheduler
  {
    protected IndexUpdateScheduler(String datasourceIndexName)
    {
      super("index:"+datasourceIndexName);
    }
  }

  /*
   * Fetch all index datasources and start index tasks.
   * 
   * @see de.tif.jacob.core.BootstrapEntry#init()
   */
  public void init() throws Throwable
  {
    try
    {
      // Prefetch persistent task states to accelerate performance
      //

      // String:taskname->SchedulerTaskState)
      Map persistentTaskStateMap = new HashMap();

      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      {
        IDataTable table = accessor.getTable(Enginetaskstatus.NAME);
        table.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
        table.qbeClear();
        table.qbeSetKeyValue(Enginetaskstatus.applicationname, AdminApplicationProvider.ADMIN_APPLICATION_NAME);
        table.qbeSetValue(Enginetaskstatus.taskname, IndexTask.TASK_NAME_PREFIX + "*");
        table.search();
        for (int i = 0; i < table.recordCount(); i++)
        {
          IDataTableRecord record = table.getRecord(i);
          persistentTaskStateMap.put(record.getStringValue(Enginetaskstatus.taskname), SchedulerTaskState.get(record.getStringValue(Enginetaskstatus.taskstatus)));
        }
      }

      // start scheduler tasks for all index datasources
      //
      IDataTable table = accessor.getTable(Datasource.NAME);
      table.setUnlimitedRecords();
      table.qbeClear();
      table.qbeSetKeyValue(Datasource.rdbtype, Datasource.rdbType_ENUM._Lucene);
      table.search();
      for (int i = 0; i < table.recordCount(); i++)
      {
        IDataTableRecord record = table.getRecord(i);

        String datasourceIndexName = record.getStringValue(Datasource.name);

        // determine initial states
        //
        SchedulerTaskState indexUpdateInitialState = (SchedulerTaskState) persistentTaskStateMap.get(IndexUpdateTask.getTaskName(datasourceIndexName));
        if (indexUpdateInitialState != null)
          indexUpdateInitialState = SchedulerTaskState.SCHEDULED;

        SchedulerTaskState indexOptimizeInitialState = (SchedulerTaskState) persistentTaskStateMap.get(IndexOptimizeTask.getTaskName(datasourceIndexName));
        if (indexOptimizeInitialState != null)
          indexOptimizeInitialState = SchedulerTaskState.SCHEDULED;

        // and start scheduler with tasks
        //
        startIndexScheduler(datasourceIndexName, indexUpdateInitialState, indexOptimizeInitialState);
      }
      if (logger.isInfoEnabled())
        logger.info(table.recordCount() + " index update tasks scheduled");
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(e);
    }
  }

  public void destroy() throws Throwable
  {
    // do nothing here
  }

  /**
   * (Re)Starts report scheduler for scheduled reports. If the report does not
   * exit anymore the scheduler task (if existing) will be deleted.
   * 
   * @param guid
   *          the id of the report
   * @throws Exception
   *           on any problem
   */
  public static void restartIndexScheduler(String datasourceIndexName) throws Exception
  {
    // plausibility check
    if (null == datasourceIndexName)
      throw new NullPointerException("datasourceIndexName is null");

    IndexUpdateScheduler scheduler;
    synchronized (indexSchedulers)
    {
      scheduler = (IndexUpdateScheduler) indexSchedulers.remove(datasourceIndexName);
    }
    if (scheduler != null)
    {
      if (scheduler.cancel(IndexUpdateTask.getTaskId(datasourceIndexName)))
      {
        if (logger.isInfoEnabled())
          logger.info("Index update task stopped for index " + datasourceIndexName);
      }
      if (scheduler.cancel(IndexOptimizeTask.getTaskId(datasourceIndexName)))
      {
        if (logger.isInfoEnabled())
          logger.info("Index optimize task stopped for index " + datasourceIndexName);
      }
      scheduler.cancel();
    }

    // index data source still exists?
    //
    try
    {
      DataSource dataSource = DataSource.get(datasourceIndexName);
      if (dataSource instanceof IndexDataSource)
      {
        startIndexScheduler(datasourceIndexName, null, null);
      }
    }
    catch (UnavailableDatasourceException ex)
    {
      // ignore
    }
    catch (UndefinedDatasourceException ex)
    {
      // ignore
    }
  }

  private static void startIndexScheduler(String datasourceIndexName, SchedulerTaskState indexUpdateInitialState, SchedulerTaskState indexOptimizeInitialState) throws Exception
  {
    IndexUpdateScheduler scheduler;
    synchronized (indexSchedulers)
    {
      scheduler = (IndexUpdateScheduler) indexSchedulers.get(datasourceIndexName);
      if (scheduler == null)
      {
        indexSchedulers.put(datasourceIndexName, scheduler = new IndexUpdateScheduler(datasourceIndexName));
      }
    }
    
    scheduler.schedule(new IndexUpdateTask(datasourceIndexName), indexUpdateInitialState);
    if (logger.isInfoEnabled())
      logger.info("Index update task started for index " + datasourceIndexName);
    
    scheduler.schedule(new IndexOptimizeTask(datasourceIndexName), indexOptimizeInitialState);
    if (logger.isInfoEnabled())
      logger.info("Index optimize task started for index " + datasourceIndexName);
  }
}
