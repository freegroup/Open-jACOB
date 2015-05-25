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
package de.tif.jacob.cluster;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.Service;
import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.report.ReportManager;
import de.tif.jacob.security.UserManagement;
import de.tif.jacob.util.logging.LoggingManager;

/**
 * Cluster manager
 */
public class ClusterManager extends BootstrapEntry
{
  static public final transient String RCS_ID = "$Id: ClusterManager.java,v 1.4 2009/07/30 12:27:35 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  private static final transient Log logger = LogFactory.getLog(ClusterManager.class);
  
  private static final ClusterProvider DEFAULT_PROVIDER = new NoClusterProvider();
  
  /**
   * The node name of this jACOB instance
   */
  private static String nodename;
  
  /**
   * Set of <code>String:sessionid</code>
   * <p>
   * Note: User counts have to be maintained locally, because a new license key
   * could be installed.
   */
  private static final Set activeUserSessions = new HashSet();
  
  /**
   * The one and only cluster provider instance or <code>null</code> if
   * {@link #DEFAULT_PROVIDER} is used.
   */
  private static ClusterProvider instance;
  
  /**
   * Increments the local user session counter and propagates it to all
   * reachable jACOB nodes.
   * 
   * @param sessionId
   *          the new session id
   */
  public static void incrementUserSessionCount(String sessionId)
  {
    int userCount = incrementUserSessionCountInternal(sessionId);
    if (instance != null)
    {
      instance.propagateUserCount(userCount);
    }
  }
  
  private static synchronized int incrementUserSessionCountInternal(String sessionId)
  {
    if (!activeUserSessions.add(sessionId))
    {
      // Nur zur Überprüfung, ob hier der Sessioncounter richtig funktioniert
      // Siehe Aufgabe 196
      logger.warn("Session '" + sessionId + "' already exists");
    }
    return activeUserSessions.size();
  }
  
  /**
   * Decrements the local user session counter and propagates it to all
   * reachable jACOB nodes.
   * 
   * @param sessionId
   *          the destroyed session id
   */
  public static void decrementUserSessionCount(String sessionId)
  {
    int userCount = decrementUserSessionCountInternal(sessionId);
    if (instance != null)
    {
      instance.propagateUserCount(userCount);
    }
  }
  
  private static synchronized int decrementUserSessionCountInternal(String sessionId)
  {
    if (!activeUserSessions.remove(sessionId))
    {
      // Nur zur Überprüfung, ob hier der Sessioncounter richtig funktioniert
      // Siehe Aufgabe 196
      logger.warn("Session '" + sessionId + "' already removed");
    }
    return activeUserSessions.size();
  }
  
  /**
   * Returns the local user session count, i.e. the user session currently
   * active on this node.
   * 
   * @return local user session count
   */
  public static synchronized int getLocalUserSessionCount()
  {
    return activeUserSessions.size();
  }
  
  /**
   * Checks whether the given session id points to a (still) active session.
   * <p>
   * Note: A session becomes inactive if its database presentation is deleted
   * (internal table activeUserSessions).
   * 
   * @param sessionId
   *          the session id to check
   * @return <code>true</code> the session is valid otherwise
   *         <code>false</code>
   */
  public static synchronized boolean isActiveUserSession(String sessionId)
  {
    return activeUserSessions.contains(sessionId);
  }
  
  /**
   * Determines the total user session count of all nodes using the same license
   * key.
   * 
   * @return total user session count
   * @throws Exception
   *           on any problem
   */
  public static int getUserSessionCount() throws Exception
  {
    if (instance != null)
    {
      int userSessions = instance.getUserSessionCount();
      if (userSessions >= 0)
        return userSessions;
    }
    return getLocalUserSessionCount();
  }
  
  /**
   * Gets the cluster implementation provider.
   * 
   * @return cluster provider
   */
  public static ClusterProvider getProvider()
  {
    // if no cluster implementation has been found -> return default
    if (instance == null)
      return DEFAULT_PROVIDER;
    
    return instance;
  }
  
  /**
   * Returns the node name of this jACOB application server instance.
   * <p>
   * It is guaranteed that the node name keeps the same in case of a simple
   * restart of the jACOB application server or its hosting web server. A simple
   * restart does not involve major configuration changes such like changing the
   * port, the hostname, the name of jACOB web application context (per default
   * this is "jacob") or moving the web server to a different directory.
   * 
   * @return the node name of this jACOB application server instance.
   */
  public synchronized static String getNodeName()
  {
    String applicationRootPath = Bootstrap.getApplicationRootPath();
    if (null == applicationRootPath)
      return "SingleNode";
//      throw new IllegalStateException("Node name can not be determined so far");

    if (nodename == null)
    {
      try
      {
        // build node name as 
        // <hostname>:<applicationRootPath>
        // Example: djoser:J:\jakarta-tomcat-5.0.28\webapps\jacob\
        //
        // Note: For other web servers this implementation might change
        //
        nodename = InetAddress.getLocalHost().getHostName() + ":" + applicationRootPath;
      }
      catch (UnknownHostException ex)
      {
        throw new RuntimeException("Unknown local host (assign ip address in hosts file to your local host's name)", ex);
      }
    }
    return nodename;
  }

