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
import java.io.File;

import javax.servlet.jsp.JspException;

import de.tif.jacob.deployment.DeployEntry;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.util.StringUtil;

public class FavIcon extends AbstractTagSupport
{
  /**
   * 
   */
  private static final long serialVersionUID = 8011483526825475364L;
  
  public static transient final String RCS_ID = "$Id: FavIcon.java,v 1.4 2009/09/15 20:44:24 ibissw Exp $";
  public static transient final String RCS_REV = "$Revision: 1.4 $";

  private static final String PROPERTY = "faviconpath";
  private static final String DEFAULT_PATH = "./favicon.ico";

  private String applName = null;
  private String applVersion = null;

  /**
     *
     */
  public int doStartTag() throws JspException
  {
    try
    {
      String iconPath = DEFAULT_PATH;
      if (!StringUtil.emptyOrNull(this.applName) && !StringUtil.emptyOrNull(this.applVersion))
      {
        DeployEntry entry = DeployManager.getDeployEntry(this.applName, this.applVersion);
        if (entry != null)
        {
          iconPath = (String) entry.getProperty(PROPERTY);
          if (iconPath == null)
          {
            File file = new File(entry.getDeployWebPath() + "/favicon.ico");
            iconPath = file.exists() ? "./application/" + entry.getName() + "/" + entry.getVersion().toShortString() + "/favicon.ico" : DEFAULT_PATH;
            entry.setProperty(PROPERTY, iconPath);
          }
        }
      }
      if (iconPath != null)
        pageContext.getOut().print(iconPath);
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

  /**
   * @return the applName
   */
  public String getApplName()
  {
    return applName;
  }

  /**
   * @param applName
   *          the applName to set
   */
  public void setApplName(String applName)
  {
    this.applName = applName;
  }

  /**
   * @return the applVersion
   */
  public String getApplVersion()
  {
    return applVersion;
  }

  /**
   * @param applVersion
   *          the applVersion to set
   */
  public void setApplVersion(String applVersion)
  {
    this.applVersion = applVersion;
  }
}
