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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

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
import de.tif.jacob.util.top.IProcessInformation;

/**
 * 
 */
public class Top extends SchedulerTaskSystem
{
  static public transient final String RCS_ID = "$Id: Top.java,v 1.3 2010/10/13 13:22:37 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";

  /**
   * Only record process info with total cpu usage above this value.
   */
  private static final BigDecimal PROCESS_INFO_THRESHOLD = new BigDecimal("0.10");
  
  /**
   * Attention class must be public otherwise application classloader throws an
   * exception.
   * 
   * @author Andreas Sonntag
   */
  public static class TopIterator extends SecondsIterator
  {
    public TopIterator()
    {
      super(Property.TOP_INTERVAL);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.scheduler.iterators.SecondsIterator#next()
     */
    public Date next()
    {
      // do not start scheduler task, if no implementation exists
      // for this operating system
      if (de.tif.jacob.util.top.Top.get() == null)
        return null;

      return super.next();
    }
  }

  /*
   * @see de.tif.jacob.scheduler.SchedulerTaskSystem#iterator()
   */
  public ScheduleIterator iterator()
  {
    return new TopIterator();
  }

  /*
   * (non-Javadoc)
   * 
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
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.scheduler.SchedulerTaskSystem#getSchedulerName()
   */
  public String getSchedulerName()
  {
    // ensure that top has its own scheduler, i.e. each own thread
    return "top";
  }

  /*
   * @see de.tif.jacob.scheduler.SchedulerTaskSystem#run(de.tif.jacob.scheduler.TaskContextSystem)
   */
  public void run(TaskContextSystem context) throws Exception
  {
    Iterator iter;
    try
    {
      iter = de.tif.jacob.util.top.Top.get().getProcessInformation().iterator();
    }
    catch (Exception exc)
    {
      // Es kann sein, das der Benutzer mit dem der jACOB läuft nicht
      // die Rechte hat 'top' unter UNIX auszuführen oder der Befehl ist
      // nicht installiert. (Default unter Solaris)
      // Es ist somit ein strukturelles Problem -> hibernate
      hibernate();

      // Exception durch lassen, damit die Exception dem Job zugeordnet werden
      // kann. Die Exception wird in der Admin Datenbank eingetragen.
      //
      throw exc;
    }
    
    IProcessInformation totalUsage = (IProcessInformation) iter.next();
    if (totalUsage.getUserTime().add(totalUsage.getKernelTime()).compareTo(Property.TOP_CPU_THRESHOLD.getDecimalValue()) > 0)
    {
      IDataAccessor accessor = context.getDataAccessor();
      IDataTransaction trans = accessor.newTransaction();
      try
      {
        IDataTable history = accessor.getTable("process_history");
        IDataTableRecord historyRecord = history.newRecord(trans);

        historyRecord.setValue(trans, "systemtime", "now");
        historyRecord.setValue(trans, "nodename", ClusterManager.getNodeName());
        historyRecord.setValue(trans, "kerneltime", totalUsage.getKernelTime());
        historyRecord.setValue(trans, "usertime", totalUsage.getUserTime());
        historyRecord.setValue(trans, "totaltime", totalUsage.getTotalTime());

        while (iter.hasNext())
        {
          IProcessInformation obj = (IProcessInformation) iter.next();
          if (obj.getTotalTime().compareTo(PROCESS_INFO_THRESHOLD) > 0)
          {
            IDataTable pi = accessor.getTable("process_info");
            IDataTableRecord piRecord = pi.newRecord(trans);
            piRecord.setValue(trans, "kerneltime", obj.getKernelTime());
            piRecord.setValue(trans, "usertime", obj.getUserTime());
            piRecord.setValue(trans, "totaltime", obj.getTotalTime());
            piRecord.setValue(trans, "owner", obj.getProcessUserName());
            piRecord.setIntValue(trans, "pid", obj.getPid());
            piRecord.setLinkedRecord(trans, historyRecord);
            piRecord.setStringValueWithTruncation(trans, "processname", obj.getProcessInformation());
          }
        }
        trans.commit();
      }
      finally
      {
        trans.close();
      }
    }
  }
}
