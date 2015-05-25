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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.RequestCanceledException;
import de.tif.jacob.screen.ILongLastingOperation;
import de.tif.jacob.security.IUser;

/**
 * The generic base class for all context classes in jACOB.<br>
 * <br>
 * 
 * This will be used for the UI, scheduler, entry points....
 */
public abstract class Context
{
	static public transient final String RCS_ID = "$Id: Context.java,v 1.10 2011/01/17 08:07:59 freegroup Exp $";
	static public transient final String RCS_REV = "$Revision: 1.10 $";

  private final static ThreadLocal<Context> threadLocal = new ThreadLocal<Context>();

  private final static String STATUS_MESSAGE_GUID="9D5C5BA4-8B3B-11DE-B38B-DCDF55D89593";
  
	static protected final transient Log logger = LogFactory.getLog(Context.class);
	
	private final IUser user;
  private final IApplicationDefinition applicationDefinition;
  private IDataAccessor dataAccessor = null;
  private Locale applicationLocale = null;

  private boolean duringReleaseResources = false;
  
  // the managed resources for one request cycle
  //
  private Set requestManagedResources;

  // Properties welche für genau einen Request cyclus gültig sind
  // Nach dem cyclus werden die properties gelöscht. 
  //
  private Map requestCycleProperties;
  
  public Context(IApplicationDefinition appDef, IUser user)
  {
    this.user = user;
    this.applicationDefinition = appDef;
  }
  
  /**
   * Returns the current locale of the user or if you are running in the system 
   * context the locale of the system.
   * 
   * @return Locale of the user or system
   */
  public Locale getLocale()
  {
    // FREEGROUP: Locale aus der Admin-Datenbank holen
    return getApplicationLocale();
  }
  
  /**
	 * Returns the locale of the current application.
	 * <p>
	 * 
	 * The application locale might be different for different applications running in the same engine.
	 * 
	 * @return Locale of the current application
	 */
  public final Locale getApplicationLocale()
  {
    if (this.applicationLocale == null)
    {
      String language = Property.LANGUAGE_DEFAULT.getValue(this);
      String country = Property.COUNTRY_DEFAULT.getValue(this);
      if (language == null)
      {
        this.applicationLocale = Locale.getDefault();
      }
      else
      {
        try
        {
          this.applicationLocale = new Locale(language, country == null ? "" : country);
        }
        catch (Exception ex)
        {
          logger.warn("Invalid locale property settings (language=" + language + ";country=" + country + ")");
          this.applicationLocale = Locale.getDefault();
        }
      }
    }
    return this.applicationLocale;
  }
  
  /**
   * Returns the current time zone of the user or if you are running in the system 
   * context the time zone of the system.
   * 
   * @return TimeZone of the user or system
   */
  public TimeZone getTimeZone()
  {
    // FREEGROUP: TimeZone aus der Admin-Datenbank holen
    return TimeZone.getDefault();
  }

  
  /**
   * Register the ManagedResource in the user session of the current context.<br>
   * All registerd resources will be release if the session canceld abnormaly. <br>
   * One examples of an abnormal session termination is if the user press ALT+F4 in the 
   * InternetExplorer without a logout.
   * 
   * 
   * @param resource The resource which must be released if the session terminates.
   */
  public abstract void registerForSession(ManagedResource resource);
  
  public abstract void registerForWindow(ManagedResource resource);
  
  public final void registerForRequest(ManagedResource resource)
  {
    if(duringReleaseResources)
    {
      logger.warn("try to register manage resource during release of resources");
      // ensure that the resource will be released....the context is during the invalidate! 
      resource.release();
      return;
    }
    
    if (this.requestManagedResources == null)
      this.requestManagedResources = new HashSet();
    requestManagedResources.add(resource);
  }
  
  public void unregister(ManagedResource resource)
  {
    if(duringReleaseResources)
    {
      return;
    }
    if (this.requestManagedResources != null)
      requestManagedResources.remove(resource);
  }
  
  /**
   * Flag to indicate that the application or runtime is in demo mode
   * 
   * @return
   */
  public boolean isDemoMode()
  {
    return Property.DEMO_MODE.getBooleanValue();
  }

  
  /**
   * Set a property which has a session lifetime.<br>
   * If the session will be closed by the user or the jACOB application server all resources
   * will be freed.
   * @param key
   * @param value
   */
  public abstract void setPropertyForSession(Object key, Object value);
  
  /**
   * Set a property which has a window lifetime. If the user close a jACOB application
   * window all resources will be closed.<br>
   * 
   * @param key
   * @param value
   */
  public abstract void setPropertyForWindow(Object key, Object value);
  
  /**
   * Set a property which has short time life cycle. After the server request all
   * properties will be freed from the jACOB application server.
   * 
   * @param key
   * @param value
   */
  public final void setPropertyForRequest(Object key, Object value)
  {
    if (this.requestCycleProperties == null)
      this.requestCycleProperties = new HashMap();
    
    if(value==null)
      requestCycleProperties.remove(key);
    else
      requestCycleProperties.put(key,value);
  }
  
