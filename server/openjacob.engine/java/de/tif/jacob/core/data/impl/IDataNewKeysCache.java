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

import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;

/**
 * Internal interface for key cache.
 * 
 * @author Andreas Sonntag
 * @since 2.8.1
 */
public interface IDataNewKeysCache
{
  /**
   * Internal interface for key creator implementations called by
   * implementations of {@link IDataNewKeysCache}.
   * 
   * @author Andreas Sonntag
   * @since 2.8.1
   */
  public interface IDataNewKeysCreator
  {
    public long createNewKeys(DataSource dataSource, ITableDefinition table, ITableField field, int increment) throws Exception;
  }

  public long newKey(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCreator creator) throws Exception;
}
