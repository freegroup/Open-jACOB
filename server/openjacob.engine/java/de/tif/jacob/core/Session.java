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

package de.tif.jacob.core;

import java.util.ArrayList;
import java.util.Collection;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.security.IUser;

/**
 *
 */
public abstract class Session
{
  /**
   * Returns the unique session id. The session id is assigned to one login.
   * The session id will be assigned at the login.
   * @return
   */
  public abstract String getId();
  
  /**
   * It is possible to opne more than one window for a session (login).<br> 
   * Each window has an unique id. It is possible to retrieve the IApplication 
   * instance with this id.<br>
   * 
   * @param browserId The client browser window id. 
   * @return The IApplication instance which is assigned to this id
   * @throws Exception will be throws if the hands over id is invalid.
   */
  public abstract IApplication getApplication(String browserId) throws Exception;
  
  /**
   * Returns the application definition which is assigned to the session.<br>
   * A session can only create/handle one type of application. <br>
   * The type and the version of the application are fixed for one session.
   * 
   * @return The ApplicationDefinition with the meta information about the assigned application type.
   */
  public abstract IApplicationDefinition getApplicationDefinition();
  
  /**
   * Returns the applications root web path, which is the absolute path there
   * the applications web resources are located, i.e.
   * "J:\jakarta-tomcat-5.5.23\webapps\jacob\application\visualenterprise\0.7".
   * <p>
   * Note: The path is not ending with a file separator!
   * 
   * @return the applications root web path
   * @since 2.7.2
   */
  public abstract String getApplicationRootWebPath();
  
  /**
   * Returns the IUser instance of the logged in user. It is not possible to create an Session without
   * any user information.
   * 
   * @return return the logged in IUser.
   */
  public abstract IUser getUser();
  
  /**
   * The hostname to which the client has been connected. This is required for the
   * different session/auth handling.<br>
   * <br>
   * It is possible that the client has been connected to <code>host</code> or <code>host.domain</code>
   * <code>ip-address</code>. This method returns the name of the host (my name itself).
   * <br>
   * 
   * @return The name of the host
   */
  public abstract String getHost();
  
  /**
   * Returns the client IP address if available.
   * 
   * @return the IP address of the client or <code>null</code> if not available
   * @since 2.8.3
   */
  public abstract String getClientAddress();
  
  
  /**
   * Refresh the keep alive for the hands over browserId.<br>
   * The keep alive <b>must</b> be called in fixed time intervalls.
   * The data structur for the browser window (user client) will be removed if 
   * the signal are absence. The correponding user window is now invalid.
   *  
   * @param browserId the unique id of the browser (client) window.
   */
  public abstract void sendKeepAlive(String browserId);
  
  
  /**
   * Returns all living applications instances in this Session.<br>
   * For each client browser is one IApplication created.
   *  
   * @return Collection[IApplication]
   */
  public abstract Collection getApplications();
  
  /**
   * Create the Task which should run in the background for the session context.<br>
   * This method will be called once per session.<br>
   * 
   * @return Collection[SchedulerTask]
   */
  public Collection createSessionTasks()
  {
    Collection result = new ArrayList();
    
    return result;
  }
}
