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

import java.util.List;

import de.tif.jacob.core.Context;

/**
 * Interface for the application programmer to feed the alert mechanism for
 * jACOB.
 * <p>
 * Your implementations of <code>IAlertItemProvider</code> must be in the java
 * package <code>jacob.alert.*</code>.<br>
 * Ensure that your class has an public default constructor! The AlertManager
 * displays the alerts in a new dialog if your provider returns a
 * <code>List</code> of {@link IAlertItem}<br>
 * It is your turn to implement the interface {@link IAlertItem} and create them
 * with data from a database or something else. <br>
 * 
 * @author Andreas Herz
 */
public interface IAlertItemProvider
{
  /**
   * Returns the alert items for the current logged in user. You can request the current user at the
   * hands over context (<code>context.getUser()</code>).<br>
   * <br>
   * @param context the current working context.
   * @return <code>List</code> of {@link IAlertItem} or <code>null</code> if not provided.
   * @throws Exception on any problem accessing alert items
   */
  public List getReceivedAlertItems(Context context)throws Exception;

  /**
   * Returns the alert items which have been sent by the current logged in user.
   * 
   * @param context
   *          the current working context.
   * @return <code>List</code> of {@link IAlertItem} or <code>null</code> if
   *         not provided.
   * @throws Exception
   *           on any problem accessing alert items
   */
  public List getSendedAlertItems(Context context) throws Exception;
}
