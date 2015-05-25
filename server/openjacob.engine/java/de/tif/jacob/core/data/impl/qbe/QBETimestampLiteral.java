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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QBETimestampLiteral extends QBELiteral
{
	static public transient final String RCS_ID = "$Id: QBETimestampLiteral.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
	static public transient final String RCS_REV = "$Revision: 1.1 $";

	private final QBEDateLiteral dateLiteral;
	private final QBETimeLiteral timeLiteral;
	private final Calendar calendar;

	public QBETimestampLiteral(QBEDateLiteral dateLiteral)
	{
		this.dateLiteral = dateLiteral;
		this.timeLiteral = null;
		this.calendar = null;
	}

	public QBETimestampLiteral(QBEDateLiteral dateLiteral, QBETimeLiteral timeLiteral)
	{
		this.dateLiteral = dateLiteral;
		this.timeLiteral = timeLiteral;
		this.calendar = null;
	}

  public QBETimestampLiteral(Timestamp timestamp)
  {
    this.dateLiteral = null;
    this.timeLiteral = null;
    
    // IBIS: initialize Locale and TimeZone?
    this.calendar = new GregorianCalendar();
    this.calendar.setTime(timestamp);
  }

  public QBETimestampLiteral(Calendar calendar)
  {
    this.dateLiteral = null;
    this.timeLiteral = null;
    this.calendar = calendar;
  }

	protected QBETimestampLiteral()
	{
		this.dateLiteral = null;
		this.timeLiteral = null;
		this.calendar = null;
	}

	public void print(PrintWriter writer)
	{
		writer.print(getCalendar());
	}

  public final void makeConstraint(QBEConstraintBuilder builder, boolean doNot) throws InvalidExpressionException
  {
    builder.appendTimestampLiteral(getCalendar(), doNot);
  }

  /**
	 * @return Returns the calendar.
	 */
	public Calendar getCalendar()
	{
		if (this.calendar != null)
		{
			return this.calendar;
		}

		Calendar dateCalendar = this.dateLiteral.getCalendar();
		if (null == this.timeLiteral)
		{
			dateCalendar.set(Calendar.HOUR_OF_DAY, 0);
			dateCalendar.set(Calendar.MINUTE, 0);
			dateCalendar.set(Calendar.SECOND, 0);
			dateCalendar.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			Calendar timeCalendar = this.timeLiteral.getCalendar();
			dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
			dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
			dateCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
			dateCalendar.set(Calendar.MILLISECOND, timeCalendar.get(Calendar.MILLISECOND));
		}
		return dateCalendar;
	}
}
