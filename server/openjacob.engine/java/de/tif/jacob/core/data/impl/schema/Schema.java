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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Schema extends SchemaObject implements ISchemaDefinition
{
  /**
   * Map(tablename.toLowerCase() -> Table)
   */
  private Map tableAndViewMap = new HashMap();
  
  /**
   * Set(procname.toLowerCase())
   */
  private Set procedureNameSet = new HashSet();

  public void add(Table table)
  {
    this.tableAndViewMap.put(table.getDBName().toLowerCase(), table);
  }

  public void addType(int sqlType, String localType)
  {

  }

  /**
   * @param proc_name
   */
  public void addProcedure(String proc_name)
  {
    this.procedureNameSet.add(proc_name.toLowerCase());
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaDefinition#hasProcedure(java.lang.String)
   */
  public boolean hasProcedure(String procName)
  {
    return this.procedureNameSet.contains(procName.toLowerCase());
  }
  
  public boolean hasTable(String name)
  {
    return this.tableAndViewMap.containsKey(name.toLowerCase());
  }
  
  public Table getTable(String name)
  {
    Table result = (Table) this.tableAndViewMap.get(name.toLowerCase());
    if (result == null)
    {
      if ("dtproperties".equalsIgnoreCase(name) || name.toLowerCase().startsWith("sys"))
      {
        // Hack für MS SQL Server: Beim Holen der Tabellen wird dtproperies nicht
        // zurückgeliefert, wenn der Benutzer nicht der system Benutzer ist.
        result = new Table(name, Table.SYSTEM_TABLE);
        this.tableAndViewMap.put(name.toLowerCase(), result);
      }
      else
        throw new RuntimeException("Could not find table "+name+".");
    }
    return result;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaDefinition#getSchemaTableDefinitions()
   */
  public Iterator getSchemaTableDefinitions()
  {
    return new TableIterator(true);
  }

  public Iterator getTableAndViews()
  {
    return this.tableAndViewMap.values().iterator();
  }

  public Iterator getTables()
  {
    return new TableIterator(false);
  }

  private class TableIterator implements Iterator
  {
    private Iterator tableDefIter;
    private ISchemaTableDefinition next;
    private final boolean userTablesOnly;

    private TableIterator(boolean userTablesOnly)
    {
      this.userTablesOnly = userTablesOnly;
      this.tableDefIter = Schema.this.tableAndViewMap.values().iterator();
      iterate();
    }

    private void iterate()
    {
      while (this.tableDefIter.hasNext())
      {
        Table tableDef = (Table) this.tableDefIter.next();

        if (tableDef.getType() == Table.USER_TABLE || (tableDef.getType() == Table.SYSTEM_TABLE && !this.userTablesOnly))
        {
          this.next = tableDef;
          return;
        }
      }

      this.next = null;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext()
    {
      return this.next != null;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    public Object next()
    {
      Object result = this.next;
      iterate();
      return result;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}
