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

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.deployment.DeployMain;

/**
 * Checks whether the user wants to force login to a given application by means
 * of:<br>
 * http://host:port/jacob/application
 * 
 * @author Andreas Sonntag
 */
public class ForceApplicationFilter implements Filter
{
  static public final transient String RCS_ID = "$Id: ForceApplicationFilter.java,v 1.3 2009/07/30 23:25:23 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private static final Log logger = LogFactory.getLog(ForceApplicationFilter.class);

  public void init(FilterConfig filterConfig) throws ServletException
  {
    // nothing to do here
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
  {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    String query = request.getQueryString();
    String path = request.getServletPath();

    if (query == null)
    {
      if (path.indexOf('/', 1) < 0 && path.indexOf('.') < 0)
      {
        // extract application name, e.g. "/admin" -> "admin"
        String applicationName = path.substring(1);

        if (DeployMain.hasActiveApplication(applicationName))
        {
          // Ein Forward kann hier schwer gemacht werden, da Context-Root beim
          // Forward nicht auf das
          // Root-Verzeichnis der Applikations-Loginseite gesetzt werden kann.
          // TODO: Doch eine Möglichkeit?

          // IApplicationDefinition forceApplication =
          // DeployMain.getActiveApplication(applicationName);
          // String appLogin = "/application/" + forceApplication.getName() +
          // "/" + forceApplication.getVersion().toShortString() + "/login.jsp";
          // String realpath =
          // request.getSession().getServletContext().getRealPath(appLogin);
          // File file = new File(realpath);
          // if (file.exists())
          // {
          // request.getRequestDispatcher(appLogin).forward(request, response);
          // return;
          // }

          request.getRequestDispatcher("/login.jsp?forceApp=" + applicationName).forward(request, response);
          return;
        }
      }
      else
      {
        // Check whether somebody has called an old link such like:
        // http://localhost:8081/jacob/application/visualenterprise/0.1/login.jsp
        // In this case redirect to http://localhost:8081/jacob/visualenterprise which itself 
        // forwards to the highest productive application version.
        String applicationName = checkForceRedirect(path);
        if (applicationName != null)
        {
          String url = request.getRequestURL().toString();
          String redirectURL = url.substring(0, url.indexOf(APPLICATION_PREFIX) + 1) + applicationName;
          if (logger.isInfoEnabled())
            logger.info("Redirecting to " + redirectURL);
          response.sendRedirect(redirectURL);
          return;
        }
      }
    }
    
    if (path.startsWith(APPLICATION_PREFIX))
    {
      int defaultPos = path.indexOf(DEFAULT_VERSION_WILDCARD);
      if (defaultPos > 0)
      {
        String applicationName = path.substring(APPLICATION_PREFIX.length(), defaultPos);
        if (DeployMain.hasActiveApplication(applicationName))
        {
          IApplicationDefinition applDef = DeployMain.getActiveApplication(applicationName);

          String url = request.getRequestURL().toString();
          StringBuffer redirectURL = new StringBuffer();
          redirectURL.append(url.substring(0, url.indexOf(APPLICATION_PREFIX)));
          redirectURL.append(APPLICATION_PREFIX);
          redirectURL.append(applicationName);
          redirectURL.append("/").append(applDef.getVersion().toShortString()).append("/");
          redirectURL.append(path.substring(defaultPos + DEFAULT_VERSION_WILDCARD.length()));
          if (query != null)
            redirectURL.append("?").append(query);

          if (logger.isInfoEnabled())
            logger.info("Redirecting to " + redirectURL.toString());
          response.sendRedirect(redirectURL.toString());
          return;
        }
      }
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  private static final String APPLICATION_PREFIX = "/application/";
  private static final String LOGIN_SUFFIX = "/login.jsp";
  private static final String DEFAULT_VERSION_WILDCARD = "/default/";

  private static String checkForceRedirect(String path)
  {
    if (path.startsWith(APPLICATION_PREFIX) && path.endsWith(LOGIN_SUFFIX))
    {
      int slashPos = path.indexOf('/', APPLICATION_PREFIX.length());
      if (slashPos > 0)
      {
        String applicationName = path.substring(APPLICATION_PREFIX.length(), slashPos);
        String versionShort = path.substring(slashPos + 1, path.length() - LOGIN_SUFFIX.length());

        List applDefs = DeployMain.getActiveApplications(applicationName);
        for (int i = 0; i < applDefs.size(); i++)
        {
          IApplicationDefinition appDef = (IApplicationDefinition) applDefs.get(i);
          if (appDef.getVersion().toShortString().endsWith(versionShort))
            // no forward
            return null;
        }
        return applicationName;
      }
    }

    return null;
  }

  public void destroy()
  {
    // nothing to do here
  }
}