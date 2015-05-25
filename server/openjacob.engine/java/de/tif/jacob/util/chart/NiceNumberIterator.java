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

package de.tif.jacob.util.chart;


import java.util.Iterator;

public class NiceNumberIterator implements Iterator
{
  private boolean  isBounded;
  private double   fromValue;
  private double   toValue;
  private double   firstValue;    // First nice number < from
  private double   lastValue;     // First nice number > to
  private double   majorStep;
  private double   minorStep;
  private double   minorMinorStep;
  private double   step;
  private int      nValues;
  private int      valueNo;

  
  
  /**
   * Create nice numbers in an interval.
   *
   * @param fromValue       From value.
   * @param toValue         To value.
   * @param nNumbersApprox  Approximate number of major nice numbers to produce.
   * @param isBounded       True if fromValue/toValue should be end points
   *                        and hence reported as nice numbers.
   */
  public NiceNumberIterator (double from, double to, int approx, boolean bounded)
  {
    this.fromValue = from;
    this.toValue   = to;
    this.isBounded = bounded;

    if (approx <= 0) 
      approx = 1;
    double step = (this.toValue - this.fromValue) / approx;
    if (step == 0.0) 
      step = 1.0;

    boolean isAscending = step > 0.0;
    
    // Scale abs(step) to interval 1 - 10
    double scaleFactor = 1.0;
    while (Math.abs (step) > 10) 
    {
      step /= 10.0;
      scaleFactor *= 10.0;
    }
    
    while (Math.abs (step) < 1) 
    {
      step *= 10.0;
      scaleFactor /= 10.0;
    }

    // Find nice major step value
    majorStep = Math.abs (step);
    if      (majorStep > 7.50) 
      majorStep = 10.0;
    else if (majorStep > 3.50) 
      majorStep =  5.0;
    else if (majorStep > 2.25) 
      majorStep =  2.5;
    else if (majorStep > 1.50) 
      majorStep =  2.0;
    else                        
      majorStep =  1.0;

    // Find corresponding minor step value
    if      (majorStep == 10.0) 
      minorStep = 5.0;
    else if (majorStep ==  5.0) 
      minorStep = 2.5;
    else if (majorStep ==  2.5) 
      minorStep = 0.5;
    else if (majorStep ==  2.0) 
      minorStep = 1.0;
    else                         
      minorStep = 0.1;
    
    // Find corresponding minor minor step value
    if      (minorStep ==  5.0) 
      minorMinorStep = 1.0;
    else if (minorStep ==  2.5) 
      minorMinorStep = 0.5;
    else if (minorStep ==  1.0) 
      minorMinorStep = 0.1;    
    else if (minorStep ==  0.5) 
      minorMinorStep = 0.1;
    else                         
      minorMinorStep = 0.0;

    if (step < 0) 
    {
      majorStep      = -majorStep;
      minorStep      = -minorStep;
      minorMinorStep = -minorMinorStep;      
    }
    
    majorStep      *= scaleFactor;
    minorStep      *= scaleFactor;
    minorMinorStep *= scaleFactor;    

    // Find first nice value before fromValue
    firstValue = ((int) (fromValue / majorStep)) * majorStep;
    
    if( isAscending && firstValue > fromValue) 
      firstValue -=majorStep;
    else if (!isAscending && firstValue < fromValue) 
      firstValue -=majorStep;

    // Find last nice value after toValue
    lastValue = ((int) (toValue / majorStep)) * majorStep;
    if( isAscending && lastValue < toValue) 
      lastValue += majorStep;
    else if (!isAscending && lastValue > toValue) 
      lastValue += majorStep;

    // Find total number of values
    this.step = minorMinorStep != 0.0 ? minorMinorStep :  minorStep != 0.0 ? minorStep : majorStep;
    nValues =  (int) Math.round ((lastValue - firstValue) / this.step) + 1;
    
    // Move the steps from value space to count space
    majorStep      = (double) Math.round (majorStep      / this.step);
    minorStep      = (double) Math.round (minorStep      / this.step);
    minorMinorStep = (double) Math.round (minorMinorStep / this.step);

//    System.out.println("nValues:"+nValues);
//    System.out.println("mValues:"+((int) Math.round ((lastValue - firstValue) / this.majorStep) + 1));
  }


  
  /**
   * Create nice numbers in an unbound interval.
   * 
   * @param fromValue       From value.
   * @param toValue         To value.
   * @param nNumbersApprox  Approximate number of major nice numbers to produce.
   */
  public NiceNumberIterator (double fromValue, double toValue, int nNumbersApprox)
  {
    this (fromValue, toValue, nNumbersApprox, false);
  }



