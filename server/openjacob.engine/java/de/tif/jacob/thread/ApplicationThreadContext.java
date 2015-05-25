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

package de.tif.jacob.thread;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.ManagedResource;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.security.UserManagement;

/**
 *
 */
public class ApplicationThreadContext extends Context
{
  static public final transient String RCS_ID = "$Id: ApplicationThreadContext.java,v 1.2 2009/08/17 21:17:08 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  protected ApplicationThreadContext(IApplicationDefinition app)
  {
    super(app, UserManagement.getSystemUser());
  }

  public void canAbort(boolean flag)
  {
    // ignore. A ApplicationthreadContext didn't provide a abort feature
  }

  public boolean shouldAbort()
  {
    return false;
  }

  /**
   * There is no session for a taskContext. The TaskContext is 
   * only valid for one schedule cycle. Redirect the registration to the 
   * request life cycle handler.<br>
   * <br>
   * Call Context.getCurrent().unregister(..) if you have destroyed the resource
   * manually. If you don't call it, the resource will be released twice. 
   * 
   * @param resource The resource to manage.
   */
  public void registerForSession(ManagedResource resource)
  {
    registerForRequest(resource);
  }

  /**
   * There is no window for a taskContext. The TaskContext is 
   * only valid for one schedule cycle. Redirect the registration to the 
   * request life cycle handler.<br>
   * <br>
   * Call Context.getCurrent().unregister(..) if you have destroyed the resource
   * manually. If you don't call it, the resource will be released twice. 
   * 
   * @param resource The resource to manage.
   */
  public void registerForWindow(ManagedResource resource)
  {
    registerForRequest(resource);
  }
  
  /**
   * There is no session for a ApplicationThreadContext. The ApplicationThreadContext is 
   * only valid for one schedule cycle. Redirect the property to the 
   * request life cycle handler.<br>
   * <br>
   */
  public void setPropertyForSession(Object key, Object value)
  {
    setPropertyForRequest(key,value);
  }
  
  /**
   * There is no window for a ApplicationThreadContext. The ApplicationThreadContext is 
   * only valid for one schedule cycle. Redirect the property to the 
   * request life cycle handler.<br>
   * <br>
   */
  public void setPropertyForWindow(Object key, Object value)
  {
    setPropertyForRequest(key,value);
  }
}
