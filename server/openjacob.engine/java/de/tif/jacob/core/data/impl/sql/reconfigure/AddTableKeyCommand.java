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

package de.tif.jacob.core.data.impl.sql.reconfigure;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.schema.ISchemaTableDefinition;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * @author Andreas Sonntag
 */
public final class AddTableKeyCommand extends AlterTableSubCommand
{
  private final ISchemaTableDefinition desiredTableDefinition;
  
  /**
   * @param desiredTableDefinition
   * @param statements
   */
  protected AddTableKeyCommand(ISchemaTableDefinition desiredTableDefinition)
  {
    this.desiredTableDefinition = desiredTableDefinition;
  }

  protected static void addQuintusKeyStatement(List statements, ISchemaTableDefinition tableDefinition)
  {
    statements.add("INSERT INTO qw_keys (tablename,keyvalue) VALUES ('"+tableDefinition.getDBName()+"',0)");
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.reconfigure.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public List getSQLStatements(SQLDataSource dataSource)
  {
    List statements = new ArrayList();
    AddTableKeyCommand.addQuintusKeyStatement(statements, desiredTableDefinition);
    return statements;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("ADD KEY TO QW_KEYS OF TABLE ").append(this.desiredTableDefinition.getDBName());
    return buffer.toString();
  }
}
