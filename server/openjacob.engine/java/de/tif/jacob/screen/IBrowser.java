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
package de.tif.jacob.screen;
import java.util.List;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.screen.event.IBrowserEventHandler;
/**
 * @author Andreas Herz
 * 
 */
public interface IBrowser extends IGuiElement, ISelectionProvider
{
  static public final String RCS_ID = "$Id: IBrowser.java,v 1.36 2010/09/30 09:20:37 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.36 $";

  public void setData(IClientContext context, IDataBrowser data);

  /**
   * Returns the underlying IDataBrowser of this UI element.
   * 
   * @return
   */
  public IDataBrowser getData();

  /**
   * Returns the data browser record by means of the current row index.
   * <p>
   * Instead of {@link IDataBrowser#getRecord(int)} this method considers the
   * current column order selected by the user, which might be different from
   * the initial/default browser order as established by the database. This
   * method should be used then implementing
   * {@link IBrowserEventHandler#filterCell(IClientContext, IBrowser, int, int, String)}
   * or
   * {@link IBrowserEventHandler#decorateCell(IClientContext, IBrowser, int, int, String)}
   * since the given row index is based on the current display order.
   * 
   * @param rowIndex
   *          the row index of the record to get
   * @return the desired data browser record
   * @throws IndexOutOfBoundsException
   *           if
   *           <code>0 <= rowIndex < {@link IDataBrowser#recordCount()}</code>
   *           is not fullfilled
   * @since 2.7.2
   */
  public IDataBrowserRecord getDataRecord(int rowIndex) throws IndexOutOfBoundsException;

  /**
   * Returns the underlying browser definition of this GUI browser.
   * 
   * @return the browser definition
   */
  public IBrowserDefinition getDefinition();

  /**
   * Select the hands over index in the current dataBrowser.<br>
   * This enforce a <code>propagateSelection</code> on the DataBrowser.<br>
   * <br>
   * The selection will be reset if you hands over <code>-1>/code><br>
   * 
   * @param context
   *          the current client context
   * @param index 
   *          The zero based index to select or -1 to unselect.
   * @throws Exception
   */
  public void setSelectedRecordIndex(IClientContext context, int index) throws Exception;

  /**
   * Selects the record.
   * 
   * @param context
   *          the current client context
   * @param record
   *          the record to select
   * @throws Exception
   * @since 2.7.4
   */
  public void setSelectedRecord(IClientContext context, IDataTableRecord record) throws Exception;

  /**
   * Selects the record.
   * 
   * @param context
   *          the current client context
   * @param record
   *          the record to select
   * @throws Exception
   * @since 2.10
   */
  public void setSelectedRecord(IClientContext context, IDataBrowserRecord record) throws Exception;

  /**
   * Returns the current selected database record.
   * 
   * @param context
   * @return
   * @throws Exception
   * @since 2.7.4
   */
  public IDataTableRecord getSelectedRecord(IClientContext context) throws Exception;

  /**
   * Returns the current selected browser record.
   * 
   * @param context
   * @return
   * @throws Exception
   * @since 2.7.4
   */
  public IDataBrowserRecord getSelectedBrowserRecord(IClientContext context) throws Exception;

  /**
   * Resets the selected record, if any.
   * <p>
   * Note: This method has the same effect than
   * <code>setSelectedRecordIndex(context, -1)</code>.
   * 
   * @param context
   *          the current client context
   * @throws Exception
   * @since 2.7.2
   */
  public void resetSelectedRecord(IClientContext context) throws Exception;

  /**
   * Returns the definitions of all browser fields of this browser.
   * 
   * @return <code>List</code> of {@link IBrowserField}
   */
  public List getColumns();

  /**
   * Appends a table record always at the end of this gui data browser.
   * 
   * @param record
   *          the record to add
   * @return the index of the location where the record has been added
   * @since 2.7.4
   */
  public int append(IClientContext context, IDataTableRecord record) throws Exception;
  
  /**
  * Appends a table record always at the end of this gui data browser.
   * 
   * @param context
   * @param record
   *          the record to add
   * @return the index of the location where the record has been added
   * @param setAsSelectedRecord
   *          hand over <b>false</b> if you want to avoid to autoselect the record (backfill).
   * @throws Exception
   * @since 2.8.5
   */
  public int append(IClientContext context, IDataTableRecord record, boolean setAsSelectedRecord) throws Exception;

