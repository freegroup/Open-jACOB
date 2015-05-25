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
package de.tif.jacob.license;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.tif.jacob.core.Bootstrap;

import java.io.*;

/**
 * Servlet Filter which can be added to the filter chain in the web.xml to
 * enable validation of the license before serving protected resources
 * 
 * @author Andreas
 */
public class LicenseFilter implements Filter
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: LicenseFilter.java,v 1.1 2007/01/19 09:50:39 freegroup Exp $";
  
  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

  public LicenseFilter()
  {
  }

  public void init(FilterConfig filterConfig) throws ServletException
  {
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
  {
    // see if this filter has already been applied to this request
    if (servletRequest.getAttribute(FILTER_APPLIED) != null)
    {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }
    servletRequest.setAttribute(FILTER_APPLIED, Boolean.TRUE);
    
    // if bootstrap is not ok, let it continue, i.e. show error page
    if (!Bootstrap.isOk())
    {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }

    HttpServletRequest req = (HttpServletRequest) servletRequest;
    String uri = req.getServletPath();

    // if the uri is the license error URL, let it continue
    if (uri.endsWith(INVALID_LICENSE_URL))
    {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }

    // resourcen für licenseError.jsp z.B. Bilder oder css müssen verfügbar sein
    if(uri.startsWith("/login/"))
    {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }
    
    // Es sind erst Resourcen verfügbar wenn der User den Lizenbestimmungen zugestimmt hat
    //
    if(!Bootstrap.hasLicenseAccepted())
    {
      ((HttpServletResponse) servletResponse).sendRedirect(req.getContextPath() + ACCEPT_LICENSE_URL);
      return;
    }

    // get the license. if there are any errors, redirect to the
    // INVALID_LICENSE_URL
    License license = null;
    try
    {
      license = LicenseFactory.getLicenseManager().getLicense();
    }
    catch (LicenseException e)
    {
      //			System.out.println(e.getMessage());
      license = null;
    }

    if (license == null || license.isExpired())
    {
      ((HttpServletResponse) servletResponse).sendRedirect(req.getContextPath() + INVALID_LICENSE_URL);
      return;
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  public void destroy()
  {
  }

  public static boolean isFilterApplied(ServletRequest servletRequest)
  {
    return  servletRequest.getAttribute(FILTER_APPLIED) == Boolean.TRUE;
  }
  
  private static final String FILTER_APPLIED = "LICENSE_FILTER_APPLIED";

  public static final String INVALID_LICENSE_URL = "/licenseError.jsp";
  public static final String ACCEPT_LICENSE_URL = "/login/license.jsp";


}