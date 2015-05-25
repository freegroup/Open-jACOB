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

package de.tif.jacob.screen.impl;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;

/**
 *
 */
public interface HTTPForeignField extends IForeignField
{
  public void tryBackfill(IClientContext context, int maxDisplayRecords, boolean displaySelectionDialog, boolean countRecordsAsWell) throws Exception;
  public void setQbeValues(IClientContext context, IDataAccessor accessor) throws Exception;
  public IKey getToKey();
  public ITableAlias getToTable();
  public DataField getDataField();
  
  /**
   * @deprecated use {@link #getSelectedRecord(IClientContext)} instead
   * @param context
   * @return
   * @throws Exception
   */
  public IDataTableRecord getDisplayRecord(IClientContext context) throws Exception;

}
