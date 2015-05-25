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

import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;

/**
 * Base interface for Browser actions. Added actions to a browser will be shown in the 
 * right hands side of the browser header.
 * 
 * @author Andras Herz
 */
public abstract class BrowserAction extends AbstractAction
{
  // Typische Action welche in der Engine verwendet werden. Die Teile sind alle ThreadSave. Aus diesem Grund werden diese hier angelegt um ein stetises "new" zu
  // vermeiden.
  //
  public static final BrowserAction ACTION_DELETE   = new BrowserActionDelete();
  public static final BrowserAction ACTION_UPDATE   = new BrowserActionUpdate();
  public static final BrowserAction ACTION_ADD      = new BrowserActionAdd();
  public static final BrowserAction ACTION_CLICK    = new BrowserActionClick();
  public static final BrowserAction ACTION_CLICK_AND_CONFIRM = new BrowserActionClickAndConfirm(); // The user clicks in a row. This is not a action with a icon
  public static final BrowserAction ACTION_EXCEL    = new BrowserActionExcel();
  public static final BrowserAction ACTION_SORT_DESC = new BrowserActionSortDesc();
  public static final BrowserAction ACTION_SORT_ASC  = new BrowserActionSortAsc();
  public static final BrowserAction ACTION_SORT_NONE = new BrowserActionSortNone();
  public static final BrowserAction ACTION_ADD_COLUMN      = new BrowserActionAddInformBrowserColumn();
  public static final BrowserAction ACTION_EXPAND_ROW = new BrowserActionExpandRow();
  public static final BrowserAction ACTION_COLLAPSE_ROW = new BrowserActionCollapseRow();
  public static final BrowserAction ACTION_COLUMN_PICKER = new BrowserActionColumnPicker();
  public static final BrowserAction ACTION_DRAG_DROP = new BrowserActionDragDrop();        // DragDrop Event handler for the TreeBrowser
  public static final BrowserAction ACTION_REFRESH = new BrowserActionRefresh();
  public static final BrowserAction ACTION_SELECT_ALL = new BrowserActionSelectAll(); // The user clicks in a row. This is not a action with a icon
  public static final BrowserAction ACTION_TOGGLE_SELECTION=new BrowserActionToggleSelection(); // The user clicks in a row. This is not a action with a icon
  public static final BrowserAction ACTION_UNLIMITED_SEARCH = new BrowserActionUnlimitedSearch();
  public static final BrowserAction ACTION_ADD_CONSTRAINTS  = new BrowserActionAddSearchConstraint();
  public static final BrowserAction ACTION_LAZY_BACKFILL = new BrowserActionLazyBackfill();
  public static final BrowserAction ACTION_CANCEL_REPORT = new BrowserActionCancelReport();
  public static final BrowserAction ACTION_SAVE_REPORT = new BrowserActionSaveReport();
  public static final BrowserAction ACTION_SAVE_SHOW_REPORT = new BrowserActionSaveAndShowReport();
  public static final BrowserAction ACTION_DELETE_REPORT_COLUMN = new BrowserActionDeleteReportColumn();
  public static final BrowserAction ACTION_SHIFT_COLUMN_LEFT = new BrowserActionShiftLeftReportColumn();
  public static final BrowserAction ACTION_SHIFT_COLUMN_RIGHT = new BrowserActionShiftRightReportColumn();
  public static final BrowserAction ACTION_SHOW_BROWSER= new BrowserActionShow();            // make the SearchBrowser visible
  
  public boolean isEnabled(IClientContext context, IBrowser browser)
  {
    return true;
  }
  
  public final void execute(IClientContext context, IGuiElement element, String value) throws Exception
  {
    // Cast auf ein Vernünftiges Interface
    this.execute(context, (IBrowser)element, value);
  }

  /**
   * The action method. This will be called if the use clicks on the browser action icon.
   * 
   * @param context The current context if the application
   * @param browser The browser assigned to the action
   * @param value The value of the action. In the case of a row action it is the index of the row.
   * @throws Exception w
   */
  public abstract void   execute(IClientContext context, IBrowser browser, String value) throws Exception;
}
