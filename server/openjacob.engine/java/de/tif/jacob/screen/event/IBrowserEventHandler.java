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

import java.util.Iterator;

import de.tif.jacob.core.data.IBrowserRecordList;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.impl.HTTPBrowser;

/**
 * Abstract event handler class for browsers. Derived implementations of this
 * event handler class have to be used to "hook" application-specific business
 * logic to browsers.
 * 
 * @author Andreas Herz
 */
public abstract class IBrowserEventHandler extends GuiEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IBrowserEventHandler.java,v 1.14 2010/09/25 22:29:56 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.14 $";

  
  /**
   * Decorate the cell for the given browser with an predefined icon. A browser
   * can be an in-form browser or a search browser.
   * <p>
   * Note: To access the data browser record for a given row index use
   * {@link IBrowser#getDataRecord(int)} since otherwise the current user sort
   * order might not be considered!
   * 
   * @param context
   *          the current client context
   * @param browser
   *          the browser itself
   * @param row
   *          the row which can be filtered
   * @param column
   *          the column
   * @param value
   *          the original value from the database
   * @return The Icon type or <code>Icon.NONE</code>. Note: <code>null</code>
   *         is not a valid value.
   * @see IBrowser#getDataRecord(int)
   * @since 2.7
   * @deprecated use
   *             {@link #decorateCell(IClientContext, IBrowser, int, int, IDataBrowserRecord, String)}
   *             instead
   */
  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, String value) throws Exception
  {
    return Icon.NONE;
  }

  /**
   * Decorate the cell for the given browser with an predefined icon. A browser can be an in-form
   * browser or a search browser.
   * 
   * @param context
   *          the current client context
   * @param browser
   *          the browser itself
   * @param row
   *          the row which can be filtered
   * @param column
   *          the column
    * @param record
    *          the data browser record of this row
   * @param value
   *          the original value from the database
   * @return The Icon type or <code>Icon.NONE</code>. Note: <code>null</code> is not a valid value.
   * @since 2.7.2
   */
  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    return decorateCell(context, browser, row, column, value);
  }

  /**
   * Filters the cell data for the given browser. A browser can be an in-form
   * browser or a search browser.
   * <p>
   * Note: To access the data browser record for a given row index use
   * {@link IBrowser#getDataRecord(int)} since otherwise the current user sort
   * order might not be considered!
   * 
   * @param context
   *          the current client context
   * @param browser
   *          the browser itself
   * @param row
   *          the row which can be filtered
   * @param column
   *          the column
   * @param value
   *          the original value from the database
   * @return the new value for the browser or <code>null</code> to keep cell
   *         empty.
   * @see IBrowser#getDataRecord(int)
   * @deprecated use
   *             {@link #filterCell(IClientContext, IBrowser, int, int, IDataBrowserRecord, String)}
   *             instead
   */
   public String filterCell(IClientContext context, IBrowser browser, int row, int column, String value) throws Exception
   {
     return value;
   }
   
   /**
    * Filters the cell data for the given browser. A browser can be an in-form
    * browser or a search browser.
    * 
    * @param context
    *          the current client context
    * @param browser
    *          the browser itself
    * @param row
    *          the row which can be filtered
    * @param column
    *          the column
    * @param record
    *          the data browser record of this row
    * @param value
    *          the original value from the database
    * @return the new value for the browser or <code>null</code> to keep cell
    *         empty.
    * @since 2.7.2
    */
   public String filterCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
   {
     return filterCell(context, browser, row, column, value);
   }

   /**
    * Returns whether the given record has children.<br>
    * Intended as an optimization for when the viewer does not need the actual children. 
    * Clients may be able to implement this more efficiently than getChildren.
    * 
    * @param context
    * @param browser
    * @param record
    * @return whether the given record has children
    * @throws Exception
    * @since 2.7.4
    * @see #getChildren(IClientContext, IBrowser, IDataBrowserRecord)
    */
   public boolean hasChildren(IClientContext context, IBrowser browser, IDataBrowserRecord record) throws Exception
   {
     // Falls es sich um eine HTTPBrowser handelt und in der Definition ein connectBy hinterlegt ist,
     // wird auf diese zurückgegriffen.
     //
     if(browser instanceof HTTPBrowser)
     {
       HTTPBrowser httpBrowser = (HTTPBrowser)browser;
       IKey connectByKey = httpBrowser.getConnectByKey();
       if(connectByKey==null)
         return false;
       

       // Eine Suche mit allen Key-Parameter auf der betroffenen Tabelle absetzen
       IDataAccessor acc = context.getDataAccessor().newAccessor();
       IDataTableRecord tableRecord = record.getTableRecord();
       IDataTable table = acc.getTable(record.getTableAlias().getName());
       IKey primaryKey = table.getTableAlias().getTableDefinition().getPrimaryKey();
       Iterator foreignIter = connectByKey.getTableFields().iterator();
       Iterator primaryIter = primaryKey.getTableFields().iterator();
       while (foreignIter.hasNext())
       {
         ITableField foreignField = (ITableField) foreignIter.next();
         ITableField primaryField = (ITableField) primaryIter.next();

         table.qbeSetKeyValue(foreignField, tableRecord.getValue(primaryField.getFieldIndex()));
       }
       return table.exists();
     }
     return false;
   }

   /**
    * Returns the child elements of the given parent element.
    * You must return a valid IDataBrowser element. <code>null</code> is not allowed.  
    * 
    * @param context
    * @param browser
    * @param record
    * @return the children of the given record
    * @throws Exception
    * @since 2.7.4
    * @see #hasChildren(IClientContext, IBrowser, IDataBrowserRecord)
    */
   public IBrowserRecordList getChildren(IClientContext context, IBrowser browser, IDataBrowserRecord record) throws Exception
   {
     // Falls es sich um eine HTTPBrowser handelt und in der Definition ein connectBy hinterlegt ist,
     // wird auf diese zurückgegriffen.
     //
     if(browser instanceof HTTPBrowser)
     {
       HTTPBrowser httpBrowser = (HTTPBrowser)browser;
       IKey connectByKey = httpBrowser.getConnectByKey();
       if(connectByKey==null)
         return HTTPBrowser.NO_TREE_BROWSER;
       
       IDataAccessor acc = context.getDataAccessor().newAccessor();
  
       IDataBrowser processBrowser= acc.getBrowser(httpBrowser.getDefinition().getName());

       // Eine Suche mit allen Key-Parameter auf der betroffenen Tabelle absetzen
       IDataTableRecord tableRecord = record.getTableRecord();
       IDataTable table = acc.getTable(record.getTableAlias().getName());
       IKey primaryKey = table.getTableAlias().getTableDefinition().getPrimaryKey();
       Iterator foreignIter = connectByKey.getTableFields().iterator();
       Iterator primaryIter = primaryKey.getTableFields().iterator();
       while (foreignIter.hasNext())
       {
         ITableField foreignField = (ITableField) foreignIter.next();
         ITableField primaryField = (ITableField) primaryIter.next();

         table.qbeSetKeyValue(foreignField, tableRecord.getValue(primaryField.getFieldIndex()));
       }
  
       processBrowser.search(IRelationSet.LOCAL_NAME);
       return processBrowser;
     }  
     // return empty browser as default implementation
     return HTTPBrowser.NO_TREE_BROWSER;
   }
   
   /**
    * Return the parent record of the hands over record. Return null per default.
    * This means, that the hands over record didn't have any parent.
    * 
    * @param context
    * @param browser
    * @param record
    * @since 2.7.4
    * @return the parent of the given record
    */
   public IDataTableRecord getParent(IClientContext context, IBrowser browser, IDataTableRecord record) throws Exception
   {
     // Falls es sich um eine HTTPBrowser handelt und in der Definition ein connectBy hinterlegt ist,
     // wird auf diese zurückgegriffen.
     //
     if(browser instanceof HTTPBrowser)
     {
       HTTPBrowser httpBrowser = (HTTPBrowser)browser;
       IKey connectByKey = httpBrowser.getConnectByKey();
       if(connectByKey==null)
         return null;
       
       IDataAccessor acc = context.getDataAccessor().newAccessor();
  
       // Eine Suche mit allen Key-Parameter auf der betroffenen Tabelle absetzen
       boolean searchCriteriaSet = false;
       IDataTable table = acc.getTable(record.getTableAlias().getName());
       IKey primaryKey = table.getTableAlias().getTableDefinition().getPrimaryKey();
       Iterator foreignIter = connectByKey.getTableFields().iterator();
       Iterator primaryIter = primaryKey.getTableFields().iterator();
       while (foreignIter.hasNext())
       {
         ITableField foreignField = (ITableField) foreignIter.next();
         ITableField primaryField = (ITableField) primaryIter.next();

         Object value = record.getValue(foreignField.getFieldIndex());
         if(value!=null)
         {
           searchCriteriaSet=true;
           table.qbeSetKeyValue(primaryField, value);
         }
         
       }
       if( searchCriteriaSet==true && table.search()==1 )
        return table.getSelectedRecord();
     }  
     return null;
   }
   
  /**
   * This hook method will be called, if the user selects a record in the
   * browser.
   * 
   * @param context
   *          the current client context
   * @param browser
   *          The browser with the click event
   * @param selectedRecord
   *          the record which has been selected
   */
  public abstract void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception;

  /**
   * The data of the browser has been changed.<br>
   * You can override this method to preprocess the data and set error/warning decorations.
   * 
   * @see IBrowser#setErrorDecoration(IClientContext, de.tif.jacob.core.data.IDataRecord, String)
   * @see IBrowser#setWarningDecoration(IClientContext, de.tif.jacob.core.data.IDataRecord, String)
   * 
   * @param context
   *          the current client context
   * @param browser
   *          The browser with the click event
   * @param data
   *          The new data of the browser.
   */
  public void onDataChanged(IClientContext context, IBrowser browser, IDataBrowser data) throws Exception
  {
  }
  
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
   * @since 2.7.4
   */
  public boolean beforeAction(IClientContext context,IBrowser browser, IDataBrowserRecord record) throws Exception
  {
    return true;
  }
}
