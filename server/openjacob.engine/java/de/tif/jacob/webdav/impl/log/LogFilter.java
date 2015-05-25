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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.slide.webdav.logger.StatusHttpServletResponseWrapper;
import org.apache.slide.webdav.util.WebdavStatus;

/**
 * A servlet filter for one-line-per-request logging. Log format and where to
 * output can be configured in the deployment descriptors.
 * 
 * @version $Revision: 1.1 $
 */
public class LogFilter implements Filter
{
  static public final transient String RCS_ID = "$Id: LogFilter.java,v 1.1 2007/01/19 09:50:41 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private ServletContext context;
  private String logFormat = "%T, %t, %P, %m, %s \"%l\", %i, %p";
  private boolean contentToConsole = false;
  private boolean outputToConsole = true;
  private boolean outputToServletLog = false;
  private boolean outputToFile = false;
  private String outputFilePath = null;
  private File outputFile = null;
  private BufferedOutputStream fout = null;
  private DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

  // log elements

  /**
   * Interface implementation
   * 
   * @param config
   *          a FilterConfig
   * 
   * @throws ServletException
   * 
   */
  public void init(FilterConfig config) throws ServletException
  {
    this.context = config.getServletContext();

    // get the init parms
    String p = config.getInitParameter("logFormat");
    if (p != null && !"".equals(p))
      logFormat = p;
    p = config.getInitParameter("contentToConsole");
    if ("true".equalsIgnoreCase(p))
      contentToConsole = true;
    p = config.getInitParameter("outputToConsole");
    if ("false".equalsIgnoreCase(p))
      outputToConsole = false;
    p = config.getInitParameter("outputToServletLog");
    if ("true".equalsIgnoreCase(p))
      outputToServletLog = true;
    p = config.getInitParameter("outputToFile");
    if (p != null && !"".equals(p))
    {
      outputFilePath = p;
      outputFile = new File(outputFilePath);
      try
      {
        if (outputFile.canWrite() || outputFile.createNewFile())
        {
          fout = new BufferedOutputStream(new FileOutputStream(outputFilePath, true));
          outputToFile = true;
        }
      }
      catch (IOException e)
      {
      }
    }
  }

