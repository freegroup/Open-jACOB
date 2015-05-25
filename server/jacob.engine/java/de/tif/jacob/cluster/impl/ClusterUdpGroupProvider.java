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

package de.tif.jacob.cluster.impl;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.cluster.ClusterProvider;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.definition.fieldtypes.TimestampResolution;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.model.Clusterconfig;
import de.tif.jacob.core.model.Clusternode;
import de.tif.jacob.core.model.Licensenode;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.license.License;
import de.tif.jacob.license.LicenseException;
import de.tif.jacob.license.LicenseFactory;
import de.tif.jacob.report.ReportManager;
import de.tif.jacob.scheduler.Scheduler;
import de.tif.jacob.util.logging.LoggingManager;

/**
 * Class for UDP group cluster implementation.
 * 
 * @author Andreas Sonntag
 */
public class ClusterUdpGroupProvider extends ClusterProvider
{
  static public final transient String RCS_ID = "$Id: ClusterUdpGroupProvider.java,v 1.5 2010-07-07 14:09:02 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";
  
  private static final transient Log logger = LogFactory.getLog(ClusterUdpGroupProvider.class);
  
  private static final String SERVER_URL_PREFIX = "serverUrl:";
  private static final String USER_COUNT_PREFIX = "userCount:";
  
  private static final String SCHEDULED_REPORT_CHANGE_MSG_TYPE = "scheduledReportChange";
  private static final String LICENSE_CHANGE_MSG_TYPE = "licenseChange";
  private static final String LOGGING_CHANGE_MSG_TYPE = "loggingChange";
  private static final String PROPERTY_CHANGE_MSG_TYPE = "propertyChange";
  private static final String DATASOURCE_CHANGE_MSG_TYPE = "datasourceChange";
  private static final String APPLICATIONS_CHECK_MSG_TYPE = "applicationsCheck";
  private static final String UNDEPLOY_APPLICATION_MSG_TYPE = "undeployApplication";
  private static final String TASK_STATUS_CHANGE_MSG_TYPE = "taskStatusChange";
  private static final String TERMINATE_USER_SESSIONS_MSG_TYPE = "terminateUserSessions";
    
  private static final String MCAST_ADDR_WILDCARD = "{MCAST_ADDR}";
  private static final String MCAST_PORT_WILDCARD = "{MCAST_PORT}";
  
  private String initializationError;
  
  private Group licenseGroup;
  
  private Group clusterGroup;
  
  private long licenseHashCode;
  
  /**
   * Instances of this class are used to propagate the current user session
   * count of a node. Together with the counter the license id is propagated as
   * well.
   * 
   * @author Andreas Sonntag
   */
  private static class UserSessionCount implements java.io.Serializable
  {
    /** use serialVersionUID for interoperability */
    private static final long serialVersionUID = -6565832497536512353L;
    
    private final int count;
    private final long licenseHashCode;
    
    private UserSessionCount(long licenseHashCode, int count)
    {
      this.count = count;
      this.licenseHashCode = licenseHashCode;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return this.licenseHashCode + ":" + count;
    }
  }
  
  /**
   * Class to exchange data for {@link ClusterUdpGroupProvider#UNDEPLOY_APPLICATION_MSG_TYPE}.
   * 
   * @author Andreas Sonntag
   */
  private static class ApplicationVersion implements java.io.Serializable
  {
    /** use serialVersionUID for interoperability */
    private static final long serialVersionUID = -5658324975365123536L;
    
    private final String appName;
    private final String version;
    
    private ApplicationVersion(String appName, String version)
    {
      this.appName = appName;
      this.version = version;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return this.appName + "-" + version;
    }
  }
  
  /**
   * Class to exchange data for {@link ClusterUdpGroupProvider#TASK_STATUS_CHANGE_MSG_TYPE}.
   * 
   * @author Andreas Sonntag
   */
  private static class TaskStatus implements java.io.Serializable
  {
    /** use serialVersionUID for interoperability */
    private static final long serialVersionUID = -832497536512353619L;
    
    private final String appName;
    private final String taskName;
    private final String taskStatus;
    
    private TaskStatus(String appName, String taskName, String taskStatus)
    {
      this.appName = appName;
      this.taskName = taskName;
      this.taskStatus = taskStatus;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return this.appName + "." + this.taskName + ":" + this.taskStatus;
    }
  }
  
