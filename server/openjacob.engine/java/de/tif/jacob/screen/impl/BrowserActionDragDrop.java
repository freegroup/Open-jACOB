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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataBrowser;
import de.tif.jacob.core.data.impl.DataTable;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IDragDropListener;

/**
 * Will be fired if the user clicks in a row of the browser.
 * 
 * @author Andras Herz
 */
public class BrowserActionDragDrop extends BrowserAction
{

  /**
   * DragDrop muss seine eigene, fest ID habe, da diese im JAvaScript referenziert wird. Die autom. generierte Id ist hier 
   * nicht hilfreich.
   */
  public String getId()
  {
    return "dragdrop";
  }
  
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
    
    // Browser-Hook unterstuetzt kein DragDrop
    //
    GuiEventHandler handler = httpBrowser.getEventHandler(context);
    if(!(handler instanceof IDragDropListener))
      return;
    IDragDropListener dropListener = (IDragDropListener)handler;
    
    String[] ids = value.split("[:]");
    int dragId = Integer.parseInt(ids[0]);
    int dropId = Integer.parseInt(ids[1]);
    
    IDataBrowserRecord dragBrowserRecord = httpBrowser.getDataRecord(dragId);

    // Der Record wurde auf eine leere Browserzeile geworfen
    if(dropId>=browser.getData().recordCount())
    {
      dropListener.drop(context,browser,dragBrowserRecord,null);
    }
    else
    {
      IDataBrowserRecord dropBrowserRecord = httpBrowser.getDataRecord(dropId);
      dropListener.drop(context,browser,dragBrowserRecord,dropBrowserRecord);
    }
  }
}
