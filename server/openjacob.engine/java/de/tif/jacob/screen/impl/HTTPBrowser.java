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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.tif.jacob.core.data.IBrowserRecordList;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IBrowserEventHandler;

/**
 *
 */
public interface HTTPBrowser extends IBrowser, HTTPDataBrowserProvider
{
  // use Collections.unmodifiableList to make sure that we do not have a memory leak
  
  public final static List NO_CHILDREN = Collections.unmodifiableList(new ArrayList());

  public final static List UNKNOWN_CHILDREN = Collections.unmodifiableList(new ArrayList());
  
  /**
   * This object is an internal flag that the browser does not provide a tree structure,
   * hence either connectByKey is existing nor method
   * {@link IBrowserEventHandler#getChildren(IClientContext, IBrowser, IDataBrowserRecord)}
   * has been overwritten by the application programmer.
   */
  public final static IBrowserRecordList NO_TREE_BROWSER = new IBrowserRecordList()
  {
    public IDataBrowserRecord getRecord(int index) throws IndexOutOfBoundsException
    {
      throw new IndexOutOfBoundsException();
    }

    public int recordCount()
    {
      return 0;
    }
  }; 
  

 public class Marker
  {
    public String error=null;
    public String warning=null;
    public boolean emphasize = false;
    public boolean checked = false;
    public int deep=0;
    public Marker(int deep,String error, String warning, boolean emphasize, boolean checked)
    {
      this.deep=deep;
      this.error = error;
      this.emphasize = emphasize;
      this.checked = checked;
      this.warning = warning;
    }
    public boolean allFalse()
    {
      return deep==0 && error==null && warning==null && emphasize ==false && checked==false;
    }
  }


  /**
   * Check the row with an checkbox tick.<br>
   * The row can now be retrieved with the selection API of 
   * the browser.
   * @param index
   */
  public void checkRow(int index);
	public IKey getConnectByKey();
  public GuiEventHandler getEventHandler(IClientContext context);
  public Map getRecordChildrenMap();
  public Map getRecordMarkerMap();
  public boolean isColumnConfigureable(int columnIndex);
  public boolean isColumnVisible(int columnIndex);

  public void addHiddenAction(BrowserAction action);
  public void removeHiddenAction(BrowserAction action);
  
  public void addRowAction(BrowserAction action);
  public void removeRowAction(BrowserAction action);
  
  public void resetCache();

  public void scrollTo(long x, long y);

  public void setColumnVisible(int columnIndex, boolean flag);

  /**
   * Toggle the tick of the row in the browser.
   * 
   * @param index
   */
  public void toggleRow(int index);

  /**
   * Remove the tick of the row in the browser.
   * 
   * @param index
   */
  public void uncheckRow(int index); 
}
