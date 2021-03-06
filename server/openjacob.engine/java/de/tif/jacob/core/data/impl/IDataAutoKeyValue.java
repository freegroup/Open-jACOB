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
 * Interface for data values which are generated by the data source itself upon {@link DataTransaction.commit()}; 
 * 
 * @author Andreas Sonntag
 */
public interface IDataAutoKeyValue
{
  /**
   * Sets the database generated key value.
   * 
   * @param value the database generated key value, which must not be <code>null</code>.
   */
  public void setGeneratedValue(Object value);
  
  /**
   * Gets the database generated key value.
   * 
   * @return the database generated key value or <code>null</code>, if not set so far.
   */
  public Object getGeneratedValue();
}
