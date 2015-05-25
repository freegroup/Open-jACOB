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
package de.tif.jacob.core.data.impl.qbe;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * QBE date range expression
 * 
 * @author Andreas Sonntag
 */
public class QBEDateRangeExpression extends QBERangeExpression
{
  static public transient final String RCS_ID = "$Id: QBEDateRangeExpression.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  /**
   * Year to date.
   */
  public static final int YTD = 0;
  
  /**
   * Quarter to date.
   */
  public static final int QTD = 1;
  
  /**
   * Month to date.
   */
  public static final int MTD = 2;
  
  /**
   * Week to date (starts always with monday!).
   */
  public static final int WTD = 3;
  
  /**
   * This year.
   */
  public static final int THISY = 4;
  
  /**
   * This quarter.
   */
  public static final int THISQ = 5;
  
  /**
   * This month.
   */
  public static final int THISM = 6;
  
  /**
   * This week (starts always with monday!).
   */
  public static final int THISW = 7;
  
  private static final String[] MODES =
  { "YTD", "QTD", "MTD", "WTD", "THISY", "THISQ", "THISM", "THISW" };
  
  private final QBETimeLag timeLag;
  private final int mode;

  /**
   * @param timeLag
   * @param mode
   */
  public QBEDateRangeExpression(QBETimeLag timeLag, int mode)
  {
    super(getLeftExpression(timeLag, mode), getRightExpression(timeLag, mode), false);
    this.timeLag = timeLag;
    this.mode = mode;
  }

  private static QBEExpression getLeftExpression(QBETimeLag timeLag, int mode)
  {
    // TODO: initialize Locale and TimeZone
    Calendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(System.currentTimeMillis());
    switch (mode)
    {
      case YTD:
      case THISY:
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        break;

      case QTD:
      case THISQ:
        int currentMonth = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, 3 * (currentMonth / 3));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        break;

      case MTD:
      case THISM:
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        break;

      case WTD:
      case THISW:
        // lets start the week with monday
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        break;
    }
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    if (null != timeLag)
      calendar = timeLag.addToCalendar(calendar);

    return new QBEDateLiteral(calendar);
  }

  private static QBEExpression getRightExpression(QBETimeLag timeLag, int mode)
  {
    switch (mode)
    {
      case YTD:
      case QTD:
      case MTD:
      case WTD:
        QBETodayLiteral today = new QBETodayLiteral(timeLag);
        today.increment();
        return today;
    }

    // TODO: initialize Locale and TimeZone
    Calendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.setLenient(true);
    switch (mode)
    {
      case THISY:
        // add one day
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        break;

      case THISQ:
        int currentMonth = calendar.get(Calendar.MONTH);
        // add one quarter
        calendar.set(Calendar.MONTH, 3 * (1 + currentMonth / 3));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        break;

      case THISM:
        // add one month
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        break;

      case THISW:
        // lets start the week with monday
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // enforce calculation
        calendar.getTimeInMillis();
        // add one week
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 7);
        break;
    }
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    if (null != timeLag)
      calendar = timeLag.addToCalendar(calendar);

    return new QBEDateLiteral(calendar);
  }

  public void print(PrintWriter writer)
  {
    writer.print("(");
    writer.print(MODES[this.mode]);
    if (this.timeLag != null)
      writer.print(this.timeLag);
    writer.print(")");
  }
}
