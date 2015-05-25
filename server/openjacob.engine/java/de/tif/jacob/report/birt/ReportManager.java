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

package de.tif.jacob.report.birt;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.report.birt.impl.BirtEngineManager;

/**
 * @since 2.6
 */
public final class ReportManager
{
  public static final transient String RCS_ID = "$Id: ReportManager.java,v 1.3 2008/11/29 18:41:01 freegroup Exp $";
  public static final transient String RCS_REV = "$Revision: 1.3 $";

  private static final transient Log logger = LogFactory.getLog(ReportManager.class);

  private static final String ODA_Driver_Class = "odaDriverClass";
  private static final String ODA_URL = "odaURL";
  private static final String ODA_USER = "odaUser";
  private static final String ODA_PASSWORD = "odaPassword";

  /**
   * Creates a BIRT report task for the given BIRT report definition.
   * <p>
   * The configuration of BIRT report data sources, which match to jACOB SQL
   * data sources, is adapted (i.e. changed) in such a way that the already
   * given jACOB data source configuration is used. By this way BIRT reports
   * become "environment/database" independant.
   * 
   * @param rptStream
   *          input stream containing BIRT report definition, which will be
   *          closed automatically
   * @return BIRT report task
   * @throws Exception
   *           on any problem
   */
  public static IRunAndRenderTask createRunAndRenderTask(InputStream rptStream) throws Exception
  {
    IReportRunnable design = openReportDesign(rptStream);
    String dataSourceName  = getDataSourceName(design);
    // Create task to run the report - use the task to execute and run the
    // report,
    IRunAndRenderTask task= design.getReportEngine().createRunAndRenderTask(design);
    
    return new RunAndRenderTask(task,  getConnection(dataSourceName) );
  }

  private static Connection getConnection(String dataSourceName) throws SQLException
  {
    DataSource dataSource;
    try
    {
      dataSource = DataSource.get(dataSourceName);
    }
    catch (Exception ex)
    {
      throw new SQLException("Could not access data source '" + dataSourceName + "': " + ex.toString());
    }

    if (dataSource instanceof SQLDataSource)
    {
      return ((SQLDataSource) dataSource).getConnection();
    }

    throw new SQLException("Data source '" + dataSourceName + "' is not an SQL data source");
  }

  private static String getDataSourceName(IReportRunnable report) throws Exception
  {
    ReportDesignHandle handle = (ReportDesignHandle) report.getDesignHandle();
    // Loop over all data source definitions and search for data sources
    // which map to jACOB data sources.
    // Note: Mapping is done by name!
    //
    SlotHandle slotHandle = handle.getDataSources();
    Iterator iter = slotHandle.iterator();
    while (iter.hasNext())
    {
      Object next = iter.next();

      // Data source definition is an ODA data source.
      // Note: Should always be the case?
      //
      if (next instanceof OdaDataSourceHandle)
      {
        OdaDataSourceHandle odaDataSourceHandle = (OdaDataSourceHandle) next;

        return odaDataSourceHandle.getName();
      }
    }    
    return null;
  }
  
  private static IReportRunnable openReportDesign(InputStream rptStream) throws Exception
  {
    ReportEngine engine = BirtEngineManager.getEngine();

    // open a report design - use design to modify design, retrieve embedded
    // images etc.
    return engine.openReportDesign(rptStream);
  }
}
