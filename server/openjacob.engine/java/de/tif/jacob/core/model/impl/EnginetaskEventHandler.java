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

package de.tif.jacob.core.model.impl;

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.model.Enginetask;
import de.tif.jacob.core.model.Enginetaskstatus;
import de.tif.jacob.screen.IClientContext;

/**
 * Event handler to handle changes of task status.
 * 
 * @author Andreas Sonntag
 */
public class EnginetaskEventHandler extends DataTableRecordEventHandler
{
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord enginetaskRecord) throws Exception
  {
    // check whether task status has been changed (by means of admin application)
    // Note: Ignore session tasks!
    //
    if (enginetaskRecord.isUpdated() && enginetaskRecord.hasChangedValue(Enginetask.taskstatus) && !Enginetask.scope_ENUM._session.equals(enginetaskRecord.getValue(Enginetask.scope)))
    {
      // only react on the changes coming from the admin application
      // note: this avoids a ping-pong effect when having multiple cluster nodes
      //
      if (Context.getCurrent() instanceof IClientContext)
      {
        // 1. update task status in persistent table
        //
        IDataAccessor accessor = enginetaskRecord.getAccessor();
        IDataTable enginetaskstatusTable = accessor.getTable(Enginetaskstatus.NAME);
        enginetaskstatusTable.qbeClear();
        enginetaskstatusTable.qbeSetKeyValue(Enginetaskstatus.applicationname, enginetaskRecord.getValue(Enginetask.applicationname));
        enginetaskstatusTable.qbeSetKeyValue(Enginetaskstatus.taskname, enginetaskRecord.getValue(Enginetask.name));
        enginetaskstatusTable.search();
        IDataTransaction trans = accessor.newTransaction();
        try
        {
          IDataTableRecord enginetaskstatusRecord;
          if (enginetaskstatusTable.recordCount() == 0)
          {
            enginetaskstatusRecord = enginetaskstatusTable.newRecord(trans);
            enginetaskstatusRecord.setValue(trans, Enginetaskstatus.applicationname, enginetaskRecord.getValue(Enginetask.applicationname));
            enginetaskstatusRecord.setValue(trans, Enginetaskstatus.taskname, enginetaskRecord.getValue(Enginetask.name));
          }
          else
          {
            enginetaskstatusRecord = enginetaskstatusTable.getRecord(0);
          }
          enginetaskstatusRecord.setValue(trans, Enginetaskstatus.taskstatus, enginetaskRecord.getValue(Enginetask.taskstatus));
          trans.commit();
        }
        finally
        {
          trans.close();
        }

        // 2. and notify all other nodes about the change
        //
        ClusterManager.getProvider().notifyTaskStatusChanged(enginetaskRecord.getStringValue(Enginetask.applicationname), enginetaskRecord.getStringValue(Enginetask.name),
            enginetaskRecord.getStringValue(Enginetask.taskstatus));
      }
    }
  }
}
