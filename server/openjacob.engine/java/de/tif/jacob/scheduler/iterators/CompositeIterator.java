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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.tif.jacob.scheduler.ScheduleIterator;

/**
 * A <code>CompositeIterator</code> combines a number of {@link ScheduleIterator}s
 * into a single {@link ScheduleIterator}. Duplicate dates are removed.
 */
public class CompositeIterator implements ScheduleIterator 
{
	private List orderedTimes     = new ArrayList();
	private List orderedIterators = new ArrayList();

	public CompositeIterator(ScheduleIterator[] scheduleIterators) 
	{
		for (int i = 0; i < scheduleIterators.length; i++) 
		{
			insert(scheduleIterators[i]);
		}
	}

	private void insert(ScheduleIterator scheduleIterator) 
	{
		Date time = scheduleIterator.next();
		if (time == null) 
		{
			return;
		}
		int index = Collections.binarySearch(orderedTimes, time);
		if (index < 0) 
		{
			index = -index - 1;
		}
		orderedTimes.add(index, time);
		orderedIterators.add(index, scheduleIterator);
	}

	public synchronized Date next() 
	{
		Date next = null;
		while (!orderedTimes.isEmpty() &&	(next == null || next.equals( orderedTimes.get(0)))) 
		{
			next = (Date) orderedTimes.remove(0);
			insert((ScheduleIterator) orderedIterators.remove(0));
		}
		return next;
	}


}