  /**
   * Class to exchange data for {@link ClusterUdpGroupProvider#TERMINATE_USER_SESSIONS_MSG_TYPE}.
   * 
   * @author Andreas Sonntag
   */
  private static class ApplicationUser implements java.io.Serializable
  {
    /** use serialVersionUID for interoperability */
    private static final long serialVersionUID = -832497536512353620L;
    
    private final String appName;
    private final String userLoginId;
    
    private ApplicationUser(String appName, String userLoginId)
    {
      this.appName = appName;
      this.userLoginId = userLoginId;
    }
    
    public String toString()
    {
      return this.userLoginId + "@" + this.appName;
    }
  }
  
  public boolean init() throws Throwable
  {
    boolean result = false;
    
    // get the configuration for this cluster from the jACOB configuration
    // datasource
    IDataTableRecord configRecord = getConfigurationRecord(false);

    try
    {
      // create cluster group
      //
      String channelName = "jACOB:cluster:" + configRecord.getStringValue("clustername");
      this.clusterGroup = new Group(channelName, configRecord.getStringValue("clustergroup"));

      // register property change message listener
      //
      this.clusterGroup.register(PROPERTY_CHANGE_MSG_TYPE, new IGroupMessageListener()
      {
        public void receive(Serializable msg)
        {
          Property property = Property.getProperty((String) msg);
          if (null != property)
          {
            property.refresh();
          }
        }
      }, true);

      // register datasource change message listener
      //
      this.clusterGroup.register(DATASOURCE_CHANGE_MSG_TYPE, new IGroupMessageListener()
      {
        public void receive(Serializable msg) throws Exception
        {
          DataSource.destroy((String) msg);
        }
      }, true);

      // register message listener of checking installed applications
      //
      this.clusterGroup.register(APPLICATIONS_CHECK_MSG_TYPE, new IGroupMessageListener()
      {
        public void receive(Serializable msg) throws Exception
        {
          // simply check for new changes
          DeployManager.checkInstalledApplicationUpdates();
        }
      }, false);

      // register message listener to undeploy application
      //
      this.clusterGroup.register(UNDEPLOY_APPLICATION_MSG_TYPE, new IGroupMessageListener()
      {
        public void receive(Serializable msg) throws Exception
        {
          ApplicationVersion appVers = (ApplicationVersion) msg;
          DeployManager.undeploy(appVers.appName, appVers.version);
        }
      }, true);

      // register message listener of checking license changes
      //
      this.clusterGroup.register(LICENSE_CHANGE_MSG_TYPE, new IGroupMessageListener()
      {
        public void receive(Serializable msg) throws Exception
        {
          refreshLicense();
        }
      }, true);

      // register message listener of logging configuration changes
      //
      this.clusterGroup.register(LOGGING_CHANGE_MSG_TYPE, new IGroupMessageListener()
      {
        public void receive(Serializable msg) throws Exception
        {
          LoggingManager.readConfiguration();
        }
      }, true);

      // register message listener of scheduled report changes
      //
      this.clusterGroup.register(SCHEDULED_REPORT_CHANGE_MSG_TYPE, new IGroupMessageListener()
      {
        public void receive(Serializable msg) throws Exception
        {
          ReportManager.restartReportScheduler((String) msg);
        }
      }, true);

      // register message listener for task status changes
      //
      this.clusterGroup.register(TASK_STATUS_CHANGE_MSG_TYPE, new IGroupMessageListener()
      {
        public void receive(Serializable msg) throws Exception
        {
          TaskStatus taskStatus = (TaskStatus) msg;
          Scheduler.updateTaskStatus(taskStatus.appName, taskStatus.taskName, taskStatus.taskStatus);
        }
      }, false);

      // after all listeners have been installed -> start the group
      this.clusterGroup.start();

      // return true, if cluster group has been successfully started
      // Note: Never mind license group!
      result = true;
    }
    catch (Exception ex)
    {
      initializationError = ex.toString();
      logger.warn("Could not enable clustering", ex);

      // do not rethrow exception because we want to run
      // even if clustering could not be started
    }

    // create license group
    //
    try
    {
      License license = LicenseFactory.getLicenseManager().fetchLicense();
      initLicenseGroup(license, configRecord);
    }
    catch (LicenseException ex)
    {
      logger.warn("No valid license existing: " + ex.toString());
    }
    
    return result;
  }
  
