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

package jacob.event.ui.japplversion;

import jacob.model.Datasource;
import jacob.model.Useddatasource;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.exception.UndefinedDatasourceException;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 *
 * @author andreas
 */
public class UseddatasourceIFBrowserIFB extends de.tif.jacob.screen.event.IBrowserEventHandler
{
  static public final transient String RCS_ID = "$Id: UseddatasourceIFBrowserIFB.java,v 1.2 2010/06/29 12:40:15 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private static final int NAME_INDEX = 0;
  private static final int STATUS_INDEX = 1;

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IBrowserEventHandler#filterCell(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IBrowser, int, int, java.lang.String)
   */
  public String filterCell(IClientContext context, IBrowser browser, int row, int column, String data) throws Exception
  {
    if (column == STATUS_INDEX)
    {
      String datasourceName = browser.getData().getRecord(row).getStringValue(NAME_INDEX);
      try
      {
        DataSource datasource = DataSource.get(datasourceName);
        return datasource.isPredefined() ? "predefined" : "defined";
      }
      catch (Exception ex)
      {
        return "undefined";
      }
    }
    return data;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IBrowserEventHandler#onRecordSelect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IBrowser, de.tif.jacob.core.data.IDataTableRecord)
   */
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
  {
    String datasourceName = selectedRecord.getStringValue(Useddatasource.datasourcename);
    String datasourceType = selectedRecord.getStringValue(Useddatasource.datasourcetype);
    try
    {
      DataSource datasource = DataSource.get(datasourceName);
      if (datasource.isPredefined())
      {
        context.createMessageDialog("Predefined datasources cannot be configured").show();
      }
      else
      {
        // backfill defined datasource
        IDataAccessor accessor = context.getDataAccessor();
        accessor.qbeClearAll();
        IDataTable table = accessor.getTable(Datasource.NAME);
        table.qbeSetKeyValue(Datasource.name, datasource.getName());
        IDataBrowser dataBrowser = accessor.getBrowser("datasourceBrowser");
        dataBrowser.search(accessor.getApplication().getDefaultRelationSet(), Filldirection.BOTH);
        dataBrowser.setSelectedRecordIndex(0);
        dataBrowser.propagateSelections();

        context.setCurrentForm("datasourceform");
      }
    }
    catch (UndefinedDatasourceException ex)
    {
      // create new datasource but do not commit
      IDataTable locationTable = context.getDataTable(Datasource.NAME);
      IDataTransaction trans = locationTable.startNewTransaction();
      IDataTableRecord record = locationTable.newRecord(trans);
      record.setValue(trans, Datasource.name, datasourceName);
      if (Useddatasource.datasourcetype_ENUM._index.equals(datasourceType))
      {
        record.setValue(trans, Datasource.rdbtype, Datasource.rdbType_ENUM._Lucene);
        record.setValue(trans, Datasource.adjustment, Datasource.adjustment_ENUM._jACOB);
        record.setValue(trans, Datasource.location, Datasource.location_ENUM._jACOB);
        record.setValue(trans, Datasource.connectstring, "index:lucene:fsDirectory:<DIRECTORYNAME>");
      }

      context.setCurrentForm("datasourceform");
    }
  }
}
