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

package de.tif.jacob.core.data.impl.qbe;


/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class QBEWildcard
{
  static public transient final String RCS_ID = "$Id: QBEWildcard.java,v 1.2 2008/01/24 14:38:20 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  public static final QBEWildcard MULTIPLE = new QBEWildcard(0, true);
  
	private int times;
  private boolean multiple; 

  protected QBEWildcard(int times, boolean multiple)
  {
    this.times = times;
    this.multiple = multiple;
  }

  protected QBEWildcard()
  {
    this.times = 1;
    this.multiple = false;
  }

  protected void inc()
  {
    this.times++;
  }
  
  public boolean isMultipleTimes()
  {
    return this.multiple;
  }
  
  public void setMultipleTimes()
  {
    this.multiple =true;
  }
  
  public int getTimes()
  {
    return this.times;
  }
  
	public String toString()
	{
		StringBuffer buffer = new StringBuffer(this.times);
    if (this.multiple)
      buffer.append("*");
		for (int i = 0; i < this.times; i++)
		{
			buffer.append('?');
		}
		return buffer.toString();
	}
}
