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

import jacob.model.Japplversion;
import jacob.model.Useddatasource;
import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.definition.ITableField;

/**
 * @author andreas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JapplversionTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: JapplversionTableRecord.java,v 1.3 2009/04/21 14:48:52 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // remove all underlying entries as well
    //
    IDataTable useddatasourceTable = tableRecord.getAccessor().getTable(Useddatasource.NAME);
    useddatasourceTable.qbeClear();
    useddatasourceTable.qbeSetKeyValue(Useddatasource.applicationname, tableRecord.getStringValue(Japplversion.name));
    useddatasourceTable.qbeSetKeyValue(Useddatasource.applicationversion, tableRecord.getStringValue(Japplversion.version));
    useddatasourceTable.fastDelete(transaction);
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
  public void beforeCommitAction(IDataTableRecord japplversionRecord, IDataTransaction transaction) throws Exception
  {
    if (!japplversionRecord.isDeleted())
    {
      // generate new sequence number
      ITableField seqField = japplversionRecord.getFieldDefinition(Japplversion.installseqnbr);
      DataSource dataSource = DataSource.get(seqField.getTableDefinition().getDataSourceName());
      long installseqNbr = dataSource.newJacobIds(seqField.getTableDefinition(), seqField, 1);
      japplversionRecord.setLongValue(transaction, Japplversion.installseqnbr, installseqNbr);
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
    if (tableRecord.isDeleted())
    {
      // application version is uninstalled -> undeploy it
      ClusterManager.getProvider().notifyUndeployApplication(tableRecord.getStringValue(Japplversion.name), tableRecord.getStringValue(Japplversion.version));
    }
    else
    {
      // simply send a notification to check for installed application changes
      ClusterManager.getProvider().notifyCheckApplications();
    }
  }
}
