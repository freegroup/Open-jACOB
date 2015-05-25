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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IBrowserEventHandler;

/**
 * Will be fired if the user clicks in a row of the browser.
 * 
 * @author Andras Herz
 */
public class BrowserActionCollapseRow extends BrowserAction
{

  public boolean isEnabled(IClientContext context, IBrowser browser)
  {
    return browser.getData().recordCount()>0;
  }

  /* 
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getIcon()
   */
  public String getIcon()
  {
    return "minus.png";
  }



  /*
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getTooltipMessageId()
   */
  public String getTooltipId()
  {
    return "BROWSER_ACTION_TOOLTIP_EXPAND";
  }
  
  public String getLabelId()
  {
    return "BUTTON_COMMON_EXPAND";
  }
  
  /* 
   * @see de.tif.jacob.screen.impl.html.BrowserAction#execute(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.impl.html.Browser, java.lang.String)
   */
  public void execute(IClientContext context,final IBrowser browser,final String value)  throws Exception
  {
    HTTPBrowser httpBrowser = (HTTPBrowser)browser;
    
    int index = Integer.parseInt(value);
    IDataBrowserRecord record = browser.getDataRecord(index);
    
    GuiEventHandler handler = httpBrowser.getEventHandler(context);
    if(handler instanceof IBrowserEventHandler)
    {
        Map record2children = httpBrowser.getRecordChildrenMap();
        Map recordMarkerMap= httpBrowser.getRecordMarkerMap();

        IDataBrowserInternal data = httpBrowser.getDataInternal();
        removeChildren(data,recordMarkerMap, record2children,record);
        httpBrowser.resetCache();
    }
  }
  
  public void removeChildren(IDataBrowserInternal browser,Map recordMarkerMap, Map record2children, IDataBrowserRecord record)
  {
    // erst alle Kinder von diesem Record aus dem Browser entfernen
    //
    List children = (List) record2children.get(record);
    if(children==null|| children==HTTPBrowser.NO_CHILDREN || children ==HTTPBrowser.UNKNOWN_CHILDREN)
      return;
    
    try
    {
      Iterator iter = children.iterator();
      while(iter.hasNext())
      {
        IDataBrowserRecord child = (IDataBrowserRecord)iter.next();
        removeChildren(browser,recordMarkerMap, record2children, child);
        browser.removeRecord(child);
        recordMarkerMap.remove(child);
      }
    }
    finally
    {
      // Vormerken das der Browser Records hat, ich diese aber icht mehr kenne
      record2children.put(record, HTTPBrowser.UNKNOWN_CHILDREN);
    }
  }
}
