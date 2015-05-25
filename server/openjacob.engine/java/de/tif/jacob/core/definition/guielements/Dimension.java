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

package de.tif.jacob.core.definition.guielements;

import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Dimension
{
  static public final transient String RCS_ID = "$Id: Dimension.java,v 1.2 2007/10/10 09:04:53 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private final int top;
  private final int left;
  private final int width;
  private final int height;
  
  public Dimension(CastorDimension dim)
  {
    this.top = Math.max(0, dim.getY()); // < 0 is not a valid position. 
    this.left = Math.max(0,dim.getX()); // < 0 is not a valid position.
    this.width = dim.getWidth();
    this.height = dim.getHeight();
  }
  
  public Dimension(int left, int top, int width, int height)
  {
    this.top = top;
    this.left = left;
    this.width = width;
    this.height = height;
  }
  

	/**
	 * @return Returns the height.
	 */
	public final int getHeight()
	{
		return height;
	}

	/**
	 * @return Returns the left.
	 */
	public final int getLeft()
	{
		return left;
	}

	/**
	 * @return Returns the top.
	 */
	public final int getTop()
	{
		return top;
	}

	/**
	 * @return Returns the width.
	 */
	public final int getWidth()
	{
		return width;
	}

  public final CastorDimension toJacob()
  {
    CastorDimension jacobDimension = new CastorDimension();
    jacobDimension.setX(getLeft());
    jacobDimension.setY(getTop());
    jacobDimension.setHeight(getHeight());
    jacobDimension.setWidth(getWidth());
    return jacobDimension;
  }
  
	/**
	 * toString methode: creates a String representation of the object
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	
	 */
	public final String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Dimension[");
		buffer.append("top = ").append(top);
		buffer.append(", left = ").append(left);
		buffer.append(", width = ").append(width);
		buffer.append(", height = ").append(height);
		buffer.append("]");
		return buffer.toString();
	}
}
