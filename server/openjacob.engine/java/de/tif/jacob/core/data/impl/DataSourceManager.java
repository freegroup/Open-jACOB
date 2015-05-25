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


import org.hsqldb.DatabaseManager;

import de.tif.jacob.core.BootstrapEntry;

/**
 * The datasource manager ensures that all active datasources are properly
 * destroyed, if jACOB is shutdowned.
 * 
 * @author Andreas Sonntag
 */
public class DataSourceManager extends BootstrapEntry
{
  /**
   * Public default constructor.
   */
  public DataSourceManager()
  {
    // nothing to do here
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    // destroy all datasources, i.e to release connections in pools, if
    // jACOB is restarted as web application in the web server.
    //
    DataSource.destroyAll();
    
    // to avoid at shutdown:
    // Exception in thread "HSQLDB Timer @a97f68" java.lang.NullPointerException
    // at org.hsqldb.lib.HsqlTimer.nextTask(Unknown Source)
    // at org.hsqldb.lib.HsqlTimer$TaskRunner.run(Unknown Source)
    // at java.lang.Thread.run(Thread.java:595)
    DatabaseManager.getTimer().shutDown();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#init()
   */
  public void init() throws Throwable
  {
    // nothing to do here
  }
}