  /**
   * Initiate the iteration and return the iterator object
   * 
   * @return  The iterator (which is this).
   */
  public Iterator iterator()
  {
    valueNo = 0;
    return this;
  }
  
  
  
  /**
   * Return true if there are more nice numbers, false otherwise.
   * 
   * @return  True if there are more nice numbers, false otherwise.
   */
  public boolean hasNext()
  {
    return valueNo < nValues;
  }


  
  /**
   * Return the first nice number of the interval.
   * 
   * @return  First nice number of the interval.
   */
  public double getFirstValue()
  {
    return isBounded ? fromValue : firstValue;
  }


  
  /**
   * Return the last nice number of the interval.
   * 
   * @return  Last nice number of the interval.
   */
  public double getLastValue()
  {
    return isBounded ? toValue : lastValue;
  }


  
  /**
   * Return number of nice values in this interval.
   * 
   * @return  Total number of nice numbers in the interval.
   */
  public int getAllValueCount()
  {
    return nValues;
  }
  
  public int getMajorValueCount()
  {
    return nValues;
  }
  
  private boolean equals (double a, double b)
  {
    double limit = a == 0.0 ? 0.001 : Math.abs (a) * 0.001;
    return b > a - limit && b < a + limit;
  }

  
  
  public Object next()
  {
    // Solve the bounded case
    if (isBounded) {
      if (valueNo == 0) {
        double value = firstValue;

        while ((fromValue < toValue && value <= fromValue) ||
               (fromValue > toValue && value >= fromValue)) {
          valueNo++;
          value = firstValue + valueNo * this.step;
        }

        int rank = equals (firstValue, fromValue) ? 0 : 1;
        return new NiceNumber (fromValue, rank, 0.0);
      }
      else {
        double value = firstValue + valueNo * this.step;
        if ((fromValue < toValue && value >= toValue) ||
            (fromValue > toValue && value <= toValue)) {
          valueNo = nValues;

          int rank = equals (lastValue, toValue) ? 0 : 1;
          return new NiceNumber (toValue, rank, 1.0);
        }
      }
    }
    
    double value = firstValue + valueNo * this.step;

    int rank;
    if(valueNo % (int) majorStep == 0.0) 
      rank = 0;
    else if (valueNo % (int) minorStep == 0.0) 
      rank = 1;
    else                                         
      rank = 2;

    // Find position
    double first = getFirstValue();
    double last  = getLastValue();
    double position = first == last ? 0.0 : (value - first) / (last - first);
    
    NiceNumber niceNumber = new NiceNumber (value, rank, position);

    valueNo++;
    
    return niceNumber;
  }


  public void remove()
  {
    // Not possible
  }


  
  public static void main (String[] args)
  {
    NiceNumberIterator numbers = new NiceNumberIterator (1.0, 1246.0, 10, false);

    System.out.println( numbers.getAllValueCount());
    for (Iterator i = numbers.iterator(); i.hasNext(); ) {
      NiceNumber niceNumber = (NiceNumber) numbers.next();
      if (niceNumber.getRank() < 2)
        System.out.println (niceNumber);
    }
  }
}
