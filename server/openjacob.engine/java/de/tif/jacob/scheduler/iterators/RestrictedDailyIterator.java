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
package de.tif.jacob.scheduler.iterators;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import de.tif.jacob.scheduler.ScheduleIterator;

/**
 * A <code>RestrictedDailyIterator</code> returns a sequence of dates on
 * subsequent days (restricted to a set of days, e.g. weekdays only)
 * representing the same time each day.
 */
public class RestrictedDailyIterator implements ScheduleIterator 
{
	private final int[] days;
	private final Calendar calendar = Calendar.getInstance();

	public RestrictedDailyIterator(int hourOfDay, int minute, int second, int[] days) 
	{
		this(hourOfDay, minute, second, days, new Date());
	}
	
	public RestrictedDailyIterator(int hourOfDay, int minute, int second, int[] days, Date date) 
	{
		this.days = (int[]) days.clone();
		Arrays.sort(this.days);
		
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		if (!calendar.getTime().before(date)) 
		{
			calendar.add(Calendar.DATE, -1);
		}
	}

	public Date next() 
	{
		do 
		{
			calendar.add(Calendar.DATE, 1);
		} 
		while (Arrays.binarySearch(days, calendar.get(Calendar.DAY_OF_WEEK)) < 0);
		return calendar.getTime();
	}

}
