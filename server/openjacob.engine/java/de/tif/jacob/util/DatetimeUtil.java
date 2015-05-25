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

package de.tif.jacob.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import de.tif.jacob.core.data.impl.qbe.QBEDateLiteral;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBELiteral;
import de.tif.jacob.core.data.impl.qbe.QBELiteralExpression;
import de.tif.jacob.core.data.impl.qbe.QBEScanner;
import de.tif.jacob.core.data.impl.qbe.QBETimeLiteral;
import de.tif.jacob.core.data.impl.qbe.QBETimestampLiteral;
import de.tif.jacob.core.definition.fieldtypes.TimestampResolution;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * Class to convert date, time and datetime objects to strings and vice versa.
 * 
 * @author Andreas Sonntag
 */
public final class DatetimeUtil
{
  static public final transient String RCS_ID = "$Id: DatetimeUtil.java,v 1.4 2009/12/08 00:19:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  private static final DateFormat ansiDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat deDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
  private static final DateFormat usDateFormatter = new SimpleDateFormat("MM/dd/yyyy");

  private static final DateFormat time24Formatter = new SimpleDateFormat("HH:mm");
  private static final DateFormat usTimeFormatter = new SimpleDateFormat("h:mm a");

  /**
   * Converts a string to a date object.
   * 
   * @param str
   *          the string to convert
   * @return the date object or <code>null</code> if <code>str</code>
   *         is <code>null</code> or an empty string
   * @throws InvalidExpressionException
   *           if <code>str</code> could not be parsed correctly
   */
  public static Date convertToDate(String str) throws InvalidExpressionException
  {
    if (str == null)
    {
      return null;
    }
    QBEExpression expr = QBEExpression.parse(QBEScanner.DATE_VALUE, str);
    if (expr == null)
    {
      return null;
    }
    else if (expr instanceof QBELiteralExpression)
    {
      QBELiteral literal = ((QBELiteralExpression) expr).getLiteral();
      if (literal instanceof QBEDateLiteral)
      {
        try
        {
          return new Date(((QBEDateLiteral) literal).getCalendar().getTimeInMillis());
        }
        catch (Exception ex)
        {
          throw new InvalidExpressionException(str, "Invalid date", ex);
        }
      }
    }
    // should never occur
    throw new InvalidExpressionException(str);
  }

  /**
   * Converts a string to a time object.
   * 
   * @param str
   *          the string to convert
   * @return the time object or <code>null</code> if <code>str</code>
   *         is <code>null</code> or an empty string
   * @throws InvalidExpressionException
   *           if <code>str</code> could not be parsed correctly
   */
  public static Time convertToTime(String str) throws InvalidExpressionException
  {
    if (str == null)
    {
      return null;
    }
    QBEExpression expr = QBEExpression.parse(QBEScanner.TIME_VALUE, str);
    if (expr == null)
    {
      return null;
    }
    else if (expr instanceof QBELiteralExpression)
    {
      QBELiteral literal = ((QBELiteralExpression) expr).getLiteral();
      if (literal instanceof QBETimeLiteral)
      {
        try
        {
          return new Time(((QBETimeLiteral) literal).getCalendar().getTimeInMillis());
        }
        catch (Exception ex)
        {
          throw new InvalidExpressionException(str, "Invalid time", ex);
        }
      }
    }
    // should never occur
    throw new InvalidExpressionException(str);
  }

