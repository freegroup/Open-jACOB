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
package de.tif.jacob.balancer;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.balancer.impl.RoundRobinStrategy;
import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.model.Clusterserverurl;

/**
 * Cluster node balancer manager.
 */
public final class Balancer extends BootstrapEntry
{
  static public final transient String RCS_ID = "$Id: Balancer.java,v 1.3 2010/07/07 14:09:30 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  static private final transient Log logger = LogFactory.getLog(Balancer.class);
  
  /**
   * Use round robin as default balancer strategy.
   */
  private static final Class DEFAULT_STRATEGY = RoundRobinStrategy.class;
  
  private static String persistentServerUrl;
  private static IStrategy strategy;

  public static synchronized String getRedirectUrl(HttpServletRequest request)
  {
    // Kein Redirect wenn der Anwender/Server es eigentlich nicht wünscht.
    // Wichtig, damit ein Browser nicht von einem Server zum nächsten
    // geschickt
    // wird.
    // Es kann somit maximal 1 mal ein Redirect erfolgen.
    //
    String queryString = request.getQueryString();
    if (queryString != null && queryString.indexOf("redirected=true") != -1)
    {
      // no redirect
      return null;
    }

    String clazz = Property.BALANCER_STRATEGY.getValue();
    if (clazz == null)
      clazz = DEFAULT_STRATEGY.getName();
    if (strategy == null || !strategy.getClass().getName().equals(clazz))
    {
      try
      {
        strategy = (IStrategy) Class.forName(clazz).newInstance();
      }
      catch (Exception e)
      {
        ExceptionHandler.handle(e);
        strategy = new RoundRobinStrategy();
        // reset the not existing strategy to a valid default value
        //
        Property.BALANCER_STRATEGY.setValue(DEFAULT_STRATEGY.getName());
      }
    }
    String requestUrl = request.getRequestURL().toString();
    String redirectUrl = strategy.getRedirectUrl(requestUrl);
    if (redirectUrl == null)
      return null;
    
    if (queryString != null)
      return redirectUrl + "?" + queryString + "&redirected=true";
    return redirectUrl + "?redirected=true";
  }
  
  // Bootstrap methods
  //
  public void init() throws Throwable
  {
    // clustering switch on?
    if (ClusterManager.isEnabled())
    {
      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();

      // Search for persistent server URL in jACOB datasource and propagate it to the group,
      // if it does exist.
      // Reason: There is no common way to access the port a webserver (like Tomcat) is
      // listening. So we do not know the server url until the first client request. To
      // register a node, which has just been (re)started, to the load balancer we access
      // the previously stored url.
      //
      IDataTable table = accessor.getTable("clusterserverurl");
      table.qbeSetKeyValue("nodename", ClusterManager.getNodeName());
      table.search();
      if (table.recordCount() == 1)
      {
        IDataTableRecord record = table.getRecord(0);
        persistentServerUrl = record.getStringValue("serverurl"); 
        if (persistentServerUrl != null)
        {
          ClusterManager.getProvider().propagateNodeUrl(persistentServerUrl);
        }
      }
    }
  }

  public void destroy() throws Throwable
  {
    // nothing to do here
  }

  /**
   * Register the given URL for load balancing.
   * <p>
   * The registration consists of:
   * <li>propagation of the url to all node within the same cluster
   * <li>store url for immediate use after restart.
   * 
   * @param url
   *          the url to register
   * @throws Exception
   *           on any registration problem
   */
  public static void register(URL url) throws Exception
  {
    if (logger.isInfoEnabled())
      logger.info("Registering server URL: " + url);
    
    if (ClusterManager.isEnabled())
    {
      // 1. propagate server url
      //
      String urlStr = url.toString();
      ClusterManager.getProvider().propagateNodeUrl(urlStr);
      
      // 2. update persistent server url, if necessary
      //
      if (!urlStr.equals(persistentServerUrl))
      {
        if (logger.isInfoEnabled())
          logger.info("Storing server URL: " + url);
        
        IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
        IDataTable table = accessor.getTable(Clusterserverurl.NAME);
        String nodename = ClusterManager.getNodeName();
        table.qbeSetKeyValue(Clusterserverurl.nodename, nodename);
        table.search();
        IDataTransaction trans = accessor.newTransaction();
        try
        {
          IDataTableRecord record;
          if (table.recordCount() == 0)
          {
            record = table.newRecord(trans);
            record.setValue(trans, Clusterserverurl.nodename, nodename);
          }
          else
          {
            record = table.getRecord(0);
          }
          record.setValue(trans, Clusterserverurl.serverurl, urlStr);
          trans.commit();
        }
        finally
        {
          trans.close();
        }
      }
    }
  }
}
