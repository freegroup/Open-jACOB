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
import java.net.InetAddress;
import java.net.URL;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import de.tif.jacob.balancer.Balancer;

/**
 * Upon first http request, the server URL is registered at the load balancer.
 * 
 * @author Andreas Herz
 * @author Andreas Sonntag
 */
public class UrlDetermineFilter implements Filter
{
  static public final transient String RCS_ID = "$Id: UrlDetermineFilter.java,v 1.2 2007/04/17 16:22:48 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  private static final Object MUTEX = UrlDetermineFilter.class;
  
  private static String url;
  
  /* (non-Javadoc)
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig filterConfig) throws ServletException
  {
    // nothing to do here
  }

  /* (non-Javadoc)
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
  {
    synchronized (MUTEX)
    {
      if (Bootstrap.isOk() && url == null && servletRequest instanceof HttpServletRequest)
      {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        
        // determine server url
        //
        String host = servletRequest.getServerName();
        if ("localhost".equalsIgnoreCase(host))
        {
          host = InetAddress.getLocalHost().getHostName();
        }
        int port = servletRequest.getServerPort();
        StringBuffer x = httpServletRequest.getRequestURL();
        String protocol = x.substring(0,x.indexOf("://"));
        String tempUrl = protocol+"://" + host + ":" + port + httpServletRequest.getContextPath();
        
        // register url
        //
        try
        {
          Balancer.register(new URL(tempUrl));
        }
        catch (Exception ex)
        {
          throw new ServletException(ex);
        }

        // and keep registered url on successful registration
        url = tempUrl;
      }
    }
    
    // Attention: Must be outside of the synchronized block to avoid to serialize all
    // server requests!
    filterChain.doFilter(servletRequest, servletResponse);
  }

  /* (non-Javadoc)
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy()
  {
    // nothing to do here
  }
}