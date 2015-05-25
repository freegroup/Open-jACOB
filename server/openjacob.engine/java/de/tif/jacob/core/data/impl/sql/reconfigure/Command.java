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
/*
 * Created on 10.01.2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data.impl.sql.reconfigure;

import java.sql.Types;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;

/**
 * Abstract base class of a reconfigure command.
 * 
 * @author Andreas Sonntag
 */
public abstract class Command
{
  /**
   * SQL statements to execute this command.
   * 
   * @param dataSource the data source to reconfigure
   * 
   * @return List of <code>String</code> or {@link ReconfigureComment}
   */
  public List getSQLStatements(SQLDataSource dataSource)
  {
    return Collections.EMPTY_LIST;
  }
  
  protected static String escapeDefaultValue(SQLDataSource dataSource, int sqlType, String value)
  {
    switch (sqlType)
    {
      case Types.CHAR:
      case Types.VARCHAR:
      case Types.LONGVARCHAR:
        return dataSource.convertToSQL(value, true);
    }
    return value;
  }

  protected static String createUniqueConstraintFragment(SQLDataSource dataSource, ISchemaKeyDefinition index)
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("CONSTRAINT ");
    dataSource.appendDBName(buffer, index.getDBName()).append(" UNIQUE (");
    Iterator columnNameIter = index.getSchemaColumnNames();
    for (int j = 0; columnNameIter.hasNext(); j++)
    {
      if (j != 0)
        buffer.append(",");
      dataSource.appendDBName(buffer, (String) columnNameIter.next());
    }
    buffer.append(")");
    return buffer.toString();
  }

  protected static String createIndexStatement(SQLDataSource dataSource, ISchemaTableDefinition table, ISchemaKeyDefinition index)
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("CREATE");
    if (index.isUnique() && (index.hasRequiredColumns() || dataSource.supportsMultipleNullsForUniqueIndices()))
      buffer.append(" UNIQUE");
    buffer.append(" INDEX ");
    dataSource.appendDBName(buffer, index.getDBName()).append(" ON ");
    dataSource.appendDBName(buffer, table.getDBName()).append(" (");
    Iterator columnNameIter = index.getSchemaColumnNames();
    for (int j = 0; columnNameIter.hasNext(); j++)
    {
      if (j != 0)
        buffer.append(",");
      dataSource.appendDBName(buffer, (String) columnNameIter.next());
    }
    buffer.append(")");
    return buffer.toString();
  }

}
