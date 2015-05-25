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

import de.tif.jacob.core.Context;
import de.tif.jacob.core.Property;
import de.tif.jacob.i18n.I18N;

/**
 *
 */
public class DialogTitle extends AbstractI18NSupport
{
  /**
   * 
   */
  private static final long serialVersionUID = -7905379478781986357L;
  
  public static transient final String RCS_ID = "$Id: DialogTitle.java,v 1.5 2009/09/15 21:00:06 ibissw Exp $";
  public static transient final String RCS_REV = "$Revision: 1.5 $";

  private String id = null;

  /**
   *
   */
  public int doStartTag() throws JspException
  {
    try
    {
      pageContext.getOut().print(Property.WINDOW_TITLE_PREFIX.getValue()+ getLocalized(id));
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

  public void setId(String var)
  {
    this.id = var;
  }
}