  /**
   * Add a table record at the top of this gui data browser or insert it into
   * corresponding position if the browser has been configured as tree browser.
   * 
   * @param record
   *          the record to add
   * @since 2.7.4
   */
  public void add(IClientContext context, IDataTableRecord record) throws Exception;

  /**
   * Add a table record always at the top of the gui data browser.
    * 
    * @param context
    * @param record
    *          the record to add
    * @param setAsSelectedRecord
    *          hand over <b>false</b> if you want to avoid to autoselect the record (backfill).
    * @throws Exception
    * @since 2.8.5
    */
   public void add(IClientContext context, IDataTableRecord record, boolean setAsSelectedRecord) throws Exception;

   /**
    * Remove the browser from the browser representation. Neither the reocrd will be deleted nor removed from the DataAccessor (if available).
    * <br>
    * @param context
    * @param record
    * @throws Exception
    * @since 2.10
    */
   public void remove(IClientContext context, IDataTableRecord record) throws Exception;
   
   /**
    * Remove the browser from the browser representation. Neither the reocrd will be deleted nor removed from the DataAccessor (if available).
    * <br>
    * @param context
    * @param record
    * @throws Exception
    * @since 2.10
    */
    public void remove(IClientContext context, IDataBrowserRecord browserRecord) throws Exception;
   
   /**
    * Create new empty table record and add them as first element to the browser.
    * 
    * @param context the current application context
    * @param trans the transaction to use
    * @return
    * @since 2.10
    */
   public IDataTableRecord newRecord(IClientContext context, IDataTransaction trans) throws Exception;
  
   /**
   * Highlight the background of the row and fade it smoothly back to the normal
   * style. This is good feature for visual user feedback in a UI table.
   * 
   * @since 2.7.4
   * @param rowIndex
   *          The rowIndex to highlight
   */
  public void highlightRecord(IClientContext context, int rowIndex);

  /**
   * Highlight the background of the row and fade it smoothly back to the normal
   * style. This is good feature for visual user feedback in a UI table.
   * 
   * @since 2.7.4
   * @param record
   *          The rowIndex to highlight
   */
  public void highlightRecord(IClientContext context, IDataTableRecord record);

  /**
   * Highlight the background of the row and fade it smoothly back to the normal
   * style. This is good feature for visual user feedback in a UI table.
   * 
   * @since 2.7.4
   * @param record
   *          The rowIndex to highlight
   */
  public void highlightRecord(IClientContext context, IDataBrowserRecord record);

  /**
   * Expand the record in the browser. All children of the record are visible
   * after this call.<br>
   * The tree node will be refreshed if the node is already expanded.
   * 
   * @param context
   * @param record
   * @since 2.7.4
   */
  public void expand(IClientContext context, IDataBrowserRecord record) throws Exception;

  /**
   * Expand the record in the browser and all child records. All children of the record are visible
   * after this call.<br>
   * The tree node will be refreshed if the node is already expanded.
   * 
   * @param context
   * @param record
   * @since 2.8.6
   */
  public void expandAll(IClientContext context, IDataBrowserRecord record) throws Exception;


  /**
   * Expand the record in the browser. All children of the record are visible
   * after this call. The tree node will be refreshed if the node is already
   * expanded.
   * 
   * @param context
   * @param record
   * @since 2.7.4
   */
  public void expand(IClientContext context, IDataTableRecord record) throws Exception;

  /**
   * Checks whether the given record has child records.
   * <p>
   * Note: This method might be used to set different icons in a tree browser by
   * means of
   * {@link IBrowserEventHandler#decorateCell(IClientContext, IBrowser, int, int, IDataBrowserRecord, String)}
   * . Do not call
   * {@link IBrowserEventHandler#hasChildren(IClientContext, IBrowser, IDataBrowserRecord)}
   * directly, since this might be an expensive operation and has usually
   * already been done by the underlying framework!
   * 
   * @param context
   * @param record
   * @return <code>true</code> if child records are present, otherwise
   *         <code>false</code>
   * @throws Exception
   * @since 2.10
   */
  public boolean hasChildren(IClientContext context, IDataBrowserRecord record) throws Exception;
  
  /**
   * Refresh the given record in the browser. Normally this will be done by the
   * internal framework. The one and only reason to call this method manually
   * is, that you change the parent/child relation in an ISelectionAction or a
   * TableHook of more than one record.<br>
   * 
   * This is a design fault at the moment.
   * 
   * @param context
   * @param record
   * @throws Exception
   * @since 2.7.4
   */
  public void refresh(IClientContext context, IDataTableRecord record) throws Exception;

