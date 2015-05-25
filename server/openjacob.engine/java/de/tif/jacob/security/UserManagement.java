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

package de.tif.jacob.security;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.SystemContext;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.AuthenticationException;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.exception.StartupException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.core.model.Activesession;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.deployment.DeployEntry;
import de.tif.jacob.deployment.DeployMain;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.deployment.DeployNotifyee;
import de.tif.jacob.screen.impl.HTTPClientSession;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UserManagement extends BootstrapEntry implements DeployNotifyee
{
  static public final transient String RCS_ID = "$Id: UserManagement.java,v 1.11 2010/07/02 14:43:13 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.11 $";

  private static final Log logger = LogFactory.getLog(UserManagement.class);
  
  private static final HashMap userFactories = new HashMap();

  public static void logOutUser(HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    HTTPClientSession.remove(request);
  }
  
  /**
   * Terminate all user sessions of the given user login id and application name
   * on this cluster node.
   * 
   * @param userLoginId
   * @param applicationName
   * @throws Exception
   */
  public static void logOutUser(String userLoginId, String applicationName) throws Exception
  {
    // remove all active user sessions
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTransaction transaction = accessor.newTransaction();
    try
    {
      IDataTable table = accessor.getTable(Activesession.NAME);
      table.qbeSetKeyValue(Activesession.applicationname, applicationName);
      table.qbeSetKeyValue(Activesession.userid, userLoginId);

      table.searchAndDelete(transaction);
      transaction.commit();

      // und den Sessioncounter korrigieren und propagieren
      //
      for (int i = 0; i < table.recordCount(); i++)
      {
        ClusterManager.decrementUserSessionCount(table.getRecord(i).getStringValue(Activesession.sessionid));
      }
    }
    finally
    {
      transaction.close();
    }
  }
  
  /**
   * retrieve all current active users for an application. The application
   * version is doesn't matter in this call
   * 
   * @param applicationName
   * @return
   * @throws Exception
   * @since 2.9.1
   */
  public static List getActiveUsers(String application, String version) throws Exception
  {
    List result = new ArrayList();
    Set  addedUsers= new HashSet();
    // remove all active user sessions
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable table = accessor.getTable(Activesession.NAME);
    table.qbeSetKeyValue(Activesession.applicationname, application);
    table.qbeSetKeyValue(Activesession.applicationversion, version);

    table.search();

    for(int i=0; i<table.recordCount();i++)
    {
      IDataTableRecord activeUser = table.getRecord(i);
      String userId= activeUser.getSaveStringValue(Activesession.userid);
      if(addedUsers.add(userId))
        result.add(getUser(application, version, userId));
    }
    
    return result;
  }

  /**
   * return true if the client browser has a valid cookie
   * 
   * @param request
   * @param response
   * @return
   * @throws IOException
   */
  public static boolean isLoggedInUser(HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    try
    {
      // we are not logged out, if the corresponding session still exists
      return HTTPClientSession.get(request) != null;
    }
    catch (RuntimeException e)
    {
      
      return false;
    }
  }

  /**
   * Returns the (application) user object given by its user login id. 
   * 
   * @param application the name of the application
   * @param version the application version
   * @param userLoginId the user login id
   * @return the requested user object
   * @throws UserNotExistingException if the user does not exist
   * @throws GeneralSecurityException
   */
  public static IUser getUser(String application, String version, String userLoginId) throws UserNotExistingException, GeneralSecurityException
  {
    return getUserFactory(application, version).findUser(userLoginId);
  }
  
  public static IUser getValid(String application, String version, String userLoginId, String passwd) throws AuthenticationException, GeneralSecurityException
  {
    Context oldContext = Context.getCurrent();
    try
    {
      // Bei dem Aufruf eines Hook in der Application muss immer der Context ordentlich gesetzt sein.
      // Um mit der Datenschicht arbeiten zu kömmen muss als Minimum die ApplicationDefinition vorhanden sein.
      // Grund: Z.B. bei dem Anlegen von initialen Benutzer im Usermangement müssen Tablehooks ordentlich ausgeführt
      // werden. Dies ist nicht immer möglich wenn keine ApplicationDefinition vorhanden ist.
      //
      Context context = new SystemContext(DeployMain.getApplication(application,version));
      Context.setCurrent(context);
      return getUserFactory(application, version).getValid(userLoginId, passwd);
    }
    catch (GeneralSecurityException ex)
    {
      logger.warn("Could not validate user: " + ex.toString());
      throw ex;
    }
    finally
    {
      Context.setCurrent(oldContext);
    }
  }
  
  public static IUserFactory getUserFactory(IApplicationDefinition appDef) throws GeneralSecurityException
  {
    return getUserFactory(appDef.getName(), appDef.getVersion().toString());
  }

  public static IUserFactory getUserFactory(String application, String version) throws GeneralSecurityException
  {
    synchronized (userFactories)
    {
      IUserFactory factory = (IUserFactory) userFactories.get(application + version);
      if (factory == null)
        throw new GeneralSecurityException("No user management found for application [" + application + "-" + version + "]");
      return factory;
    }
  }

  /**
   * Init the UserManagement. 
   * Loads the UserFactory implementation from the common.properties file
   * 
   * @throws StartupException
   */
  public void init() throws Exception
  {
    Log logger = LogFactory.getLog(BootstrapEntry.class);
    // load for all deployed applications the user management implementation
    //
    logger.info("\tloading user managements for deployed applications");
    for (Iterator iter = DeployManager.getDeployEntries().iterator(); iter.hasNext();)
    {
      DeployEntry entry = (DeployEntry) iter.next();
      try
      {
        onDeploy(entry);
      }
      catch (Throwable e)
      {
        // catch throwables to avoid that jACOB could not startup due to an
        // invalid
        // deployed application, e.g. NoClassDefFoundError error for
        // instantiation of
        // a task class!
        logger.error("Could not init user management for application " + entry.getFile(), e);
        DeployManager.setDeployError(entry, e.getMessage());
      }
    }
    DeployManager.registerNotifyee(this);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    // do nothing here
  }

  private static IUserFactory createFactory(DeployEntry dentry) throws Exception
  {
    final String className = "jacob.security.UserFactory";
    logger.info("\tvalidating UserFactory [" + className + "] for application [" + dentry.getName() + "-" + dentry.getVersion() + "]");
    IApplicationDefinition appDef = DeployMain.getApplication(dentry.getName(), dentry.getVersion());
    IUserFactory factory = (IUserFactory) ClassProvider.getInstance(appDef,className);
    if (factory == null)
      throw new Exception("No user management found for application [" + dentry.getName() + "-" + dentry.getVersion() + "]");
    factory.setApplicationDefinition(appDef);
    return factory;
  }
  
  /* 
   * @see de.tif.jacob.deployment.DeployNotifyee#onDeploy(de.tif.jacob.deployment.DeployEntry)
   */
  public void onDeploy(DeployEntry entry) throws Exception
  {
    if (entry.getStatus().isActive())
    {
      synchronized (userFactories)
      {
        userFactories.put(entry.getName() + entry.getVersion(), createFactory(entry));
      }
    }
  }
  
  public void onUndeploy(DeployEntry entry) throws Exception
  {
    // remove user factory to avoid logins for inactive applications
    synchronized (userFactories)
    {
      userFactories.remove(entry.getName() + entry.getVersion());
    }

    // remove all active user sessions
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTransaction transaction = accessor.newTransaction();
		try
		{
			IDataTable table = accessor.getTable(Activesession.NAME);
			table.qbeSetKeyValue(Activesession.applicationname, entry.getName());
			table.qbeSetKeyValue(Activesession.applicationversion, entry.getVersion().toString());
      
      // Achtung: kein fastDelete, da Hooks ausgeführt werden müssen (Aufgabe 196)
			table.searchAndDelete(transaction);
			transaction.commit();
		}
		finally
		{
		  transaction.close();
		}
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.deployment.DeployNotifyee#beforeRedeploy(de.tif.jacob.deployment.DeployEntry)
   */
  public void beforeRedeploy(DeployEntry oldEntry) throws Exception
  {
    // do nothing here
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.deployment.DeployNotifyee#afterRedeploy(de.tif.jacob.deployment.DeployEntry)
   */
  public void afterRedeploy(DeployEntry newEntry) throws Exception
  {
    if (newEntry.getStatus().isActive())
    {
      onDeploy(newEntry);
    }
    else
    {
      // status might have changed -> kill all user sessions
      onUndeploy(newEntry);
    }
  }
  
  public static IUser getSystemUser()
  {
    return new SystemUser();
  }

  private static class SystemUser implements IUser
  {
    private final Map properties = Collections.synchronizedMap(new HashMap());
    
    private SystemUser()
    {
      // nothing more to do
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#isSystem()
     */
    public boolean isSystem()
    {
      return true;
    }
    
 		public boolean isAnonymous()
    {
      return false;
    }

    /* (non-Javadoc)
		 * @see de.tif.jacob.security.IUser#getEMail()
		 */
		public String getEMail()
		{
			return null;
		}

    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#getCellPhone()
     */
    public String getCellPhone()
    {
      return null;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#getFax()
     */
    public String getFax()
    {
      return null;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#getLocale()
     */
    public Locale getLocale()
    {
      return null;
    }
    
    public int getTimezoneOffset()
    {
      return 0;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#getPhone()
     */
    public String getPhone()
    {
      return null;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#setProperty(java.lang.String, java.lang.Object)
     */
    public void setProperty(String key, Object value)
    {
      properties.put(key, value);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#getProperty(java.lang.String)
     */
    public Object getProperty(String key)
    {
      return properties.get(key);
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#setPassword(java.lang.String)
     */
    public void setPassword(String password) throws InvalidNewPasswordException, Exception
    {
      // do nothing here

    }
		/* (non-Javadoc)
		 * @see de.tif.jacob.security.IUser#getFullName()
		 */
		public String getFullName()
		{
			return "jACOB Systemuser";
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.security.IUser#getKey()
		 */
		public String getKey()
		{
		  // The system user is not a real UserManagement user. Avoid the access to the key of
		  // the user. You can't use the key for search, storing,....
		  //
			throw new RuntimeException("No permission to determine system user key.");
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.security.IUser#getLoginId()
		 */
		public String getLoginId()
		{
			return "systemuser";
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.security.IUser#getRole(java.lang.String)
		 */
		public IRole getRole(String role) throws GeneralSecurityException
		{
			return null;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.security.IUser#hasOneRoleOf(java.util.List)
		 */
		public boolean hasOneRoleOf(List roleNames) throws GeneralSecurityException
		{
			// system user has all roles
			return true;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.security.IUser#hasRole(java.lang.String)
		 */
		public boolean hasRole(String role) throws GeneralSecurityException
		{
      // system user has all roles
      return true;
    }

		/* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#hasRole(de.tif.jacob.security.IRole)
     */
    public boolean hasRole(IRole role) throws GeneralSecurityException
    {
      // system user has all roles
      return true;
    }

    /* (non-Javadoc)
		 * @see de.tif.jacob.security.IUser#getMandatorId()
		 */
		public String getMandatorId()
		{
			return null;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.security.IUser#getRoles()
		 */
		public Iterator getRoles()
		{
      // TODO: deliver all roles of the current usermanagement?
			return Collections.EMPTY_LIST.iterator();
		}
  }
}
