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

import de.tif.jacob.core.data.IDataTableRecord;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class DataExecutionContext
{
  static public transient final String        RCS_ID = "$Id: DataExecutionContext.java,v 1.1 2007/01/19 09:50:34 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";
  
  private IDataTableRecord currentRecord;
  
  private final DataSource dataSource;
  private final DataTransaction transaction;
  
  protected DataExecutionContext(DataSource dataSource, DataTransaction transaction)
	{
		this.dataSource = dataSource;
		this.transaction = transaction;
	}

	public abstract void commit() throws Exception;

	public abstract void rollback();

	public abstract void close();
  
	/**
	 * @return Returns the currentRecord.
	 */
	public final IDataTableRecord getCurrentRecord()
	{
		return currentRecord;
	}

	/**
	 * @param currentRecord The currentRecord to set.
	 */
	protected final void setCurrentRecord(IDataTableRecord currentRecord)
	{
		this.currentRecord = currentRecord;
	}

	/**
	 * @return Returns the dataSource.
	 */
	public final DataSource getDataSource()
	{
		return dataSource;
	}

  /**
   * @return Returns the transaction.
   */
  public final DataTransaction getTransaction()
  {
    return transaction;
  }
  
}
