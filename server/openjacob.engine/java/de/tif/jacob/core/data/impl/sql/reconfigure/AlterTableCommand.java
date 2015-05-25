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
public class AlterTableCommand extends Command
{
  protected final ISchemaTableDefinition tableDefinition;
  protected final List alterTableCommands;
  
  public AlterTableCommand(ISchemaTableDefinition tableDefinition, List alterTableCommands)
  {
    this.tableDefinition = tableDefinition;
    this.alterTableCommands = alterTableCommands;
  }
  
  protected boolean useBrackets()
  {
    return false;
  }
  
  protected boolean doCascadeConstraints()
  {
    return false;
  }
  
  protected void setTable(AlterTableSubCommand command)
  {
    // IBIS: unschön - warum nicht gleich setzen?
    command.table = this.tableDefinition;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.schema.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public List getSQLStatements(SQLDataSource dataSource)
  {
    List statements = new ArrayList();

    // scan alter table subcommands
    StringBuffer addFragments = null;
    StringBuffer modifyFragments = null;
    StringBuffer dropFragments = null;
    for (int i=0; i < this.alterTableCommands.size(); i++)
    {
      AlterTableSubCommand command = (AlterTableSubCommand) this.alterTableCommands.get(i);
      
      setTable(command);
      
      // check for sub statements
      statements.addAll(command.getSQLStatements(dataSource));
      
      // check for alter add fragments
      String addFragment = command.getAlterTableAddFragment(dataSource);
      if (addFragment != null)
      {
        if (addFragments == null)
        {
          addFragments = new StringBuffer();
        }
        else
        {
          addFragments.append(",\r\n  ");
        }
        addFragments.append(addFragment);
      }
      
      // check for alter modify fragments
      String modifyFragment = command.getAlterTableModifyFragment(dataSource);
      if (modifyFragment != null)
      {
        if (modifyFragments == null)
        {
          modifyFragments = new StringBuffer();
        }
        else
        {
          modifyFragments.append(",\r\n  ");
        }
        modifyFragments.append(modifyFragment);
      }
      
      // check for alter drop fragments
      String dropFragment = command.getAlterTableDropFragment(dataSource);
      if (dropFragment != null)
      {
        if (dropFragments == null)
        {
          dropFragments = new StringBuffer();
        }
        else
        {
          dropFragments.append(",\r\n  ");
        }
        dropFragments.append(dropFragment);
      }
    }
    
    if (modifyFragments != null)
    {
      StringBuffer alterTableBuffer = new StringBuffer();
      alterTableBuffer.append("ALTER TABLE ");
      dataSource.appendDBName(alterTableBuffer, this.tableDefinition.getDBName());
      alterTableBuffer.append(useBrackets() ? " ALTER(" : " ALTER ").append(modifyFragments.toString());
      alterTableBuffer.append(useBrackets() ? ")" : "");
      statements.add(0, alterTableBuffer.toString());
    }
    if (addFragments != null)
    {
      StringBuffer alterTableBuffer = new StringBuffer();
      alterTableBuffer.append("ALTER TABLE ");
      dataSource.appendDBName(alterTableBuffer, this.tableDefinition.getDBName());
      alterTableBuffer.append(useBrackets() ? " ADD(" : " ADD ").append(addFragments.toString());
      alterTableBuffer.append(useBrackets() ? ")" : "");
      statements.add(0, alterTableBuffer.toString());
    }
    if (dropFragments != null)
    {
      StringBuffer alterTableBuffer = new StringBuffer();
      alterTableBuffer.append("ALTER TABLE ");
      dataSource.appendDBName(alterTableBuffer, this.tableDefinition.getDBName());
      alterTableBuffer.append(useBrackets() ? " DROP(" : " DROP ").append(dropFragments.toString());
      alterTableBuffer.append(useBrackets() ? ")" : "");
      if (doCascadeConstraints())
        alterTableBuffer.append(" CASCADE CONSTRAINTS");
      statements.add(alterTableBuffer.toString());
    }
    
    return statements;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("ALTER TABLE ").append(this.tableDefinition.getDBName()).append(":");
    for (int i=0; i < this.alterTableCommands.size(); i++)
    {
      buffer.append("\r\n\t");
      buffer.append(this.alterTableCommands.get(i).toString());
    }
    return buffer.toString();
  }
  
}
