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

import jacob.model.Loggingconfig;
import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author andreas
 */
public class LoggingconfigTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: LoggingconfigTableRecord.java,v 1.1 2007/01/19 07:44:33 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if (tableRecord.isDeleted())
      return;

    // the status has changed to active?
    if (Loggingconfig.status_ENUM._active.equals(tableRecord.getValue(Loggingconfig.status)) && tableRecord.hasChangedValue(Loggingconfig.status))
    {
      // switch old active configuration record to inactive!
      //
      IDataTable dataTable = tableRecord.getAccessor().newAccessor().getTable(tableRecord.getTableAlias());
      dataTable.qbeSetKeyValue(Loggingconfig.status, Loggingconfig.status_ENUM._active);
      dataTable.qbeSetKeyValue(Loggingconfig.type, tableRecord.getValue(Loggingconfig.type));
      dataTable.search();
      for (int i = 0; i < dataTable.recordCount(); i++)
      {
        IDataTableRecord otherConfigRecord = dataTable.getRecord(i);
        if (otherConfigRecord.getPrimaryKeyValue().equals(tableRecord.getPrimaryKeyValue()))
          continue;

        otherConfigRecord.setValue(transaction, Loggingconfig.status, Loggingconfig.status_ENUM._inactive);
      }
    }
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
    ClusterManager.getProvider().propagateLoggingChange();
	}
}
