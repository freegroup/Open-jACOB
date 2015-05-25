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

package de.tif.jacob.core.data.impl.index.update;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.index.IndexDataSource;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.scheduler.SchedulerTask;

/**
 * Abstract task to run for an index datasource.
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public abstract class IndexTask extends SchedulerTask
{
  static public final transient String RCS_ID = "$Id: IndexTask.java,v 1.1 2010/07/13 17:55:23 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  static private final transient Log logger = LogFactory.getLog(IndexTask.class);

  protected static final String TASK_NAME_PREFIX = "Index:";

  private final String datasourceIndexName;

  /**
   * Constructor
   * 
   * @param datasourceIndexName
   */
  protected IndexTask(String datasourceIndexName)
  {
    this.datasourceIndexName = datasourceIndexName;
  }

  public final String getApplicationName()
  {
    return AdminApplicationProvider.ADMIN_APPLICATION_NAME;
  }

  public final Version getApplicationVersion()
  {
    return null;
  }

  public final boolean runPerInstance()
  {
    return false;
  }

  public final Scope getScope()
  {
    return CLUSTER_SCOPE;
  }

  public final String getTaskDetails()
  {
    return this.datasourceIndexName;
  }

  protected abstract void run(IndexDataSource indexDataSource) throws Exception;
  
  public void run() throws Exception
  {
    DataSource dataSource = DataSource.get(this.datasourceIndexName);
    if (dataSource instanceof IndexDataSource)
    {
      run((IndexDataSource) dataSource);
    }
    else
    {
      logger.warn("Index '" + this.datasourceIndexName + "' is not configured as an index data source (" + dataSource.getClass().getName() + ")");
    }
  }
}
