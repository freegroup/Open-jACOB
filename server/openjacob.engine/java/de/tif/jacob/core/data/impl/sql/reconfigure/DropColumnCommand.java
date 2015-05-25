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
 * Created on 11.01.2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data.impl.sql.reconfigure;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.schema.ISchemaColumnDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DropColumnCommand extends AlterTableSubCommand
{
  protected final ISchemaColumnDefinition currentColumn;
  
  public DropColumnCommand(ISchemaColumnDefinition currentColumn)
  {
    this.currentColumn = currentColumn;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.schema.Command#getAlterTableDropFragment(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public String getAlterTableDropFragment(SQLDataSource dataSource)
  {
    StringBuffer buffer = new StringBuffer("COLUMN ");
    dataSource.appendDBName(buffer, this.currentColumn.getDBName());
    return buffer.toString();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.reconfigure.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public List getSQLStatements(SQLDataSource dataSource)
  {
    // just for Quintus!
    if (dataSource.isQuintusAdjustment())
    {
      if (this.currentColumn.isEnumeration())
      {
        List statements = new ArrayList();
        statements.addAll(super.getSQLStatements(dataSource));
        addQuintusEnumerationStatements(statements, this.currentColumn);
        return statements;
      }
    }
    
    return super.getSQLStatements(dataSource);
  }
  
  protected static void addQuintusEnumerationStatements(List statements, ISchemaColumnDefinition column)
  {
    statements.add("DELETE FROM qw_fieldinfo WHERE table_name='"+column.getDBTableName()+"' AND column_name='"+column.getDBName()+"'");
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("DROP COLUMN ").append(this.currentColumn.getDBName());
    return buffer.toString();
  }
  
}
