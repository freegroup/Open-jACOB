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

package de.tif.jacob.report;

import java.io.OutputStream;
import java.util.Locale;

import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.report.impl.castor.types.SortOrderType;
import de.tif.jacob.searchbookmark.ISearchConstraint;
import de.tif.jacob.security.IUser;

/**
 * @author Andreas Herz
 *
 */
public interface IReport extends ISearchConstraint
{
  // move to Report.java
  public static class Column
  {
   public String table;
   public String field;
   public String label;
   public SortOrder order = SortOrder.NONE;
   private final de.tif.jacob.report.impl.castor.Column castor;
   public Column(de.tif.jacob.report.impl.castor.Column cc)
   {
     this.castor = cc;
     table =cc.getTableAlias();
     field =cc.getField();
     label =cc.getLabel();
     SortOrderType sort = cc.getSortOrder();
     if(sort!=null)
     {
       switch(sort.getType())
       {
         case SortOrderType.ASCENDING_TYPE:
           order = SortOrder.ASCENDING;
           break;
         case SortOrderType.DESCENDING_TYPE:
           order = SortOrder.DESCENDING;
           break;
         default:
           order = SortOrder.NONE;
           break;
       }
     }
   }
   
   public void setSortOrder(SortOrder order)
   {
     this.castor.setSortOrder(SortOrderType.valueOf(order.toString()));
     this.order=order;
   }
  }

  
  /**
   * Adds an column to the output of the report.<br>
   * 
   * @param tableAlias
   * @param field
   * @param label
   */
  public void  addColumn(String tableAlias, String field, String label);
  
 
  public void  addColumn(String tableAlias, String field, String label, SortOrder order);


  /**
   * return all columns in this report.
   * 
   * @return
   */
  public abstract Column[] getColumns();

  /**
   * Save the report.
   *
   */
  public abstract void save() throws Exception;
  

  /**
   * If the isPrivate flag is set, the report is only visible for the owner.
   * 
   * @return <code>true</code> if this is a private report, <code>false</code>
   *         if this is a public report.
   */
  public abstract boolean isPrivate();
  
  /**
   * Mark the report as private or not. If the report is private, the report
   * is only visible for the owner.
   * 
   * @param flag
   */
  public abstract void setPrivate(boolean flag);
  
  /**
   * Deletes the report definiton. After this call, the report is no longer
   * available.
   */
  public abstract void delete() throws Exception;

  /**
   * 
   * @return the mimetype for the report
   */
  public abstract String getDefaultMimeType();
  
  /**
   * Render the report to the default mimeType.
   * 
   * @param out
   *          the output stream
   * @throws Exception
   *           on any problem
   */
  public abstract void render(OutputStream out) throws Exception;
  
  /**
   * 
   * @return The record count of the last execution of the report.
   */
  public abstract int getRecordCount();
  
  /**
	 * Returns the real number of records matching the search criterias of the
	 * last execution of the report.
	 * 
   * @return The real record count of the last execution of the report.
   */
  public abstract long getRealRecordCount();
  
  /**
   * Render the report to the hands over mime type.
   * 
   * @param out
   *          the output stream
   * @param mimeType
   *          the mime type
   * @throws Exception
   *           on any problem
   */
  public abstract void render(OutputStream out, String mimeType) throws Exception;
  
  /**
   * Render the report to the hands over mime type.
   * 
   * @param out
   *          the output stream
   * @param mimeType
   *          the mime type
   * @param locale
   *          the locale to render or <code>null</code> to use default locale
   *          of the report owner
   * @throws Exception
   *           on any problem
   * @since 2.5.6
   */
  public abstract void render(OutputStream out, String mimeType, Locale locale) throws Exception;
  
  /**
   * Render the report to the hands over mime type.
   * 
   * @param out
   *          the output stream
   * @param mimeType
   *          the mime type
   * @param maxRecords
   *          limit for the number of records retrieved. Note: Useful to render a preview of the report
   * @param locale
   *          the locale to render or <code>null</code> to use default locale
   *          of the report owner
   * @throws Exception
   *           on any problem
   * @since 2.9
   */
  public abstract void render(OutputStream out, String mimeType, int maxRecords, Locale locale) throws Exception;
  
  /**
   * Returns the report notification for the given user.
   * 
   * @param user
   *          the user to get report notification for.
   * @return the report notifyee or <code>null</code>, if no notification has
   *         been specified for the given user.
   * @since 2.6
   */
  public abstract ReportNotifyee getReportNotifyee(IUser user);
  
  /**
   * Returns the report notification for the current user.
   * 
   * @return the report notifyee or <code>null</code>, if no notification has
   *         been specified for the current user.
   * @deprecated use {@link #getReportNotifyee(IUser)} instead         
   */
  public abstract ReportNotifyee getReportNotifyee();
  
  /**
   * Returns all report notifyees of this report.
   * 
   * @return All report notifyees
   */
  public abstract ReportNotifyee[] getReportNotifyees();
  
  /**
   * Sets the report notification for the notifyee.
   *  
   * @param notifyee the notifyee to set report notification for
   */
  public abstract void setReportNotifyee(ReportNotifyee notifyee);
  
  /**
   * Removes the notification of the given user.
   * 
   * @param notifyee
   *          the user to delete report notification
   * @since 2.6
   */
  public abstract void deleteReportNotifyee(IUser notifyee);
}
