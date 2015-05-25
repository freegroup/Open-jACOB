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

package de.tif.jacob.core.data;

/**
 * Callback interface to perform a search and iterate operation on a data browser.
 * <p>
 * Application programmers should provide an implementation of this interface!
 * 
 * @see IDataBrowser#searchAndIterate(IDataBrowserSearchIterateCallback)
 * @see IDataBrowser#searchAndIterate(IDataBrowserSearchIterateCallback, String)
 * @see IDataBrowser#searchAndIterate(IDataBrowserSearchIterateCallback,
 *      de.tif.jacob.core.definition.IRelationSet)
 * @author Andreas Sonntag
 * @since 2.10
 */
public interface IDataBrowserSearchIterateCallback
{
  /**
   * This method will be called for each record retrieved until no further
   * records are available or this method returns <code>false</code> to signal
   * that the iteration should be stopped.
   * 
   * @param record
   *          the current record retrieved
   * @return <code>true</code> to provide further records (if existing) or
   *         <code>false</code> to stop retrieval of further records.
   * @throws Exception in case of any problem
   */
  public boolean onNextRecord(IDataBrowserRecord record) throws Exception;
}
