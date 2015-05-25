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
import java.util.Calendar;
import java.util.Date;

import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.Scheduler;
import de.tif.jacob.scheduler.SchedulerTask;
import de.tif.jacob.scheduler.SchedulerTaskState;
import de.tif.jacob.scheduler.iterators.CompositeIterator;
import de.tif.jacob.scheduler.iterators.RestrictedDailyIterator;

public class HeatingController 
{

	private final Scheduler scheduler = new Scheduler();
	private final SimpleDateFormat dateFormat =	new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS");
	private final int weekdayHourOfDay, weekdayMinute, weekdaySecond;
	private final int weekendHourOfDay, weekendMinute, weekendSecond;

	public HeatingController(int weekdayHourOfDay, int weekdayMinute, int weekdaySecond,
	                         int weekendHourOfDay, int weekendMinute, int weekendSecond) 
	{
		this.weekdayHourOfDay = weekdayHourOfDay;
		this.weekdayMinute    = weekdayMinute;
		this.weekdaySecond    = weekdaySecond;
		this.weekendHourOfDay = weekendHourOfDay;
		this.weekendMinute    = weekendMinute;
		this.weekendSecond    = weekendSecond;
	}

	public void start() 
	{
		final int[] weekdays = new int[] { Calendar.MONDAY,  Calendar.TUESDAY,  Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY};
		final int[] weekend  = new int[] { Calendar.SATURDAY,Calendar.SUNDAY};
		SchedulerTask task =new SchedulerTask() {

      public ScheduleIterator iterator()
      {
    		ScheduleIterator i = new CompositeIterator(
					  new ScheduleIterator[] {
						new RestrictedDailyIterator(weekdayHourOfDay, weekdayMinute, weekdaySecond, weekdays),
						new RestrictedDailyIterator(weekendHourOfDay, weekendMinute, weekendSecond, weekend)
					  });
					return i;
      }
      public boolean runPerInstance()
      {
        return false;
      }
      
		  public void run() {
				switchHeatingOn();
			}
			private void switchHeatingOn() {
				System.out.println("Switch heating on at " +
					dateFormat.format(new Date()));
				// Start a new thread to switch the heating on...
			}
		};
		scheduler.schedule(task, SchedulerTaskState.SCHEDULED);
	}

	public static void main(String[] args) {
		HeatingController heatingController =
			new HeatingController(13, 4, 0, 9, 0, 0);
		heatingController.start();
	}
}