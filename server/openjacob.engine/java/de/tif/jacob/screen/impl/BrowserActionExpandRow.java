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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.tif.jacob.core.data.IBrowserRecordList;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.impl.DataRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IBrowserEventHandler;
import de.tif.jacob.screen.impl.HTTPBrowser.Marker;

/**
 * Will be fired if the user clicks in a row of the browser.
 * 
 * @author Andras Herz
 */
public class BrowserActionExpandRow extends BrowserAction
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
    return "plus.png";
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
      IBrowserEventHandler browserHandler = (IBrowserEventHandler)handler;
      IBrowserRecordList children= browserHandler.getChildren(context,browser,record);
      if(children!=null && children.recordCount()>0)
      {
        Map childrenMap = httpBrowser.getRecordChildrenMap();
        Map markerMap     = httpBrowser.getRecordMarkerMap();
        
        List childrenArray =(List) childrenMap.get(record);
        Marker marker = (Marker)markerMap.get(record);
        
        if(childrenArray==HTTPBrowser.UNKNOWN_CHILDREN || childrenArray==null || childrenArray==HTTPBrowser.NO_CHILDREN)
          childrenMap.put(record,childrenArray=new ArrayList());
        
        if(marker==null)
          markerMap.put(record,marker=new Marker(0,null,null,false,false));

        
        for (int i=(children.recordCount()-1);i>=0;i-- )
        {
          DataRecord child = (DataRecord)children.getRecord(i);
          httpBrowser.getDataInternal().addRecord(index+1,child);
          childrenArray.add(child);
          markerMap.put(child,new Marker(marker.deep+1,null,null,false,false));
        }
        httpBrowser.resetCache();
      }
    }
  }
}
