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
 * Created on 25.01.2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data.impl.sql.reconfigure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.impl.schema.Schema;
import de.tif.jacob.core.data.impl.schema.Table;
import de.tif.jacob.core.data.impl.schema.TableColumn;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.schema.ISchemaColumnDefinition;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;
import de.tif.jacob.core.schema.ISchemaRelationDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 * 
 * Default reconfigure implementation of SQL data sources.
 * <p>
 * Concrete SQL data sources should extend this class and overwrite default
 * behaviour where needed.
 */
public class Reconfigure
{
  public static final String RCS_ID = "$Id: Reconfigure.java,v 1.3 2008/12/23 15:21:32 ibissw Exp $";
  public static final String RCS_REV = "$Revision: 1.3 $";

  protected static final Log logger = LogFactory.getLog(Reconfigure.class);
  
  protected final SQLDataSource dataSource;
  
  private final Set tablesToRecreate = new HashSet();
    
  protected Reconfigure(SQLDataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  /**
   * Compares 2 key definitions.
   * <p>
   * Note: The name of the keys is not considered at all!
   * 
   * @param currentKeyDefinition
   *          the first key
   * @param desiredKeyDefinition
   *          the second key
   * @return <code>true</code> if identical, otherwise <code>false</code>
   */
  private boolean equals(ISchemaKeyDefinition currentKeyDefinition, ISchemaKeyDefinition desiredKeyDefinition)
  {
//    if (!currentKeyDefinition.getDBName().equalsIgnoreCase(desiredKeyDefinition.getDBName()))
//      return false;
    
    boolean desiredUnique = desiredKeyDefinition.isUnique()
        && (desiredKeyDefinition.hasRequiredColumns() || dataSource.supportsMultipleNullsForUniqueIndices());
    if (currentKeyDefinition.isUnique() ^ desiredUnique)
      return false;
    
    Iterator iter1 = currentKeyDefinition.getSchemaColumnNames();
    Iterator iter2 = desiredKeyDefinition.getSchemaColumnNames();
    while (iter1.hasNext() && iter2.hasNext())
    {
      String columnName1 = (String) iter1.next();
      String columnName2 = (String) iter2.next();
      if (!columnName1.equalsIgnoreCase(columnName2))
        return false; 
    }
    
    if (iter1.hasNext() || iter2.hasNext())
      return false;
    
    return true;
  }
  
  private static String getKey(ISchemaKeyDefinition keyDefinition)
  {
    StringBuffer buffer = new StringBuffer();
    Iterator iter = keyDefinition.getSchemaColumnNames();
    for (int i = 0; iter.hasNext(); i++)
    {
      if (i > 0)
        buffer.append(".");
      buffer.append(((String) iter.next()).toLowerCase());
    }
    return buffer.toString();
  }
  
  private static String getKey(ISchemaRelationDefinition relationDefinition)
  {
    StringBuffer buffer = new StringBuffer();
    Iterator iter = relationDefinition.getSchemaForeignColumnNames();
    for (int i = 0; iter.hasNext(); i++)
    {
      if (i > 0)
        buffer.append(".");
      buffer.append(((String) iter.next()).toLowerCase());
    }
    return buffer.toString();
  }
  
  protected CreateTableCommand newCreateTableCommand(ISchemaTableDefinition tableDefinition)
  {
    return new CreateTableCommand(tableDefinition);
  }
  
  protected AlterTableCommand newAlterTableCommand(ISchemaTableDefinition tableDefinition, List alterTableCommands)
  {
    return new AlterTableCommand(tableDefinition, alterTableCommands);
  }
  
  protected DropTableCommand newDropTableCommand(ISchemaTableDefinition tableDefinition)
  {
    return new DropTableCommand(tableDefinition);
  }
  
  /**
   * @param currentTable
   * @return
   */
  protected DropColumnCommand newDropColumnCommand(ISchemaColumnDefinition currentColumn)
  {
    return new DropColumnCommand(currentColumn);
  }

  /**
   * @param desiredColumn
   * @param currentColumn
   * @param modifyType
   * @param modifyDefault
   * @param modifyRequired
   * @param modifyAutoGenerated 
   * @return
   */
  protected ModifyColumnCommand newModifyColumnCommand(ISchemaColumnDefinition desiredColumn, ISchemaColumnDefinition currentColumn, boolean modifyType, boolean modifyDefault, boolean modifyRequired, boolean modifyAutoGenerated)
  {
    return new ModifyColumnCommand(desiredColumn, currentColumn, modifyType, modifyDefault, modifyRequired, modifyAutoGenerated);
  }

  /**
   * @param desiredColumn
   * @return
   */
  protected AddColumnCommand newAddColumnCommand(ISchemaColumnDefinition desiredColumn)
  {
    return new AddColumnCommand(desiredColumn);
  }

  /**
   * @param desiredRelation
   * @return
   */
  protected CreateRelationCommand newCreateRelationCommand(ISchemaRelationDefinition desiredRelation, ISchemaTableDefinition primaryTable)
  {
    return new CreateRelationCommand(desiredRelation, primaryTable);
  }

  /**
   * @param currentRelation
   * @return
   */
  protected DropRelationCommand newDropRelationCommand(ISchemaRelationDefinition currentRelation)
  {
    return new DropRelationCommand(currentRelation);
  }

  /**
   * @param desiredIndex
   * @return
   */
  protected CreateIndexCommand newCreateIndexCommand(ISchemaKeyDefinition desiredIndex)
  {
    return new CreateIndexCommand(desiredIndex);
  }

  /**
   * @param currentIndex
   * @return
   */
  protected DropIndexCommand newDropIndexCommand(ISchemaKeyDefinition currentIndex)
  {
    return new DropIndexCommand(currentIndex);
  }

  /**
   * @param currentPrimaryKeyDefinition
   * @return
   */
  protected DropPrimaryKeyCommand newDropPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition)
  {
    return new DropPrimaryKeyCommand(currentPrimaryKeyDefinition);
  }

