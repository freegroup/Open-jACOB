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

import java.util.Locale;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * @author andreas
 *
 */
public interface IDataTableInternal extends IDataTable
{
  public long getChangeCount();
  
  public void qbeSetValue(ITableField tableField, Object value, Locale locale);
  
  public void qbeSetGuiElementValue(String guiElementName, ITableField tableField, Object value, Locale locale);
  
  public void setSelectedRecord(IDataTableRecord record) throws Exception;
  
  public IDataTableRecord updateSelectedRecord(IDataTransaction trans) throws RecordLockedException, RecordNotFoundException;
}
