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

package de.tif.jacob.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimpleFormatter extends Formatter 
{
  static public final transient String RCS_ID = "$Id: SimpleFormatter.java,v 1.1 2007/01/19 09:50:38 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  
  Timestamp dat = new Timestamp(0);

  // Line separator string.  This is the value of the line.separator
  // property at the moment that the SimpleFormatter was created.
  private String lineSeparator = (String) java.security.AccessController.doPrivileged(
      new sun.security.action.GetPropertyAction("line.separator"));

  /**
   * Format the given LogRecord.
   * @param record the log record to be formatted.
   * @return a formatted log record
   */
  public synchronized String format(LogRecord record) 
  {
    StringBuffer sb = new StringBuffer();
    /*
    // Minimize memory allocations here.
    dat.setTime(record.getMillis());
    sb.append(dat.toString());
    sb.append(" ");
    if (record.getSourceClassName() != null) {  
      sb.append(record.getSourceClassName());
    } else {
      sb.append(record.getLoggerName());
    }
    if (record.getSourceMethodName() != null) { 
      sb.append(" ");
      sb.append(record.getSourceMethodName());
    }
//    sb.append(lineSeparator);
 * */

    String message = formatMessage(record);
    sb.append(record.getLevel().getLocalizedName());
    sb.append(": ");
    sb.append(message);
    sb.append(lineSeparator);
    if (record.getThrown() != null) {
      try {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        record.getThrown().printStackTrace(pw);
        pw.close();
        sb.append(sw.toString());
      } catch (Exception ex) {
      }
    }
    return sb.toString();
  }
}
