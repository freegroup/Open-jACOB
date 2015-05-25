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

import java.util.List;
import java.util.Properties;

import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.EntryPointUrl;
import de.tif.jacob.entrypoint.ICmdEntryPoint;
import de.tif.jacob.report.IReport;
import de.tif.jacob.report.ReportManager;
import de.tif.jacob.transformer.ITransformer;
import de.tif.jacob.transformer.TransformerFactory;
import de.tif.jacob.util.clazz.ClassUtil;

/**
 *
 */
public class ReportListWebQuery implements ICmdEntryPoint
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
    List reports=ReportManager.getReports(context.getApplicationDefinition(),context.getUser());

    String entryPointName = ClassUtil.getShortClassName(ExcelReportWebQuery.class);
    ITransformer trans = TransformerFactory.get(getMimeType(context,properties));
    String[]   header = new String[]{"Report Name","WebQuery URL"};
    String[][] data   = new String[reports.size()][2];
    for(int i=0; i<reports.size();i++)
    {
      IReport report=(IReport)reports.get(i);
      Properties props = new Properties();
      props.put("reportId",report.getGUID());
      data[i][0]=report.getName();
      EntryPointUrl url=new EntryPointUrl(EntryPointUrl.ENTRYPOINT_CMD,entryPointName,props);
      data[i][1]=url.toURL(context);
    }
    trans.transform(context.getStream(),header,data);
  }
}
