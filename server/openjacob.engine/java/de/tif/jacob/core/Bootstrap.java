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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.license.License;
import de.tif.jacob.license.LicenseFactory;
import de.tif.jacob.util.config.Config;


/**
 * Central boot strap for jACOB application server runup.
 *  
 * @author Administrator
 */
public class Bootstrap extends HttpServlet
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: Bootstrap.java,v 1.2 2009/06/23 10:55:13 ibissw Exp $";
  
  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.2 $";

  static private final Log logger = LogFactory.getLog(Bootstrap.class);

  /**
   * Timestamp to be set on startup, i.e. first access.
   */
  private static final Date ACTIVE_SINCE = new Date();
    
  // build jACOB application server description
  private static final String FULL_APPLICATION_SERVER_NAME = Version.ENGINE_NAME + " Application Server " + Version.ENGINE;
  
  private static String applicationRootPath = null;
  private static String applicationName = null;
  
  /**
   * Remember servlet context for mime mapping.
   */
  private static ServletContext servletContext = null;
  
  private static Throwable error = null;
  
  private static boolean initialized = false;
  private static boolean destroyed = false;
  
  //only required in demo mode. In the other case it will read from the DB
  private static boolean licenseAccepted=false; 
  
  private static final List bootstrapEntries = new Vector();
  
  /**
   * Returns the date this jACOB application server instance is active since.
   * 
   * @return date active since of this node
   */
  public static Date getActiveSince()
  {
    return ACTIVE_SINCE;
  }
  
  /**
   * Returns the context name of the jACOB web application, i.e.
   * <code>jacob</code>.
   * 
   * @return the jACOB web application context name
   * @deprecated use {@link #getBaseUrl(HttpServletRequest)} instead
   */
  public static String getApplicationName()
  {
    // Anmerkung: Es ist prinzipiell nicht möglich den jACOB web application context name zuverlässig
    // zu bestimmen bevor nicht ein HTTP request eingegangen ist.
    if (applicationName == null)
      throw new IllegalStateException("applicationName has not been determined so far");
    return applicationName;
  }
  
  /**
   * Returns the jACOB web application base URL like <code>http://hostname:port/jacob/</code>
   * @return the jACOB web application base URL
   * @see Property#BASE_URL
   * @since 2.5.5
   */
  public static String getBaseUrl(HttpServletRequest request)
  {
    String baseurl = Property.BASE_URL.getValue();
    if (baseurl != null)
      return baseurl;
    
    try
    {
      URL requestUrl = new URL(request.getRequestURL().toString());
      StringBuffer buffer = new StringBuffer();
      buffer.append(requestUrl.getProtocol());
      buffer.append("://");
      buffer.append(requestUrl.getHost());
      if (requestUrl.getPort() > 0)
        buffer.append(":").append(requestUrl.getPort());
      buffer.append(request.getContextPath());
      buffer.append("/");
      return buffer.toString();
    }
    catch (MalformedURLException ex)
    {
      // should never occur
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * The path in which the *.jacapp files has been deployed
   * The path always ends with a '/' (File.separator)
   * 
   * @return the path of the *.jacapp files.
   */
  public static String getJacappPath()
  {
    return getApplicationRootPath() + "WEB-INF" + File.separator + "jacapp" + File.separator;
  }
  
  
  
  /**
   * The path in which the *.jacapp files has been deployed
   * The path always ends with a '/' (File.separator)
   * 
   * @return the path of the *.jacapp files.
   */
  public static String getBIRTPath()
  {
    return getApplicationRootPath() + "WEB-INF" + File.separator + "birt-runtime" + File.separator;
  }
  
  
  /**
   * The directory in which the application server has deployed the web application.
   * The applicationRootPath ends always with a '/' (File.separator) at the end.
   *
   * Directories always end with a '/'.
   * 
   * @return Returns the applicationRootPath or <code>null</code> if the application server 
   * root path has not been determined or method is not invoked within an application
   * server environment.
   */
  public static String getApplicationRootPath()
  {
    if (applicationRootPath == null)
      throw new IllegalStateException("applicationRootPath has not been determined so far");
    return applicationRootPath;
  }
  
  
  /**
   * The directory in which the jACOB themes are deployed. This is a local absolute
   * path
   *
   * Directories always end with a '/'.
   * 
   * @return Returns the applicationRootPath or <code>null</code> if the application server 
   * root path has not been determined or method is not invoked within an application
   * server environment.
   */
  public static String getThemeRootPath()
  {
    return getApplicationRootPath() + "themes" + File.separator;
  }
  
  /**
   * Directories always end with a '/'.
   *  
   * @return <myWebapp>/WEB-INF/conf/ directory
   */
  public static String getWebInfConfPath()
  {
    return getApplicationRootPath() + "WEB-INF" + File.separator + "conf" + File.separator;
  }
  
  
  /* (non-Javadoc)
   * @see javax.servlet.GenericServlet#init()
   */
  public void init() throws ServletException
  {
    initStatic(getServletConfig());
  }
  
  /* (non-Javadoc)
   * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
   */
  public void init(ServletConfig servletConfig) throws ServletException
  {
    initStatic(servletConfig);
  }
  
  /**
   * Note: This method must be synchronized on class level (therefore static
   * method) in case the servlet engine calls this method more than once.
   */
  private static synchronized void initStatic(ServletConfig servletConfig) throws ServletException
  {
    // just leave if already initialized
    if (initialized)
      return;

    servletContext = servletConfig.getServletContext();

    // determine jacob root path
    //
    applicationRootPath = servletConfig.getInitParameter("applicationRoot");
    if (applicationRootPath == null || applicationRootPath.equals("webContext"))
      applicationRootPath = servletContext.getRealPath("/");
    if(!applicationRootPath.endsWith(SystemUtils.FILE_SEPARATOR))
      applicationRootPath=applicationRootPath+SystemUtils.FILE_SEPARATOR;
    logger.info("getApplicationRootPath() = " + getApplicationRootPath());

    // determine jacob web application name
    //
    if (applicationRootPath.endsWith(File.separator))
      applicationName = applicationRootPath.substring(0, applicationRootPath.length() - 1);
    if (applicationName.endsWith(File.separator))
      applicationName = applicationName.substring(0, applicationName.length() - 1);
    applicationName = applicationName.substring(applicationName.lastIndexOf(File.separator) + 1);
    if (applicationName.endsWith(".war"))
    {
      // WebSphere deploys an web application a directory like:
      // C:\Programme\IBM\WebSphere\AppServer\profiles\jacobServer\installedApps\vmwas60Node02Cell\jACOB.ear\jacob.war\
      //
      applicationName = applicationName.substring(0, applicationName.length() - ".war".length());
    }
    logger.info("getApplicationName() = " + getApplicationName());
    
    // the plattform dependent properties
    //
    System.setProperty("plattform_" + applicationName + ".configfile", applicationRootPath + "WEB-INF" + File.separator + "conf" + File.separator + "plattform.properties");

    // the platform independent properties
    //
    System.setProperty("common_" + applicationName + ".configfile", applicationRootPath + "WEB-INF" + File.separator + "conf" + File.separator + "common.properties");

    // loading the property files
    //Config p_conf = Config.getPlattformConfig();
    Config c_conf = Config.getCommonConfig();

    try
    {
      long start = System.currentTimeMillis();
      
      System.out.println("+=================================================");
      System.out.println("+ Starting " + FULL_APPLICATION_SERVER_NAME);
      System.out.println("+=================================================");
      
      // Just to access servlet info in case of development
      //
      if (logger.isDebugEnabled())
      {
        System.out.println("+-------------------------------------------------");
        
        System.out.println("+ Servlet init parameter:");
        Enumeration enumeration = servletConfig.getInitParameterNames();
        while (enumeration.hasMoreElements())
        {
          String key = (String) enumeration.nextElement();
          System.out.println("+ " + key + "=" + servletConfig.getInitParameter(key));
        }
        
        System.out.println("+-------------------------------------------------");
        
        System.out.println("+ Servlet context attribute:");
         enumeration = servletContext.getAttributeNames();
        while (enumeration.hasMoreElements())
        {
          String key = (String) enumeration.nextElement();
          System.out.println("+ " + key + "=" + servletContext.getAttribute(key));
        }
        
        System.out.println("+-------------------------------------------------");
        
        System.out.println("+ Real path: " + servletContext.getRealPath("/"));
        System.out.println("+ Server info: " + servletContext.getServerInfo());
        
        System.out.println("+-------------------------------------------------");
      }

      // Set the current thread local context.
      //
      Context.setCurrent(new SystemContext());

      // execute all bootstrap entries in the right order
      //
      boolean hasWarnings = false;
      for (int i = 0; i < 100; i++)
      {
        String clazz = c_conf.getProperty("bootstrap.class." + i);
        if (clazz != null)
        {
          BootstrapEntry entry = (BootstrapEntry) Class.forName(clazz).newInstance();
          bootstrapEntries.add(entry);
          try
          {
            entry.boot();
          }
          catch (Exception ex)
          {
            throw new Exception("Unable to execute BootstrapEntry [" + clazz + "]", ex);
          }
          if (entry.hasWarnings())
            hasWarnings = true;
        }
      }
      
      // check the license key
      //
      try
      {
        License license = LicenseFactory.getLicenseManager().getLicense();
        System.out.println("License information:");
        System.out.println(license.toString());
      }
      catch (Exception e)
      {
        System.out.println("jACOB Application server runs without any valid license key information!");
      }

      long millis = System.currentTimeMillis() - start;
      if (hasWarnings)
      {
        System.out.println("+==========================================================================");
        System.out.println("+ " + FULL_APPLICATION_SERVER_NAME + " started with some restrictions in " + millis + "ms");
        System.out.println("+==========================================================================");
      }
      else
      {
        System.out.println("+=================================================================");
        System.out.println("+ " + FULL_APPLICATION_SERVER_NAME + " successfully started in " + millis + "ms");
        System.out.println("+=================================================================");
      }
    }
    catch (Throwable e)
    {
      error = e;
      
      System.out.println("+===============================================");
      System.out.println("+ " + FULL_APPLICATION_SERVER_NAME + " startup FAILED!");
      System.out.println("+===============================================");

      ExceptionHandler.handle(e);
      
      throw new ServletException(e);
    }
    finally
    {
      // set flag to ensure calling this method once even if an error occurs
      initialized = true;
      
      // just to be sure to release all resources
      Context.setCurrent(null);
    }
  }

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
	{
		// do nothing
	}
    
	/**
   * Checks whether all bootstrap entries have been successfully executed, i.e.
   * startup is complete.
   * 
   * @return <code>true</code> startup complete, otherwise <code>false</code>
   */
	public static synchronized boolean isOk()
	{
		return null == error && initialized;
	}

  /**
   * Checks whether the jACOB engine has been stopped or is going to be stopped.
   * 
   * @return <code>true</code> engine is stopped or stopping, otherwise
   *         <code>false</code>
   */
  public static boolean isDestroyed()
  {
    return destroyed;
  }

  public static synchronized boolean hasLicenseAccepted()
  {
    // Im open mode muss der User bei jedem Hochfahren des jACOB Servers der
    // Lizenzbestimmung zustimmen.
    //
//    if (Version.IS_OPEN_ENGINE)
//      return licenseAccepted;
    
    // Acceptance of agreement not necessary for productive licenses!
    return true;
  }
  
  public static synchronized void setLicenseAccepted()
  {
    licenseAccepted = true;
  }

  /**
	 * @return Returns the error.
	 */
	public static Throwable getError()
	{
		return error;
	}

  /* (non-Javadoc)
   * @see javax.servlet.Servlet#destroy()
   */
  public void destroy()
  {
    destroyStatic();
  }
  
  /**
   * Note: This method must be synchronized on class level (therefore static
   * method) in case the servlet engine calls this method more than once.
   */
  private static synchronized void destroyStatic()
  {
    // just leave if already destroyed
    if (destroyed)
      return;
    
    // set flag immediately to ensure calling this method once even if an
    // error occurs
    destroyed = true;
    
    // also release reference to servlet context
    //
    servletContext = null;
    
    long start = System.currentTimeMillis();
    
    System.out.println("+=================================================");
    System.out.println("+ Stopping " + FULL_APPLICATION_SERVER_NAME);
    System.out.println("+=================================================");
    System.out.println("stopping now the different subsystems....");

    //
    // call destroy methods of bootstrap entries in reverse order
    for (int i = bootstrapEntries.size() - 1; i >= 0; i--)
    {
      BootstrapEntry entry = (BootstrapEntry) bootstrapEntries.get(i);
      entry.shutdown();
    }
    bootstrapEntries.clear();
    
    System.out.println("+=================================================");
    System.out.println("+ " + FULL_APPLICATION_SERVER_NAME + " stopped in " + (System.currentTimeMillis() - start) + "ms");
    System.out.println("+=================================================");
  }
}
  