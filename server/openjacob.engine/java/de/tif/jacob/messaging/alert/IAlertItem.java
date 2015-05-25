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

import java.net.URL;
import java.util.Date;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;

/**
 *
 */
public abstract class IAlertItem
{
  public static class Severity{String label; Severity(String label){this.label=label;}public String toString(){return label;}};
  
  public final static Severity HIGH   = new Severity("HIGH");
  public final static Severity MEDIUM = new Severity("MEDIUM");
  public final static Severity LOW    = new Severity("LOW");
  public final static Severity DEFAULT= new Severity("DEFAULT");
  
  /**
   * Returns the URL of the extended property display dialog of the AlertItem.<br>
   * The URL must be full qualified like <code>http://my.server/applicationA/display.jsp?item=abc</code>
   *  
   * @return Returns the URL of the extended property display dialog of the AlertItem.<br>
   */
  public abstract URL getDisplayUrl(SessionContext context);
  
  
  /**
   * Returns the unique identifier of the alert item
   * 
   * @return Returns the unique identifier of the alert item.
   */
  public abstract String getKey();
  
  /**
   * Delete the <code>IAlertItem</code> from the storage.
   * 
   * @param context the current work context of the jACOB.
   * 
   * @throws Exception 
   */
  public abstract void  delete(Context context) throws Exception;
  
  /**
   * Returns true if the current user (context.getUser()) has the rights to delete the
   * <code>IAlertItem</code><br>
   * 
   * @param context the current work context of the jACOB
   * @return returns true if the context.getUser() has the rights to delete the IAlertItem.
   * 
   * @throws Exception
   */
  public abstract boolean isDeleteable() throws Exception;
  
  /**
   * Return the message of the IAlertItem
   * 
   * @param context the current work context of the jACOB
   * @return the message of the alert itself.
   * 
   * @throws Exception
   */
  public abstract String  getMessage() throws Exception;
  
  
  /**
   * The date of the receive of the alert item.
   * 
   * @param context the current work context of the jACOB
   * @return The date of the receiveing of hte alert item.
   * 
   * @throws Exception
   */
  public abstract Date  getDate()    throws Exception;
  
  /**
   * The IUser instance of the sender of null if the user comes from an external system.<br>
   * 
   * @param context the current work context of the jACOB
   * @return The name/id of the sender.
   * 
   * @throws Exception
   */
  public abstract String getSender() throws Exception;
  
  /**
   * Returns the severity of the alert item.
   * 
   * @return
   * @throws Exception
   */
  public abstract Severity getSeverity() throws Exception;
}
