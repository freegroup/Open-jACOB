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

package de.tif.jacob.screen.event;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * Abstract event handler class for table list boxes. Derived implementations of this
 * event handler class have to be used to "hook" application-specific business
 * logic to table list boxes.
 * 
 * @author Andreas Herz
 */
public abstract class ITableListBoxEventHandler extends IGroupMemberEventHandler
{
  static public final transient String RCS_ID = "$Id: ITableListBoxEventHandler.java,v 1.7 2010/12/08 17:01:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.7 $";

  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    // redirect zu der Methode mit einem Interface welches vom AppProgrammiere später kein casting erforder
    //
    onGroupStatusChanged(context,state,(ITableListBox)element);
  }

  /**
   * This event method will be called, if the status of the corresponding group
   * has been changed. Derived event handlers could overwrite this method, e.g.
   * to enable/disable GUI elements in relation to the group state. <br>
   * Possible group state values are defined in
   * {@link IGuiElement}:<br>
   * <ul>
   *     <li>{@link IGuiElement#UPDATE}</li>
   *     <li>{@link IGuiElement#NEW}</li>
   *     <li>{@link IGuiElement#SEARCH}</li>
   *     <li>{@link IGuiElement#SELECTED}</li>
   * </ul>
   * 
   * @param context
   *          The current client context
   * @param state
   *          The new group state
   * @param listBox
   *          The corresponding GUI element to this event handler
   */
  public abstract void onGroupStatusChanged(IClientContext context, GroupState state, ITableListBox listBox) throws Exception;
  
  /**
   * Filters the cell data for the given tablelistbox. 
   * 
   * @param context
   *          the current client context
   * @param listbox
   *          the list box itself
   * @param row
   *          the row which can be filtered
   * @param fieldName
   *          the column
   * @param record
   *          the data browser record of this row
   * @param value
   *          the original value from the database
   * @return the new value for the browser or <code>null</code> to keep cell
   *         empty.
   * @since 2.9
   */
  public String filterCell(IClientContext context, ITableListBox listbox, int row, String fieldName, IDataBrowserRecord record, String value) throws Exception
  {
    return value;
  }
  
  /**
   * The data of the browser has been changed.<br>
   * You can override this method to preprocess the data and set error/warning decorations.
   * 
   * @see IBrowser#setErrorDecoration(IClientContext, de.tif.jacob.core.data.IDataRecord, String)
   * @see IBrowser#setWarningDecoration(IClientContext, de.tif.jacob.core.data.IDataRecord, String)
   * 
   * @param context
   *          the current client context
   * @param listbox
   *          The listbox with related to the event
   * @param data
   *          The new data of the browser.
   *          @since 3.0
   */
  public void onDataChanged(IClientContext context, ITableListBox listbox, IDataBrowser data) throws Exception
  {
  }

  /**
   * This hook method will be called, if the user clicks on one item in the
   * table list box.
   * <p>
   * 
   * Then overwriting this method, this is a good place for enable/disable other
   * GUI elements.
   * <p>
   * 
   * Note: This method will only be invoked, if the corresponding group state is
   * either {@link de.tif.jacob.screen.IGuiElement#NEW} or
   * {@link de.tif.jacob.screen.IGuiElement#UPDATE}.
   * 
   * @param context
   *          the current client context
   * @param listBox
   *          the listbox itself
   */
  public abstract void onSelect(IClientContext context, ITableListBox listBox, IDataTableRecord selectedRecord) throws Exception;

  /**
   * This event method will be called, if the user clicks on a row in the 
   * corresponding browser. You can prevent the selection/backfill of the 
   * IDataBrowserRecord if you return <code>false</code>. Nevertheless, 
   * you should inform the user by means of a proper notification, e.g. 
   * create and show a message dialog.
   * 
   * @param context
   *          The current context of the application
   * @param browser
   *          The browser (the emitter of the event)
   * @param record 
   *          The corresponding record 
   * @return Return <code>false</code>, if you want to avoid the execution of
   *         the action else return <code>true</code>.
   * @since 2.8.2
   */
  public boolean beforeAction(IClientContext context,ITableListBox listBox, IDataBrowserRecord record) throws Exception
  {
    return true;
  }
}
