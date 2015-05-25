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

import java.util.List;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * Abstract base class for all cluster implementations.
 * 
 * @author Andreas Sonntag
 */
public abstract class ClusterProvider
{
  static public final transient String RCS_ID = "$Id: ClusterProvider.java,v 1.2 2008/04/02 15:42:07 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  protected abstract boolean init() throws Throwable;

  protected abstract String getInitializationError();

  protected abstract void close() throws Throwable;

  /**
   * Returns the total user session count, i.e. the sum of all user sessions of
   * all cluster nodes.
   * 
   * @return the total user session count or -1, if not applicable
   */
  protected abstract int getUserSessionCount();

  /**
   * Checks whether this node is the cluster coordinator.
   * 
   * @return <code>true</code> node is the coordinator, otherwise
   *         <code>false</code>
   */
  protected abstract boolean isCoordinator();

  /**
   * Propagates the local user session counter to all reachable jACOB nodes.
   * @param userCount the user count to propagate
   */
  protected abstract void propagateUserCount(int userCount);

  /**
   * Propagates a property change to all nodes (including this node) of this
   * cluster.
   * 
   * @param propertyName
   *          the name of the property, which value has been changed.
   * @throws Exception
   *           if the propagation fails
   */
  public abstract void propagatePropertyChange(String propertyName) throws Exception;

  /**
   * Propagates a datasource change to all nodes (including this node) of this
   * cluster.
   * 
   * @param datasourceName
   *          the name of the datasource, which configuration has been changed
   * @throws Exception
   *           if the propagation fails
   */
  public abstract void propagateDatasourceChange(String datasourceName) throws Exception;

  /**
   * Propagates a change of a scheduled report to all nodes (including this node) of this
   * cluster.
   * 
   * @param guid
   *          the id of the report definition
   * @throws Exception
   *           if the propagation fails
   */
  public abstract void propagateScheduledReportChange(String guid) throws Exception;

  /**
   * Propagates a license change to all nodes (including this node) of this
   * cluster.
   * 
   * @throws Exception
   *           if the propagation fails
   */
  public abstract void propagateLicenseChange() throws Exception;

  /**
   * Propagates a logging configuration change to all nodes (including this node)
   * of this cluster.
   * 
   * @throws Exception
   *           if the propagation fails
   */
  public abstract void propagateLoggingChange() throws Exception;

  /**
   * Propagates the server URL of this node to all nodes (including this node)
   * of this cluster.
   * 
   * @param url the server url of this node
   * @throws Exception
   *           if the propagation fails
   */
  public abstract void propagateNodeUrl(String url) throws Exception;

  /**
   * Fetches the server URLs of all nodes (including this node) of this cluster.
   * 
   * @return <code>List</code> of <code>String</code>
   */
  public abstract List getNodeUrls();

  /**
   * Notifies all nodes (including this node) to check installed applications.
   * 
   * @throws Exception
   *           if the notification fails
   */
  public abstract void notifyCheckApplications() throws Exception;

  /**
   * Notifies all nodes (including this node) to terminate user sessions of a
   * given application and the given user login id.
   * 
   * @param applicationname
   *          the name of the application
   * @param userloginid
   *          the user login id
   * @throws Exception
   *           if the notification fails
   * @since 2.7.2           
   */
  public abstract void notifyTerminateUserSessions(String applicationname, String userloginid) throws Exception;

  /**
   * Notifies all nodes (including this node) to undeploy the given application
   * version.
   * 
   * @param appName
   *          the name of the application
   * @param version
   *          the version of the application
   * @throws Exception
   *           if the notification fails
   */
  public abstract void notifyUndeployApplication(String appName, String version) throws Exception;

  /**
   * Notifies all nodes (excluding this node) about a task status change.
   * 
   * @param applicationname
   *          the name of the application the task is assigned to
   * @param taskname
   *          the name of the task
   * @param taskstatus
   *          the new status of the task
   * @throws Exception
   */
  public abstract void notifyTaskStatusChanged(String applicationname, String taskname, String taskstatus) throws Exception;

  /**
   * Internal method for fetching cluster node data.
   * 
   * @param clusterNodeTable
   * @param trans
   * @return
   * @throws Exception
   */
  public abstract void fetchClusterNodeInfo(IDataTable clusterNodeTable, IDataTransaction trans) throws Exception;

  /**
   * Internal method for fetching license node data.
   * 
   * @param licenseNodeTable
   * @param trans
   * @return
   * @throws Exception
   */
  public abstract void fetchLicenseNodeInfo(IDataTable licenseNodeTable, IDataTransaction trans) throws Exception;
  
  /**
   * Resets the cluster configuration to factory default.
   * 
   * @return the reset cluster configuration record or <code>null</code> if not supported
   * @throws Exception
   *           on any problem
   */
  public abstract IDataTableRecord resetConfigurationToFactory() throws Exception;
  
}
