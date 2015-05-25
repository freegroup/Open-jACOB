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

package de.tif.jacob.entrypoint.cmd;

import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;
import de.tif.jacob.report.IReport;
import de.tif.jacob.report.ReportManager;

/**
 *
 */
public class ExcelReportWebQuery implements ICmdEntryPoint
{

  /* 
   * @see de.tif.jacob.entrypoint.ICmdEntryPoint#getMimeType()
   */
  public String getMimeType(CmdEntryPointContext context, Properties properties)
  {
    return "text/html";
  }

  /* 
   * @see de.tif.jacob.entrypoint.ICmdEntryPoint#enter(de.tif.jacob.entrypoint.CmdEntryPointContext, java.util.Properties)
   */
  public void enter(CmdEntryPointContext context, Properties properties) throws Exception
  {
    String reportId = properties.getProperty("reportId");
    IReport report=ReportManager.getReport(reportId);
    if(!report.getApplication().getName().equals(context.getApplicationDefinition().getName()))
    {
//      System.out.println("ReportApp:"+report.getApplication().getName());
//      System.out.println("ContextApp:"+context.getApplicationDefinition().getName());
      return /*silently*/;
    }
    
    // locale specified?
    // note: If not, the owner's locale will be used
    //
    Locale locale = null; 
    String localeString = properties.getProperty("locale");
    if (localeString != null && localeString.length() > 0)
    {
      String[] langParts = StringUtils.split(localeString, "_");
      locale = new Locale(langParts[0], langParts.length > 1 ? langParts[1] : "");
    }
    report.render(context.getStream(), getMimeType(context, properties), locale);
  }
}
