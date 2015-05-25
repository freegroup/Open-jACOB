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

public final class MsSQLServerDataSourceProvider extends EnterpriseDataSourceProvider
{
  static public transient final String RCS_ID = "$Id: MsSQLServerDataSourceProvider.java,v 1.1 2006-12-21 11:25:22 sonntag Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  public Class getDataSourceClassByName(String rdbName)
  {
    if ("MSSQL".equals(rdbName))
      return MsSQLServerDataSource.class;
    return null;
  }

  public Class getDataSourceClassByMeta(DatabaseMetaData meta) throws SQLException
  {
    if ("Microsoft SQL Server".equals(meta.getDatabaseProductName()))
      return MsSQLServerDataSource.class;
    return null;
  }

  public Class getLdapDataSourceClass()
  {
    return null;
  }
}
