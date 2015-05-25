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
package de.tif.jacob.scheduler.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.Scheduler;
import de.tif.jacob.scheduler.SchedulerTask;
import de.tif.jacob.scheduler.SchedulerTaskState;
import de.tif.jacob.scheduler.iterators.SecondsIterator;

public class RecurringTimer
{
  private final Scheduler        scheduler  = new Scheduler();
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS");

  private final int seconds;

  public RecurringTimer(int seconds)
  {
    this.seconds = seconds;
  }

  public void start()
  {
    SchedulerTask task =new SchedulerTask()
    {
      public ScheduleIterator iterator()
      {
        return new SecondsIterator(5);
      }
      public boolean runPerInstance()
      {
        return false;
      }
      
      public void run()
      {
        System.out.println( "Event um: " + dateFormat.format(new Date()));
      }
    };
    scheduler.schedule(task, SchedulerTaskState.SCHEDULED);
  }

  public static void main(String[] args)
  {
    RecurringTimer timer = new RecurringTimer(5);
    timer.start();
  }
}