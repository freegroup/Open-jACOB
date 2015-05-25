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
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class QBETimeLiteral extends QBELiteral
{
  static public transient final String        RCS_ID = "$Id: QBETimeLiteral.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";

	private final Calendar calendar;

  public QBETimeLiteral(Time time)
  {
    // IBIS: initialize Locale and TimeZone?
    this.calendar = new GregorianCalendar();
    this.calendar.setTime(time);
  }

  public QBETimeLiteral(int hours24, int mins)
  {
    this(hours24, mins, 0);
  }

	public QBETimeLiteral(int hours24, int mins, int secs)
	{
		this(hours24, mins, secs, 0);
	}

	public QBETimeLiteral(int hours24, int mins, int secs, int msecs)
	{
		this.calendar = new GregorianCalendar();
    // reset to 1.1.1970
    this.calendar.setTimeInMillis(0);
    this.calendar.setLenient(false);
		this.calendar.set(Calendar.HOUR_OF_DAY, hours24);
		this.calendar.set(Calendar.MINUTE, mins);
		this.calendar.set(Calendar.SECOND, secs);
		this.calendar.set(Calendar.MILLISECOND, msecs);
		
		// this would throw an exception if an invalid time is given, e.g. 25:31
		this.calendar.getTimeInMillis();
	}

	public QBETimeLiteral(int hours12, int mins, boolean isPmNotAm)
	{
		this(hours12, mins, 0, isPmNotAm);
	}

	public QBETimeLiteral(int hours12, int mins, int secs, boolean isPmNotAm)
	{
		this(hours12, mins, secs, 0, isPmNotAm);
	}

	public QBETimeLiteral(int hours12, int mins, int secs, int msecs, boolean isPmNotAm)
	{
	  // Hack: JRE (at least 1.4) library has a bug because
	  // 04/15/2005 12:00:00 AM would throw an exception, which is not ok
	  // The reason for this is, that hours12 is check for being between 0..11
	  // and not between 1..12!
	  if (hours12 == 12)
	    hours12 = 0;
	  
		this.calendar = new GregorianCalendar();
    this.calendar.setLenient(false);
		this.calendar.set(Calendar.HOUR, hours12);
		this.calendar.set(Calendar.AM_PM, isPmNotAm ? Calendar.PM : Calendar.AM);
		this.calendar.set(Calendar.MINUTE, mins);
		this.calendar.set(Calendar.SECOND, secs);
		this.calendar.set(Calendar.MILLISECOND, msecs);
		
		// this would throw an exception if an invalid time is given, e.g. 25:31
		this.calendar.getTimeInMillis();
	}

  public void makeConstraint(QBEConstraintBuilder builder, boolean doNot) throws InvalidExpressionException
  {
    builder.appendTimeLiteral(getCalendar(), doNot);
  }

  public void print(PrintWriter writer)
	{
		writer.print(this.calendar);
	}

	/**
	 * @return Returns the calendar.
	 */
	public Calendar getCalendar()
	{
		return calendar;
	}
}
