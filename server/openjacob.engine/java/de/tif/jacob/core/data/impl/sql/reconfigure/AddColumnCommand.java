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
public class AddColumnCommand extends AlterTableSubCommand
{
  protected final ISchemaColumnDefinition desiredColumn;
  
  /**
   * @param desiredColumn
   */
  public AddColumnCommand(ISchemaColumnDefinition desiredColumn)
  {
    this.desiredColumn = desiredColumn;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.schema.Command#getAlterTableAddFragment(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public String getAlterTableAddFragment(SQLDataSource dataSource)
  {
    StringBuffer buffer = new StringBuffer();
    String dataType = dataSource.getSqlColumnType(
        this.desiredColumn.getSQLType(dataSource), 
        this.desiredColumn.getSQLSize(dataSource), 
        this.desiredColumn.getSQLDecimalDigits(dataSource));
    dataSource.appendDBName(buffer, this.desiredColumn.getDBName()).append(" ").append(dataType);
    
    // For Oracle the default value must be specified before NULL/NOT NULL!
    if (this.desiredColumn.getDBDefaultValue(dataSource)!=null)
      buffer.append(" DEFAULT ").append(escapeDefaultValue(dataSource, this.desiredColumn.getSQLType(dataSource), this.desiredColumn.getDBDefaultValue(dataSource)));
    buffer.append(this.desiredColumn.isRequired() ? " NOT NULL" : " NULL");
    
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
      if (this.desiredColumn.isEnumeration())
      {
        List statements = new ArrayList();
        statements.addAll(super.getSQLStatements(dataSource));
        addQuintusEnumerationStatements(dataSource, statements, this.desiredColumn);
        return statements;
      }
    }
    
    return super.getSQLStatements(dataSource);
  }
  
  protected static void addQuintusEnumerationStatements(SQLDataSource datasource, List statements, ISchemaColumnDefinition enumColumn)
  {
    // be sure: drop all entries first
    DropColumnCommand.addQuintusEnumerationStatements(statements, enumColumn);
    
    List enumLabels = enumColumn.getEnumerationLabels();
    for (int i = 0; i < enumLabels.size(); i++)
    {
      statements.add("INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('" + enumColumn.getDBTableName() + "','"
          + enumColumn.getDBName() + "',14," + i + ",'" + datasource.convertToSQL((String) enumLabels.get(i), false) + "')");
    }
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("ADD COLUMN ").append(this.desiredColumn.getDBName());
    buffer.append(" TYPE ").append(this.desiredColumn.getSQLType(SQLDataSource.TEST_INSTANCE));
    if (this.desiredColumn.getDBDefaultValue(SQLDataSource.TEST_INSTANCE) != null)
      buffer.append(" DEFAULT ").append(this.desiredColumn.getDBDefaultValue(SQLDataSource.TEST_INSTANCE));
    buffer.append(this.desiredColumn.isRequired() ? " NOT NULL" : " NULL");
    return buffer.toString();
  }
  
}
