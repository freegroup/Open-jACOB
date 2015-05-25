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

import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.definition.ITableDefinition;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class DataRecordId implements IDataRecordId
{
  static public transient final String RCS_ID = "$Id: DataRecordId.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";

  private final IDataKeyValue primaryKeyValue;
  private final ITableDefinition tableDefinition;
//  private final int pseudoId;

  public DataRecordId(ITableDefinition tableDefinition, IDataKeyValue primaryKeyValue)
  {
    this.primaryKeyValue = primaryKeyValue;
    this.tableDefinition = tableDefinition;
//    this.pseudoId = 0;
  }

  // IBIS: pseudoId wird nie benutzt. Eventuell entfernen
  protected DataRecordId(ITableDefinition tableDefinition, int pseudoId)
  {
    this.primaryKeyValue = null;
    this.tableDefinition = tableDefinition;
//    this.pseudoId = pseudoId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object anObject)
  {
    if (this == anObject)
    {
      return true;
    }
    if (anObject instanceof DataRecordId)
    {
      DataRecordId another = (DataRecordId) anObject;

      if (this.primaryKeyValue != null && this.primaryKeyValue.equals(another.primaryKeyValue))
      {
        return this.tableDefinition.equals(another.tableDefinition);
      }
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    if (this.primaryKeyValue == null)
      return super.hashCode();
    return 31 * this.tableDefinition.hashCode() + this.primaryKeyValue.hashCode();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer(64);
    buffer.append(this.tableDefinition.getDataSourceName());
    buffer.append(".");
    buffer.append(this.tableDefinition.getDBName());
    buffer.append(":");
    if (this.primaryKeyValue == null)
      buffer.append(super.hashCode());
    else
      buffer.append(this.primaryKeyValue.toString());
    return buffer.toString();
  }

  
	/**
	 * @return Returns the primaryKeyValue.
	 */
	public IDataKeyValue getPrimaryKeyValue()
	{
		return primaryKeyValue;
	}

	/**
	 * @return Returns the tableDefinition.
	 */
	public ITableDefinition getTableDefinition()
	{
		return tableDefinition;
	}

}
