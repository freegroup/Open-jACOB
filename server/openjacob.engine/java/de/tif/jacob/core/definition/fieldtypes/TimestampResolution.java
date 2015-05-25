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

package de.tif.jacob.core.definition.fieldtypes;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class TimestampResolution
{
  static public final transient String RCS_ID = "$Id: TimestampResolution.java,v 1.2 2008/02/12 22:44:31 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  public static final TimestampResolution MIN_BASE =
    new TimestampResolution(
        "MIN_BASE", 
        new SimpleDateFormat("yyyy-MM-dd HH:mm"), 
        new SimpleDateFormat("dd.MM.yyyy HH:mm"), 
        new SimpleDateFormat("MM/dd/yyyy h:mm a"),
        TimestampFieldResolutionType.MINBASE);
  public static final TimestampResolution SEC_BASE = 
    new TimestampResolution(
        "SEC_BASE", 
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), 
        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"),
        new SimpleDateFormat("MM/dd/yyyy h:mm:ss a"),
        TimestampFieldResolutionType.SECBASE);
  public static final TimestampResolution MSEC_BASE = 
    new TimestampResolution(
        "MSEC_BASE", 
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"), 
        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS"), 
        new SimpleDateFormat("MM/dd/yyyy h:mm:ss.SSS a"),
        TimestampFieldResolutionType.MSECBASE);
  
  private final String name;
  private final DateFormat ansiFormatter;
  private final DateFormat deFormatter;
  private final DateFormat usFormatter;
  private final TimestampFieldResolutionType castorType;
  
  public static TimestampResolution fromJacob(TimestampFieldResolutionType castorType)
  {
    if (castorType == TimestampFieldResolutionType.MINBASE)
      return MIN_BASE;
    if (castorType == TimestampFieldResolutionType.SECBASE)
      return SEC_BASE;
    if (castorType == TimestampFieldResolutionType.MSECBASE)
      return MSEC_BASE;
    throw new RuntimeException("Unknown resolution: "+castorType);
  }

  private TimestampResolution(String name, DateFormat ansiFormatter, DateFormat deFormatter, DateFormat usFormatter, TimestampFieldResolutionType castorType)
  {
    this.name = name;
    this.ansiFormatter = ansiFormatter;
    this.deFormatter = deFormatter;
    this.usFormatter = usFormatter; 
    this.castorType = castorType;
  }
  
  public String toString(Timestamp timestamp, Locale locale)
  {
    DateFormat formatter;
    if (locale == null)
    {
      formatter = this.ansiFormatter;
    }
    else
    {
      formatter = this.deFormatter;
      if ("en".equals(locale.getLanguage()))
      {
        if ("US".equals(locale.getCountry()))
          formatter = this.usFormatter;
      }    
    }
      
    synchronized (formatter)
		{
			return formatter.format(timestamp);
		}
  }
  
  protected Timestamp adjust(Timestamp timestamp)
  {
    if (null != timestamp)
    {  
    	if (this != MSEC_BASE)
    	{
    		timestamp.setNanos(0);
    		if (this == MIN_BASE)
    		{
    			timestamp.setSeconds(0);
    		}
//      Calendar calendar = new GregorianCalendar();
//      calendar.setTime(timestamp);
//      calendar.setLenient(false);
//      calendar.set(Calendar.MILLISECOND, 0);
//      if (this == MIN_BASE)
//      {
//        calendar.set(Calendar.SECOND, 0); 
//      }
//      return new Timestamp(calendar.getTimeInMillis());
      }
    }
    return timestamp;
  }
  
  /**
   * toString methode: creates a String representation of the object
   * @return the String representation
   */
  public String toString()
  {
    return this.name;
  }
  
  public TimestampFieldResolutionType toJacob()
  {
    return this.castorType;
  }
  
}
