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
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;

/**
 * Abstract event handler class for foreign fields. Derived implementations of
 * this event handler class have to be used to "hook" application-specific
 * business logic to foreign fields.
 * 
 * @author Andreas Herz
 */
public abstract class IForeignFieldEventHandler extends IGroupMemberEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IForeignFieldEventHandler.java,v 1.2 2010/01/20 16:21:29 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   * This hook method will be called, if the search icon of the foreign field
   * has been pressed. <br>
   * You can avoid the search action, if you return <code>false</code> or you
   * can add QBE search constraints to the respective data tables to constraint
   * the search result. <br>
   * 
   * @param context
   *          The current client context
   * @param foreignField
   *          The foreign field itself
   * @return <code>false</code>, if you want to avoid the execution of the
   *         search action, otherwise <code>true</code>
   *         
   * @see IForeignFieldEventHandler#beforeSearch(IClientContext, IForeignField, IDataBrowser)        
   */
  public boolean beforeSearch(IClientContext context, IForeignField foreignField) throws Exception
  {
    return true;
  };

  /**
   * This hook method will be called, if the search icon of the foreign field
   * has been pressed. <br>
   * You can avoid the search action, if you return <code>false</code> or you
   * can add QBE search constraints to the respective data tables to constraint
   * the search result. <br>
   * 
   * @see IForeignFieldEventHandler#beforeSearch(IClientContext, IForeignField)
   * @since 2.9.1
   **/
  public boolean beforeSearch(IClientContext context, IForeignField foreignField, IDataBrowser searchBrowser) throws Exception
  {
    return beforeSearch(context, foreignField);
  };
  
  /**
   * This hook method will be called, if a record has been filled back
   * (selected) in the foreign field.
   * 
   * @param context
   *          The current client context
   * @param foreignRecord
   *          The record which has been filled in the foreign field.
   * @param foreignField
   *          The foreign field itself
   */
  public void onSelect(IClientContext context, IDataTableRecord foreignRecord, IForeignField foreignField) throws Exception
  {
  };

  /**
   * This hook method will be called, if the foreign field has been cleared
   * (deselected).
   * 
   * @param context
   *          The current client context
   * @param foreignField
   *          The foreign field itself
   */
  public void onDeselect(IClientContext context, IForeignField foreignField) throws Exception
  {
  };
}
