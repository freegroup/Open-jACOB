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
import java.util.Locale;

import de.tif.jacob.core.Context;

/**
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 * 
 * @author Andreas Sonntag
 */
public class QBEWeekRangeExpression extends QBERangeExpression
{
  static public transient final String RCS_ID = "$Id: QBEWeekRangeExpression.java,v 1.2 2011/02/17 13:34:09 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";

  private final QBETimeLag timeLag;
  private final int startWeekNum;
  private final int endWeekNum;

  /**
   * @param weekNum
   * @param timeLag
   */
  public QBEWeekRangeExpression(int weekNum, QBETimeLag timeLag)
  {
    super(getExpression(timeLag, weekNum, 0, false), getExpression(timeLag, weekNum, 0, true), false);
    this.timeLag = timeLag;
    this.startWeekNum = weekNum;
    this.endWeekNum = weekNum;
  }

  public QBEWeekRangeExpression(int weekNum, int yearOffset)
  {
    super(getExpression(null, weekNum, yearOffset, false), getExpression(null, weekNum, yearOffset, true), false);
    this.timeLag = null;
    this.startWeekNum = weekNum;
    this.endWeekNum = weekNum;
  }

  public QBEWeekRangeExpression(int startWeekNum, int endWeekNum, int yearOffset)
  {
    super(getExpression(null, startWeekNum, yearOffset, false), getExpression(null, endWeekNum, yearOffset, true), false);
    this.timeLag = null;
    this.startWeekNum = startWeekNum;
    this.endWeekNum = endWeekNum;
  }

  private static QBEExpression getExpression(QBETimeLag timeLag, int weekNum, int yearOffset, boolean addWeek)
  {
    // get application locale for calendar week calculation
    Locale locale = Context.getCurrent().getApplicationLocale();
    Calendar calendar = new GregorianCalendar(locale);
    calendar.setTimeInMillis(System.currentTimeMillis());
    if (yearOffset != 0)
    {
      // handle year offset
      calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + yearOffset);
      // enforce calculation
      long temp = calendar.getTimeInMillis();
      calendar = new GregorianCalendar(locale);
      calendar.setTimeInMillis(temp);
    }
    // set calendar week
    calendar.set(Calendar.WEEK_OF_YEAR, weekNum);
    // enforce calculation
    calendar.getTimeInMillis();
    // add one week
    // lets start the week with monday
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    // enforce calculation
    calendar.getTimeInMillis();
    if (addWeek)
    {
      // add one week
      calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 7);
    }
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    if (null != timeLag)
      calendar = timeLag.addToCalendar(calendar);

    return new QBEDateLiteral(calendar);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.impl.qbe.QBEExpression#print(java.io.PrintWriter)
   */
  public void print(PrintWriter writer)
  {
    writer.print("(WEEK");
    writer.print(this.startWeekNum);
    if (this.startWeekNum != this.endWeekNum)
    {
      writer.print("..WEEK");
      writer.print(this.endWeekNum);
    }
    if (this.timeLag != null)
      writer.print(this.timeLag);
    writer.print(")");
  }
}
