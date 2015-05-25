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
package de.tif.jacob.core.data.impl.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.schema.ISchemaRelationDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Relation extends SchemaObject implements ISchemaRelationDefinition
{
  private final String fk_name;
  
  // the name of the table containing the foreign key
  private final String fktable_name;
  
  // the name of the table this relation points to
  private final String pktable_name;
  
  private final List columns = new ArrayList();

  /**
   * @param fk_name
   * @param pktable_name
   */
  public Relation(String fk_name, String fktable_name, String pktable_name)
  {
    this.fk_name = fk_name;
    this.fktable_name = fktable_name;
    this.pktable_name = pktable_name;
  }

  /**
   * @param fkcolumn_name
   * @param ordinalPosition
   */
  public void addColumnName(String fkcolumn_name, short ordinalPosition)
  {
    while (this.columns.size() < ordinalPosition)
    {
      this.columns.add(null);
    }

    // decrement since ordinal position starts with 1..
    ordinalPosition--;

    this.columns.set(ordinalPosition, fkcolumn_name);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaRelationDefinition#getSchemaForeignColumnNames()
   */
  public Iterator getSchemaForeignColumnNames()
  {
    return this.columns.iterator();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaRelationDefinition#getSchemaForeignKeyName()
   */
  public String getSchemaForeignKeyName()
  {
    return this.fk_name;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaRelationDefinition#getSchemaPrimaryTableName()
   */
  public String getSchemaPrimaryTableName()
  {
    return this.pktable_name;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaRelationDefinition#getSchemaTableName()
   */
  public String getSchemaTableName()
  {
    return this.fktable_name;
  }
}
