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
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.schema.ISchemaRelationDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CreateRelationCommand extends Command
{
  protected final ISchemaRelationDefinition desiredRelation;
  protected final ISchemaTableDefinition primaryTable;
  
  /**
   * @param desiredRelation
   */
  public CreateRelationCommand(ISchemaRelationDefinition desiredRelation, ISchemaTableDefinition primaryTable)
  {
    if (desiredRelation == null)
      throw new NullPointerException("desiredRelation");
    if (primaryTable == null)
      throw new NullPointerException("primaryTable");
    this.desiredRelation = desiredRelation;
    this.primaryTable = primaryTable; 
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.schema.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public List getSQLStatements(SQLDataSource dataSource)
  {
    List statements = new ArrayList();

    // Build statement as follows:
    //
    // ALTER TABLE organization ADD CONSTRAINT fk41_organization_ownerorg 
    //    FOREIGN KEY (ownerorg) REFERENCES employee (pkey)
    //
    StringBuffer buffer = new StringBuffer();
    buffer.append("ALTER TABLE ");
    dataSource.appendDBName(buffer, this.desiredRelation.getSchemaTableName());
    buffer.append(" ADD CONSTRAINT ");
    dataSource.appendDBName(buffer, this.desiredRelation.getSchemaForeignKeyName());
    buffer.append(" FOREIGN KEY (");
    Iterator foreignColumnNameIter = this.desiredRelation.getSchemaForeignColumnNames();
    for (int j = 0; foreignColumnNameIter.hasNext(); j++)
    {
      if (j != 0)
        buffer.append(",");
      dataSource.appendDBName(buffer, (String) foreignColumnNameIter.next());
    }
    buffer.append(") REFERENCES ");
    dataSource.appendDBName(buffer, this.desiredRelation.getSchemaPrimaryTableName());
    buffer.append(" (");
    Iterator primaryColumnNameIter = this.primaryTable.getSchemaPrimaryKeyDefinition().getSchemaColumnNames();
    for (int j = 0; primaryColumnNameIter.hasNext(); j++)
    {
      if (j != 0)
        buffer.append(",");
      dataSource.appendDBName(buffer, (String) primaryColumnNameIter.next());
    }
    buffer.append(")");
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
    buffer.append("CREATE FOREIGN KEY RELATION ").append(this.desiredRelation.getSchemaForeignKeyName());
    return buffer.toString();
  }
}