  protected String getInitializationError()
  {
    // no warnings existing
    return this.initializationError;
  }

  public void close() throws Throwable
  {
    if (this.licenseGroup != null)
      this.licenseGroup.close();
    
    if (this.clusterGroup != null)
      this.clusterGroup.close();
  }
  
  public synchronized int getUserSessionCount()
  {
    if (this.licenseGroup != null)
    {
      List userCountList = this.licenseGroup.getValues(USER_COUNT_PREFIX);
      if (null != userCountList)
      {
        int totalUserCount = 0;
        for (int i = 0; i < userCountList.size(); i++)
        {
          UserSessionCount userCount = (UserSessionCount) userCountList.get(i);
          
          // only count users of the same license
          if (this.licenseHashCode == userCount.licenseHashCode)
          {
            totalUserCount += userCount.count;
          }
        }
        return totalUserCount;
      }
    }
    return -1;
  }
  
  /**
   * Refreshes the license used by this node, i.e. the license is refetched from
   * persistent store and processed accordingly.
   * 
   * @throws Exception on any problem
   */
  private synchronized void refreshLicense() throws Exception
  {
    // load license from physical store again
    License license = LicenseFactory.getLicenseManager().fetchLicense();

    if (license != null)
    {
      long licenseHashCode = license.getLicenseNbr();
      if (this.licenseGroup != null && this.licenseHashCode == licenseHashCode)
      {
        // nothing to do, i.e. all up to date
        return;
      }
    }

    // close current group
    //
    if (this.licenseGroup != null)
    {
      this.licenseGroup.close();
      this.licenseGroup = null;
      this.licenseHashCode = 0;
    }

    // open new group
    initLicenseGroup(license, getConfigurationRecord(false));
  }
  
  /**
   * Initialise license group, which is named global group just to confuse the
   * Russians :-) <br>
   * The license group contains all members using the same license key
   * (regardless of cluster membership) in the same subnet.
   * <p>
   * Note: We do not like to have a real global group, because nodes (using
   * different license) which are not running within the same cluster should
   * influence each other at a minimum!!! For example a test system should have
   * no influence (worthy of mention) to a productive system.
   * 
   * @param license
   * @param configRecord
   */
  private void initLicenseGroup(License license, IDataTableRecord configRecord)
  {
    if (license != null)
    {
      try
      {
        this.licenseHashCode = license.getLicenseNbr();

        String props = configRecord.getStringValue(Clusterconfig.globalgroup);
        // generate port in the range 35000..44999 to avoid warnings of jgroup
        // if
        // different channels are using the same IP address and port.
        // Note: the port should be identical for equal license keys and (most
        // likely)
        // not identical for different license keys!
        int licenseHashCodePort = 35000 + (int) (licenseHashCode % 10000);
        // replace wildcard (if existing) by generated port
        props = StringUtils.replace(props, MCAST_PORT_WILDCARD, Integer.toString(licenseHashCodePort));
        this.licenseGroup = new Group("jACOB:global", props);
        this.licenseGroup.start();
      }
      catch (Exception ex)
      {
        this.licenseGroup = null;
        logger.error("Could not init global group", ex);
      }
      
      // and of course propagate user count
      propagateUserCount(ClusterManager.getLocalUserSessionCount());
    }
  }
  
  protected boolean isCoordinator()
  {
    return this.clusterGroup.isCoordinator();
  }
  
  protected synchronized void propagateUserCount(int userCount)
  {
    // Die Anzahl der Benutzer ist an die Lizenz gebunden.
    // Es wird nur für diesen Knoten und Lizenz ein Eintrag in der allgemeinen
    // Hashtable gemacht. 
    // Später werden alle Einträge der Knoten zu einer Lizenz addiert. So spart man 
    // sich die Transaktionen. Man kann nicht nur für die Lizenz einen Eintrag machen, da dieser
    // von verschiedenen Knoten modifiziert werden kann.
    //
    try
    {
      if (this.licenseGroup != null)
      {
        this.licenseGroup.setValue(USER_COUNT_PREFIX, new UserSessionCount(this.licenseHashCode, userCount));
      }
    }
    catch (Exception ex)
    {
      logger.warn("Could not propagate user count", ex);
    }
  }
  
