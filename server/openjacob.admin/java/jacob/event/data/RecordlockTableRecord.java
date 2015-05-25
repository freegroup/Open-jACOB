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
package jacob.event.data;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.DataSource;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RecordlockTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: RecordlockTableRecord.java,v 1.1 2007/01/19 07:44:33 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord arg0) throws Exception
	{
		// nothing to do here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
	 *      de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord arg0, IDataTransaction arg1) throws Exception
	{
		// nothing to do here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord,
	 *      de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord arg0, IDataTransaction arg1) throws Exception
	{
		// nothing to do here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
	 *      de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord record, IDataTransaction transaction) throws Exception
	{
		if (record.isDeleted())
		{
		  // and physically delete lock from data source
		  //
			DataSource dataSource = DataSource.get(record.getStringValue("datasource"));
			String tablename = record.getStringValue("tablename");
			String keyvalue = record.getStringValue("keyvalue");
			dataSource.getAdjustment().getLockingImplementation().removeLock(dataSource, tablename, keyvalue);
		}
	}

}
