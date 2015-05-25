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
 * Created on 14.01.2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data.impl.sql.reconfigure;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.schema.ISchemaRelationDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DropRelationCommand extends Command
{
  protected final ISchemaRelationDefinition currentRelation;
  
  /**
   * @param currentRelation
   */
  public DropRelationCommand(ISchemaRelationDefinition currentRelation)
  {
    this.currentRelation = currentRelation;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.schema.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public List getSQLStatements(SQLDataSource dataSource)
  {
    List statements = new ArrayList();

    // Build statement as follows:
    //
    // ALTER TABLE organization DROP CONSTRAINT fk41_organization_ownerorg 
    //
    StringBuffer buffer = new StringBuffer();
    buffer.append("ALTER TABLE ");
    dataSource.appendDBName(buffer, this.currentRelation.getSchemaTableName());
    buffer.append(" DROP CONSTRAINT ");
    dataSource.appendDBName(buffer, this.currentRelation.getSchemaForeignKeyName());
    statements.add(buffer.toString());

    return statements;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("DROP FOREIGN KEY RELATION ").append(this.currentRelation.getSchemaForeignKeyName());
    return buffer.toString();
  }
}
