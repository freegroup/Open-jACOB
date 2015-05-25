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

package de.tif.jacob.core.data.internal;

import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * Internal data record interface.
 * 
 * @author Andreas Sonntag
 * @since 2.10
 */
public interface IDataRecordInternal extends IDataRecord
{
  public IDataTableRecord getTableRecord() throws RecordNotFoundException;
  
  /**
   * Adds a sticky retrieval object to this data record.
   * 
   * @param stickyRetrievalObject the sticky retrieval object
   */
  public void addStickyRetrievalObject(Object stickyRetrievalObject);

  /**
   * Returns the sticky retrieval object, if existing
   * 
   * @return the sticky retrieval object or <code>null</code>
   */
  public Object getStickyRetrievalObject();
}
