/*******************************************************************************
 *    This file is part of jACOB
 *    Copyright (C) 2005-2009 Tarragon GmbH
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

import java.sql.Types;

import de.tif.jacob.core.data.IDataTableRecord;

public final class Oracle9DataSource extends OracleDataSource
{
  static public transient final String RCS_ID = "$Id: Oracle9DataSource.java,v 1.1 2009-07-28 23:05:54 sonntag Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  public Oracle9DataSource(AutoDetectSQLDataSource parent)
  {
    super(parent);
  }

  public Oracle9DataSource(IDataTableRecord record) throws Exception
  {
    super(record);
  }

  public Oracle9DataSource(String name) throws Exception
  {
    super(name);
  }

  public boolean needsMappingOfLongText()
  {
    return false;
  }

  public boolean needsMappingOfBinary()
  {
    return false;
  }
  
  public int getDefaultBinarySQLType()
  {
    return Types.BLOB;
  }

  public int getDefaultLongTextSQLType()
  {
    return Types.CLOB;
  }
}
