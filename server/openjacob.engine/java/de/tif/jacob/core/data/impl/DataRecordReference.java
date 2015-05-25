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

import java.io.Serializable;

import de.tif.jacob.core.data.IDataKeyValue;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataRecordReference extends DataReference implements Serializable
{
  static public transient final String        RCS_ID = "$Id: DataRecordReference.java,v 1.1 2007/01/19 09:50:34 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";
  
  private final String tableAliasName;
  private final IDataKeyValue primaryKeyValue;
  
  protected DataRecordReference(String tableAliasName, IDataKeyValue primaryKeyValue)
  {
    this.tableAliasName = tableAliasName;
    this.primaryKeyValue = primaryKeyValue; 
  }
  
	/*
   * IBIS: Wird von mir nicht mehr benötigt. Kann eventuell entfernt werden. (A.Herz)
   * @deprecated (?)
   */ 
  public static DataRecordReference createByReference(String referenceString)
  {
    return (DataRecordReference) createInstanceByReference(referenceString);
  }

	/**
	 * @return Returns the primaryKeyValue.
	 */
	public final IDataKeyValue getPrimaryKeyValue()
	{
		return primaryKeyValue;
	}

	/**
	 * @return Returns the tableAliasName.
	 */
	public final String getTableAliasName()
	{
		return tableAliasName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("DataRecordReference[");
		buffer.append("tableAliasName = ").append(tableAliasName);
		buffer.append(", primaryKeyValue = ").append(primaryKeyValue);
		buffer.append("]");
		return buffer.toString();
	}
}