  /**
   * Interface implementation
   * 
   * @param req
   *          a ServletRequest
   * @param resp
   *          a ServletResponse
   * @param chain
   *          a FilterChain
   * 
   * @throws IOException
   * @throws ServletException
   * 
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
  {
    try
    {
      HttpServletRequest req = (HttpServletRequest) request;
      StatusHttpServletResponseWrapper resp = new StatusHttpServletResponseWrapper((HttpServletResponse) response);
      long start = System.currentTimeMillis();

      // incomming
      String thread = Thread.currentThread().getName();
      String useragent = req.getHeader("User-Agent") != null ? req.getHeader("User-Agent") : "<user-agent-unknown>";
      String datetime = df.format(new Date());
      String method = req.getMethod();
      String uri = req.getRequestURI();
      Principal p = req.getUserPrincipal();
      String principal = (p != null ? p.getName() : null);
      // with tomcat p.getName() may be null too
      if (principal == null)
        principal = "unauthenticated";
      String contentlength = req.getHeader("Content-Length");
      String contenttype = req.getHeader("Content-Type");
      if (contentlength == null)
        contentlength = "-";

      String reqcontent = null;
      String respcontent = null;
      ServletRequestWrapper wrapperReq = null;
      HttpServletResponseWrapper wrapperResp = null;
      if (contentToConsole)
      {
        if (req instanceof HttpServletRequest)
          wrapperReq = new HttpServletRequestWrapper(req);
        else
          wrapperReq = new ServletRequestWrapper(req);

        wrapperResp = new HttpServletResponseWrapper(resp);
      }

      // next please!
      chain.doFilter(req, resp);

      if (contentToConsole)
      {
        if (contenttype != null && contenttype.indexOf("text/xml") != -1)
        {
          String encoding = req.getCharacterEncoding();
          if (encoding != null)
            reqcontent = new String(wrapperReq.getBytesRead(), encoding);
          else
            reqcontent = new String(wrapperReq.getBytesRead());
        }

        if (wrapperResp.getContentType() != null && wrapperResp.getContentType().indexOf("text/xml") != -1)
        {
          String encoding = resp.getCharacterEncoding();
          if (encoding != null)
            respcontent = new String(wrapperResp.getBytesRead(), encoding);
          else
            respcontent = new String(wrapperResp.getBytesRead());
        }
      }

      // way back
      int status = resp.getStatus();
      String message = WebdavStatus.getStatusText(status);
      String detail = resp.getStatusText();
      if (detail == null || "".equals(detail))
        detail = message;
      String path = (String) req.getAttribute("slide_uri"); // set by

      long end = System.currentTimeMillis();
      StringBuffer b = logLine(end - start, status, thread, method, datetime, uri, path, contentlength, principal, message, detail, useragent);

      if (contentToConsole)
      {
        {
          b.append("\n### Request-Header:\n");
          Enumeration en = req.getHeaderNames();
          while (en.hasMoreElements())
          {
            String name = (String) en.nextElement();
            b.append(name);
            b.append(": ");
            boolean first = true;
            Enumeration en2 = req.getHeaders(name);
            while (en2.hasMoreElements())
            {
              if (first)
                first = false;
              else
                b.append(" | ");
              b.append(en2.nextElement());
            }
            b.append("\n");
          }
          b.append("\n");
        }
        if (reqcontent != null)
        {
          b.append("### Request-Content:\n");
          b.append(reqcontent);
          b.append("\n\n");
        }

        {
          b.append("### Response-Header:\n");
          b.append("Content-Type: " + wrapperResp.getContentType() + "\n");
          b.append("Content-Length: " + wrapperResp.getContentLength() + "\n");
          Iterator it = wrapperResp.getHeaderNames();
          while (it.hasNext())
          {
            String name = (String) it.next();
            b.append(name);
            b.append(": ");
            boolean first = true;
            Iterator it2 = wrapperResp.getHeaders(name);
            while (it2.hasNext())
            {
              if (first)
                first = false;
              else
                b.append(" | ");
              b.append(it2.next());
            }
            b.append("\n");
          }
          b.append("\n");
        }
        if (respcontent != null)
        {
          b.append("### Response-Content:\n");
          b.append(respcontent);
          b.append("\n\n");
        }
      }

      // and flush log data
      if (outputToConsole)
        System.out.println(b.toString());
      if (outputToServletLog)
        context.log(b.toString());
      if (outputToFile)
      {
        b.append("\n");
        fout.write(b.toString().getBytes("UTF-8"));
        fout.flush();
      }
    }
    catch (RuntimeException ex)
    {
      ex.printStackTrace();
      throw ex;
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
      throw ex;
    }
    catch (ServletException ex)
    {
      ex.printStackTrace();
      throw ex;
    }
  }

  /**
   * Log one line.
   * 
   * @param req
   *          a XHttpServletRequestFacade
   * @param resp
   *          a XHttpServletResponseFacade
   * @param elapsed
   *          a long
   * 
   * @throws IOException
   * 
   */
  private StringBuffer logLine(long elapsed, int status, String thread, String method, String datetime, String uri, String path, String contentlength,
      String principal, String message, String detail, String useragent) throws IOException
  {

    StringBuffer b = new StringBuffer(logFormat);
    int i;
    i = b.toString().indexOf("%T");
    if (i >= 0)
      b.replace(i, i + 2, thread);
    i = b.toString().indexOf("%t");
    if (i >= 0)
      b.replace(i, i + 2, datetime);
    i = b.toString().indexOf("%P");
    if (i >= 0)
      b.replace(i, i + 2, principal);
    i = b.toString().indexOf("%m");
    if (i >= 0)
      b.replace(i, i + 2, method);
    i = b.toString().indexOf("%s");
    if (i >= 0)
      b.replace(i, i + 2, String.valueOf(status));
    i = b.toString().indexOf("%l");
    if (i >= 0)
      b.replace(i, i + 2, message);
    i = b.toString().indexOf("%L");
    if (i >= 0)
      b.replace(i, i + 2, detail);
    i = b.toString().indexOf("%i");
    if (i >= 0)
      b.replace(i, i + 2, String.valueOf(elapsed) + " ms");
    i = b.toString().indexOf("%p");
    if (i >= 0)
      b.replace(i, i + 2, path);
    i = b.toString().indexOf("%u");
    if (i >= 0)
      b.replace(i, i + 2, uri);
    i = b.toString().indexOf("%x");
    if (i >= 0)
      b.replace(i, i + 2, contentlength);
    i = b.toString().indexOf("%A");
    if (i >= 0)
      b.replace(i, i + 2, useragent);

    return b;
  }

  /**
   * Interface implementation.
   * 
   */
  public void destroy()
  {
    try
    {
      if (outputToFile)
        fout.close();
    }
    catch (IOException e)
    {
    }
  }
}
