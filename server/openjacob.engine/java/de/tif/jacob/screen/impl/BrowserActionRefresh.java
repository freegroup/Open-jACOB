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

import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.internal.IDataAccessorInternal;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 * Will be called if the user clicks on the 'repeat last search' icon in the top of the
 * browser header bar.
 * 
 * @author Andras Herz
 */
public class BrowserActionRefresh extends BrowserAction
{
  public boolean isEnabled(IClientContext context, IBrowser browser)
  {
    return ((HTTPBrowser)browser).getDataInternal().isSearchPerformed();
  }

  /* 
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getIcon()
   */
  public String getIcon()
  {
    return "20x20_refresh.png";
  }



  public String getLabelId()
  {
    return "BUTTON_COMMON_REFRESH";
  }

  /*
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getTooltipMessageId()
   */
  public String getTooltipId()
  {
    return "BROWSER_ACTION_TOOLTIP_REFRESH";
  }
  
  /* 
   * @see de.tif.jacob.screen.impl.html.BrowserAction#execute(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.impl.html.Browser, java.lang.String)
   */
  public void execute(IClientContext context, IBrowser browser, String value)  throws Exception
  {
    IDataBrowserInternal dataBrowser = (IDataBrowserInternal) context.getDataBrowser();
    if(dataBrowser != null && dataBrowser.getLastSearchConstraints() != null)
    {  
	    IDataAccessorInternal accessor = (IDataAccessorInternal) context.getDataAccessor();
      
      // enter search criteria for the browser
      //
      accessor.qbeClearAll();
      accessor.qbeSetConstraints(dataBrowser.getLastSearchConstraints());
	    
	    // start the search itself
	    //
      dataBrowser.setMaxRecords(Property.BROWSER_COMMON_MAX_RECORDS.getIntValue());
      dataBrowser.search(browser.getData().getRelationSet(),browser.getData().getFillDirection());
	    browser.setData(context,dataBrowser);
    }
  }

}
