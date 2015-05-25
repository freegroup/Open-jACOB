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

package de.tif.jacob.core.data.impl.sql;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import de.tif.jacob.core.data.impl.EnterpriseDataSourceProvider;
import de.tif.jacob.core.model.Datasource;

public final class OracleDataSourceProvider extends EnterpriseDataSourceProvider
{
  static public transient final String RCS_ID = "$Id: OracleDataSourceProvider.java,v 1.3 2009-07-30 12:55:17 sonntag Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";
  
  public Class getDataSourceClassByName(String rdbName)
  {
    if (Datasource.rdbType_ENUM._Oracle.equals(rdbName))
      return Oracle8DataSource.class;
    if (Datasource.rdbType_ENUM._Oracle9.equals(rdbName))
      return Oracle9DataSource.class;
    return null;
  }
  
  public Class getDataSourceClassByMeta(DatabaseMetaData meta) throws SQLException
  {
    if ("Oracle".equals(meta.getDatabaseProductName()))
    {
    // Achtung: Wegen Kompatibilität zu vorhandenen jACOBPersistenceDatasources immer die alte Version des Adapters nehmen!
//      int majorDatabaseVersion = OracleDataSource.extractMajorVersionNumber(meta);
//      if (majorDatabaseVersion >= 9)
//        return Oracle9DataSource.class;
      return Oracle8DataSource.class;
    }
    return null;
  }

  public Class getLdapDataSourceClass()
  {
    return null;
  }
}
