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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

public class ServletRequestWrapper implements ServletRequest
{
  static public final transient String RCS_ID = "$Id: ServletRequestWrapper.java,v 1.2 2010/07/07 15:33:34 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private class MyStream extends ServletInputStream
  {
    private final ServletInputStream embedded;
    private final ByteArrayOutputStream os;
    
    private MyStream(ServletInputStream embedded, int contentLength)
    {
      this.embedded = embedded;
      this.os = new ByteArrayOutputStream(contentLength < 1024 ? 1024 : contentLength);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletInputStream#readLine(byte[], int, int)
     */
    public int readLine(byte[] b, int off, int len) throws IOException
    {
      int res = this.embedded.readLine(b,  off,  len);
      if (res >= 0)
        this.os.write(b, off, res);
      return res;
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#available()
     */
    public int available() throws IOException
    {
      return this.embedded.available();
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#close()
     */
    public void close() throws IOException
    {
      this.embedded.close();
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#mark(int)
     */
    public synchronized void mark(int readlimit)
    {
      this.embedded.mark(readlimit);
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#markSupported()
     */
    public boolean markSupported()
    {
      return this.embedded.markSupported();
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    public int read() throws IOException
    {
      int res = this.embedded.read();
      if (res >= 0)
        this.os.write(res);
      return res;
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#read(byte[], int, int)
     */
    public int read(byte[] b, int off, int len) throws IOException
    {
      int res = this.embedded.read(b, off, len);
      if (res >= 0)
        this.os.write(b, off, res);
      return res;
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#read(byte[])
     */
    public int read(byte[] b) throws IOException
    {
      return read(b, 0, b.length);
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#reset()
     */
    public synchronized void reset() throws IOException
    {
      this.embedded.reset();
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#skip(long)
     */
    public long skip(long n) throws IOException
    {
      return this.embedded.skip(n);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    protected Object clone() throws CloneNotSupportedException
    {
      throw new CloneNotSupportedException();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
      return this.embedded.equals(obj);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
      return this.embedded.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return this.embedded.toString();
    }
  }

  private final ServletRequest embedded;
  private final MyStream stream;

  public ServletRequestWrapper(ServletRequest embedded) throws IOException
  {
    this.embedded = embedded;
    this.stream = new MyStream(embedded.getInputStream(), embedded.getContentLength());
  }
  
  public byte[] getBytesRead()
  {
    return this.stream.os.toByteArray();
  }

  public String getLocalAddr()
  {
    return this.embedded.getLocalAddr();
  }

  public String getLocalName()
  {
    return this.embedded.getLocalName();
  }

  public int getLocalPort()
  {
    return this.embedded.getLocalPort();
  }

  public int getRemotePort()
  {
    return this.embedded.getRemotePort();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
   */
  public Object getAttribute(String arg0)
  {
    return this.embedded.getAttribute(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getAttributeNames()
   */
  public Enumeration getAttributeNames()
  {
    return this.embedded.getAttributeNames();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getCharacterEncoding()
   */
  public String getCharacterEncoding()
  {
    return this.embedded.getCharacterEncoding();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getContentLength()
   */
  public int getContentLength()
  {
    return this.embedded.getContentLength();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getContentType()
   */
  public String getContentType()
  {
    return this.embedded.getContentType();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getInputStream()
   */
  public ServletInputStream getInputStream() throws IOException
  {
    return this.stream;
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getLocale()
   */
  public Locale getLocale()
  {
    return this.embedded.getLocale();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getLocales()
   */
  public Enumeration getLocales()
  {
    return this.embedded.getLocales();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
   */
  public String getParameter(String arg0)
  {
    return this.embedded.getParameter(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getParameterMap()
   */
  public Map getParameterMap()
  {
    return this.embedded.getParameterMap();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getParameterNames()
   */
  public Enumeration getParameterNames()
  {
    return this.embedded.getParameterNames();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
   */
  public String[] getParameterValues(String arg0)
  {
    return this.embedded.getParameterValues(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getProtocol()
   */
  public String getProtocol()
  {
    return this.embedded.getProtocol();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getReader()
   */
  public BufferedReader getReader() throws IOException
  {
    return this.embedded.getReader();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
   */
  public String getRealPath(String arg0)
  {
    return this.embedded.getRealPath(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getRemoteAddr()
   */
  public String getRemoteAddr()
  {
    return this.embedded.getRemoteAddr();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getRemoteHost()
   */
  public String getRemoteHost()
  {
    return this.embedded.getRemoteHost();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
   */
  public RequestDispatcher getRequestDispatcher(String arg0)
  {
    return this.embedded.getRequestDispatcher(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getScheme()
   */
  public String getScheme()
  {
    return this.embedded.getScheme();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getServerName()
   */
  public String getServerName()
  {
    return this.embedded.getServerName();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#getServerPort()
   */
  public int getServerPort()
  {
    return this.embedded.getServerPort();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#isSecure()
   */
  public boolean isSecure()
  {
    return this.embedded.isSecure();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
   */
  public void removeAttribute(String arg0)
  {
    this.embedded.removeAttribute(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
   */
  public void setAttribute(String arg0, Object arg1)
  {
    this.embedded.setAttribute(arg0, arg1);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
   */
  public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException
  {
    this.embedded.setCharacterEncoding(arg0);
  }

}
