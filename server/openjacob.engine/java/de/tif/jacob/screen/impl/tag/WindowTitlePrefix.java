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
import javax.servlet.jsp.JspException;

import de.tif.jacob.util.StringUtil;

public class WindowTitlePrefix extends AbstractTagSupport
{
  /**
   * 
   */
  private static final long serialVersionUID = 1006573660573393419L;
  
  public static transient final String RCS_ID = "$Id: WindowTitlePrefix.java,v 1.4 2009/09/15 20:44:24 ibissw Exp $";
  public static transient final String RCS_REV = "$Revision: 1.4 $";

  private String applName = null;

  /**
     *
     */
  public int doStartTag() throws JspException
  {
    try
    {
      String prefix;
      if (StringUtil.emptyOrNull(this.applName))
        prefix = de.tif.jacob.core.Property.WINDOW_TITLE_PREFIX.getValue();
      else
        prefix = de.tif.jacob.core.Property.WINDOW_TITLE_PREFIX.getValue(this.applName);
      if (prefix != null)
        pageContext.getOut().print(prefix);
    }
    catch (Exception exc)
    {
      handleStartTagException(exc);
    }
    return SKIP_BODY;
  }

  /**
     *
     */
  public int doEndTag()
  {
    return EVAL_PAGE;
  }

  public String getApplName()
  {
    return applName;
  }

  public void setApplName(String applName)
  {
    this.applName = applName;
  }
}
