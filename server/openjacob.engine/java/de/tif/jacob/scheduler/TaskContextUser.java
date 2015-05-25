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

package de.tif.jacob.scheduler;

import java.util.TimeZone;

import de.tif.jacob.core.ManagedResource;
import de.tif.jacob.core.Session;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.screen.impl.html.dialogs.MessageDialog;
import de.tif.jacob.screen.impl.html.dialogs.UrlDialog;

/**
 * @author Andreas Herz
 *
 */
public class TaskContextUser extends SessionContext
{
  static public final transient String RCS_ID = "$Id: TaskContextUser.java,v 1.3 2010/11/12 11:23:18 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private final Session session;
  
  public TaskContextUser(Session session)
  {
    super(session.getApplicationDefinition(), session.getUser());
    
    this.session=session;
  }
  

  public void canAbort(boolean flag)
  {
    // ignore. A TaskContextUser didn't provide a abort feature
  }

  public boolean shouldAbort()
  {
    return false;
  }

  public TimeZone getTimeZone()
  {
    // FREEGROUP: TimeZone des Benutzers ausgeben
    return TimeZone.getDefault();
  }
  
  // FREEGROUP: Dies ist ein hack. Wer sagt den, dass ein Task im context einer HTMLsession läuft?!
  //            Es könnte z.B. auch eine SwingSession sein.
	//            Es muss dringend ein DialogProvider erstellt werden, welch unabhängig der Art des Clients
	//            die benötigten Dialoge erstellen kann.
	//
	public IMessageDialog createMessageDialog(String message)
	{
	  // HTML Dialog
	  return new MessageDialog(message,null);
	}

	
	// FREEGROUP: Dies ist ein hack. Wer sagt den, dass ein Task im context einer HTMLsession läuft?! 
	//            Es könnte z.B. auch eine SwingSession sein.
	//            Es muss dringend ein DialogProvider erstellt werden, welch unabhängig der Art des Clients
	//            die benötigten Dialoge erstellen kann.
	//
	public IUrlDialog createUrlDialog(String url)
	{
	  // HTML Dialog
	  return new UrlDialog(url);
	}

	/**
	 * There is no window for a taskContext. The TaskContext is 
	 * only valid for one schedule cycle. Redirect the registration to the 
	 * life cycle handler for the scope of a 'request'.<br>
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
	 * Register the Resource in the ClientSession scope. If the session will be destroyed, the 
	 * resource will be destroyed too.<br>
	 * <br>
	 * 
	 * Call Context.getCurrent().unregister(..) if you have destroyed the resource
	 * manually. If you don't call it, the resource will be released twice. 
	 * 
	 * @param resource The resource to manage.
	 */
	public void registerForSession(ManagedResource resource)
	{
	  ((HTTPClientSession)getSession()).register(resource);
	}

	public void unregister(ManagedResource resource)
	{
	  super.unregister(resource);
	  ((HTTPClientSession)getSession()).unregister(resource);
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

  /**
	 * Register the property in the ClientSession scope. If the session will be destroyed, the 
	 * properties will be removed too.<br>
   * <br>
   */
  public void setPropertyForSession(Object key, Object value)
  {
	  ((HTTPClientSession)getSession()).setPropertyForSession(key,value);
  }
  
  
  public Object getProperty(Object key)
  {
    Object value = ((HTTPClientSession)getSession()).getPropertyForSession(key);

    return value!=null?value:super.getProperty(key);
  }
  
	/**
   * @return Returns the session.
   */
  public final Session getSession()
  {
    return session;
  }
}
