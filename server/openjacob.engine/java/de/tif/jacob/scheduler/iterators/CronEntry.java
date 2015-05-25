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

import java.util.StringTokenizer;

/**
 * Implementation of a cron entry. A cron entry is used to establish a cron
 * iterator (see {@link CronIterator}).
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
public class CronEntry
{
  static public final transient String RCS_ID = "$Id: CronEntry.java,v 1.4 2010/07/15 09:37:32 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  private final String cronEntry;

  protected final boolean[] minuteFlags = new boolean[60];
  protected final boolean[] hourFlags = new boolean[24];
  protected final boolean[] daysOfMonthFlags = new boolean[31];
  protected final boolean[] monthFlags = new boolean[12];
  protected final boolean[] daysOfWeekFlags = new boolean[7];

  /**
   * Constructor of the cron schedule iterator.
   * 
   * @param cronEntry
   *          the cron entry as string as described above
   * @throws IllegalArgumentException
   *           in case of an invalid cron entry
   */
  public CronEntry(String cronEntry) throws IllegalArgumentException
  {
    this.cronEntry = cronEntry;
    parseEntry(cronEntry);
  }
  

  private void parseEntry(String cronEntry) throws IllegalArgumentException
  {
    try
    {
      StringTokenizer tokenizer = new StringTokenizer(cronEntry);

      int numTokens = tokenizer.countTokens();
      for (int i = 0; tokenizer.hasMoreElements(); i++)
      {
        String token = tokenizer.nextToken();
        switch (i)
        {
          case 0: // Minutes
            parseToken(token, minuteFlags, false);
            break;
          case 1: // Hours
            parseToken(token, hourFlags, false);
            break;
          case 2: // Days of month
            parseToken(token, daysOfMonthFlags, true);
            break;
          case 3: // Months
            parseToken(token, monthFlags, true);
            break;
          case 4: // Days of week
            // special handling for sunday == 0 or 7!
            boolean[] bDaysOfWeekPlusOne = new boolean[8];
            parseToken(token, bDaysOfWeekPlusOne, false);
            if (bDaysOfWeekPlusOne[7])
              bDaysOfWeekPlusOne[0] = true;
            System.arraycopy(bDaysOfWeekPlusOne, 0, this.daysOfWeekFlags, 0, 7);
            break;
          default:
            break;
        }
      }

      // We expect exactly 5 tokens
      if (numTokens != 5)
      {
        throw new IllegalArgumentException("Cron entry must have 5 columns");
      }
    }
    catch (IllegalArgumentException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException("Invalid cron entry '" + cronEntry + "':" + e.toString());
    }
  }

  private static void parseToken(String token, boolean[] arrayBool, boolean bBeginInOne)
  {
    int i;
    int index;
    int each = 1;
    // Look for step first
    index = token.indexOf("/");
    if (index > 0)
    {
      each = Integer.parseInt(token.substring(index + 1));
      if (each == 0)
      {
        throw new RuntimeException("Invalid token '" + token + "'");
      }
      token = token.substring(0, index);
    }

    if (token.equals("*"))
    {
      for (i = 0; i < arrayBool.length; i += each)
      {
        arrayBool[i] = true;
      }
      return;
    }

    index = token.indexOf(",");
    if (index > 0)
    {
      StringTokenizer tokenizer = new StringTokenizer(token, ",");
      while (tokenizer.hasMoreElements())
      {
        parseToken(tokenizer.nextToken(), arrayBool, bBeginInOne);
      }
      return;
    }

    index = token.indexOf("-");
    if (index > 0)
    {
      int start = Integer.parseInt(token.substring(0, index));
      int end = Integer.parseInt(token.substring(index + 1));

      if (bBeginInOne)
      {
        start--;
        end--;
      }
      for (int j = start; j <= end; j += each)
        arrayBool[j] = true;
      return;
    }

    int indexVal = Integer.parseInt(token);
    if (bBeginInOne)
    {
      indexVal--;
    }
    arrayBool[indexVal] = true;
  }

  /**
   * Checks whether the given minute is included in this cron entry.
   * 
   * @param minute
   *          minute value which must be between 0 and 59
   * @return <code>true</code> if included, otherwise <code>false</code>
   * @see java.util.Calendar.MINUTE
   */
  public boolean includesMinute(int minute)
  {
    if (minute < 0 || minute > 59)
      throw new IllegalArgumentException("Minute must be between 0..59");
    return this.minuteFlags[minute];
  }

  /**
   * Checks whether any minute is included in this cron entry.
   * 
   * @return <code>true</code> if included, otherwise <code>false</code>
   */
  public boolean includesAnyMinute()
  {
    for (int i = 0; i < this.minuteFlags.length; i++)
      if (!this.minuteFlags[i])
        return false;
    return true;
  }

  /**
   * Checks whether the given hour of day is included in this cron entry.
   * 
   * @param hourOfDay
   *          hour of day value which must be between 0 and 23
   * @return <code>true</code> if included, otherwise <code>false</code>
   * @see java.util.Calendar.HOUR_OF_DAY
   */
  public boolean includesHour(int hourOfDay)
  {
    if (hourOfDay < 0 || hourOfDay > 23)
      throw new IllegalArgumentException("HourOfDay must be between 0..23");
    return this.hourFlags[hourOfDay];
  }

  /**
   * Checks whether any hour is included in this cron entry.
   * 
   * @return <code>true</code> if included, otherwise <code>false</code>
   */
  public boolean includesAnyHour()
  {
    for (int i = 0; i < this.hourFlags.length; i++)
      if (!this.hourFlags[i])
        return false;
    return true;
  }

  /**
   * Checks whether the given day of month is included in this cron entry.
   * 
   * @param dayOfMonth
   *          day of month value which must be between 1 and 31
   * @return <code>true</code> if included, otherwise <code>false</code>
   * @see java.util.Calendar.DAY_OF_MONTH
   */
  public boolean includesDayOfMonth(int dayOfMonth)
  {
    if (dayOfMonth < 1 || dayOfMonth > 31)
      throw new IllegalArgumentException("DayOfMonth must be between 1..31");
    return this.daysOfMonthFlags[dayOfMonth - 1];
  }

  /**
   * Checks whether any day of month is included in this cron entry.
   * 
   * @return <code>true</code> if included, otherwise <code>false</code>
   */
  public boolean includesAnyDayOfMonth()
  {
    for (int i = 0; i < this.daysOfMonthFlags.length; i++)
      if (!this.daysOfMonthFlags[i])
        return false;
    return true;
  }

  /**
   * Checks whether the given month is included in this cron entry.
   * 
   * @param month
   *          month value which must be between 0 and 11
   * @return <code>true</code> if included, otherwise <code>false</code>
   * @see java.util.Calendar.MONTH
   * @see java.util.Calendar.JANUARY
   * @see java.util.Calendar.DECEMBER
   */
  public boolean includesMonth(int month)
  {
    if (month < 0 || month > 11)
      throw new IllegalArgumentException("Month must be between 0..11");
    return this.monthFlags[month];
  }

  /**
   * Checks whether any month is included in this cron entry.
   * 
   * @return <code>true</code> if included, otherwise <code>false</code>
   */
  public boolean includesAnyMonth()
  {
    for (int i = 0; i < this.monthFlags.length; i++)
      if (!this.monthFlags[i])
        return false;
    return true;
  }

  /**
   * Checks whether the given day of week is included in this cron entry.
   * 
   * @param dayOfWeek
   *          day of week value which must be between 1 and 7
   * @return <code>true</code> if included, otherwise <code>false</code>
   * @see java.util.Calendar.DAY_OF_WEEK
   * @see java.util.Calendar.SUNDAY
   * @see java.util.Calendar.SATURDAY
   */
  public boolean includesDayOfWeek(int dayOfWeek)
  {
    if (dayOfWeek < 1 || dayOfWeek > 7)
      throw new IllegalArgumentException("DayOfWeek must be between 1..7");
    return this.daysOfWeekFlags[dayOfWeek - 1];
  }

  /**
   * Checks whether any day of week is included in this cron entry.
   * 
   * @return <code>true</code> if included, otherwise <code>false</code>
   */
  public boolean includesAnyDayOfWeek()
  {
    for (int i = 0; i < this.daysOfWeekFlags.length; i++)
      if (!this.daysOfWeekFlags[i])
        return false;
    return true;
  }
  
  public String toString()
  {
    return this.cronEntry;
  }
}
