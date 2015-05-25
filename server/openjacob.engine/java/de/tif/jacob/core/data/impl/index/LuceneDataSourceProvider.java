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

package de.tif.jacob.core.data.impl.index;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import de.tif.jacob.core.data.impl.DataSourceProvider;

/**
 * Lucene data source implementation provider. 
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public final class LuceneDataSourceProvider extends DataSourceProvider
{
  static public transient final String RCS_ID = "$Id: LuceneDataSourceProvider.java,v 1.1 2010/06/29 12:39:00 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  public Class getDataSourceClassByName(String rdbName)
  {
    if ("Lucene".equals(rdbName))
      return LuceneDataSource.class;
    return null;
  }
  
  public Class getDataSourceClassByMeta(DatabaseMetaData meta) throws SQLException
  {
    return null;
  }

  public Class getLdapDataSourceClass()
  {
    return null;
  }
}
