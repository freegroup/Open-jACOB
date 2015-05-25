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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.ManagedResource;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.RecordLockedException;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class DataRecordLock extends ManagedResource
{
	static public final transient String RCS_ID = "$Id: DataRecordLock.java,v 1.2 2007/06/27 17:35:05 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	static private final transient Log logger = LogFactory.getLog(DataRecordLock.class);

	private final IDataTransaction transaction;
	private final IDataRecordId recordId;

	/**
	 *  
	 */
	protected DataRecordLock(IDataTransaction transaction, DataSource dataSource, IDataRecordId recordId) throws RecordLockedException
	{
		this.transaction = transaction;
		this.recordId = recordId;

		// create lock
		dataSource.lock(transaction, recordId);

		// register the resource for the life cycle management if they successful 
		// created.
		//
		Context.getCurrent().registerForWindow(this);
	}
  
  public void unlock()
  {
    release();
    
    // Unregister the ManagedResource in the context. It is not necessary to release this
    // resource with a life cycle management. 
    //
    Context.getCurrent().unregister(this);
  }

	public void release()
	{
    try
    {
      DataSource dataSource = DataSource.get(this.recordId.getTableDefinition().getDataSourceName());
      dataSource.unlock(this.transaction, this.recordId);
    }
    catch (Exception e)
    {
      if (logger.isWarnEnabled())
        logger.warn("Unlock of record " + this.recordId + " failed", e);
    }
	}
}
