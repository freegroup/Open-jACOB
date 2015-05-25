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

package de.tif.jacob.screen.impl.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import de.tif.jacob.core.Property;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.impl.HTTPApplication;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.screen.impl.IApplicationFactory;
import de.tif.jacob.screen.impl.html.schedulertasks.AlertMonitor;
import de.tif.jacob.screen.impl.html.schedulertasks.ApplicationMonitor;
import de.tif.jacob.screen.impl.html.schedulertasks.DialogMonitor;
import de.tif.jacob.screen.impl.html.schedulertasks.SessionMonitor;
import de.tif.jacob.screen.impl.html.schedulertasks.SessionStatisticCollector;
import de.tif.jacob.screen.impl.tag.BrowserType;
import de.tif.jacob.screen.impl.theme.Theme;
import de.tif.jacob.screen.impl.theme.ThemeManager;
import de.tif.jacob.security.IUser;

/**
 * @author Andreas Herz
 *  
 */
public class ClientSession extends HTTPClientSession 
{
  transient static public  final String RCS_ID = "$Id: ClientSession.java,v 1.4 2010/10/15 11:18:00 ibissw Exp $";
  transient static public  final String RCS_REV = "$Revision: 1.4 $";

  transient private final IApplicationFactory applicationFactory = new ApplicationFactory()
  {
    public IApplication createApplication(IUser user, IApplicationDefinition definition) throws InstantiationException
    {
      try
      {
        return new Application(definition, ClientSession.this.getId() + SESSION_APPLICATION_KEY_SEPARATOR + ClientSession.this.nextApplicationNumber++);
      }
      catch (Exception e)
      {
        throw new InstantiationException(e.toString());
      }
    }
  };
  
  transient private int nextApplicationNumber = 1;
  transient private StringBuffer asynchronJavaScriptCode = null;
  transient private StringBuffer asynchronOnLoadJavaScriptCode = null;
  transient protected final String clientBrowserType;
  transient Theme currentUserTheme=null;
  
  public ClientSession(HttpServletRequest request, IUser user, IApplicationDefinition applicationDef)
  {
    super(request, user, applicationDef);

    this.clientBrowserType = BrowserType.getType(request);
  }
  
  public IApplicationFactory getApplicationFactory()
  {
    return this.applicationFactory;
  }
	

  public HTTPClientContext createContext(IUser user, HTTPApplication app, String clientId)  throws IOException
  {
    return new  de.tif.jacob.screen.impl.html.ClientContext(user,app, clientId);
  }
  
	
  public List getDomains(String browserId) throws Exception
  {
    List result = getApplication(browserId).getChildren();
    for (Iterator iter = result.iterator(); iter.hasNext();)
    {
      Domain element = (Domain) iter.next();
      element.browserId = browserId;
     
    }
    return result;
  }
  
  /**
   * 
   * @param message
   * @author Andreas Herz
   */
  public synchronized void addAsynchronJavaScript(String code)
  {
    if (asynchronJavaScriptCode == null)
    {
      asynchronJavaScriptCode = new StringBuffer(3*code.length());
    }
    else
    {
      asynchronJavaScriptCode.append("\n\t\t");
    }
    asynchronJavaScriptCode.append(code); 
  }
  
  public synchronized void addAsynchronOnLoadJavaScript(String code)
  {
    if (this.asynchronOnLoadJavaScriptCode == null)
    {
      this.asynchronOnLoadJavaScriptCode = new StringBuffer(3*code.length());
    }
    else
    {
      this.asynchronOnLoadJavaScriptCode.append("\n\t\t\t");
    }
    this.asynchronOnLoadJavaScriptCode.append(code); 
  }
  
  /**
   */
  public synchronized String fetchAsynchronJavaScript()
  {
    if (asynchronJavaScriptCode == null)
    {
      return "";
    }
    String m = asynchronJavaScriptCode.toString();
    asynchronJavaScriptCode = null;
    return m;
  }
  
  public synchronized String fetchAsynchronOnLoadJavaScript()
  {
    if (this.asynchronOnLoadJavaScriptCode == null)
    {
      return "";
    }
    String m = this.asynchronOnLoadJavaScriptCode.toString();
    this.asynchronOnLoadJavaScriptCode = null;
    return m;
  }
  
 
	/**
	 * @return Returns the currentTheme.
   * @deprecated use ClientSession.getTheme() instead
	 */
	public synchronized String getCurrentTheme()
	{
	  return getTheme().getId();
	}

  public synchronized Theme getTheme()
  {
    if(currentUserTheme==null)
      currentUserTheme = ThemeManager.getTheme(getRuntimeProperty(Property.USER_THEME_DEFAULT.getName()));
    return currentUserTheme;
  }

  /**
	 * @param newThemeId The currentTheme to set.
	 */
	protected synchronized void setCurrentTheme(String newThemeId)
	{
    // get the current theme or a valid default theme if the Id doesn't exists.
    //
    currentUserTheme = ThemeManager.getTheme(newThemeId);
    
    // write the new 'valid' themeId in the database
    //
	  setRuntimeProperty(Property.USER_THEME_DEFAULT.getName(),currentUserTheme.getId());
	  addAsynchronJavaScript("refreshTeaser();\n");
	}
  
	/**
   * Create the Task which should run in the background for the session context.<br>
   * This method will be called once per session.<br>
   * 
   * @return Collection[SchedulerTask]
   */
  public Collection createSessionTasks()
  {
    Collection result = new ArrayList();
    
    result.add(new SessionMonitor(this));

    // create the statistik modul ( session trecking) scheduler
    //
    result.add(new SessionStatisticCollector(this));

    result.add(new ApplicationMonitor(this));
    
    result.add(new DialogMonitor(this));
    
    // The alert notification mechanism for clients
    //
    result.add(new AlertMonitor(this));

    return result;
  }


  public String getClientBrowserType()
  {
    return clientBrowserType;
  }
}
