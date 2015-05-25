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

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IBrowserEventHandler;

/**
 * Will be fired if the user clicks in a row of the browser.
 * 
 * @author Andras Herz
 */
public class BrowserActionClickAndConfirm extends BrowserAction
{

  /*
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getTooltipMessageId()
   */
  public String getTooltipId()
  {
    return "BROWSER_ACTION_TOOLTIP_CLICKANDCONFIRM";
  }
  
  public String getLabelId()
  {
    return "BROWSER_ACTION_TOOLTIP_CLICKANDCONFIRM";
  }

  public String getId()
  {
    // muss die gleiche ID haben wie "BrowserActionClick"
    return "click";
  }

  /* 
   * @see de.tif.jacob.screen.impl.html.BrowserAction#execute(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.impl.html.Browser, java.lang.String)
   */
  public void execute(IClientContext context,final IBrowser browser, String value)  throws Exception
  {
    final int index =  Integer.parseInt(value);
    
    // The Hook can veto the backfill of a IDataBrowserRecord
    //
    HTTPBrowser httpBrowser = (HTTPBrowser)browser;
    GuiEventHandler obj = httpBrowser.getEventHandler(context);
    if(obj instanceof IBrowserEventHandler)
    {
      IBrowserEventHandler handler = (IBrowserEventHandler)obj;
      if(!handler.beforeAction(context,browser,browser.getDataRecord(index)))
        return;
    }

    if(((HTTPGroup)context.getGroup()).hasChildInDataStatus(context, IGuiElement.UPDATE) || ((HTTPGroup)context.getGroup()).hasChildInDataStatus(context, IGuiElement.NEW))
    {
      IOkCancelDialog dialog = context.createOkCancelDialog(new CoreMessage("CONFIRM_UNSELECT_RECORD"),new IOkCancelDialogCallback()
			          {
			        public void onOk(IClientContext context) throws Exception
			        {
			          // close the pending transaction.
			          //
			          IDataTableRecord currentRecord = context.getSelectedRecord();
			          if(currentRecord!=null && currentRecord.getCurrentTransaction()!=null)
			            currentRecord.getCurrentTransaction().close();
			          
                // select a recod in the DataBrowser
                //
			          browser.setSelectedRecordIndex(context,index);
			        }
			        public void onCancel(IClientContext context) throws Exception {}
			      });
      dialog.show();
    }
    else
    {  
      // select a recod in the DataBrowser
      //
      browser.setSelectedRecordIndex(context,index);
    }
  }
}
