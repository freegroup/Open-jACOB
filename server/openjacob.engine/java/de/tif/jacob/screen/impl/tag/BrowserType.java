
package de.tif.jacob.screen.impl.tag;

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
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

public class BrowserType extends AbstractTagSupport
{
  /**
   * 
   */
  private static final long serialVersionUID = -2349040517475700874L;
  
  static public final transient String RCS_ID = "$Id: BrowserType.java,v 1.4 2009/09/15 20:44:24 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  public int doStartTag() throws JspException
  {
    try
    {
      pageContext.getOut().print(getType((HttpServletRequest) this.pageContext.getRequest()));
    }
    catch (IOException exc)
    {
      handleStartTagException(exc);
    }
    return SKIP_BODY;
  }

  /**
   * Returns an id for the client browser type.<br>
   * ie = InternetExplorer<br>
   * ns = Firefox or Netscape<br>
   * op = Opera<br>
   * 
   */
  public static String getType(HttpServletRequest request)
  {
    String browserType = request.getHeader("User-Agent");
    if (browserType != null)
    {
      browserType = browserType.toLowerCase();

      if (browserType.indexOf("opera") != -1)
      {
        return "op";
      }
      else if (browserType.indexOf("msie") != -1)
      {
        return "ie";
      }
      else
      {
        return "ns";
      }
    }

    // return ie by default
    return "ie";
  }

  /**
   *
   */
  public int doEndTag()
  {
    return EVAL_PAGE;
  }
}
