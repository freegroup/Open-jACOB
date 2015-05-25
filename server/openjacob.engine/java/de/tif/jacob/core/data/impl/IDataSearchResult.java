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

package de.tif.jacob.core.data.impl;

import java.util.List;

/**
 * Internal interface to provide search result information from data source
 * implementation to the data layer.
 * 
 * @author Andreas Sonntag
 * @since 2.10
 */
public interface IDataSearchResult
{
  /**
   * @return Returns <code>true</code>, if more matching records are available,
   *         otherwise <code>false</code>
   */
  public boolean hasMore();

  /**
   * @return Returns the number of matching records retrieved.
   */
  public int getRecordCount();

  /**
   * @return Returns the result records or an empty list, if records are not
   *         collected.
   * @see IDataSearchIterateCallback
   */
  public List getRecords();
}
