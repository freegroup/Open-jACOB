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
 * Created on 12.01.2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data.impl.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.schema.ISchemaKeyDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class Key extends SchemaObject implements ISchemaKeyDefinition
{
  private final String name;
  private final boolean unique;
  private final List columns = new ArrayList();
  
  public Key(String name, boolean unique)
  {
    this.name = name;
    this.unique = unique;
  }
  
  public void addColumnName(String columnName, short ordinalPosition)
  {
    while (this.columns.size() < ordinalPosition)
    {
      this.columns.add(null);
    }

    // decrement since ordinal position starts with 1..
    ordinalPosition--;

    this.columns.set(ordinalPosition, columnName);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaKeyDefinition#getDBName()
   */
  public String getDBName()
  {
    return this.name;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaKeyDefinition#isUnique()
   */
  public boolean isUnique()
  {
    return this.unique;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaKeyDefinition#hasRequiredColumns()
   */
  public boolean hasRequiredColumns()
  {
    // always return true to ensure unique indices for internal tables
    return true;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaKeyDefinition#getSchemaColumnNames()
   */
  public Iterator getSchemaColumnNames()
  {
    return this.columns.iterator();
  }
}
