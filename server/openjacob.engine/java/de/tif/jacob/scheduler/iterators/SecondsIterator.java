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

import de.tif.jacob.core.Property;
import de.tif.jacob.scheduler.ScheduleIterator;

/**
 * A <code>SecondsIterator</code> returns a sequence of dates on subsequent days representing the
 * same time each day.
 */
public class SecondsIterator implements ScheduleIterator
{
  private final int seconds;
  private final Property secondsProperty;
  private final Calendar calendar = Calendar.getInstance();
  private Date startDate;

  public SecondsIterator(int seconds)
  {
    this.seconds = seconds;
    this.secondsProperty = null;
    calendar.setTimeInMillis(System.currentTimeMillis());
  }

  public SecondsIterator(Property secondsProperty)
  {
    this.seconds = 0;
    this.secondsProperty = secondsProperty;
    calendar.setTimeInMillis(System.currentTimeMillis());
  }

  public SecondsIterator(int seconds, Date startDate)
  {
    this.seconds = seconds;
    this.secondsProperty = null;
    this.startDate = startDate;
    calendar.setTime(startDate);
  }

  public SecondsIterator(Property secondsProperty, Date startDate)
  {
    this.seconds = 0;
    this.secondsProperty = secondsProperty;
    this.startDate = startDate;
    calendar.setTime(startDate);
  }

  public Date next()
  {
    Date result;
    
    // ensure first execution on startDate
    if (this.startDate != null)
    {
      result = this.startDate;
      this.startDate = null;
    }
    else
    {
      calendar.add(Calendar.SECOND, this.secondsProperty == null ? this.seconds : this.secondsProperty.getIntValue());
      result = calendar.getTime();
      
      // ensure that execution time is not in the past
      //
      long current = System.currentTimeMillis();
      if (result.getTime() < current)
      {
        result = new Date(current);
        calendar.setTime(result);
      }
    }
    
    return result;
  }
}
