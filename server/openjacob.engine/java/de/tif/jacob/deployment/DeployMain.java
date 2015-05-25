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

package de.tif.jacob.deployment;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Property;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.config.IConfig;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IApplicationProvider;
import de.tif.jacob.core.exception.MissingPropertyException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.i18n.CoreMessage;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class DeployMain
{
  static public final transient String RCS_ID = "$Id: DeployMain.java,v 1.6 2009/04/21 15:14:47 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";

  static private final transient Log logger = LogFactory.getLog(DeployMain.class);
  
  private final static Class PROVIDER_ARGS[] = { IConfig.class };
  private final static String PROVIDER_PROPERTY = "application.provider";
  
  /**
   * Map{String:applicationame->Map{Version->IApplicationDefinition}}
   */
  private final static Map registeredApplications = new HashMap();
  
  /**
   * Returns the default application definition, if existing.
   * <p>
   * Note: The default application can be configured by means of <code>Property.APPLICATION_DEFAULT</code>. In case
   * this property is set, the definition of the highest released version will be returned.
   * 
   * @return the default application definition or <code>null</code>
   */
  public static IApplicationDefinition getDefaultApplication()
  {
    String defaultApplication = Property.APPLICATION_DEFAULT.getValue();
    if (defaultApplication != null)
    {
      try
      {
        return getActiveApplication(defaultApplication);
      }
      catch (Exception ex)
      {
        if (logger.isWarnEnabled())
        {
          logger.warn("Default application '" + defaultApplication + "' cannot be accessed.");
        }
      }
    }

    return null;
  }
  
  /**
   * Returns the active application definition specified by name and version.
   * <p>
   * 
   * @param applicationName
   *          name of the application
   * @param versionString
   *          the version of the application
   * @return the application definition of the given name and version
   * @throws RuntimeException
   *           if no active application with the given name and version exists
   *           or if the respective application version is not active
   * @since 2.8
   */
  public static IApplicationDefinition getActiveApplication(String applicationName, String versionString) throws RuntimeException
  {
    synchronized (registeredApplications)
    {
      Map versionMap = (Map) registeredApplications.get(applicationName);
      if (null == versionMap)
      {
        throw new RuntimeException("No application '" + applicationName + "' existing.");
      }

      Version version = Version.parseVersion(versionString);
      IApplicationDefinition applDef = (IApplicationDefinition)versionMap.get(version);
      if (applDef == null)
      {
        throw new RuntimeException("No active application '" + applicationName + "' existing.");
      }
      DeployEntry entry = DeployManager.getDeployEntry(applDef);
      if (entry == null || !entry.getStatus().isActive())
        throw new RuntimeException("No active application '" + applicationName + "' existing.");
      return applDef;
    }
  }

  /**
   * Returns the most recent active application definition specified by name.
   * <p>
   * @param applicationName
   *          name of the application
   * @return the application definition of the given name
   * @throws RuntimeException
   *           if no active application with the given name exists
   */
  public static IApplicationDefinition getActiveApplication(String applicationName) throws RuntimeException
  {
    synchronized (registeredApplications)
    {
      Map versionMap = (Map) registeredApplications.get(applicationName);
      if (null == versionMap)
      {
        throw new RuntimeException("No application '" + applicationName + "' existing.");
      }

      // find highest active version
      Iterator iter = versionMap.keySet().iterator();
      Version highest = null;
      while (iter.hasNext())
      {
        Version version = (Version) iter.next();
        if (highest == null || version.compareTo(highest) > 0)
        {
          IApplicationDefinition applDef = (IApplicationDefinition) versionMap.get(version);
          DeployEntry entry = DeployManager.getDeployEntry(applDef);
          if (entry != null && entry.getStatus().isActive())
            highest = version;
        }
      }
      if (highest == null)
      {
        throw new RuntimeException("No active application '" + applicationName + "' existing.");
      }
      return (IApplicationDefinition) versionMap.get(highest);
    }
  }
  
  /**
   * Returns the active application definitions specified by name.
   * <p>
   * @param applicationName
   *          name of the application
   * @return List of active application definitions of the given name
   * @since 2.8.5
   */
  public static List getActiveApplications(String applicationName) throws RuntimeException
  {
    synchronized (registeredApplications)
    {
      List result = new ArrayList();
      Map versionMap = (Map) registeredApplications.get(applicationName);
      if (null == versionMap)
      {
        return result;
      }

      // find all active version
      Iterator iter = versionMap.keySet().iterator();
      while (iter.hasNext())
      {
        Version version = (Version) iter.next();
        IApplicationDefinition applDef = (IApplicationDefinition) versionMap.get(version);
        DeployEntry entry = DeployManager.getDeployEntry(applDef);
        if (entry != null && entry.getStatus().isActive())
          result.add(applDef);
      }
      return result;
    }
  }
  
  /**
   * Checks whether an active application exists.
   * <p>
   * @param applicationName
   *          name of the application
   * @return <code>true</code> if an active application exists, otherwise <code>false</code>
   * @since 2.8.5 
   */
  public static boolean hasActiveApplication(String applicationName)
  {
    synchronized (registeredApplications)
    {
      Map versionMap = (Map) registeredApplications.get(applicationName);
      if (null == versionMap)
      {
        return false;
      }

      Iterator iter = versionMap.values().iterator();
      while (iter.hasNext())
      {
        IApplicationDefinition applDef = (IApplicationDefinition) iter.next();
        DeployEntry entry = DeployManager.getDeployEntry(applDef);
        if (entry != null && entry.getStatus().isActive())
          return true;
      }
      return false;
    }
  }
  
  /**
   * Returns the application definition specified by name and version.
   * <p>
   * @param applicationName name of the application
   * @param version the version of the application
   * @return the application definition of the given name
   * @throws RuntimeException if no application with the given name and version exists
   */
  public static IApplicationDefinition getApplication(String applicationName, String version) throws RuntimeException
	{
    // This method is called from login page
    // check whether an application has been selected!
    //
    if (applicationName == null || applicationName.length()==0 || version==null || version.length()==0)
      throw new UserRuntimeException(new CoreMessage(CoreMessage.MISSING_APPLICATION_VERSION));
    
  	return getApplication(applicationName, Version.parseVersion(version));
  }
  
  /**
   * Returns the application definition specified by name and version.
   * <p>
   * @param applicationName name of the application
   * @param version the version of the application
   * @return the application definition of the given name
   * @throws RuntimeException if no application with the given name and version exists
   */
  public static IApplicationDefinition getApplication(String applicationName, Version version) throws RuntimeException
  {
    synchronized (registeredApplications)
    {
      Map versionMap = (Map) registeredApplications.get(applicationName);
      if (null == versionMap)
      {
        throw new RuntimeException("No such application existing: "+applicationName);
      }
      IApplicationDefinition definition = (IApplicationDefinition) versionMap.get(version);
      if (null == definition)
      {
        throw new RuntimeException("No such application version existing: "+applicationName+"-"+version);
      }
      return definition;
    }
  }
  
  private static IConfig loadConfig(String applicationName, String version) throws Exception
  {
    final String clazzName = "jacob.config.Config";
    
    logger.info("Loading configuration: "+clazzName);
    
    IConfig config = (IConfig) ClassProvider.getInstance(applicationName, version, clazzName);
    if (config == null)
    {
      throw new Exception("Could not access configuration: "+clazzName);
    }
    return config;
  }
  
  /**
   * @param config
   * @return
   * @throws Exception
   */
  private static IApplicationProvider loadApplicationProvider(IConfig config) throws Exception
  {
    String providerClassName = config.getProperty(PROVIDER_PROPERTY);
    

    if (providerClassName == null)
    {
      throw new MissingPropertyException("Undefined application provider", PROVIDER_PROPERTY);
    }
    
    if (logger.isInfoEnabled())
      logger.info("Loading provider: "+providerClassName);
    
    Class clazz = Class.forName(providerClassName);
    Constructor constructor = clazz.getConstructor(PROVIDER_ARGS);
    return (IApplicationProvider) constructor.newInstance(new Object[] { config });
  }

  public static IApplicationProvider getApplicationProvider(String applicationName, String version) throws Exception
  {
    IConfig config = loadConfig(applicationName, version);
    return loadApplicationProvider(config);
  }
  
  /**
   * The callback method if an new/current application will be deployed/refreshed
   * in the jACOB server.
   * 
   * Create all deploy/application entries.
   */
  protected static IApplicationDefinition loadApplicationDefinition(DeployEntry entry) throws Exception
	{
		if (logger.isInfoEnabled())
			logger.info("Loading application: " + entry);

		// load application definition
		IConfig config = loadConfig(entry.getName(), entry.getVersion().toString());
		IApplicationProvider provider = loadApplicationProvider(config);
		IApplicationDefinition applDefinition = provider.getApplication(entry.getName(), entry.getVersion());

		// and register internally
		synchronized (registeredApplications)
		{
			Map versionMap = (Map) registeredApplications.get(entry.getName());
			if (null == versionMap)
			{
        // use TreeMap to do not distinguish version fix number
        versionMap = new TreeMap(new Comparator()
        {
          public int compare(Object arg0, Object arg1)
          {
            Version vers0 = (Version) arg0;
            Version vers1 = (Version) arg1;
            if (vers0.getMajor() != vers1.getMajor())
              return vers0.getMajor() - vers1.getMajor();
            return vers0.getMinor() - vers1.getMinor();
          }
        });
				registeredApplications.put(entry.getName(), versionMap);
			}
			versionMap.put(entry.getVersion(), applDefinition);
		}

		if (logger.isDebugEnabled())
			logger.debug("Loading application finished: " + entry);
		
		return applDefinition;
	}
  
  /**
   * Unregister application definition of deploy entry
   * 
   * @param entry
   *          deploy entry
   */
  protected static void unregisterApplicationDefinition(DeployEntry entry)
  {
    if (logger.isInfoEnabled())
      logger.info("Unregister application: " + entry);

    // unregister internally
    synchronized (registeredApplications)
    {
      Map versionMap = (Map) registeredApplications.get(entry.getName());
      if (null != versionMap)
      {
        versionMap.remove(entry.getVersion());

        // last entry removed?
        if (versionMap.size() == 0)
        {
          // remove version map itself
          registeredApplications.remove(entry.getName());
        }
      }
    }
  }
}