  /**
   * Refresh the parent to child position of the given record in the browser.
   * Normally this will be done by the internal framework. The one and only
   * reason to call this method manually is, that you change the parent/child
   * relation in an ISelectionAction or a TableHook of more than one record.<br>
   * 
   * This is a design fault at the moment.
   * 
   * @param context
   * @param record
   * @throws Exception
   * @since 2.7.4
   */
  public void refresh(IClientContext context, IDataBrowserRecord record) throws Exception;

  /**
   * Marks the given browser record with an error decoration.
   * 
   * @param context
   *          the current client context
   * @param record
   *          the browser record to decorate
   * @param message
   *          the message to add to the error decoration
   * @since 2.7.4
   */
  public void setErrorDecoration(IClientContext context, IDataRecord record, String message);

  /**
   * Marks the given row index with an error decoration.
   * 
   * @param context
   *          the current client context
   * @param index
   *          the row index to decorate
   * @param message
   *          the message to add to the error decoration
   * @since 2.7.4
   */
  public void setErrorDecoration(IClientContext context, int index, String message);

  /**
   * Remove an error decoration from the given browser record (if any).
   * 
   * @param context
   *          the current client context
   * @param record
   *          the browser record
   * @since 2.7.4
   */
  public void resetErrorDecoration(IClientContext context, IDataRecord record);

  /**
   * Remove an error decoration from the given row (if any).
   * 
   * @param context
   *          the current client context
   * @param rowIndex
   *          the row index
   * @since 2.7.4
   */
  public void resetErrorDecoration(IClientContext context, int rowIndex);


  /**
   * Marks the given row index with an warning decoration.
   * 
   * @param context
   *          the current client context
   * @param index
   *          the row index to decorate
   * @param message
   *          the message to add to the error decoration
   * @since 2.8.4
   */
  public void setWarningDecoration(IClientContext context, int index, String message);


  /**
   * Marks the given browser record with an warning decoration.
   * 
   * @param context
   *          the current client context
   * @param record
   *          the browser record to decorate
   * @param message
   *          the message to add to the error decoration
   * @since 2.8.4
   */
  public void setWarningDecoration(IClientContext context, IDataRecord record, String message);

  /**
   * Remove an warning decoration from the given row (if any).
   * 
   * @param context
   *          the current client context
   * @param rowIndex
   *          the row index
   * @since 2.8.4
   */
  public void resetWarningDecoration(IClientContext context, int rowIndex);


  /**
   * Remove all warning decoration from this GUI element (if any).
   * 
   * @param context
   *          the current client context
   * @since 2.8.4
   */
  public void resetWarningDecoration(IClientContext context);

  /**
   * Emphasize the given browser record
   * 
   * @param context
   *          the current client context
   * @param record
   *          the browser record to decorate
   * @param flag
   *          emphasize or not
   * @since 2.7.4
   */
  public void setEmphasize(IClientContext context, IDataRecord record, boolean flag);

  /**
   * Emphasize the given row index.
   * 
   * @param context
   *          the current client context
   * @param rowIndex
   *          the row index to decorate
   * @param flag
   *          emphasize or not
   * @since 2.7.4
   */
  public void setEmphasize(IClientContext context, int rowIndex, boolean flag);

  /**
   * Reset the emphasize attribute for all elements in the browser.
   * 
   * @param context
   *          the current client context
   * @since 2.7.4
   */
  public void resetEmphasize(IClientContext context);

  /**
   * Remove all error decoration from this GUI element (if any).
   * 
   * @param context
   *          the current client context
   * @since 2.7.4
   */
  public void resetErrorDecoration(IClientContext context);

  /**
   * Scroll to make sure the record is visible on the screen
   * 
   * @param context
   * @param record
   * @since 2.7.4
   */
  public void ensureVisible(IClientContext context, IDataRecord record);

  
  /**
   * Install a custom cell renderer. The cell renderer will be called for the 
   * column with the given name.
   *  
   * @param context
   * @param browserFieldName
   * @param renderer
   * @since 2.9.1
   */
  public void installCellRenderer(IClientContext context , String browserColumnName, IBrowserCellRenderer renderer) throws Exception;
  
  /**
   * Enable or disable the treeview of the browser. This feature is only supported if the browser definition
   * did have set the "connectBy" attribute in the jACOB designer.<br>
   * 
   * @since 2.10
   * @param context
   * @param flag
   */
  public void setEnableTreeView(IClientContext context, boolean flag) throws Exception;
}
