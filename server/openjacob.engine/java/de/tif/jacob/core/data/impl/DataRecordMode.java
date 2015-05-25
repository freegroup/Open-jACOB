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
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class DataRecordMode
{
  static public transient final String        RCS_ID = "$Id: DataRecordMode.java,v 1.1 2007/01/19 09:50:34 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";
  
  public static final DataRecordMode NORMAL = new DataRecordMode("NORMAL");
  public static final DataRecordMode NEW = new DataRecordMode("NEW");
  public static final DataRecordMode UPDATE = new DataRecordMode("UPDATE");
  public static final DataRecordMode DELETE = new DataRecordMode("DELETE");
  
  private final String name;
  
	/**
	 * 
	 */
	private DataRecordMode(String name)
	{
    this.name = name;
	}

  public String toString()
  {
    return this.name;
  }
}
