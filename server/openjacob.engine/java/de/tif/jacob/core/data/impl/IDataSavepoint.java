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

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * @author Andreas Sonntag
 */
public interface IDataSavepoint
{
  /**
   * Checks whether a data value to rollback exists.
   * 
   * @param fieldIndex the data field index
   * @return <code>true</code> if a data value to rollback exists, otherwise <code>false</code> 
   */
  public boolean hasRollbackValue(int fieldIndex);

  /**
   * Returns the data value to rollback, i.e. this is the original data value at save point creation time. 
   * @param fieldIndex the data field index
   * @return the data value to rollback
   */
  public Object getRollbackValue(int fieldIndex);
}