  /**
   * @param desiredPrimaryKeyDefinition
   * @return
   */
  protected RenewPrimaryKeyCommand newRenewPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition, ISchemaKeyDefinition desiredPrimaryKeyDefinition)
  {
    return new RenewPrimaryKeyCommand(currentPrimaryKeyDefinition, desiredPrimaryKeyDefinition);
  }
  
  protected boolean modifyColumnType(ISchemaColumnDefinition currentColumn, ISchemaColumnDefinition desiredColumn)
  {
    String desiredType = dataSource.getSqlColumnType(
        desiredColumn.getSQLType(dataSource), 
        desiredColumn.getSQLSize(dataSource), 
        desiredColumn.getSQLDecimalDigits(dataSource));
    String currentType = dataSource.getSqlColumnType(
        currentColumn.getSQLType(dataSource), 
        currentColumn.getSQLSize(dataSource), 
        currentColumn.getSQLDecimalDigits(dataSource));
    return !desiredType.equals(currentType);
  }

  protected boolean modifyColumnDefault(ISchemaColumnDefinition currentColumn, ISchemaColumnDefinition desiredColumn)
  {
    boolean result = !StringUtil.saveEquals(desiredColumn.getDBDefaultValue(dataSource), currentColumn.getDBDefaultValue(dataSource));
    if (result && logger.isDebugEnabled())
      System.out.println("modifyColumnDefault(" + currentColumn.getDBDefaultValue(dataSource) + "," + desiredColumn.getDBDefaultValue(dataSource) + ")");
    return result;
  }

  protected boolean modifyColumnRequired(ISchemaColumnDefinition currentColumn, ISchemaColumnDefinition desiredColumn)
  {
    return desiredColumn.isRequired() ^ currentColumn.isRequired(); 
  }
  
  protected boolean modifyColumnAutoGenerated(ISchemaColumnDefinition currentColumn, ISchemaColumnDefinition desiredColumn)
  {
    return desiredColumn.isDBAutoGenerated(dataSource) ^ currentColumn.isDBAutoGenerated(dataSource); 
  }
  
  protected boolean dropIndex(ISchemaKeyDefinition currentIndex, ISchemaTableDefinition currentTableDefinition, ISchemaTableDefinition desiredTableDefinition)
  {
    // by default: always true
    return true;
  }

  private List reconfigureTables(ISchemaTableDefinition currentTableDefinition, ISchemaTableDefinition desiredTableDefinition, boolean destructive)
  {
    // plausibility check
    if (!currentTableDefinition.getDBName().equalsIgnoreCase(desiredTableDefinition.getDBName()))
      throw new RuntimeException("Tables must have the same name");
    
    List tableCommands = new ArrayList();
    
    // ------------------------------
    // Compare columns
    // ------------------------------
    
    //
    // create maps to compare columns
    Map currentColumns = new HashMap();
    Iterator iter = currentTableDefinition.getSchemaColumnDefinitions();
    while (iter.hasNext())
    {
      ISchemaColumnDefinition columnDef = (ISchemaColumnDefinition) iter.next();
      currentColumns.put(columnDef.getDBName().toLowerCase(), columnDef);
    }
    Map desiredColumns = new HashMap();
    iter = desiredTableDefinition.getSchemaColumnDefinitions();
    while (iter.hasNext())
    {
      ISchemaColumnDefinition columnDef = (ISchemaColumnDefinition) iter.next();
      if (desiredColumns.put(columnDef.getDBName().toLowerCase(), columnDef) != null )
      {
        throw new RuntimeException("Table definition '" + desiredTableDefinition.getDBName() + "' contains ambiguous '" + columnDef.getDBName() + "' columns!");
      }
    }
    
    //
    // check for new columns to create and columns to modify
    iter = desiredColumns.values().iterator();
    while (iter.hasNext())
    {
      ISchemaColumnDefinition desiredColumn = (ISchemaColumnDefinition) iter.next();
      ISchemaColumnDefinition currentColumn = (ISchemaColumnDefinition) currentColumns.get(desiredColumn.getDBName().toLowerCase());

      if (currentColumn == null)
      {
        tableCommands.add(newAddColumnCommand(desiredColumn));
      }
      else
      {
        boolean modifyType = modifyColumnType(currentColumn, desiredColumn);
        boolean modifyDefault = modifyColumnDefault(currentColumn, desiredColumn);
        boolean modifyRequired = modifyColumnRequired(currentColumn, desiredColumn); 
        boolean modifyAutoGenerated = modifyColumnAutoGenerated(currentColumn, desiredColumn); 
        
        if (modifyType || modifyDefault || modifyRequired || modifyAutoGenerated)
        {
          tableCommands.add(newModifyColumnCommand(desiredColumn, currentColumn, modifyType, modifyDefault, modifyRequired, modifyAutoGenerated));
        }
        
        // just for Quintus!
        if (dataSource.isQuintusAdjustment())
        {
          if (desiredColumn.isEnumeration())
          {
            List statements = new ArrayList();

            boolean diffsFound = true; 
            if (currentColumn.isEnumeration())
            {
              diffsFound = ModifyEnumCommand.checkQuintusEnumLabels(dataSource, desiredColumn, currentColumn, statements);
            }

            if (diffsFound)
            {
              AddColumnCommand.addQuintusEnumerationStatements(dataSource, statements, desiredColumn);
              tableCommands.add(new ModifyEnumCommand(desiredColumn, statements));
            }
          }
        }
      }
    }

    //
    // check for columns to drop
    if (destructive)
    {
      iter = currentColumns.values().iterator();
      while (iter.hasNext())
      {
        ISchemaColumnDefinition currentColumn = (ISchemaColumnDefinition) iter.next();
        if (!desiredColumns.containsKey(currentColumn.getDBName().toLowerCase()))
        {
          tableCommands.add(newDropColumnCommand(currentColumn));
        }
      }
    }

    // ------------------------------
    // Compare primary key
    // ------------------------------
    ISchemaKeyDefinition currentPrimaryKeyDefinition = currentTableDefinition.getSchemaPrimaryKeyDefinition();
    ISchemaKeyDefinition desiredPrimaryKeyDefinition = desiredTableDefinition.getSchemaPrimaryKeyDefinition();
    if (currentPrimaryKeyDefinition == null)
    {
      if (desiredPrimaryKeyDefinition != null)
      {
        tableCommands.add(newRenewPrimaryKeyCommand(null, desiredPrimaryKeyDefinition));
      }
    }
    else
    {
      if (desiredPrimaryKeyDefinition == null)
      {
        tableCommands.add(newDropPrimaryKeyCommand(currentPrimaryKeyDefinition));
      }
      else if (!equals(currentPrimaryKeyDefinition, desiredPrimaryKeyDefinition))
      {
        tableCommands.add(newRenewPrimaryKeyCommand(currentPrimaryKeyDefinition, desiredPrimaryKeyDefinition));
      }
    }
    
    // ------------------------------
    // Compare indices
    // ------------------------------
    
    // create maps to compare indices
    //
    // Attention: Do not use name as key, since two index definition should
    // be treated as equal, if there columns and the unique flag are the same
    // even if the name is different.
    //
    List indicesToDrop = new ArrayList();
    List indicesToCreate = new ArrayList();
    Map currentIndices = new HashMap();
    iter = currentTableDefinition.getSchemaIndexDefinitions();
    while (iter.hasNext())
    {
      ISchemaKeyDefinition indexDef = (ISchemaKeyDefinition) iter.next();
      Object previous = currentIndices.put(getKey(indexDef), indexDef);
      if (previous != null)
      {
        // handle redundant indices (same columns but different names)
        indicesToDrop.add(previous);
      }
    }
    Map desiredIndices = new HashMap();
    
    // 1. check for new indices to create and indices to modify
    //
    iter = desiredTableDefinition.getSchemaIndexDefinitions();
    while (iter.hasNext())
    {
      ISchemaKeyDefinition desiredIndex = (ISchemaKeyDefinition) iter.next();
      String desiredIndexKey = getKey(desiredIndex);
      desiredIndices.put(desiredIndexKey, desiredIndex);

      ISchemaKeyDefinition currentIndex = (ISchemaKeyDefinition) currentIndices.get(desiredIndexKey);
      if (currentIndex == null)
      {
        indicesToCreate.add(desiredIndex);
      }
      else
      {
        if (!equals(currentIndex, desiredIndex))
        {
          // could only happen, if the one is unique and the other not
          
          // drop immediately
          // Note: Indices must be dropped before droping or modifying columns
          tableCommands.add(0, newDropIndexCommand(currentIndex));
          
          indicesToCreate.add(desiredIndex);
        }
      }
    }

    // 2. check for indices to drop
    //
    if (destructive)
    {
      // first, delete redundant indices
      //
      iter = indicesToDrop.iterator();
      while (iter.hasNext())
      {
        ISchemaKeyDefinition currentIndex = (ISchemaKeyDefinition) iter.next();
        
        // Note: Indices must be dropped before droping or modifying columns
        tableCommands.add(0, newDropIndexCommand(currentIndex));
      }
      
      // second, delete indices which are not needed anymore
      //
      iter = currentIndices.keySet().iterator();
      while (iter.hasNext())
      {
        String currentIndexKey = (String) iter.next();
        ISchemaKeyDefinition currentIndex = (ISchemaKeyDefinition) currentIndices.get(currentIndexKey);
        if (!desiredIndices.containsKey(currentIndexKey))
        {
          if (dropIndex(currentIndex, currentTableDefinition, desiredTableDefinition))
          {
            // Note: Indices must be dropped before droping or modifying columns
            tableCommands.add(0, newDropIndexCommand(currentIndex));
          }
        }
      }
    }

    // 3. and create the indices
    //
    iter = indicesToCreate.iterator();
    while (iter.hasNext())
    {
      ISchemaKeyDefinition desiredIndex = (ISchemaKeyDefinition) iter.next();
      tableCommands.add(newCreateIndexCommand(desiredIndex));
    }

    // ------------------------
    // just for Quintus!
    // ------------------------
    //
    if (dataSource.isQuintusAdjustment())
    {
      // for this table an entry in qw_keys is still missing?
      //
      if (null == currentTableDefinition.getProperty(Table.QW_KEYVALUE_PROPERTY))
      {
        tableCommands.add(new AddTableKeyCommand(desiredTableDefinition));
      }
    }
    
    // ------------------------------
    // Finished
    // ------------------------------
    
    return tableCommands;
  }
  
  private void reconfigureTableRelations(CommandList commandList, Map desiredTables, ISchemaTableDefinition currentTableDefinition,
      ISchemaTableDefinition desiredTableDefinition, boolean destructive)
  {
    // If no foreign key constraints are supported, skip relation reconfigurement
    //
    if (!this.dataSource.supportsForeignKeyConstraints())
    {
      return;
    }
    
    // ------------------------------
    // Compare relations
    // ------------------------------

    //
    // create maps to compare relations
    //
    // Attention: Do not use name as key, since two relation definition should
    // be treated as equal, if there foreign columns and the primary table name
    // are the same even if the name of the relation is different.
    List relationsToDrop = new ArrayList();
    Map currentRelations = new HashMap();
    if (currentTableDefinition != null)
    {
      Iterator iter = currentTableDefinition.getSchemaRelationDefinitions();
      while (iter.hasNext())
      {
        ISchemaRelationDefinition relationDef = (ISchemaRelationDefinition) iter.next();
        Object previous = currentRelations.put(getKey(relationDef), relationDef);
        if (previous != null)
        {
          // handle redundant relations (same columns but different names)
          relationsToDrop.add(previous);
        }
      }
    }
    Map desiredRelations = new HashMap();

    //
    // check for new relations to create and relations to modify
    Iterator iter = desiredTableDefinition.getSchemaRelationDefinitions();
    while (iter.hasNext())
    {
      ISchemaRelationDefinition desiredRelation = (ISchemaRelationDefinition) iter.next();
      String desiredRelationKey = getKey(desiredRelation);
      desiredRelations.put(desiredRelationKey, desiredRelation);

      ISchemaRelationDefinition currentRelation = (ISchemaRelationDefinition) currentRelations.get(desiredRelationKey);
      if (currentRelation == null)
      {
        ISchemaTableDefinition primaryTable = (ISchemaTableDefinition) desiredTables.get(desiredRelation.getSchemaPrimaryTableName().toLowerCase());
        commandList.add(newCreateRelationCommand(desiredRelation, primaryTable));
      }
      else
      {
        // treat relations as equal, if they point to the same primary table
        if (!desiredRelation.getSchemaPrimaryTableName().equalsIgnoreCase(currentRelation.getSchemaPrimaryTableName()))
        {
          commandList.add(newDropRelationCommand(currentRelation));

          ISchemaTableDefinition primaryTable = (ISchemaTableDefinition) desiredTables.get(desiredRelation.getSchemaPrimaryTableName().toLowerCase());
          commandList.add(newCreateRelationCommand(desiredRelation, primaryTable));
        }
      }
    }

    //
    // check for relations to drop
    if (destructive)
    {
      // first, delete redundant relations
      //
      iter = relationsToDrop.iterator();
      while (iter.hasNext())
      {
        ISchemaRelationDefinition currentRelation = (ISchemaRelationDefinition) iter.next();
        commandList.add(newDropRelationCommand(currentRelation));
      }
      
      // second, delete relations which are not needed anymore
      //
      iter = currentRelations.keySet().iterator();
      while (iter.hasNext())
      {
        String currentRelationKey = (String) iter.next();
        ISchemaRelationDefinition currentRelation = (ISchemaRelationDefinition) currentRelations.get(currentRelationKey);
        if (!desiredRelations.containsKey(currentRelationKey))
        {
          commandList.add(newDropRelationCommand(currentRelation));
        }
      }
    }
  }
  
  /**
   * Registers a table which will be in any case dropped before potential
   * recreation.
   * <p>
   * Note: This functionality could be used to perform a complete restruction in
   * case lost data is not a problem.
   * 
   * @param tableName
   *          the name of the table to recreate
   */
  public final void addTableToRecreate(String tableName)
  {
    tablesToRecreate.add(tableName.toLowerCase());
  }
  
  private void addDropTableCommands(CommandList commands, ISchemaTableDefinition currentTable)
  {
    commands.add(newDropTableCommand(currentTable));
    
    //
    // drop all relations also to ensure that a set of tables which are linked
    // under each other could be dropped without any particular order.
    Iterator relIter = currentTable.getSchemaRelationDefinitions();
    while (relIter.hasNext())
    {
      commands.add(newDropRelationCommand((ISchemaRelationDefinition) relIter.next()));
    }
  }
  
  /**
   * @param desiredSchema
   * @param currentSchema
   * @param destructive
   * @return
   * @throws Exception
   */
  public final CommandList reconfigure(ISchemaDefinition desiredSchema, ISchemaDefinition currentSchema, boolean destructive) throws Exception
  {
    CommandList commands = new CommandList();

    //
    // create table maps to compare tables
    Map currentTables = new HashMap();
    Iterator iter = currentSchema.getSchemaTableDefinitions();
    while (iter.hasNext())
    {
      ISchemaTableDefinition tableDef = (ISchemaTableDefinition) iter.next();
      currentTables.put(tableDef.getDBName().toLowerCase(), tableDef);
    }
    Map desiredTables = new HashMap();
    iter = desiredSchema.getSchemaTableDefinitions();
    while (iter.hasNext())
    {
      ISchemaTableDefinition tableDef = (ISchemaTableDefinition) iter.next();
      if (desiredTables.put(tableDef.getDBName().toLowerCase(), tableDef) != null )
      {
        // normally this should be handled by the designer :-)
        throw new RuntimeException("Table definition '"+tableDef.getDBName()+"' is not unique!");
      }
    }

    //
    // check for new tables to create and fields to add
    iter = desiredTables.values().iterator();
    while (iter.hasNext())
    {
      ISchemaTableDefinition desiredTable = (ISchemaTableDefinition) iter.next();
      ISchemaTableDefinition currentTable = (ISchemaTableDefinition) currentTables.get(desiredTable.getDBName().toLowerCase());
      
      // current table does not exist or should be recreated?
      if (currentTable == null || tablesToRecreate.contains(currentTable.getDBName().toLowerCase()))
      {
        if (currentTable != null)
        {
          logger.info("Recreating table " + currentTable.getDBName());
          addDropTableCommands(commands, currentTable);
        }
        
        commands.add(newCreateTableCommand(desiredTable));
      }
      else
      {
        // compare both table definitions
        List alterTableCommands = reconfigureTables(currentTable, desiredTable, destructive);
        
        // there is only something to do, if command list is not empty
        if (!alterTableCommands.isEmpty())
        {
          // only alter table if not excluded
          if (!this.dataSource.getAdjustment().excludeFromReconfigureAlterTable(currentTable.getDBName()))
          {
            commands.add(newAlterTableCommand(desiredTable, alterTableCommands));
          }
        }
      }
      
      // compare table relations (attention: currentTable could be null!)
      // Note: consider tables which are excluded from altering (tables containing the foreign key)
      //
      if (!this.dataSource.getAdjustment().excludeFromReconfigureAlterTable(desiredTable.getDBName()))
      {
        reconfigureTableRelations(commands, desiredTables, currentTable, desiredTable, destructive);
      }
    }

    //
    // check for tables to drop
    if (destructive)
    {
      iter = currentTables.values().iterator();
      while (iter.hasNext())
      {
        ISchemaTableDefinition currentTable = (ISchemaTableDefinition) iter.next();
        if (!desiredTables.containsKey(currentTable.getDBName().toLowerCase()))
        {
          // only drop table if not excluded
          if (!this.dataSource.getAdjustment().excludeFromReconfigureDropTable(currentTable.getDBName()))
          {
            addDropTableCommands(commands, currentTable);
          }
        }
      }
    }

    boolean debug = logger.isDebugEnabled();
    if (debug)
    {
      System.out.println("************* Command list START ***********");
      commands.print(System.out);
      System.out.println("************* Command list END *************");

      System.out.println("----------- SQL Statements START -----------");
    }
    for (int i = 0; i < commands.size(); i++)
    {
      if (i > 0 && debug)
        System.out.println("---------------------------------------");
      List statements = commands.get(i).getSQLStatements(dataSource);
      for (int j = 0; j < statements.size(); j++)
      {
        System.out.println(statements.get(j));
      }
    }
    if (debug)
      System.out.println("----------- SQL Statements END -----------");
    
    return commands;
  }
  
  
  /**
   * Returns the database catalog name used to extract meta data from this data
   * source.
   * 
   * @return catalog name or <code>null</code>, if catalog name should not be
   *         used for filtering of meta data.
   */
  public String getMetaCatalogName()
  {
    try
    {
  		Connection connection = this.dataSource.getConnection();
  		try
      {
        return connection.getCatalog();
      }
      finally
      {
        connection.close();
      }
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * Returns the database schema pattern used to extract meta data from this
   * data source. By default <code>null</code> is returned, i.e. meta data is
   * filtered by catalog name only.
   * 
   * @return meta schema pattern or <code>null</code>
   */
  protected String getMetaSchemaPattern()
  {
    // By default: Set to null since we do the filtering by means of catalog name only
    return null;
  }
  
  protected Table fetchTable(ResultSet rs) throws Exception
  {
    String table_cat = rs.getString("TABLE_CAT");
    String table_schem = rs.getString("TABLE_SCHEM");
    String table_name = rs.getString("TABLE_NAME");
    String table_type = rs.getString("TABLE_TYPE");
    String table_remarks = null; //rs.getString("REMARKS");
    String type_cat = null; //rs.getString("TYPE_CAT");
    String type_schem = null;//rs.getString("TYPE_SCHEM");
    String type_name = null;//rs.getString("TYPE_NAME");
    String self_ref = null;//rs.getString("SELF_REFERENCING_COL_NAME");
    String ref_gen = null;//rs.getString("REF_GENERATION");
    
    if (logger.isDebugEnabled())
      System.out.println(table_cat+"."+table_schem+"."+table_name+":"+table_type+","+table_remarks+","+type_cat+","+type_schem+","+type_name+","+self_ref+","+ref_gen);
    
    if ("TABLE".equals(table_type))
      return new Table(table_name, Table.USER_TABLE);
    else if ("VIEW".equals(table_type))
      return new Table(table_name, Table.VIEW);
    else if ("SYSTEM TABLE".equals(table_type))
      return new Table(table_name, Table.SYSTEM_TABLE);
    else
      return new Table(table_name, Table.OTHER_TYPE);        
  }
  
  protected TableColumn fetchTableColumn(ResultSet rs) throws Exception
  {
    String table_name = rs.getString("TABLE_NAME");
    String column_name = rs.getString("COLUMN_NAME");
    int data_type = rs.getInt("DATA_TYPE");
    String type_name = rs.getString("TYPE_NAME");
    int column_size = rs.getInt("COLUMN_SIZE");
    int decimal_digits = rs.getInt("DECIMAL_DIGITS");
    String column_default = rs.getString("COLUMN_DEF");
    boolean is_nullable = "YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"));

    if (logger.isDebugEnabled())
      System.out.println(table_name + "." + column_name + ":" + data_type + "," + type_name + "," + column_size + "." + decimal_digits + "," + column_default
          + "," + is_nullable);
    return fetchTableColumn(table_name, column_name, data_type, type_name, column_size, decimal_digits, column_default, is_nullable);
  }
  
  /**
   * @param table_name
   * @param column_name
   * @param data_type
   * @param type_name
   * @param column_size
   * @param decimal_digits
   * @param column_default
   * @param is_nullable
   * @return
   */
  protected TableColumn fetchTableColumn(String table_name, String column_name, int data_type, String type_name, int column_size, int decimal_digits, String column_default, boolean is_nullable)
  {
    // default implementation : just pass thru
    return new TableColumn(table_name, column_name, data_type, column_size, decimal_digits, column_default, is_nullable);
  }
  
  protected void fetchIndexInfo(Connection connection, String catalogName, String schemaPattern, Table table) throws Exception
  {
    // The 5. argument (approximate) must be set to true. Because otherwise
    // detailed (not needed) index
    // information is requested, which slowes down reading schema info
    // enormeously on a database with huge tables!!!
    ResultSet rs = connection.getMetaData().getIndexInfo(catalogName, schemaPattern, table.getDBName(), false, true);
    try
    {
      while (rs.next())
      {
        addTableIndexPart(table, rs);
      }
    }
    finally
    {
      rs.close();
    }
  }
  
  protected final void addTableIndexPart(Table table, ResultSet rs) throws Exception
  {
    // String TABLE_NAME = rs.getString("TABLE_NAME");
    boolean unique = !rs.getBoolean("NON_UNIQUE");
    String indexName = rs.getString("INDEX_NAME");
    short ordinalPosition = rs.getShort("ORDINAL_POSITION");
    String columnName = rs.getString("COLUMN_NAME");
    // String ASC_OR_DESC = rs.getString("ASC_OR_DESC");
    //int CARDINALITY = rs.getInt("CARDINALITY");
    //String filterCondition = rs.getString("FILTER_CONDITION");

    // check whether any indices existing
    if (null == indexName)
      return;

    if (logger.isDebugEnabled())
      System.out.println(table.getDBName() + "." + indexName + ":" + unique + "," + ordinalPosition + "," + columnName);

    // skip primary keys
    ISchemaKeyDefinition primaryKey = table.getSchemaPrimaryKeyDefinition();
    if (primaryKey != null && primaryKey.getDBName().equals(indexName))
      return;

    addTableIndexPart(table, indexName, unique, ordinalPosition, columnName);
  }
  
  /**
   * Might be overwritten to skip adding system indices.
   * 
   * @param table
   * @param indexName
   * @param unique
   * @param ordinalPosition
   * @param columnName
   * @throws Exception
   */
  protected void addTableIndexPart(Table table, String indexName, boolean unique, short ordinalPosition, String columnName)
  {
    table.addIndexPart(indexName, unique, ordinalPosition, columnName);
  }
  
  private final static String QW_FIELDINFO = "qw_fieldinfo";
  
  private final static String QW_KEYS = "qw_keys";
  
  /**
   * @param connection
   * @param schema
   * @throws Exception
   */
  protected void fetchSchemaPreProcessing(Connection connection, Schema schema) throws Exception
  {
    // by default do nothing here
  }
  
  /**
   * @param connection
   * @param schema
   * @throws Exception
   */
  protected void fetchSchemaPostProcessing(Connection connection, Schema schema) throws Exception
  {
    // -------------------------------------------------------------
    // For Quintus databases fetch all enum values from qw_fieldinfo
    // -------------------------------------------------------------
    //
    if (this.dataSource.isQuintusAdjustment() && schema.hasTable(QW_FIELDINFO))
    {
      Statement statement = connection.createStatement();
      try
      {
        String query = "SELECT DISTINCT table_name, column_name, enum_value, enum_label FROM "+QW_FIELDINFO + // 
            " WHERE column_type=14 ORDER BY table_name, column_name, enum_value"; // filter for enum types only 

        ResultSet rs = statement.executeQuery(query);
        try
        {
          TableColumn currentTableColumn = null;
          List enumLabels = null;
          boolean inconsistent = false;
          
          while (rs.next())
          {
            String tableName = rs.getString(1);
            String columnName = rs.getString(2);
            int enumValue = rs.getInt(3);
            String enumLabel = rs.getString(4);
            
            if (schema.hasTable(tableName))
            {
              Table table = schema.getTable(tableName);
              if (table.hasTableColumn(columnName))
              {
                TableColumn tableColumn = table.getTableColumn(columnName);
                if (!tableColumn.equals(currentTableColumn))
                {
                  // set enum labels
                  if (currentTableColumn != null)
                    currentTableColumn.putProperty(TableColumn.ENUM_LABELS_PROPERTY, inconsistent ? TableColumn.INCONSISTENT_ENUM_LABELS : enumLabels);
                  
                  currentTableColumn = tableColumn;
                  enumLabels = new ArrayList();
                  inconsistent = false;
                }
                
                if (enumValue != enumLabels.size())
                {
                  inconsistent = true;
                  logger.warn("Missing label for enum value "+enumLabels.size()+" of column '"+table.getDBName()+"."+tableColumn.getDBName()+"'");
                }
                
                if (enumLabel == null)
                {
                  inconsistent = true;
                  logger.warn("Label for enum value "+enumLabels.size()+" of column '"+table.getDBName()+"."+tableColumn.getDBName()+"' is null");
                }
                
                enumLabels.add(enumLabel);
              }
            }

            // set enum labels
            if (currentTableColumn != null)
              currentTableColumn.putProperty(TableColumn.ENUM_LABELS_PROPERTY, inconsistent ? TableColumn.INCONSISTENT_ENUM_LABELS : enumLabels);
          }
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        statement.close();
      }
    }
    
    // -------------------------------------------------------------
    // For Quintus databases fetch key entries from qw_keys
    // -------------------------------------------------------------
    //
    if (this.dataSource.isQuintusAdjustment() && schema.hasTable(QW_KEYS))
    {
      Statement statement = connection.createStatement();
      try
      {
        String query = "SELECT DISTINCT tablename, keyvalue FROM " + QW_KEYS;

        ResultSet rs = statement.executeQuery(query);
        try
        {
          while (rs.next())
          {
            String tableName = rs.getString(1);
            int keyValue = rs.getInt(2);

            if (schema.hasTable(tableName))
            {
              Table table = schema.getTable(tableName);
              table.putProperty(Table.QW_KEYVALUE_PROPERTY, new Integer(keyValue));
            }
          }
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        statement.close();
      }
    }
  }
  
  public final ISchemaDefinition fetchSchemaInformation() throws Exception
  {
    String catalogName = getMetaCatalogName();
    String schemaPattern = getMetaSchemaPattern();

    Connection connection = dataSource.getConnection();

    // Muß für MS SQL gesetzt werden, da ansonsten beim Aufruf von
    // - connection.getMetaData().getTypeInfo()
    // - connection.getMetaData().getIndexInfo()
    // folgender Fehler auftritt:
    // Can't start a cloned connection while in manual transaction mode
    connection.setAutoCommit(true);

    boolean debug = logger.isDebugEnabled();
    try
    {
      Schema schema = new Schema();

      // execute pre processing actions to fetch additional data source specific data
      //
      fetchSchemaPreProcessing(connection, schema);

      // fetch all local type infos
      {
        if (debug)
          System.out.println("### TYPE-INFO ##########################");
        ResultSet rs = connection.getMetaData().getTypeInfo();
        try
        {
          while (rs.next())
          {
            String type_name = rs.getString("TYPE_NAME");
            int data_type = rs.getInt("DATA_TYPE");
            if (debug)
              System.out.println(data_type + ":" + type_name);
            schema.addType(data_type, type_name);
          }
        }
        finally
        {
          rs.close();
        }
      }

      // fetch all keywords
      if (debug)
      {
        System.out.println("### KEYWORDS ##########################");
        System.out.println(connection.getMetaData().getSQLKeywords());
      }

      // fetch all procedures
      {
        if (debug)
          System.out.println("### PROCEDURES ##########################");
        ResultSet rs = connection.getMetaData().getProcedures(catalogName, schemaPattern, "%");
        try
        {
          while (rs.next())
          {
            String proc_cat = rs.getString("PROCEDURE_CAT");
            String proc_schem = rs.getString("PROCEDURE_SCHEM");
            String proc_name = rs.getString("PROCEDURE_NAME");
            if (debug)
              System.out.println(proc_cat+"."+proc_schem+"."+proc_name);
            schema.addProcedure(proc_name);
          }
        }
        finally
        {
          rs.close();
        }
      }

      // fetch tables of schema
      {
        if (debug)
          System.out.println("### TABLE ##########################");
        ResultSet rs = connection.getMetaData().getTables(catalogName, schemaPattern, "%", null);
        try
        {
          while (rs.next())
          {
            Table table = fetchTable(rs);
            if (table != null)
              schema.add(table);
          }
        }
        finally
        {
          rs.close();
        }
      }

      // fetch table columns
      if (debug)
        System.out.println("### COLUMN ##########################");
      {
        ResultSet rs = connection.getMetaData().getColumns(catalogName, schemaPattern, "%", null);
        try
        {
          while (rs.next())
          {
            TableColumn column = fetchTableColumn(rs);
            if (column != null)
            {
              String table_name = rs.getString("TABLE_NAME");
              schema.getTable(table_name).add(column);
            }
          }
        }
        finally
        {
          rs.close();
        }
      }

      // fetch table primary keys
      if (debug)
        System.out.println("### PRIMARY ##########################");
      Iterator iter = schema.getTableAndViews();
      while (iter.hasNext())
      {
        Table table = (Table) iter.next();

        ResultSet rs = connection.getMetaData().getPrimaryKeys(catalogName, schemaPattern, table.getDBName());
        try
        {
          while (rs.next())
          {
            String table_name = rs.getString("TABLE_NAME");
            String columnName = rs.getString("COLUMN_NAME");
            short ordinalPosition = rs.getShort("KEY_SEQ");
            String pk_name = rs.getString("PK_NAME");

            // check whether any primary key existing
            if (null == pk_name)
              continue;

            if (debug)
              System.out.println(ordinalPosition + ":" + table_name + "." + columnName + ":" + pk_name);
            schema.getTable(table_name).addPrimaryKeyPart(pk_name, ordinalPosition, columnName);
          }
        }
        finally
        {
          rs.close();
        }
      }

      // fetch table indices and keys
      if (debug)
        System.out.println("### INDEX ##########################");
      iter = schema.getTables();
      while (iter.hasNext())
      {
        Table table = (Table) iter.next();
        fetchIndexInfo(connection, catalogName, schemaPattern, table);
      }

      // fetch table foreign keys
      if (debug)
        System.out.println("### FOREIGN ##########################");
      iter = schema.getTables();
      while (iter.hasNext())
      {
        Table table = (Table) iter.next();

        ResultSet rs = connection.getMetaData().getExportedKeys(catalogName, schemaPattern, table.getDBName());
        try
        {
          while (rs.next())
          {
            String pktable_name = rs.getString("PKTABLE_NAME");
            String pkcolumn_name = rs.getString("PKCOLUMN_NAME");
            String fktable_name = rs.getString("FKTABLE_NAME");
            String fkcolumn_name = rs.getString("FKCOLUMN_NAME");
            short ordinalPosition = rs.getShort("KEY_SEQ");
            String update_rule = rs.getString("UPDATE_RULE");
            String delete_rule = rs.getString("DELETE_RULE");
            String fk_name = rs.getString("FK_NAME");
            String pk_name = rs.getString("PK_NAME");
            String defer = rs.getString("DEFERRABILITY");

            if (debug)
              System.out.println(ordinalPosition + ":" + fktable_name + "." + fkcolumn_name + "->" + pktable_name + "." + pkcolumn_name + ":" + fk_name + "," + pk_name + "," + update_rule + "," + delete_rule + "," + defer);
            schema.getTable(fktable_name).addForeignKeyPart(fk_name, ordinalPosition, fkcolumn_name, pk_name, pktable_name, pkcolumn_name);
          }
        }
        finally
        {
          rs.close();
        }
      }
      
      // execute post processing actions to fetch additional data source specific data
      //
      fetchSchemaPostProcessing(connection, schema);

      return schema;
    }
    finally
    {
      try
      {
        //        connection.clearWarnings();
        connection.setAutoCommit(false);
      }
      catch (Exception ex)
      {
        // ignore
      }
      connection.close();
    }
  }
}
