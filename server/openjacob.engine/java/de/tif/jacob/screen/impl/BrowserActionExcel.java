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

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IExcelDialog;
import de.tif.jacob.screen.event.IBrowserEventHandler;

/**
 * Will be fored if hte user clicks on the 'Export to Excel' icon in the browser
 * header bar.
 * 
 * @author Andras Herz
 */
public class BrowserActionExcel extends BrowserAction
{
 
  public boolean isEnabled(IClientContext context, IBrowser browser)
  {
    return browser.getData().recordCount()>0;
  }

  public boolean reloadPage()
  {
    return false;
  }
 
  /* 
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getIcon()
   */
  public String getIcon()
  {
    return "20x20_excel.png";
  }


  public String getLabelId()
  {
    return "BUTTON_COMMON_EXCELEXPORT";
  }

  /*
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getTooltipMessageId()
   */
  public String getTooltipId()
  {
    return "BROWSER_ACTION_TOOLTIP_EXCELEXPORT";
  }
  
  /* 
   * @see de.tif.jacob.screen.impl.html.BrowserAction#execute(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.impl.html.Browser, java.lang.String)
   */
  public void execute(IClientContext context, IBrowser b, String value)  throws Exception
  {
    HTTPBrowser browser = (HTTPBrowser)b;
    // create the header for the selection grid dialog
    //
    String[] header = new String[browser.getColumns().size()];
    for(int i=0;i<browser.getColumns().size();i++)
    {
      IBrowserField element = (IBrowserField) browser.getColumns().get(i);
      header[i] = I18N.localizeLabel(element.getLabel(), context);
    }
    
    IBrowserEventHandler browserFilter=(IBrowserEventHandler)((GuiElement)browser).getEventHandler(context);
    String[][] rows = new String[browser.getData().recordCount()][];
    if(browserFilter!=null)
    {
      for(int row=0 ;row<browser.getData().recordCount();row++)
      {
        rows[row]= new String[header.length];
        for(int column=0; column<browser.getColumns().size();column++)
        {
          IDataBrowserRecord record = (IDataBrowserRecord) browser.getDataInternal().getGuiSortStrategy().getRecord(row);
          rows[row][column] = browserFilter.filterCell(context, browser, row, column, record, record.getStringValue(column, context.getLocale()));
        }
      }
    }
    else
    {
      for(int row=0 ;row<browser.getData().recordCount();row++)
      {
        rows[row]= new String[header.length];
        for(int column=0; column<browser.getColumns().size();column++)
        {
          IDataBrowserRecord record = (IDataBrowserRecord) browser.getDataInternal().getGuiSortStrategy().getRecord(row);
          rows[row][column]=record.getStringValue(column, context.getLocale());
        }
      }
    }
    
    // create the dialog with the header / data
    //
    IExcelDialog dialog = context.createExcelDialog();
    dialog.setHeader(header);
    dialog.setData(rows);
    dialog.show();
		((HTTPApplication)context.getApplication()).doForward(dialog);
  }

}