  /**
   * Checks whether this node is the cluster coordinator.
   * 
   * @return <code>true</code> node is the coordinator, otherwise
   *         <code>false</code>
   */
  public static boolean isCoordinator()
  {
    // if clustering is switched off, this (one and only) node is always
    // the coordinator
    //
    if (instance == null)
      return true;

    return instance.isCoordinator();
  }
  
  public void init() throws Throwable
  {
    // Search for cluster implementations
    //
    Iterator iter = Service.providers(ClusterProvider.class);
    if (iter.hasNext())
    {
      ClusterProvider provider = (ClusterProvider) iter.next();
      if (provider.init())
        instance = provider;
      
      if (iter.hasNext())
      {
        ClusterProvider provider2 = (ClusterProvider) iter.next();
        throw new Exception("Multiple cluster implementations existing: '" + provider.getClass() + "' and '" + provider2.getClass() + "'");
      }
    }
    
    if (instance != null)
    {
      String initError = instance.getInitializationError();
      if (initError == null)
        logger.info("Clustering is enabled: Using provider '" + instance.getClass() + "'");
      else
        logger.warn("Clustering is not enabled: " + initError);
    }
    else
    {
      logger.info("Clustering is not enabled: No provider existing");
    }
  }
  
  public boolean hasWarnings()
  {
    return getInitializationError() != null;
  }

  public void destroy() throws Throwable
  {
    if (instance != null)
      instance.close();
  }

  public static void main(String[] args)
  {
    try
    {
      ClusterManager c = new ClusterManager();
      c.init();
      System.out.println("isCoordinator:"+isCoordinator());
      Thread.sleep(300000);
      System.out.println("isCoordinator:"+isCoordinator());
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
  }
  
  /**
   * Determines if clustering is enabled.
   * 
   * @return <code>true</code> if enabled otherwise <code>false</code>
   */
  public static boolean isEnabled()
  {
    return instance != null;
  }
  
  /**
   * Returns the possible initialization error of the cluster implementation.
   * 
   * @return cluster initialization error or <code>null</code>, if no error
   *         exists or clustering is not enabled
   */
  public static String getInitializationError()
  {
    if (instance != null)
      instance.getInitializationError();
    return null;
  }

  /**
   * Default cluster implementation which means no cluster support.
   * 
   * @author Andreas Sonntag
   */
  private static class NoClusterProvider extends ClusterProvider
  {
    static public final transient String RCS_ID = "$Id: ClusterManager.java,v 1.4 2009/07/30 12:27:35 ibissw Exp $";
    static public final transient String RCS_REV = "$Revision: 1.4 $";

    protected boolean init() throws Throwable
    {
      // nothing to do here
      return true;
    }

    protected String getInitializationError()
    {
      // no warnings existing
      return null;
    }

    protected void close() throws Throwable
    {
      // nothing to do here
    }

    protected boolean isCoordinator()
    {
      // always
      return true;
    }

    protected int getUserSessionCount()
    {
      return -1;
    }

    protected void propagateUserCount(int userCount)
    {
      // nothing to do here
    }

    public void propagatePropertyChange(String propertyName) throws Exception
    {
      Property property = Property.getProperty(propertyName);
      if (null != property)
      {
        property.refresh();
      }
    }
    
    public void notifyCheckApplications()
    {
      // do not do anything here, since local deploy operations are immediatly performed
    }

    public void notifyUndeployApplication(String appName, String version) throws Exception
    {
      DeployManager.undeploy(appName, version);
    }

    public void notifyTaskStatusChanged(String applicationname, String taskname, String taskstatus) throws Exception
    {
      // do not do anything here, since we have already done everything locally
    }

    public void notifyTerminateUserSessions(String applicationname, String userloginid) throws Exception
    {
      UserManagement.logOutUser(userloginid, applicationname);
    }

    public void propagateDatasourceChange(String datasourceName) throws Exception
    {
      DataSource.destroy(datasourceName);
    }

    public void propagateScheduledReportChange(String guid) throws Exception
    {
      ReportManager.restartReportScheduler(guid);
    }

    public void propagateLicenseChange() throws Exception
    {
      // do not do anything here, since license is already processed locally
    }

    public void propagateLoggingChange() throws Exception
    {
      LoggingManager.readConfiguration();
    }

    public List getNodeUrls()
    {
      return Collections.EMPTY_LIST;
    }

    public void propagateNodeUrl(String url) throws Exception
    {
      // nothing to do here
    }

    public void fetchClusterNodeInfo(IDataTable clusterNodeTable, IDataTransaction trans) throws Exception
    {
      // do nothing here
    }

    public void fetchLicenseNodeInfo(IDataTable licenseNodeTable, IDataTransaction trans) throws Exception
    {
      IDataTableRecord licenseNodeRecord = licenseNodeTable.newRecord(trans);
      licenseNodeRecord.setValue(trans, "id", "This node");
      licenseNodeRecord.setValue(trans, "name", ClusterManager.getNodeName());
      licenseNodeRecord.setValue(trans, "activesince", Bootstrap.getActiveSince());
      licenseNodeRecord.setIntValue(trans, "usersessions", ClusterManager.getLocalUserSessionCount());
    }

    public IDataTableRecord resetConfigurationToFactory() throws Exception
    {
      // not supported
      return null;
    }
  }
}

