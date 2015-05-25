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

package de.tif.jacob.screen.impl.tag;

import javax.servlet.jsp.JspException;

import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class I18N2Unicode extends AbstractI18NSupport
{
  /**
   * 
   */
  private static final long serialVersionUID = -2091509312857233872L;
  
  public static transient final String RCS_ID = "$Id: I18N2Unicode.java,v 1.2 2009/09/15 20:44:24 ibissw Exp $";
  public static transient final String RCS_REV = "$Revision: 1.2 $";

  private String id = null;
  
  /**
   *
   */
  public int doStartTag() throws JspException
  {
    try
    {
      pageContext.getOut().print(StringUtil.convertToUnicodeString(getLocalized(this.id)));
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
  
  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }
}