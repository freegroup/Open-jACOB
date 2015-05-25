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

package de.tif.jacob.screen.impl;

import java.io.ByteArrayOutputStream;

import de.tif.jacob.report.IReport;
import de.tif.jacob.screen.IClientContext;

/**
 * Will be fired if the user clicks in a row of the browser.
 * 
 * @author Andreas Sonntag
 */
public class BrowserActionSaveAndShowReport extends BrowserActionSaveReport
{
  
  public String getLabelId()
  {
    return "BUTTON_COMMON_SAVE_AND_SHOW_AS_EXCEL";
  }

  public String getTooltipId()
  {
    return "BUTTON_COMMON_SAVE_AND_SHOW";
  }

  // 13.4.2010: ASML request for showing Excel
  protected void afterSaveAction(IClientContext context, IReport report) throws Exception
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String mimeType = "application/excel";

    report.render(out, mimeType);
    out.flush();
    context.createDocumentDialog(mimeType, report.getName() + ".xls", out.toByteArray()).show();
  }

}
