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
/*
 * Created on 05.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data.impl;

import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.RecordNotFoundException;


/**
 * Internal interface!
 * 
 * @author Andreas Sonntag
 */
public interface IDataBrowserRecordValueProvider
{
  public Object getValueInternal(int fieldIndex);
  
  // TODO: temporärer HACK für Andherz (26.6.08) und for Lucene Score-Value not
  // to be updated in BrowserRecord by TableRecord (18.8.2010)
  public boolean isModifiedValue(int fieldIndex);

  public void setValueInternal(int fieldIndex, Object value);

  public IDataKeyValue getPrimaryKeyValue();
  
  public IDataTableRecord getTableRecord() throws RecordNotFoundException;

  public DataTableRecord getWrappedTableRecord();
}
