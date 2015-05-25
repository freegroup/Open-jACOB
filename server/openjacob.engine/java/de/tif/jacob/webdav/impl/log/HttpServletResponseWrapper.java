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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseWrapper extends ServletResponseWrapper implements HttpServletResponse
{
  static public final transient String RCS_ID = "$Id: HttpServletResponseWrapper.java,v 1.1 2007/01/19 09:50:41 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private final HttpServletResponse embedded;
  private final Map headers = new HashMap();

  /**
   * @param embedded
   * @throws IOException
   */
  protected HttpServletResponseWrapper(HttpServletResponse embedded) throws IOException
  {
    super(embedded);
    this.embedded = embedded;
  }
  
  public Iterator getHeaderNames()
  {
    return this.headers.keySet().iterator();
  }
  
  public Iterator getHeaders(String name)
  {
    return ((List) this.headers.get(name)).iterator();
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
   */
  public void addCookie(Cookie arg0)
  {
    this.embedded.addCookie(arg0);
  }

  private void addHeaderInternal(String arg0, Object value)
  {
    List headerdata = (List) this.headers.get(arg0);
    if (headerdata == null)
    {
      headerdata = new ArrayList();
      this.headers.put(arg0, headerdata);
    }
    headerdata.add(value);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
   */
  public void addDateHeader(String arg0, long arg1)
  {
    addHeaderInternal(arg0, new Date(arg1));
    this.embedded.addDateHeader(arg0, arg1);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
   */
  public void addHeader(String arg0, String arg1)
  {
    addHeaderInternal(arg0, arg1);
    this.embedded.addHeader(arg0, arg1);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
   */
  public void addIntHeader(String arg0, int arg1)
  {
    addHeaderInternal(arg0, Integer.toString(arg1));
    this.embedded.addIntHeader(arg0, arg1);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
   */
  public boolean containsHeader(String arg0)
  {
    return this.embedded.containsHeader(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
   */
  public String encodeRedirectUrl(String arg0)
  {
    return this.embedded.encodeRedirectUrl(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
   */
  public String encodeRedirectURL(String arg0)
  {
    return this.embedded.encodeRedirectURL(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
   */
  public String encodeUrl(String arg0)
  {
    return this.embedded.encodeUrl(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
   */
  public String encodeURL(String arg0)
  {
    return this.embedded.encodeURL(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
   */
  public void sendError(int arg0, String arg1) throws IOException
  {
    this.embedded.sendError(arg0, arg1);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#sendError(int)
   */
  public void sendError(int arg0) throws IOException
  {
    this.embedded.sendError(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
   */
  public void sendRedirect(String arg0) throws IOException
  {
    this.embedded.sendRedirect(arg0);
  }

  private void setHeaderInternal(String arg0, Object value)
  {
    List headerdata = new ArrayList();
    this.headers.put(arg0, headerdata);
    headerdata.add(value);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
   */
  public void setDateHeader(String arg0, long arg1)
  {
    setHeaderInternal(arg0, new Date(arg1));
    this.embedded.setDateHeader(arg0, arg1);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
   */
  public void setHeader(String arg0, String arg1)
  {
    setHeaderInternal(arg0, arg1);
    this.embedded.setHeader(arg0, arg1);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
   */
  public void setIntHeader(String arg0, int arg1)
  {
    setHeaderInternal(arg0, Integer.toString(arg1));
    this.embedded.setIntHeader(arg0, arg1);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
   */
  public void setStatus(int arg0, String arg1)
  {
    this.embedded.setStatus(arg0, arg1);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletResponse#setStatus(int)
   */
  public void setStatus(int arg0)
  {
    this.embedded.setStatus(arg0);
  }

}
