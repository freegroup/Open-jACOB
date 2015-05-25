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

package de.tif.jacob.report;

import java.util.Calendar;

import de.tif.jacob.scheduler.iterators.CronEntry;
import de.tif.jacob.security.IUser;

/**
 *
 */
public class ReportNotifyee
{
  final String userId;
  final String userLoginId;
  final String address;
  final String protocol;
  final String mimeType;
  final int   minute;
  final int   hour;
  final int[] days;
  final CronEntry cronEntry;
  final boolean omitEmpty;
  
  /**
   * @param user
   * @param address
   * @param protocol
   * @param mimeType
   * @param minute
   * @param hour
   * @param days
   * @param omitEmpty
   * @deprecated use {@link #ReportNotifyee(String, String, String, String, String, CronEntry, boolean)}
   */
  public ReportNotifyee(IUser user, String address, String protocol, String mimeType, int minute, int hour, int[] days, boolean omitEmpty)
  {
    this(user.getKey(), user.getLoginId(), address, protocol, mimeType, minute, hour, days, omitEmpty);
  }
  
  /**
   * @param userId
   * @param userLoginId
   * @param address
   * @param protocol
   * @param mimeType
   * @param minute
   * @param hour
   * @param days
   * @param omitEmpty
   * @deprecated use {@link #ReportNotifyee(String, String, String, String, String, CronEntry, boolean)}
   */
  public ReportNotifyee(String userId, String userLoginId, String address, String protocol, String mimeType, int minute, int hour, int[] days, boolean omitEmpty)
  {
    this.userId = userId;
    this.userLoginId = userLoginId;
    this.address = address;
    this.protocol = protocol;
    this.mimeType = mimeType;
    this.minute = minute;
    this.hour = hour;
    this.days = days;
    this.omitEmpty = omitEmpty;
    this.cronEntry = buildCronEntry(minute, hour, days);
  }
  
  /**
   * @param user
   * @param address
   * @param protocol
   * @param mimeType
   * @param cronEntry
   * @param omitEmpty
   * @since 2.9.3
   */
  public ReportNotifyee(IUser user, String address, String protocol, String mimeType, CronEntry cronEntry, boolean omitEmpty)
  {
    this(user.getKey(), user.getLoginId(), address, protocol, mimeType, cronEntry, omitEmpty);
  }
  
  /**
   * @param userId
   * @param userLoginId
   * @param address
   * @param protocol
   * @param mimeType
   * @param cronEntry
   * @param omitEmpty
   * @since 2.9.3
   */
  public ReportNotifyee(String userId, String userLoginId, String address, String protocol, String mimeType, CronEntry cronEntry, boolean omitEmpty)
  {
    this.userId = userId;
    this.userLoginId = userLoginId;
    this.address = address;
    this.protocol = protocol;
    this.mimeType = mimeType;
    this.cronEntry = cronEntry;
    this.omitEmpty = omitEmpty;

    // try to convert cron entry to the less general old min/hour/days
    // specification
    //
    {
      boolean conversionPossible = true;
      int minute = -1, hour = -1;
      int[] days = new int[7];
      int daysNum = 0;
      {
        int i;
        for (i = 0; i < 60; i++)
        {
          if (cronEntry.includesMinute(i))
          {
            if (minute == -1)
              minute = i;
            else
              conversionPossible = false;
          }
        }
        for (i = 0; i < 24; i++)
        {
          if (cronEntry.includesHour(i))
          {
            if (hour == -1)
              hour = i;
            else
              conversionPossible = false;
          }
        }
        if (!cronEntry.includesAnyDayOfMonth())
          conversionPossible = false;
        if (!cronEntry.includesAnyMonth())
          conversionPossible = false;
        for (i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++)
        {
          if (cronEntry.includesDayOfWeek(i))
            days[daysNum++] = i;
        }
      }

      if (conversionPossible && daysNum > 0)
      {
        this.minute = minute;
        this.hour = hour;
        this.days = new int[daysNum];
        System.arraycopy(days, 0, this.days, 0, daysNum);
      }
      else
      {
        this.minute = -1;
        this.hour = -1;
        this.days = null;
      }
    }
  }
  
  private static CronEntry buildCronEntry(int minute, int hour, int[] days)
  {
    StringBuffer str = new StringBuffer();
    str.append(minute);
    str.append(" ");
    str.append(hour);
    str.append(" * * ");
    if (days.length == 0)
    {
      // no day restriction -> all days
      str.append("*");
    }
    else
    {
      for (int i = 0; i < days.length; i++)
      {
        if (i > 0)
          str.append(",");
        str.append(days[i]-1);
      }
    }
      
    try
    {
      return new CronEntry(str.toString());
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * @return
   * @since 2.9.3
   */
  public CronEntry getCronEntry()
  {
    return this.cronEntry;
  }
  
  /**
   * @return Returns the days or <code>null</code>, if notification is performed by a more general cron entry.
   * @deprecated use {@link #getCronEntry()}
   */
  public final int[] getDays()
  {
    return days;
  }
  
  /**
   * @return Returns the hour or <code>-1</code>, if notification is performed by a more general cron entry.
   * @deprecated use {@link #getCronEntry()}
   */
  public final int getHour()
  {
    return hour;
  }

  /**
   * @return Returns the minute or <code>-1</code>, if notification is performed by a more general cron entry.
   * @deprecated use {@link #getCronEntry()}
   */
  public final int getMinute()
  {
    return minute;
  }

  /**
   * @return Returns the recipient address.
   */
  public final String getAddress()
  {
    return address;
  }
  
  /**
   * @return Returns mimetype of the report format to deliver.
   */
  public final String getMimeType()
  {
    return mimeType;
  }
  
  /**
   * @return Returns the omitEmpty.
   */
  public final boolean isOmitEmpty()
  {
    return omitEmpty;
  }

  /**
   * @return Returns the user id.
   */
  public final String getUserId()
  {
    return userId;
  }
  
  /**
   * @return Returns the user login id.
   */
  public final String getUserLoginId()
  {
    return userLoginId;
  }
  
  /**
   * @return Returns the protocol for the jAN application. Like 'email://' or 'rightfax://'
   */
  public final String getProtocol()
  {
    return protocol;
  }
}
