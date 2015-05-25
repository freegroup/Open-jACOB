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
/*
 * Created on 15.09.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data.impl;

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface IDataBrowserSortStrategy
{
	public static final String RCS_ID = "$Id: IDataBrowserSortStrategy.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
	public static final String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Adds a new sort criteria to this sort strategy.
	 * <p>
	 * Note: Sort criterias added first have higher priority than subsequent criterias.
	 * 
	 * @param columnIndex
	 *          the column to sort
	 * @param sortOrder
	 *          the order of the column to sort
	 */
  public void add(int columnIndex, SortOrder sortOrder);

	/**
	 * Checks whether a column is involved in this sort strategy
	 * 
	 * @param columnIndex
	 *          the column index
	 * @return the sort order of the column or <code>SortOrder.NONE</code> if the column is not involved
	 */
  public SortOrder isColumnInvolved(int columnIndex);
	
	public int recordCount();
	
	public IDataBrowserRecord getRecord(int index) throws IndexOutOfBoundsException;
	
	/**
	 * @return Returns the index of the selected record or <code>-1</code>, if no record is
	 *         selected.
	 */
	public int getSelectedRecordIndex();
	
	public void setSelectedRecordIndex(int index);
	
  public IDataTableRecord getRecordToUpdate(IDataTransaction transaction, int index) throws RecordLockedException, RecordNotFoundException;
}
