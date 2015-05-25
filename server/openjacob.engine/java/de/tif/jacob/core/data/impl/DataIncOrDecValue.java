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

import java.math.BigDecimal;

/**
 * Pseudo value to increment or decrement a numeric value
 * 
 * @author Andreas Sonntag
 */
public final class DataIncOrDecValue
{
  private final Number incOrDecBy;
  private final boolean decrement;
  private Number incrementedValue;

  public DataIncOrDecValue(Number incOrDecBy, boolean decrement)
  {
    this.incOrDecBy = incOrDecBy;
    this.decrement = decrement;
  }

  public Object getIncrementedValue()
  {
    if (this.incrementedValue == null)
      throw new RuntimeException("Value has not been incremented so far");
    return this.incrementedValue;
  }

  private static BigDecimal toDecimal(Number number)
  {
    if (number instanceof BigDecimal)
      return (BigDecimal) number;

    if (number instanceof Float || number instanceof Double)
      return new BigDecimal(number.doubleValue());

    return BigDecimal.valueOf(number.longValue());
  }

  public void setIncrementedValue(Object value, boolean doIncrement)
  {
    if (value == null)
      throw new NullPointerException();

    Number number = (Number) value;
    if (doIncrement)
    {
      if (number instanceof BigDecimal)
      {
        if (this.decrement)
          number = ((BigDecimal) number).subtract(toDecimal(this.incOrDecBy));
        else
          number = ((BigDecimal) number).add(toDecimal(this.incOrDecBy));
      }
      else if (number instanceof Integer)
      {
        if (this.decrement)
          number = new Integer(number.intValue() - this.incOrDecBy.intValue());
        else
          number = new Integer(number.intValue() + this.incOrDecBy.intValue());
      }
      else if (number instanceof Long)
      {
        if (this.decrement)
          number = new Long(number.longValue() - this.incOrDecBy.longValue());
        else
          number = new Long(number.longValue() + this.incOrDecBy.longValue());
      }
      else if (number instanceof Double)
      {
        if (this.decrement)
          number = new Double(number.doubleValue() - this.incOrDecBy.doubleValue());
        else
          number = new Double(number.doubleValue() + this.incOrDecBy.doubleValue());
      }
      else if (number instanceof Float)
      {
        if (this.decrement)
          number = new Float(number.floatValue() - this.incOrDecBy.floatValue());
        else
          number = new Float(number.floatValue() + this.incOrDecBy.floatValue());
      }
      else
      {
        throw new RuntimeException("Unexpected number class: " + value.getClass());
      }
    }
    this.incrementedValue = number;
  }

  /**
   * @return Returns the incOrDecBy, which is always positive.
   */
  public Number getIncOrDecBy()
  {
    return incOrDecBy;
  }

  public boolean isDecrement()
  {
    return this.decrement;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    String numstr = this.incOrDecBy.toString();
    if (this.decrement)
      return "value-" + numstr;
    return "value+" + numstr;
  }

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
