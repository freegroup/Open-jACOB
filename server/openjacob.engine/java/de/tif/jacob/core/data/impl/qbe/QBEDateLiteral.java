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
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QBEDateLiteral extends QBELiteral
{
  static public transient final String        RCS_ID = "$Id: QBEDateLiteral.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";

	private static final int YEAR_WRAP = 29;

  private Calendar calendar;
  
  private boolean increment = false;
  
  
  protected QBEDateLiteral()
  {
  }
  
  protected QBEDateLiteral(Calendar calendar)
  {
    this.calendar = calendar;
  }
  
  public QBEDateLiteral(Date date)
  {
    // IBIS: initialize Locale and TimeZone?
    this.calendar = new GregorianCalendar();
    this.calendar.setTime(date);
  }
  
	/**
	 * @param str
	 *          string in format DDMMYY, DDMMYYYY
	 */
	public QBEDateLiteral(String str)
	{
		int day = 10 * (str.charAt(0) - '0') + (str.charAt(1) - '0');
		int month = 10 * (str.charAt(2) - '0') + (str.charAt(3) - '0');
		int year = 10 * (str.charAt(4) - '0') + (str.charAt(5) - '0');
		if (str.length() == 8)
		{
			year *= 100;
			year += 10 * (str.charAt(6) - '0') + (str.charAt(7) - '0');
		}
		init(day, month, year, true);
	}

	public QBEDateLiteral(int day, int month, int year, boolean shortYear)
	{
		init(day, month, year, shortYear);
	}
	
	/**
	 * Adds one day to this date literal.<p>
	 * 
	 * Note: This is needed for date ranges!
	 */
	public void increment()
	{
	  this.increment = true;
	}

  private void init(int day, int month, int year, boolean shortYear)
  {
		if (shortYear && year < 100)
			year += year > YEAR_WRAP ? 1900 : 2000;
		
		// TODO: initialize Locale and TimeZone
		this.calendar = new GregorianCalendar();
    this.calendar.setLenient(false);
		this.calendar.set(year, month - 1, day, 0, 0, 0);
    this.calendar.set(Calendar.MILLISECOND, 0);
    
    // this would throw an exception if an invalid date is given, e.g. 30.2.04
    this.calendar.getTimeInMillis();
  }

  public final void makeConstraint(QBEConstraintBuilder builder, boolean doNot) throws InvalidExpressionException
  {
    builder.appendDateLiteral(getCalendar(), doNot);
  }

	public void print(PrintWriter writer)
	{
    writer.print(getCalendar());
  }
  
	/**
	 * @return Returns the calendar.
	 */
	public Calendar getCalendar()
	{
	  if (this.increment)
	  {
	    // add one day
	    calendar.setLenient(true);
	    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
	    
	    this.increment = false;
	  }
		return calendar;
	}

  /**
   * @return Returns the increment.
   */
  protected boolean isIncrement()
  {
    return increment;
  }
}
