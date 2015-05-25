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
import java.util.List;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.schema.ISchemaTableDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DropTableCommand extends Command
{
  protected final ISchemaTableDefinition tableDefinition;

  /**
   * @param tableDefinition
   */
  public DropTableCommand(ISchemaTableDefinition tableDefinition)
  {
    this.tableDefinition = tableDefinition;
  }

  /**
   * This method is currently overwritten by Oracle only.
   * 
   * @param dataSource the data source
   */
  protected String appendToSQL92DropStatement(SQLDataSource dataSource)
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
    buffer.append("DROP TABLE ");
    dataSource.appendDBName(buffer, this.tableDefinition.getDBName()); // + " CASCADE CONSTRAINTS");

    // just in case some options should be added
    buffer.append(appendToSQL92DropStatement(dataSource));
    
    statements.add(buffer.toString());
    
    // -------------------------------------------------------------------------------------
    // Attention: If this method is overwritten (by removing final) the following statements
    // have to be regarded!
    // -------------------------------------------------------------------------------------
    
    // just for Quintus!
    if (dataSource.isQuintusAdjustment())
    {
      statements.add("DELETE FROM qw_keys WHERE tablename='"+tableDefinition.getDBName()+"'");
    }
    
    return statements;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("DROP TABLE ").append(this.tableDefinition.getDBName());
    return buffer.toString();
  }

}
