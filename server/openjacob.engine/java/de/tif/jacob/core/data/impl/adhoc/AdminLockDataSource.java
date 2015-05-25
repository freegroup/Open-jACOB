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
package de.tif.jacob.core.data.impl.adhoc;

import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.adjustment.ILocking;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.AdhocDataSource;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.model.Datasource;
import de.tif.jacob.core.model.Recordlock;

/**
 * Internal datasource implementation for handling distributed data about locks.
 * 
 * @author Andreas Sonntag
 */
public class AdminLockDataSource extends AdhocDataSource
{
  static public transient final String RCS_ID = "$Id: AdminLockDataSource.java,v 1.5 2010/06/29 12:39:05 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.5 $";
  
  private static final String QBE_DATASOURCE_NAME = "QBE_DATASOURCE_NAME";
  
  /**
   * @param name
   */
  public AdminLockDataSource(String name)
  {
    super(name);
  }
  
  /**
   * Method called in admin application to constrain datasources, i.e. to avoid blocking calls to inactive datasources.
   * @param context
   * @param datasourcenameConstrain
   */
  public static void constrainDatasource(Context context, String datasourcenameConstrain)
  {
    if (datasourcenameConstrain != null)
      context.setPropertyForRequest(QBE_DATASOURCE_NAME, datasourcenameConstrain);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.AdhocDataSource#initialize(de.tif.jacob.core.data.IDataAccessor)
   */
  protected void initialize(IDataAccessor accessor) throws Exception
  {
    String datasourcenameConstrain = (String) Context.getCurrent().getProperty(QBE_DATASOURCE_NAME);
    IDataAccessor datasourceAccessor = AdminApplicationProvider.newDataAccessor();
    IDataTable datasourceTable = datasourceAccessor.getTable(Datasource.NAME);
    if (datasourcenameConstrain != null)
      datasourceTable.qbeSetValue(Datasource.name, datasourcenameConstrain);
    datasourceTable.search();
    for (int i = 0; i < datasourceTable.recordCount(); i++)
    {
      // fetch database
      //
      IDataTableRecord datasourceRecord = datasourceTable.getRecord(i);
      initialize(accessor, datasourceRecord.getStringValue(Datasource.name));
    }

    // get locks from internal datasource as well
    initialize(accessor, datasourceTable.getTableAlias().getTableDefinition().getDataSourceName());
  }
  
  private static void initialize(IDataAccessor accessor, String datasourceName) throws Exception
  {
      // fetch database
      //
      DataSource dataSource = DataSource.get(datasourceName);
      try
      {
        dataSource.test();
      }
      catch (Exception ex)
      {
        // ignore -> obviously datasource is not ready
        return;
      }
      
      IDataTransaction trans = accessor.newTransaction();
      IDataTable recordLockTable = accessor.getTable(Recordlock.NAME);
      try
      {
        ILocking locking = dataSource.getAdjustment().getLockingImplementation();
        List currentLocks = locking.getCurrentLocks(dataSource);
        for (int j=0; j< currentLocks.size(); j++)
        {
          ILocking.IRecordLock recordLock = (ILocking.IRecordLock) currentLocks.get(j);
          
          IDataTableRecord recordLockRecord = recordLockTable.newRecord(trans);
          recordLockRecord.setValue(trans, Recordlock.datasource, datasourceName);
          recordLockRecord.setValue(trans, Recordlock.tablename, recordLock.getTableName());
          recordLockRecord.setValue(trans, Recordlock.keyvalue, recordLock.getKeyValue());
          recordLockRecord.setValue(trans, Recordlock.nodename, recordLock.getNodeName());
          recordLockRecord.setValue(trans, Recordlock.acquiredate, recordLock.created());
          recordLockRecord.setValue(trans, Recordlock.userid, recordLock.getUserId());
          recordLockRecord.setValue(trans, Recordlock.username, recordLock.getUserName());
        }
        
        trans.commit();
      }
      finally
      {
        trans.close();
      }
  }
  
}
