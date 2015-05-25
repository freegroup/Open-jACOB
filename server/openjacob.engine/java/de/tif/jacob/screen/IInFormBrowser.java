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

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTableRecord;

/**
 * @author Andreas Herz
 *
 */
public interface IInFormBrowser extends IBrowser
{
  static public final String RCS_ID = "$Id: IInFormBrowser.java,v 1.4 2010/10/04 22:49:54 ibissw Exp $";
  static public final String RCS_REV = "$Revision: 1.4 $";

  /**
   * Returns the underlying IDataBrowser of this UI element.
   * @return
   * @since 2.7.4
   */
  public IDataBrowser getData();
  
  /**
   * Appends a table record always at the end of this gui data browser.
   * 
   * @param record
   *          the record to add
   * @return the index of the location where the record has been added
   * @since 2.8.6
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
   * @since 2.8.6
   */
  public int append(IClientContext context, IDataTableRecord record, boolean setAsSelectedRecord) throws Exception;

  /**
   * Add a table record at the top of this gui data browser or insert it into
   * corresponding possition if the browser has been configured as tree browser.
   * 
   * @param record
   *          the record to add
   * @since 2.8.6
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
   * @since 2.8.6
   */
   public void add(IClientContext context, IDataTableRecord record, boolean setAsSelectedRecord) throws Exception;
}

