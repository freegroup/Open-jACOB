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

import java.util.Collection;
import java.util.Collections;

import de.tif.jacob.core.Session;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.security.IUser;

/**
 *
 */
public class SOAPSession extends Session
{
  private final IUser user;
  private final IApplicationDefinition appDef;
  
  protected SOAPSession(IApplicationDefinition appDef, IUser user)
  {
    this.user=user;
    this.appDef=appDef;
  }
  
  public IApplication getApplication(String browserId) throws Exception
  {
    return null;
  }
  
  public IApplicationDefinition getApplicationDefinition()
  {
    return appDef;
  }
  
  public Collection getApplications()
  {
    return Collections.EMPTY_LIST;
  }
  
  public String getApplicationRootWebPath()
  {
    // TODO: Fehlt noch
    return null;
  }

  public String getClientAddress()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public String getHost()
  {
    // FREEGROUP: Host fehlt und unter Session ist nicht beschrieben, daß auch null zurückkommen darf!
    return null;
  }
  
  public String getId()
  {
    return "SOAPSession:"+this.hashCode();
  }
  
  public IUser getUser()
  {
    return user;
  }
  
  public void sendKeepAlive(String browserId)
  {
    // do nothing
  }
}