  /**
   * Converts a string to a timestamp object.
   * 
   * @param str
   *          the string to convert
   * @return the timestamp object or <code>null</code> if <code>str</code>
   *         is <code>null</code> or an empty string
   * @throws InvalidExpressionException
   *           if <code>str</code> could not be parsed correctly
   */
  public static Timestamp convertToTimestamp(String str) throws InvalidExpressionException
  {
    if (str == null)
    {
      return null;
    }
    QBEExpression expr = QBEExpression.parse(QBEScanner.TIMESTAMP_VALUE, str);
    if (expr == null)
    {
      return null;
    }
    else if (expr instanceof QBELiteralExpression)
    {
      QBELiteral literal = ((QBELiteralExpression) expr).getLiteral();
      try
      {
        if (literal instanceof QBETimestampLiteral)
        {
          return new Timestamp(((QBETimestampLiteral) literal).getCalendar().getTimeInMillis());
        }
        if (literal instanceof QBEDateLiteral)
        {
          return new Timestamp(((QBEDateLiteral) literal).getCalendar().getTimeInMillis());
        }
        if (literal instanceof QBETimeLiteral)
        {
          return new Timestamp(((QBETimeLiteral) literal).getCalendar().getTimeInMillis());
        }
      }
      catch (Exception ex)
      {
        throw new InvalidExpressionException(str, "Invalid timestamp", ex);
      }
    }
    // should never occur
    throw new InvalidExpressionException(str);
  }

  /**
   * Converts a date object to a localized string.
   * 
   * @param date
   *          the date object
   * @param locale
   *          the locale
   * @return the localized string or <code>null</code> if <code>date</code>
   *         is <code>null</code>
   * @since 2.7.2
   */
  public static String convertDateToString(java.util.Date date, Locale locale)
  {
    if (date == null)
      return null;

    DateFormat format;
    if (locale != null)
    {
      format = deDateFormatter;
      if ("en".equals(locale.getLanguage()))
      {
        if ("US".equals(locale.getCountry()))
          format = usDateFormatter;
      }
    }
    else
    {
      format = ansiDateFormatter;
    }

    // Note: date formatters are not synchronized
    synchronized (format)
    {
      return format.format(date);
    }
  }

  /**
   * Converts a timestamp object to a localized string.
   * <p>
   * Note: The timestamp object will be converted on second resolution base.
   * 
   * @param timestamp
   *          the timestamp object
   * @param locale
   *          the locale
   * @return the localized string or <code>null</code> if <code>timestamp</code>
   *         is <code>null</code>
   * @since 2.7.2
   */
  public static String convertTimestampToString(Timestamp timestamp, Locale locale)
  {
    if (timestamp == null)
      return null;

    return TimestampResolution.SEC_BASE.toString(timestamp, locale);
  }

  /**
   * Converts a timestamp object to a localized string.
   * <p>
   * Note: The timestamp object will be converted on minute resolution base.
   * 
   * @param timestamp
   *          the timestamp object
   * @param locale
   *          the locale
   * @return the localized string or <code>null</code> if <code>timestamp</code>
   *         is <code>null</code>
   * @since 2.7.2
   */
  public static String convertTimestampOnMinuteBaseToString(Timestamp timestamp, Locale locale)
  {
    if (timestamp == null)
      return null;

    return TimestampResolution.MIN_BASE.toString(timestamp, locale);
  }

  /**
   * Converts a timestamp object to a localized string.
   * <p>
   * Note: The timestamp object will be converted on millisecond resolution base.
   * 
   * @param timestamp
   *          the timestamp object
   * @param locale
   *          the locale
   * @return the localized string or <code>null</code> if <code>timestamp</code>
   *         is <code>null</code>
   * @since 2.7.2
   */
  public static String convertTimestampOnMillisecondBaseToString(Timestamp timestamp, Locale locale)
  {
    if (timestamp == null)
      return null;

    return TimestampResolution.MSEC_BASE.toString(timestamp, locale);
  }

  /**
   * Converts a time object to a localized string.
   * 
   * @param time
   *          the time object
   * @param locale
   *          the locale
   * @return the localized string or <code>null</code> if <code>time</code>
   *         is <code>null</code>
   * @since 2.9
   */
  public static String convertTimeToString(Time time, Locale locale)
  {
    if (time == null)
      return null;

    DateFormat format = time24Formatter;
    if (locale != null)
    {
      if ("en".equals(locale.getLanguage()))
      {
        if ("US".equals(locale.getCountry()))
          format = usTimeFormatter;
      }
    }

    // Note: date formatters are not synchronized
    synchronized (format)
    {
      return format.format(time);
    }
  }
}
