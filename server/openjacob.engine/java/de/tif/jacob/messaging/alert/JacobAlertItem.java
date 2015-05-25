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
package de.tif.jacob.messaging.alert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.security.IUser;

/**
 * This class represents an alert item as it is stored within the jACOB
 * configuration data source.
 * 
 * @author Andreas Sonntag
 */
public class JacobAlertItem extends IAlertItem implements Comparable
{
  public static final String RCS_ID = "$Id: JacobAlertItem.java,v 1.2 2009/03/02 19:06:41 ibissw Exp $";
  public static final String RCS_REV = "$Revision: 1.2 $";

  static private final transient Log logger = LogFactory.getLog(JacobAlertItem.class);
  
  private final String message;
  private final String url;
  private final Date date;
  private final String sender;
  private final String idStr;
  private final int id;
  private final Severity severity;

  /**
   *  
   */
  JacobAlertItem(IDataTableRecord alert) throws Exception
  {
    this.message = alert.getSaveStringValue("message");
    this.url = alert.getStringValue("url");
    this.date = alert.getTimestampValue("created");
    this.sender = alert.getStringValue("sender");
    this.id = alert.getintValue("id");
    this.idStr = alert.getStringValue("id");

    String sev = alert.getStringValue("severity");
    if ("low".equals(sev))
      this.severity = LOW;
    else if ("medium".equals(sev))
      this.severity = MEDIUM;
    else if ("high".equals(sev))
      this.severity = HIGH;
    else
      this.severity = DEFAULT;
  }

  /*
   * @see de.tif.jacob.alert.IAlertItem#delete(de.tif.jacob.core.Context)
   */
  public void delete(Context context) throws Exception
  {
    IUser user = context.getUser();
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();

    IDataTransaction trans = accessor.newTransaction();
    try
    {
      // the current user is the sender (owner) of the alert?
      if (user.getLoginId().equals(this.sender))
      {
        // yes -> delete complete alert item
        IDataTable alerts = accessor.getTable(JacobAlertItemProvider.JACOB_ALERT_ALIAS);
        alerts.qbeSetValue("id", this.idStr);
        alerts.searchAndDelete(trans);
        trans.commit();
      }
      else
      {
        // no -> just mark alert as being deleted for this user
        IDataTable alertUserStatus = accessor.getTable(JacobAlertItemProvider.JACOB_ALERT_USER_STATUS);
        IDataTableRecord record = alertUserStatus.newRecord(trans);
        record.setIntValue(trans, "alertid", this.id);
        record.setValue(trans, "userid", user.getLoginId());
        record.setValue(trans, "userstatus", "deleted");
        
        // IBIS: Exception abfangen sofern der eigentlich Alert schon gelöscht ist.
        trans.commit();
      }
    }
    finally
    {
      trans.close();
    }
  }

  /*
   * @see de.tif.jacob.alert.IAlertItem#getKey()
   */
  public String getKey()
  {
    return idStr;
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
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object anObject)
  {
    if (this == anObject)
    {
      return true;
    }
    if (anObject instanceof JacobAlertItem)
    {
      JacobAlertItem another = (JacobAlertItem) anObject;
      return this.id == another.id;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object o)
  {
    JacobAlertItem other = (JacobAlertItem) o;

    // note: higher id's should come first!
    return other.id - this.id;
  }

  /**
   * toString methode: creates a String representation of the object
   * 
   * @return the String representation
   * @author info.vancauwenberge.tostring plugin
   *  
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("JacobAlertItem[");
    buffer.append("message = ").append(message);
    buffer.append(", url = ").append(url);
    buffer.append(", date = ").append(date);
    buffer.append(", sender = ").append(sender);
    buffer.append(", id = ").append(id);
    buffer.append("]");
    return buffer.toString();
  }

  /*
   * @see de.tif.jacob.alert.IAlertItem#getDisplayUrl(de.tif.jacob.core.Context)
   */
  public URL getDisplayUrl(SessionContext context)
  {
    if (this.url != null)
    {
      try
      {
        return new URL(this.url);
      }
      catch (MalformedURLException ex)
      {
        if (logger.isWarnEnabled())
          logger.warn("Malformed alert item URL: " + this.url);
      }
    }
    return null;
  }
}
