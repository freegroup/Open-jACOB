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

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.AdhocDataSource;

/**
 * Internal datasource implementation for handling distributed cluster data.
 * 
 * @author Andreas Sonntag
 */
public class AdminClusterDataSource extends AdhocDataSource
{
  static public transient final String RCS_ID = "$Id: AdminClusterDataSource.java,v 1.1 2007/01/19 09:50:47 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  /**
   * @param name
   */
  public AdminClusterDataSource(String name)
  {
    super(name);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.AdhocDataSource#initialize(de.tif.jacob.core.data.IDataAccessor)
   */
  protected void initialize(IDataAccessor accessor) throws Exception
  {
    IDataTransaction trans = accessor.newTransaction();
    try
    {
      // create cluster node entries
      //
      IDataTable clusterNodeTable = accessor.getTable("clusternode");
      ClusterManager.getProvider().fetchClusterNodeInfo(clusterNodeTable, trans);

      // create license node entries
      //
      IDataTable licenseNodeTable = accessor.getTable("licensenode");
      ClusterManager.getProvider().fetchLicenseNodeInfo(licenseNodeTable, trans);

      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }
}
