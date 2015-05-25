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

import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IDataFieldConstraint
{
  /**
   * Returns the table alias of this field constraint.
   * @return table alias
   */
  public ITableAlias getTableAlias();

  /**
   * Returns the table field of this field constraint.
   * @return table field
   */
  public ITableField getTableField();

  /**
   * Returns the QBE value as string.
   * @return the QBE value
   */
  public String getQbeValue();

  /**
   * Determines whether QBE value returned by
   * 
   * @link #getQbeValue() has to be interpreted as key value or not.
   *       <p>
   *       Note: Depending on the result of this method,
   *       <code>IDataTable.qbeSetValue()</code> or
   *       <code>IDataTable.qbeSetKeyValue()</code> should be used to set QBE
   *       constraint.
   * @return <code>true</code> if key value, otherwise <code>false</code>
   */
  public boolean isQbeKeyValue();
}
