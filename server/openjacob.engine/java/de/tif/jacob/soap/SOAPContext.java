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

package de.tif.jacob.soap;

import de.tif.jacob.core.ManagedResource;
import de.tif.jacob.core.Session;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.screen.impl.HTTPApplication;
import de.tif.jacob.security.IUser;

/**
 *
 */
public class SOAPContext extends SessionContext
{
  private final SOAPSession session;
  
  protected SOAPContext(SOAPSession session, IApplicationDefinition appDef, IUser user)
  {
    super(appDef, user);
    this.session = session;
  }
  
  public void canAbort(boolean flag)
  {
    // ignore. A SOAPContext didn't provide a abort feature
  }

  public boolean shouldAbort()
  {
    return false;
  }

  /* 
   * @see de.tif.jacob.core.SessionContext#getSession()
   */
  public Session getSession()
  {
    return session;
  }

  /* 
   * @see de.tif.jacob.core.Context#registerForSession(de.tif.jacob.core.ManagedResource)
   */
  public void registerForSession(ManagedResource resource)
  {
    // SOAP Request is stateless. In this case all resource has only a request life cycle.
    //
    super.registerForRequest(resource);
  }

  /* 
   * @see de.tif.jacob.core.Context#registerForWindow(de.tif.jacob.core.ManagedResource)
   */
  public void registerForWindow(ManagedResource resource)
  {
    // SOAP Request is stateless. In this case all resource has only a request life cycle.
    //
    super.registerForRequest(resource);
  }
  
  /**
   * There is no session for a SOAPContext. The SOAPContext is 
   * only valid for one schedule cycle. Redirect the property to the 
   * request life cycle handler.<br>
   * <br>
   */
  public void setPropertyForSession(Object key, Object value)
  {
    setPropertyForRequest(key,value);
  }
  
  /**
   * There is no window for a SOAPContext. The SOAPContext is 
   * only valid for one schedule cycle. Redirect the property to the 
   * request life cycle handler.<br>
   * <br>
   */
  public void setPropertyForWindow(Object key, Object value)
  {
    setPropertyForRequest(key,value);
  }
}
