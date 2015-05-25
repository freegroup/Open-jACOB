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

import java.util.Calendar;
import java.util.Date;

import de.tif.jacob.scheduler.ScheduleIterator;

/**
 * A <code>WeeklyIterator</code> returns a sequence of dates on subsequent days
 * representing the same time each day of week.
 * 
 * @since 2.8.5
 */
public class WeeklyIterator implements ScheduleIterator
{
	private final Calendar calendar = Calendar.getInstance();

	public WeeklyIterator(int dayOfWeek, int hourOfDay, int minute, int second) 
	{
		this(dayOfWeek, hourOfDay, minute, second, new Date());
	}

	public WeeklyIterator(int dayOfWeek, int hourOfDay, int minute, int second, Date date) 
	{
		calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		if (!calendar.getTime().before(date)) 
		{
			calendar.add(Calendar.DATE, -7);
		}
	}

	public Date next() 
	{
		calendar.add(Calendar.DATE, 7);
		return calendar.getTime();
	}

}
