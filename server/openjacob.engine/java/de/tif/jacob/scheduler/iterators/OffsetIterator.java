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
 * An <code>OffsetIterator</code> modifies the dates returned by a
 * {@link ScheduleIterator} by a constant calendar offset.
 */
public class OffsetIterator implements ScheduleIterator 
{

	private final ScheduleIterator scheduleIterator;
	private final int field;
	private final int offset;
	private final Calendar calendar = Calendar.getInstance();

	public OffsetIterator(ScheduleIterator scheduleIterator, int field, int offset) 
	{
		this.scheduleIterator = scheduleIterator;
		this.field = field;
		this.offset = offset;
	}

	public Date next() 
	{
		Date date = scheduleIterator.next();
		if (date == null) 
		{
			return null;
		}
		calendar.setTime(date);
		calendar.add(field, offset);
		return calendar.getTime();
	}


}
