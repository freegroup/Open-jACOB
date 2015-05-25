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
import java.util.GregorianCalendar;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QBENowLiteral extends QBETimestampLiteral
{
  static public transient final String        RCS_ID = "$Id: QBENowLiteral.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";

	private final QBETimeLag timeLag;
  
	/**
	 * @param timeLag
	 */
	public QBENowLiteral(QBETimeLag timeLag)
	{
    this.timeLag = timeLag;
	}

  /**
   * @return Returns the calendar.
   */
  public Calendar getCalendar()
  {
    // TODO: initialize Locale and TimeZone
    Calendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(System.currentTimeMillis());
    if (null != this.timeLag)
      calendar = this.timeLag.addToCalendar(calendar);
    return calendar;
  }
  
}
