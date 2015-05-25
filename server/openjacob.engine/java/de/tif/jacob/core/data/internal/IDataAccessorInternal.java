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

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataRecord;
import de.tif.jacob.core.data.impl.IDataFieldConstraints;
import de.tif.jacob.core.data.impl.qbe.QBERelationGraph;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * @author andreas
 *
 */
public interface IDataAccessorInternal extends IDataAccessor
{
  public void qbeSetConstraints(IDataFieldConstraints constraints);
  
  /**
   * Attention: For internal use only!!!
   * 
   * @param relationGraph
   * @param filldirection
   * @param record
   * @return the table record which has been propagated or <code>null</code> if no record exists
   */
  public IDataTableRecord propagateRecord(QBERelationGraph relationGraph, Filldirection filldirection, DataRecord record) throws RecordNotFoundException;
}
