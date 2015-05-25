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

package de.tif.jacob.core.data.event;

import de.tif.jacob.core.data.IDataBrowserRecord;

/**
 * Interface to modifiy data browser records.
 * 
 * @author andreas
 * @since 2.7.3
 * @see DataTableRecordEventHandler#afterSearchAction(IDataBrowserModifiableRecord)
 */
public interface IDataBrowserModifiableRecord extends IDataBrowserRecord
{
  /**
   * Sets the field value specified by its index.
   * 
   * @param fieldIndex
   *          the field index of the value
   * @param value
   *          the value to set. If possible the value should implement
   *          {@link Comparable} to enable sorting.
   */
  public void setValue(int fieldIndex, Object value);

  /**
   * Sets the field value specified by its name.
   * 
   * @param fieldName
   *          the name of the field
   * @param value
   *          the value to set. If possible the value should implement
   *          {@link Comparable} to enable sorting.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   */
  public void setValue(String fieldName, Object value) throws NoSuchFieldException;
}
