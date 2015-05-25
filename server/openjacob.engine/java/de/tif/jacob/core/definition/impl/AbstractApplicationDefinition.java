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

package de.tif.jacob.core.definition.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.core.definition.IAdhocBrowserDefinition;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IDomainDefinition;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.IManyToManyRelation;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.IRelation;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.IToolbarDefinition;
import de.tif.jacob.core.definition.guielements.ToolbarDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceReconfigureType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceTypeType;
import de.tif.jacob.core.misc.AdhocBrowserDefinition;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;
import de.tif.jacob.core.schema.ISchemaRelationDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to Window>Preferences>Java>Code Generation>Code and
 * Comments
 */
public abstract class AbstractApplicationDefinition extends AbstractElement implements IApplicationDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractApplicationDefinition.java,v 1.5 2010/07/13 17:55:22 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.5 $";

  private final AbstractDefinition definition;
  private final Version version;
  private final AbstractApplicationInfo applicationInfo;
  private final List domainDefinitions;
  private final Map domainDefinitionMap;
  private final ToolbarDefinition toolbar;

  // sorted lists
  private final List sortedDatasourceNames;
  private final List sortedTableDefinitions;
  private final List sortedTableAliases;
  private final List sortedBrowserDefinitions;
  private final List sortedRelationSets;
  private final List sortedRelations;
  
  private final List datasourceIndexNames;

  protected AbstractApplicationDefinition(AbstractDefinition definition, String name, Version version)
  {
    super(name, definition.getApplicationInfo(name).getDescription());

    if (version == null)
      throw new NullPointerException();

    this.version = version;
    this.definition = definition;
    this.applicationInfo = definition.getApplicationInfo(name);
    this.domainDefinitionMap = new HashMap();

    // IBIS: Über JAD konfigurierbar machen bzw. fest-kodiert für QES
    this.toolbar = new ToolbarDefinition();

    {
      List list = new ArrayList();
      Iterator iter = this.applicationInfo.getDomainNames();
      while (iter.hasNext())
      {
        AbstractDomainDefinition domain = definition.getAbstractDomainDefinition((String) iter.next());
        list.add(domain);
        this.domainDefinitionMap.put(domain.getName(), domain);
      }
      this.domainDefinitions = Collections.unmodifiableList(list);
    }

    // sort table aliases
    this.sortedDatasourceNames = sort(this.definition.getDatasourceNames());
    this.sortedTableDefinitions = sort(this.definition.getTables());
    this.sortedTableAliases = sort(this.definition.getTableAliases());
    this.sortedBrowserDefinitions = sort(this.definition.getBrowsers());
    this.sortedRelations = sort(this.definition.getRelations());
    this.sortedRelationSets = sort(this.definition.getRelationSets());
    
    // collect indices
    {
      List indices = new ArrayList();
      for (int i = 0; i < this.sortedDatasourceNames.size(); i++)
      {
        String datasourceName = (String) this.sortedDatasourceNames.get(i);
        if (CastorDataSourceTypeType.INDEX.equals(getDataSourceType(datasourceName)))
          indices.add(datasourceName);
      }
      this.datasourceIndexNames = Collections.unmodifiableList(indices);
    }
  }

  /**
   * @param iter
   * @return
   */
  private static List sort(Iterator iter)
  {
    List list = new ArrayList();
    while (iter.hasNext())
    {
      list.add(iter.next());
    }
    Collections.sort(list);
    return Collections.unmodifiableList(list);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getTitle()
   */
  public final String getTitle()
  {
    return this.applicationInfo.getTitle();
  }

  /**
   * Returns the class name of the event handler of this application.
   * 
   * @return event handler class name or <code>null</code> if default method
   *         to determine the event handler should be used.
   */
  public final String getEventHandler()
  {
    return this.applicationInfo.getEventHandler();
  }
  
  /**
   * Checks whether event handlers should be looked up by reference.
   * 
   * @return <code>true</true> event handlers should be determined by reference, 
   *       if <code>false</code> event handlers are determined by name convention.
   */
  public final boolean lookupEventHandlerByReference()
  {
    return this.applicationInfo.lookupEventHandlerByReference();
  }

  public final DataScope getDataScope()
  {
    return this.applicationInfo.getDataScope();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getVersion()
   */
  public final Version getVersion()
  {
    return this.version;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getDomainDefinition(java.lang.String)
   */
  public final IDomainDefinition getDomainDefinition(String domainName)
  {
    return (IDomainDefinition) this.domainDefinitionMap.get(domainName);
  }

  public final List getDomainDefinitions()
  {
    return this.domainDefinitions;
  }

  public final IAdhocBrowserDefinition createAdhocBrowserDefinition(IBrowserDefinition browserDefinition)
  {
    return new AdhocBrowserDefinition(this, browserDefinition);
  }
  
  public final IAdhocBrowserDefinition createAdhocBrowserDefinition(ITableAlias alias)
  {
    return new AdhocBrowserDefinition(this, alias.getName());
  }
  
  public final IAdhocBrowserDefinition createAdhocBrowserDefinition(String aliasName)
  {
    return new AdhocBrowserDefinition(this, aliasName);
  }

  public final IBrowserDefinition getBrowserDefinition(String name)
  {
    return this.definition.getBrowserDefinition(name);
  }

  public final ITableDefinition getTableDefinition(String name)
  {
    return this.definition.getTableDefinition(name);
  }

  public final ITableDefinition getTableDefinition(String datasourceName, String dbname) throws RuntimeException
  {
    return this.definition.getTableDefinition(datasourceName, dbname);
  }

  public final IRelation getRelation(String name)
  {
    return this.definition.getRelation(name);
  }

  public final ITableAlias getTableAlias(String name)
  {
    return this.definition.getTableAlias(name);
  }

  public final IRelationSet getDefaultRelationSet()
  {
    return this.definition.getDefaultRelationSet();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getLocalRelationSet()
   */
  public final IRelationSet getLocalRelationSet()
  {
    return this.definition.getLocalRelationSet();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getRelationSet(java.lang.String)
   */
  public final IRelationSet getRelationSet(String name)
  {
    return this.definition.getRelationSet(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getToolbarDefinition()
   */
  public IToolbarDefinition getToolbarDefinition()
  {
    return this.toolbar;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition,
   *      de.tif.jacob.core.definition.impl.AbstractElement)
   */
  public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    // not needed so far
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getTableAliases()
   */
  public final List getTableAliases()
  {
    return this.sortedTableAliases;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getBrowserDefinitions()
   */
  public List getBrowserDefinitions()
  {
    return this.sortedBrowserDefinitions;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getRelations()
   */
  public List getRelations()
  {
    return this.sortedRelations;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getRelationSets()
   */
  public final List getRelationSets()
  {
    return this.sortedRelationSets;
  }

  public final List getDataSourceNames()
  {
    return this.sortedDatasourceNames;
  }

  public final List getDataSourceIndexNames()
  {
    return this.datasourceIndexNames;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getTableDefinitions()
   */
  public final List getTableDefinitions()
  {
    return this.sortedTableDefinitions;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public final String toString()
  {
    return getName() + "-" + getVersion();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IApplicationDefinition#getSchemaDefinition(java.lang.String)
   */
  public ISchemaDefinition getSchemaDefinition(String datasourceName)
  {
    // check for any tables of the given datasource
    for (int i = 0; i < this.sortedTableDefinitions.size(); i++)
    {
      if (datasourceName.equals(((ITableDefinition) this.sortedTableDefinitions.get(i)).getDataSourceName()))
      {
        return new SchemaDef(datasourceName);
      }
    }

    // application is not based on the given datasource
    return null;
  }
  
  public final CastorDataSourceReconfigureType getReconfigureMode(String datasourceName)
  {
    return this.definition.getReconfigureMode(datasourceName);
  }

  /**
   * @param datasourceName
   * @return
   * @since 2.10
   */
  public final CastorDataSourceTypeType getDataSourceType(String datasourceName)
  {
    return this.definition.getDataSourceType(datasourceName);
  }

  private class SchemaDef implements ISchemaDefinition
  {
    private final String datasourceName;
    
    // Map{tablename->SchemaTableDefinition}
    private final Map tableMap = new HashMap();

    private SchemaDef(String datasourceName)
    {
      this.datasourceName = datasourceName;

      //
      // collect all table definitions of the given datasource
      Iterator tableDefIter = AbstractApplicationDefinition.this.sortedTableDefinitions.iterator();
      while (tableDefIter.hasNext())
      {
        AbstractTableDefinition tableDef = (AbstractTableDefinition) tableDefIter.next();

        // table definition belongs to the desired datasource?
        if (tableDef.getDataSourceName().equals(this.datasourceName))
        {
          this.tableMap.put(tableDef.getName(), new STabDef(tableDef));
        }
      }

      //
      // loop for relations which contain a foreign key (toTable) and are defined for
      // the given datasource
      Iterator iter = AbstractApplicationDefinition.this.sortedRelations.iterator();
      while (iter.hasNext())
      {
        AbstractRelation relation = (AbstractRelation) iter.next();
        if (relation instanceof IOneToManyRelation)
        {
          add((IOneToManyRelation) relation);
        }
        else
        {
          IManyToManyRelation manyToManyRelation = (IManyToManyRelation) relation;
          add(manyToManyRelation.getFromRelation());
          add(manyToManyRelation.getToRelation());
        }
      }
    }
    
    private void add(IOneToManyRelation oneToManyRelation)
    {
      ITableDefinition toTable = oneToManyRelation.getToTableAlias().getTableDefinition();
      ITableDefinition fromTable = oneToManyRelation.getFromTableAlias().getTableDefinition();

      STabDef schemaTable = (STabDef) this.tableMap.get(toTable.getName());
      // IBIS: Offener Punkt: Wie soll mit Relationen, welche 2 Datenquellen überspannen umgegangen
      // werden? Anmerkung: Im Moment erst einmal ignorieren!
      if (null != schemaTable && this.tableMap.containsKey(fromTable.getName()))
      {
        schemaTable.addRelation(oneToManyRelation.getFromTableAlias().getTableDefinition(), oneToManyRelation.getToForeignKey());
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.schema.ISchemaDefinition#getSchemaTableDefinitions()
     */
    public Iterator getSchemaTableDefinitions()
    {
      return this.tableMap.values().iterator();
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.schema.ISchemaDefinition#hasProcedure(java.lang.String)
     */
    public boolean hasProcedure(String procName)
    {
      // always return false, because we do not need this here!
      return false;
    }
  }

  /**
   * Class to implement JAD schema table definitions
   * 
   * @author Andreas Sonntag
   */
  private static class STabDef implements ISchemaTableDefinition
  {
    private final AbstractTableDefinition tableDef;

    /**
     * Map{KeyComp->SRelDef}
     */
    private final Map relationMap = new HashMap();

    private STabDef(AbstractTableDefinition tableDef)
    {
      this.tableDef = tableDef;
    }

    /**
     * @param tableDefinition
     * @param toForeignKey
     */
    private void addRelation(ITableDefinition primaryTableDefinition, IKey foreignKey)
    {
      ForeignKeyHashKey key = new ForeignKeyHashKey(foreignKey);
      SRelDef relation = (SRelDef) this.relationMap.get(key);
      if (relation == null)
      {
        this.relationMap.put(key, new SRelDef(primaryTableDefinition, foreignKey));
      }
      else
      {
        // plausibility check
        if (!relation.getSchemaPrimaryTableName().equalsIgnoreCase(primaryTableDefinition.getDBName()))
        {
          throw new RuntimeException("Foreign key " + key + " points to different tables");
        }
      }
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.schema.ISchemaTableDefinition#getProperty(java.lang.String)
     */
    public Object getProperty(String name)
    {
      // at first always return null (might change if needed)
      return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.schema.ISchemaTableDefinition#getDBName()
     */
    public String getDBName()
    {
      return this.tableDef.getDBName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.schema.ISchemaTableDefinition#getSchemaColumnDefinitions()
     */
    public Iterator getSchemaColumnDefinitions()
    {
      return this.tableDef.getTableFields().iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.schema.ISchemaTableDefinition#getSchemaIndexDefinitions()
     */
    public Iterator getSchemaIndexDefinitions()
    {
      return new SIndexIter();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.schema.ISchemaTableDefinition#getSchemaPrimaryKeyDefinition()
     */
    public ISchemaKeyDefinition getSchemaPrimaryKeyDefinition()
    {
      return this.tableDef.getSchemaPrimaryKeyDefinition();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.schema.ISchemaTableDefinition#getSchemaRelationDefinitions()
     */
    public Iterator getSchemaRelationDefinitions()
    {
      return this.relationMap.values().iterator();
    }
    
    /**
     * This class is used for hash table keys to equalize different foreign
     * key definitions, which point to the exact save table field sequence.
     * 
     * @author Andreas Sonntag
     */
    private static class ForeignKeyHashKey
    {
      private final IKey key;
      private final int hash;
      
      private ForeignKeyHashKey(IKey key)
      {
        this.key = key;

        int h = key.getTableDefinition().hashCode();

        List thisFields = key.getTableFields();
        for (int i = 0; i < thisFields.size(); i++)
        {
          h = 3 * h + thisFields.get(i).hashCode();
        }
        this.hash = h;
      }

      /*
       * (non-Javadoc)
       * 
       * @see java.lang.Object#equals(java.lang.Object)
       */
      public boolean equals(Object anObject)
      {
        if (this == anObject)
        {
          return true;
        }
        if (anObject instanceof ForeignKeyHashKey)
        {
          ForeignKeyHashKey another = (ForeignKeyHashKey) anObject;
          
          // key is of the same table?
          if (this.key.getTableDefinition().equals(another.key.getTableDefinition()))
          {
            List thisFields = this.key.getTableFields();
            List otherFields = another.key.getTableFields();
            if (thisFields.size() == otherFields.size())
            {
              for (int i = 0; i < thisFields.size(); i++)
              {
                if (!thisFields.get(i).equals(otherFields.get(i)))
                {
                  return false;
                }
              }
              return true;
            }
          }
        }
        return false;
      }

      /* (non-Javadoc)
       * @see java.lang.Object#hashCode()
       */
      public int hashCode()
      {
        return this.hash;
      }

      /* (non-Javadoc)
       * @see java.lang.Object#toString()
       */
      public String toString()
      {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.key.getTableDefinition().getName());
        buffer.append("(");
        List thisFields = this.key.getTableFields();
        for (int i = 0; i < thisFields.size(); i++)
        {
          if (i != 0)
            buffer.append(",");
          buffer.append(((ITableField) thisFields.get(i)).getName());
        }
        buffer.append(")");
        return buffer.toString();
      }
    }

    /**
     * @author Andreas
     *
     * Iterator which iterates over all table definitions belonging to a given datasource.
     */
    private class SIndexIter implements Iterator
    {
      private Iterator keyIter;
      private ISchemaKeyDefinition next;
      
      private SIndexIter()
      {
        this.keyIter = STabDef.this.tableDef.getKeys();
        iterate();
      }
      
      private void iterate()
      {
        while (this.keyIter.hasNext())
        {
          AbstractKey key = (AbstractKey) this.keyIter.next();
          
          // table definition belongs to the desired datasource?
          if (key.getType().isIndex())
          {
            this.next = key;
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
    
    private class SRelDef implements ISchemaRelationDefinition
    {
      private final ITableDefinition primaryTableDefinition;
      private final IKey foreignKey;

      /**
       * @param primaryTableDefinition
       * @param foreignKey
       */
      public SRelDef(ITableDefinition primaryTableDefinition, IKey foreignKey)
      {
        this.primaryTableDefinition = primaryTableDefinition;
        this.foreignKey = foreignKey;
      }

      /*
       * (non-Javadoc)
       * 
       * @see de.tif.jacob.core.schema.ISchemaRelationDefinition#getSchemaForeignKeyName()
       */
      public String getSchemaForeignKeyName()
      {
        return this.foreignKey.getDBName();
      }

      /* (non-Javadoc)
       * @see de.tif.jacob.core.schema.ISchemaRelationDefinition#getSchemaTableName()
       */
      public String getSchemaTableName()
      {
        return STabDef.this.tableDef.getDBName();
      }
      
      /*
       * (non-Javadoc)
       * 
       * @see de.tif.jacob.core.schema.ISchemaRelationDefinition#getSchemaPrimaryTableName()
       */
      public String getSchemaPrimaryTableName()
      {
        return this.primaryTableDefinition.getDBName();
      }

      /*
       * (non-Javadoc)
       * 
       * @see de.tif.jacob.core.schema.ISchemaRelationDefinition#getSchemaForeignColumnNames()
       */
      public Iterator getSchemaForeignColumnNames()
      {
        return new SForeignColIter();
      }

      /**
       * @author Andreas
       * 
       * This iterator simply iterates over all key fields and returns field's db name.
       */
      private class SForeignColIter implements Iterator
      {
        private Iterator tableFieldIter;

        private SForeignColIter()
        {
          this.tableFieldIter = SRelDef.this.foreignKey.getTableFields().iterator();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext()
        {
          return this.tableFieldIter.hasNext();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Iterator#next()
         */
        public Object next()
        {
          ITableField tableField = (ITableField) this.tableFieldIter.next();
          return tableField.getDBName();
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
  }
}
