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

import de.tif.jacob.core.definition.IBrowserTableField;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.SortOrder;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QBEField
{
  static public transient final String        RCS_ID = "$Id: QBEField.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";
  
  private final ITableAlias tableAlias;
	private final ITableField tableField;
  private final SortOrder sortOrder;
  private final boolean keepEmpty;

	/**
	 * @param tableAlias
	 * @param tableField
	 */
  public QBEField(ITableAlias tableAlias, ITableField tableField)
  {
    this.tableAlias = tableAlias;
    this.tableField = tableField;
    this.sortOrder = SortOrder.NONE;
    this.keepEmpty = false;
  }

  public QBEField(IBrowserTableField browserField, boolean keepEmpty)
  {
    this.tableAlias = browserField.getTableAlias();
    this.tableField = browserField.getTableField();
    this.sortOrder = browserField.getSortorder();
    this.keepEmpty = keepEmpty;
  }

  public QBEField()
  {
    this.tableAlias = null;
    this.tableField = null;
    this.sortOrder = SortOrder.NONE;
    this.keepEmpty = true;
  }
  
  private boolean isDummy()
  {
    return this.tableAlias == null && this.tableField == null;
  }

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object anObject)
	{
		if (this == anObject)
		{
			return true;
		}
		if (anObject instanceof QBEField)
		{
			QBEField another = (QBEField) anObject;
			if (this.isDummy())
			  return another.isDummy();
			return this.tableAlias.equals(another.tableAlias) && this.tableField.equals(another.tableField);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
	  if (isDummy())
	    return 0;
		return this.tableAlias.hashCode() + this.tableField.hashCode();
	}

	/**
	 * toString methode: creates a String representation of the object
	 * 
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	 *  
	 */
	public String toString()
	{
	  if (isDummy())
	    return "dummy";
	  
	  StringBuffer buffer = new StringBuffer();
		buffer.append(tableAlias).append(".").append(tableField);
		return buffer.toString();
	}
  
	/**
	 * @return Returns the tableAlias.
	 */
	public ITableAlias getTableAlias()
	{
		return tableAlias;
	}

	/**
	 * @return Returns the tableField.
	 */
	public ITableField getTableField()
	{
		return tableField;
	}

	/**
	 * @return Returns the sortOrder.
	 */
	public SortOrder getSortOrder()
	{
		return sortOrder;
	}

	/**
	 * @return Returns the keepEmpty.
	 */
	public boolean isKeepEmpty()
	{
		return keepEmpty;
	}

}