  public Object getProperty(Object key)
  {
    if (this.requestCycleProperties == null)
      return null;
    return requestCycleProperties.get(key);
  }
  

  /**
   * Set the new status message for a long running process.<br>
   * It's the task of the UI theme to display this message.
   * 
   * @param newMessage
   * @since 2.8.8
   */
  public final void setStatusMessage(String newMessage)
  {
    this.setPropertyForWindow(STATUS_MESSAGE_GUID, newMessage);
  }
  
  /**
   * reset the status message
   * @since 2.8.8
   */
  public final void resetStatusMessage()
  {
    this.setPropertyForWindow(STATUS_MESSAGE_GUID, null);
  }
  
  /**
   * retrieve the status message for a long running process
   * 
   * @since 2.8.8
   * @return
   */
  public final String getStatusMessage()
  {
    return (String)this.getProperty(STATUS_MESSAGE_GUID);
  }
  
  
  /**
   * Return true if the current running context should abort and rollback
   * all done operations. 
   *  
   * @since 2.8.8
   */
  public abstract boolean shouldAbort();
  
  /**
   * Set the flag that all running context related to the window 
   * can be aborted or not (default).
   * 
   * @param flag
   * @since 2.8.8
   */
  public abstract void canAbort(boolean flag);



  /**
   * Returns a valid DataAccessor in the scope of the current ApplicationDefinition.
   * 
   * @return
   */
  public IDataAccessor getDataAccessor()
  {
    if(dataAccessor==null)
      dataAccessor = new DataAccessor(getApplicationDefinition());
    return dataAccessor;
  }
  
  /**
   * Clears all resources occupied by this context
   */
  private void clearResources()
  {
    if (this.requestManagedResources != null)
    {
      duringReleaseResources = true;
      Iterator iter = requestManagedResources.iterator();
      while (iter.hasNext())
      {
        // iter.next sollte ausserhalb des try/catch blockes sein um eine
        // ConcurentModificationException NICHT
        // zu fangen. Diese würde ansonsten in der while-Schleife eine
        // endlos-loop hervorrufen.
        //
        Object obj = iter.next();
        try
        {
          ManagedResource resource = (ManagedResource) obj;
          resource.release();
        }
        catch (RuntimeException e)
        {
          ExceptionHandler.handle(e);
        }
      }
      duringReleaseResources = false;
      requestManagedResources.clear();
    }
    
  	if (this.dataAccessor != null)
    {
      this.dataAccessor.clear();
    }
  	
  	if (this.requestCycleProperties != null)
  	{
  	  this.requestCycleProperties.clear();
  	}
  }

  /**
   * <b>Each</b> context has a assigned IUser.
   * 
   * @return The current logged in user.
   */
  public final IUser getUser()
  {
    return user;
  }
  
  /**
   * Returns the current application definition, if existing.
   * 
   * Remark: Does not exist for bootstrap contextes, i.e. an
   * exception is thrown in this case.
   * 
   * @return the current application definition
   */
  public final IApplicationDefinition getApplicationDefinition()
  {
  	if (null == applicationDefinition)
  		throw new IllegalStateException("No application definition existing");
  	return applicationDefinition;
  }

  /**
	 * Checks whether an application definition has already been bound to this
	 * context.
	 * 
	 * @return <code>true</code> if an application definition exists, otherwise
	 *         <code>false</code>
	 */
	public final boolean hasApplicationDefinition()
	{
		return null != applicationDefinition;
	}

  /**
   * Returns the current data browser for the given name.
   * 
   * @param name
   *          the name of the browser definition
   * @return returns the data browser with the hands over name
   * @see de.tif.jacob.core.definition.IBrowserDefinition
   */
  public final IDataBrowser getDataBrowser(String name)
  {
    return getDataAccessor().getBrowser(name);
  }

  /**
   * Returns the named data table.
   *  
   * @return the desired data table
   * @throws RuntimeException  if no data table with the given name exists
  */
  public final IDataTable getDataTable(String name)
  {
    return getDataAccessor().getTable(name);
  }

  
  /**
   * Internal method - not intended to be called by application programmer!<p>
   * 
   * All the code in the jACOB application server runs within a user Context. The context hold information
   * about the application definition, the current user, database information and a lot of other stuff.<br>
   * The application programmer can use this <b>runtime context</b> for database handling.
   * 
   * Returns The current valid Context.
   */
  public synchronized static void setCurrent(Context context)
  {
    // release all registered ManagedResources in the scope of "Request".
    // if there an *old* context in the thread local
    //
    Context oldContext = (Context) threadLocal.get();
    if (oldContext != null)
    {
      oldContext.clearResources();
    }
    
    threadLocal.set(context);
  }
  
  /**
   * Returns the current user context.
   * 
   * @return the context.
   */
  public synchronized static Context getCurrent()
	{
		// provide a valid Context in all cases
		// The SystemContext contains the System IUser object.
		Context context = (Context) threadLocal.get();
		if (context == null)
		{
			context = new SystemContext();
	    threadLocal.set(context);
		}

		return context;
	}
}
