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

import java.util.Calendar;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QBETimeLag
{
  static public transient final String        RCS_ID = "$Id: QBETimeLag.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";

	private final int years;
	private final int months;
	private final int days;
	private final int hours;
	private final int mins;
	private final int secs;

	public QBETimeLag(int years, int months, int days, int hours, int mins, int secs)
	{
		this.years = years;
		this.months = months;
		this.days = days;
		this.hours = hours;
		this.mins = mins;
		this.secs = secs;
	}

	public Calendar addToCalendar(Calendar calendar)
	{
		calendar.setLenient(true);
		calendar.set(
			calendar.get(Calendar.YEAR) + this.years,
			calendar.get(Calendar.MONTH) + this.months,
			calendar.get(Calendar.DAY_OF_MONTH) + this.days,
			calendar.get(Calendar.HOUR_OF_DAY) + this.hours,
			calendar.get(Calendar.MINUTE) + this.mins,
			calendar.get(Calendar.SECOND) + this.secs);
		return calendar;
	}
  
  
	/**
	 * toString methode: creates a String representation of the object
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("QBETimeLag[");
		buffer.append("years = ").append(years);
		buffer.append(", months = ").append(months);
		buffer.append(", days = ").append(days);
		buffer.append(", hours = ").append(hours);
		buffer.append(", mins = ").append(mins);
		buffer.append(", secs = ").append(secs);
		buffer.append("]");
		return buffer.toString();
	}
}
