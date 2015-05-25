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

class NiceNumber
{
  private double  value;
  private double  position;  // Realtive. First is 0.0, last is 1.0
  private int     rank;      // Major is 0, minor is 1, minorminor is 2 ...

  
  NiceNumber (double value, int rank, double position)
  {
    this.value    = value;
    this.rank     = rank;
    this.position = position;
  }

  public double getValue()
  {
    return value;
  }


  public int getRank()
  {
    return rank;
  }

  
  /**
   * Create a string representation of this instance.
   * 
   * @return  A string representation of this class.
   */
  public String toString()
  {
    switch (rank) {
      case 0  : 
        return "==== " + Double.toString (value)+ "\t"+position;
      case 1  : 
        return "--   " + Double.toString (value)+ "\t"+position;
      default : 
        return "-    " + Double.toString (value)+ "\t"+position;  
    }
  }

  protected double getPosition()
  {
    return position;
  }
}