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

import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 * 
 * @author Andras Herz
 */
public class BrowserActionLazyBackfill extends BrowserAction
{

  // not required. This is a non visible action
  public String getLabelId()
  {
    return null;
  }

  // not required. This is a non visible action
  public String getTooltipId()
  {
    return null;
  }

  public void execute(IClientContext context, IBrowser browser, String value)  throws Exception
  {
    HTTPDataBrowserProvider provider = (HTTPDataBrowserProvider)browser;
   
    IDataBrowserInternal dataBrowser = provider.getDataInternal();
    // IBIS: HACK: Diese Abfrage wird für VE 3.0 benötigt. Weiss mir nicht anders zu helfen ;-( 
    if (!dataBrowser.isDisableSearchIFB())
    {
      try
      {
        dataBrowser.searchIFB(browser.getGroupTableAlias());
      }
      catch (Exception ex)
      {
        // avoid exceptions during calculateHtml()
        dataBrowser.clear();
        ExceptionHandler.handle(context, ex);
      }
      browser.setData(context, dataBrowser);
    }
    
    if(browser instanceof HTTPBrowser)
    {
      HTTPBrowser b = (HTTPBrowser)browser;
      b.removeRowAction(BrowserAction.ACTION_DELETE);
      b.removeHiddenAction(BrowserAction.ACTION_UPDATE);
      b.addHiddenAction(BrowserAction.ACTION_CLICK);
      if (b.getDataInternal().recordCount() > 1)
      {
        b.addHiddenAction(BrowserAction.ACTION_SORT_ASC);
        b.addHiddenAction(BrowserAction.ACTION_SORT_DESC);
        b.addHiddenAction(BrowserAction.ACTION_SORT_NONE);
      }
    }
    else  if(browser instanceof HTTPTableListBox)
    {
      HTTPTableListBox b = (HTTPTableListBox)browser;
      b.removeRowAction(BrowserAction.ACTION_DELETE);
      b.removeHiddenAction(BrowserAction.ACTION_UPDATE);
      b.addHiddenAction(BrowserAction.ACTION_CLICK);
      if (b.getDataInternal().recordCount() > 1)
      {
        b.addHiddenAction(BrowserAction.ACTION_SORT_ASC);
        b.addHiddenAction(BrowserAction.ACTION_SORT_DESC);
        b.addHiddenAction(BrowserAction.ACTION_SORT_NONE);
      }
    }
    else
    {
      throw new RuntimeException("Unknown interface ["+browser.getClass().getName()+"] for BrowserActionLazyBackfill");
    }
    }
}

