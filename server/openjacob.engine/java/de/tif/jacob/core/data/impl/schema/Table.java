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

package de.tif.jacob.core.data.impl.schema;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.tif.jacob.core.schema.ISchemaKeyDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class Table extends SchemaObject implements ISchemaTableDefinition
{
  public static final String QW_KEYVALUE_PROPERTY = "QW_KEYVALUE";
  
  // table types
  public static final int USER_TABLE = 0;
  public static final int SYSTEM_TABLE = 1;
  public static final int VIEW = 2;
  public static final int OTHER_TYPE = -1;
  
  private final String name;
  private int type;
  private Key primaryKey;
  
  /**
   * Map(indexName -> Key)
   */
  private final Map indices = new HashMap();
  
  /**
   * Map(fk_name -> Relation)
   */
  private final Map relations = new HashMap();
  
  /**
   * Map(columnname.toLowerCase() -> TableColumn)
   */
  private final Map columns = new HashMap();
  
  public Table(String name, int type)
  {
    if (null == name)
      throw new NullPointerException();
    this.name = name;
    this.type = type;
  }
  
  public void add(TableColumn column)
  {
    Object old = this.columns.put(column.getDBName().toLowerCase(), column);
    
    // plausibility check
    if (old != null)
      throw new RuntimeException("Column '" + column.getDBName() + "' already exists for table '" + name + "'");
  }

  public TableColumn getTableColumn(String name)
  {
    TableColumn result = (TableColumn) this.columns.get(name.toLowerCase());
    if (result == null)
    {
      throw new RuntimeException("Could not find column '" + name + "' of table '" + this.name + "'");
    }
    return result;
  }
  
  public boolean hasTableColumn(String name)
  {
    return this.columns.containsKey(name.toLowerCase());
  }
  
  /**
   * @return Returns the name.
   */
  public String getDBName()
  {
    return name;
  }

  /**
   * @return Returns the type.
   */
  public int getType()
  {
    return type;
  }

  /**
   * @param type The type to set.
   */
  public void setType(int type)
  {
    this.type = type;
  }
  
  /**
   * @param indexName
   * @param unique
   * @param ordinalPosition
   * @param columnName
   */
  public void addIndexPart(String indexName, boolean unique, short ordinalPosition, String columnName)
  {
    // plausibilty check
    if (null == indexName)
      throw new NullPointerException();
    if (null == columnName)
      throw new NullPointerException();
    if (ordinalPosition < 1)
      throw new IllegalArgumentException();
    
    Key index = (Key) this.indices.get(indexName);
    if (index == null)
    {
      index = new Key(indexName, unique);
      this.indices.put(indexName, index);
    }
    else
    {
      // plausibilty check
      if (index.isUnique() ^ unique)
        throw new IllegalStateException("unique attribute has changed");
    }
    
    index.addColumnName(columnName, ordinalPosition);
  }
  
  /**
   * @param indexName
   */
  public void dropIndex(String indexName)
  {
    this.indices.remove(indexName);
  }

  /**
   * @param pk_name
   * @param ordinalPosition
   * @param columnName
   */
  public void addPrimaryKeyPart(String pk_name, short ordinalPosition, String columnName)
  {
    // plausibilty check
    if (null == pk_name)
      throw new NullPointerException();
    if (null == columnName)
      throw new NullPointerException();
    if (ordinalPosition < 1)
      throw new IllegalArgumentException();
    
    if (this.primaryKey == null)
    {
      this.primaryKey = new Key(pk_name, true);
    }
    else
    {
      // plausibility check
      if (!this.primaryKey.getDBName().equals(pk_name))
        throw new RuntimeException("Table '" + this.name + "': A table might have one primary key only");
    }
    
    this.primaryKey.addColumnName(columnName, ordinalPosition);
  }

  /**
   * @param fk_name
   * @param ordinalPosition
   * @param fkcolumn_name
   * @param pk_name
   * @param pktable_name
   * @param pkcolumn_name
   */
  public void addForeignKeyPart(String fk_name, short ordinalPosition, String fkcolumn_name, String pk_name, String pktable_name, String pkcolumn_name)
  {
    // plausibilty check
    if (null == fk_name)
      throw new NullPointerException();
    if (null == fkcolumn_name)
      throw new NullPointerException();
    if (ordinalPosition < 1)
      throw new IllegalArgumentException();
    // MySQL does not (always) deliver a primary key name, e.g. for autocreated foreign keys 
//    if (null == pk_name)
//      throw new NullPointerException();
    if (null == pktable_name)
      throw new NullPointerException();
    if (null == pkcolumn_name)
      throw new NullPointerException();
    
    Relation relation = (Relation) this.relations.get(fk_name);
    if (relation == null)
    {
      relation = new Relation(fk_name, this.name, pktable_name);
      this.relations.put(fk_name, relation);
    }
    else
    {
      // plausibilty check
      if (!relation.getSchemaPrimaryTableName().equals(pktable_name))
        throw new IllegalStateException("Table '" + this.name + "': Primary key table name has changed: '" + relation.getSchemaPrimaryTableName() + "' <> '" + pktable_name + "'");
    }
    
    relation.addColumnName(fkcolumn_name, ordinalPosition);
  }
  
  
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaTableDefinition#getSchemaColumnDefinitions()
   */
  public Iterator getSchemaColumnDefinitions()
  {
    return this.columns.values().iterator();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaTableDefinition#getSchemaPrimaryKeyDefinition()
   */
  public ISchemaKeyDefinition getSchemaPrimaryKeyDefinition()
  {
    return this.primaryKey;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaTableDefinition#getSchemaIndexDefinitions()
   */
  public Iterator getSchemaIndexDefinitions()
  {
    return this.indices.values().iterator();
  }
  
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaTableDefinition#getSchemaRelationDefinitions()
   */
  public Iterator getSchemaRelationDefinitions()
  {
    return this.relations.values().iterator();
  }
}
