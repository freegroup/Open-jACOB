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
 * Implementation of a schedule iterator which is based on cron entry. By means
 * of this iterator tasks came be scheduled easily on minute, hour, day, month
 * and week day base and all combinations of those.
 * <p>
 * Examples for cron entries:<b>
 * <li>"* * * * *" (every minute)
 * <li>"0 0 1 1 *" (yearly every 1. January at 00:00)
 * <li>"0 6,15 * * 1-5" (on 6:00 and 15:00 o'clock on weekdays)
 * 
 * For more detailed information see <a>http://en.wikipedia.org/wiki/Cron</a>,
 * but consider that the sixth column, i.e. the command to execute, has to be
 * omitted.
 * 
 * @since 2.9.3
 * @author Andreas Sonntag
 */
public class CronIterator implements ScheduleIterator
{
  static public final transient String RCS_ID = "$Id: CronIterator.java,v 1.4 2010/03/21 13:38:55 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  private final CronEntry cronEntry;

  private final Calendar calendar;

  /**
   * Constructor of the cron schedule iterator.
   * 
   * @param cronEntry
   *          the cron entry as described above
   * @throws Exception
   *           in case of an invalid cron entry
   */
  public CronIterator(String cronEntry) throws Exception
  {
    this(new CronEntry(cronEntry), Calendar.getInstance());
  }

  /**
   * Constructor of the cron schedule iterator.
   * 
   * @param cronEntry
   *          the cron entry as described above
   */
  public CronIterator(CronEntry cronEntry)
  {
    this(cronEntry, Calendar.getInstance());
  }

  private CronIterator(CronEntry cronEntry, Calendar calendar)
  {
    this.cronEntry = cronEntry;
    this.calendar = calendar;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.scheduler.ScheduleIterator#next()
   */
  public Date next()
  {
    return calculateNext();
  }

  /**
   * Calculates the next date which is after (!) the given calendar object.
   * 
   * @return
   */
  private Date calculateNext()
  {
    this.calendar.set(Calendar.MILLISECOND, 0);
    this.calendar.set(Calendar.SECOND, 0);
    this.calendar.add(Calendar.MINUTE, 1);

    {
      int minute = getMatchingIndex(this.cronEntry.minuteFlags, this.calendar.get(Calendar.MINUTE));
      if (minute == -1)
      {
        this.calendar.set(Calendar.MINUTE, getMatchingIndex(this.cronEntry.minuteFlags, 0));
        this.calendar.add(Calendar.HOUR_OF_DAY, 1);
      }
      else
      {
        this.calendar.set(Calendar.MINUTE, minute);
      }
    }

    {
      int hour = getMatchingIndex(this.cronEntry.hourFlags, this.calendar.get(Calendar.HOUR_OF_DAY));
      if (hour == -1)
      {
        this.calendar.set(Calendar.MINUTE, getMatchingIndex(this.cronEntry.minuteFlags, 0));
        this.calendar.set(Calendar.HOUR_OF_DAY, getMatchingIndex(this.cronEntry.hourFlags, 0));
        this.calendar.add(Calendar.DAY_OF_MONTH, 1);
      }
      else
      {
        this.calendar.set(Calendar.HOUR_OF_DAY, hour);
      }
    }

    boolean validDate;
    do
    {
      int dayOfMonth = getMatchingIndex(this.cronEntry.daysOfMonthFlags, this.calendar.get(Calendar.DAY_OF_MONTH) - 1);
      if (dayOfMonth == -1)
      {
        this.calendar.set(Calendar.MINUTE, getMatchingIndex(this.cronEntry.minuteFlags, 0));
        this.calendar.set(Calendar.HOUR_OF_DAY, getMatchingIndex(this.cronEntry.hourFlags, 0));

        dayOfMonth = getMatchingIndex(this.cronEntry.daysOfMonthFlags, 0);
        this.calendar.add(Calendar.MONTH, 1);
      }
      this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth + 1);

      int month = getMatchingIndex(this.cronEntry.monthFlags, this.calendar.get(Calendar.MONTH));
      if (month == -1)
      {
        this.calendar.set(Calendar.MINUTE, getMatchingIndex(this.cronEntry.minuteFlags, 0));
        this.calendar.set(Calendar.HOUR_OF_DAY, getMatchingIndex(this.cronEntry.hourFlags, 0));
        this.calendar.set(Calendar.DAY_OF_MONTH, (dayOfMonth = getMatchingIndex(this.cronEntry.daysOfMonthFlags, 0)) + 1);

        month = getMatchingIndex(this.cronEntry.monthFlags, 0);
        this.calendar.add(Calendar.YEAR, 1);
      }
      this.calendar.set(Calendar.MONTH, month);

      validDate = dayOfMonth == this.calendar.get(Calendar.DAY_OF_MONTH) - 1;

      if (validDate && !this.cronEntry.daysOfWeekFlags[this.calendar.get(Calendar.DAY_OF_WEEK) - 1])
      {
        validDate = false;
        this.calendar.add(Calendar.DAY_OF_YEAR, 1);
      }
    }
    while (validDate == false);

    return calendar.getTime();
  }

  private static int getMatchingIndex(boolean[] flags, int startIndex)
  {
    for (int i = startIndex; i < flags.length; i++)
    {
      if (flags[i])
        return i;
    }
    return -1;
  }

  private static final String[] TEST_CRON_ENTRIES = { //
      "5,10 * * * *", //
      "*/10 * * * *", //
      "*/10 7 * * *", //
      "* * 14,29 * *", //
      "0 * 14,29 * *", //
      "0 1,11,21 14,29 * *", //
      "0 7 14,29 * *", //
      "0 7 29,30,31 * *", //
      "0 7 29,30,31 * 7", //
      "0 7 29 2 7", //
      "0 7 4,14,24 * *", //
      "0 9 5,25 1 *", //
  };

  public static void main(String[] args)
  {
    Calendar cl = Calendar.getInstance();
    cl.set(Calendar.MINUTE, 41);
    cl.set(Calendar.HOUR_OF_DAY, 8);
    cl.set(Calendar.DAY_OF_MONTH, 10);
    cl.set(Calendar.MONTH, Calendar.FEBRUARY);
    cl.set(Calendar.YEAR, 2010);
    Date startDate = cl.getTime();
    System.out.println("Startdate = " + startDate);
    System.out.println();

    for (int i = 0; i < TEST_CRON_ENTRIES.length; i++)
    {
      String cronEntry = TEST_CRON_ENTRIES[i];
      System.out.println("\"" + cronEntry + "\":");
      try
      {
        cl.setTimeInMillis(startDate.getTime());

        CronIterator iter = new CronIterator(new CronEntry(cronEntry), cl);
        for (int j = 0; j < 7; j++)
        {
          System.out.println("\t" + iter.next());
        }
      }
      catch (Exception ex)
      {
        System.out.println("\t" + ex.toString());
      }
      System.out.println();
    }
  }
}
