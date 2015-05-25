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
 * Abstract class for database generated numeric key values.
 * 
 * @author Andreas Sonntag
 */
public abstract class DataAutoKeyNumberValue extends Number implements IDataAutoKeyValue
{
  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  protected final Object clone() throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException();
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public final boolean equals(Object obj)
  {
    return this == obj;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public final int hashCode()
  {
    throw new UnsupportedOperationException();
  }
}
