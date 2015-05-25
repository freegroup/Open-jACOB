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
 * Implementation for database generated long key values.
 * 
 * @author Andreas Sonntag
 */
public final class DataAutoKeyLongValue extends DataAutoKeyNumberValue
{
  private Long value = null;

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.IDataAutoKeyValue#getGeneratedValue()
   */
  public Object getGeneratedValue()
  {
    if (this.value == null)
      throw new RuntimeException("Key has not been generated so far");
    return this.value;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.IDataAutoKeyValue#setGeneratedValue(java.lang.Object)
   */
  public void setGeneratedValue(Object value)
  {
    // plausibility check 
    if (this.value != null)
      throw new RuntimeException("Value has already been set");

    if (value == null)
      throw new NullPointerException();

    if (value instanceof Long)
    {
      this.value = (Long) value;
    }
    else if (value instanceof Integer)
    {
      this.value = new Long(((Integer) value).longValue());
    }
    else
    {
      throw new IllegalArgumentException(value.getClass().toString());
    }
  }
  
  /* (non-Javadoc)
   * @see java.lang.Number#doubleValue()
   */
  public double doubleValue()
  {
    return longValue();
  }
  
  /* (non-Javadoc)
   * @see java.lang.Number#floatValue()
   */
  public float floatValue()
  {
    return longValue();
  }
  
  /* (non-Javadoc)
   * @see java.lang.Number#intValue()
   */
  public int intValue()
  {
    return (int) longValue();
  }
  
  /* (non-Javadoc)
   * @see java.lang.Number#longValue()
   */
  public long longValue()
  {
    if (this.value == null)
      throw new RuntimeException("Long value has not been determined so far");
    return this.value.longValue();
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    if (this.value == null)
      return "AUTOKEY";
    return this.value.toString();
  }
}
