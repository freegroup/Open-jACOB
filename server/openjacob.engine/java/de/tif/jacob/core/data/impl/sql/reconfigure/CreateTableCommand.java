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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.schema.ISchemaColumnDefinition;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CreateTableCommand extends Command
{
  protected final ISchemaTableDefinition tableDefinition;

  public CreateTableCommand(ISchemaTableDefinition tableDefinition)
  {
    this.tableDefinition = tableDefinition;
  }
  
  /**
   * This method is currently overwritten by MySQL only.
   * 
   * @param dataSource the data source
   */
  protected String appendToSQL92CreateStatement(SQLDataSource dataSource)
  {
    // by default do nothing
    return "";
  }
  
  /**
   * This method is currently overwritten by MySQL only.
   * 
   * @param dataSource the data source
   */
  protected String appendToSQL92CreateColumnStatement(SQLDataSource dataSource, ISchemaColumnDefinition column)
  {
    // by default do nothing
    return "";
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.schema.Command#getSQLStatements()
   */
  public final List getSQLStatements(SQLDataSource dataSource)
  {
    List statements = new ArrayList();

    StringBuffer buffer = new StringBuffer();
    buffer.append("CREATE TABLE ");
    dataSource.appendDBName(buffer, this.tableDefinition.getDBName()).append(" (");

    // handle columns
    Iterator columnIter = this.tableDefinition.getSchemaColumnDefinitions();
    for (int i = 0; columnIter.hasNext(); i++)
    {
      if (i != 0)
        buffer.append(",");
      ISchemaColumnDefinition column = (ISchemaColumnDefinition) columnIter.next();
      buffer.append("\r\n  ");
      dataSource.appendDBName(buffer, column.getDBName()).append(" ").append(dataSource.getSqlColumnType(
          column.getSQLType(dataSource), 
          column.getSQLSize(dataSource), 
          column.getSQLDecimalDigits(dataSource)));
      
      // For Oracle the default value must be specified before NULL/NOT NULL!
      if (column.getDBDefaultValue(dataSource)!=null)
        buffer.append(" DEFAULT ").append(escapeDefaultValue(dataSource, column.getSQLType(dataSource), column.getDBDefaultValue(dataSource)));
      
      buffer.append(column.isRequired() ? " NOT NULL" : " NULL");
      
      // just in case some options should be added
      buffer.append(appendToSQL92CreateColumnStatement(dataSource, column));
    }

    // handle primary key
    ISchemaKeyDefinition primaryKey = this.tableDefinition.getSchemaPrimaryKeyDefinition();
    if (primaryKey != null)
    {
      buffer.append(",\r\n  ");
      buffer.append("CONSTRAINT ");
      dataSource.appendDBName(buffer, primaryKey.getDBName()).append(" PRIMARY KEY (");
      Iterator columnNameIter = primaryKey.getSchemaColumnNames();
      for (int i = 0; columnNameIter.hasNext(); i++)
      {
        if (i != 0)
          buffer.append(",");
        dataSource.appendDBName(buffer, (String) columnNameIter.next());
      }
      buffer.append(")");
    }

    // handle indices
    Iterator indexIter = this.tableDefinition.getSchemaIndexDefinitions();
    for (int i = 0; indexIter.hasNext(); i++)
    {
      ISchemaKeyDefinition index = (ISchemaKeyDefinition) indexIter.next();
      if (index.isUnique() && (index.hasRequiredColumns() || dataSource.supportsMultipleNullsForUniqueIndices()))
      {
        buffer.append(",\r\n  ").append(createUniqueConstraintFragment(dataSource, index));
      }
      else
      {
        if (index.isUnique())
          statements.add(new ReconfigureComment("Can not create index " + index.getDBName() + " as unique because multiple null values are not supported"));
          
        statements.add(createIndexStatement(dataSource, this.tableDefinition, index));
      }
    }

    buffer.append(")");
    
    // just in case some options should be added
    buffer.append(appendToSQL92CreateStatement(dataSource));
    
    // add statement at first position!
    statements.add(0, buffer.toString());
    
    // -------------------------------------------------------------------------------------
    // Attention: If this method is overwritten (by removing final) the following statements
    // have to be regarded!
    // -------------------------------------------------------------------------------------
    
    // just for Quintus!
    if (dataSource.isQuintusAdjustment())
    {
      // add statement at first position!
      statements.add(0, "DELETE FROM qw_fieldinfo WHERE table_name='"+this.tableDefinition.getDBName()+"'");
      
      // add enum labels to qw_fieldinfo
      columnIter = this.tableDefinition.getSchemaColumnDefinitions();
      while (columnIter.hasNext())
      {
        ISchemaColumnDefinition column = (ISchemaColumnDefinition) columnIter.next();
        if (column.isEnumeration())
        {
          AddColumnCommand.addQuintusEnumerationStatements(dataSource, statements, column);
        }
      }
      
      AddTableKeyCommand.addQuintusKeyStatement(statements, this.tableDefinition);
    }
    
    addResetPrivilegesStatements(dataSource, statements);
    
    return statements;
  }
  
  protected void addResetPrivilegesStatements(SQLDataSource dataSource, List statements)
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("REVOKE ALL ON ");
    dataSource.appendDBName(buffer, this.tableDefinition.getDBName());
    buffer.append(" FROM PUBLIC");
    statements.add(buffer.toString());
    
    buffer = new StringBuffer();
    buffer.append("GRANT SELECT, UPDATE, INSERT, DELETE ON ");
    dataSource.appendDBName(buffer, this.tableDefinition.getDBName());
    buffer.append(" TO PUBLIC");
    statements.add(buffer.toString());
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("CREATE TABLE ").append(this.tableDefinition.getDBName());
    return buffer.toString();
  }
}
