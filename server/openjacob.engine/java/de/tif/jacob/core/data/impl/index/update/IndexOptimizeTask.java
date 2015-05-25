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

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.impl.index.IndexDataSource;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.CronIterator;

/**
 * Task to run for an index data source to optimize it.
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public final class IndexOptimizeTask extends IndexTask
{
  static public final transient String RCS_ID = "$Id: IndexOptimizeTask.java,v 1.1 2010/07/13 17:55:23 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private final String taskName;
  private final String taskId;

  /**
   * Constructor
   * 
   * @param datasourceIndexName
   */
  protected IndexOptimizeTask(String datasourceIndexName)
  {
    super(datasourceIndexName);
    this.taskName = getTaskName(datasourceIndexName);
    this.taskId = getTaskId(datasourceIndexName);
  }

  protected static String getTaskName(String datasourceIndexName)
  {
    return TASK_NAME_PREFIX + "Optimize:" + datasourceIndexName;
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
    try
    {
      // IBIS: This should be configurable at IndexDataSource
      // every night at 2 o'clock
      return new CronIterator("0 2 * * *");
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
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

  protected void run(IndexDataSource indexDataSource) throws Exception
  {
    IApplicationDefinition indexUpdateApplication = indexDataSource.getDefaultIndexUpdateApplication();
    if (indexUpdateApplication == null)
      return;

    Context schedulerContext = Context.getCurrent();
    try
    {
      // The task must run in the context of the index updating
      // application
      //
      TaskContextSystem context = new TaskContextSystem(indexUpdateApplication);
      Context.setCurrent(context);

      indexDataSource.optimize(context);
    }
    finally
    {
      // switch back to the original Context
      //
      Context.setCurrent(schedulerContext);
    }
  }
}
