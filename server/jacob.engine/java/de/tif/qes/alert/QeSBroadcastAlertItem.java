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

package de.tif.qes.alert;

import java.net.URL;
import java.util.Date;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.messaging.alert.IAlertItem;

/**
 *
 */
public class QeSBroadcastAlertItem extends IAlertItem
{
  static public final transient String RCS_ID = "$Id: QeSBroadcastAlertItem.java,v 1.1 2006-12-21 11:32:21 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  final private String message;
  final private Date date;
  final private String sender;
  final private String qwkey;
  final private Severity severity;

  /**
   * 
   */
  public QeSBroadcastAlertItem(IDataTableRecord alert) throws Exception
  {
    this.message = alert.getSaveStringValue("message");
    this.date = alert.getTimestampValue("dateposted");
    this.sender = alert.getSaveStringValue("sender");
    this.qwkey = alert.getStringValue("qwkey");
    switch (alert.getintValue("severity"))
    {
      case 0: // 1-Normal
        this.severity = DEFAULT;
        break;
      case 1: // 2-Kritisch
        this.severity = LOW;
        break;
      case 2: // 3-Produktion
        this.severity = MEDIUM;
        break;
      case 3: // 4-Notfall
        this.severity = HIGH;
        break;
      case 4: // DEFAULT
        this.severity = DEFAULT;
        break;
      default:
        this.severity = DEFAULT;
    }
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#delete(de.tif.jacob.core.Context)
   */
  public void delete(Context context) throws Exception
  {
    throw new UserException("Unable to delete a common/global alert item.");
  }

  /*
   * @see de.tif.jacob.alert.IAlertItem#getKey()
   */
  public String getKey()
  {
    return qwkey;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getDate()
   */
  public Date getDate() throws Exception
  {
    return date;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getMessage()
   */
  public String getMessage() throws Exception
  {
    return message;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getSender()
   */
  public String getSender() throws Exception
  {
    return sender;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getSeverity()
   */
  public Severity getSeverity() throws Exception
  {
    return severity;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#isDeleteable()
   */
  public boolean isDeleteable() throws Exception
  {
    return false;
  }

  /**
   * toString methode: creates a String representation of the object
   * @return the String representation
   * @author info.vancauwenberge.tostring plugin
   
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("BroadcastAlertItem[");
    buffer.append("message = ").append(message);
    buffer.append(", date = ").append(date);
    buffer.append(", sender = ").append(sender);
    buffer.append(", qwkey = ").append(qwkey);
    buffer.append(", severity = ").append(severity);
    buffer.append("]");
    return buffer.toString();
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getDisplayUrl(de.tif.jacob.core.Context)
   */
  public URL getDisplayUrl(SessionContext context)
  {
    return null;
  }

}
