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

package de.tif.jacob.core;

/**
 * A managed resource. You can register a instance of a ManagedResource at the
 * context for different life cycles. <br>
 * The context calls {@link #release()}if the scope of the resource will be
 * dropped. <br>
 * 
 * @author Andreas Herz
 */
public abstract class ManagedResource
{
  static public final String RCS_ID = "$Id: ManagedResource.java,v 1.1 2007/01/19 16:38:08 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.1 $";
  
  
  /**
   * Called from the application if the corresponding user session will
   * be dropped. 
   */
  public abstract void release();
  
  
  /**
   * make sure default implementation is not overwritten
   */
	public final boolean equals(Object obj)
	{
		return super.equals(obj);
	}

	/**
	 * make sure default implementation is not overwritten
	 */
	public final int hashCode()
	{
    return super.hashCode();
	}

}
