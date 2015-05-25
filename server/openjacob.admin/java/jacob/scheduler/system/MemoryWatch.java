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

package jacob.scheduler.system;

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.SecondsIterator;

/**
 * Task to collect memory info from JVM
 * 
 * @author Andreas Sonntag
 */
public class MemoryWatch extends SchedulerTaskSystem
{
  static public transient final String RCS_ID = "$Id: MemoryWatch.java,v 1.1 2007/01/19 07:44:34 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  /* 
   * @see de.tif.jacob.scheduler.SchedulerTaskSystem#iterator()
   */
  public ScheduleIterator iterator()
  {
    return new SecondsIterator(Property.MEMORY_WATCH_INTERVAL);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#runPerInstance()
   */
  public boolean runPerInstance()
  {
    return true;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#hibernatedOnSchedule()
   */
  public boolean hibernatedOnSchedule()
  {
    return true;
  }

  /* 
   * @see de.tif.jacob.scheduler.SchedulerTaskSystem#run(de.tif.jacob.scheduler.TaskContextSystem)
   */
  public void run(TaskContextSystem context) throws Exception
  {
    // collect memory info
    //
    Runtime rt = Runtime.getRuntime();
    Long freeBefore = null;
    Long totalBefore = null;
    Long duration = null;
    
    // call garbage collector?
    if ("on".equals(Property.MEMORY_WATCH_GC.getValue()))
    {
      freeBefore = new Long(rt.freeMemory() / 1024);
      totalBefore = new Long(rt.totalMemory() / 1024);

      // run garbage collector now
      long start = System.currentTimeMillis();
      rt.gc();
      duration = new Long(System.currentTimeMillis() - start);

    }
    
    long max = rt.maxMemory()/1024;
    long free = rt.freeMemory()/1024;
    long total = rt.totalMemory()/1024;
    float freePercentage = (100f *(free + max - total))/max;

    // write memory info if below threshold
    //
    if (freePercentage < Property.MEMORY_WATCH_THRESHOLD.getIntValue())
    {
      IDataAccessor accessor = context.getDataAccessor();
      IDataTransaction trans = accessor.newTransaction();
      try
      {
        IDataTable history = accessor.getTable("memory_history");
        IDataTableRecord historyRecord = history.newRecord(trans);

        historyRecord.setValue(trans, "systemtime", "now");
        historyRecord.setValue(trans, "nodename", ClusterManager.getNodeName());
        historyRecord.setValue(trans, "before_free_kb", freeBefore);
        historyRecord.setValue(trans, "before_total_kb", totalBefore);
        historyRecord.setValue(trans, "gc_duration", duration);
        historyRecord.setLongValue(trans, "max_kb", max);
        historyRecord.setLongValue(trans, "free_kb", free);
        historyRecord.setLongValue(trans, "total_kb", total);
        historyRecord.setValue(trans, "free_percentage", new Float(freePercentage));
        historyRecord.setLongValue(trans, "user_sessions", ClusterManager.getLocalUserSessionCount());

        trans.commit();
      }
      finally
      {
        trans.close();
      }
    }
  }
}