  public void propagatePropertyChange(String propertyName) throws Exception
  {
      this.clusterGroup.sendMessage(PROPERTY_CHANGE_MSG_TYPE, propertyName);
  }
  
  public void propagateDatasourceChange(String datasourceName) throws Exception
  {
      this.clusterGroup.sendMessage(DATASOURCE_CHANGE_MSG_TYPE, datasourceName);
  }
  
  public void propagateScheduledReportChange(String guid) throws Exception
  {
      this.clusterGroup.sendMessage(SCHEDULED_REPORT_CHANGE_MSG_TYPE, guid);
  }
  
  public void propagateLicenseChange() throws Exception
  {
      this.clusterGroup.sendMessage(LICENSE_CHANGE_MSG_TYPE, null);
  }
  
  public void propagateLoggingChange() throws Exception
  {
      this.clusterGroup.sendMessage(LOGGING_CHANGE_MSG_TYPE, null);
  }
  
  public List getNodeUrls()
  {
    List result = this.clusterGroup.getValues(SERVER_URL_PREFIX);
    if (null != result)
      return result;
    return Collections.EMPTY_LIST;
  }

  public void propagateNodeUrl(String url) throws Exception
  {
    this.clusterGroup.setValue(SERVER_URL_PREFIX, url);
  }

  public void notifyCheckApplications() throws Exception
  {
    this.clusterGroup.sendMessage(APPLICATIONS_CHECK_MSG_TYPE, null);
  }

  public void notifyUndeployApplication(String appName, String version) throws Exception
  {
    this.clusterGroup.sendMessage(UNDEPLOY_APPLICATION_MSG_TYPE, new ApplicationVersion(appName, version));
  }

  public void notifyTaskStatusChanged(String applicationname, String taskname, String taskstatus) throws Exception
  {
    this.clusterGroup.sendMessage(TASK_STATUS_CHANGE_MSG_TYPE, new TaskStatus(applicationname, taskname, taskstatus));
  }
  
  public void notifyTerminateUserSessions(String applicationname, String userloginid) throws Exception
  {
    this.clusterGroup.sendMessage(TERMINATE_USER_SESSIONS_MSG_TYPE, new ApplicationUser(applicationname, userloginid));
  }

  public void fetchClusterNodeInfo(IDataTable clusterNodeTable, IDataTransaction trans) throws Exception
  {
    List members = this.clusterGroup.getMembers();
    if (members != null)
    {
      for (int i = 0; i < members.size(); i++)
      {
        Group.Member member = (Group.Member) members.get(i);

        IDataTableRecord clusterNodeRecord = clusterNodeTable.newRecord(trans);
        clusterNodeRecord.setValue(trans, Clusternode.id, member.getId());
        clusterNodeRecord.setValue(trans, Clusternode.name, member.getNodeName());
        clusterNodeRecord.setValue(trans, Clusternode.activesince, member.since());
        clusterNodeRecord.setValue(trans, Clusternode.coordinator, member.isCoordinator() ? Clusternode.coordinator_ENUM._Yes : Clusternode.coordinator_ENUM._No);
        clusterNodeRecord.setValue(trans, Clusternode.serverurl, clusterGroup.getValue(SERVER_URL_PREFIX, member));
      }
    }
  }

  public synchronized void fetchLicenseNodeInfo(IDataTable licenseNodeTable, IDataTransaction trans) throws Exception
  {
    if (this.licenseGroup != null)
    {
      List members = this.licenseGroup.getMembers();
      if (members != null)
      {
        for (int i = 0; i < members.size(); i++)
        {
          Group.Member member = (Group.Member) members.get(i);
          UserSessionCount userCount = (UserSessionCount) this.licenseGroup.getValue(USER_COUNT_PREFIX, member);

          // only show nodes using the same license hash
          if (userCount != null && this.licenseHashCode == userCount.licenseHashCode)
          {
            IDataTableRecord licenseNodeRecord = licenseNodeTable.newRecord(trans);
            licenseNodeRecord.setValue(trans, Licensenode.id, member.getId());
            licenseNodeRecord.setValue(trans, Licensenode.name, member.getNodeName());
            licenseNodeRecord.setValue(trans, Licensenode.activesince, member.since());
            licenseNodeRecord.setIntValue(trans, Licensenode.usersessions, userCount.count);
          }
        }
      }
    }
  }
  
