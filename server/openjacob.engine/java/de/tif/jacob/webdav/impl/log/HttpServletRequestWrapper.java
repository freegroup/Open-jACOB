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
package de.tif.jacob.webdav.impl.log;

import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpServletRequestWrapper extends ServletRequestWrapper implements HttpServletRequest
{
  static public final transient String RCS_ID = "$Id: HttpServletRequestWrapper.java,v 1.1 2007/01/19 09:50:41 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private final HttpServletRequest embedded;

  /**
   * @param embedded
   * @throws IOException
   */
  protected HttpServletRequestWrapper(HttpServletRequest embedded) throws IOException
  {
    super(embedded);
    this.embedded = embedded;
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getAuthType()
   */
  public String getAuthType()
  {
    return this.embedded.getAuthType();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getContextPath()
   */
  public String getContextPath()
  {
    return this.embedded.getContextPath();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getCookies()
   */
  public Cookie[] getCookies()
  {
    return this.embedded.getCookies();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
   */
  public long getDateHeader(String arg0)
  {
    return this.embedded.getDateHeader(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
   */
  public String getHeader(String arg0)
  {
    return this.embedded.getHeader(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
   */
  public Enumeration getHeaderNames()
  {
    return this.embedded.getHeaderNames();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
   */
  public Enumeration getHeaders(String arg0)
  {
    return this.embedded.getHeaders(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
   */
  public int getIntHeader(String arg0)
  {
    return this.embedded.getIntHeader(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getMethod()
   */
  public String getMethod()
  {
    return this.embedded.getMethod();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getPathInfo()
   */
  public String getPathInfo()
  {
    return this.embedded.getPathInfo();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
   */
  public String getPathTranslated()
  {
    return this.embedded.getPathTranslated();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getQueryString()
   */
  public String getQueryString()
  {
    return this.embedded.getQueryString();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
   */
  public String getRemoteUser()
  {
    return this.embedded.getRemoteUser();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
   */
  public String getRequestedSessionId()
  {
    return this.embedded.getRequestedSessionId();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getRequestURI()
   */
  public String getRequestURI()
  {
    return this.embedded.getRequestURI();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getRequestURL()
   */
  public StringBuffer getRequestURL()
  {
    return this.embedded.getRequestURL();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getServletPath()
   */
  public String getServletPath()
  {
    return this.embedded.getServletPath();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getSession()
   */
  public HttpSession getSession()
  {
    return this.embedded.getSession();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
   */
  public HttpSession getSession(boolean arg0)
  {
    return this.embedded.getSession(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
   */
  public Principal getUserPrincipal()
  {
    return this.embedded.getUserPrincipal();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
   */
  public boolean isRequestedSessionIdFromCookie()
  {
    return this.embedded.isRequestedSessionIdFromCookie();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
   */
  public boolean isRequestedSessionIdFromUrl()
  {
    return this.embedded.isRequestedSessionIdFromUrl();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
   */
  public boolean isRequestedSessionIdFromURL()
  {
    return this.embedded.isRequestedSessionIdFromURL();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
   */
  public boolean isRequestedSessionIdValid()
  {
    return this.embedded.isRequestedSessionIdValid();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
   */
  public boolean isUserInRole(String arg0)
  {
    return this.embedded.isUserInRole(arg0);
  }
  
}