  /**
   * Fetches the jgroup configuration for the given multicast address and port.
   * 
   * @param mcast_addr
   *          multicast address
   * @param mcast_port
   *          multicast port
   * @return the desired jgroup xml configuration
   * @throws Exception
   *           on any problem
   */
  protected static String getJGroupProperties(String mcast_addr, String mcast_port) throws Exception
  {
    String filename = "UDP.xml";
    InputStream inputStream = ClusterUdpGroupProvider.class.getResourceAsStream(filename);
    if (null == inputStream)
    {
      throw new Exception("Could not open file '" + filename + "' as stream");
    }
    try
    {
      // IBIS: Avoid fixed encoding
      String props = IOUtils.toString(inputStream, "ISO-8859-1");
      if (mcast_addr != null)
        props = StringUtils.replace(props, MCAST_ADDR_WILDCARD, mcast_addr);
      if (mcast_port != null)
        props = StringUtils.replace(props, MCAST_PORT_WILDCARD, mcast_port);
      return props;
    }
    finally
    {             
      IOUtils.closeQuietly(inputStream);
    }
  }
  
  public IDataTableRecord resetConfigurationToFactory() throws Exception
  {
    return getConfigurationRecord(true);
  }
  
  /**
   * Builds a default cluster name in the following form:
   * <p>
   * "jACOB yyyy-MM-dd HH:mm:ss"
   * 
   * @return default cluster name
   */
  private static String buildDefaultClusterName()
  {
    return "jACOB " + TimestampResolution.SEC_BASE.toString(new Timestamp(System.currentTimeMillis()), null);
  }
  
  /**
   * Primary key for cluster configuration record.
   */
  private static final String CONFIG_NAME = "CONFIGURATION"; 
  
  /**
   * Fetches the cluster configuration record from the jACOB admin data source.
   * If no one exists so far, create one.
   * 
   * @param resetConfiguration
   *          if <code>true</code> configuration record will be overwritten
   *          with factory default, if existing.
   * @return cluster configuration record
   * @throws Exception
   *           on any problem
   */
  private static IDataTableRecord getConfigurationRecord(boolean resetConfiguration) throws Exception
  {
    IDataAccessor accessor = new DataAccessor(AdminApplicationProvider.getApplication());
    IDataTable clusterconfigTable = accessor.getTable(Clusterconfig.NAME);
    clusterconfigTable.setMaxRecords(1);
    clusterconfigTable.qbeSetKeyValue(Clusterconfig.configname, CONFIG_NAME);
    clusterconfigTable.search();
    IDataTableRecord clusterconfigRecord;
    if (clusterconfigTable.recordCount() == 0)
    {
      clusterconfigRecord = null;
    }
    else
    {
      clusterconfigRecord = clusterconfigTable.getRecord(0);
      if (!resetConfiguration)
        return clusterconfigRecord;
    }

    IDataTransaction transaction = accessor.newTransaction();
    try
    {
      if (clusterconfigRecord == null)
      {
        // configuration not existing so far -> create one
        //
        clusterconfigRecord = clusterconfigTable.newRecord(transaction);
        clusterconfigRecord.setValue(transaction, Clusterconfig.configname, CONFIG_NAME);
      }

      clusterconfigRecord.setValue(transaction, Clusterconfig.clustername, buildDefaultClusterName());
      // keep wildcard for port, i.e. null as second argument
      clusterconfigRecord.setValue(transaction, Clusterconfig.globalgroup, getJGroupProperties("228.8.8.7", null));
      // generate random port in the range 35000..44999 to avoid warnings of
      // jgroup if different
      // channels are using the same IP address and port.
      int randomPort = 35000 + (int) ((System.currentTimeMillis() / 10) % 10000);
      clusterconfigRecord.setValue(transaction, Clusterconfig.clustergroup, getJGroupProperties("228.8.8.8", "" + randomPort));
      transaction.commit();
    }
    finally
    {
      transaction.close();
    }
    return clusterconfigRecord;
  }
  
  /**
   * Internal method for group info JSP!
   * 
   * @return
   */
  public Group getGlobalGroup()
  {
    return this.licenseGroup;
  }
  
  /**
   * Internal method for group info JSP!
   * 
   * @return
   */
  public Group getClusterGroup()
  {
    return this.clusterGroup;
  }
}
